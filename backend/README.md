# Desafio Topaz - URL Shortener

Projeto desenvolvido para o desafio técnico Topaz com o objetivo de criar um encurtador de URLs simples, funcional e de fácil manutenção.

A proposta foi entregar uma solução organizada, com backend em Java e frontend em Angular, priorizando clareza no código e funcionamento completo.

---

# Visão Geral

O sistema permite informar uma URL original e gerar um link encurtado.

Exemplo:

URL original:

https://www.google.com

URL curta gerada:

http://localhost:8080/desafiotopaz/r/google

Ao acessar a URL curta, o usuário é redirecionado automaticamente para o endereço original.

---

# Tecnologias Utilizadas

## Backend

- Java 8
- Maven
- WildFly 10
- JAX-RS
- CDI
- JPA + Hibernate
- Banco H2 local

## Frontend

- Angular
- TypeScript
- CSS

---

# Estrutura do Projeto

desafiotopaz/
├── backend/
└── frontend/

---

# Como Executar o Projeto

## Backend

Entrar na pasta backend:

cd backend

Gerar o pacote:

mvn clean package

Após gerar o .war, copiar para:

wildfly/standalone/deployments/

Com o WildFly iniciado, o backend ficará disponível em:

http://localhost:8080/desafiotopaz

---

## Frontend

Entrar na pasta frontend:

cd frontend

Instalar dependências:

npm install

Executar aplicação:

ng serve

Frontend disponível em:

http://localhost:4200

---

# Funcionalidades Implementadas

- Cadastro de nova URL
- Alias personalizado
- Geração automática de código curto
- Consulta por ID
- Listagem completa de links cadastrados
- Atualização de registros
- Exclusão de registros
- Copiar link gerado
- Abrir link diretamente no navegador
- Redirecionamento pela URL curta

---

# Endpoints Disponíveis

## API Administrativa

POST   /api/link/salvar
GET    /api/link/buscarPorId/{id}
GET    /api/link/buscarTodos
PUT    /api/link/atualizar/{id}
DELETE /api/link/excluir/{id}

## URL Pública

GET /r/{aliasOuCodigo}

Exemplo:

http://localhost:8080/desafiotopaz/r/google

---

# Organização do Código

Foi utilizada uma estrutura simples em camadas:

Controller -> Service -> Repository

Separando responsabilidades entre:

- Controller: recebe requisições
- Service: regras de negócio
- Repository: acesso a dados

Também foi mantida organização por domínio para facilitar manutenção futura.

---

# Decisões Durante o Desenvolvimento

A proposta do projeto não foi exagerar em complexidade, e sim entregar uma solução coerente com o desafio.

Por isso, foram priorizados:

- Código limpo
- Estrutura objetiva
- Fácil entendimento
- Interface funcional
- Backend desacoplado do frontend

---

# Melhorias Futuras

- Autenticação administrativa
- Swagger
- Testes unitários
- Docker
- Deploy em nuvem
- Métricas de acesso
- Paginação
- Expiração de links

---

# Postman

A collection com todos os endpoints utilizados acompanha o projeto.

---

# Considerações Finais

Este projeto foi construído buscando equilíbrio entre simplicidade técnica e boa apresentação.

A ideia principal foi demonstrar organização, entendimento de arquitetura e entrega funcional, sem criar complexidade desnecessária.
