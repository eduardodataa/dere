# Evolução das correções

Atualizado em 22/10/2026.

Controle do que já entrou no código e do que continua aberto. O diagnóstico original, com a evidência de cada risco, está em [auditoria-arquitetura.md](auditoria-arquitetura.md). Os códigos R01–R31 são os dessa auditoria.

Marque aqui quando um item mudar de estado. Não use este arquivo como plano de arquitetura.

## Feito

- [x] **R08 — Consulta agendada.** Lote real consulta a Receita. Lote simulado fica no mock. Cada lote grava em transação própria (`ConsultaLoteAgendada`, `PersistenciaConsultaLote`).
- [x] **R09 — HTTP do envio fora da transação.** O envio é gravado como `RESULTADO_DESCONHECIDO` antes da chamada. Timeout, rede ou 5xx permanecem nesse estado e o mesmo id não é reenviado. Recusa definitiva (400, 401, 403, 404) grava `REJECTED`.
- [x] **R09 — Consulta manual fora da transação.** `query` e `consultarProtocolo` leem e chamam a Receita fora do banco. A gravação do retorno entra numa transação nova e recarrega o lote pelo id.
- [x] **R04 — Certificado do envio real.** Sem certificado A1 da entidade, o envio real responde 422. O certificado de teste só entra no envio simulado.
- [x] **R05 — DOCTYPE.** XML com `<!DOCTYPE` vira crítica e não segue para o XSD nem para a conversão. O validador também bloqueia DTD e schema externos no documento (`XmlSupport`, `ServicoValidacaoLeiaute`).
- [x] **R16 — ZIP.** A entrada é lida em memória contando os bytes já descompactados. A leitura para ao passar de 50 MB, antes de materializar o restante. Vale para `XmlBatchIngestionService` e `D1001ImportService`, pelo `LeitorXmlUpload`. Nome com `..` ou caminho absoluto continua fora do lote. Acima de 10.000 entradas o ZIP é recusado.
- [x] **R17 — Multipart.** `spring.servlet.multipart.max-file-size` e `max-request-size` estão em 50 MB em `application.yml`. Sem isso o padrão do Spring (1 MB) descartava o arquivo antes do teto do código.
- [x] **R01 e R02 — Acesso das APIs, no recorte desta POC.** Login devolve token HMAC. O filtro cobre `/api/**`, exceto `POST /api/auth/login` e `GET /api/ambiente`. O `X-User-Id` enviado pelo cliente é ignorado. Relatório, tentativas, lista de entidades e validações exigem o usuário do token e o vínculo com a entidade.

## Parcial

- [ ] **R26 — Transação do agendador.** O ciclo não é mais uma transação única, então uma falha não desfaz o lote dos outros. O lease da consulta passa a ser visível antes da chamada HTTP.
- [ ] **R27 — Duas instâncias.** O lease commitado ajuda, mas o desenho ainda assume um agendador só. Segunda instância continua dependendo de armazenamento compartilhado e de fila.
- [ ] **R10 — Retry.** 429 e 503 continuam retentáveis. POST com 500, 502 ou 504 não volta a ser retentado: depois de `RESULTADO_DESCONHECIDO`, repetir o POST reabre envio duplo.
- [ ] **R25 — `findAll` no mock.** O agendador não varre mais a tabela inteira. `LocalDereFlowService.query` ainda cai em `findAll` quando o evento simulado antigo não tem `batchId`.

## Aberto — código, sem infraestrutura nova

- [ ] **R11 — Um evento por lote.** Cada envio continua gerando um protocolo. Lote de até 50 eventos é mudança de fluxo, não um ajuste local.
- [ ] **R18 — XSD recompilado a cada validação.** `Schema` pode ser compilado uma vez por arquivo e versão. `Validator` continua um por uso.
- [ ] **R19 — Validação que relê o histórico da entidade.** `ChaveEventoTabelaValidator` e `D1011DependencyValidator` carregam XML antigo da entidade. Fechar isso bem pede coluna de chave e índice. Sem migration nova nesta leva.
- [ ] **R20 — Resposta da importação.** O relatório devolve os XMLs, o CSV e a planilha em base64 na mesma resposta.
- [ ] **R24 — Listagem.** `listar` carrega o evento (com LOB) e o lote, e `resumida()` só apaga o XML depois. Falta uma consulta paginada sem LOB.
- [ ] **R29 — Certificado recarregado no envio.** A chave A1 é lida de novo a cada transmissão.
- [ ] **R21 — CSV inteiro em uma `String`.** `TabelaCsv` parte o texto todo de uma vez.

## Adiado de propósito

- [ ] **R03 — CORS aberto.** O token fica só na memória da página e a interface fala com a mesma origem. Uma allowlist errada quebra o uso local. Mexer quando houver domínio fixo de produção.
- [ ] **R06 — Ambientes no mesmo host e no mesmo banco.** Separação de máquina e de base. Não é correção de código isolada.
- [ ] **R07 — Chave mestra.** Envelope com KMS e rotação. Depende de infraestrutura.
- [ ] **R12 — `Idempotency-Key` da Receita.** O manual não fecha o comportamento. Não inventar retry em cima disso.
- [ ] **R22 — Migrations.** O que falta de schema continua em script manual. Não criar Flyway novo até haver uma decisão de baseline.
- [ ] **R23 — Artefatos em disco local.** `data/transmissoes` não é compartilhado entre instâncias. O destino é armazenamento externo, não pasta temporária.
- [ ] **R28 — Pool de conexões no padrão.** Dimensionar quando a API e o worker estiverem separados.
- [ ] **R30 — Cota por entidade.** Depende da fila e do desenho comercial.
- [ ] **R31 — Métricas, health e correlação.** Actuator e log com id de correlação. Útil, mas não bloqueia o envio.
- [ ] **Fila, worker e lote assíncrono.** Fase 1 da auditoria. O envio síncrono já não segura a transação durante o HTTP. A fila vem depois disso, não no lugar disso.

## ZIP — decisão desta leva

| Limite | Valor |
|---|---|
| Arquivo enviado (multipart) | 50 MB, teto do servidor |
| XML avulso e ZIP já descompactado | parâmetro da tela, padrão 50 MB, máximo 50 MB |
| Quantidade de entradas no ZIP | 10.000 |

O teto que importa contra zip bomb é o dos bytes descompactados, contados enquanto a entrada é lida. Um ZIP pequeno compactado ainda é recusado se a expansão passar de 50 MB. Pasta temporária não entra: arquivo órfão e disco que não é compartilhado entre instâncias.

O `maxAllowedPacket` padrão da URL local é 64 MB. 50 MB de XML cabe. Subir o teto do upload sem subir esse pacote faz a gravação falhar no banco.

## Testes que cobrem esta leva

- `CorrecoesEnvioAcessoIntegrationTest` — token, vínculo, consulta real e simulada, certificado, resultado desconhecido e recusa definitiva.
- `XmlSupportValidationTest.rejeitaDoctypeAntesDoXsd` — DOCTYPE não passa no XSD.
- `LeitorXmlUploadTest` — XML dentro do teto, ZIP misto, nome com `..` e entrada que expande além do limite.
