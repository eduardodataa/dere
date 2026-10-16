// @ts-nocheck
import React, { useEffect, useRef, useState } from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import { Alert, Box, Button, Card, CardContent, Chip, CircularProgress, Collapse, Divider, Drawer, FormControlLabel, Grid, LinearProgress, List, ListItemButton, ListItemText, MenuItem, Paper, Stack, Switch, Tab, Tabs, TextField, Tooltip, Typography } from '@mui/material';
import './style.css';
import logotipoDataA from './dataa-logotipo.png';
import seloDere from './dere-selo.jpg';

const apiPrefix = (window.location.pathname.match(/^\/dere(?:-stg|-hml)?(?=\/|$)/) || [''])[0];
const api = axios.create();
api.interceptors.request.use((config) => {
  const url = config.url || '';
  const root = `/${'api'}/`;
  if (url.startsWith(root) && !url.startsWith(`${apiPrefix}${root}`)) {
    config.url = `${apiPrefix}${url}`;
  }
  return config;
});

function nomeCsvDaOrigem(nome) {
  if (!nome) return 'convertido.csv';
  const base = nome.replace(/^.*[\\/]/, '');
  return base.replace(/\.(xml|zip)$/i, '.csv');
}

function raizCnpj(valor) {
  const digitos = String(valor || '').replace(/\D/g, '');
  return digitos.length >= 8 ? digitos.slice(0, 8) : '';
}

function gravidadeAviso(texto) {
  if (/falha|inválid|obrigatór|informe|erro|unprocessable|recus/i.test(texto)) return 'error';
  if (/sucesso|salva|excluíd|cadastrad|atualizad|armazenad|enviado\.|validado/i.test(texto)) return 'success';
  return 'info';
}

function textoErroConsulta(error) {
  const data = error.response?.data;
  const bruto = data?.message && data.message !== data.error ? data.message : data?.detail;
  if (!bruto) return 'Falha ao consultar o protocolo na Receita.';
  if (/Duplicate entry|SQL|constraint|could not execute|uk_transmission_attempt/i.test(bruto)) {
    return 'Não foi possível gravar esta consulta do protocolo. Consulte de novo para atualizar o retorno.';
  }
  return bruto;
}

const TENTATIVAS_ENVIO = 3;
const ESPERA_RETRY_MS = 1500;

function tituloHttp(valor) {
  return /unprocessable|internal server error|bad gateway|bad request|conflict|not found|forbidden|unauthorized/i.test(String(valor || ''));
}

function textoErroEnvio(error) {
  if (!error.response) return 'Falha de comunicação com a Receita Federal. Verifique a conexão e tente novamente.';
  const data = error.response.data;
  const texto = [data?.message, data?.detail, data?.title, data?.error].find(item => item && !tituloHttp(item));
  if (texto) {
    if (/Duplicate entry|SQL|constraint|could not execute|uk_transmission_attempt/i.test(texto)) {
      return 'O envio chegou à Receita, mas não foi possível gravar o retorno local. Tente novamente.';
    }
    return texto;
  }
  const status = error.response.status;
  if (status === 502 || status === 503 || status === 504) return 'A Receita Federal não respondeu. Tente novamente.';
  if (status === 408 || status === 429) return 'A Receita Federal está ocupada ou a conexão expirou. Tente novamente.';
  return 'Falha no envio à Receita Federal.';
}

function falhaComunicacao(error) {
  if (!error.response) return true;
  const status = error.response.status;
  return status === 408 || status === 429 || status === 502 || status === 503 || status === 504;
}

function esperar(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function baixar(nome, conteudo) {
  const tipo = nome.endsWith('.xml') ? 'application/xml;charset=utf-8' : 'text/csv;charset=utf-8';
  baixarBlob(nome, new Blob([conteudo], { type: tipo }));
}

function baixarBlob(nome, blob) {
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = nome;
  link.click();
  URL.revokeObjectURL(url);
}

function rotuloCodificacao(codificacao) {
  return codificacao === 'ANSI' ? 'windows-1252' : 'utf-8';
}

async function lerTextoArquivo(arquivo, codificacao) {
  const bytes = await arquivo.arrayBuffer();
  return new TextDecoder(rotuloCodificacao(codificacao)).decode(bytes);
}

function nomeXlsx(nome, leiaute) {
  const base = (nome || leiaute || 'validacao').replace(/^.*[\\/]/, '').replace(/\.(csv|xml|zip|xlsx)$/i, '');
  return `${base}-criticas.xlsx`;
}

function baixarXlsx(nome, codificado) {
  if (!codificado) return;
  const bytes = Uint8Array.from(atob(codificado), caractere => caractere.charCodeAt(0));
  const url = URL.createObjectURL(new Blob([bytes], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }));
  const link = document.createElement('a');
  link.href = url;
  link.download = nome;
  link.click();
  URL.revokeObjectURL(url);
}

const ROTULO_TIPO = { OBRIGATORIO: 'Obrigatório', FORA_DO_PADRAO: 'Fora do padrão', FORA_DO_DOMINIO: 'Fora de domínio', NEGOCIO: 'Regra de negócio', ESTRUTURA: 'Estrutura do arquivo' };
const COR_TIPO = { OBRIGATORIO: 'warning', FORA_DO_PADRAO: 'warning', FORA_DO_DOMINIO: 'error', NEGOCIO: 'secondary', ESTRUTURA: 'default' };
const CHAVE_MENUS = 'dere.menus-visiveis';

function lerMenusVisiveis() {
  try {
    const bruto = localStorage.getItem(CHAVE_MENUS);
    if (!bruto) return {};
    const lido = JSON.parse(bruto);
    if (!lido || typeof lido !== 'object') return {};
    const renomeados = { 'D-1001': 'Enviar D-1001', 'D-1011': 'Enviar D-1011', 'D-1101': 'Enviar D-1101', 'D-1199': 'Enviar D-1199' };
    Object.entries(renomeados).forEach(([antigo, novo]) => { if (lido[antigo] !== undefined && lido[novo] === undefined) lido[novo] = lido[antigo]; });
    return lido;
  } catch {
    return {};
  }
}

function PainelConfiguracoes({ api, uid, master, itens, visivel, onChange, setMessage }) {
  const [aba, setAba] = useState(0);
  const [integracao, setIntegracao] = useState({
    realAdapterEnabled: false,
    mockEnabled: true,
    baseUrl: 'https://api.receitafederal.gov.br/dere',
    tokenUrl: 'https://api.receitafederal.gov.br/token',
    clientId: '',
    clientSecret: '',
    clientSecretConfigured: false,
    environment: 'restricted-production',
    schemaVersion: 'nota_2026_001',
  });
  const [carregando, setCarregando] = useState(false);
  const [implantacao, setImplantacao] = useState('local');
  const canalTravado = implantacao === 'staging' || implantacao === 'homologacao' || implantacao === 'producao';
  const avisoCanal = implantacao === 'staging'
    ? 'Neste ambiente o envio é somente simulado.'
    : implantacao === 'homologacao'
      ? 'Neste ambiente o envio é somente de pré-produção.'
      : implantacao === 'producao'
        ? 'Neste ambiente o envio é somente de produção.'
        : '';

  useEffect(() => {
    if (!uid || !master) return;
    api.get('/api/settings/integration', { headers: { 'X-User-Id': uid } })
      .then(response => setIntegracao(atual => ({ ...atual, ...response.data, clientSecret: '' })))
      .catch(() => setMessage('Falha ao carregar configurações de integração.'));
  }, [api, uid, master, setMessage]);
  useEffect(() => {
    const daUrl = ambienteDaUrl();
    if (daUrl.codigo !== 'local') setImplantacao(daUrl.codigo);
    api.get('/api/ambiente').then(response => {
      if (daUrl.codigo === 'local' && response.data?.codigo) setImplantacao(response.data.codigo);
    }).catch(() => {});
  }, [api]);

  async function salvarIntegracao() {
    if (!master) { setMessage('Apenas usuário master altera a integração.'); return; }
    setCarregando(true);
    try {
      const corpo = { ...integracao };
      if (!corpo.clientSecret) delete corpo.clientSecret;
      const response = await api.put('/api/settings/integration', corpo, { headers: { 'X-User-Id': uid } });
      setIntegracao(atual => ({ ...atual, ...response.data, clientSecret: '' }));
      setMessage('Configurações de integração salvas.');
    } catch (error) {
      setMessage(error.response?.data?.message || 'Falha ao salvar integração.');
    } finally {
      setCarregando(false);
    }
  }

  const abaMenus = <Card><CardContent><Stack spacing={2}><Typography variant="h6">Menus da barra lateral</Typography><Typography color="text.secondary">Marque o que deve aparecer no menu. Configurações permanece sempre visível.</Typography>{itens.filter(item => item !== 'Configurações').map(item => <FormControlLabel key={item} control={<Switch checked={visivel[item] !== false} onChange={event => onChange(item, event.target.checked)} />} label={item} />)}</Stack></CardContent></Card>;

  const abaIntegracao = <Card><CardContent><Stack spacing={2}>
    <Typography variant="h6">Integração com a Receita Federal</Typography>
    <Typography color="text.secondary">Parâmetros do envio. O adaptador real usa OAuth (Receita Integra) e as URLs oficiais. O client secret não é reexibido após salvar.</Typography>
    {!master ? <Alert severity="warning">Somente usuário master pode alterar estes parâmetros.</Alert> : null}
    {avisoCanal ? <Alert severity="info">{avisoCanal}</Alert> : null}
    <FormControlLabel control={<Switch checked={!!integracao.realAdapterEnabled} disabled={!master || canalTravado} onChange={event => setIntegracao(atual => ({ ...atual, realAdapterEnabled: event.target.checked }))} />} label="Adaptador real (Receita Federal)" />
    <FormControlLabel control={<Switch checked={!!integracao.mockEnabled} disabled={!master || canalTravado || !!integracao.realAdapterEnabled} onChange={event => setIntegracao(atual => ({ ...atual, mockEnabled: event.target.checked }))} />} label="Envio simulado (mock)" />
    <TextField label="URL base" fullWidth disabled={!master || canalTravado} value={integracao.baseUrl || ''} onChange={event => setIntegracao(atual => ({ ...atual, baseUrl: event.target.value }))} helperText="Ex.: https://api.receitafederal.gov.br/prr-dere" />
    <TextField label="URL do token (Receita Integra)" fullWidth disabled={!master} value={integracao.tokenUrl || ''} onChange={event => setIntegracao(atual => ({ ...atual, tokenUrl: event.target.value }))} />
    <TextField label="Client ID" fullWidth disabled={!master} value={integracao.clientId || ''} onChange={event => setIntegracao(atual => ({ ...atual, clientId: event.target.value }))} />
    <TextField label="Client secret" type="password" fullWidth disabled={!master} value={integracao.clientSecret || ''} onChange={event => setIntegracao(atual => ({ ...atual, clientSecret: event.target.value }))} helperText={integracao.clientSecretConfigured ? 'Já há um secret cadastrado. Preencha só para substituir.' : 'Informe o secret obtido no Receita Integra.'} />
    <TextField label="Ambiente" fullWidth disabled={!master || canalTravado} value={integracao.environment || ''} onChange={event => setIntegracao(atual => ({ ...atual, environment: event.target.value }))} />
    <TextField label="Versão de schema" fullWidth disabled={!master} value={integracao.schemaVersion || ''} onChange={event => setIntegracao(atual => ({ ...atual, schemaVersion: event.target.value }))} />
    <Box><Button variant="contained" disabled={!master || carregando} onClick={salvarIntegracao}>{carregando ? 'Salvando…' : 'Salvar integração'}</Button></Box>
  </Stack></CardContent></Card>;

  return <Stack spacing={2}><Tabs value={aba} onChange={(_, valor) => setAba(valor)}><Tab label="Menus" /><Tab label="Integração Receita" /></Tabs>{aba === 0 ? abaMenus : abaIntegracao}</Stack>;
}

function PainelSegurancaCertificado() {
  const [aberto, setAberto] = useState(false);
  return <Paper variant="outlined" sx={{ p: 2, background: '#f8fafc' }}><Stack spacing={1.5}><Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}><Typography variant="subtitle1">Por que é seguro anexar seu certificado aqui?</Typography><Button variant="outlined" onClick={() => setAberto(atual => !atual)} sx={{ flexShrink: 0 }}>{aberto ? 'Recolher' : 'Como funciona'}</Button></Stack><Typography color="text.secondary">A chave privada não é enviada à Receita Federal. O sistema transmite apenas o documento já assinado. A senha do certificado é custodiada no AWS Secrets Manager, protegida por AWS KMS, e permanece separada do arquivo. Seu uso ocorre somente em memória, no instante da assinatura. Esse modelo de custódia é o padrão adotado por instituições financeiras e o que há de mais consolidado no mercado para proteção de segredos em nuvem.</Typography><Collapse in={aberto}><Stack spacing={1.5} sx={{ pt: 1 }}><Typography variant="subtitle2">1. Informar a senha</Typography><Typography color="text.secondary">A senha é enviada uma única vez, no mesmo pedido do arquivo, por canal protegido. Este formulário não a retém e ela nunca volta a ser exibida.</Typography><Typography variant="subtitle2">2. Conferência do arquivo</Typography><Typography color="text.secondary">O serviço verifica, em memória, se o arquivo é um certificado digital válido e se a senha corresponde. Havendo inconsistência, nada é armazenado.</Typography><Typography variant="subtitle2">3. Custódia da senha e do arquivo</Typography><Typography color="text.secondary">O arquivo do certificado permanece cifrado no ambiente da aplicação. A senha é mantida em cofre gerenciado (AWS Secrets Manager), com criptografia de ponta de mercado provida pelo AWS KMS — o mesmo padrão de proteção de chaves adotado por bancos e demais instituições financeiras. Arquivo e senha não tramitam juntos nem compartilham o mesmo repositório de dados de negócio.</Typography><Typography variant="subtitle2">4. Controle de acesso</Typography><Typography color="text.secondary">O certificado fica vinculado à entidade. Somente usuários autorizados dessa entidade podem utilizá-lo. A exclusão o desativa imediatamente.</Typography><Typography variant="subtitle2">5. Assinatura da declaração</Typography><Typography color="text.secondary">No envio, o certificado é aberto apenas em memória e o documento é assinado com criptografia de ponta de mercado. A Receita recebe o XML autenticado e o certificado público necessário à verificação. A chave privada não integra o arquivo transmitido.</Typography><Typography variant="subtitle2">6. Encaminhamento à Receita</Typography><Typography color="text.secondary">O lote contém o documento assinado. A chave privada permanece no ambiente da aplicação somente durante essa operação e não é retransmitida.</Typography><Typography variant="body2">Anexe apenas certificado A1 de sua titularidade. Não compartilhe o arquivo nem a senha por canais não seguros.</Typography></Stack></Collapse></Stack></Paper>;
}

function App() {
  const [uid, setUid] = useState<number>();
  const [master, setMaster] = useState(false);
  const [login, setLogin] = useState('eduardo');
  const [password, setPassword] = useState('eduardo86');
  const [page, setPage] = useState('Dashboard');
  const [entities, setEntities] = useState<any[]>([]);
  const [eid, setEid] = useState(1);
  const [message, setMessage] = useState('');
  const [certificados, setCertificados] = useState<any[]>([]);
  const [certFile, setCertFile] = useState<File>();
  const [certLabel, setCertLabel] = useState('');
  const [certPassword, setCertPassword] = useState('');
  const [menusVisiveis, setMenusVisiveis] = useState(lerMenusVisiveis);

  const load = async () => {
    try {
      const response = await api.get('/api/admin/entities');
      const list = Array.isArray(response.data) ? response.data : [];
      setEntities(list);
      if (list[0]) setEid(current => list.some(entity => entity.id === current) ? current : list[0].id);
    } catch { setMessage('Falha ao carregar entidades'); }
  };
  useEffect(() => { api.get('/api/admin/entities').then(response => setEntities(Array.isArray(response.data) ? response.data : [])).catch(() => {}); }, []);
  useEffect(() => { if (uid && eid) api.get(`/api/certificates/entity/${eid}`).then(response => setCertificados(Array.isArray(response.data) ? response.data : [])).catch(() => {}); }, [uid, eid]);
  useEffect(() => { if (message) window.scrollTo({ top: 0, behavior: 'smooth' }); }, [message]);
  useEffect(() => {
    if (uid && !master && (page === 'Configurações' || page === 'Entidades' || page === 'Usuários')) setPage('Dashboard');
  }, [uid, master, page]);

  async function signIn() {
    try { const response = await api.post('/api/auth/login', { login, password }); setUid(response.data.userId); setMaster(!!response.data.master); api.defaults.headers.common['X-User-Id'] = response.data.userId; setMessage(''); await load(); }
    catch { setMessage('Login inválido'); }
  }
  function signOut() {
    delete api.defaults.headers.common['X-User-Id'];
    setUid(undefined);
    setMaster(false);
    setPassword('');
    setMessage('');
    setPage('Dashboard');
  }
  const entidadeTravada = !master && entities.length <= 1;
  async function recarregarCertificados(entidadeId = eid) {
    const response = await api.get(`/api/certificates/entity/${entidadeId}`, { headers: { 'X-User-Id': uid, 'X-Entity-Id': entidadeId } });
    setCertificados(Array.isArray(response.data) ? response.data : []);
  }
  async function armazenarCertificado() { if (!certFile || !certPassword || !certLabel) { setMessage('Informe nome, arquivo P12 e senha.'); return; } const form = new FormData(); form.append('label', certLabel); form.append('file', certFile); form.append('password', certPassword); try { await api.post(`/api/certificates/entity/${eid}`, form, { headers: { 'Content-Type': 'multipart/form-data', 'X-User-Id': uid, 'X-Entity-Id': eid } }); setCertPassword(''); setCertFile(undefined); setCertLabel(''); await recarregarCertificados(); setMessage('Certificado armazenado com sucesso.'); } catch (error) { setMessage(error.response?.data?.message || 'Falha ao armazenar certificado.'); } }
  async function excluirCertificado(cert) {
    if (!window.confirm(`Excluir o certificado ${cert.label}?`)) return;
    try {
      await api.delete(`/api/certificates/${cert.id}`, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      await recarregarCertificados();
      setMessage('Certificado excluído.');
    } catch (error) { setMessage(error.response?.data?.message || 'Falha ao excluir certificado.'); }
  }

  if (!uid) return <Box className="login-shell"><Stack className="login-bloco" alignItems="center" spacing={3}><img className="selo-login" src={seloDere} alt="DeRE — Declaração de Regimes Específicos" /><Card className="login-card"><CardContent><Typography variant="h4" mb={1}>DeRE</Typography><Typography color="text.secondary" mb={3}>Declarações de Regimes Específicos</Typography><Stack spacing={2}><TextField label="Login" value={login} onChange={event => setLogin(event.target.value)} /><TextField label="Senha" type="password" value={password} onChange={event => setPassword(event.target.value)} /><Button variant="contained" size="large" onClick={signIn}>Entrar</Button>{message && <Alert severity="error">{message}</Alert>}</Stack></CardContent></Card></Stack></Box>;

  const menu = ['Dashboard', 'Enviar D-1001', 'Enviar D-1011', 'Enviar D-1101', 'Enviar D-1199', 'Importações', 'Validações', 'Certificado Digital', 'Transmissões', ...(master ? ['Configurações', 'Entidades', 'Usuários'] : [])];
  const menuExibido = menu.filter(item => item === 'Configurações' || menusVisiveis[item] !== false);
  function alternarMenu(item, marcado) {
    setMenusVisiveis(atual => {
      const proximo = { ...atual, [item]: marcado };
      localStorage.setItem(CHAVE_MENUS, JSON.stringify(proximo));
      return proximo;
    });
    if (!marcado && page === item) setPage('Configurações');
  }
  const workspacePages = ['Enviar D-1001', 'Enviar D-1011', 'Enviar D-1101', 'Enviar D-1199'];
  const currentEntity = entities.find(entity => entity.id === eid);
  const workspaceProps = { api, uid, eid, entities, setEid, setMessage, master };
  const cartaoCertificado = <Card><CardContent><Stack spacing={2}><PainelSegurancaCertificado /><Typography variant="h6">Certificado A1</Typography><Typography color="text.secondary">A senha não é reexibida após o armazenamento. Ela permanece no AWS Secrets Manager, protegida por AWS KMS. O arquivo do certificado fica cifrado à parte e é utilizado somente para assinar a declaração.</Typography><TextField select fullWidth label="Entidade" value={eid} disabled={entidadeTravada} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField><TextField label="Nome" value={certLabel} onChange={event => setCertLabel(event.target.value)} /><Button component="label" variant="outlined">Selecionar P12/PFX<input hidden type="file" accept=".p12,.pfx" onChange={event => setCertFile(event.target.files?.[0])} /></Button>{certFile ? <Typography color="text.secondary">Arquivo: {certFile.name}</Typography> : null}<TextField label="Senha do certificado" type="password" value={certPassword} onChange={event => setCertPassword(event.target.value)} /><Button variant="contained" onClick={armazenarCertificado}>Armazenar certificado</Button><Divider />{certificados.map(cert => <Box key={cert.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: 2, borderBottom: '1px solid #e5e7eb', py: 1.5 }}><Box><Typography>{cert.label} · {cert.alias}</Typography><Typography variant="body2" color="text.secondary">Titular: {cert.subject || '—'}</Typography><Typography variant="body2" color="text.secondary">Emissor: {cert.issuer || '—'}</Typography><Typography variant="body2" color="text.secondary">Emissão: {formatarDataEnvio(cert.issuedAt)} · Validade: {formatarDataEnvio(cert.validUntil)}</Typography></Box><Stack direction="row" spacing={1} alignItems="center"><Chip color={cert.icpBrasil ? 'success' : 'default'} label={cert.icpBrasil ? 'ICP-Brasil' : 'Fora da ICP-Brasil'} /><Chip color={cert.status === 'VALID' ? 'success' : 'warning'} label={cert.status === 'EXPIRING_SOON' ? `vence em ${cert.daysUntilExpiry} dias` : cert.status} /><Button size="small" color="error" onClick={() => excluirCertificado(cert)}>Excluir</Button></Stack></Box>)}</Stack></CardContent></Card>;
  return <Box className="app-shell"><Drawer variant="permanent" className="sidebar"><Box className="brand"><img className="marca" src={logotipoDataA} alt="maps + Data A" /><Typography variant="h5">DeRE</Typography><Typography variant="caption">EFPC / PREVIC</Typography></Box><List>{menuExibido.map(item => <ListItemButton selected={page === item} onClick={() => { setPage(item); setMessage(''); }} key={item}><ListItemText primary={item} /></ListItemButton>)}</List></Drawer><Box component="main" className="content"><Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}><Box><Typography variant="h4">{page}</Typography><Typography color="text.secondary">Operação local da declaração</Typography></Box><Stack direction="row" spacing={1.5} alignItems="center"><Chip label={currentEntity?.legalName || 'Entidade não selecionada'} /><Button variant="outlined" onClick={signOut}>Sair</Button><img className="selo-topo" src={seloDere} alt="DeRE" /></Stack></Stack>{message && <Alert sx={{ mb: 3 }} severity={gravidadeAviso(message)}>{message}</Alert>}{page === 'Dashboard' ? <Dashboard onImport={() => setPage('Enviar D-1001')} /> : null}<Box hidden={page !== 'Enviar D-1001'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1001" /></Box><Box hidden={page !== 'Enviar D-1011'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1011" /></Box><Box hidden={page !== 'Enviar D-1101'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1101" /></Box><Box hidden={page !== 'Enviar D-1199'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1199" /></Box>{page === 'Importações' ? <Alert severity="info">A conversão CSV/XML fica em D-1001 e D-1011. Este menu será o histórico de lotes (arquivo, layout, entidade, status e críticas).</Alert> : null}{page === 'Validações' ? <Alert severity="info">Validações será o relatório das críticas já apuradas nos lotes. A validação de um arquivo novo é feita no menu do layout.</Alert> : null}{page === 'Transmissões' ? <PainelTransmissoes api={api} uid={uid} eid={eid} setMessage={setMessage} master={master} /> : null}{page === 'Configurações' && master ? <PainelConfiguracoes api={api} uid={uid} master={master} itens={menu} visivel={menusVisiveis} onChange={alternarMenu} setMessage={setMessage} /> : page === 'Certificado Digital' ? cartaoCertificado : page === 'Entidades' && master ? <EntityAdmin api={api} onChanged={load} setMessage={setMessage} master={master} /> : page === 'Usuários' && master ? <UserAdmin api={api} currentUserId={uid} onSelfMaster={setMaster} setMessage={setMessage} /> : !['Dashboard', 'Certificado Digital', 'Importações', 'Validações', 'Transmissões', 'Entidades', 'Configurações', ...workspacePages].includes(page) && !(page === 'Usuários' && master) ? <Alert severity="info">O módulo {page} está preparado para a próxima integração.</Alert> : null}</Box></Box>;
}

function ImportWorkspace({ api, uid, eid, entities, setEid, setMessage, lockedLayout, master }) {
  const [layout, setLayout] = useState(lockedLayout || 'D-1001');
  const [file, setFile] = useState();
  const [uploadResult, setUploadResult] = useState();
  const [csvResult, setCsvResult] = useState();
  const [csvText, setCsvText] = useState('');
  const [arquivoCsv, setArquivoCsv] = useState();
  const [codificacao, setCodificacao] = useState('UTF-8');
  const [validando, setValidando] = useState(false);
  const [aba, setAba] = useState(0);
  const [envio, setEnvio] = useState();
  const [listaEnvio, setListaEnvio] = useState(0);
  const [enviando, setEnviando] = useState(false);
  const [progressoEnvio, setProgressoEnvio] = useState('');
  const [erroEnvio, setErroEnvio] = useState('');
  const currentLayout = lockedLayout || layout;
  const critiques = (uploadResult?.arquivos || []).flatMap(arquivo => (arquivo.criticas || []).map(item => ({ arquivo: arquivo.nomeArquivo, idEvento: arquivo.idEvento, ...item })));
  const subtitle = lockedLayout === 'D-1001'
    ? 'Escolha um CSV, XML ou ZIP do D-1001. A validação começa ao selecionar o arquivo.'
    : lockedLayout === 'D-1011'
      ? 'Escolha um CSV ou XML do D-1011. A validação começa ao selecionar o arquivo.'
      : lockedLayout === 'D-1101'
        ? 'Escolha um CSV ou XML do D-1101 (Balancete Mensal). A validação começa ao selecionar o arquivo.'
        : lockedLayout === 'D-1199'
          ? 'Escolha um CSV ou XML do D-1199 (Fechamento Mensal). Somente inclusão.'
          : 'Escolha o leiaute e um CSV, XML ou ZIP. A validação começa ao selecionar o arquivo.';

  function endpointCsv(leiaute) {
    if (leiaute === 'D-1011') return '/api/layouts/d1011/validate-csv';
    if (leiaute === 'D-1101') return '/api/layouts/d1101/validate-csv';
    if (leiaute === 'D-1199') return '/api/layouts/d1199/validate-csv';
    return '/api/validation/csv-to-xml';
  }

  function endpointXml(leiaute) {
    if (leiaute === 'D-1011') return '/api/layouts/d1011/validate-xml';
    if (leiaute === 'D-1101') return '/api/layouts/d1101/validate-xml';
    if (leiaute === 'D-1199') return '/api/layouts/d1199/validate-xml';
    return '';
  }

  async function baixarModelo() {
    const codigo = currentLayout.toLowerCase().replace('-', '');
    const response = await api.get(`/api/layouts/${codigo}/modelo.csv`, { responseType: 'blob' });
    baixarBlob(`${codigo}-modelo.csv`, response.data);
  }

  async function validarCsvTexto(texto, nome) {
    if (!String(texto || '').trim()) { setMessage('Selecione um arquivo CSV.'); return; }
    setValidando(true);
    try {
      const endpoint = endpointCsv(currentLayout);
      const response = await api.post(endpoint, texto, { headers: { 'Content-Type': 'text/csv', 'X-Entity-Id': eid } });
      setCsvResult(response.data);
      setUploadResult(undefined);
      setMessage(response.data.valido ? `${nome || currentLayout} validado. O XML pode ser baixado ou enviado.` : `${nome || currentLayout} possui críticas.`);
    } catch (error) { setMessage(error.response?.data?.message || error.response?.data?.error || error.response?.data?.detail || 'Falha na validação CSV'); }
    finally { setValidando(false); }
  }

  async function validarArquivoXml(arquivo) {
    setValidando(true);
    try {
      const xmlEndpoint = endpointXml(currentLayout);
      if (xmlEndpoint) {
        if (arquivo.name.toLowerCase().endsWith('.zip')) { setMessage('Este leiaute valida um XML. O ZIP de lote fica no D-1001.'); return; }
        const xml = await arquivo.text();
        const response = await api.post(xmlEndpoint, xml, { headers: { 'Content-Type': 'application/xml', 'X-Entity-Id': eid } });
        setCsvResult(response.data);
        setUploadResult(undefined);
        setMessage(response.data.valido ? `XML ${currentLayout} validado.` : `XML ${currentLayout} possui críticas.`);
        return;
      }
      const form = new FormData();
      form.append('file', arquivo);
      const response = await api.post('/api/layouts/d1001/import', form, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      setUploadResult(response.data);
      setCsvResult(undefined);
      setMessage(response.data.quantidadeInvalidos ? 'Foram encontradas críticas no lote.' : 'Lote validado sem críticas.');
    } catch (error) { setMessage(error.response?.data?.message || error.response?.data?.error || error.response?.data?.detail || 'Falha no upload/validação'); }
    finally { setValidando(false); }
  }

  async function postarEnvio(origem, conteudo, nome) {
    let ultimo;
    for (let tentativa = 1; tentativa <= TENTATIVAS_ENVIO; tentativa++) {
      setProgressoEnvio(tentativa === 1
        ? 'Enviando à Receita Federal…'
        : `Falha de comunicação. Nova tentativa ${tentativa} de ${TENTATIVAS_ENVIO}…`);
      try {
        return await api.post('/api/transmissions/simulate', { layout: currentLayout, origem, arquivo: nome, conteudo }, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      } catch (error) {
        ultimo = error;
        if (!falhaComunicacao(error) || tentativa === TENTATIVAS_ENVIO) throw error;
        await esperar(ESPERA_RETRY_MS);
      }
    }
    throw ultimo;
  }

  async function enviarSimulado(origem, conteudo, nome) {
    const response = await postarEnvio(origem, conteudo, nome);
    setEnvio(response.data);
    setListaEnvio(atual => atual + 1);
    if (response.data.enviado) setMessage(`${currentLayout} enviado. Protocolo ${response.data.transmissao?.protocol}.`);
    else setMessage(motivoRecusa(response.data) || `${rotuloCanal(response.data?.transmissao)} recusado.`);
    return response.data;
  }

  async function enviarLoteValidado(lote, arquivo) {
    const eventos = (lote?.arquivos || []).filter(item => item.valido && item.xml);
    if (eventos.length) {
      let ok = 0;
      let ultimo;
      for (const item of eventos) {
        ultimo = await enviarSimulado('XML', item.xml, item.nomeArquivo || arquivo?.name);
        if (ultimo?.enviado) ok += 1;
        else break;
      }
      if (ok === eventos.length) setMessage(`${ok} evento(s) do lote enviados. Protocolo ${ultimo?.transmissao?.protocol || '—'}.`);
      else if (ultimo && !ultimo.enviado) setMessage(motivoRecusa(ultimo) || `Envio interrompido após ${ok} sucesso(s).`);
      return;
    }
    if (arquivo?.name?.toLowerCase().endsWith('.zip')) {
      setMessage('ZIP validado, mas sem XML disponível para simular. Processe o lote novamente.');
      return;
    }
    await enviarSimulado('XML', await arquivo.text(), arquivo.name);
  }

  async function dispararSimulacao() {
    if (arquivoCsv) {
      await enviarSimulado('CSV', csvText, arquivoCsv.name);
      return;
    }
    if (uploadResult && (file?.name?.toLowerCase().endsWith('.zip') || (uploadResult.arquivos || []).length)) {
      await enviarLoteValidado(uploadResult, file);
      return;
    }
    const conteudo = file ? await file.text() : csvResult?.xml;
    await enviarSimulado('XML', conteudo, nomeArquivo);
  }

  function limparResultados() {
    setCsvResult(undefined);
    setUploadResult(undefined);
    setEnvio(undefined);
    setErroEnvio('');
    setProgressoEnvio('');
    setMessage('');
  }

  function limparDados() {
    limparResultados();
    setFile(undefined);
    setArquivoCsv(undefined);
    setCsvText('');
  }

  async function carregarArquivo(event) {
    const selected = event.target.files?.[0];
    event.target.value = '';
    limparDados();
    if (!selected) return;
    const nome = selected.name.toLowerCase();
    if (nome.endsWith('.csv')) {
      setArquivoCsv(selected);
      const texto = await lerTextoArquivo(selected, codificacao);
      setCsvText(texto);
      await validarCsvTexto(texto, selected.name);
      return;
    }
    if (nome.endsWith('.xml') || nome.endsWith('.zip')) {
      setFile(selected);
      await validarArquivoXml(selected);
      return;
    }
    setMessage('Use um arquivo CSV, XML ou ZIP.');
  }

  async function alterarCodificacao(valor) {
    setCodificacao(valor);
    if (!arquivoCsv) return;
    const texto = await lerTextoArquivo(arquivoCsv, valor);
    setCsvText(texto);
    await validarCsvTexto(texto, arquivoCsv.name);
  }

  const criticasCsv = csvResult?.criticas || [];
  const criticasXml = uploadResult?.criticas || critiques;
  const nomeArquivo = arquivoCsv?.name || file?.name;
  const csvValido = Boolean(arquivoCsv) && csvResult?.valido === true && criticasCsv.length === 0;
  const xmlValido = Boolean(file) && ((csvResult?.valido === true && criticasCsv.length === 0) || (uploadResult && !uploadResult.quantidadeInvalidos && criticasXml.length === 0));
  const criticasAtivas = arquivoCsv ? criticasCsv : criticasXml;
  const resultadoAtivo = arquivoCsv ? csvResult : (csvResult || uploadResult);
  async function enviarArquivo() {
    setEnviando(true);
    setErroEnvio('');
    setProgressoEnvio('Enviando à Receita Federal…');
    setMessage('Enviando à Receita Federal…');
    try {
      await dispararSimulacao();
      setErroEnvio('');
    } catch (error) {
      const texto = textoErroEnvio(error);
      setErroEnvio(texto);
      setMessage(texto);
    } finally {
      setEnviando(false);
      setProgressoEnvio('');
    }
  }
  const painelEnvio = <Stack spacing={2}><Card><CardContent><Stack spacing={2}>
    <Typography variant="h6">Importação e validação</Typography>
    <Typography color="text.secondary">{subtitle}</Typography>
    {!lockedLayout && <TextField select label="Leiaute" value={layout} onChange={event => { setLayout(event.target.value); limparDados(); }}><MenuItem value="D-1001">D-1001 — Informações do contribuinte</MenuItem><MenuItem value="D-1011">D-1011 — PGCC</MenuItem><MenuItem value="D-1101">D-1101 — Balancete mensal</MenuItem><MenuItem value="D-1199">D-1199 — Fechamento mensal</MenuItem></TextField>}
    <TextField select fullWidth label="Entidade" value={eid} disabled={!master && entities.length <= 1} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField>
    <Stack direction="row" spacing={2} alignItems="center" flexWrap="wrap" useFlexGap>
      <Button variant="outlined" onClick={() => baixarModelo().catch(() => setMessage('Falha ao baixar o modelo CSV'))}>Baixar layout CSV</Button>
      <Button component="label" variant="contained" disabled={validando || enviando}>{validando ? 'Validando…' : 'Selecionar arquivo'}<input hidden type="file" accept=".csv,.xml,.zip" onChange={carregarArquivo} /></Button>
      {arquivoCsv && <TextField select label="Codificação" value={codificacao} onChange={event => alterarCodificacao(event.target.value)} sx={{ minWidth: 160 }}><MenuItem value="UTF-8">UTF-8</MenuItem><MenuItem value="ANSI">ANSI</MenuItem></TextField>}
      <Button variant="outlined" color="inherit" onClick={limparDados} disabled={enviando || (!nomeArquivo && !csvResult && !uploadResult)}>Limpar</Button>
    </Stack>
    <Typography color="text.secondary">{nomeArquivo ? `Arquivo: ${nomeArquivo}${arquivoCsv ? ` · ${codificacao}` : ''}` : 'Nenhum arquivo selecionado.'}</Typography>
    {(csvValido || xmlValido) && <Alert severity="success">{csvValido ? 'CSV válido. Baixe o XML ou envie à DeRE.' : 'Arquivo válido. O envio à Receita Federal está liberado.'}</Alert>}
    <Stack direction="row" spacing={2} flexWrap="wrap" useFlexGap>
      {csvValido && csvResult.xml && <Button variant="outlined" disabled={enviando} onClick={() => baixar(`${currentLayout.toLowerCase()}.xml`, csvResult.xml)}>Baixar XML</Button>}
      {csvValido && <Button variant="contained" disabled={enviando} onClick={enviarArquivo} startIcon={enviando ? <CircularProgress size={16} color="inherit" /> : null}>{enviando ? 'Enviando…' : 'Enviar XML a partir do CSV à DeRE-RFB'}</Button>}
      {xmlValido && <Button variant="contained" disabled={enviando} onClick={enviarArquivo} startIcon={enviando ? <CircularProgress size={16} color="inherit" /> : null}>{enviando ? 'Enviando…' : 'Enviar à Receita Federal'}</Button>}
    </Stack>
    {enviando && <Box><LinearProgress /><Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{progressoEnvio || 'Enviando à Receita Federal…'}</Typography></Box>}
    {erroEnvio && <Alert severity="error" action={<Button color="inherit" size="small" disabled={enviando} onClick={enviarArquivo}>Tentar novamente</Button>}>{erroEnvio}</Alert>}
    {envio && <EnvioSimuladoCard envio={envio} api={api} uid={uid} eid={eid} />}
    {criticasAtivas.length > 0 && resultadoAtivo && <ResumoResultado resultado={resultadoAtivo} aoRelatorio={resultadoAtivo.relatorioXlsx ? () => baixarXlsx(nomeXlsx(nomeArquivo, currentLayout), resultadoAtivo.relatorioXlsx) : undefined} criticas={criticasAtivas} />}
  </Stack></CardContent></Card><PainelTransmissoes api={api} uid={uid} eid={eid} setMessage={setMessage} layout={currentLayout} reloadToken={listaEnvio} master={master} /></Stack>;
  return <Stack spacing={2}>
    <Tabs value={aba} onChange={(_, valor) => setAba(valor)}><Tab label="Envio" /><Tab label="Consultas" /></Tabs>
    {aba === 0 ? painelEnvio : <PainelConsulta api={api} uid={uid} eid={eid} layout={currentLayout} />}
  </Stack>;
}

function PainelConsulta({ api, uid, eid, layout }) {
  const [protocolo, setProtocolo] = useState('');
  const [resultado, setResultado] = useState();
  const [erro, setErro] = useState('');
  const [consultando, setConsultando] = useState(false);
  async function consultar() {
    const numero = protocolo.trim();
    if (!numero) { setErro('Informe o protocolo.'); setResultado(undefined); return; }
    setConsultando(true);
    setErro('');
    try {
      const response = await api.post('/api/transmissions/consulta', { protocolo: numero }, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      setResultado(response.data);
    } catch (error) {
      setResultado(undefined);
      setErro(textoErroConsulta(error));
    } finally { setConsultando(false); }
  }
  const transmissao = resultado?.transmissao;
  const outroLeiaute = transmissao?.layout && layout && transmissao.layout !== layout;
  return <Card><CardContent><Stack spacing={2}>
    <Typography variant="h6">Consulta na Receita</Typography>
    <Typography color="text.secondary">Informe o protocolo de recepção do lote. A consulta usa o ambiente configurado em Integração Receita.</Typography>
    <Stack direction="row" spacing={2} alignItems="flex-start">
      <TextField label="Protocolo" value={protocolo} onChange={event => setProtocolo(event.target.value)} onKeyDown={event => { if (event.key === 'Enter') consultar(); }} sx={{ minWidth: 360 }} />
      <Button variant="contained" disabled={consultando} onClick={consultar}>{consultando ? 'Consultando…' : 'Consultar'}</Button>
    </Stack>
    {consultando && <Box><LinearProgress /><Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>Conectando à Receita Federal e consultando o protocolo…</Typography></Box>}
    {erro && <Alert severity="error">{erro}</Alert>}
    {resultado?.aviso && <Alert severity="info">{resultado.aviso}</Alert>}
    {resultado && <Stack spacing={1}>
      {outroLeiaute && <Alert severity="info">Este protocolo está gravado como {transmissao.layout}.</Alert>}
      <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
        <Chip color="info" label={`Protocolo ${resultado.protocol || '—'}`} />
        <Chip label={rotuloStatusEnvio(resultado.status)} />
        <Chip color={resultado.receiptNumber ? 'success' : 'warning'} label={resultado.receiptNumber ? `Recibo ${resultado.receiptNumber}` : 'Sem recibo'} />
        {resultado.codigoGoverno && <Chip label={`Código ${resultado.codigoGoverno}`} />}
      </Stack>
      {transmissao && <EnvioSimuladoCard envio={{ enviado: true, transmissao }} api={api} uid={uid} eid={eid} />}
      {!transmissao && resultado.returnXml && <TextField className="resizable-preview" multiline rows={8} label="Retorno da Receita" value={resultado.returnXml} InputProps={{ readOnly: true }} />}
    </Stack>}
  </Stack></CardContent></Card>;
}

function rotuloOperacaoEnvio(operacao) {
  if (operacao === '1' || operacao === 1) return 'Cadastro';
  if (operacao === '2' || operacao === 2) return 'Edição';
  if (operacao === '3' || operacao === 3) return 'Exclusão';
  return operacao || '—';
}

function rotuloStatusEnvio(status) {
  if (status === 'PROCESSING' || status === 'PROTOCOL_RECEIVED') return 'Aguardando processamento';
  if (status === 'ACCEPTED') return 'Aceito';
  if (status === 'ACCEPTED_WITH_WARNINGS') return 'Aceito com avisos';
  if (status === 'REJECTED') return 'Rejeitado';
  return status || '—';
}

function motivoRecusa(envio) {
  return (envio?.validacao?.criticas || []).map(item => item.problema).filter(Boolean).join(' ');
}

function formatarDataEnvio(iso) {
  if (!iso) return '—';
  const data = new Date(iso);
  if (Number.isNaN(data.getTime())) return '—';
  return data.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
}

function ambienteDaUrl() {
  const host = window.location.hostname.toLowerCase();
  const path = window.location.pathname;
  const staging = { codigo: 'staging', rotulo: 'Staging', rodape: 'Ambiente de staging. Os envios são simulados.' };
  const homologacao = { codigo: 'homologacao', rotulo: 'Homologação', rodape: 'Ambiente de homologação. Os envios são de pré-produção.' };
  const producao = { codigo: 'producao', rotulo: 'Produção', rodape: null };
  if (host.startsWith('dere-stg.') || host.startsWith('dere-staging.') || /^\/dere-stg(?=\/|$)/.test(path)) return staging;
  if (host.startsWith('dere-hml.') || /^\/dere-hml(?=\/|$)/.test(path)) return homologacao;
  if (host === 'dere.dataa.com.br' || /^\/dere(?=\/|$)/.test(path)) return producao;
  return { codigo: 'local', rotulo: 'Local', rodape: null };
}

function rotuloCanal(transmissao) {
  if (!transmissao || transmissao.simulated) return 'Envio simulado';
  if (transmissao.environment === 'restricted-production') return 'Envio pré-produção';
  return 'Envio de produção';
}

function rotuloAcaoEnvio(aposValidar) {
  const codigo = ambienteDaUrl().codigo;
  const base = codigo === 'staging' ? 'Simular envio' : codigo === 'homologacao' ? 'Enviar para pré-produção' : codigo === 'producao' ? 'Enviar para produção' : 'Enviar';
  return aposValidar ? `${base} após validar` : base;
}

function BarraAmbiente() {
  const [ambiente, setAmbiente] = useState(ambienteDaUrl);
  useEffect(() => {
    api.get('/api/ambiente').then(response => {
      const daUrl = ambienteDaUrl();
      if (daUrl.codigo !== 'local') setAmbiente(daUrl);
      else if (response.data?.codigo) setAmbiente(response.data);
    }).catch(() => setAmbiente(ambienteDaUrl()));
  }, []);
  useEffect(() => {
    document.body.classList.toggle('com-barra-ambiente', Boolean(ambiente?.rodape));
    return () => document.body.classList.remove('com-barra-ambiente');
  }, [ambiente]);
  if (!ambiente?.rodape) return null;
  return <Box className={`barra-ambiente ${ambiente.codigo}`}>{ambiente.rodape}</Box>;
}

function AcaoDownload({ rotulo, texto, onClick, destaque }) {
  return <Button variant={destaque ? 'contained' : 'text'} onClick={onClick} sx={{ justifyContent: 'flex-start', textTransform: 'none' }} startIcon={<Box component="svg" viewBox="0 0 24 24" sx={{ width: 18, height: 18 }} aria-hidden="true"><path fill="currentColor" d="M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z" /></Box>}>{rotulo}<Typography component="span" variant="body2" color={destaque ? 'inherit' : 'text.secondary'} sx={{ ml: 1 }}>— {texto}</Typography></Button>;
}

function EnvioSimuladoCard({ envio, api, uid, eid }) {
  const transmissao = envio?.transmissao;
  if (!envio) return null;
  async function baixarPdf() {
    if (!transmissao?.batchId) return;
    const response = await api.get(`/api/transmissions/${transmissao.batchId}/protocolo.pdf`, { responseType: 'blob', headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    baixarBlob(`protocolo-${transmissao.protocol || transmissao.batchId}.pdf`, response.data);
  }
  if (!envio.enviado) {
    return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Alert severity="warning">{motivoRecusa(envio) || `${rotuloCanal(envio?.transmissao)} recusado.`}</Alert>{transmissao?.protocol ? <Stack spacing={1}><Typography color="text.secondary">Lote já existente para este id. O recibo é consultado automaticamente a cada 1 minuto.</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao.protocol}`} /><Chip color={transmissao.receiptNumber ? 'success' : 'warning'} label={transmissao.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao.status)} /></Stack>{transmissao.batchId && <Tooltip title="Download do PDF com os dados do protocolo em tabela." followCursor><Button onClick={baixarPdf}>PDF do protocolo</Button></Tooltip>}</Stack> : null}</Stack></Paper>;
  }
  return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Typography variant="subtitle1">{rotuloCanal(transmissao)}</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao?.protocol || '—'}`} /><Chip color={transmissao?.receiptNumber ? 'success' : 'warning'} label={transmissao?.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao?.status)} /></Stack><Typography color="text.secondary">Protocolo é a recepção do lote. O recibo é consultado automaticamente a cada 1 minuto.</Typography><Stack spacing={0.5} alignItems="flex-start">{transmissao?.eventXml && <AcaoDownload rotulo="XML do evento" texto="XML do evento enviado à Receita, sem a assinatura digital." onClick={() => baixar(`${transmissao.layout || 'evento'}.xml`, transmissao.eventXml)} />}{transmissao?.batchXml && <AcaoDownload rotulo="XML do lote" texto="XML do lote enviado à Receita, com o evento assinado." onClick={() => baixar('lote.xml', transmissao.batchXml)} />}{transmissao?.returnXml && <AcaoDownload rotulo="XML de retorno" texto="XML de retorno da Receita, da recepção ou da consulta do lote." onClick={() => baixar('retorno.xml', transmissao.returnXml)} />}{transmissao?.csv && <AcaoDownload rotulo="CSV" texto="CSV de origem usado na geração ou conversão do evento." onClick={() => baixar(`${transmissao.layout || 'evento'}.csv`, transmissao.csv)} />}{transmissao?.batchId && <AcaoDownload rotulo="PDF do protocolo" texto="PDF com os dados do protocolo em tabela." onClick={baixarPdf} destaque />}</Stack></Stack></Paper>;
}

function PainelTransmissoes({ api, uid, eid, setMessage, layout, reloadToken, master }) {
  const [linhas, setLinhas] = useState([]);
  const [detalhes, setDetalhes] = useState({});
  const [de, setDe] = useState('');
  const [ate, setAte] = useState('');
  const [filtroLayout, setFiltroLayout] = useState('');
  const [filtroOperacao, setFiltroOperacao] = useState('');
  const [filtroProtocolo, setFiltroProtocolo] = useState('');
  const [filtroStatus, setFiltroStatus] = useState('');
  const linhasRef = useRef([]);
  const cabecalhos = { 'X-User-Id': uid, 'X-Entity-Id': eid };
  async function carregar() {
    const response = await api.get('/api/transmissions', { headers: cabecalhos });
    const todas = Array.isArray(response.data) ? response.data : [];
    const base = layout ? todas.filter(item => item.layout === layout) : todas;
    setLinhas(base);
    linhasRef.current = base;
  }
  useEffect(() => { if (uid && eid) carregar().catch(() => setMessage('Falha ao carregar transmissões')); }, [uid, eid, layout, reloadToken]);
  useEffect(() => {
    if (!uid || !eid) return undefined;
    const timer = setInterval(async () => {
      const pendentes = linhasRef.current.filter(item => item.batchId && !item.receiptNumber);
      if (!pendentes.length) return;
      for (const linha of pendentes) {
        try { await api.post(`/api/transmissions/${linha.batchId}/query`, {}, { headers: cabecalhos }); } catch { /* lote ainda em processamento */ }
      }
      try { await carregar(); } catch { /* recarrega na próxima janela */ }
    }, 60000);
    return () => clearInterval(timer);
  }, [uid, eid]);
  async function abrir(linha) {
    if (detalhes[linha.batchId]) {
      setDetalhes(atual => {
        const proximo = { ...atual };
        delete proximo[linha.batchId];
        return proximo;
      });
      return;
    }
    const response = await api.get(`/api/transmissions/${linha.batchId}`, { headers: cabecalhos });
    setDetalhes(atual => ({ ...atual, [linha.batchId]: response.data }));
  }
  async function excluir(linha) {
    if (!linha?.batchId || !master) return;
    if (!window.confirm(`Excluir a transmissão ${linha.sourceName || linha.protocol || linha.batchId}?`)) return;
    await api.delete(`/api/transmissions/${linha.batchId}`, { headers: cabecalhos });
    setDetalhes(atual => {
      const proximo = { ...atual };
      delete proximo[linha.batchId];
      return proximo;
    });
    await carregar();
    setMessage(`Transmissão ${linha.sourceName || linha.batchId} excluída`);
  }
  function noIntervalo(linha) {
    const marca = linha.sentAt || linha.createdAt;
    if (!marca) return !de && !ate;
    const dia = new Date(marca);
    if (Number.isNaN(dia.getTime())) return true;
    if (de) {
      const inicio = new Date(`${de}T00:00:00`);
      if (dia < inicio) return false;
    }
    if (ate) {
      const fim = new Date(`${ate}T23:59:59`);
      if (dia > fim) return false;
    }
    return true;
  }
  const filtradas = linhas.filter(linha => {
    if (!layout && filtroLayout && linha.layout !== filtroLayout) return false;
    if (filtroOperacao && String(linha.operationType) !== filtroOperacao) return false;
    if (filtroProtocolo && !(linha.protocol || '').toLowerCase().includes(filtroProtocolo.toLowerCase())) return false;
    if (filtroStatus && linha.status !== filtroStatus) return false;
    return noIntervalo(linha);
  });
  async function emitirRelatorio() {
    const response = await api.post('/api/transmissions/relatorio.pdf', filtradas, { responseType: 'blob', headers: cabecalhos });
    baixarBlob('relatorio-transmissoes.pdf', response.data);
  }
  async function baixarPdf(linha) {
    const response = await api.get(`/api/transmissions/${linha.batchId}/protocolo.pdf`, { responseType: 'blob', headers: cabecalhos });
    baixarBlob(`protocolo-${linha.protocol || linha.batchId}.pdf`, response.data);
  }
  const colunas = layout
    ? ['Arquivo', 'Operação', 'Id', 'Protocolo', 'Recibo', 'Status', 'Data de envio', 'Ações']
    : ['Layout', 'Arquivo', 'Operação', 'Id', 'Protocolo', 'Recibo', 'Status', 'Data de envio', 'Ações'];
  const pendentes = filtradas.filter(item => item.batchId && !item.receiptNumber).length;
  return <Card><CardContent><Stack spacing={2}>
    <Typography variant="h6">{layout ? `Transmissões do ${layout}` : 'Lotes simulados e oficiais'}</Typography>
    <Typography color="text.secondary">Recepção gera protocolo. Lotes sem recibo são consultados automaticamente a cada 1 minuto.</Typography>
    <Stack direction="row" spacing={2} flexWrap="wrap" useFlexGap>
      <TextField type="date" label="Data inicial" InputLabelProps={{ shrink: true }} value={de} onChange={event => setDe(event.target.value)} sx={{ minWidth: 170 }} />
      <TextField type="date" label="Data final" InputLabelProps={{ shrink: true }} value={ate} onChange={event => setAte(event.target.value)} sx={{ minWidth: 170 }} />
      {!layout && <TextField select label="Layout" value={filtroLayout} onChange={event => setFiltroLayout(event.target.value)} sx={{ minWidth: 160 }}><MenuItem value="">Todos</MenuItem><MenuItem value="D-1001">D-1001</MenuItem><MenuItem value="D-1011">D-1011</MenuItem><MenuItem value="D-1101">D-1101</MenuItem><MenuItem value="D-1199">D-1199</MenuItem></TextField>}
      <TextField select label="Operação" value={filtroOperacao} onChange={event => setFiltroOperacao(event.target.value)} sx={{ minWidth: 160 }}><MenuItem value="">Todas</MenuItem><MenuItem value="1">Cadastro</MenuItem><MenuItem value="2">Edição</MenuItem><MenuItem value="3">Exclusão</MenuItem></TextField>
      <TextField label="Protocolo" value={filtroProtocolo} onChange={event => setFiltroProtocolo(event.target.value)} sx={{ minWidth: 200 }} />
      <TextField select label="Status" value={filtroStatus} onChange={event => setFiltroStatus(event.target.value)} sx={{ minWidth: 220 }}><MenuItem value="">Todos</MenuItem><MenuItem value="PROTOCOL_RECEIVED">Aguardando processamento</MenuItem><MenuItem value="PROCESSING">Aguardando processamento</MenuItem><MenuItem value="ACCEPTED">Aceito</MenuItem><MenuItem value="ACCEPTED_WITH_WARNINGS">Aceito com avisos</MenuItem><MenuItem value="REJECTED">Rejeitado</MenuItem></TextField>
      <Button variant="contained" onClick={() => emitirRelatorio().catch(() => setMessage('Falha ao emitir o relatório'))} disabled={!filtradas.length}>Emitir relatório</Button>
    </Stack>
    {pendentes > 0 && <Alert severity="info">{pendentes} lote(s) sem recibo. A consulta automática roda a cada 1 minuto.</Alert>}
    {!filtradas.length && <Alert severity="info">{layout ? `Nenhuma transmissão de ${layout} para os filtros informados.` : 'Nenhuma transmissão para os filtros informados.'}</Alert>}
    <Box className="tabela-criticas-wrap" sx={{ maxHeight: 'none' }}><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{colunas.map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{filtradas.map(linha => { const detalhe = detalhes[linha.batchId]; return <React.Fragment key={linha.batchId || linha.eventId}><Box component="tr">{!layout && <Box component="td">{linha.layout}</Box>}<Box component="td">{linha.sourceName || '—'}</Box><Box component="td">{rotuloOperacaoEnvio(linha.operationType)}</Box><Box component="td">{linha.eventIdentifier}</Box><Box component="td">{linha.protocol || '—'}</Box><Box component="td">{linha.receiptNumber || '—'}</Box><Box component="td">{rotuloStatusEnvio(linha.status)}</Box><Box component="td">{formatarDataEnvio(linha.sentAt || linha.createdAt)}</Box><Box component="td"><Stack direction="row" spacing={1}>{linha.batchId && <Tooltip title="Abre o que já está gravado: protocolo, XML do evento, lote, retorno e CSV. Não consulta a Receita." placement="top"><span><Button size="small" variant={detalhe ? 'contained' : 'text'} onClick={() => abrir(linha)}>Artefatos</Button></span></Tooltip>}{linha.batchId && <Button size="small" onClick={() => baixarPdf(linha).catch(() => setMessage('Falha ao gerar o PDF do protocolo'))}>PDF</Button>}{master && linha.batchId && <Tooltip title="Remove o lote e o evento desta transmissão da base local." followCursor><Button size="small" color="error" onClick={() => excluir(linha).catch(error => setMessage(error.response?.data?.message || error.response?.data?.error || 'Falha ao excluir a transmissão'))}>Excluir</Button></Tooltip>}</Stack></Box></Box>{detalhe && <Box component="tr"><Box component="td" colSpan={colunas.length} sx={{ p: 1.5, background: '#f8fafc' }}><EnvioSimuladoCard envio={{ enviado: true, transmissao: detalhe }} api={api} uid={uid} eid={eid} /></Box></Box>}</React.Fragment>; })}</Box></Box></Box>
  </Stack></CardContent></Card>;
}

function Dashboard({ onImport }) {
  const ambiente = ambienteDaUrl();
  const transmissao = ambiente.codigo === 'staging'
    ? ['Simulado', 'Envios simulados']
    : ambiente.codigo === 'homologacao'
      ? ['Pré-produção', 'Envios de pré-produção']
      : ambiente.codigo === 'producao'
        ? ['Produção', 'Envios de produção']
        : ['Mock local', 'Integração controlada'];
  return <><Grid container spacing={2} mb={3}><Grid item xs={12} md={4}><Metric title="Layouts ativos" value="4" detail="D-1001, D-1011, D-1101 e D-1199" /></Grid><Grid item xs={12} md={4}><Metric title="Importações" value="Pronto" detail="CSV e XML" /></Grid><Grid item xs={12} md={4}><Metric title="Transmissões" value={transmissao[0]} detail={transmissao[1]} /></Grid></Grid><Card><CardContent><Stack spacing={2}><Typography variant="h6">Comece uma operação</Typography><Typography color="text.secondary">Importe um arquivo, valide o modelo canônico e acompanhe as críticas por campo.</Typography><Button variant="contained" onClick={onImport}>Nova importação</Button></Stack></CardContent></Card></>;
}
function Metric({ title, value, detail }) { return <Card><CardContent><Typography color="text.secondary">{title}</Typography><Typography variant="h4" sx={{ mt: 1 }}>{value}</Typography><Typography variant="body2" color="text.secondary">{detail}</Typography></CardContent></Card>; }
function ResumoResultado({ resultado, aoBaixar, aoRelatorio, rotuloDownload = 'Baixar CSV', criticas = [] }) {
  const linhasComProblema = resultado.linhasComProblema ?? new Set(criticas.map(item => item.linha).filter(Boolean)).size;
  return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Typography variant="subtitle1">Críticas da validação</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="error" label="Com críticas" /><Chip label={`${resultado.totalLinhas ?? resultado.quantidadeArquivos ?? 1} linha(s)`} /><Chip label={`${linhasComProblema} linha(s) com problema`} /><Chip label={`${criticas.length} crítica(s)`} /></Stack>{criticas.length > 0 && aoRelatorio && <Box><Button variant="contained" onClick={aoRelatorio}>Baixar relatório de críticas</Button></Box>}{aoBaixar && <Button variant="outlined" onClick={aoBaixar}>{rotuloDownload}</Button>}<Box className="tabela-criticas-wrap"><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{['Linha', 'Coluna', 'Tipo', 'Encontrado', 'Esperado', 'Como corrigir'].map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{criticas.map((item, indice) => <Box component="tr" key={indice}><Box component="td">{item.linha || '—'}</Box><Box component="td">{item.coluna || 'Documento'}</Box><Box component="td"><Chip size="small" color={COR_TIPO[item.tipo] || 'default'} label={item.rotuloTipo || ROTULO_TIPO[item.tipo] || item.tipo} /></Box><Box component="td">{item.valorEncontrado || '—'}</Box><Box component="td">{item.valorEsperado || '—'}</Box><Box component="td">{item.problema}</Box></Box>)}</Box></Box></Box></Stack></Paper>;
}
function UserAdmin({ api, currentUserId, onSelfMaster, setMessage }) {
  const empty = { id: null, login: '', name: '', password: '', master: false };
  const [rows, setRows] = useState([]);
  const [form, setForm] = useState(empty);
  const load = async () => setRows((await api.get('/api/admin/users')).data);
  useEffect(() => { load().catch(() => setMessage('Falha ao carregar usuários')); }, []);
  async function saveMaster(user, value) { await api.put(`/api/admin/users/${user.id}`, { login: user.login, name: user.name, password: '', master: value }); if (user.id === currentUserId) onSelfMaster(value); await load(); setMessage(value ? `${user.login} agora é master` : `${user.login} deixou de ser master`); }
  async function saveUser() { const body = { login: form.login, name: form.name, password: form.password, master: form.master }; if (form.id) { await api.put(`/api/admin/users/${form.id}`, body); if (form.id === currentUserId) onSelfMaster(form.master); setMessage('Usuário atualizado'); } else { await api.post('/api/admin/users', body); setMessage('Usuário cadastrado'); } setForm(empty); await load(); }
  async function removeUser(user) { await api.delete(`/api/admin/users/${user.id}`); if (form.id === user.id) setForm(empty); await load(); setMessage('Usuário excluído'); }
  return <Card><CardContent><Stack spacing={2}><Typography variant="h6">{form.id ? `Editar ${form.login}` : 'Novo usuário'}</Typography><Stack direction="row" spacing={2} alignItems="center"><TextField label="Login" value={form.login} onChange={event => setForm({ ...form, login: event.target.value })} /><TextField label="Nome" value={form.name} onChange={event => setForm({ ...form, name: event.target.value })} /><TextField label={form.id ? 'Nova senha' : 'Senha'} type="password" value={form.password} onChange={event => setForm({ ...form, password: event.target.value })} helperText={form.id ? 'Em branco mantém a senha' : ' '} /><Button variant={form.master ? 'contained' : 'outlined'} onClick={() => setForm({ ...form, master: !form.master })}>{form.master ? 'Master' : 'Comum'}</Button><Button variant="contained" onClick={saveUser}>{form.id ? 'Salvar' : 'Cadastrar'}</Button>{form.id && <Button onClick={() => setForm(empty)}>Cancelar</Button>}</Stack><Box component="table" sx={{ width: '100%', borderCollapse: 'collapse' }}><Box component="thead"><Box component="tr">{['Login', 'Nome', 'Master', 'Ações'].map(title => <Box component="th" key={title} sx={{ textAlign: 'left', py: 1 }}>{title}</Box>)}</Box></Box><Box component="tbody">{rows.map(user => <Box component="tr" key={user.id} sx={{ borderTop: '1px solid #e5e7eb' }}><Box component="td" sx={{ py: 1 }}>{user.login}</Box><Box component="td">{user.name}</Box><Box component="td"><Button size="small" variant={user.master ? 'contained' : 'outlined'} onClick={() => saveMaster(user, !user.master)}>{user.master ? 'Master' : 'Comum'}</Button></Box><Box component="td"><Button size="small" onClick={() => setForm({ id: user.id, login: user.login, name: user.name, password: '', master: user.master })}>Editar</Button><Button size="small" color="error" disabled={user.id === currentUserId} onClick={() => removeUser(user)}>Excluir</Button></Box></Box>)}</Box></Box></Stack></CardContent></Card>;
}
function EntityAdmin({ api, onChanged, setMessage, master }) {
  const [users, setUsers] = useState([]);
  const [entities, setEntities] = useState([]);
  const [form, setForm] = useState(null);
  const locked = !master;
  const load = async () => {
    const entityResponse = await api.get('/api/admin/entities');
    const lista = entityResponse.data;
    setEntities(lista);
    if (master) {
      const userResponse = await api.get('/api/admin/users');
      setUsers(userResponse.data);
    } else {
      setUsers([]);
      if (lista.length === 1) setForm({ id: lista[0].id, cnpjRoot: lista[0].cnpjRoot, legalName: lista[0].legalName, userIds: lista[0].userIds || [] });
    }
  };
  useEffect(() => { load().catch(() => setMessage('Falha ao carregar entidades')); }, []);
  function openNew() { setForm({ id: null, cnpjRoot: '', legalName: '', userIds: [] }); }
  function openEdit(entity) { setForm({ id: entity.id, cnpjRoot: entity.cnpjRoot, legalName: entity.legalName, userIds: entity.userIds || [] }); }
  async function save() {
    const cnpjRoot = raizCnpj(form.cnpjRoot);
    if (!cnpjRoot) { setMessage('Informe o CNPJ raiz com 8 dígitos (ou o CNPJ completo).'); return; }
    try {
      const body = { cnpjRoot, legalName: form.legalName, userIds: form.userIds };
      if (form.id) await api.put(`/api/admin/entities/${form.id}`, body);
      else await api.post('/api/admin/entities', body);
      setForm(null);
      await load();
      if (onChanged) await onChanged();
      setMessage('Entidade salva com sucesso.');
    } catch (error) {
      setMessage(error.response?.data?.message || error.response?.data?.detail || 'Falha ao salvar a entidade.');
    }
  }
  async function remove(entity) {
    try {
      await api.delete(`/api/admin/entities/${entity.id}`);
      await load();
      if (onChanged) await onChanged();
      setMessage('Entidade excluída com sucesso.');
    } catch (error) {
      setMessage(error.response?.data?.message || error.response?.data?.detail || 'Falha ao excluir a entidade.');
    }
  }
  if (!form) return <Card><CardContent><Stack spacing={2}><Stack direction="row" justifyContent="space-between"><Typography variant="h6">Entidades</Typography>{master && <Button variant="contained" onClick={openNew}>Nova entidade</Button>}</Stack>{entities.map(entity => <Box key={entity.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #e5e7eb', py: 1 }}><Box><Typography>{entity.legalName}</Typography><Typography variant="body2" color="text.secondary">{entity.cnpjRoot} · {(entity.userIds || []).length} usuário(s)</Typography></Box><Stack direction="row" spacing={1}><Button onClick={() => openEdit(entity)}>{master ? 'Editar' : 'Ver'}</Button>{master && <Button color="error" onClick={() => remove(entity)}>Excluir</Button>}</Stack></Box>)}</Stack></CardContent></Card>;
  return <Card><CardContent><Stack spacing={3}><Typography variant="h6">{locked ? 'Entidade' : form.id ? 'Editar entidade' : 'Nova entidade'}</Typography><Stack direction="row" spacing={2}><TextField label="CNPJ raiz" value={form.cnpjRoot} disabled={locked} helperText="8 dígitos da raiz. Aceita o CNPJ completo." onChange={event => setForm({ ...form, cnpjRoot: event.target.value })} /><TextField label="Razão social" value={form.legalName} disabled={locked} onChange={event => setForm({ ...form, legalName: event.target.value })} fullWidth /></Stack>{master && <Typography color="text.secondary">Usuários vinculados à entidade</Typography>}{master && <UserPickList users={users} selectedIds={form.userIds} onChange={userIds => setForm({ ...form, userIds })} />}<Stack direction="row" spacing={2}>{master && <Button variant="contained" onClick={save}>Salvar</Button>}{!(locked && entities.length === 1) && <Button onClick={() => setForm(null)}>{master ? 'Cancelar' : 'Voltar'}</Button>}</Stack></Stack></CardContent></Card>;
}
function UserPickList({ users, selectedIds, onChange }) {
  const [availableMarked, setAvailableMarked] = useState([]);
  const [linkedMarked, setLinkedMarked] = useState([]);
  const available = users.filter(user => !selectedIds.includes(user.id));
  const linked = users.filter(user => selectedIds.includes(user.id));
  function toggle(list, setList, id) { setList(list.includes(id) ? list.filter(item => item !== id) : [...list, id]); }
  function move(ids, include) { const next = new Set(selectedIds); ids.forEach(id => include ? next.add(id) : next.delete(id)); onChange([...next]); setAvailableMarked([]); setLinkedMarked([]); }
  return <Box className="pick-list"><PickColumn title="Disponíveis" items={available} marked={availableMarked} onToggle={id => toggle(availableMarked, setAvailableMarked, id)} /><Box className="pick-actions"><Button variant="contained" onClick={() => move(availableMarked, true)} disabled={!availableMarked.length}>Adicionar</Button><Button variant="contained" onClick={() => move(available.map(user => user.id), true)} disabled={!available.length}>Adicionar todos</Button><Button variant="contained" onClick={() => move(linkedMarked, false)} disabled={!linkedMarked.length}>Remover</Button><Button variant="contained" onClick={() => move(linked.map(user => user.id), false)} disabled={!linked.length}>Remover todos</Button></Box><PickColumn title="Vinculados" items={linked} marked={linkedMarked} onToggle={id => toggle(linkedMarked, setLinkedMarked, id)} /></Box>;
}
function PickColumn({ title, items, marked, onToggle }) { return <Box><Typography variant="subtitle2" mb={1}>{title}</Typography><Box className="pick-column">{items.map(user => <Button key={user.id} onClick={() => onToggle(user.id)} sx={{ bgcolor: marked.includes(user.id) ? '#2563eb' : 'transparent', color: marked.includes(user.id) ? '#fff' : 'inherit' }}>{user.name} — {user.login}</Button>)}{!items.length && <Typography sx={{ p: 2 }} color="text.secondary">Nenhum usuário</Typography>}</Box></Box>; }

createRoot(document.getElementById('root')!).render(<React.StrictMode><App /><BarraAmbiente /></React.StrictMode>);
