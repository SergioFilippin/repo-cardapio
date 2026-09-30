import type { Prato } from './types'

const normalizar = (texto: string) =>
  texto
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLocaleLowerCase('pt-BR')
    .trim()

export function filtrarPratos(
  pratos: Prato[],
  busca: string,
  nacionalidade: string,
) {
  const termo = normalizar(busca)

  return pratos.filter((prato) => {
    const correspondeNacionalidade =
      !nacionalidade || prato.nacionalidade === nacionalidade
    const conteudo = normalizar(
      `${prato.nome} ${prato.descricao} ${prato.detalhe} ${prato.tipo}`,
    )

    return correspondeNacionalidade && (!termo || conteudo.includes(termo))
  })
}
