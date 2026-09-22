# Autenticação da DeRE no sistema do governo

Documento para a Dataa emitir a credencial de API e o time técnico ligar o sistema à Receita.

Há **duas autenticações**. Sem as duas, o lote não é aceito.

| Camada | Para que serve | Quem resolve agora |
|---|---|---|
| Receita Integra (OAuth 2.0) | Liberar a API: token Bearer para enviar e consultar lotes | Contadora/administradora da Dataa, com certificado e-CAC / gov.br da empresa |
| Certificado ICP-Brasil | Assinar cada XML da DeRE (XMLDSig) | Depois da credencial; homologação usa certificado de homologação |

gov.br e e-CAC autenticam **a pessoa que representa a Dataa**. A credencial da API (`client_id` / `client_secret`) é do **Receita Integra**, não um login genérico do gov.br.

A transmissão da DeRE é por **API**, não por digitação no e-CAC.

Confirmar nomes de tela, URLs e escopos no manual vigente antes de protocolar. A página oficial muda com frequência:

- [DeRE — CGIBS](https://cgibs.gov.br/declaracao-de-regimes-especificos-dere)
- [Manual Receita Integra v1.4](https://cgibs.gov.br/upload/arquivos/202608/14165847-09-manual-de-integracao-tecnica-receita-integra-v-1-4.pdf)
- [Manual do Desenvolvedor DeRE v1.0.2](https://cgibs.gov.br/upload/arquivos/202608/18211952-07-manual-do-desenvolvedor-v-1-0-2.pdf)
- Portal do piloto (Produção Restrita): https://piloto-cbs.tributos.gov.br/

---

## Passo 1 — Emissão da credencial (contadora / administradora Dataa)

Este passo é o único que a administradora precisa executar. O restante é do time de sistema.

Objetivo: gerar `client_id` e `client_secret` da Dataa no ambiente de **Produção Restrita** (piloto) e devolver isso com segurança para o desenvolvimento.

### 1.1 O que ela precisa ter em mãos antes de começar

1. **Certificado digital da Dataa no e-CAC / gov.br**  
   O mesmo que ela já usa para representar a empresa (e-CNPJ A1 ou A3, ou CPF com procuração eletrônica válida). Sem isso o portal não mostra a Dataa como empresa representada.

2. **Confirmar que o certificado ainda vale**  
   Abrir o e-CAC e o gov.br com o certificado. Se pedir senha/PIN do token, ter o PIN. Se o certificado estiver vencido ou revogado, parar e renovar antes.

3. **Confirmar que ela representa a Dataa certa**  
   Anotar CNPJ completo e razão social da empresa que vai declarar a DeRE. Se a Dataa tiver mais de um CNPJ (matriz/filial, prestadora vs entidade), alinhar com o time **qual CNPJ raiz de 8 dígitos** entra no D-1001 (`nrInsc`).

4. **Poder de representação**  
   - Se ela entra com **e-CNPJ da Dataa**: em geral já representa a empresa.  
   - Se ela entra com **CPF** + certificado de pessoa física: precisa de **procuração eletrônica no e-CAC** outorgada pela Dataa, com poder para atos da Reforma Tributária / DeRE / Receita Integra. Sem procuração o portal entra, mas não deixa gerar credencial da empresa.  
   Pedir ao jurídico/contábil: conferir no e-CAC → Procuração Eletrônica se o perfil dela cobre “declarar / transmitir / integrar sistemas” da empresa. Se não cobrir, o outorgante (e-CNPJ da Dataa) emite a procuração **antes** do passo 1.4.

5. **Confirmar participação no piloto**  
   A DeRE em Produção Restrita não é aberta para qualquer CNPJ. A Dataa (ou a EFPC cliente, se a credencial for da entidade e não da prestadora) precisa estar no piloto da Reforma Tributária / CBS / IBS.  
   Se ninguém da Dataa recebeu convite, e-mail ou protocolo de adesão: **não gerar credencial ainda**. Abrir chamado / falar com o contato CGIBS/Receita e só depois seguir.  
   Atenção: se o software é da Dataa e a declaração é da **EFPC cliente**, a credencial e o certificado de assinatura no fim das contas são da **entidade declarante**, não da software house. Neste primeiro teste interno, alinhar se a emissão será:
   - no CNPJ da **Dataa** (teste da prestadora / piloto interno), ou
   - no CNPJ da **EFPC** (cenário real de transmissão).  
   A administradora só consegue emitir no CNPJ que o certificado/procuração representa.

6. **Computador preparado**  
   - Windows com o certificado instalado (A1) ou token/smartcard plugado (A3).  
   - Cadeia ICP-Brasil instalada.  
   - Navegador que o e-CAC aceita (em geral Edge/Chrome com extensão de certificado).  
   - Bloqueador de pop-up desligado para `gov.br`, `receita.fazenda.gov.br` e `tributos.gov.br`.

7. **Onde guardar o segredo**  
   Combinar **antes** com o time técnico o canal seguro: cofre (1Password/Bitwarden), pasta criptografada ou entrega presencial.  
   **Não** mandar `client_secret` por e-mail, WhatsApp, Slack ou print. O secret aparece **uma vez**. Se perder, revoga e gera de novo.

### 1.2 O que ela vai gerar (e o que não vai)

Ela **não** precisa:

- assinar XML;
- instalar o sistema DeRE;
- saber Java, OAuth ou XSD;
- enviar o certificado `.pfx` neste passo (isso é o passo 2).

Ela **vai** gerar:

| Item | O que é | Quem usa depois |
|---|---|---|
| `client_id` | Identificador público da aplicação no Receita Integra | Sistema (variável `DERE_CLIENT_ID`) |
| `client_secret` | Senha da aplicação; equivale à chave da API | Sistema (variável `DERE_CLIENT_SECRET`) |
| Ambiente | Produção Restrita (piloto), não produção | Time técnico |
| Data/hora da emissão | Controle interno | Registro |
| CNPJ representado | Empresa selecionada no portal | Conferência com o D-1001 |
| Protocolo / identificador da aplicação no portal | Se o portal mostrar | Auditoria |

O sistema troca `client_id` + `client_secret` por um **token Bearer** (vale cerca de 60 minutos) e só então chama a API da DeRE.

### 1.3 Roteiro de emissão

Fazer em horário comercial, com o certificado à mão e alguém do time técnico por perto (telefone) para receber o secret na hora.

**A. Entrar como a Dataa**

1. Conectar o certificado / token.
2. Abrir https://www.gov.br/ e autenticar com **certificado digital** (não conta gov.br só de senha, se o portal exigir certificado).
3. Confirmar que o nome/CPF na sessão é o dela.
4. Abrir o [e-CAC](https://cav.receita.fazenda.gov.br/) com o mesmo certificado e ver se a **Dataa aparece como empresa representada**.
5. Se a Dataa não aparecer: parar. É procuração, certificado errado ou CNPJ diferente do esperado.

**B. Abrir o portal do piloto**

1. Acessar https://piloto-cbs.tributos.gov.br/ já autenticada.
2. Selecionar a **empresa representada** (Dataa ou a EFPC, conforme o alinhamento do item 1.1.5).
3. Procurar o módulo de **Receita Integra**, **credenciais de API**, **aplicações** ou **integração de sistemas**. O rótulo exato está no Manual Receita Integra v1.4 — usar o do PDF vigente, não chutar menu antigo.
4. Se o portal recusar a empresa (“não habilitada”, “fora do piloto”, “sem permissão”): anotar a mensagem **completa**, print da tela **sem** dado secreto, e devolver ao time. Não insistir gerando credencial em outro CNPJ.

**C. Criar a aplicação / credencial**

No formulário do portal, preencher de forma identificável:

- Nome da aplicação: `Dataa DeRE — Produção Restrita`
- Descrição: sistema interno de preparação e transmissão da DeRE (EFPC)
- Tipo: **Client Credentials** (máquina para máquina). Não escolher autorização de usuário final, se o portal oferecer as duas.
- Escopos: os que o manual da DeRE / Receita Integra listar para recepção e consulta de lotes. Se houver dúvida, marcar só o que o PDF pedir e fotografar a lista para o time.

Gerar / salvar.

**D. Copiar o secret na hora**

1. O portal mostra o `client_secret` **uma vez**.
2. Copiar para o cofre combinado com o time (ou ditá-lo na ligação e o técnico colar no cofre).
3. Copiar também o `client_id` (esse pode ser anotado no e-mail).
4. Não tirar print do secret e não gravar em Word/planilha na rede.
5. Se fechar a tela sem copiar: revogar aquela credencial e gerar outra. Não inventar o secret.

**E. Registrar o que foi feito**

Preencher a ficha da seção 1.5 e devolver ao time técnico.

### 1.4 Problemas comuns neste passo

| Sintoma | Causa mais provável | O que fazer |
|---|---|---|
| gov.br não lê o certificado | Token não plugado, driver, PIN, cadeia ICP | Testar o mesmo certificado no e-CAC. Se e-CAC funcionar e o piloto não, é o portal. |
| Empresa não aparece | Certificado de outra empresa ou sem procuração | Conferir CNPJ do certificado e procurações no e-CAC. |
| Portal diz que não está no piloto | CNPJ sem adesão | Tratar adesão com CGIBS/Receita antes de gerar credencial. |
| Gera credencial mas o time leva 401 depois | Secret errado, ambiente errado (produção vs restrita), escopo faltando | Revogar, gerar de novo, conferir URL de token do manual. |
| Secret vazou (e-mail, chat) | Tratamento inseguro | Revogar imediatamente e emitir outra. Avisar o time. |

### 1.5 Ficha para devolver ao time técnico

A administradora preenche e entrega junto com o `client_id`. O `client_secret` só no cofre.

```text
Data da emissão:
Nome de quem emitiu:
CPF de quem emitiu (não precisa enviar cópia do certificado):
Empresa / CNPJ representado no portal:
Ambiente: [ ] Produção Restrita (piloto)    [ ] Produção (não usar agora)
Portal usado (URL):
Nome da aplicação no portal:
client_id:
client_secret: (somente no cofre; não preencher neste papel/e-mail)
Escopos selecionados:
Protocolo / id da aplicação no portal (se houver):
Validade ou data de expiração da credencial (se o portal mostrar):
Mensagem de erro, se não conseguiu emitir:
```

O que o time faz com isso: coloca `DERE_CLIENT_ID` e `DERE_CLIENT_SECRET` no servidor de homologação, testa o POST do token e **não** commita no git.

### 1.6 O que a administradora **não** deve fazer

- Gerar credencial de **produção** neste momento.
- Reutilizar `client_secret` de outro sistema (e-Social, EFD-Reinf, etc.). Cada API tem a sua.
- Enviar o `.pfx` ou a senha do certificado da Dataa por e-mail neste passo.
- Publicar o secret em chamado aberto, print ou pasta compartilhada.
- Assumir que “já tenho e-CAC, então a API já está liberada”. e-CAC ≠ token Receita Integra.

---

## Passo 2 — Certificado para assinar o XML (depois da credencial)

Mesmo com `client_id` / `client_secret`, o XML da DeRE precisa de assinatura ICP-Brasil (XMLDSig, RSA-SHA256, enveloped).

O POC hoje assina com certificado **autoassinado de teste**. Isso não vale na Receita.

Para homologação:

- certificado ICP-Brasil de **homologação** da entidade que declara (A1 `.pfx` ou A3/HSM);
- senha/PIN só em cofre;
- CNPJ do certificado alinhado ao `nrInsc` do D-1001;
- se outro CPF assinar: procuração eletrônica com poder para DeRE.

A administradora só precisa apontar **qual certificado** será usado para assinar (o mesmo do e-CAC ou outro A1 de homologação) e quem guarda a senha. A instalação no servidor é do time técnico.

---

## Passo 3 — Ligar o sistema (time técnico)

O código já tem o cliente OAuth (`ReceitaIntegraAuthClient`) e o envio de lote com Bearer.

Variáveis:

```text
DERE_CLIENT_ID=
DERE_CLIENT_SECRET=
RECEITA_INTEGRA_TOKEN_URL=   # confirmar no manual; default atual: https://api.receitafederal.gov.br/token
DERE_BASE_URL=               # confirmar no manual; default atual: https://api.receitafederal.gov.br/prr-dere
DERE_MOCK_ENABLED=false
```

Fluxo que o sistema executa:

1. `POST` no token, `Authorization: Basic base64(client_id:client_secret)`, corpo `grant_type=client_credentials`.
2. Recebe `access_token` (cerca de 60 minutos) e guarda em cache.
3. `POST /v1/recepcao/lotes` e `GET /v1/consulta/lotes/{protocolo}` com `Authorization: Bearer …`.

Testar **só o token** antes de mandar lote. Se o token falhar, não adianta assinar XML.

Não versionar `.pfx`, `.p12`, senhas, `client_secret` nem `.env` real.

---

## Passo 4 — Produção

Só depois do aceite na Produção Restrita:

- nova credencial de **produção** (não reaproveitar a do piloto);
- certificado ICP-Brasil de produção;
- URLs de produção do manual vigente;
- repetir a ficha da seção 1.5.

---

## Referências oficiais

- [Página central da DeRE — CGIBS](https://cgibs.gov.br/declaracao-de-regimes-especificos-dere)
- [Manual de Integração Técnica Receita Integra v1.4](https://cgibs.gov.br/upload/arquivos/202608/14165847-09-manual-de-integracao-tecnica-receita-integra-v-1-4.pdf)
- [Manual do Desenvolvedor DeRE v1.0.2](https://cgibs.gov.br/upload/arquivos/202608/18211952-07-manual-do-desenvolvedor-v-1-0-2.pdf)
- Portal do piloto: https://piloto-cbs.tributos.gov.br/
- e-CAC: https://cav.receita.fazenda.gov.br/
- FAQ DeRE: https://www.gov.br/receitafederal/pt-br/acesso-a-informacao/perguntas-frequentes/sped/dere/dere
