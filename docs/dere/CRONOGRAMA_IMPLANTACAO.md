# Cronograma executivo DeRE — EFPC/PREVIC

Referência de planejamento: 02/09/2026. As datas abaixo são estimativas e devem ser ajustadas ao calendário da diretoria, à liberação do ambiente oficial e ao retorno da Receita/CGIBS.

## Resumo para decisão

| Fase | Janela sugerida | Entrega | Dependências |
|---|---|---|---|
| 0. Governança e escopo | 02–04/09/2026 | responsáveis, CNPJ raiz, eventos aplicáveis, dados-fonte | diretoria/contabilidade |
| 1. POC local | 02–18/09/2026 | fluxo mockado, CSV/XML, assinatura de teste, relatórios | em execução |
| 2. Homologação técnica | 14–25/09/2026 | ambiente isolado, H2/MariaDB, CI, testes de carga e restore | infraestrutura |
| 3. Acesso Receita Integra | 14–30/09/2026 | participação no piloto, procuração quando necessária, client_id/client_secret | representante legal |
| 4. Certificados | 21–30/09/2026 | certificado ICP-Brasil de homologação, cofre/HSM, rotação | compras/segurança |
| 5. Fase 1 — Estruturais | a partir de 01/10/2026 | D-1001 e D-1011 | D-1011 deve mapear o Plano Referencial PREVIC para EFPC |
| 6. Fase 2 — Outubro/2026 | até 15/11/2026 | D-1101, D-1106, D-1121, D-2101 e D-1199 | D-1001/D-1011 processados; eventos conforme aplicabilidade |
| 7. Go/no-go operacional | 02–13/11/2026 | aceite contábil, segurança, operação e suporte | primeira competência mensal |
| 8. Fase 3 — Demais eventos | a partir de 01/01/2027 | eventos não incluídos nas fases 1 e 2 | leiautes, regras e aplicabilidade confirmados |

## Etapas detalhadas

## Calendário oficial de implantação considerado

| Quando | Competência/grupo | Evento | Conteúdo | Observação |
|---|---|---|---|---|
| A partir de 01/10/2026 | Estrutural | D-1001 | Informações do Contribuinte | Primeiro evento a transmitir |
| A partir de 01/10/2026 | Estrutural | D-1011 | Plano Geral de Contas Comentado — PGCC | Para EFPC, mapeia contas para o Plano Referencial PREVIC |
| Até 15/11/2026 | Outubro/2026 | D-1101 | Balancete Mensal | Primeira competência mensal |
| Até 15/11/2026 | Outubro/2026 | D-1106 | Identificação de Aplicações Financeiras | Quando aplicável |
| Até 15/11/2026 | Outubro/2026 | D-1121 | Relação de Deduções Utilizadas na Apuração | Conforme aplicabilidade |
| Até 15/11/2026 | Outubro/2026 | D-2101 | Débito em Operações com Títulos de Dívida com Oferta Pública | Quando aplicável |
| Até 15/11/2026 | Outubro/2026 | D-1199 | Fechamento Mensal | Consolida a escrituração |
| A partir de 01/01/2027 | — | Demais eventos | Eventos fora das duas fases anteriores | Terceira fase |

`01/10/2026` é a data de início de recebimento dos eventos estruturais, não necessariamente um prazo final. O prazo mensal informado para a competência outubro/2026 é `15/11/2026`.

### 1. Preparar homologação

- criar conta/servidor isolado, banco separado e DNS/HTTPS;
- separar configurações e segredos por ambiente;
- configurar CI/CD, logs sem senha/token/XML sensível e alertas;
- executar migrations Flyway automaticamente;
- validar D-1001 e D-1011 com dados anonimizados;
- criar testes de rejeição, duplicidade, alteração, indisponibilidade e timeout.
- garantir que D-1001 seja transmitido primeiro;
- aguardar o processamento bem-sucedido do D-1001 antes de transmitir D-1011;
- somente liberar os eventos mensais após D-1001 e D-1011 processados com sucesso.

### 2. Infraestrutura e continuidade

- produção e homologação em redes, bancos e credenciais independentes;
- backup diário completo, incremental quando aplicável e retenção definida;
- cópia criptografada fora do servidor principal;
- teste mensal de restore em ambiente descartável;
- RPO/RTO aprovados pela diretoria;
- inventário de certificados, vencimentos, responsáveis e procedimento de revogação;
- trilha de auditoria para usuário, entidade, lote, protocolo, XML, recibo e erro;
- acesso administrativo com MFA, menor privilégio e segregação de funções.

### 3. Receita Integra / DeRE

O manual vigente orienta que a empresa participe do piloto e gere a credencial no Portal da Produção Restrita, autenticando-se com gov.br e selecionando a empresa representada. Quando não for usado o próprio e-CNPJ para assinar, deve ser verificada a procuração eletrônica para o CPF responsável. A transmissão da DeRE ocorre por API, não por digitação no e-CAC.

Checklist para solicitar:

1. confirmar participação no piloto da Reforma Tributária;
2. definir representante legal e responsável técnico;
3. verificar poderes no e-CAC/procuração para DeRE, se aplicável;
4. acessar `https://piloto-cbs.tributos.gov.br/` com gov.br;
5. gerar e guardar `client_id` e `client_secret` em cofre;
6. confirmar escopos, URLs e certificado exigidos no manual vigente;
7. registrar protocolo, responsável, data de concessão e validade;
8. repetir a validação para produção somente após o aceite da homologação.

Isso não deve ser tratado como uma autorização genérica do gov.br: gov.br é o canal de autenticação/gestão indicado; a credencial da API DeRE é do Receita Integra. Confirmar o procedimento vigente diretamente com CGIBS/Receita antes do protocolo.

### 4. Certificado digital

O sistema já executa a assinatura XMLDSig no fluxo local. Porém, o formato atual **não deve ser usado em homologação oficial ou produção** porque o código gera um certificado autoassinado efêmero apenas para mock.

Para uso real, implementar antes do go-live:

- carregamento de certificado ICP-Brasil A1 em PKCS#12 por segredo externo, ou integração com A3/HSM;
- nunca persistir senha/chave privada em banco, logs, `.env` versionado ou XML;
- validação de validade, cadeia ICP-Brasil, KeyUsage e correspondência com a entidade;
- controle de acesso por entidade e aprovação para uso do certificado;
- rotação, revogação, backup seguro e teste de restauração;
- assinatura individual de cada evento, referência correta ao atributo `id`, RSA-SHA256, SHA-256, enveloped signature e somente o certificado final;
- verificação pós-assinatura e validação XSD do evento e do lote;
- teste oficial de rejeição por certificado inválido/expirado.

Conclusão: a assinatura está funcional para demonstração local, mas ainda não é segura nem suficiente como módulo de produção. O próximo passo é substituir `LocalTestCertificate` por um provider de certificado gerenciado.

## Plano por marco de entrega

### Marco 1 — 01/10/2026

Deve estar disponível o envio dos eventos estruturais:

1. validar e transmitir D-1001;
2. consultar e confirmar o recibo do D-1001;
3. gerar, validar e transmitir D-1011;
4. para EFPC, validar o mapeamento conta interna → Plano Referencial PREVIC;
5. armazenar XML, protocolo, retorno, recibo ou rejeição.

### Marco 2 — 15/11/2026

Para a competência outubro/2026, entregar os eventos aplicáveis — D-1101, D-1106, D-1121 e D-2101 — e o fechamento D-1199. O D-1199 somente deve ser liberado depois que os eventos mensais aplicáveis forem validados e transmitidos.

### Marco 3 — 01/01/2027

Expandir o catálogo para os demais eventos publicados, mantendo versionamento de XSD, regras de aplicabilidade, dependências e testes específicos.

## Critérios de passagem

- todos os eventos do escopo validados contra o pacote XSD aprovado;
- restore comprovado em ambiente separado;
- nenhum segredo em código ou log;
- usuário somente transmite pelas entidades associadas;
- protocolo, recibo, rejeição e XML reproduzíveis;
- aceite formal de contabilidade, segurança, infraestrutura e diretoria.
