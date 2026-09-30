import '@testing-library/jest-dom/vitest'
import { cleanup, render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, describe, expect, it, vi } from 'vitest'
import App from './App'

const pratos = [
  { id: 1, tipo: 'RISOTO', nome: 'Risoto de camarão', descricao: 'Arroz cremoso', preco: 49.9, nacionalidade: 'Italiana', detalhe: 'Camarão', disponivel: true },
  { id: 2, tipo: 'JANTINHA', nome: 'Jantinha brasileira', descricao: 'Prato completo', preco: 29.9, nacionalidade: 'Brasileira', detalhe: 'Feijão', disponivel: true },
  { id: 3, tipo: 'PORCAO', nome: 'Batata frita', descricao: 'Batatas crocantes', preco: null, nacionalidade: 'Brasileira', detalhe: 'Porção tamanho único', disponivel: true },
]

afterEach(() => {
  cleanup()
  vi.unstubAllGlobals()
})

const mockFetch = () => {
  vi.stubGlobal('fetch', vi.fn(() => Promise.resolve({
    ok: true,
    json: () => Promise.resolve(pratos),
  })))
}

describe('App', () => {
  it('filtra os pratos pela busca', async () => {
    mockFetch()
    const usuario = userEvent.setup()
    render(<App />)

    expect(await screen.findByText('Risoto de camarão')).toBeInTheDocument()
    await usuario.type(screen.getByRole('searchbox', { name: 'Buscar no cardápio' }), 'feijao')

    expect(screen.getByText('Jantinha brasileira')).toBeInTheDocument()
    expect(screen.queryByText('Risoto de camarão')).not.toBeInTheDocument()
  })

  it('mostra uma grade única e filtra por porções', async () => {
    mockFetch()
    const usuario = userEvent.setup()
    render(<App />)

    expect(await screen.findByText('Risoto de camarão')).toBeInTheDocument()
    expect(screen.getByText('Jantinha brasileira')).toBeInTheDocument()
    expect(screen.queryByText(/Cozinha (Italiana|Brasileira)/)).not.toBeInTheDocument()

    await usuario.click(screen.getByRole('button', { name: 'Porções' }))

    expect(screen.getByText('Batata frita')).toBeInTheDocument()
    expect(screen.queryByText('Risoto de camarão')).not.toBeInTheDocument()
    expect(screen.queryByText(/R\$/)).not.toBeInTheDocument()
  })
})
