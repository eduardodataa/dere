// @ts-nocheck
import React, { useEffect, useRef, useState } from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import { Alert, Box, Button, Card, CardContent, Checkbox, Chip, Collapse, Divider, Drawer, FormControlLabel, Grid, List, ListItemButton, ListItemText, MenuItem, Paper, Stack, TextField, Tooltip, Typography } from '@mui/material';
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

function PainelSegurancaCertificado() {
  const [aberto, setAberto] = useState(false);
  return <Paper variant="outlined" sx={{ p: 2, background: '#f8fafc' }}><Stack spacing={1.5}><Stack direction="row" justifyContent="space-between" alignItems="center" gap={2}><Box><Typography variant="subtitle1">Por que é seguro anexar seu certificado aqui?</Typography><Typography color="text.secondary">A chave privada não vai para a Receita. O que viaja é o XML já assinado. O P12 fica cifrado no servidor. A senha do P12 não fica na tabela do certificado: localmente ela vai para um cofre em disco; em produção o mesmo contrato aponta para o AWS Secrets Manager (KMS). Só é lida na memória na hora de assinar.</Typography></Box><Button variant="outlined" onClick={() => setAberto(atual => !atual)}>{aberto ? 'Recolher' : 'Ver fluxo técnico'}</Button></Stack><Collapse in={aberto}><Stack spacing={1.5} sx={{ pt: 1 }}><Typography variant="subtitle2">1. Digitação da senha</Typography><Typography color="text.secondary">A senha do P12/PFX é digitada neste formulário e enviada uma vez, no mesmo pedido do arquivo, para a API. Em produção isso deve ocorrer em HTTPS. A tela não guarda a senha: o campo é limpo após o armazenamento e ela nunca é mostrada de volta.</Typography><Typography variant="subtitle2">2. Abertura do P12 na memória</Typography><Typography color="text.secondary">O servidor usa a senha só para abrir o PKCS#12 em memória e conferir se existe certificado X.509 com chave privada. Se o arquivo for inválido ou a senha estiver errada, nada é gravado.</Typography><Typography variant="subtitle2">3. Onde a senha e o P12 ficam</Typography><Typography color="text.secondary">O P12 é cifrado e permanece no armazenamento da aplicação. A senha não fica na tabela do certificado: no ambiente local ela vai para um cofre em disco (data/cert-secrets), cifrado com AES-256-GCM. Em produção o mesmo contrato aponta para o AWS Secrets Manager, cifrado pelo KMS, com acesso só pela role da API (IAM) e trilha no CloudTrail. A tela nunca reexibe a senha. Certificados antigos com senha ainda no banco são migrados para o cofre na primeira leitura.</Typography><Typography variant="subtitle2">4. Quem pode usar</Typography><Typography color="text.secondary">O certificado fica vinculado à entidade. A API exige o usuário autenticado e o acesso àquela entidade. Excluir o certificado o desativa: deixa de ser carregado para assinar.</Typography><Typography variant="subtitle2">5. Assinatura digital (XMLDSig)</Typography><Typography color="text.secondary">No envio, o servidor decifra o P12 em memória, recarrega o KeyStore e assina o XML do evento com XMLDSig (RSA-SHA256, transformação enveloped). O bloco KeyInfo leva só o certificado público, para a Receita validar a assinatura. A chave privada não entra no XML.</Typography><Typography variant="subtitle2">6. Envio à Receita</Typography><Typography color="text.secondary">O lote enviado contém o XML assinado (e, no fluxo local, a recepção/consulta simulada). A Receita recebe o documento e o certificado público da assinatura. A chave privada permanece no servidor, em memória só durante aquela requisição, e não é retransmitida.</Typography><Typography variant="body2">Isto é certificado A1 em software, não um HSM nem um A3 em token. Secrets Manager + KMS separam a senha do dump do banco e auditam quem leu o segredo. Se a role da API for comprometida, a aplicação ainda consegue ler a senha — o ganho não é o mesmo de um HSM. Não compartilhe o P12 nem a senha por e-mail ou chat.</Typography></Stack></Collapse></Stack></Paper>;
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
  useEffect(() => { if (message) window.scrollTo({ top: 0, behavior: 'smooth' }); }, [message]);

  async function signIn() {
    try { const response = await api.post('/api/auth/login', { login, password }); setUid(response.data.userId); setMaster(!!response.data.master); api.defaults.headers.common['X-User-Id'] = response.data.userId; setMessage(''); await load(); }
    catch { setMessage('Login inválido'); }
  }
  const entidadeTravada = !master && entities.length <= 1;
  async function recarregarCertificados(entidadeId = eid) {
    const response = await api.get(`/api/certificates/entity/${entidadeId}`, { headers: { 'X-User-Id': uid, 'X-Entity-Id': entidadeId } });
    setCertificates(Array.isArray(response.data) ? response.data : []);
  }
  async function uploadCertificate() { if (!certFile || !certPassword || !certLabel) { setMessage('Informe nome, arquivo P12 e senha.'); return; } const form = new FormData(); form.append('label', certLabel); form.append('file', certFile); form.append('password', certPassword); try { await api.post(`/api/certificates/entity/${eid}`, form, { headers: { 'Content-Type': 'multipart/form-data', 'X-User-Id': uid, 'X-Entity-Id': eid } }); setCertPassword(''); setCertFile(undefined); setCertLabel(''); await recarregarCertificados(); setMessage('Certificado armazenado com sucesso.'); } catch (error) { setMessage(error.response?.data?.message || 'Falha ao armazenar certificado.'); } }
  async function deleteCertificate(cert) {
    if (!window.confirm(`Excluir o certificado ${cert.label}?`)) return;
    try {
      await api.delete(`/api/certificates/${cert.id}`, { headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
      await recarregarCertificados();
      setMessage('Certificado excluído.');
    } catch (error) { setMessage(error.response?.data?.message || 'Falha ao excluir certificado.'); }
  }

  if (!uid) return <Box className="login-shell"><Card className="login-card"><CardContent><Typography variant="h4" mb={1}>DeRE</Typography><Typography color="text.secondary" mb={3}>Declarações de Regimes Específicos</Typography><Stack spacing={2}><TextField label="Login" value={login} onChange={event => setLogin(event.target.value)} /><TextField label="Senha" type="password" value={password} onChange={event => setPassword(event.target.value)} /><Button variant="contained" size="large" onClick={signIn}>Entrar</Button>{message && <Alert severity="error">{message}</Alert>}</Stack></CardContent></Card></Box>;

  const menu = ['Dashboard', 'D-1001', 'D-1011', 'D-1101', 'D-1199', 'Importações', 'Validações', 'Certificado Digital', 'Transmissões', 'Configurações', 'Entidades', ...(master ? ['Usuários'] : [])];
  const workspacePages = ['D-1001', 'D-1011', 'D-1101', 'D-1199'];
  const currentEntity = entities.find(entity => entity.id === eid);
  const workspaceProps = { api, uid, eid, entities, setEid, setMessage, master };
  const certificateCard = <Card><CardContent><Stack spacing={2}><PainelSegurancaCertificado /><Typography variant="h6">Certificado A1</Typography><Typography color="text.secondary">A senha não volta a ser exibida e não fica na tabela do certificado. Localmente ela vai para o cofre em disco; em produção, para o AWS Secrets Manager. O P12 fica cifrado à parte, só para assinar o XML.</Typography><TextField select fullWidth label="Entidade" value={eid} disabled={entidadeTravada} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField><TextField label="Nome" value={certLabel} onChange={event => setCertLabel(event.target.value)} /><Button component="label" variant="outlined">Selecionar P12/PFX<input hidden type="file" accept=".p12,.pfx" onChange={event => setCertFile(event.target.files?.[0])} /></Button>{certFile ? <Typography color="text.secondary">Arquivo: {certFile.name}</Typography> : null}<TextField label="Senha do certificado" type="password" value={certPassword} onChange={event => setCertPassword(event.target.value)} /><Button variant="contained" onClick={uploadCertificate}>Armazenar certificado</Button><Divider />{certificates.map(cert => <Box key={cert.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: 2, borderBottom: '1px solid #e5e7eb', py: 1.5 }}><Box><Typography>{cert.label} · {cert.alias}</Typography><Typography variant="body2" color="text.secondary">Titular: {cert.subject || '—'}</Typography><Typography variant="body2" color="text.secondary">Emissor: {cert.issuer || '—'}</Typography><Typography variant="body2" color="text.secondary">Emissão: {formatarDataEnvio(cert.issuedAt)} · Validade: {formatarDataEnvio(cert.validUntil)}</Typography></Box><Stack direction="row" spacing={1} alignItems="center"><Chip color={cert.icpBrasil ? 'success' : 'default'} label={cert.icpBrasil ? 'ICP-Brasil' : 'Fora da ICP-Brasil'} /><Chip color={cert.status === 'VALID' ? 'success' : 'warning'} label={cert.status === 'EXPIRING_SOON' ? `vence em ${cert.daysUntilExpiry} dias` : cert.status} /><Button size="small" color="error" onClick={() => deleteCertificate(cert)}>Excluir</Button></Stack></Box>)}</Stack></CardContent></Card>;
  return <Box className="app-shell"><Drawer variant="permanent" className="sidebar"><Box className="brand"><img className="marca" src={logotipoDataA} alt="Data A" /><Typography variant="h5">DeRE</Typography><Typography variant="caption">EFPC / PREVIC</Typography></Box><List>{menu.map(item => <ListItemButton selected={page === item} onClick={() => { setPage(item); setMessage(''); }} key={item}><ListItemText primary={item} /></ListItemButton>)}</List></Drawer><Box component="main" className="content"><Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}><Box><Typography variant="h4">{page}</Typography><Typography color="text.secondary">Operação local da declaração</Typography></Box><Chip label={currentEntity?.legalName || 'Entidade não selecionada'} /></Stack>{message && <Alert sx={{ mb: 3 }} severity="info">{message}</Alert>}{page === 'Dashboard' ? <Dashboard onImport={() => setPage('D-1001')} /> : null}<Box hidden={page !== 'D-1001'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1001" /></Box><Box hidden={page !== 'D-1011'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1011" /></Box><Box hidden={page !== 'D-1101'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1101" /></Box><Box hidden={page !== 'D-1199'}><ImportWorkspace {...workspaceProps} lockedLayout="D-1199" /></Box>{page === 'Importações' ? <Alert severity="info">A conversão CSV/XML fica em D-1001 e D-1011. Este menu será o histórico de lotes (arquivo, layout, entidade, status e críticas).</Alert> : null}{page === 'Validações' ? <Alert severity="info">Validações será o relatório das críticas já apuradas nos lotes. A validação de um arquivo novo é feita no menu do layout.</Alert> : null}{page === 'Transmissões' ? <TransmissionsPanel api={api} uid={uid} eid={eid} setMessage={setMessage} /> : null}{page === 'Certificado Digital' ? certificateCard : page === 'Entidades' ? <EntityAdmin api={api} onChanged={load} setMessage={setMessage} master={master} /> : page === 'Usuários' && master ? <UserAdmin api={api} currentUserId={uid} onSelfMaster={setMaster} setMessage={setMessage} /> : !['Dashboard', 'Certificado Digital', 'Importações', 'Validações', 'Transmissões', 'Entidades', ...workspacePages].includes(page) && !(page === 'Usuários' && master) ? <Alert severity="info">O módulo {page} está preparado para a próxima integração.</Alert> : null}</Box></Box>;
}

function ImportWorkspace({ api, uid, eid, entities, setEid, setMessage, lockedLayout, master }) {
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
      : lockedLayout === 'D-1101'
        ? 'Envie CSV ou XML do D-1101 (Balancete Mensal) para análise.'
        : lockedLayout === 'D-1199'
          ? 'Envie CSV ou XML do D-1199 (Fechamento Mensal). Somente inclusão.'
          : 'Selecione o layout e envie CSV ou XML para análise.';

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

  async function uploadXml() {
    if (!file) { setMessage('Selecione um XML ou ZIP'); return; }
    try {
      const xmlEndpoint = endpointXml(currentLayout);
      if (xmlEndpoint) {
        const xml = await file.text();
        const response = await api.post(xmlEndpoint, xml, { headers: { 'Content-Type': 'application/xml', 'X-Entity-Id': eid } });
        setCsvResult(response.data);
        setUploadResult(undefined);
        setMessage(response.data.valido ? `XML ${currentLayout} validado.` : `XML ${currentLayout} possui críticas.`);
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
      const endpoint = endpointCsv(currentLayout);
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
  return <Stack spacing={2}><Card><CardContent><Stack spacing={2}><Typography variant="h6">Importação e validação</Typography><Typography color="text.secondary">{subtitle}</Typography>{!lockedLayout && <TextField select label="Leiaute" value={layout} onChange={event => { setLayout(event.target.value); setCsvResult(undefined); setUploadResult(undefined); }}><MenuItem value="D-1001">D-1001 — Informações do contribuinte</MenuItem><MenuItem value="D-1011">D-1011 — PGCC</MenuItem><MenuItem value="D-1101">D-1101 — Balancete mensal</MenuItem><MenuItem value="D-1199">D-1199 — Fechamento mensal</MenuItem></TextField>}<TextField select fullWidth label="Entidade" value={eid} disabled={!master && entities.length <= 1} onChange={event => setEid(+event.target.value)}>{entities.map(entity => <MenuItem key={entity.id} value={entity.id}>{entity.legalName} — {entity.cnpjRoot}</MenuItem>)}</TextField><Stack direction="row" spacing={2} alignItems="center" flexWrap="wrap" useFlexGap><Button variant="outlined" onClick={() => baixarModelo().catch(() => setMessage('Falha ao baixar o modelo CSV'))}>Baixar modelo CSV</Button><Button component="label" variant="outlined">Selecionar CSV<input hidden type="file" accept=".csv" onChange={loadCsvFile} /></Button><Button component="label" variant="outlined">Selecionar XML/ZIP<input hidden type="file" accept=".xml,.zip" onChange={loadXmlFile} /></Button><TextField select label="Codificação" value={codificacao} onChange={event => alterarCodificacao(event.target.value)} sx={{ minWidth: 180 }}><MenuItem value="UTF-8">UTF-8</MenuItem><MenuItem value="ANSI">ANSI</MenuItem></TextField><Button variant="outlined" color="inherit" onClick={limparDados} disabled={!nomeArquivo && !csvResult && !uploadResult}>Limpar dados</Button></Stack>{nomeArquivo ? <Typography color="text.secondary">Arquivo: {nomeArquivo}{arquivoCsv ? ` · ${codificacao}` : ''}</Typography> : <Typography color="text.secondary">Nenhum arquivo selecionado.</Typography>}<FormControlLabel control={<Checkbox checked={simularEnvio} onChange={event => setSimularEnvio(event.target.checked)} />} label="Simular envio à Receita após validar" /><Stack direction="row" spacing={2}><Button variant="contained" onClick={validateCsv}>Validar CSV</Button><Button variant="contained" color="secondary" onClick={uploadXml}>Processar XML/ZIP</Button><Button variant="outlined" disabled={!simularEnvio || !(csvResult?.valido || (uploadResult && !uploadResult.quantidadeInvalidos))} onClick={async () => { try { const origem = arquivoCsv ? 'CSV' : 'XML'; const conteudo = arquivoCsv ? csvText : file ? await file.text() : csvResult?.xml; await enviarSimulado(origem, conteudo, nomeArquivo); } catch (error) { setMessage(error.response?.data?.message || error.response?.data?.error || error.response?.data?.detail || 'Falha no envio simulado'); } }}>Simular envio</Button></Stack>{envio && <EnvioSimuladoCard envio={envio} api={api} uid={uid} eid={eid} />}{csvResult && <ResumoResultado resultado={csvResult} rotuloDownload="Baixar XML" aoBaixar={csvResult.xml ? () => baixar(`${currentLayout.toLowerCase()}.xml`, csvResult.xml) : undefined} aoRelatorio={csvResult.relatorioXlsx ? () => baixarXlsx(nomeXlsx(arquivoCsv?.name || file?.name || 'arquivo.csv', currentLayout), csvResult.relatorioXlsx) : undefined} criticas={criticasCsv} />}{uploadResult && <ResumoResultado resultado={uploadResult} rotuloDownload="Baixar CSV" aoBaixar={uploadResult.csvConvertido ? () => baixar(nomeCsvDaOrigem(file?.name || uploadResult.nomeOrigem), uploadResult.csvConvertido) : undefined} aoRelatorio={uploadResult.relatorioXlsx ? () => baixarXlsx(nomeXlsx(file?.name || uploadResult.nomeOrigem, currentLayout), uploadResult.relatorioXlsx) : undefined} criticas={criticasXml} />}</Stack></CardContent></Card><TransmissionsPanel api={api} uid={uid} eid={eid} setMessage={setMessage} layout={currentLayout} reloadToken={listaEnvio} /></Stack>;
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

function EnvioSimuladoCard({ envio, api, uid, eid }) {
  const transmissao = envio?.transmissao;
  if (!envio) return null;
  async function baixarPdf() {
    if (!transmissao?.batchId) return;
    const response = await api.get(`/api/transmissions/${transmissao.batchId}/protocolo.pdf`, { responseType: 'blob', headers: { 'X-User-Id': uid, 'X-Entity-Id': eid } });
    baixarBlob(`protocolo-${transmissao.protocol || transmissao.batchId}.pdf`, response.data);
  }
  if (!envio.enviado) {
    return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Alert severity="warning">{motivoRecusa(envio) || 'Envio simulado recusado.'}</Alert>{transmissao?.protocol ? <Stack spacing={1}><Typography color="text.secondary">Lote já existente para este id. O recibo é consultado automaticamente a cada 1 minuto.</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao.protocol}`} /><Chip color={transmissao.receiptNumber ? 'success' : 'warning'} label={transmissao.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao.status)} /></Stack>{transmissao.batchId && <Button onClick={baixarPdf}>PDF do protocolo</Button>}</Stack> : null}</Stack></Paper>;
  }
  return <Paper variant="outlined" sx={{ p: 2 }}><Stack spacing={1}><Typography variant="subtitle1">Envio simulado</Typography><Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap><Chip color="info" label={`Protocolo ${transmissao?.protocol || '—'}`} /><Chip color={transmissao?.receiptNumber ? 'success' : 'warning'} label={transmissao?.receiptNumber ? `Recibo ${transmissao.receiptNumber}` : 'Sem recibo ainda'} /><Chip label={rotuloStatusEnvio(transmissao?.status)} /></Stack><Typography color="text.secondary">Protocolo é a recepção do lote. O recibo é consultado automaticamente a cada 1 minuto.</Typography><Stack direction="row" spacing={2}>{transmissao?.eventXml && <Button onClick={() => baixar(`${transmissao.layout || 'evento'}.xml`, transmissao.eventXml)}>XML do evento</Button>}{transmissao?.batchXml && <Button onClick={() => baixar('lote.xml', transmissao.batchXml)}>XML do lote</Button>}{transmissao?.returnXml && <Button onClick={() => baixar('retorno.xml', transmissao.returnXml)}>XML de retorno</Button>}{transmissao?.csv && <Button onClick={() => baixar(`${transmissao.layout || 'evento'}.csv`, transmissao.csv)}>CSV</Button>}{transmissao?.batchId && <Button variant="contained" onClick={baixarPdf}>PDF do protocolo</Button>}</Stack></Stack></Paper>;
}

function TransmissionsPanel({ api, uid, eid, setMessage, layout, reloadToken }) {
  const [linhas, setLinhas] = useState([]);
  const [detalhe, setDetalhe] = useState();
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
    if (detalhe && detalhe.batchId === linha.batchId) { setDetalhe(undefined); return; }
    const response = await api.get(`/api/transmissions/${linha.batchId}`, { headers: cabecalhos });
    setDetalhe(response.data);
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
    <Box className="tabela-criticas-wrap" sx={{ maxHeight: 'none' }}><Box component="table" className="tabela-criticas"><Box component="thead"><Box component="tr">{colunas.map(titulo => <Box component="th" key={titulo}>{titulo}</Box>)}</Box></Box><Box component="tbody">{filtradas.map(linha => <React.Fragment key={linha.batchId || linha.eventId}><Box component="tr">{!layout && <Box component="td">{linha.layout}</Box>}<Box component="td">{linha.sourceName || '—'}</Box><Box component="td">{rotuloOperacaoEnvio(linha.operationType)}</Box><Box component="td">{linha.eventIdentifier}</Box><Box component="td">{linha.protocol || '—'}</Box><Box component="td">{linha.receiptNumber || '—'}</Box><Box component="td">{rotuloStatusEnvio(linha.status)}</Box><Box component="td">{formatarDataEnvio(linha.sentAt || linha.createdAt)}</Box><Box component="td"><Stack direction="row" spacing={1}>{linha.batchId && <Tooltip title="Abre o que já está gravado: protocolo, XML do evento, lote, retorno e CSV. Não consulta a Receita." placement="top"><span><Button size="small" variant={detalhe && detalhe.batchId === linha.batchId ? 'contained' : 'text'} onClick={() => abrir(linha)}>Artefatos</Button></span></Tooltip>}{linha.batchId && <Button size="small" onClick={() => baixarPdf(linha).catch(() => setMessage('Falha ao gerar o PDF do protocolo'))}>PDF</Button>}</Stack></Box></Box>{detalhe && detalhe.batchId === linha.batchId && <Box component="tr"><Box component="td" colSpan={colunas.length} sx={{ p: 1.5, background: '#f8fafc' }}><EnvioSimuladoCard envio={{ enviado: true, transmissao: detalhe }} api={api} uid={uid} eid={eid} /></Box></Box>}</React.Fragment>)}</Box></Box></Box>
  </Stack></CardContent></Card>;
}

function Dashboard({ onImport }) { return <><Grid container spacing={2} mb={3}><Grid item xs={12} md={4}><Metric title="Layouts ativos" value="4" detail="D-1001, D-1011, D-1101 e D-1199" /></Grid><Grid item xs={12} md={4}><Metric title="Importações" value="Pronto" detail="CSV e XML" /></Grid><Grid item xs={12} md={4}><Metric title="Transmissões" value="Mock local" detail="Integração controlada" /></Grid></Grid><Card><CardContent><Stack spacing={2}><Typography variant="h6">Comece uma operação</Typography><Typography color="text.secondary">Importe um arquivo, valide o modelo canônico e acompanhe as críticas por campo.</Typography><Button variant="contained" onClick={onImport}>Nova importação</Button></Stack></CardContent></Card></>; }
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
  async function save() { const body = { cnpjRoot: form.cnpjRoot, legalName: form.legalName, userIds: form.userIds }; if (form.id) await api.put(`/api/admin/entities/${form.id}`, body); else await api.post('/api/admin/entities', body); setForm(null); await load(); if (onChanged) await onChanged(); setMessage('Entidade salva'); }
  async function remove(entity) { await api.delete(`/api/admin/entities/${entity.id}`); await load(); if (onChanged) await onChanged(); setMessage('Entidade excluída'); }
  if (!form) return <Card><CardContent><Stack spacing={2}><Stack direction="row" justifyContent="space-between"><Typography variant="h6">Entidades</Typography>{master && <Button variant="contained" onClick={openNew}>Nova entidade</Button>}</Stack>{entities.map(entity => <Box key={entity.id} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #e5e7eb', py: 1 }}><Box><Typography>{entity.legalName}</Typography><Typography variant="body2" color="text.secondary">{entity.cnpjRoot} · {(entity.userIds || []).length} usuário(s)</Typography></Box><Stack direction="row" spacing={1}><Button onClick={() => openEdit(entity)}>{master ? 'Editar' : 'Ver'}</Button>{master && <Button color="error" onClick={() => remove(entity)}>Excluir</Button>}</Stack></Box>)}</Stack></CardContent></Card>;
  return <Card><CardContent><Stack spacing={3}><Typography variant="h6">{locked ? 'Entidade' : form.id ? 'Editar entidade' : 'Nova entidade'}</Typography><Stack direction="row" spacing={2}><TextField label="CNPJ raiz" value={form.cnpjRoot} disabled={locked} onChange={event => setForm({ ...form, cnpjRoot: event.target.value })} /><TextField label="Razão social" value={form.legalName} disabled={locked} onChange={event => setForm({ ...form, legalName: event.target.value })} fullWidth /></Stack>{master && <Typography color="text.secondary">Usuários vinculados à entidade</Typography>}{master && <UserPickList users={users} selectedIds={form.userIds} onChange={userIds => setForm({ ...form, userIds })} />}<Stack direction="row" spacing={2}>{master && <Button variant="contained" onClick={save}>Salvar</Button>}{!(locked && entities.length === 1) && <Button onClick={() => setForm(null)}>{master ? 'Cancelar' : 'Voltar'}</Button>}</Stack></Stack></CardContent></Card>;
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
