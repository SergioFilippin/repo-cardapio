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
    id: 3,
    tipo: 'PORCAO',
    nome: 'Batata com cheddar',
    descricao: 'Batatas fritas com cheddar cremoso',
    preco: null,
    nacionalidade: 'Brasileira',
    detalhe: 'Porção tamanho único',
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

  it('combina busca por ingrediente e categoria', () => {
    expect(filtrarPratos(pratos, 'cheddar', 'PORCAO')).toEqual([pratos[1]])
    expect(filtrarPratos(pratos, 'cheddar', 'RISOTO')).toEqual([])
  })
})
