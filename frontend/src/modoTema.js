export const CHAVE_TEMA = 'dere-theme-mode';

export function lerModoTema(storage) {
  try {
    const valor = storage?.getItem(CHAVE_TEMA);
    if (valor === 'light' || valor === 'dark') return valor;
  } catch {
    return 'light';
  }
  return 'light';
}

export function gravarModoTema(storage, modo) {
  if (modo !== 'light' && modo !== 'dark') return;
  try {
    storage?.setItem(CHAVE_TEMA, modo);
  } catch {
    return;
  }
}
