# Desafio Topaz

Projeto desenvolvido como solução para o desafio técnico Topaz.

A aplicação permite cadastrar URLs longas e gerar links curtos para redirecionamento, com interface web simples e API REST em Java.

---

## Visão Geral

O sistema recebe uma URL original e gera um identificador curto.

Exemplo:

http://localhost:8080/desafiotopaz/r/google

ou

http://localhost:8080/desafiotopaz/r/abc123

Também possui tela web para gerenciamento dos links cadastrados.

---

## Estrutura do Projeto

desafiotopaz/

- backend/   -> API Java + WildFly
- frontend/  -> Angular
- postman/   -> Collection para testes

---

## Tecnologias Utilizadas

## Backend

- Java 8
- Maven
- WildFly 10
- JAX-RS
- CDI
- JPA / Hibernate
- Banco H2
- Docker

## Frontend

- Angular
- TypeScript
- CSS
- HttpClient
- Docker

---

## Funcionalidades

- Criar URL encurtada
- Alias personalizado
- Código curto automático
- Listar links cadastrados
- Buscar link por ID
- Atualizar link
- Excluir link
- Copiar link
- Abrir link
- Redirecionamento real

---

## Endpoints da API

Base:

http://localhost:8080/desafiotopaz/api/link

### Criar link

POST /salvar

### Buscar por ID

GET /buscarPorId/{id}

### Listar todos

GET /buscarTodos

### Atualizar

PUT /atualizar/{id}

### Excluir

DELETE /excluir/{id}

### Redirecionamento público

GET /r/{aliasOuCodigo}

---

## Como Executar

## Backend local

```bash
cd backend
mvn clean package
```

Gerar WAR e publicar no WildFly.

## Backend Docker

```bash
cd backend
docker compose up --build -d
```

Acesso:

http://localhost:8080/desafiotopaz

---

## Frontend local

```bash
cd frontend
npm install
ng serve -o
```

Acesso:

http://localhost:4200

---

## Frontend Docker

```bash
cd frontend
docker compose up --build -d
```

Acesso:

http://localhost

---

## Organização Técnica

Projeto organizado por domínio.

Exemplo backend:

br.com.topaz.desafiotopaz.link

- Entity
- Repository
- Service
- Controller
- Mapper
- DTOs

Fluxo:

Controller -> Service -> Repository

---

## Docker

Cada módulo pode subir separado:

- backend container
- frontend container

Também podem rodar juntos via compose principal.

---

## Postman

Existe collection pronta dentro da pasta:

postman/

Com todos endpoints para testes.

---

## Melhorias Futuras

- Testes automatizados
- Swagger/OpenAPI
- Login de usuários
- Dashboard analítico
- Deploy cloud
- Pipeline CI/CD

---

## Considerações Finais

Projeto desenvolvido buscando simplicidade, organização e clareza.

Atende os requisitos principais do desafio técnico com backend funcional, frontend operacional e estrutura preparada para evolução futura.
