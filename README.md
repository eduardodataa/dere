# DeRE POC — EFPC/PREVIC

POC local para demonstrar o ciclo de preparação e transmissão de eventos da Declaração de Regimes Específicos (DeRE), com foco inicial em EFPC/PREVIC.

O fluxo local é:

`usuário → entidade → CSV/dados → validação XSD → XML → assinatura XMLDSig → lote → protocolo mockado → consulta → recibo/relatório`

O ambiente externo da Receita não é acionado automaticamente.

## Status da implementação

| Capacidade | Status |
|---|---|
| Modelo e geração D-1001 | Implementado |
| Validação XSD do evento | Implementado |
| Validação XSD do lote | Implementado |
| Assinatura XMLDSig | Implementado para certificado local de teste |
| Verificação pós-assinatura | Implementado |
| Lote XML | Implementado |
| Receita Integra OAuth2 | Cliente implementado, mock não usa credenciais externas |
| Transmissão e consulta reais | Pendente de credenciais e habilitação |
| Transmissão e consulta mockadas | Implementado |
| Usuários e senhas | Implementado com BCrypt |
| Entidades e vínculos | Implementado |
| Auditoria de validações/envios | Implementado no banco |
| Relatório para download | Implementado via API |
| CSV → XML e XML → CSV | Implementado para D-1001 |
| Interface React | Implementada para login, envio e cadastros |
| D-1011/PREVIC, D-1101 e D-1106 | Pendente |

## Stack

- Backend: Java 21, Spring Boot 4.1, Spring Web/RestClient, Spring Data JPA, Hibernate, Flyway.
- Banco: H2 em arquivo no perfil local; MariaDB previsto para homologação/produção.
- XML: XSD oficial, DOM seguro, XMLDSig e Apache Santuario como dependência.
- Segurança de senha: Spring Security Crypto/BCrypt.
- Frontend: React, Vite, TypeScript, MUI e Axios.
- Testes: JUnit 5 e estrutura preparada para Testcontainers.

## Estrutura

```text
dere/
├── frontend/                         # Interface React/Vite
├── examples/                         # Dados CSV demonstrativos
├── scripts/sql/                      # Cargas SQL
├── docs/dere/                        # Decisões e cronograma
├── src/main/java/br/gov/dere/
│   ├── api/                          # Controllers, CORS e seed local
│   ├── application/                  # D-1001, CSV e fluxo mockado
│   ├── domain/                       # Modelos e estados
│   ├── integration/                  # XML, XSD, assinatura e Receita Integra
│   └── persistence/                  # Entidades e repositories JPA
├── src/main/resources/
│   ├── db/migration/                 # Migrations Flyway
│   ├── dere/schemas/                 # XSDs oficiais versionados
│   └── application*.yml
└── pom.xml
```

## Execução local

Requisitos: Java 21, Maven 3.9+, Node.js e npm.

Na raiz do projeto:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
mvn spring-boot:run
```

O perfil `local` usa H2 em arquivo em `./data/dere`, executa as migrations e habilita o fluxo mockado.

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

Acesse [http://localhost:5173](http://localhost:5173).

### Usuário inicial

Há duas opções:

1. O perfil local cria automaticamente `admin`/`admin` e uma entidade de teste.
2. Para a carga apresentada à diretoria, execute [carga-inicial-eduardo.sql](C:\Users\edordeiro\projetos\dere\scripts\sql\carga-inicial-eduardo.sql) após as migrations.

Credenciais da carga:

```text
Login: eduardo
Senha: eduardo86
Entidade: Entidade Teste EFPC
CNPJ raiz: 12345678
```

A senha é persistida somente como hash BCrypt.

## Telas

### Login

Autentica o usuário local. A POC usa o identificador do usuário para demonstrar autorização por entidade; autenticação de produção com sessão/JWT ainda deve ser adicionada.

### Envio D-1001

Permite:

- selecionar a entidade;
- informar a vigência;
- validar, assinar e enviar o evento mockado;
- visualizar XML do lote;
- visualizar protocolo e status;
- consultar o processamento e receber recibo.

### Usuários

Permite cadastrar login, nome e senha. A senha nunca é retornada pela API.

### Entidades

Permite cadastrar CNPJ raiz e razão social. A DeRE usa o CNPJ raiz com oito posições.

### Associações

Permite associar o usuário logado a uma entidade. O backend bloqueia o envio quando não existe vínculo entre usuário e entidade selecionada.

## APIs locais

### Autenticação

```http
POST /api/auth/login
Content-Type: application/json

{"login":"eduardo","password":"eduardo86"}
```

### Administração

```http
GET  /api/admin/users
POST /api/admin/users
GET  /api/admin/entities
POST /api/admin/entities
POST /api/admin/users/{userId}/entities/{entityId}
```

Exemplos de cadastro:

```json
{"login":"maria","password":"senha-temporaria","name":"Maria Contabilidade"}
```

```json
{"cnpjRoot":"87654321","legalName":"Outra Entidade EFPC"}
```

### Validação e conversão

```http
POST /api/validation/csv-to-xml
Content-Type: text/plain
```

```http
POST /api/validation/xml-to-csv
Content-Type: application/xml
```

CSV demonstrativo: [d1001-efpc-valid.csv](C:\Users\edordeiro\projetos\dere\examples\d1001-efpc-valid.csv).

Formato do CSV:

```csv
id,tpOper,tpAmb,verAplic,cnpjRoot,iniValid,fimValid,regTribPrinc,indNatTrib
DeRE10011000000123456782026090212000000006,1,2,dere-poc/0.1,12345678,2026-10-01,,9,0
```

### Fluxo mockado

```http
POST /api/mock/dere/d1001
X-User-Id: 1
X-Entity-Id: 1
Content-Type: application/json
```

```json
{"id":"DeRE10011000000123456782026090212000000006","validFrom":"2026-10-01"}
```

Depois da transmissão:

```http
POST /api/mock/dere/batches/{batchId}/query
```

O primeiro retorno fica em `PROCESSING`; a consulta mockada conclui o lote, salva o recibo e marca o evento como `ACCEPTED`.

### Relatórios

```http
GET /api/reports/{auditId}
```

O relatório é baixado como texto e contém data/hora, validade, mensagens, XML e CSV registrados.

## Fluxo técnico

1. Usuário é autenticado.
2. Entidade é selecionada.
3. Backend confirma o vínculo usuário-entidade.
4. Dados são convertidos para o modelo D-1001.
5. XML sem assinatura é gerado.
6. XML é validado contra o XSD da Nota Orientativa 2026.001.
7. Evento é assinado individualmente.
8. Assinatura é verificada.
9. XML assinado é validado novamente.
10. Lote é montado e validado.
11. Tentativa, usuário, entidade, data/hora e tamanho são auditados.
12. Protocolo mockado é persistido.
13. Consulta assíncrona mockada atualiza o lote.
14. Recibo ou rejeição é persistido.
15. Relatório é disponibilizado para download.

## Banco de dados

Migrations:

- `V1__dere_auditoria.sql`: eventos, lotes e erros.
- `V2__usuarios_entidades_auditoria.sql`: usuários, entidades, vínculos e auditoria.

Tabelas principais:

| Tabela | Finalidade |
|---|---|
| `dere_user` | login, hash da senha e situação do usuário |
| `dere_entity` | CNPJ raiz e razão social |
| `dere_user_entity` | autorização usuário-entidade |
| `dere_event` | XML sem assinatura, assinado e status |
| `dere_batch` | lote, protocolo, retorno e status |
| `dere_event_error` | erros por evento |
| `dere_validation_audit` | histórico de validações e tentativas |

## XSDs e versões

Os schemas oficiais estão versionados sem sobrescrita:

- `src/main/resources/dere/schemas/v1_1_0/xsd`
- `src/main/resources/dere/schemas/nota_2026_001/xsd`

Para EFPC/PREVIC, o pacote da Nota Orientativa 2026.001 é o preferencial enquanto representar a versão implantada no ambiente oficial. A estrutura técnica deve sempre ser conferida no XSD vigente.

## Segurança da assinatura

O fluxo local usa certificado autoassinado efêmero apenas para demonstração. Ele não deve ser usado na Receita.

Para homologação oficial/produção, ainda é necessário implementar o provider de certificado ICP-Brasil:

- A1 PKCS#12 protegido por cofre de segredos, ou A3/HSM;
- chave privada fora do banco, logs e repositório;
- validação da cadeia ICP-Brasil, validade e KeyUsage;
- controle do certificado por entidade;
- rotação, revogação, backup e restore seguro;
- auditoria de uso;
- testes com certificado oficial.

O código já executa assinatura individual, referência ao atributo `id`, RSA-SHA256, SHA-256, assinatura envelopada e verificação pós-assinatura.

## Configuração externa

As URLs e credenciais ficam fora do código:

```yaml
dere:
  environment: restricted-production
  api:
    base-url: ${DERE_BASE_URL}
    token-url: ${RECEITA_INTEGRA_TOKEN_URL}
  auth:
    client-id: ${DERE_CLIENT_ID}
    client-secret: ${DERE_CLIENT_SECRET}
```

Nunca versionar `.pfx`, `.p12`, senhas, `client_secret`, chaves privadas ou arquivos `.env` com valores reais.

## Testes e validação

Frontend:

```powershell
cd frontend
npm run build
```

Backend:

```powershell
mvn test
```

O backend requer Java 21 e Maven 3.9+. Os testes previstos cobrem geração XML, XSD válido/inválido, assinatura, lote, parsing de retorno, migrations e persistência.

## Pendências para homologação e produção

- implementar D-1011 com Tabela 24/PREVIC;
- implementar D-1101 e D-1106;
- substituir certificado local por ICP-Brasil gerenciado;
- adicionar autenticação real com sessão/JWT, perfis e MFA;
- completar auditoria de validações CSV independentes;
- criar histórico visual de lotes, protocolos, recibos e relatórios;
- conectar `DereTransmissionClient` ao ambiente oficial;
- obter credenciais Receita Integra e validar escopos;
- criar infraestrutura isolada de homologação/produção;
- configurar backup, restore, monitoramento, alertas e CI/CD;
- executar testes oficiais e aceite de contabilidade, segurança e diretoria.

O cronograma detalhado está em [CRONOGRAMA_IMPLANTACAO.md](C:\Users\edordeiro\projetos\dere\docs\dere\CRONOGRAMA_IMPLANTACAO.md).

## Referências oficiais

- [Página oficial da DeRE — CGIBS](https://cgibs.gov.br/declaracao-de-regimes-especificos-dere)
- [Manual do Desenvolvedor DeRE v1.0.2](https://cgibs.gov.br/upload/arquivos/202608/18211952-07-manual-do-desenvolvedor-v-1-0-2.pdf)
- [Leiautes dos Eventos DeRE v1.1.0](https://cgibs.gov.br/upload/arquivos/202606/22141225-02-leiautes-da-dere-eventos-v-1-1-0.pdf)
- [Nota Orientativa 2026.001](https://cgibs.gov.br/upload/arquivos/202608/18211950-dere-nota-orientativa-2026-001-18-08-2026.pdf)
- [FAQ DeRE — Receita Federal](https://www.gov.br/receitafederal/pt-br/acesso-a-informacao/perguntas-frequentes/sped/dere/dere)
