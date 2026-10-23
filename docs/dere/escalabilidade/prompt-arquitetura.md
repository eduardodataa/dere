# Auditoria Arquitetural — Escalabilidade, Alta Disponibilidade e Processamento Massivo do DeRE

## 1. Contexto

Você atuará como um **Principal Software Architect / Staff Engineer**, especialista em:

- Java 21, Spring Boot, Spring Data JPA e Hibernate.
- Sistemas distribuídos e processamento assíncrono.
- Concorrência, multithreading e gerenciamento de recursos.
- Arquiteturas SaaS multi-tenant.
- Processamento massivo de XML e validação XSD.
- Integrações governamentais com certificados digitais, assinatura XMLDSig e OAuth 2.0. Não presuma mTLS.
- MariaDB, mensageria, armazenamento de objetos e AWS.
- Performance, observabilidade, resiliência e alta disponibilidade.

O projeto em análise é o **DeRE — Declaração de Regimes Específicos**, desenvolvido para permitir que entidades realizem a preparação, validação, transmissão e acompanhamento de declarações perante os serviços governamentais correspondentes.

O sistema deverá evoluir para uma solução comercial capaz de atender simultaneamente pequenas, médias e grandes entidades.

A aplicação, conferida no repositório, possui esta stack:

**Backend:** Java 21, Spring Boot 4.1.0, Spring Data JPA, Hibernate, Flyway, MariaDB. H2 é dependência de runtime, não o banco padrão de `application.yml`.

**Frontend:** React 19, TypeScript, Vite, Material UI e Axios. Não há TanStack Query. O build do frontend entra no mesmo JAR, em `static/`.

**Infraestrutura vista no repositório:** GitHub Actions publica um JAR único por SSH em uma EC2 por ambiente. Não há fila, S3, SQS nem orquestrador no código. AWS presente no código é Secrets Manager/KMS para senha de certificado. Confirme a topologia pela inspeção; não a complete por suposição.

**Integração:** XML via DOM, XSD, assinatura XMLDSig com certificado ICP-Brasil por entidade e autenticação da API pelo Receita Integra (OAuth 2.0 client credentials, Bearer token, `HttpClient`/`RestClient`). Não presuma mTLS.

**Autenticação atual:** headers `X-User-Id` e `X-Entity-Id`, com `AccessService.assertAccess`. O `pom.xml` traz `spring-security-crypto`, não um filter chain web. Não presuma sessão, JWT nem Spring Security web.

**Leiautes no código:** D-1001, D-1011, D-1101 e D-1199. A fonte da verdade é o código. O README descreve uma POC anterior e está desatualizado.

**Dois fluxos de transmissão:** o mock (`dere.mock.enabled`, padrão `true`; `LocalDereFlowService`, `TransmissaoSimuladaService`, `MockDereController`) e o HTTP real (`ClienteTransmissaoDere`, `HttpTransmissaoGovernoAdapter`). Separe os dois em toda a análise. O polling agendado hoje consulta o fluxo local.

Não presuma fila, processamento assíncrono, armazenamento de objetos nem multi-tenant com `tenant_id`. Verifique o que o código realmente faz.

## 2. Objetivo principal

Realizar uma auditoria técnica profunda da arquitetura atual para determinar se ela consegue suportar:

1. Milhares de usuários simultâneos.
2. Grandes quantidades de arquivos XML importados simultaneamente.
3. Validação e conversão massiva de CSV para XML.
4. Múltiplas entidades enviando declarações simultaneamente.
5. Transmissões concorrentes para os serviços da Receita Federal.
6. Consultas assíncronas de protocolos e resultados.
7. Reprocessamento seguro após falhas.
8. Escalabilidade horizontal da aplicação.
9. Isolamento operacional entre diferentes entidades.
10. Crescimento progressivo sem reescrita completa da solução.

A prioridade é garantir **confiabilidade, consistência, segurança e escalabilidade**, evitando complexidade arquitetural desnecessária.

## 3. Instruções obrigatórias

Antes de propor qualquer mudança:

1. Inspecione a estrutura completa do repositório.
2. Identifique os módulos e suas responsabilidades.
3. Analise controllers, services, repositories, entidades, configurações e integrações.
4. Identifique fluxos síncronos e assíncronos.
5. Investigue como são executados uploads, validações, transmissões e consultas.
6. Identifique executores, threads, schedulers e mecanismos de retry.
7. Verifique como certificados digitais são carregados e utilizados.
8. Analise a modelagem do banco e a persistência dos arquivos.
9. Verifique o isolamento entre clientes e entidades.
10. Identifique configurações de infraestrutura e deployment.

**Não altere código, configuração, SQL nem frontend.** O único efeito permitido no repositório é criar os markdowns da seção 11.

Não apresente recomendações genéricas. Cada problema identificado deverá conter evidência do código, incluindo arquivo, classe, método e linhas, quando disponíveis.

Classifique cada conclusão como:

- CONFIRMADO: evidenciado diretamente pelo código.
- RISCO POTENCIAL: inferência técnica fundamentada.
- NÃO VERIFICADO: depende de infraestrutura, documentação ou medição não disponível.

Não invente limites de capacidade, regras da Receita ou resultados de benchmarks.

## 4. Análise do processamento de XML

Investigue:

- Como arquivos XML são recebidos e armazenados.
- Se uploads são processados integralmente em memória.
- Se há utilização de DOM, SAX, StAX ou JAXB.
- Possíveis problemas com XMLs grandes.
- Consumo de heap durante validações XSD.
- Possibilidade de streaming para grandes arquivos.
- Concorrência na validação.
- Reutilização segura de parsers, schemas e validadores.
- Riscos de XML bomb, XXE e arquivos ZIP maliciosos.
- Processamento de grandes arquivos CSV.
- Uso de transações longas.
- Possíveis vazamentos de memória.

Avalie a necessidade de:

- Processamento assíncrono.
- Divisão de arquivos em unidades menores de trabalho.
- Workers independentes.
- Filas persistentes.
- Limites de memória por processamento.
- Backpressure e controle de concorrência.

Determine o que já está implementado e o que precisa ser evoluído.

## 5. Análise da integração com a Receita Federal

Este é um dos pontos mais críticos.

Investigue todo o fluxo de transmissão e consulta de declarações.

Avalie:

- Concorrência entre transmissões.
- Controle de requisições simultâneas.
- Timeouts de conexão, leitura e escrita.
- Connection pooling HTTP e reutilização do `HttpClient`/`RestClient`.
- Cache, expiração e concorrência do Bearer token do Receita Integra.
- Gerenciamento do certificado de assinatura por entidade, separado da credencial OAuth da API.
- Assinatura digital sob concorrência.
- Diferença entre o fluxo mock e o fluxo HTTP real, inclusive qual dos dois o scheduler chama.
- Retentativas e backoff exponencial com jitter.
- Circuit breaker.
- Rate limiting global e por entidade.
- Tratamento de respostas HTTP e códigos de negócio.
- Persistência de protocolos.
- Consulta posterior dos resultados.
- Tratamento de indisponibilidade dos serviços governamentais.
- Recuperação de operações interrompidas.

Investigue especialmente o cenário em que uma requisição de transmissão sofre timeout após ter sido recebida pelo serviço governamental.

Proponha como evitar transmissões duplicadas, considerando que uma nova tentativa pode produzir efeitos fiscais ou operacionais.

Diferencie claramente:

- Falha antes do envio.
- Falha durante a comunicação.
- Resultado desconhecido.
- Recepção confirmada.
- Processamento confirmado.
- Rejeição definitiva.

Não considere retry automático seguro sem evidência de idempotência ou mecanismo de reconciliação.

Consulte os manuais em `docs/dere/manuais/` para limites de lote, tamanho de XML e frequência de requisições. Cite o arquivo. Se os manuais do desenvolvedor divergirem entre si, registre a divergência. Quando não houver documentação, registre como dependência externa. Não invente limite.

## 6. Banco de dados e persistência

Analise o MariaDB e a camada JPA/Hibernate.

Verifique:

- Índices ausentes.
- Consultas potencialmente lentas.
- Problemas N+1.
- Paginação e ordenação.
- Lock pessimista e otimista.
- Concorrência na atualização de lotes.
- Deadlocks.
- Pool de conexões.
- Transações extensas.
- Persistência de XMLs e arquivos grandes.
- Crescimento das tabelas.
- Estratégias de arquivamento.
- Integridade referencial.
- Recuperação após falhas.

Avalie a separação entre:

- Metadados transacionais.
- Conteúdo dos arquivos XML.
- Histórico de transmissão.
- Logs técnicos.
- Auditoria.

Compare o armazenamento de arquivos no banco com o uso de armazenamento de objetos, como Amazon S3.

Considere custo, segurança, desempenho e complexidade de migração.

## 7. Escalabilidade horizontal e arquitetura distribuída

Determine se a aplicação consegue operar com múltiplas instâncias simultaneamente. O deploy visto no repositório é uma instância por ambiente. Há `@EnableScheduling`. Investigue `claimQuery` no lote e responda se esse lease impede consulta e transmissão duplicadas com três instâncias, no caminho real e no mock.

Investigue:

- Estado mantido em memória.
- Uso de variáveis estáticas e singletons mutáveis.
- Schedulers executados em todas as instâncias.
- Filas locais. Não presuma que existam.
- Concorrência entre workers.
- Locks locais versus distribuídos.
- Sessões de usuário. A autenticação atual é header, não sessão.
- Dependência de arquivos locais.
- Cache local.
- Gerenciamento de certificados.
- Balanceamento de carga.

Avalie a possibilidade de escalar separadamente:

1. API REST.
2. Processamento de XML.
3. Transmissão de declarações.
4. Consulta de protocolos.

Compare três alternativas:

**A — Monólito modular otimizado**

Manter a aplicação atual, corrigindo gargalos e adicionando mecanismos de resiliência.

**B — Monólito modular com workers assíncronos**

Manter o mesmo domínio e base de código, mas executar processamento e transmissão por workers independentes e filas persistentes.

**C — Serviços distribuídos**

Separar componentes em serviços implantáveis independentes.

Compare custo, complexidade, manutenção, escalabilidade e esforço de migração.

Não recomende microsserviços automaticamente.

## 8. Multi-tenancy e isolamento entre entidades

Avalie a arquitetura para atender múltiplos clientes comerciais. O limite de isolamento neste código é a entidade, identificada pelo CNPJ raiz de 8 dígitos, ligada ao usuário. Não procure `tenant_id`, schema por cliente nem banco por cliente, a menos que o código os tenha.

Verifique:

- Como a entidade é identificada e como `assertAccess` é aplicado.
- Risco de o cliente forjar `X-User-Id` ou `X-Entity-Id`.
- Isolamento de dados.
- Autorização por entidade.
- Isolamento de certificados digitais.
- Isolamento de arquivos.
- Limites de processamento por entidade.
- Priorização e justiça no consumo das filas.
- Auditoria.
- Risco de vazamento de informações entre clientes.

Analise cenários em que uma entidade envia 100.000 XMLs enquanto outras entidades executam operações normais.

Proponha mecanismos para impedir que um cliente monopolize os recursos.

## 9. Observabilidade e resiliência

Avalie:

- Logs estruturados.
- Correlation ID.
- Métricas de processamento.
- Métricas de filas.
- Tempo médio e percentis de transmissão.
- Taxa de erros.
- Saturação de CPU e memória.
- Pool de conexões.
- Saúde das instâncias.
- Monitoramento de certificados.
- Dead-letter queues.
- Reprocessamento manual.
- Alertas operacionais.

Sugira tecnologias compatíveis com a stack existente, preferindo soluções de baixa complexidade operacional.

## 10. Cenários de capacidade e testes de carga

Considere os seguintes cenários hipotéticos de planejamento, não como capacidade já comprovada:

| Cenário | Usuários simultâneos | XMLs por entidade | Entidades |
|---|---:|---:|---:|
| Pequeno | 20 | 1.000 | 5 |
| Médio | 100 | 10.000 | 20 |
| Grande | 500 | 50.000 | 50 |
| Enterprise | 2.000 | 100.000 | 100 |

Avalie também cargas concentradas em períodos de fechamento fiscal.

Proponha testes com:

- k6 ou Gatling para carga HTTP.
- Testcontainers para testes de integração. Não afirme que já está no `pom.xml`; proponha somente se fizer sentido adicionar.
- Testes de concorrência e idempotência.
- Simulador de serviços governamentais.
- Latência variável.
- Erros HTTP 429 e 5xx.
- Timeouts.
- Indisponibilidade prolongada.
- Reinicialização de workers durante processamento.
- Acúmulo de filas.
- Grandes arquivos XML e CSV.

Defina métricas, hipóteses, critérios de aceitação e como obter a capacidade real por benchmark.

Não execute testes de carga contra os serviços reais da Receita Federal sem autorização e limites previamente definidos.

## 11. Entregáveis

Crie somente estes markdowns, dentro de:

`docs/dere/escalabilidade/`

Não altere nenhum outro arquivo.

Produza:

**01-arquitetura-atual.md**
- Componentes existentes.
- Fluxos identificados.
- Dependências.
- Diagrama Mermaid da arquitetura atual.

**02-gargalos-e-riscos.md**
- Problemas identificados.
- Evidências no código.
- Impacto.
- Probabilidade.
- Severidade.
- Prioridade P0, P1 ou P2.

**03-arquitetura-proposta.md**
- Arquitetura recomendada.
- Justificativa.
- Alternativas descartadas.
- Diagrama Mermaid da arquitetura futura.
- Fluxo de processamento assíncrono.
- Estratégia de escalabilidade horizontal.

**04-plano-evolucao.md**
- Fases de implementação.
- Dependências.
- Complexidade.
- Esforço estimado.
- Riscos de migração.
- Estratégia de rollback.
- Compatibilidade com funcionalidades existentes.

**05-plano-testes-carga.md**
- Cenários.
- Ferramentas.
- Métricas.
- Critérios de aprovação.
- Estratégia para identificar gargalos.

**06-resumo-executivo.md**
- Diagnóstico geral.
- Principais riscos.
- Capacidade comprovada versus desconhecida.
- Recomendações prioritárias.
- Investimento técnico relativo.
- Caminho recomendado para suportar grandes entidades.

## 12. Priorização

Para cada recomendação, atribua:

- Criticidade: P0, P1 ou P2.
- Benefício esperado.
- Complexidade: baixa, média ou alta.
- Esforço estimado em faixa de dias, marcada como estimativa não medida, com a premissa que a sustenta. Não apresente um número como medição.
- Dependências.
- Risco de regressão.
- Necessidade de alteração de infraestrutura.
- Possibilidade de implementação incremental.

Separe claramente melhorias obrigatórias para segurança e consistência de melhorias destinadas apenas ao aumento de capacidade.

## 13. Resultado esperado

Ao final, responda objetivamente:

1. A arquitetura atual está preparada para grandes entidades?
2. Quais são os cinco maiores riscos de escalabilidade?
3. O que aconteceria se 100.000 XMLs fossem importados simultaneamente?
4. O que aconteceria se 500 usuários transmitissem declarações ao mesmo tempo?
5. O sistema consegue operar com três instâncias simultâneas sem duplicar transmissões?
6. Como impedir que uma entidade sobrecarregue as demais?
7. Qual arquitetura oferece o melhor equilíbrio entre escalabilidade, custo e manutenção?
8. Quais melhorias devem ser implementadas primeiro?
9. Quais capacidades dependem de testes e não podem ser afirmadas apenas pela leitura do código?
10. Qual seria o plano de evolução mais seguro para comercializar o sistema para grandes entidades?

**Importante: nesta etapa, faça somente a análise e os markdowns da seção 11. Não refatore nem implemente alterações.**

Comece mapeando a arquitetura real do repositório. Depois investigue os gargalos e somente então proponha a arquitetura futura.