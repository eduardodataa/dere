# Implementação da POC DeRE

## Fonte técnica adotada

Os arquivos em `src/main/resources/dere/schemas` foram baixados da página oficial do CGIBS em 02/09/2026:

- pacote base XSD DeRE 1.1.0;
- pacote XSD da Nota Orientativa 2026.001, priorizado para EFPC/PREVIC;
- schema padrão W3C XMLDSig, referenciado pelos XSDs oficiais.

O leiaute D-1001 confirmado no documento oficial usa o namespace `http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1`; o lote usa `http://www.dere.gov.br/schemas/envioLoteDere/v1_0_1`. Essas versões não devem ser atualizadas sem trocar os XSDs e revisar os testes.

## Fluxo local

O perfil `local` usa H2 em arquivo e `DERE_MOCK_ENABLED=true`. A UI em `frontend/` chama os endpoints mockados e permite acompanhar o XML do lote, protocolo e status. O processamento de consulta é simulado para permitir o teste completo sem credenciais externas.

## Decisões e pendências

- A integração externa não é acionada automaticamente; o fluxo local é mockado.
- Certificado ICP-Brasil, senha, `client_id` e `client_secret` entram somente por configuração/segredo externo.
- O ambiente local desta máquina possui apenas JDK 17; o projeto exige JDK 21 conforme a decisão tecnológica. A validação Maven deve ser executada após instalar/configurar JDK 21.
- D-1011/PREVIC, D-1101, D-1106 e Testcontainers ainda são evolução posterior.

## Referências oficiais

- https://cgibs.gov.br/declaracao-de-regimes-especificos-dere
- https://cgibs.gov.br/upload/arquivos/202608/18211952-07-manual-do-desenvolvedor-v-1-0-2.pdf
- https://cgibs.gov.br/upload/arquivos/202606/22141225-02-leiautes-da-dere-eventos-v-1-1-0.pdf
- https://cgibs.gov.br/upload/arquivos/202608/18211950-dere-nota-orientativa-2026-001-18-08-2026.pdf
