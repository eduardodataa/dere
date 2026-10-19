import assert from 'node:assert/strict';
import test from 'node:test';
import { apresentarErroTransmissao, bloqueiaEnvioSimultaneo, deveRetentarAutomatico, interpretarErroTransmissao, tokensAlertaTransmissao } from './erroTransmissao.js';

test('MS0015 mostra texto contábil e código', () => {
  const view = apresentarErroTransmissao({
    categoria: 'REJEICAO_RECEITA',
    titulo: 'Não foi possível transmitir a declaração',
    mensagem: 'A Receita Federal não recebeu o lote.',
    orientacao: 'O arquivo passou pela validação local, mas o envio foi recusado. Consulte os detalhes do retorno antes de tentar novamente.',
    codigo: 'MS0015',
    descricaoOriginal: 'Lote não recebido',
    http: 422,
    correlacao: 'abc',
    repetir: false
  });
  assert.equal(view.titulo, 'Não foi possível transmitir a declaração');
  assert.equal(view.mensagem, 'A Receita Federal não recebeu o lote.');
  assert.equal(view.codigo, 'MS0015');
  assert.equal(view.repetir, false);
  assert.equal(view.mensagem.includes('<'), false);
  assert.equal(view.mensagem.includes('422'), false);
});

test('várias ocorrências entram no diagnóstico', () => {
  const view = apresentarErroTransmissao({
    codigo: 'MS0015',
    mensagem: 'A Receita Federal não recebeu o lote.',
    ocorrencias: [
      { codigo: '100', descricao: 'Campo ausente', localizacao: '/DeRE' },
      { codigo: '220', descricao: 'Competência inválida' }
    ]
  });
  assert.equal(view.detalhes.some(item => item.valor.includes('Campo ausente')), true);
  assert.equal(view.diagnostico.includes('Competência inválida'), true);
});

test('namespace permanece só no detalhe técnico', () => {
  const view = apresentarErroTransmissao({
    mensagem: 'A Receita Federal recusou o envio.',
    schema: 'http://www.receita.fazenda.gov.br/dere/lote/v1_2_0 1.2.0'
  });
  assert.equal(view.mensagem.includes('xmlns'), false);
  assert.equal(view.detalhes.some(item => item.rotulo === 'Schema'), true);
});

test('xml malformado não aparece na mensagem', () => {
  const view = apresentarErroTransmissao({ mensagem: '<retorno><codigo>MS0015</codigo></retorno>' });
  assert.equal(view.mensagem.includes('<'), false);
});

test('resposta sem código continua genérica', () => {
  const view = apresentarErroTransmissao({ mensagem: 'A Receita Federal recusou o envio.', descricaoOriginal: 'falha interna' });
  assert.equal(view.codigo, '');
  assert.equal(view.mensagem, 'A Receita Federal recusou o envio.');
  assert.equal(view.detalhes.some(item => item.valor === 'falha interna'), true);
});

test('http 422 estruturado', () => {
  const erro = interpretarErroTransmissao({ response: { status: 422, data: { message: 'A Receita Federal não recebeu o lote.', erro: { codigo: 'MS0015', http: 422, mensagem: 'A Receita Federal não recebeu o lote.', categoria: 'REJEICAO_RECEITA', repetir: false } } } });
  assert.equal(erro.codigo, 'MS0015');
  assert.equal(erro.http, 422);
  assert.equal(apresentarErroTransmissao(erro).mensagem.includes('422'), false);
});

test('erro de comunicação sem resposta não libera reenvio', () => {
  const erro = interpretarErroTransmissao({});
  assert.equal(erro.categoria, 'COMUNICACAO');
  assert.equal(erro.inconclusivo, true);
  assert.equal(deveRetentarAutomatico(erro), false);
});

test('timeout mantém resultado desconhecido', () => {
  const erro = interpretarErroTransmissao({ response: { status: 504, data: { message: 'A comunicação falhou antes de uma confirmação da Receita Federal.', erro: { categoria: 'COMUNICACAO', inconclusivo: true, repetir: false, mensagem: 'A comunicação falhou antes de uma confirmação da Receita Federal.' } } } });
  assert.equal(erro.inconclusivo, true);
  assert.equal(deveRetentarAutomatico(erro), false);
  assert.equal(apresentarErroTransmissao(erro).repetir, false);
});

test('envio simultâneo fica bloqueado', () => {
  assert.equal(bloqueiaEnvioSimultaneo(true), true);
  assert.equal(bloqueiaEnvioSimultaneo(false), false);
  assert.equal(deveRetentarAutomatico({ categoria: 'COMUNICACAO', repetir: true, inconclusivo: false }), true);
  assert.equal(deveRetentarAutomatico({ categoria: 'REJEICAO_RECEITA', repetir: true, inconclusivo: false }), false);
});

test('tema claro e escuro usam tokens do MUI', () => {
  for (const valor of Object.values(tokensAlertaTransmissao)) {
    assert.equal(/^#|rgb\(/i.test(valor), false);
  }
  assert.equal(tokensAlertaTransmissao.severidade, 'error');
  assert.equal(tokensAlertaTransmissao.apoio, 'text.secondary');
});
