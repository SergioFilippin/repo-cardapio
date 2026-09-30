export type TipoPrato = 'LASANHA' | 'RISOTO' | 'JANTINHA' | 'HAMBURGUER' | 'PORCAO'

export interface Prato {
  id: number
  tipo: TipoPrato
  nome: string
  descricao: string
  preco: number | null
  nacionalidade: string
  detalhe: string
  disponivel: boolean
}
