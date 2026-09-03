# Projeto DeRE para EFPC --- Contexto, POC e Plano Inicial

> Documento de contexto para iniciar no Cursor uma POC de integração com
> a **DeRE --- Declaração de Regimes Específicos**, com foco em
> **Entidades Fechadas de Previdência Complementar (EFPC/PREVIC)**.
>
> Atualizado em: 01/09/2026.
>
> **Importante:** a documentação da DeRE ainda está evoluindo. Antes de
> implementar qualquer regra, conferir a página oficial e os XSDs
> vigentes.

## 1. Objetivo

Construir uma POC que prove tecnicamente o fluxo básico de integração
com a DeRE:

1.  representar os dados exigidos pelos eventos;
2.  gerar XML conforme o leiaute oficial;
3.  validar o XML contra os XSDs oficiais;
4.  assinar cada evento com certificado ICP-Brasil usando XMLDSig;
5.  montar o lote XML;
6.  autenticar nas APIs pelo Receita Integra (OAuth 2.0 / Client
    Credentials);
7.  transmitir o lote;
8.  armazenar o protocolo;
9.  consultar o processamento assíncrono;
10. armazenar recibos, rejeições e mensagens de validação.

A primeira POC deve priorizar os eventos estruturantes:

-   **D-1001 --- Informações do Contribuinte**
-   **D-1011 --- Plano Geral de Contas Comentado (PGCC)**

Em seguida:

-   **D-1101 --- Balancete Mensal**
-   **D-1106 --- Identificação de Aplicações Financeiras**

------------------------------------------------------------------------

## 2. Visão geral

A DeRE é uma obrigação acessória ligada aos regimes específicos de
IBS/CBS.

Arquiteturalmente, ela é bastante semelhante às integrações do
ecossistema SPED, como e-Financeira, eSocial e EFD-Reinf:

``` text
Sistema da Entidade / Sistema Previdenciário
                |
                v
       Extração / Mapeamento
                |
                v
        Regras do evento DeRE
                |
                v
          Geração do XML
                |
                v
        Validação pelos XSD
                |
                v
   Assinatura XMLDSig / ICP-Brasil
                |
                v
          Montagem do lote
                |
                v
 OAuth2 / Receita Integra -> Bearer Token
                |
                v
          API DeRE - POST
                |
                v
             Protocolo
                |
                v
       Consulta assíncrona
                |
          +-----+------+
          |            |
          v            v
       Recibo       Rejeição
                       |
                       v
               Correção / reenvio
```

O protocolo de recepção do lote **não significa aceitação dos eventos**.
O processamento é assíncrono e deve ser consultado posteriormente.

------------------------------------------------------------------------

## 3. Comparação conceitual com e-Financeira

  -----------------------------------------------------------------------
  Característica          e-Financeira            DeRE
  ----------------------- ----------------------- -----------------------
  Integração              Sim                     Sim
  sistema-sistema                                 

  Eventos estruturados    Sim                     Sim

  XML                     Sim                     Sim

  XSD oficial             Sim                     Sim

  Certificado ICP-Brasil  Sim                     Sim

  Assinatura digital      Sim                     Sim

  Transmissão eletrônica  Sim                     Sim

  Processamento           Sim                     Sim
  assíncrono                                      

  Protocolo/recibo        Sim                     Sim

  Tratamento de rejeições Sim                     Sim

  Principal finalidade    Reporte                 Escrituração/apuração
                          financeiro/fiscal       IBS/CBS dos regimes
                                                  específicos

  Componente contábil     Dependente do evento    Muito relevante
  -----------------------------------------------------------------------

Se já existir uma infraestrutura interna de e-Financeira, avaliar
reaproveitamento de:

-   gestão de certificado;
-   assinatura XML;
-   geração de XML;
-   validação XSD;
-   transporte HTTP;
-   controle de lotes;
-   protocolo;
-   polling/consulta assíncrona;
-   persistência de recibos;
-   tratamento de erros;
-   retificação;
-   trilha de auditoria.

Não presumir que os modelos de dados ou regras da e-Financeira possam
ser reutilizados diretamente.

------------------------------------------------------------------------

## 4. Autenticação x assinatura digital

São responsabilidades diferentes.

### Autenticação da API

A API usa o **Receita Integra** com:

-   OAuth 2.0;
-   fluxo `Client Credentials`;
-   `client_id`;
-   `client_secret`;
-   Bearer Token.

Segundo o Manual do Desenvolvedor v1.0.2, o token possui validade de 60
minutos.

### Assinatura do evento

Cada evento XML deve ser assinado **individualmente**, antes de ser
colocado no lote.

Padrão:

-   certificado ICP-Brasil válido;
-   XMLDSig;
-   Enveloped Signature;
-   observar exatamente as regras do Manual do Desenvolvedor e XSD
    vigente.

Portanto:

``` text
Credenciais Receita Integra
        |
        +--> OAuth2 --> Bearer Token --> autenticação da API

Certificado ICP-Brasil
        |
        +--> XMLDSig --> assinatura de cada evento
```

------------------------------------------------------------------------

## 5. Fluxo técnico esperado

``` text
1. Extrair dados
2. Mapear para DTO/modelo DeRE
3. Gerar XML do evento
4. Validar XML contra XSD
5. Assinar evento XML
6. Validar novamente quando aplicável
7. Encapsular evento(s) assinados no lote
8. Validar lote contra XSD
9. Obter Bearer Token no Receita Integra
10. POST do lote
11. Persistir protocolo
12. Consultar processamento
13. Interpretar retorno por evento
14. Persistir recibo ou erro
15. Disponibilizar reprocessamento/reenvio
```

Eventos com dependência lógica devem respeitar a precedência. Não
colocar eventos dependentes no mesmo lote apenas por conveniência.

------------------------------------------------------------------------

## 6. Calendário inicial

### Fase 1 --- Eventos estruturantes

**A partir de 01/10/2026** o ambiente passa a receber:

-   D-1001 --- Informações do Contribuinte;
-   D-1011 --- Plano Geral de Contas Comentado.

**01/10/2026 não é prazo final.**

D-1001 e D-1011 devem estar transmitidos e processados com sucesso
**antes dos eventos periódicos da competência outubro/2026**.

### Fase 2 --- Primeira competência mensal

Competência: **outubro/2026**

Prazo: **15/11/2026**

Eventos periódicos aplicáveis:

  -----------------------------------------------------------------------
  Evento                 Descrição              Aplicabilidade
  ---------------------- ---------------------- -------------------------
  D-1101                 Balancete Mensal       Principal evento mensal

  D-1106                 Identificação de       Quando obrigatório
                         Aplicações Financeiras 

  D-1121                 Relação de Deduções    Conforme aplicabilidade
                         Utilizadas na Apuração 

  D-2101                 Débito em Operações    Quando aplicável
                         com Títulos de Dívida  
                         com Oferta Pública     

  D-1199                 Fechamento Mensal      Consolidação/fechamento
  -----------------------------------------------------------------------

A regra geral dos periódicos mensais é entrega até o **dia 15 do mês
subsequente**.

Segundo o esclarecimento oficial, o prazo não é prorrogado quando o dia
15 cair em sábado, domingo ou feriado.

### Visão simplificada

``` text
01/10/2026
   |
   +--> D-1001
   |
   +--> aguardar sucesso
   |
   +--> D-1011
   |
   +--> aguardar sucesso
   |
   |      competência outubro
   |              |
   |              v
15/11/2026 <--- D-1101 / D-1106 / demais aplicáveis / D-1199
```

------------------------------------------------------------------------

## 7. Eventos prioritários para EFPC

### D-1001 --- Informações do Contribuinte

Evento estrutural/cadastral.

POC:

-   identificar todos os campos obrigatórios no leiaute;
-   criar modelo de domínio;
-   gerar XML;
-   validar contra XSD;
-   assinar;
-   enviar;
-   consultar retorno.

Este deve ser o **primeiro evento funcional da POC**.

### D-1011 --- Plano Geral de Contas Comentado (PGCC)

Evento estrutural especialmente importante para EFPC.

A atualização de agosto/2026 adicionou o **Plano de Contas Referencial
da PREVIC**, permitindo o mapeamento contábil das Entidades Fechadas de
Previdência Complementar.

Conceitualmente:

``` text
Conta contábil interna da EFPC
          |
          +--> Conta referencial PREVIC
          |
          +--> Código de tributação / classificação DeRE
```

Investigar detalhadamente:

-   estrutura das contas analíticas;
-   código da conta;
-   descrição;
-   vigência;
-   natureza;
-   conta referencial PREVIC;
-   `codTrib`;
-   cardinalidades;
-   regras de inclusão/alteração/exclusão;
-   dependências.

### D-1101 --- Balancete Mensal

Evento periódico.

Objetivo da segunda etapa da POC:

``` text
Balancete interno
      |
      +--> contas previamente declaradas no D-1011
      |
      +--> saldos / valores
      |
      +--> XML D-1101
```

É importante validar a relação entre o balancete e a versão vigente do
PGCC.

### D-1106 --- Identificação de Aplicações Financeiras

Muito relevante para EFPC.

A Nota Orientativa 2026.001 incluiu ajustes como:

-   variações mensais negativas na carteira de ativos;
-   bloqueio de períodos de apuração futuros;
-   validação matemática do saldo final;
-   validação dos rendimentos apurados.

Deve ser analisado depois que D-1001/D-1011 estiverem funcionais.

------------------------------------------------------------------------

## 8. Ordem de implementação sugerida

### POC 0 --- infraestrutura

Objetivo: provar geração + validação XSD local.

Entregas:

-   baixar XSD oficial;
-   organizar schemas por versão;
-   criar módulo XML;
-   gerar um D-1001 fictício;
-   validar localmente;
-   testes automatizados de XML válido/inválido.

### POC 1 --- assinatura

Adicionar:

-   leitura segura do certificado;
-   XMLDSig;
-   Enveloped Signature;
-   validação da assinatura;
-   testes automatizados.

Nunca versionar:

-   `.pfx`;
-   `.p12`;
-   senha;
-   `client_secret`;
-   certificados/chaves privadas.

Adicionar ao `.gitignore`.

### POC 2 --- lote

Adicionar:

-   envelope XML de lote;
-   eventos assinados individualmente;
-   validação do lote pelo XSD oficial;
-   identificadores e rastreabilidade.

### POC 3 --- Receita Integra

Adicionar:

-   OAuth2 Client Credentials;
-   obtenção do token;
-   cache seguro do token;
-   renovação;
-   tratamento de 401/403.

### POC 4 --- transmissão

Adicionar:

-   POST do lote;
-   persistência da requisição;
-   persistência do protocolo;
-   logs sem dados sensíveis.

### POC 5 --- processamento assíncrono

Adicionar:

-   consulta por protocolo;
-   estados do processamento;
-   polling com backoff;
-   mensagens de erro;
-   recibos;
-   persistência do XML enviado e retorno.

### POC 6 --- D-1011 / PREVIC

Adicionar:

-   modelo PGCC;
-   mapeamento de conta interna -\> PREVIC;
-   `codTrib`;
-   vigência;
-   geração/validação/transmissão.

### POC 7 --- D-1101

Adicionar:

-   importação/extração de balancete;
-   validação contra PGCC;
-   geração;
-   assinatura;
-   transmissão;
-   retorno.

### POC 8 --- D-1106

Adicionar aplicações financeiras e validações específicas.

------------------------------------------------------------------------

## 9. Arquitetura de referência

Sugestão de separação:

``` text
dere/
├── domain/
│   ├── contributor/
│   ├── chartofaccounts/
│   ├── trialbalance/
│   └── investments/
│
├── application/
│   ├── generate/
│   ├── validate/
│   ├── sign/
│   ├── transmit/
│   └── query/
│
├── infrastructure/
│   ├── xml/
│   ├── xsd/
│   ├── certificate/
│   ├── receita-integra/
│   ├── dere-api/
│   └── persistence/
│
└── events/
    ├── d1001/
    ├── d1011/
    ├── d1101/
    ├── d1106/
    └── d1199/
```

Evitar espalhar detalhes de XML/XSD pela regra de negócio.

Idealmente:

``` text
Domain Model
    |
    v
Event Mapper
    |
    v
XML Model
    |
    v
Serializer
    |
    v
XSD Validator
    |
    v
XML Signer
    |
    v
Batch Builder
    |
    v
DeRE Client
```

------------------------------------------------------------------------

## 10. Persistência mínima recomendada

Criar uma estrutura que permita auditoria completa.

### `dere_event`

Campos conceituais:

``` text
id
event_type
event_identifier
environment
reference_period
operation_type
schema_version
status
xml_unsigned
xml_signed
receipt_number
created_at
sent_at
processed_at
```

### `dere_batch`

``` text
id
protocol
status
request_xml
response_xml
created_at
sent_at
last_query_at
```

### `dere_event_error`

``` text
id
event_id
code
description
location
created_at
```

Não persistir senha de certificado ou `client_secret` em texto puro.

------------------------------------------------------------------------

## 11. Estados sugeridos

``` text
DRAFT
GENERATED
XSD_VALIDATED
SIGNED
BATCHED
SENT
PROTOCOL_RECEIVED
PROCESSING
ACCEPTED
ACCEPTED_WITH_WARNINGS
REJECTED
```

O estado `SENT` não significa que o evento foi aceito.

Somente o recibo após processamento bem-sucedido deve ser tratado como
aceitação definitiva.

------------------------------------------------------------------------

## 12. Produção Restrita

A POC deve ser preparada para o **Ambiente de Produção Restrita**.

URL base documentada:

``` text
https://api.receitafederal.gov.br/prr-dere
```

Token:

``` text
POST https://api.receitafederal.gov.br/token
```

Recepção:

``` text
POST https://api.receitafederal.gov.br/prr-dere/v1/recepcao/lotes
```

Consulta:

``` text
https://api.receitafederal.gov.br/prr-dere/v1/consulta/lotes/
```

Não codificar URLs diretamente nas classes.

Usar configuração:

``` yaml
dere:
  environment: restricted-production
  api:
    base-url: ${DERE_BASE_URL}
    token-url: ${RECEITA_INTEGRA_TOKEN_URL}
  auth:
    client-id: ${DERE_CLIENT_ID}
    client-secret: ${DERE_CLIENT_SECRET}
```

Confirmar endpoints, métodos e parâmetros diretamente no Manual do
Desenvolvedor vigente antes da implementação.

------------------------------------------------------------------------

## 13. Documentação oficial --- baixar antes de iniciar

Página central oficial do CGIBS, que deve ser usada para verificar a
**versão vigente**:

https://cgibs.gov.br/declaracao-de-regimes-especificos-dere

### Downloads prioritários

#### 1. Manual do Desenvolvedor v1.0.2

Fundamental para API, OAuth2, lote, XMLDSig, certificados, erros e
Produção Restrita.

https://cgibs.gov.br/upload/arquivos/202608/18211952-07-manual-do-desenvolvedor-v-1-0-2.pdf

#### 2. Leiautes dos Eventos v1.1.0

Fundamental para implementar D-1001, D-1011, D-1101, D-1106 etc.

https://cgibs.gov.br/upload/arquivos/202606/22141225-02-leiautes-da-dere-eventos-v-1-1-0.pdf

#### 3. Anexo I --- Tabelas v1.1.0

Inclui tabelas de domínio usadas nos eventos.

https://cgibs.gov.br/upload/arquivos/202606/22141221-03-leiautes-da-dere-anexo-i-tabelas-v-1-1-0.pdf

**Atenção EFPC:** verificar também a Nota Orientativa 2026.001 e os XSDs
atualizados, pois a atualização de agosto incluiu a **Tabela 24 ---
Plano de Contas Referencial PREVIC**.

#### 4. Anexo II --- Regras de Validação v1.1.0

https://cgibs.gov.br/upload/arquivos/202606/22141448-04-leiautes-da-dere-anexo-ii-regras-de-validacao-v-1-1-0.pdf

#### 5. XSDs

Os XSDs devem ser baixados sempre da página central para evitar usar uma
versão obsoleta:

https://cgibs.gov.br/declaracao-de-regimes-especificos-dere

Na página procurar:

-   `06 Arquivos XSD (v 1 1 0)`
-   `06 Arquivos XSD (Nota Orientativa 2026 001)`

**Para EFPC, priorizar o pacote atualizado pela Nota Orientativa
2026.001 enquanto ele representar a versão implantada no ambiente de
Produção Restrita.**

#### 6. Nota Orientativa 2026.001 --- 18/08/2026

Importante para EFPC/PREVIC.

https://cgibs.gov.br/upload/arquivos/202608/18211950-dere-nota-orientativa-2026-001-18-08-2026.pdf

Principais impactos:

-   PREVIC no D-1011;
-   alterações no D-1106;
-   alterações nos retornos D-9101/D-9106;
-   Tabela 24 PREVIC;
-   novas/alteradas regras de validação;
-   atualização dos XSDs.

#### 7. Manual Receita Integra v1.4

Fundamental para autenticação OAuth2.

https://cgibs.gov.br/upload/arquivos/202608/14165847-09-manual-de-integracao-tecnica-receita-integra-v-1-4.pdf

### Fonte alternativa oficial --- SPED

Página da DeRE:

https://www.gov.br/sped/pt-br/assuntos/documentos-fiscais/dere

FAQ oficial:

https://www.gov.br/receitafederal/pt-br/acesso-a-informacao/perguntas-frequentes/sped/dere/dere

------------------------------------------------------------------------

## 14. Atenção ao versionamento

Nunca assumir que:

``` text
versão do Manual == versão do Leiaute == versão do XSD
```

Em 01/09/2026, a página oficial lista, entre outros:

``` text
Manual do Usuário:        1.1.0
Leiautes:                 1.1.0
Anexo I:                  1.1.0
Anexo II:                 1.1.0
XSD pacote base:          1.1.0
Manual do Desenvolvedor:  1.0.2
Receita Integra:          1.4
Nota Orientativa:         2026.001
```

Além disso, existem XSDs atualizados pela Nota Orientativa 2026.001.

Criar o código considerando versionamento explícito de schemas.

Exemplo:

``` text
resources/dere/
├── v1_1_0/
│   └── xsd/
└── nota-2026-001/
    └── xsd/
```

Não sobrescrever silenciosamente versões antigas.

------------------------------------------------------------------------

## 15. Questões que a POC deve responder

### Técnica

-   Conseguimos gerar D-1001 válido?
-   Conseguimos validar localmente com XSD?
-   Conseguimos assinar corretamente com XMLDSig?
-   O XML continua válido após assinatura?
-   Conseguimos montar o lote exatamente conforme o XSD?
-   Conseguimos autenticar no Receita Integra?
-   Conseguimos transmitir em Produção Restrita?
-   Conseguimos consultar o protocolo?
-   Conseguimos interpretar recibos e erros?

### Negócio / EFPC

-   Quais dados do D-1001 já existem no sistema?
-   Onde está o plano de contas da entidade?
-   Como mapear contas internas para a Tabela 24 PREVIC?
-   Quem define/valida `codTrib`?
-   Onde obter o balancete mensal?
-   O balancete atual possui granularidade suficiente para D-1101?
-   Onde estão os dados de aplicações necessários ao D-1106?
-   Existem dados necessários que hoje estão apenas no sistema contábil
    externo?
-   Quais eventos adicionais são efetivamente aplicáveis à EFPC?

------------------------------------------------------------------------

## 16. Fora do escopo inicial da POC

Não tentar resolver inicialmente:

-   todos os eventos da DeRE;
-   interface administrativa completa;
-   fechamento D-1199 completo;
-   retificação completa;
-   produção;
-   automação contábil integral;
-   migração histórica;
-   dashboards;
-   alta disponibilidade.

Primeiro provar o caminho crítico:

``` text
DADO
 -> XML
 -> XSD
 -> ASSINATURA
 -> LOTE
 -> API
 -> PROTOCOLO
 -> CONSULTA
 -> RECIBO
```

------------------------------------------------------------------------

## 17. Critério de sucesso da primeira POC

A POC inicial estará tecnicamente validada quando conseguir:

``` text
[ ] carregar os XSDs oficiais
[ ] criar D-1001 com dados de teste
[ ] gerar XML
[ ] validar XML localmente
[ ] assinar com certificado de teste/adequado ao ambiente
[ ] montar lote válido
[ ] obter token Receita Integra
[ ] transmitir em Produção Restrita
[ ] receber protocolo
[ ] consultar protocolo
[ ] receber resultado individual do evento
[ ] persistir recibo ou rejeição
```

Depois disso, implementar D-1011/PREVIC.

------------------------------------------------------------------------

## 18. Prompt inicial sugerido para o Cursor

Copiar este documento para o repositório, por exemplo:

``` text
docs/dere/PROJECT_CONTEXT.md
```

Depois usar no Cursor:

``` text
Leia integralmente @docs/dere/PROJECT_CONTEXT.md.

Este projeto implementará uma integração com a DeRE (Declaração de Regimes
Específicos), inicialmente como POC para Entidades Fechadas de Previdência
Complementar (EFPC/PREVIC).

Antes de escrever código:

1. Analise a estrutura atual do repositório.
2. Identifique linguagem, framework, padrões arquiteturais e infraestrutura de testes.
3. Leia a documentação oficial da DeRE indicada no PROJECT_CONTEXT.md.
4. Se os PDFs/XSDs já estiverem no repositório, use os arquivos locais como fonte
   primária da especificação técnica.
5. Não invente campos, namespaces, endpoints, estruturas XML ou regras.
6. Para qualquer elemento do XML, confirme no XSD/leiaute vigente.
7. Considere explicitamente o versionamento dos schemas.
8. Não implemente integração de produção nesta etapa.

Objetivo da primeira POC:

D-1001
 -> geração XML
 -> validação XSD
 -> XMLDSig
 -> lote
 -> autenticação Receita Integra
 -> transmissão em Produção Restrita
 -> protocolo
 -> consulta assíncrona
 -> recibo/rejeição.

Primeiro gere um plano de implementação detalhado, incluindo:
- estrutura de módulos/pacotes;
- componentes;
- modelos;
- bibliotecas necessárias;
- tratamento do certificado;
- validação XSD;
- assinatura XMLDSig;
- cliente OAuth2;
- cliente DeRE;
- persistência mínima;
- testes;
- riscos;
- dependências externas;
- ordem das atividades.

Não altere código ainda.

Ao final, apresente o plano e aguarde aprovação para iniciar a implementação.
```

------------------------------------------------------------------------

## 19. Próxima evolução recomendada

Depois do D-1001 funcional:

``` text
D-1001
  |
  v
D-1011 + PREVIC
  |
  v
D-1101 Balancete
  |
  v
D-1106 Aplicações
  |
  v
demais eventos aplicáveis
  |
  v
D-1199 Fechamento
```

O maior trabalho provavelmente não estará no transporte HTTP, mas no
**mapeamento correto dos dados contábeis/previdenciários para os eventos
e regras fiscais da DeRE**.

------------------------------------------------------------------------

## 20. Regra para desenvolvimento assistido por IA

Para Cursor/Codex:

> **XSD + Leiaute + Anexo de Validações são a fonte de verdade da
> estrutura técnica. Não inferir estrutura XML a partir de exemplos ou
> nomes de campos.**

Sempre que houver divergência entre este documento e uma publicação
oficial mais recente, prevalece a publicação oficial vigente.

------------------------------------------------------------------------

## 21. Stack tecnológica adotada

A POC e a evolução do projeto DeRE utilizarão a **mesma stack
tecnológica do Fênix3**, priorizando padronização, reaproveitamento e
menor custo operacional.

### Backend

-   Java 21
-   Spring Boot 4.1
-   Spring Web / `RestClient`
-   Spring Data JPA
-   Hibernate
-   Jakarta Validation
-   MapStruct
-   Lombok
-   Flyway
-   MariaDB
-   springdoc-openapi
-   JUnit 5
-   Testcontainers
-   JAXB / Jakarta XML Binding
-   Apache Santuario XML Security

### Frontend

-   React
-   Vite
-   TypeScript
-   MUI
-   MUI X Data Grid
-   React Hook Form
-   Zod
-   TanStack Query
-   React Router
-   Axios

### Integração DeRE

-   XML e XSD
-   XMLDSig
-   certificado ICP-Brasil
-   OAuth 2.0 Client Credentials
-   REST APIs Receita Integra / DeRE
-   processamento assíncrono por protocolo

### JWT

`jjwt` pode continuar sendo usado para autenticação própria dos
usuários, caso seja o padrão do Fênix3. Não confundir com a autenticação
da Receita:

``` text
JWT do usuário do sistema
          !=
OAuth2 / Bearer Token Receita Integra
```

A integração Receita Integra deve possuir componente próprio.

------------------------------------------------------------------------

## 22. DeRE como pipeline de processamento

O núcleo deve refletir o fluxo:

``` text
EXTRAIR
 -> MAPEAR
 -> VALIDAR REGRAS
 -> GERAR XML
 -> VALIDAR XSD
 -> ASSINAR XML
 -> MONTAR LOTE
 -> TRANSMITIR
 -> RECEBER PROTOCOLO
 -> CONSULTAR PROCESSAMENTO
 -> PROCESSAR RECIBO / REJEIÇÃO
```

Evitar uma classe genérica gigante como `DereService`. Preferir:

``` text
DereEventGenerator
DereXsdValidator
DereXmlSigner
DereBatchBuilder
DereTransmissionClient
DereProcessingClient
DereReceiptProcessor
```

------------------------------------------------------------------------

## 23. Separação domínio x modelo oficial DeRE

Não utilizar diretamente classes geradas do XSD como domínio:

``` text
Modelo de domínio
      ↓
Mapeamento DeRE
      ↓
Modelo XML/XSD oficial
```

Exemplo:

``` text
AccountingAccount
      ↓ MapStruct
D1011EventModel
      ↓
JAXB / classes do schema
      ↓
XML D-1011
```

Isso reduz o impacto de mudanças futuras de leiaute.

------------------------------------------------------------------------

## 24. Versionamento de XSDs e bindings

``` text
src/main/resources/dere/schemas/
├── v1_1_0/
│   └── xsd/
└── nota_2026_001/
    └── xsd/
```

Caso sejam geradas classes JAXB:

``` text
generated/
├── v1_1_0/
└── v1_2_0/
```

Regras:

-   nunca sobrescrever silenciosamente um XSD anterior;
-   registrar a versão usada em cada evento;
-   conseguir reproduzir o XML transmitido;
-   não inferir estruturas XML quando o XSD as define;
-   avaliar geração automática dos bindings JAXB.

------------------------------------------------------------------------

## 25. XML e assinatura

Usar **JAXB/Jakarta XML Binding** para serialização estruturada quando
compatível com os schemas oficiais. Evitar XML por concatenação de
strings.

Usar **Apache Santuario XML Security** para XMLDSig. Não implementar
algoritmos criptográficos ou assinatura XML manualmente.

``` text
Event Model
 -> JAXB
 -> XML
 -> XSD Validator
 -> Apache Santuario
 -> XML assinado
```

Canonicalização, digest, algoritmo de assinatura, localização da
`Signature`, referência/ID e regras do certificado devem seguir
exclusivamente o Manual do Desenvolvedor vigente.

------------------------------------------------------------------------

## 26. Cliente HTTP

Preferir inicialmente o **Spring `RestClient`**. Não introduzir
programação reativa apenas para a DeRE.

Separar:

``` text
ReceitaIntegraAuthClient
  -> obtenção / renovação do Bearer Token

DereTransmissionClient
  -> envio de lotes

DereProcessingClient
  -> consulta de protocolos
```

URLs e credenciais ficam externas ao código.

------------------------------------------------------------------------

## 27. Estratégia de testes

Adicionar **Testcontainers** para MariaDB real nos testes de integração,
migrations Flyway e persistência de eventos/lotes.

Fixtures:

``` text
src/test/resources/dere/
├── valid/
│   ├── d1001.xml
│   └── d1011.xml
├── invalid/
│   ├── missing-required-field.xml
│   ├── invalid-format.xml
│   └── invalid-account.xml
└── responses/
    ├── processing.xml
    ├── accepted.xml
    └── rejected.xml
```

Testes mínimos:

``` text
[ ] geração do XML
[ ] XML válido contra XSD
[ ] XML inválido rejeitado pelo XSD
[ ] assinatura XML válida
[ ] alteração posterior invalida assinatura
[ ] montagem do lote
[ ] parsing de protocolo
[ ] parsing de recibo
[ ] parsing de rejeição
[ ] migrations Flyway
[ ] persistência MariaDB
```

------------------------------------------------------------------------

## 28. Frontend administrativo

O frontend não é necessário para provar a primeira POC. Primeiro
validar:

``` text
D-1001
 -> XML
 -> XSD
 -> XMLDSig
 -> lote
 -> OAuth2
 -> API
 -> protocolo
 -> consulta
 -> recibo/rejeição
```

Depois utilizar React/MUI para:

``` text
Dashboard

Contribuinte
└── D-1001

Plano de Contas
└── D-1011
    └── Conta interna x PREVIC

Competências
├── D-1101
├── D-1106
└── Fechamento

Transmissões
├── Lotes
├── Protocolos
├── Recibos
└── Rejeições

Configuração
├── Ambiente
├── Certificado
└── Receita Integra
```

MUI X Data Grid é adequado para o acompanhamento operacional.

------------------------------------------------------------------------

## 29. Reaproveitamento do Fênix3

Reaproveitar, quando adequado:

-   estrutura base dos projetos;
-   convenções Java/Spring;
-   segurança e autenticação de usuários;
-   tratamento padronizado de erros;
-   logging e observabilidade;
-   Docker e CI/CD;
-   Flyway e MariaDB;
-   componentes React e tema MUI;
-   formulários e grids;
-   padrão de APIs REST;
-   estrutura de testes.

Apesar do reaproveitamento, manter a DeRE isolada como domínio próprio.

------------------------------------------------------------------------

## 30. Estrutura de pacotes revisada

``` text
dere/
├── domain/
│   ├── contributor/
│   ├── accounting/
│   ├── investment/
│   └── transmission/
├── application/
│   ├── d1001/
│   ├── d1011/
│   ├── d1101/
│   └── d1106/
├── integration/
│   ├── receita/
│   │   ├── auth/
│   │   ├── transmission/
│   │   └── processing/
│   ├── xml/
│   ├── xsd/
│   └── signature/
├── persistence/
└── api/
```

------------------------------------------------------------------------

## 31. Sequência técnica atualizada da POC

1.  **Bootstrap:** Java 21, Spring Boot 4.1, MariaDB, Flyway, JUnit 5 e
    Testcontainers. Sem frontend inicialmente.
2.  **Schemas:** baixar, versionar e configurar os XSDs e testes.
3.  **D-1001:** Domain -\> Mapper -\> Event Model -\> JAXB -\> XML -\>
    XSD.
4.  **Assinatura:** ICP-Brasil -\> Apache Santuario -\> XMLDSig -\>
    validação.
5.  **Lote:** evento assinado -\> batch builder -\> XML do lote -\> XSD.
6.  **Receita Integra:** Client Credentials -\> OAuth2 -\> Bearer Token.
7.  **DeRE API:** POST do lote -\> protocolo -\> persistência.
8.  **Consulta:** protocolo -\> processamento -\> recibo/rejeição -\>
    persistência.
9.  **D-1011:** plano de contas -\> PREVIC -\> `codTrib` -\> evento.
10. **Mensais:** D-1101 -\> D-1106 -\> demais aplicáveis -\> D-1199.
11. **Frontend:** somente após o caminho crítico estar comprovado.

------------------------------------------------------------------------

## 32. Decisão tecnológica resumida

**Decisão: utilizar a stack do Fênix3.**

``` text
Stack Fênix3
   +
JAXB
   +
Apache Santuario
   +
Testcontainers
   +
XSD versionado
   +
XMLDSig / ICP-Brasil
   +
OAuth2 Receita Integra
```

A padronização com o Fênix3 é uma vantagem arquitetural e operacional. A
DeRE não justifica, neste momento, introduzir outra linguagem ou
framework backend/frontend.
