# 🏗️ Arquitetura e Design da API

## 🎯 Visão Geral

Esta API foi desenvolvida seguindo **100% os princípios REST** e **arquitetura em camadas**, garantindo escalabilidade, manutenibilidade e boas práticas de desenvolvimento.

---

## ⚡ Princípios REST Implementados

### **Os 6 Princípios REST**

| Princípio              | Status | Como Implementamos                 |
| ---------------------- | ------ | ---------------------------------- |
| **Cliente-Servidor**   | ✅     | Separação clara frontend/backend   |
| **Stateless**          | ✅     | Cada request é independente        |
| **Cache**              | ✅     | GET naturalmente cacheável         |
| **Interface Uniforme** | ✅     | URIs, métodos, status padronizados |
| **Sistema em Camadas** | ✅     | Controller → Service → Repository  |
| **Código Sob Demanda** | N/A    | Opcional - não aplicável           |

### **Métodos HTTP Semânticos**

| Método     | Significado               | Usamos Para               | Não Usamos Para         |
| ---------- | ------------------------- | ------------------------- | ----------------------- |
| **GET**    | "Me dê dados"             | Consultar, listar         | Criar, alterar, deletar |
| **POST**   | "Crie algo novo"          | Criar recursos            | Consultas, atualizações |
| **PUT**    | "Substitua completamente" | Atualizar recurso inteiro | Criar, consultar        |
| **DELETE** | "Remova isso"             | Deletar recursos          | Qualquer outra coisa    |

---

## 🏗️ Arquitetura em Camadas

### **Estrutura Visual**

```
┌─────────────────────┐
│  @RestController    │ ← Endpoints HTTP
│   (controller/)     │   Validação, Status Codes
├─────────────────────┤
│    @Service         │ ← Lógica de Negócio
│   (service/)        │   Regras, Transações
├─────────────────────┤
│   @Repository       │ ← Acesso a Dados
│  (repository/)      │   Queries, Persistência
├─────────────────────┤
│    Database         │ ← MySQL 8.0
│   (MySQL)           │   Armazenamento
└─────────────────────┘
```

### **Organização de Pacotes**

| Pacote                  | Arquivos                    | Responsabilidade        |
| ----------------------- | --------------------------- | ----------------------- |
| **controller/**         | `AtividadeController.java`  | Endpoints REST, HTTP    |
| **service/**            | `AtividadeService.java`     | Interface de negócio    |
| **service/impl/**       | `AtividadeServiceImpl.java` | Implementação da lógica |
| **service/dto/input/**  | `AtividadeInput.java`       | Dados de entrada (DTO)  |
| **service/dto/output/** | `AtividadeOutput.java`      | Dados de saída (DTO)    |
| **model/**              | `Atividade.java`            | Entidade JPA            |
| **repository/**         | `AtividadeRepository.java`  | Acesso aos dados        |
| **mapper/**             | `AtividadeMapper.java`      | Conversão DTO ↔ Entity  |
| **config/**             | `CorsConfig.java`           | Configurações gerais    |

### **Fluxo de Dados**

```
1. Cliente → AtividadeController (HTTP Request)
2. Controller → Validação (Bean Validation)
3. Controller → AtividadeService (Interface)
4. Service → AtividadeServiceImpl (Business Logic)
5. ServiceImpl → AtividadeMapper (DTO → Entity)
6. ServiceImpl → AtividadeRepository (Data Access)
7. Repository → Database (MySQL)
8. Retorno: Database → Repository → Entity → Mapper → DTO → Controller → Cliente
```

---

## 🎯 Design Patterns Utilizados

### **1. Dependency Injection (DI)**

```java
@RestController
public class AtividadeController {
    @Autowired
    private AtividadeService atividadeService; // Injeção automática
}
```

### **2. Data Transfer Object (DTO)**

```java
// Separação clara entre dados de entrada/saída e entidade
AtividadeInput  → Dados de entrada
AtividadeOutput → Dados de saída
Atividade       → Entidade de banco
```

### **3. Repository Pattern**

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    // Abstração do acesso a dados
}
```

### **4. Service Layer Pattern**

```java
@Service
public class AtividadeServiceImpl implements AtividadeService {
    // Centralizaação da lógica de negócio
}
```

---

## 🔒 Validações e Segurança

### **Bean Validation**

```java
@NotBlank(message = "Funcional não pode estar vazio")
@Size(max = 50, message = "Funcional não pode ter mais de 50 caracteres")
private String funcional;
```

### **CORS Configuration**

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    // Configuração para permitir requisições cross-origin
}
```

### **Exception Handling**

```java
// Tratamento centralizado de erros
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseStatus(HttpStatus.NOT_FOUND)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
```

---

## 📊 Benefícios da Arquitetura

### **Escalabilidade**

- ✅ Stateless permite distribuição horizontal
- ✅ Camadas independentes facilitam scaling

### **Manutenibilidade**

- ✅ Separação de responsabilidades
- ✅ Código organizado e testável
- ✅ Baixo acoplamento entre camadas

### **Flexibilidade**

- ✅ Múltiplos clientes (web, mobile, desktop)
- ✅ Interface uniforme e padronizada
- ✅ Fácil integração com outros sistemas

### **Performance**

- ✅ Cache HTTP natural em GET requests
- ✅ Connection pooling para banco de dados
- ✅ Queries otimizadas com JPA

---

## 🚀 Tecnologias e Frameworks

### **Core Stack**

- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - REST controllers
- **Bean Validation** - Validação de entrada

### **Banco de Dados**

- **MySQL 8.0** - SGBD relacional
- **HikariCP** - Connection pool
- **Flyway/Liquibase** - Versionamento (se necessário)

### **Mapeamento e Conversão**

- **MapStruct** - Conversão DTO ↔ Entity
- **Lombok** - Redução de boilerplate

### **Documentação**

- **SpringDoc OpenAPI** - Documentação automática
- **Swagger UI** - Interface visual para testes

---

**🎯 Resultado: API REST profissional, escalável e seguindo as melhores práticas da indústria!**
