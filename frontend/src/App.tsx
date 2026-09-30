import { useEffect, useState } from 'react'
import { carregarCardapio } from './api'
import { filtrarPratos } from './filter'
import type { Nacionalidade, Prato, TipoPrato } from './types'

const moeda = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
})

const nomesTipo: Record<TipoPrato, string> = {
  LASANHA: 'Lasanha',
  RISOTO: 'Risoto',
  JANTINHA: 'Jantinha',
  HAMBURGUER: 'Hambúrguer',
}

type Estado = 'carregando' | 'sucesso' | 'erro'

function App() {
  const [pratos, setPratos] = useState<Prato[]>([])
  const [nacionalidades, setNacionalidades] = useState<Nacionalidade[]>([])
  const [busca, setBusca] = useState('')
  const [filtro, setFiltro] = useState('')
  const [estado, setEstado] = useState<Estado>('carregando')
  const [tentativa, setTentativa] = useState(0)

  useEffect(() => {
    const controller = new AbortController()
    setEstado('carregando')

    carregarCardapio(controller.signal)
      .then((dados) => {
        setPratos(dados.pratos)
        setNacionalidades(dados.nacionalidades)
        setEstado('sucesso')
      })
      .catch((erro: unknown) => {
        if (erro instanceof DOMException && erro.name === 'AbortError') return
        setEstado('erro')
      })

    return () => controller.abort()
  }, [tentativa])

  const filtrados = filtrarPratos(pratos, busca, filtro)
  const grupos = filtrados.reduce<Record<string, Prato[]>>((resultado, prato) => {
    ;(resultado[prato.nacionalidade] ??= []).push(prato)
    return resultado
  }, {})
  const limparFiltros = () => {
    setBusca('')
    setFiltro('')
  }

  return (
    <>
      <header className="topo">
        <a className="marca" href="#inicio" aria-label="Casa de Dentro, início">
          <span className="marca-selo" aria-hidden="true">CD</span>
          <span>Casa de Dentro</span>
        </a>
        <a className="link-cardapio" href="#cardapio">Ver o cardápio ↓</a>
      </header>

      <main id="inicio">
        <section className="hero" aria-labelledby="titulo-principal">
          <div className="hero-texto">
            <p className="sobretitulo">Cozinha de afeto · desde sempre</p>
            <h1 id="titulo-principal">Tem lugar<br />à mesa.</h1>
            <p className="hero-resumo">
              Receitas que atravessam fronteiras, preparadas sem pressa e
              servidas como domingo em família.
            </p>
          </div>
          <div className="hero-prato" aria-hidden="true">
            <div className="prato-grafico">
              <span className="folha folha-a" />
              <span className="folha folha-b" />
              <span className="molho" />
            </div>
            <p>feito aqui<br /><strong>com tempo</strong></p>
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
            <div className="filtros" role="group" aria-label="Filtrar por nacionalidade">
              <button
                className={!filtro ? 'ativo' : ''}
                type="button"
                aria-pressed={!filtro}
                onClick={() => setFiltro('')}
              >
                Todas
              </button>
              {nacionalidades.map((nacionalidade) => (
                <button
                  className={filtro === nacionalidade.nome ? 'ativo' : ''}
                  type="button"
                  aria-pressed={filtro === nacionalidade.nome}
                  onClick={() => setFiltro(nacionalidade.nome)}
                  key={nacionalidade.id}
                >
                  {nacionalidade.nome}
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
            <div className="grupos">
              {Object.entries(grupos).map(([nacionalidade, itens], indiceGrupo) => (
                <section className="grupo" key={nacionalidade} aria-labelledby={`grupo-${indiceGrupo}`}>
                  <div className="grupo-titulo">
                    <span>{String(indiceGrupo + 1).padStart(2, '0')}</span>
                    <h3 id={`grupo-${indiceGrupo}`}>Cozinha {nacionalidade}</h3>
                    <span>{itens.length} {itens.length === 1 ? 'receita' : 'receitas'}</span>
                  </div>
                  <div className="grade-pratos">
                    {itens.map((prato, indice) => (
                      <article className={`prato ${!prato.disponivel ? 'indisponivel' : ''}`} key={prato.id}>
                        <div className="prato-meta">
                          <span>{nomesTipo[prato.tipo]}</span>
                          <span>{String(indice + 1).padStart(2, '0')}</span>
                        </div>
                        <h4>{prato.nome}</h4>
                        <p className="descricao">{prato.descricao}</p>
                        <p className="detalhe"><span aria-hidden="true">✦</span> {prato.detalhe}</p>
                        <div className="prato-rodape">
                          <strong>{moeda.format(prato.preco)}</strong>
                          {!prato.disponivel && <span className="aviso">Indisponível hoje</span>}
                        </div>
                      </article>
                    ))}
                  </div>
                </section>
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
