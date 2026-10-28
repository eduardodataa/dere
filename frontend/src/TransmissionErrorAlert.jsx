import React, { useState } from 'react';
import { Alert, AlertTitle, Button, Collapse, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, Stack, Tooltip, Typography } from '@mui/material';
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined';
import { aguardaReenvioAutomatico, apresentarErroTransmissao, tokensAlertaTransmissao } from './erroTransmissao';

export function TransmissionErrorAlert({ erro, aoRepetir, repetindo, complemento }) {
  const [aberto, setAberto] = useState(false);
  const [copiado, setCopiado] = useState(false);
  if (!erro) return null;
  const view = apresentarErroTransmissao(erro);
  async function copiar() {
    try {
      await navigator.clipboard.writeText(view.diagnostico);
      setCopiado(true);
    } catch {
      setCopiado(false);
    }
  }
  return <Alert severity={tokensAlertaTransmissao.severidade} sx={{ '& .MuiAlert-message': { width: '100%' } }}>
    <AlertTitle>{view.titulo}</AlertTitle>
    <Typography color={tokensAlertaTransmissao.texto}>{view.mensagem}</Typography>
    {view.codigo ? <Typography variant="body2" sx={{ mt: 1 }}>Código: {view.codigo}</Typography> : null}
    <Typography variant="body2" color={tokensAlertaTransmissao.apoio} sx={{ mt: 1 }}>{view.orientacao}</Typography>
    {complemento ? <Typography variant="body2" color={tokensAlertaTransmissao.apoio} sx={{ mt: 1 }}>{complemento}</Typography> : null}
    <Stack direction="row" spacing={1} useFlexGap flexWrap="wrap" sx={{ mt: 1.5 }}>
      <Button color="inherit" size="small" onClick={() => setAberto(valor => !valor)}>{aberto ? 'Ocultar detalhes técnicos' : 'Detalhes técnicos'}</Button>
      <Button color="inherit" size="small" onClick={copiar}>{copiado ? 'Diagnóstico copiado' : 'Copiar diagnóstico'}</Button>
      {aguardaReenvioAutomatico(erro) ? <BotaoInfoReenvio /> : null}
      {view.repetir && aoRepetir ? <Button color="inherit" size="small" disabled={repetindo} onClick={aoRepetir}>Tentar novamente</Button> : null}
    </Stack>
    <Collapse in={aberto}>
      <Stack spacing={0.5} sx={{ mt: 1.5, borderTop: 1, borderColor: tokensAlertaTransmissao.divisor, pt: 1.5 }}>
        {view.detalhes.map(item => <Typography key={`${item.rotulo}-${item.valor}`} variant="body2" color={tokensAlertaTransmissao.apoio}>{item.rotulo}: {item.valor}</Typography>)}
      </Stack>
    </Collapse>
  </Alert>;
}

export function BotaoInfoReenvio({ compacto }) {
  const [aberto, setAberto] = useState(false);
  const rotulo = 'Sobre o reenvio automático';
  return <>
    {compacto
      ? <Tooltip title={rotulo}><IconButton size="small" aria-label={rotulo} onClick={() => setAberto(true)}><InfoOutlinedIcon fontSize="small" /></IconButton></Tooltip>
      : <Button color="inherit" size="small" startIcon={<InfoOutlinedIcon />} onClick={() => setAberto(true)}>Info</Button>}
    <Dialog open={aberto} onClose={() => setAberto(false)} fullWidth maxWidth="sm">
      <DialogTitle>Reenvio quando a Receita voltar</DialogTitle>
      <DialogContent>
        <Stack spacing={1.5} sx={{ pt: 0.5 }}>
          <Typography>Este lote não chegou a ser transmitido. O monitoramento da aplicação consulta a Receita e, quando ela voltar a responder, envia o mesmo arquivo já assinado e o mesmo identificador.</Typography>
          <Typography>Enquanto a Receita estiver fora, esse envio não é repetido. Não é preciso criar outro id. Se a Receita indicar que esse identificador já existe, o monitoramento para e o lote fica para conciliação.</Typography>
        </Stack>
      </DialogContent>
      <DialogActions><Button onClick={() => setAberto(false)}>Fechar</Button></DialogActions>
    </Dialog>
  </>;
}
