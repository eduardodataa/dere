package br.gov.dere.domain.periodico;

public record D1101Conta(
    String cCta,
    String natSaldoInic,
    String vSaldoInic,
    String vMovDebt,
    String vAjusteDebt,
    String vMovCred,
    String vAjusteCred,
    String natSaldoFinal,
    String vSaldoFinal,
    String natVApur,
    String vApur) {}
