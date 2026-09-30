import type { Prato } from './types'

const baseUrl = (import.meta.env.VITE_API_URL ?? '').replace(/\/$/, '')

async function getJson<T>(path: string, signal?: AbortSignal): Promise<T> {
  const response = await fetch(`${baseUrl}${path}`, {
    signal,
    headers: { Accept: 'application/json' },
  })

  if (!response.ok) {
    throw new Error(`A API respondeu com o status ${response.status}.`)
  }

  return response.json() as Promise<T>
}

export function carregarCardapio(signal?: AbortSignal) {
  return getJson<Prato[]>('/api/pratos', signal)
}
