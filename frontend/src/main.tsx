// @ts-nocheck
import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import { Alert, Box, Button, Card, CardContent, Checkbox, Chip, Divider, Drawer, FormControlLabel, Grid, List, ListItemButton, ListItemText, MenuItem, Paper, Stack, TextField, Typography } from '@mui/material';
import './style.css';
import logotipoDataA from './dataa-logotipo.png';

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

function baixar(nome, conteudo) {
  const tipo = nome.endsWith('.xml') ? 'application/xml;charset=utf-8' : 'text/csv;charset=utf-8';
  const url = URL.createObjectURL(new Blob([conteudo], { type: tipo }));
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

function App() {
  const [uid, setUid] = useState<number>();
  const [master, setMaster] = useState(false);
  const [login, setLogin] = useState('eduardo');
  const [password, setPassword] = useState('eduardo86');
  const [page, setPage] = useState('Dashboard');
  const [entities, setEntities] = useState<any[]>([]);
  const [eid, setEid] = useState(1);
  const [message, setMessage] = useState('');
  const [certificates, setCertificates] = useState<any[]>([]);
  const [certFile, setCertFile] = useState<File>();
  const [certLabel, setCertLabel] = useState('');
  const [certPassword, setCertPassword] = useState('');

  const load = async () => {
    try {
      const response = await api.get('/api/admin/entities');
      const list = Array.isArray(response.data) ? response.data : [];
      setEntities(list);
      if (list[0]) setEid(current => list.some(entity => entity.id === current) ? current : list[0].id);
    } catch { setMessage('Falha ao carregar entidades'); }
  };
  useEffect(() => { api.get('/api/admin/entities').then(response => setEntities(Array.isArray(response.data) ? response.data : [])).catch(() => {}); }, []);
  useEffect(() => { if (uid && eid) api.get(`/api/certificates/entity/${eid}`).then(response => setCertificates(Array.isArray(response.data) ? response.data : [])).catch(() => {}); }, [uid, eid]);

  async function signIn() {
    try { const response = await api.post('/api/auth/login', { login, password }); setUid(response.data.userId); setMaster(!!response.data.master); api.defaults.headers.common['X-User-Id'] = response.data.userId; setMessage(''); await load(); }
    catch { setMessage('Login inválido'); }
  }
  async function uploadCertificate() { if (!certFile || !certPassword || !certLabel) { setMessage('Informe nome, arquivo P12 e senha.'); return; } const form = new FormData(); form.append('label', certLabel); form.append('file', certFile); form.append('password', certPassword); try { await api.post(`/api/certificates/entity/${eid}`, form, { headers: { 'Content-Type': 'multipart/form-data' } }); setCertPassword(''); setCertFile(undefined); setCertLabel(''); const response = await api.get(`/api/certificates/entity/${eid}`); setCertificates(response.data); setMessage('Certificado armazenado com sucesso.'); } catch (error) { setMessage(error.response?.data?.message || 'Falha ao armazenar certificado.'); } }

  if (!uid) return <Box className="login-shell"><Card className="login-card"><CardContent><Typography variant="h4" mb={1}>DeRE</Typography><Typography color="text.secondary" mb={3}>Declarações de Regimes Específicos</Typography><Stack spacing={2}><TextField label="Login" value={login} onChange={event => setLogin(event.target.value)} /><TextField label="Senha" type="password" value={password} onChange={event => setPassword(event.target.value)} /><Button variant="contained" size="large" onClick={signIn}>Entrar</Button>{message && <Alert severity="error">{message}</Alert>}</Stack></CardContent></Card></Box>;

  const menu = ['Dashboard', 'D-1001', 'D-1011', 'Importações', 'Validações', 'Certificado Digital', 'Transmissões', 'Configurações', ...(master ? ['Usuários', 'Entidades'] : [])];
  const workspacePages = ['D-1001', 'D-1011'];
  const currentEntity = entities.find(entity => entity.id === eid);
  const workspaceProps = { api, uid, eid, entities, setEid, setMessage };
  const certificateCard = <Card><CardContent><Stack spacing={2}><Typography variant="h6">Certificado A1</Typography><Typography color="text.secondary">A senha é usada somente durante a importação e não é exibida novamente.</Typography><TextField label="Nome" value={certLabel} onChange={event => setCertLabel(event.target.value)} /><Button component="label" variant="outlined">Selecionar P12/PFX<input hidden type="file" accept=".p12,.pfx" onChange={event => setCertFile(event.target.files?.[0])} /></Button><TextField label="Senha do certificado" type="password" value={certPassword} onChange={event => setCertPassword(event.target.value)} /><Button variant="contained" onClick={uploadCertificate}>Armazenar certificado</Button><Divider />{certificates.map(cert => <Box key={cert.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}><Typography>{cert.label} · {cert.alias}</Typography><Chip color={cert.status === 'VALID' ? 'success' : 'warning'} label={cert.status === 'EXPIRING_SOON' ? `vence em ${cert.daysUntilExpiry} dias` : cert.status} /></Box>)}</Stack></CardContent></Card>;
  return <Box className="app-shell"><Drawer variant="permanent" className="sidebar"><Box className="brand"><img className="marca" src={logotipoDataA} alt="Data A" /><Typography variant="h5">DeRE</Typography><Typography variant="caption">EFPC / PREVIC</Typography></Box><List>{menu.map(item => <ListItemButton selected={page === item} onClick={() => { setPage(item); setMessage(''); }} key={item}><ListItemText primary={item} /></ListItemButton>)}</List></Drawer><Box component="main" className="content"><Stack direction="row" justifyContent="space-between" alignItems="center" mb={3}><Box><Typography variant="h4">{page}</Typography><Typography color="text.secondary">Operação local da declaração</Typography></Box><Chip label={currentEntity?.legalName || 'Entidade não selecionada'} /></Stack>{page === 'Dashboard' ? <Dashboard onImport={() => setPage('D-1001')} /> : null}<Box hidden={page !== 'D-1001'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1001" /></Box><Box hidden={page !== 'D-1011'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1011" /></Box>{page === 'Importações' ? <Alert severity="info">A conversão CSV/XML fica em D-1001 e D-1011. Este menu será o histórico de lotes (arquivo, layout, entidade, status e críticas).</Alert> : null}{page === 'Validações' ? <Alert severity="info">Validações será o relatório das críticas já apuradas nos lotes. A validação de um arquivo novo é feita no menu do layout.</Alert> : null}{page === 'Transmissões' ? <TransmissionsPanel api={api} uid={uid} eid={eid} setMessage={setMessage} /> : null}{page === 'Certificado Digital' ? certificateCard : page === 'Entidades' && master ? <EntityAdmin api={api} onChanged={load} setMessage={setMessage} /> : page === 'Usuários' && master ? <UserAdmin api={api} currentUserId={uid} onSelfMaster={setMaster} setMessage={setMessage} /> : !['Dashboard', 'Certificado Digital', 'Importações', 'Validações', 'Transmissões', ...workspacePages].includes(page) && !((page === 'Entidades' || page === 'Usuários') && master) ? <Alert severity="info">O módulo {page} está preparado para a próxima integração.</Alert> : null}{message && <Alert sx={{ mt: 2 }} severity="info">{message}</Alert>}</Box></Box>;
}

function ImportWorkspace({ api, uid, eid, entities, setEid, setMessage, lockedLayout }) {
  const [layout, setLayout] = useState(lockedLayout || 'D-1001');
  const [file, setFile] = useState();
  const [uploadResult, setUploadResult] = useState();
  const [csvResult, setCsvResult] = useState();
  const [csvText, setCsvText] = useState('');
  const [arquivoCsv, setArquivoCsv] = useState();
  const [codificacao, setCodificacao] = useState('UTF-8');
  const [simularEnvio, setSimularEnvio] = useState(false);
  const [envio, setEnvio] = useState();
  const [listaEnvio, setListaEnvio] = useState(0);
  const currentLayout = lockedLayout || layout;
  const critiques = (uploadResult?.arquivos || []).flatMap(arquivo => (arquivo.criticas || []).map(item => ({ arquivo: arquivo.nomeArquivo, idEvento: arquivo.idEvento, ...item })));
  const subtitle = lockedLayout === 'D-1001'
    ? 'Envie CSV ou XML do D-1001 para análise.'
    : lockedLayout === 'D-1011'
      ? 'Envie CSV ou XML do D-1011 para análise.'
      : 'Selecione o layout e envie CSV ou XML para análise.';

  async function uploadXml() {
    if (!file) { setMessage('Selecione um XML ou ZIP'); return; }
    try {
      if (currentLayout === 'D-1011') {
        const xml = await file.text();
        const response = await api.post('/api/layouts/d1011/validate-xml', xml, { headers: { 'Content-Type': 'application/xml', 'X-Entity-Id': eid } });
        setCsvResult(response.data);
        setUploadResult(undefined);
        setMessage(response.data.valido ? 'XML D-1011 validado.' : 'XML D-1011 possui críticas.');
        if (response.data.valido && simularEnvio) await enviarSimulado('XML', xml, file.name);
        return;
      }
      const form = new FormData();
      form.append('file', file);
      const response = await api.post('/api/layouts/d1001/import', form, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      setUploadResult(response.data);
      setMessage(response.data.quantidadeInvalidos ? 'Foram encontradas críticas no lote.' : 'Lote validado sem críticas.');
      if (!response.data.quantidadeInvalidos && simularEnvio && !file.name?.toLowerCase().endsWith('.zip')) {
        await enviarSimulado('XML', await file.text(), file.name);
      }
    } catch (error) { setMessage(error.response?.data?.message || error.response?.data?.error || error.response?.data?.detail || 'Falha no upload/validação'); }
  }

  async function validateCsv() {
    if (!csvText.trim()) { setMessage('Selecione um arquivo CSV.'); return; }
    try {
      const endpoint = currentLayout === 'D-1011' ? '/api/layouts/d1011/validate-csv' : '/api/validation/csv-to-xml';
      const response = await api.post(endpoint, csvText, { headers: { 'Content-Type': 'text/csv', 'X-Entity-Id': eid } });
      setCsvResult(response.data);
      setMessage(response.data.valido ? `${currentLayout} validado com sucesso.` : `${currentLayout} possui críticas.`);
      if (response.data.valido && simularEnvio) await enviarSimulado('CSV', csvText, arquivoCsv?.name || `${currentLayout.toLowerCase()}.csv`);
    } catch (error) { setMessage(error.response?.data?.message || error.response?.data?.error || error.response?.data?.detail || 'Falha na validação CSV'); }
  }

  async function enviarSimulado(origem, conteudo, nome) {
    const response = await api.post('/api/transmissions/simulate', { layout: currentLayout, origem, arquivo: nome, conteudo }, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    setEnvio(response.data);
    setListaEnvio(atual => atual + 1);
    if (response.data.enviado) setMessage(`${currentLayout} recebido pela Receita (simulado). Protocolo ${response.data.transmissao?.protocol}.`);
    else setMessage(motivoRecusa(response.data) || 'Envio simulado recusado.');
    return response.data;
  }

  async function consultarRetorno() {
    if (!envio?.transmissao?.batchId) return;
    const response = await api.post(`/api/transmissions/${envio.transmissao.batchId}/query`, {}, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    setEnvio({ ...envio, transmissao: response.data });
    setListaEnvio(atual => atual + 1);
    setMessage(response.data.receiptNumber ? `Retorno consultado. Recibo ${response.data.receiptNumber}.` : 'Consulta do lote concluída.');
  }

  function limparResultados() {
    setCsvResult(undefined);
    setUploadResult(undefined);
    setEnvio(undefined);
    setMessage('');
  }

  function limparDados() {
    limparResultados();
    setFile(undefined);
    setArquivoCsv(undefined);
    setCsvText('');
  }

  async function aplicarCodificacao(arquivo, valor) {
    if (!arquivo) return;
    setCsvText(await lerTextoArquivo(arquivo, valor));
    setCsvResult(undefined);
    setUploadResult(undefined);
  }

  function loadCsvFile(event) {
    const selected = event.target.files?.[0];
    limparResultados();
    setFile(undefined);
    setArquivoCsv(selected);
    if (selected) aplicarCodificacao(selected, codificacao);
    else setCsvText('');
    event.target.value = '';
  }

  function loadXmlFile(event) {
    const selected = event.target.files?.[0];
    limparResultados();
    setCsvText('');
    setArquivoCsv(undefined);
    setFile(selected);
    event.target.value = '';
  }

  function alterarCodificacao(valor) {
    setCodificacao(valor);
    if (arquivoCsv) aplicarCodificacao(arquivoCsv, valor);
  }

  const criticasCsv = csvResult?.criticas || [];
  const criticasXml = uploadResult?.criticas || critiques;
  const nomeArquivo = arquivoCsv?.name || file?.name;
  return <Stack spacing={2}><Card><CardContent><Stack spacing={2}><Typography variant="h6">Importação e validação</Typography><Typography color="text.secondary">{subtitle}</Typography>{!lockedLayout && <TextField select label="Leiaute" value={layout} onChange={event => { setLayout(event.target.value); setCsvResult(undefined); setUploadResult(undefined); }}><MenuItem value="D-1001">D-1001 — Informações do contribuinte</MenuItem><MenuItem value="D-1011">D-1011 — PGCC</MenuItem></TextField>}<TextField select fullWidth label="Entidade" value={eid} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField><Stack direction="row" spacing={2} alignItems="center" flexWrap="wrap" useFlexGap><Button component="label" variant="outlined">Selecionar CSV<input hidden type="file" accept=".csv" onChange={loadCsvFile} /></Button><Button component="label" variant="outlined">Selecionar XML/ZIP<input hidden type="file" accept=".xml,.zip" onChange={loadXmlFile} /></Button><TextField select label="Codificação" value={codificacao} onChange={event => alterarCodificacao(event.target.value)} sx={{ minWidth: 180 }}><MenuItem value="UTF-8">UTF-8</MenuItem><MenuItem value="ANSI">ANSI</MenuItem></TextField><Button variant="outlined" color="inherit" onClick={limparDados} disabled={!nomeArquivo && !csvResult && !uploadResult}>Limpar dados</Button></Stack>{nomeArquivo ? <Typography color="text.secondary">Arquivo: {nomeArquivo}{arquivoCsv ? ` · ${codificacao}` : ''}</Typography> : <Typography color="text.secondary">Nenhum arquivo selecionado.</Typography>}<FormControlLabel control={<Checkbox checked={simularEnvio} onChange={event => setSimularEnvio(event.target.checked)} />} label="Simular envio à Receita após validar" /><Stack direction="row" spacing={2}><Button variant="contained" onClick={validateCsv}>Validar CSV</Button><Button variant="contained" color="secondary" onClick={uploadXml}>Processar XML/ZIP</Button><Button variant="outlined" disabled={!simularEnvio || !(csvResult?.valido || (uploadResult && !uploadResult.quantidadeInvalidos))} onClick={async () => { try { const origem = arquivoCsv ? 'CSV' : 'XML'; const conteudo = arquivoCsv ? csvText : file ? await file.text() : csvResult?.xml; await enviarSimulado(origem, conteudo, nomeArquivo); } catch (error) { setMessage(error.response?.data?.message || error.response?.data?.error || error.response?.data?.detail || 'Falha no envio simulado'); } }}>Simular envio</Button></Stack>{envio && <EnvioSimuladoCard envio={envio} aoConsultar={consultarRetorno} />}{csvResult && <ResumoResultado resultado={csvResult} rotuloDownload="Baixar XML" aoBaixar={csvResult.xml ? () => baixar(`${currentLayout.toLowerCase()}.xml`, csvResult.xml) : undefined} aoRelatorio={csvResult.relatorioXlsx ? () => baixarXlsx(nomeXlsx(arquivoCsv?.name || file?.name || 'arquivo.csv', currentLayout), csvResult.relatorioXlsx) : undefined} criticas={criticasCsv} />}{uploadResult && <ResumoResultado resultado={uploadResult} rotuloDownload="Baixar CSV" aoBaixar={uploadResult.csvConvertido ? () => baixar(nomeCsvDaOrigem(file?.name || uploadResult.nomeOrigem), uploadResult.csvConvertido) : undefined} aoRelatorio={uploadResult.relatorioXlsx ? () => baixarXlsx(nomeXlsx(file?.name || uploadResult.nomeOrigem, currentLayout), uploadResult.relatorioXlsx) : undefined} criticas={criticasXml} />}</Stack></CardContent></Card><TransmissionsPanel api={api} uid={uid} eid={eid} setMessage={setMessage} layout={currentLayout} reloadToken={listaEnvio} /></Stack>;
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

function EnvioSimuladoCard({ envio, aoConsultar }) {
  const transmissao = envio?.transmissao;
  if (!envio) return null;
  if (!envio.enviado) {
    return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Alert severity="warning">{motivoRecusa(envio) || 'Envio simulado recusado.'}</Alert>{transmissao?.protocol ? <Stack spacing={1}><Typography color="text.secondary">Lote já existente para este id.</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao.protocol}`} /><Chip color={transmissao.receiptNumber ? 'success' : 'warning'} label={transmissao.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao.status)} /></Stack><Stack direction="row" spacing={2}>{!transmissao.receiptNumber && <Button variant="contained" onClick={aoConsultar}>Consultar retorno</Button>}</Stack></Stack> : null}</Stack></Paper>;
  }
  return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Typography variant="subtitle1">Envio simulado</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao?.protocol || '—'}`} /><Chip color={transmissao?.receiptNumber ? 'success' : 'warning'} label={transmissao?.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao?.status)} /></Stack><Typography color="text.secondary">Protocolo é a recepção do lote. Recibo só existe depois da consulta do processamento.</Typography><Stack direction="row" spacing={2}>{!transmissao?.receiptNumber && <Button variant="contained" onClick={aoConsultar}>Consultar retorno</Button>}{transmissao?.eventXml && <Button onClick={() => baixar(`${transmissao.layout || 'evento'}.xml`, transmissao.eventXml)}>XML do evento</Button>}{transmissao?.batchXml && <Button onClick={() => baixar('lote.xml', transmissao.batchXml)}>XML do lote</Button>}{transmissao?.returnXml && <Button onClick={() => baixar('retorno.xml', transmissao.returnXml)}>XML de retorno</Button>}{transmissao?.csv && <Button onClick={() => baixar(`${transmissao.layout || 'evento'}.csv`, transmissao.csv)}>CSV</Button>}</Stack></Stack></Paper>;
}

function TransmissionsPanel({ api, uid, eid, setMessage, layout, reloadToken }) {
  const [linhas, setLinhas] = useState([]);
  const [detalhe, setDetalhe] = useState();
  async function carregar() {
    const response = await api.get('/api/transmissions', { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    const todas = Array.isArray(response.data) ? response.data : [];
    setLinhas(layout ? todas.filter(item => item.layout === layout) : todas);
  }
  useEffect(() => { if (uid && eid) carregar().catch(() => setMessage('Falha ao carregar transmissões')); }, [uid, eid, layout, reloadToken]);
  async function consultar(linha) {
    const response = await api.post(`/api/transmissions/${linha.batchId}/query`, {}, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    setDetalhe(response.data);
    await carregar();
    setMessage(response.data.receiptNumber ? `Recibo ${response.data.receiptNumber}` : 'Consulta concluída');
  }
  async function abrir(linha) {
    const response = await api.get(`/api/transmissions/${linha.batchId}`, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    setDetalhe(response.data);
  }
  const colunas = layout ? ['Arquivo', 'Id', 'Protocolo', 'Recibo', 'Status', 'Ações'] : ['Layout', 'Arquivo', 'Id', 'Protocolo', 'Recibo', 'Status', 'Ações'];
  return <Card><CardContent><Stack spacing={2}><Typography variant="h6">{layout ? `Transmissões do ${layout}` : 'Lotes simulados e oficiais'}</Typography><Typography color="text.secondary">Recepção gera protocolo. A consulta devolve o evtRetornoTabela com recibo.</Typography>{!linhas.length && <Alert severity="info">{layout ? `Nenhuma transmissão de ${layout} para esta entidade.` : 'Nenhuma transmissão para esta entidade. Valide um D-1001 ou D-1011 com “Simular envio” marcado.'}</Alert>}<Box className="tabela-criticas-wrap"><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{colunas.map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{linhas.map(linha => <Box component="tr" key={linha.batchId || linha.eventId}>{!layout && <Box component="td">{linha.layout}</Box>}<Box component="td">{linha.sourceName || '—'}</Box><Box component="td">{linha.eventIdentifier}</Box><Box component="td">{linha.protocol || '—'}</Box><Box component="td">{linha.receiptNumber || '—'}</Box><Box component="td">{rotuloStatusEnvio(linha.status)}</Box><Box component="td"><Stack direction="row" spacing={1}>{linha.batchId && <Button size="small" onClick={() => abrir(linha)}>Artefatos</Button>}{linha.batchId && !linha.receiptNumber && <Button size="small" variant="contained" onClick={() => consultar(linha)}>Consultar</Button>}</Stack></Box></Box>)}</Box></Box></Box>{detalhe && <EnvioSimuladoCard envio={{ enviado: true, transmissao: detalhe }} aoConsultar={() => consultar(detalhe)} />}</Stack></CardContent></Card>;
}

function Dashboard({ onImport }) { return <><Grid container spacing={2} mb={3}><Grid item xs={12} md={4}><Metric title="Layouts ativos" value="2" detail="D-1001 e D-1011" /></Grid><Grid item xs={12} md={4}><Metric title="Importações" value="Pronto" detail="CSV e XML" /></Grid><Grid item xs={12} md={4}><Metric title="Transmissões" value="Mock local" detail="Integração controlada" /></Grid></Grid><Card><CardContent><Stack spacing={2}><Typography variant="h6">Comece uma operação</Typography><Typography color="text.secondary">Importe um arquivo, valide o modelo canônico e acompanhe as críticas por campo.</Typography><Button variant="contained" onClick={onImport}>Nova importação</Button></Stack></CardContent></Card></>; }
function Metric({ title, value, detail }) { return <Card><CardContent><Typography color="text.secondary">{title}</Typography><Typography variant="h4" sx={{ mt: 1 }}>{value}</Typography><Typography variant="body2" color="text.secondary">{detail}</Typography></CardContent></Card>; }
function ResumoResultado({ resultado, aoBaixar, aoRelatorio, rotuloDownload = 'Baixar CSV', criticas = [] }) {
  const invalido = resultado.valido === false || resultado.quantidadeInvalidos > 0 || criticas.length > 0;
  const linhasComProblema = resultado.linhasComProblema ?? new Set(criticas.map(item => item.linha).filter(Boolean)).size;
  return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Typography variant="subtitle1">Relatório da validação</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color={invalido ? 'error' : 'success'} label={invalido ? 'Com críticas' : 'Válido'} /><Chip label={`${resultado.totalLinhas ?? resultado.quantidadeArquivos ?? 1} linha(s)`} /><Chip label={`${linhasComProblema} linha(s) com problema`} /><Chip label={`${criticas.length} crítica(s)`} /></Stack><Stack direction="row" spacing={2}>{aoRelatorio && <Button variant="contained" onClick={aoRelatorio}>Baixar relatório Excel</Button>}{aoBaixar && <Button variant="outlined" onClick={aoBaixar}>{rotuloDownload}</Button>}</Stack>{resultado.xml && <TextField className="resizable-preview" multiline rows={5} label="XML gerado" value={resultado.xml} InputProps={{ readOnly: true }} />}{criticas.length > 0 && <Box className="tabela-criticas-wrap"><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{['Linha', 'Coluna', 'Tipo', 'Encontrado', 'Esperado', 'Como corrigir'].map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{criticas.map((item, indice) => <Box component="tr" key={indice}><Box component="td">{item.linha || '—'}</Box><Box component="td">{item.coluna || 'Documento'}</Box><Box component="td"><Chip size="small" color={COR_TIPO[item.tipo] || 'default'} label={item.rotuloTipo || ROTULO_TIPO[item.tipo] || item.tipo} /></Box><Box component="td">{item.valorEncontrado || '—'}</Box><Box component="td">{item.valorEsperado || '—'}</Box><Box component="td">{item.problema}</Box></Box>)}</Box></Box></Box>}</Stack></Paper>;
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
function EntityAdmin({ api, onChanged, setMessage }) {
  const [users, setUsers] = useState([]);
  const [entities, setEntities] = useState([]);
  const [form, setForm] = useState(null);
  const load = async () => { const [userResponse, entityResponse] = await Promise.all([api.get('/api/admin/users'), api.get('/api/admin/entities')]); setUsers(userResponse.data); setEntities(entityResponse.data); };
  useEffect(() => { load().catch(() => setMessage('Falha ao carregar entidades')); }, []);
  function openNew() { setForm({ id: null, cnpjRoot: '', legalName: '', userIds: [] }); }
  function openEdit(entity) { setForm({ id: entity.id, cnpjRoot: entity.cnpjRoot, legalName: entity.legalName, userIds: entity.userIds || [] }); }
  async function save() { const body = { cnpjRoot: form.cnpjRoot, legalName: form.legalName, userIds: form.userIds }; if (form.id) await api.put(`/api/admin/entities/${form.id}`, body); else await api.post('/api/admin/entities', body); setForm(null); await load(); if (onChanged) await onChanged(); setMessage('Entidade salva'); }
  async function remove(entity) { await api.delete(`/api/admin/entities/${entity.id}`); await load(); if (onChanged) await onChanged(); setMessage('Entidade excluída'); }
  if (!form) return <Card><CardContent><Stack spacing={2}><Stack direction="row" justifyContent="space-between"><Typography variant="h6">Entidades</Typography><Button variant="contained" onClick={openNew}>Nova entidade</Button></Stack>{entities.map(entity => <Box key={entity.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #e5e7eb', py: 1 }}><Box><Typography>{entity.legalName}</Typography><Typography variant="body2" color="text.secondary">{entity.cnpjRoot} · {(entity.userIds || []).length} usuário(s)</Typography></Box><Stack direction="row" spacing={1}><Button onClick={() => openEdit(entity)}>Editar</Button><Button color="error" onClick={() => remove(entity)}>Excluir</Button></Stack></Box>)}</Stack></CardContent></Card>;
  return <Card><CardContent><Stack spacing={3}><Typography variant="h6">{form.id ? 'Editar entidade' : 'Nova entidade'}</Typography><Stack direction="row" spacing={2}><TextField label="CNPJ raiz" value={form.cnpjRoot} onChange={event => setForm({ ...form, cnpjRoot: event.target.value })} /><TextField label="Razão social" value={form.legalName} onChange={event => setForm({ ...form, legalName: event.target.value })} fullWidth /></Stack><Typography color="text.secondary">Usuários vinculados à entidade</Typography><UserPickList users={users} selectedIds={form.userIds} onChange={userIds => setForm({ ...form, userIds })} /><Stack direction="row" spacing={2}><Button variant="contained" onClick={save}>Salvar</Button><Button onClick={() => setForm(null)}>Cancelar</Button></Stack></Stack></CardContent></Card>;
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

createRoot(document.getElementById('root')!).render(<React.StrictMode><App /></React.StrictMode>);
