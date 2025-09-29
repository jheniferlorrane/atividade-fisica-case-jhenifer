# API REST - Endpoints, Testes e Validações

## Visão Geral da API

Esta API RESTful segue os padrões HTTP e oferece endpoints para gerenciamento completo (CRUD) de atividades físicas de funcionários. A API é stateless, utiliza códigos de status HTTP apropriados e retorna dados em formato JSON.

> **Para Iniciantes**:
>
> - **Stateless**: A API não "lembra" de informações entre uma requisição e outra. É como se cada pergunta que você faz fosse a primeira vez
> - **CRUD**: **C**reate (Criar), **R**ead (Ler), **U**pdate (Atualizar), **D**elete (Excluir) - as 4 operações básicas com dados
> - **Endpoint**: Cada "porta de entrada" específica da API (como /atividades)

### **Características da API**

- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`
- **Encoding**: UTF-8
- **Versionamento**: Implícito (v1)
- **Documentação**: Swagger/OpenAPI 3.0

> **Para Iniciantes**:
>
> - **Base URL**: o endereço principal onde a API "mora" (como o endereço de uma casa)
> - **JSON**: formato de texto que é fácil para computadores entenderem (como uma linguagem universal)
> - **UTF-8**: padrão que permite usar acentos e caracteres especiais
> - **Endpoint**: cada "porta de entrada" específica da API (como /atividades)

---

## Endpoints Implementados

### **POST /atividades** - Criar Atividade

Cadastra uma nova atividade física no sistema.

**Request:**

```http
POST /atividades HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 5km no parque da empresa"
}
```

**Response (201 Created):**

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 5km no parque da empresa"
}
```

> **Para Iniciantes**: Este bloco JSON é a "resposta" que a API dá quando você cria uma atividade com sucesso. É como um recibo confirmando que foi salvo.

**Validações:**

- `funcional`: Obrigatório, máximo 50 caracteres
- `dataHora`: Obrigatório, formato ISO 8601
- `codigoAtividade`: Obrigatório, máximo 20 caracteres
- `descricaoAtividade`: Obrigatório, máximo 255 caracteres

---

### **GET /atividades** - Listar Atividades

Lista todas as atividades com suporte a filtros opcionais.

**Request:**

```http
GET /atividades HTTP/1.1
Host: localhost:8080
```

**Query Parameters:**
| Parâmetro | Tipo | Obrigatório | Descrição | Exemplo |
|-----------|------|-------------|-----------|---------|
| `funcional` | String | Não | Código do funcionário | `EMP001` |
| `codigoAtividade` | String | Não | Tipo da atividade | `RUN` |
| `descricaoAtividade` | String | Não | Busca na descrição (parcial) | `corrida` |
| `dataInicio` | Date | Não | Data inicial (YYYY-MM-DD) | `2025-09-01` |
| `dataFim` | Date | Não | Data final (YYYY-MM-DD) | `2025-09-30` |

> **Para Iniciantes**: "Query Parameters" são como filtros que você adiciona na URL após o "?" para pedir dados específicos. É como dizer "me mostre apenas as atividades do funcionário EMP001".

### **Detalhes dos Filtros Disponíveis**

#### **1. Filtro por Funcional**

- **Tipo**: Busca exata
- **Formato**: String (máximo 50 caracteres)
- **Exemplo**: `?funcional=EMP001`
- **Comportamento**: Retorna apenas atividades do funcionário especificado

#### **2. Filtro por Código da Atividade**

- **Tipo**: Busca exata
- **Formato**: String (máximo 20 caracteres)
- **Exemplo**: `?codigoAtividade=RUN`
- **Comportamento**: Retorna apenas atividades com o código especificado
- **Valores comuns**: `RUN`, `GYM`, `WALK`, `BIKE`, `SWIM`

#### **3. Filtro por Descrição**

- **Tipo**: Busca parcial (contém)
- **Formato**: String (máximo 255 caracteres)
- **Exemplo**: `?descricaoAtividade=corrida`
- **Comportamento**: Retorna atividades que contenham a palavra na descrição
- **Sensível**: Case-insensitive (não diferencia maiúsculas/minúsculas)

#### **4. Filtro por Período (Data Início)**

- **Tipo**: Data maior ou igual
- **Formato**: `YYYY-MM-DD`
- **Exemplo**: `?dataInicio=2025-09-01`
- **Comportamento**: Retorna atividades a partir da data especificada (inclusive)

#### **5. Filtro por Período (Data Fim)**

- **Tipo**: Data menor ou igual
- **Formato**: `YYYY-MM-DD`
- **Exemplo**: `?dataFim=2025-09-30`
- **Comportamento**: Retorna atividades até a data especificada (inclusive até 23:59:59)

**Exemplos de Uso:**

### **Filtros Simples (Um critério)**

```http
# Listar todas as atividades (sem filtros)
GET /atividades

# Filtrar apenas por funcionário
GET /atividades?funcional=EMP001

# Filtrar apenas por tipo de atividade
GET /atividades?codigoAtividade=RUN

# Filtrar apenas por descrição (busca parcial)
GET /atividades?descricaoAtividade=corrida

# Filtrar apenas atividades a partir de uma data
GET /atividades?dataInicio=2025-09-01

# Filtrar apenas atividades até uma data
GET /atividades?dataFim=2025-09-30
```

### **Filtros Combinados (Múltiplos critérios)**

```http
# Funcionário + Tipo de atividade
GET /atividades?funcional=EMP001&codigoAtividade=RUN

# Funcionário + Período específico
GET /atividades?funcional=EMP001&dataInicio=2025-09-01&dataFim=2025-09-30

# Tipo + Período
GET /atividades?codigoAtividade=GYM&dataInicio=2025-09-15

# Descrição + Funcionário
GET /atividades?descricaoAtividade=matinal&funcional=EMP001

# Todos os filtros combinados
GET /atividades?funcional=EMP001&codigoAtividade=RUN&descricaoAtividade=parque&dataInicio=2025-09-01&dataFim=2025-09-30
```

### **Casos de Uso Práticos**

```http
# 📊 Relatório mensal de um funcionário
GET /atividades?funcional=EMP001&dataInicio=2025-09-01&dataFim=2025-09-30

# 🏃 Todas as corridas do mês
GET /atividades?codigoAtividade=RUN&dataInicio=2025-09-01&dataFim=2025-09-30

# 🔍 Atividades que mencionam "parque"
GET /atividades?descricaoAtividade=parque

# 📅 Atividades da última semana
GET /atividades?dataInicio=2025-09-21

# 👥 Atividades de academia de todos os funcionários
GET /atividades?codigoAtividade=GYM

# 🎯 Atividades específicas de um funcionário no último mês
GET /atividades?funcional=EMP001&codigoAtividade=RUN&dataInicio=2025-08-28
```

> **Para Iniciantes**:
>
> - **Filtros simples**: use apenas um critério por vez (como buscar só por funcionário)
> - **Filtros combinados**: use vários critérios juntos com "&" (como funcionário + data)
> - **Busca na descrição**: não precisa ser a palavra exata, pode ser parte dela

**Response (200 OK):**

```json
[
  {
    "idAtividade": 1,
    "funcional": "EMP001",
    "dataHora": "2025-09-28T08:00:00",
    "codigoAtividade": "RUN",
    "descricaoAtividade": "Corrida matinal de 5km no parque da empresa"
  },
  {
    "idAtividade": 2,
    "funcional": "EMP001",
    "dataHora": "2025-09-27T19:30:00",
    "codigoAtividade": "GYM",
    "descricaoAtividade": "Treino de musculação - 1h30min"
  }
]
```

### **Regras de Filtragem**

#### **Comportamento dos Filtros**

- **Todos os filtros são opcionais**: Se não informar nenhum, retorna todas as atividades
- **Filtros são combinados com AND**: Todos os critérios devem ser atendidos simultaneamente
- **Busca case-insensitive**: A busca por descrição não diferencia maiúsculas/minúsculas
- **Período inclusivo**: As datas de início e fim incluem os dias especificados

#### **Validações nos Filtros**

- **Funcional**: Máximo 50 caracteres
- **Código da Atividade**: Máximo 20 caracteres, apenas letras maiúsculas e underscore
- **Descrição**: Máximo 255 caracteres
- **Data Início/Fim**: Formato YYYY-MM-DD válido
- **Período**: Data de início não pode ser posterior à data de fim

#### **Exemplos de Respostas com Filtros**

```http
# Request com filtros
GET /atividades?funcional=EMP001&dataInicio=2025-09-01

# Response (apenas atividades que atendem AMBOS os critérios)
[
  {
    "idAtividade": 1,
    "funcional": "EMP001",
    "dataHora": "2025-09-28T08:00:00",
    "codigoAtividade": "RUN",
    "descricaoAtividade": "Corrida matinal de 5km"
  }
]
```

```http
# Request que não encontra resultados
GET /atividades?funcional=EMP999

# Response (lista vazia)
[]
```

---

### **GET /atividades/{id}** - Buscar por ID

Busca uma atividade específica pelo ID.

**Request:**

```http
GET /atividades/1 HTTP/1.1
Host: localhost:8080
```

**Response (200 OK):**

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 5km no parque da empresa"
}
```

**Response (404 Not Found):**

```json
{
  "timestamp": "2025-09-28T15:30:00.123Z",
  "status": 404,
  "error": "Not Found",
  "message": "Atividade não encontrada",
  "path": "/atividades/999"
}
```

---

### **PUT /atividades/{id}** - Atualizar Atividade

Atualiza uma atividade existente.

**Request:**

```http
PUT /atividades/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:30:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 7km no parque da empresa - tempo melhorado"
}
```

**Response (200 OK):**

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:30:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 7km no parque da empresa - tempo melhorado"
}
```

---

### **DELETE /atividades/{id}** - Excluir Atividade

Remove uma atividade do sistema.

**Request:**

```http
DELETE /atividades/1 HTTP/1.1
Host: localhost:8080
```

**Response (204 No Content):**

```
(Sem conteúdo - apenas status 204)
```

**Response (404 Not Found):**

```json
{
  "timestamp": "2025-09-28T15:30:00.123Z",
  "status": 404,
  "error": "Not Found",
  "message": "Atividade não encontrada",
  "path": "/atividades/999"
}
```

---

## ✅ Sistema de Validações

### **Bean Validation (Java)**

O backend utiliza **Bean Validation** (JSR 380) para validar dados de entrada:

> **Para Iniciantes**: "Bean Validation" é como ter um inspetor que verifica se os dados que chegam estão corretos antes de salvar no banco. Se você esquecer de preencher um campo obrigatório, ele vai avisar o erro.

> **Para Iniciantes**: "Bean Validation" é como ter um inspetor que verifica se os dados que chegam estão corretos antes de salvar no banco. Se você esquecer de preencher um campo obrigatório, ele vai avisar o erro.

```java
public class AtividadeInput {

    @NotBlank(message = "Funcional é obrigatório")
    @Size(max = 50, message = "Funcional deve ter no máximo 50 caracteres")
    private String funcional;

    @NotNull(message = "Data/hora é obrigatória")
    @Future(message = "Data/hora deve ser futura")
    private LocalDateTime dataHora;

    @NotBlank(message = "Código da atividade é obrigatório")
    @Size(max = 20, message = "Código deve ter no máximo 20 caracteres")
    @Pattern(regexp = "^[A-Z_]+$", message = "Código deve conter apenas letras maiúsculas e underscore")
    private String codigoAtividade;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricaoAtividade;
}
```

### **Validações por Campo**

| Campo                  | Validações Aplicadas                                                                                            |
| ---------------------- | --------------------------------------------------------------------------------------------------------------- |
| **funcional**          | • Obrigatório (`@NotBlank`)<br>• Máximo 50 caracteres (`@Size`)                                                 |
| **dataHora**           | • Obrigatório (`@NotNull`)<br>• Deve ser futura (`@Future`)<br>• Formato ISO 8601                               |
| **codigoAtividade**    | • Obrigatório (`@NotBlank`)<br>• Máximo 20 caracteres (`@Size`)<br>• Apenas letras maiúsculas e \_ (`@Pattern`) |
| **descricaoAtividade** | • Obrigatório (`@NotBlank`)<br>• Máximo 255 caracteres (`@Size`)                                                |

### **Exemplo de Resposta de Erro (400 Bad Request)**

```json
{
  "timestamp": "2025-09-28T15:30:00.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "funcional",
      "rejectedValue": "",
      "message": "Funcional é obrigatório"
    },
    {
      "field": "dataHora",
      "rejectedValue": "2025-01-01T10:00:00",
      "message": "Data/hora deve ser futura"
    },
    {
      "field": "codigoAtividade",
      "rejectedValue": "run123",
      "message": "Código deve conter apenas letras maiúsculas e underscore"
    }
  ],
  "path": "/atividades"
}
```

---

## 📱 Códigos de Status HTTP

> **Para Iniciantes**: Códigos de status HTTP são como "emoticons" que o servidor usa para dizer se deu certo ou errado. 200 = "tudo certo 😄", 404 = "não achei 😕", 500 = "algo deu errado aqui 😱".

### **Códigos de Sucesso**

| Código             | Descrição          | Quando Usar                   |
| ------------------ | ------------------ | ----------------------------- |
| **200 OK**         | Sucesso            | GET, PUT - operação realizada |
| **201 Created**    | Criado com sucesso | POST - recurso criado         |
| **204 No Content** | Sem conteúdo       | DELETE - recurso removido     |

### **Códigos de Erro**

| Código                 | Descrição       | Quando Ocorre    |
| ---------------------- | --------------- | ---------------- |
| **400 Bad Request**    | Dados inválidos | Validação falhou |
| **404 Not Found**      | Não encontrado  | ID não existe    |
| **500 Internal Error** | Erro interno    | Erro no servidor |

---

## 🧪 Testes Implementados

> **Para Iniciantes**: Testes são como "simulados" que o sistema faz para ter certeza de que tudo funciona corretamente. É como testar se uma porta abre antes de entregar a casa para o cliente.

### 🔬 **Estrutura de Testes**

```
src/test/java/
├── controller/
│   └── AtividadeControllerTest.java     # Testes de endpoints
├── service/
│   └── AtividadeServiceTest.java        # Testes de lógica de negócio
├── model/
│   └── AtividadeTest.java               # Testes de entidade
└── config/
    └── CorsConfigTest.java              # Testes de configuração
```

### **Testes de Controller (Integration Tests)**

> **Para Iniciantes**: "Integration Tests" testam se todas as peças funcionam bem juntas, como testar se o carro inteiro anda bem (não só o motor).

```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class AtividadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve criar atividade com dados válidos")
    void deveCriarAtividadeComDadosValidos() throws Exception {
        String atividadeJson = """
            {
                "funcional": "EMP001",
                "dataHora": "2025-12-25T10:30:00",
                "codigoAtividade": "RUN",
                "descricaoAtividade": "Corrida teste"
            }
            """;

        mockMvc.perform(post("/atividades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atividadeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.funcional").value("EMP001"))
                .andExpect(jsonPath("$.codigoAtividade").value("RUN"));
    }

    @Test
    @DisplayName("Deve retornar 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        String atividadeInvalidaJson = """
            {
                "funcional": "",
                "dataHora": null,
                "codigoAtividade": "",
                "descricaoAtividade": ""
            }
            """;

        mockMvc.perform(post("/atividades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(atividadeInvalidaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar atividades com filtros")
    void deveListarAtividadesComFiltros() throws Exception {
        mockMvc.perform(get("/atividades")
                .param("funcional", "EMP001")
                .param("codigoAtividade", "RUN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Deve retornar 404 para ID inexistente")
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get("/atividades/999"))
                .andExpect(status().isNotFound());
    }
}
```

### ⚙️ **Testes de Service (Unit Tests)**

```java
@ExtendWith(MockitoExtension.class)
class AtividadeServiceTest {

    @Mock
    private AtividadeRepository repository;

    @Mock
    private AtividadeMapper mapper;

    @InjectMocks
    private AtividadeServiceImpl service;

    @Test
    @DisplayName("Deve criar atividade com sucesso")
    void deveCriarAtividadeComSucesso() {
        // Given
        AtividadeInput input = new AtividadeInput("EMP001",
            LocalDateTime.of(2025, 12, 25, 10, 30),
            "RUN", "Teste");
        Atividade entity = new Atividade();
        AtividadeOutput expectedOutput = new AtividadeOutput();

        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toOutput(entity)).thenReturn(expectedOutput);

        // When
        AtividadeOutput result = service.criarAtividade(input);

        // Then
        assertThat(result).isEqualTo(expectedOutput);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Deve buscar atividades por funcional")
    void deveBuscarAtividadesPorFuncional() {
        // Given
        String funcional = "EMP001";
        List<Atividade> entities = Arrays.asList(new Atividade());
        List<AtividadeOutput> expectedOutputs = Arrays.asList(new AtividadeOutput());

        when(repository.findByFuncional(funcional)).thenReturn(entities);
        when(mapper.toOutputList(entities)).thenReturn(expectedOutputs);

        // When
        List<AtividadeOutput> result = service.listarPorFuncional(funcional);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedOutputs);
    }
}
```

### **Cobertura de Testes (JaCoCo)**

```bash
# Executar testes com relatório de cobertura
mvn clean test jacoco:report

# Relatório gerado em:
target/site/jacoco/index.html
```

**Métricas de Cobertura:**

- **Controllers**: 95%+
- **Services**: 90%+
- **Repositories**: 85%+
- **Models**: 100%

---

## 🔍 Ferramentas de Teste

### **Swagger UI - Testes Interativos**

Acesse: http://localhost:8080/swagger-ui.html

> **Para Iniciantes**: Swagger é como um "manual interativo" da API. Você pode testar todos os endpoints diretamente no navegador, sem precisar saber programação - é só clicar e preencher!

**Funcionalidades:**

- ✅ Teste todos os endpoints diretamente no navegador
- ✅ Visualização automática de esquemas JSON
- ✅ Exemplos de request/response
- ✅ Validação em tempo real

### 📡 **Collections Postman/Insomnia**

O projeto inclui collections prontas em `/collections`:

```bash
# Importar no Insomnia
collections/collection

# Importar no Postman
collections/collection-json
```

**Testes Incluídos:**

- ✅ CRUD completo de atividades
- ✅ Cenários de erro (400, 404)
- ✅ Filtros com diferentes combinações
- ✅ Validações de campo

### **Testes com cURL**

```bash
# Criar atividade
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "EMP001",
    "dataHora": "2025-12-25T10:30:00",
    "codigoAtividade": "RUN",
    "descricaoAtividade": "Corrida teste"
  }'

# Listar com filtros
curl "http://localhost:8080/atividades?funcional=EMP001&codigoAtividade=RUN"

# Buscar por ID
curl http://localhost:8080/atividades/1

# Atualizar
curl -X PUT http://localhost:8080/atividades/1 \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "EMP001",
    "dataHora": "2025-12-25T11:00:00",
    "codigoAtividade": "WALK",
    "descricaoAtividade": "Caminhada teste atualizada"
  }'

# Excluir
curl -X DELETE http://localhost:8080/atividades/1
```

---
