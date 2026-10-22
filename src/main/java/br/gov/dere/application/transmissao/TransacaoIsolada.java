package br.gov.dere.application.transmissao;

import java.util.function.Supplier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Component
public class TransacaoIsolada {
  private final TransactionTemplate template;

  public TransacaoIsolada(PlatformTransactionManager manager) {
    if (manager == null) {
      template = null;
      return;
    }
    template = new TransactionTemplate(manager);
    template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
  }

  public static TransacaoIsolada direta() {
    return new TransacaoIsolada(null);
  }

  public <T> T executar(Supplier<T> acao) {
    if (template == null) return acao.get();
    return template.execute(status -> acao.get());
  }
}
