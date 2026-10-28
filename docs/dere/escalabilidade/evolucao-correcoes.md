# Evolução das correções

Atualizado em 28/10/2026.

Controle do que já entrou no código e do que continua aberto. O diagnóstico original, com a evidência de cada risco, está em [auditoria-arquitetura.md](auditoria-arquitetura.md). Os códigos R01–R31 são os dessa auditoria.

Marque aqui quando um item mudar de estado. Não use este arquivo como plano de arquitetura.

## Feito

- [x] **R08 — Consulta agendada.** Lote real consulta a Receita. Lote simulado fica no mock. Cada lote grava em transação própria (`ConsultaLoteAgendada`, `PersistenciaConsultaLote`).
- [x] **R09 — HTTP do envio fora da transação.** O envio é gravado como `RESULTADO_DESCONHECIDO` antes da chamada. Timeout, rede ou 5xx depois do POST permanecem nesse estado e o mesmo id não é reenviado. Recusa definitiva (400, 401, 403, 404) grava `REJECTED`. Falha antes do POST passa para `AGUARDANDO_ENVIO` e entra no reenvio do lote que não saiu.
- [x] **R09 — Consulta manual fora da transação.** `query` e `consultarProtocolo` leem e chamam a Receita fora do banco. A gravação do retorno entra numa transação nova e recarrega o lote pelo id.
- [x] **R04 — Certificado do envio real.** Sem certificado A1 da entidade, o envio real responde 422. O certificado de teste só entra no envio simulado.
- [x] **R05 — DOCTYPE.** XML com `<!DOCTYPE` vira crítica e não segue para o XSD nem para a conversão. O validador também bloqueia DTD e schema externos no documento (`XmlSupport`, `ServicoValidacaoLeiaute`).
- [x] **R16 — ZIP.** A entrada é lida em memória contando os bytes já descompactados. A leitura para ao passar de 50 MB, antes de materializar o restante. Vale para `XmlBatchIngestionService` e `D1001ImportService`, pelo `LeitorXmlUpload`. Nome com `..` ou caminho absoluto continua fora do lote. Acima de 10.000 entradas o ZIP é recusado.
- [x] **R17 — Multipart.** `spring.servlet.multipart.max-file-size` e `max-request-size` estão em 50 MB em `application.yml`. Sem isso o padrão do Spring (1 MB) descartava o arquivo antes do teto do código.
- [x] **R01 e R02 — Acesso das APIs, no recorte desta POC.** Login devolve token HMAC. O filtro cobre `/api/**`, exceto `POST /api/auth/login` e `GET /api/ambiente`. O `X-User-Id` enviado pelo cliente é ignorado. Relatório, tentativas, lista de entidades e validações exigem o usuário do token e o vínculo com a entidade.
- [x] **Força bruta no login.** Cinco falhas do mesmo usuário, ou trinta do mesmo IP, bloqueiam novas tentativas por 15 minutos. O contador fica na memória desta instância.
- [x] **Reenvio do lote que não saiu.** Se a falha acontece antes do POST, o lote fica `AGUARDANDO_ENVIO` e o agendador reenvia o mesmo XML e o mesmo id quando o token volta. Lote antigo em `RESULTADO_DESCONHECIDO`, sem protocolo, não entra sozinho: o detalhe do envio tem o botão **Reenviar este lote**, que usa o mesmo identificador. Duplicidade, recusa 400/401/403/404, timeout ou resposta sem protocolo tiram o lote desse ciclo.
- [x] **Consulta do token.** Com a Receita respondendo, o painel não pede outro token. Se a resposta for HTTP 429, a próxima tentativa espera 15 minutos. Nas outras falhas, espera 5 minutos. Um envio que falha antes do POST ou com 429 libera uma nova consulta.

## Parcial

- [ ] **R26 — Falha de um lote no agendador.** A cada 30 segundos o agendador pega até 100 lotes em processamento. Antes, os 100 compartilhavam uma transação só: o erro do lote 40 desfazia no banco o que já tinha sido gravado nos lotes 1 a 39. Agora cada lote grava e confirma sozinho. O lote 40 falha, é adiado, e os outros permanecem consultados. A reserva desse lote (60 segundos) também é confirmada no banco antes da chamada à Receita, então outra passagem enxerga que ele já está em consulta. Ainda aberto: se a gravação do adiamento também falhar, o laço para e os lotes seguintes esperam o próximo ciclo. Nada do que já foi confirmado é desfeito.
- [ ] **R27 — Segunda instância.** A reserva confirmada evita que duas passagens consultem o mesmo lote ao mesmo tempo. O resto do desenho ainda assume um processo só. Exemplo: duas instâncias atrás de um balanceador. O XML maior que 400 KB foi gravado no disco da primeira (`data/transmissoes`); a segunda não lê esse arquivo. Um duplo clique pode transmitir o mesmo evento nas duas antes de qualquer uma confirmar o registro. Segunda instância continua fora de operação até haver fila e armazenamento compartilhado.
- [ ] **R10 — Nova tentativa na Receita.** Resposta 429 ou 503 é tratada como indisponibilidade temporária: o POST é repetido até 3 vezes, com uma pausa curta entre elas. Resposta 500, 502 ou 504 não é repetida. Exemplo: o POST sai, a Receita recebe o lote e a conexão cai com 504. O lote fica em `RESULTADO_DESCONHECIDO`. Um segundo POST automático poderia criar outra transmissão do mesmo evento. A nova tentativa, nesse caso, fica para conciliação manual.
- [ ] **R25 — Busca do evento simulado antigo.** No lote novo, o agendador acha o evento pelo `batchId`. No lote simulado gravado antes dessa coluna, o código não tem o vínculo e chama `findAll()`: traz todos os eventos da tabela, com o XML, e procura o id dentro do texto. Exemplo: uma consulta de um lote antigo, com 2.000 eventos na base, lê os 2.000 XML para achar um.

## Aberto — código, sem infraestrutura nova

- [ ] **R11 — Um evento por lote.** Cada transmissão gera um protocolo e uma consulta. O XSD do lote aceita 50 eventos. Exemplo: 50 alterações viram 50 POST e 50 protocolos. No lote de 50, seria um POST e um protocolo, com o resultado de cada evento na consulta. Isso muda o fluxo de envio, não é um ajuste local.
- [ ] **R18 — XSD recompilado.** Cada validação lê o arquivo XSD e monta o schema de novo. Exemplo: validar 1.000 XML do mesmo leiaute recompila o mesmo XSD 1.000 vezes. O schema pode ser montado uma vez por arquivo e versão e reutilizado. O validador em si continua um por uso, porque guarda o erro daquela validação e não é seguro compartilhá-lo entre requisições.
- [ ] **R19 — Histórico relido na validação.** Para saber se a chave `nrInsc` + vigência já foi enviada, o sistema carrega os XML antigos da entidade e abre um por um. Exemplo: a décima inclusão lê os nove XML anteriores; a milésima lê os 999. O atalho é gravar essa chave em colunas indexadas. Nesta leva não entra migration.
- [ ] **R20 — Resposta da importação.** O retorno do ZIP traz, na mesma resposta, cada XML importado, o CSV convertido e a planilha em base64. Exemplo: um ZIP de 10 MB pode voltar com dezenas de MB, porque o conteúdo vai e volta inteiro. A tela precisa do resumo e do link para baixar o relatório, não do arquivo repetido no JSON.
- [ ] **R24 — Lista de transmissões.** Abrir o histórico carrega o XML de cada evento e o do lote, e só depois descarta esse conteúdo para mostrar a linha resumida. Exemplo: 2.000 envios leem cerca de 2.000 XML para exibir protocolo, situação e data. Falta uma consulta paginada só com esses campos.
- [ ] **R29 — Certificado relido no envio.** Cada transmissão decifra o P12 e busca a senha no cofre de novo. Exemplo: dez envios seguidos da mesma entidade leem o mesmo certificado dez vezes, mesmo sem troca no cadastro. A leitura pode ficar em memória até o certificado ser substituído ou vencer.
- [ ] **R21 — CSV inteiro na memória.** `TabelaCsv` recebe o arquivo como um único texto e parte todas as linhas de uma vez. No D-1001 isso é uma linha e não pesa. Exemplo: um CSV de dezenas de MB de outro leiaute ocupa a memória inteira antes da primeira crítica. A leitura linha a linha fica para quando esse arquivo existir de fato.

## Adiado de propósito

- [ ] **R03 — Origem do browser.** A API aceita chamada de qualquer site. O token fica na memória da página, não em cookie, então outro site não o lê sozinho. Exemplo: restringir a origem a `dere.dataa.com.br` agora quebraria o uso em `localhost` e no Vite. A lista de origens entra quando o domínio de produção estiver fechado.
- [ ] **R06 — Ambientes na mesma máquina.** Staging, homologação e produção sobem na mesma EC2 e no mesmo MariaDB. Exemplo: um teste pesado em staging disputa CPU e disco com a produção. Separar é máquina e banco novos, não uma alteração isolada de código.
- [ ] **R07 — Chave que cifra o certificado.** A chave atual é o SHA-256 da string de configuração. Quem tem essa string abre o P12 e o segredo gravados. Exemplo de destino: o KMS guarda a chave e o aplicativo só pede para cifrar e decifrar, com rotação. Sem o KMS ligado na conta, não há o que chamar.
- [ ] **R12 — Cabeçalho de idempotência.** O POST envia `Idempotency-Key` com o id do evento. O manual da Receita não diz o que um segundo POST com a mesma chave faz. O reenvio automático usa essa mesma chave só quando o XML não chegou a sair. Se a resposta falar em duplicidade, o lote sai do ciclo e fica para conciliação. Um POST que pode ter sido recebido continua sem repetição.
- [ ] **R22 — Schema fora do Flyway.** Colunas como `is_master`, `batch_id` e `secret_ref`, e a tabela `dere_app_setting`, foram criadas por script na EC2. Exemplo: um banco novo, só com o Flyway, sobe sem essas colunas e a aplicação quebra no primeiro login ou envio. Migration nova espera um baseline único do que já está em produção.
- [ ] **R23 — XML grande no disco local.** Acima de 400 KB, o XML é gravado em `data/transmissoes`, no disco do processo. Exemplo: o backup do MariaDB não leva esse arquivo, e uma segunda instância não o encontra. O destino é armazenamento externo, como S3. Pasta local não resolve.
- [ ] **R28 — Conexões com o banco.** O pool segue no padrão do Spring Boot, 10 conexões. A chamada à Receita já não segura conexão durante a espera. Exemplo: mudar para 50 agora, com API e consulta no mesmo processo, é chute. O número entra quando a API e o worker estiverem separados e a medição mostrar a fila de espera.
- [ ] **R30 — Limite por entidade.** Não há cota. Exemplo: a entidade A importa milhares de XML enquanto a B envia um D-1001. As duas disputam a mesma CPU e as mesmas conexões. O limite por entidade depende da fila e da franquia comercial.
- [ ] **R31 — Medição e saúde.** Não há métrica de tempo de transmissão, taxa de erro nem fila. O deploy só verifica se a página inicial responde. Exemplo: um lote parado em `RESULTADO_DESCONHECIDO` não gera alerta. Actuator e um id de correlação no log ajudam a operar e não mudam o resultado do envio.
- [ ] **Fila e worker.** O usuário ainda espera a transmissão terminar dentro do próprio pedido HTTP. A gravação do lote já acontece antes da chamada e não é desfeita se a Receita não responder. Exemplo de passo seguinte: o pedido grava o lote, devolve 202 e um worker transmite depois, com limite por entidade. A fila vem depois da correção atual, não no lugar dela.

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
