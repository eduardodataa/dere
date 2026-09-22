**CGIBS** Comitê Gestor do IBS 

**RECEITA FEDERAL** 

Ministério da Fazenda 

# **DeRE** Declaração de Regimes Específicos Leiautes 





## **Leiautes da DeRE** 

**Versão 1.2.0  |  5 de setembro de 2026** 

Versão 1.2.0 

Leiautes da DeRE 

### **<mark>SUMÁRIO</mark>** 

|**LEIAUTES ........................................................................................................................................................................................................................ 3**|
|---|
|**1 EVENTOS DE TABELA ............................................................................................................................................................................................ 3**|
|1.1 EVENTO D-1001 – INFORMAÇÕES DO CONTRIBUINTE ................................................................................................................................ 3|
|_1.1.1 Estrutura Hierárquica do Evento (Resumo) ................................................................................................................................................. 3_|
|_1.1.2 Especificação Técnica dos Campos (Detalhamento) .................................................................................................................................. 4_|
|1.2 EVENTO D-1011 – PLANO GERAL DE CONTAS COMENTADO ....................................................................................................................... 7|
|_1.2.1 Estrutura Hierárquica do Evento (Resumo) ................................................................................................................................................. 7_|
|_1.2.2 Especificação Técnica dos Campos (Detalhamento) .................................................................................................................................. 8_|
|**2 EVENTOS PERIÓDICOS MENSAIS ....................................................................................................................................................................... 12**|
|2.1 EVENTO D-1101 – BALANCETE MENSAL ...................................................................................................................................................... 12|
|_2.1.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 12_|
|_2.1.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 13_|
|2.2 EVENTO D-1106 – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS ......................................................................................................... 16|
|_2.2.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 16_|
|_2.2.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 17_|
|2.3 EVENTO D-1121 – RELAÇÃO DE DEDUÇÕES UTILIZADAS NA APURAÇÃO ............................................................................................... 20|
|_2.3.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 20_|
|_2.3.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 21_|
|2.4 EVENTO D-1198 – REABERTURA DE PERÍODO DE APURAÇÃO ................................................................................................................. 26|
|_2.4.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 26_|
|_2.4.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 27_|
|2.5 EVENTO D-1199 – FECHAMENTO DE EVENTOS MENSAIS ......................................................................................................................... 28|
|_2.5.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 28_|
|_2.5.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 29_|
|2.6 EVENTO D-2101 – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA ............................................................ 32|
|_2.6.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 32_|
|_2.6.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 33_|
|**3 EVENTOS PERIÓDICOS TRANSACIONAIS (LEIAUTES PRELIMINARES) .......................................................................................................... 36**|
|3.1 EVENTO D-2201 – SERVIÇOS REMUNERADOS POR PREÇO - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR) .............. 36|
|_3.1.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 36_|
|_3.1.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 37_|
|3.2 EVENTO D-2202 – TARIFAS DO REGIME GERAL - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR) ................................... 42|
|_3.2.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 42_|
|_3.2.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 43_|
|3.3 EVENTO D-2211 – OPERAÇÕES DE CRÉDITO E VALORES MOBILIÁRIOS (TVM) - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE|
|PRELIMINAR) ........................................................................................................................................................................................................ 47|
|_3.3.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 47_|
|_3.3.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 48_|
|3.4 EVENTO D-2221 – ANTECIPAÇÃO DE RECEBÍVEIS (SECURITIZAÇÃO, FATURIZAÇÃO E ARRANJOS) - IDENTIFICAÇÃO DE|
|ADQUIRENTES (LEIAUTE PRELIMINAR) ............................................................................................................................................................. 52|
|_3.4.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 52_|
|_3.4.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 53_|
|3.5 EVENTO D-2231 – ARRENDAMENTO MERCANTIL - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR) ................................ 56|
|_3.5.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 56_|
|_3.5.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 57_|
|3.6 EVENTO D-2241 – ARRANJOS DE PAGAMENTO - IDENTIFICAÇÃO DE CREDENCIADOS OU DESTINATÁRIOS DOS SERVIÇOS (LEIAUTE|
|<br>PRELIMINAR) ........................................................................................................................................................................................................ 61|
|_3.6.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 61_|
|<br>_3.6.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 62_|
|3.7 EVENTO D-2242 – ARRANJOS DE PAGAMENTO - OPERAÇÕES ENTRE PARTICIPANTES (LEIAUTE PRELIMINAR) ............................... 66|
|_3.7.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 66_|
|_3.7.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 67_|
|3.8 EVENTO D-2251 – SEGUROS, PREVIDÊNCIA COMPLEMENTAR E CAPITALIZAÇÃO - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE<br>PRELIMINAR) ........................................................................................................................................................................................................ 71|



**Página 1 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|_3.8.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 71_|
|---|
|_3.8.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 72_|
|3.9 EVENTO D-3201 – PLANOS DE ASSISTÊNCIA À SAÚDE - IDENTIFICAÇÃO DE ADQUIRENTES E BENEFICIÁRIOS (LEIAUTE PRELIMINAR)<br>.............................................................................................................................................................................................................................. 77|
|_3.9.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 77_|
|_3.9.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 78_|
|3.10 EVENTO D-4201 – CONCURSOS DE PROGNÓSTICOS - DISCRIMINAÇÃO DE APOSTAS, PRÊMIOS E APOSTADORES (LEIAUTE<br>PRELIMINAR) ........................................................................................................................................................................................................ 82|
|_3.10.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................. 82_|
|_3.10.2 Especificação Técnica dos Campos (Detalhamento) .............................................................................................................................. 83_|
|**4 EVENTOS DE RETORNO E TOTALIZAÇÃO .......................................................................................................................................................... 86**|
|4.1 EVENTO D-9001 – RETORNO – EVENTOS DE TABELA ................................................................................................................................ 86|
|_4.1.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 86_|
|_4.1.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 87_|
|4.2 EVENTO D-9101 – RETORNO TOTALIZADOR – BALANCETE MENSAL ....................................................................................................... 89|
|_4.2.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 89_|
|_4.2.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 90_|
|4.3 EVENTO D-9106 – RETORNO TOTALIZADOR – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS .......................................................... 92|
|_4.3.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 92_|
|_4.3.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 93_|
|4.4 EVENTO D-9112 – RETORNO – RELAÇÃO DE DEDUÇÕES UTILIZADAS NA APURAÇÃO .......................................................................... 95|
|_4.4.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 95_|
|_4.4.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 96_|
|4.5 EVENTO D-9121 – RETORNO TOTALIZADOR – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA .............. 98|
|_4.5.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................... 98_|
|_4.5.2 Especificação Técnica dos Campos (Detalhamento) ................................................................................................................................ 99_|
|4.6 EVENTO D-9198 – RETORNO – REABERTURA DE PERÍODO DE APURAÇÃO ......................................................................................... 101|
|_4.6.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................. 101_|
|_4.6.2 Especificação Técnica dos Campos (Detalhamento) .............................................................................................................................. 102_|
|4.7 EVENTO D-9199 – RETORNO TOTALIZADOR – FECHAMENTO DE EVENTOS MENSAIS ........................................................................ 104|
|_4.7.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................. 104_|
|_4.7.2 Especificação Técnica dos Campos (Detalhamento) .............................................................................................................................. 106_|
|4.8 EVENTO D-9209 – RETORNO – EVENTOS TRANSACIONAIS .................................................................................................................... 121|
|_4.8.1 Estrutura Hierárquica do Evento (Resumo) ............................................................................................................................................. 121_|
|_4.8.2 Especificação Técnica dos Campos (Detalhamento) .............................................................................................................................. 122_|



**Página 2 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **<mark>LEIAUTES</mark>** 

### **1 EVENTOS DE TABELA** 

### **1.1 EVENTO D-1001 – INFORMAÇÕES DO CONTRIBUINTE** 

### **1.1.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-1001 – Informações do Contribuinte – Estrutura Hierárquica do Evento (Resumo)** 

|**Nível**|<br>**Grupo **|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtInfoContrib|DeRE|Evento Informações do Contribuinte.|1-1|id|O|
|3|ideEvento|evtInfoContrib|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtInfoContrib|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtInfoContrib|Período de validade.|1-1|iniValid, fimValid|O|
|4|novaValidade|idePeriodo|Novo período de validade.|0-1|iniValid, fimValid|F (se {tpOper} = [2]);<br>N (nos demais casos).|
|3|infoContrib|evtInfoContrib|Informações do contribuinte.|0-1|-|N (se {tpOper} = [3]);<br>O (nos demais casos).|
|4|servFinanc|infoContrib|Informações de serviços financeiros.|0-1|-|O (se {regTribPrinc} = [1] ou<br>se existir {regTribSecund} =<br>[1]);<br>N (nos demais casos).|
|5|tpAtividades|servFinanc|Lista de atividades realizadas pelo<br>contribuinte.|1-1|-|O|
|4|plAssistSaude|infoContrib|Informações de planos de assistência à<br>saúde.|0-1|-|O (se {regTribPrinc} = [2] ou<br>se existir {regTribSecund} =<br>[2]);<br>N (nos demais casos).|
|5|tpAtividades|plAssistSaude|Lista de atividades realizadas pelo<br>contribuinte.|1-1|-|O|
|4|prognosticos|infoContrib|Informações de concursos de prognósticos.|0-1|-|O (se {regTribPrinc} = [3] ou<br>se existir {regTribSecund} =<br>[3]);<br>N (nos demais casos).|
|5|tpAtividades|prognosticos|Lista de atividades realizadas pelo<br>contribuinte.|1-1|-|O|
|5|UFsCredenc|prognosticos|Listagem das Unidades da Federação (UF)<br>nas quais o contribuinte possui<br>credenciamento para operar.|0-1|-|OC|



**Página 3 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **1.1.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D**<br>**#**<br>**Grupo/Tag**|**-1001 – Informaçõ**<br>**Grupo Pai**|**es do**<br>**Cat**|**Cont**<br>**Tipo**|**ribuinte**<br> <br>**Ocorr**|**– Espe**<br>**Tam**|**cificação Técnica de Campos (Detalhamento)**<br>**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtInfoContrib|DeRE|G|-|1-1|-|-<br>Evento Informações do Contribuinte.|
|3<br>id|evtInfoContrib|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtInfoContrib|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1|-<br>Tipo de operação do evento.<br>**Nota:**A alteração substitui integralmente as informações do evento<br>enviado anteriormente.<br>**Valores válidos:**<br>**1**– Inclusão;<br>**2**– Alteração;<br>**3**– Exclusão.<br>**Regras de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE<br>ALTERACAO_SEM_CHAVE_PREEXISTENTE<br>EXCLUSAO_SEM_CHAVE_PREEXISTENTE|
|6<br>motExcl|ideEvento|E|N|0-1|1|-<br>Motivo da exclusão.<br>Código do motivo que justifica a exclusão do evento.<br>**Preenchimento:**Obrigatório se {tpOper} = [3].<br>**Valores válidos:**<br>**1**– Determinação judicial ou administrativa;<br>**2**– Envio indevido (fato inexistente);<br>**3**– Erro na identificação (CNPJ/período incorretos);<br>**9**– Outro.<br>**Regra de validação:**<br>EXIGIR_MOTIVO_EXCLUSAO|
|7<br>nrProc|ideEvento|E|C|0-1|1-21|-<br>Informar o número do processo administrativo/judicial.<br>**Preenchimento:**Obrigatório se {motExcl} = [1].<br>**Validação:**Deve ser um número de processo válido e existente no<br>evento D-1021.<br>**Regra de validação:**<br>EXIGIR_NUMERO_PROCESSO|
|8<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|9<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|10verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|11<br>ideContrib|evtInfoContrib|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|12nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regra de validação:**<br>CONTRIBUINTE_NO_CADASTRO|



**Página 4 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D**<br>**#**<br>**Grupo/Tag**|**-1001 – Informaçõ**<br>**Grupo Pai**|**es do**<br>**Cat**|**Contri**<br>**Tipo**|**buinte**<br>**Ocorr**|**– Espe**<br>**Tam**|**cificação Técnica de Campos (Detalhamento)**<br>**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|13<br>idePeriodo|evtInfoContrib|G|-|1-1|-|-<br>Período de validade.|
|14iniValid|idePeriodo|E|D|1-1|10|-<br>Data de início da validade das informações.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Deve ser igual ou posterior à data de início da<br>obrigatoriedade da DeRE para o declarante.<br>**Regra de validação:**<br>INI_VALID|
|15fimValid|idePeriodo|E|D|0-1|10|-<br>Data de término da validade das informações, se houver.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Se informado, deve ser igual ou posterior a {iniValid}.<br>**Regra de validação:**<br>FIM_VALID|
|16<br>novaValidade|idePeriodo|G|-|0-1|-|-<br>Novo período de validade.<br>Usado exclusivamente quando a vigência do evento precisa ser<br>alterada para um valor diferente.<br>**Preenchimento:**Exclusivo se {tpOper} = [2] (Alteração) E se houver<br>alteração do {idePeriodo} referenciado.<br>**Regra de validação:**<br>EXIGIR_TP_OPER_ALTERACAO_PARA_NOVA_VALIDADE|
|17iniValid|novaValidade|E|D|1-1|10|-<br>Data de início da nova validade das informações.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Deve ser igual ou posterior à data de início da<br>obrigatoriedade da DeRE para o declarante.<br>**Regra de validação:**<br>INI_VALID|
|18fimValid|novaValidade|E|D|0-1|10|-<br>Data de término da nova validade das informações, se houver.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Se informado, deve ser igual ou posterior a {iniValid}.<br>**Regra de validação:**<br>FIM_VALID|
|19<br>infoContrib|evtInfoContrib|G|-|0-1|-|-<br>Informações do contribuinte.<br>**Regra de validação:**<br>OBRIGAR_PREENCH_INFO_CONTRIB|
|20regTribPrinc|infoContrib|E|N|1-1|1|-<br>Regime de tributação ao qual o contribuinte está sujeito em sua<br>atividade preponderante.<br>**Valores válidos:**<br>**1**– Regime Específico de Serviços Financeiros;<br>**2**– Regime Específico de Plano de Assistência à Saúde;<br>**3**– Regime Específico de Concursos de Prognósticos;<br>**9**– Outros Regimes de Tributação.|
|21regTribSecund|infoContrib|E|N|0-3|1|-<br>Regime de tributação ao qual o contribuinte está sujeito em sua<br>atividade secundária.<br>**Validação:**Não pode ser igual ao informado em {regTribPrinc}.<br>**Valores válidos:**<br>**1**– Regime Específico de Serviços Financeiros;<br>**2**– Regime Específico de Plano de Assistência à Saúde;<br>**3**– Regime Específico de Concursos de Prognósticos.<br>**Regra de validação:**<br>REG_SEC_DIFERENTE_REG_PRINC|
|22indNatTrib|infoContrib|E|N|1-1|1|-<br>Indicador da natureza tributária do declarante (para casos de<br>imunidades ou não incidências subjetivas).<br>**Valores válidos:**<br>**0**– Tributação regular;<br>**1**– Imunidade ou não incidência.|



**Página 5 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-1001 – Informaçõ**|**es do**|**Cont**|**ribuinte**|**– Esp**|**ecificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|23<br>servFinanc|infoContrib|G|-|0-1|-|-<br>Informações de serviços financeiros.<br>Exclusivo para contribuintes deste setor.<br>**Regras de validação:**<br>EXIGIR_GRUPO_REGIME<br>REJEITAR_GRUPO_REGIME|
|24<br>tpAtividades|servFinanc|G|-|1-1|-|-<br>Lista de atividades realizadas pelo contribuinte.|
|25tpAtividade|tpAtividades|E|C|1-99|3|-<br>Identificação da atividade realizada pelo contribuinte, conforme<br>[[Tabela21– Atividades de Serviços Financeiros]].<br>**Máscara:**NNC<br>**Regra de validação:**<br>TPATIVIDADE_REGFIN|
|26<br>plAssistSaude|infoContrib|G|-|0-1|-|-<br>Informações de planos de assistência à saúde.<br>Exclusivo para contribuintes deste setor.<br>**Regras de validação:**<br>EXIGIR_GRUPO_REGIME<br>REJEITAR_GRUPO_REGIME|
|27<br>tpAtividades|plAssistSaude|G|-|1-1|-|-<br>Lista de atividades realizadas pelo contribuinte.|
|28tpAtividade|tpAtividades|E|C|1-99|3|-<br>Identificação da atividade realizada pelo contribuinte, conforme<br>[[Tabela31– Atividades de Planos de Assistência à Saúde]].<br>**Máscara:**NNC<br>**Regra de validação:**<br>TPATIVIDADE_REGSAUDE|
|29<br>prognosticos|infoContrib|G|-|0-1|-|-<br>Informações de concursos de prognósticos.<br>Exclusivo para contribuintes deste setor.<br>**Regras de validação:**<br>EXIGIR_GRUPO_REGIME<br>REJEITAR_GRUPO_REGIME|
|30<br>tpAtividades|prognosticos|G|-|1-1|-|-<br>Lista de atividades realizadaspelo contribuinte.|
|31tpAtividade|tpAtividades|E|C|1-99|3|-<br>Identificação da atividade realizada pelo contribuinte, conforme<br>[[Tabela41– Atividades de Concursos de Prognósticos]].<br>**Máscara:**NNC<br>**Regra de validação:**<br>TPATIVIDADE_REGPROG|
|32<br>UFsCredenc|prognosticos|G|-|0-1|-|-<br>Listagem das Unidades da Federação (UF) nas quais o contribuinte<br>possui credenciamento para operar.|
|33UFCredenc|UFsCredenc|E|N|1-30|2|-<br>Identificação da Unidade Federativa credenciadora, conforme [[Tabela<br>13– Unidades Federativas (UF)]].|



**Página 6 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **1.2 EVENTO D-1011 – PLANO GERAL DE CONTAS COMENTADO** 

### **1.2.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D**<br> <br>|**-1011 – Plano Gera**<br>|**l de Contas Comentado – Estrutura Hie**<br>|**rárquica d**<br>|**o Evento (Resum**<br>|**o)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtPGCC|DeRE|Evento Plano Geral de Contas Comentado.|1-1|id|O|
|3|ideEvento|evtPGCC|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtPGCC|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtPGCC|Período de validade.|1-1|iniValid, fimValid|O|
|4|novaValidade|idePeriodo|Novo período de validade.|0-1|iniValid, fimValid|F (se {tpOper} = [2]);<br>N (nos demais casos).|
|3|infoPGCC|evtPGCC|Informações do plano geral de contas<br>comentado.|0-1|-|N (se {tpOper} = [3]);<br>O (nos demais casos).|
|4|infoContas|infoPGCC|Lista de detalhamentos de informações de<br>contas contábeis.|1-1|-|O|
|5|infoConta|infoContas|Detalhamento de informações da conta<br>contábil.|1-150000|cCta|O|



**Página 7 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **1.2.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-1**<br>**#**<br>**Grupo/Tag**|**011 – Plano Geral**<br>**Grupo Pai**|**de Con**<br>**Cat**|**tas C**<br>**Tipo**|**omenta**<br>**Ocorr**|**do – Espe**<br>**Tam**<br>**De**|**cificação Técnica de Campos (Detalhamento)**<br>**c**<br>**Descrição**|
|---|---|---|---|---|---|---|
|1<br>DeRE|-|G|-|1-1|-<br>-|Envelope raiz dos eventos da DeRE.|
|2<br>evtPGCC|DeRE|G|-|1-1|-<br>-|Evento Plano Geral de Contas Comentado.|
|3<br>id|evtPGCC|A|C|1-1|42<br>-|Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtPGCC|G|-|1-1|-<br>-|Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1<br>-|Tipo de operação do evento.<br>**Nota:**A alteração substitui integralmente as informações do evento<br>enviado anteriormente.<br>**Valores válidos:**<br>**1**– Inclusão;<br>**2**– Alteração;<br>**3**– Exclusão.<br>**Regras de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE<br>ALTERACAO_SEM_CHAVE_PREEXISTENTE<br>EXCLUSAO_SEM_CHAVE_PREEXISTENTE|
|6<br>motExcl|ideEvento|E|N|0-1|1<br>-|Motivo da exclusão.<br>Código do motivo que justifica a exclusão do evento.<br>**Preenchimento:**Obrigatório se {tpOper} = [3].<br>**Valores válidos:**<br>**1**– Determinação judicial ou administrativa;<br>**2**– Envio indevido (fato inexistente);<br>**3**– Erro na identificação (CNPJ/período incorretos);<br>**9**– Outro.<br>**Regra de validação:**<br>EXIGIR_MOTIVO_EXCLUSAO|
|7<br>nrProc|ideEvento|E|C|0-1|1-21<br>-|Informar o número do processo administrativo/judicial.<br>**Preenchimento:**Obrigatório se {motExcl} = [1].<br>**Validação:**Deve ser um número de processo válido e existente no<br>evento D-1021.<br>**Regra de validação:**<br>EXIGIR_NUMERO_PROCESSO|
|8<br>tpAmb|ideEvento|E|N|1-1|1<br>-|Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|9<br>aplicEmi|ideEvento|E|N|1-1|1<br>-|Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|10verAplic|ideEvento|E|C|1-1|1-20<br>-|Versão do aplicativo emissor do evento.|
|11<br>ideContrib|evtPGCC|G|-|1-1|-<br>-|Informações de identificação do contribuinte.|
|12nrInsc|ideContrib|E|C|1-1|8<br>-|Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|



**Página 8 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-1**|**011 – Plano Geral**|**de Con**|**tas C**|**omenta**|**do – Es**|**pec**|**ificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>|**Dec**|<br>**Descrição**|
|13<br>idePeriodo|evtPGCC|G|-|1-1|-|-|Período de validade.|
|14iniValid|idePeriodo|E|D|1-1|10|-|Data de início da validade das informações.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Deve ser igual ou posterior à data de início da<br>obrigatoriedade da DeRE para o declarante.<br>**Regra de validação:**<br>INI_VALID|
|15fimValid|idePeriodo|E|D|0-1|10|-|Data de término da validade das informações, se houver.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Se informado, deve ser igual ou posterior a {iniValid}.<br>**Regra de validação:**<br>FIM_VALID|
|16<br>novaValidade|idePeriodo|G|-|0-1|-|-|Novo período de validade.<br>Usado exclusivamente quando a vigência do evento precisa ser<br>alterada para um valor diferente.<br>**Preenchimento:**Exclusivo se {tpOper} = [2] (Alteração) E se houver<br>alteração do {idePeriodo} referenciado.<br>**Regra de validação:**<br>EXIGIR_TP_OPER_ALTERACAO_PARA_NOVA_VALIDADE|
|17iniValid|novaValidade|E|D|1-1|10|-|Data de início da nova validade das informações.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Deve ser igual ou posterior à data de início da<br>obrigatoriedade da DeRE para o declarante.<br>**Regra de validação:**<br>INI_VALID|
|18fimValid|novaValidade|E|D|0-1|10|-|Data de término da nova validade das informações, se houver.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Se informado, deve ser igual ou posterior a {iniValid}.<br>**Regra de validação:**<br>FIM_VALID|
|19<br>infoPGCC|evtPGCC|G|-|0-1|-|-|Informações do plano geral de contas comentado.<br>**Regra de validação:**<br>OBRIGAR_PREENCH_INFO_PGCC|
|20planoCtaRef|infoPGCC|E|N|1-1|1|-|Identificação do Plano de Contas Referencial vinculado à atividade<br>preponderante do contribuinte.<br>**Valores válidos:**<br>**1**– COSIF;<br>**2**– ANS;<br>**3**– SUSEP;<br>**4**– SPED;<br>**5**– PREVIC.|
|21freqEncerr|infoPGCC|E|C|1-1|1|-|Indicador de frequência de encerramento contábil.<br>Informa a periodicidade com que a entidade realiza o encerramento<br>das contas de resultado (receitas e despesas) para apuração do<br>resultado do exercício.<br>**Valores válidos:**<br>**A**– Anual;<br>**S**– Semestral;<br>**Q**– Quadrimestral;<br>**T**– Trimestral;<br>**B**– Bimestral;<br>**M**– Mensal.|



**Página 9 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-**|**1011 – Plano Geral**|**de Con**|**tas C**|**omenta**|**do – Esp**|**ecificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|22<br>infoContas|infoPGCC|G|-|1-1|-<br>-|<br>Lista de detalhamentos de informações de contas contábeis.|
|23<br>infoConta|infoContas|G|-|1-150000|<br>-<br>-|<br>Detalhamento de informações da conta contábil.|
|24cCta|infoConta|E|C|1-1|1-53<br>-|<br>Código completo da conta. Resultante da concatenação de<br>{cCtaInterna} e {cDbrMista}.<br>**Preenchimento:**Informar apenas caracteres alfanuméricos, sem<br>pontos ou traços.<br>**Regras de validação:**<br>CCTA_CONCATENACAO<br>UNICIDADE_CCTA|
|25cCtaInterna|infoConta|E|C|1-1|1-50<br>-|<br>Código da conta contábil de acordo com o Plano de Contas interno<br>(Grupo, Subgrupo, Título e Subtítulo).<br>**Preenchimento:**Informar apenas caracteres alfanuméricos, sem<br>pontos ou traços.<br>**Nota:**Devem ser informadas todas as contas patrimoniais e de<br>resultado.|
|26cDbrMista|infoConta|E|C|1-1|3<br>-|<br>Desdobramento de conta mista.<br>**Preenchimento:**Se não houver desdobramento, informar [000]. Caso<br>contrário, sequencial de [001] a [999].<br>**Nota:**Havendo conta mista, esta assume nível sintético [000] e seus<br>desdobramentos assumem nível analítico.<br>**Valores válidos:**<br>**000-999**.|
|27nomeCta|infoConta|E|C|1-1|1-100<br>-|<br>Nome da conta.|
|28indCta|infoConta|E|C|1-1|1<br>-|<br>Indicador do tipo de conta (sintética ou analítica).<br>**Valores válidos:**<br>**S**– Sintética;<br>**A**– Analítica.<br>**Regra de validação:**<br>INDCTA_CONTAS_DESDOBR|
|29descCta|infoConta|E|C|0-1|1-600<br>-|<br>Descrição detalhada da natureza das operações contabilizadas na<br>conta.<br>**Validação:**Obrigatório se {indCta} = [A].<br>**Regra de validação:**<br>DESCRICAO_CTA|
|30cCtaSup|infoConta|E|C|0-1|1-53<br>-|<br>Código da conta ({cCta}) hierárquica imediatamente superior.<br>**Preenchimento:**Informar apenas caracteres alfanuméricos, sem<br>pontos ou traços. A conta desdobrada deve referenciar a mesma<br>superior da conta mista.<br>**Validação:**Não preencher se for conta de nível 1 (raiz). Deve existir<br>um {cCta} correspondente no grupo {infoConta}.<br>**Regra de validação:**<br>PAI_CTA_ANALITICA|
|31cCtaRef|infoConta|E|C|1-1|1-13<br>-|<br>Código da conta do plano de contas referencial, conforme opção<br>informada no campo {planoCtaRef}.<br>**Preenchimento:**Informar apenas caracteres alfanuméricos, sem<br>pontos ou traços.<br>**Validação:**O código deve existir na tabela correspondente ao plano<br>informado em {planoCtaRef}:<br>Se [1] (COSIF): [[Tabela22– Plano de Contas Referencial – COSIF]]<br>Se [2] (ANS): [[Tabela32– Plano de Contas Referencial – ANS]]<br>Se [3] (SUSEP): [[Tabela23– Plano de Contas Referencial – SUSEP]]<br>Se [4] (SPED): [[Tabela14– Plano de Contas Referencial – SPED]]<br>Se [5] (PREVIC): [[Tabela24– Plano de Contas Referencial – PREVIC]]<br>Caso o {cDbrMista} seja maior que [000], o {cCtaRef} desta {cCta} deve<br>ser igual ao {cCtaRef} da {cCtaSup}.<br>**Regras de validação:**<br>CONTA_NO_PLANO_CONTAS_REFERENCIAL<br>OBRIGAR_CCTAREF_IGUAL_CONTA_PRINCIPAL|



**Página 10 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-1011 – Plano Geral de Contas Comentado – Especificação Técnica de Campos (Detalhamento)** 

|**#**<br>**Grupo/Tag**|<br>**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|32nivelCta|infoConta|E|N|1-1|1-2|-<br>Nível hierárquico da conta.<br>**Preenchimento:**Número sequencial de 1 a 99 (da mais sintética para<br>a mais analítica). Uma conta de nível 1 sempre será sintética ({indCta}<br>= [S]).<br>**Nota:**Havendo conta objeto de desdobramento, as contas<br>desdobradas devem ser classificadas no nível subsequente.<br>**Exemplo:**Se uma conta mista tiver {nivelCta} = [5], seus<br>desdobramentos terão {nivelCta} = [6].<br>**Valores válidos:**<br>**1-99**.|
|33natCta|infoConta|E|C|1-1|1|-<br>Natureza da conta.<br>**Valores válidos:**<br>**C**– Credora;<br>**D**– Devedora;<br>**V**– Variável.|
|34codNat|infoConta|E|N|1-1|1|-<br>Código da Natureza da Conta.<br>**Valores válidos:**<br>**1**– Contas do ativo;<br>**2**– Contas do passivo;<br>**3**– Contas do patrimônio líquido;<br>**4**– Contas de receita;<br>**5**– Contas de despesa.|
|35codTrib|infoConta|E|N|0-1|9|-<br>Código de tributação para fins de IBS, CBS e IS.<br>**Preenchimento:**Informar apenas caracteres numéricos, sem pontos<br>ou traços.<br>**Validação:**Exclusivo se {indCta} = [A]. Deve ser um código vigente na<br>[[Tabela11– Códigos de Tributação (codTrib)]].<br>**Regras de validação:**<br>OBRIGAR_CODTRIB<br>EXISTE_CODTRIB_NA_TABELA|
|36indTribISS|infoConta|E|N|0-1|1|-<br>Indicador de sujeição ou vinculação ao ISSQN.<br>Informa se os valores recebidos ou receitas auferidas sofrem a<br>incidência de ISSQN.<br>**Validação:**Não preencher se {indCta} = [S].<br>**Valores válidos:**<br>**0**– Não sujeita ao ISS;<br>**1**– Sujeita ao ISS.<br>**Regra de validação:**<br>OBRIGAR_IND_TRIB_ISS|
|37idLeiDisp|infoConta|E|C|0-1|5|-<br>Código que identifica o fundamento legal utilizado para embasar a<br>destinação obrigatória de recursos a órgãos/fundos públicos/demais<br>beneficiários ou a classificação tributária que exija comprovação legal<br>específica na escrituração.<br>**Máscara:**CC-CC<br>**Nota:**Regra de preenchimento do campo a ser informada em futura<br>versão da DeRE.<br>**Valores válidos:**<br>**00-00**.|
|38iniVig|infoConta|E|D|1-1|10|-<br>Data de início de vigência da conta.<br>**Máscara:**AAAA-MM-DD|
|39fimVig|infoConta|E|D|0-1|10|-<br>Data de término de vigência da conta, se houver.<br>**Máscara:**AAAA-MM-DD<br>**Validação:**Se informada, deve ser igual ou posterior a {iniVig}.<br>**Regra de validação:**<br>DT_FIM_VIGENCIA|



**Página 11 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2 EVENTOS PERIÓDICOS MENSAIS** 

### **2.1 EVENTO D-1101 – BALANCETE MENSAL** 

### **2.1.1 Estrutura Hierárquica do Evento (Resumo)** 

||<br>|**D-1101 – Ba**<br>|**lancete Mensal – Estrutura Hierárquica**<br>|**do Evento (**<br>|**Resumo)**<br>||
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtBalancete|DeRE|Evento Balancete Mensal.|1-1|id|O|
|3|ideEvento|evtBalancete|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtBalancete|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtBalancete|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoBalancete|evtBalancete|Informações do Balancete Mensal.|0-1|-|N (se {tpOper} = [3]);<br>O (nos demais casos).|
|4|infoContas|infoBalancete|Lista de contas do balancete.|1-1|-|O|
|5|infoConta|infoContas|Detalhamento de informações da conta<br>contábil no balancete.|1-90000|-|O|



**Página 12 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.1.2 Especificação Técnica dos Campos (Detalhamento)** 

||**D-1101 – Bala**|**ncete**|**Mens**|**al – Esp**|**ecificação**|**Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**Dec**|<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-<br>-|Envelope raiz dos eventos da DeRE.|
|2<br>evtBalancete|DeRE|G|-|1-1|-<br>-|Evento Balancete Mensal.|
|3<br>id|evtBalancete|A|C|1-1|42<br>-|Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtBalancete|G|-|1-1|-<br>-|Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1<br>-|Tipo de operação do evento.<br>**Nota:**A alteração substitui integralmente as informações do evento<br>enviado anteriormente.<br>**Valores válidos:**<br>**1**– Inclusão;<br>**2**– Alteração;<br>**3**– Exclusão.<br>**Regra de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE|
|6<br>motExcl|ideEvento|E|N|0-1|1<br>-|Motivo da exclusão.<br>Código do motivo que justifica a exclusão do evento.<br>**Preenchimento:**Obrigatório se {tpOper} = [3].<br>**Valores válidos:**<br>**1**– Determinação judicial ou administrativa;<br>**2**– Envio indevido (fato inexistente);<br>**3**– Erro na identificação (CNPJ/período incorretos);<br>**9**– Outro.<br>**Regra de validação:**<br>EXIGIR_MOTIVO_EXCLUSAO|
|7<br>nrProc|ideEvento|E|C|0-1|1-21<br>-|Informar o número do processo administrativo/judicial.<br>**Preenchimento:**Obrigatório se {motExcl} = [1].<br>**Validação:**Deve ser um número de processo válido e existente no<br>evento D-1021.<br>**Regra de validação:**<br>EXIGIR_NUMERO_PROCESSO|
|8<br>nrRecibo|ideEvento|E|C|0-1|31<br>-|Caso seja um evento de alteração/retificação ou exclusão, preencher<br>com o número do recibo do arquivo a ser alterado/retificado ou<br>excluído.<br>**Regras de validação:**<br>RN - Formação do Número do Recibo do Evento<br>ULTIMO_RECIBO_ATIVO<br>EXIGIR_NR_RECIBO|
|9<br>tpAmb|ideEvento|E|N|1-1|1<br>-|Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|10aplicEmi|ideEvento|E|N|1-1|1<br>-|Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|11verAplic|ideEvento|E|C|1-1|1-20<br>-|Versão do aplicativo emissor do evento.|



**Página 13 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-1101 – Bala**|**ncete**|**Mens**|**al – Esp**|**ecifica**|**ção Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|12<br>ideContrib|evtBalancete|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|13nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|14<br>idePeriodo|evtBalancete|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|15perApur|idePeriodo|E|D|1-1|7|-<br>Informar o período de apuração, sendo o ano e mês da competência<br>da declaração.<br>**Máscara:**AAAA-MM<br>**Validação:**Se {tpOper} = [1], só pode existir um único {perApur} para<br>cada {nrInsc}.<br>Quando informado {nrRecibo}, o {perApur} deve ser exatamente o<br>mesmo existente no {nrRecibo} (caracteres 6 a 11 do recibo).<br>**Regras de validação:**<br>OBRIGAR_PERAPUR_IGUAL_NRRECIBO<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|16<br>infoBalancete|evtBalancete|G|-|0-1|-|-<br>Informações do Balancete Mensal.<br>**Regra de validação:**<br>OBRIGAR_PREENCH_INFO_BALANCETE|
|17<br>infoContas|infoBalancete|G|-|1-1|-|-<br>Lista de contas do balancete.|
|18<br>infoConta|infoContas|G|-|1-90000|<br>-|-<br>Detalhamento de informações da conta contábil no balancete.|
|19cCta|infoConta|E|C|1-1|1-53|-<br>Código da conta analítica (como informada no evento D-1011.{cCta}).<br>**Preenchimento:**Não informar contas sintéticas.<br>**Nota:**Devem estar no PGCC vigente no último dia do {perApur} todas<br>as contas que tiveram movimentação no período, ainda que tenham<br>sido criadas ou encerradas no período.<br>**Validação:**O {cCta} deve existir e estar vigente na tabela PGCC do<br>contribuinte no último dia do {perApur} correspondente.<br>**Regra de validação:**<br>CCTA_NO_PGCC|
|20natSaldoInic|infoConta|E|C|1-1|1|-<br>Natureza do saldo inicial da conta na abertura do período de<br>apuração.<br>**Valores válidos:**<br>**D**– Devedor;<br>**C**– Credor.|
|21vSaldoInic|infoConta|E|N|1-1|4-18|2<br>Valor do saldo inicial da conta no período de competência.<br>**Preenchimento:**Valor absoluto (sem sinal).<br>Se D-1011.{codNat} = [1; 2; 3] (contas patrimoniais); então {vSaldoInic}<br>deve corresponder ao {vSaldoFinal} da conta no período anterior.<br>Se {codNat} = [4; 5] e D-1011.{freqEncerr} = [A], e {perApur} = [AAAA-<br>01]; então {vSaldoInic} = [0.00].<br>Se {codNat} = [4; 5] e D-1011.{freqEncerr} = [S], e {perApur} = [AAAA-<br>01 ou AAAA-07]; então {vSaldoInic} = [0.00].<br>Se {codNat} = [4; 5] e D-1011.{freqEncerr} = [Q], e {perApur} = [AAAA-<br>01; AAAA-05 ou AAAA-09]; então {vSaldoInic} = [0.00].<br>Se {codNat} = [4; 5] e D-1011.{freqEncerr} = [T], e {perApur} = [AAAA-<br>01; AAAA-04; AAAA-07 ou AAAA-10]; então {vSaldoInic} = [0.00].<br>Se {codNat} = [4; 5] e D-1011.{freqEncerr} = [B], e {perApur} = [AAAA-<br>01; AAAA-03; AAAA-05; AAAA-07; AAAA-09; AAAA-11]; então<br>{vSaldoInic} = [0.00].<br>Se {codNat} = [4; 5] e D-1011.{freqEncerr} = [M], então {vSaldoInic} =<br>[0.00].|
|22vMovDebt|infoConta|E|N|1-1|4-18|2<br>Valor total dos lançamentos a débito realizados na conta no mês de<br>competência, em valor absoluto e sem sinal.<br>**Preenchimento:**Informar [0.00] se não houver movimentação.|



**Página 14 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**#**<br>**Grupo/Tag**|**D-1101 – Bala**<br>**Grupo Pai**|**ncete**<br>**Cat**|**Mens**<br>**Tipo**|**al – Esp**<br>**Ocorr**|**ecifica**<br>**Tam**|**ção Técnica de Campos (Detalhamento)**<br>**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|23vAjusteDebt|infoConta|E|N|0-1|4-18|2<br>Valor dos ajustes a serem excluídos no {vMovDebt} para fins de cálculo<br>do {vApur} (ex: estornos, cancelamentos etc.).<br>**Validação:**Deve ser menor ou igual a {vMovDebt}.<br>**Regra de validação:**<br>CONFERIR_AJUSTE_DEBITO|
|24vMovCred|infoConta|E|N|1-1|4-18|2<br>Valor total dos lançamentos a crédito realizados na conta no mês de<br>competência, em valor absoluto e sem sinal.<br>**Preenchimento:**Informar [0.00] se não houver movimentação.|
|25vAjusteCred|infoConta|E|N|0-1|4-18|2<br>Valor dos ajustes a serem excluídos no {vMovCred}, ex: estornos,<br>cancelamentos etc., ou a serem somados no {vMovDev} para fins de<br>cálculo do {vApur}.<br>**Validação:**Deve ser menor ou igual a {vMovCred}.<br>**Regra de validação:**<br>CONFERIR_AJUSTE_CREDITO|
|26natSaldoFinal|infoConta|E|C|1-1|1|-<br>Natureza do saldo final da conta no encerramento do mês de<br>competência.<br>**Valores válidos:**<br>**D**– Devedor;<br>**C**– Credor.|
|27vSaldoFinal|infoConta|E|N|1-1|4-18|2<br>Valor do saldo final da conta no período de competência. Deve ser<br>informado antes do encerramento das contas de resultado.<br>**Preenchimento:**Valor absoluto (sem sinal).<br>**Regra de validação:**<br>CONFERIR_SALDO_FINAL_NAT_SALDO_FINAL|
|28natVApur|infoConta|E|C|0-1|1|-<br>Informar a natureza do movimento informado no {vApur}.<br>**Preenchimento:**Obrigatório se {vApur} > [0.00].<br>**Valores válidos:**<br>**D**– Movimento devedor;<br>**C**– Movimento credor.<br>**Regra de validação:**<br>OBRIGAR_NATVAPUR|
|29vApur|infoConta|E|N|1-1|4-18|2<br>Valor base sobre o qual serão aplicadas as regras definidas pelo<br>código de tributação ({codTrib}).<br>**Preenchimento:**Informar [0.00] se não houver movimentação.<br>**Regra de validação:**<br>CONFERIR_VAPUR|



**Página 15 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.2 EVENTO D-1106 – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS** 

### **2.2.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-11**<br> <br>|**06 – Identificação**<br>|**de Aplicações Financeiras – Estrutura Hie**<br>|**rárquica**<br>|**do Evento (R**<br>|**esumo)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtAplicResTec|DeRE|Evento de identificação e detalhamento de<br>aplicações financeiras sobre reserva técnica.|1-1|id|O|
|3|ideEvento|evtAplicResTec|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtAplicResTec|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtAplicResTec|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoAplicResTec|evtAplicResTec|Informações de aplicações financeiras sobre<br>reserva técnica.|0-1|-|N (se {tpOper} = [3]);<br>O (nos demais casos).|
|4|infoAplic|infoAplicResTec|Detalhamento das informações da aplicação<br>financeira sobre reserva técnica.|0-100|cCta|N (se {semAplic} = [1]);<br>O (nos demais casos).|
|5|detAtivo|infoAplic|Detalhamento do título/ativo.|1-500|idAtivo|O|



**Página 16 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.2.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-110**|**6 – Identificação d**|**e Apli**|**cações**|**Finan**|**ceiras –**|**Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtAplicResTec|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de aplicações financeiras<br>sobre reserva técnica.<br>**Regra de validação:**<br>CODTRIB_PERMITIDO_D1106|
|3<br>id|evtAplicResTec|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtAplicResTec|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1|-<br>Tipo de operação do evento.<br>**Nota:**A alteração substitui integralmente as informações do evento<br>enviado anteriormente.<br>**Valores válidos:**<br>**1**– Inclusão;<br>**2**– Alteração;<br>**3**– Exclusão.<br>**Regra de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE|
|6<br>motExcl|ideEvento|E|N|0-1|1|-<br>Motivo da exclusão.<br>Código do motivo que justifica a exclusão do evento.<br>**Preenchimento:**Obrigatório se {tpOper} = [3].<br>**Valores válidos:**<br>**1**– Determinação judicial ou administrativa;<br>**2**– Envio indevido (fato inexistente);<br>**3**– Erro na identificação (CNPJ/período incorretos);<br>**9**– Outro.<br>**Regra de validação:**<br>EXIGIR_MOTIVO_EXCLUSAO|
|7<br>nrProc|ideEvento|E|C|0-1|1-21|-<br>Informar o número do processo administrativo/judicial.<br>**Preenchimento:**Obrigatório se {motExcl} = [1].<br>**Validação:**Deve ser um número de processo válido e existente no<br>evento D-1021.<br>**Regra de validação:**<br>EXIGIR_NUMERO_PROCESSO|
|8<br>nrRecibo|ideEvento|E|C|0-1|31|-<br>Caso seja um evento de alteração/retificação ou exclusão, preencher<br>com o número do recibo do arquivo a ser alterado/retificado ou<br>excluído.<br>**Regras de validação:**<br>RN - Formação do Número do Recibo do Evento<br>EXIGIR_NR_RECIBO<br>ULTIMO_RECIBO_ATIVO|
|9<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|10aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|11verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|



**Página 17 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-1106 – Identificação de Aplicações Financeiras – Especificação Técnica de Campos (Detalhamento)** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|12<br>ideContrib|evtAplicResTec|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|13nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|14<br>idePeriodo|evtAplicResTec|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|15perApur|idePeriodo|E|D|1-1|7|-<br>Informar o período de apuração, sendo o ano e mês da competência<br>da declaração.<br>**Máscara:**AAAA-MM<br>**Validação:**Se {tpOper} = [1], só pode existir um único {perApur} para<br>cada {nrInsc}.<br>Quando informado {nrRecibo}, o {perApur} deve ser exatamente o<br>mesmo existente no {nrRecibo} (caracteres 6 a 11 do recibo).<br>**Regras de validação:**<br>OBRIGAR_PERAPUR_IGUAL_NRRECIBO<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|16<br>infoAplicResTec|evtAplicResTec|G|-|0-1|-|-<br>Informações de aplicações financeiras sobre reserva técnica.<br>**Regra de validação:**<br>OBRIGAR_PREENCH_INFO_RES_TEC|
|17semAplic|infoAplicResTec|E|N|0-1|1|-<br>Indicação da inexistência de aplicações financeiras sobre reserva<br>técnica.<br>**Preenchimento:**Deve ser preenchido exclusivamente quando o<br>contribuinte não possuir ativos financeiros vinculados à reserva<br>técnica a detalhar no período de apuração {perApur}.<br>**Valores válidos:**<br>**1**– Não existem informações de aplicações financeiras a declarar para<br>este período.|
|18<br>infoAplic|infoAplicResTec|G|-|0-100|-|-<br>Detalhamento das informações da aplicação financeira sobre reserva<br>técnica.|
|19cCta|infoAplic|E|C|1-1|1-53|-<br>Código da conta analítica (como informada no evento D-1011.{cCta}).<br>**Preenchimento:**Não informar contas sintéticas.<br>**Nota:**Devem estar no PGCC vigente no último dia do {perApur} todas<br>as contas que tiveram movimentação no período, ainda que tenham<br>sido criadas ou encerradas no período.<br>**Validação:**O {cCta} deve existir e estar vigente na tabela PGCC do<br>contribuinte no último dia do {perApur} correspondente.<br>**Regras de validação:**<br>CCTA_NO_PGCC<br>CODTRIB_COMPATIVEL_EVT|
|20<br>detAtivo|infoAplic|G|-|1-500|-|-<br>Detalhamento do título/ativo.|
|21idAtivo|detAtivo|A|C|1-1|1-30|-<br>Número de identificação do título/ativo.|
|22descAtivo|detAtivo|E|C|1-1|1-255|-<br>Descrição da denominação do ativo financeiro vinculado a reserva<br>técnica.|
|23vSaldoInic|detAtivo|E|N|1-1|4-18|2<br>Valor do título em 01/01/2026 ou na data da aquisição, se esta for<br>posterior a esta data.<br>**Preenchimento:**Deve ser igual ao {vSaldoFinal} do período anterior<br>para o mesmo {idAtivo} (exceto na primeira competência do<br>declarante).|
|24vRendPerReceb|detAtivo|E|N|0-1|4-18|2<br>Valor de rendimentos periódicos recebidos no período de apuração<br>(cupons, dividentos, etc).|
|25vVarMensal|detAtivo|E|N|0-1|4-19|2<br>Valores de variação mensal a serem incorporados ao saldo do ativo.<br>**Nota:**Valores podem ser positivos ou negativos.|
|26vPrincLiqResg|detAtivo|E|N|1-1|4-18|2<br>Valor do principal liquidado ou resgatado do ativo financeiro.|



**Página 18 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-1106**|**– Identificação d**|**e Aplic**|**ações**|**Financ**|**eiras**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|27vRendLiqResg|detAtivo|E|N|0-1|4-18|2<br>Valor dos rendimentos recebidos na liquidação ou resgate do ativo<br>financeiro.|
|28vSaldoFinal|detAtivo|E|N|1-1|4-18|2<br>Valor do título no último dia do período de apuração {perApur}.<br>**Cálculo:**<br>{vSaldoInic} + {vVarMensal} - {vPrincLiqResg}<br>**Regra de validação:**<br>CONFERIR_SALDO_FINAL_ATIVO|
|29vApur|detAtivo|E|N|1-1|4-18|2<br>Valor base sobre o qual serão aplicadas as regras definidas pelo<br>código de tributação ({codTrib}).<br>**Cálculo:**<br>{vRendPerReceb} + {vRendLiqResg}<br>**Regra de validação:**<br>VALIDAR_VAPUR_D1106|



**Página 19 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.3 EVENTO D-1121 – RELAÇÃO DE DEDUÇÕES UTILIZADAS NA APURAÇÃO** 

### **2.3.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-1121**<br> <br>|**– Relação de Dedu**<br>|**ções Utilizadas na Apuração – Estrutura**<br>|**Hierárqu**<br>|**ica do Evento**<br>|**(Resumo)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRelDeducoes|DeRE|Evento Relação de Deduções Utilizadas na<br>Apuração.|1-1|id|O|
|3|ideEvento|evtRelDeducoes|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtRelDeducoes|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtRelDeducoes|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoDeducoes|evtRelDeducoes|Informações das deduções utilizadas na<br>apuração.|0-1|-|N (se {tpOper} = [3]);<br>O (nos demais casos).|
|4|infoDeducao|infoDeducoes|Informações da dedução.|1-50000 c|hDFe, chDFeRetif,<br>CPFAlien|O|
|5|infoDFe|infoDeducao|Informação do documento fiscal.|0-1|-|OC|
|5|infoImovel|infoDeducao|Informações de imóvel objeto de<br>arrendamento mercantil.|0-1|CPFAlien|OC|
|5|detDeducao|infoDeducao|Grupo de informação das deduções.|1-1|-|O|
|5|itemDFe|infoDeducao|Relação de itens específicos do documento<br>fiscal eletrônico (DFe) utilizados para<br>dedução.|0-998|-|O (se OU({perApur} igual ao<br>mês/ano de {dtEmi} E {tpDFe}<br>= [01; 03; 04] E {vDedTotal} <<br>{vOper}) OU ({perApur}<br>posterior ao mês/ano de<br>{dtEmi} E  {vItemDed} > [0.00]<br>E {chDFe} já existir na base<br>com detalhamento do grupo<br>{itemDFe}));<br>N (nos demais casos).|



**Página 20 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.3.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-1121 –**<br>**#**<br>**Grupo/Tag**|**Relação de Deduç**<br>**Grupo Pai**|**ões Ut**<br>**Cat**|**ilizad**<br>**Tipo**|**as na A**<br>**Ocorr**|**puração –**<br>**Tam**<br>**Dec**|**Especificação Técnica de Campos (Detalhamento)**<br> <br>**Descrição**|
|---|---|---|---|---|---|---|
|1<br>DeRE|-|G|-|1-1|-<br>-|Envelope raiz dos eventos da DeRE.|
|2<br>evtRelDeducoes|DeRE|G|-|1-1|-<br>-|Evento Relação de Deduções Utilizadas na Apuração.|
|3<br>id|evtRelDeducoes|A|C|1-1|42<br>-|Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtRelDeducoes|G|-|1-1|-<br>-|Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1<br>-|Tipo de operação do evento.<br>**Nota:**A alteração substitui integralmente as informações do evento<br>enviado anteriormente.<br>A inclusão, alteração ou exclusão do evento completo só é permitida<br>antes do fechamento mensal.<br>**Validação:**<br>1. Antes de existir processamento com sucesso de um evento de<br>Fechamento Mensal (D-1199) para o período ({perApur}) não é<br>permitida a recepção de eventos com {tpOper} = [4] (Retificação após<br>fechamento mensal);<br>2. Após o processamento com sucesso do Fechamento Mensal (D-<br>1199) para o período ({perApur}), caso sejam necessários ajustes, estes<br>devem ser feitos individualmente por documento fiscal utilizando a<br>operação [4] (Retificação após fechamento mensal), sendo necessário<br>o envio de um evento Reabertura de Período de Apuração (D-1198)<br>previamente e um novo Fechamento Mensal (D-1199).<br>**Valores válidos:**<br>**1**– Inclusão;<br>**2**– Alteração;<br>**3**– Exclusão;<br>**4**– Retificação após fechamento mensal.<br>**Regra de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE|
|6<br>motExcl|ideEvento|E|N|0-1|1<br>-|Motivo da exclusão.<br>Código do motivo que justifica a exclusão do evento.<br>**Preenchimento:**Obrigatório se {tpOper} = [3].<br>**Valores válidos:**<br>**1**– Determinação judicial ou administrativa;<br>**2**– Envio indevido (fato inexistente);<br>**3**– Erro na identificação (CNPJ/período incorretos);<br>**9**– Outro.<br>**Regra de validação:**<br>EXIGIR_MOTIVO_EXCLUSAO|
|7<br>nrProc|ideEvento|E|C|0-1|1-21<br>-|Número do processo administrativo/judicial.<br>**Preenchimento:**Obrigatório se {motExcl} = [1].<br>**Validação:**Deve ser um número de processo válido e existente no<br>evento D-1021.<br>**Regra de validação:**<br>EXIGIR_NUMERO_PROCESSO|
|8<br>nrRecibo|ideEvento|E|C|0-1|31<br>-|Caso seja um evento de alteração/retificação ou exclusão, preencher<br>com o número do recibo do arquivo a ser alterado/retificado ou<br>excluído.<br>**Regras de validação:**<br>RN - Formação do Número do Recibo do Evento<br>ULTIMO_RECIBO_ATIVO<br>EXIGIR_NR_RECIBO|



**Página 21 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**#**|**D-1121 –**<br>**Grupo/Tag**|**Relação de Deduç**<br>**Grupo Pai**|**ões Ut**<br>**Cat**|**ilizad**<br>**Tipo**|**as na A**<br> <br>**Ocorr**|**puração –**<br>**Tam**<br>**Dec**|**Especificação Técnica de Campos (Detalhamento)**<br> <br>**Descrição**|
|---|---|---|---|---|---|---|---|
|9<br>|tpAmb|ideEvento|E|N|1-1|1<br>-|Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|10|aplicEmi|ideEvento|E|N|1-1|1<br>-|Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|11|verAplic|ideEvento|E|C|1-1|1-20<br>-|Versão do aplicativo emissor do evento.|
|12<br>|ideContrib|evtRelDeducoes|G|-|1-1|-<br>-|Informações de identificação do contribuinte.|
|13|nrInsc|ideContrib|E|C|1-1|8<br>-|Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|14<br>|idePeriodo|evtRelDeducoes|G|-|1-1|-<br>-|Período de referência das informações do evento.|
|15|perApur|idePeriodo|E|D|1-1|7<br>-|Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Validação:**Se {tpOper} = [1], só pode existir um único {perApur} para<br>cada {nrInsc}.<br>Quando informado {nrRecibo}, o {perApur} deve ser exatamente o<br>mesmo existente no {nrRecibo} (caracteres 6 a 11 do recibo).<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|16<br>|infoDeducoes|evtRelDeducoes|G|-|0-1|-<br>-|Informações das deduções utilizadas na apuração.<br>**Preenchimento:**Não informar se {tpOper} = [3] (exclusão);<br>Obrigatório nos demais casos.|
|17|finEvt|infoDeducoes|E|N|0-1|1<br>-|Finalidade do evento.<br>**Preenchimento:**Exclusivo e obrigatório se {tpOper} for igual a [4]<br>(Retificação após fechamento mensal).<br>**Valores válidos:**<br>**1**– Inclusão de registro;<br>**2**– Alteração de registro;<br>**3**– Exclusão de registro.|
|18<br>|infoDeducao|infoDeducoes|G|-|1-50000|-<br>-|Informações da dedução.|
|19<br>|infoDFe|infoDeducao|CG|-|0-1|-<br>-|Informação do documento fiscal.<br>**Preenchimento:**Não informar se {infoImovel} for informado.|
|20|tpDFe|infoDFe|CE|C|0-1|2<br>-|Tipo de documento fiscal.<br>**Preenchimento:**Não informar se {chDFeRetif} for informado.<br>Obrigatório nos demais casos.<br>**Valores válidos:**<br>**01**– DeRE;<br>**02**– NFS-e;<br>**03**– NF-e;<br>**04**– NFC-e;<br>**05**– NF-e ABI.|



**Página 22 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-1121 –**<br>**#**<br>**Grupo/Tag**|**Relação de Deduç**<br>**Grupo Pai**|**ões Ut**<br>**Cat**|**ilizada**<br>**Tipo**|**s na A**<br>**Ocorr**|**puraçã**<br>**Tam**|**o –**<br>**Dec**|**Especificação Técnica de Campos (Detalhamento)**<br> <br>**Descrição**|
|---|---|---|---|---|---|---|---|
|21chDFe|infoDFe|CE|C|0-1|44-53|-|Chave de acesso do documento fiscal que acobertou a operação que<br>deu origem à dedução.<br>**Preenchimento:**Não informar se {chDFeRetif} for informado.<br>Obrigatório nos demais casos.<br>Uma chave de um documento fiscal ({chDFe}) só pode ocorrer uma<br>única vez por arquivo em cada período de apuração ({perApur}),<br>vedando repetições da mesma nota no mesmo mês.<br>**Nota:**<br>1. O registro inicial da chave na base da DeRE inaugura uma conta-<br>corrente para o controle de saldos do documento;<br>2. Em competências futuras, o reenvio da mesma chave é tratado<br>como apropriação contínua de deduções pendentes (ex: regime de<br>caixa), acumulando os valores de {vDed} até o limite de {vDedTotal};<br>3. A conta-corrente do documento fiscal só é inaugurada se informada<br>no seu mês de emissão, sendo vedada a abertura em {perApur}<br>distinto do {dtEmi}.|
|22dtEmi|infoDFe|E|D|1-1|10|-|Data de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DD|
|23chDFeRetif|infoDFe|CE|C|0-1|44-53|-|Chave de acesso do documento fiscal cujo registro será objeto de<br>alteração ou exclusão.<br>**Preenchimento:**Exclusivo e obrigatório se {tpOper} for igual a [4] E<br>{finEvt} for diferente de [1].|
|24tpAtiv|infoDFe|E|C|1-1|2|-|Tipo de atividade que está vinculada a dedução.<br>**Valores válidos:**<br>**01**– Operações de Crédito, Câmbio, TVM, Securitização e Faturização;<br>**02**– Arrendamentos;<br>**03**– Seguros, com exceção de Seguro Saúde;<br>**04**– Previdência;<br>**05**– Capitalização;<br>**06**– Planos de Assistência à Saúde;<br>**07**– Concursos de Prognósticos.|
|25<br>infoImovel|infoDeducao|CG|-|0-1|-|-|Informações de imóvel objeto de arrendamento mercantil.<br>**Nota:**Preenchimento exclusivo em caso de aquisição de imóvel de<br>pessoa física não contribuinte (sem emissão de DF-e).<br>**Preenchimento:**Não informar se {infoDFe} for informado no mesmo<br>grupo {infoDeducao}. Caso o declarante tenha adquirido mais de um<br>imóvel do mesmo alienante, deve ser gerada uma ocorrência distinta<br>dogrupo {infoDeducao}para cada imóvel.|
|26CPFAlien|infoImovel|E|C|1-1|11|-|Número de inscrição no CPF do alienante do imóvel, quando este não<br>for contribuinte do IBS/CBS, adquirido para fins de arrendamento<br>mercantil.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|27<br>detDeducao|infoDeducao|G|-|1-1|-|-|Grupo de informação das deduções.|
|28vOper|detDeducao|E|N|1-1|4-18|2|Valor total da operação, conforme documento fiscal ou transação.<br>**Preenchimento:**<br>1. Quando o grupo {infoDFe} for informado, este campo deve<br>corresponder ao valor total do respectivo documento {chDFe};<br>2. Quando o grupo {infoImovel} for informado, preencher com o valor<br>do imóvel.|



**Página 23 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-1121 –**|**Relação de Deduç**|**ões Ut**|**ilizad**|**as na A**|**puraçã**|**o –**|**Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**|<br>**Descrição**|
|29vDedTotal|detDeducao|E|N|0-1|4-18|2|Valor total do documento fiscal eletrônico (DFe) que será destinado à<br>dedução da base de cálculo, ainda que o aproveitamento ocorra de<br>forma parcelada em competências futuras.<br>**Exemplo:**Se o documento fiscal apresentar o valor total ({vOper}) de<br>R$ 1.000,00, mas apenas R$ 800,00 forem passíveis de dedução na<br>atividade, informar R$ 800,00.<br>**Nota:**No caso de aquisição de bens para arrendamento, informar o<br>valor total depreciável do bem.<br>**Preenchimento:**Exclusivo e obrigatório no período de apuração<br>correspondente ao mês/ano de emissão do documento fiscal (quando<br>o mês/ano de {dtEmi} corresponder ao {perApur}), e desde que o<br>grupo {infoDFe} esteja preenchido. O valor total previsto para a<br>dedução ({vDedTotal}) deve ser declarado uma única vez (no mês de<br>emissão do DF-e), sendo vedado o seu preenchimento em<br>competências subsequentes (quando o {perApur} for posterior ao<br>mês/ano do documento fiscal ({dtEmi})).<br>**Validação:**Não pode ser superior a {vOper}.|
|30vDed|detDeducao|E|N|1-1|4-18|2|Valor efetivamente utilizado como dedução da base de cálculo do IBS<br>e da CBS no período de apuração corrente. No caso de despesas ou<br>aquisições liquidadas de forma fracionada (regime de caixa), informar<br>apenas a parcela cuja dedução seja apropriada na competência atual.<br>**Preenchimento:**<br>1. No período de apuração de emissão do documento fiscal (registro<br>inicial neste evento) se não houver fração de valor a deduzir na<br>competência corrente, informar [0.00];<br>2. Nos períodos de apuração subsequentes (continuação de dedução<br>parcelada ou depreciação mensal): preenchimento obrigatório para<br>reportar a fração mensal utilizada na competência, sendo vedada a<br>informação do campo {vDedTotal} neste evento. Para a utilização da<br>dedução neste formato, é obrigatória a existência prévia da<br>informação do campo {vDedTotal} deste documento fiscal ({chDFe})<br>em período de apuração ({perApur}) anterior.<br>**Nota:**No caso de aquisição de bens destinados a arrendamento<br>mercantil operacional amparados por documento fiscal, informar o<br>valor da quota de depreciação mensal apropriada na competência.<br>**Validação:**<br>1. Quando {vDedTotal} for informado, {vDed} deve ser igual ou inferior<br>a {vDedTotal}.<br>2. A soma histórica acumulada das ocorrências de {vDed} para a<br>mesma chave de documento fiscal ({chDFe}) não poderá ultrapassar o<br>limite estabelecido no campo {vDedTotal} original.|



**Página 24 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-1121 – Relação de Deduções Utilizadas na Apuração – Especificação Técnica de Campos (Detalhamento)** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|31<br>itemDFe|infoDeducao|G|-|0-998|-|-<br>Relação de itens específicos do documento fiscal eletrônico (DFe)<br>utilizados para dedução.<br>**Preenchimento:**<br>1. Registro inicial: ({perApur} igual ao mês/ano de {dtEmi}): Obrigatório<br>se {tpDFe} = [01; 03; 04] E o valor total dedutível for menor que o valor<br>total do documento fiscal ({vDedTotal} < {vOper});<br>2. Períodos subsequentes ({perApur} posterior ao mês/ano de {dtEmi}):<br>Obrigatório para os itens com dedução na competência ({vItemDed}<br>> [0.00]), desde que a conta-corrente do documento fiscal ({chDFe})<br>tenha sido inaugurada no mês de sua emissão com o detalhamento<br>do grupo {itemDFe}.<br>**Cálculo:**<br>1. No registro inicial o somatório de todos os valores totais dedutíveis<br>por item ({vItemDedTotal}) associados ao documento fiscal deve ser<br>igual<br>ao<br>campo<br>geral<br>de teto<br>do<br>documento<br>declarado<br>(SOMA({vItemDedTotal}) = {vDedTotal});<br>2. O somatório de todos os valores de dedução do período por item<br>({vItemDed}) informados na competência atual deve ser igual ao<br>campo geral de dedução do mês (SOMA({vItemDed}) = {vDed}) para<br>este documento fiscal ({chDFe}).|
|32nItem|itemDFe|E|N|1-1|1-3|-<br>Número do item ou sequencial do documento fiscal que identifica o<br>item utilizado para a dedução.<br>**Validação:**O número informado deve corresponder a um item válido<br>e existente no documento fiscal eletrônico referenciado.|
|33vItem|itemDFe|E|N|1-1|4-18|2<br>Valor total do item, conforme documento fiscal ou transação.|
|34vItemDedTotal|itemDFe|E|N|0-1|4-18|2<br>Valor total dedutível correspondente ao item do documento fiscal,<br>ainda que o aproveitamento ocorra de forma parcelada ou diferida em<br>competências futuras.<br>**Preenchimento:**Exclusivo e obrigatório se o {vDedTotal} for<br>preenchido para este documento fiscal ({chDFe}).<br>**Validação:**O valor de {vItemDedTotal} não pode ser superior ao valor<br>correspondente ao respectivo item no documento fiscal eletrônico<br>({vItem}) referenciado.|
|35vItemDed|itemDFe|E|N|1-1|4-18|2<br>Valor efetivamente utilizado como dedução correspondente a este<br>item específico na competência (período de apuração) corrente.<br>**Preenchimento:**<br>1. No período de apuração de emissão do documento fiscal (registro<br>inicial neste evento) se não houver fração de valor a deduzir na<br>competência corrente, informar [0.00];<br>2. Nos períodos de apuração subsequentes: preenchimento<br>obrigatório para reportar a fração mensal utilizada na competência,<br>sendo vedada, neste caso, a informação do campo {vItemDedTotal}<br>neste evento. Para a utilização da dedução neste formato, é<br>obrigatória<br>a<br>existência<br>prévia<br>da<br>informação<br>do<br>campo<br>{vItemDedTotal} para este mesmo item ({nItem}) deste documento<br>fiscal ({chDFe}) em período de apuração ({perApur}) anterior.<br>**Validação:**<br>1. Quando {vItemDedTotal} for informado no mesmo evento (registro<br>inicial), o valor de {vItemDed} deve ser igual ou inferior a<br>{vItemDedTotal};<br>2. A soma histórica acumulada das ocorrências de {vItemDed} para o<br>mesmo item ({nItem}) da chave de documento fiscal ({chDFe}) não<br>poderá ultrapassar o limite de saldo estabelecido no campo<br>{vItemDedTotal} original.|



**Página 25 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.4 EVENTO D-1198 – REABERTURA DE PERÍODO DE APURAÇÃO** 

### **2.4.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-11**<br> <br>|**98 – Reabertura d**<br>|**e Período de Apuração – Estrutura Hier**<br>|**árquica**<br>|**do Evento (Resum**<br>|**o)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtReabertMensal|DeRE|Evento de Reabertura Mensal.|1-1|id|O|
|3|ideEvento|evtReabertMensal|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtReabertMensal|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtReabertMensal|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoReabertura|evtReabertMensal|Informações do evento de reabertura<br>mensal.|1-1|nrReciboReab|O|



**Página 26 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.4.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-119**|**8 – Reabertura de**|**Perío**|**do de**|**Apura**|**ção – E**|**specificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtReabertMensal|DeRE|G|-|1-1|-|-<br>Evento de Reabertura Mensal.|
|3<br>id|evtReabertMensal|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtReabertMensal|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1|-<br>Tipo de operação do evento.<br>**Nota:**O evento de Reabertura de Período de Apuração (D-1198)<br>aceita exclusivamente a operação de Inclusão.<br>**Valores válidos:**<br>**1**– Inclusão.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtReabertMensal|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtReabertMensal|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Informar o período de apuração a que se deseja realizar a reabertura,<br>sendo o ano e mês da competência da declaração.<br>**Máscara:**AAAA-MM<br>**Validação:**Deve existir um evento D-1199 ativo para o {perApur}<br>correspondente.<br>**Regra de validação:**<br>PERMITIR_PERAPUR_FECHADO|
|13<br>infoReabertura|evtReabertMensal|G|-|1-1|-|-<br>Informações do evento de reabertura mensal.|
|14nrReciboReab|infoReabertura|E|C|1-1|31|-<br>Número do recibo do evento de Fechamento Mensal (D-1199) relativo<br>ao período de apuração a que se deseja realizar a reabertura.<br>**Validação:**Validar a consistência do recibo de fechamento<br>referenciado no evento de reabertura D-1198:<br>1. O sistema deve verificar se o prefixo (caracteres 1 a 4) do campo<br>{nrReciboReab}<br>informado<br>corresponde<br>a<br>1199<br>(Evento<br>de<br>Fechamento Mensal). Caso seja diferente de 1199, o evento deve ser<br>rejeitado;<br>2. O recibo informado no campo {nrReciboReab} deve existir e ser o<br>último vigente na base de dados da DeRE associado ao CNPJ<br>declarante ({nrInsc}) com o status "Ativo" para aquele {perApur};<br>3. O período de apuração {perApur} informado no grupo {idePeriodo}<br>deve ser idêntico ao período apontado nos caracteres 6 a 11 do recibo<br>informado ({nrReciboReab}).<br>**Regras de validação:**<br>RN - Formação do Número do Recibo do Evento<br>EXISTE_RECIBO_FECH_ATIVO|



**Página 27 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.5 EVENTO D-1199 – FECHAMENTO DE EVENTOS MENSAIS** 

### **2.5.1 Estrutura Hierárquica do Evento (Resumo)** 

||<br>|**D-1199 – Fech**<br>|**amento Mensal – Estrutura Hierárquica**<br>|**do Event**<br>|**o (Resumo)**<br>||
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtFechMensal|DeRE|Evento de Fechamento Mensal.|1-1|id|O|
|3|ideEvento|evtFechMensal|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtFechMensal|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtFechMensal|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoFechamento|evtFechMensal|Informações sobre o fechamento da<br>apuração mensal e aproveitamento de<br>bases de cálculo negativas (BCN).|0-1|-|F|
|4|infoParamFech|infoFechamento|Grupo de informações de indicadores de<br>não ocorrência ou outras parametrizações<br>específicas que qualificam o encerramento<br>do período.|0-1|-|OC|
|4|gUtilizBCN|infoFechamento|Grupo de informação de aproveitamento de<br>bases de cálculo negativa de períodos<br>anteriores.|0-1|-|F|
|5|infoBCN|gUtilizBCN|Detalhamento de informações da base de<br>cálculo negativa por tipo de serviço.|1-99|codBCNRaiz|O|
|5|detBCNeg|gUtilizBCN|Informação individualizada de cada base de<br>cálculo negativa a ser aproveitada pelo<br>contribuinte.|0-99|codBCN|O (se {metodoAproveit} =<br>[1]);<br>N (nos demais casos).|



**Página 28 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.5.2 Especificação Técnica dos Campos (Detalhamento)** 

|**#**<br>**Grupo/Tag**|**D-1199 – Fecha**<br>**Grupo Pai**|**ment**<br>**Cat**|**o Men**<br>**Tipo**|**sal – Es**<br> <br>**Ocorr**|**pecifica**<br>**Tam**<br>**D**|**ção Técnica de Campos (Detalhamento)**<br>**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtFechMensal|DeRE|G|-|1-1|-|-<br>Evento de Fechamento Mensal.<br>**Regras de validação:**<br>EVENTOS_OBRIGATORIOS_PERIODO<br>CONSISTIR_PGCC_EVENTOS_PERIODO<br>RN - Validar Saldos Finais Eventos Auxiliares e Balancete|
|3<br>id|evtFechMensal|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtFechMensal|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1|-<br>Tipo de operação do evento.<br>**Nota:**O envio deste evento consolida a totalização da apuração<br>mensal. Para retificações, exige-se a transmissão prévia do evento de<br>reabertura correspondente.<br>**Valores válidos:**<br>**1**– Inclusão.<br>**Regra de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtFechMensal|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtFechMensal|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Informar o período de apuração, sendo o ano e mês da competência<br>da declaração.<br>**Máscara:**AAAA-MM<br>**Validação:**Só pode existir um único {perApur} para cada {nrInsc}. Não<br>é permitida a inclusão de um novo fechamento para um período que<br>já possua um evento D-1199 ativo, exceto se precedido pelo<br>respectivo evento de reabertura.<br>**Regra de validação:**<br>REJEITAR_PERAPUR_FUTURO|



**Página 29 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-1199 – Fecha**|**ment**|**o Men**|**sal – Es**|**pecificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**Dec**<br>**Descrição**|
|13<br>infoFechamento|evtFechMensal|G|-|0-1|-<br>-<br>Informações<br>sobre<br>o<br>fechamento<br>da<br>apuração<br>mensal<br>e<br>aproveitamento de bases de cálculo negativas (BCN).|
|14<br>infoParamFech|infoFechamento|G|-|0-1|-<br>-<br>Grupo de informações de indicadores de não ocorrência ou outras<br>parametrizações específicas que qualificam o encerramento do<br>período.<br>**Preenchimento:**Deve ser informado exclusivamente quando houver<br>necessidade de declarar dispensa de eventos auxiliares ou outras<br>opções do contribuintepara operíodo de apuração {perApur}.|
|15indInexistDedu|infoParamFech|E|N|0-1|1<br>-<br>Declaração de inexistência de deduções a detalhar no período,<br>dispensando a transmissão do evento auxiliar D-1121 (Relação de<br>Deduções Utilizadas na Apuração) para os contribuintes sujeitos à sua<br>obrigatoriedade.<br>**Preenchimento:**Este campo não deve ser informado (deve ser<br>omitido do XML) nas seguintes situações:<br>1. Caso o contribuinte possua deduções no mês (hipótese em que<br>deverá transmitir o respectivo evento D-1121 com os dados); ou<br>2. Caso o contribuinte, por sua natureza jurídica ou regime, não esteja<br>sujeito a nenhuma regra de obrigatoriedade de entrega do evento D-<br>1121.<br>**Valores válidos:**<br>**1**– Declaro que, embora sujeito à obrigatoriedade de entrega do<br>evento D-1121, não possuo deduções a detalhar neste período.|
|16<br>gUtilizBCN|infoFechamento|G|-|0-1|-<br>-<br>Grupo de informação de aproveitamento de bases de cálculo negativa<br>deperíodos anteriores.|
|17<br>infoBCN|gUtilizBCN|G|-|1-99|-<br>-<br>Detalhamento de informações da base de cálculo negativa por tipo de<br>serviço.|
|18codBCNRaiz|infoBCN|E|C|1-1|5<br>-<br>Informar o código identificador da base de cálculo negativa específica<br>do qual se deseja utilizar a base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|19usarBCNAcum|infoBCN|E|N|1-1|1<br>-<br>Informar a opção por utilizar (se existir) o valor de base de cálculo<br>negativa acumulada em períodos anteriores.<br>**Valores válidos:**<br>**0**– Opção por NÃO efetuar o aproveitamento de bases negativas<br>neste período;<br>**1**– Opção por efetuar o aproveitamento de bases negativas neste<br>período.|
|20metodoAproveit|infoBCN|E|N|0-1|1<br>-<br>Informar qual método o contribuinte deseja para o aproveitamento da<br>base de cálculo negativa.<br>**Preenchimento:**Obrigatório se {usarBCNAcum} = [1].<br>Não informar se {usarBCNAcum} = [0].<br>**Valores válidos:**<br>**0**– Método PEPS calculado automaticamente pelo sistema;<br>**1**– Informação manual pelo contribuinte de valores de bases de<br>cálculo negativas que deseja realizar o aproveitamento, com a<br>informação dos respectivos períodos de origem.<br>**Regra de validação:**<br>VALIDAR_METODO_APROVEIT|



**Página 30 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-1199 – Fecha**|**mento**|**Men**|**sal – Es**|**pecifi**|**cação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|21<br>detBCNeg|gUtilizBCN|G|-|0-99|-|-<br>Informação individualizada de cada base de cálculo negativa a ser<br>aproveitada pelo contribuinte.<br>**Regra de validação:**<br>VERIFICAR_DETBCNEG|
|22codBCN|detBCNeg|E|C|1-1|13|-<br>Informar o código da base de cálculo negativa que será utilizado para<br>dedução nesta competência.|
|23vUsarBCN|detBCNeg|E|N|0-1|4-18|2<br>Valor máximo da base de cálculo negativa que o contribuinte deseja<br>realizar o aproveitamento na competência atual.<br>**Preenchimento:**Não informar caso o contribuinte deseje aproveitar<br>toda a base de cálculo negativa disponível no {codBCN}.<br>**Validação:**Se o valor informado pelo contribuinte for superior ao<br>existente em sua conta corrente para aquele {codBCN}, será atribuído<br>o valor máximo disponível na conta corrente e retornado alerta<br>informando não existir o saldo total disponível naquela base de cálculo<br>negativa.<br>**Regra de validação:**<br>AVISO_CONFERIR_SALDO_BCN|



**Página 31 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.6 EVENTO D-2101 – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA** 

### **2.6.1 Estrutura Hierárquica do Evento (Resumo)** 

|**D**|**-2101 – Débito e**|**m Operações com**|**Títulos de Dívida com Oferta Pública –**|**Estrutura**|**Hierárquica d**|**o Evento (Resumo)**|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtDebOpOfPublic|DeRE|Evento débito mensal de operações com<br>títulos de dívida com oferta pública.|1-1|id|O|
|3|ideEvento|evtDebOpOfPublic|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtDebOpOfPublic|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtDebOpOfPublic|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoDebOpOfPublic|evtDebOpOfPublic|Informações de títulos de dívida com oferta<br>pública.|0-1|-|N (se {tpOper} = [3]);<br>O (nos demais casos).|
|4|infoTitulos|infoDebOpOfPublic|Informações do título de dívida com oferta<br>pública.|0-1|-|N (se {semTitulos} = [1]);<br>O (nos demais casos).|
|5|detTitulo|infoTitulos|Detalhamento do título.|1-10000|idTitulo, cCta|O|



**Página 32 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **2.6.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2101 – Débito em**|**Operações com T**|**ítulo**|**s de Dí**|**vida c**|**om Ofer**|**ta Pública – Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtDebOpOfPublic|DeRE|G|-|1-1|-|-<br>Evento débito mensal de operações com títulos de dívida com oferta<br>pública.<br>**Regra de validação:**<br>CODTRIB_PERMITIDO_D2101|
|3<br>id|evtDebOpOfPublic|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtDebOpOfPublic|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>tpOper|ideEvento|E|N|1-1|1|-<br>Tipo de operação do evento.<br>**Nota:**A alteração substitui integralmente as informações do evento<br>enviado anteriormente.<br>**Valores válidos:**<br>**1**– Inclusão;<br>**2**– Alteração;<br>**3**– Exclusão.<br>**Regra de validação:**<br>INCLUSAO_COM_CHAVE_PREEXISTENTE|
|6<br>motExcl|ideEvento|E|N|0-1|1|-<br>Motivo da exclusão.<br>Código do motivo que justifica a exclusão do evento.<br>**Preenchimento:**Obrigatório se {tpOper} = [3].<br>**Valores válidos:**<br>**1**– Determinação judicial ou administrativa;<br>**2**– Envio indevido (fato inexistente);<br>**3**– Erro na identificação (CNPJ/período incorretos);<br>**9**– Outro.<br>**Regra de validação:**<br>EXIGIR_MOTIVO_EXCLUSAO|
|7<br>nrProc|ideEvento|E|C|0-1|1-21|-<br>Informar o número do processo administrativo/judicial.<br>**Preenchimento:**Obrigatório se {motExcl} = [1].<br>**Validação:**Deve ser um número de processo válido e existente no<br>evento D-1021.<br>**Regra de validação:**<br>EXIGIR_NUMERO_PROCESSO|
|8<br>nrRecibo|ideEvento|E|C|0-1|31|-<br>Caso seja um evento de alteração/retificação ou exclusão, preencher<br>com o número do recibo do arquivo a ser alterado/retificado ou<br>excluído.<br>**Regras de validação:**<br>RN - Formação do Número do Recibo do Evento<br>EXIGIR_NR_RECIBO<br>ULTIMO_RECIBO_ATIVO|
|9<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|10aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|11verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|



**Página 33 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2101 – Débito em Operações com Títulos de Dívida com Oferta Pública – Especificação Técnica de Campos (Detalhamento)** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|12<br>ideContrib|evtDebOpOfPublic|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|13nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|14<br>idePeriodo|evtDebOpOfPublic|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|15perApur|idePeriodo|E|D|1-1|7|-<br>Informar o período de apuração, sendo o ano e mês da competência<br>da declaração.<br>**Máscara:**AAAA-MM<br>**Validação:**Se {tpOper} = [1], só pode existir um único {perApur} para<br>cada {nrInsc}.<br>Quando informado {nrRecibo}, o {perApur} deve ser exatamente o<br>mesmo existente no {nrRecibo} (caracteres 6 a 11 do recibo).<br>**Regras de validação:**<br>OBRIGAR_PERAPUR_IGUAL_NRRECIBO<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|16<br>infoDebOpOfPublic|<br>evtDebOpOfPublic|G|-|0-1|-|-<br>Informações de títulos de dívida com oferta pública.<br>**Regra de validação:**<br>OBRIGAR_PREENCH_INFO_DEB_OF_PUB|
|17semTitulos|infoDebOpOfPublic|E|N|0-1|1|-<br>Indicação da inexistência de títulos de dívida com oferta pública<br>movimentados no período.<br>**Preenchimento:**Deve ser preenchido exclusivamente quando o<br>contribuinte não possuir movimento com títulos de dívida com oferta<br>pública a detalhar no período de apuração ({perApur}).<br>**Valores válidos:**<br>**1**– Sem movimento com títulos de dívida com oferta pública neste<br>período.|
|18<br>infoTitulos|infoDebOpOfPublic|G|-|0-1|-|-<br>Informações do título de dívida com oferta pública.|
|19<br>detTitulo|infoTitulos|G|-|1-10000|-|-<br>Detalhamento do título.|
|20idTitulo|detTitulo|A|C|1-1|12|-<br>Código de identificação global do título no padrão ISO 6166 (ISIN).<br>**Preenchimento:**Deve ser informado sem espaços ou caracteres<br>especiais, contendo exatamente 12 caracteres alfanuméricos.|
|21cCta|detTitulo|A|C|1-1|1-53|-<br>Código da conta analítica (como informada no evento D-1011.{cCta}).<br>**Preenchimento:**Não informar contas sintéticas.<br>**Nota:**Devem estar no PGCC vigente no último dia do {perApur} todas<br>as contas que tiveram movimentação no período, ainda que tenham<br>sido criadas ou encerradas no período.<br>**Validação:**O {cCta} deve existir e estar vigente na tabela PGCC do<br>contribuinte no último dia do {perApur} correspondente.<br>**Regras de validação:**<br>CCTA_NO_PGCC<br>CODTRIB_COMPATIVEL_EVT|
|22descFundo|detTitulo|E|C|0-1|1-255|-<br>Descrição do nome do fundo.<br>**Validação:**Obrigatório se aquisição se der por meio de fundos de<br>investimento com composição mínima de 95% de títulos de dívida.<br>**Regra de validação:**<br>OBRIGAR_PREENCH_DESC_FUNDO|
|23CNPJDevedor|detTitulo|E|C|1-1|14|-<br>Informar o número de inscrição no CNPJ do devedor emitente do<br>título de dívida.<br>**Validação:**Deve ser um CNPJ válido com 14 posições.<br>**Regra de validação:**<br>DV_CNPJ|



**Página 34 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2101 – Débito em Operações com Títulos de Dívida com Oferta Pública – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|24|vSaldoContInic|detTitulo|E|N|1-1|6-20|4<br>Representa o valor do título sobre o qual haverá a incidência dos juros<br>no período.<br>**Preenchimento:**Deve ser igual ao {vSaldoContFin} do período<br>anterior para o mesmo {idTitulo} (exceto na primeira competência do<br>declarante).|
|25|vEntradas|detTitulo|E|N|1-1|6-20|4<br>Valor total das entradas (compras de novos títulos) da mesma emissão<br>e série de ativos no período de apuração.|
|26|vSaidas|detTitulo|E|N|1-1|6-20|4<br>Valor total das saídas (vendas de títulos) da mesma emissão e série de<br>ativos, no período de apuração.|
|27|vJurosRec|detTitulo|E|N|1-1|6-20|4<br>Valor dos juros recebidos relativos à mesma emissão e série de ativos,<br>no período de apuração.|
|28|vJurosApropr|detTitulo|E|N|1-1|6-20|4<br>Valor total da receita de juros e demais rendimentos financeiros<br>auferidos sobre o título no período.<br>**Regra de validação:**<br>CONFERIR_JUROS|
|29|vSaldoContFin|detTitulo|E|N|1-1|6-20|4<br>Valor final do agrumento de títulos.<br>**Cálculo:**<br>{vSaldoContFin} = {vSaldoContIni} + {vEntradas} - {vSaidas} -<br>{vJurosRec} + {vJurosApropr}|
|30|vSelic|detTitulo|E|N|1-1|6-20|4<br>Valor equivalente à variação da taxa Selic no período.<br>Representa o teto tributável para a operação.<br>**Regra de validação:**<br>CONFERIR_SELIC|
|31|vPisCofins|detTitulo|E|N|0-1|6-20|4<br>Valor da dedução relativa ao PIS/COFINS incidente sobre a receita<br>tributada.<br>**Nota:**Informar apenas a parcela passível de dedução da base de<br>cálculo do IBS/CBS.|
|32|vApur|detTitulo|E|N|1-1|6-20|4<br>Valor base sobre o qual serão aplicadas as regras definidas pelo<br>código de tributação ({codTrib}).<br>**Cálculo:**<br>{vApur} = (MENORENTRE({vSelic} e {vJurosAprop})) - {vPisCofins}<br>**Regra de validação:**<br>VALIDAR_VAPUR_D2101|



**Página 35 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3 EVENTOS PERIÓDICOS TRANSACIONAIS (LEIAUTES PRELIMINARES)** 

### **3.1 EVENTO D-2201 – SERVIÇOS REMUNERADOS POR PREÇO - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR)** 

### **3.1.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2201 – Serviços Remunerados por Preço - Identificação de Adquirentes (Leiaute Preliminar) – Estrutura Hierárquica do** 

**Evento (Resumo)** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtServRemPreco|DeRE|Evento de identificação e detalhamento de<br>serviços remunerados por preço.|1-1|id|O|
|3|ideEvento|evtServRemPreco|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtServRemPreco|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtServRemPreco|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtServRemPreco|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000|chDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|infoDest|detOper|Informações do destinatário da operação<br>individualizada.|0-1|CPFDest, CNPJDest|OC (se {codBC} = [3605; 3610;<br>3615; 3620; 3625; 3630; 3635;<br>3640]);<br>N (nos demais casos).|
|8|destExterior|infoDest|Identificação do destinatário residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPFDest} e {CNPJDest}<br>não informados);<br>N (nos demais casos).|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|1-1|-|O|



**Página 36 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.1.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2201 – Serviços**|**Remunerados po**|**r Preç**|**o - Id**|**entific**<br>**Camp**|**ação de**<br>**os(Det**|**Adquirentes (Leiaute Preliminar) – Especificação Técnica de**<br>**alhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtServRemPreco|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de serviços remunerados por<br>preço.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[1015; 1020; 1205; 1210; 1225; 1230; 1805; 1810; 2005; 2010; 2015;<br>2020; 2210; 2805; 2810; 3605; 3610; 3615; 3620; 3625; 3630; 3635;<br>3640; 3805; 3810; 4005; 4010].|
|3<br>id|evtServRemPreco|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtServRemPreco|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtServRemPreco|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtServRemPreco|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|



**Página 37 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2201 – Serviços Remunerados por Preço - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|14<br>infoOper|evtServRemPreco|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[1015; 1020; 1205; 1210; 1225; 1230; 1805; 1810; 2005; 2010; 2015;<br>2020; 2210; 2805; 2810; 3605; 3610; 3615; 3620; 3625; 3630; 3635;<br>3640; 3805; 3810; 4005; 4010].|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|



**Página 38 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2201 – Serviços Remunerados por Preço - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de**<br>**Campos(Detalhamento)**|
|---|



|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.<br>**Nota:**As operações são informadas individualmente. Na existência de<br>destinatários em operações de intermediação, cada {detOper} pode<br>conter um único destinatário.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|



**Página 39 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2201 – Serviços Remunerados por Preço - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|34<br>infoDest|detOper|G|-|0-1|-|-<br>Informações do destinatário da operação individualizada.<br>**Preenchimento:**Exclusivo quando {codBC} = [3605; 3610; 3615; 3620;<br>3625; 3630; 3635; 3640] (intermediação de negócios).<br>**Validação:**<br>Caso exista um ou mais {infoDest} dentro do grupo {dadosOper}, é<br>obrigatório que todas as ocorrências de {detOper} tenham {infoDest}.<br>Se o contribuinte tiver operações com e sem destinatário, deverá<br>enviar em eventos separados.|
|35CPFDest|infoDest|CE|C|0-1|11|-<br>Número de inscrição no CPF do destinatário do serviço, se pessoa<br>física.<br>**Preenchimento:**Não informar se o campo {CNPJDest} ou grupo<br>{destExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|36CNPJDest|infoDest|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do destinatário do serviço, se pessoa<br>jurídica.<br>**Preenchimento:**Não informar se o campo {CPFDest} ou o grupo<br>{destExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|37cMunDest|infoDest|E|N|0-1|7|-<br>Código IBGE do município do endereço do destinatário, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPFDest} ou {CNPJDest} forem<br>informados. Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|38<br>destExterior|infoDest|G|-|0-1|-|-<br>Identificação do destinatário residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o destinatário da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|39NIFDest|destExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do destinatário estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIFDest} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|40cNaoNIFDest|destExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do destinatário<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIFDest} não for<br>informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|41nrDocIdentDest|destExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIFDest} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|42cPaisDest|destExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do destinatário residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|



**Página 40 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2201 – Serviços Remunerados por Preço - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|43|gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|44|vBCApur|gBC|E|N|1-1|4-18|2<br>Valor da base de cálculo líquida correspondente à operação, obtido<br>após a exclusão dos tributos que não integram a base de cálculo (ISS<br>e PIS/COFINS), deduzidas as parcelas e custos permitidos pela<br>legislação e aplicado o gross-down para exclusão dos tributos<br>embutidos, se aplicável.|
|45|gTributos|detOper|G|-|1-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.|
|46|vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|47|pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|48|vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|49|pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|50|vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|51|pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|
|52|vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 41 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.2 EVENTO D-2202 – TARIFAS DO REGIME GERAL - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR)** 

### **3.2.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2202 – Tarifas do Regime Geral - Identificação de Adquirentes (Leiaute Preliminar) – Estrutura Hierárquica do Evento** 

||||**(Resumo)**||||
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtServRemTarifa|DeRE|Evento de identificação e detalhamento de<br>tarifas.|1-1|id|O|
|3|ideEvento|evtServRemTarifa|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtServRemTarifa|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtServRemTarifa|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtServRemTarifa|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000 c|hDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|qualifOper|detOper|Grupo destinado aos atributos e<br>qualificadores específicos da transação.|0-1|-|OC|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|1-1|-|O|



**Página 42 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.2.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2202 – Tarifas**|**do Regime Geral -**|**Ident**|**ificaç**|**ão de A**<br>**(**|**dquire**<br>**Detalha**|**ntes (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**mento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtServRemTarifa|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de tarifas.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[4210].|
|3<br>id|evtServRemTarifa|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtServRemTarifa|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtServRemTarifa|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtServRemTarifa|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|



**Página 43 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2202 – Tarifas**|**do Regime Geral -**|**Ident**|**ificaç**|**ão de A**<br>**(**|**dquiren**<br>**Detalha**|**tes (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**mento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|14<br>infoOper|evtServRemTarifa|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[4210].|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|



**Página 44 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2202 – Tarifas do Regime Geral - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de Campos** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|34<br>qualifOper|detOper|G|-|0-1|-|-<br>Grupo destinado aos atributos e qualificadores específicos da<br>transação.|
|35indPresenc|qualifOper|E|N|0-1|1|-<br>Indicador que define se o serviço prestado foi fruído presencialmente<br>por pessoa física.<br>**Preenchimento:**Obrigatório se {CPF} for informado.<br>**Valores válidos:**<br>**0**– Não (serviço prestado de forma remota, eletrônica ou online);<br>**1**– Sim (serviço fruído presencialmente).|
|36cMunOper|qualifOper|E|N|0-1|7|-<br>Código do município correspondente ao local do estabelecimento<br>onde o serviço foi prestado, conforme tabela do IBGE.<br>**Preenchimento:**Obrigatório se {indPresenc} = [1].<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|



**Página 45 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-2202 – Tarifas**<br>|**do Regime Geral -**<br>|**Ident**<br>|**ificaç**<br>|**ão de**<br>**(**<br>|**Adquir**<br>**Detalh**<br>|**ente**<br>**ame**<br>|**s (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**nto)**<br> <br>|
|---|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**|<br>**Descrição**|
|37|gBC|detOper|G|-|1-1|-|-|Grupo de aferição da base de cálculo do IBS e da CBS.|
|38|vBCApur|gBC|E|N|1-1|4-18|2|Valor da base de cálculo líquida correspondente à operação, obtido<br>após a exclusão dos tributos que não integram a base de cálculo (ISS<br>e PIS/COFINS), deduzidas as parcelas e custos permitidos pela<br>legislação e aplicado o gross-down para exclusão dos tributos<br>embutidos, se aplicável.|
|39|gTributos|detOper|G|-|1-1|-|-|Grupo de totalização de tributos incidentes sobre a operação.|
|40|vBCTrib|gTributos|E|N|1-1|4-18|2|Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|41|pIBSMunTrib|gTributos|E|N|1-1|4-8|2-4|Alíquota do IBS municipal incidente sobre a operação.|
|42|vIBSMunTrib|gTributos|E|N|1-1|4-18|2|Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|43|pIBSUFTrib|gTributos|E|N|1-1|4-8|2-4|Alíquota do IBS estadual incidente sobre a operação.|
|44|vIBSUFTrib|gTributos|E|N|1-1|4-18|2|Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|45|pCBSTrib|gTributos|E|N|1-1|4-8|2-4|Alíquota da CBS incidente sobre a operação.|
|46|vCBSTrib|gTributos|E|N|1-1|4-18|2|Valor da CBS.<br>**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 46 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.3 EVENTO D-2211 – OPERAÇÕES DE CRÉDITO E VALORES MOBILIÁRIOS (TVM) - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR)** 

### **3.3.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2211 – Operações de Crédito e Valores Mobiliários (TVM) - Identificação de Adquirentes (Leiaute Preliminar) – Estrutura** 

**Hierár** **<mark>q</mark> uica do Evento** **<mark>(</mark> Resumo** **<mark>)</mark>** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtOperFinanc|DeRE|Evento de identificação e detalhamento de<br>operações de crédito e TVM.|1-1|id|O|
|3|ideEvento|evtOperFinanc|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtOperFinanc|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtOperFinanc|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtOperFinanc|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000 c|hDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|qualifOper|detOper|Grupo destinado aos atributos e<br>qualificadores específicos da transação.|1-1|-|O|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|1-1|-|O|



**Página 47 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.3.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2211 – Op**|**erações de Crédit**|**o e Val**<br>**Espec**|**ores**<br>**ificaç**|**Mobiliá**<br>**ão Téc**|**rios (TV**<br>**nica de**|**M) - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**Campos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtOperFinanc|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de operações de crédito e<br>TVM.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[1005; 1010; 2220].|
|3<br>id|evtOperFinanc|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtOperFinanc|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtOperFinanc|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtOperFinanc|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|



**Página 48 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2211 – Op**|**erações de Crédit**|**o e Val**<br>**Espec**|**ores**<br>**ificaç**|**Mobiliár**<br>**ão Técn**|**ios (TV**<br>**ica de C**|**M) - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**ampos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|14<br>infoOper|evtOperFinanc|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[1005; 1010; 2220].|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|



**Página 49 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2211 – Operações de Crédito e Valores Mobiliários (TVM) - Identificação de Adquirentes (Leiaute Preliminar) – Es** **<mark>p</mark> ecifica** **<mark>ç</mark> ão Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|34<br>qualifOper|detOper|G|-|1-1|-|-<br>Grupo destinado aos atributos e qualificadores específicos da<br>transação.|
|35indMoedaEstr|qualifOper|E|N|1-1|1|-<br>Indicador de contrato referenciado em moeda estrangeira.<br>**Valores válidos:**<br>**0**– Não;<br>**1**– Sim.|
|36<br>gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|37vBCApur|gBC|E|N|1-1|4-18|2<br>Valor da base de cálculo líquida correspondente à operação, obtido<br>após a exclusão dos tributos que não integram a base de cálculo (ISS<br>e PIS/COFINS), deduzidas as parcelas e custos permitidos pela<br>legislação e aplicado o gross-down para exclusão dos tributos<br>embutidos, se aplicável.|
|38<br>gTributos|detOper|G|-|1-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.|
|39vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|40pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|41vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|42pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|43vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|44pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|



**Página 50 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2211 – Operações de Crédito e Valores Mobiliários (TVM) - Identificação de Adquirentes (Leiaute Preliminar) – Es** **<mark>p</mark> ecifica** **<mark>ç</mark> ão Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**||**Descrição**|
|---|---|---|---|---|---|---|---|---|
|45vCBSTrib|gTributos|E|N|1-1|4-18|2|Valor da CBS.||
||||||||**Cálculo:**||
||||||||{vCBSTrib} = {vBCTrib} *|({pCBSTrib} / 100)|



**Página 51 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.4 EVENTO D-2221 – ANTECIPAÇÃO DE RECEBÍVEIS (SECURITIZAÇÃO, FATURIZAÇÃO E ARRANJOS) - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR)** 

### **3.4.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2221 – Antecipação de Recebíveis (Securitização, Faturização e Arranjos) - Identificação de Adquirentes (Leiaute Preliminar** **<mark>)</mark> – Estrutura Hierár** **<mark>q</mark> uica do Evento** **<mark>(</mark> Resumo** **<mark>)</mark>** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtAntecReceb|DeRE|Evento de identificação e detalhamento de<br>operações de antecipação de recebíveis.|1-1|id|O|
|3|ideEvento|evtAntecReceb|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtAntecReceb|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtAntecReceb|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtAntecReceb|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000 c|hDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|qualifOper|detOper|Grupo destinado aos atributos e<br>qualificadores específicos da transação.|0-1|-|O (se {codBC} = [1215; 1220;<br>1235; 1240]);<br>N (nos demais casos).|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|1-1|-|O|



**Página 52 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.4.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2221 – Ant**|**ecipação de Receb**<br>**Prelim**|**íveis (**<br>**inar)  **|**Secur**<br>**– Esp**|**itização**<br>**ecificaç**|**, Faturi**<br>**ão Técni**|**zação e Arranjos) - Identificação de Adquirentes (Leiaute**<br>**ca de Campos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtAntecReceb|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de operações de antecipação<br>de recebíveis.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[1215; 1220; 1235; 1240; 2505; 2510; 2515; 2520; 2525; 2530].|
|3<br>id|evtAntecReceb|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtAntecReceb|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtAntecReceb|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtAntecReceb|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|
|14<br>infoOper|evtAntecReceb|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[1215; 1220; 1235; 1240; 2505; 2510; 2515; 2520; 2525; 2530].|



**Página 53 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2221 – Ant**|**ecipação de Receb**<br>**Preli**|**íveis (**<br>**minar)  **|**Securi**<br>**– Espe**|**tização**<br>**cificaç**|**, Faturiz**<br>**ão Técni**|**ação e Arranjos) - Identificação de Adquirentes (Leiaute**<br>**ca de Campos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|



**Página 54 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2221 – Ant**|**ecipação de Receb**<br>**Preli**|**íveis (**<br>**minar)  **|**Secur**<br>**– Esp**|**itização**<br>**ecificaç**|**, Fatur**<br>**ão Téc**|**ização e Arranjos) - Identificação de Adquirentes (Leiaute**<br>**nica de Campos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|34<br>qualifOper|detOper|G|-|0-1|-|-<br>Grupo destinado aos atributos e qualificadores específicos da<br>transação.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [1215; 1220;<br>1235; 1240].|
|35indMoedaEstr|qualifOper|E|N|1-1|1|-<br>Indicador de contrato referenciado em moeda estrangeira.<br>**Valores válidos:**<br>**0**– Não;<br>**1**– Sim.|
|36<br>gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|37vBCApur|gBC|E|N|1-1|4-18|2<br>Valor da base de cálculo líquida correspondente à operação, obtido<br>após a exclusão dos tributos que não integram a base de cálculo (ISS<br>e PIS/COFINS), deduzidas as parcelas e custos permitidos pela<br>legislação e aplicado o gross-down para exclusão dos tributos<br>embutidos, se aplicável.|
|38<br>gTributos|detOper|G|-|1-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.|
|39vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|40pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|41vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|42pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|43vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|44pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|
|45vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>|
|||||||**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 55 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.5 EVENTO D-2231 – ARRENDAMENTO MERCANTIL - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR)** 

### **3.5.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2231 – Arrendamento Mercantil - Identificação de Adquirentes (Leiaute Preliminar) – Estrutura Hierárquica do Evento** 

||||**(Resumo)**||||
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtArrendMerc|DeRE|Evento de identificação e detalhamento das<br>operações de arrendamento mercantil.|1-1|id|O|
|3|ideEvento|evtArrendMerc|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtArrendMerc|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtArrendMerc|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtArrendMerc|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000|chDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|0-1|-|O (se {codBC} = [1410; 1420;<br>1430; 1440; 1450; 1460; 1470;<br>1480; 1605; 1610; 1630;<br>1640]);<br>N (nos demais casos).|



**Página 56 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.5.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2231 – Arrend**|**amento Mercantil**|**- Iden**|**tifica**|**ção de**<br>**(**|**Adquire**<br>**Detalha**|**ntes (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**mento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtArrendMerc|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento das operações de<br>arrendamento mercantil.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[1410; 1420; 1430; 1440; 1450; 1460; 1470; 1480; 1605; 1610; 1615;<br>1620; 1630; 1640].|
|3<br>id|evtArrendMerc|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtArrendMerc|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtArrendMerc|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtArrendMerc|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|



**Página 57 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2231 – Arrend**|**amento Mercantil**|**- Iden**|**tifica**|**ção de**<br>**(**|**Adquire**<br>**Detalha**|**ntes (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**mento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|14<br>infoOper|evtArrendMerc|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[1410; 1420; 1430; 1440; 1450; 1460; 1470; 1480; 1605; 1610; 1615;<br>1620; 1630; 1640].|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|



**Página 58 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2231 – Arrendamento Mercantil - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de Campos** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|34<br>gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|35vOper|gBC|CE|N|0-1|4-18|2<br>Valor da parcela recebida pelo arrendamento mercantil financeiro,<br>incluindo o valor de tarifa vinculada ao contrato.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [1615; 1620].|
|36vBCApur|gBC|CE|N|0-1|4-18|2<br>Valor da base de cálculo líquida correspondente à operação, obtido<br>após a exclusão dos tributos que não integram a base de cálculo (ISS<br>e PIS/COFINS), deduzidas as parcelas e custos permitidos pela<br>legislação e aplicado o gross-down para exclusão dos tributos<br>embutidos, se aplicável.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [1410; 1420;<br>1430; 1440; 1450; 1460; 1470; 1480; 1605; 1610; 1630; 1640].|
|37<br>gTributos|detOper|G|-|0-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [1410; 1420;<br>1430; 1440; 1450; 1460; 1470; 1480; 1605; 1610; 1630; 1640].|
|38vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|39pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|40vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|41pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|42vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|43pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|



**Página 59 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2231 – Arrendamento Mercantil - Identificação de Adquirentes (Leiaute Preliminar) – Especificação Técnica de Campos** 

**<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**|**Descrição**|
|---|---|---|---|---|---|---|---|
|44vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.||
|||||||**Cálculo:**||
|||||||{vCBSTrib} = {vBCTrib} *|({pCBSTrib} / 100)|



**Página 60 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.6 EVENTO D-2241 – ARRANJOS DE PAGAMENTO - IDENTIFICAÇÃO DE CREDENCIADOS OU DESTINATÁRIOS DOS SERVIÇOS (LEIAUTE PRELIMINAR)** 

### **3.6.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2241 – Arranjos de Pagamento - Identificação de Credenciados ou Destinatários dos Serviços (Leiaute Preliminar) – Estrutura Hierár** **<mark>q</mark> uica do Evento** **<mark>(</mark> Resumo** **<mark>)</mark>** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtArranjoCredD|est DeRE|Evento de identificação e detalhamento de<br>operações com identificação de<br>credenciados ou destinatários dos serviços<br>de arranjos de pagamento<br>(estabelecimentos comerciais etc.|1-1|id|O|
|3|ideEvento|evtArranjoCredDest|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtArranjoCredDest|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtArranjoCredDest|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtArranjoCredDest|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000 c|hDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|8|gVOper|gBC|Detalhamento do valor da operação.|1-1|-|O|
|8|gDeducoes|gBC|Grupo de informação das deduções<br>aplicadas à operação.|0-1|-|OC|
|8|detBC|gBC|Grupo de detalhamento da base de cálculo<br>do IBS/CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|1-1|-|O|



**Página 61 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.6.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2241 – Arranj**|**os de Pagamento -**<br>**E**|**Iden**<br>**spec**|**tifica**<br>**ificaç**|**ção de**<br>**ão Técn**|**Creden**<br>**ica de**|**ciados ou Destinatários dos Serviços (Leiaute Preliminar) –**<br>**Campos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtArranjoCredDest|<br>DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de operações com<br>identificação de credenciados ou destinatários dos serviços de<br>arranjos de pagamento (estabelecimentos comerciais etc.).<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[2405; 2410; 2415; 2420; 2425; 2430].|
|3<br>id|evtArranjoCredDest|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtArranjoCredDest|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtArranjoCredDest|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtArranjoCredDest|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|



**Página 62 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2241 – Arran**|**jos de Pagamento**<br>|**- Iden**<br>**Espec**|**tifica**<br>**ificaç**|**ção de**<br>**ão Téc**|**Credenc**<br>**nica de C**|**iados ou Destinatários dos Serviços (Leiaute Preliminar) –**<br>**ampos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|14<br>infoOper|evtArranjoCredDest|<br>G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[2405; 2410; 2415; 2420; 2425; 2430].|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|



**Página 63 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2241 – Arran**|**jos de Pagament**|**o - Iden**<br>**Espec**|**tifica**<br>**ificaç**|**ção de**<br>**ão Téc**|**Creden**<br>**nica de**|**ciados ou Destinatários dos Serviços (Leiaute Preliminar) –**<br>**Campos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|34<br>gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|35<br>gVOper|gBC|G|-|1-1|-|-<br>Detalhamento do valor da operação.|
|36vOper|gVOper|E|N|1-1|4-18|2<br>Valor bruto da operação.|
|37vDescConced|gVOper|E|N|0-1|4-18|2<br>Indicar o valor do desconto concedido.|
|38tpDesconto|gVOper|E|N|0-1|1|-<br>Identificação do tipo de desconto concedido.<br>**Preenchimento:**Obrigatório se {vDescConced} for informado.<br>**Valores válidos:**<br>**1**– Desconto condicional;<br>**2**– Desconto incondicional.|
|39vLiqOper|dhOper|E|N|1-1|4-18|2<br>Valor líquido da operação.<br>**Cálculo:**<br>{vLiqOper} = {vOper} - {vDescConced}|
|40<br>gDeducoes|gBC|G|-|0-1|-|-<br>Grupo de informação das deduções aplicadas à operação.|
|41vISSQN|gDeducoes|E|N|0-1|4-18|2<br>Informar o valor do ISSQN devido sobre a operação.|
|42vPisCofins|gDeducoes|E|N|0-1|4-18|2<br>Valor do PIS Cofins tributado na operação.|
|43vSomaDeducoes|gDeducoes|E|N|0-1|4-18|2<br>Valor total das deduções.<br>**Cálculo:**<br>{vSomaDeducoes} = {vISSQN} + {vPisCofins}|



**Página 64 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2241 – Arranjos de Pagamento - Identificação de Credenciados ou Destinatários dos Serviços (Leiaute Preliminar) –**|
|---|
|**Especificação Técnica de Campos(Detalhamento)**|



|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|44<br>|detBC|gBC|G|-|1-1|-|-<br>Grupo de detalhamento da base de cálculo do IBS/CBS.|
|45|vBCApur|detBC|E|N|1-1|4-18|2<br>Base de Cálculo do IBS/CBS.<br>**Cálculo:**<br>1. SE {tpDesconto} = [1],<br>ENTÃO {vBCApur} = {vOper} - {vSomaDeducoes};<br>2. SE {tpDesconto} = [2],<br>ENTÃO {vBCApur} = {vLiqOper} - {vSomaDeducoes}.|
|46<br>|gTributos|detOper|G|-|1-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.|
|47|vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|48|pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|49|vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|50|pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|51|vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|52|pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|
|53|vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 65 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.7 EVENTO D-2242 – ARRANJOS DE PAGAMENTO - OPERAÇÕES ENTRE PARTICIPANTES (LEIAUTE PRELIMINAR)** 

### **3.7.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2242 – Arranjos de Pagamento - Operações entre Participantes (Leiaute Preliminar) – Estrutura Hierárquica do Evento** 

||||**(Resumo)**||||
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtArranjoPartic|DeRE|Evento de identificação e detalhamento de<br>operações entre participantes de arranjos de<br>pagamento.|1-1|id|O|
|3|ideEvento|evtArranjoPartic|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtArranjoPartic|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtArranjoPartic|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtArranjoPartic|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000 c|hDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|8|gVOper|gBC|Detalhamento do valor da operação.|1-1|-|O|
|8|gDeducoes|gBC|Grupo de informação das deduções<br>aplicadas à operação.|0-1|-|OC|
|8|detBC|gBC|Grupo de detalhamento da base de cálculo<br>do IBS/CBS.|1-1|-|O|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|1-1|-|O|



**Página 66 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.7.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2242 – Arranjo**|**s de Pagamento -**|**Opera**|**ções**|**entre P**<br>**(**|**articip**<br>**Detalh**|**antes (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**amento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtArranjoPartic|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de operações entre<br>participantes de arranjos de pagamento.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[2435; 2440; 2445; 2450; 2455; 2460].|
|3<br>id|evtArranjoPartic|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtArranjoPartic|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtArranjoPartic|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11tpParticip|ideContrib|E|C|1-1|1|-<br>Tipo de participação do contribuinte no arranjo de pagamento.<br>**Valores válidos:**<br>**1**– Instituidor/Bandeira;<br>**2**– Emissor;<br>**3**– Credenciador.|



**Página 67 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2242 – Arranjos de Pagamento - Operações entre Participantes (Leiaute Preliminar) – Especificação Técnica de Campos** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|12<br>idePeriodo|evtArranjoPartic|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|13perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|14perArranjo|idePeriodo|E|C|1-1|1|-<br>Indicação do período de agrupamento das operações.<br>**Valores válidos:**<br>**1**– Dias 01 a 05;<br>**2**– Dias 06 a 10;<br>**3**– Dias 11 a 15;<br>**4**– Dias 16 a 20;<br>**5**– Dias 21 a 25;<br>**6**– Dias 26 a 31.|
|15dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|
|16<br>infoOper|evtArranjoPartic|G|-|1-1|-|-<br>Informações das operações.|
|17<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|18chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[2435; 2440; 2445; 2450; 2455; 2460].|
|20chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|21<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|22<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|23CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|24CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|25cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|



**Página 68 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-2242 – Arranjos de Pagamento - Operações entre Participantes (Leiaute Preliminar) – Especificação Técnica de Campos** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|26<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|27NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|28cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|29nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|30cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|31<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|32<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|33seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|34seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|35dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|



**Página 69 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-2242 – Arranjo**|**s de Pagamento -**|**Opera**|**ções**|**entre**<br>|**Particip**<br>**(Detalh**|**antes (Leiaute Preliminar) – Especificação Técnica de Campos**<br>**amento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|36|gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|37|gVOper|gBC|G|-|1-1|-|-<br>Detalhamento do valor da operação.|
|38|vOper|gVOper|E|N|1-1|4-18|2<br>Valor bruto da operação.|
|39|vDescConced|gVOper|E|N|0-1|4-18|2<br>Indicar o valor do desconto concedido.|
|40|tpDesconto|gVOper|E|N|0-1|1|-<br>Identificação do tipo de desconto concedido.<br>**Preenchimento:**Obrigatório se {vDescConced} for informado.<br>**Valores válidos:**<br>**1**– Desconto condicional;<br>**2**– Desconto incondicional.|
|41|vLiqOper|gVOper|E|N|1-1|4-18|2<br>Valor líquido da operação.<br>**Cálculo:**<br>{vLiqOper} = {vOper} - {vDescConced}|
|42|gDeducoes|gBC|G|-|0-1|-|-<br>Grupo de informação das deduções aplicadas à operação.|
|43|vISSQNProp|gDeducoes|E|N|0-1|4-18|2<br>Informar o valor do ISSQN devido sobre a operação própria.|
|44|vISSQNOutro|gDeducoes|E|N|0-1|4-18|2<br>Informar o valor do ISSQN devido sobre operações de outros<br>participantes de arranjo.|
|45|vPisCofins|gDeducoes|E|N|0-1|4-18|2<br>Valor do PIS Cofins tributado na operação.|
|46|vSomaDeducoes|gDeducoes|E|N|0-1|4-18|2<br>Valor total das deduções.<br>**Cálculo:**<br>{vSomaDeducoes} = {vISSQNProp} + {vISSQNOutro} + {vPisCofins}|
|47|detBC|gBC|G|-|1-1|-|-<br>Grupo de detalhamento da base de cálculo do IBS/CBS.|
|48|vBCApur|detBC|E|N|1-1|4-18|2<br>Base de Cálculo do IBS/CBS.<br>**Cálculo:**<br>1. SE {tpDesconto} = [1],<br>ENTÃO {vBCApur} = {vOper} - {vSomaDeducoes};<br>2. SE {tpDesconto} = [2],<br>ENTÃO {vBCApur} = {vLiqOper} - {vSomaDeducoes}.|
|49|gTributos|detOper|G|-|1-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.|
|50|vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|51|pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|52|vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|53|pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|54|vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|55|pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|
|56|vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 70 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.8 EVENTO D-2251 – SEGUROS, PREVIDÊNCIA COMPLEMENTAR E CAPITALIZAÇÃO - IDENTIFICAÇÃO DE ADQUIRENTES (LEIAUTE PRELIMINAR)** 

### **3.8.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-2251 – Seguros, Previdência Complementar e Capitalização - Identificação de Adquirentes (Leiaute Preliminar) – Estrutura Hierár** **<mark>q</mark> uica do Evento** **<mark>(</mark> Resumo** **<mark>)</mark>** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtSegPrevCap|DeRE|Evento de identificação e detalhamento de<br>operações de seguros, previdência e<br>capitalização.|1-1|id|O|
|3|ideEvento|evtSegPrevCap|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtSegPrevCap|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtSegPrevCap|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtSegPrevCap|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000|chDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do<br>adquirente.|0-1|CPF, CNPJ|F (se {codBC} = [3005; 3010]<br>E {indIdentifAdq} = [0]);<br>O (nos demais casos).|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|6|infoPDV|idePartes|Informações do ponto de venda.|0-1|CPFPDV, CNPJPDV|O (se grupo {infoAdq} não<br>informado);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da<br>operação individualizada.|1-999|seq, seqRef|O|
|7|gSemCobSobr|detOper|Grupo de informações de seguros de<br>ramos elementares e seguros de pessoas<br>sem cobertura por sobrevivência.|0-1|-|O (se {codBC} = [3005; 3010;<br>3015; 3020]);<br>N (nos demais casos).|
|7|gCobSobrPrevComp|detOper|Grupo de informações de seguros de<br>pessoas com cobertura por sobrevivência e<br>previdência complementar.|0-1|-|O (se {codBC} = [3025;<br>3030]);<br>N (nos demais casos).|
|7|gCapitalizacao|detOper|Identificação de operações de<br>capitalização.|0-1|-|O (se {codBC} = [3405;<br>3410]);<br>N (nos demais casos).|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|0-1|-|O (se {codBC} = [3005;<br>3010]);<br>N (nos demais casos).|



**Página 71 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.8.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-2251 – Segu**|**ros, Previdência C**|**omple**<br>**Especif**|**menta**<br>**icaçã**|**r e Cap**<br>**o Técni**|**italizaç**<br>**ca de C**|**ão - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**ampos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo **|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtSegPrevCap|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de operações de seguros,<br>previdência e capitalização.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[3005; 3010; 3015; 3020; 3025; 3030; 3405; 3410].|
|3<br>id|evtSegPrevCap|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtSegPrevCap|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**21**– Pagamento de dedução posterior ao recebimento da receita;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtSegPrevCap|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtSegPrevCap|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|



**Página 72 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2251 – Segu**|**ros, Previdência**|**Comple**<br>**Especif**|**ment**<br>**icaçã**|**ar e Cap**<br>**o Técni**|**italizaç**<br>**ca de C**|**ão - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**ampos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo **|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|14<br>infoOper|evtSegPrevCap|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[3005; 3010; 3015; 3020; 3025; 3030; 3405; 3410].|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20indIdentifAdq|idePartes|E|N|0-1|1|-<br>Indicador se o subscritor é obrigado a se identificar por ocasião da<br>aquisição, nos termos de norma do órgão regulador competente.<br>**Nota:**Campo exclusivo para operações de capitalização.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3405; 3410].<br>**Valores válidos:**<br>**0**– Subscritor não obrigado a se identificar;<br>**1**– Subscritor obrigado a se identificar.|
|21<br>infoAdq|idePartes|CG|-|0-1|-|-<br>Informações de identificação do adquirente.<br>**Preenchimento:**<br>Opcional<br>se<br>{codBC}<br>=<br>[3005;<br>3010]<br>E<br>{indIdentifAdq} = [0].<br>Obrigatório nos demais casos.|
|22CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|23CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|24cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|



**Página 73 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2251 – Segu**|**ros, Previdência**|**Complem**<br>**Especif**|**ent**<br>**icaçã**|**ar e Cap**<br>**o Técni**|**italizaç**<br>**ca de Ca**|**ão - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**mpos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo **|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|25<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|26NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|27cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for<br>informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|28nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|29cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF)<br>ou de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|
|30<br>infoPDV|idePartes|CG|-|0-1|-|-<br>Informações do ponto de venda.<br>**Preenchimento:**Exclusivo e obrigatório quando o grupo {infoAdq}<br>não for informado.|
|31CPFPDV|infoPDV|CE|C|0-1|11|-<br>Número de inscrição no CPF do ponto de venda.<br>**Preenchimento:**Não preencher se {CNPJPDV} for informado.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|32CNPJPDV|infoPDV|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do ponto de venda.<br>**Preenchimento:**Não preencher se {CPFPDV} for informado.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|



**Página 74 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-2251 – Segu**|**ros, Previdência**|**Comple**<br>**Especif**|**menta**<br>**icaçã**|**r e Cap**<br>**o Técni**|**italiza**<br>**ca de C**|**ção - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**ampos(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo **|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|33<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|34<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|35seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|36seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|37dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|38<br>gSemCobSobr|detOper|CG|-|0-1|-|-<br>Grupo de informações de seguros de ramos elementares e seguros<br>de pessoas sem cobertura por sobrevivência.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3005; 3010;<br>3015; 3020].|
|39vPremioReceb|gSemCobSobr|CE|N|0-1|4-18|2<br>Valor total do prêmio recebido.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3015; 3020].|
|40vBCApur|gSemCobSobr|CE|N|0-1|4-18|2<br>Valor da base de cálculo líquida correspondente à operação, obtido<br>após a exclusão dos tributos que não integram a base de cálculo (ISS<br>e PIS/COFINS), deduzidas as parcelas e custos permitidos pela<br>legislação e aplicado o gross-down para exclusão dos tributos<br>embutidos, se aplicável.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3005; 3010].|
|41<br>gCobSobrPrevCom|p<br>detOper|CG|-|0-1|-|-<br>Grupo de informações de seguros de pessoas com cobertura por<br>sobrevivência e previdência complementar.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3025; 3030].|
|42vBCDistr|gCobSobrPrevCo|mp<br>E|N|1-1|4-18|2<br>Valor da base de cálculo aferida na operação para fins de distribuição<br>do IBS.<br>Corresponde ao valor da soma:<br>1. das contribuições ou prêmios para a entidade de previdência<br>complementar ou seguradora, deduzida da parcela destinada à<br>constituição de provisões ou reservas técnicas; e<br>2. dos encargos do fundo decorrentes da estruturação e da<br>manutenção de planos de previdência e seguro de pessoas com<br>cobertura por sobrevivência.|



**Página 75 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-2251 – Segu**|**ros, Previdência**|**Comple**<br>**Especif**|**ment**<br>**icaçã**|**ar e Cap**<br>**o Técni**|**italiza**<br>**ca de**|**ção - Identificação de Adquirentes (Leiaute Preliminar) –**<br>**Campos(Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo **|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|43|gCapitalizacao|detOper|CG|-|0-1|-|-<br>Identificação de operações de capitalização.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3405; 3410].|
|44|vOper|gCapitalizacao|E|N|1-1|4-18|2<br>Valor arrecadado com a venda do título de capitalização.|
|45|cMunOper|gCapitalizacao|E|N|0-1|7|-<br>Identificação<br>do<br>Município<br>correspondente<br>ao<br>local<br>de<br>comercialização do título de capitalização.<br>Código do município do IBGE.<br>**Preenchimento:**Exclusivo e obrigatório se {indIdentifAdq} = [0]<br>(Subscritor não obrigado a se identificar).|
|46|gTributos|detOper|G|-|0-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [3005;  3010].|
|47|vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|48|pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|49|vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|50|pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|51|vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|52|pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|
|53|vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 76 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.9 EVENTO D-3201 – PLANOS DE ASSISTÊNCIA À SAÚDE - IDENTIFICAÇÃO DE ADQUIRENTES E BENEFICIÁRIOS (LEIAUTE PRELIMINAR)** 

### **3.9.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-3201 – Planos de Assistência à Saúde - Identificação de Adquirentes e Beneficiários (Leiaute Preliminar) – Estrutura Hierár** **<mark>q</mark> uica do Evento** **<mark>(</mark> Resumo** **<mark>)</mark>** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtPlAssistSaude|DeRE|Evento de identificação e detalhamento dos<br>adquirentes e beneficiários dos planos de<br>assistência à saúde.|1-1|id|O|
|3|ideEvento|evtPlAssistSaude|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtPlAssistSaude|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtPlAssistSaude|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtPlAssistSaude|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000 c|hDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|1-1|CPF, CNPJ|O|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|qualifOper|detOper|Grupo destinado aos atributos e<br>qualificadores específicos da transação.|1-1|-|O|
|7|gBC|detOper|Grupo de aferição da base de cálculo do IBS<br>e da CBS.|1-1|-|O|
|8|gVOper|gBC|Detalhamento do valor da operação.|1-1|-|O|
|8|detBC|gBC|Grupo de detalhamento da base de cálculo<br>do IBS/CBS.|0-1|-|O (se {codBC} = [5050]);<br>N (nos demais casos).|
|8|gIdBenef|gBC|Grupo de identificação dos beneficiários de<br>planos de assistência à saúde.|0-1|-|O (se ({tpContrato} = [2; 4])<br>OU ({tpContrato} = [3] E<br>{codBC} = [5050]));<br>N (nos demais casos).|
|9|detBenef|gIdBenef|Detalhamento de beneficiários do contrato.|1-20000|-|O|
|10|detDependentes|detBenef|Detalhamento de beneficiários dependentes<br>vinculados.|0-1|-|OC (se {tpContrato} = [4]<br>(Plano ou seguro sem valor<br>definido por usuário) e<br>existirem dependentes<br>vinculados ao titular);<br>N (nos demais casos).|
|7|gTributos|detOper|Grupo de totalização de tributos incidentes<br>sobre a operação.|0-1|-|O (se {codBC} = [5050]);<br>N (nos demais casos).|



**Página 77 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.9.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-3201 – Planos**|**de Assistência à Sa**|**úde -**|**Ident**<br>**Téc**|**ificação**<br>**nica de**|**de Adq**<br>**Campos**|**uirentes e Beneficiários (Leiaute Preliminar) – Especificação**<br> **(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtPlAssistSaude|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento dos adquirentes e<br>beneficiários dos planos de assistência à saúde.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[5010; 5020; 5030; 5050].|
|3<br>id|evtPlAssistSaude|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtPlAssistSaude|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtPlAssistSaude|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtPlAssistSaude|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|
|14<br>infoOper|evtPlAssistSaude|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[5010; 5020; 5030; 5050].|



**Página 78 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-3201 – Planos de Assistência à Saúde - Identificação de Adquirentes e Beneficiários (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20<br>infoAdq|idePartes|G|-|1-1|-|-<br>Informações de identificação do adquirente.|
|21CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|22CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|23cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|24<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|25NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|26cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|27nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|28cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|



**Página 79 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-3201 – Planos de Assistência à Saúde - Identificação de Adquirentes e Beneficiários (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|29<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operações por adquirente.|
|30<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|31seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|32seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|33dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|34<br>qualifOper|detOper|G|-|1-1|-|-<br>Grupo destinado aos atributos e qualificadores específicos da<br>transação.|
|35idContrato|qualifOper|E|C|1-1|1-30|-<br>Número de identificação do contrato relativo à operação.<br>Código alfanumérico que identifica unicamente o contrato ou apólice<br>de saúde na base da operadora ou administradora.|
|36tpContrato|qualifOper|E|N|1-1|1|-<br>Tipo de contrato.<br>**Valores válidos:**<br>**1**– Plano ou seguro individual/familiar;<br>**2**– Plano ou seguro coletivo sem administradora de benefícios;<br>**3**– Plano ou seguro coletivo com administradora de benefícios;<br>**4**– Plano ou seguro sem valor definido por usuário.|
|37CNPJEntidRelac|qualifOper|E|C|0-1|14|-<br>Número de inscrição no CNPJ da outra entidade jurídica participante<br>da relação do contrato coletivo administrado.<br>**Preenchimento:**Exclusivo e obrigatório se {tpContrato} = [3].<br>**Validação:**<br>SE {codBC} = [5050], informar o CNPJ da entidade (plano ou seguro<br>saúde);<br>SENÃO, informar o CNPJ da administradora de benefícios vinculada.<br>**Regra de validação:**<br>DV_CNPJ|
|38<br>gBC|detOper|G|-|1-1|-|-<br>Grupo de aferição da base de cálculo do IBS e da CBS.|
|39<br>gVOper|gBC|G|-|1-1|-|-<br>Detalhamento do valor da operação.|
|40vOper|gVOper|E|N|1-1|4-18|2<br>Valor total recebido pela contraprestação ou prêmio de seguro saúde<br>referente ao contrato ou, em se tratando de  administradoras de<br>benefícios, o valor da comissão recebida.|



**Página 80 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-3201 – Planos**|**de Assistência à Sa**|**úde -**|**Ident**<br>**Téc**|**ificação**<br>**nica de**|**de Ad**<br>**Camp **|**quirentes e Beneficiários (Leiaute Preliminar) – Especificação**<br>**os(Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|41|detBC|gBC|G|-|0-1|-|-<br>Grupo de detalhamento da base de cálculo do IBS/CBS.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [5050].|
|42|vBCApur|detBC|E|N|1-1|4-18|2<br>Valor da base de cálculo líquida correspondente ao valor da operação<br>({vOper}) com a exclusão dos tributos que não integram a base de<br>cálculo (ISS e PIS/COFINS).|
|43|gIdBenef|gBC|G|-|0-1|-|-<br>Grupo de identificação dos beneficiários de planos de assistência à<br>saúde.<br>**Preenchimento:**Exclusivo e obrigatório nos seguintes casos:<br>1. Contrato coletivo sem administradora de benefícios ({tpContrato} =<br>[2]);<br>2. Contrato sem valor definido por usuário ({tpContrato} = [4]);<br>3. Contrato coletivo com administradora de benefícios quando o<br>evento for enviado por essa entidade ({tpContrato} = [3] E {codBC} =<br>[5050]);<br>Não informar nos demais casos.|
|44|detBenef|gIdBenef|G|-|1-20000|-|-<br>Detalhamento de beneficiários do contrato.|
|45|CPFTitular|detBenef|E|C|0-1|11|-<br>Número de inscrição no CPF do beneficiário titular do plano de saúde.<br>**Preenchimento:**Obrigatório se {tpContrato} = [2; 3; 4];<br>Não informar nos demais casos.<br>**Regra de validação:**<br>DV_CPF|
|46|dtNascTitular|detBenef|E|D|0-1|10|-<br>Data de nascimento do beneficiário titular do plano de saúde.<br>**Máscara:**AAAA-MM-DD<br>**Preenchimento:**Obrigatório se {tpContrato} = [4].<br>Não informar nos demais casos.|
|47|vTitularDep|detBenef|E|N|0-1|4-18|2<br>Valor total relativo ao titular do plano ou seguro, incluindo seus<br>dependentes.<br>**Preenchimento:**Obrigatório se {tpContrato} = [2; 3].<br>Não informar nos demais casos.|
|48|detDependentes|detBenef|G|-|0-1|-|-<br>Detalhamento de beneficiários dependentes vinculados.<br>**Preenchimento:**Exclusivo se {tpContrato} = [4] (Plano ou seguro sem<br>valor definido por usuário) e existirem dependentes vinculados ao<br>titular.|
|49|dtNascDep|detDependentes|E|D|1-50|10|-<br>Data de nascimento do beneficiário dependente vinculado ao titular<br>do plano de saúde.<br>**Máscara:**AAAA-MM-DD|
|50|gTributos|detOper|G|-|0-1|-|-<br>Grupo de totalização de tributos incidentes sobre a operação.<br>**Preenchimento:**Exclusivo e obrigatório se {codBC} = [5050].|
|51|vBCTrib|gTributos|E|N|1-1|4-18|2<br>Valor da base de cálculo oferecida à tributação.<br>**Cálculo:**<br>{vBCTrib} = {vBCApur}|
|52|pIBSMunTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS municipal incidente sobre a operação.|
|53|vIBSMunTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Municipal.<br>**Cálculo:**<br>{vIBSMunTrib} = {vBCTrib} * ({pIBSMunTrib} / 100)|
|54|pIBSUFTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota do IBS estadual incidente sobre a operação.|
|55|vIBSUFTrib|gTributos|E|N|1-1|4-18|2<br>Valor do IBS Estadual.<br>**Cálculo:**<br>{vIBSUFTrib} = {vBCTrib} * ({pIBSUFTrib} / 100)|
|56|pCBSTrib|gTributos|E|N|1-1|8-10|6<br>Alíquota da CBS incidente sobre a operação.|
|57|vCBSTrib|gTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>|
||||||||**Cálculo:**<br>{vCBSTrib} = {vBCTrib} * ({pCBSTrib} / 100)|



**Página 81 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.10 EVENTO D-4201 – CONCURSOS DE PROGNÓSTICOS - DISCRIMINAÇÃO DE APOSTAS, PRÊMIOS E APOSTADORES (LEIAUTE PRELIMINAR)** 

### **3.10.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-4201 – Concursos de Prognósticos - Discriminação de Apostas, Prêmios e Apostadores (Leiaute Preliminar) – Estrutura** 

**Hierár** **<mark>q</mark> uica do Evento** **<mark>(</mark> Resumo** **<mark>)</mark>** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtIdApostPrem|DeRE|Evento de identificação e detalhamento de<br>apostas e prêmios por apostador.|1-1|id|O|
|3|ideEvento|evtIdApostPrem|Informações de identificação do evento.|1-1|-|O|
|3|ideContrib|evtIdApostPrem|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|idePeriodo|evtIdApostPrem|Período de referência das informações do<br>evento.|1-1|perApur|O|
|3|infoOper|evtIdApostPrem|Informações das operações.|1-1|-|O|
|4|gOper|infoOper|Grupo de operações por adquirente.|1-1000|chDeRE, chOperRef|O|
|5|idePartes|gOper|Grupo de identificação e qualificação das<br>partes envolvidas.|1-1|-|O|
|6|infoAdq|idePartes|Informações de identificação do adquirente.|0-1|CPF, CNPJ|O (se {indApostaPresenc} =<br>[0] (Virtual));<br>OC (se {indApostaPresenc} =<br>[1] (Presencial)).|
|7|adqExterior|infoAdq|Identificação do adquirente residente ou<br>domiciliado no exterior.|0-1|-|O (se {CPF} e {CNPJ} não<br>informados);<br>N (nos demais casos).|
|6|infoPDV|idePartes|Informações do ponto de venda.|0-1|CPFPDV, CNPJPDV|O (se grupo {infoAdq} não<br>informado);<br>N (nos demais casos).|
|5|dadosOper|gOper|Grupo de informação das operações por<br>adquirente.|1-1|-|O|
|6|detOper|dadosOper|Detalhamento das informações da operação<br>individualizada.|1-999|seq, seqRef|O|
|7|infoAposta|detOper|Grupo de detalhamento de informações de<br>apostas realizadas, consolidadas<br>diariamente para cada apostador.|1-1|-|O|



**Página 82 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **3.10.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-4201 – Concurso**|**s de Prognósticos**|**- Disc**|**rimin**<br>**Téc**|**ação de**<br>**nica de**|**Aposta**<br>**Campo **|**s, Prêmios e Apostadores (Leiaute Preliminar) – Especificação**<br>**s(Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtIdApostPrem|DeRE|G|-|1-1|-|-<br>Evento de identificação e detalhamento de apostas e prêmios por<br>apostador.<br>**Validação:**Evento exclusivo e obrigatório para o registro de<br>operações cujos códigos de base de cálculo ({codBC}) correspondam<br>a um dos seguintes valores:<br>[6010].|
|3<br>id|evtIdApostPrem|A|C|1-1|42|-<br>Identificador que representa unicamente o evento.<br>**Regra de validação:**<br>RN - Unicidade Recepção Evento|
|4<br>ideEvento|evtIdApostPrem|G|-|1-1|-|-<br>Informações de identificação do evento.|
|5<br>finEvt|ideEvento|E|N|1-1|2|-<br>Finalidade do evento.<br>**Valores válidos:**<br>**11**– Registro original;<br>**31**– Devolução;<br>**41**– Cancelamento.|
|6<br>tpAmb|ideEvento|E|N|1-1|1|-<br>Identificação do ambiente para o qual os dados estão sendo<br>transmitidos.<br>**Valores válidos:**<br>**1**– Produção;<br>**2**– Produção restrita.<br>**Regra de validação:**<br>AMBIENTE|
|7<br>aplicEmi|ideEvento|E|N|1-1|1|-<br>Identificação do aplicativo emissor do evento.<br>**Valores válidos:**<br>**1**– Emissão com aplicativo da empresa;<br>**2**– Aplicativo governamental.|
|8<br>verAplic|ideEvento|E|C|1-1|1-20|-<br>Versão do aplicativo emissor do evento.|
|9<br>ideContrib|evtIdApostPrem|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|10nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.<br>**Regras de validação:**<br>EXISTE_INFO_CONTRIBUINTE<br>CONTRIBUINTE_NO_CADASTRO|
|11<br>idePeriodo|evtIdApostPrem|G|-|1-1|-|-<br>Período de referência das informações do evento.|
|12perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Regras de validação:**<br>REJEITAR_PERAPUR_FUTURO<br>PERAPUR_FECHADO|
|13dhEmi|idePeriodo|E|D|1-1|29|-<br>Data e hora de emissão do documento fiscal.<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD|
|14<br>infoOper|evtIdApostPrem|G|-|1-1|-|-<br>Informações das operações.|
|15<br>gOper|infoOper|G|-|1-1000|-|-<br>Grupo de operações por adquirente.|
|16chDeRE|gOper|E|C|1-1|53|-<br>Chave de acesso de agrupamento de operações da DeRE.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|17codBC|gOper|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].<br>**Validação:**Deve ser um código da listagem abaixo:<br>[6010].|



**Página 83 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-4201 – Concursos de Prognósticos - Discriminação de Apostas, Prêmios e Apostadores (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|18chOperRef|gOper|E|C|0-1|53|-<br>Chave da operação objeto de cancelamento ou devolução.<br>**Preenchimento:**Exclusivo e obrigatório se {finEvt} = [31; 41].<br>**Validação:**Deve ser uma chave válida e existente no banco de dados.<br>**Regra de validação:**<br>RN - Formação da Chave da DeRE|
|19<br>idePartes|gOper|G|-|1-1|-|-<br>Grupo de identificação e qualificação das partes envolvidas.|
|20indApostaPresenc|idePartes|E|N|1-1|1|-<br>Indicador se a aposta no concurso de prognósticos foi realizada de<br>forma presencial.<br>**Valores válidos:**<br>**0**– Aposta realizada em meio virtual, remoto, eletrônico ou online;<br>**1**– Aposta realizada presencialmente.|
|21<br>infoAdq|idePartes|G|-|0-1|-|-<br>Informações de identificação do adquirente.<br>**Preenchimento:**Opcional se {indApostaPresenc} = [1] (Presencial).<br>Obrigatório nos demais casos.|
|22CPF|infoAdq|CE|C|0-1|11|-<br>Número de inscrição no CPF do adquirente, se pessoa física.<br>**Preenchimento:**Não informar se o campo {CNPJ} ou grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|23CNPJ|infoAdq|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do adquirente, se pessoa jurídica.<br>**Preenchimento:**Não informar se o campo {CPF} ou o grupo<br>{adqExterior} forem informados.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|24cMun|infoAdq|E|N|0-1|7|-<br>Código IBGE do município do endereço do adquirente, conforme<br>informações existentes na base cadastral do declarante.<br>**Preenchimento:**Obrigatório se {CPF} ou {CNPJ forem informados.<br>Não preencher nos demais casos.<br>**Validação:**Deve ser um código existente na tabela de municípios do<br>IBGE.|
|25<br>adqExterior|infoAdq|G|-|0-1|-|-<br>Identificação do adquirente residente ou domiciliado no exterior.<br>**Preenchimento:**Exclusivo e obrigatório quando o adquirente da<br>operação for residente ou domiciliado no exterior e não possuir<br>inscrição ativa no CPF ou CNPJ no território nacional.|
|26NIF|adqExterior|CE|C|0-1|1-20|-<br>Número de Identificação Fiscal (NIF) do adquirente estrangeiro,<br>fornecido por órgão de administração tributária no exterior.<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se o {cNaoNIF} não for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|27cNaoNIF|adqExterior|CE|N|0-1|1|-<br>Código do motivo para a não informação do NIF do adquirente<br>estrangeiro.<br>**Preenchimento:**Exclusivo e obrigatório se o {NIF} não for informado.<br>**Valores válidos:**<br>**1**– Dispensado de NIF;<br>**2**– Não exigência de NIF.|
|28nrDocIdent|adqExterior|CE|C|0-1|1-20|-<br>Número do documento de identificação oficial no exterior<br>(passaporte, carteira de identidade estrangeira ou documento oficial<br>equivalente).<br>**Preenchimento:**<br>1. Exclusivo e obrigatório se {cNaoNIF} for informado;<br>2. Formato alfanumérico, sem máscaras ou caracteres especiais.|
|29cPais|adqExterior|E|C|1-1|2|-<br>Código de identificação do país de domicílio fiscal (emissor do NIF) ou<br>de residência física do adquirente residente no exterior, conforme<br>coluna “A2” da [[Tabela15– Tabela de Países]].<br>**Exemplo:**[US; PT; AR].|



**Página 84 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-4201 – Concursos de Prognósticos - Discriminação de Apostas, Prêmios e Apostadores (Leiaute Preliminar) – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|30<br>infoPDV|idePartes|G|-|0-1|-|-<br>Informações do ponto de venda.<br>**Preenchimento:**Exclusivo e obrigatório quando o grupo {infoAdq}<br>não for informado.|
|31CPFPDV|infoPDV|CE|C|0-1|11|-<br>Número de inscrição no CPF do ponto de venda.<br>**Preenchimento:**Não preencher se {CNPJPDV} for informado.<br>**Validação:**Deve ser um CPF válido.<br>**Regra de validação:**<br>DV_CPF|
|32CNPJPDV|infoPDV|CE|C|0-1|14|-<br>Número de inscrição no CNPJ do ponto de venda.<br>**Preenchimento:**Não preencher se {CPFPDV} for informado.<br>**Validação:**Deve ser um CNPJ válido.<br>**Regra de validação:**<br>DV_CNPJ|
|33<br>dadosOper|gOper|G|-|1-1|-|-<br>Grupo de informação das operaçõespor adquirente.|
|34<br>detOper|dadosOper|G|-|1-999|-|-<br>Detalhamento das informações da operação individualizada.|
|35seq|detOper|A|C|1-1|3|-<br>Número sequencial de identificação da operação individualizada<br>dentro deste agrupamento.<br>**Preenchimento:**Deve ser preenchido de forma incremental e<br>cronológica (de [001] a [999]).<br>**Nota:**Este sequencial é utilizado pela base de dados, substituindo o<br>sufixo [000] da chave-mãe do agrupamento (chDeRE) para compor a<br>chave única da transação (chave-filha).<br>**Valores válidos:**<br>**001-999**.|
|36seqRef|detOper|E|C|0-1|3|-<br>Número sequencial ({seq}) da chave-filha da operação de origem<br>registrada anteriormente na DeRE, à qual se vincula a presente<br>operação (ex: devolução, cancelamento etc.).<br>**Preenchimento:**Exclusivo e obrigatório se o campo {chOperRef} for<br>preenchido.<br>**Validação:**O sequencial informado deve constar como ativo e<br>associado à chave-mãe referenciada ({chOperRef}) na base de dados<br>da DeRE.<br>**Valores válidos:**<br>**001-999**.|
|37dhOper|detOper|E|D|1-1|29|-<br>Data e hora da operação (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssTZD<br>**Validação:**Deve estar compreendida no período de apuração<br>({perApur}) deste evento.|
|38<br>infoAposta|detOper|G|-|1-1|-|-<br>Grupo de detalhamento de informações de apostas realizadas,<br>consolidadas diariamente para cada apostador.|
|39cMunOper|infoAposta|E|N|0-1|7|-<br>Identificação do Município correspondente ao local das apostas<br>realizadas presencialmente. Código do município do IBGE.<br>**Preenchimento:**Exclusivo e obrigatório se {indApostaPresenc} = [1]<br>(Presencial).|
|40vOper|infoAposta|E|N|1-1|4-18|2<br>Valor total das operações realizadas pelo apostador.|



**Página 85 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4 EVENTOS DE RETORNO E TOTALIZAÇÃO** 

### **4.1 EVENTO D-9001 – RETORNO – EVENTOS DE TABELA** 

### **4.1.1 Estrutura Hierárquica do Evento (Resumo)** 

||<br> <br>|**D-9001 – Retorno**<br>|**– Eventos de Tabela – Estrutura Hierárqu**<br>|**ica do**<br>|**Evento (Resumo)**<br>|<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoTabela|DeRE|Retorno de eventos de tabela.|1-1|id|O|
|3|ideContrib|evtRetornoTabela|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoTabela|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoTabela|Informações de processamento dos eventos.|1-1|-|O|
|3|infoEvento|evtRetornoTabela|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>validade do evento.|1-1|iniValid, fimValid|O|
|5|novaValidade|idePeriodo|Novo período de validade.|0-1|iniValid, fimValid|OC|
|3|extratoEventos|evtRetornoTabela|Grupo contendo o "extrato" completo das<br>vigências ativas para os eventos<br>processados da respectiva tabela do<br>contribuinte.|0-1|-|OC|
|4|detEvento|extratoEventos|Detalhamento do evento constante na<br>tabela do contribuinte.|0-100|-|O|
|4|detLacuna|extratoEventos|Detalhamento de período descoberto (sem<br>cobertura de evento) na linha do tempo.|0-100|-|OC|



**Página 86 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.1.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-**<br>**#**<br>**Grupo/Tag**|**9001 – Retorno –**<br>**Grupo Pai**|**Event**<br>**Cat**|**os de**<br>**Tipo**|**Tabel**<br> <br>**Ocorr**|**a – Especi**<br> <br>**Tam**<br>**De**|**ficação Técnica de Campos (Detalhamento)**<br>**c**<br>**Descrição**|
|---|---|---|---|---|---|---|
|1<br>DeRE|-|G|-|1-1|-<br>-|<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoTabela|DeRE|G|-|1-1|-<br>-|<br>Retorno de eventos de tabela.|
|3<br>id|evtRetornoTabela|A|C|1-1|42<br>-|<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoTabela|G|-|1-1|-<br>-|<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8<br>-|<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoTabela|G|-|1-1|-<br>-|<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1<br>-|<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7<br>-|<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-<br>-|<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6<br>-|<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048<br>-|<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1<br>-|<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048<br>-|<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoTabela|G|-|1-1|-<br>-|<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31<br>-|<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16protocoloLote|infoRecEv|E|C|0-1|28<br>-|<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|17dhRecepcao|infoRecEv|E|D|1-1|25-33<br>-|<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|18dhProcess|infoRecEv|E|D|1-1|25-33<br>-|<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|19tpEv|infoRecEv|E|C|1-1|6<br>-|<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-1001, D-1011.|
|20hash|infoRecEv|E|C|1-1|44<br>-|<br>Hashcode do arquivo processado.|
|21<br>infoEvento|evtRetornoTabela|G|-|0-1|-<br>-|<br>Informações do evento processado.|
|22<br>idePeriodo|infoEvento|G|-|1-1|-<br>-|<br>Grupo de identificação do período de validade do evento.|
|23iniValid|idePeriodo|E|D|1-1|10<br>-|<br>Data de início da validade informada no evento.<br>**Máscara:**AAAA-MM-DD|
|24fimValid|idePeriodo|E|D|0-1|10<br>-|<br>Data de término da validade informada no evento, se houver.<br>**Máscara:**AAAA-MM-DD|



**Página 87 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D**|**-9001 – Retorno –**|**Event**|**os de**|**Tabela**|**– Espe**|**cificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|25<br>|novaValidade|idePeriodo|G|-|0-1|-|-<br>Novo período de validade.<br>**Nota:**Preenchido exclusivamente em casos de alteração de vigência<br>de eventojá existente.|
|26|iniValid|novaValidade|E|D|1-1|10|-<br>Data de início da nova validade das informações.<br>**Máscara:**AAAA-MM-DD|
|27|fimValid|novaValidade|E|D|0-1|10|-<br>Data de término da nova validade das informações, se houver.<br>**Máscara:**AAAA-MM-DD|
|28<br>|extratoEventos|evtRetornoTabela|G|-|0-1|-|-<br>Grupo contendo o "extrato" completo das vigências ativas para os<br>eventos processados da respectiva tabela do contribuinte.<br>Retorna a "Foto" atualizada após o processamento.|
|29<br>|detEvento|extratoEventos|G|-|0-100|-|-<br>Detalhamento do evento constante na tabela do contribuinte.|
|30|nrRecibo|detEvento|E|C|1-1|31|-<br>Número do recibo do evento que fundamenta a vigência deste<br>período.|
|31|iniValid|detEvento|E|D|1-1|10|-<br>Data de início de validade informada pelo contribuinte.<br>**Máscara:**AAAA-MM-DD|
|32|fimValid|detEvento|E|D|0-1|10|-<br>Data de fim de validade informada pelo contribuinte.<br>**Preenchimento:**Não informado se for indeterminada/aberta.<br>**Máscara:**AAAA-MM-DD|
|33|fimValidEfetiva|detEvento|E|D|0-1|10|-<br>Data limite da validade do evento considerada pelo sistema.<br>Corresponde à data efetiva de encerramento do período, aplicando-<br>se as regras de não-sobreposição.<br>**Preenchimento:**<br>a) Se {fimValid} foi informado: Este campo será idêntico ao {fimValid};<br>b) Se {fimValid} é indeterminado e não há evento posterior: Este<br>campo não é informado;<br>c) Se {fimValid} é indeterminado, mas existe evento posterior: Este<br>campo conterá a data de corte (dia anterior ao início do próximo<br>evento),<br>indicando<br>encurtamento<br>forçado<br>da<br>validade<br>por<br>sobreposição de novo evento ({indAjusteAutomatico} = [1]).<br>**Máscara:**AAAA-MM-DD|
|34|indAjusteAuto|detEvento|E|N|0-1|1|-<br>Indicador se este período sofreu corte temporal com a atribuição do<br>campo {fimValidEfetiva} pelo sistema em razão da existência de outro<br>evento com vigência posterior.<br>**Valores válidos:**<br>**0**– Não;<br>**1**– Sim (houve corte pelo sistema).|
|35<br>|detLacuna|extratoEventos|G|-|0-100|-|-<br>Detalhamento de período descoberto (sem cobertura de evento) na<br>linha do tempo.|
|36|iniLacuna|detLacuna|E|D|1-1|10|-<br>Data de início do período da lacuna.<br>**Máscara:**AAAA-MM-DD|
|37|fimLacuna|detLacuna|E|D|0-1|10|-<br>Data fim do período da lacuna.<br>**Preenchimento:**Não informado se for indeterminada/aberta.<br>**Máscara:**AAAA-MM-DD|



**Página 88 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.2 EVENTO D-9101 – RETORNO TOTALIZADOR – BALANCETE MENSAL** 

### **4.2.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-910**<br> <br>|**1 – Retorno Totali**<br>|**zador – Balancete Mensal – Estrutura Hie**<br>|**rárqui**<br>|**ca do Evento (Res**<br>|**umo)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoBalan|DeRE|Retorno do evento Balancete Mensal.|1-1|id|O|
|3|ideContrib|evtRetornoBalan|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoBalan|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoBalan|Informações de processamento dos eventos.|1-1|-|O|
|3|infoEvento|evtRetornoBalan|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>referência das informações do evento.|1-1|perApur|O|
|4|infoAdic|infoEvento|Grupo destinado à prestação de<br>informações complementares relativas ao<br>evento processado.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|
|4|infoTotBalan|infoEvento|Informações de totalizadores do evento D-<br>1101 - Balancete Mensal {evtBalancete}.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|
|5|gTotalCodTrib|infoTotBalan|Totalização de informações por Código de<br>Tributação.|0-999|codTrib, indTribISS|OC|



**Página 89 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.2.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-9101**|**– Retorno Totaliz**|**ador –**|**Bala**|**ncete**|**Mensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoBalan|DeRE|G|-|1-1|-|-<br>Retorno do evento Balancete Mensal.|
|3<br>id|evtRetornoBalan|A|C|1-1|42|-<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoBalan|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoBalan|G|-|1-1|-|-<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1|-<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7|-<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-|-<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6|-<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048|-<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1|-<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048|-<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoBalan|G|-|1-1|-|-<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31|-<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16seqEvento|infoRecEv|E|N|0-1|2|-<br>Número sequencial de identificação que indica a versão do evento<br>processado na base de dados.<br>**Nota:**O controle sequencial é ininterrupto para a mesma chave de<br>identificação. A exclusão de um evento não zera a contagem para<br>futuras reinclusões.<br>**Exemplo:**<br>1º Envio (Inclusão): seqEvento = [00];<br>2º Envio (Alteração): seqEvento = [01];<br>3º Envio (Exclusão): seqEvento = [02];<br>4º Envio (Nova Inclusão): seqEvento = [03].<br>**Valores válidos:**<br>**00**– Evento Original (atribuído exclusivamente no processamento com<br>sucesso da primeira Inclusão ({tpOper} = [1]) para uma chave inédita<br>no banco de dados);<br>**01-99**– Evento Versionado (atribuído de forma sucessiva e<br>cronológica ao processar eventos de Alteração ({tpOper} = [2]),<br>Exclusão ({tpOper} = [3]) ou nova Inclusão ({tpOper} = [1]) de uma<br>chave previamente excluída).|
|17protocoloLote|infoRecEv|E|C|0-1|28|-<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|18dhRecepcao|infoRecEv|E|D|1-1|25-33|-<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|



**Página 90 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9101 – Retorno Totalizador – Balancete Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|
|19dhProcess|infoRecEv|E|D|1-1|25-33|-<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|20tpEv|infoRecEv|E|C|1-1|6|-<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-1101.|
|21hash|infoRecEv|E|C|1-1|44|-<br>Hashcode do arquivo processado.|
|22<br>infoEvento|evtRetornoBalan|G|-|0-1|-|-<br>Informações do evento processado.|
|23<br>idePeriodo|infoEvento|G|-|1-1|-|-<br>Grupo de identificação do período de referência das informações do<br>evento.|
|24perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Cálculo:**<br>Valor do campo {perApur} informado no evento de origem.|
|25<br>infoAdic|infoEvento|G|-|0-1|-|-<br>Grupo destinado à prestação de informações complementares<br>relativas ao evento processado.|
|26nrReciboPGCC|infoAdic|E|C|1-1|31|-<br>Número do recibo do evento D-1011 (Plano Geral de Contas<br>Comentado) utilizado para o mapeamento das contas e totalização<br>deste processamento.|
|27<br>infoTotBalan|infoEvento|G|-|0-1|-|-<br>Informações de totalizadores do evento D-1101– Balancete Mensal<br>{evtBalancete}.|
|28<br>gTotalCodTrib|infoTotBalan|G|-|0-999|-|-<br>Totalização de informações por Código de Tributação.|
|29codTrib|gTotalCodTrib|E|N|1-1|9|-<br>Código de tributação para fins de IBS, CBS e IS.<br>**Preenchimento:**Listagem de valores únicos de {codTrib} informados<br>no evento D-1101 ({evtBalancete}).|
|30indTribISS|gTotalCodTrib|E|N|1-1|1|-<br>Indicador de sujeição ou vinculação ao ISSQN.<br>**Preenchimento:**Se não informado, atribuir [0].<br>**Valores válidos:**<br>**0**– Não sujeita ao ISS;<br>**1**– Sujeita ao ISS.|
|31vApurTot|gTotalCodTrib|E|N|1-1|4-18|2<br>Somatório de todas as ocorrências de {vApur} do evento D-1101<br>({evtBalancete}) para o {codTrib} e {indTribISS} informados.<br>**Cálculo:**<br>SOMA({vApur.codTrib} = {codTrib})|
|32vTotSaldoInic|gTotalCodTrib|E|N|0-1|4-18|2<br>Somatório de todas as ocorrências de {vSaldoInic} do evento D-1101<br>({evtBalancete}) para o {codTrib} informado.<br>**Preenchimento:**Campo retornado apenas se {codTrib} = [220110001;<br>220210001; 220310001].<br>**Cálculo:**<br>SOMA({vSaldoInic.codTrib} = [220110001; 220210001; 220310001])|
|33vTotSaldoFinal|gTotalCodTrib|E|N|0-1|4-18|2<br>Somatório de todas as ocorrências de {vSaldoFinal} do evento D-1101<br>({evtBalancete}) para o {codTrib} informado.<br>**Preenchimento:**Campo retornado apenas se {codTrib} = [220110001;<br>220210001; 220310001].<br>**Cálculo:**<br>SOMA({vSaldoFinal.codTrib} = [220110001; 220210001; 220310001])|



**Página 91 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.3 EVENTO D-9106 – RETORNO TOTALIZADOR – IDENTIFICAÇÃO DE APLICAÇÕES FINANCEIRAS** 

### **4.3.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-9106 – Retorno Totalizador – Identificação de Aplicações Financeiras – Estrutura Hierárquica do Evento (Resumo)** 

|**Nível**|**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoAplicFin|DeRE|Retorno do evento Identificação de<br>Aplicações Financeiras.|1-1|id|O|
|3|ideContrib|evtRetornoAplicFin|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoAplicFin|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoAplicFin|Informações de processamento dos eventos.|1-1|-|O|
|3|infoEvento|evtRetornoAplicFin|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>referência das informações do evento.|1-1|perApur|O|
|4|infoAdic|infoEvento|Grupo destinado à prestação de<br>informações complementares relativas ao<br>evento processado.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|
|4|infoTotAplicFin|infoEvento|Informações de totalizadores do evento D-<br>1106 - Aplicações Financeiras<br>{evtAplicResTec}.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|



**Página 92 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.3.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-9106 – Retorno**|**Totalizador – Ident**|**ifica**|**ção de**|**Aplic**|**ações Fin**|**anceiras – Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoAplicFin|DeRE|G|-|1-1|-|-<br>Retorno do evento Identificação de Aplicações Financeiras.|
|3<br>id|evtRetornoAplicFin|A|C|1-1|42|-<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoAplicFin|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoAplicFin|G|-|1-1|-|-<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1|-<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7|-<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-|-<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6|-<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048|-<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1|-<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048|-<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoAplicFin|G|-|1-1|-|-<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31|-<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16seqEvento|infoRecEv|E|N|0-1|2|-<br>Número sequencial de identificação que indica a versão do evento<br>processado na base de dados.<br>**Nota:**O controle sequencial é ininterrupto para a mesma chave de<br>identificação. A exclusão de um evento não zera a contagem para<br>futuras reinclusões.<br>**Exemplo:**<br>1º Envio (Inclusão): seqEvento = [00];<br>2º Envio (Alteração): seqEvento = [01];<br>3º Envio (Exclusão): seqEvento = [02];<br>4º Envio (Nova Inclusão): seqEvento = [03].<br>**Valores válidos:**<br>**00**– Evento Original (atribuído exclusivamente no processamento com<br>sucesso da primeira Inclusão ({tpOper} = [1]) para uma chave inédita<br>no banco de dados);<br>**01-99**– Evento Versionado (atribuído de forma sucessiva e<br>cronológica ao processar eventos de Alteração ({tpOper} = [2]),<br>Exclusão ({tpOper} = [3]) ou nova Inclusão ({tpOper} = [1]) de uma<br>chave previamente excluída).|
|17protocoloLote|infoRecEv|E|C|0-1|28|-<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|18dhRecepcao|infoRecEv|E|D|1-1|25-33|-<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|



**Página 93 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9106 – Retorno Totalizador – Identificação de Aplicações Financeiras – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|19|dhProcess|infoRecEv|E|D|1-1|25-33|-<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|20|tpEv|infoRecEv|E|C|1-1|6|-<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-1106.|
|21|hash|infoRecEv|E|C|1-1|44|-<br>Hashcode do arquivo processado.|
|22|infoEvento|evtRetornoAplicFin|G|-|0-1|-|-<br>Informações do evento processado.|
|23|idePeriodo|infoEvento|G|-|1-1|-|-<br>Grupo de identificação do período de referência das informações do<br>evento.|
|24|perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Cálculo:**<br>Valor do campo {perApur} informado no evento de origem.|
|25|infoAdic|infoEvento|G|-|0-1|-|-<br>Grupo destinado à prestação de informações complementares<br>relativas ao evento processado.|
|26|nrReciboPGCC|infoAdic|E|C|1-1|31|-<br>Número do recibo do evento D-1011 (Plano Geral de Contas<br>Comentado) utilizado para o mapeamento das contas e totalização<br>deste processamento.|
|27|infoTotAplicFin|infoEvento|G|-|0-1|-|-<br>Informações de totalizadores do evento D-1106– Aplicações<br>Financeiras {evtAplicResTec}.|
|28|vApurTot|infoTotAplicFin|E|N|1-1|4-18|2<br>Somatório de todas as ocorrências de {vApur} do evento D-1106<br>({evtAplicResTec}).<br>**Cálculo:**<br>SOMA({vApur})|



**Página 94 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.4 EVENTO D-9112 – RETORNO – RELAÇÃO DE DEDUÇÕES UTILIZADAS NA APURAÇÃO** 

### **4.4.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-9112 – Reto**<br> <br>|**rno – Relação de**<br>|**Deduções Utilizadas na Apuração – Estru**<br>|**tura Hier**<br>|**árquica do E**<br>|**vento (Resumo)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoRDed|DeRE|Retorno do evento Relação de Deduções<br>Utilizadas na Apuração.|1-1|id|O|
|3|ideContrib|evtRetornoRDed|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoRDed|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoRDed|Informações de processamento dos eventos.|1-1|-|O|
|3|infoEvento|evtRetornoRDed|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>referência das informações do evento.|1-1|perApur|O|
|4|infoAdic|infoEvento|Grupo destinado à prestação de<br>informações complementares relativas ao<br>evento processado.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|



**Página 95 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.4.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-9112 – Retor**|**no – Relação de D**|**eduçõ**|**es Uti**|**lizada**|**s na Apu**|**ração – Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|<br>**Tam**<br>**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoRDed|DeRE|G|-|1-1|-|-<br>Retorno do evento Relação de Deduções Utilizadas na Apuração.|
|3<br>id|evtRetornoRDed|A|C|1-1|42|-<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoRDed|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoRDed|G|-|1-1|-|-<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1|-<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7|-<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-|-<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6|-<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048|-<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1|-<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048|-<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoRDed|G|-|1-1|-|-<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31|-<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16seqEvento|infoRecEv|E|N|0-1|2|-<br>Número sequencial de identificação que indica a versão do evento<br>processado na base de dados.<br>**Nota:**O controle sequencial é ininterrupto para a mesma chave de<br>identificação. A exclusão de um evento não zera a contagem para<br>futuras reinclusões.<br>**Exemplo:**<br>1º Envio (Inclusão): seqEvento = [00];<br>2º Envio (Alteração): seqEvento = [01];<br>3º Envio (Exclusão): seqEvento = [02];<br>4º Envio (Nova Inclusão): seqEvento = [03].<br>**Valores válidos:**<br>**00**– Evento Original (atribuído exclusivamente no processamento com<br>sucesso da primeira Inclusão ({tpOper} = [1]) para uma chave inédita<br>no banco de dados);<br>**01-99**– Evento Versionado (atribuído de forma sucessiva e<br>cronológica ao processar eventos de Alteração ({tpOper} = [2]),<br>Exclusão ({tpOper} = [3]) ou nova Inclusão ({tpOper} = [1]) de uma<br>chave previamente excluída).|
|17protocoloLote|infoRecEv|E|C|0-1|28|-<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|18dhRecepcao|infoRecEv|E|D|1-1|25-33|-<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|



**Página 96 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9112 – Retor**|**no – Relação de D**|**eduçõ**|**es Uti**|**lizadas**|**na Ap**|**uração – Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|19|dhProcess|infoRecEv|E|D|1-1|25-33|-<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|20|tpEv|infoRecEv|E|C|1-1|6|-<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-1121.|
|21|hash|infoRecEv|E|C|1-1|44|-<br>Hashcode do arquivo processado.|
|22|infoEvento|evtRetornoRDed|G|-|0-1|-|-<br>Informações do evento processado.|
|23|idePeriodo|infoEvento|G|-|1-1|-|-<br>Grupo de identificação do período de referência das informações do<br>evento.|
|24|perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Cálculo:**<br>Valor do campo {perApur} informado no evento de origem.|
|25|infoAdic|infoEvento|G|-|0-1|-|-<br>Grupo destinado à prestação de informações complementares<br>relativas ao evento processado.|
|26|nrReciboPGCC|infoAdic|E|C|1-1|31|-<br>Número do recibo do evento D-1011 (Plano Geral de Contas<br>Comentado) utilizado para o mapeamento das contas e totalização<br>deste processamento.|



**Página 97 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.5 EVENTO D-9121 – RETORNO TOTALIZADOR – DÉBITO EM OPERAÇÕES COM TÍTULOS DE DÍVIDA COM OFERTA PÚBLICA** 

### **4.5.1 Estrutura Hierárquica do Evento (Resumo)** 

**D-9121 – Retorno Totalizador – Débito em Operações com Títulos de Dívida com Oferta Pública – Estrutura Hierárquica do** 

||||**Evento(Resumo)**<br>||||
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoTitPub|DeRE|Retorno do evento Débito em Operações<br>com Títulos de Dívida com Oferta Pública.|1-1|id|O|
|3|ideContrib|evtRetornoTitPub|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoTitPub|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoTitPub|Informações de processamento dos eventos.|1-1|-|O|
|3|infoEvento|evtRetornoTitPub|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>referência das informações do evento.|1-1|perApur|O|
|4|infoAdic|infoEvento|Grupo destinado à prestação de<br>informações complementares relativas ao<br>evento processado.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|
|4|infoTotTitOfPub|infoEvento|Informações de totalizadores do evento D-<br>2101 - Débito em Operações com Títulos de<br>Dívida com Oferta Pública<br>{evtDebOpOferPublica}.|0-1|-|O (se {tpOper} = [1; 2]);<br>N (se {tpOper} = [3]).|



**Página 98 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.5.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-9121 – Retorno**|**Totalizador – Débi**|**to em**|**Ope**|**rações**<br>**Cam**|**com Tí**<br>**pos(De**|**t**<br>**t**|**ulos de Dívida com Oferta Pública – Especificação Técnica de**<br>**alhamento)**|
|---|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|<br>**Tam**|**D**|**ec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-||-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoTitPub|DeRE|G|-|1-1|-||-<br>Retorno do evento Débito em Operações com Títulos de Dívida com<br>Oferta Pública.|
|3<br>id|evtRetornoTitPub|A|C|1-1|42||-<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoTitPub|G|-|1-1|-||-<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8||-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoTitPub|G|-|1-1|-||-<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1||-<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7||-<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-||-<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6||-<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048||-<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1||-<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048||-<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoTitPub|G|-|1-1|-||-<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31||-<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16seqEvento|infoRecEv|E|N|0-1|2||-<br>Número sequencial de identificação que indica a versão do evento<br>processado na base de dados.<br>**Nota:**O controle sequencial é ininterrupto para a mesma chave de<br>identificação. A exclusão de um evento não zera a contagem para<br>futuras reinclusões.<br>**Exemplo:**<br>1º Envio (Inclusão): seqEvento = [00];<br>2º Envio (Alteração): seqEvento = [01];<br>3º Envio (Exclusão): seqEvento = [02];<br>4º Envio (Nova Inclusão): seqEvento = [03].<br>**Valores válidos:**<br>**00**– Evento Original (atribuído exclusivamente no processamento com<br>sucesso da primeira Inclusão ({tpOper} = [1]) para uma chave inédita<br>no banco de dados);<br>**01-99**– Evento Versionado (atribuído de forma sucessiva e<br>cronológica ao processar eventos de Alteração ({tpOper} = [2]),<br>Exclusão ({tpOper} = [3]) ou nova Inclusão ({tpOper} = [1]) de uma<br>chave previamente excluída).|
|17protocoloLote|infoRecEv|E|C|0-1|28||-<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|



**Página 99 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9121 – Retorno Totalizador – Débito em Operações com Títulos de Dívida com Oferta Pública – Especificação Técnica de Cam** **<mark>p</mark> os** **<mark>(</mark> Detalhamento** **<mark>)</mark>** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|18|dhRecepcao|infoRecEv|E|D|1-1|25-33|-<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|19|dhProcess|infoRecEv|E|D|1-1|25-33|-<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|20|tpEv|infoRecEv|E|C|1-1|6|-<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-2101.|
|21|hash|infoRecEv|E|C|1-1|44|-<br>Hashcode do arquivo processado.|
|22|infoEvento|evtRetornoTitPub|G|-|0-1|-|-<br>Informações do evento processado.|
|23|idePeriodo|infoEvento|G|-|1-1|-|-<br>Grupo de identificação do período de referência das informações do<br>evento.|
|24|perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Cálculo:**<br>Valor do campo {perApur} informado no evento de origem.|
|25|infoAdic|infoEvento|G|-|0-1|-|-<br>Grupo destinado à prestação de informações complementares<br>relativas ao evento processado.|
|26|nrReciboPGCC|infoAdic|E|C|1-1|31|-<br>Número do recibo do evento D-1011 (Plano Geral de Contas<br>Comentado) utilizado para o mapeamento das contas e totalização<br>deste processamento.|
|27|infoTotTitOfPub|infoEvento|G|-|0-1|-|-<br>Informações de totalizadores do evento D-2101– Débito em<br>Operações<br>com<br>Títulos<br>de<br>Dívida<br>com<br>Oferta<br>Pública<br>{evtDebOpOferPublica}.|
|28|vApurTot|infoTotTitOfPub|E|N|1-1|4-18|2<br>Somatório de todas as ocorrências de {vApur} do evento D-2101<br>({evtDebOpOferPublica}).<br>**Cálculo:**<br>SOMA({vApur})|



**Página 100 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.6 EVENTO D-9198 – RETORNO – REABERTURA DE PERÍODO DE APURAÇÃO** 

### **4.6.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-9198 – R**<br> <br>|**etorno – Reabert**<br>|**ura de Período de Apuração – Estrutura**<br>|**Hierárqu**<br>|**ica do Event**<br>|**o (Resumo)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoReabert|DeRE|Retorno – Reabertura de Período de<br>Apuração.|1-1|id|O|
|3|ideContrib|evtRetornoReabert|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoReabert|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoReabert|Informações de processamento dos eventos.|1-1|-|O|
|3|infoEvento|evtRetornoReabert|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>referência das informações do evento.|1-1|perApur|O|



**Página 101 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.6.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-9198 – R**|**etorno – Reabertur**|**a de**|**Perío**|**do de**|**Apuração**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>**De**|**c**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-<br>-|<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoReabert|DeRE|G|-|1-1|-<br>-|<br>Retorno – Reabertura de Período de Apuração.|
|3<br>id|evtRetornoReabert|A|C|1-1|42<br>-|<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoReabert|G|-|1-1|-<br>-|<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8<br>-|<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoReabert|G|-|1-1|-<br>-|<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1<br>-|<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7<br>-|<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-<br>-|<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6<br>-|<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048<br>-|<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1<br>-|<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048<br>-|<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoReabert|G|-|1-1|-<br>-|<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31<br>-|<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16seqEvento|infoRecEv|E|N|0-1|2<br>-|<br>Número sequencial de identificação que indica a versão do evento<br>processado na base de dados.<br>**Nota:**O controle sequencial é ininterrupto para a mesma chave de<br>identificação. Como o evento D-1198 admite apenas a operação de<br>Inclusão ({tpOper} = [1]), as versões subsequentes ({seqEvento} maior<br>que [00]) decorrem obrigatoriamente de novas transmissões de<br>Reabertura efetuadas após o período ter sido novamente encerrado<br>por um evento de Fechamento Mensal (D-1199).<br>**Exemplo:**<br>1º Envio (Inclusão): seqEvento = [00];<br>2º Envio (Nova Inclusão): seqEvento = [01];<br>3º Envio (Nova Inclusão): seqEvento = [02].<br>**Valores válidos:**<br>**00**– Evento Original (atribuído exclusivamente no processamento com<br>sucesso da primeira Inclusão ({tpOper} = [1]) para uma chave inédita<br>no banco de dados);<br>**01-99**– Evento Versionado (atribuído de forma sucessiva e<br>cronológica ao processar novas Inclusões ({tpOper} = [1])).|
|17protocoloLote|infoRecEv|E|C|0-1|28<br>-|<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|18dhRecepcao|infoRecEv|E|D|1-1|25-33<br>-|<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|



**Página 102 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9198 – R**|**etorno – Reabertur**|**a de**|**Perío**|**do de**|**Apuraç**|**ão – Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|19|dhProcess|infoRecEv|E|D|1-1|25-33|-<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|20|tpEv|infoRecEv|E|C|1-1|6|-<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-1198.|
|21|hash|infoRecEv|E|C|1-1|44|-<br>Hashcode do arquivo processado.|
|22|infoEvento|evtRetornoReabert|G|-|0-1|-|-<br>Informações do evento processado.|
|23|idePeriodo|infoEvento|G|-|1-1|-|-<br>Grupo de identificação do período de referência das informações do<br>evento.|
|24|perApur|idePeriodo|E|D|1-1|7|-<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Cálculo:**<br>Valor do campo {perApur} informado no evento de origem.|



**Página 103 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.7 EVENTO D-9199 – RETORNO TOTALIZADOR – FECHAMENTO DE EVENTOS MENSAIS** 

### **4.7.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-9199**|**– Retorno Totaliza**|**dor – Fechamento Mensal – Estrutura H**|**ierárqui**|**ca do Evento (**|**Resumo)**|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoMensal|DeRE|Retorno Totalizador – Fechamento Mensal.|1-1|id|O|
|3|ideContrib|evtRetornoMensal|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoMensal|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoMensal|Informações de processamento dos<br>eventos.|1-1|-|O|
|3|infoEvento|evtRetornoMensal|Informações do evento processado.|0-1|-|O (se {cdRetorno} = [1]);<br>N (nos demais casos).|
|4|idePeriodo|infoEvento|Grupo de identificação do período de<br>referência das informações do evento.|1-1|perApur|O|
|4|infoAdic|infoEvento|Grupo destinado ao retorno de informações<br>complementares relativas ao evento<br>processado.|1-1|-|O|
|4|infoTotFinanceiro|infoEvento|Informações relativas a totalizadores dos<br>Serviços Financeiros.|0-1|-|OC|
|5|detBC|infoTotFinanceiro|Detalhamento da base de cálculo.|1-100|-|O|
|6|gCoeficientes|detBC|Grupo de detalhamento dos coeficientes de<br>rateio e de reversão calculados pelo sistema<br>para a formação desta base de cálculo.|0-1|-|OC|
|6|gBCIBS|detBC|Detalhamento da base de cálculo do IBS.|1-1|-|O|
|6|gBCCBS|detBC|Detalhamento da base de cálculo da CBS.|1-1|-|O|
|6|infoBCN|detBC|Grupo de informações de bases de cálculo<br>negativas.|0-1|-|OC|
|7|gBCNIBS|infoBCN|Grupo de detalhamento de bases de cálculo<br>negativas de IBS.|1-1|codBCNRaiz|O|
|8|detBCN|gBCNIBS|Detalhamento da composição do saldo por<br>período de origem.|0-100|codBCN|OC|
|7|gBCNCBS|infoBCN|Grupo de detalhamento de bases de cálculo<br>negativas de CBS.|1-1|codBCNRaiz|O|
|8|detBCN|gBCNCBS|Detalhamento da composição do saldo por<br>período de origem.|0-100|codBCN|OC|
|5|totalTributos|infoTotFinanceiro|Totalização dos tributos do Regime<br>Específico de Serviços Financeiros.|1-1|-|O|
|4|infoTotSaude|infoEvento|Informações relativas a totalizadores dos<br>Planos de Assistência à Saúde.|0-1|-|OC|
|5|detBC|infoTotSaude|Detalhamento da base de cálculo.|1-100|-|O|
|6|gBCIBS|detBC|Detalhamento da base de cálculo do IBS.|1-1|-|O|
|6|gBCCBS|detBC|Detalhamento da base de cálculo da CBS.|1-1|-|O|
|6|infoBCN|detBC|Grupo de informações de bases de cálculo<br>negativas.|0-1|-|OC|
|7|gBCNIBS|infoBCN|Grupo de detalhamento de bases de cálculo<br>negativas de IBS.|1-1|codBCNRaiz|O|



**Página 104 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Estrutura Hierárquica do Evento (Resumo)** 

|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|---|---|---|---|---|---|---|
|8|detBCN|gBCNIBS|Detalhamento da composição do saldo por<br>período de origem.|0-100|codBCN|OC|
|7|gBCNCBS|infoBCN|Grupo de detalhamento de bases de cálculo<br>negativas de CBS.|1-1|codBCNRaiz|O|
|8|detBCN|gBCNCBS|Detalhamento da composição do saldo por<br>período de origem.|0-100|codBCN|OC|
|5|totalTributos|infoTotSaude|Totalização dos tributos do Regime<br>Específico de Planos de Assistência à Saúde.|1-1|-|O|
|4|infoTotProg|infoEvento|Informações relativas a totalizadores dos<br>Concursos de Prognósticos.|0-1|-|OC|
|5|detBC|infoTotProg|Detalhamento da base de cálculo.|1-100|-|O|
|6|gCoeficientes|detBC|Grupo de detalhamento dos coeficientes de<br>rateio e de reversão calculados pelo sistema<br>para a formação desta base de cálculo.|0-1|-|OC|
|6|gBCIBS|detBC|Detalhamento da base de cálculo do IBS.|1-1|-|O|
|6|gBCCBS|detBC|Detalhamento da base de cálculo da CBS.|1-1|-|O|
|6|gBCIS|detBC|Detalhamento da base de cálculo do IS.|1-1|-|O|
|6|infoBCN|detBC|Grupo de informações de bases de cálculo<br>negativas.|0-1|-|OC|
|7|gBCNIBS|infoBCN|Grupo de detalhamento de bases de cálculo<br>negativas de IBS.|1-1|codBCNRaiz|O|
|8|detBCN|gBCNIBS|Detalhamento da composição do saldo por<br>período de origem.|0-100|codBCN|OC|
|7|gBCNCBS|infoBCN|Grupo de detalhamento de bases de cálculo<br>negativas de CBS.|1-1|codBCNRaiz|O|
|8|detBCN|gBCNCBS|Detalhamento da composição do saldo por<br>período de origem.|0-100|codBCN|OC|
|5|totalTributos|infoTotProg|Totalização dos tributos do Regime<br>Específico de Concursos de Prognósticos.|1-1|-|O|
|4|totalTributosGeral|infoEvento|Totalização dos tributos do declarante.|1-1|-|O|



**Página 105 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.7.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-9199 –**|**Retorno Totalizado**|**r – Fe**|**cham**|**ento**|**Mensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|<br>**Tam**<br>|**Dec**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-|-<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoMensal|DeRE|G|-|1-1|-|-<br>Retorno Totalizador – Fechamento Mensal.|
|3<br>id|evtRetornoMensal|A|C|1-1|42|-<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoMensal|G|-|1-1|-|-<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8|-<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoMensal|G|-|1-1|-|-<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1|-<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7|-<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-|-<br>Informações de ocorrências registradas.|
|10<br>codigo|ocorrencias|E|C|1-1|1-6|-<br>Código numérico da ocorrência.|
|11<br>descricao|ocorrencias|E|C|1-1|1-2048|-<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12<br>tipo|ocorrencias|E|N|1-1|1|-<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13<br>localizacao|ocorrencias|E|C|0-1|1-2048|-<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoMensal|G|-|1-1|-|-<br>Informações de processamento dos eventos.|
|15<br>nrRecibo|infoRecEv|E|C|0-1|31|-<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16<br>seqEvento|infoRecEv|E|N|0-1|2|-<br>Número sequencial de identificação que indica a versão do evento<br>processado na base de dados.<br>**Nota:**O controle sequencial é ininterrupto para a mesma chave de<br>identificação. Como o evento D-1199 admite apenas a operação de<br>Inclusão ({tpOper} = [1]), as versões subsequentes ({seqEvento} maior<br>que [00]) decorrem obrigatoriamente de novas transmissões de<br>Fechamento efetuadas após o período ter sido reaberto por um<br>evento de Reabertura de Período de Apuração (D-1198).<br>**Exemplo:**<br>1º Envio (Inclusão): seqEvento = [00];<br>2º Envio (Nova Inclusão): seqEvento = [01];<br>3º Envio (Nova Inclusão): seqEvento = [02].<br>**Valores válidos:**<br>**00**– Evento Original (atribuído exclusivamente no processamento<br>com sucesso da primeira Inclusão ({tpOper} = [1]) para uma chave<br>inédita no banco de dados);<br>**01-99**– Evento Versionado (atribuído de forma sucessiva e<br>cronológica ao processar novas Inclusões ({tpOper} = [1])).|
|17<br>protocoloLote|infoRecEv|E|C|0-1|28|-<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|18<br>dhRecepcao|infoRecEv|E|D|1-1|25-33|-<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|



**Página 106 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|**Tam**<br>|**De**|**c**<br>**Descrição**|
|---|---|---|---|---|---|---|---|---|
|19|dhProcess|infoRecEv|E|D|1-1|25-33|-|<br>Data e hora do início do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|20|tpEv|infoRecEv|E|C|1-1|6|-|<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-1199.|
|21|hash|infoRecEv|E|C|1-1|44|-|<br>Hashcode do arquivo processado.|
|22|infoEvento|evtRetornoMensal|G|-|0-1|-|-|<br>Informações do evento processado.|
|23|idePeriodo|infoEvento|G|-|1-1|-|-|<br>Grupo de identificação do período de referência das informações do<br>evento.|
|24|perApur|idePeriodo|E|D|1-1|7|-|<br>Período de apuração, sendo o ano e mês da competência da<br>declaração.<br>**Máscara:**AAAA-MM<br>**Cálculo:**<br>Valor do campo {perApur} informado no evento de origem.|
|25|infoAdic|infoEvento|G|-|1-1|-|-|<br>Grupo destinado ao retorno de informações complementares<br>relativas ao evento processado.|
|26|nrReciboBalancete|infoAdic|E|C|1-1|31|-|<br>Número do recibo do evento D-1101 (Balancete Mensal) utilizado<br>para a totalização deste processamento.<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|27|nrReciboAplicFin|infoAdic|E|C|0-1|31|-|<br>Número do recibo do evento D-1106 (Detalhamento de Aplicações<br>Financeiras) utilizado para a totalização deste processamento.<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|28|nrReciboRelDedu|infoAdic|E|C|0-99|31|-|<br>Número do recibo do evento D-1121 (Relação de Deduções<br>Utilizadas na Apuração) utilizado para a totalização deste<br>processamento.<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|29|nrReciboOperTitPub|infoAdic|E|C|0-1|31|-|<br>Número do recibo do evento D-2101 (Operações com Título de<br>Oferta Pública) utilizado para a totalização deste processamento.<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|30|infoTotFinanceiro|infoEvento|G|-|0-1|-|-|<br>Informações relativas a totalizadores dos Serviços Financeiros.|
|31|detBC|infoTotFinanceiro|G|-|1-100|-|-|<br>Detalhamento da base de cálculo.|
|32|codBC|detBC|E|C|1-1|4|-|<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].|
|33|xDetBC|detBC|E|C|1-1|1-1024|-|<br>Descrição da base de cálculo.|
|34|gCoeficientes|detBC|G|-|0-1|-|-|<br>Grupo de detalhamento dos coeficientes de rateio e de reversão<br>calculadospelo sistemapara a formação desta base de cálculo.|
|35|pRateioDespCapt|gCoeficientes|E|N|0-1|10-12|8|<br>Coeficiente utilizado para ratear as despesas de captação de recursos<br>entre as atividades financeiras que permitem essa dedução.|
|36|pRevDedAtoCoop|gCoeficientes|E|N|0-1|10-12|8|<br>Coeficiente utilizado para reverter as deduções de despesas<br>vinculadas a receitas decorrentes de atos cooperados (sujeitas à<br>alíquota zero).|
|37|pExportIaV|gCoeficientes|E|N|0-1|10-12|8|<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas nos incisos I a V do<br>caput do art. 182 da LC 214/2025.|
|38|pExportVI|gCoeficientes|E|N|0-1|10-12|8|<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas no inciso VI do<br>caput do art. 182 da LC 214/2025.|
|39|pExportIX|gCoeficientes|E|N|0-1|10-12|8|<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas no inciso IX do<br>caput do art. 182 da LC 214/2025.|



**Página 107 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|<br>**Tam**<br>|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|40|pExportXI|gCoeficientes|E|N|0-1|10-12|8<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas no inciso XI do<br>caput do art. 182 da LC 214/2025.|
|41|pExportXIII|gCoeficientes|E|N|0-1|10-12|8<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas no inciso XIII do<br>caput do art. 182 da LC 214/2025.|
|42|pExportXIV|gCoeficientes|E|N|0-1|10-12|8<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas no inciso XIV do<br>caput do art. 182 da LC 214/2025.|
|43|memoriaCalculo|detBC|E|C|1-1|1-4096|-<br>Descrição textual dos valores intermediários usados no cálculo.<br>Todos os valores apresentados serão arredondados para oito casas<br>decimais.|
|44|gBCIBS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo do IBS.|
|45|vBCIBS|gBCIBS|E|N|1-1|4-18|2<br>Valor da base de cálculo do IBS.<br>**Cálculo:**<br>SE {tabCodBC.vBCIBS} >= [0.00],<br>ENTÃO {vBCIBS} = {tabCodBC.vBCIBS},<br>SENÃO {vBCIBS} = [0.00]|
|46|vBCNIBS|gBCIBS|E|N|0-1|4-18|2<br>Valor absoluto do prejuízo (base de cálculo negativa do IBS) gerado<br>na apuração do período corrente.<br>**Preenchimento:**Este campo é informado exclusivamente caso o<br>resultado do cálculo resulte em uma base de cálculo negativa<br>({tabCodBC.vBCIBS} < [0.00]). O valor é apresentado em módulo.<br>**Cálculo:**<br>{vBCNIBS} = {tabCodBC.vBCIBS} * (-1)|
|47|vDedBCN|gBCIBS|E|N|0-1|4-18|2<br>Valor da base de cálculo negativa relativa a período de apuração<br>anterior a deduzir nesta competência.<br>**Preenchimento:**<br>1. SE o grupo {D-1199.gUtilizBCN} não for informado, OU SE o campo<br>{D-1199.usarBCNAcum} correspondente for igual a [0] (Opção por<br>NÃO efetuar o aproveitamento de bases negativas neste período),<br>ENTÃO o campo {vDedBCN} não será retornado;<br>2. SE o campo {D-1199.usarBCNAcum} for igual a [1], o<br>preenchimento observará o método indicado pelo declarante:<br>a. Método PEPS Automático ({D-1199.metodoAproveit} = [0]): O<br>valor de {vDedBCN} será calculado automaticamente pelo sistema<br>DeRE, consumindo os saldos de bases negativas mais antigos da<br>{codBCNRaiz} correspondente até o limite da base de cálculo positiva<br>do mês corrente;<br>b. Método Manual ({D-1199.metodoAproveit} = [1]): O valor de<br>{vDedBCN} corresponderá ao somatório dos valores informados no<br>campo {D-1199.vUsarBCN} dentro do grupo {D-1199.detBCNeg} para<br>os códigos {D-1199.codBCN} correspondentes.<br>**Validação:**O valor de {vDedBCN} deve ser menor ou igual a {vBCIBS}.<br>Caso ultrapasse, o valor de {vDedBCN} fica limitado ao valor da<br>{vBCIBS}, ajustando o valor utilizado na tabela de bases de cálculo<br>negativas.|
|48|vBCApurIBS|gBCIBS|E|N|1-1|4-18|2<br>Base de cálculo efetiva do IBS após compensação de saldos de base<br>de cálculo negativas de períodos anteriores.<br>**Cálculo:**<br>SE {vBCIBS} = [0.00],<br>ENTÃO {vBCApurIBS} = [0.00],<br>SENÃO {vBCApurIBS} = {vBCIBS} - {vDedBCN}|
|49|pIBSMun|gBCIBS|E|N|1-1|8-10|6<br>Alíquota do IBS municipal.<br>Sem símbolo (%).|
|50|vIBSMun|gBCIBS|E|N|1-1|4-18|2<br>Valor do IBS municipal.<br>**Cálculo:**<br>{vBCApurIBS} * ({pIBSMun} / 100)|



**Página 108 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-9199 –**|**Retorno Totalizad**|**or – Fe**|**cham**|**ento**|**Mensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|51<br>pIBSUF|gBCIBS|E|N|1-1|8-10|6<br>Alíquota do IBS estadual.<br>Sem símbolo (%).|
|52<br>vIBSUF|gBCIBS|E|N|1-1|4-18|2<br>Valor do IBS estadual.<br>**Cálculo:**<br>{vBCApurIBS} * ({pIBSUF} / 100)|
|53<br>pIBS|gBCIBS|E|N|1-1|8-10|6<br>Soma das alíquotas {pIBSMun} e {pIBSUF}.<br>Sem símbolo (%).|
|54<br>vIBSTot|gBCIBS|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>{vIBSMun} + {vIBSUF}|
|55<br>gBCCBS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo da CBS.|
|56<br>vBCCBS|gBCCBS|E|N|1-1|4-18|2<br>Valor da base de cálculo da CBS.<br>**Cálculo:**<br>SE {tabCodBC.vBCCBS} >= [0.00],<br>ENTÃO {vBCCBS} = {tabCodBC.vBCCBS},<br>SENÃO {vBCCBS} = [0.00]|
|57<br>vBCNCBS|gBCCBS|E|N|0-1|4-18|2<br>Valor absoluto da base de cálculo negativa da CBS gerado na<br>apuração do período corrente.<br>**Preenchimento:**Este campo é informado exclusivamente caso o<br>resultado do cálculo resulte em uma base de cálculo negativa<br>({tabCodBC.vBCCBS} < [0.00]). O valor é apresentado em módulo.<br>**Cálculo:**<br>{vBCNCBS} = {tabCodBC.vBCCBS} * (-1)|
|58<br>vDedBCN|gBCCBS|E|N|0-1|4-18|2<br>Valor da base de cálculo negativa relativa a período de apuração<br>anterior a deduzir nesta competência.<br>**Preenchimento:**<br>1. SE o grupo {D-1199.gUtilizBCN} não for informado, OU SE o campo<br>{D-1199.usarBCNAcum} correspondente for igual a [0] (Opção por<br>NÃO efetuar o aproveitamento de bases negativas neste período),<br>ENTÃO o campo {vDedBCN} não será retornado;<br>2. SE o campo {D-1199.usarBCNAcum} for igual a [1], o<br>preenchimento observará o método indicado pelo declarante:<br>a. Método PEPS Automático ({D-1199.metodoAproveit} = [0]): O<br>valor de {vDedBCN} será calculado automaticamente pelo sistema<br>DeRE, consumindo os saldos de bases negativas mais antigos da<br>{codBCNRaiz} correspondente até o limite da base de cálculo positiva<br>do mês corrente;<br>b. Método Manual ({D-1199.metodoAproveit} = [1]): O valor de<br>{vDedBCN} corresponderá ao somatório dos valores informados no<br>campo {D-1199.vUsarBCN} dentro do grupo {D-1199.detBCNeg} para<br>os códigos {D-1199.codBCN} correspondentes.<br>**Validação:**O valor de {vDedBCN} deve ser menor ou igual a<br>{vBCCBS}. Caso ultrapasse, o valor de {vDedBCN} fica limitado ao<br>valor da {vBCCBS}, ajustando o valor utilizado na tabela de bases de<br>cálculo negativas.|
|59<br>vBCApurCBS|gBCCBS|E|N|1-1|4-18|2<br>Base de cálculo efetiva da CBS após compensação de saldos de base<br>de cálculo negativas de períodos anteriores.<br>**Cálculo:**<br>SE {vBCCBS} = [0.00],<br>ENTÃO {vBCApurCBS} = [0.00],<br>SENÃO {vBCApurCBS} = {vBCCBS} - {vDedBCN}|
|60<br>pCBS|gBCCBS|E|N|1-1|8-10|6<br>Alíquota da CBS.<br>Sem símbolo (%).|
|61<br>vCBS|gBCCBS|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vBCApurCBS} * ({pCBS} / 100)|



**Página 109 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9199 –**|**Retorno Totalizad**|**or – Fe**|**cham**|**ento**|**Mensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|62|infoBCN|detBC|G|-|0-1|-|-<br>Grupo de informações de bases de cálculo negativas.|
|63|gBCNIBS|infoBCN|G|-|1-1|-|-<br>Grupo de detalhamento de bases de cálculo negativas de IBS.|
|64|codBCNRaiz|gBCNIBS|E|C|1-1|5|-<br>Código identificador raiz da base de cálculo negativa, conforme<br>[[Tabela12– Códigos de Bases de Cálculo]].|
|65|vSaldoAnt|gBCNIBS|E|N|1-1|4-18|2<br>Valor do saldo anterior.<br>Montante acumulado da base de cálculo negativa disponível no<br>início do período de apuração atual, antes das compensações do<br>mês.|
|66|vUtilPer|gBCNIBS|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizado como dedução da base<br>de cálculo do período de apuração atual.<br>**Cálculo:**<br>Igual a {vDedBCN}|
|67|vSaldoFinal|gBCNIBS|E|N|1-1|4-18|2<br>Valor do saldo remanescente de base de cálculo negativa a ser<br>transportado para o próximo período de apuração.<br>**Cálculo:**<br>{vSaldoAnt} - {vUtilPer} + {vBCNIBS} (se houver)|
|68|qtdOrigens|gBCNIBS|E|N|1-1|1-2|-<br>Quantidade de períodos de origem. Indica quantos períodos<br>distintos compõem o saldo atual.<br>**Exemplo:**Se o saldo é composto por bases negativas de jan/25 e<br>mar/26, {qtdOrigens} = [2].|
|69|detBCN|gBCNIBS|G|-|0-100|-|-<br>Detalhamento da composição do saldo por período de origem.|
|70|codBCN|detBCN|E|C|1-1|13|-<br>Código identificador da base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|71|perOrigem|detBCN|E|D|1-1|7|-<br>Período de origem da base de cálculo negativa.<br>**Máscara:**AAAA-MM|
|72|vOrigemIni|detBCN|E|N|1-1|4-18|2<br>Valor inicial da base de cálculo negativa no respectivo período de<br>origem.|
|73|vSaldoAnt|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa disponível para uso neste período<br>de apuração.<br>**Cálculo:**<br>SE {perOrigem} = {perApur},<br>ENTÃO {vSaldoAnt} = [0.00],<br>SENÃO {vSaldoAnt} = {vSaldoFinal} do {perApur} imediatamente<br>anterior.|
|74|vUtilPer|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizada neste período de<br>apuração.|
|75|vSaldoFinal|detBCN|E|N|1-1|4-18|2<br>Saldo remanescente desta base de cálculo negativa para os próximos<br>períodos de apuração.|
|76|gBCNCBS|infoBCN|G|-|1-1|-|-<br>Grupo de detalhamento de bases de cálculo negativas de CBS.|
|77|codBCNRaiz|gBCNCBS|E|C|1-1|5|-<br>Código identificador raiz da base de cálculo negativa, conforme<br>[[Tabela12– Códigos de Bases de Cálculo]].|
|78|vSaldoAnt|gBCNCBS|E|N|1-1|4-18|2<br>Valor do saldo anterior.<br>Montante acumulado da base de cálculo negativa disponível no<br>início do período de apuração atual, antes das compensações do<br>mês.|
|79|vUtilPer|gBCNCBS|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizado como dedução da base<br>de cálculo do período de apuração atual.<br>**Cálculo:**<br>Igual a {vDedBCN}|
|80|vSaldoFinal|gBCNCBS|E|N|1-1|4-18|2<br>Valor do saldo remanescente de base de cálculo negativa a ser<br>transportado para o próximo período de apuração.<br>**Cálculo:**<br>{vSaldoAnt} - {vUtilPer} + {vBCNCBS} (se houver)|



**Página 110 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9199 –**|**Retorno Totalizado**|**r – Fe**|**cham**|**ento M**|**ensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**<br>|**Dec**<br>**Descrição**|
|81|qtdOrigens|gBCNCBS|E|N|1-1|1-2|-<br>Quantidade de períodos de origem. Indica quantos períodos<br>distintos compõem o saldo atual.<br>**Exemplo:**Se o saldo é composto por bases negativas de jan/25 e<br>mar/26, {qtdOrigens} = [2].|
|82|detBCN|gBCNCBS|G|-|0-100|-|-<br>Detalhamento da composição do saldoporperíodo de origem.|
|83|codBCN|detBCN|E|C|1-1|13|-<br>Código identificador da base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|84|perOrigem|detBCN|E|D|1-1|7|-<br>Período de origem da base de cálculo negativa.<br>**Máscara:**AAAA-MM|
|85|vOrigemIni|detBCN|E|N|1-1|4-18|2<br>Valor inicial da base de cálculo negativa no respectivo período de<br>origem.|
|86|vSaldoAnt|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa disponível para uso neste período<br>de apuração.<br>**Cálculo:**<br>SE {perOrigem} = {perApur},<br>ENTÃO {vSaldoAnt} = [0.00],<br>SENÃO {vSaldoAnt} = {vSaldoFinal} do {perApur} imediatamente<br>anterior.|
|87|vUtilPer|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizada neste período de<br>apuração.|
|88|vSaldoFinal|detBCN|E|N|1-1|4-18|2<br>Saldo remanescente desta base de cálculo negativa para os próximos<br>períodos de apuração.|
|89|totalTributos|infoTotFinanceiro|G|-|1-1|-|-<br>Totalização dos tributos do Regime Específico de Serviços<br>Financeiros.|
|90|vIBSMun|totalTributos|E|N|1-1|4-18|2<br>Valor do IBS municipal.<br>**Cálculo:**<br>SOMA({infoTotFinanceiro.detBC.vIBSMun})|
|91|vIBSUF|totalTributos|E|N|1-1|4-18|2<br>Valor do IBS estadual.<br>**Cálculo:**<br>SOMA({infoTotFinanceiro.detBC.vIBSUF})|
|92|vIBSTot|totalTributos|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>{totalTributos.vIBSMun} + {totalTributos.vIBSUF}|
|93|vCBS|totalTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>SOMA({infoTotFinanceiro.detBC.vCBS})|
|94|infoTotSaude|infoEvento|G|-|0-1|-|-<br>Informações relativas a totalizadores dos Planos de Assistência à<br>Saúde.|
|95|detBC|infoTotSaude|G|-|1-100|-|-<br>Detalhamento da base de cálculo.|
|96|codBC|detBC|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].|
|97|xDetBC|detBC|E|C|1-1|1-1024|-<br>Descrição da base de cálculo.|
|98|memoriaCalculo|detBC|E|C|1-1|1-4096|-<br>Descrição textual dos valores intermediários usados no cálculo.<br>Todos os valores apresentados serão arredondados para oito casas<br>decimais.|



**Página 111 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|99|gBCIBS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo do IBS.|
|100|vBCIBS|gBCIBS|E|N|1-1|4-18|2<br>Valor da base de cálculo do IBS.<br>**Cálculo:**<br>SE {tabCodBC.vBCIBS} >= [0.00],<br>ENTÃO {vBCIBS} = {tabCodBC.vBCIBS},<br>SENÃO {vBCIBS} = [0.00]|
|101|vBCNIBS|gBCIBS|E|N|0-1|4-18|2<br>Valor absoluto do prejuízo (base de cálculo negativa do IBS) gerado<br>na apuração do período corrente.<br>**Preenchimento:**Este campo é informado exclusivamente caso o<br>resultado do cálculo resulte em uma base de cálculo negativa<br>({tabCodBC.vBCIBS} < [0.00]). O valor é apresentado em módulo.<br>**Cálculo:**<br>{vBCNIBS} = {tabCodBC.vBCIBS} * (-1)|
|102|vDedBCN|gBCIBS|E|N|0-1|4-18|2<br>Valor da base de cálculo negativa relativa a período de apuração<br>anterior a deduzir nesta competência.<br>**Preenchimento:**<br>1. SE o grupo {D-1199.gUtilizBCN} não for informado, OU SE o campo<br>{D-1199.usarBCNAcum} correspondente for igual a [0] (Opção por<br>NÃO efetuar o aproveitamento de bases negativas neste período),<br>ENTÃO o campo {vDedBCN} não será retornado;<br>2. SE o campo {D-1199.usarBCNAcum} for igual a [1], o<br>preenchimento observará o método indicado pelo declarante:<br>a. Método PEPS Automático ({D-1199.metodoAproveit} = [0]): O<br>valor de {vDedBCN} será calculado automaticamente pelo sistema<br>DeRE, consumindo os saldos de bases negativas mais antigos da<br>{codBCNRaiz} correspondente até o limite da base de cálculo positiva<br>do mês corrente;<br>b. Método Manual ({D-1199.metodoAproveit} = [1]): O valor de<br>{vDedBCN} corresponderá ao somatório dos valores informados no<br>campo {D-1199.vUsarBCN} dentro do grupo {D-1199.detBCNeg} para<br>os códigos {D-1199.codBCN} correspondentes.<br>**Validação:**O valor de {vDedBCN} deve ser menor ou igual a {vBCIBS}.<br>Caso ultrapasse, o valor de {vDedBCN} fica limitado ao valor da<br>{vBCIBS}, ajustando o valor utilizado na tabela de bases de cálculo<br>negativas.|
|103|vBCApurIBS|gBCIBS|E|N|1-1|4-18|2<br>Base de cálculo efetiva do IBS após compensação de saldos de base<br>de cálculo negativas de períodos anteriores.<br>**Cálculo:**<br>SE {vBCIBS} = [0.00],<br>ENTÃO {vBCApurIBS} = [0.00],<br>SENÃO {vBCApurIBS} = {vBCIBS} - {vDedBCN}|
|104|pIBSMun|gBCIBS|E|N|1-1|8-10|6<br>Alíquota do IBS municipal.<br>Sem símbolo (%).|
|105|vIBSMun|gBCIBS|E|N|1-1|4-18|2<br>Valor do IBS municipal.<br>**Cálculo:**<br>{vBCApurIBS} * ({pIBSMun} / 100)|
|106|pIBSUF|gBCIBS|E|N|1-1|8-10|6<br>Alíquota do IBS estadual.<br>Sem símbolo (%).|
|107|vIBSUF|gBCIBS|E|N|1-1|4-18|2<br>Valor do IBS estadual.<br>**Cálculo:**<br>{vBCApurIBS} * ({pIBSUF} / 100)|
|108|pIBS|gBCIBS|E|N|1-1|8-10|6<br>Soma das alíquotas {pIBSMun} e {pIBSUF}.<br>Sem símbolo (%).|
|109|vIBSTot|gBCIBS|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>{vIBSMun} + {vIBSUF}|



**Página 112 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|110|gBCCBS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo da CBS.|
|111|vBCCBS|gBCCBS|E|N|1-1|4-18|2<br>Valor da base de cálculo da CBS.<br>**Cálculo:**<br>SE {tabCodBC.vBCCBS} >= [0.00],<br>ENTÃO {vBCCBS} = {tabCodBC.vBCCBS},<br>SENÃO {vBCCBS} = [0.00]|
|112|vBCNCBS|gBCCBS|E|N|0-1|4-18|2<br>Valor absoluto da base de cálculo negativa da CBS gerado na<br>apuração do período corrente.<br>**Preenchimento:**Este campo é informado exclusivamente caso o<br>resultado do cálculo resulte em uma base de cálculo negativa<br>({tabCodBC.vBCCBS} < [0.00]). O valor é apresentado em módulo.<br>**Cálculo:**<br>{vBCNCBS} = {tabCodBC.vBCCBS} * (-1)|
|113|vDedBCN|gBCCBS|E|N|0-1|4-18|2<br>Valor da base de cálculo negativa relativa a período de apuração<br>anterior a deduzir nesta competência.<br>**Preenchimento:**<br>1. SE o grupo {D-1199.gUtilizBCN} não for informado, OU SE o campo<br>{D-1199.usarBCNAcum} correspondente for igual a [0] (Opção por<br>NÃO efetuar o aproveitamento de bases negativas neste período),<br>ENTÃO o campo {vDedBCN} não será retornado;<br>2. SE o campo {D-1199.usarBCNAcum} for igual a [1], o<br>preenchimento observará o método indicado pelo declarante:<br>a. Método PEPS Automático ({D-1199.metodoAproveit} = [0]): O<br>valor de {vDedBCN} será calculado automaticamente pelo sistema<br>DeRE, consumindo os saldos de bases negativas mais antigos da<br>{codBCNRaiz} correspondente até o limite da base de cálculo positiva<br>do mês corrente;<br>b. Método Manual ({D-1199.metodoAproveit} = [1]): O valor de<br>{vDedBCN} corresponderá ao somatório dos valores informados no<br>campo {D-1199.vUsarBCN} dentro do grupo {D-1199.detBCNeg} para<br>os códigos {D-1199.codBCN} correspondentes.<br>**Validação:**O valor de {vDedBCN} deve ser menor ou igual a<br>{vBCCBS}. Caso ultrapasse, o valor de {vDedBCN} fica limitado ao<br>valor da {vBCCBS}, ajustando o valor utilizado na tabela de bases de<br>cálculo negativas.|
|114|vBCApurCBS|gBCCBS|E|N|1-1|4-18|2<br>Base de cálculo efetiva da CBS após compensação de saldos de base<br>de cálculo negativas de períodos anteriores.<br>**Cálculo:**<br>SE {vBCCBS} = [0.00],<br>ENTÃO {vBCApurCBS} = [0.00],<br>SENÃO {vBCApurCBS} = {vBCCBS} - {vDedBCN}|
|115|pCBS|gBCCBS|E|N|1-1|8-10|6<br>Alíquota da CBS.<br>Sem símbolo (%).|
|116|vCBS|gBCCBS|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vBCApurCBS} * ({pCBS} / 100)|
|117|infoBCN|detBC|G|-|0-1|-|-<br>Grupo de informações de bases de cálculo negativas.|
|118|gBCNIBS|infoBCN|G|-|1-1|-|-<br>Grupo de detalhamento de bases de cálculo negativas de IBS.|
|119|codBCNRaiz|gBCNIBS|E|C|1-1|5|-<br>Código identificador raiz da base de cálculo negativa, conforme<br>[[Tabela12– Códigos de Bases de Cálculo]].|
|120|vSaldoAnt|gBCNIBS|E|N|1-1|4-18|2<br>Valor do saldo anterior.<br>Montante acumulado da base de cálculo negativa disponível no<br>início do período de apuração atual, antes das compensações do<br>mês.|
|121|vUtilPer|gBCNIBS|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizado como dedução da base<br>de cálculo do período de apuração atual.<br>**Cálculo:**<br>Igual a {vDedBCN}|



**Página 113 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9199 –**|**Retorno Totalizad**|**or – F**|**echam**|**ento M**|**ensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|<br>**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|122|vSaldoFinal|gBCNIBS|E|N|1-1|4-18|2<br>Valor do saldo remanescente de base de cálculo negativa a ser<br>transportado para o próximo período de apuração.<br>**Cálculo:**<br>{vSaldoAnt} - {vUtilPer} + {vBCNIBS} (se houver)|
|123|qtdOrigens|gBCNIBS|E|N|1-1|1-2|-<br>Quantidade de períodos de origem. Indica quantos períodos<br>distintos compõem o saldo atual.<br>**Exemplo:**Se o saldo é composto por bases negativas de jan/25 e<br>mar/26, {qtdOrigens} = [2].|
|124|detBCN|gBCNIBS|G|-|0-100|-|-<br>Detalhamento da composição do saldo por período de origem.|
|125|codBCN|detBCN|E|C|1-1|13|-<br>Código identificador da base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|126|perOrigem|detBCN|E|D|1-1|7|-<br>Período de origem da base de cálculo negativa.<br>**Máscara:**AAAA-MM|
|127|vOrigemIni|detBCN|E|N|1-1|4-18|2<br>Valor inicial da base de cálculo negativa no respectivo período de<br>origem.|
|128|vSaldoAnt|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa disponível para uso neste período<br>de apuração.<br>**Cálculo:**<br>SE {perOrigem} = {perApur},<br>ENTÃO {vSaldoAnt} = [0.00],<br>SENÃO {vSaldoAnt} = {vSaldoFinal} do {perApur} imediatamente<br>anterior.|
|129|vUtilPer|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizada neste período de<br>apuração.|
|130|vSaldoFinal|detBCN|E|N|1-1|4-18|2<br>Saldo remanescente desta base de cálculo negativa para os próximos<br>períodos de apuração.|
|131|gBCNCBS|infoBCN|G|-|1-1|-|-<br>Grupo de detalhamento de bases de cálculo negativas de CBS.|
|132|codBCNRaiz|gBCNCBS|E|C|1-1|5|-<br>Código identificador raiz da base de cálculo negativa, conforme<br>[[Tabela12– Códigos de Bases de Cálculo]].|
|133|vSaldoAnt|gBCNCBS|E|N|1-1|4-18|2<br>Valor do saldo anterior.<br>Montante acumulado da base de cálculo negativa disponível no<br>início do período de apuração atual, antes das compensações do<br>mês.|
|134|vUtilPer|gBCNCBS|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizado como dedução da base<br>de cálculo do período de apuração atual.<br>**Cálculo:**<br>Igual a {vDedBCN}|
|135|vSaldoFinal|gBCNCBS|E|N|1-1|4-18|2<br>Valor do saldo remanescente de base de cálculo negativa a ser<br>transportado para o próximo período de apuração.<br>**Cálculo:**<br>{vSaldoAnt} - {vUtilPer} + {vBCNCBS} (se houver)|
|136|qtdOrigens|gBCNCBS|E|N|1-1|1-2|-<br>Quantidade de períodos de origem. Indica quantos períodos<br>distintos compõem o saldo atual.<br>**Exemplo:**Se o saldo é composto por bases negativas de jan/25 e<br>mar/26, {qtdOrigens} = [2].|
|137|detBCN|gBCNCBS|G|-|0-100|-|-<br>Detalhamento da composição do saldo por período de origem.|
|138|codBCN|detBCN|E|C|1-1|13|-<br>Código identificador da base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|139|perOrigem|detBCN|E|D|1-1|7|-<br>Período de origem da base de cálculo negativa.<br>**Máscara:**AAAA-MM|
|140|vOrigemIni|detBCN|E|N|1-1|4-18|2<br>Valor inicial da base de cálculo negativa no respectivo período de<br>origem.|



**Página 114 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|<br>**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|141|vSaldoAnt|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa disponível para uso neste período<br>de apuração.<br>**Cálculo:**<br>SE {perOrigem} = {perApur},<br>ENTÃO {vSaldoAnt} = [0.00],<br>SENÃO {vSaldoAnt} = {vSaldoFinal} do {perApur} imediatamente<br>anterior.|
|142|vUtilPer|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizada neste período de<br>apuração.|
|143|vSaldoFinal|detBCN|E|N|1-1|4-18|2<br>Saldo remanescente desta base de cálculo negativa para os próximos<br>períodos de apuração.|
|144|totalTributos|infoTotSaude|G|-|1-1|-|-<br>Totalização dos tributos do Regime Específico de Planos de<br>Assistência à Saúde.|
|145|vIBSMun|totalTributos|E|N|1-1|4-18|2<br>Valor do IBS municipal<br>**Cálculo:**<br>SOMA({infoTotSaude.detBC.vIBSMun})|
|146|vIBSUF|totalTributos|E|N|1-1|4-18|2<br>Valor do IBS estadual.<br>**Cálculo:**<br>SOMA({infoTotSaude.detBC.vIBSUF})|
|147|vIBSTot|totalTributos|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>{totalTributos.vIBSMun} + {totalTributos.vIBSUF}|
|148|vCBS|totalTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>SOMA({infoTotSaude.detBC.vCBS})|
|149|infoTotProg|infoEvento|G|-|0-1|-|-<br>Informações relativas a totalizadores dos Concursos de Prognósticos.|
|150|detBC|infoTotProg|G|-|1-100|-|-<br>Detalhamento da base de cálculo.|
|151|codBC|detBC|E|C|1-1|4|-<br>Código identificador da base de cálculo, conforme [[Tabela12–<br>Códigos de Bases de Cálculo]].|
|152|xDetBC|detBC|E|C|1-1|1-1024|-<br>Descrição da base de cálculo.|
|153|gCoeficientes|detBC|G|-|0-1|-|-<br>Grupo de detalhamento dos coeficientes de rateio e de reversão<br>calculados pelo sistema para a formação desta base de cálculo.|
|154|pExportProg|gCoeficientes|E|N|0-1|10-12|8<br>Coeficiente aplicado para reverter deduções de despesas vinculadas<br>a receitas de exportação das atividades previstas no art. 244 da LC<br>214/2025.|
|155|memoriaCalculo|detBC|E|C|1-1|1-4096|-<br>Descrição textual dos valores intermediários usados no cálculo.<br>Todos os valores apresentados serão arredondados para oito casas<br>decimais.|
|156|gBCIBS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo do IBS.|
|157|vBCIBS|gBCIBS|E|N|1-1|4-18|2<br>Valor da base de cálculo do IBS.<br>**Cálculo:**<br>SE {tabCodBC.vBCIBS} >= [0.00],<br>ENTÃO {vBCIBS} = {tabCodBC.vBCIBS},<br>SENÃO {vBCIBS} = [0.00]|
|158|vBCNIBS|gBCIBS|E|N|0-1|4-18|2<br>Valor absoluto do prejuízo (base de cálculo negativa do IBS) gerado<br>na apuração do período corrente.<br>**Preenchimento:**Este campo é informado exclusivamente caso o<br>resultado do cálculo resulte em uma base de cálculo negativa<br>({tabCodBC.vBCIBS} < [0.00]). O valor é apresentado em módulo.<br>**Cálculo:**<br>{vBCNIBS} = {tabCodBC.vBCIBS} * (-1)|



**Página 115 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|159|vDedBCN|gBCIBS|E|N|0-1|4-18|2<br>Valor da base de cálculo negativa relativa a período de apuração<br>anterior a deduzir nesta competência.<br>**Preenchimento:**<br>1. SE o grupo {D-1199.gUtilizBCN} não for informado, OU SE o campo<br>{D-1199.usarBCNAcum} correspondente for igual a [0] (Opção por<br>NÃO efetuar o aproveitamento de bases negativas neste período),<br>ENTÃO o campo {vDedBCN} não será retornado;<br>2. SE o campo {D-1199.usarBCNAcum} for igual a [1], o<br>preenchimento observará o método indicado pelo declarante:<br>a. Método PEPS Automático ({D-1199.metodoAproveit} = [0]): O<br>valor de {vDedBCN} será calculado automaticamente pelo sistema<br>DeRE, consumindo os saldos de bases negativas mais antigos da<br>{codBCNRaiz} correspondente até o limite da base de cálculo positiva<br>do mês corrente;<br>b. Método Manual ({D-1199.metodoAproveit} = [1]): O valor de<br>{vDedBCN} corresponderá ao somatório dos valores informados no<br>campo {D-1199.vUsarBCN} dentro do grupo {D-1199.detBCNeg} para<br>os códigos {D-1199.codBCN} correspondentes.<br>**Validação:**O valor de {vDedBCN} deve ser menor ou igual a {vBCIBS}.<br>Caso ultrapasse, o valor de {vDedBCN} fica limitado ao valor da<br>{vBCIBS}, ajustando o valor utilizado na tabela de bases de cálculo<br>negativas.|
|160|vBCApurIBS|gBCIBS|E|N|1-1|4-18|2<br>Base de cálculo efetiva do IBS após compensação de saldos de base<br>de cálculo negativas de períodos anteriores.<br>**Cálculo:**<br>SE {vBCIBS} = [0.00],<br>ENTÃO {vBCApurIBS} = [0.00],<br>SENÃO {vBCApurIBS} = {vBCIBS} - {vDedBCN}|
|161|pIBSMun|gBCIBS|E|N|1-1|8-10|6<br>Alíquota do IBS municipal.<br>Sem símbolo (%).|
|162|vIBSMun|gBCIBS|E|N|1-1|4-18|2<br>Valor do IBS municipal.<br>**Cálculo:**<br>{vBCApurIBS} * ({pIBSMun} / 100)|
|163|pIBSUF|gBCIBS|E|N|1-1|8-10|6<br>Alíquota do IBS estadual.<br>Sem símbolo (%).|
|164|vIBSUF|gBCIBS|E|N|1-1|4-18|2<br>Valor do IBS estadual.<br>**Cálculo:**<br>{vBCApurIBS} * ({pIBSUF} / 100)|
|165|pIBS|gBCIBS|E|N|1-1|8-10|6<br>Soma das alíquotas {pIBSMun} e {pIBSUF}.<br>Sem símbolo (%).|
|166|vIBSTot|gBCIBS|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>{vIBSMun} + {vIBSUF}|
|167|gBCCBS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo da CBS.|
|168|vBCCBS|gBCCBS|E|N|1-1|4-18|2<br>Valor da base de cálculo da CBS.<br>**Cálculo:**<br>SE {tabCodBC.vBCCBS} >= [0.00],<br>ENTÃO {vBCCBS} = {tabCodBC.vBCCBS},<br>SENÃO {vBCCBS} = [0.00]|
|169|vBCNCBS|gBCCBS|E|N|0-1|4-18|2<br>Valor absoluto da base de cálculo negativa da CBS gerado na<br>apuração do período corrente.<br>**Preenchimento:**Este campo é informado exclusivamente caso o<br>resultado do cálculo resulte em uma base de cálculo negativa<br>({tabCodBC.vBCCBS} < [0.00]). O valor é apresentado em módulo.<br>**Cálculo:**<br>{vBCNCBS} = {tabCodBC.vBCCBS} * (-1)|



**Página 116 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**#**|**D-9199 –**<br>**Grupo/Tag**|**Retorno Totalizad**<br>**Grupo Pai**|**or – Fe**<br>**Cat**|**cham**<br>**Tipo**|**ento M**<br> <br>**Ocorr**|**ensal**<br>**Tam**|**– Especificação Técnica de Campos (Detalhamento)**<br>**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|170|vDedBCN|gBCCBS|E|N|0-1|4-18|2<br>Valor da base de cálculo negativa relativa a período de apuração<br>anterior a deduzir nesta competência.<br>**Preenchimento:**<br>1. SE o grupo {D-1199.gUtilizBCN} não for informado, OU SE o campo<br>{D-1199.usarBCNAcum} correspondente for igual a [0] (Opção por<br>NÃO efetuar o aproveitamento de bases negativas neste período),<br>ENTÃO o campo {vDedBCN} não será retornado;<br>2. SE o campo {D-1199.usarBCNAcum} for igual a [1], o<br>preenchimento observará o método indicado pelo declarante:<br>a. Método PEPS Automático ({D-1199.metodoAproveit} = [0]): O<br>valor de {vDedBCN} será calculado automaticamente pelo sistema<br>DeRE, consumindo os saldos de bases negativas mais antigos da<br>{codBCNRaiz} correspondente até o limite da base de cálculo positiva<br>do mês corrente;<br>b. Método Manual ({D-1199.metodoAproveit} = [1]): O valor de<br>{vDedBCN} corresponderá ao somatório dos valores informados no<br>campo {D-1199.vUsarBCN} dentro do grupo {D-1199.detBCNeg} para<br>os códigos {D-1199.codBCN} correspondentes.<br>**Validação:**O valor de {vDedBCN} deve ser menor ou igual a<br>{vBCCBS}. Caso ultrapasse, o valor de {vDedBCN} fica limitado ao<br>valor da {vBCCBS}, ajustando o valor utilizado na tabela de bases de<br>cálculo negativas.|
|171|vBCApurCBS|gBCCBS|E|N|1-1|4-18|2<br>Base de cálculo efetiva da CBS após compensação de saldos de base<br>de cálculo negativas de períodos anteriores.<br>**Cálculo:**<br>SE {vBCCBS} = [0.00],<br>ENTÃO {vBCApurCBS} = [0.00],<br>SENÃO {vBCApurCBS} = {vBCCBS} - {vDedBCN}|
|172|pCBS|gBCCBS|E|N|1-1|8-10|6<br>Alíquota da CBS.<br>Sem símbolo (%).|
|173|vCBS|gBCCBS|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>{vBCApurCBS} * ({pCBS} / 100)|
|174|gBCIS|detBC|G|-|1-1|-|-<br>Detalhamento da base de cálculo do IS.|
|175|vBCIS|gBCIS|E|N|1-1|4-18|2<br>Valor da base de cálculo do Imposto Seletivo (IS).<br>**Cálculo:**<br>SE {tabCodBC.vBCIS} >= [0.00],<br>ENTÃO {vBCIS} = {tabCodBC.vBCIS},<br>SENÃO {vBCIS} = [0.00]|
|176|vBCApurIS|gBCIS|E|N|1-1|4-18|2<br>Valor da base de cálculo efetiva do Imposto Seletivo.<br>**Cálculo:**<br>{vBCApurIS} = {vBCIS}|
|177|pIS|gBCIS|E|N|1-1|8-10|6<br>Alíquota do Imposto Seletivo do Regime Específico de Concursos de<br>Prognósticos.<br>Sem símbolo (%).|
|178|vIS|gBCIS|E|N|1-1|4-18|2<br>Valor do Imposto Seletivo.<br>**Cálculo:**<br>{vBCApurIS} * ({pIS} / 100)|



**Página 117 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9199 –**|**Retorno Totalizad**|**or – F**|**echam**|**ento M**|**ensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|<br>**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|179|infoBCN|detBC|G|-|0-1|-|-<br>Grupo de informações de bases de cálculo negativas.|
|180|gBCNIBS|infoBCN|G|-|1-1|-|-<br>Grupo de detalhamento de bases de cálculo negativas de IBS.|
|181|codBCNRaiz|gBCNIBS|E|C|1-1|5|-<br>Código identificador raiz da base de cálculo negativa, conforme<br>[[Tabela12– Códigos de Bases de Cálculo]].|
|182|vSaldoAnt|gBCNIBS|E|N|1-1|4-18|2<br>Valor do saldo anterior.<br>Montante acumulado da base de cálculo negativa disponível no<br>início do período de apuração atual, antes das compensações do<br>mês.|
|183|vUtilPer|gBCNIBS|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizado como dedução da base<br>de cálculo do período de apuração atual.<br>**Cálculo:**<br>Igual a {vDedBCN}|
|184|vSaldoFinal|gBCNIBS|E|N|1-1|4-18|2<br>Valor do saldo remanescente de base de cálculo negativa a ser<br>transportado para o próximo período de apuração.<br>**Cálculo:**<br>{vSaldoAnt} - {vUtilPer} + {vBCNIBS} (se houver)|
|185|qtdOrigens|gBCNIBS|E|N|1-1|1-2|-<br>Quantidade de períodos de origem. Indica quantos períodos<br>distintos compõem o saldo atual.<br>**Exemplo:**Se o saldo é composto por bases negativas de jan/25 e<br>mar/26, {qtdOrigens} = [2].|
|186|detBCN|gBCNIBS|G|-|0-100|-|-<br>Detalhamento da composição do saldo por período de origem.|
|187|codBCN|detBCN|E|C|1-1|13|-<br>Código identificador da base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|188|perOrigem|detBCN|E|D|1-1|7|-<br>Período de origem da base de cálculo negativa.<br>**Máscara:**AAAA-MM|
|189|vOrigemIni|detBCN|E|N|1-1|4-18|2<br>Valor inicial da base de cálculo negativa no respectivo período de<br>origem.|
|190|vSaldoAnt|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa disponível para uso neste período<br>de apuração.<br>**Cálculo:**<br>SE {perOrigem} = {perApur},<br>ENTÃO {vSaldoAnt} = [0.00],<br>SENÃO {vSaldoAnt} = {vSaldoFinal} do {perApur} imediatamente<br>anterior.|
|191|vUtilPer|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizada neste período de<br>apuração.|
|192|vSaldoFinal|detBCN|E|N|1-1|4-18|2<br>Saldo remanescente desta base de cálculo negativa para os próximos<br>períodos de apuração.|
|193|gBCNCBS|infoBCN|G|-|1-1|-|-<br>Grupo de detalhamento de bases de cálculo negativas de CBS.|
|194|codBCNRaiz|gBCNCBS|E|C|1-1|5|-<br>Código identificador raiz da base de cálculo negativa, conforme<br>[[Tabela12– Códigos de Bases de Cálculo]].|
|195|vSaldoAnt|gBCNCBS|E|N|1-1|4-18|2<br>Valor do saldo anterior.<br>Montante acumulado da base de cálculo negativa disponível no<br>início do período de apuração atual, antes das compensações do<br>mês.|
|196|vUtilPer|gBCNCBS|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizado como dedução da base<br>de cálculo do período de apuração atual.<br>**Cálculo:**<br>Igual a {vDedBCN}|
|197|vSaldoFinal|gBCNCBS|E|N|1-1|4-18|2<br>Valor do saldo remanescente de base de cálculo negativa a ser<br>transportado para o próximo período de apuração.<br>**Cálculo:**<br>{vSaldoAnt} - {vUtilPer} + {vBCNCBS} (se houver)|



**Página 118 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

||**D-9199 –**|**Retorno Totalizad**|**or – Fe**|**cham**|**ento M**|**ensal**|**– Especificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|---|
|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|198|qtdOrigens|gBCNCBS|E|N|1-1|1-2|-<br>Quantidade de períodos de origem. Indica quantos períodos<br>distintos compõem o saldo atual.<br>**Exemplo:**Se o saldo é composto por bases negativas de jan/25 e<br>mar/26, {qtdOrigens} = [2].|
|199|detBCN|gBCNCBS|G|-|0-100|-|-<br>Detalhamento da composição do saldoporperíodo de origem.|
|200|codBCN|detBCN|E|C|1-1|13|-<br>Código identificador da base de cálculo negativa, conforme [[Tabela<br>12– Códigos de Bases de Cálculo]].|
|201|perOrigem|detBCN|E|D|1-1|7|-<br>Período de origem da base de cálculo negativa.<br>**Máscara:**AAAA-MM|
|202|vOrigemIni|detBCN|E|N|1-1|4-18|2<br>Valor inicial da base de cálculo negativa no respectivo período de<br>origem.|
|203|vSaldoAnt|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa disponível para uso neste período<br>de apuração.<br>**Cálculo:**<br>SE {perOrigem} = {perApur},<br>ENTÃO {vSaldoAnt} = [0.00],<br>SENÃO {vSaldoAnt} = {vSaldoFinal} do {perApur} imediatamente<br>anterior.|
|204|vUtilPer|detBCN|E|N|1-1|4-18|2<br>Valor da base de cálculo negativa utilizada neste período de<br>apuração.|
|205|vSaldoFinal|detBCN|E|N|1-1|4-18|2<br>Saldo remanescente desta base de cálculo negativa para os próximos<br>períodos de apuração.|
|206|totalTributos|infoTotProg|G|-|1-1|-|-<br>Totalização dos tributos do Regime Específico de Concursos de<br>Prognósticos.|
|207|vIS|totalTributos|E|N|1-1|4-18|2<br>Valor do Imposto Seletivo (IS).<br>**Cálculo:**<br>SOMA({infoTotProg.detBC.vIS})|
|208|vIBSMun|totalTributos|E|N|1-1|4-18|2<br>Valor do IBS municipal.<br>**Cálculo:**<br>SOMA({infoTotProg.detBC.vIBSMun})|
|209|vIBSUF|totalTributos|E|N|1-1|4-18|2<br>Valor do IBS estadual.<br>**Cálculo:**<br>SOMA({infoTotProg.detBC.vIBSUF})|
|210|vIBSTot|totalTributos|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>{totalTributos.vIBSMun} + {totalTributos.vIBSUF}|
|211|vCBS|totalTributos|E|N|1-1|4-18|2<br>Valor da CBS.<br>**Cálculo:**<br>SOMA({infoTotProg.detBC.vCBS})|



**Página 119 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

**D-9199 – Retorno Totalizador – Fechamento Mensal – Especificação Técnica de Campos (Detalhamento)** 

|**#**|**Grupo/Tag**|**Grupo Pai**|**Cat**<br>|**Tipo**|**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|---|---|---|---|---|---|---|---|
|212|totalTributosGeral|infoEvento|G|-|1-1|-|-<br>Totalização dos tributos do declarante.|
|213|vIS|totalTributosGeral|E|N|0-1|4-18|2<br>Valor total do Imposto Seletivo (IS).<br>**Preenchimento:**<br>Corresponde<br>ao<br>campo<br>{vIS}<br>do<br>grupo<br>{totalTributos} do grupo {infoTotProg}.<br>**Cálculo:**<br>SOMA({totalTributos.vIS})|
|214|vIBSMun|totalTributosGeral|E|N|1-1|4-18|2<br>Valor total do IBS municipal.<br>**Preenchimento:**Soma do campo {vIBSMun} de todas as ocorrências<br>do grupo {totalTributos} dos grupos {infoTotFinanc}, {infoTotSaude}<br>e {infoTotProg}.<br>**Cálculo:**<br>SOMA({totalTributos.vIBSMun})|
|215|vIBSUF|totalTributosGeral|E|N|1-1|4-18|2<br>Valor total do IBS estadual.<br>**Preenchimento:**Soma do campo {vIBSUF} de todas as ocorrências<br>do grupo {totalTributos} dos grupos {infoTotFinanc}, {infoTotSaude}<br>e {infoTotProg}.<br>**Cálculo:**<br>SOMA({totalTributos.vIBSUF})|
|216|vIBSTot|totalTributosGeral|E|N|1-1|4-18|2<br>Valor total do IBS.<br>**Cálculo:**<br>SOMA({totalTributosGeral.vIBSMun}) +<br>SOMA({totalTributosGeral.vIBSUF})|
|217|vCBS|totalTributosGeral|E|N|1-1|4-18|2<br>Valor total da CBS.<br>**Preenchimento:**Soma do campo {vCBS} de todas as ocorrências do<br>grupo {totalTributos} dos grupos {infoTotFinanc}, {infoTotSaude} e<br>{infoTotProg}.<br>**Cálculo:**<br>SOMA({totalTributos.vCBS})|



**Página 120 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.8 EVENTO D-9209 – RETORNO – EVENTOS TRANSACIONAIS** 

### **4.8.1 Estrutura Hierárquica do Evento (Resumo)** 

||**D-**<br> <br>|**9209 – Retorno – E**<br>|**ventos Transacionais – Estrutura Hierá**<br>|**rquica do E**<br>|**vento (Res**<br>|**umo)**<br>|
|---|---|---|---|---|---|---|
|**Nível**|<br>**Grupo**|**Grupo Pai**|**Descrição**|**Ocorr**|**Chave**|**Condição**|
|1|DeRE||Envelope raiz dos eventos da DeRE.|1-1|-|O|
|2|evtRetornoTransac|DeRE|Evento de retorno e recibo de<br>processamento de eventos transacionais.|1-1|id|O|
|3|ideContrib|evtRetornoTransac|Informações de identificação do<br>contribuinte.|1-1|nrInsc|O|
|3|ideStatus|evtRetornoTransac|Situação do processamento do evento.|1-1|-|O|
|4|ocorrencias|ideStatus|Informações de ocorrências registradas.|0-10|-|O (se {cdRetorno} = [0]);<br>F (nos demais casos).|
|3|infoRecEv|evtRetornoTransac|Informações de processamento dos<br>eventos.|1-1|-|O|
|3|infoPlanoSaude|evtRetornoTransac|Grupo de retorno do processamento de<br>Planos de Assistência à Saúde.|0-1|-|O (se {tpEv} = [D-3201] e<br>existir D-3201.{tpContrato} =<br>[4]);<br>N (nos demais casos).|
|4|detRateioPremio|infoPlanoSaude|Grupo de detalhamento do rateio dos<br>prêmios, individualizado por beneficiário<br>titular.|1-30000|-|O|



**Página 121 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

### **4.8.2 Especificação Técnica dos Campos (Detalhamento)** 

|**D-92**|**09 – Retorno – Ev**|**ento**|**s Tran**|**sacion**|**ais – Espe**|**cificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|<br>**Tam**<br>**De**|**c**<br>**Descrição**|
|1<br>DeRE|-|G|-|1-1|-<br>-|<br>Envelope raiz dos eventos da DeRE.|
|2<br>evtRetornoTransac|DeRE|G|-|1-1|-<br>-|<br>Evento de retorno e recibo de processamento de eventos<br>transacionais. Retorno gerado para eventos que não exijam um<br>totalizador específico de cálculo.|
|3<br>id|evtRetornoTransac|A|C|1-1|42<br>-|<br>Identificação única do evento (campo id do evento transmitido pelo<br>contribuinte, a que se refere este retorno).|
|4<br>ideContrib|evtRetornoTransac|G|-|1-1|-<br>-|<br>Informações de identificação do contribuinte.|
|5<br>nrInsc|ideContrib|E|C|1-1|8<br>-|<br>Número de inscrição do contribuinte (CNPJ raiz).<br>**Validação:**Deve ser um CNPJ raiz válido de 8 posições.|
|6<br>ideStatus|evtRetornoTransac|G|-|1-1|-<br>-|<br>Situação do processamento do evento.|
|7<br>cdRetorno|ideStatus|E|N|1-1|1<br>-|<br>Código indicativo do status do retorno.<br>**Valores válidos:**<br>**0**– Erro;<br>**1**– Sucesso.|
|8<br>descRetorno|ideStatus|E|C|1-1|4,7<br>-|<br>Descrição literal do status do retorno.<br>**Valores válidos:**<br>–**Erro**;<br>–**Sucesso**.|
|9<br>ocorrencias|ideStatus|G|-|0-10|-<br>-|<br>Informações de ocorrências registradas.|
|10codigo|ocorrencias|E|C|1-1|1-6<br>-|<br>Código numérico da ocorrência.|
|11descricao|ocorrencias|E|C|1-1|1-2048<br>-|<br>Descrição detalhada da ocorrência (mensagem de erro ou aviso).|
|12tipo|ocorrencias|E|N|1-1|1<br>-|<br>Classificação do tipo da ocorrência.<br>**Valores válidos:**<br>**1**– Erro;<br>**2**– Aviso.|
|13localizacao|ocorrencias|E|C|0-1|1-2048<br>-|<br>Identificação do campo ou grupo onde a ocorrência foi detectada.|
|14<br>infoRecEv|evtRetornoTransac|G|-|1-1|-<br>-|<br>Informações de processamento dos eventos.|
|15nrRecibo|infoRecEv|E|C|0-1|31<br>-|<br>Número do recibo do evento processado com sucesso.<br>**Preenchimento:**Preenchido somente quando {cdRetorno} = [1]<br>(Sucesso).<br>**Regra de validação:**<br>RN - Formação do Número do Recibo do Evento|
|16protocoloLote|infoRecEv|E|C|0-1|28<br>-|<br>Número do protocolo de entrega do lote.<br>**Regra de validação:**<br>RN - Numero do Protocolo do Lote|
|17dhRecepcao|infoRecEv|E|D|1-1|25-33<br>-|<br>Data e hora da recepção do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|18dhProcess|infoRecEv|E|D|1-1|25-33<br>-|<br>Data e hora do processamento do evento (UTC).<br>**Máscara:**AAAA-MM-DDThh:mm:ss.sssssssTZD|
|19tpEv|infoRecEv|E|C|1-1|6<br>-|<br>Sigla de identificação do tipo de evento.<br>**Exemplo:**D-2201.|
|20hash|infoRecEv|E|C|1-1|44<br>-|<br>Hashcode do arquivo processado.|



**Página 122 de 123** 

Versão 1.2.0 

Leiautes da DeRE 

|**D-9**|**209 – Retorno – Ev**|**ento**|**s Tran**|**saciona**|**is – Es**|**pecificação Técnica de Campos (Detalhamento)**|
|---|---|---|---|---|---|---|
|**#**<br>**Grupo/Tag**|**Grupo Pai**|**Cat**|**Tipo**|<br>**Ocorr**|**Tam**|**Dec**<br>**Descrição**|
|21<br>infoPlanoSaude|evtRetornoTransac|G|-|0-1|-|-<br>Grupo de retorno do processamento de Planos de Assistência à Saúde.<br>**Preenchimento:**Exclusivo quando {tpEv} = [D-3201] e existir campo<br>{tpContrato} igual a [4] (Plano ou seguro sem valor definido por<br>usuário).|
|22chDeREOper|infoPlanoSaude|E|C|1-1|53|-<br>Chave de acesso individualizada da operação da DeRE (chave-filha).<br>**Preenchimento:**Chave de 53 caracteres que identifica unicamente a<br>operação de rateio do beneficiário titular. Corresponde à chave-mãe<br>do agrupamento ({chDeRE} do evento D-3201 de origem) com a<br>substituição de seus 3 (três) últimos dígitos (sufixo aglutinador) pelo<br>número sequencial ({seq}) individualizado.|
|23<br>detRateioPremio|infoPlanoSaude|G|-|1-30000|-|-<br>Grupo de detalhamento do rateio dos prêmios, individualizado por<br>beneficiário titular. Gerado pelo sistema após a aplicação dos<br>coeficientes de rateio da [[Tabela33– Coeficiente de Rateio do Prêmio<br>não Individualizado]].<br>**Preenchimento:**Listagem das ocorrências válidas extraídas da tag<br>{CPFTitular} do evento D-3201 de origem, consolidando-as<br>exclusivamente para os contratos de plano ou seguro sem valor<br>definido por usuário ({tpContrato} = [4]).|
|24chDeREOper|detRateioPremio|E|C|1-1|53|-<br>Chave de acesso individualizada da operação da DeRE (chave-filha).<br>**Preenchimento:**Chave de 53 caracteres que identifica unicamente a<br>operação de rateio do beneficiário titular. Corresponde à chave-mãe<br>do agrupamento ({chDeRE} do evento D-3201 de origem) com a<br>substituição de seus 3 (três) últimos dígitos (sufixo aglutinador) pelo<br>número sequencial ({seq}) individualizado.|
|25CPFBenefTit|detRateioPremio|E|C|1-1|11|-<br>Número de inscrição no CPF do beneficiário titular.<br>**Cálculo:**<br>Listagem de todas as ocorrências de {CPFTitular} quando {tpContrato}<br>= [4] (Plano ou seguro sem valor definido por usuário).<br>**Regra de validação:**<br>DV_CPF|
|26vPremIndivid|detRateioPremio|E|N|1-1|4-18|2<br>Valor consolidado do rateio do prêmio ou contraprestação,<br>individualizado por titular.<br>**Nota:**O valor alocado a cada titular será apurado pela proporção da<br>soma dos coeficientes do titular e de seus respectivos dependentes<br>em relação ao somatório dos coeficientes de todos os beneficiários do<br>contrato. Os coeficientes de alocação são definidos pela faixa etária<br>de cada pessoa no último dia da competência da declaração<br>({perApur}).<br>**Cálculo:**<br>1. Apurar a idade em anos completos do titular e de cada dependente<br>na data do último dia do {perApur}. Identificar o coeficiente individual<br>de cada vida na  [[Tabela33– Coeficiente de Rateio do Prêmio não<br>Individualizado]];<br>2. Somar os coeficientes individuais de todas as vidas do contrato para<br>obter o coeficiente total do contrato (cTotal);<br>3. Para cada titular, somar o seu coeficiente individual ao de todos os<br>seus dependentes para obter o subcoeficiente do titular (cTitular).<br>4. Calcular o coeficiente de rateio para cada titular do contrato: cRatTit<br>= cTitular / cTotal.<br>5. Multiplicar o valor do contrato ({vOper}) pelo respectivo cRatTit para<br>se obter o valor individualizado vinculado a este titular (e seus<br>dependentes)|



**Página 123 de 123** 

