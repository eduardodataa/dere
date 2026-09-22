# Implementação da POC DeRE

## Fonte técnica adotada

Os arquivos em `src/main/resources/dere/schemas` foram baixados da página oficial do CGIBS em 02/09/2026:

- pacote base XSD DeRE 1.1.0;
- pacote XSD da Nota Orientativa 2026.001, priorizado para EFPC/PREVIC;
- schema padrão W3C XMLDSig, referenciado pelos XSDs oficiais.

O leiaute D-1001 confirmado no documento oficial usa o namespace `http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1`; o lote usa `http://www.dere.gov.br/schemas/envioLoteDere/v1_0_1`. Essas versões não devem ser atualizadas sem trocar os XSDs e revisar os testes.

## Fluxo local

O perfil `local` usa H2 em arquivo e `DERE_MOCK_ENABLED=true`. A UI em `frontend/` chama os endpoints mockados e permite acompanhar o XML do lote, protocolo e status. O processamento de consulta é simulado para permitir o teste completo sem credenciais externas.

## Estado atual da implementação

### Concluído

- estrutura independente de domínio, aplicação, infraestrutura e API;
- engine de layouts com handlers separados para D-1001 e D-1011;
- modelo canônico compartilhado entre CSV e XML;
- conversão CSV → modelo → XML e XML → modelo → CSV;
- validação de CSV, XML, XSD e dependência D-1011 → D-1001;
- persistência dos eventos, histórico e auditoria;
- armazenamento de tentativas de transmissão com status HTTP, duração, correlação e referências;
- polling persistente com lease, controle de concorrência e backoff;
- proteção da senha do certificado por chave externa, sem retorno pela API e sem logging do segredo;
- fluxo local com certificado de teste, assinatura XMLDSig e transmissão simulada;
- interface React/Vite para dashboard, importação, validações e certificado;
- cliente de autenticação OAuth 2.0 Client Credentials;
- cliente HTTP para os contratos reais documentados:
  - `POST /v1/recepcao/lotes`;
  - `GET /v1/consulta/lotes/{protocolo}`;
  - `201`/`202` para aceitação do lote;
  - `200` para consulta;
  - tratamento de `401`, `429` e respostas `5xx`;
  - cabeçalho de idempotência;
- parsing seguro de protocolo, código, situação e recibo;
- testes Maven e build do frontend executados com sucesso.

### Em andamento ou pendente

1. **Conectar o fluxo de negócio ao adaptador real**

   O adaptador HTTP real está implementado, mas o fluxo local ainda usa `LocalDereFlowService` e o adaptador simulado. Deve ser criada uma configuração explícita para alternar entre mock e ambiente autorizado, sem chamadas externas acidentais.

2. **Confirmar os payloads oficiais**

   A estrutura do lote e os endpoints foram implementados conforme o manual. Ainda é necessário validar, com o pacote XSD oficial vigente e respostas do ambiente autorizado:

   - namespace e versão final de cada evento;
   - estrutura exata do retorno de transmissão;
   - nome dos campos de protocolo, recibo, status e mensagens;
   - regras de lote com múltiplos eventos.

3. **Certificado ICP-Brasil real**

   O fluxo local usa certificado de teste. Antes de homologação, substituir o provider de teste por PFX/P12 ICP-Brasil real ou integração com cofre/HSM, validando validade, cadeia, KeyUsage, rotação, revogação e correspondência com a entidade.

4. **Credenciais e segurança de ambiente**

   Configurar `DERE_CLIENT_ID`, `DERE_CLIENT_SECRET` e `DERE_CERTIFICATE_ENCRYPTION_KEY` exclusivamente por secret externo. O valor `local-poc-key-not-for-production` só pode permanecer no perfil local.

5. **Frontend de transmissão**

   Substituir a indicação de “Mock local” por uma experiência que mostre ambiente, protocolo, status, recibo, tentativas, rejeições e detalhes de erro por evento.

6. **Testes de integração**

   Criar testes com servidor HTTP simulado para `201`, `202`, `200`, `401`, `403`, `404`, `429`, `500`, `502`, `503`, `504`, timeout e interrupção após envio. Também devem ser incluídos fixtures XML oficiais e teste de assinatura com certificado de homologação.

7. **Homologação ponta a ponta**

   Depende de credenciais autorizadas, certificado válido, ambiente disponível, XSD aprovado e dados anonimizados. Nenhuma transmissão real foi executada automaticamente.

## Decisões e pendências

- A integração externa não é acionada automaticamente; o fluxo local é mockado.
- Certificado ICP-Brasil, senha, `client_id` e `client_secret` entram somente por configuração/segredo externo.
- O perfil `local` possui um segredo determinístico de POC apenas para permitir testes sem configuração adicional; ele não deve ser usado fora do ambiente local. O perfil padrão exige `DERE_CERTIFICATE_ENCRYPTION_KEY`.
- O projeto exige JDK 21. A compilação e os testes Maven já foram executados com sucesso no ambiente configurado.
- D-1011/PREVIC, D-1101, D-1106 e Testcontainers ainda são evolução posterior.

## Referências oficiais

- https://cgibs.gov.br/declaracao-de-regimes-especificos-dere
- https://cgibs.gov.br/upload/arquivos/202608/18211952-07-manual-do-desenvolvedor-v-1-0-2.pdf
- https://cgibs.gov.br/upload/arquivos/202606/22141225-02-leiautes-da-dere-eventos-v-1-1-0.pdf
- https://cgibs.gov.br/upload/arquivos/202608/18211950-dere-nota-orientativa-2026-001-18-08-2026.pdf
