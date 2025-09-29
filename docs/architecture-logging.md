# 🏗️ Arquitetura e Logging do Sistema

## 📊 Diagrama da Arquitetura

```plaintext
┌─────────────────┐    HTTP/REST    ┌─────────────────┐    JPA/JDBC    ┌─────────────────┐
│   Frontend      │◄──────────────►│    Backend      │◄─────────────►│   MySQL         │
│   (React)       │   Axios Client  │  (Spring Boot)  │   Hibernate    │   Database      │
│   Port: 5173    │                 │   Port: 8080    │                │   Port: 3307    │
└─────────────────┘                 └─────────────────┘                └─────────────────┘
```

## 🔄 Fluxo de Dados Detalhado

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
    HTTP      Business   Data      Database
   Requests    Logic    Access     Mapping
  ```

### 3. **Camada de Persistência (Database)**

- **Tecnologia**: MySQL 8.0.35
- **Features**:
  - Transactions ACID
  - Auto-increment IDs
  - UTF-8 encoding
  - InnoDB storage engine

---

## 🏛️ Arquitetura Backend Detalhada

### 📦 Estrutura de Pacotes

```
br.com.atividade/
├── controller/          # Camada de apresentação REST
│   └── AtividadeController.java
├── service/            # Lógica de negócio
│   ├── AtividadeService.java (interface)
│   ├── impl/
│   │   └── AtividadeServiceImpl.java
│   └── dto/
│       ├── input/
│       │   └── AtividadeInput.java
│       └── output/
│           └── AtividadeOutput.java
├── repository/         # Camada de acesso a dados
│   └── AtividadeRepository.java
├── model/             # Entidades JPA
│   └── Atividade.java
├── mapper/            # MapStruct mappers
│   └── AtividadeMapper.java
├── config/           # Configurações
│   └── CorsConfig.java
└── AtividadeApplication.java  # Main class
```

### Padrões Arquiteturais Implementados

#### 🎯 **Repository Pattern**

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    List<Atividade> findByFuncional(String funcional);
    List<Atividade> findByCodigoAtividadeContaining(String codigo);
    List<Atividade> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}
```

#### 🔄 **DTO Pattern**

```java
// Input DTO para requisições
public class AtividadeInput {
    @NotBlank
    private String funcional;

    @NotNull
    @Future
    private LocalDateTime dataHora;

    @NotBlank
    @Size(max = 20)
    private String codigoAtividade;
}

// Output DTO para respostas
public class AtividadeOutput {
    private Long idAtividade;
    private String funcional;
    private LocalDateTime dataHora;
    private String codigoAtividade;
    private String descricaoAtividade;
}
```

#### 🗺️ **Mapper Pattern** (MapStruct)

```java
@Mapper(componentModel = "spring")
public interface AtividadeMapper {
    AtividadeOutput toOutput(Atividade atividade);
    Atividade toEntity(AtividadeInput input);
    List<AtividadeOutput> toOutputList(List<Atividade> atividades);
}
```

---

## 📋 Sistema de Logging Avançado

### **Implementação de Logs**

O sistema utiliza **SLF4J** com **Logback** para logging estruturado:

```java
@Slf4j  // Lombok annotation
@RestController
public class AtividadeController {

    @PostMapping
    public ResponseEntity<AtividadeOutput> criarAtividade(@Valid @RequestBody AtividadeInput input) {
        log.info("[Controller] - Recebida requisição para criar atividade: {}", input);

        try {
            AtividadeOutput resultado = atividadeService.criarAtividade(input);
            log.info("[Controller] - Atividade criada com sucesso: {}", resultado);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (IllegalArgumentException e) {
            log.error("[Controller] - Dados inválidos: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {
            log.error("[Controller] - Erro interno ao criar atividade", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno");
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

## 🔒 Configurações de Segurança e CORS

### **CORS Configuration**

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*")  // Desenvolvimento
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

### 🛡️ **Tratamento Global de Exceções**

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        log.warn("[GlobalHandler] - Erro de validação: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception e) {
        log.error("[GlobalHandler] - Erro interno", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor"));
    }
}
```

---

## 🔍 Monitoramento e Observabilidade

### **Métricas com Spring Boot Actuator**

Endpoints de monitoramento disponíveis:

- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Métricas da JVM e aplicação
- `/actuator/loggers` - Configuração dinâmica de logs
- `/actuator/env` - Variáveis de ambiente

### **Estratégia de Logs em Produção**

1. **Centralização**: Logs estruturados em JSON
2. **Correlação**: Request IDs para rastreamento
3. **Retenção**: Logs mantidos por 30 dias
4. **Alertas**: Monitoring em logs de ERROR
5. **Performance**: Async logging para alta performance
