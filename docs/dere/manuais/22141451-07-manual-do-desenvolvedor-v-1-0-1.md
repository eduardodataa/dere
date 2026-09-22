**CGIBS** Comitê Gestor do IBS 

**RECEITA FEDERAL** Ministério da Fazenda 

# **DeRE** 

Declaração de Regimes Específicos Manual do Desenvolvedor 





**Manual de Orientação aos Desenvolvedores** 

**Versão 1.0.1  |  22 de junho de 2026** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **SUMÁRIO** 

|**1 INTRODUÇÃO........................................................................................................................................................................................3**|
|---|
|1.1 Objetivo............................................................................................................................................................................................3|
|**1.2 VISÃO GERAL DA ARQUITETURA....................................................................................................................................................3**|
|**1.3 AUTENTICAÇÃO E SEGURANÇA (RECEITA INTEGRA).................................................................................................................4**|
|**1.3.1 CAMADA DE TRANSPORTE E CRIPTOGRAFIA (TLS).................................................................................................................4**|
|**1.4 FLUXO DE TRANSMISSÃO DOS LOTES DE EVENTOS.................................................................................................................5**|
|**1.5 FLUXO DE PROCESSAMENTO DOS LOTES DE EVENTOS...........................................................................................................6**|
|**1.6 FLUXO DE CONSULTA DOS RESULTADOS DO PROCESSAMENTO............................................................................................7**|
|**2. CONCEITOS FUNDAMENTAIS...........................................................................................................................................................10**|
|**3. APIS DA DERE.....................................................................................................................................................................................11**|
|**3.1 API DE TRANSMISSÃO DE LOTES.................................................................................................................................................11**|
|Endpoint e URLBASE..........................................................................................................................................................................11|
|Cabeçalhos da Requisição..................................................................................................................................................................11|
|Corpo da Requisição............................................................................................................................................................................11|
|Fluxo de Utilização...............................................................................................................................................................................12|
|Obtenção do_Token_de Acesso............................................................................................................................................................12|
|Exemplo de Transmissão.....................................................................................................................................................................13|
|**3.2 API DE CONSULTA DO RESULTADO DO PROCESSAMENTO.....................................................................................................13**|
|Endpoint...............................................................................................................................................................................................14|
|Cabeçalhos da Requisição..................................................................................................................................................................14|
|Parâmetros da Requisição...................................................................................................................................................................14|
|Fluxo de Utilização...............................................................................................................................................................................14|
|Resultado da Consulta.........................................................................................................................................................................15|
|Tratamento dos Resultados.................................................................................................................................................................15|
|**3.3 BOAS PRÁTICAS NA CONSULTA DO RESULTADO DO PROCESSAMENTO.............................................................................16**|
|**4. REGRAS GERAIS DE INTEGRAÇÃO................................................................................................................................................17**|
|**4.1 VALIDAÇÃO DOS XMLS (SCHEMAS XSD).....................................................................................................................................17**|
|Objetivo da Validação..........................................................................................................................................................................18|
|Versionamento dos Esquemas XML....................................................................................................................................................18|
|Utilização do Namespace....................................................................................................................................................................19|
|Evolução dos Leiautes.........................................................................................................................................................................19|
|**4.2 CERTIFICADOS DIGITAIS ACEITOS (ICP-BRASIL).......................................................................................................................19**|
|Requisitos do Certificado Digital..........................................................................................................................................................19|
|Tipos de Certificados Aceitos...............................................................................................................................................................20|
|Identificação do Assinante...................................................................................................................................................................20|
|**4.3 ASSINATURA DIGITAL DOS EVENTOS (****_XMLDSIG_)......................................................................................................................20**|
|Elemento Assinado..............................................................................................................................................................................20|
|Padrão de Assinatura...........................................................................................................................................................................21|
|Requisitos Técnicos da Assinatura......................................................................................................................................................21|
|Transformações Obrigatórias..............................................................................................................................................................22|
|Estrutura do Certificado na Assinatura................................................................................................................................................22|
|**4.4 TRATAMENTO DE ERROS E RETENTATIVAS................................................................................................................................22**|
|Retentativas.........................................................................................................................................................................................22|
|Tratamento dos Códigos de Respostas HTTP....................................................................................................................................23|
|Códigos de Erros de Negócio..............................................................................................................................................................24|
|**4.5 LIMITES OPERACIONAIS.................................................................................................................................................................24**|
|**4.6 AMBIENTE DE PRODUÇÃO RESTRITA..........................................................................................................................................25**|
|Limitações do Ambiente de Produção Restrita....................................................................................................................................25|
|Limpeza Periódica dos Dados do Ambiente........................................................................................................................................26|



**Página 2 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **1 INTRODUÇÃO** 

## **1.1 Objetivo** 

Este manual tem por objetivo fornecer as orientações técnicas necessárias para o desenvolvimento de soluções que realizem a geração, assinatura, transmissão, recepção e tratamento dos eventos da Declaração de Regimes Específicos (DeRE). 

O documento apresenta os conceitos fundamentais da arquitetura da DeRE, as regras gerais de integração, os fluxos de processamento dos eventos, os mecanismos de retorno e as orientações para utilização dos leiautes e serviços disponibilizados pelo ambiente da DeRE. 

A arquitetura adotada pela DeRE segue os mesmos princípios utilizados em outros sistemas do Sistema Público de Escrituração Digital (SPED), tais como o eSocial, a e- Financeira e a EFD-Reinf, utilizando uma abordagem baseada em eventos eletrônicos estruturados em XML, transmitidos por meio de APIs e processados de forma assíncrona. 

## **1.2 Visão Geral da Arquitetura** 

A DeRE utiliza uma arquitetura orientada a eventos para recepção e processamento das informações declaradas pelos contribuintes. 

As informações são organizadas em eventos eletrônicos estruturados conforme os leiautes oficiais da declaração. Cada evento representa um conjunto específico de informações e pode possuir dependências em relação a outros eventos previamente transmitidos. 

O fluxo operacional da DeRE pode ser resumido nas seguintes etapas: 

1. Geração do evento em formato XML conforme o leiaute oficial; 

2. Assinatura digital do evento utilizando certificado ICP-Brasil; 

3. Agrupamento dos eventos em lote para transmissão; 

4. Envio do lote para o ambiente da DeRE por meio das APIs disponibilizadas; 

5. Recebimento imediato de um protocolo de recepção do lote; 

6. Processamento assíncrono dos eventos pelo ambiente da DeRE; 

7. Aplicação das validações estruturais e regras de negócio; 

8. Disponibilização dos resultados de processamento e dos respectivos recibos dos eventos. 

O processamento assíncrono permite que a recepção dos arquivos ocorra de forma rápida e escalável, desacoplando o envio dos eventos da execução das validações mais complexas. Dessa forma, o recebimento de um protocolo não significa que os eventos 

**Página 3 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

foram aceitos, sendo necessária a consulta posterior dos resultados de processamento para obtenção dos recibos e da situação definitiva de cada evento. 

Os conceitos, nomenclaturas e padrões operacionais apresentados neste manual foram concebidos para manter alinhamento com a experiência já consolidada pelos desenvolvedores em integrações com sistemas do SPED, reduzindo a curva de aprendizado e facilitando a implementação das soluções integradas à DeRE. 

## **1.3 Autenticação e Segurança (Receita Integra)** 

O acesso às APIs do ambiente da DeRE é restrito e protegido por mecanismos de autenticação e autorização centralizados no sistema Receita Integra. 

A integração utiliza o protocolo **OAuth 2.0** (fluxo _Client Credentials_ ), exigindo que a aplicação consumidora obtenha previamente um token de acesso e envie um _Bearer Token_ válido no cabeçalho de todas as requisições realizadas às APIs da DeRE, incluindo operações de recepção de lotes, consultas de processamento e demais serviços disponibilizados pelo ambiente da DeRE. 

A autenticação da aplicação consumidora é realizada por meio de credenciais específicas fornecidas pelo sistema Receita Integra, compostas por identificadores e senhas de acesso próprios da integração. 

Importante destacar que a autenticação das APIs e a assinatura digital dos eventos possuem finalidades distintas. A autenticação via Receita Integra controla o acesso aos serviços disponibilizados, enquanto a assinatura digital dos eventos é responsável por garantir a autenticidade, a integridade e a validade jurídica das informações transmitidas. 

Para detalhes técnicos sobre a obtenção de credenciais ( _client_id e client_secret_ ), fluxos de geração e renovação de _tokens_ , gerenciamento de escopos e regras de segurança aplicáveis aos ambientes de homologação e produção, o desenvolvedor deve consultar o documento específico "Manual de Integração Técnica – Receita Integra". 

## **1.3.1 Camada de Transporte e Criptografia (TLS)** 

Toda a comunicação entre as aplicações dos contribuintes e os _endpoints_ da DeRE e do Receita Integra é realizada obrigatoriamente sob o protocolo HTTPS, utilizando _TLS (Transport Layer Security)_ para garantir a confidencialidade e a integridade dos dados em trânsito. 

Para estabelecer uma conexão segura, o desenvolvedor deve observar as seguintes especificações técnicas: 

- **Versão do Protocolo:** É exigida a utilização do protocolo TLS na versão **1.2 ou superior.** Versões legadas, como SSL 2.0/3.0 e TLS 1.0/1.1, não são suportadas devido a vulnerabilidades conhecidas. 

**Página 4 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

- **_Cipher Suites_ (Suítes de Criptografia)** : O ambiente prioriza o uso de suítes de criptografia fortes que ofereçam _Forward Secrecy (PFS)_ e algoritmos de criptografia autenticada (AEAD). Embora a lista exata possa ser atualizada conforme padrões de segurança da ICP-Brasil, recomenda-se a configuração de suítes modernas, como: 

- TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 

- TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384 

- **Certificados de Servidor:** A validade do certificado do servidor ( _server side_ ) será verificada pela aplicação do contribuinte durante o _handshake TLS_ . Os certificados de serviço da DeRE são emitidos por autoridades certificadoras confiáveis. 

## **1.4 Fluxo de Transmissão dos Lotes de Eventos** 

O envio de informações para a DeRE é realizado por meio da transmissão de eventos eletrônicos estruturados em XML, conforme os leiautes oficiais publicados para cada versão do sistema. 

Antes da transmissão, o contribuinte deve gerar os arquivos XML correspondentes aos eventos que deseja enviar, observando as regras de preenchimento, validações e dependências previstas neste manual e nos respectivos leiautes. 

Cada evento deve ser assinado digitalmente com certificado válido emitido no âmbito da Infraestrutura de Chaves Públicas Brasileira (ICP-Brasil), garantindo a autenticidade, integridade e autoria das informações transmitidas. 

Antes de realizar qualquer chamada às APIs da DeRE, a aplicação consumidora deve autenticar-se junto ao sistema Receita Integra e obter um _token_ de acesso válido, que deverá ser informado no cabeçalho da requisição utilizando o padrão _Bearer Token._ 

Após a obtenção do _token_ e a assinatura dos eventos, os arquivos XML são agrupados em lote e transmitidos para o ambiente da DeRE por meio das APIs de recepção. 

Durante a recepção do lote, são realizadas validações iniciais relacionadas à estrutura da requisição, integridade das informações e validade da assinatura digital. Caso essas validações sejam concluídas com sucesso, o sistema retorna um protocolo de recebimento, que identifica unicamente o lote transmitido. 

O protocolo confirma apenas o recebimento do lote pelo ambiente da DeRE, não representando a aceitação definitiva dos eventos nele contidos. 

**Página 5 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 



## **1.5 Fluxo de Processamento dos Lotes de Eventos** 

Após a recepção do lote, os eventos são encaminhados para processamento interno no ambiente da DeRE. 

A DeRE utiliza um modelo de processamento assíncrono, no qual a validação detalhada dos eventos ocorre de forma desacoplada da etapa de transmissão. Essa abordagem permite maior escalabilidade, disponibilidade e capacidade de processamento do ambiente. 

Durante o processamento são executadas, entre outras, as seguintes verificações: 

- Validação da estrutura XML conforme os esquemas oficiais; 

**Página 6 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

- Verificação da assinatura digital; 

- Validação cadastral do transmissor; 

- Verificação das dependências entre eventos; 

- Aplicação das regras de negócio previstas nos leiautes; 

- Validação de tabelas e códigos de domínio; 

- Verificação de unicidade, vigência e consistência das informações. 

Os eventos de um lote são processados individualmente. Dessa forma, a aceitação ou rejeição de um evento não implica necessariamente o mesmo resultado para os demais eventos pertencentes ao mesmo lote. 

Ao término do processamento, o sistema gera o resultado individual de cada evento, contendo sua situação de processamento e, quando aplicável, as respectivas mensagens de erro, aviso ou sucesso. 

## **1.6 Fluxo de Consulta dos Resultados do Processamento** 

Como o processamento dos eventos ocorre de forma assíncrona, o resultado definitivo não é disponibilizado no momento da transmissão do lote. 

Para consultar o andamento ou o resultado do processamento, a aplicação consumidora deve autenticar-se previamente junto ao sistema Receita Integra e obter um _token_ de acesso válido para utilização nas APIs de consulta. 

Após a autenticação, o contribuinte deve utilizar o protocolo de recebimento do lote para consultar sua situação por meio dos serviços disponibilizados pelo ambiente da DeRE. 

Durante a consulta, poderão ser identificadas diferentes situações para os eventos transmitidos, tais como: 

- Em processamento; 

- Processado com sucesso; 

- Processado com advertências; 

- Rejeitado por erro de validação. 

Quando o processamento for concluído com sucesso, será disponibilizado o respectivo recibo do evento, que constitui a comprovação definitiva do seu processamento pelo ambiente da DeRE. 

**Página 7 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

Caso sejam identificadas inconsistências, o resultado da consulta apresentará os códigos e descrições das ocorrências encontradas, permitindo ao contribuinte corrigir as informações e realizar nova transmissão, quando aplicável. 



**Página 8 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 



**Página 9 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **2. CONCEITOS FUNDAMENTAIS** 

A seguir são apresentados os principais conceitos utilizados neste manual. 

|**Conceito**|**Descrição**|
|---|---|
|**Evento**|Unidade básica de informação transmitida à DeRE, representada<br>por um arquivo XML estruturado conforme leiaute oficial.|
|**Lote**|Agrupamento de um ou mais eventos transmitidos em uma única<br>requisição para o ambiente da DeRE.|
|**Protocolo**|Identificador gerado após a recepção de um lote. Confirma apenas<br>o recebimento da transmissão.|
|**Recibo**|Comprovante emitido após o processamento bem-sucedido de um<br>evento. Representa a aceitação definitiva da informação.|
|**Processamento**<br>**Assíncrono**|Modelo em que o lote é recebido inicialmente e processado<br>posteriormente, sendo necessário consultar o resultado para<br>verificar a situação dos eventos.|
|**Evento de Tabela**|Evento utilizado para manutenção de informações cadastrais ou<br>estruturais, controladas por vigência temporal.|
|**Evento Periódico**|Evento associado a um período de apuração específico, utilizado<br>para envio de informações periódicas da declaração.|
|**Evento Transacional**|<sup>Evento destinado ao envio de informações detalhadas sobre</sup><br>operações ou transações realizadas pelo contribuinte.|
|**Evento de Retorno**|Evento gerado pela DeRE para comunicar o resultado do<br>processamento dos eventos transmitidos.|
|**Vigência**|Período de validade de uma informação, definido por datas de início<br>e fim de vigência. Aplicável principalmente aos eventos de tabela.|
|**Retificação**|Procedimento utilizado para corrigir informações transmitidas<br>anteriormente à DeRE.|
|**Receita Integra**|Plataforma responsável pela autenticação e autorização de acesso|



**Página 10 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

às APIs da DeRE por meio do protocolo OAuth 2.0. 

|**Bearer Token**|Credencial de acesso obtida junto ao Receita Integra e utilizada<br>para autenticar as chamadas às APIs da DeRE.|
|---|---|



Os conceitos apresentados neste capítulo têm caráter introdutório. As regras específicas de utilização de cada elemento serão detalhadas nos capítulos posteriores deste manual. 

## **3. APIS DA DERE** 

## **3.1 API DE TRANSMISSÃO DE LOTES** 

A API de transmissão de lotes permite o envio de eventos da DeRE para processamento. 

Os eventos devem ser previamente gerados conforme os leiautes oficiais, assinados digitalmente e agrupados em um lote XML antes da transmissão. 

A autenticação deve seguir o padrão Bearer Token detalhado na Seção 1.3 e no manual específico do Receita Integra. 

## **Endpoint e URLBASE** 

```
A URLBASE das APIs é https://api.receitafederal.gov.br/prr-dere
```

```
POST URLBASE/v1/recepcao/lotes
```

```
Ou seja, a URL completa em Produção Restrita é:
```

```
https://api.receitafederal.gov.br/prr-dere/v1/recepcao/lotes
```

## **Cabeçalhos da Requisição** 

|**Cabeçalho**||**Valor**|
|---|---|---|
|Content-Type|application/xml||
|Authorization|Bearer TOKEN|_RECEITA_INTEGRA|



## **Corpo da Requisição** 

O corpo da requisição deve conter um documento XML representando o lote de eventos a ser transmitido. O lote poderá conter um ou mais eventos, observadas as regras de dependência e precedência definidas para cada tipo de evento. 

**Página 11 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **Fluxo de Utilização** 

A transmissão de um lote é composta pelas seguintes etapas: 

1. Obter um _token_ de acesso junto ao sistema Receita Integra; 

2. Gerar os eventos XML conforme os leiautes oficiais da DeRE; 

3. Assinar digitalmente os eventos; 

4. Agrupar os eventos em um lote XML; 

5. Enviar o lote utilizando a API de transmissão; 

6. Armazenar o protocolo retornado para posterior consulta do resultado do processamento. 

## **Obtenção do** **_Token_ de Acesso** 

Antes de transmitir qualquer lote, a aplicação deve obter um _token_ de acesso utilizando suas credenciais cadastradas no Receita Integra. 

Exemplo utilizando curl: 

```
curl --location 'https://api.receitafederal.gov.br/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--user "CLIENT_ID:CLIENT_SECRET" \
```

```
--data-urlencode 'grant_type=client_credentials'
```

Exemplo de resposta: 

```
{
```

```
  "expires_in": 3600,
```

```
  "token_type": "bearer",
```

```
  "access_token": "TOKEN_RETORNADO"
```

```
}
```

Onde: 

|**Campo**|**Descrição**|
|---|---|
|expires_in|Tempo de validade do_token_em segundos|
|token_type|Tipo do_token_retornado|
|access_token|_Token_de acesso utilizado nas chamadas às APIs da DeRE|



**Página 12 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **Exemplo de Transmissão** 

Após a obtenção do _token_ , o lote poderá ser transmitido utilizando o _endpoint_ de transmissão. 

Exemplo simplificado: 

```
curl -X POST 'URLBASE/v1/recepcao/lotes' \
```

- `-H 'Content-Type: application/xml' \` 

- `-H 'Authorization: Bearer TOKEN_RETORNADO' \` 

- `-d '@lote.xml'` 

## Onde: 

- URLBASE corresponde ao endereço do ambiente utilizado (produção restrita ou produção); 

- TOKEN_RETORNADO corresponde ao _token_ obtido junto ao Receita Integra; 

- lote.xml corresponde ao conteúdo do arquivo XML contendo o lote de eventos da DeRE. 

## **Resposta da Transmissão** 

Após a recepção da requisição, o sistema da DeRE realizará validações iniciais de autenticação, integridade da mensagem e estrutura do lote. 

Caso a transmissão seja aceita, será retornado um protocolo de recebimento do 

lote. 

O protocolo deve ser armazenado pela aplicação, pois será utilizado posteriormente para consulta da situação do processamento. 

**Importante** : O protocolo confirma apenas o recebimento do lote pelo ambiente da DeRE. O resultado definitivo do processamento dos eventos deverá ser obtido posteriormente por meio da API de Consulta do Resultado do Processamento. 

## **3.2 API DE CONSULTA DO RESULTADO DO PROCESSAMENTO** 

A API de Consulta do Resultado do Processamento permite acompanhar a situação dos lotes transmitidos para a DeRE e obter o resultado do processamento dos eventos neles contidos. 

A consulta é realizada a partir do número do protocolo retornado pela API de transmissão de lotes. 

Assim como nas demais APIs da DeRE, a autenticação é realizada por meio de _token_ de acesso obtido junto ao sistema Receita Integra, detalhado na Seção 1.3 e no manual específico do Receita Integra. 

**Página 13 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **Endpoint** 

GET URLBASE/v1/consulta/lotes/{protocolo} 

## **Em Produção Restrita:** 

## **`GET`** 

```
https://api.receitafederal.gov.br/prr-dere/v1/consulta/lotes/{prot
ocolo}
```

## **Cabeçalhos da Requisição** 

|**Cabeçalho**|**Valor**|
|---|---|
|Authorization Bearer TOKEN_|RECEITA_INTEGRA|



## **Parâmetros da Requisição** 

|**Parâmetro**|**Descrição**|
|---|---|
|protocolo|Número do protocolo obtido na transmissão do lote.|



## **Fluxo de Utilização** 

A consulta do resultado do processamento é composta pelas seguintes etapas: 

1. Obter um _token_ de acesso válido junto ao Receita Integra; 

2. Informar o protocolo retornado pela transmissão do lote; 

3. Executar a consulta da situação do processamento; 

4. Interpretar o resultado retornado para cada evento do lote; 

5. Armazenar os recibos dos eventos processados com sucesso. 

## **Obtenção do Token de Acesso:** 

Para obter um _token_ de acesso, consulte o procedimento descrito na seção **API de Transmissão de Lotes** . 

## **Exemplo de Consulta** 

```
curl -X GET 'URLBASE/v1/consulta/lotes/{protocolo}' \
-H 'Authorization: Bearer TOKEN_RETORNADO'
```

Onde: 

- **URLBASE** corresponde ao endereço do ambiente utilizado (produção restrita ou produção); 

**Página 14 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

- **protocolo** corresponde ao identificador retornado pela API de transmissão de Lotes; 

- **TOKEN_RETORNADO** corresponde ao _token_ obtido junto ao Receita Integra. 

## **Resultado da Consulta** 

A consulta retorna a situação atual do lote e dos eventos nele contidos. 

Como a DeRE utiliza processamento assíncrono, o resultado da consulta poderá variar de acordo com o estágio de processamento do lote. 

De forma geral, poderão ocorrer as seguintes situações: 

|**Situação**|**Descrição**|
|---|---|
|Em processamento|O lote foi recebido, porém o processamento ainda não foi<br>concluído.|
|Processado com<br>sucesso|Todos os eventos do lote foram validados e aceitos pela DeRE.|
|Processado com erros|O lote foi processado, porém foram identificados erros em um<br>ou mais eventos do lote. Verifique as ocorrências de erro no<br>arquivo de retorno.|
|Rejeitado|Foram identificados erros que impediram o processamento do<br>evento. Verifique as ocorrências de erro.|



## **Tratamento dos Resultados** 

O sistema consumidor deve analisar individualmente o resultado do processamento de cada evento retornado pela consulta. 

## **Lote processado com sucesso** 

Quando um evento for processado com sucesso, será disponibilizado o respectivo recibo de processamento. 

O recibo constitui a comprovação da aceitação do evento pelo ambiente da DeRE e deverá ser armazenado pelo sistema transmissor para utilização em consultas futuras, retificações e demais operações previstas nos leiautes. 

## **Lote processado com erros** 

O lote foi processado, porém foram identificados erros em um ou mais eventos do lote. É necessário verificar no arquivo de retorno quais eventos foram processados e quais possuem erros. 

**Página 15 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

Quando um evento for rejeitado, o retorno apresentará as ocorrências identificadas durante as validações. 

As ocorrências são compostas por códigos e descrições que permitem identificar a regra violada e a causa da rejeição. 

Nessa situação, o sistema deverá: 

1. Identificar os erros informados; 

2. Corrigir as informações do evento; 

3. Gerar novo evento com nova numeração para o campo _id_ , observando as regras aplicáveis; 

4. Realizar nova transmissão. 

## **Lote rejeitado** 

Quando o lote for rejeitado, nenhum evento foi tratado. No arquivo de retorno haverá a descrição do erro que precisará ser corrigido. Então deverá ser gerado um novo lote e transmitido para o ambiente da DeRE. 

## **Boas Práticas** 

Recomenda-se que os sistemas integrados: 

- Armazenem o protocolo retornado na transmissão do lote; 

- Armazenem os recibos dos eventos processados com sucesso; 

- Implementem mecanismo de consulta periódica para lotes em processamento; 

- Registrem as mensagens de erro e advertência retornadas pela DeRE; 

- Mantenham rastreabilidade entre eventos transmitidos, protocolos e recibos recebidos. 

## **Importante:** 

O protocolo retornado na transmissão apenas confirma o recebimento do lote pelo ambiente da DeRE. 

## **3.3 BOAS PRÁTICAS NA CONSULTA DO RESULTADO DO PROCESSAMENTO** 

A confirmação definitiva do processamento dos eventos somente ocorre após a consulta do resultado e a emissão dos respectivos recibos. 

Como a DeRE utiliza processamento assíncrono, recomenda-se que a aplicação aguarde um intervalo mínimo antes de realizar a primeira consulta do resultado de processamento após a transmissão do lote. 

**Página 16 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

Consultas realizadas imediatamente após o envio possuem maior probabilidade de retornar o status "Em processamento", não agregando valor ao fluxo da aplicação e gerando consumo desnecessário dos recursos computacionais do ambiente da DeRE. 

Além disso, as APIs da DeRE poderão adotar mecanismos de limitação de requisições ( _rate limit_ ) para garantir a disponibilidade e estabilidade dos serviços. Em função disso, as aplicações consumidoras devem evitar consultas repetitivas em intervalos muito curtos. 

Recomenda-se que: 

- Seja aguardado um intervalo mínimo entre a transmissão do lote e a primeira consulta de processamento; 

- Seja adotado um intervalo entre consultas sucessivas para lotes ainda em processamento; 

- Não sejam implementados ciclos contínuos de consulta ( _busy waiting_ ); 

- Seja utilizado mecanismo de retentativa gradual ( _backoff_ ) para consultas repetidas; 

- Sejam respeitados os limites de utilização eventualmente definidos para cada ambiente. 

Exemplo de estratégia recomendada: 

1. Transmitir o lote; 

2. Aguardar um período inicial antes da primeira consulta; 

3. Caso o lote permaneça em processamento, aguardar novo intervalo antes da próxima consulta; 

4. Repetir o procedimento até a conclusão do processamento ou até atingir o limite de tentativas definido pela aplicação. 

Essa abordagem contribui para uma utilização mais eficiente das APIs e reduz a probabilidade de bloqueios decorrentes da aplicação de políticas de controle de tráfego. 

## **4. REGRAS GERAIS DE INTEGRAÇÃO** 

## **4.1 VALIDAÇÃO DOS XMLS (SCHEMAS XSD)** 

Os eventos transmitidos para a DeRE devem ser gerados em formato XML e seguir rigorosamente a estrutura definida pelos esquemas XML ( _XML Schema Definition – XSD_ ) vigentes. Os esquemas oficiais da DeRE serão disponibilizados nos portais abaixo e deverão ser utilizados pelas aplicações na validação prévia dos eventos antes da transmissão: 

**Página 17 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

I - Portal do Sistema Público de Escrituração Digital - SPED, no sítio eletrônico da Secretaria Especial da Receita Federal do Brasil (https://www.gov.br/sped); e 

II - Portal Nacional do IBS e da CBS, no sítio eletrônico do Comitê Gestor do IBS <u>(https://cgibs.gov.br/).</u> 

Os arquivos XSD constituem a especificação técnica oficial da estrutura dos eventos e são utilizados para validar elementos, atributos, tipos de dados, cardinalidade, obrigatoriedade dos campos e demais restrições estruturais dos documentos XML. 

## **Objetivo da Validação** 

A validação dos eventos contra os esquemas oficiais permite identificar inconsistências estruturais antes da transmissão ao ambiente da DeRE, reduzindo rejeições e melhorando a qualidade das informações enviadas. 

A validação contra os esquemas XSD verifica exclusivamente a conformidade estrutural do XML. A aprovação na validação estrutural não garante a aceitação do evento pela DeRE, uma vez que o processamento também contempla validações de assinatura digital, consistência cadastral, dependências entre eventos e regras de negócio previstas nos leiautes da declaração. 

Recomenda-se que toda aplicação realize a validação dos arquivos XML localmente antes da assinatura digital e da transmissão dos eventos. 

## **Versionamento dos Esquemas XML** 

As alterações na estrutura dos eventos são controladas por meio do versionamento dos esquemas XML. A versão de cada leiaute é identificada no _namespace_ do XML e no nome do arquivo XSD correspondente. 

Exemplo de _namespace_ : 

http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1 

Onde: 

|**Componente**|**Descrição**|
|---|---|
|`http://www.dere.gov.br/schemas/`|<sup>Identifica a base dos esquemas da</sup><br>DeRE|
|`evtInfoContrib`|Identifica o tipo de evento|
|`v1_0_1`|Identifica a versão do leiaute|



O arquivo XSD correspondente ao exemplo acima seria _`evtInfoContribv1_0_1.xsd.`_ 

**Página 18 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **Utilização do Namespace** 

Todo evento XML deve declarar o _namespace_ correspondente à versão do leiaute utilizada. 

Exemplo: 

<DeRE xmlns="http://www.dere.gov.br/schemas/evtInfoContrib/v1_0_1"> 

## </DeRE> 

A utilização de _namespace_ incompatível com a versão esperada pelo ambiente resultará na rejeição do evento durante o processamento. 

## **Evolução dos Leiautes** 

Os leiautes da DeRE poderão sofrer alterações em decorrência de: 

- Mudanças na legislação aplicável; 

- Necessidades técnicas identificadas durante a evolução da plataforma; 

- Correções ou aperfeiçoamentos dos modelos de dados. 

Quando as alterações decorrerem de modificações legislativas, sua implementação observará os prazos estabelecidos nos respectivos atos normativos. 

As alterações de natureza técnica serão divulgadas previamente por meio dos canais oficiais (Portal SPED e Portal Nacional do IBS e da CBS) e disponibilizadas com as novas versões dos esquemas XSD. 

## **4.2 CERTIFICADOS DIGITAIS ACEITOS (ICP-BRASIL)** 

Os eventos transmitidos para a DeRE devem ser assinados digitalmente utilizando certificado digital emitido por Autoridade Certificadora credenciada pela Infraestrutura de Chaves Públicas Brasileira (ICP-Brasil). 

A assinatura digital tem por objetivo garantir a autenticidade, a integridade e o não repúdio das informações transmitidas. 

Durante o processamento dos eventos, o ambiente da DeRE realizará validações relacionadas à cadeia de certificação e à validade do certificado utilizado na assinatura. 

## **Requisitos do Certificado Digital** 

O certificado digital utilizado para assinatura dos eventos deverá atender aos seguintes requisitos: 

- Possuir cadeia de certificação válida e confiável; 

**Página 19 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

- Possuir cadeia certificadora vinculada à Autoridade Certificadora Raiz da ICPBrasil; 

- Não estar revogado no momento da validação; 

- Não estar expirado na data da verificação da assinatura; 

- Ser do tipo e-CNPJ, e-PJ, e-CPF, e-PF ou e-Aplicação; 

- Possuir os atributos **_digitalSignature_** e **_nonRepudiation_** definidos na extensão **_Key Usage_** do certificado. 

## **Tipos de Certificados Aceitos** 

A DeRE aceita certificados digitais dos tipos: 

- ICP-Brasil; 

- A1; 

- A3; 

- certificado do contribuinte; 

- certificado do representante legal; 

- certificado do procurador eletrônico (se aplicável). 

A utilização de certificados emitidos fora da cadeia ICP-Brasil ou que não atendam aos requisitos definidos neste manual resultará na rejeição do evento durante o processamento. 

## **Identificação do Assinante** 

As informações necessárias para identificação do assinante são obtidas diretamente a partir do certificado digital utilizado na assinatura do evento. 

Por esse motivo, não é necessária a inclusão de informações adicionais de identificação do signatário no conteúdo do evento além daquelas previstas pelo padrão _XML Digital Signature_ . 

## **4.3 ASSINATURA DIGITAL DOS EVENTOS (** **_XMLDSIG_** **<u>)</u>** 

A assinatura digital dos eventos deverá seguir o padrão _XML Digital Signature_ ( _XMLDSig_ ), conforme especificação definida pelo _World Wide Web Consortium (W3C)_ . 

A assinatura é aplicada diretamente ao evento XML e permite verificar a autoria, integridade e autenticidade das informações transmitidas. 

## **Elemento Assinado** 

A assinatura digital deve ser aplicada ao elemento do XML que contém o atributo 

**Id** . 

**Página 20 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

Cada evento possui um identificador único e a assinatura deve referenciar explicitamente esse identificador por meio do atributo **_Reference URI_** . 

Exemplo: 

<evtInfoContrib id="DeRE10011000000123456782026012809000000311"> 

## **Padrão de Assinatura** 

A DeRE adota um subconjunto do padrão _XML Digital Signature (XMLDSig)_ , utilizando assinatura do tipo **_Enveloped Signature_** , na qual o elemento de assinatura é incorporado ao próprio documento XML assinado. 

## **Requisitos Técnicos da Assinatura** 

A assinatura digital deverá observar os seguintes padrões: 

|**Item**|**Padrão Adotado**|
|---|---|
|Padrão de assinatura|XML Digital Signature (XMLDSig)|
|Formato da assinatura|Enveloped Signature|
|Certificado digital|ICP-Brasil|
|Cadeia de certificação|EndCertOnly|
|Tipo de certificado|A1 ou A3|
|Algoritmo de assinatura|RSA-SHA256|
|Algoritmo de resumo criptográfico<br>(Digest)|SHA-256|
|Codificação|Base64|
|Tamanho da chave|Compatível com certificados A1 e A3 (1024 e 2048<br>bits)|



## **Cadeia de Certificação** 

A assinatura deverá conter apenas o certificado do assinante final. 

Não devem ser incluídos certificados intermediários ou certificados da autoridade certificadora na estrutura XML da assinatura. 

**Página 21 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

## **Transformações Obrigatórias** 

Para garantir a correta validação da assinatura digital, deverão ser utilizadas as seguintes transformações: 

**Transformação Identificador** Enveloped Signature <u>http://www.w3.org/2000/09/xmldsig#enveloped-signature</u> 

Canonicalização XML (C14N) <u>http://www.w3.org/TR/2001/REC-xml-c14n-20010315</u> 

Essas transformações são utilizadas para normalizar o conteúdo XML antes do cálculo dos resumos criptográficos e da validação da assinatura. 

## **Estrutura do Certificado na Assinatura** 

As informações do certificado digital devem ser representadas por meio do elemento **_X509Certificate_** , conforme previsto pelo padrão _XMLDSig_ . 

## **Importante** 

Os algoritmos criptográficos adotados pela DeRE poderão ser atualizados futuramente em decorrência de evolução dos padrões de segurança, recomendações dos órgãos responsáveis pela ICP-Brasil e necessidades técnicas ou regulatórias. Essas atualizações serão divulgadas no Portal SPED e no Portal Nacional do IBS e da CBS. 

As bibliotecas utilizadas para geração da assinatura digital devem implementar integralmente o padrão _XML Digital Signature (XMLDSig)_ e suportar os algoritmos definidos neste manual. 

## **4.4 TRATAMENTO DE ERROS E RETENTATIVAS** 

As aplicações integradas à DeRE devem estar preparadas para tratar adequadamente falhas de comunicação, erros de autenticação, indisponibilidades temporárias dos serviços e rejeições decorrentes de validações técnicas ou de negócio. 

Em situações de _timeout_ , interrupção de rede ou falha de comunicação durante a transmissão de um lote, a aplicação não deve presumir automaticamente que o lote não foi recebido pelo ambiente da DeRE. 

Nesses casos, recomenda-se que o sistema registre a ocorrência e realize verificações adicionais antes de efetuar uma nova transmissão, evitando o envio indevido de informações em duplicidade. 

## **Retentativas** 

As retentativas devem ser utilizadas apenas para falhas temporárias de comunicação ou indisponibilidades dos serviços. 

**Página 22 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

Recomenda-se que: 

- As retentativas sejam realizadas de forma controlada; 

- Seja adotado intervalo crescente entre tentativas sucessivas ( _backoff_ ); 

- Não sejam executadas tentativas contínuas em intervalos muito curtos; 

- As falhas sejam registradas para fins de auditoria e suporte. 

Erros decorrentes de inconsistências nos dados transmitidos ou violações de regras de negócio devem ser corrigidos antes da realização de nova transmissão. 

## **Tratamento dos Códigos de Respostas HTTP** 

As APIs da DeRE utilizam códigos de status _HTTP_ para indicar o resultado das requisições realizadas. As aplicações integradas devem interpretar esses códigos e adotar o tratamento adequado para cada situação. 

|**Código HTTP**|**Descrição**|**Tratamento Recomendado**|
|---|---|---|
|200 (OK)|Requisição processada com<br>sucesso.|Processar normalmente a resposta<br>retornada pela API.|
|202 (_Accepted_)|Requisição recebida e aceita<br>para processamento<br>assíncrono.|Armazenar o protocolo retornado e<br>realizar consulta posterior do resultado<br>do processamento.|
|400 (_Bad_<br>_Request_)|Requisição inválida ou<br>malformada.|Corrigir os dados da requisição antes<br>de realizar novo envio.|
|401<br>(_Unauthorized_)|Token ausente, inválido ou<br>expirado.|Obter novo token junto ao Receita<br>Integra e repetir a operação.|
|403 (_Forbidden_)|Acesso não autorizado para a<br>operação solicitada.|Verificar credenciais, permissões e<br>configurações de acesso.|
|404 (_Not Found_)|Recurso não encontrado.|Verificar os parâmetros informados,<br>como número de protocolo ou URL<br>utilizada.|
|429 (_Too Many_<br>_Requests_)|Limite de utilização da API<br>excedido.|Aguardar antes de realizar novas<br>chamadas e reduzir a frequência das<br>requisições.|
|500(_Internal_|Erro interno da aplicação.|Registrar a ocorrência e realizar nova|



**Página 23 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

|**Código HTTP**|**Descrição**|**Tratamento Recomendado**|
|---|---|---|
|_Server Error_)||tentativa após intervalo adequado.|
|502 (_Bad_<br>_Gateway_)|Falha temporária de<br>comunicação entre serviços.|Realizar retentativa controlada após<br>aguardar alguns instantes.|
|503 (_Service_<br>_Unavailable_)|Serviço temporariamente<br>indisponível.|Aguardar a normalização do serviço<br>antes de realizar nova tentativa.|
|504 (_Gateway_<br>_Timeout_)|Tempo limite excedido durante<br>o processamento.|Realizar nova tentativa observando as<br>recomendações de retentativa da<br>aplicação.|



**Importante:** Nas operações de transmissão de lotes, o código HTTP indica apenas o resultado da comunicação com a API. A confirmação definitiva do processamento dos eventos deve ser obtida posteriormente por meio da API de Consulta do Resultado do Processamento. 

## **Códigos de Erros de Negócio** 

Além dos códigos de status HTTP, a DeRE poderá retornar códigos de ocorrência específicos relacionados às validações realizadas durante o processamento dos eventos. Esses códigos permitem identificar de forma detalhada a causa de rejeições, advertências ou outras situações encontradas durante a análise das informações transmitidas. 

A relação completa dos códigos de ocorrência, suas descrições e orientações de tratamento será disponibilizada em documento específico, publicado como anexo deste manual do desenvolvedor. Esse documento poderá ser atualizado independentemente deste manual, acompanhando a evolução das regras de validação da DeRE. 

## **4.5 LIMITES OPERACIONAIS** 

Com o objetivo de garantir a disponibilidade, estabilidade e desempenho dos serviços, os ambientes da DeRE adotarão limites operacionais para utilização das APIs, abrangendo, entre outros aspectos: 

- Quantidade de requisições realizadas em determinado período; 

- Frequência de consultas aos serviços; 

- Quantidade de eventos por lote; 

- Tamanho máximo das mensagens transmitidas; 

- Tempo máximo de processamento das requisições. 

**Página 24 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

As aplicações integradas devem ser desenvolvidas considerando a possibilidade de rejeição ou bloqueio temporário de requisições que excedam os limites estabelecidos. 

Quando aplicável, os limites vigentes serão divulgados pelos canais oficiais (Portal SPED e Portal Nacional do IBS e da CBS). 

As aplicações devem evitar comportamentos que possam gerar consumo excessivo dos recursos computacionais disponibilizados pela DeRE, tais como: 

- Consultas repetitivas em intervalos reduzidos; 

- Retentativas contínuas sem controle; 

- Transmissões redundantes do mesmo conteúdo; 

- Processos automatizados que realizem chamadas em volume incompatível com a finalidade da integração. 

A observância dos limites operacionais contribui para a manutenção da disponibilidade do serviço para todos os usuários. 

## **4.6 AMBIENTE DE PRODUÇÃO RESTRITA** 

A DeRE disponibilizará um ambiente de Produção Restrita destinado à realização de testes e validações das integrações antes da utilização do ambiente de produção. 

Esse ambiente permite que os desenvolvedores validem a geração dos eventos, a assinatura digital, a transmissão dos lotes, a consulta dos resultados e o tratamento dos retornos disponibilizados pelas APIs. 

As informações transmitidas nesse ambiente não produzem efeitos legais ou fiscais. 

## **Limitações do Ambiente de Produção Restrita** 

O ambiente de Produção Restrita destina-se exclusivamente à validação funcional e técnica das integrações com a DeRE. 

Esse ambiente não foi projetado para execução de testes de carga, testes de estresse, testes de desempenho ou qualquer outra modalidade de teste que tenha como objetivo avaliar a capacidade operacional da infraestrutura disponibilizada. 

Em razão disso, as aplicações não devem realizar transmissões massivas de eventos ou consultas em volume incompatível com as atividades normais de homologação. 

Para preservar a disponibilidade e a estabilidade do ambiente de Produção Restrita, poderão ser estabelecidos limites para a quantidade de eventos transmitidos por contribuinte, aplicação ou período. 

**Página 25 de 26** 

DERE - Manual de Orientação ao Desenvolvedor 

Versão 1.0.1 

Os limites operacionais vigentes serão divulgados pelos canais oficiais (Portal SPED e Portal Nacional do IBS e da CBS) e poderão ser alterados conforme as necessidades de administração do ambiente. 

## **Limpeza Periódica dos Dados do Ambiente** 

Os eventos transmitidos para o ambiente de Produção Restrita não possuem caráter permanente. 

Com o objetivo de preservar a capacidade operacional do ambiente e garantir condições adequadas para realização de testes por todos os usuários, poderão ser realizadas rotinas periódicas de limpeza dos dados armazenados. 

Essas rotinas poderão remover eventos, lotes, protocolos, recibos e demais informações registradas no ambiente de homologação. 

As limpezas programadas serão comunicadas previamente pelos canais oficiais (Portal SPED e Portal Nacional do IBS e da CBS), permitindo que os usuários adotem as providências necessárias para seus processos de teste. 

Em razão dessa característica, as aplicações não devem considerar as informações armazenadas no ambiente de Produção Restrita como base permanente para testes de longa duração ou validações históricas. 

**Página 26 de 26** 

