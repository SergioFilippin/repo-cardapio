import { describe, expect, it } from 'vitest'
import { filtrarPratos } from './filter'
import type { Prato } from './types'

const pratos: Prato[] = [
  {
    id: 1,
    tipo: 'RISOTO',
    nome: 'Risoto de camarão',
    descricao: 'Arroz cremoso',
    preco: 49.9,
    nacionalidade: 'Italiana',
    detalhe: 'Camarão fresco',
    disponivel: true,
  },
  {
    id: 2,
    tipo: 'JANTINHA',
    nome: 'Jantinha brasileira',
    descricao: 'Refeição completa',
    preco: 29.9,
    nacionalidade: 'Brasileira',
    detalhe: 'Feijão tropeiro',
    disponivel: true,
  },
]

describe('filtrarPratos', () => {
  it('busca sem diferenciar acentos ou maiúsculas', () => {
    expect(filtrarPratos(pratos, 'CAMARAO', '')).toEqual([pratos[0]])
  })

  it('combina busca por ingrediente e nacionalidade', () => {
    expect(filtrarPratos(pratos, 'feijao', 'Brasileira')).toEqual([pratos[1]])
    expect(filtrarPratos(pratos, 'feijao', 'Italiana')).toEqual([])
  })
})
