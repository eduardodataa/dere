# Exemplos CSV dos layouts DeRE

Os arquivos abaixo são exemplos tabulares para apresentação e preparação do mapeamento. O conversor executável atual da aplicação aceita o CSV do D-1001; os demais arquivos documentam os campos do layout e dependem da implementação dos respectivos geradores XML.

Campos repetitivos dos XSDs foram representados como uma linha por ocorrência. O cabeçalho `id` identifica o evento e `nrInsc` usa o CNPJ raiz fictício `12345678`.

No D-1001, `ideContrib` e `idePeriodo` são grupos XML, portanto não recebem um valor próprio no CSV: são representados por `nrInsc`, `iniValid` e `fimValid`. `motExcl`, `nrProc`, `novaValidade*`, `regTribSecund` e os campos de atividades/UF são colunas opcionais ou condicionais do XSD e permanecem vazias no exemplo de inclusão.

Mapeamento do D-1001:

| Coluna CSV | Caminho XML |
|---|---|
| `id` | `DeRE/evtInfoContrib/@id` |
| `motExcl`, `nrProc`, `tpOper`, `tpAmb`, `aplicEmi`, `verAplic` | `DeRE/evtInfoContrib/ideEvento/*` |
| `nrInsc` | `DeRE/evtInfoContrib/ideContrib/nrInsc` |
| `iniValid`, `fimValid` | `DeRE/evtInfoContrib/idePeriodo/*` |
| `novaValidadeIniValid`, `novaValidadeFimValid` | `DeRE/evtInfoContrib/idePeriodo/novaValidade/*` |
| `regTribPrinc`, `regTribSecund`, `indNatTrib` | `DeRE/evtInfoContrib/infoContrib/*` |
| `tpAtividade*`, `UFCredenc` | grupos condicionais de `infoContrib` |

O layout D-1121 aparece no calendário informado, mas não está presente no pacote XSD baixado nem no leiaute oficial v1.1.0 atualmente versionado no projeto. Por isso, seu arquivo é somente um template de controle, sem campos fiscais inventados. `retornos.csv` reúne exemplos tabulares dos layouts D-9001, D-9101, D-9106, D-9121 e D-9199.

XML estrutural de referência: [d1001-evento-exemplo.xml](C:\Users\edordeiro\projetos\dere\examples\xml\d1001-evento-exemplo.xml). A assinatura desse arquivo é apenas um placeholder para demonstrar a estrutura exigida; não deve ser transmitida. A aplicação recalcula digest, assinatura e certificado.
