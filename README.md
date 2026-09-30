# Cardapio Casa de Dentro

Aplicacao full stack de cardapio, composta por uma API REST em Java e uma interface web responsiva.

## Tecnologias

- Java 21, Spring Boot, Spring Web, Validation e Spring Data JPA
- H2 para desenvolvimento rapido e PostgreSQL para execucao com Docker
- React, TypeScript e Vite
- JUnit/MockMvc, Vitest e Testing Library

## Executar com Docker

Requer Docker Desktop com Docker Compose habilitado.

```bash
docker compose up --build
```

Acesse:

- Frontend: http://localhost:5173
- API: http://localhost:8080/api/pratos

O PostgreSQL fica disponivel apenas para os outros containers. Os dados permanecem no volume `cardapio-db`.

## Executar para desenvolvimento

Requisitos: Java 21 e Node.js 22 ou superior.

Inicie a API, que usa H2 em memoria por padrao:

```bash
cd backend
./mvnw spring-boot:run
```

Em outro terminal, inicie o frontend:

```bash
cd frontend
npm install
npm run dev
```

O Vite encaminha requisicoes `/api` para `http://localhost:8080`.

## API

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `GET` | `/api/pratos` | Lista os pratos |
| `GET` | `/api/pratos?busca=lasanha&nacionalidade=Italiana` | Filtra o cardapio |
| `GET` | `/api/pratos/{id}` | Busca um prato |
| `POST` | `/api/pratos` | Cria um prato |
| `PUT` | `/api/pratos/{id}` | Atualiza um prato |
| `DELETE` | `/api/pratos/{id}` | Exclui um prato |
| `GET` | `/api/nacionalidades` | Lista as nacionalidades |

Exemplo de criacao:

```json
{
  "tipo": "RISOTO",
  "nome": "Risoto de limao siciliano",
  "descricao": "Arroz arboreo cremoso.",
  "preco": 45.50,
  "nacionalidade": "Italiana",
  "detalhe": "Limao siciliano",
  "disponivel": true
}
```

Os tipos aceitos sao `LASANHA`, `RISOTO`, `JANTINHA` e `HAMBURGUER`.

## POO no backend

- **Encapsulamento:** os atributos do dominio sao privados e alterados por metodos que protegem suas invariantes.
- **Abstracao:** `Prato` concentra os dados e contratos comuns.
- **Heranca:** `Lasanha`, `Risoto`, `Jantinha` e `Hamburguer` especializam `Prato`.
- **Polimorfismo:** cada subtipo implementa `getTipo()`, `getDetalhe()` e `setDetalhe()`; o servico e os DTOs trabalham com a abstracao `Prato`.

## Testes

```bash
cd backend && ./mvnw test
cd frontend && npm test
cd frontend && npm run lint && npm run build
```
