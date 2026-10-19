export const tokensAlertaTransmissao = {
  severidade: 'error',
  texto: 'text.primary',
  apoio: 'text.secondary',
  divisor: 'divider'
};

export function bloqueiaEnvioSimultaneo(enviando) {
  return enviando === true;
}

export function deveRetentarAutomatico(erro) {
  return erro?.repetir === true && erro?.inconclusivo !== true && erro?.categoria === 'COMUNICACAO';
}

export function interpretarErroTransmissao(error) {
  const data = error?.response?.data;
  if (data?.erro) return normalizar(data.erro);
  if (!error?.response) {
    return normalizar({
      categoria: 'COMUNICACAO',
      titulo: 'Não foi possível confirmar o envio',
      mensagem: 'A comunicação falhou antes de uma confirmação da Receita Federal.',
      orientacao: 'O resultado da transmissão é desconhecido. Não envie de novo até confirmar se o lote foi recebido.',
      inconclusivo: true,
      repetir: false
    });
  }
  const status = error.response.status;
  const texto = [data?.message, data?.detail].find(item => item && !pareceXml(item) && !tituloHttp(item));
  const inconclusivo = status === 408 || status === 504 || status === 502 || status === 500;
  return normalizar({
    categoria: inconclusivo ? 'COMUNICACAO' : 'REJEICAO_RECEITA',
    titulo: inconclusivo ? 'Não foi possível confirmar o envio' : 'Não foi possível transmitir a declaração',
    mensagem: texto || (inconclusivo
      ? 'A comunicação falhou antes de uma confirmação da Receita Federal.'
      : 'A Receita Federal recusou o envio.'),
    orientacao: inconclusivo
      ? 'O resultado da transmissão é desconhecido. Não envie de novo até confirmar se o lote foi recebido.'
      : 'Consulte os detalhes técnicos e encaminhe o diagnóstico ao suporte antes de um novo envio.',
    http: status,
    inconclusivo,
    repetir: false
  });
}

export function diagnosticoRetornoReceita(xml) {
  const retorno = lerRetornoReceita(xml);
  const principal = retorno.ocorrencias[0];
  if (!principal) return null;
  const codigo = principal.codigo || '';
  const conhecido = mensagensReceita[codigo];
  return {
    categoria: 'REJEICAO_RECEITA',
    titulo: conhecido?.titulo || 'A Receita Federal rejeitou a declaração',
    mensagem: conhecido?.mensagem || principal.descricao || 'A Receita Federal recusou o evento.',
    orientacao: conhecido?.orientacao || 'Corrija o arquivo conforme a descrição da Receita e envie novamente. O protocolo confirma apenas a recepção do lote.',
    codigo,
    descricaoOriginal: principal.descricao || retorno.descResposta,
    protocolo: retorno.protocolo,
    status: retorno.cdResposta,
    ocorrencias: retorno.ocorrencias,
    repetir: false,
    inconclusivo: false
  };
}

export function lerRetornoReceita(xml) {
  const texto = String(xml || '');
  const blocos = [...blocosTag(texto, 'ocorrencia'), ...blocosTag(texto, 'ocorrencias')];
  const ocorrencias = blocos.map(bloco => ({
    codigo: tagInterna(bloco, 'codigo'),
    descricao: tagInterna(bloco, 'descricao'),
    tipo: tagInterna(bloco, 'tipo'),
    localizacao: tagInterna(bloco, 'localizacao')
  })).filter(item => item.codigo || item.descricao);
  return {
    cdResposta: tagInterna(texto, 'cdResposta'),
    descResposta: tagInterna(texto, 'descResposta'),
    protocolo: tagInterna(texto, 'protocolo'),
    ocorrencias
  };
}

const mensagensReceita = {
  MS0015: {
    titulo: 'Não foi possível transmitir a declaração',
    mensagem: 'A Receita Federal não recebeu o lote.',
    orientacao: 'O arquivo passou pela validação local, mas o envio foi recusado. Consulte os detalhes do retorno antes de tentar novamente.'
  },
  MS1050: {
    titulo: 'A Receita Federal rejeitou a declaração',
    mensagem: 'O identificador do evento não está no formato exigido.',
    orientacao: 'O identificador precisa ter 42 caracteres: DeRE, o código do evento, o tipo de inscrição, o CNPJ com 14 dígitos, a data, a hora e um sequencial. Corrija o arquivo e envie novamente.'
  }
};

function blocosTag(xml, nome) {
  const encontrados = [];
  const expressao = new RegExp(`<(?:[\\w.-]+:)?${nome}\\b[^>]*>([\\s\\S]*?)</(?:[\\w.-]+:)?${nome}>`, 'gi');
  let achado = expressao.exec(xml);
  while (achado) {
    encontrados.push(achado[1]);
    achado = expressao.exec(xml);
  }
  return encontrados;
}

function tagInterna(xml, nome) {
  const achado = new RegExp(`<(?:[\\w.-]+:)?${nome}\\b[^>]*>([\\s\\S]*?)</(?:[\\w.-]+:)?${nome}>`, 'i').exec(xml);
  if (!achado) return '';
  return achado[1]
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&quot;/g, '"')
    .replace(/&apos;/g, "'")
    .replace(/&amp;/g, '&')
    .trim();
}

export function apresentarErroTransmissao(erro) {
  const atual = normalizar(erro);
  const detalhes = [
    ['Categoria', rotuloCategoria(atual.categoria)],
    ['HTTP', atual.http],
    ['Código', atual.codigo],
    ['Descrição original', atual.descricaoOriginal],
    ['Identificador', atual.identificador],
    ['Protocolo', atual.protocolo],
    ['Status', atual.status],
    ['Schema', atual.schema],
    ['Correlação', atual.correlacao]
  ].filter(([, valor]) => valor !== undefined && valor !== null && String(valor).trim() !== '')
    .map(([rotulo, valor]) => ({ rotulo, valor: String(valor) }));
  const ocorrencias = (atual.ocorrencias || []).map(item => ({
    rotulo: item.codigo ? `Ocorrência ${item.codigo}` : 'Ocorrência',
    valor: [item.descricao, item.localizacao].filter(Boolean).join(' · ')
  })).filter(item => item.valor);
  return {
    titulo: atual.titulo,
    mensagem: atual.mensagem,
    orientacao: atual.orientacao,
    codigo: atual.codigo || '',
    repetir: atual.repetir === true && atual.inconclusivo !== true,
    detalhes: [...detalhes, ...ocorrencias],
    diagnostico: textoDiagnostico([...detalhes, ...ocorrencias])
  };
}

function normalizar(erro) {
  const atual = erro || {};
  return {
    categoria: atual.categoria || 'REJEICAO_RECEITA',
    titulo: atual.titulo || 'Não foi possível transmitir a declaração',
    mensagem: pareceXml(atual.mensagem) ? 'A Receita Federal recusou o envio.' : (atual.mensagem || 'A Receita Federal recusou o envio.'),
    orientacao: atual.orientacao || 'Consulte os detalhes técnicos e encaminhe o diagnóstico ao suporte antes de um novo envio.',
    codigo: atual.codigo || '',
    descricaoOriginal: pareceXml(atual.descricaoOriginal) ? 'O retorno não pôde ser interpretado.' : (atual.descricaoOriginal || ''),
    http: atual.http,
    identificador: atual.identificador || '',
    protocolo: atual.protocolo || '',
    status: atual.status || '',
    schema: atual.schema || '',
    ocorrencias: Array.isArray(atual.ocorrencias) ? atual.ocorrencias : [],
    correlacao: atual.correlacao || '',
    repetir: atual.repetir === true,
    inconclusivo: atual.inconclusivo === true
  };
}

function textoDiagnostico(detalhes) {
  return ['Diagnóstico DeRE', ...detalhes.map(item => `${item.rotulo}: ${item.valor}`)].join('\n');
}

function rotuloCategoria(categoria) {
  const rotulos = {
    VALIDACAO_LOCAL: 'Validação local',
    AUTENTICACAO: 'Autenticação',
    CERTIFICADO: 'Certificado digital',
    COMUNICACAO: 'Comunicação',
    REJEICAO_RECEITA: 'Rejeição da Receita Federal'
  };
  return rotulos[categoria] || categoria;
}

function pareceXml(valor) {
  return /<\s*[A-Za-z][^>]*>/.test(String(valor || ''));
}

function tituloHttp(valor) {
  return /unprocessable|internal server error|bad gateway|bad request|conflict|not found|forbidden|unauthorized/i.test(String(valor || ''));
}
