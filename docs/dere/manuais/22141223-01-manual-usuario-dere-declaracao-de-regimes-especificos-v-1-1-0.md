**CGIBS** Comitê Gestor do IBS 

**RECEITA FEDERAL** 

Ministério da Fazenda 

# **DeRE** 

Declaração de Regimes Específicos Manual do Usuário 





**Manual de Orientação do Usuário** 

**Versão 1.1.0  |  22 de junho de 2026** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **<mark>SUMÁRIO</mark>** 

|**CAPÍTULO I – INFORMAÇÕES GERAIS E CONCEITUAIS .................................................................................................................... 5**|
|---|
|**1 APRESENTAÇÃO DO MANUAL (MOD)........................................................................................................................................... 5**|
|<br>1.1 OBJETIVO ................................................................................................................................................................................. 5|
|1.2 PÚBLICO-ALVO ........................................................................................................................................................................ 5|
|1.3 COMPOSIÇÃO DA DOCUMENTAÇÃO TÉCNICA ..................................................................................................................... 6|
|**2 CONCEITO, FINALIDADE E NATUREZA JURÍDICA DA DERE....................................................................................................... 7**|
|2.1 CONCEITO DA DERE ............................................................................................................................................................... 7|
|2.2 FUNCIONALIDADES DA DERE ................................................................................................................................................ 7|
|2.2.1 Aferição da Base de Cálculo (IBS/CBS/IS) nos Regimes Específicos ................................................................................. 7|
|2.2.2 Princípios da Não Cumulatividade e do Destino,_Cashback_e Cidadania Fiscal .................................................................. 8|
|2.3 NATUREZA JURÍDICA DA DERE .............................................................................................................................................. 8|
|**3 GESTÃO E GOVERNANÇA COMPARTILHADA ............................................................................................................................. 9**|
|**4 CONTRIBUINTES OBRIGADOS E DISPENSADOS ........................................................................................................................ 9**|
|4.1 OBRIGATORIEDADE DA DERE ................................................................................................................................................ 9|
|4.2 SITUAÇÕES ESPECIAIS DE OBRIGATORIEDADE .................................................................................................................. 9|
|4.3 REGRAS DE DISPENSA DA DERE ........................................................................................................................................ 10|
|**5 CONTEÚDO DA ESCRITURAÇÃO ................................................................................................................................................ 11**|
|5.1 OPERAÇÕES ESCRITURADAS NA DERE ............................................................................................................................. 11|
|5.2 OPERAÇÕES TRIBUTADAS NA DERE .................................................................................................................................. 11|
|5.3 AQUISIÇÕES DE BENS E SERVIÇOS .................................................................................................................................... 11|
|5.4 COEXISTÊNCIA COM OUTRAS OBRIGAÇÕES ..................................................................................................................... 12|
|**CAPÍTULO II – ARQUITETURA TÉCNICA E REGRAS GERAIS DE TRANSMISSÃO .......................................................................... 13**|
|**1 DEFINIÇÕES E CONCEITOS ......................................................................................................................................................... 13**|
|1.1 CONCEITO DE EVENTO ........................................................................................................................................................ 13|
|1.1.1 Convenção Semântica dos Eventos (Padrão D-RPNN) .................................................................................................... 13|
|1.1.2 Eventos de Tabela do Contribuinte ................................................................................................................................... 13|
|1.1.3 Eventos Periódicos Mensais ............................................................................................................................................. 14|
|1.1.4 Eventos Periódicos Transacionais .................................................................................................................................... 14|
|1.1.5 Eventos de Retorno e Totalização (Série D-9000) ............................................................................................................ 15|
|1.1.6 Listagem dos Eventos da DeRE ....................................................................................................................................... 15|
|1.2 TABELAS DO SISTEMA (TABELAS DE DOMÍNIO) ................................................................................................................. 15|
|1.3 REPRESENTAÇÃO DOS LEIAUTES (XML) ............................................................................................................................ 16|
|1.3.1 Estrutura Hierárquica do Evento (Resumo) ...................................................................................................................... 16|
|1.3.2 Especificação Técnica de Campos (Detalhamento) .......................................................................................................... 17|
|1.4 CONVENÇÕES TIPOGRÁFICAS E PADRÕES DE LEITURA ................................................................................................. 18|
|1.4.1 Referências a Campos, Tabelas e Regras ........................................................................................................................ 18|
|<br>1.4.2 Padrões e Nomenclatura de Campos ............................................................................................................................... 19|
|1.4.3 Padrão Numérico e Decimal ............................................................................................................................................. 19|
|1.5 REGRAS DE PREENCHIMENTO E CARACTERES ESPECIAIS (XML) ................................................................................. 20|
|**2 FLUXO DE TRANSMISSÃO E PROCESSAMENTO ...................................................................................................................... 20**|
|2.1 ASSINATURA DIGITAL (CERTIFICAÇÃO ICP-BRASIL) .......................................................................................................... 20|
|2.1.1 Requisitos Técnicos do Certificado Digital ........................................................................................................................ 20|
|<br>2.1.2 Regras de Permissão de Assinatura e Acesso .................................................................................................................. 21|
|2.1.3 Implicações Legais ........................................................................................................................................................... 21|
|2.2 TRANSMISSÃO POR LOTES DE EVENTOS (_APIS_) ............................................................................................................... 21|
|2.2.1 Gerenciamento de Pré-Requisitos e Dependências .......................................................................................................... 21|
|2.3 PROCESSAMENTO ASSÍNCRONO........................................................................................................................................ 22|
|2.4 PROTOCOLO DE RECEBIMENTO VERSUS RECIBO DE PROCESSAMENTO .................................................................... 22|
|**3 REGRAS DE INCLUSÃO E MANUTENÇÃO DE INFORMAÇÕES ................................................................................................ 23**|
|3.1 INCLUSÃO E MANUTENÇÃO DE EVENTOS DE TABELA (MODELO DE VIGÊNCIA/VALIDADE) ......................................... 23|



**Página 1 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

|3.1.1 Inclusão de Eventos de Tabela ......................................................................................................................................... 23<br>_3.1.1.1 Eventos com Validade Aberta/Flexível ...................................................................................................................... 23_|
|---|
|_3.1.1.2 Eventos com Validade Fechada/Rígida ..................................................................................................................... 24_|
|_3.1.1.3 Validação de Unicidade ............................................................................................................................................. 25_|
|3.1.2 Alteração de Eventos de Tabela ....................................................................................................................................... 25|
|_3.1.2.1 Alteração de Eventos (Mesma Vigência) ................................................................................................................... 25_|
|_3.1.2.2 Alteração de Validade (Novo Período) ....................................................................................................................... 26_|
|<br>3.1.3 Exclusão de Evento de Tabela .......................................................................................................................................... 26|
|3.2 INCLUSÃO E MANUTENÇÃO DE EVENTOS PERIÓDICOS MENSAIS (MODELO DE RECIBO) ........................................... 26|
|3.2.1 Inclusão (Envio Original)................................................................................................................................................... 26|
|3.2.2 Alteração (Substituição Integral) ....................................................................................................................................... 27|
|3.2.3 Exclusão de Evento Periódico .......................................................................................................................................... 27|
|**4 A FORMAÇÃO DAS BASES DE CÁLCULO (CODBC) .................................................................................................................. 27**|
|4.1 CONCEITO E FINALIDADE ..................................................................................................................................................... 27|
|4.2 IDENTIDADE E PARÂMETROS FORMADORES DOS CÓDIGOS DE BASE DE CÁLCULO .................................................. 27|
|4.3 DINÂMICA DE FORMAÇÃO DA BASE DE CÁLCULO............................................................................................................. 28|
|4.3.1 Coeficientes de Rateio e de Reversão .............................................................................................................................. 29|
|4.3.2 Regra de Transição: Receitas não sujeitas à CBS (sujeitas ao PIS/Cofins) ...................................................................... 31|
|4.4 MEMÓRIA DE CÁLCULO NOS EVENTOS DE RETORNO ..................................................................................................... 32|
|4.5 TRATAMENTO DE BASES DE CÁLCULO NEGATIVAS (BCN) ............................................................................................... 33|
|4.5.1 Regras de Compensação de Bases de Cálculo Negativa ................................................................................................. 33|
|**CAPÍTULO III – EVENTOS COMUNS A TODOS OS REGIMES SÉRIE (D-1000).................................................................................. 34**|
|**1 EVENTOS DE TABELA .................................................................................................................................................................. 34**|
|1.1 D-1001 – EVENTO DE INFORMAÇÕES DO CONTRIBUINTE ................................................................................................ 34|
|1.1.1 Conceito e Finalidade ....................................................................................................................................................... 34|
|1.1.2 Frequência e Obrigatoriedade de Envio ........................................................................................................................... 34|
|1.1.3 Regras de Negócio e Preenchimento ............................................................................................................................... 34|
|<br>_1.1.3.1 Classificação de Regime Tributário Principal ............................................................................................................. 34_|
|_1.1.3.2 Contribuintes com Alíquota Zero ou Não Incidência .................................................................................................. 35_|
|_1.1.3.3 Identificação de Atividades ........................................................................................................................................ 35_|
|1.2 D-1011 – PLANO GERAL DE CONTAS COMENTADO - PGCC .............................................................................................. 35|
|1.2.1 Conceito e Finalidade ....................................................................................................................................................... 35|
|1.2.2 Frequência e Obrigatoriedade de Envio ........................................................................................................................... 36|
|1.2.3 Código de Tributação ({codTrib})...................................................................................................................................... 36|
|1.2.4 Regras de Negócio e Preenchimento ............................................................................................................................... 37|
|_1.2.4.1 Vigência do PGCC e das Contas .............................................................................................................................. 37_|
|_1.2.4.2 Tratamento Base das Contas .................................................................................................................................... 37_|
|_1.2.4.3 O Plano de Contas Referencial ................................................................................................................................. 38_|
|_1.2.4.4 Indicador de Frequência de Encerramento Contábil{freqEncerr}.............................................................................. 38_|
|_1.2.4.5 Indicação de Tributação do ISS{indTribISS}............................................................................................................. 39_|
|**2 EVENTOS PERIÓDICOS MENSAIS ............................................................................................................................................... 39**|
|2.1 D-1101 – BALANCETE MENSAL............................................................................................................................................. 39|
|2.1.1 Conceito e Finalidade ....................................................................................................................................................... 39|
|2.1.2 Frequência e Obrigatoriedade de Envio ........................................................................................................................... 39<br>2.1.3 Regras de Negócio e Preenchimento ............................................................................................................................... 40|
|<br>_2.1.3.1 A Dependência do PGCC .......................................................................................................................................... 40_|
|_2.1.3.2 Regras de Consistência Contábil .............................................................................................................................. 40_|
|_2.1.3.3 Dinâmica de Lançamentos, Ajustes e Valor Apurado ................................................................................................ 40_|
|2.1.4 Vínculo do Balancete com Outros Eventos ....................................................................................................................... 42<br>|
|2.2 D-1106 – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS ................................................................................................ 42|
|<br>2.2.1 Conceito e Finalidade ....................................................................................................................................................... 42|
|2.2.2 Frequência e Obrigatoriedade de Envio ........................................................................................................................... 43|
|223 Regras de Negócio e Preenchimento  43|
|..      ...............................................................................................................................<br>_2.2.3.1 A Dependência do PGCC .......................................................................................................................................... 43_|



**Página 2 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

|_2.2.3.2 Individualização de Ativos por Conta Contábil ........................................................................................................... 44_<br>_2.2.3.3 Declaração de Inexistência de Aplicações Vinculadas à Reserva Técnica ................................................................ 44_|
|---|
|<br>_2.2.3.4 Regras de Validação ................................................................................................................................................. 44_|
|2.2.4 Vínculo do Evento D-1106 com Outros Eventos ............................................................................................................... 45|
|2.3 D-1199 – FECHAMENTO MENSAL ......................................................................................................................................... 45|
|2.3.1 Conceito e Finalidade ....................................................................................................................................................... 45|
|2.3.2 Frequência e Obrigatoriedade de Envio ........................................................................................................................... 46|
|2.3.3 Regras de Negócio e Preenchimento ............................................................................................................................... 46|
|_2.3.3.1 Regras de Integridade e Dependência ...................................................................................................................... 46_|
|_2.3.3.2 Consistência e Validação de Saldos Contábeis ......................................................................................................... 46_|
|_2.3.3.3 Aproveitamento de Base de Cálculo Negativa (BCN) ................................................................................................ 47_|
|2.3.4 Eventos Mensais Sujeitos ao Evento de Fechamento (D-1199) ........................................................................................ 48|
|2.3.5 Efeitos do Evento de Fechamento (D-1199) ..................................................................................................................... 48|
|_2.3.5.1 Bloqueio de Operações após o Fechamento ............................................................................................................. 48_|
|**CAPÍTULO IV – EVENTOS DO REGIME ESPECÍFICO DE SERVIÇOS FINANCEIROS (SÉRIE D-2000) ............................................ 49**|
|**1 EVENTOS PERIÓDICOS MENSAIS ............................................................................................................................................... 49**|
|1.1 D-2101 – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA ................................................... 49|
|1.1.1 Conceito e Finalidade ....................................................................................................................................................... 49|
|1.1.2 Frequência e Obrigatoriedade de Envio ........................................................................................................................... 49|
|1.1.3 Regras de Negócio e Preenchimento ............................................................................................................................... 50|
|_1.1.3.1 A Dependência do PGCC .......................................................................................................................................... 50_|
|_1.1.3.2 Individualização de Títulos por Conta Contábil .......................................................................................................... 50_|
|_1.1.3.3 Formas de Aquisição e Identificação do Título .......................................................................................................... 50_|
|_1.1.3.4 Declaração de Inexistência de Títulos a Informar ...................................................................................................... 51_|
|_1.1.3.5 Regras de Validação ................................................................................................................................................. 51_|
|1.1.4 Vínculo do Evento D-2101 com Outros Eventos ............................................................................................................... 52|
|**CAPÍTULO V – EVENTOS DE RETORNO E TOTALIZAÇÃO (SÉRIE D-9000) ..................................................................................... 53**|
|**1 DISPOSIÇÕES GERAIS ................................................................................................................................................................. 53**|
|1.1 DISPOSIÇÕES COMUNS A TODOS OS EVENTOS DE RETORNO ....................................................................................... 53|
|1.1.1 Identificação do Status (Sucesso, Erro) ............................................................................................................................ 53|
|1.1.2 Grupo de Ocorrências (Interpretação de Erros) ................................................................................................................ 54|
|1.2 DISPOSIÇÕES COMUNS AOS RETORNOS DE EVENTOS DE TABELA ............................................................................... 54|
|1.2.1 Extrato de Eventos Vigentes............................................................................................................................................. 54|
|1.3 DISPOSIÇÕES COMUNS AOS RETORNOS DE EVENTOS PERIÓDICOS MENSAIS ........................................................... 55|
|1.3.1 Versionamento de Eventos ............................................................................................................................................... 55|
|**2 RETORNO DE EVENTOS DE TABELA .......................................................................................................................................... 55**|
|2.1 D-9001 – RETORNO – EVENTOS DE TABELA....................................................................................................................... 55|
|2.1.1 Conceito de Retorno de Tabelas ....................................................................................................................................... 55|
|**3 RETORNO DE EVENTOS PERIÓDICOS MENSAIS ...................................................................................................................... 56**|
|3.1 D-9101 – RETORNO TOTALIZADOR – BALANCETE MENSAL .............................................................................................. 56|
|3.1.1 Conceito e Finalidade ....................................................................................................................................................... 56|
|3.1.2 Lógica de Totalização do Balancete .................................................................................................................................. 56|
|3.1.3 Ações Subsequentes ao Retorno ..................................................................................................................................... 56|
|3.2 D-9106 – RETORNO TOTALIZADOR – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS ................................................. 57|
|3.2.1 Conceito e Finalidade ....................................................................................................................................................... 57|
|3.2.2 Lógica de Totalização do Evento ...................................................................................................................................... 57|
|3.2.3 Ações Subsequentes ao Retorno ..................................................................................................................................... 57<br>3.2.4 O Fluxo da Totalização ..................................................................................................................................................... 57|
|3.3 D-9121 – RETORNO TOTALIZADOR – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA ..... 58|
|3.3.1 Conceito e Finalidade ....................................................................................................................................................... 58|
|3.3.2 Lógica de Totalização do Evento ...................................................................................................................................... 58|
|333 Ações Subsequentes ao Retorno  58|
|..     .....................................................................................................................................<br>3.3.4 Conteúdo da Totalização .................................................................................................................................................. 59|



**Página 3 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

|3.3.5 Fluxo da Totalização ......................................................................................................................................................... 59|
|---|
|3.4 D-9199 – RETORNO TOTALIZADOR – FECHAMENTO MENSAL .......................................................................................... 59|
|3.4.1 Conceito e Finalidade ....................................................................................................................................................... 59|
|3.4.2 Memória de Cálculo e Aferição por Regime Específico ..................................................................................................... 59|
|3.4.3 O Extrato de Bases de Cálculo Negativas (BCN) ............................................................................................................. 60|
|3.4.4 Integração com a Apuração Assistida ............................................................................................................................... 61|



**Página 4 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **<mark>CAPÍTULO I – INFORMAÇÕES GERAIS E CONCEITUAIS</mark>** 

## **1 APRESENTAÇÃO DO MANUAL (MOD)** 

## **1.1 OBJETIVO** 

O Manual de Orientação do Usuário da Declaração de Regimes Específicos - DeRE (MOD) tem como finalidade orientar o contribuinte no cumprimento dessa obrigação acessória. A declaração é o instrumento que formaliza a escrituração contábil-fiscal das operações e informações relacionadas aos regimes específicos de tributação, instituídos no contexto da legislação que implementou a Reforma Tributária sobre o Consumo - RTC, trazida pela Emenda Constitucional nº 132, de 20 de dezembro de 2023. 

Este manual traz as diretrizes técnicas e operacionais para a correta escrituração contábil-fiscal das operações relacionadas aos regimes específicos de tributação, abrangendo o Imposto sobre Bens e Serviços (IBS), a Contribuição sobre Bens e Serviços (CBS) e, quando aplicável, o Imposto Seletivo (IS). 

O MOD, aprovado e atualizado por meio de ato administrativo conjunto do Comitê Gestor do IBS (CGIBS) e da Secretaria da Receita Federal do Brasil (RFB), cumpre os seguintes propósitos essenciais: 

- **a)** Detalhar as especificações técnicas, esclarecer o leiaute estruturado (XML), as tabelas de códigos e as regras de validação necessárias para a correta geração e transmissão dos arquivos da DeRE; 

- **b)** Orientar o contribuinte sobre a classificação das informações econômico-fiscais e contábeis da escrituração; 

- **c)** Padronizar a interpretação dos leiautes e das regras de negócio, sendo essencial para as equipes contábeis e fiscais do contribuinte; e 

- **d)** Garantir a conformidade ao estabelecer as regras práticas para a declaração. 

## **1.2 PÚBLICO-ALVO** 

O público-alvo pode ser segmentado em duas categorias principais: 

- **a)** Os contribuintes que operam nos seguintes regimes específicos: (i) Serviços Financeiros, (ii) Planos de Assistência à Saúde, e (iii) Concursos de Prognósticos; e 

- **b)** Os desenvolvedores e equipes de tecnologia da informação (TI). 

O MOD é um documento técnico complementar para desenvolvedores de _software_ . Ele detalha a arquitetura da DeRE, fornecendo especificações técnicas (leiautes, tabelas oficiais, regras de validação da estrutura de arquivo e das regras de negócio) para garantir a recepção com sucesso do arquivo gerado pelo Ambiente Nacional da DeRE. 

**Página 5 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **1.3 COMPOSIÇÃO DA DOCUMENTAÇÃO TÉCNICA** 

A documentação técnica da DeRE é composta por um conjunto de arquivos, organizados da seguinte forma: 

- **01-Manual Usuário DeRE - Declaração de Regimes Específicos (MOD):** Documento central que consolida as diretrizes conceituais, o fluxo operacional, os pré-requisitos de transmissão e as orientações detalhadas de preenchimento; 

- **02-Leiautes da DeRE - Eventos:** Especificação técnica detalhada das estruturas hierárquicas e dos campos de todos os eventos em formato XML; 

- **03-Leiautes da DeRE - Anexo I - Tabelas:** Relação das tabelas de domínio do sistema (ex: Códigos de Tributação e Códigos de Base de Cálculo); 

- **04-Leiautes da DeRE - Anexo II - Regras de Validação:** Detalhamento das regras de negócio, validações, exigências de integridade e críticas aplicadas pelo Ambiente da DeRE durante o processamento dos arquivos; 

- **05-Leiautes da DeRE - Histórico de Versões:** Registro de controle das alterações, inclusões e correções realizadas entre as versões publicadas, garantindo a rastreabilidade das atualizações; 

- **06-Arquivos XSD (** **_XML Schema Definition_ ):** Esquemas utilizados para a validação estrutural dos arquivos XML; 

- **07-Manual do Desenvolvedor:** Orientações técnicas para a integração de sistemas, detalhando o fluxo de comunicação via APIs, os protocolos de segurança, a validação de esquemas XSD, a assinatura digital padrão ICP-Brasil e o tratamento de retornos no processamento assíncrono; 

- **08-Mensagens de Erro do Sistema:** Catálogo dos códigos e descrições das mensagens de erro, alertas e ocorrências retornadas pelas APIs da DeRE, estruturado para auxiliar na identificação e correção de falhas de transmissão; e 

- **09-Manual de Integração Técnica - Receita Integra:** Especificações técnicas e de segurança sobre o processo de autenticação e autorização, para controle de acesso às APIs da DeRE. 

Adicionalmente, os portais oficiais de publicação da documentação técnica disponibilizam a seção de **Perguntas e Respostas Frequentes (FAQ)** . Este repositório compila orientações práticas e soluciona dúvidas recorrentes sobre a obrigatoriedade, os ambientes de transmissão, a formatação dos arquivos e as regras específicas de escrituração contábil-fiscal. 

**Página 6 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **2 CONCEITO, FINALIDADE E NATUREZA JURÍDICA DA DERE** 

## **2.1 CONCEITO DA DERE** 

A DeRE é um documento fiscal eletrônico instituído para o registro de dados e a aferição de débitos e créditos das operações, processos necessários à apuração, à distribuição e a outras destinações legais da administração tributária, referentes aos regimes específicos da CBS, do IBS e, quando aplicável, do IS. 

A solução técnica trazida pela DeRE é fundamental para atender à complexidade operacional das regras de tributação dos regimes específicos, cuja base de cálculo é determinada por margem (constituída por receitas tributáveis subtraídas das deduções permitidas por lei), apurada mensalmente, e não pelo valor de cada transação considerada individualmente. 

O contribuinte deve extrair as informações que lastreiam a DeRE de sua escrituração contábil, a qual deve ser mantida em conformidade com as Normas Brasileiras de Contabilidade (NBC), ou em controles extracontábeis, quando aplicáveis. 

Para padronizar a estrutura contábil, a DeRE utiliza os planos de contas referenciais dos respectivos órgãos reguladores (BCB, ANS, SUSEP, entre outros que possam vir a instituir planos de contas referenciais). 

Caso o contribuinte não esteja sujeito a nenhum órgão de controle governamental, ele deve utilizar como referência o plano de contas do SPED Contábil. 

## **2.2 FUNCIONALIDADES DA DERE** 

A DeRE agrega dados contábeis para a apuração consolidada dos tributos mencionados no item 1.1 deste Capítulo e detalha informações em nível transacional para garantir a efetividade dos princípios da não cumulatividade e do destino, além de prover insumos à implementação do _cashback_ e aos programas de incentivo à cidadania fiscal. 

## **2.2.1 Aferição da Base de Cálculo (IBS/CBS/IS) nos Regimes Específicos** 

O propósito da declaração é capturar as informações fiscais e contábeis correspondentes ao período de apuração. A aferição da base de cálculo dos tributos depende da correta classificação contábil-fiscal, que será realizada pelo contribuinte em suas contas internas de acordo com o código de tributação ( <mark>{codTrib}</mark> ). O sistema, com base nessa classificação, aplica as regras tributárias pertinentes aos saldos contábeis do balancete mensal para aferir o valor do débito consolidado. 

**Página 7 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **2.2.2 Princípios da Não Cumulatividade e do Destino,** **_Cashback_ e Cidadania Fiscal** 

A DeRE fornece os dados individualizados necessários para: 

- **a)** Calcular os créditos dos adquirentes/destinatários de serviços sujeitos ao regime regular do IBS e da CBS; 

- **b)** Viabilizar a distribuição do IBS aos entes subnacionais (Estados, Distrito Federal e Municípios), com base no princípio do destino; e 

- **c)** Fornecer insumos para a operacionalização dos programas de devolução personalizada ( _cashback_ ) e de incentivo à cidadania fiscal. 

Para atender esses princípios e programas, a DeRE utiliza arquivos individualizados, registrando, por adquirente/destinatário, os valores das operações, os tributos envolvidos, o domicílio ou o local onde a operação ocorreu. 

## **2.3 NATUREZA JURÍDICA DA DERE** 

A entrega da DeRE possui implicações jurídicas e fiscais para o contribuinte, destacando-se os seguintes aspectos: 

- **a) Natureza Declaratória:** as informações prestadas na DeRE pelo contribuinte possuem caráter declaratório, constituindo confissão de valores devidos de IBS, CBS e IS, consignados na obrigação acessória; 

- **b) Autenticidade e Validade Jurídica:** para garantir a autenticidade, integridade e validade jurídica da declaração, é obrigatória a assinatura digital do contribuinte ou de seu procurador, por meio de certificado digital emitido no âmbito da Infraestrutura de Chaves Públicas Brasileira (ICP-Brasil); 

- **c) Ato de Recebimento:** a recepção do arquivo eletrônico e a emissão do respectivo recibo de entrega não implicam o reconhecimento da veracidade ou da legitimidade das informações prestadas, nem a homologação da apuração do tributo. O Fisco reserva-se o direito de fiscalizar e lançar de ofício o que entender cabível, nos prazos e termos da legislação tributária; 

- **d) Dever de Conservação:** o contribuinte deve conservar, pelo prazo legal, todos os documentos fiscais, contábeis e demais documentos que deram origem às informações constantes na DeRE; e 

- **e) Compartilhamento de Dados:** uma vez recepcionado o arquivo eletrônico da DeRE, suas informações são compartilhadas com: (i) o CGIBS, (ii) a RFB e (iii) as demais Administrações Tributárias estaduais, distrital e municipais, nos limites de suas respectivas competências e interesses. 

**Página 8 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**3 GESTÃO E GOVERNANÇA COMPARTILHADA** 

A administração da DeRE adota um modelo de governança compartilhada entre a RFB e o CGIBS. Esse modelo cooperativo possibilita a apuração de tributos da competência tributária da União (CBS e IS, sob responsabilidade da RFB) e dos entes subnacionais (IBS, sob responsabilidade do CGIBS). 

## **4 CONTRIBUINTES OBRIGADOS E DISPENSADOS** 

A obrigatoriedade de entrega da DeRE é estabelecida pela Lei Complementar nº 214, de 16 de janeiro de 2025 - LC nº 214/2025 e pelas normas complementares, sendo definida por critérios técnicos e objetivos, vinculados à natureza da atividade econômica e à complexidade do regime de apuração do contribuinte. 

A DeRE é um documento fiscal eletrônico de padrão nacional, unificado e entregue de forma consolidada pela raiz do CNPJ (8 posições) do contribuinte, que deve enviar uma única declaração, agregando a escrituração da matriz e de suas filiais. 

## **4.1 OBRIGATORIEDADE DA DERE** 

A obrigatoriedade abrange os contribuintes que forneçam os seguintes serviços previstos na LC nº 214/2025: 

- **a)** Serviços financeiros (art. 182); 

- **b)** Serviços remunerados por tarifas e comissões que, embora prestados por instituições financeiras, sujeitam-se às normas gerais de incidência (art. 184); 

- **c)** Serviços prestados na relação jurídica entre o emissor e o portador de instrumento de pagamento, remunerados por tarifas e comissões (tais como anuidade e emissão de cartão), que se sujeitam às normas gerais de incidência (§ 2º do art. 214); 

- **d)** Planos de assistência à saúde (art. 234); 

- **e)** Planos de assistência funerária (art. 236); 

- **f)** Planos de assistência à saúde de animais domésticos (art. 243); e 

- **g)** Concursos de prognósticos (art. 244). 

Ato administrativo conjunto do CGIBS e da RFB poderá definir outros fornecimentos como obrigados à entrega da DeRE. 

## **4.2 SITUAÇÕES ESPECIAIS DE OBRIGATORIEDADE** 

Os contribuintes que forneçam os serviços mencionados no item 4.1 deste Capítulo e que também se enquadrem nos subitens abaixo continuam obrigados a entregar a DeRE, conforme a legislação aplicável: 

- **a) Imunidade/Não Incidência:** a condição de imunidade ou não incidência tributária não exime o contribuinte da entrega da DeRE; 

- **b) Contribuintes Optantes pelo Regime Específico de Cooperativas:** todas as cooperativas sujeitas aos regimes específicos estão obrigadas à apresentação da 

**Página 9 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

DeRE, ainda que suas operações sejam constituídas exclusivamente por atos cooperativos sujeitos à alíquota zero; e 

- **c) Contribuintes Optantes pelo Simples Nacional:** a microempresa (ME) e a empresa de pequeno porte (EPP) optante pelo Regime Especial Unificado de Arrecadação de Tributos e Contribuições das Microempresas e Empresas de Pequeno Porte (Simples Nacional) são obrigadas à DeRE apenas se ocorrer uma das seguintes situações: (i) optar expressamente por apurar o IBS e a CBS pelo regime regular (art. 41, § 3º, da LC nº 214/2025); ou (ii) exceder o sublimite de receita bruta definido no art. 13-A da LC nº 123/2006. 

## **4.3 REGRAS DE DISPENSA DA DERE** 

Ficam dispensados da entrega da DeRE, devendo observar a legislação específica sobre a obrigatoriedade ou não de outro documento fiscal, os seguintes contribuintes: 

- **a) Consultores e Assessores:** que fornecerem, exclusivamente, os serviços de assessor de investimento ou de consultor de valores mobiliários, ou ambos, elencados no inciso III do _caput_ do art. 182 da LC nº 214/2025; 

- **b) Corretores e Intermediários:** que fornecerem, exclusivamente, os serviços de intermediação de consórcios, de seguros ou resseguros, de previdência complementar, de capitalização e de planos de assistência à saúde, elencados no inciso XV do _caput_ do art. 182 e no art. 240 da LC nº 214/2025; 

- **c) Correspondentes Bancários:** que auferirem receitas próprias decorrentes da atuação por conta e sob as diretrizes da instituição financeira e demais instituições autorizadas a funcionar pelo Banco Central do Brasil - BCB, nos termos do inciso X do § 1º do art. 183 da LC nº 214/2025; 

- **d) Optantes pelo Simples Nacional de que trata a LC nº 123/2006:** exceto nas situações previstas na letra “c” do item 4.2 deste Capítulo; 

- **e) Microempreendedor Individual – MEI:** nos termos da LC nº 123/2006; e 

- **f) Pessoa Física:** salvo se prestar os serviços elencados no item 4.1 deste Capítulo, no desenvolvimento de atividade econômica de modo habitual ou em volume que caracterize atividade econômica ou de forma profissional, ocasião em que deverá se inscrever como contribuinte do IBS e da CBS, com natureza jurídica específica. 

**Página 10 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **5 CONTEÚDO DA ESCRITURAÇÃO** 

## **5.1 OPERAÇÕES ESCRITURADAS NA DERE** 

Todos os fatos contábeis devem ser evidenciados no Evento D-1101 (Balancete Mensal), independentemente da natureza das contas e das regras de tributação aplicáveis. 

## **5.2 OPERAÇÕES TRIBUTADAS NA DERE** 

Serão tributadas na DeRE, para fins de apuração do IBS, da CBS e do IS, todas as operações relativas aos serviços elencados no item 4.1 deste Capítulo. 

Os contribuintes obrigados à DeRE ficam desobrigados da emissão de nota fiscal em relação ao IBS, à CBS e ao IS, salvo em relação aos fornecimentos de bens ou serviços não previstos neste item. 

**Exemplo:** Operações que exigem Nota Fiscal. 

- Venda de ativo imobilizado ou mercadorias; 

- Serviços hospitalares ou de comércio de medicamentos prestados por operadora de saúde 

- (que não se confundem com o plano de saúde); 

- Prestação de serviços de tecnologia, receitas de aluguel ou de estacionamentos. 

## **5.3 AQUISIÇÕES DE BENS E SERVIÇOS** 

As aquisições de bens e serviços, como regra geral, não são escrituradas na DeRE. No entanto, a escrituração de aquisições será obrigatória na DeRE se dedutível da base de cálculo, nos casos permitidos pela legislação. 

A legislação permite deduções em casos específicos, tais como: 

- **a)** Despesas com assessores de investimento, consultores e correspondentes do Banco Central (relativas a operações específicas), desde que não sejam empregados ou administradores da empresa; ou 

- **b)** Serviços de intermediação para administradoras de consórcio, seguradoras, empresas de capitalização, planos de assistência à saúde e previdência privada. 

Sempre que o regulamento do IBS e da CBS assim o exigir, a dedução deve estar individualizada na nota fiscal ou na DeRE, quando o serviço objeto da dedução for prestado pelo declarante da DeRE a contribuinte do regime regular. Por exemplo: no setor de seguros, o intermediário emite nota fiscal, a seguradora deduz o valor pago pela intermediação na DeRE e referencia a respectiva nota fiscal para que não seja aproveitado o crédito do IBS e da CBS incidentes sobre o serviço, evitando o duplo aproveitamento (dedução e crédito). 

Outro exemplo aplicado ao setor de seguros: o banco presta serviço de intermediação de seguros à seguradora; neste caso, a DeRE do banco deverá identificar a seguradora como adquirente do serviço e poderá identificar o segurado como destinatário. 

**Página 11 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **5.4 COEXISTÊNCIA COM OUTRAS OBRIGAÇÕES** 

A DeRE não substitui outras obrigações acessórias municipais, distritais, estaduais ou federais. Os casos de dispensa de emissão de nota fiscal previstos no <u>item 4.3</u> deste Capítulo aplicam-se estrita e exclusivamente aos novos tributos (IBS, CBS e IS). 

Para a apuração dos tributos legados (ISS, ICMS, PIS, COFINS e IPI), o contribuinte deve continuar observando a legislação aplicável, mantendo a entrega de declarações legadas como a DES-IF (Declaração Eletrônica de Serviços de Instituições Financeiras), a e-Financeira e a DIMP (Declaração de Informações de Meios de Pagamentos), bem como a emissão de notas fiscais, quando exigido pelos respectivos entes. 

É proibido o destaque de IBS e CBS nesses documentos fiscais e declarações legadas referentes às receitas dos regimes específicos. Para informações sobre a emissão de outros documentos fiscais e o cumprimento de obrigações legadas, o contribuinte deve consultar os respectivos canais oficiais das administrações tributárias competentes. 

**Página 12 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **<mark>CAPÍTULO II – ARQUITETURA TÉCNICA E REGRAS GERAIS DE TRANSMISSÃO</mark>** 

**1 DEFINIÇÕES E CONCEITOS** 

## **1.1 CONCEITO DE EVENTO** 

No contexto da DeRE, considera-se evento o arquivo eletrônico individual, estruturado em formato XML, conforme leiautes. A escrituração na DeRE não é feita por um arquivo único, mas pela transmissão coordenada e cronológica de múltiplos eventos. 

**1.1.1 Convenção Semântica dos Eventos (Padrão D-RPNN)** 

A DeRE adota uma convenção semântica padronizada para a codificação de seus arquivos eletrônicos: o padrão D-RPNN. Esta estrutura modular padroniza a identificação dos eventos, permitindo acomodar a diversidade de regras dos diferentes regimes de tributação. 

A nomenclatura D-RPNN é composta por quatro caracteres, que definem o regime, a periodicidade de entrega e a função do evento dentro do fluxo da declaração: 

|**Elemento**|**Significado**|**Detalhamento**|
|---|---|---|
|**D**|**Identificador Fixo**|Demarca o arquivo digital como pertencente à DeRE.|
|**R**|**Regime Específico**|Indica o setor econômico e o conjunto de regras específicas<br>aplicáveis.<br>**Valores válidos:**<br>**1 -**Comum a Todos os Setores;<br>**2 -**Serviços Financeiros;<br>**3 -**Planos de Assistência à Saúde;<br>**4 -**Concursos de Prognósticos.|
|**P**|**Periodicidade**|Define a frequência e a função principal do evento.<br>**Valores válidos:**<br>**0 -**Eventual;<br>**1 -**Mensal;<br>**2 -**Transacional.|
|**NN**|**Número/Agrupamento**|Numeração que identifica o evento específico dentro de seu<br>Regime e Periodicidade.|



**Exemplo:** Evento Informações do Contribuinte. 

O evento D-1001 identifica um evento do “Comum a Todos os Setores” (1), de periodicidade “Eventual” (0), sendo atribuído o “número” (01) para o evento de “Informações do Contribuinte”. 

## **1.1.2 Eventos de Tabela do Contribuinte** 

As tabelas do contribuinte armazenam no Ambiente da DeRE os dados cadastrais e as estruturas de referência essenciais para parametrizar e validar os eventos periódicos de 

**Página 13 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

apuração. Tais tabelas são preenchidas e gerenciadas mediante o envio de eventos gerados e transmitidos pelo próprio contribuinte. 

O envio possui periodicidade eventual (P = 0), devendo ocorrer no início da obrigatoriedade da DeRE ou sempre que houver necessidade de manutenção (inclusão, alteração ou exclusão). Este modelo permite a gestão do ciclo de vida das informações mediante controle de vigência. 

**Exemplos:** D-1001 (Informações do Contribuinte) e D-1011 (Plano Geral de Contas Comentado). 

**Importante:** A existência de eventos de tabela vigentes no Ambiente da DeRE constitui prérequisito para viabilizar a recepção dos eventos periódicos e a aferição dos débitos. 

## **1.1.3 Eventos Periódicos Mensais** 

Destinam-se à captação dos dados necessários para o cálculo do débito agregado do IBS, da CBS e do IS, quando aplicável. São eventos de periodicidade mensal (P = 1) e devem ser transmitidos após o encerramento de cada período de apuração ({perApur}) a que se referem, em observância às datas-limite estipuladas na documentação técnica. 

O Balancete Mensal (D-1101) é principal evento mensal. Paralelamente, cenários específicos exigem o envio obrigatório de eventos de detalhamento auxiliar, a exemplo da Identificação de Aplicações Financeiras (D-1106) e do Débito em Operações com Títulos de Dívida com Oferta Pública (D-2101). 

**Importante:** O processamento destes eventos depende diretamente da existência de eventos de tabela vigentes para a mesma competência. O ciclo mensal de apuração só é concluído mediante o envio obrigatório do evento de Fechamento Mensal (D-1199). 

## **1.1.4 Eventos Periódicos Transacionais** 

Possuem alta granularidade e capturam informações analíticas em nível de operação. São transmitidos de forma contínua ou em lotes com prazos diferenciados (ex: envios diários, semanais) e desempenham três funções primárias: 

- **a)** Identificar o crédito a ser apropriado pelo adquirente do serviço e aferir o débito das operações, conforme a legislação; 

- **b)** Capturar os dados necessários para a correta distribuição federativa da arrecadação; 

- **c)** Viabilizar a operacionalização do _cashback_ e de programas de incentivos de cidadania fiscal. 

**Nota:** As especificações e os leiautes dos eventos periódicos transacionais serão detalhados em versão futura deste Manual. 

**Página 14 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **1.1.5 Eventos de Retorno e Totalização (Série D-9000)** 

Constituem os arquivos eletrônicos em formato XML gerados e devolvidos pelo Ambiente da DeRE ao contribuinte, após o processamento dos lotes de eventos transmitidos. Eventos de retorno atestam o status da transmissão (sucesso ou erro), devolvem o recibo de entrega para controle de versionamento, apontam erros de validação estrutural ou de negócio e apresentam a memória de cálculo com a consolidação das bases de cálculo e dos tributos aferidos. 

**Exemplos:** D-9001 (Retorno – Eventos de Tabela), D-9101 (Retorno Totalizador do Balancete) e D-9199 (Retorno Totalizador – Fechamento Mensal). 

## **1.1.6 Listagem dos Eventos da DeRE** 

## São eventos da DeRE: 

|**Evento**|**Tipo**|**Descrição**|
|---|---|---|
|**D-1001**|Tabela|Informações do Contribuinte|
|**D-1011**|Tabela|Plano Geral de Contas Comentado|
|**D-1101**|Periódico|Balancete Mensal|
|**D-1106**|Periódico|Identificação de Aplicações Financeiras|
|**D-1199**|Periódico|Fechamento Mensal|
|**D-2101**|Periódico|Débito em Operações com Títulos de Dívida com Oferta Pública<br>(exclusivo para Serviços Financeiros)|
|**D-9001**|Retorno|Retorno–Eventos de Tabela|
|**D-9101**|Retorno|Retorno Totalizador–Balancete Mensal|
|**D-9106**|Retorno|Retorno Totalizador–Identificação de Aplicações Financeiras|
|**D-9121**|Retorno|Retorno Totalizador–Débito em Operações com Títulos de<br>Dívida com Oferta Pública(exclusivo para Serviços Financeiros)|
|**D-9199**|Retorno|Retorno Totalizador–Fechamento Mensal|



**Nota:** O detalhamento dos leiautes encontra-se no documento ‘Leiautes da DeRE – Eventos’. 

## **1.2 TABELAS DO SISTEMA (TABELAS DE DOMÍNIO)** 

As tabelas do sistema, também denominadas como tabelas de domínio ou tabelas de códigos auxiliares, são conjuntos de valores e códigos padronizados. São o repositório oficial e centralizado de códigos, classificações e parâmetros utilizados para a validação das regras de negócio e para a correta apuração dos tributos. O conteúdo dessas tabelas é alterado somente mediante a publicação de novo ato administrativo conjunto. 

Seu propósito é padronizar a linguagem fiscal, permitindo que o sistema interprete os dados do contribuinte e realize validações automáticas (Regras de Validação). O contribuinte é obrigado a referenciar e utilizar os códigos constantes dessas tabelas para o 

**Página 15 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

preenchimento de campos específicos nos eventos, como os campos de “código de atividade” ou “código de tributação”. São exemplos de tabelas de sistema: 

- <<Tabela 11 – Códigos de Tributação (codTrib)>>; 

- <<Tabela 12 – Códigos de Base de Cálculo (codBC)>>; 

- <<Tabela 21 – Atividades de Serviços Financeiros>>; 

- <<Tabela 22 – Plano de Contas Referencial – COSIF>>; 

- <<Tabela 32 – Plano de Contas Referencial – ANS>>; 

- <<Tabela 41 – Atividades de Concursos de Prognósticos>>. 

**Nota:** A íntegra das tabelas de sistema vigentes encontra-se no documento ‘Leiautes da DeRE - Anexo I – Tabelas’. 

## **1.3 REPRESENTAÇÃO DOS LEIAUTES (XML)** 

O contribuinte deve gerar o arquivo eletrônico da DeRE em formato estruturado XML ( _eXtensible Markup Language_ ), de acordo com o leiaute, as tabelas de códigos e as regras de validação estabelecidas neste manual e nas publicações oficiais dos leiautes da DeRE. A geração desse arquivo é de responsabilidade do contribuinte. 

As informações econômico-fiscais e contábeis que lastreiam a DeRE devem ser extraídas da escrituração do contribuinte, a qual deve estar em conformidade com as Normas Brasileiras de Contabilidade (NBC) e, quando aplicáveis, com os planos de contas e regulamentações do respectivo órgão regulador. 

O contribuinte deve gerar arquivos XML consoante os _schemas_ (XSD) oficiais publicados no Portal Nacional da Reforma Tributária. 

Cada evento é descrito por meio de duas tabelas complementares: a Estrutura Hierárquica (Resumo) e a Especificação Técnica (Detalhamento). 

## **1.3.1 Estrutura Hierárquica do Evento (Resumo)** 

A primeira tabela do leiaute apresenta a visão lógica e o encadeamento dos grupos de informações (nós do XML), permitindo a compreensão da árvore hierárquica do evento. 

|**Coluna**|**Descrição**|
|---|---|
||Indica a profundidade hierárquica do grupo dentro da árvore XML. O Nível 1 representa|
|**Nível**|sempre o envelope raiz do evento. Níveis subsequentes (2, 3, etc.) indicam o grau de<br>subordinação (aninhamento) do grupo.|
|**Grupo**|Identifica o nome da_tag_do grupo (nó complexo) no arquivo XML.|
|**Grupo Pai**|Indica o nome do registro hierarquicamente superior ao qual o grupo atual está subordinado.|
|**Descrição**|Texto explicativo sobre a finalidade do grupo de informações no contexto do evento.|



**Página 16 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

|**Coluna**|**Descrição**|
|---|---|
|**Ocorr**<br>**(Ocorrência)**|Define a cardinalidade do grupo, indicando a quantidade mínima e máxima de repetições<br>permitidas. A notação segue o padrão “min-max”:<br>**1-1**: Obrigatório e único. Deve ocorrer exatamente uma vez;<br>**0-1**: Facultativo (ou condicional) e único. Pode não existir ou existir uma única vez;<br>**1-N**: Obrigatório e múltiplo. Deve ocorrer pelo menos uma vez, sem limite superior definido<br>(ou limitado a N ocorrências conforme regra específica);<br>**0-N**: Facultativo e múltiplo. Pode não existir ou ocorrer múltiplas (N) vezes.|
|**Chave**|Indica se o grupo ou campos dentro dele compõem a chave única de identificação daquele<br>registro dentro de uma lista.|
||Estabelece a regra de negócio que determina a obrigatoriedade lógica do preenchimento,<br>complementando a validação estrutural (ex: O, F, N ou expressão lógica):<br>**O (Obrigatório)**: O grupo deve ser sempre informado;|
|**Condição**|**F (Facultativo)**: O preenchimento é opcional, a critério do declarante.;<br>**OC (Obrigatório Condicional)**: Obrigatório se a informação existir. A prestação de<br>informações naquele grupo é obrigatória caso o contribuinte possua a informação;<br>**N (Não informar)**: Uso reservado ou descontinuado. Não deve constar no XML.|



## **1.3.2 Especificação Técnica de Campos (Detalhamento)** 

A segunda tabela documenta cada nó da árvore XML listado na estrutura hierárquica. É a referência técnica para o desenvolvimento da geração do arquivo, contendo metadados de sintaxe e regras semânticas. 

|**Coluna**|**Descrição**|
|---|---|
|**#**|Número sequencial para referência no leiaute.|
|**Grupo/Tag**|Identifica o nome exato da_tag_no arquivo XML. Pode referir-se a um Grupo ou a um<br>Elemento.|
|**Grupo Pai**|Referencia o nó superior imediato onde este elemento ou grupo está inserido.|
|**Cat**<br>**(Categoria)**|Define a natureza do item na estrutura XSD:<br>**G (Grupo):**Campo que agrupa outros campos ou grupos (corresponde aos itens da Tabela<br>de Resumo).<br>**E (Elemento):**Campo de dado que carrega a informação final.<br>**A (Atributo):**Informação que qualifica uma_tag_(geralmente utilizado para atributos de ID ou<br>versão).|
|**Tipo**|Define o tipo primitivo de dado aceito no campo:<br>**C (Caractere):**Texto alfanumérico;<br>**N (Numérico):**Apenas números; e<br>**D (Data):**Formato AAAA-MM-DD.|
|**Ocorr**<br>**(Ocorrência)**|Segue a mesma lógica da Tabela Hierárquica (1-1, 0-1, etc.), definindo a obrigatoriedade do<br>campo individualmente. A notação segue o padrão “min-max”:<br>**1-1:**Obrigatório e único. Deve ocorrer exatamente uma vez;<br>**0-1:**Facultativo (ou condicional) e único. Pode não existir ou existir uma única vez;<br>**1-N:**Obrigatório e múltiplo. Deve ocorrer pelo menos uma vez, sem limite superior definido<br>(ou limitado a N ocorrências conforme regra específica);<br>**0-N:**Facultativo e múltiplo. Pode não existir ou ocorrer múltiplas (N) vezes.|



**Página 17 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

|**Coluna**|**Descrição**|
|---|---|
|**Tam**<br>**(Tamanho)**|Indica o limite máximo de caracteres permitidos para o campo, podendo ser:<br>**Fixo (N):**Um único número indica tamanho fixo obrigatório.<br>Ex: 11 (CPF deve ter exatamente 11 dígitos)<br>**Intervalo (Min-Max):**Hífen indica tamanho variável dentro de um limite.<br>Ex: 1-60 (Nome pode ter de 1 a 60 caracteres).<br>**Discreto (A,B):**Vírgula indica opções de tamanhos fixos excludentes.<br>Ex: 8,11 (O campo aceita apenas 8 caracteres ou 11 caracteres, não aceitando 9 ou 10). Comum<br>para campos com opções, como inscrição CNPJ raiz ou CPF.|
|**Dec**<br>**(Decimais)**|Aplicável apenas para tipo N. Indica a quantidade de casas decimais permitidas. O separador<br>decimal não conta como parte do tamanho, mas deve ser usado no XML.<br>**0:**Número inteiro.|
||**>0:**Número com precisão decimal (ex: 2 para valores monetários, 4 para alíquotas).|
|**Descrição**|Descreve o conteúdo esperado do campo e, frequentemente, as regras de validação ou os<br>valores válidos (ex: “Preencher com [0, 1, 2]”).|



## **1.4 CONVENÇÕES TIPOGRÁFICAS E PADRÕES DE LEITURA** 

## **1.4.1 Referências a Campos, Tabelas e Regras** 

A leitura e a identificação visual dos elementos técnicos ao longo da documentação obedecem à seguinte formatação: 

- **Campos do Leiaute:** São representados entre chaves. A menção direta a um campo é grafada isoladamente como <mark>{nomeDoCampo}.</mark> Quando a citação for explicativa, o nome descritivo antecede a representação técnica. Exemplo: “quando o código de tributação ( <mark>{codTrib}</mark> ) for relativo a uma receita tributável...”; 

- **Tabelas de Domínio:** São representadas entre sinais de menor e maior duplos. Exemplo: <<Tabela 11 – Códigos de Tributação (codTrib)>>. A íntegra destas tabelas está prevista no Anexo I dos leiautes da DeRE; 

- **Regras de Validação:** São representadas entre aspas simples e geralmente iniciadas pelo prefixo ‘RN’. Exemplo: ‘RN - Unicidade Recepção Evento’. A íntegra destas regras está prevista no Anexo II dos leiautes da DeRE; 

- **Valores Literais:** Os conteúdos, códigos ou valores literais a serem preenchidos dentro de um campo são representados entre colchetes. Exemplo: [1.00]. 

**Página 18 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **1.4.2 Padrões e Nomenclatura de Campos** 

As tags XML da DeRE adotam prefixos e sufixos semânticos que indicam a natureza do dado esperado no campo. São exemplos de padrões utilizados: 

|**Prefixo / Sufixo**|**Descrição**|**Exemplos**<br>**Práticos**|
|---|---|---|
|**v (valor)**|Representa grandezas monetárias informadas com o número<br>de casas decimais especificadas no leiaute|{vBC},{vApur}|
|**p (percentual)**|Representa taxas, alíquotas ou coeficientes|{pIBSMun},{pCBS}|
|**c / cod (código)**|Representam códigos de identificação ou chaves de tabelas de<br>domínio|{cCta},{codBCN}|
|**ch (chave)**|Representa chaves de acesso de documentos fiscais<br>eletrônicos|{chNFSe},<br>{chDeRE}|
|**x / desc**<br>**(descrição)**|Representam textos descritivos ou nomenclaturas|{xDetBC},<br>{descCta}|
|**ind (indicador)**|Representa campos de opções paramétricas, lógicas ou<br>indicadores de status|{indTribISS}|
|**dh (data e hora)**|Representa marcações temporais completas (timestamp)|{dhRecepcao}|
|**dt (data)**|Representa datas específicas de calendário|{dtAquisicao}|
|**tp (tipo)**|Representa a classificação tipológica de uma informação|{tpAmb},{tpOper}|
|**per (período)**|Representa um período de apuração ou de origem|{perApur},<br>{perOrigem}|
|**n / nr (número)**|Representam numerações de controle ou identificadores<br>numéricos|{nItem},{nrRecibo}|
|**id / ide**<br>**(identificação)**|Representam grupos ou campos de identificação de instâncias<br>únicas|{ideEvento},<br>{ideContrib}|
|**det (detalhe)**|Representa grupos hierárquicos de detalhamento, geralmente<br>para estruturas repetitivas|{detBC}|
|**BC (base de**<br>**cálculo)**|Utilizado para identificar campos formadores de bases de<br>cálculo|{vBCIBS}|
|**seq (sequencial)**|Representa números sequenciais de controle interno ou de<br>versão|{seqEvento}|
|**tot (total)**|Representa totalizadores matemáticos|{vIBSTot},<br>{vApurTot}|



## **1.4.3 Padrão Numérico e Decimal** 

A DeRE utiliza a formatação padronizada para sistemas XML. As grandezas numéricas devem ser declaradas observando as seguintes regras: 

- **Separador Decimal:** Utiliza-se exclusivamente o ponto (.) como separador decimal. O uso de vírgulas ou separadores de milhar é proibido; 

**Página 19 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **Representação de Percentuais:** As alíquotas, taxas, coeficientes ou índices percentuais são expressos de forma direta e absoluta, sem o símbolo de porcentagem (%) e sem conversão para base centesimal. O preenchimento deve respeitar estritamente o número de casas decimais exigido no leiaute. 

**Exemplo:** Uma alíquota de 3% em um campo que exige seis casas decimais deve ser informada como [3.000000]. 

## **1.5 REGRAS DE PREENCHIMENTO E CARACTERES ESPECIAIS (XML)** 

O formato XML possui caracteres reservados que não podem ser utilizados diretamente no conteúdo dos campos (tipo “C”). Caso necessite informar um desses caracteres, eles devem ser substituídos pela sequência de “escape” correspondente: 

|**Caractere**|**Sequência de Escape XML**|
|---|---|
|< (menor que)|&lt|
|> (maior que)|&gt|
|& (e comercial)|&amp|
|“ (aspas duplas)|&quot|
|‘ (apóstrofo)|&apos|



## **2 FLUXO DE TRANSMISSÃO E PROCESSAMENTO** 

O fluxo operacional da DeRE é implementado com uma arquitetura orientada a eventos, visando alta performance e escalabilidade. O contribuinte deve gerar o arquivo, assiná-lo digitalmente e transmiti-lo ao Ambiente Nacional da DeRE por meio de _Web Service_ . 

## **2.1 ASSINATURA DIGITAL (CERTIFICAÇÃO ICP-BRASIL)** 

A assinatura digital do arquivo eletrônico é obrigatória e essencial para garantir a autenticidade, a integridade dos dados e a validade jurídica da informação prestada. 

O evento (arquivo XML) deve ser assinado digitalmente pelo contribuinte, ou pelo representante legal da entidade contribuinte ou por procurador devidamente constituído. 

## **2.1.1 Requisitos Técnicos do Certificado Digital** 

Para a assinatura digital dos arquivos, é obrigatória a utilização de certificado digital válido, emitido por Autoridade Certificadora credenciada pela Infraestrutura de Chaves Públicas Brasileira (ICP-Brasil). O certificado não deve estar revogado ou expirado no momento da transmissão, garantindo a integridade e a autoria dos eventos enviados ao Ambiente Nacional da DeRE. 

**Página 20 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **2.1.2 Regras de Permissão de Assinatura e Acesso** 

A permissão para assinar os eventos e acessar as consultas de resultado do processamento de lotes é controlada pelo sistema, que consulta bases cadastrais externas (Sistema CNPJ) e de autorização (Sistema de Procurações Eletrônicas). 

O evento somente será validado se a assinatura digital for realizada por um dos seguintes números de inscrição: 

- **a)** O CNPJ da matriz do contribuinte; 

- **b)** O CPF do representante legal da matriz do contribuinte, conforme identificado na base de dados do Sistema CNPJ; ou 

- **c)** O CPF ou CNPJ de procurador eletrônico devidamente habilitado no Sistema de Procurações Eletrônicas, com a outorga válida na data do processamento do evento. 

## **2.1.3 Implicações Legais** 

A transmissão do evento com a assinatura digital confere validade jurídica e tem implicações fiscais. O contribuinte é o único responsável pela veracidade, consistência e integridade dos dados informados na DeRE, devendo conservar os documentos contábeis e fiscais que deram origem às informações contidas no evento pelo prazo estabelecido na legislação tributária. 

## **2.2 TRANSMISSÃO POR LOTES DE EVENTOS (** **_APIS_ )** 

A transmissão dos arquivos eletrônicos para o Ambiente Nacional da DeRE é realizada obrigatoriamente por meio de _APIs_ . Essa metodologia assegura a escalabilidade, a alta disponibilidade e a padronização no recebimento das informações. 

Os eventos (arquivos XML individuais) devem ser agrupados em lotes para otimizar o fluxo de comunicação. 

## **2.2.1 Gerenciamento de Pré-Requisitos e Dependências** 

O contribuinte deve observar a ordem lógica e as dependências hierárquicas entre os eventos. A recepção e o registro de “Informações do Contribuinte” e do “Plano Geral de Contas Comentado - PGCC” são pré-requisitos para possibilitar a validação dos eventos dos Balancetes e Identificação dos adquirentes (transacionais). 

O sistema exige que o evento referenciado esteja ativo e sem erros na base de dados no momento da validação do evento dependente. 

Instruções operacionais que merecem destaque: 

- **a) Validação por Recibo:** o contribuinte deve utilizar o protocolo de recebimento do lote para consultar recibo de processamento no retorno dos totalizadores e confirmar o _status_ “Sucesso” de um evento de tabela antes de transmitir qualquer evento periódico que dependa dele; e 

**Página 21 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **b) Impacto de Falha:** se o evento Plano Geral de Contas Comentado for transmitido e rejeitado por erros, ele não será gravado no sistema. Consequentemente, o balancete mensal não será aceito caso não exista um PGCC ativo para o período. 

## **2.3 PROCESSAMENTO ASSÍNCRONO** 

O sistema utiliza o método de processamento assíncrono, o que significa que a validação detalhada dos dados não é imediata e ocorre em segundo plano, desacoplada da recepção do arquivo. O fluxo assíncrono ocorre da seguinte forma: 

- **a) Envio:** o contribuinte transmite o lote de eventos via _API_ ; 

- **b) Recepção Imediata:** o sistema realiza uma validação inicial rápida (sintática e de assinatura); 

- **c) Protocolo:** emite um protocolo de recebimento imediato; 

- **d) Enfileiramento:** o lote é armazenado em uma fila de processamento; e 

- **e) Processamento Detalhado:** um componente especializado executa o processamento sequencial de cada evento no lote, aplicando as regras de negócio. 

Essa metodologia garante a escalabilidade e a alta disponibilidade do serviço de recepção, acomodando picos de carga e evitando o bloqueio da conexão do usuário/sistema remetente. O contribuinte deve, posteriormente, consultar o resultado desse processamento. 

## **2.4 PROTOCOLO DE RECEBIMENTO VERSUS RECIBO DE PROCESSAMENTO** 

É importante diferenciar os dois comprovantes emitidos pelo sistema: 

|**Tipo de**<br>**Comprovante**|**Emissão**|**Finalidade**|**Validade Jurídica**|
|---|---|---|---|
|**Protocolo de**<br>**Recebimento**|Imediata, após a<br>entrega do lote.|Confirma apenas a**entrega** do<br>lote e o recebimento pelo<br>Ambiente da DeRE.|Não atesta o cumprimento da<br>obrigação acessória. Serve como<br>identificador provisório para<br>consulta.|
|**Recibo de**<br>**Processamento**|Após a conclusão do<br>processamento<br>assíncrono.|Confirma o**processamento bem-**<br>**sucedido**e a validação completa<br>de cada evento dentro do lote.|É o comprovante definitivo do<br>cumprimento da obrigação<br>acessória e formaliza os fatos<br>declarados.|



O contribuinte deve utilizar o número do protocolo de recebimento para consultar o _status_ do processamento e obter o recibo de processamento de cada evento. O recibo de processamento inclui o seu status (Sucesso, Erro ou Aviso) e o número de identificação único gerado para o evento. 

**Página 22 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **3 REGRAS DE INCLUSÃO E MANUTENÇÃO DE INFORMAÇÕES** 

A arquitetura da DeRE adota dois modelos distintos para a manutenção (alteração e exclusão) de eventos, dependendo da natureza do evento: 

- **a) Modelo de Vigência/Validade (Linha do Tempo):** Aplicável exclusivamente aos eventos de tabela (Ex: D-1001, D-1011). A unicidade é dada pelas datas de validade do evento; 

- **b) Modelo de Recibo:** Aplicável aos eventos periódicos. A unicidade é dada pelo período de apuração <mark>({perApur})</mark> e a manutenção exige o número do recibo anterior. 

**Nota:** Enquanto o evento de Fechamento Mensal (D-1199) não for transmitido, o contribuinte pode realizar inclusão, alteração ou exclusão dos eventos periódicos previamente enviados, não configurando uma retificação formal. 

**3.1 INCLUSÃO E MANUTENÇÃO DE EVENTOS DE TABELA (MODELO DE VIGÊNCIA/VALIDADE)** 

Os eventos de tabela não utilizam o conceito de “período de apuração”, mas sim de “Linha do Tempo”. A chave de identificação destes registros é composta pelas datas de início <mark>({iniValid})</mark> e fim de validade <mark>({fimValid})</mark> . 

- **3.1.1 Inclusão de Eventos de Tabela** 

Caracteriza-se pela inclusão ( <mark>{tpOper}</mark> = [1]) de um evento com uma data de início <mark>({iniValid})</mark> inédita para aquele tipo de evento. 

## **_3.1.1.1 Eventos com Validade Aberta/Flexível_** 

Se o campo <mark>{fimValid}</mark> não for informado, o evento permanece vigente indefinidamente até que uma nova inclusão com <mark>{iniValid}</mark> futura o encerre automaticamente (corte temporal pelo sistema). 

**Exemplo:** Inclusão de evento com vigência/validade indeterminada (período aberto). 

**Cenário:** 

**1. Evento A (Vigente):** O contribuinte possui um Plano de Contas (D-1011) ativo, enviado anteriormente com <mark>{iniValid}</mark> = 01/01/X1 e <mark>{fimValid}</mark> em branco (vigência indeterminada); 

**2. Necessidade de Negócio:** A partir de 01/02/X1, o declarante precisa utilizar uma nova estrutura de contas (ex: criação de novas contas analíticas ou mudança de código de tributação). 

**Procedimento Correto:** 

- O contribuinte deve enviar um novo evento de inclusão ( <mark>{tpOper}</mark> = [1]); 

- Preenchimento: <mark>{iniValid}</mark> = 01/02/X1 e <mark>{fimValid}</mark> em branco; 

- **Atenção:** Não é necessário enviar um evento de alteração para “fechar” o Evento A. 

**Página 23 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**Comportamento do Sistema (Ajuste Automático):** 

Ao recepcionar o novo evento (Evento B), o sistema identifica a intersecção com o período aberto do Evento A e aplica o corte temporal automático: 

**1.** O sistema atribui internamente a data 31/01/X1 como data fim efetiva ( <mark>{fimValidEfetiva}</mark> ) do Evento A; 

**2.** O Evento B passa a vigorar a partir de 01/02/X1, mantendo-se vigente por prazo indeterminado até que uma nova inclusão futura ocorra. 



## **_3.1.1.2 Eventos com Validade Fechada/Rígida_** 

Ocorre quando o contribuinte informa, no momento do envio, tanto a data de início <mark>({iniValid}</mark> ) quanto a data de fim ( <mark>{fimValid})</mark> . 

**Comportamento do Sistema (Imutabilidade):** Ao definir uma data fim, o contribuinte estabelece um “Período Fechado”. Essa informação ( <mark>{fimValid}</mark> ) é imutável pelo sistema, só podendo ser alterada pelo contribuinte. Não se aplica o ajuste automático de vigência sobre períodos fechados. 

**Consequência:** Qualquer tentativa de enviar um novo evento de inclusão que coincida ou sobreponha, total ou parcialmente, esse período “rígido” resultará na rejeição do arquivo (erro). 

**Exemplo:** Inclusão de evento com vigência determinada (período fechado). 

**Cenário:** 

**1. Evento A (Vigência Indeterminada):** O contribuinte envia, em Janeiro/X1, um evento (ex: Plano de Contas) com <mark>{iniValid}</mark> = 01/01/X1 e <mark>{fimValid}</mark> em branco (aberto); 

**2. Evento B (Vigência Determinada/Fechada):** Posteriormente, o contribuinte envia um novo evento de inclusão ( <mark>{tpOper}</mark> = [1]), referente a uma situação transitória, definindo <mark>{iniValid}</mark> = 01/02/X1 e <mark>{fimValid}</mark> = 28/02/X1. 

**Comportamento do Sistema (Corte Temporal):** 

Ao processar o Evento B, o sistema identifica a sobreposição e aplica automaticamente o encurtamento da vigência do Evento A, e com isso: 

- A data fim efetiva ( <mark>{fimValidEfetiva})</mark> do Evento A é alterada pelo sistema para 31/01/X1 

- (o dia imediatamente anterior ao início do Evento B); 

- Não é necessário enviar um evento de alteração para o Evento A; o corte é 

- automático. 

**Página 24 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**Atenção (Geração de Lacuna):** O encerramento automático do Evento A é definitivo. Após o término da vigência do Evento B (em 28/02/X1), não ocorre a restauração automática das regras do Evento A (salvo se houver a exclusão do Evento B, quando permitida). **Consequência:** A partir de 01/03/X1, o contribuinte ficará sem cobertura de tabela (lacuna de vigência), o que impedirá a recepção de eventos periódicos que dependam daquela tabela naquele período. **Ação Necessária:** Para cobrir o período a partir de março, o contribuinte deverá enviar um novo Evento com <mark>{iniValid}</mark> = 01/03/X1 (e fim aberto, se desejar retomar a vigência indeterminada). 



## **_3.1.1.3 Validação de Unicidade_** 

O sistema verificará se já existe um evento “ativo” (mesma chave/períodos de vigência) na base de dados. Caso positivo, a inclusão será rejeitada com erro de duplicidade. 

## **3.1.2 Alteração de Eventos de Tabela** 

Caracteriza-se pelo envio de um evento com <mark>{tpOper}</mark> = [2] (alteração). Para que o sistema identifique qual registro deve ser modificado, o contribuinte deve informar, no grupo <mark>{idePeriodo},</mark> a chave temporal exata do evento original: 

- **a)** Apenas <mark>{iniValid}</mark> (se o evento original possui validade aberta/indeterminada); ou 

- **b)** <mark>{iniValid}</mark> e <mark>{fimValid}</mark> (se o evento original possui validade fechada/determinada). 

**Regra Geral:** O contribuinte deve reenviar o evento completo, preenchendo todos os campos e ocorrências (inclusive os que não sofreram alteração), pois a alteração substitui integralmente o registro anterior na base de dados. 

## **_3.1.2.1 Alteração de Eventos (Mesma Vigência)_** 

Utilizado para corrigir erros de preenchimento ou atualizar dados (ex: descrição da conta, mapeamento do codTrib) sem modificar o período de validade. 

**Procedimento:** Preencher o grupo <mark>{idePeriodo}</mark> com a chave original e deixar o grupo <mark>{novaValidade}</mark> vazio. 

**Comportamento:** O sistema localiza o evento pela chave e sobrescreve as informações de conteúdo. 

**Página 25 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **_3.1.2.2 Alteração de Validade (Novo Período)_** 

Quando permitido, para corrigir a vigência de um evento já processado, o contribuinte deve enviar um evento de alteração <mark>({tpOper}</mark> = [2]), preenchendo obrigatoriamente dois grupos distintos: 

**1. Grupo** **<mark>{idePeriodo}</mark> (Original)** : Informe as datas de início e fim originais para que o sistema localize o evento no banco de dados; 

**2. Grupo** **<mark>{novaValidade}</mark> (Substituto):** Informe as novas datas de início e fim desejadas. Estes valores sobrescreverão a chave temporal antiga, corrigindo o registro. 

## **3.1.3 Exclusão de Evento de Tabela** 

Quando permitida, a exclusão remove um período de validade específico da base de dados, perdendo seus efeitos jurídicos e o cumprimento da obrigação no prazo. 

**Requisito:** Deve ser informada no grupo <mark>{idePeriodo}</mark> a mesma chave temporal <mark>({iniValid}</mark> e, se houver, <mark>{fimValid}</mark> ) do evento que se deseja excluir. 

**Motivo da Exclusão:** É obrigatório informar o campo <mark>{motExcl}</mark> . Caso a exclusão decorra de determinação judicial ou administrativa ( <mark>{motExcl}</mark> = [1]), deve-se informar também o número do processo ( <mark>{nrProc})</mark> . 

**Atenção (Risco de Lacuna):** A exclusão de um evento intermediário pode gerar lacunas na linha do tempo, o que impedirá a recepção de eventos periódicos que dependam daquela tabela naquele período. 

**Consequência:** A exclusão pode gerar pendências de obrigação se realizada fora do prazo ou sem a substituição por outro evento válido. 

**3.2 INCLUSÃO E MANUTENÇÃO DE EVENTOS PERIÓDICOS MENSAIS (MODELO DE RECIBO)** 

A manutenção dos eventos periódicos baseia-se no número do recibo da transmissão anterior <mark>{nrRecibo}</mark> e no período de apuração <mark>{perApur},</mark> pois referem-se a fatos ocorridos em um mês específico. 

## **3.2.1 Inclusão (Envio Original)** 

É o primeiro envio de um evento para um determinado período de apuração <mark>{perApur}.</mark> O sistema retorna um número de recibo que se torna a “identidade” daquela transação. 

**Página 26 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **3.2.2 Alteração (Substituição Integral)** 

Diferente das tabelas, os eventos periódicos não são alterados campo a campo. Para modificar qualquer informação, o contribuinte deve enviar um evento de alteração, com todos os campos presentes no leiaute, inclusive os não alterados. 

**Regra de Substituição:** O evento de alteração deve conter o indicador de alteração <mark>{tpOper}</mark> = [2] e obrigatoriamente, o número do recibo <mark>{nrRecibo}</mark> do evento que está sendo substituído. 

**Efeito:** O evento de alteração substitui integralmente o evento anterior para todos os efeitos legais. Não há “histórico de validade” dentro do mesmo mês; vale a última versão transmitida. 

## **3.2.3 Exclusão de Evento Periódico** 

Utilizada para tornar sem efeito um evento enviado indevidamente. 

**Procedimento:** Envia-se um evento específico de exclusão ( <mark>{tpOper}</mark> = [3]), referenciando o número do recibo e do período de apuração do evento a ser excluído. 

**Consequência:** O evento original é logicamente removido da apuração. 

**4 A FORMAÇÃO DAS BASES DE CÁLCULO (CODBC)** 

## **4.1 CONCEITO E FINALIDADE** 

A Matriz de Regras do Código de Base de Cálculo ( <mark>{codBC})</mark> é uma tabela de domínio interna e estrutural do sistema da DeRE. Sua função é agregar os valores declarados pelo contribuinte e aplicar as regras para a formação das bases de cálculo do IBS, da CBS e do IS, quando aplicável. 

Diferentemente do Código de Tributação <mark>({codTrib})</mark> , que reflete a classificação da rubrica contábil isolada (mapeada no evento D-1011), o <mark>{codBC}</mark> representa o agrupamento de diversas rubricas que compartilham a mesma natureza de incidência, regra de creditamento, modalidade de distribuição e alíquota. O sistema da DeRE utiliza a <<Tabela 12 – Códigos de Base de Cálculo (codBC)>>, (prevista no Anexo I dos leiautes da DeRE), para consolidar os valores informados pelo contribuinte e aplicar as fórmulas matemáticas que definirão o valor final da base de cálculo. 

**4.2 IDENTIDADE E PARÂMETROS FORMADORES DOS CÓDIGOS DE BASE DE CÁLCULO** 

Constituem os parâmetros formadores dos Códigos de Base de Cálculo: 

- **a) Regime Tributário:** Identifica o Regime Específico da operação (ex.: Serviços Financeiros, Planos de Assistência à Saúde, Concursos de Prognósticos); 

**Página 27 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

   - **b) Tipo de Aferição:** Determina se o débito é aferido de forma agregada pelo Balancete Mensal (D-1101) ou de forma individualizada por eventos transacionais; 

   - **c) Incidência de ISS:** Determina o comportamento da operação frente às regras de transição. Para os Serviços Financeiros, a indicação de incidência ou não do ISS altera a alíquota aplicável do IBS e da CBS para a formação da base; 

   - **d) Regra de Distribuição:** Define a distribuição da arrecadação do IBS para os entes subnacionais (Municípios e Estados), garantindo a aplicação do Princípio do Destino; 

   - **e) Regra de Creditamento:** Especifica o comportamento da base de cálculo quanto à geração de direito a crédito financeiro para o adquirente, assegurando a não cumulatividade. 

- **4.3 DINÂMICA DE FORMAÇÃO DA BASE DE CÁLCULO** 

Ao recepcionar o evento D-1199 (Fechamento Mensal), o ambiente da DeRE processa os saldos totalizados no Balancete Mensal (D-1101) e nos eventos auxiliares (como D-1106 e D-2101), aplicando as regras de cálculo de acordo com a matriz do <mark>{codBC}.</mark> Como regra geral, o processamento ocorre da seguinte forma: 

**1. Receitas Tributáveis:** O sistema executa o somatório dos saldos de todas as contas analíticas que possuam códigos de tributação classificados como receitas tributáveis; 

**2. Despesas Dedutíveis:** O sistema realiza o somatório das despesas tributárias dedutíveis permitidas pela legislação. Quando a atividade exigir a segregação das despesas de captação, o sistema aplica coeficientes de rateio, conforme item 4.3.1 deste Capítulo, para deduzir proporcionalmente as despesas em relação à receita de cada atividade; 

**3. Reversões de Despesas Dedutíveis:** Quando houver receitas com exportação ou com atos cooperados, serão realizados cálculos para reversão das despesas dedutíveis na proporção dessas receitas, conforme item 4.3.1 deste Capítulo; 

**Observação:** Concluído o fluxo informacional da DeRE, os créditos das aquisições de bens e serviços que foram deduzidos da base de cálculo e, posteriormente, objeto de reversão, serão garantidos ao contribuinte na mesma proporção dos estornos, quando do processamento da apuração assistida. 

**4. Aferição da Base de Cálculo Bruta:** O resultado entre o somatório das receitas tributáveis, subtraído do somatório das despesas dedutíveis, e adicionado à soma das reversões compõe a base de cálculo bruta do período; 

**Página 28 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**5. Exclusão de Tributos Embutidos (** **_Gross-Down_ ):** Para garantir o cumprimento da regra constitucional de que o tributo não pode integrar sua própria base de cálculo, é realizado o cálculo de exclusão do tributo de sua base ( _gross-down_ ). Para isso, o sistema utiliza a fórmula de divisão pelo fator composto das alíquotas incidentes: <mark>{vBCBruta}</mark> / (1 + ( <mark>{pIBSMun}</mark> + <mark>{pIBSUF}</mark> + <mark>{pCBS})</mark> / 100). O valor extraído resulta na base de cálculo efetiva final, representada por <mark>{vBCIBS}</mark> e <mark>{vBCCBS}</mark> ; 

**Observação:** Para o cálculo de exclusão do Imposto Seletivo (IS) deve ser observada a seguinte variação da fórmula: ( <mark>{vBCBrutaIS}</mark> / ((1 + ( <mark>{pIS}</mark> / 100)) * (1 + (( <mark>{pIBSMun}</mark> + <mark>{pIBSUF}</mark> + <mark>{pCBS})</mark> / 100))) * <mark>{pIS}</mark> / 100) 

**Legenda dos Campos:** <mark>{vBCBruta}:</mark> Valor da base de cálculo bruta antes do _gross-down_ ; <mark>{pIBSMun}:</mark> Alíquota do IBS municipal; <mark>{pIBSUF}:</mark> Alíquota do IBS estadual; <mark>{pCBS}:</mark> Alíquota da CBS; <mark>{pIS}:</mark> Alíquota do Imposto Seletivo; <mark>{vBCIBS}:</mark> Valor da base de cálculo do IBS; <mark>{vBCCBS}:</mark> Valor da base de cálculo da CBS. 

   **6.** O resultado do cálculo consolidado é devolvido ao contribuinte no evento D-9199 (Retorno Totalizador do Fechamento Mensal), segregado explicitamente por <mark>{codBC}.</mark> 

- **4.3.1 Coeficientes de Rateio e de Reversão** 

Para operacionalizar a reversão de deduções vinculadas a operações não tributadas e a correta alocação de custos de captação, o sistema da DeRE aplica coeficientes sistêmicos de rateio e de reversão. Estes coeficientes são aplicados sempre que o contribuinte declarar despesas de captação quando houver mais de uma atividade com possibilidade de dedução desse tipo de despesa, receitas de exportação (imunes) ou receitas de atos cooperados sujeitas à alíquota zero. 

- **a) Coeficiente de Rateio de Despesas de Captação:** Coeficiente utilizado para ratear as despesas de captação de recursos entre as atividades financeiras que permitem essa dedução. O coeficiente representa o montante da despesa de captação que cada “unidade de receita” carrega consigo. O coeficiente de rateio apurado <mark>({pRateioDespCapt}</mark> ) é multiplicado pela respectiva receita de cada atividade para se obter o valor proporcional da despesa a ser deduzida; 

**Exemplo:** Um coeficiente de 0,5 significa que, para cada R$ 100,00 de receitas auferidas, R$ 50,00 correspondem à despesa de captação vinculada a essa receita. 

**Observação** : Em relação às deduções de captação vinculadas ao valor da parcela de arrendamento mercantil financeiro, somente serão consideradas, para fins de proporcionalização, as parcelas recebidas de não contribuintes do IBS e da CBS. 

**Página 29 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **b) Coeficiente de Reversão de Despesas com Atos Cooperados:** Coeficiente utilizado para reverter as deduções de despesas vinculadas a receitas decorrentes de atos cooperados (sujeitas à alíquota zero). O coeficiente é a razão entre a receita de atos cooperados e a receita total das atividades dos regimes específicos de serviços financeiros. O resultado ( <mark>{pRevDedAtoCoop})</mark> é, então, multiplicado pelo valor das despesas gerais dedutíveis para se obter o montante das reversões a serem realizadas; 

- **c) Coeficiente de Reversão de Despesas de Exportação:** Os coeficientes para reversão de deduções de despesas vinculadas a receitas de exportação no regime específico dos serviços financeiros são calculados, de forma isolada, na proporção das exportações sobre a receita de cada tipo de atividade realizada pelo contribuinte. O coeficiente obtido <mark>({pExportAtividade})</mark> é, então, multiplicado pelas despesas gerais e de captação correspondentes, para se obter o montante das reversões a serem realizadas. 

**Observação 1:** Em relação às indenizações de operações de seguro com segurado residente fora do país, as reversões das indenizações se darão na proporção das receitas recebidas de exportação em razão das receitas recebidas de segurados não contribuintes do IBS e da CBS (residentes ou não no país). 

**Observação 2:** As memórias de cálculo destas variáveis estão detalhadas na <<Tabela 12.1 – Coeficientes de Rateio e Reversão (Auxiliar)>>, prevista no Anexo I dos leiautes da DeRE. 

## **Exemplo de Cálculo (Arrendamento Mercantil Financeiro - Valor da Parcela):** 

||**Operações com**|**Operações com**||
|---|---|---|---|
|**Formação da Base de Cálculo**|**contribuintes**<br>**IBS/CBS**|**não contribuintes**<br>**IBS/CBS**|**Total**|
|Parcelas recebidas nacional|10.000,00|16.000,00|26.000,00|
|Parcelas recebidas exportação|-|12.000,00|12.000,00|
|Total de Receitas|10.000,00|28.000,00|38.000,00|
|Despesa de Captação Total|-|-|19.000,00|
|Proporção Despesa de Captação|5.000,00|14.000,00|*****|
|Reversão de Despesa de Captação|-|6.000,00|******|
|Base de Cálculo IBS/CBS|10.000,00|8.000,00|*******|



***** As despesas de captação (19.000,00) são proporcionalizadas, considerando a participação das receitas com contribuintes (10.000,00) e com não contribuintes (28.000,00) em relação ao somatório desses dois agrupamentos da receita (38.000,00). 

**Observação:** As despesas vinculadas a contribuintes não são dedutíveis da base de cálculo. 

****** As despesas de captação (14.000,00) sofrerão reversão parcial em função das receitas com exportação (12.000,00) em razão das receitas totais das operações com não contribuintes do IBS/CBS (28.000,00). 

******* A base de cálculo bruta do IBS/CBS para não contribuintes será calculada considerando as Parcelas recebidas de residentes no país (16.000,00), subtraindo as despesas de captação (14.000,00) e somando a reversão das despesas em função das receitas com exportação de serviços (6.000,00). 

**Página 30 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**4.3.2 Regra de Transição: Receitas não sujeitas à CBS (sujeitas ao PIS/Cofins)** 

Durante o período de transição, as operações que geram receitas, ainda sujeitas a tributação pelo PIS/Cofins, não sofrem a incidência da CBS, mas submetem-se normalmente à incidência do IBS e do IS, quando aplicável. 

O sistema divide a base de cálculo bruta em duas frações: a ‘Base de Cálculo Geral’, sujeita a todos os tributos (IBS, CBS e IS), e a ‘Base de Cálculo de Transição’, sujeita apenas ao IBS e IS, observados os seguintes passos: 

**Passo 1:** Rateio Proporcional e Formação das Bases Brutas 

O sistema isola as bases e aplica um rateio sobre as deduções gerais e reversões, formando duas bases brutas distintas, uma geral e uma relativa ao período de transição do PIS/Cofins. 

**Passo 2:** Extração dos Tributos Embutidos ( _Gross-down_ ) 

O sistema elimina o efeito da tributação embutida no preço cobrado do adquirente para calcular as bases líquidas. Na base de cálculo do período de transição não há incidência da CBS. 

**Nota:** Quando houver a incidência do IS sobre a operação, a sua alíquota deve compor o divisor de forma acumulada. 

**Passo 3:** Formação da Base de Cálculo Mensal 

A definição das bases de cálculo mensais do IBS, da CBS e do IS ocorre a partir das respectivas bases líquidas apuradas no Passo 2. A base de cálculo da CBS será composta somente pela base líquida geral <mark>({vBCLiqGeral})</mark> , enquanto as bases de cálculo do IBS e a do IS serão compostas pela soma das bases líquidas geral <mark>({vBCLiqGeral}</mark> ) e de transição <mark>({vBCLiqTransiPisCofins}</mark> ). 

**Observação:** As memórias de cálculo destas variáveis estão detalhadas na <<Tabela 12.2 – Variáveis para Composição de Bases de Cálculo (Auxiliar)>>, prevista no Anexo I dos leiautes da DeRE. 

**Página 31 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **Exemplo de Cálculo (Aferição de Bases com IBS, CBS e IS):** 

|**Exclusão de Tributos Embutidos no**<br>**Preço (****_Gross-Down_) **|**Base Geral**|**Base de**<br>**Transição**<br>**PIS/Cofins**|**Totais**|
|---|---|---|---|
|Base de Cálculo Bruta|123.750,00|13.500,00|137.250,00|
|% Tributos*****|**1,375**|**1,350**||
|Base de Cálculo Líquida de IS|90.000,00|10.000,00|100.000,00|
|Valor do IS(Imposto Seletivo)|22.500,00|2.500,00|25.000,00|
|Base de Cálculo Líquida de IBS/CBS|112.500,00|12.500,00|125.000,00|
|Valor do IBS|9.000,00|1.000,00|10.000,00|
|Valor da CBS|2.250,00|-|2.250,00|
||**123.750,00**|**13.500,00**|**137.250,00**|
|*****Quando houver a incidência do Imposto<br>da base de cálculo bruta (_Gross-Down_)<br>sobre o percentual de IS, uma vez que o|Seletivo (IS), o cál<br>será feito acumul<br>último compõe a|culo dos tributos que<br>ando-se os percentu<br>BC dos primeiros.|serão excluídos<br>ais de IBS/CBS|
|**Exemplo de Cálculo do % Tributos**|**Geral**|**Transição**||
|Alíquota do IS ({pIS})|25,00%|25,00%||
|Alíquota do IBS({pIBSTot})|8,00%|8,00%||
|Alíquota da CBS ({pCBS})|2,00%|-||
|% Tributos<br>(1 +{pIS})*(1 +{pIBSTot}+{pCBS})|**1,375**|**1,350**||



## **4.4 MEMÓRIA DE CÁLCULO NOS EVENTOS DE RETORNO** 

A matriz de cálculo opera de forma automatizada durante o processamento pela DeRE. O resultado da aplicação do <mark>{codBC}</mark> é devolvido detalhadamente ao contribuinte no evento D-9199 (Retorno Totalizador do Fechamento Mensal). 

Dentro do grupo de Detalhamento da Base de Cálculo ( <mark>{detBC})</mark> , o contribuinte terá acesso aos seguintes dados: 

- **O Código** **<mark>({codBC})</mark> e a Descrição** **<mark>({xDetBC})</mark> :** A identificação exata do agrupamento de regras que foi aplicado àquelas contas, conforme <<Tabela 12 – Códigos de Base de Cálculo (codBC)>>, prevista no Anexo I dos leiautes da DeRE; 

- **A Memória de Cálculo (** **<mark>{memoriaCalculo})</mark> :** Uma cadeia de texto contendo a demonstração matemática dos valores intermediários processados (receitas, despesas, reversões e _gross-down_ ); 

- **As Bases Efetivas:** A demonstração explícita da base de cálculo gerada para cada tributo e a apuração isolada de eventuais bases de cálculo negativas (para mais informações vide item 4.5 deste Capítulo). 

**Página 32 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **4.5 TRATAMENTO DE BASES DE CÁLCULO NEGATIVAS (BCN)** 

Caso a totalização do <mark>{codBC}</mark> resulte em um valor negativo (deduções superiores às receitas), o sistema da DeRE, automaticamente, zera a base tributável daquele código na competência corrente e faz o seu registro como uma Base de Cálculo Negativa (BCN). Cada Base de Cálculo Negativa possui seu respectivo <mark>{codBCN}.</mark> 

**Observação:** O <mark>{codBCN}</mark> é um código de 13 caracteres, formado pela concatenação do <mark>{codBCNRaiz},</mark> do <mark>{perApur}</mark> e do campo <mark>{seqEvento}</mark> (retornado após o processamento com sucesso do Evento de Fechamento). 

**Legenda dos Campos:** 

<mark>{codBCNRaiz}:</mark> Código identificador raiz da base de cálculo negativa, conforme <<Tabela 12 – Códigos de Bases de Cálculo>>, prevista no Anexo I dos leiautes da DeRE. <mark>{perApur}:</mark> Período de apuração, sendo o ano e mês da competência da declaração (sem máscara). 

<mark>{seqEvento}:</mark> Número sequencial de identificação que indica a versão do evento processado na base de dados (aqui representado com 2 dígitos). O evento original é representado por [00] e para qualquer nova transmissão no mesmo <mark>{perApur}</mark> acrescenta-se essa numeração de forma sequencial (01, 02...). 

**Exemplo:** Para serviços de Crédito, Câmbio e TVM com totalização sob o <mark>{codBC}</mark> igual a [1010], seus respectivos <mark>{codBCNRaiz}</mark> são [ **1010I** ] (para a base de cálculo negativa de IBS) e [ **1010C** ] (para a base de cálculo negativa de CBS). Considerando o respectivo período de apuração de junho de 2026 ( <mark>{perApur}</mark> = [ **202606** ]) e tratando-se do evento original ( <mark>{seqEvento}</mark> = [ **00** ]), o <mark>{codBCN}</mark> será: Para o IBS, <mark>{codBCN}</mark> = **1010I20260600** Para a CBS, <mark>{codBCN}</mark> = **1010C20260600** 

## **4.5.1 Regras de Compensação de Bases de Cálculo Negativa** 

A compensação da base de cálculo negativa só poderá ser utilizada nos períodos de apuração subsequentes para abater bases de cálculo positivas da mesma natureza (mesmo <mark>{codBC}</mark> formador). 

Para o aproveitamento futuro deste saldo, o contribuinte deverá utilizar o exato código raiz da BCN ( <mark>{codBCNRaiz}</mark> ) originado pelo sistema e disponibilizado no evento de retorno totalizador (D-9199). A parametrização para o uso do saldo nos meses subsequentes deve ser informada exclusivamente no evento de fechamento mensal (D1199), por meio do grupo <mark>{gUtilizBCN}.</mark> Neste grupo, o declarante indicará, obrigatoriamente, se deseja utilizar o método de aproveitamento automático (PEPS) ou manual (definindo de forma individualizada como fará o aproveitamento de cada base de cálculo negativa disponível). 

**Observação:** Para os procedimentos de vinculação e transmissão das regras de bases negativas, vide o detalhamento do Evento D-1199 na Seção 2.3 do Capítulo III. Para a verificação do retorno do cálculo processado pelo sistema, vide o Evento D-9199 na Seção 3.4 do Capítulo <u>V.</u> 

**Página 33 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **<mark>CAPÍTULO III – EVENTOS COMUNS A TODOS OS REGIMES SÉRIE (D-1000)</mark>** 

Os eventos da série D-1000 são de uso comum a todos os contribuintes sujeitos à entrega da DeRE. 

## **1 EVENTOS DE TABELA** 

- **1.1 D-1001 – EVENTO DE INFORMAÇÕES DO CONTRIBUINTE** 

## **1.1.1 Conceito e Finalidade** 

É o evento inicial da DeRE e obrigatório, que estabelece a base de informações essenciais para o processamento e validação de todos os demais eventos. 

Ele tem como finalidade fornecer os dados de identificação do contribuinte, incluindo aspectos que influenciam o enquadramento em diferentes regras tributárias. Este evento é utilizado para: 

- **a)** Registrar os dados de identificação do contribuinte; 

- **b)** Informar ao Fisco quais regimes específicos o contribuinte opera (principal e secundário(s)); e 

- **c)** Informar os tipos de fornecimentos <mark>({tpAtividade})</mark> que ele realiza. 

Essas informações são indispensáveis para permitir a validação e a recepção dos demais eventos aplicáveis ao contribuinte. 

**Pré-requisito:** por ele ser o evento inicial, não possui pré-requisitos para sua transmissão. 

- **1.1.2 Frequência e Obrigatoriedade de Envio** 

O envio deve ocorrer no início da obrigatoriedade da entrega da DeRE pelo contribuinte, ou quando houver manutenção dos dados identificadores do contribuinte. 

- **1.1.3 Regras de Negócio e Preenchimento** 

## **_1.1.3.1 Classificação de Regime Tributário Principal_** 

Para a definição do regime tributário principal, deve ser observada a atividade preponderante do contribuinte, assim entendida aquela que o sujeita a órgão regulador governamental (BCB, ANS ou SUSEP), para fiscalização de suas atividades. Na ausência de sujeição a órgão regulador governamental, ou se sujeitando a mais de um órgão regulador, aquela que representa maior parte do seu faturamento no ano anterior ou, em se tratando de início de atividade, da expectativa de faturamento no exercício. 

**Página 34 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**Exemplo:** O “Banco G”. 

**Situação:** Uma instituição financeira complexa opera diversas frentes de negócio sob o mesmo CNPJ raiz: **Atividade A:** Banco (regulado pelo BCB). **Atividade B:** Plano de Saúde de Autogestão para funcionários (regulado pela ANS). **Atividade C:** Loterias (Prognósticos). 

Neste caso, como preencher o evento D-1001? 

|**Hierarquia**|**Grupo de Regime**|**Seleção de Atividades (Códigos)**|
|---|---|---|
|||**01A**- Operações de Crédito<br>**02A**- Câmbio|
|**PRINCIPAL**|**Serviços Financeiros**|**03A**- Títulos e Val. Mobiliários<br>**06A**- Leasing<br>**09A**- Arranjos de Pagamento|
|**SECUNDÁRIO 1**|**Plano de Saúde**|**08A**- Plano de Saúde Modalidade Autogestão|
|**SECUNDÁRIO 2**|**Prognósticos**|**01C**- Modalidades Lotéricas|



## **_1.1.3.2 Contribuintes com Alíquota Zero ou Não Incidência_** 

O contribuinte, mesmo sujeito à não incidência ou à alíquota zero deve enviar esse evento, declarando no campo <mark>{indNatTrib}</mark> do Grupo <mark>{infoContrib}</mark> essa condição específica que vai permitir ao sistema aplicar automaticamente regras de não incidência e alíquota zero. 

## **_1.1.3.3 Identificação de Atividades_** 

O contribuinte deve detalhar as atividades específicas de cada regime principal e/ou secundário, informando nos grupos <mark>{servFinanc}, {plAssistSaude}, {prognosticos}</mark> os tipos de atividades no campo <mark>{tpAtividade}</mark> com os respectivos códigos da tabela de atividades anexa ao leiaute. 

## **1.2 D-1011 – PLANO GERAL DE CONTAS COMENTADO - PGCC** 

O Plano Geral de Contas Comentado - PGCC é um evento de tabela, de envio obrigatório para todos os contribuintes sujeitos à DeRE. 

## **1.2.1 Conceito e Finalidade** 

O PGCC materializa a estrutura contábil necessária para a apuração do débito agregado de IBS e CBS e, portanto, estabelece a conexão entre a contabilidade interna do contribuinte e o código de tributação responsável pelos cálculos dos totalizadores, observando os seguintes pontos: 

**Página 35 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **a) Mapeamento Contábil:** Permitir que o contribuinte realize um mapeamento obrigatório (de-para), vinculando cada conta contábil analítica de sua escrituração ao código de tributação <mark>({codTrib})</mark> correspondente; e 

- **b) Estrutura para Cálculo:** Estruturar as contas de resultado e patrimoniais para que o sistema possa processar o Balancete Mensal e determinar as receitas tributáveis, as deduções e os débitos consolidados. 

Essas informações são indispensáveis para permitir a validação e a recepção dos eventos que possuam dependência ao plano de contas. 

**Pré-Requisitos:** Envio do evento Informações do Contribuinte (D-1001). 

## **1.2.2 Frequência e Obrigatoriedade de Envio** 

O envio deve ocorrer no início da obrigatoriedade da entrega da DeRE pelo contribuinte, ou quando houver manutenção dos dados. 

## **1.2.3 Código de Tributação (** **<mark>{codTrib})</mark>** 

O Código de Tributação – <mark>{codTrib}</mark> é o elemento que conecta a contabilidade do contribuinte com as regras fiscais estabelecidas pela legislação tributária. Deve ser usado um código existente e vigente na Tabela Oficial de Códigos de Tributação (<<Tabela 11 – Códigos de Tributação (codTrib)>>), prevista no Anexo I dos leiautes da DeRE. 

**a) Conceito e Função Estratégica** 

O <mark>{codTrib}</mark> é um código numérico de 9 dígitos que deve ser atribuído pelo contribuinte a cada conta contábil analítica declarada no Plano Geral de Contas Comentado (PGCC). 

Ele define a regra tributária específica (incidência, dedução ou imunidade) à qual o saldo indicado para fins tributários daquela conta contábil está sujeito. Ao vincular cada rubrica contábil a sua regra fiscal, o <mark>{codTrib}</mark> permite que a DeRE calcule a base de cálculo e o débito consolidado do contribuinte. 

- **b) Tipos de Classificação** 

O <mark>{codTrib}</mark> permite que o sistema saiba se a movimentação contábil registrada em determinada conta será tratada, por exemplo: 

**1. Receita Tributável:** indicando a inclusão do valor na base de cálculo do IBS/CBS; 

**2. Despesa Dedutível:** indicando a permissão de dedução na apuração por margem; 

**3. Receita Imune ou Não Tributável:** indicando que o valor está fora do escopo de tributação; e 

**4. Outros Componentes da Base de Cálculo:** incluindo tributos incidentes sobre a operação (ex: ISSQN, PIS/Cofins devidos) ou valores que influenciam o cálculo da margem. 

**Página 36 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

Ele também identifica se a operação é financeira, de saúde ou de prognóstico, permitindo ao sistema classificar a natureza tributária de cada conta de acordo com cada um desses setores. 

## **c) Obrigatoriedade de Classificação das Contas** 

O contribuinte tem a obrigatoriedade e a responsabilidade de atribuir o <mark>{codTrib}</mark> a cada conta analítica <mark>({indCta}</mark> = [A]). O sistema da DeRE não realiza este mapeamento automaticamente. 

**Atenção:** O {codTrib} não pode ser informado em contas sintéticas. 

## **1.2.4 Regras de Negócio e Preenchimento** 

## **_1.2.4.1 Vigência do PGCC e das Contas_** 

As informações do PGCC são mantidas com controle de vigência ( <mark>{iniValid}</mark> e <mark>{fimValid})</mark> . O controle aplica-se tanto ao evento D-1011 (a tabela como um todo) quanto às contas contábeis individualmente. 

**Nota:** Alterações retroativas no D-1011 podem impactar eventos periódicos já processados e, consequentemente, alterar a apuração dos tributos. O tratamento para reprocessamento de períodos anteriores será detalhado em versão futura deste manual. 

## **_1.2.4.2 Tratamento Base das Contas_** 

Para as contas analíticas <mark>({indCta}</mark> = [A]) é obrigatório o preenchimento da descrição detalhada <mark>({descCta})</mark> , que deve ser clara o suficiente para permitir a identificação da natureza exata da operação ali contabilizada. 

Para fins deste manual, as contas de natureza tributária mista serão denominadas “contas mistas”, sendo estas as contas contábeis que, em sua escrituração interna, agregam lançamentos com naturezas fiscais distintas (ex: receitas tributáveis e isentas, despesas dedutíveis e não dedutíveis). 

Para garantir a segregação fiscal e permitir a correta aplicação das regras tributárias, a DeRE exige o desdobramento fiscal dessas contas no PGCC, da seguinte forma: 

- **a) Mecanismo de Desdobramento:** o desdobramento é realizado através do campo <mark>{cDbrMista}</mark> (desdobramento de conta mista); 

- **b) Codificação:** a conta original (mista) que será desdobrada deve utilizar o código [000] no campo <mark>{cDbrMista}.</mark> Os desdobramentos (as “contas filhas”) devem ser preenchidos sequencialmente, de [001] a [999]; 

- **c) Atribuição de códigos de tributação (** **<mark>{codTrib})</mark> :** Cada conta desdobrada <mark>({cDbrMista}</mark> = [001] em diante) atua como uma rubrica analítica independente. O declarante pode atribuir códigos de tributação distintos para cada desdobramento ou 

**Página 37 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

até mesmo repeti-los, caso o desdobramento obedeça a critérios gerenciais internos que recaiam na mesma regra tributária; e 

- **d) Chave da Conta:** o código final da conta que será utilizada no PGCC é a concatenação do código da conta interna <mark>({cCtaInterna})</mark> e do desdobramento da conta mista <mark>({cDbrMista}</mark> ), formando o campo <mark>{cCta},</mark> sendo única para a vigência do PGCC. 

## **_1.2.4.3 O Plano de Contas Referencial_** 

O campo <mark>{planoCtaRef}</mark> , localizado neste evento, define qual padrão de plano de contas regulatório o contribuinte adota. Este campo é de ocorrência única e determina a regra de validação para todas as contas informadas. 

**Critério de Escolha:** O contribuinte deve informar o plano vinculado ao seu respectivo órgão regulador. Na ausência de regulação específica, deve ser utilizado o plano referencial do SPED (RFB). 

|**Código**|**Plano de Contas Referencial**|**Setor Aplicável**|
|---|---|---|
|**1**|COSIF (Banco Central)|Instituições Financeiras|
|**2**|ANS (Agência Nacional de Saúde Suplementar)|Planos de Assistência à Saúde|
|**3**|SUSEP (Superintendência de Seguros Privados)|Seguradoras|
|**4**|SPED (Receita Federal do Brasil)|Outros|



**Regra de Validação:** O sistema da DeRE utilizará o valor informado em <mark>{planoCtaRef}</mark> para validar o campo <mark>{cCtaRef}</mark> (código da conta referencial) de cada conta analítica declarada. O código da conta referencial deve ser válido e vigente no plano escolhido. 

**Exemplo:** 

Se o contribuinte informar <mark>{planoCtaRef}</mark> = [1] (COSIF), todas as contas analíticas devem referenciar um código válido e vigente na tabela do plano de contas COSIF. A referência a um código inexistente no plano escolhido causará a rejeição do evento. 

**Regra de Validação:** Para contas desdobradas ( <mark>{cDbrMista}</mark> > [000]), o campo <mark>{cCtaRef}</mark> deve ser obrigatoriamente igual ao <mark>{cCtaRef}</mark> de sua conta superior imediata <mark>({cCtaSup})</mark> . 

## **_1.2.4.4 Indicador de Frequência de Encerramento Contábil_** **_<mark>{freqEncerr}</mark>_** 

Este campo define a periodicidade com que a entidade realiza o encerramento das contas de resultado (receitas e despesas) para apuração do resultado do exercício. 

**Impacto nas Regras de Validação:** Esta informação determina a regra de validação do saldo inicial <mark>({vSaldoInic}</mark> ) das contas de resultado no evento de Balancete Mensal (D- 

**Página 38 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

1101). O sistema exigirá que o saldo inicial seja [0.00] no primeiro mês de cada novo ciclo, conforme declarado no campo <mark>{freqEncerr}.</mark> 

## **_1.2.4.5 Indicação de Tributação do ISS_** **_<mark>{indTribISS}</mark>_** 

O campo deverá ser preenchido para as operações dos Regimes Específicos que sofram incidência do ISSQN (Imposto Sobre Serviços de Qualquer Natureza). Este indicador tem por finalidade segregar contabilmente estas operações, para fins de aplicação das regras de transição e alíquotas do IBS/CBS. 

**Regras de Preenchimento:** O contribuinte deve informar este campo nas contas analíticas, conforme abaixo: 

- **0 – Não sujeita ao ISS:** Para receitas sem incidência de ISS e suas respectivas despesas vinculadas; 

**Consequência:** Para serviços financeiros, o sistema aplicará a alíquota padrão do regime específico de serviços financeiros; 

- **1 – Sujeita ao ISS:** Para receitas com incidência de ISS e suas respectivas despesas vinculadas; 

**Consequência:** Para serviços financeiros, o sistema aplicará a alíquota reduzida do regime específico (mitigação de carga tributária durante a transição). 

## **2 EVENTOS PERIÓDICOS MENSAIS** 

## **2.1 D-1101 – BALANCETE MENSAL** 

## **2.1.1 Conceito e Finalidade** 

O evento Balancete Mensal é o motor central para a determinação da base de cálculo e tem como finalidade capturar a movimentação das contas analíticas patrimoniais e de resultado, servindo como base para a aferição do IBS, da CBS e do IS para os regimes específicos. 

Sua estrutura exige que todas as contas informadas estejam rigorosamente mapeadas no evento D-1011 (Plano Geral de Contas Comentado - PGCC). O sistema utiliza o código de tributação ( <mark>{codTrib})</mark> vinculado a cada conta analítica no PGCC para aplicar as matrizes de cálculo e totalização dos tributos devidos. 

## **2.1.2 Frequência e Obrigatoriedade de Envio** 

A periodicidade deste evento é **mensal** . O balancete consolida, na raiz do CNPJ, as informações de todas as filiais e da matriz em um único evento, correspondente ao mês de competência informado no campo de período de apuração ( <mark>{perApur}</mark> ). A data limite de entrega é o dia 15 do mês subsequente ao período de apuração, ainda que recaia em sábado, domingo, feriado ou outro dia não útil. 

**Página 39 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **2.1.3 Regras de Negócio e Preenchimento** 

## **_2.1.3.1 A Dependência do PGCC_** 

O Balancete Mensal é diretamente subordinado às parametrizações estabelecidas no evento Plano Geral de Contas Comentado. Para a recepção com sucesso do balancete, devem ser observadas as seguintes diretrizes na informação das contas ( <mark>{cCta})</mark> : 

- **a) Vigência Exigida:** A conta informada deve existir e estar vigente no PGCC do contribuinte no último dia do mês de apuração correspondente <mark>({perApur}</mark> ). 

- **b) Apenas Contas Analíticas:** O balancete deve ser composto exclusivamente por contas analíticas. É vedada a informação de contas sintéticas. 

- **c) Integralidade:** Devem ser informadas todas as contas patrimoniais e de resultado que tiveram movimentação ou saldo no período, inclusive aquelas que tenham sido criadas ou encerradas durante o próprio mês de apuração. 

## **_2.1.3.2 Regras de Consistência Contábil_** 

**Continuidade de Saldos:** Para contas patrimoniais (Ativo, Passivo e Patrimônio Líquido), o saldo inicial <mark>({vSaldoInic})</mark> do mês atual deve ser igual ao saldo final <mark>({vSaldoFinal})</mark> do mês anterior. 

**Tratamento de Contas de Resultado:** As contas de receita e despesa devem iniciar com saldo zero no primeiro mês de cada novo ciclo de encerramento (anual, semestral, etc.), conforme definido no campo <mark>{freqEncerr}</mark> do PGCC. No mês de encerramento do exercício contábil, o contribuinte deve enviar o evento de Balancete Mensal antes da apuração do resultado do exercício. 

**Arredondamentos:** os valores devem seguir a regra básica prescrita pela ABNT NBR 5891. Os números deverão ser expressos com duas casas decimais, observando-se o critério de arredondamento. 

**Importante:** As despesas dedutíveis oriundas de aquisições de bens e serviços informadas no balancete devem ter seu lastro em documento fiscal idôneo. 

## **_2.1.3.3 Dinâmica de Lançamentos, Ajustes e Valor Apurado_** 

Para conciliar a contabilidade societária com a exigência fiscal, a DeRE instituiu, no próprio balancete, colunas de ajustes dos movimentos devedores e credores. O contribuinte deve declarar o movimento bruto e utilizar os campos específicos de ajuste para informar estornos ou cancelamentos, formando assim o valor apurado ( <mark>{vApur}</mark> ). 

O preenchimento desses campos deve seguir as seguintes regras: 

- **a) Movimento Bruto do Período (** **<mark>{vMovDebt}</mark> e** **<mark>{vMovCred})</mark> :** Informar o valor total dos lançamentos a débito e a crédito realizados na conta durante o mês de competência; 

**Página 40 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **b) Valor de Ajuste do Movimento a Débito (** **<mark>{vAjusteDebt})</mark> :** Destina-se à exclusão de valores que compõem o movimento bruto a débito (ex: estornos, cancelamentos, etc.) ou à inclusão de valores no movimento a crédito para fins de cálculo do <mark>{vApur}.</mark> O valor do ajuste declarado deve ser, obrigatoriamente, menor ou igual ao respectivo movimento bruto a débito do período ( <mark>{vMovDebt})</mark> ; 

- **c) Valor de Ajuste do Movimento a Crédito (** **<mark>{vAjusteCred})</mark> :** Destina-se à exclusão de valores que compõem o movimento bruto a crédito (ex: estornos, devoluções, cancelamentos etc.) ou à inclusão de valores no movimento a débito para fins de cálculo do <mark>{vApur}</mark> . O valor do ajuste declarado deve ser, obrigatoriamente, menor ou igual ao respectivo movimento bruto a crédito do período ( <mark>{vMovCred}</mark> ); 

- **d) Valor Apurado** **<mark>({vApur}</mark> ) e sua Natureza (** **<mark>{natVApur})</mark> :** O campo <mark>{vApur}</mark> é a base líquida final sobre a qual serão aplicadas as regras definidas pelo código de tributação (exemplo: ‘Receitas Tributáveis’, ‘Despesas Dedutíveis’ etc.). Se o <mark>{vApur}</mark> for maior que zero, é obrigatório informar a natureza do valor apurado ( <mark>{natVApur})</mark> , indicando se o resultado é de movimento devedor ([D]) ou movimento credor ([C]); 

- **e) Regra de Consistência Matemática:** O sistema valida o cálculo do <mark>{vApur}</mark> declarado pelo contribuinte. O valor informado no evento deve respeitar a seguinte fórmula (regra ‘CONFERIR_VAPUR’, prevista no Anexo II dos leiautes da DeRE): 

   - Se <mark>{natVApur}</mark> = [D] (Movimento Devedor): <mark>{vApur}</mark> = <mark>({vMovDebt}</mark> - <mark>{vAjusteDebt}</mark> + <mark>{vAjusteCred})</mark> ; 

   - Se <mark>{natVApur}</mark> = [C] (Movimento Credor): <mark>{vApur}</mark> = <mark>({vMovCred}</mark> - <mark>{vAjusteCred}</mark> + <mark>{vAjusteDebt})</mark> . 

### **Exemplo 1:** Obtenção do <mark>{vApur} e</mark> m Contas de Resultado. 

**Cenário de Ajuste a Débito:** 

**1.** Uma conta analítica de despesa, parametrizada no PGCC com o <mark>{codTrib}</mark> de ‘Perdas incorridas nas operações de crédito’, registrou um movimento a débito bruto no mês de R$100.000,00 ( <mark>{vMovDebt})</mark> ; 

**2.** O contribuinte identifica que R$ 15.000,00 referem-se a um estorno de lançamento indevido e não devem compor a dedução fiscal. 

**Procedimento Correto:** 

   **1.** O declarante informará <mark>{vAjusteDebt}</mark> = 15.000,00; 

   **2.** A apuração resultará em um <mark>{vApur}</mark> de 85.000,00 com <mark>{natVApur}</mark> = [D] (Natureza Devedora). 

- **Exemplo 2:** Valor Apurado ( <mark>{vApur})</mark> em Contas Patrimoniais (Regime de Caixa). 

   - Para atividades cuja tributação é exigida pelo Regime de Caixa (ex: seguros, arrendamento mercantil), o fato gerador não é o faturamento, mas a liquidação financeira. Nestes casos, o contribuinte extrairá o <mark>{vApur}</mark> diretamente das contas patrimoniais (Ativo/Passivo), onde a ocorrência de ajustes simultâneos a débito e a crédito é frequente para isolar apenas o recurso que efetivamente será oferecido à tributação. 

**Página 41 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

### **Cenário de Aferição pelo Regime de Caixa:** 

**1.** Considere uma conta do Ativo Circulante (“Prêmios de Seguros a Receber”), classificada com um <mark>{codTrib}</mark> de “Prêmios de seguros, cosseguros, resseguros e retrocessão recebidos”. Como é uma conta de Ativo, os novos faturamentos entram a débito e os recebimentos entram a crédito. Logo, a base tributável será extraída do movimento credor ( <mark>{natVApur}</mark> = [C]); 

**2.** Movimentos brutos no mês: 

   - <mark>{vMovDebt}</mark> = 500.000,00 (Novas apólices emitidas/faturadas); 

   - <mark>{vMovCred}</mark> = 600.000,00 (Total de baixas a crédito). 

**3.** O declarante identifica que R$ 50.000,00 referem-se a baixas por cancelamento de apólices (glosas ou distratos onde não houve entrada real de dinheiro); 

**4.** Ao mesmo tempo, verifica-se o lado do débito e constata-se que R$ 10.000,00 referem-se a um recebimento legítimo de cliente que, por erro de parametrização contábil, foi lançado como um “estorno de débito” em vez de ser baixado a crédito. 

### **Procedimento Correto:** 

**1.** Para não oferecer à tributação um valor que não recebeu (item 3 acima), informa-se <mark>{vAjusteCred}</mark> = 50.000,00; 

**2.** Para garantir que o recebimento do item 4 seja tributado conforme a legislação, o declarante precisa somá-lo à base credora, informando <mark>{vAjusteDebt}</mark> = 10.000,00; 

**3.** Natureza declarada <mark>{natVApur}</mark> = [C] (Movimento Credor); 

**4.** A fórmula exigida pelo sistema será: <mark>{vApur}</mark> = ( <mark>{vMovCred}</mark> - <mark>{vAjusteCred}</mark> + <mark>{vAjusteDebt})</mark> ; 

**5.** O valor a ser informado em <mark>{vApur}</mark> será de: (600.000,00 - 50.000,00 + 10.000,00) = 560.000,00. 

## **2.1.4 Vínculo do Balancete com Outros Eventos** 

**Retorno Totalizador (D-9101):** Após o processamento bem-sucedido do balancete, o sistema devolve ao contribuinte o evento D-9101. Este arquivo consolida as somas de <mark>{vApur}</mark> agrupadas por Código de Tributação ( <mark>{codTrib})</mark> e indicador de ISS. 

**Fechamento Mensal (D-1199):** O balancete, de forma isolada, não constitui o crédito tributário. Para a efetivação da aferição, o contribuinte deve obrigatoriamente transmitir o Evento de Fechamento (D-1199), que irá consolidar as informações do balancete com os eventos auxiliares para a aferição e totalização dos tributos. 

**Eventos Auxiliares:** Os saldos das contas informadas no balancete devem ser consistentes com as informações prestadas nos eventos D-1106 (Identificação de Aplicações Financeiras) e D-2101 (Débito em Operações com Títulos de Dívida com Oferta Pública), quando estes forem obrigatórios para o declarante. 

## **2.2 D-1106 – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS** 

## **2.2.1 Conceito e Finalidade** 

O evento Identificação de Aplicações Financeiras tem como finalidade fornecer o lastro analítico individualizado por ativo garantidor mantido pelo contribuinte para a aferição 

**Página 42 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

da base de cálculo tributável decorrente dos rendimentos financeiros dessas aplicações no período de apuração. 

A entrega deste evento é obrigatória para contribuintes de setores que exigem a constituição de ativos garantidores, provisões ou reservas técnicas (como entidades de planos de assistência à saúde e seguradoras). As informações, apresentadas de forma individualizada, permitem a aferição de cada ativo garantidor e os rendimentos efetivamente auferidos, os quais comporão a base de cálculo tributável de acordo com as regras de incidência. 

O sistema utiliza esses dados para validar a tributação das receitas financeiras. Especificamente para os Planos de Assistência à Saúde, as receitas financeiras serão consideradas efetivamente liquidadas quando houver, cumulativamente, a liquidação ou o resgate do respectivo ativo garantidor e a redução das provisões técnicas lastreadas por ativo garantidor, considerando a diferença entre o valor total de provisões técnicas no período de apuração e no período imediatamente anterior (redução dos saldos das reservas técnicas no final do período de apuração, em comparação com os saldos iniciais do mesmo período). 

## **2.2.2 Frequência e Obrigatoriedade de Envio** 

A periodicidade de envio deste evento é **mensal** , correspondente ao mês de competência informado no campo de período de apuração ( <mark>{perApur}</mark> ). Ele pode ser transmitido antes ou depois do envio do evento Balancete Mensal (D-1101), não havendo regra de precedência obrigatória entre eles para a recepção do arquivo. No entanto, é obrigatório o seu processamento com sucesso antes do envio do Fechamento Mensal (D1199). 

O evento D-1106 não se aplica a todos os contribuintes, sendo de preenchimento obrigatório e exclusivo para contribuintes com exigibilidade de ativos financeiros garantidores de provisão técnica, identificados pela existência de códigos de tributação <mark>({codTrib})</mark> específicos em seu PGCC no respectivo período de apuração. 

**Nota:** A obrigatoriedade de envio é vinculada aos códigos de tributação especificados na regra de validação ‘RN - Tabela de codtribs obrigatórios para eventos auxiliares’ (prevista no Anexo II dos leiautes da DeRE), sendo vedada a entrega deste evento nos demais casos. 

## **2.2.3 Regras de Negócio e Preenchimento** 

## **_2.2.3.1 A Dependência do PGCC_** 

A prestação de informações dos ativos exige a vinculação obrigatória a uma conta contábil analítica ( <mark>{cCta}</mark> ). Para que a escrituração e o detalhamento dos ativos sejam aceitos pelo sistema, o contribuinte deve observar as seguintes regras: 

- **Vedação a Contas Sintéticas:** É vedado informar contas sintéticas neste evento; 

**Página 43 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **Vigência no PGCC:** O código da conta ( <mark>{cCta})</mark> deve existir e estar vigente no Plano Geral de Contas Comentado do contribuinte no último dia do período de apuração <mark>({perApur})</mark> correspondente; 

- **Compatibilidade do** **<mark>{codTrib}:</mark>** A conta contábil analítica deve, obrigatoriamente, estar vinculada a um código de tributação compatível com aplicações de reservas técnicas, conforme a regra ‘RN - Tabela de codtribs obrigatórios para eventos auxiliares’ (prevista no Anexo II dos leiautes da DeRE). 

## **_2.2.3.2 Individualização de Ativos por Conta Contábil_** 

O evento D-1106 foi desenhado para otimizar a escrituração, permitindo que uma mesma conta contábil ( <mark>{cCta}</mark> ) recepcione o detalhamento de múltiplos títulos ou ativos financeiros. O sistema permite a informação de até 500 ativos individualizados ( <mark>{idAtivo})</mark> vinculados a uma única conta contábil analítica. 

**Dispensa de Desdobramento no PGCC:** O contribuinte não é obrigado a realizar o desdobramento de contas ( <mark>{cDbrMista})</mark> no Plano Geral de Contas Comentado para segregar cada ativo de forma isolada na estrutura contábil principal, a menos que tal detalhamento ou desdobramento seja exigido por outro evento ou regra de tributação da DeRE. 

**Atenção:** O uso de desdobramentos pode continuar a ser exigido caso: 

**1.** Os ativos agrupados na mesma conta analítica interna exigirem a aplicação de códigos de tributação distintos; ou 

**2.** Alguma regra concorrente de validação em outro evento demandar a segregação prévia de saldos para a correta apuração. 

## **_2.2.3.3 Declaração de Inexistência de Aplicações Vinculadas à Reserva Técnica_** 

Caso o contribuinte obrigado à entrega do evento D-1106 não possua ativos financeiros vinculados à reserva técnica a serem informados no período de apuração, ele não está dispensado do envio do evento. Neste cenário, o contribuinte deve transmitir este evento preenchendo o indicador de inexistência de aplicações ( <mark>{semAplic}</mark> = [1]). O preenchimento deste indicador dispensa a informação dos demais grupos de detalhamento de ativos. 

## **_2.2.3.4 Regras de Validação_** 

O contribuinte deve informar a movimentação no mês para cada ativo financeiro pertencente à reserva técnica, sendo aplicáveis as seguintes regras de validação: 

- **a) Validação do Saldo Inicial:** O valor inicial do título ( <mark>{vSaldoInic}</mark> ) no mês corrente deve ser igual ao saldo final ( <mark>{vSaldoFinal})</mark> declarado para o mesmo <mark>{idAtivo}</mark> no período de apuração imediatamente anterior (exceto na competência de aquisição do ativo); 

**Página 44 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **b) Formação do Valor de Apuração:** O valor base de apuração corresponde à soma dos rendimentos periódicos recebidos no mês ( <mark>{vRendPerReceb}</mark> ) com os rendimentos recebidos no momento da liquidação ou resgate do ativo <mark>({vRendLiqResg})</mark> ; 

- **c) Consistência do Saldo Final:** O sistema valida o cálculo aritmético do saldo de encerramento do título por meio da fórmula: <mark>{vSaldoInic}</mark> = <mark>{vSaldoInic}</mark> + <mark>{vVarMensal}</mark> (Variação Positiva ou Negativa) - <mark>{vPrincLiqResg}</mark> (Resgate do Principal). 

**Exemplo Prático:** Rendimento mensal de ativo garantidor de reserva técnica. 

|**Descrição (Campo)**<br>**Valor**|
|---|
|Período de Apuração ({perApur})<br>2027-01|
|Identificação do Ativo ({idAtivo})<br>CDB-PROV-9876|
|Valor Inicial do Ativo ({vSaldoInic})<br>R$ 5.000.000,00|
|Variação Mensal ({vVarMensal})<br>R$ 30.000,00|
|Rendimentos Periódicos Recebidos ({vRendPerReceb})<br>R$ 50.000,00|
|Resgate do Principal no Mês ({vPrincLiqResg})<br>R$ 0,00|
|Rendimentos no Resgate ({vRendLiqResg})<br>R$ 0,00|
|Base de Cálculo do Período ({vApur})<br>R$ 50.000,00|
|Valor do Título em 31/01/2027 ({vSaldoFinal})<br>R$ 5.030.000,00|
|**Nota:**O valor de R$ 50.000,00 informado em{vApur}comporá o totalizador do evento<br>de retorno (D-9106) e será utilizado pelo sistema para calcular a base de cálculo<br>líquida e o respectivo débito de IBS e CBS do regime específico.|



## **2.2.4 Vínculo do Evento D-1106 com Outros Eventos** 

**Retorno Totalizador (D-9106):** Após o processamento bem-sucedido do evento D- 1106, o sistema devolve ao contribuinte o evento D-9106. Este arquivo consolida as somas de todas as ocorrências do campo <mark>{vApur}</mark> do evento. 

**Fechamento Mensal (D-1199):** O evento D-1106, de forma isolada, não constitui o crédito tributário. Para a efetivação da aferição, o contribuinte deve obrigatoriamente transmitir o Evento de Fechamento (D-1199), que irá consolidar as informações do balancete com os eventos auxiliares para a aferição e totalização dos tributos. 

## **2.3 D-1199 – FECHAMENTO MENSAL** 

## **2.3.1 Conceito e Finalidade** 

O evento de Fechamento Mensal é o instrumento pelo qual o contribuinte declara à administração tributária o encerramento da transmissão de todos os eventos periódicos 

**Página 45 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

(como o Balancete Mensal e os eventos auxiliares) exigidos referentes a um determinado período de apuração ( <mark>{perApur}</mark> ). 

Sua finalidade primária é indicar ao sistema da DeRE que a escrituração está completa e apta para cálculo. O processamento com sucesso deste evento atua como o gatilho para o envio das informações do ambiente da DeRE para o ambiente da Apuração Assistida. 

Adicionalmente, é através do evento D-1199 que o contribuinte deve parametrizar a forma de aproveitamento de bases de cálculo negativas (BCN) acumuladas em períodos anteriores, conforme tópico 5.3.3 a seguir. 

**Importante:** O fechamento é uma confissão dos valores dos tributos apurados, inclusive para períodos sem movimento, onde o contribuinte declara a inexistência de fatos geradores. 

## **2.3.2 Frequência e Obrigatoriedade de Envio** 

A periodicidade deste evento é **mensal** . A sua transmissão deve ocorrer após o envio e o processamento com sucesso (com a geração de recibo) de todos os eventos exigidos para o contribuinte no respectivo período de apuração ( <mark>{perApur})</mark> , como o Balancete Mensal (D-1101) e os eventos auxiliares (D-1106 e D-2101), quando aplicáveis. 

## **2.3.3 Regras de Negócio e Preenchimento** 

## **_2.3.3.1 Regras de Integridade e Dependência_** 

O ambiente da DeRE submeterá o fechamento a uma validação de completude e este será rejeitado se o sistema identificar a ausência de eventos mensais obrigatórios para o declarante. 

**Exemplo:** 

Se o contribuinte atua com Seguros ou Planos de Saúde, setores onde há incidência do IBS e da CBS sobre os rendimentos financeiros vinculados às reservas técnicas (obrigatoriedade validada pela existência de códigos de tributação específicos no PGCC), o evento D-1199 será rejeitado caso o evento D-1106 não tenha sido entregue para o mesmo período de apuração. 

## **_2.3.3.2 Consistência e Validação de Saldos Contábeis_** 

O ambiente da DeRE executa rotinas de consistência para assegurar a integridade de saldos entre os eventos auxiliares e o Balancete Mensal. 

Ao identificar no PGCC contas cujo <mark>{codTrib}</mark> exija o envio de eventos auxiliares, o processamento disparará a regra de negócio ‘RN - Validar Saldos Finais Eventos Auxiliares e Balancete’ (prevista no Anexo II dos leiautes da DeRE). O sistema executa as seguintes validações de consistência: 

**Página 46 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **a) Evento D-1106 (Identificação de Aplicações Financeiras):** Compara a soma dos saldos finais ( <mark>{vSaldoFinal}</mark> ) das aplicações que compõem cada conta analítica com o registro do saldo final dessa mesma conta ( <mark>{cCta})</mark> declarado no D-1101 (Balancete Mensal); 

- **b) Evento D-2101 (Débito em Operações com Títulos de Dívida com Oferta Pública):** Compara a soma dos valores finais ( <mark>{vFinalTitulo}</mark> ) dos títulos que compõem cada conta analítica com o registro do saldo final dessa mesma conta <mark>({cCta})</mark> declarado no D-1101 (Balancete Mensal). 

A constatação de qualquer omissão ou falha de equivalência entre os resultados dos eventos de detalhamento auxiliar e os saldos reportados no balancete conduzirá à rejeição do evento de fechamento (D-1199). Em caso de rejeição, o procedimento impõe que o contribuinte promova a revisão e a respectiva retificação dos eventos originários faltantes ou incorretos, antes de solicitar novamente o processamento por meio de um novo evento D-1199. 

## **_2.3.3.3 Aproveitamento de Base de Cálculo Negativa (BCN)_** 

Caso o contribuinte possua saldo de bases de cálculo negativas acumulado em meses anteriores, o evento D-1199 é o instrumento hábil para parametrizar a utilização desse estoque para o abatimento na base de cálculo do período corrente vinculado a mesma receita tributável que lhe deu origem. 

A parametrização no grupo de aproveitamento ( <mark>{gUtilizBCN})</mark> deve obedecer às seguintes diretrizes: 

- **a) Identificação da Origem (** **<mark>{codBCNRaiz})</mark> :** A utilização não é genérica. O declarante deve, obrigatoriamente, informar o código identificador raiz que gerou a base negativa, conforme os domínios estabelecidos na <<Tabela 12 – Códigos de Bases de Cálculo (codBC)>>, prevista no Anexo I dos leiautes da DeRE; 

- **b) Declaração de Uso (** **<mark>{usarBCNAcum})</mark> :** O contribuinte deve assinalar se opta ([1]) ou não opta ([0]) por utilizar os saldos disponíveis de bases negativas para aquele período e base de cálculo; 

- **c) Método de Aproveitamento (** **<mark>{metodoAproveit}</mark> ):** Havendo a opção pelo uso, o contribuinte deve definir o método de aproveitamento que o sistema deverá adotar: 

   - **PEPS Automático (** **<mark>{metodoAproveit}</mark> = [0]):** O sistema realizará o abatimento do saldo, priorizando rigorosamente as bases mais antigas (Primeiro que Entra, Primeiro que Sai). Neste método, o declarante informa, se desejar, apenas o limite máximo global que deseja consumir no campo valor a utilizar ( <mark>{vUtilBCN})</mark> ; 

   - **Aproveitamento Manual (** **<mark>{metodoAproveit}</mark> = [1]):** O contribuinte assume o controle estrito das deduções. O sistema exige o preenchimento do grupo de detalhamento ( <mark>{detBCNeg})</mark> , no qual o declarante deve indicar a chave exata de cada base negativa de origem ( <mark>{codBCN})</mark> , devolvida previamente no evento de Retorno D-9199 de períodos de apuração anteriores, e o respectivo valor que 

**Página 47 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

deseja utilizar como dedução na respectiva base de cálculo do período de apuração corrente ( <mark>{vUsarBCN})</mark> . 

**Observação:** Independentemente do método escolhido, o sistema limitará matematicamente a dedução ao valor do débito apurado no mês, não permitindo compensações a maior. O saldo remanescente será atualizado e devolvido no evento D-9199. 

## **2.3.4 Eventos Mensais Sujeitos ao Evento de Fechamento (D-1199)** 

O fechamento mensal é obrigatório para os seguintes eventos: 

|**Evento**|**Descrição**|
|---|---|
|D-1101|Balancete Mensal|
|D-1106|Identificação de Aplicações Financeiras|
|D-2101|Débito em Operações com Títulos de<br>Dívida com Oferta Pública|



## **2.3.5 Efeitos do Evento de Fechamento (D-1199)** 

**Evento de Retorno Totalizador:** Uma vez recepcionado e processado com sucesso o evento D-1199, será gerado o Retorno Totalizador – Fechamento Mensal (D-9199), calculando o valor do débito dos tributos IBS, CBS e IS, quando aplicável. 

## **_2.3.5.1 Bloqueio de Operações após o Fechamento_** 

A arquitetura do evento de fechamento difere dos demais eventos da declaração. O evento D-1199 aceita unicamente operações de inclusão ( <mark>{tpOper}</mark> = [1]). Não existe alteração ou exclusão direta de um fechamento mensal. 

Ao ser recepcionado com sucesso, a competência correspondente é encerrada e bloqueada no sistema. O contribuinte fica, tecnicamente, impedido de enviar, alterar ou excluir qualquer evento periódico referente àquele período de apuração. 

**Ação Necessária para Correções:** Caso seja necessário retificar dados de um período já encerrado, o contribuinte fica obrigado a transmitir previamente o evento de Reabertura, que permitirá novas transmissões, exigindo um novo evento de fechamento para o período. 

**Observação:** O evento de reabertura será publicado na próxima atualização da documentação técnica. 

**Página 48 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **<mark>CAPÍTULO IV – EVENTOS DO REGIME ESPECÍFICO DE SERVIÇOS FINANCEIROS (SÉRIE D-2000)</mark>** 

## **1 EVENTOS PERIÓDICOS MENSAIS** 

## **1.1 D-2101 – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA** 

## **1.1.1 Conceito e Finalidade** 

O evento D-2101, exclusivo para contribuintes do Regime Específico de Serviços Financeiros, destina-se à escrituração e ao detalhamento contábil-fiscal das operações com títulos de dívida emitidos exclusivamente por empresas domiciliadas no país que sejam objeto de oferta pública, na forma regulamentada pela Comissão de Valores Mobiliários (CVM), abrangendo tanto as aquisições diretas quanto aquelas realizadas por meio de fundos de investimento exclusivos cuja carteira seja composta por, no mínimo, 95% desses títulos. 

Sua finalidade principal é permitir o controle individualizado dos títulos de dívida, como debêntures e notas comerciais. Ele destina-se ao detalhamento mensal das receitas financeiras auferidas por instituições financeiras credoras nas operações com estes títulos, identificando a parcela dos juros que compõe a base de cálculo, garantindo a exclusão da parcela que exceder o “custo de oportunidade” representado pela variação da Taxa Selic. Como contrapartida à desoneração parcial da margem, o devedor (emissor do título) não apropriará créditos sobre essa operação. 

## **1.1.2 Frequência e Obrigatoriedade de Envio** 

A periodicidade de envio deste evento é **mensal** , correspondente ao mês de competência informado no campo de período de apuração ( <mark>{perApur}</mark> ). Ele pode ser transmitido antes ou depois do envio do evento Balancete Mensal (D-1101), não havendo regra de precedência obrigatória entre eles para a recepção do arquivo. No entanto, é obrigatório o seu processamento com sucesso antes do envio do Fechamento Mensal (D1199). 

Este evento não se aplica a todos os contribuintes, sendo de preenchimento obrigatório e exclusivo para contribuintes do Regime Específico de Serviços Financeiros que possuam, no seu Plano Geral de Contas Comentado vigente no último dia do <mark>{perApur},</mark> contas analíticas mapeadas com códigos de tributação relativos a essas operações. 

**Nota:** A obrigatoriedade de envio é vinculada aos códigos de tributação especificados na regra de validação ‘RN - Tabela de codtribs obrigatórios para eventos auxiliares’ (prevista no Anexo II dos leiautes da DeRE), sendo vedada a entrega deste evento nos demais casos. 

**Página 49 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **1.1.3 Regras de Negócio e Preenchimento** 

## **_1.1.3.1 A Dependência do PGCC_** 

A prestação de informações dos títulos exige a vinculação obrigatória a uma conta contábil analítica ( <mark>{cCta}</mark> ). Para que a escrituração e o detalhamento dos títulos sejam aceitos pelo sistema, o contribuinte deve observar as seguintes regras: 

- **Vedação a Contas Sintéticas:** É vedado informar contas sintéticas neste evento; 

- **Vigência no PGCC:** O código da conta ( <mark>{cCta})</mark> deve existir e estar vigente no Plano Geral de Contas Comentado do contribuinte no último dia do período de apuração <mark>({perApur})</mark> correspondente; 

- **Compatibilidade do** **<mark>{codTrib}:</mark>** A conta contábil analítica deve, obrigatoriamente, estar vinculada a um código de tributação compatível com títulos de dívida, conforme a regra de negócio ‘RN - Tabela de codtribs obrigatórios para eventos auxiliares’ (prevista no Anexo II dos leiautes da DeRE). 

## **_1.1.3.2 Individualização de Títulos por Conta Contábil_** 

O evento D-2101 foi desenhado para otimizar a escrituração, permitindo que uma mesma conta contábil ( <mark>{cCta}</mark> ) recepcione o detalhamento de múltiplos títulos. O sistema permite a informação de até 500 títulos individualizados ( <mark>{idTitulo})</mark> vinculados a uma única conta contábil analítica. 

**Dispensa de Desdobramento no PGCC:** O contribuinte não é obrigado a realizar o desdobramento de contas ( <mark>{cDbrMista})</mark> no Plano Geral de Contas Comentado para segregar cada título de forma isolada na estrutura contábil principal, a menos que tal detalhamento ou desdobramento seja exigido por outro evento ou regra de tributação da DeRE. 

**Atenção:** O uso de desdobramentos pode continuar a ser exigido caso: 

**1.** Os títulos agrupados na mesma conta analítica interna exigirem a aplicação de códigos de tributação distintos; ou 

**2.** Alguma regra concorrente de validação em outro evento demandar a segregação prévia de saldos para a correta apuração. 

## **_1.1.3.3 Formas de Aquisição e Identificação do Título_** 

O contribuinte deve segregar os títulos com base na forma como foram adquiridos, o que afeta diretamente as regras de validação do sistema: 

**1. Compra Direta** **<mark>({tpAquis}</mark> = [1]):** O campo de identificação do título ( <mark>{idTitulo})</mark> deve ser preenchido com o código padrão de mercado adotado (ex: referência ISO 6166 ou ISIN); 

**2. Compra por meio de Fundo Exclusivo** **<mark>({tpAquis}</mark> = [2]):** Aplicável quando a aquisição ocorre via fundos de investimento exclusivos com composição mínima de 

**Página 50 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

95% em títulos de dívida. Neste caso, o <mark>{idTitulo}</mark> passa a ser um número sequencial único criado pelo declarante para aquele título dentro da conta, e o preenchimento da descrição do fundo ( <mark>{descFundo})</mark> torna-se obrigatório. 

## **_1.1.3.4 Declaração de Inexistência de Títulos a Informar_** 

Caso o contribuinte obrigado à entrega do evento D-2101 não possua títulos a serem informados no período de apuração, ele não está dispensado do envio do evento. Neste cenário, o contribuinte deve transmiti-lo preenchendo o indicador de inexistência de aplicações ( <mark>{semTitulos}</mark> = [1]). O preenchimento deste indicador dispensa a informação dos demais grupos de detalhamento de títulos. 

## **_1.1.3.5 Regras de Validação_** 

O contribuinte deve informar a movimentação no mês para cada ativo financeiro pertencente à reserva técnica, sendo aplicáveis as seguintes regras de validação: 

- **a) Validação do Saldo Inicial:** O valor inicial do título ( <mark>{vInicTitulo}</mark> ) no mês corrente deve ser igual ao valor final ( <mark>{vFinalTitulo})</mark> declarado para o mesmo <mark>{idTitulo}</mark> no período de apuração imediatamente anterior (exceto na competência de aquisição do título); 

- **b) Formação do Valor de Apuração** **<mark>({vApur})</mark> :** O valor base de apuração corresponde ao menor valor apurado entre o rendimento efetivo ( <mark>{vJuros})</mark> e o valor teto da Selic <mark>({vSelic})</mark> , de modo a excluir da base de cálculo do IBS e da CBS os rendimentos que excederem este limite, aplicando-se sobre o resultado a dedução da parcela relativa ao PIS/Cofins incidente sobre a operação <mark>({vPisCofins})</mark> , quando houver. 

**Exemplo Prático:** Rendimento mensal de debênture adquirida em oferta pública. 

|**Descrição (Campo)**|**Valor**|
|---|---|
|Valor do título sobre o qual incidem juros ({vInicTitulo})|R$ 10.000.000,0000|
|Taxa de juros contratual do período ({pJuros})|1,200000%|
|Receita total de juros auferida ({vJuros})<br>({vInicTitulo}*{pJuros})|R$ 120.000,0000|
|Taxa Selic diária acumulada no período ({pSelic})|0,850000%|
|Teto tributável Selic ({vSelic})<br>({vInicTitulo}*{pSelic})|R$ 85.000,0000|
|Parcela passiva de dedução (transição) ({vPisCofins})|R$ 0,0000|
|Base de Cálculo ({vApur})<br>(corresponde ao menor entre{vSelic}e{vJuros})|R$ 85.000,0000|



**Nota:** O excedente de R$ 35.000,00 (R$ 120.000,00 - R$ 85.000,00) é automaticamente excluído da apuração de débito pelo sistema conforme art. 195, §1º, da LC 214/25. O montante de R$ 85.000,00 comporá o totalizador do evento de retorno (D-9121) e será utilizado para composição da base de cálculo. 

**Página 51 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **1.1.4 Vínculo do Evento D-2101 com Outros Eventos** 

**Retorno Totalizador (D-9121):** Após o processamento bem-sucedido do evento D- 2101, o sistema devolve ao contribuinte o evento D-9121. Este arquivo consolida as somas de todas as ocorrências do campo <mark>{vApur}</mark> do evento. 

**Fechamento Mensal (D-1199):** O evento D-2101, de forma isolada, não constitui o crédito tributário. Para a efetivação da aferição, o contribuinte deve obrigatoriamente transmitir o Evento de Fechamento (D-1199), que irá consolidar as informações do balancete com os eventos auxiliares para a aferição e totalização dos tributos. 

**Página 52 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **<mark>CAPÍTULO V – EVENTOS DE RETORNO E TOTALIZAÇÃO (SÉRIE D-9000)</mark>** 

## **1 DISPOSIÇÕES GERAIS** 

Os eventos da Série D-9000 são os arquivos eletrônicos gerados e retornados pelo ambiente nacional da DeRE ao contribuinte, após o processamento assíncrono dos lotes submetidos. 

## **1.1 DISPOSIÇÕES COMUNS A TODOS OS EVENTOS DE RETORNO** 

A estrutura dos eventos de retorno é padronizada e deve fornecer ao contribuinte todas as informações necessárias para confirmar a recepção e rastrear o processamento. 

## **1.1.1 Identificação do Status (Sucesso, Erro)** 

O _status_ do processamento do evento é determinado pelo campo <mark>{cdRetorno}</mark> (código de retorno), localizado no grupo <mark>{ideStatus}.</mark> 

|**cdRetorno**|**Descrição**|**Implicação**|
|---|---|---|
|**1**|**Sucesso**|<sup>O evento foi validado, registrado com sucesso na base de dados, e o número do</sup><br>recibo{nrRecibo}foi gerado.|
|**0**|**Erro**|Ocorreu um erro impeditivo de processamento e o evento foi rejeitado e**não**<br>**gravado**na base de dados. O contribuinte deve corrigir o arquivo e retransmiti-lo.|



**Nota:** O sistema pode retornar “Sucesso” ( <mark>{cdRetorno}</mark> = [1]) mesmo que existam ocorrências, desde que estas sejam classificadas apenas como “ **Aviso** ”, que são inconsistências não impeditivas à recepção e à gravação do evento. 

Ao recepcionar os eventos de retorno, o contribuinte deve analisar o grupo de _status_ da declaração ( <mark>{ideStatus}</mark> ), que indicará a situação do evento enviado: 

- **Sucesso (** **<mark>{cdRetorno}</mark> = [1]):** O evento foi processado e validado. O sistema gerará um número recibo ( <mark>{nrRecibo})</mark> e fará o preenchimento do grupo de informações totalizadoras; 

- **Erro** **<mark>({cdRetorno}</mark> = [0]):** O evento foi rejeitado devido a algum erro de validação (ex: conta não cadastrada no PGCC vigente). Neste caso, o grupo de totalização não será preenchido e o contribuinte deverá verificar o grupo <mark>{ocorrencias}</mark> para identificar o código do erro, sua descrição, obtendo a localização exata da falha, para promover a correção e o reenvio do respectivo evento. 

**Atenção:** O Protocolo de Recebimento emitido no momento da transmissão via API não atesta o cumprimento da obrigação acessória, consistindo apenas em um comprovante provisório de entrega do pacote de arquivos. O cumprimento legal dá-se exclusivamente pela obtenção do <mark>{nrRecibo}</mark> após o processamento com sucesso pelo sistema da DeRE. 

**Página 53 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **1.1.2 Grupo de Ocorrências (Interpretação de Erros)** 

Quando o processamento não for bem-sucedido ou contiver advertências, o grupo <mark>{ocorrencias}</mark> detalha as inconsistências encontradas. 

O contribuinte deve analisar os seguintes campos dentro do grupo <mark>{ocorrencias}:</mark> 

|**Campo**|**Finalidade**|**Ação Obrigatória**|
|---|---|---|
|**codigo**|Código do erro ou aviso.|Identificar o código da falha.|
|**descricao**|Descrição literal do erro ou<br>aviso.|Compreender a natureza do problema.|
|**tipo**|Tipo da ocorrência.|**1**- Erro (recusa o evento);<br>**2**- Aviso (permite a recepção, mas sinaliza inconsistência).|
|**localizacao**|Campo que originou o erro.|Informa o caminho do campo ou grupo que causou a falha,<br>facilitando a correção.|



O evento será recusado se for identificado o tipo “Erro” <mark>({tipo}</mark> = [1]) e o contribuinte deve corrigir a falha no campo indicado na <mark>{localizacao}.</mark> 

- **1.2 DISPOSIÇÕES COMUNS AOS RETORNOS DE EVENTOS DE TABELA** 

- **1.2.1 Extrato de Eventos Vigentes** 

Ao processar com sucesso um evento de tabela, o retorno correspondente (D-9001) não apenas confirma a recepção, mas devolve o grupo <mark>{extratoEventos}</mark> . Este grupo atua como uma “foto” atualizada da linha do tempo daquela tabela na base de dados após o processamento. 

O extrato permite que o contribuinte visualize o comportamento do sistema diante de eventos de validade aberta ou sobreposta, detalhando: 

- **a) Eventos Ativos** **<mark>({detEvento})</mark> :** Lista todos os recibos ( <mark>{nrRecibo})</mark> que fundamentam os períodos vigentes, demonstrando a data de início ( <mark>{iniValid}</mark> ) e, quando aplicável, a data limite de validade efetiva imposta pelo sistema <mark>({fimValidEfetiva})</mark> caso tenha havido corte temporal automático ( <mark>{indAjusteAuto}</mark> = [1]); 

- **b) Períodos Descobertos (** **<mark>{detLacuna})</mark> :** Detalha as lacunas ( <mark>{iniLacuna}</mark> e <mark>{fimLacuna})</mark> onde o contribuinte encontra-se sem cobertura de tabela. A existência de lacunas alertará o contribuinte sobre a impossibilidade de enviar eventos periódicos dependentes naqueles intervalos. 

**Nota:** Para mais informações sobre o funcionamento das vigências de eventos de tabela, ver o <u>item 3.1 do Capítulo II.</u> 

**Página 54 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **1.3 DISPOSIÇÕES COMUNS AOS RETORNOS DE EVENTOS PERIÓDICOS MENSAIS** 

## **1.3.1 Versionamento de Eventos** 

Os eventos de retorno de eventos periódicos mensais possuem uma mecânica unificada de versionamento. O sistema informa ao contribuinte o controle de versão da sua escrituração na base de dados por meio do campo <mark>{seqEvento}</mark> (Número sequencial de identificação). 

A contagem do versionamento é sucessiva, ininterrupta para a mesma chave de identificação ( <mark>{nrInsc}</mark> + <mark>{perApur}</mark> ), e obedece à seguinte lógica: 

- **[00]: Evento Original:** Atribuído exclusivamente no processamento com sucesso da primeira Inclusão ( <mark>{tpOper}</mark> = [1]) para uma chave inédita no banco de dados; 

- **[01 a 99]: Evento Versionado:** Atribuído de forma sucessiva e cronológica ao processar eventos de Alteração ( <mark>{tpOper}</mark> = [2]), Exclusão ( <mark>{tpOper}</mark> = [3]) ou de uma nova Inclusão ( <mark>{tpOper}</mark> = [1]) de uma chave que havia sido previamente excluída. 

Sempre que houver a necessidade de substituição ou retificação de um evento mensal de uma competência já processada, a numeração do retorno refletirá essa esteira de alterações: 

- **a) Nova Emissão:** Para cada envio de alteração ou exclusão que for aceito, o Ambiente da DeRE gerará um novo evento de retorno correspondente (D-91XX); 

- **b) Rastreabilidade:** O controle contínuo pelo <mark>{seqEvento}</mark> garante que o contribuinte possua o espelho fiscal de qual é a última informação válida e vigente considerada pelo Sistema da DeRE para aquele período de apuração. 

## **2 RETORNO DE EVENTOS DE TABELA** 

- **2.1 D-9001 – RETORNO – EVENTOS DE TABELA** 

## **2.1.1 Conceito de Retorno de Tabelas** 

O evento D-9001 é o retorno, em formato XML, gerado pelo sistema para os eventos de tabela. Neste evento serão emitidos o número de protocolo, resultante do envio do arquivo, e o número do recibo, o qual somente é emitido após o processamento “com sucesso” do evento. 

O recibo gerado pelo evento D-9001 resulta do processamento dos seguintes eventos recebidos: 

|**Evento**|**Descrição**|
|---|---|
|D-1001|Informações do contribuinte|
|D-1011|Plano Geral de Contas Comentado|



**Página 55 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **3 RETORNO DE EVENTOS PERIÓDICOS MENSAIS** 

## **3.1 D-9101 – RETORNO TOTALIZADOR – BALANCETE MENSAL** 

## **3.1.1 Conceito e Finalidade** 

O evento D-9101 é o arquivo digital gerado automaticamente pelo Ambiente da DeRE em resposta ao envio do evento D-1101 (Balancete Mensal). 

Além de atestar o _status_ do processamento, este evento retorna ao contribuinte a totalização dos valores apurados ( <mark>{vApurTot})</mark> , consolidado por código de tributação <mark>({codTrib})</mark> e indicador de tributação pelo ISS ( <mark>{indTribISS})</mark> . Sua finalidade é permitir que o declarante faça a conferência da agregação das contas analíticas informadas em seu balancete antes do fechamento mensal. 

## **3.1.2 Lógica de Totalização do Balancete** 

Quando o balancete é processado com sucesso, o sistema da DeRE mapeia cada conta analítica <mark>({cCta})</mark> informada no D-1101 com o seu respectivo <mark>{codTrib}</mark> cadastrado no evento PGCC (D-1011). Em seguida, é realizada a sumarização dos valores, retornando o grupo <mark>{gTotalCodTrib}</mark> contendo a consolidação da seguinte forma: 

- **a) Valor Apurado Total** **<mark>({vApurTot})</mark> :** O sistema agrupa e sumariza todos os valores informados no campo <mark>{vApur}</mark> para cada combinação de código de tributação <mark>({codTrib})</mark> e indicador de tributação pelo ISS ( <mark>{indTribISS})</mark> ; 

**Exemplo:** Se o contribuinte possui 50 contas analíticas diferentes informadas no balancete que, no PGCC, estão mapeadas com o <mark>{codTrib}</mark> = [NNNNNNNNN] e <mark>{indTribISS}</mark> = [0], o totalizador retornará apenas uma linha para este código e indicador, contendo a soma de todos os valores de <mark>{vApur}</mark> dessas 50 contas. 

   - **b) Totalização de Saldos de Provisões Técnicas:** Para os contribuintes do regime específico de Planos de Assistência à Saúde que contenham em seu PGCC contas vinculadas a códigos de tributação que exijam a entrega do evento auxiliar Identificação de Aplicações Financeiras (D-1106), conforme regra ‘RN - Tabela de codtribs obrigatórios para eventos auxiliares’ (prevista no Anexo II dos leiautes da DeRE), o D-9101 retorna a totalização dos saldos iniciais e finais das contas relativas a provisões técnicas lastreadas em ativo garantidor. 

- **3.1.3 Ações Subsequentes ao Retorno** 

Ao receber este retorno com _status_ de sucesso, o contribuinte deve validar se os totais agrupados e consolidados em <mark>{vApurTot}</mark> refletem a soma de seu Livro Razão para as respectivas rubricas. Estando os valores corretos, o declarante estará apto a enviar o evento D-1199 (Fechamento Mensal). 

**Página 56 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**Atenção:** O evento D-9101 não calcula o valor do débito de IBS, CBS ou IS, ele apenas realiza a totalização das variáveis do balancete, que ficarão armazenadas no sistema, aguardando o evento de Fechamento (D-1199) para a aferição definitiva dos débitos. 

## **3.2 D-9106 – RETORNO TOTALIZADOR – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS** 

## **3.2.1 Conceito e Finalidade** 

O evento D-9106 é o arquivo digital gerado automaticamente pelo Ambiente da DeRE em resposta ao envio do evento D-1106 (Identificação de Aplicações Financeiras). 

Além de atestar o _status_ do processamento, este evento retorna ao contribuinte a totalização consolidada dos valores apurados ( <mark>{vApurTot})</mark> . Com base neste retorno o contribuinte pode conferir como o sistema totalizou os valores informados no evento D-1106 antes do fechamento mensal. 

## **3.2.2 Lógica de Totalização do Evento** 

Sendo o processamento concluído com sucesso, o sistema retornará o grupo de totalização <mark>({infoTotAplicFin}</mark> ), exceto se o D-1106 tiver sido enviado com a indicação expressa de ausência de aplicações a informar no período ( <mark>{semAplic}</mark> = [1]). 

**Consolidação da Base Financeira:** O evento retornará um único campo totalizador de valores ( <mark>{vApurTot}</mark> ) contendo a soma de todos os valores de <mark>{vApur}</mark> das aplicações financeiras que o declarante reportou no evento D-1106 de origem. 

## **3.2.3 Ações Subsequentes ao Retorno** 

Ao receber este retorno com _status_ de sucesso, o contribuinte deve validar se o total consolidado em <mark>{vApurTot}</mark> reflete o total de rendimentos auferidos na carteira garantidora no período. 

**Atenção:** O evento D-9106 não calcula o valor do débito de IBS, CBS ou IS, ele apenas realiza a totalização das variáveis do evento D-1106, que ficarão armazenadas no sistema, aguardando o evento de Fechamento (D-1199) para a aferição definitiva dos débitos. 

## **3.2.4 O Fluxo da Totalização** 

Diferentemente do balancete mensal (D-1101), que totaliza por código de tributação <mark>({codTrib})</mark> , o D-9106 gera uma totalização global do evento, uma vez que todas as aplicações ali informadas devem, obrigatoriamente, estar vinculadas a códigos de tributação específicos. 

O fluxo de integração com o evento D-9199 ocorre da seguinte forma: 

**Página 57 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

**1.** O sistema armazena o valor do <mark>{vApurTot}</mark> após a validação do D-1106; 

**2.** Ao ser transmitido o evento D-1199 (Fechamento Mensal), o sistema utilizará este montante para compor a respectiva base de cálculo (conforme <<Tabela 12 – Códigos de Base de Cálculo (codBC)>>, prevista no Anexo I dos leiautes da DeRE); 

**3.** No D-9199 (Retorno Totalizador – Fechamento Mensal), este valor é confrontado com o saldo das reservas técnicas. Nas declarações relativas aos Planos de Assistência à Saúde, caso o total dos saldos finais das contas das reservas técnicas seja menor que o total dos saldos iniciais dessas contas constantes no balancete, o rendimento financeiro (retornado no campo <mark>{vApurTot})</mark> é integrado à base de cálculo tributável. 

## **3.3 D-9121 – RETORNO TOTALIZADOR – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA** 

**3.3.1 Conceito e Finalidade** 

O evento D-9121 é o arquivo digital gerado automaticamente pelo Ambiente da DeRE em resposta ao envio do evento D-2101 (Débito em Operações com Títulos de Dívida com Oferta Pública). 

Além de atestar o _status_ do processamento, este evento retorna ao contribuinte a totalização consolidada dos valores apurados ( <mark>{vApurTot})</mark> . Com base neste retorno, o declarante pode conferir como o sistema totalizou as bases de cálculo informadas analiticamente (título a título) no evento D-2101 antes do fechamento mensal. 

## **3.3.2 Lógica de Totalização do Evento** 

Sendo o processamento concluído com sucesso, o sistema retornará o grupo de totalizadores <mark>({infoTotTitOfPub}</mark> ), exceto se o D-2101 tiver sido enviado com a indicação expressa de ausência de títulos a informar no período ( <mark>{semTitulos}</mark> = [1]). 

**Consolidação da Base Financeira:** O evento retornará um único campo totalizador de valores ( <mark>{vApurTot})</mark> contendo a soma de todos os valores de <mark>{vApur}</mark> dos títulos que o declarante reportou no evento D-2101 de origem. 

## **3.3.3 Ações Subsequentes ao Retorno** 

Ao receber este retorno com _status_ de sucesso, o contribuinte deve validar se o total consolidado em <mark>{vApurTot}</mark> reflete a soma de suas margens tributáveis apuradas na carteira de títulos de dívida com oferta pública para o período de apuração. 

**Atenção:** O evento D-9121 não calcula o valor dos débitos de IBS, CBS ou IS, ele apenas realiza a totalização das variáveis do evento D-2101, que ficarão armazenadas no sistema, aguardando o evento de Fechamento (D-1199) para a aferição definitiva dos débitos. 

**Página 58 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

## **3.3.4 Conteúdo da Totalização** 

O elemento central deste retorno é o campo <mark>{vApurTot}.</mark> Diferente da lógica de agrupamento por <mark>{codTrib}</mark> do balancete, o retorno D-9121 totaliza os valores tributáveis informados em cada título existente no D-2101, somando de todas as ocorrências do campo <mark>{vApur}.</mark> 

O <mark>{vApur}</mark> de origem representa o menor valor entre o rendimento real e o teto da SELIC, deduzido do PIS/COFINS. Portanto, o <mark>{vApurTot}</mark> é o montante consolidado que será levado para a respectiva base de cálculo, conforme a <<Tabela 12 – Códigos de Base de Cálculo (codBC)>>, prevista no Anexo I dos leiautes da DeRE. 

## **3.3.5 Fluxo da Totalização** 

- **Alimentação do Fechamento:** O valor totalizado no D-9121 é automaticamente armazenado para ser utilizado pelo sistema para cálculo do débito no evento de Fechamento (D-1199); 

- **Tratamento de Saldos Negativos:** Se a soma dos valores de rentabilidade dos títulos <mark>({vApur})</mark> resultar em um valor negativo no mês, o totalizador retornará zero, pois eventual saldo negativo não pode ser utilizado como dedução. 

## **3.4 D-9199 – RETORNO TOTALIZADOR – FECHAMENTO MENSAL** 

## **3.4.1 Conceito e Finalidade** 

O evento D-9199 (Retorno Totalizador – Fechamento Mensal) é o recibo gerado pelo sistema da DeRE após a recepção e o processamento com sucesso do evento de fechamento D-1199. 

Ele atua como o extrato de consolidação dos débitos na DeRE, atestando a conclusão e o encerramento do ciclo de aferição do mês de referência. Sua finalidade é apresentar ao contribuinte os valores do IBS (Municipal e Estadual), da CBS e do IS, quando aplicável, aferidos no período, além de certificar a geração, a utilização e o controle das Bases de Cálculo Negativas (BCN). 

**Atenção:** As informações contidas neste retorno são consideradas confissão do valor devido, para instruir a cobrança do saldo devedor, embora sua recepção não implique homologação tácita do lançamento, reservando à Administração Tributária o direito de revisão posterior. 

## **3.4.2 Memória de Cálculo e Aferição por Regime Específico** 

O sistema da DeRE segmenta o retorno dos cálculos de acordo com os regimes declarados, acionando os grupos <mark>{infoTotFinanceiro}, {infoTotSaude}</mark> ou <mark>{infoTotProg}.</mark> 

Para cada setor, a base de cálculo é detalhada individualmente pelo grupo <mark>{detBC}</mark> e vinculada ao seu respectivo código ( <mark>{codBC})</mark> . Este grupo detalha a origem e a demonstração matemática da apuração por meio dos seguintes parâmetros: 

**Página 59 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **O Código** **<mark>({codBC})</mark> e a Descrição** **<mark>({xDetBC})</mark> :** A identificação exata do agrupamento de regras que foi aplicado àquelas contas, conforme <<Tabela 12 – Códigos de Base de Cálculo (codBC)>>, prevista no Anexo I dos leiautes da DeRE; 

- **A Memória de Cálculo (** **<mark>{memoriaCalculo}</mark> ):** Uma cadeia de texto semiestruturada contendo a demonstração matemática dos valores intermediários processados pelo sistema (receitas, despesas, reversões e _gross-down_ ). 

A aferição das bases de cálculo é demonstrada de forma segregada para o IBS, a CBS e, quando aplicável, o Imposto Seletivo (IS). A interpretação dos valores do retorno obedece à seguinte estrutura: 

**1. Base de Cálculo** **<mark>({vBCIBS}, {vBCCBS}, {vBCIS}</mark> ):** Representa o montante da base de cálculo líquida (após _Gross-Down_ ), antes de qualquer compensação de bases negativas, quando aplicável. Se o resultado da operação após deduções for negativo, este campo será [0.00]; 

**2. Geração de Nova Base de Cálculo Negativa** **<mark>({vBCNIBS}, {vBCNCBS}</mark> ):** Se o fechamento do período resultar em base de cálculo negativa na aferição do tributo, o valor gerado será demonstrado em módulo (valor absoluto) nestes campos; 

**3. Dedução de Bases Negativas** **<mark>({vDedBCN})</mark>** : Informa o valor da base de cálculo negativa (de períodos anteriores) que o sistema acatou para compensação na competência corrente. O sistema limita o <mark>{vDedBCN}</mark> para que ele jamais ultrapasse o valor da base positiva do tributo; 

**4. Base de Cálculo Efetiva (** **<mark>{vBCApurIBS}, {vBCApurCBS}, {vBCApurIS}</mark> ):** A base tributável definitiva da operação, resultante da dedução de bases de cálculo negativas da base apurada neste período <mark>({vBC}</mark> - <mark>{vDedBCN})</mark> ; 

**5. Cálculo dos Tributos Devidos (** **<mark>{vIBSMun}, {vIBSUF}, {vCBS}</mark> ,** **<mark>{vIS}</mark> ):** A multiplicação da Base de Cálculo Efetiva pelas alíquotas correspondentes (ex: <mark>{pIBSMun}, {pCBS}</mark> ) determinará o montante final devido de cada tributo. 

**3.4.3 O Extrato de Bases de Cálculo Negativas (BCN)** 

O D-9199 possui também a finalidade de fornecer ao contribuinte um extrato de suas bases de cálculo negativas, segregando saldos via grupo <mark>{infoBCN},</mark> dividido entre IBS <mark>({gBCNIBS})</mark> e CBS ( <mark>{gBCNCBS}</mark> ). 

O detalhamento é apresentado em dois níveis de agregação. Em primeiro nível, é apresentado o saldo geral de cada raiz de base de cálculo negativa <mark>({codBCNRaiz})</mark> , contendo os valores agregados de todas as bases de cálculo que incluam aquele mesmo <mark>{codBCNRaiz}.</mark> Para o detalhamento de cada uma dessas bases que compõem o <mark>{codBCNRaiz},</mark> é gerado o grupo de detalhamento ( <mark>{detBCN}</mark> ), que apresenta, para cada base de cálculo negativa <mark>({codBCN}</mark> ), as informações identificadoras e controle de saldos disponíveis e a utilizar. 

**Página 60 de 61** 

Manual de Orientação do Usuário da DeRE (MOD) 

Versão 1.1.0 

- **a) Saldo Anterior** **<mark>({vSaldoAnt}</mark> ):** O montante disponível no início do período, antes das compensações; 

- **b) Valor Utilizado** **<mark>({vUtilPer}</mark> ):** Parcela utilizada para abatimento da base de cálculo aferida no mês atual; 

- **c) Saldo Final** **<mark>({vSaldoFinal}</mark> ):** Valor disponível para compensação nos próximos 5 anos, sem atualização monetária; 

- **d) Período de Origem (** **<mark>{perOrigem})</mark> :** Exclusivo para o grupo de detalhamento, indica o período de origem da base de cálculo negativa; 

- **e) Valor Inicial Original** **<mark>({vOrigemIni})</mark> :** Exclusivo para o grupo de detalhamento, indica o valor inicial da base de cálculo negativa no respectivo período de origem. 

**Atenção:** Cada base de cálculo negativa possui controle individualizado de acordo com a <<Tabela 12 – Códigos de Bases de Cálculo>>, prevista no Anexo I dos leiautes da DeRE. Para mais informações (ver item 4.5 do Capítulo II). 

## **3.4.4 Integração com a Apuração Assistida** 

O evento D-9199 encerra o fluxo informacional da DeRE e envia os dados para a Apuração Assistida, onde o débito aqui totalizado será confrontado com os créditos de aquisições (entradas via Documentos Fiscais) para a geração da guia de recolhimento. 

**Página 61 de 61** 

