import '@testing-library/jest-dom/vitest'
import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { afterEach, describe, expect, it, vi } from 'vitest'
import App from './App'

const pratos = [
  { id: 1, tipo: 'RISOTO', nome: 'Risoto de camarão', descricao: 'Arroz cremoso', preco: 49.9, nacionalidade: 'Italiana', detalhe: 'Camarão', disponivel: true },
  { id: 2, tipo: 'JANTINHA', nome: 'Jantinha brasileira', descricao: 'Prato completo', preco: 29.9, nacionalidade: 'Brasileira', detalhe: 'Feijão', disponivel: true },
]

afterEach(() => vi.unstubAllGlobals())

describe('App', () => {
  it('filtra os pratos pela busca', async () => {
    vi.stubGlobal('fetch', vi.fn((url: string) => Promise.resolve({
      ok: true,
      json: () => Promise.resolve(url.includes('nacionalidades')
        ? [{ id: 1, nome: 'Italiana' }, { id: 2, nome: 'Brasileira' }]
        : pratos),
    })))
    const usuario = userEvent.setup()
    render(<App />)

    expect(await screen.findByText('Risoto de camarão')).toBeInTheDocument()
    await usuario.type(screen.getByRole('searchbox', { name: 'Buscar no cardápio' }), 'feijao')

    expect(screen.getByText('Jantinha brasileira')).toBeInTheDocument()
    expect(screen.queryByText('Risoto de camarão')).not.toBeInTheDocument()
  })
})
