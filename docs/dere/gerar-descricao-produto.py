from pathlib import Path
from docx import Document
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.shared import Cm, Pt, RGBColor

NAVY = RGBColor(0x11, 0x18, 0x27)
RED = RGBColor(0xB0, 0x1C, 0x2E)
GRAY = RGBColor(0x4B, 0x55, 0x63)
WHITE = RGBColor(0xFF, 0xFF, 0xFF)


def set_run(run, size=11, bold=False, color=NAVY, italic=False):
    run.bold = bold
    run.italic = italic
    run.font.size = Pt(size)
    run.font.color.rgb = color
    run.font.name = "Calibri"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Calibri")


def add_p(doc, text, size=11, bold=False, color=NAVY, space_after=8, align=None, italic=False):
    p = doc.add_paragraph()
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.space_before = Pt(0)
    if align:
        p.alignment = align
    run = p.add_run(text)
    set_run(run, size=size, bold=bold, color=color, italic=italic)
    return p


def shade(cell, hex_color):
    tc = cell._tc
    tcPr = tc.get_or_add_tcPr()
    shd = tcPr.makeelement(qn("w:shd"), {
        qn("w:val"): "clear",
        qn("w:color"): "auto",
        qn("w:fill"): hex_color,
    })
    tcPr.append(shd)


def set_cell(cell, text, bold=False, color=NAVY, fill=None, size=10):
    if fill:
        shade(cell, fill)
    cell.text = ""
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(3)
    p.paragraph_format.space_after = Pt(3)
    run = p.add_run(text)
    set_run(run, size=size, bold=bold, color=color)


def add_table(doc, headers, rows):
    table = doc.add_table(rows=1 + len(rows), cols=len(headers))
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    table.autofit = True
    for i, h in enumerate(headers):
        set_cell(table.rows[0].cells[i], h, bold=True, color=WHITE, fill="111827", size=10)
    for r, row in enumerate(rows):
        fill = "F5F7FB" if r % 2 == 0 else "FFFFFF"
        for c, value in enumerate(row):
            set_cell(table.rows[r + 1].cells[c], value, fill=fill, size=10)
    doc.add_paragraph().paragraph_format.space_after = Pt(10)
    return table


doc = Document()
for section in doc.sections:
    section.top_margin = Cm(2)
    section.bottom_margin = Cm(2)
    section.left_margin = Cm(2.2)
    section.right_margin = Cm(2.2)

add_p(doc, "Data A", size=12, bold=True, color=RED, space_after=4)
add_p(doc, "DeRE", size=28, bold=True, space_after=2)
add_p(doc, "Declaração de Regimes Específicos", size=16, color=GRAY, space_after=12)
add_p(doc, "Descrição do produto para clientes EFPC / PREVIC", size=13, bold=True, space_after=18)

add_p(doc, "1. O que o produto faz", size=16, bold=True, color=RED, space_after=8)
add_p(
    doc,
    "A DeRE (Declaração de Regimes Específicos) é a obrigação acessória nacional da Reforma Tributária para contribuintes de regimes específicos — entre eles os serviços financeiros e a previdência complementar. Ela é entregue à Receita Federal / CGIBS pela raiz do CNPJ (8 dígitos), de forma eletrônica, com assinatura digital ICP-Brasil.",
)
add_p(
    doc,
    "O produto DeRE da Data A é o sistema que a entidade fechada de previdência complementar (EFPC) usa para cumprir essa obrigação sem montar XML manualmente e sem operar diretamente a API da Receita. A entidade envia a planilha (CSV) ou o XML no leiaute oficial; o sistema valida, assina, transmite o lote e acompanha protocolo e recibo.",
)
add_p(
    doc,
    "Em resumo: o produto transforma o dado contábil/cadastral da EFPC em evento DeRE aceito pelo governo, com trilha de críticas, protocolo e recibo.",
)

add_p(doc, "2. O que o sistema oferece", size=16, bold=True, color=RED, space_after=8)
add_p(doc, "Preparação e validação", size=13, bold=True, space_after=4)
add_p(doc, "• Modelo CSV de cada leiaute, no padrão brasileiro (separador e decimal).")
add_p(doc, "• Importação de CSV, XML ou ZIP (lote no D-1001).")
add_p(doc, "• Validação imediata ao selecionar o arquivo: campos, domínio, XSD e regras de negócio.")
add_p(doc, "• Relatório de críticas em tela e em Excel (linha, coluna, encontrado, esperado e como corrigir).")
add_p(doc, "• Conversão CSV → XML no leiaute oficial e download do XML gerado.")
add_p(doc, "Transmissão", size=13, bold=True, space_after=4)
add_p(doc, "• Assinatura XMLDSig do evento com o certificado A1 da entidade.")
add_p(doc, "• Envio do lote à Receita (pré-produção ou produção, conforme o ambiente).")
add_p(doc, "• Protocolo de recepção na hora e consulta automática do recibo a cada minuto.")
add_p(doc, "• Consulta manual de protocolo, PDF do protocolo e relatório de transmissões.")
add_p(doc, "• Nova tentativa visível em falha de comunicação, com o botão de envio travado até o fim da tentativa.")
add_p(doc, "Operação", size=13, bold=True, space_after=4)
add_p(doc, "• Visão geral da entidade, pendências e últimos envios.")
add_p(doc, "• Histórico filtrável por leiaute, situação, período e protocolo.")
add_p(doc, "• Ciclo incluir / alterar / excluir nos eventos de tabela (D-1001, D-1011, D-1101, D-1199).")
add_p(doc, "• Dependência D-1011 → D-1001 e mapeamento do PGCC ao plano referencial PREVIC (código 5).")
add_p(doc, "• Isolamento por entidade: o usuário só opera o CNPJ ao qual está vinculado.")

add_p(doc, "3. O que oferece de segurança", size=16, bold=True, color=RED, space_after=8)
add_p(
    doc,
    "A chave privada do certificado não vai para a Receita. O sistema assina o XML no ambiente da Data A e transmite só o documento já autenticado, com o certificado público necessário à verificação.",
)
add_p(doc, "• Acesso com usuário e senha. Perfis: operacional (sua entidade) e master (administração).")
add_p(doc, "• Certificado A1 (P12/PFX) vinculado à entidade. Só quem tem acesso a essa entidade usa o certificado.")
add_p(doc, "• A senha do certificado é informada uma vez, nunca reexibida, e fica no AWS Secrets Manager, cifrada com AWS KMS.")
add_p(doc, "• O arquivo do certificado fica cifrado em repositório separado da senha e dos dados de negócio.")
add_p(doc, "• Conferência em memória: se o P12 ou a senha não corresponderem, nada é gravado.")
add_p(doc, "• Assinatura em memória no instante do envio. Depois, a chave privada não permanece em uso.")
add_p(doc, "• Trilha de tentativas de transmissão (operação, protocolo, retorno HTTP).")
add_p(doc, "• Comunicação com a Receita em HTTPS, com OAuth (Receita Integra) nos ambientes reais.")
add_p(doc, "• Exclusão do certificado o desativa na hora.")
add_p(
    doc,
    "Recomendação ao cliente: use apenas certificado A1 de titularidade da entidade. Não envie o arquivo nem a senha por e-mail ou chat.",
    italic=True,
    color=GRAY,
)

add_p(doc, "4. Leiautes: o que já está no sistema e o que tem entrega obrigatória agora", size=16, bold=True, color=RED, space_after=8)
add_p(
    doc,
    "O cronograma oficial da DeRE (Ato Conjunto RFB/CGIBS nº 4/2026; documentação técnica v1.2.0) tem eventos estruturais, mensais e demais eventos a partir de 2027. No produto Data A hoje estão implementados quatro leiautes. Dois são a obrigação estrutural em vigor. Os outros dois já rodam no sistema, mas a entrega está isenta neste momento — o cliente pode validar e conhecer o fluxo, sem obrigação de transmitir até nova orientação da Receita / CGIBS / PREVIC.",
)
add_table(
    doc,
    ["Leiaute", "Conteúdo", "No produto", "Situação para o cliente"],
    [
        ["D-1001", "Informações do contribuinte", "Implementado", "Em uso — primeiro evento a transmitir"],
        ["D-1011", "PGCC (Plano Geral de Contas Comentado), com plano referencial PREVIC", "Implementado", "Isento no momento — tela e validação disponíveis"],
        ["D-1101", "Balancete mensal", "Implementado", "Isento no momento — tela e validação disponíveis"],
        ["D-1199", "Fechamento mensal (somente inclusão)", "Implementado", "Isento no momento — tela e validação disponíveis"],
        ["D-1106", "Identificação de aplicações financeiras", "Ainda não implementado", "Fora do produto; aplicável só quando a norma exigir"],
        ["D-1121", "Relação de deduções na apuração", "Ainda não implementado", "Fora do produto; conforme aplicabilidade"],
        ["D-2101", "Débito em títulos de dívida com oferta pública", "Ainda não implementado", "Fora do produto; conforme aplicabilidade"],
        ["Demais eventos (a partir de 2027)", "Eventos fora das fases estrutural e mensal inicial", "Não implementados", "Fora do escopo atual"],
    ],
)
add_p(doc, "5. Telas disponíveis", size=16, bold=True, color=RED, space_after=8)
add_table(
    doc,
    ["Tela", "Quem acessa", "Para que serve"],
    [
        ["Login", "Todos", "Acesso com usuário e senha"],
        ["Visão geral", "Todos", "Resumo da entidade, certificado, pendências e atalho para novo envio"],
        ["Enviar declaração", "Todos", "Escolha do leiaute (D-1001, D-1011, D-1101, D-1199), arquivo, validação e envio à Receita"],
        ["Histórico de envios", "Todos", "Protocolo, recibo, situação, filtros, detalhes, XML/PDF e consulta de protocolo"],
        ["Certificado digital", "Todos (da entidade)", "Cadastro, validade, substituição e exclusão do A1"],
        ["Configurações", "Somente Data A", "Visibilidade de menus e parâmetros da Integra Receita"],
        ["Entidades", "Somente Data A", "Cadastro da EFPC (CNPJ raiz e razão social) e vínculo de usuários"],
        ["Usuários", "Somente Data A", "Cadastro de operadores e indicação de master"],
    ],
)
add_p(doc, "Na tela de envio o usuário também baixa o layout CSV, o XML gerado e o Excel de críticas.")

add_p(doc, "6. Ambientes", size=16, bold=True, color=RED, space_after=8)
add_p(
    doc,
    "Os quatro ambientes compartilham o mesmo produto e bancos/credenciais separados. Staging e homologação sobem automaticamente no GitHub Actions; produção sobe só com disparo manual.",
)
add_table(
    doc,
    ["Ambiente", "Endereço", "O que o envio faz"],
    [
        ["Local (demonstração)", "http://localhost:5173/", "Uso interno / POC. Envio simulado, sem Receita."],
        ["Staging", "https://dere-stg.dataa.com.br/", "Teste técnico. Envio somente simulado."],
        ["Homologação", "https://dere-hml.dataa.com.br/", "Pré-produção da Receita (piloto / produção restrita)."],
        ["Produção", "https://dere.dataa.com.br/", "Envio oficial à Receita Federal."],
    ],
)
add_p(
    doc,
    "Homologação e produção exigem credencial Receita Integra (client_id / client_secret) e certificado A1 da entidade. Staging e local não falam com a API oficial.",
)

add_p(doc, "7. Como o cliente usa, na prática", size=16, bold=True, color=RED, space_after=8)
add_p(doc, "1. A Data A cadastra a entidade (CNPJ raiz) e os usuários.")
add_p(doc, "2. A entidade anexa o certificado A1 e a senha uma única vez.")
add_p(doc, "3. Envia o D-1001 (CSV ou XML). Corrige críticas, se houver, e transmite.")
add_p(doc, "4. Aguarda o recibo de aceito.")
add_p(doc, "5. Envia o D-1011 (PGCC / PREVIC) pela mesma tela, quando este layout estiver disponível para EFPC.")
add_p(doc, "6. Quando a isenção dos mensais acabar, usa as mesmas telas para D-1101 e D-1199.")

add_p(doc, "8. Pontos de atenção:", size=16, bold=True, color=RED, space_after=8)
add_p(doc, "• Não inclui D-1106, D-1121 e D-2101 neste momento.")
add_p(doc, "• Homologação oficial depende de participação no piloto e da credencial da entidade no Receita Integra.")

saida = Path(__file__).with_name("DeRE-descricao-do-produto-para-validacao.docx")
doc.save(saida)
print(saida)