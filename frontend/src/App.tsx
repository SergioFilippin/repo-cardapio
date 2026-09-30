import { useEffect, useState } from 'react'
import { carregarCardapio } from './api'
import { filtrarPratos } from './filter'
import type { Prato, TipoPrato } from './types'

const nomesTipo: Record<TipoPrato, string> = {
  LASANHA: 'Lasanha',
  RISOTO: 'Risoto',
  JANTINHA: 'Jantinha',
  HAMBURGUER: 'Hambúrguer',
  PORCAO: 'Porção',
}

const categorias: { tipo: TipoPrato; nome: string }[] = [
  { tipo: 'LASANHA', nome: 'Lasanhas' },
  { tipo: 'RISOTO', nome: 'Risotos' },
  { tipo: 'JANTINHA', nome: 'Jantinhas' },
  { tipo: 'HAMBURGUER', nome: 'Hambúrgueres' },
  { tipo: 'PORCAO', nome: 'Porções' },
]

type Estado = 'carregando' | 'sucesso' | 'erro'

function App() {
  const [pratos, setPratos] = useState<Prato[]>([])
  const [busca, setBusca] = useState('')
  const [filtro, setFiltro] = useState<TipoPrato | ''>('')
  const [estado, setEstado] = useState<Estado>('carregando')
  const [tentativa, setTentativa] = useState(0)

  useEffect(() => {
    const controller = new AbortController()
    setEstado('carregando')

    carregarCardapio(controller.signal)
      .then((dados) => {
        setPratos(dados)
        setEstado('sucesso')
      })
      .catch((erro: unknown) => {
        if (erro instanceof DOMException && erro.name === 'AbortError') return
        setEstado('erro')
      })

    return () => controller.abort()
  }, [tentativa])

  const filtrados = filtrarPratos(pratos, busca, filtro)
  const limparFiltros = () => {
    setBusca('')
    setFiltro('')
  }

  return (
    <>
      <header className="topo">
        <a className="marca" href="#inicio" aria-label="Casa de Dentro, início">
          <span className="marca-selo" aria-hidden="true">CD</span>
          <span>Restaurante do Sérgio</span>
        </a>
        <a className="link-cardapio" href="#cardapio">Ver o cardápio ↓</a>
      </header>

      <main id="inicio">
        <section className="hero" aria-labelledby="titulo-principal">
          <div className="hero-texto">
            <p className="sobretitulo">Cozinha de afeto · desde sempre</p>
            <h1 id="titulo-principal">Tem lugar<br />à mesa.</h1>
            <p className="hero-resumo">
              Receitas que gosto de preparar para pessoas que amo.
            </p>
          </div>
          <div className="hero-prato" aria-hidden="true">
            <div className="prato-grafico">
              <span className="folha folha-a" />
              <span className="folha folha-b" />
              <span className="molho" />
            </div>
            <p>sz<br /><strong>:D</strong></p>
          </div>
          <div className="hero-rodape">
            <span>01</span>
            <p>Nosso cardápio muda,<br />o cuidado não.</p>
          </div>
        </section>

        <section className="menu" id="cardapio" aria-labelledby="titulo-cardapio">
          <div className="menu-cabecalho">
            <div>
              <p className="sobretitulo">Escolha com calma</p>
              <h2 id="titulo-cardapio">O cardápio</h2>
            </div>
            <p className="contagem" aria-live="polite">
              <strong>{estado === 'sucesso' ? filtrados.length : '—'}</strong>
              {filtrados.length === 1 ? ' prato encontrado' : ' pratos encontrados'}
            </p>
          </div>

          <div className="controles" aria-label="Filtros do cardápio">
            <label className="campo-busca">
              <span className="sr-only">Buscar no cardápio</span>
              <span aria-hidden="true">⌕</span>
              <input
                type="search"
                placeholder="Busque por prato ou ingrediente"
                value={busca}
                onChange={(evento) => setBusca(evento.target.value)}
              />
            </label>
            <div className="filtros" role="group" aria-label="Filtrar por categoria">
              <button
                className={!filtro ? 'ativo' : ''}
                type="button"
                aria-pressed={!filtro}
                onClick={() => setFiltro('')}
              >
                Todas
              </button>
              {categorias.map((categoria) => (
                <button
                  className={filtro === categoria.tipo ? 'ativo' : ''}
                  type="button"
                  aria-pressed={filtro === categoria.tipo}
                  onClick={() => setFiltro(categoria.tipo)}
                  key={categoria.tipo}
                >
                  {categoria.nome}
                </button>
              ))}
            </div>
          </div>

          {estado === 'carregando' && <Carregando />}
          {estado === 'erro' && (
            <Mensagem
              titulo="A cozinha demorou a responder"
              texto="Não conseguimos carregar os pratos agora. Confira sua conexão e tente novamente."
              acao="Tentar novamente"
              onClick={() => setTentativa((valor) => valor + 1)}
            />
          )}
          {estado === 'sucesso' && filtrados.length === 0 && (
            <Mensagem
              titulo="Nenhum prato por aqui"
              texto="Experimente outro termo ou volte a ver todas as receitas da casa."
              acao="Limpar filtros"
              onClick={limparFiltros}
            />
          )}
          {estado === 'sucesso' && filtrados.length > 0 && (
            <div className="grade-pratos">
              {filtrados.map((prato, indice) => (
                <article className={`prato ${!prato.disponivel ? 'indisponivel' : ''}`} key={prato.id}>
                  <div className="prato-meta">
                    <span>{nomesTipo[prato.tipo]}</span>
                    <span>{String(indice + 1).padStart(2, '0')}</span>
                  </div>
                  <h3>{prato.nome}</h3>
                  <p className="descricao">{prato.descricao}</p>
                  <p className="detalhe"><span aria-hidden="true">✦</span> {prato.detalhe}</p>
                  {!prato.disponivel && (
                    <div className="prato-rodape">
                      <span className="aviso">Indisponível hoje</span>
                    </div>
                  )}
                </article>
              ))}
            </div>
          )}
        </section>
      </main>

      <footer>
        <div className="footer-marca">Casa<br />de Dentro<span>.</span></div>
        <p>Comida boa aproxima.<br />Chegue, sente e fique à vontade.</p>
        <a href="#inicio">Voltar ao topo ↑</a>
      </footer>
    </>
  )
}

function Carregando() {
  return (
    <div className="carregando" role="status">
      <span className="sr-only">Carregando o cardápio</span>
      {[1, 2, 3].map((item) => <span key={item} />)}
    </div>
  )
}

function Mensagem({
  titulo,
  texto,
  acao,
  onClick,
}: {
  titulo: string
  texto: string
  acao: string
  onClick: () => void
}) {
  return (
    <div className="mensagem" role="status">
      <span className="mensagem-ornamento" aria-hidden="true">✣</span>
      <h3>{titulo}</h3>
      <p>{texto}</p>
      <button type="button" onClick={onClick}>{acao}</button>
    </div>
  )
}

export default App
