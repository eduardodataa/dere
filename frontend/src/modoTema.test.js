import assert from 'node:assert/strict';
import test from 'node:test';
import { CHAVE_TEMA, gravarModoTema, lerModoTema } from './modoTema.js';

function memoria(inicial) {
  const dados = { ...inicial };
  return {
    getItem(chave) { return Object.prototype.hasOwnProperty.call(dados, chave) ? dados[chave] : null; },
    setItem(chave, valor) { dados[chave] = valor; },
  };
}

test('tema claro é o padrão', () => {
  assert.equal(lerModoTema(memoria()), 'light');
  assert.equal(lerModoTema(null), 'light');
});

test('alterna e persiste claro e escuro', () => {
  const storage = memoria();
  gravarModoTema(storage, 'dark');
  assert.equal(lerModoTema(storage), 'dark');
  gravarModoTema(storage, 'light');
  assert.equal(lerModoTema(storage), 'light');
  assert.equal(storage.getItem(CHAVE_TEMA), 'light');
});

test('ignora valor inválido e falha de storage', () => {
  assert.equal(lerModoTema(memoria({ [CHAVE_TEMA]: 'azul' })), 'light');
  const quebrado = {
    getItem() { throw new Error('bloqueado'); },
    setItem() { throw new Error('bloqueado'); },
  };
  assert.equal(lerModoTema(quebrado), 'light');
  assert.doesNotThrow(() => gravarModoTema(quebrado, 'dark'));
});
