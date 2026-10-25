// @ts-nocheck
import React, { useEffect, useRef, useState } from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import { Alert, Box, Button, Card, CardContent, Chip, CircularProgress, Collapse, CssBaseline, Dialog, DialogActions, DialogContent, DialogTitle, Divider, Drawer, FormControlLabel, Grid, IconButton, InputAdornment, LinearProgress, List, ListItemButton, ListItemIcon, ListItemText, MenuItem, Paper, Radio, RadioGroup, Stack, Step, StepLabel, Stepper, Switch, Tab, Tabs, TextField, Tooltip, Typography } from '@mui/material';
import { ThemeProvider, useColorScheme } from '@mui/material/styles';
import GridViewIcon from '@mui/icons-material/GridView';
import SendIcon from '@mui/icons-material/Send';
import FactCheckIcon from '@mui/icons-material/FactCheck';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import SettingsIcon from '@mui/icons-material/Settings';
import BusinessIcon from '@mui/icons-material/Business';
import PeopleIcon from '@mui/icons-material/People';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import LightModeIcon from '@mui/icons-material/LightMode';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';
import './style.css';
import { temaDere } from './tema';
import { bloqueiaEnvioSimultaneo, deveRetentarAutomatico, diagnosticoRetornoReceita, interpretarErroTransmissao } from './erroTransmissao';
import { TransmissionErrorAlert } from './TransmissionErrorAlert';
import logotipoDataA from './dataa-logotipo.png';
import seloDere from './dere-selo.jpg';

const apiPrefix = (window.location.pathname.match(/^\/dere(?:-stg|-hml)?(?=\/|$)/) || [''])[0];
const api = axios.create();
let tokenSessao = null;
api.interceptors.request.use((config) => {
  const url = config.url || '';
  const root = `/${'api'}/`;
  if (url.startsWith(root) && !url.startsWith(`${apiPrefix}${root}`)) {
    config.url = `${apiPrefix}${url}`;
  }
  if (tokenSessao) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${tokenSessao}`;
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
const PAGINA_VISAO = 'Visão geral';
const PAGINA_ENVIO = 'Enviar declaração';
const PAGINA_HISTORICO = 'Histórico de envios';
const PAGINA_CERTIFICADO = 'Certificado digital';
const TIPOS_DECLARACAO = [
  ['D-1001', 'D-1001 — Informações do contribuinte'],
  ['D-1011', 'D-1011 — PGCC'],
  ['D-1101', 'D-1101 — Balancete mensal'],
  ['D-1199', 'D-1199 — Fechamento mensal'],
];

function lerMenusVisiveis() {
  try {
    const bruto = localStorage.getItem(CHAVE_MENUS);
    if (!bruto) return {};
    const lido = JSON.parse(bruto);
    if (!lido || typeof lido !== 'object') return {};
    const renomeados = {
      Dashboard: PAGINA_VISAO,
      Transmissões: PAGINA_HISTORICO,
      'Certificado Digital': PAGINA_CERTIFICADO,
      'D-1001': 'Enviar D-1001',
      'D-1011': 'Enviar D-1011',
      'D-1101': 'Enviar D-1101',
      'D-1199': 'Enviar D-1199',
    };
    Object.entries(renomeados).forEach(([antigo, novo]) => { if (lido[antigo] !== undefined && lido[novo] === undefined) lido[novo] = lido[antigo]; });
    if (lido[PAGINA_ENVIO] === undefined) {
      const antigos = ['Enviar D-1001', 'Enviar D-1011', 'Enviar D-1101', 'Enviar D-1199'];
      if (antigos.every(chave => lido[chave] === false)) lido[PAGINA_ENVIO] = false;
    }
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
    limiteUploadMb: 50,
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
      const limite = Number(corpo.limiteUploadMb);
      if (!Number.isInteger(limite) || limite < 1 || limite > 50) {
        setMessage('O limite de upload deve ficar entre 1 e 50 MB.');
        return;
      }
      corpo.limiteUploadMb = limite;
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
    <TextField label="Limite de upload (MB)" type="number" fullWidth disabled={!master} value={integracao.limiteUploadMb ?? 50} onChange={event => setIntegracao(atual => ({ ...atual, limiteUploadMb: event.target.value === '' ? '' : Number(event.target.value) }))} helperText="XML avulso e ZIP já descompactado. O máximo é 50 MB." slotProps={{ htmlInput: { min: 1, max: 50, step: 1 } }} />
    <Box><Button variant="contained" disabled={!master || carregando} onClick={salvarIntegracao}>{carregando ? 'Salvando…' : 'Salvar integração'}</Button></Box>
  </Stack></CardContent></Card>;

  return <Stack spacing={2}><Tabs value={aba} onChange={(_, valor) => setAba(valor)}><Tab label="Menus" /><Tab label="Integração Receita" /></Tabs>{aba === 0 ? abaMenus : abaIntegracao}</Stack>;
}

function PainelSegurancaCertificado() {
  const [aberto, setAberto] = useState(false);
  return <Box><Button onClick={() => setAberto(atual => !atual)} sx={{ textTransform: 'none', px: 0 }}>{aberto ? 'Recolher' : 'Como protegemos seu certificado e sua senha'}</Button><Collapse in={aberto}><Stack spacing={1.5} sx={{ pt: 1 }}><Typography color="text.secondary">A chave privada não é enviada à Receita Federal. O sistema transmite apenas o documento já assinado. A senha do certificado é custodiada no AWS Secrets Manager, protegida por AWS KMS, e permanece separada do arquivo. Seu uso ocorre somente em memória, no instante da assinatura.</Typography><Typography variant="subtitle2">1. Informar a senha</Typography><Typography color="text.secondary">A senha é enviada uma única vez, no mesmo pedido do arquivo, por canal protegido. Este formulário não a retém e ela nunca volta a ser exibida.</Typography><Typography variant="subtitle2">2. Conferência do arquivo</Typography><Typography color="text.secondary">O serviço verifica, em memória, se o arquivo é um certificado digital válido e se a senha corresponde. Havendo inconsistência, nada é armazenado.</Typography><Typography variant="subtitle2">3. Custódia da senha e do arquivo</Typography><Typography color="text.secondary">O arquivo do certificado permanece cifrado no ambiente da aplicação. A senha é mantida em cofre gerenciado (AWS Secrets Manager), com criptografia de ponta de mercado provida pelo AWS KMS. Arquivo e senha não tramitam juntos nem compartilham o mesmo repositório de dados de negócio.</Typography><Typography variant="subtitle2">4. Controle de acesso</Typography><Typography color="text.secondary">O certificado fica vinculado à entidade. Somente usuários autorizados dessa entidade podem utilizá-lo. A exclusão o desativa imediatamente.</Typography><Typography variant="subtitle2">5. Assinatura da declaração</Typography><Typography color="text.secondary">No envio, o certificado é aberto apenas em memória e o documento é assinado. A Receita recebe o XML autenticado e o certificado público necessário à verificação. A chave privada não integra o arquivo transmitido.</Typography><Typography variant="subtitle2">6. Encaminhamento à Receita</Typography><Typography color="text.secondary">O lote contém o documento assinado. A chave privada permanece no ambiente da aplicação somente durante essa operação e não é retransmitida.</Typography><Typography variant="body2">Anexe apenas certificado A1 de sua titularidade. Não compartilhe o arquivo nem a senha por canais não seguros.</Typography></Stack></Collapse></Box>;
}

function PainelCertificado({ eid, entities, setEid, entidadeTravada, certificados, certLabel, setCertLabel, certFile, setCertFile, certPassword, setCertPassword, armazenarCertificado, excluirCertificado }) {
  const [substituir, setSubstituir] = useState(false);
  const atual = certificadoPrincipal(certificados);
  const cadastrado = Boolean(atual);
  return <Card><CardContent><Stack spacing={2}>
    <Stack direction="row" justifyContent="space-between" alignItems="center"><Typography variant="h6">Certificado digital</Typography>{atual?.status === 'VALID' ? <VerifiedUserIcon color="success" /> : <VerifiedUserIcon color="disabled" />}</Stack>
    {cadastrado ? <Stack spacing={0.5}><Stack direction="row" spacing={1} alignItems="center"><CheckCircleIcon color="success" fontSize="small" /><Typography>Certificado cadastrado</Typography></Stack><Typography>Titular: {atual.subject || atual.label || '—'}</Typography><Typography color="text.secondary">Validade: {formatarDia(atual.validUntil)}</Typography></Stack> : <Alert severity="warning">Nenhum certificado cadastrado para esta entidade.</Alert>}
    <Button variant="contained" onClick={() => setSubstituir(atualValor => !atualValor)}>{cadastrado ? (substituir ? 'Fechar substituição' : 'Substituir certificado') : 'Cadastrar certificado'}</Button>
    <Collapse in={substituir || !cadastrado}><Stack spacing={2}><TextField select fullWidth label="Entidade" value={eid} disabled={entidadeTravada} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField><TextField label="Nome" value={certLabel} onChange={event => setCertLabel(event.target.value)} /><Button component="label" variant="outlined">Selecionar P12/PFX<input hidden type="file" accept=".p12,.pfx" onChange={event => setCertFile(event.target.files?.[0])} /></Button>{certFile ? <Typography color="text.secondary">Arquivo: {certFile.name}</Typography> : null}<TextField label="Senha do certificado" type="password" value={certPassword} onChange={event => setCertPassword(event.target.value)} /><Button variant="contained" onClick={armazenarCertificado}>Armazenar certificado</Button>{certificados.length > 0 && <Divider />}{certificados.map(cert => <Stack key={cert.id} direction="row" justifyContent="space-between" alignItems="center"><Box><Typography>{cert.label}</Typography><Typography variant="body2" color="text.secondary">{cert.subject || '—'} · {formatarDia(cert.validUntil)}</Typography></Box><Button size="small" color="error" onClick={() => excluirCertificado(cert)}>Excluir</Button></Stack>)}</Stack></Collapse>
    <PainelSegurancaCertificado />
  </Stack></CardContent></Card>;
}

function BotaoTema() {
  const esquema = useColorScheme();
  const modo = esquema.mode === 'dark' ? 'dark' : 'light';
  const rotulo = modo === 'dark' ? 'Ativar tema claro' : 'Ativar tema escuro';
  return <Tooltip title={rotulo}><IconButton aria-label={rotulo} color="inherit" onClick={() => esquema.setMode(modo === 'dark' ? 'light' : 'dark')}><>{modo === 'dark' ? <LightModeIcon /> : <DarkModeIcon />}</></IconButton></Tooltip>;
}

function App() {
  const [uid, setUid] = useState<number>();
  const [master, setMaster] = useState(false);
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [senhaVisivel, setSenhaVisivel] = useState(false);
  const [entrando, setEntrando] = useState(false);
  const [capsLock, setCapsLock] = useState(false);
  const [page, setPage] = useState(PAGINA_VISAO);
  const [canal, setCanal] = useState(ambienteDaUrl);
  const [filtroHistorico, setFiltroHistorico] = useState({ layout: '', situacao: '' });
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
  useEffect(() => { if (!uid) return; api.get('/api/admin/entities').then(response => setEntities(Array.isArray(response.data) ? response.data : [])).catch(() => {}); }, [uid]);
  useEffect(() => { if (uid && eid) api.get(`/api/certificates/entity/${eid}`).then(response => setCertificados(Array.isArray(response.data) ? response.data : [])).catch(() => {}); }, [uid, eid]);
  useEffect(() => { if (message) window.scrollTo({ top: 0, behavior: 'smooth' }); }, [message]);
  useEffect(() => {
    if (uid && !master && (page === 'Configurações' || page === 'Entidades' || page === 'Usuários')) setPage(PAGINA_VISAO);
  }, [uid, master, page]);
  useEffect(() => {
    const daUrl = ambienteDaUrl();
    if (daUrl.codigo !== 'local') setCanal(daUrl);
    api.get('/api/ambiente').then(response => {
      if (daUrl.codigo === 'local' && response.data?.codigo) setCanal(response.data);
    }).catch(() => {});
  }, [uid]);

  function atualizarCapsLock(event) {
    setCapsLock(typeof event.getModifierState === 'function' && event.getModifierState('CapsLock'));
  }
  async function signIn(event) {
    event?.preventDefault?.();
    if (entrando) return;
    if (!login.trim() || !password) { setMessage('Informe o usuário e a senha.'); return; }
    setEntrando(true);
    setMessage('');
    try {
      const response = await api.post('/api/auth/login', { login, password });
      setUid(response.data.userId);
      setMaster(!!response.data.master);
      tokenSessao = response.data.token;
      api.defaults.headers.common.Authorization = `Bearer ${response.data.token}`;
      setPassword('');
      setMessage('');
      await load();
    } catch (error) {
      if (!error.response) setMessage('Não foi possível entrar. Tente novamente.');
      else if (error.response.status === 429) setMessage(error.response.data?.message || 'Muitas tentativas de login. Tente novamente em alguns minutos.');
      else setMessage('Usuário ou senha incorretos.');
    } finally {
      setEntrando(false);
    }
  }
  function signOut() {
    tokenSessao = null;
    delete api.defaults.headers.common.Authorization;
    delete api.defaults.headers.common['X-User-Id'];
    setUid(undefined);
    setMaster(false);
    setPassword('');
    setMessage('');
    setPage(PAGINA_VISAO);
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

  if (!uid) return <Box className="login-shell" component="main"><Box className="login-tema"><BotaoTema /></Box><Stack className="login-bloco" component="form" spacing={2} alignItems="center" onSubmit={signIn}><Card className="login-card"><CardContent><Stack spacing={2.5} alignItems="center"><img className="selo-login" src={seloDere} alt="DeRE" /><Box sx={{ textAlign: 'center', width: '100%' }}><Typography variant="h5">Bem-vindo à DeRE</Typography><Typography color="text.secondary">Declaração de Regimes Específicos</Typography></Box><Typography variant="subtitle1" sx={{ alignSelf: 'stretch' }}>Acesse sua conta</Typography><TextField label="Usuário" name="username" autoComplete="username" autoFocus fullWidth value={login} onChange={event => setLogin(event.target.value)} disabled={entrando} /><TextField label="Senha" name="password" autoComplete="current-password" fullWidth type={senhaVisivel ? 'text' : 'password'} value={password} disabled={entrando} onChange={event => setPassword(event.target.value)} onKeyUp={atualizarCapsLock} onKeyDown={atualizarCapsLock} helperText={capsLock ? 'Caps Lock está ativado.' : ' '} FormHelperTextProps={{ role: capsLock ? 'status' : undefined }} slotProps={{ input: { endAdornment: <InputAdornment position="end"><IconButton aria-label={senhaVisivel ? 'Ocultar senha' : 'Mostrar senha'} edge="end" onClick={() => setSenhaVisivel(atual => !atual)} disabled={entrando}>{senhaVisivel ? <VisibilityOffIcon /> : <VisibilityIcon />}</IconButton></InputAdornment> } }} /><Button type="submit" variant="contained" size="large" fullWidth disabled={entrando} startIcon={entrando ? <CircularProgress size={18} color="inherit" /> : null}>{entrando ? 'Entrando…' : 'Entrar'}</Button>{message && <Alert severity="error" sx={{ alignSelf: 'stretch' }}>{message}</Alert>}</Stack></CardContent></Card><Typography variant="caption" color="text.secondary">Data A · Soluções em Previdência</Typography></Stack></Box>;

  const menu = [PAGINA_VISAO, PAGINA_ENVIO, PAGINA_HISTORICO, PAGINA_CERTIFICADO, ...(master ? ['Configurações', 'Entidades', 'Usuários'] : [])];
  const menuExibido = menu.filter(item => item === 'Configurações' || menusVisiveis[item] !== false);
  const secoes = [
    { titulo: '', itens: [{ id: PAGINA_VISAO, Icone: GridViewIcon }] },
    { titulo: 'Declarações', itens: [{ id: PAGINA_ENVIO, Icone: SendIcon }, { id: PAGINA_HISTORICO, Icone: FactCheckIcon }] },
    { titulo: 'Configurações', itens: [{ id: PAGINA_CERTIFICADO, Icone: VerifiedUserIcon }] },
    ...(master ? [{ titulo: 'Administração', itens: [{ id: 'Configurações', Icone: SettingsIcon }, { id: 'Entidades', Icone: BusinessIcon }, { id: 'Usuários', Icone: PeopleIcon }] }] : []),
  ].map(secao => ({ ...secao, itens: secao.itens.filter(item => menuExibido.includes(item.id)) })).filter(secao => secao.itens.length);
  function alternarMenu(item, marcado) {
    setMenusVisiveis(atual => {
      const proximo = { ...atual, [item]: marcado };
      localStorage.setItem(CHAVE_MENUS, JSON.stringify(proximo));
      return proximo;
    });
    if (!marcado && page === item) setPage('Configurações');
  }
  const currentEntity = entities.find(entity => entity.id === eid);
  const subtitulo = `${currentEntity ? `${currentEntity.legalName} — ${currentEntity.cnpjRoot}` : 'Entidade não selecionada'} · ${rotuloCanalAmbiente(canal.codigo)}`;
  function abrirHistorico(layout, situacao) {
    setFiltroHistorico({ layout: layout || '', situacao: situacao || '' });
    setPage(PAGINA_HISTORICO);
    setMessage('');
  }
  const certificadoProps = { eid, entities, setEid, entidadeTravada, certificados, certLabel, setCertLabel, certFile, setCertFile, certPassword, setCertPassword, armazenarCertificado, excluirCertificado };
  return <Box className="app-shell">
    <Drawer variant="permanent" className="sidebar">
      <Box className="brand"><img className="marca" src={logotipoDataA} alt="maps + Data A" /><Typography variant="h5">DeRE</Typography><Typography variant="caption">EFPC / PREVIC</Typography></Box>
      <List>
        {secoes.map(secao => <Box key={secao.titulo || 'inicio'}>
          {secao.titulo ? <Typography className="menu-secao" component="span">{secao.titulo}</Typography> : null}
          {secao.itens.map(item => <ListItemButton selected={page === item.id} onClick={() => { setPage(item.id); setMessage(''); }} key={item.id}><ListItemIcon><item.Icone fontSize="small" /></ListItemIcon><ListItemText primary={item.id} /></ListItemButton>)}
        </Box>)}
      </List>
    </Drawer>
    <Box component="main" className="content">
      <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
        <Box><Typography variant="h4">{page}</Typography><Typography color="text.secondary">{subtitulo}</Typography></Box>
        <Stack direction="row" spacing={1.5} alignItems="center"><Chip label={currentEntity?.legalName || 'Entidade não selecionada'} /><BotaoTema /><Button variant="outlined" onClick={signOut}>Sair</Button><img className="selo-topo" src={seloDere} alt="DeRE" /></Stack>
      </Stack>
      {message && <Alert sx={{ mb: 3 }} severity={gravidadeAviso(message)}>{message}</Alert>}
      {page === PAGINA_VISAO ? <Dashboard api={api} uid={uid} eid={eid} entities={entities} setEid={setEid} master={master} certificados={certificados} onNova={() => { setPage(PAGINA_ENVIO); setMessage(''); }} onPendencias={() => abrirHistorico('', 'PENDENCIAS')} /> : null}
      <Box hidden={page !== PAGINA_ENVIO}><ImportWorkspace api={api} uid={uid} eid={eid} entities={entities} setEid={setEid} setMessage={setMessage} master={master} entidadeNome={currentEntity?.legalName} onHistorico={layout => abrirHistorico(layout, '')} /></Box>
      {page === PAGINA_HISTORICO ? <PainelTransmissoes api={api} uid={uid} eid={eid} setMessage={setMessage} master={master} entidadeNome={currentEntity ? `${currentEntity.legalName} — ${currentEntity.cnpjRoot}` : ''} filtroInicial={filtroHistorico} /> : null}
      {page === 'Configurações' && master ? <PainelConfiguracoes api={api} uid={uid} master={master} itens={menu} visivel={menusVisiveis} onChange={alternarMenu} setMessage={setMessage} /> : page === PAGINA_CERTIFICADO ? <PainelCertificado {...certificadoProps} /> : page === 'Entidades' && master ? <EntityAdmin api={api} onChanged={load} setMessage={setMessage} master={master} /> : page === 'Usuários' && master ? <UserAdmin api={api} currentUserId={uid} onSelfMaster={setMaster} setMessage={setMessage} /> : null}
    </Box>
  </Box>;
}

function ImportWorkspace({ api, uid, eid, entities, setEid, setMessage, master, onHistorico }) {
  const [layout, setLayout] = useState('D-1001');
  const [file, setFile] = useState();
  const [uploadResult, setUploadResult] = useState();
  const [csvResult, setCsvResult] = useState();
  const [csvText, setCsvText] = useState('');
  const [arquivoCsv, setArquivoCsv] = useState();
  const [codificacao, setCodificacao] = useState('UTF-8');
  const [validando, setValidando] = useState(false);
  const [etapa, setEtapa] = useState(0);
  const [envio, setEnvio] = useState();
  const [listaEnvio, setListaEnvio] = useState(0);
  const [enviando, setEnviando] = useState(false);
  const [progressoEnvio, setProgressoEnvio] = useState('');
  const [erroEnvio, setErroEnvio] = useState(null);
  const envioEmCurso = useRef(false);
  const currentLayout = layout;
  const critiques = (uploadResult?.arquivos || []).flatMap(arquivo => (arquivo.criticas || []).map(item => ({ arquivo: arquivo.nomeArquivo, idEvento: arquivo.idEvento, ...item })));
  const entidade = entities.find(item => item.id === eid);

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
      setEnvio(undefined);
      setEtapa(1);
      setMessage(response.data.valido ? `${nome || currentLayout} validado localmente. A declaração ainda não foi enviada.` : `${nome || currentLayout} possui críticas. Corrija o arquivo antes de enviar.`);
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
        setEnvio(undefined);
        setEtapa(1);
        setMessage(response.data.valido ? `XML ${currentLayout} validado localmente. A declaração ainda não foi enviada.` : `XML ${currentLayout} possui críticas. Corrija o arquivo antes de enviar.`);
        return;
      }
      const form = new FormData();
      form.append('file', arquivo);
      const response = await api.post('/api/layouts/d1001/import', form, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      setUploadResult(response.data);
      setCsvResult(undefined);
      setEnvio(undefined);
      setEtapa(1);
      setMessage(response.data.quantidadeInvalidos ? 'Foram encontradas críticas no lote. Corrija o arquivo antes de enviar.' : 'Lote validado localmente. A declaração ainda não foi enviada.');
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
        if (!deveRetentarAutomatico(interpretarErroTransmissao(error)) || tentativa === TENTATIVAS_ENVIO) throw error;
        await esperar(ESPERA_RETRY_MS);
      }
    }
    throw ultimo;
  }

  async function enviarSimulado(origem, conteudo, nome) {
    const response = await postarEnvio(origem, conteudo, nome);
    setEnvio(response.data);
    setListaEnvio(atual => atual + 1);
    if (response.data.enviado) setMessage(textoSituacaoEnvio(response.data.transmissao));
    else setMessage('');
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
      if (ok === eventos.length) setMessage(textoSituacaoEnvio(ultimo?.transmissao));
      else setMessage('');
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

  function aplicarSituacao(atualizadas) {
    if (!Array.isArray(atualizadas) || !envio?.transmissao?.batchId) return;
    const nova = atualizadas.find(item => item.batchId === envio.transmissao.batchId);
    if (!nova) return;
    const transmissao = { ...envio.transmissao };
    Object.entries(nova).forEach(([chave, valor]) => {
      if (valor != null && valor !== '') transmissao[chave] = valor;
    });
    setEnvio({ ...envio, transmissao });
    setMessage(textoSituacaoEnvio(transmissao));
  }

  function limparDados() {
    limparResultados();
    setFile(undefined);
    setArquivoCsv(undefined);
    setCsvText('');
    setEtapa(0);
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
  const arquivoValido = csvValido || xmlValido;
  const criticasAtivas = arquivoCsv ? criticasCsv : criticasXml;
  const resultadoAtivo = arquivoCsv ? csvResult : (csvResult || uploadResult);
  async function enviarArquivo() {
    if (bloqueiaEnvioSimultaneo(envioEmCurso.current)) return;
    envioEmCurso.current = true;
    setEnviando(true);
    setErroEnvio(null);
    setProgressoEnvio('Enviando à Receita Federal…');
    setMessage('');
    try {
      await dispararSimulacao();
      setErroEnvio(null);
      setEtapa(2);
    } catch (error) {
      setErroEnvio(interpretarErroTransmissao(error));
      setMessage('');
    } finally {
      envioEmCurso.current = false;
      setEnviando(false);
      setProgressoEnvio('');
    }
  }
  function irPara(indice) {
    if (enviando) return;
    if (indice === 0) setEtapa(0);
    if (indice === 1 && (csvResult || uploadResult)) setEtapa(1);
    if (indice === 2 && arquivoValido) setEtapa(2);
  }
  const passoArquivo = <Stack spacing={2}>
    <TextField select fullWidth label="Entidade" value={eid} disabled={!master && entities.length <= 1} onChange={event => { setEid(+event.target.value); limparDados(); }}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField>
    <Typography variant="subtitle2">Tipo de declaração</Typography>
    <RadioGroup value={layout} onChange={event => { setLayout(event.target.value); limparDados(); }}>
      {TIPOS_DECLARACAO.map(([codigo, rotulo]) => <FormControlLabel key={codigo} value={codigo} control={<Radio />} label={rotulo} />)}
    </RadioGroup>
    {layout === 'D-1199' ? <Typography color="text.secondary">O D-1199 aceita somente inclusão.</Typography> : null}
    <Box component="label" className="zona-arquivo">
      <UploadFileIcon />
      <Typography>Selecione o arquivo da declaração</Typography>
      <Typography variant="body2" color="text.secondary">Formatos aceitos: CSV, XML ou ZIP</Typography>
      <Typography variant="body2" color="text.secondary">A aplicação identificará o formato e realizará as validações correspondentes.</Typography>
      <input hidden type="file" accept=".csv,.xml,.zip" onChange={carregarArquivo} disabled={validando || enviando} />
    </Box>
    {validando && <Box><LinearProgress /><Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>Validando o arquivo…</Typography></Box>}
    <Stack direction="row" spacing={2} alignItems="center">
      <Button variant="outlined" onClick={() => baixarModelo().catch(() => setMessage('Falha ao baixar o modelo CSV'))}>Modelo CSV</Button>
      <Button variant="outlined" color="inherit" onClick={limparDados} disabled={enviando || (!nomeArquivo && !csvResult && !uploadResult)}>Limpar</Button>
    </Stack>
  </Stack>;
  const passoValidacao = <Stack spacing={2}>
    <Typography>Entidade: {entidade ? `${entidade.legalName} — ${entidade.cnpjRoot}` : '—'}</Typography>
    <Typography>Arquivo: {nomeArquivo || '—'} · {currentLayout}</Typography>
    {arquivoCsv && <TextField select label="Codificação" value={codificacao} onChange={event => alterarCodificacao(event.target.value)} sx={{ maxWidth: 220 }}><MenuItem value="UTF-8">UTF-8</MenuItem><MenuItem value="ANSI">ANSI</MenuItem></TextField>}
    {arquivoValido && <Alert severity="success">Arquivo validado localmente. A declaração ainda não foi enviada à Receita Federal.</Alert>}
    {criticasAtivas.map((item, indice) => <Alert key={indice} severity="warning">{fraseCritica(item)}</Alert>)}
    {criticasAtivas.length > 0 && resultadoAtivo && <ResumoResultado resultado={resultadoAtivo} aoRelatorio={resultadoAtivo.relatorioXlsx ? () => baixarXlsx(nomeXlsx(nomeArquivo, currentLayout), resultadoAtivo.relatorioXlsx) : undefined} criticas={criticasAtivas} />}
    <Stack direction="row" spacing={2}>
      <Button onClick={() => setEtapa(0)}>Voltar ao arquivo</Button>
      {arquivoValido && <Button variant="contained" onClick={() => setEtapa(2)}>Continuar para o envio</Button>}
      {csvValido && csvResult.xml && <Button variant="outlined" onClick={() => baixar(`${currentLayout.toLowerCase()}.xml`, csvResult.xml)}>Baixar XML</Button>}
    </Stack>
  </Stack>;
  const passoEnvio = <Stack spacing={2}>
    <Typography>Entidade: {entidade ? `${entidade.legalName} — ${entidade.cnpjRoot}` : '—'} · {currentLayout}</Typography>
    <Typography>Arquivo: {nomeArquivo || '—'}</Typography>
    {!envio && <Alert severity="info">A validação local foi concluída. Confirme para assinar e transmitir. O protocolo de recepção não significa que a declaração foi aceita.</Alert>}
    {!envio && <Button variant="contained" disabled={enviando || !arquivoValido} onClick={enviarArquivo} startIcon={enviando ? <CircularProgress size={16} color="inherit" /> : null}>{enviando ? 'Enviando…' : rotuloAcaoEnvio(false)}</Button>}
    {enviando && <Box><LinearProgress /><Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>{progressoEnvio || 'Enviando à Receita Federal…'}</Typography></Box>}
    {erroEnvio && <TransmissionErrorAlert erro={erroEnvio} aoRepetir={enviarArquivo} repetindo={enviando} />}
    {envio && <Alert severity={envio.enviado ? 'info' : 'warning'}>{envio.enviado ? textoSituacaoEnvio(envio.transmissao) : (motivoRecusa(envio) || 'Envio recusado antes da recepção.')}</Alert>}
    {envio && <EnvioSimuladoCard envio={envio} api={api} uid={uid} eid={eid} />}
    <Button onClick={() => setEtapa(1)} disabled={enviando}>Voltar à validação</Button>
  </Stack>;
  return <Stack spacing={2}>
    <Card><CardContent><Stack spacing={3}>
      <Stepper activeStep={etapa}>
        {['Arquivo', 'Validação', 'Envio'].map((rotulo, indice) => <Step key={rotulo}><StepLabel onClick={() => irPara(indice)} sx={{ cursor: 'pointer' }}>{rotulo}</StepLabel></Step>)}
      </Stepper>
      {etapa === 0 ? passoArquivo : etapa === 1 ? passoValidacao : passoEnvio}
    </Stack></CardContent></Card>
    <PainelTransmissoes api={api} uid={uid} eid={eid} setMessage={setMessage} layout={currentLayout} reloadToken={listaEnvio} master={master} resumo onVerHistorico={() => onHistorico && onHistorico(currentLayout)} onSituacaoAtualizada={aplicarSituacao} />
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
  if (operacao === '1' || operacao === 1) return 'Inclusão';
  if (operacao === '2' || operacao === 2) return 'Alteração';
  if (operacao === '3' || operacao === 3) return 'Exclusão';
  return operacao || '—';
}

function rotuloStatusEnvio(status) {
  if (status === 'RESULTADO_DESCONHECIDO') return 'Resultado desconhecido';
  if (status === 'PROCESSING' || status === 'PROTOCOL_RECEIVED') return 'Recebida para processamento';
  if (status === 'ACCEPTED') return 'Aceita';
  if (status === 'ACCEPTED_WITH_WARNINGS') return 'Aceita com avisos';
  if (status === 'REJECTED') return 'Rejeitada';
  return status || 'Enviada';
}

function grupoSituacao(status) {
  if (status === 'RESULTADO_DESCONHECIDO') return 'desconhecido';
  if (status === 'ACCEPTED' || status === 'ACCEPTED_WITH_WARNINGS') return 'aceita';
  if (status === 'REJECTED') return 'erro';
  if (status === 'PROCESSING' || status === 'PROTOCOL_RECEIVED') return 'processando';
  return 'outro';
}

function textoSituacaoEnvio(transmissao) {
  if (!transmissao) return 'Enviada. A situação na Receita ainda não foi confirmada.';
  if (grupoSituacao(transmissao.status) === 'desconhecido') return 'O lote pode ter sido recebido pela Receita, mas o protocolo não foi confirmado. Não reenvie este identificador. Concilie pelo protocolo, se a Receita o tiver emitido.';
  const situacao = rotuloStatusEnvio(transmissao.status);
  if (grupoSituacao(transmissao.status) === 'aceita') return `Aceita pela Receita. ${transmissao.receiptNumber ? `Recibo ${transmissao.receiptNumber}.` : ''}`.trim();
  if (grupoSituacao(transmissao.status) === 'erro') return 'Rejeitada pela Receita. O protocolo registra a recepção, não a aceitação.';
  if (transmissao.protocol) return `Enviada. Protocolo ${transmissao.protocol}. Situação: ${situacao}. O protocolo não significa aceitação.`;
  return `Enviada. Situação: ${situacao}.`;
}

function fraseCritica(item) {
  const linha = item.linha ? `Linha ${item.linha}` : 'Arquivo';
  const campo = item.coluna ? `, campo ${item.coluna}` : '';
  const problema = item.problema || 'O campo informado não corresponde ao formato exigido.';
  const valor = item.valorEncontrado ? ` Valor recebido: ${item.valorEncontrado}.` : '';
  const esperado = item.valorEsperado ? ` Formato esperado: ${item.valorEsperado}.` : '';
  return `${linha}${campo} — ${problema}${valor}${esperado} Corrija o arquivo e tente novamente.`;
}

function rotuloCanalAmbiente(codigo) {
  if (codigo === 'staging') return 'Simulado';
  if (codigo === 'homologacao') return 'Pré-produção';
  if (codigo === 'producao') return 'Produção';
  return 'Local';
}

function certificadoPrincipal(certificados) {
  if (!certificados?.length) return null;
  return [...certificados].sort((a, b) => new Date(b.validUntil || 0) - new Date(a.validUntil || 0))[0];
}

function dentroDoPeriodo(linha, periodo) {
  if (periodo === 'tudo') return true;
  const marca = new Date(linha.sentAt || linha.createdAt);
  if (Number.isNaN(marca.getTime())) return false;
  const agora = new Date();
  if (periodo === 'ano') return marca.getFullYear() === agora.getFullYear();
  return marca.getMonth() === agora.getMonth() && marca.getFullYear() === agora.getFullYear();
}

async function copiarTexto(texto, setMessage) {
  if (!texto) return;
  try {
    await navigator.clipboard.writeText(texto);
    setMessage('Copiado.');
  } catch {
    setMessage('Não foi possível copiar.');
  }
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

function formatarDia(iso) {
  if (!iso) return '—';
  const data = new Date(iso);
  if (Number.isNaN(data.getTime())) return '—';
  return data.toLocaleDateString('pt-BR');
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
  const motivoRetorno = diagnosticoRetornoReceita(transmissao?.returnXml);
  return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}>{motivoRetorno && <TransmissionErrorAlert erro={motivoRetorno} />}<Typography variant="subtitle1">{rotuloCanal(transmissao)}</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao?.protocol || '—'}`} /><Chip color={transmissao?.receiptNumber ? 'success' : 'warning'} label={transmissao?.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao?.status)} /></Stack><Typography color="text.secondary">Protocolo é a recepção do lote. O recibo é consultado automaticamente a cada 1 minuto.</Typography><Stack spacing={0.5} alignItems="flex-start">{transmissao?.eventXml && <AcaoDownload rotulo="XML do evento" texto="XML do evento enviado à Receita, sem a assinatura digital." onClick={() => baixar(`${transmissao.layout || 'evento'}.xml`, transmissao.eventXml)} />}{transmissao?.batchXml && <AcaoDownload rotulo="XML do lote" texto="XML do lote enviado à Receita, com o evento assinado." onClick={() => baixar('lote.xml', transmissao.batchXml)} />}{transmissao?.returnXml && <AcaoDownload rotulo="XML de retorno" texto="XML de retorno da Receita, da recepção ou da consulta do lote." onClick={() => baixar('retorno.xml', transmissao.returnXml)} />}{transmissao?.csv && <AcaoDownload rotulo="CSV" texto="CSV de origem usado na geração ou conversão do evento." onClick={() => baixar(`${transmissao.layout || 'evento'}.csv`, transmissao.csv)} />}{transmissao?.batchId && <AcaoDownload rotulo="PDF do protocolo" texto="PDF com os dados do protocolo em tabela." onClick={baixarPdf} destaque />}</Stack></Stack></Paper>;
}

function PainelTransmissoes({ api, uid, eid, setMessage, layout, reloadToken, master, resumo, onVerHistorico, onSituacaoAtualizada, entidadeNome, filtroInicial }) {
  const [linhas, setLinhas] = useState([]);
  const [atualizandoSituacao, setAtualizandoSituacao] = useState(false);
  const [segundosBloqueio, setSegundosBloqueio] = useState(0);
  const bloqueioAte = useRef(0);
  const [detalhe, setDetalhe] = useState();
  const [tentativas, setTentativas] = useState([]);
  const [de, setDe] = useState('');
  const [ate, setAte] = useState('');
  const [filtroLayout, setFiltroLayout] = useState(filtroInicial?.layout || '');
  const [filtroOperacao, setFiltroOperacao] = useState('');
  const [filtroProtocolo, setFiltroProtocolo] = useState('');
  const [filtroStatus, setFiltroStatus] = useState(filtroInicial?.situacao || '');
  const [avancados, setAvancados] = useState(false);
  const [consultaAberta, setConsultaAberta] = useState(false);
  const linhasRef = useRef([]);
  const cabecalhos = { 'X-User-Id': uid, 'X-Entity-Id': eid };
  async function carregar() {
    const response = await api.get('/api/transmissions', { headers: cabecalhos });
    const todas = Array.isArray(response.data) ? response.data : [];
    const base = layout ? todas.filter(item => item.layout === layout) : todas;
    setLinhas(base);
    linhasRef.current = base;
    return base;
  }
  useEffect(() => {
    if (segundosBloqueio <= 0) return undefined;
    const timer = setTimeout(() => setSegundosBloqueio(Math.max(0, Math.ceil((bloqueioAte.current - Date.now()) / 1000))), 1000);
    return () => clearTimeout(timer);
  }, [segundosBloqueio]);
  async function atualizarSituacao() {
    if (atualizandoSituacao || Date.now() < bloqueioAte.current) return;
    bloqueioAte.current = Date.now() + 10000;
    setSegundosBloqueio(10);
    setAtualizandoSituacao(true);
    try {
      const pendentes = linhasRef.current.filter(item => item.batchId && !item.receiptNumber && grupoSituacao(item.status) === 'processando');
      const consultadas = [];
      for (const linha of pendentes) {
        try {
          const response = await api.post(`/api/transmissions/${linha.batchId}/query`, {}, { headers: cabecalhos });
          if (response.data) consultadas.push(response.data);
        } catch {}
      }
      const base = await carregar();
      if (onSituacaoAtualizada) onSituacaoAtualizada(consultadas.length ? consultadas : base);
    } catch {
      setMessage('Falha ao atualizar a situação');
    } finally {
      setAtualizandoSituacao(false);
    }
  }
  useEffect(() => { if (uid && eid) carregar().catch(() => setMessage('Falha ao carregar envios')); }, [uid, eid, layout, reloadToken]);
  useEffect(() => {
    if (!filtroInicial) return;
    setFiltroLayout(filtroInicial.layout || '');
    setFiltroStatus(filtroInicial.situacao || '');
  }, [filtroInicial?.layout, filtroInicial?.situacao]);
  useEffect(() => {
    if (!uid || !eid || resumo) return undefined;
    const timer = setInterval(async () => {
      const pendentes = linhasRef.current.filter(item => item.batchId && !item.receiptNumber && grupoSituacao(item.status) === 'processando');
      if (!pendentes.length) return;
      for (const linha of pendentes) {
        try { await api.post(`/api/transmissions/${linha.batchId}/query`, {}, { headers: cabecalhos }); } catch {}
      }
      try { await carregar(); } catch {}
    }, 60000);
    return () => clearInterval(timer);
  }, [uid, eid, resumo]);
  async function abrir(linha) {
    const response = await api.get(`/api/transmissions/${linha.batchId}`, { headers: cabecalhos });
    setDetalhe(response.data);
    setTentativas([]);
    if (response.data?.eventId) {
      try {
        const historico = await api.get(`/api/transmissions/events/${response.data.eventId}/attempts`, { headers: cabecalhos });
        setTentativas(Array.isArray(historico.data) ? historico.data : []);
      } catch { setTentativas([]); }
    }
  }
  async function excluir(linha) {
    if (!linha?.batchId || !master) return;
    if (!window.confirm('Esta ação remove só o registro local. Não envia um evento de exclusão à Receita Federal. Deseja continuar?')) return;
    await api.delete(`/api/transmissions/${linha.batchId}`, { headers: cabecalhos });
    setDetalhe(undefined);
    await carregar();
    setMessage('Registro local excluído. Nenhum evento de exclusão foi enviado à Receita.');
  }
  function noIntervalo(linha) {
    const marca = linha.sentAt || linha.createdAt;
    if (!marca) return !de && !ate;
    const dia = new Date(marca);
    if (Number.isNaN(dia.getTime())) return true;
    if (de && dia < new Date(`${de}T00:00:00`)) return false;
    if (ate && dia > new Date(`${ate}T23:59:59`)) return false;
    return true;
  }
  const filtradas = linhas.filter(linha => {
    if (!layout && filtroLayout && linha.layout !== filtroLayout) return false;
    if (filtroOperacao && String(linha.operationType) !== filtroOperacao) return false;
    if (filtroProtocolo && !(linha.protocol || '').toLowerCase().includes(filtroProtocolo.toLowerCase())) return false;
    if (filtroStatus === 'PENDENCIAS' && !['processando', 'erro', 'desconhecido'].includes(grupoSituacao(linha.status))) return false;
    if (filtroStatus && filtroStatus !== 'PENDENCIAS' && linha.status !== filtroStatus) return false;
    return noIntervalo(linha);
  });
  const visiveis = resumo ? [...filtradas].sort((a, b) => new Date(b.sentAt || b.createdAt || 0) - new Date(a.sentAt || a.createdAt || 0)).slice(0, 3) : filtradas;
  async function emitirRelatorio() {
    const response = await api.post('/api/transmissions/relatorio.pdf', filtradas, { responseType: 'blob', headers: cabecalhos });
    baixarBlob('relatorio-transmissoes.pdf', response.data);
  }
  async function baixarPdf(linha) {
    const response = await api.get(`/api/transmissions/${linha.batchId}/protocolo.pdf`, { responseType: 'blob', headers: cabecalhos });
    baixarBlob(`protocolo-${linha.protocol || linha.batchId}.pdf`, response.data);
  }
  const colunas = resumo ? ['Data', 'Arquivo', 'Situação'] : ['Data', 'Declaração', 'Operação', 'Situação', 'Detalhes'];
  return <Card><CardContent><Stack spacing={2}>
    <Stack direction="row" justifyContent="space-between" alignItems="center">
      <Typography variant="h6">{resumo ? 'Últimos envios deste leiaute' : 'Histórico de envios'}</Typography>
      <Stack direction="row" spacing={1} alignItems="center">
        {resumo ? <Button variant="outlined" disabled={atualizandoSituacao || segundosBloqueio > 0} onClick={atualizarSituacao}>{atualizandoSituacao ? 'Atualizando…' : segundosBloqueio > 0 ? `Aguarde ${segundosBloqueio}s` : 'Atualizar situação'}</Button> : null}
        {resumo && onVerHistorico ? <Button onClick={onVerHistorico}>Ver histórico completo</Button> : null}
      </Stack>
    </Stack>
    {!resumo && <Typography color="text.secondary">A situação mostra o retorno da Receita. Protocolo é a recepção do lote. Aceita é o processamento concluído com recibo.</Typography>}
    {!resumo && <Stack direction="row" spacing={2} flexWrap="wrap" useFlexGap>
      <TextField type="date" label="Data inicial" InputLabelProps={{ shrink: true }} value={de} onChange={event => setDe(event.target.value)} sx={{ minWidth: 170 }} />
      <TextField type="date" label="Data final" InputLabelProps={{ shrink: true }} value={ate} onChange={event => setAte(event.target.value)} sx={{ minWidth: 170 }} />
      <TextField select label="Layout" value={filtroLayout} onChange={event => setFiltroLayout(event.target.value)} sx={{ minWidth: 160 }}><MenuItem value="">Todos</MenuItem>{TIPOS_DECLARACAO.map(([codigo]) => <MenuItem key={codigo} value={codigo}>{codigo}</MenuItem>)}</TextField>
      <TextField select label="Situação" value={filtroStatus} onChange={event => setFiltroStatus(event.target.value)} sx={{ minWidth: 240 }}><MenuItem value="">Todas</MenuItem><MenuItem value="PENDENCIAS">Pendências</MenuItem><MenuItem value="PROTOCOL_RECEIVED">Recebida para processamento</MenuItem><MenuItem value="PROCESSING">Recebida para processamento</MenuItem><MenuItem value="ACCEPTED">Aceita</MenuItem><MenuItem value="ACCEPTED_WITH_WARNINGS">Aceita com avisos</MenuItem><MenuItem value="REJECTED">Rejeitada</MenuItem></TextField>
      <Button variant="contained" onClick={() => emitirRelatorio().catch(() => setMessage('Falha ao exportar o relatório'))} disabled={!filtradas.length}>Exportar relatório PDF</Button>
      <Button onClick={() => setAvancados(atual => !atual)}>{avancados ? 'Ocultar filtros' : 'Filtros avançados'}</Button>
    </Stack>}
    {!resumo && <Collapse in={avancados}><Stack direction="row" spacing={2} flexWrap="wrap" useFlexGap>
      <TextField select label="Operação" value={filtroOperacao} onChange={event => setFiltroOperacao(event.target.value)} sx={{ minWidth: 160 }}><MenuItem value="">Todas</MenuItem><MenuItem value="1">Inclusão</MenuItem><MenuItem value="2">Alteração</MenuItem><MenuItem value="3">Exclusão</MenuItem></TextField>
      <TextField label="Protocolo" value={filtroProtocolo} onChange={event => setFiltroProtocolo(event.target.value)} sx={{ minWidth: 240 }} />
    </Stack></Collapse>}
    {!resumo && <Box><Button onClick={() => setConsultaAberta(atual => !atual)}>{consultaAberta ? 'Fechar consulta de protocolo' : 'Consultar protocolo'}</Button><Collapse in={consultaAberta}><Box sx={{ mt: 2 }}><PainelConsulta api={api} uid={uid} eid={eid} layout={filtroLayout} /></Box></Collapse></Box>}
    {!visiveis.length && <Alert severity="info">Nenhum envio para os filtros informados.</Alert>}
    {visiveis.length > 0 && <Box className="tabela-criticas-wrap" sx={{ maxHeight: 'none' }}><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{colunas.map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{visiveis.map(linha => <Box component="tr" key={linha.batchId || linha.eventId}><Box component="td">{formatarDia(linha.sentAt || linha.createdAt)}</Box>{resumo ? <Box component="td">{linha.sourceName || '—'}</Box> : <><Box component="td">{linha.layout}</Box><Box component="td">{rotuloOperacaoEnvio(linha.operationType)}</Box></>}<Box component="td"><Chip size="small" color={grupoSituacao(linha.status) === 'aceita' ? 'success' : grupoSituacao(linha.status) === 'erro' ? 'error' : 'warning'} label={rotuloStatusEnvio(linha.status)} /></Box>{!resumo && <Box component="td">{linha.batchId && <Button size="small" onClick={() => abrir(linha).catch(() => setMessage('Falha ao abrir os detalhes'))}>Detalhes</Button>}</Box>}</Box>)}</Box></Box></Box>}
    <Dialog open={Boolean(detalhe)} onClose={() => setDetalhe(undefined)} fullWidth maxWidth="md">
      <DialogTitle>Detalhes do envio</DialogTitle>
      <DialogContent><Stack spacing={1.5} sx={{ pt: 1 }}>
        <Typography>Arquivo: {detalhe?.sourceName || '—'}</Typography>
        <Typography>Entidade: {entidadeNome || '—'}</Typography>
        <Typography>Declaração: {detalhe?.layout || '—'} · {rotuloOperacaoEnvio(detalhe?.operationType)}</Typography>
        <Stack direction="row" spacing={1} alignItems="center"><Typography sx={{ wordBreak: 'break-all' }}>Identificador: {detalhe?.eventIdentifier || '—'}</Typography>{detalhe?.eventIdentifier && <Button size="small" onClick={() => copiarTexto(detalhe.eventIdentifier, setMessage)}>Copiar</Button>}</Stack>
        <Stack direction="row" spacing={1} alignItems="center"><Typography sx={{ wordBreak: 'break-all' }}>Protocolo: {detalhe?.protocol || '—'}</Typography>{detalhe?.protocol && <Button size="small" onClick={() => copiarTexto(detalhe.protocol, setMessage)}>Copiar</Button>}</Stack>
        <Stack direction="row" spacing={1} alignItems="center"><Typography sx={{ wordBreak: 'break-all' }}>Recibo: {detalhe?.receiptNumber || '—'}</Typography>{detalhe?.receiptNumber && <Button size="small" onClick={() => copiarTexto(detalhe.receiptNumber, setMessage)}>Copiar</Button>}</Stack>
        <Chip sx={{ alignSelf: 'flex-start' }} color={grupoSituacao(detalhe?.status) === 'aceita' ? 'success' : grupoSituacao(detalhe?.status) === 'erro' ? 'error' : 'warning'} label={rotuloStatusEnvio(detalhe?.status)} />
        <Typography variant="subtitle2">Histórico de processamento</Typography>
        {!tentativas.length && <Typography color="text.secondary">Nenhuma tentativa registrada.</Typography>}
        {tentativas.map(item => <Typography key={item.id} color="text.secondary">{formatarDataEnvio(item.startedAt)} — tentativa {item.attempt} — {item.outcome}</Typography>)}
        {detalhe && <EnvioSimuladoCard envio={{ enviado: true, transmissao: detalhe }} api={api} uid={uid} eid={eid} />}
        {detalhe?.batchId && <Button onClick={() => baixarPdf(detalhe).catch(() => setMessage('Falha ao gerar o PDF do protocolo'))}>PDF do protocolo</Button>}
      </Stack></DialogContent>
      <DialogActions>
        {master && detalhe?.batchId && <Button color="error" onClick={() => excluir(detalhe).catch(error => setMessage(error.response?.data?.message || error.response?.data?.error || 'Falha ao excluir o registro local'))}>Excluir registro local</Button>}
        <Button onClick={() => setDetalhe(undefined)}>Fechar</Button>
      </DialogActions>
    </Dialog>
  </Stack></CardContent></Card>;
}

function Dashboard({ api, uid, eid, entities, setEid, master, certificados, onNova, onPendencias }) {
  const [linhas, setLinhas] = useState([]);
  const [periodo, setPeriodo] = useState('mes');
  const [erro, setErro] = useState('');
  useEffect(() => {
    if (!uid || !eid) return;
    api.get('/api/transmissions', { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } })
      .then(response => { setLinhas(Array.isArray(response.data) ? response.data : []); setErro(''); })
      .catch(() => setErro('Falha ao carregar os envios.'));
  }, [uid, eid]);
  const doPeriodo = linhas.filter(linha => dentroDoPeriodo(linha, periodo));
  const enviadas = doPeriodo.length;
  const aceitas = doPeriodo.filter(linha => grupoSituacao(linha.status) === 'aceita').length;
  const processando = doPeriodo.filter(linha => grupoSituacao(linha.status) === 'processando').length;
  const comErro = doPeriodo.filter(linha => grupoSituacao(linha.status) === 'erro').length;
  const certificado = resumoCertificado(certificados);
  const ultimos = [...linhas].sort((a, b) => new Date(b.sentAt || b.createdAt || 0) - new Date(a.sentAt || a.createdAt || 0)).slice(0, 3);
  return <Stack spacing={2}>
    <Card><CardContent><Stack spacing={2}>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="h6">Visão geral</Typography>
        <TextField select label="Período" value={periodo} onChange={event => setPeriodo(event.target.value)} sx={{ minWidth: 160 }}><MenuItem value="mes">Este mês</MenuItem><MenuItem value="ano">Este ano</MenuItem><MenuItem value="tudo">Tudo</MenuItem></TextField>
      </Stack>
      <TextField select fullWidth label="Entidade" value={eid} disabled={!master && entities.length <= 1} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField>
      {erro && <Alert severity="error">{erro}</Alert>}
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}><Metric title="Declarações enviadas" value={String(enviadas)} detail="No período selecionado" /></Grid>
        <Grid item xs={12} sm={6}><Metric title="Aceitas" value={String(aceitas)} detail="Processadas com sucesso" cor="success.main" /></Grid>
        <Grid item xs={12} sm={6}><Metric title="Em processamento" value={String(processando)} detail="Recebidas, ainda sem conclusão" /></Grid>
        <Grid item xs={12} sm={6}><Metric title="Com erro" value={String(comErro)} detail="Rejeitadas pela Receita" cor="error.main" /></Grid>
      </Grid>
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography color={certificado.cor}>{certificado.texto}</Typography>
        <Typography color="text.secondary">{certificado.detalhe}</Typography>
      </Stack>
    </Stack></CardContent></Card>
    <Button variant="contained" size="large" onClick={onNova}>Nova declaração</Button>
    <Card><CardContent><Stack spacing={2}>
      <Stack direction="row" justifyContent="space-between" alignItems="center"><Typography variant="h6">Últimos envios</Typography><Button onClick={onPendencias}>Ver pendências</Button></Stack>
      {!ultimos.length && <Alert severity="info">Nenhum envio nesta entidade.</Alert>}
      {ultimos.length > 0 && <Box className="tabela-criticas-wrap" sx={{ maxHeight: 'none' }}><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{['Layout', 'Arquivo', 'Situação'].map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{ultimos.map(linha => <Box component="tr" key={linha.batchId || linha.eventId}><Box component="td">{linha.layout}</Box><Box component="td">{linha.sourceName || '—'}</Box><Box component="td"><Typography color={grupoSituacao(linha.status) === 'aceita' ? 'success.main' : grupoSituacao(linha.status) === 'erro' ? 'error.main' : 'text.primary'}>{rotuloStatusEnvio(linha.status)}</Typography></Box></Box>)}</Box></Box></Box>}
    </Stack></CardContent></Card>
  </Stack>;
}

function resumoCertificado(certificados) {
  const atual = certificadoPrincipal(certificados);
  if (!atual) return { texto: 'Nenhum certificado cadastrado', detalhe: 'Cadastre para assinar as declarações', cor: 'warning.main' };
  if (atual.status === 'EXPIRING_SOON') return { texto: 'Certificado digital vence em breve', detalhe: `Até ${formatarDia(atual.validUntil)}`, cor: 'warning.main' };
  if (atual.status === 'VALID') return { texto: 'Certificado digital válido', detalhe: `Até ${formatarDia(atual.validUntil)}`, cor: 'success.main' };
  return { texto: 'Certificado digital indisponível', detalhe: `Até ${formatarDia(atual.validUntil)}`, cor: 'error.main' };
}

function Metric({ title, value, detail, cor }) { return <Card><CardContent><Typography color="text.secondary">{title}</Typography><Typography variant="h4" color={cor || 'text.primary'} sx={{ mt: 1 }}>{value}</Typography><Typography variant="body2" color="text.secondary">{detail}</Typography></CardContent></Card>; }
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
  return <Card><CardContent><Stack spacing={2}><Typography variant="h6">{form.id ? `Editar ${form.login}` : 'Novo usuário'}</Typography><Stack direction="row" spacing={2} alignItems="center"><TextField label="Login" value={form.login} onChange={event => setForm({ ...form, login: event.target.value })} /><TextField label="Nome" value={form.name} onChange={event => setForm({ ...form, name: event.target.value })} /><TextField label={form.id ? 'Nova senha' : 'Senha'} type="password" value={form.password} onChange={event => setForm({ ...form, password: event.target.value })} helperText={form.id ? 'Em branco mantém a senha' : ' '} /><Button variant={form.master ? 'contained' : 'outlined'} onClick={() => setForm({ ...form, master: !form.master })}>{form.master ? 'Master' : 'Comum'}</Button><Button variant="contained" onClick={saveUser}>{form.id ? 'Salvar' : 'Cadastrar'}</Button>{form.id && <Button onClick={() => setForm(empty)}>Cancelar</Button>}</Stack><Box component="table" sx={{ width: '100%', borderCollapse: 'collapse' }}><Box component="thead"><Box component="tr">{['Login', 'Nome', 'Master', 'Ações'].map(title => <Box component="th" key={title} sx={{ textAlign: 'left', py: 1 }}>{title}</Box>)}</Box></Box><Box component="tbody">{rows.map(user => <Box component="tr" key={user.id} sx={{ borderTop: 1, borderColor: 'divider' }}><Box component="td" sx={{ py: 1 }}>{user.login}</Box><Box component="td">{user.name}</Box><Box component="td"><Button size="small" variant={user.master ? 'contained' : 'outlined'} onClick={() => saveMaster(user, !user.master)}>{user.master ? 'Master' : 'Comum'}</Button></Box><Box component="td"><Button size="small" onClick={() => setForm({ id: user.id, login: user.login, name: user.name, password: '', master: user.master })}>Editar</Button><Button size="small" color="error" disabled={user.id === currentUserId} onClick={() => removeUser(user)}>Excluir</Button></Box></Box>)}</Box></Box></Stack></CardContent></Card>;
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
  if (!form) return <Card><CardContent><Stack spacing={2}><Stack direction="row" justifyContent="space-between"><Typography variant="h6">Entidades</Typography>{master && <Button variant="contained" onClick={openNew}>Nova entidade</Button>}</Stack>{entities.map(entity => <Box key={entity.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: 1, borderColor: 'divider', py: 1 }}><Box><Typography>{entity.legalName}</Typography><Typography variant="body2" color="text.secondary">{entity.cnpjRoot} · {(entity.userIds || []).length} usuário(s)</Typography></Box><Stack direction="row" spacing={1}><Button onClick={() => openEdit(entity)}>{master ? 'Editar' : 'Ver'}</Button>{master && <Button color="error" onClick={() => remove(entity)}>Excluir</Button>}</Stack></Box>)}</Stack></CardContent></Card>;
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
function PickColumn({ title, items, marked, onToggle }) { return <Box><Typography variant="subtitle2" mb={1}>{title}</Typography><Box className="pick-column">{items.map(user => <Button key={user.id} onClick={() => onToggle(user.id)} sx={{ bgcolor: marked.includes(user.id) ? 'primary.main' : 'transparent', color: marked.includes(user.id) ? 'primary.contrastText' : 'inherit' }}>{user.name} — {user.login}</Button>)}{!items.length && <Typography sx={{ p: 2 }} color="text.secondary">Nenhum usuário</Typography>}</Box></Box>; }

createRoot(document.getElementById('root')!).render(<React.StrictMode><ThemeProvider theme={temaDere} defaultMode="light" modeStorageKey="dere-theme-mode" colorSchemeStorageKey="dere-theme-mode" disableTransitionOnChange noSsr><CssBaseline /><App /><BarraAmbiente /></ThemeProvider></React.StrictMode>);
