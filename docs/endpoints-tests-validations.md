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

> **Para Iniciantes - HTTP/1.1**: É a "versão da linguagem" que o navegador e servidor usam para conversar. HTTP/1.1 (lançado em 1997) ainda é muito usado porque é universal, simples e compatível com todas as ferramentas. É como usar português clássico - todo mundo entende!

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

> **Para Iniciantes**: Códigos de status HTTP são como "emoji" que o servidor usa para dizer se deu certo ou errado. 200 = "tudo certo 😄", 404 = "não achei 😕", 500 = "algo deu errado aqui 😱".

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
src/test/java/br/com/atividade/
├── AtividadeApplicationTests.java           # Testes de integração da aplicação
├── config/
│   └── CorsConfigTest.java                  # Testes de configuração CORS
├── controller/
│   └── AtividadeControllerTest.java         # Testes de endpoints REST
├── mapper/
│   └── AtividadeMapperImplTest.java         # Testes do mapper (MapStruct)
├── model/
│   └── AtividadeTest.java                   # Testes da entidade JPA
├── service/
│   ├── AtividadeServiceTest.java            # Interface de testes do service
│   ├── dto/
│   │   ├── input/
│   │   │   └── AtividadeInputTest.java      # Testes de validação do DTO de entrada
│   │   └── output/
│   │       └── AtividadeOutputTest.java     # Testes do DTO de saída
│   └── impl/
│       └── AtividadeServiceImplTest.java    # Testes da implementação do service
└── resources/
    └── application-test.properties          # Configurações para ambiente de teste
```

> **Para Iniciantes**: Esta estrutura espelha a organização do código principal, garantindo que cada camada da aplicação tenha seus testes específicos - desde a entrada (controllers) até a saída (DTOs), passando por regras de negócio (services) e persistência (entities).

### 📋 **Tipos de Testes Implementados**

#### **🚪 Testes de Entrada (AtividadeApplicationTests.java)**

- **Finalidade**: Testa se a aplicação Spring Boot inicializa corretamente
- **Cobertura**: Context loading, configurações, método main
- **Tipo**: Integration Test (testa o sistema como um todo)

#### **🌐 Testes de Controller (AtividadeControllerTest.java)**

- **Finalidade**: Testa endpoints REST (HTTP requests/responses)
- **Cobertura**: CRUD completo, validações, filtros, códigos de status
- **Tipo**: Integration Test com MockMvc
- **Tecnologias**: @SpringBootTest, MockMvc, JSONPath

> **Para Iniciantes - MockMvc**: É um "simulador de navegador" que testa sua API REST sem iniciar um servidor web real. MockMvc "finge" fazer requisições HTTP, permitindo testar controllers de forma rápida e isolada - como um teatro onde os atores (controllers) encenam, mas o cenário (servidor) é simulado.

#### **🔄 Testes de Service (AtividadeServiceImplTest.java)**

- **Finalidade**: Testa lógica de negócio isoladamente
- **Cobertura**: Operações CRUD, regras de validação, tratamento de exceções
- **Tipo**: Unit Test com mocks
- **Tecnologias**: @ExtendWith(MockitoExtension.class), @Mock, @InjectMocks

#### **🗺️ Testes de Mapper (AtividadeMapperImplTest.java)**

- **Finalidade**: Testa conversões entre DTOs e entidades
- **Cobertura**: Mapeamento bidirecional, listas, valores nulos, campos parciais
- **Tipo**: Unit Test puro (sem Spring Context)
- **Tecnologias**: MapStruct, JUnit 5, AssertJ

#### **💾 Testes de Model/Entity (AtividadeTest.java)**

- **Finalidade**: Testa entidade JPA e suas validações
- **Cobertura**: Constraints, relacionamentos, equals/hashCode
- **Tipo**: Unit Test com validação Bean
- **Tecnologias**: JPA, Bean Validation

#### **📥 Testes de Input DTO (AtividadeInputTest.java)**

- **Finalidade**: Testa validações de entrada de dados
- **Cobertura**: @NotBlank, @Size, @Pattern, @Future, cenários edge
- **Tipo**: Unit Test de validação
- **Tecnologias**: Jakarta Bean Validation, Hibernate Validator

#### **📤 Testes de Output DTO (AtividadeOutputTest.java)**

- **Finalidade**: Testa estrutura de dados de resposta
- **Cobertura**: Serialização JSON, getters/setters, construtores
- **Tipo**: Unit Test de estrutura

#### **⚙️ Testes de Configuração (CorsConfigTest.java)**

- **Finalidade**: Testa configurações CORS da aplicação
- **Cobertura**: Headers permitidos, métodos HTTP, origens
- **Tipo**: Integration Test de configuração

### 📊 **Resumo da Cobertura de Testes**

**🎯 Total: 8 classes de teste + 90+ cenários de teste**

| Camada                | Arquivo de Teste                 | Cenários | Foco Principal                  |
| --------------------- | -------------------------------- | -------- | ------------------------------- |
| **Application**       | `AtividadeApplicationTests.java` | 2        | Context loading, método main    |
| **Controller**        | `AtividadeControllerTest.java`   | 25+      | Endpoints REST, validações HTTP |
| **Service Impl**      | `AtividadeServiceImplTest.java`  | 25+      | Lógica de negócio, exceções     |
| **Service Interface** | `AtividadeServiceTest.java`      | 10+      | Contratos de interface          |
| **Mapper**            | `AtividadeMapperImplTest.java`   | 20+      | Conversões DTO ↔ Entity         |
| **Model**             | `AtividadeTest.java`             | 10+      | Entidade JPA, validações        |
| **Input DTO**         | `AtividadeInputTest.java`        | 13+      | Bean Validation, entrada        |
| **Output DTO**        | `AtividadeOutputTest.java`       | 8+       | Estrutura de saída              |
| **Config**            | `CorsConfigTest.java`            | 5+       | Configurações CORS              |

**✨ Principais Benefícios da Estrutura:**

- 🔍 **Isolamento**: Cada camada testada independentemente
- 🚀 **Velocidade**: Testes unitários executam rapidamente
- 🎯 **Precisão**: Falhas apontam exatamente onde está o problema
- 📈 **Cobertura**: 87% de cobertura geral (excelente!)
- 🔒 **Confiabilidade**: Detecta regressões automaticamente

### **Cobertura de Testes (JaCoCo)**

O projeto utiliza **JaCoCo (Java Code Coverage)** para medir a cobertura de testes e garantir qualidade do código.

> **Para Iniciantes**: JaCoCo é como um "contador" que verifica quantas linhas do seu código foram testadas. Se você tem 100 linhas e 87 foram testadas, sua cobertura é de 87% - quanto maior, melhor!

#### **Como Executar e Gerar Relatório**

```bash
# Executar todos os testes com relatório de cobertura
mvn clean test jacoco:report

# Ou executar apenas os testes (sem limpar)
mvn test jacoco:report

# Para projetos com Maven Wrapper
./mvnw clean test jacoco:report
```

#### **Onde Encontrar o Relatório**

Após executar os comandos acima, o relatório será gerado em:

```
📁 target/site/jacoco/
├── 📄 index.html          # Página principal do relatório
├── 📁 br.com.atividade/   # Cobertura por pacote
└── 📁 jacoco-sessions/    # Dados de sessão
```

**Para visualizar:**

1. **Navegador**: Abra o arquivo `target/site/jacoco/index.html` no navegador
2. **VS Code**: Use a extensão "Live Server" para abrir o HTML
3. **IntelliJ**: Clique com botão direito no arquivo → "Open in Browser"

#### **Métricas Atuais de Cobertura**

**📊 Cobertura Geral: 87% (Excelente!)**

| Pacote        | Instruções | Branches | Complexidade | Linhas  | Métodos | Classes  |
| ------------- | ---------- | -------- | ------------ | ------- | ------- | -------- |
| **📦 Total**  | **87%**    | **67%**  | **74%**      | **89%** | **96%** | **100%** |
| `sevice.impl` | 83%        | 59%      | 61%          | 87%     | 91%     | 100%     |
| `controller`  | 85%        | 83%      | 50%          | 84%     | 100%    | 100%     |
| `mapper`      | 100%       | 100%     | 100%         | 100%    | 100%    | 100%     |
| `config`      | 100%       | n/a      | 100%         | 100%    | 100%    | 100%     |
| `main`        | 100%       | n/a      | 100%         | 100%    | 100%    | 100%     |

#### **Interpretação das Métricas**

> **Para Iniciantes**: Cada métrica mede um aspecto diferente:

- **📏 Instruções**: Quantas "comandos" do código foram executados nos testes
- **🌳 Branches**: Quantos "caminhos" (if/else) foram testados
- **🔄 Complexidade**: Quão complicadas são as funções testadas
- **📄 Linhas**: Quantas linhas de código foram "tocadas" pelos testes
- **⚙️ Métodos**: Quantas funções foram chamadas durante os testes
- **📦 Classes**: Quantas classes foram utilizadas nos testes

#### **Metas de Cobertura**

```xml
<!-- Configuração no pom.xml -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <id>default-prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>default-report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>INSTRUCTION</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum> <!-- 80% mínimo -->
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### **Comandos Úteis**

```bash
# Executar testes e verificar se atende critério mínimo
mvn clean test jacoco:report jacoco:check

# Apenas gerar relatório (se já executou os testes)
mvn jacoco:report

# Executar testes específicos com cobertura
mvn test -Dtest=AtividadeControllerTest jacoco:report

# Limpar relatórios anteriores
mvn clean
```

#### **Integração com IDEs**

**🔧 IntelliJ IDEA:**

- Plugin "JaCoCo" nativo
- Menu: Run → Run with Coverage

**🔧 VS Code:**

- Extensão: "Coverage Gutters"
- Mostra cobertura diretamente no código

**🔧 Eclipse:**

- Plugin "EclEmma" (JaCoCo integration)
- Botão "Coverage As" na toolbar

#### **Como Ler o Relatório Visual**

Ao abrir o `index.html`, você verá:

**🏠 Página Principal:**

```
📊 Element         Instructions    Branches    Cxty    Lines    Methods    Classes
   br.com.atividade      87%         67%       74%     89%       96%       100%
   ├── controller        85%         83%       50%     84%      100%       100%
   ├── service.impl      83%         59%       61%     87%       91%       100%
   ├── mapper           100%        100%      100%    100%      100%       100%
   ├── config           100%         n/a      100%    100%      100%       100%
   └── main             100%         n/a      100%    100%      100%       100%
```

**🎨 Código Colorido:**

- 🟢 **Verde**: Linha totalmente coberta pelos testes
- 🟡 **Amarelo**: Linha parcialmente coberta (alguns branches)
- 🔴 **Vermelho**: Linha não coberta pelos testes
- ⚫ **Cinza**: Linha não executável (comentários, imports)

**📱 Navegação:**

1. **Clique no pacote** → ver classes do pacote
2. **Clique na classe** → ver métodos da classe
3. **Clique no método** → ver código linha por linha
4. **Hover nas cores** → ver detalhes da cobertura

#### **Exemplo Prático de Uso**

```bash
# 1. Execute os testes com cobertura
./mvnw clean test jacoco:report

# 2. Abra o relatório
# Windows: start target/site/jacoco/index.html
# macOS: open target/site/jacoco/index.html
# Linux: xdg-open target/site/jacoco/index.html

# 3. Navegue para identificar código não testado:
# index.html → br.com.atividade → service.impl → AtividadeServiceImpl.java

# 4. Identifique linhas vermelhas/amarelas

# 5. Crie testes para melhorar cobertura

# 6. Execute novamente para ver melhoria
./mvnw test jacoco:report
```

**💡 Dica Pro**: Use `./mvnw test jacoco:report && start target/site/jacoco/index.html` (Windows) para executar e abrir o relatório automaticamente!

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

## Desenvolvedora

**Jhenifer Lorrane**

- GitHub: [@jheniferlorrane](https://github.com/jheniferlorrane)
- LinkedIn: [Jhenifer Lorrane](https://www.linkedin.com/in/jheniferanacleto/)

---

## Versão

**v1.0.0** – Case Técnico Completo

- Requisitos 100% atendidos
- Funcionalidades extras implementadas