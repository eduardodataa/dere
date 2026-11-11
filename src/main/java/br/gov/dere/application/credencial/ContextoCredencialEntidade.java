package br.gov.dere.application.credencial;

import java.util.concurrent.Callable;

public final class ContextoCredencialEntidade {
  private static final ThreadLocal<Long> ATUAL = new ThreadLocal<>();

  private ContextoCredencialEntidade() {}

  public static Long atual() {
    return ATUAL.get();
  }

  public static void executar(Long entityId, Runnable acao) {
    var anterior = ATUAL.get();
    ATUAL.set(entityId);
    try {
      acao.run();
    } finally {
      restaurar(anterior);
    }
  }

  public static <T> T executar(Long entityId, Callable<T> acao) {
    var anterior = ATUAL.get();
    ATUAL.set(entityId);
    try {
      return acao.call();
    } catch (RuntimeException | Error ex) {
      throw ex;
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    } finally {
      restaurar(anterior);
    }
  }

  private static void restaurar(Long anterior) {
    if (anterior == null) ATUAL.remove();
    else ATUAL.set(anterior);
  }
}
