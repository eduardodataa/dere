package br.gov.dere.application.access;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class ControleTentativaLogin {
  public static final String MENSAGEM = "Muitas tentativas de login. Tente novamente em alguns minutos.";
  static final int LIMITE_LOGIN = 5;
  static final int LIMITE_IP = 30;
  private static final Duration JANELA = Duration.ofMinutes(15);
  private static final Duration BLOQUEIO = Duration.ofMinutes(15);
  private static final int TAMANHO_CHAVE = 80;

  private final Supplier<Instant> agora;
  private final ConcurrentHashMap<String, Contador> porLogin = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Contador> porIp = new ConcurrentHashMap<>();

  public ControleTentativaLogin() {
    this(Instant::now);
  }

  ControleTentativaLogin(Supplier<Instant> agora) {
    this.agora = agora;
  }

  public boolean bloqueado(String login, String ip) {
    var instante = agora.get();
    return ativo(porLogin.get(chave("login", login)), instante) || ativo(porIp.get(chave("ip", ip)), instante);
  }

  public void registrarFalha(String login, String ip) {
    var instante = agora.get();
    subir(porLogin, chave("login", login), LIMITE_LOGIN, instante);
    subir(porIp, chave("ip", ip), LIMITE_IP, instante);
  }

  public void registrarSucesso(String login, String ip) {
    porLogin.remove(chave("login", login));
    porIp.remove(chave("ip", ip));
  }

  public void limpar() {
    porLogin.clear();
    porIp.clear();
  }

  private void subir(ConcurrentHashMap<String, Contador> mapa, String chave, int limite, Instant instante) {
    var contador = mapa.computeIfAbsent(chave, ignorada -> new Contador());
    synchronized (contador) {
      if (contador.bloqueadoAte != null && instante.isBefore(contador.bloqueadoAte)) return;
      if (contador.inicio.plus(JANELA).isBefore(instante)) {
        contador.falhas = 0;
        contador.bloqueadoAte = null;
      }
      if (contador.falhas == 0) contador.inicio = instante;
      contador.falhas++;
      if (contador.falhas >= limite) contador.bloqueadoAte = instante.plus(BLOQUEIO);
    }
  }

  private static boolean ativo(Contador contador, Instant instante) {
    if (contador == null) return false;
    synchronized (contador) {
      return contador.bloqueadoAte != null && instante.isBefore(contador.bloqueadoAte);
    }
  }

  private static String chave(String tipo, String valor) {
    var texto = valor == null ? "" : valor.trim().toLowerCase();
    if (texto.length() > TAMANHO_CHAVE) texto = texto.substring(0, TAMANHO_CHAVE);
    return tipo + ":" + texto;
  }

  private static final class Contador {
    private int falhas;
    private Instant inicio = Instant.EPOCH;
    private Instant bloqueadoAte;
  }
}
