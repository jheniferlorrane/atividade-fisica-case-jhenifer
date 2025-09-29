# Arquitetura e Logging do Sistema

## Diagrama da Arquitetura

```plaintext
┌─────────────────┐    HTTP/REST    ┌─────────────────┐    JPA/JDBC    ┌─────────────────┐
│   Frontend      │◄───────────────►│    Backend      │◄──────────────►│   MySQL         │
│   (React)       │   Axios Client  │  (Spring Boot)  │   Hibernate    │   Database      │
│   Port: 5173    │                 │   Port: 8080    │                │   Port: 3307    │
└─────────────────┘                 └─────────────────┘                └─────────────────┘
```

## Fluxo de Dados Detalhado

### 1. **Camada de Apresentação (Frontend)**

- **Tecnologia**: React 19 + Vite
- **Responsabilidades**:
  - Interface do usuário
  - Validação de formulários (client-side)
  - Comunicação HTTP com a API
  - Formatação de dados para exibição

### 2. **Camada de API (Backend)**

- **Tecnologia**: Spring Boot 3.5.6 + Java 17
- **Estrutura em Camadas**:

  ```
  Controller → Service → Repository → Entity
       ↓         ↓          ↓         ↓
  Requisições  Lógica de  Acesso a   Entidade
    HTTP       Negócio     Dados      JPA
  ```

  **Explicação das Camadas:**

  - **Controller (Requisições HTTP)**: Recebe requests REST, valida dados de entrada, retorna responses JSON
  - **Service (Lógica de Negócio)**: Implementa regras de negócio, validações de domínio, coordena operações
  - **Repository (Acesso a Dados)**: Executa queries no banco, operações CRUD, filtros e buscas
  - **Entity (Entidade JPA)**: Representa tabelas do banco como classes Java, mapeamento objeto-relacional

### 3. **Camada de Persistência (Banco de Dados)**

- **Tecnologia**: MySQL 8.0.35
- **Funcionalidades**:
  - Transações ACID
  - IDs auto incrementais
  - Codificação UTF-8
  - Motor de armazenamento InnoDB

---

## Estrutura de Pacotes do Backend

### **Organização Hierárquica**

```
br.com.atividade/
├── AtividadeApplication.java          # Classe principal Spring Boot
├── config/                            # Configurações da aplicação
│   └── CorsConfig.java               # Configuração CORS para frontend
├── controller/                        # Camada de controle REST
│   └── AtividadeController.java      # Endpoints REST da API
├── sevice/                           # Camada de serviço (lógica de negócio)
│   ├── AtividadeService.java         # Interface do serviço
│   ├── impl/
│   │   └── AtividadeServiceImpl.java # Implementação do serviço
│   └── dto/                          # Data Transfer Objects
│       ├── input/
│       │   └── AtividadeInput.java   # DTO para entrada de dados
│       └── output/
│           └── AtividadeOutput.java  # DTO para saída de dados
├── repository/                        # Camada de acesso a dados
│   └── AtividadeRepository.java      # Interface JPA Repository
├── model/                            # Entidades do domínio
│   └── Atividade.java               # Entidade JPA mapeada para tabela
└── mapper/                           # Conversores de objetos
    └── AtividadeMapper.java         # MapStruct para conversões
```

### **Responsabilidades por Camada**

#### **1. Controller (Camada de Apresentação)**

```java
@RestController
@RequestMapping("/atividades")
public class AtividadeController {
    // Responsabilidades:
    // ✅ Receber requisições HTTP
    // ✅ Validar dados de entrada (@Valid)
    // ✅ Chamar services apropriados
    // ✅ Retornar responses padronizados
    // ✅ Tratamento de exceções HTTP
    // ✅ Logging de requisições
}
```

#### **2. Service (Camada de Negócio)**

```java
@Service
@Transactional
public class AtividadeServiceImpl implements AtividadeService {
    // Responsabilidades:
    // ✅ Implementar regras de negócio
    // ✅ Validações de domínio
    // ✅ Coordenar operações entre repositories
    // ✅ Gerenciar transações (@Transactional)
    // ✅ Logging de operações
    // ✅ Conversão entre DTOs e Entities
}
```

#### **3. Repository (Camada de Dados)**

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    // Responsabilidades:
    // ✅ Operações CRUD básicas (JpaRepository)
    // ✅ Queries customizadas (@Query)
    // ✅ Filtros dinâmicos (JpaSpecificationExecutor)
    // ✅ Validações de existência
}
```

#### **4. Model/Entity (Camada de Domínio)**

```java
@Entity
@Table(name = "atividade")
public class Atividade {
    // Responsabilidades:
    // ✅ Representar entidade de negócio
    // ✅ Mapeamento objeto-relacional (@Entity, @Column)
    // ✅ Definir relacionamentos JPA
    // ✅ Encapsular estado dos dados
}
```

#### **5. DTO (Data Transfer Objects)**

```java
// AtividadeInput.java - Entrada de dados
public class AtividadeInput {
    // Responsabilidades:
    // ✅ Validações de entrada (@NotBlank, @Size)
    // ✅ Contratos de API bem definidos
    // ✅ Isolamento entre camadas externas e domínio
}

// AtividadeOutput.java - Saída de dados
public class AtividadeOutput {
    // Responsabilidades:
    // ✅ Formato de resposta padronizado
    // ✅ Controle de dados expostos
    // ✅ Serialização JSON limpa
}
```

#### **6. Mapper (Camada de Conversão)**

```java
@Mapper(componentModel = "spring")
public interface AtividadeMapper {
    // Responsabilidades:
    // ✅ Conversão Input → Entity
    // ✅ Conversão Entity → Output
    // ✅ Mapeamento automático com MapStruct
    // ✅ Transformações de dados
}
```

#### **7. Config (Camada de Configuração)**

```java
@Configuration
public class CorsConfig {
    // Responsabilidades:
    // ✅ Configurações de segurança (CORS)
    // ✅ Beans de configuração
    // ✅ Profiles de ambiente
}
```

---

## Stack Tecnológica Detalhada

### **Dependências Principais (pom.xml)**

#### **Core Spring Boot**

```xml
<!-- Spring Boot 3.5.6 com Java 17 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- Web MVC, Tomcat embarcado, JSON -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <!-- JPA, Hibernate, Spring Data -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    <!-- Bean Validation (JSR-303) -->
</dependency>
```

#### **Banco de Dados**

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <!-- Driver MySQL 8.0+ -->
</dependency>

<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.6.29.Final</version>
    <!-- ORM Hibernate -->
</dependency>
```

#### **Utilitários de Desenvolvimento**

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <!-- Reduz boilerplate code -->
</dependency>

<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
    <!-- Mapeamento automático de objetos -->
</dependency>
```

#### **Documentação**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.13</version>
    <!-- Swagger/OpenAPI 3.0 -->
</dependency>
```

#### **Testes**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <!-- JUnit 5, Mockito, Spring Test -->
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <!-- Banco em memória para testes -->
</dependency>
```

---

## Configurações de Ambiente

### **application.properties**

```properties
# Configuração da aplicação
spring.application.name=atividade

# Banco de Dados com variáveis Docker
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3307/atividade}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:user}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:pass}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update        # Criação automática de tabelas
spring.jpa.show-sql=false                   # Logs SQL (desabilitado em prod)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.open-in-view=false               # Performance otimizada

# Servidor
server.port=8080
server.address=0.0.0.0                      # Bind para Docker
```

### **application-test.properties**

```properties
# Configuração específica para testes
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop    # Recria BD a cada teste
spring.jpa.show-sql=true                     # Logs SQL em testes
```

---

## Validações e Tratamento de Erros

### **Bean Validation**

```java
@Data
public class AtividadeInput {
    @NotBlank(message = "Funcional não pode estar vazio")
    @Size(max = 50, message = "Funcional não pode ter mais de 50 caracteres")
    private String funcional;

    @NotNull(message = "Data e hora da atividade não podem estar vazias")
    private LocalDateTime dataHora;

    @NotBlank(message = "Código da atividade não pode estar vazio")
    @Size(max = 20, message = "Código da atividade não pode ter mais de 20 caracteres")
    private String codigoAtividade;

    @NotBlank(message = "Descrição da atividade não pode estar vazia")
    @Size(max = 255, message = "Descrição da atividade não pode ter mais de 255 caracteres")
    private String descricaoAtividade;
}
```

### **Tratamento de Exceções no Controller**

```java
@PostMapping
public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput input) {
    try {
        AtividadeOutput atividade = atividadeService.criarAtividade(input);
        return new ResponseEntity<>(atividade, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno");
    }
}
```

---

## Operações de Banco de Dados

### **Repository Customizado**

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>, JpaSpecificationExecutor<Atividade> {

    // Query método por funcional
    @Query("SELECT a FROM Atividade a WHERE a.funcional = :funcional")
    List<Atividade> findByFuncional(@Param("funcional") String funcional);

    // Query com múltiplos filtros
    @Query("SELECT a FROM Atividade a WHERE " +
           "(:funcional IS NULL OR a.funcional = :funcional) AND " +
           "(:codigoAtividade IS NULL OR a.codigoAtividade = :codigoAtividade) AND " +
           "(:descricaoAtividade IS NULL OR LOWER(a.descricaoAtividade) LIKE LOWER(CONCAT('%', :descricaoAtividade, '%')))")
    List<Atividade> findWithFilters(@Param("funcional") String funcional,
                                   @Param("codigoAtividade") String codigoAtividade,
                                   @Param("descricaoAtividade") String descricaoAtividade);

    // Validação de duplicatas
    boolean existsByCodigoAtividadeAndFuncional(String codigoAtividade, String funcional);
}
```

### **Specification Dinâmica para Filtros Avançados**

```java
// No Service - Filtros com data
public List<AtividadeOutput> listarAtividadesComFiltros(String funcional, String codigoAtividade,
                                                        String descricaoAtividade, LocalDate dataInicio, LocalDate dataFim) {
    Specification<Atividade> spec = (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        if (funcional != null && !funcional.trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("funcional"), funcional.trim()));
        }

        if (codigoAtividade != null && !codigoAtividade.trim().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("codigoAtividade"), codigoAtividade.trim()));
        }

        if (descricaoAtividade != null && !descricaoAtividade.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("descricaoAtividade")),
                "%" + descricaoAtividade.toLowerCase() + "%"
            ));
        }

        if (dataInicio != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get("dataHora"), dataInicio.atStartOfDay()
            ));
        }

        if (dataFim != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                root.get("dataHora"), dataFim.atTime(23, 59, 59)
            ));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    List<Atividade> atividades = atividadeRepository.findAll(spec);
    return atividadeMapper.toOutputList(atividades);
}
```

### **Transações e Performance**

```java
@Service
@Transactional                    // Transações por padrão
public class AtividadeServiceImpl {

    @Transactional(readOnly = true)  // Otimização para leitura
    public List<AtividadeOutput> listarTodasAtividades() {
        // Operação somente leitura
    }

    @Transactional(rollbackFor = Exception.class)  // Rollback explícito
    public AtividadeOutput criarAtividade(AtividadeInput input) {
        // Operação com possibilidade de rollback
    }
}
```

---

## Mapeamento de Objetos

### **MapStruct - Conversões Automáticas**

```java
@Mapper(componentModel = "spring")
public interface AtividadeMapper {

    // Input → Entity (ignorando ID)
    @Mapping(target = "idAtividade", ignore = true)
    Atividade toEntity(AtividadeInput input);

    // Entity → Output (incluindo ID)
    @Mapping(target = "idAtividade", source = "idAtividade")
    AtividadeOutput toOutput(Atividade entity);

    // Lista de conversões
    List<AtividadeOutput> toOutputList(List<Atividade> entities);

    // Atualização de entidade existente
    @Mapping(target = "idAtividade", ignore = true)
    void updateEntityFromInput(AtividadeInput input, @MappingTarget Atividade entity);
}
```

### **Vantagens do MapStruct**

- ✅ **Performance**: Geração de código em tempo de compilação
- ✅ **Type Safety**: Verificação de tipos estaticamente
- ✅ **Manutenção**: Reduz código boilerplate
- ✅ **Debugging**: Código gerado é legível

---

## Configuração CORS

### **CorsConfig para Frontend**

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");        // Permitir qualquer origem
        config.addAllowedHeader("*");               // Permitir qualquer header
        config.addAllowedMethod("*");               // Permitir qualquer método HTTP

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
```

### **Configuração para Produção**

```java
// Em produção, seria mais restritivo:
config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://app.empresa.com"));
config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
```

> **Sobre o Motor InnoDB**: É o "mecanismo interno" padrão do MySQL que gerencia como os dados são armazenados e recuperados. InnoDB oferece transações ACID, bloqueio por linha (permitindo múltiplos usuários simultâneos), chaves estrangeiras e recuperação automática de falhas. É o motor ideal para aplicações web modernas por sua confiabilidade e performance em operações CRUD frequentes.

---

## Padrões Arquiteturais Implementados

#### **O que são Design Patterns?**

**Design Patterns** (Padrões de Projeto) são **soluções reutilizáveis** para problemas comuns no desenvolvimento de software. São como "receitas testadas" que outros programadores já criaram e validaram ao longo dos anos.

**Analogia Simples:**
Imagine construir uma casa - ao invés de inventar como fazer uma fundação resistente, você usa concreto armado (solução testada). Design Patterns funcionam da mesma forma: ao invés de cada programador inventar sua própria solução, usamos padrões que já provaram funcionar bem.

**Definição Técnica:**

> _"Design Patterns descrevem soluções elegantes e reutilizáveis para problemas recorrentes no desenvolvimento de software orientado a objetos"_

**Por que usar Design Patterns?**

1. **Reutilização**: Soluções já testadas por milhares de desenvolvedores
2. **Comunicação**: Linguagem comum entre programadores
3. **Estrutura**: Código mais organizado e fácil de entender
4. **Manutenção**: Mudanças futuras ficam mais simples
5. **Qualidade**: Menos bugs, mais confiabilidade

**Categorias Principais:**

- **Criacionais**: Como criar objetos (Factory, Builder, Singleton)
- **Estruturais**: Como organizar classes (Repository, DTO, Facade)
- **Comportamentais**: Como classes interagem (Strategy, Observer, Template Method)

**Exemplo Prático:**

**❌ Sem Repository Pattern:**

```java
// Código problemático - SQL direto no controller
public class AtividadeController {
    public List<Atividade> buscarTodas() {
        Connection conn = DriverManager.getConnection(...);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM atividade");
        // ... conversão manual, código repetitivo
    }
}
```

**✅ Com Repository Pattern:**

```java
// Código limpo e organizado
@RestController
public class AtividadeController {
    private final AtividadeRepository repository;

    public List<Atividade> buscarTodas() {
        return repository.findAll(); // Simples e claro!
    }
}
```

---

**11 design patterns** implementados neste projeto:

#### **Repository Pattern**

**O que faz**: Encapsula toda a lógica de acesso aos dados, criando uma camada de abstração entre a lógica de negócio e o banco de dados. É como ter um "bibliotecário" que sabe exatamente onde encontrar cada informação.

**Função específica**:

- Centraliza todas as operações de banco de dados (CRUD)
- Permite trocar o banco de dados sem alterar a lógica de negócio
- Facilita testes (pode usar repositórios "falsos" para testes)
- Padroniza consultas e operações

**Benefícios práticos**:
✅ **Manutenibilidade**: Mudanças no banco ficam isoladas
✅ **Testabilidade**: Fácil de "mockar" em testes unitários
✅ **Reutilização**: Consultas podem ser reutilizadas
✅ **Performance**: Spring otimiza automaticamente as queries

**Implementação**: Interface que estende `JpaRepository` e `JpaSpecificationExecutor`

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>, JpaSpecificationExecutor<Atividade> {

    @Query("SELECT a FROM Atividade a WHERE a.funcional = :funcional")
    List<Atividade> findByFuncional(@Param("funcional") String funcional);

    @Query("SELECT a FROM Atividade a WHERE " +
           "(:funcional IS NULL OR a.funcional = :funcional) AND " +
           "(:codigoAtividade IS NULL OR a.codigoAtividade = :codigoAtividade) AND " +
           "(:descricaoAtividade IS NULL OR LOWER(a.descricaoAtividade) LIKE LOWER(CONCAT('%', :descricaoAtividade, '%')))")
    List<Atividade> findWithFilters(@Param("funcional") String funcional,
                                   @Param("codigoAtividade") String codigoAtividade,
                                   @Param("descricaoAtividade") String descricaoAtividade);

    boolean existsByCodigoAtividadeAndFuncional(String codigoAtividade, String funcional);
}
```

#### **DTO Pattern (Data Transfer Object)**

**O que faz**: Cria objetos específicos para transportar dados entre diferentes camadas da aplicação, sem expor a estrutura interna das entidades. É como ter "envelopes" específicos para cada tipo de comunicação.

**Função específica**:

- Controla exatamente quais dados são enviados/recebidos
- Protege dados sensíveis (senhas, IDs internos)
- Permite validações específicas por operação
- Desacopla API da estrutura do banco de dados

**Benefícios práticos**:
✅ **Segurança**: Nunca expõe dados internos por acidente
✅ **Versionamento**: Mudanças na entidade não quebram a API
✅ **Performance**: Transfere apenas dados necessários
✅ **Validação**: Regras específicas para entrada/saída

**Exemplo prático**:

- `AtividadeInput`: Só aceita dados necessários para criação
- `AtividadeOutput`: Só retorna dados seguros para o cliente

**Implementação**: Objetos específicos para entrada e saída de dados

```java
// DTO de entrada para requisições
@Data
public class AtividadeInput {

    @NotBlank(message = "Funcional não pode estar vazio")
    @Size(max = 50, message = "Funcional não pode ter mais de 50 caracteres")
    private String funcional;

    @NotNull(message = "Data e hora da atividade não podem estar vazias")
    private LocalDateTime dataHora;

    @NotBlank(message = "Código da atividade não pode estar vazio")
    @Size(max = 20, message = "Código da atividade não pode ter mais de 20 caracteres")
    private String codigoAtividade;

    @NotBlank(message = "Descrição da atividade não pode estar vazia")
    @Size(max = 255, message = "Descrição da atividade não pode ter mais de 255 caracteres")
    private String descricaoAtividade;
}

// DTO de saída para respostas
@Data
public class AtividadeOutput {
    private Long idAtividade;
    private String funcional;
    private LocalDateTime dataHora;
    private String codigoAtividade;
    private String descricaoAtividade;
}
```

#### **Mapper Pattern** (MapStruct)

**O que faz**: Automatiza a conversão entre diferentes tipos de objetos (DTO ↔ Entity), eliminando código repetitivo e reduzindo erros de conversão manual. É como ter um "tradutor automático" entre objetos.

**Função específica**:

- Converte automaticamente Entity → DTO e vice-versa
- Mapeia campos com nomes diferentes
- Aplica transformações durante a conversão
- Gera código otimizado em tempo de compilação

**Benefícios práticos**:
✅ **Produtividade**: Zero código manual de conversão
✅ **Performance**: Código gerado é otimizado (sem reflection)
✅ **Confiabilidade**: Menos erros de conversão manual
✅ **Manutenibilidade**: Mudanças nos objetos são detectadas automaticamente

**Exemplo prático**:

```java
// Sem Mapper (código manual repetitivo)
AtividadeOutput output = new AtividadeOutput();
output.setIdAtividade(entity.getId());
output.setFuncional(entity.getFuncional());
// ... 10 linhas de código repetitivo

// Com Mapper (uma linha)
AtividadeOutput output = mapper.toOutput(entity);
```

**Implementação**: Interface para conversão automática entre objetos

```java
@Mapper(componentModel = "spring")
public interface AtividadeMapper {

    @Mapping(target = "idAtividade", ignore = true)
    Atividade toEntity(AtividadeInput input);

    @Mapping(target = "idAtividade", source = "idAtividade")
    AtividadeOutput toOutput(Atividade entity);

    List<AtividadeOutput> toOutputList(List<Atividade> entities);

    @Mapping(target = "idAtividade", ignore = true)
    void updateEntityFromInput(AtividadeInput input, @MappingTarget Atividade entity);
}
```

#### **Dependency Injection Pattern**

**O que faz**: Remove a responsabilidade das classes de criar suas próprias dependências. Ao invés da classe "procurar" o que precisa, o Spring "entrega" tudo pronto. É como ter um "garçom" que traz tudo que você precisa sem você pedir.

**Função específica**:

- Spring cria e gerencia todas as dependências automaticamente
- Classes recebem dependências prontas via construtor
- Elimina código de inicialização manual
- Permite diferentes implementações da mesma interface

**Benefícios práticos**:
✅ **Baixo Acoplamento**: Classes não conhecem implementações específicas
✅ **Testabilidade**: Fácil injetar "mocks" em testes
✅ **Flexibilidade**: Trocar implementações sem alterar código
✅ **Manutenibilidade**: Dependências centralizadas no Spring

**Exemplo prático**:

```java
// ❌ Sem DI (alto acoplamento)
public class AtividadeService {
    private AtividadeRepository repo = new AtividadeRepositoryImpl(); // Rígido!
}

// ✅ Com DI (baixo acoplamento)
@Service
public class AtividadeService {
    private final AtividadeRepository repo; // Spring injeta automaticamente

    // Lombok gera este construtor
    public AtividadeService(AtividadeRepository repo) {
        this.repo = repo;
    }
}
```

**Implementação**: Injeção via construtor com `@RequiredArgsConstructor`

```java
@Slf4j
@Service
@RequiredArgsConstructor  // Lombok gera construtor para campos final
@Transactional
public class AtividadeServiceImpl implements AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final AtividadeMapper atividadeMapper;

    @Override
    public AtividadeOutput criarAtividade(AtividadeInput atividadeInput) {
        log.info("[ServiceImpl] - Iniciando criação de atividade para funcional: {}", atividadeInput.getFuncional());

        validarAtividadeInput(atividadeInput);

        Atividade atividade = atividadeMapper.toEntity(atividadeInput);

        log.debug("[DB] - Iniciando persistência da atividade no banco");
        Atividade atividadeSalva = atividadeRepository.save(atividade);
        log.info("[DB] - Atividade persistida com sucesso - ID: {}, Tabela: atividade", atividadeSalva.getIdAtividade());

        return atividadeMapper.toOutput(atividadeSalva);
    }
}
```

#### **Strategy Pattern**

**O que faz**: Define uma família de algoritmos/comportamentos, encapsula cada um e os torna intercambiáveis. É como ter "várias estratégias" para resolver o mesmo problema, podendo escolher qual usar.

**Função específica**:

- Define um contrato (interface) para um comportamento
- Permite múltiplas implementações do mesmo comportamento
- Cliente usa a interface, não conhece implementação específica
- Facilita adicionar novas estratégias sem alterar código existente

**Benefícios práticos**:
✅ **Extensibilidade**: Fácil adicionar novas implementações
✅ **Flexibilidade**: Trocar comportamento em runtime
✅ **Testabilidade**: Cada estratégia pode ser testada isoladamente
✅ **Manutenção**: Mudanças em uma estratégia não afetam outras

**Exemplo prático no projeto**:

- Interface `AtividadeService` define o "contrato"
- `AtividadeServiceImpl` é a implementação atual
- Futuro: Poderia ter `AtividadeServiceCache`, `AtividadeServiceBatch`
- Controller usa interface, não implementação específica

**Implementação**: Interface de serviço com múltiplas implementações possíveis

```java
public interface AtividadeService {

    AtividadeOutput criarAtividade(AtividadeInput atividadeInput);

    List<AtividadeOutput> listarTodasAtividades();

    List<AtividadeOutput> listarAtividadesPorFuncional(String funcional);

    List<AtividadeOutput> listarAtividadesComFiltros(String funcional, String codigoAtividade,
                                                    String descricaoAtividade, LocalDate dataInicio, LocalDate dataFim);

    Optional<AtividadeOutput> buscarAtividadePorId(Long id);

    AtividadeOutput atualizarAtividade(Long id, AtividadeInput atividadeInput);

    void deletarAtividade(Long id);
}

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AtividadeServiceImpl implements AtividadeService {
    // Implementação específica da estratégia
}
```

#### **Template Method Pattern**

**O que faz**: Define o "esqueleto" de um algoritmo, deixando algumas etapas específicas para as subclasses implementarem. Spring define o "template" de processamento HTTP, você só implementa a lógica específica.

**Função específica**:

- Spring gerencia todo ciclo de vida da requisição HTTP
- Intercepta, valida, converte, trata erros automaticamente
- Você só implementa a lógica específica do negócio
- Padroniza o processamento de todas as requisições

**Benefícios práticos**:
✅ **Produtividade**: Não precisa implementar infraestrutura HTTP
✅ **Padronização**: Todas requisições seguem mesmo padrão
✅ **Confiabilidade**: Spring já testou todo o "template"
✅ **Manutenibilidade**: Mudanças no template beneficiam todos

**Como funciona na prática**:

1. **Spring recebe** requisição HTTP
2. **Spring valida** JSON e parâmetros (@Valid)
3. **Spring converte** JSON para objeto Java
4. **Você implementa** apenas a lógica de negócio
5. **Spring converte** resposta para JSON
6. **Spring retorna** response com status correto

**Implementação**: Spring Framework define template para controllers REST

```java
@Slf4j
@RestController
@RequestMapping("/atividades")
@Validated
public class AtividadeController {
    // Spring define o template de processamento HTTP
    // Nós implementamos apenas os métodos específicos

    @PostMapping
    public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput atividadeInput) {
        log.info("[Controller] - Recebida requisição para criar atividade: {}", atividadeInput);
        try {
            AtividadeOutput atividade = atividadeService.criarAtividade(atividadeInput);
            log.info("[Controller] - Atividade criada com sucesso: {}", atividade);
            return new ResponseEntity<>(atividade, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("[Controller] - Dados inválidos: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception erro) {
            log.error("[Controller] - Erro interno ao criar atividade", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
        // Template: recebe request → valida → processa → retorna response
    }
}
```

#### **Facade Pattern**

**O que faz**: Fornece uma interface simples para um subsistema complexo. O Controller atua como uma "fachada" que esconde toda complexidade das camadas internas (Service, Repository, validações, etc.).

**Função específica**:

- Simplifica acesso às operações complexas do sistema
- Coordena múltiplas operações em uma única chamada
- Esconde detalhes de implementação do cliente
- Reduz dependências entre cliente e subsistemas

**Benefícios práticos**:
✅ **Simplicidade**: Cliente vê apenas interface limpa
✅ **Desacoplamento**: Mudanças internas não afetam cliente
✅ **Organização**: Lógica complexa fica encapsulada
✅ **Reutilização**: Facade pode ser usada por múltiplos clientes

**Exemplo prático**:

```java
// Cliente (Frontend) vê apenas:
POST /atividades { "funcional": "EMP001", ... }

// Mas internamente o Controller coordena:
// 1. Validação de dados
// 2. Conversão DTO → Entity
// 3. Regras de negócio
// 4. Persistência no banco
// 5. Conversão Entity → DTO
// 6. Response HTTP
```

**Implementação**: Controller atua como fachada para a camada de serviço

```java
@Slf4j
@RestController
@RequestMapping("/atividades")
@Validated
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;  // Fachada para lógica complexa

    @PostMapping
    public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput atividadeInput) {
        // Simplifica o acesso às operações complexas do service
        log.info("[Controller] - Recebida requisição para criar atividade: {}", atividadeInput);
        try {
            AtividadeOutput atividade = atividadeService.criarAtividade(atividadeInput);
            log.info("[Controller] - Atividade criada com sucesso: {}", atividade);
            return new ResponseEntity<>(atividade, HttpStatus.CREATED);
        } catch (Exception erro) {
            log.error("[Controller] - Erro interno ao criar atividade", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @GetMapping
    public ResponseEntity<List<AtividadeOutput>> listarAtividades(
            @RequestParam(required = false) String funcional,
            @RequestParam(required = false) String codigoAtividade,
            @RequestParam(required = false) String descricaoAtividade) {

        List<AtividadeOutput> atividades = atividadeService.listarAtividadesComFiltros(
            funcional, codigoAtividade, descricaoAtividade, null, null);
        return ResponseEntity.ok(atividades);
    }
}
```

#### **Factory Pattern**

**O que faz**: Delega a criação de objetos para "fábricas" especializadas, ao invés de usar `new` diretamente. Spring é uma grande "fábrica" que cria e gerencia todos os objetos da aplicação.

**Função específica**:

- Spring Container cria todas as instâncias (@Component, @Service, @Repository)
- MapStruct gera "fábricas" para criar mappers
- JPA cria "fábricas" para entidades e proxies
- Centraliza e padroniza criação de objetos

**Benefícios práticos**:
✅ **Gerenciamento**: Spring cuida de toda criação/destruição
✅ **Singleton**: Uma instância por tipo (economia de memória)
✅ **Configuração**: Objetos já vêm configurados
✅ **Lifecycle**: Spring gerencia todo ciclo de vida

**Exemplo prático**:

```java
// ❌ Sem Factory (você cria manualmente)
AtividadeMapper mapper = new AtividadeMapperImpl();
AtividadeService service = new AtividadeServiceImpl(repository, mapper);

// ✅ Com Factory (Spring cria tudo)
@Autowired // Spring "fabrica" e injeta automaticamente
private AtividadeService service; // Já vem configurado e pronto
```

**Implementação**: Spring usa factories para criação de beans

```java
@Component  // Spring Factory cria e gerencia instâncias
@Mapper(componentModel = "spring")  // MapStruct Factory para mappers
public interface AtividadeMapper {

    @Mapping(target = "idAtividade", ignore = true)
    Atividade toEntity(AtividadeInput input);

    AtividadeOutput toOutput(Atividade entity);

    List<AtividadeOutput> toOutputList(List<Atividade> entities);
}

@Data  // Lombok Factory para getters/setters
@Entity  // JPA EntityManager factory para entidades
@Table(name = "atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long idAtividade;

    @Column(name = "codigo_atividade")
    private String codigoAtividade;

    @Column(name = "descricao_atividade")
    private String descricaoAtividade;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "funcional")
    private String funcional;
}
```

#### **Proxy Pattern**

**O que faz**: Cria um "representante" (proxy) que intercepta chamadas para adicionar funcionalidades extras, sem alterar o código original. É como ter um "intermediário" que adiciona serviços.

**Função específica**:

- **Transações**: Spring intercepta métodos @Transactional para gerenciar transações
- **Segurança**: Intercepta para verificar permissões
- **Cache**: Intercepta para armazenar resultados em cache
- **Logs**: Intercepta para adicionar logs automaticamente

**Benefícios práticos**:
✅ **Transparência**: Funcionalidades extras sem alterar código
✅ **Reutilização**: Mesmo proxy serve múltiplas classes
✅ **Flexibilidade**: Liga/desliga funcionalidades via configuração
✅ **Separação**: Lógica de negócio separada de infraestrutura

**Exemplo prático com @Transactional**:

```java
@Service
@Transactional // Spring cria PROXY desta classe
public class AtividadeServiceImpl {
    public void salvarAtividade(Atividade atividade) {
        // Seu código simples
        repository.save(atividade);
    }
}

// Spring intercepta assim:
// 1. Inicia transação
// 2. Chama seu método
// 3. Se sucesso: commit
// 4. Se erro: rollback
```

**Implementação**: JPA Repositories são proxies, transações são interceptadas

```java
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional  // Spring cria PROXY desta classe para interceptar e gerenciar transações
public class AtividadeServiceImpl implements AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final AtividadeMapper atividadeMapper;

    @Override
    public AtividadeOutput criarAtividade(AtividadeInput atividadeInput) {
        // Seu código simples - Spring intercepta automaticamente
        log.info("[ServiceImpl] - Iniciando criação de atividade");

        Atividade atividade = atividadeMapper.toEntity(atividadeInput);
        Atividade atividadeSalva = atividadeRepository.save(atividade);

        log.info("[ServiceImpl] - Atividade salva com ID: {}", atividadeSalva.getIdAtividade());
        return atividadeMapper.toOutput(atividadeSalva);
    }
    // Métodos são interceptados por proxies transacionais
    // Spring intercepta assim:
    // 1. Inicia transação
    // 2. Chama seu método
    // 3. Se sucesso: commit
    // 4. Se erro: rollback
}
```

#### **Builder Pattern**

**O que faz**: Constrói objetos complexos passo a passo, permitindo diferentes representações do mesmo tipo de objeto. É como "montar" um objeto parte por parte, de forma flexível.

**Função específica**:

- Lombok gera builders automáticos para entidades (@Data)
- JPA Criteria API usa builder para construir consultas complexas
- Permite criar objetos de forma fluente e legível
- Facilita criação de objetos com muitos parâmetros opcionais

**Benefícios práticos**:
✅ **Legibilidade**: Código mais claro e autoexplicativo
✅ **Flexibilidade**: Pode omitir/incluir campos conforme necessário
✅ **Imutabilidade**: Objetos criados não mudam depois
✅ **Validação**: Pode validar durante construção

**Exemplo prático**:

```java
// ❌ Construtor tradicional (confuso com muitos parâmetros)
Atividade atividade = new Atividade(null, "EMP001", LocalDateTime.now(), "RUN", "Corrida");

// ✅ Builder pattern (claro e flexível)
Atividade atividade = Atividade.builder()
    .funcional("EMP001")
    .dataHora(LocalDateTime.now())
    .codigoAtividade("RUN")
    .descricaoAtividade("Corrida")
    .build();

// Criteria API também usa Builder
Specification<Atividade> spec = (root, query, cb) -> {
    return cb.and(  // Constrói predicados passo a passo
        cb.equal(root.get("funcional"), "EMP001"),
        cb.like(root.get("codigoAtividade"), "RUN%")
    );
};
```

**Implementação**: Lombok `@Data` gera builder implícito, QueryBuilder para consultas

```java
@Data  // Lombok gera padrão builder automaticamente
@Entity
@Table(name = "atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long idAtividade;

    @Column(name = "codigo_atividade")
    private String codigoAtividade;

    @Column(name = "descricao_atividade")
    private String descricaoAtividade;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "funcional")
    private String funcional;
    // Permite: Atividade.builder().funcional("EMP001").codigoAtividade("RUN").build()
}

// Criteria API usa Builder Pattern no projeto
public List<AtividadeOutput> listarAtividadesComFiltros(String funcional, String codigoAtividade,
                                                       String descricaoAtividade, LocalDate dataInicio, LocalDate dataFim) {
    return atividadeRepository.findAll((root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        if (funcional != null && !funcional.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("funcional"), funcional));
        }

        if (codigoAtividade != null && !codigoAtividade.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("codigoAtividade"), codigoAtividade));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }).stream().map(atividadeMapper::toOutput).collect(Collectors.toList());
}
```

#### **Specification Pattern**

**O que faz**: Encapsula regras de negócio/consultas em objetos reutilizáveis que podem ser combinados. É como ter "filtros inteligentes" que você pode combinar de diferentes formas.

**Função específica**:

- Cria consultas dinâmicas baseadas em critérios
- Permite combinar múltiplos filtros (AND, OR, NOT)
- Reutiliza lógica de consulta em diferentes contextos
- Mantém consultas complexas organizadas e testáveis

**Benefícios práticos**:
✅ **Flexibilidade**: Consultas dinâmicas baseadas em filtros do usuário
✅ **Reutilização**: Mesmos critérios em diferentes consultas
✅ **Testabilidade**: Cada specification pode ser testada isoladamente
✅ **Manutenibilidade**: Lógica de consulta organizada e clara

**Exemplo prático**:

```java
// Specifications reutilizáveis
Specification<Atividade> porFuncional(String funcional) {
    return (root, query, cb) -> cb.equal(root.get("funcional"), funcional);
}

Specification<Atividade> porPeriodo(LocalDateTime inicio, LocalDateTime fim) {
    return (root, query, cb) -> cb.between(root.get("dataHora"), inicio, fim);
}

// Combinando specifications dinamicamente
Specification<Atividade> filtro = Specification.where(null);

if (funcional != null) {
    filtro = filtro.and(porFuncional(funcional));
}
if (inicio != null && fim != null) {
    filtro = filtro.and(porPeriodo(inicio, fim));
}

// Uma query dinâmica baseada nos filtros
List<Atividade> resultado = repository.findAll(filtro);
```

**Implementação**: JPA Criteria API para consultas dinâmicas

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long>,
                                           JpaSpecificationExecutor<Atividade> {
    // Permite consultas com Specification para filtros complexos

    @Query("SELECT a FROM Atividade a WHERE a.funcional = :funcional")
    List<Atividade> findByFuncional(@Param("funcional") String funcional);
}

// Uso real no projeto:
@Override
@Transactional(readOnly = true)
public List<AtividadeOutput> listarAtividadesComFiltros(String funcional, String codigoAtividade,
                                                       String descricaoAtividade, LocalDate dataInicio, LocalDate dataFim) {
    log.info("[ServiceImpl] - Listando atividades com filtros - Funcional: {}, CodigoAtividade: {}", funcional, codigoAtividade);

    return atividadeRepository.findAll((root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        if (funcional != null && !funcional.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("funcional"), funcional));
        }

        if (codigoAtividade != null && !codigoAtividade.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("codigoAtividade"), codigoAtividade));
        }

        if (descricaoAtividade != null && !descricaoAtividade.isEmpty()) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("descricaoAtividade")),
                "%" + descricaoAtividade.toLowerCase() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }).stream().map(atividadeMapper::toOutput).collect(Collectors.toList());
}
```

#### **Resumo dos Patterns Identificados**

| Pattern                  | Onde Está                  | Benefício                             |
| ------------------------ | -------------------------- | ------------------------------------- |
| **Repository**           | `AtividadeRepository`      | Abstrai acesso aos dados              |
| **DTO**                  | `AtividadeInput/Output`    | Controla dados entre camadas          |
| **Mapper**               | `AtividadeMapper`          | Converte objetos automaticamente      |
| **Dependency Injection** | `@RequiredArgsConstructor` | Baixo acoplamento, fácil teste        |
| **Strategy**             | `AtividadeService`         | Múltiplas implementações              |
| **Template Method**      | Spring MVC                 | Padroniza processamento HTTP          |
| **Facade**               | `AtividadeController`      | Simplifica acesso ao sistema          |
| **Factory**              | Spring Container           | Criação e gerenciamento de objetos    |
| **Proxy**                | `@Transactional`           | Intercepta e adiciona funcionalidades |
| **Builder**              | Lombok `@Data`             | Construção fluida de objetos          |
| **Specification**        | JPA Criteria               | Consultas dinâmicas e reutilizáveis   |

> **Total: 11 Design Patterns**

---

## Sistema de Logging Avançado

### **Implementação de Logs**

O sistema utiliza **SLF4J** com **Logback** para logging estruturado:

```java
@Slf4j  // Anotação do Lombok
@RestController
@RequestMapping("/atividades")
@Validated
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    @PostMapping
    public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput atividadeInput) {
        log.info("[Controller] - Recebida requisição para criar atividade: {}", atividadeInput);

        try {
            AtividadeOutput atividade = atividadeService.criarAtividade(atividadeInput);
            log.info("[Controller] - Atividade criada com sucesso: {}", atividade);
            return new ResponseEntity<>(atividade, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            log.error("[Controller] - Dados inválidos: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception erro) {
            log.error("[Controller] - Erro interno ao criar atividade", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeOutput> buscarAtividadePorId(@PathVariable Long id) {
        log.info("[Controller] - Buscando atividade com ID: {}", id);
        try {
            Optional<AtividadeOutput> atividade = atividadeService.buscarAtividadePorId(id);
            if (atividade.isPresent()) {
                log.info("[Controller] - Atividade encontrada: {}", atividade.get());
                return ResponseEntity.ok(atividade.get());
            } else {
                log.warn("[Controller] - Atividade com ID {} não encontrada", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atividade não encontrada");
            }
        } catch (Exception erro) {
            log.error("[Controller] - Erro ao buscar atividade por ID", erro);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        }
    }
}
```

### **Níveis de Log e Uso**

| Nível     | Uso                                         | Exemplo                                                           |
| --------- | ------------------------------------------- | ----------------------------------------------------------------- |
| **INFO**  | Operações normais, início/fim de processos  | `log.info("[ServiceImpl] - Iniciando busca por funcional: {}")`   |
| **WARN**  | Situações suspeitas, mas não críticas       | `log.warn("[Controller] - Atividade com ID {} não encontrada")`   |
| **ERROR** | Erros e exceções                            | `log.error("[ServiceImpl] - Erro ao salvar no banco", exception)` |
| **DEBUG** | Informações detalhadas para desenvolvimento | `log.debug("[Repository] - Query executada: {}")`                 |

### **Padrão de Tags por Camada**

- **[Controller]**: Entrada/saída HTTP, validações de request
- **[ServiceImpl]**: Lógica de negócio, validações de domínio
- **[Repository]**: Operações de banco, queries customizadas
- **[Config]**: Inicialização, configurações do sistema

### **Exemplo de Fluxo Completo de Logs**

```log
2025-09-28T14:22:10.123-03:00  INFO [Controller] - Recebida requisição para criar atividade:
AtividadeInput(funcional=EMP001, dataHora=2025-09-30T08:00, codigoAtividade=RUNNING, descricaoAtividade=Corrida matinal)

2025-09-28T14:22:10.125-03:00  INFO [ServiceImpl] - Iniciando validação de dados para funcional: EMP001

2025-09-28T14:22:10.127-03:00 DEBUG [Repository] - Verificando duplicatas para funcional EMP001 na data 2025-09-30T08:00

2025-09-28T14:22:10.145-03:00  INFO [ServiceImpl] - Salvando atividade no banco de dados

2025-09-28T14:22:10.187-03:00  INFO [ServiceImpl] - Atividade criada com sucesso - ID: 15, Funcional: EMP001

2025-09-28T14:22:10.189-03:00  INFO [Controller] - Retornando atividade criada:
AtividadeOutput(idAtividade=15, funcional=EMP001, dataHora=2025-09-30T08:00, codigoAtividade=RUNNING, descricaoAtividade=Corrida matinal)
```

---

## Configurações de Segurança e CORS

### **Configuração CORS (Cross-Origin Resource Sharing)**

**O que é CORS?**
CORS significa "Cross-Origin Resource Sharing" (Compartilhamento de recursos entre origens diferentes). É uma segurança do navegador que impede sites maliciosos de acessar dados de outros sites.

**Por que precisamos dele?**
Por padrão, navegadores bloqueiam requisições entre domínios diferentes (política chamada "Same-Origin Policy"). Em nosso caso:

- Frontend roda em `http://localhost:5173` (React + Vite)
- Backend roda em `http://localhost:8080` (Spring Boot)
- Navegador bloqueia comunicação entre eles sem CORS

**Nossa configuração:**

```java
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // ⚠️ CONFIGURAÇÃO MUITO PERMISSIVA - APENAS PARA DESENVOLVIMENTO
        config.setAllowCredentials(true);    // Permite cookies/autenticação
        config.addAllowedOrigin("*");        // QUALQUER site pode acessar
        config.addAllowedHeader("*");        // QUALQUER cabeçalho é aceito
        config.addAllowedMethod("*");        // TODOS os métodos HTTP

        fonte.registerCorsConfiguration("/**", config);  // Para todas as rotas
        return new CorsFilter(fonte);
    }
}
```

**⚠️ IMPORTANTE - Segurança:**

- Esta configuração aceita requisições de **qualquer domínio**
- É conveniente para desenvolvimento, mas **PERIGOSA em produção**
- Em produção, especifique apenas domínios confiáveis:

```java
// Exemplo seguro para produção:
config.setAllowedOrigins(Arrays.asList("https://meuapp.com", "https://www.meuapp.com"));
```

**Como funciona:**

1. Frontend faz requisição para backend
2. CORS Filter verifica se o domínio de origem tem permissão
3. Se permitido: requisição prossegue normalmente
4. Se negado: navegador bloqueia e exibe erro de CORS

### **Tratamento de Exceções**

O projeto **NÃO usa** `@ControllerAdvice` (tratamento global). Cada controller trata suas próprias exceções diretamente:

```java
@PostMapping
public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput atividadeInput) {
    log.info("[Controller] - Recebida requisição para criar atividade: {}", atividadeInput);
    try {
        AtividadeOutput atividade = atividadeService.criarAtividade(atividadeInput);
        log.info("[Controller] - Atividade criada com sucesso: {}", atividade);
        return new ResponseEntity<>(atividade, HttpStatus.CREATED);

    } catch (IllegalArgumentException e) {
        // Trata erros de validação
        log.error("[Controller] - Dados inválidos: {}", e.getMessage());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

    } catch (Exception erro) {
        // Trata erros inesperados
        log.error("[Controller] - Erro interno ao criar atividade", erro);
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }
}
```

**Como funciona:**

- **ResponseStatusException**: Classe do Spring que converte exceções em respostas HTTP
- **HttpStatus.BAD_REQUEST**: Retorna erro 400 (pedido inválido)
- **HttpStatus.NOT_FOUND**: Retorna erro 404 (não encontrado)
- **HttpStatus.INTERNAL_SERVER_ERROR**: Retorna erro 500 (erro interno)
- **Logs estruturados**: Cada erro é logado com detalhes para debugging

**Vantagens desta abordagem:**
✅ **Controle específico**: Cada endpoint trata erros do seu jeito
✅ **Logs detalhados**: Erros são logados no local exato onde acontecem
✅ **Simplicidade**: Não precisa de classes extras para tratamento global
throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");

---

## Desenvolvedora

**Jhenifer Lorrane**

- GitHub: [@jheniferlorrane](https://github.com/jheniferlorrane)
- LinkedIn: [Jhenifer Lorrane](https://www.linkedin.com/in/jheniferanacleto/)

---

## Versão

**v1.0.0** – Case Técnico Completo

- Requisitos 100% atendidos
- Funcionalidades extras implementadas
