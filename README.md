# Sistema de Gestão de Atividades Físicas

## Visão Geral

Esta aplicação foi desenvolvida como resposta a um **case técnico** para criação de uma **API RESTful de registro de atividades físicas**. O projeto **excedeu os requisitos mínimos**, apresentando arquitetura moderna, containerização, frontend completo e documentação profissional.

> **Nota sobre IA**: Ferramentas como GitHub Copilot e ChatGPT foram utilizadas como apoio na escrita de código, documentação e resolução de problemas. Todas as decisões técnicas foram revisadas e compreendidas.

---

## Requisitos do Case Técnico

### Endpoints Solicitados

1. **POST /atividades** – Registrar nova atividade ✅
2. **GET /atividades** – Listar todas as atividades ✅
3. **GET /atividades/{funcional}** – Listar atividades por funcional ✅

> **Implementação REST aprimorada**: `GET /atividades?funcional=12345` (query parameter) para permitir filtros combinados e manter semântica REST correta.

### Requisitos Técnicos Atendidos

- API RESTful funcional em **Java + Spring Boot**
- Armazenamento em **MySQL** (Docker)
- Testável via **Postman/Insomnia**
- Código organizado com arquitetura em camadas
- README e documentação completa

---

## Diferenciais Implementados

| Categoria      | Entregue                                  | Valor Agregado                                                |
| -------------- | ----------------------------------------- | -----------------------------------------------------------   |
| Endpoints      | 5 endpoints - CRUD completo               | Filtros avançados por funcional, codigo da atividade, período |
| Interface      | Frontend React responsivo                 | Experiência web moderna e intuitiva                           |
| Infraestrutura | Docker Compose                            | Containerização profissional                                  |
| Documentação   | Swagger + 6 docs técnicas                 | Padrões enterprise                                            |
| Testes         | Unitários + integração + cobertura JaCoCo | Qualidade e confiabilidade                                    |
| Arquitetura    | Camadas Controller → Service → Repository | Escalabilidade e manutenção fácil                             |

---

## Funcionalidades Principais

- **Cadastro de atividades**: data/hora, codigo da atividade e descrição
- **Listagem completa** e filtros avançados: por funcional, codigo da atividade, descrição ou período
- **CRUD completo**: criar, editar, excluir e buscar atividades
- **Interface Web**: Frontend moderno em React
- **Documentação interativa**: Swagger/OpenAPI

> **Documentação Detalhada**: Para exemplos completos de endpoints, validações e testes, consulte [Endpoints, Testes e Validações](docs/endpoints-tests-validations.md)4
---

## Stack Tecnológica

**Backend (Java + Spring Boot)**

- Java 17, Spring Boot 3.5.6
- Spring Data JPA, Hibernate
- Bean Validation, MapStruct, Lombok
- SpringDoc OpenAPI, JUnit 5, JaCoCo

> **Arquitetura Detalhada**: Para compreender a estrutura completa do backend, padrões implementados e sistema de logging, consulte [Arquitetura e Logging](docs/architecture-logging.md)

**Frontend (React + Vite)**

- React 19, Axios, React Hook Form
- date-fns, ESLint, componentes reutilizáveis

> **Documentação Frontend**: Para detalhes sobre componentes, hooks e arquitetura React, consulte [Frontend](docs/frontend.md)

**Banco de Dados**

- MySQL 8.0, InnoDB, UTF-8
- Migrations automáticas, índices e constraints

> **Modelagem Completa**: Para scripts SQL, queries de exemplo e configurações avançadas, consulte [Banco de Dados](docs/database.md)

**DevOps / Infraestrutura**

- Docker + Docker Compose
- Containerização completa, volumes persistentes, redes isoladas

> **Guia Completo Docker**: Para configurações detalhadas, troubleshooting e otimizações, consulte [Setup com Docker](docs/docker-setup.md)

---

## Como Executar

### Via Docker Compose (Recomendado)

```bash
git clone https://github.com/jheniferlorrane/atividade-fisica-case-jhenifer.git
cd atividade-fisica-case-jhenifer

docker-compose up -d
docker-compose logs -f
```

**Acessos**:

- Frontend: [http://localhost:5173](http://localhost:5173)
- API: [http://localhost:8080](http://localhost:8080)
- Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- MySQL: localhost:3307

### Desenvolvimento Local

- **Banco de dados Docker**: `docker-compose up mysql -d`
- **Backend local**: `cd back-end && ./mvnw spring-boot:run`
- **Frontend local**: `cd front-end && npm install && npm run dev`

---

## Arquitetura do Sistema

```
Frontend React (5173)
        │
        ▼ HTTP
Backend Spring Boot (8080)
        │
        ▼ JPA/Hibernate
MySQL Database (3307)
```

**Containers**: `mysql_atividade`, `atividade_app`, `atividade_frontend`

> **Princípios REST**: Para compreender a implementação de padrões REST, filtros e boas práticas, consulte [RESTful e Boas Práticas](docs/restful.md)

---

## Modelo de Dados

**Tabela: atividade**

| Campo               | codigo da atividade         | Descrição                       | Constraints        |
| ------------------- | ------------ | ------------------------------- | ------------------ |
| id_atividade        | BIGINT       | ID único da atividade           | PK, AUTO_INCREMENT |
| funcional           | VARCHAR(50)  | Código funcional do funcional | NOT NULL           |
| data_hora           | DATETIME     | Data e hora da atividade        | NOT NULL           |
| codigo_atividade    | VARCHAR(20)  | codigo da atividade de atividade               | NOT NULL           |
| descricao_atividade | VARCHAR(255) | Descrição detalhada             | NOT NULL           |

---

## Logging

- **INFO**: operações principais
- **WARN**: atenção / recursos não encontrados
- **ERROR**: exceções com contexto

> Rastreabilidade completa: Controller → Service → Repository

---

## Testes e Qualidade

- Testes unitários (JUnit 5) e integração (Spring Boot Test)
- Cobertura de testes via JaCoCo
- Validação de DTOs com Bean Validation

> **Testes Detalhados**: Para exemplos de testes unitários, integração e ferramentas utilizadas, consulte [Endpoints, Testes e Validações](docs/endpoints-tests-validations.md)

**Executar testes:**

```bash
cd back-end
./mvnw test
./mvnw test jacoco:report
```

---

## Configurações Avançadas

- Variáveis de ambiente (`SPRING_DATASOURCE_URL`, `USERNAME`, `PASSWORD`, `SERVER_PORT`)
- Configuração CORS para desenvolvimento local
- Docker Compose detalhado para backend, frontend e MySQL

---

## Troubleshooting

- **Erro de conexão MySQL**: `docker-compose restart mysql`
- **Frontend não carrega**: `docker-compose restart frontend`
- **API retorna 500**: `docker-compose logs app`
- **Rebuild completo**: `docker-compose down -v && docker-compose up --build`

---

## Documentação Complementar

Explore a documentação técnica detalhada:

- **[Endpoints, Testes e Validações](docs/endpoints-tests-validations.md)** - Documentação completa da API com exemplos práticos
- **[Arquitetura e Logging](docs/architecture-logging.md)** - Detalhes da arquitetura do sistema e estrutura de logs
- **[Setup com Docker](docs/docker-setup.md)** - Configuração de infraestrutura e containers
- **[RESTful e Boas Práticas](docs/restful.md)** - Padrões REST implementados e justificativas técnicas
- **[Frontend](docs/frontend.md)** - Documentação da interface React com componentes e hooks
- **[Banco de Dados](docs/database.md)** - Estrutura, modelagem e queries do MySQL

---

## Desenvolvedor

**Jhenifer Lorrane**

- GitHub: [@jheniferlorrane](https://github.com/jheniferlorrane)
- LinkedIn: [Jhenifer Lorrane](https://www.linkedin.com/in/jheniferanacleto/)

---

## Versão

**v1.0.0** – Case Técnico Completo

- Requisitos 100% atendidos
- Funcionalidades extras implementadas
