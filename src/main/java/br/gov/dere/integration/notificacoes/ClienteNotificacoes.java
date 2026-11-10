package br.gov.dere.integration.notificacoes;

public interface ClienteNotificacoes {
  void enviarEmail(String paraNome, String paraEmail, String assunto, String html);
}
