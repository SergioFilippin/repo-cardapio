import type { Prato, TipoPrato } from './types'

const normalizar = (texto: string) =>
  texto
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLocaleLowerCase('pt-BR')
    .trim()

export function filtrarPratos(
  pratos: Prato[],
  busca: string,
  tipo: TipoPrato | '',
) {
  const termo = normalizar(busca)

  return pratos.filter((prato) => {
    const correspondeTipo = !tipo || prato.tipo === tipo
    const conteudo = normalizar(
      `${prato.nome} ${prato.descricao} ${prato.detalhe} ${prato.tipo}`,
    )

    return correspondeTipo && (!termo || conteudo.includes(termo))
  })
}
