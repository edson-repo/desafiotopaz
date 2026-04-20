# Desafio Topaz - URL Shortener

Projeto desenvolvido como solução para o desafio técnico Topaz.

A proposta foi construir uma aplicação simples, organizada e funcional para encurtamento de URLs, utilizando back-end em Java e front-end em Angular.

## Objetivo

Permitir que o usuário informe uma URL original e receba uma versão curta para compartilhamento e redirecionamento.

Exemplo:
http://localhost:8080/desafiotopaz/r/google

## Estrutura do Projeto

desafiotopaz/
- backend/
- frontend/
- postman/

## Tecnologias Utilizadas

### Backend
- Java 8
- Maven
- WildFly 10
- JAX-RS
- CDI
- JPA / Hibernate
- Banco H2
- Docker

### Frontend
- Angular
- TypeScript
- CSS
- Docker

### Testes
- JUnit
- Mockito

## Funcionalidades

- Criar URL encurtada
- Alias personalizado
- Código curto automático
- Buscar por ID
- Listar todos
- Atualizar
- Excluir
- Redirecionar
- Frontend integrado

## Endpoints

Base:
http://localhost:8080/desafiotopaz/api/link

POST /salvar
GET /buscarPorId/{id}
GET /buscarTodos
PUT /atualizar/{id}
DELETE /excluir/{id}

URL pública:
GET /r/{aliasOuCodigo}

## Como Executar

Backend:
cd backend
mvn clean package
docker compose up --build -d

Frontend:
cd frontend
npm install
ng serve -o

ou

docker compose up --build -d

## Testes

cd backend
mvn test

## Organização

Controller -> Service -> Repository

## Considerações Finais

Projeto criado com foco em simplicidade, clareza e entrega funcional.
