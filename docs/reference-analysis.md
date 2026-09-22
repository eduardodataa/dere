# Análise do Sender como referência para a DeRE

**Data da análise:** 18/09/2026  
**Escopo:** o Sender como referência funcional e técnica; `src/` e `frontend/` como estado atual da POC DeRE.  
**Decisão estrutural:** o Sender não será transformado em DeRE e nenhum tipo, enum, entidade ou pacote do Sender será usado como domínio da DeRE.

## 1. Sumário executivo

O Sender implementa um fluxo completo de importação de XML, criação de eventos, agrupamento em lotes, assinatura XML, envio HTTPS com certificado de cliente, consulta assíncrona, consulta de recibo e monitoramento por tarefas agendadas. A extensão de eventos é feita por `EventoFactory` registrado em um `Map` por tipo de evento, o que é uma boa ideia de registro por capacidade.

O principal aprendizado para a DeRE é separar a intenção de cada mecanismo da implementação concreta do Sender. A DeRE deve preservar contratos úteis — registro de handlers, XSD versionado, pipeline canônico, certificado como porta, histórico persistido e cliente governamental isolado — mas corrigir o acoplamento a enums do Sender, o builder central grande, o parsing XPath espalhado, o estado de transmissão em memória e a exposição de segredos.

Fluxo observado no Sender:

```text
arquivo ZIP/XML
  -> PacoteController
  -> PacoteService
  -> PacoteProcessor / PacoteBuilder
  -> extração e divisão em lotes de até 50 arquivos
  -> LoteAsyncService
  -> EventoBuilder
  -> EventoFactory por tipo de evento
  -> builder central ou factory específica
  -> validação XSD e validações de domínio
  -> criação do XML de lote
  -> assinatura XML
  -> TransmissaoManager / TransmissaoService
  -> cliente HTTP com mTLS
  -> protocolo e status inicial
  -> ScheduledTasks / ConsultaLoteService
  -> retorno por evento e recibos
  -> persistência e telas de monitoramento
```

## 2. Como o Sender funciona

### 2.1 Entrada e processamento do pacote

`PacoteController` expõe `PUT /api/v1/pacote/{code}` como multipart e converte o arquivo recebido em `FileInputDTO`. O parâmetro `{code}` é diretamente o enum de tipo de evento; portanto, a API já nasce acoplada ao catálogo de eventos do Sender. O controller também oferece busca paginada, download do arquivo, erros e exportação CSV.

`PacoteService.receive` delega o processamento a `PacoteProcessor` dentro de transação. `PacoteProcessor` usa `PacoteBuilder`, extrai o ZIP com `Zip.extractFiles`, aceita apenas arquivos com sufixo `.xml`, acumula caminhos e cria lotes de 50 arquivos através de `LoteAsyncService`. Ao final atualiza o pacote como sucesso, erro ou processamento parcial.

Características importantes:

- a resposta do upload é `202 Accepted`, mas a criação dos lotes é assíncrona;
- erros de arquivo são acumulados por mensagem;
- o pacote mantém o arquivo e os erros para consulta posterior;
- a separação de lotes ocorre antes da validação semântica de cada evento;
- o parâmetro `retificacao` atravessa serviço e lote, misturando uma preocupação de operação com a construção do evento.

### 2.2 Lote, evento e validação

`LoteAsyncService.create` é executado com `@Async("processamentoLoteExecutor")`. Ele cria o lote, chama `EventoBuilder.buildEventos`, gera o arquivo de envio, executa `EventoValidator`, persiste eventos e publica `LoteCriadoEvent` quando o lote fica pendente de envio.

`EventoBuilder` filtra diretórios, impõe novamente o limite de 50 e seleciona a factory no mapa de factories por tipo de evento. A configuração de factories monta esse mapa a partir do conjunto de beans Spring. As implementações encontradas cobrem pagamento de pessoa física, pagamento de pessoa jurídica e processo.

As factories de pagamento apenas delegam ao builder central de evento. A factory de processo faz parsing próprio, inclusive identificação do subtipo de processo.

O builder central concentra leitura DOM, XPath, campos comuns, retificação, fatos geradores, compactação do XML e validação XSD. Ele usa caminhos XPath construídos com o tipo do evento e trata namespace de forma frágil — há comentário explícito de que XPath não funciona com namespace. Este é o exemplo mais claro de responsabilidade excessiva a não reproduzir.

A validação de schema fica em um utilitário de XML e nos XSDs do Sender. `EventoValidator` e suas implementações adicionam regras como IDs repetidos, mais de um contribuinte e erro de schema.

### 2.3 Construção, assinatura e transmissão

O lote recebe um XML de envio criado por `EventoConverter`. `XmlSigner` usa Apache Santuario, assinatura RSA-SHA256, canonicalização sem comentários, transformação enveloped e `X509DataBuilder`. A chave privada é fornecida por `CertificateProvider`.

`TransmissaoManager` impede duas submissões simultâneas do mesmo UUID somente com `ConcurrentHashMap<String, Future<?>>`. A tarefa é removida do mapa no `finally`, portanto a coordenação desaparece quando o processo reinicia ou quando há mais de uma instância.

`TransmissaoService.transmit` carrega o lote pelo UUID, chama o cliente de envio, interpreta códigos de resposta (por exemplo 1, 7 e 99), salva protocolo/status e mantém o XML de retorno comprimido. A operação não cria um histórico explícito por tentativa, nem registra duração, status HTTP, correlação ou referência da requisição como dados de negócio.

O cliente HTTP usa `RestTemplate`, retry para erros 5xx e ausência de resposta, POST de `/recepcao/lotes`, GET de `/consulta/lotes/{protocolo}` e consulta de recibo por chave do evento. O retorno é desserializado diretamente para classes JAXB específicas do Sender.

### 2.4 Consulta assíncrona e recibo

`ConsultaLoteProcessamento` traduz o status retornado para `StatusLote`, persiste a atualização em transação nova e distribui os eventos recebidos por ID. Erros de um evento são logados individualmente.

`ScheduledTasks` executa a cada 45 segundos a inclusão de lotes pendentes no `ConsultaLoteManager` e consulta recibos pendentes em páginas. É um mecanismo funcional, mas não há no código observado lock distribuído, claim com lease, backoff por registro ou idempotência persistida para proteger múltiplas instâncias.

## 3. Componentes relevantes

| Componente | Responsabilidade observada | Uso como referência DeRE |
|---|---|---|
| `PacoteController` | Upload, consulta, download, erros e exportação | Adaptar para uma API de importação neutra por layout e formato |
| `PacoteService` | Fachada transacional da entrada | Reutilizar o conceito; separar casos de uso de upload, validação e consulta |
| `PacoteProcessor` | ZIP, filtragem de arquivos, divisão de lotes e status | Adaptar para pipeline de importação persistente e idempotente |
| `EventoBuilder` | Orquestra factory e limite de lote | Reutilizar apenas como ideia de orquestração; remover dependência do catálogo do Sender |
| configuração de factories | Registro de factories por tipo | Reutilizar conceito como registry de `DereLayoutHandler` |
| builder central de evento | Parsing, XPath, regra e criação da entidade | Não reproduzir como builder central |
| factories por tipo de evento | Adaptação por evento | Reutilizar conceito, com handlers isolados por layout |
| `LoteAsyncService` | Criação assíncrona, validação e publicação de evento | Adaptar para jobs persistidos/outbox |
| `TransmissaoService` | Envio e tradução do retorno | Reutilizar conceito, com histórico de tentativas e estados próprios |
| `TransmissaoManager` | Deduplicação em memória | Não reproduzir |
| cliente HTTP | Cliente governamental REST | Adaptar para porta de integração DeRE e contrato oficial próprio |
| `CertificateProvider` | Acesso a keystore, chave e certificado | Adaptar: não expor senha ao domínio nem guardar segredo em configuração plana |
| `ImportacaoCertificadoCreator` | Valida upload, troca certificado ativo e atualiza clientes | Reutilizar intenção; separar metadata, blob criptografado e rotação |
| `PfxUtils` | Reempacota PFX para senha/alias configurados | Não copiar como regra global; melhorar para preservar/selecionar alias com segurança |
| `XmlSigner` | Assinatura XMLDSig | Reutilizar conceito e algoritmos após confirmação do contrato DeRE |
| `ConsultaLoteProcessamento` | Mapeia retorno e atualiza eventos | Adaptar a um processador de retorno versionado e idempotente |
| `ScheduledTasks` | Polling de lotes e recibos | Reutilizar intenção; usar scheduler distribuído e leases persistidos |

## 4. Classificação dos mecanismos

### 4.1 Reutilizar conceito

- registry de handlers/factories por código de layout;
- XSD versionado e validação antes de transmitir;
- XMLDSig separado da regra de negócio;
- cliente de governo separado da aplicação;
- estados explícitos de evento, transmissão e processamento;
- consulta assíncrona quando o protocolo oficial exigir;
- telas de erros, download e exportação de resultados;
- testes unitários e de integração com XML real e schemas.

### 4.2 Adaptar e melhorar

- importação de arquivo: aceitar CSV/XML e persistir uma operação de ingestão antes do processamento;
- criação de lote: tratar como workflow persistido, não como apenas uma chamada `@Async`;
- factories: trocar o enum de tipo de evento do Sender por `LayoutCode` e `LayoutVersion` da DeRE;
- parsing: usar parsers por layout com namespace-aware XML e erros de localização;
- validação: separar XSD, regras de domínio e dependências entre layouts;
- certificado: metadata separada do segredo, criptografia autenticada, rotação e eventos de auditoria;
- mTLS: usar truststore/hostname verification apropriados e reconfiguração segura do client;
- retries: distinguir erros transitórios, rejeições definitivas e respostas ambíguas;
- polling: claim/lease, backoff, limite de tentativas, idempotência e observabilidade;
- respostas: modelo próprio para protocolo, status governamental, recibo e ocorrências;
- frontend: manter os casos de uso, mas reconstruir a experiência na stack atual React/Vite.

### 4.3 Não reproduzir

- o enum de tipo de evento do Sender ou qualquer enum/classe do Sender no domínio DeRE;
- o builder central como ponto único de todos os layouts;
- XPath literal espalhado ou dependente de namespace desabilitado;
- `switch` de códigos governamentais sem tabela/versionamento e fallback seguro;
- estado de transmissão somente em `ConcurrentHashMap`;
- senha de keystore global, alias global e reempacotamento implícito como política de segurança;
- `NoopHostnameVerifier` e confiança ampla em certificados self-signed em produção;
- `RuntimeException`, `@SneakyThrows` e mensagens genéricas como contrato de API;
- log de XML, PFX, senha, chave privada ou payload sem política de redaction;
- tabelas e regras DeRE colocadas dentro dos pacotes do Sender.

## 5. Arquitetura proposta para a DeRE

A base atual é Spring Boot 4.1/Java 21 no backend, Flyway, JPA, H2 local/MariaDB para persistência, Apache Santuario para XMLDSig e React/Vite no frontend. A recomendação é evoluir essa base sem introduzir uma camada abstrata para cada classe.

```text
br.gov.dere
├── domain
│   ├── event          # EventoDere, LayoutCode, version, status
│   ├── layout         # contrato e metadados de layouts
│   ├── contributor    # agregado canônico D-1001
│   ├── pgcc           # agregado canônico D-1011
│   ├── certificate    # metadata e estados, sem segredo bruto
│   └── transmission   # transmissão, tentativa, protocolo e recibo
├── application
│   ├── importation    # upload, ingestão e idempotência
│   ├── validation      # pipeline de validação
│   ├── conversion      # CSV/XML pelo modelo canônico
│   ├── certificate     # casos de uso de certificado
│   └── transmission    # envio, polling e processamento de retorno
├── infrastructure
│   ├── persistence     # entidades, repositories e migrations
│   ├── xml             # parser, generator, XSD e XMLDSig
│   ├── csv             # parser/generator CSV
│   ├── certificate     # PKCS12, secret box, store e SSLContext
│   ├── storage         # blob local/S3 compatível
│   └── government      # cliente DeRE, mTLS e contratos externos
└── api                 # DTOs, controllers, error mapping e autenticação
```

O frontend atual deve evoluir para módulos de Dashboard, Declarações, Importações, Validações, Certificados, Transmissões e Configurações. O backend não deve compartilhar DTOs internos ou entidades JPA com o frontend.

## 6. Modelo de dados proposto

As tabelas já existentes em `src/main/resources/db/migration` são um início útil, mas `dere_d1001_xml`, `dere_d1011_xml` e as demais tabelas específicas criadas em V4 tendem a duplicar estrutura. A evolução recomendada é usar uma tabela de ingestão/evento genérica com payload e projeções específicas apenas quando houver necessidade de consulta eficiente.

| Entidade | Dados principais |
|---|---|
| `dere_entity` | contribuinte/entidade, identificação e status |
| `dere_event` | layout, versão, identificador, competência, modelo canônico serializado, XML/CSV de origem, status e hash |
| `dere_event_error` | evento, fase, linha, campo, código, mensagem e severidade |
| `dere_upload_batch` | arquivo/operação, usuário, layout, formato, hash, contagens e status |
| `dere_layout_dependency` | layout filho, layout pai, chave de vínculo e vigência |
| `dere_certificate` | label, fingerprint, alias, validade, ativo, storage reference e auditoria |
| `dere_secret_reference` | referência ao segredo criptografado ou provider externo, nunca senha em claro |
| `dere_transmission` | evento/lote, operação, idempotency key, request reference, protocolo e estado |
| `dere_transmission_attempt` | tentativa, timestamp, HTTP status, código governo, duração, correlation ID e referências de request/response |
| `dere_government_receipt` | protocolo/recibo, evento, payload de retorno e estado final |
| `dere_audit_log` | ator, entidade, operação, resultado, fingerprint/metadata e timestamp |

Restrições importantes: hash único para impedir reprocessamento acidental; chave de idempotência por operação; `version`/`created_at` para concorrência otimista; índices por status e `next_attempt_at`; nenhuma coluna de senha em claro.

## 7. Engine de layouts

O contrato deve ser pequeno e orientado ao caso de uso, por exemplo:

```java
public interface DereLayoutHandler<T> {
    LayoutCode layout();
    LayoutVersion version();
    T parseXml(XmlInput input);
    T parseCsv(CsvInput input);
    ValidationResult validate(T model, ValidationContext context);
    XmlDocument generateXml(T model);
    CsvDocument generateCsv(T model);
}
```

O handler não deve persistir, transmitir ou conhecer HTTP. Metadados de schema, limites, identificadores, parser, generator e dependency validator ficam agrupados no registro do layout. O Spring pode montar `Map<LayoutCode, DereLayoutHandler<?>>`, mas a aplicação deve validar duplicidade de código/versão ao iniciar.

Para os primeiros layouts:

- `D1001Definition`: contribuinte, identificação, período/vigência e informações próprias do evento;
- `D1011Definition`: contas PGCC, níveis, códigos, descrição, vigência e vínculo com o D-1001 vigente;
- `D1011DependencyValidator`: consulta uma porta de persistência para verificar D-1001 existente, mesma entidade e vigência compatível.

Layouts futuros entram como novos módulos/handlers e schemas, sem alterar o domínio dos handlers existentes.

## 8. Pipeline CSV/XML e modelo canônico

```text
CSV ──> CsvParser ──────┐
                        ├──> Canonical Model ──> DereValidationEngine ──> XmlGenerator
XML ──> XSD Validator ─>┘                                      └───────> CsvGenerator
```

O XML deve ser validado contra XSD antes do parsing semântico. Depois disso, o parser XML e o parser CSV convergem para o mesmo modelo canônico. Regras de obrigatoriedade, domínio, unicidade, datas, valores e dependências executam uma única vez no `DereValidationEngine`.

Os erros devem carregar `phase` (`SYNTAX`, `XSD`, `BUSINESS`, `DEPENDENCY`), `line`, `field`, `path`, `code`, `message` e `severity`. Assim a API e a UI conseguem mostrar linha/campo sem depender de texto de exception.

## 9. Certificado digital

| Sender atual | DeRE proposta |
|---|---|
| `CertificateProviderImpl` mantém `KeyStore`/entry em campos singleton | provider carrega por certificado ativo, com cache controlado e invalidação após rotação |
| segredo vem de propriedade `key-store-secret` | chave mestra vem de secret externo/configuração injetada; nunca hardcoded |
| `PfxUtils` reempacota para alias e senha globais | validar PFX, registrar fingerprint/alias/validade e armazenar blob protegido sem alterar identidade desnecessariamente |
| senha enviada ao serviço e usada em char array | segredo é recebido apenas no caso de uso, criptografado com AES-GCM ou delegado a Secrets Manager |
| certificado ativo/inativo em registro simples | rotação cria versão nova, encerra a anterior e registra ator, motivo e validade |
| falha de validade vira exception genérica | estados explícitos: válido, expirado, ainda não válido, próximo do vencimento, revogado/desabilitado |
| factory do cliente HTTP reconfigura o client diretamente | `MtlsClientFactory` constrói client por snapshot seguro e troca atomicamente |

A API de certificados retorna somente metadata: label, alias/fingerprint, validade e status. PFX, senha, private key e SSLContext nunca aparecem em DTOs, logs ou endpoints de download. A implementação local pode usar storage criptografado, mas a porta deve aceitar futuramente AWS Secrets Manager para senha e S3 para o PFX.

## 10. Transmissão

| Sender atual | DeRE proposta |
|---|---|
| lote e evento têm status separados e específicos do Sender | `DereTransmission` e `DereEvent` possuem estados próprios, versionados e documentados |
| `TransmissaoManager` deduplica em memória | unique key + lock/lease persistido + worker seguro para múltiplas instâncias |
| retry anotado no cliente | política por categoria de erro, com tentativa persistida, jitter e limite |
| protocolo salvo no lote | protocolo, request reference e response reference ficam em transmissão/attempt |
| polling em tarefa fixa a cada 45 s | `next_attempt_at`, backoff, lease, timeout e retomada após restart |
| retorno tratado por `switch` de integer | tabela/adapter de status por versão do contrato e fallback explícito |
| auditoria depende parcialmente de logs e XML de retorno | `dere_transmission_attempt` e `dere_audit_log` são fonte de auditoria |

O envio deve seguir este workflow persistido:

```text
READY -> SIGNING -> SIGNED -> SUBMITTING -> PROTOCOL_RECEIVED
                                             -> PROCESSING
                                             -> ACCEPTED / ACCEPTED_WITH_WARNINGS / REJECTED
```

Falhas transitórias entram em `RETRY_WAIT`; falhas definitivas em `FAILED`. Uma tentativa ambígua não deve ser reenviada automaticamente sem política de idempotência e reconciliação.

Antes de ativar o cliente real, devem ser confirmados no manual oficial DeRE o envelope, endpoints, mTLS, formato de assinatura, limites de lote, semântica de protocolo/status/recibo e regras de consulta. Os XSD presentes em `src/main/resources/dere/schemas` são insumo de implementação, mas não substituem a confirmação do contrato de transmissão.

## 11. Interface proposta

Menu principal:

```text
Dashboard
Declarações
  ├── D-1001
  └── D-1011
Importações
  ├── CSV
  └── XML
Validações
Certificado Digital
Transmissões
Configurações
```

Fluxo de importação:

```text
selecionar layout -> selecionar CSV/XML -> upload -> parsing/XSD
-> validação canônica -> resumo -> detalhes -> exportar relatório
```

O resumo deve exibir válidos, avisos e erros. A tabela de erros deve permitir filtros por arquivo, linha, campo, código, fase e mensagem. O XML/CSV gerado deve ser visualizável, baixável e associado à operação de ingestão, não apenas a logs.

## 12. Estado atual da POC e lacunas

Já existe uma base DeRE independente em `src/`, com:

- modelos/persistência de eventos, lotes, entidades, usuários, auditoria e certificados;
- `D1001XmlGenerator`, `D1001ImportService`, `DereCsvConverter` e `XmlSupport`;
- XSDs DeRE versionados em `src/main/resources/dere/schemas`;
- assinatura/verificação XMLDSig e cliente de Receita separado;
- fluxo mock local e UI React/Vite;
- migrations Flyway V1–V4.

As principais lacunas arquiteturais observadas são:

- D-1001 está mais avançado que o contrato genérico de layout;
- D-1011 ainda precisa de modelo, parser/generator, persistência canônica e dependency validator;
- `DereBatchEntity` cobre apenas parte do histórico de transmissão;
- não existe ainda `dere_transmission_attempt` e lease de worker;
- `CertificateService` ainda concentra criptografia POC e acesso ao repositório;
- `PocSecretBox` usa uma chave derivada de configuração, aceitável como POC somente se a chave vier externamente, mas insuficiente como abstração de produção;
- V4 cria tabelas XML por layout futuro antes de os handlers existirem; isso deve ser revisado para não congelar uma estratégia de tabela por layout;
- autenticação e autorização ainda são mínimas para uma operação multi-entidade;
- a transmissão externa permanece mockada/local conforme `docs/dere/PROJECT_IMPLEMENTATION.md`.

## 13. Plano de implementação após aprovação

1. **Contrato e limites:** confirmar documentação oficial DeRE, endpoints, assinatura, mTLS, lote, protocolo e estados.
2. **Estrutura base:** consolidar packages DeRE, erro estruturado, correlação, auditoria e políticas de segurança.
3. **Layout engine:** registrar `LayoutCode`/versão e implementar o registry sem dependência do Sender.
4. **Modelo canônico:** definir modelos D-1001 e D-1011, validações comuns e representação de erros.
5. **D-1001:** persistência, CSV, XML, generator, parser e XSD.
6. **D-1011:** PGCC, hierarquia/contas, CSV, XML, generator, parser e XSD.
7. **Dependency engine:** implementar D-1011 → D-1001 com vigência e entidade.
8. **Validation engine:** unir sintaxe, XSD, regras de negócio e dependências em um relatório único.
9. **Conversões:** CSV → modelo → XML e XML → modelo → CSV, com testes de round-trip quando aplicável.
10. **Importação persistida:** upload batch, hash/idempotência, armazenamento seguro e relatório exportável.
11. **Certificado A1:** metadata, PFX protegido, senha criptografada, rotação, validade, auditoria e provider externo futuro.
12. **Transmissão:** modelo próprio, attempt history, idempotência, mTLS, retries e cliente real após confirmação oficial.
13. **Polling:** worker persistido, lease distribuído, backoff, reconciliação e recibo.
14. **Frontend POC:** dashboard, importação, erros, certificados e transmissões.
15. **Testes:** unitários, fixtures XML/CSV, XSD, segurança de certificado, concorrência/idempotência, integração com mock governamental e smoke E2E.
16. **Documentação operacional:** configuração, secrets, rotação, observabilidade, recuperação e matriz de status.

Cada etapa deve manter o domínio DeRE independente do Sender.

## 14. Critérios de aceite arquiteturais

- nenhum tipo DeRE é modelado como o enum de eventos do Sender;
- D-1001 e D-1011 são handlers independentes registrados pelo engine;
- CSV e XML convergem para o mesmo modelo e executam as mesmas regras;
- D-1011 consulta D-1001 persistido e vigente;
- senha/PFX não aparecem em resposta ou log;
- transmissão pode reiniciar após queda sem perder estado;
- duas instâncias não processam a mesma tentativa simultaneamente;
- cada tentativa externa é auditável sem depender de log;
- erros apontam fase, campo/linha e código;
- cliente real só é habilitado após validar contrato oficial DeRE;
- testes não dependem do certificado real nem de endpoint governamental.
