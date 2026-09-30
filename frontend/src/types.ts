export type TipoPrato = 'LASANHA' | 'RISOTO' | 'JANTINHA' | 'HAMBURGUER'

export interface Prato {
  id: number
  tipo: TipoPrato
  nome: string
  descricao: string
  preco: number
  nacionalidade: string
  detalhe: string
  disponivel: boolean
}

export interface Nacionalidade {
  id: number
  nome: string
}
