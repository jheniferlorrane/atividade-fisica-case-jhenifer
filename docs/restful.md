# RESTful API - Princípios e Boas Práticas

## O que é REST?

**REST** (Representational State Transfer) é um estilo arquitetural para sistemas distribuídos que define um conjunto de **restrições** (constraints) e princípios para criar APIs escaláveis e bem organizadas.

> **O que são "constraints" (restrições)?**
>
> **Constraints** são "regras obrigatórias" que uma API deve seguir para ser considerada RESTful. Pense como as "regras do jogo" - assim como no futebol você não pode usar as mãos (exceto o goleiro), no REST você### **Convenções de Nomenclatura**

### **Princípios Fundamentais**

#### **1. Client-Server (Cliente-Servidor)**

**O que é:** Separação clara entre cliente e servidor, onde cada um tem responsabilidades específicas.

**Como funciona:**

- **Cliente** (frontend): Cuida da interface do usuário, experiência, apresentação dos dados
- **Servidor** (backend): Processa lógica de negócio, gerencia dados, aplica regras

**No nosso projeto:**

```
Frontend (React) ←→ Backend (Spring Boot)
   - Interface        - Lógica de negócio
   - Experiência      - Banco de dados
   - Apresentação     - Validações
```

**Vantagem:** Cliente e servidor podem evoluir independentemente. Frontend pode ser substituído (React → Angular) sem afetar o backend.

#### **2. Stateless (Sem Estado)**

**O que é:** Cada requisição HTTP contém TODAS as informações necessárias para ser processada.

**Como funciona:**

- Servidor não guarda informações entre requisições
- Cada chamada é independente e completa
- Todas as informações vêm na própria requisição

**Exemplo no nosso projeto:**

```http
# ✅ STATELESS - Todas as informações na requisição
GET /atividades?funcional=EMP001&codigoAtividade=RUN HTTP/1.1
Authorization: Bearer token123
Accept: application/json

# ❌ STATEFUL - Dependeria de informações guardadas no servidor
GET /atividades HTTP/1.1  # Servidor teria que "lembrar" do usuário logado
```

**Vantagem:** Escalabilidade - qualquer servidor pode processar qualquer requisição.

#### **3. Cacheable (Pode ser Armazenado em Cache)**

**O que é:** Respostas podem ser armazenadas temporariamente para melhorar performance.

**Como funciona:**

- Servidor informa se uma resposta pode ser cacheada
- Cliente (ou proxy) armazena a resposta por um tempo
- Próximas requisições idênticas usam cache ao invés de ir ao servidor

**Exemplo no nosso projeto:**

```http
# Servidor responde com headers de cache:
HTTP/1.1 200 OK
Cache-Control: public, max-age=300  # Cache por 5 minutos
ETag: "abc123"                      # Identificador único da versão

# Próxima requisição:
GET /atividades/1 HTTP/1.1
If-None-Match: "abc123"  # "Só me envie se mudou"

# Se não mudou:
HTTP/1.1 304 Not Modified  # Use o cache!
```

**Vantagem:** Reduz carga no servidor e melhora velocidade para o usuário.

#### **4. Uniform Interface (Interface Uniforme)**

**O que é:** Todos os recursos seguem os mesmos padrões e convenções.

**Como funciona:**

- Mesmos verbos HTTP (GET, POST, PUT, DELETE)
- Mesma estrutura de URLs
- Mesmos códigos de status
- Mesma representação (JSON)

**Exemplo no nosso projeto:**

```http
# Padrão consistente para TODOS os recursos:
GET    /atividades     # Listar
POST   /atividades     # Criar
GET    /atividades/1   # Buscar específico
PUT    /atividades/1   # Atualizar
DELETE /atividades/1   # Excluir
```

**Vantagem:** Fácil de aprender - quem conhece um endpoint, conhece todos.

#### **5. Layered System (Sistema em Camadas)**

**O que é:** Arquitetura organizada em camadas, onde cada camada só conhece a camada adjacente.

> **Analogia simples:** É como um prédio - o 3º andar não precisa saber o que acontece no térreo, só precisa conhecer o 2º andar (camada de baixo) e o 4º andar (camada de cima).

**Como funciona:**

- Cliente não precisa saber se está falando diretamente com servidor ou com proxy
- Podem existir caches, load balancers, firewalls no meio
- Cada camada tem uma responsabilidade específica

**Exemplo prático no nosso projeto:**

```
Cliente (Browser/Mobile)

        ↓ "Quero listar atividades"

Load Balancer (distribui carga entre vários servidores)

        ↓ "Enviando para servidor menos ocupado"

API Gateway (portão de entrada - autenticação, roteamento)

        ↓ "Usuário autenticado, pode prosseguir"

Nossa API (Spring Boot - lógica de negócio)

        ↓ "Processando regras e buscando dados"

Banco de Dados (armazena os dados)
```

**Como isso ajuda na prática:**

- **Cliente** não sabe que existe load balancer - só faz a requisição
- **Load Balancer** não sabe sobre regras de negócio - só distribui as chamadas
- **API Gateway** não sabe sobre banco - só autentica e roteia
- **Nossa API** não sabe sobre load balancer - só processa a lógica
- **Banco** não sabe sobre clientes - só armazena dados

**Vantagem:** Se quisermos trocar o load balancer ou adicionar um cache, as outras camadas nem ficam sabendo!

#### **6. Code on Demand (Código Sob Demanda) - OPCIONAL**

**O que é:** Servidor pode enviar código executável para o cliente.

**Como funciona:**

- Servidor envia JavaScript, applets, ou plugins
- Cliente executa esse código localmente
- Usado raramente em APIs REST modernas

**Exemplo (raramente usado):**

```http
# Servidor poderia enviar:
HTTP/1.1 200 OK
Content-Type: application/javascript

function validarAtividade(dados) {
  // Código de validação dinâmico
  return dados.funcional && dados.dataHora;
}
```

**No nosso projeto:** Não implementamos - nosso frontend já tem toda lógica necessária.

**Vantagem:** Flexibilidade para enviar lógica dinâmica, mas aumenta complexidade.

---

## Como Nossa API Implementa REST

### **Uniform Interface (Interface Uniforme)**

> **Analogia simples:** É como ter **padrões** em todos os lugares. Imagine que todas as tomadas do mundo fossem iguais - você plugaria qualquer aparelho em qualquer lugar. **Uniform Interface** faz isso com APIs!

**O que significa "uniforme"?**

- Todas as operações seguem as **mesmas regras**
- Mesma **linguagem** para falar com a API
- Mesmos **códigos** de resposta
- Mesmo **formato** de dados (JSON)

Nossa API segue rigorosamente a interface uniforme através de **4 princípios**:

#### **1. Identificação de Recursos**

**O que é:** Cada "coisa" na API tem um endereço único e previsível.

**Na prática:**

```
/atividades          → Lista TODAS as atividades
/atividades/1        → Atividade específica (ID = 1)
/atividades/2        → Atividade específica (ID = 2)
```

**Por que é "uniforme"?**

- Se existe `/atividades`, você **sabe** que `/atividades/1` vai existir
- **Padrão consistente**: `/recurso` e `/recurso/{id}`
- **Previsível**: Qualquer desenvolvedor consegue "adivinhar" as URLs

#### **2. Representação de Recursos**

**O que é:** Todos os dados vêm no **mesmo formato** (JSON) com **estrutura consistente**.

**Exemplo - sempre retornamos assim:**

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 5km"
}
```

**Por que é "uniforme"?**

- **Sempre JSON** (nunca XML misturado com JSON)
- **Mesmos nomes** de campos (sempre `idAtividade`, nunca `id_atividade`)
- **Mesma estrutura** (campos no mesmo lugar)
- **Fácil de entender**: Se você viu um, viu todos

#### **3. Manipulação via Representações**

**O que é:** Você pode **modificar** recursos usando exatamente os **mesmos dados** que recebe.

**Fluxo simples:**

```javascript
// 1. BUSCAR (GET) - Recebe os dados
const atividade = await api.get("/atividades/1");
// Retorna: { "idAtividade": 1, "descricaoAtividade": "Corrida de 5km", ... }

// 2. MODIFICAR - Muda o que quiser localmente
atividade.descricaoAtividade = "Corrida de 10km"; // Mudou só isso!

// 3. ENVIAR (PUT) - Manda os mesmos dados de volta
await api.put("/atividades/1", atividade);
// API entende e atualiza!
```

**Por que é "uniforme"?**

- **Mesma estrutura** para receber e enviar
- **Não precisa converter** dados
- **Intuitivo**: O que você vê é o que você manda

#### **4. HATEOAS** (Links Relacionados) - **FUTURO**

**O que é:** API te dá **links** para tudo que você pode fazer com o recurso.

**Imagine receber isso:**

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "descricaoAtividade": "Corrida matinal",
  "_links": {
    "editar": "/atividades/1",           ← Link para editar
    "excluir": "/atividades/1",          ← Link para excluir
    "funcionario": "/funcionarios/EMP001" ← Link relacionado
  }
}
```

**Por que seria útil?**

- **Auto-descoberta**: API te mostra o que você pode fazer
- **Navegação**: Como links em páginas web
- **Menos código**: Frontend não precisa "construir" URLs

---

## 🎯 **Por que "Uniform Interface" é importante?**

**Sem padrões (caótico):**

```http
GET /buscar_atividades        ← Verbo na URL (ruim)
GET /atividade/obter/1        ← Inconsistente
POST /criar-atividade         ← Hífen vs underscore
PUT /atividades/1/update      ← Verbo desnecessário
```

**Com Uniform Interface (organizado):**

```http
GET /atividades               ← Sempre substantivo plural
GET /atividades/1             ← Padrão consistente
POST /atividades              ← Mesmo endpoint, verbo HTTP diferente
PUT /atividades/1             ← Limpo e intuitivo
```

**Benefícios reais:**

- ✅ **Fácil de aprender**: Padrões são previsíveis
- ✅ **Menos bugs**: Desenvolvedor sabe o que esperar
- ✅ **Reutilização**: Código frontend funciona igual para todos endpoints
- ✅ **Documentação**: Mais simples de explicar e entender

---

## Verbos HTTP e Semântica

### 📋 **Mapeamento de Operações**

| Verbo HTTP  | Operação             | Idempotente | Safe | Uso na API          |
| ----------- | -------------------- | ----------- | ---- | ------------------- |
| **GET**     | Leitura              | ✅          | ✅   | Buscar atividades   |
| **POST**    | Criação              | ❌          | ❌   | Criar atividade     |
| **PUT**     | Atualização completa | ✅          | ❌   | Atualizar atividade |
| **DELETE**  | Remoção              | ✅          | ❌   | Excluir atividade   |
| **PATCH**   | Atualização parcial  | ❌          | ❌   | (Futuro)            |
| **HEAD**    | Metadata             | ✅          | ✅   | (Futuro)            |
| **OPTIONS** | Capabilities         | ✅          | ✅   | CORS                |

### ⚡ **Características dos Verbos**

**Safe Methods (Seguros):**

- Não modificam o estado do servidor
- Podem ser cacheados
- Exemplo: GET, HEAD, OPTIONS

**Idempotent Methods (Idempotentes):**

- Múltiplas chamadas têm o mesmo efeito
- Podem ser repetidas com segurança
- Exemplo: GET, PUT, DELETE

---

## Códigos de Status HTTP Implementados:

### **Família 2xx - Sucesso**

| Código  | Nome       | Quando Usar            | Implementado |
| ------- | ---------- | ---------------------- | ------------ |
| **200** | OK         | GET, PUT bem-sucedidos | ✅           |
| **201** | Created    | POST criou recurso     | ✅           |
| **204** | No Content | DELETE bem-sucedido    | ✅           |

### **Família 4xx - Erro do Cliente**

| Código  | Nome        | Quando Usar        | Implementado |
| ------- | ----------- | ------------------ | ------------ |
| **400** | Bad Request | Dados inválidos    | ✅           |
| **404** | Not Found   | Recurso não existe | ✅           |

### **Família 5xx - Erro do Servidor**

| Código  | Nome                  | Quando Usar  | Implementado |
| ------- | --------------------- | ------------ | ------------ |
| **500** | Internal Server Error | Erro interno | ✅           |

---

## Padrões de Requisição/Resposta

### **POST - Criação de Recurso**

**Padrão REST:**

```http
POST /atividades HTTP/1.1
Content-Type: application/json
Content-Length: 142

{
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal"
}
```

**Resposta (201 Created):**

```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: /atividades/1

{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal"
}
```

### **GET - Leitura de Coleção**

**Padrão REST:**

```http
GET /atividades?funcional=EMP001 HTTP/1.1
Accept: application/json
```

**Resposta (200 OK):**

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "idAtividade": 1,
    "funcional": "EMP001",
    "dataHora": "2025-09-28T08:00:00",
    "codigoAtividade": "RUN",
    "descricaoAtividade": "Corrida matinal"
  },
  {
    "idAtividade": 2,
    "funcional": "EMP002",
    "dataHora": "2025-09-28T18:45:00",
    "codigoAtividade": "GYM",
    "descricaoAtividade": "Treino de força"
  }
]
```

### **GET - Leitura de Recurso Específico**

**Padrão REST:**

```http
GET /atividades/1 HTTP/1.1
Accept: application/json
If-None-Match: "abc123"
```

**Resposta (200 OK):**

```http
HTTP/1.1 200 OK
Content-Type: application/json
ETag: "def456"
Last-Modified: Wed, 28 Sep 2025 10:30:00 GMT

{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal"
}
```

### **PUT - Atualização Completa**

**Padrão REST:**

```http
PUT /atividades/1 HTTP/1.1
Content-Type: application/json
If-Match: "def456"

{
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:30:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal - 7km"
}
```

**Resposta (200 OK):**

```http
HTTP/1.1 200 OK
Content-Type: application/json
ETag: "ghi789"

{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:30:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal - 7km"
}
```

### **DELETE - Remoção de Recurso**

**Padrão REST:**

```http
DELETE /atividades/1 HTTP/1.1
If-Match: "ghi789"
```

**Resposta (204 No Content):**

```http
HTTP/1.1 204 No Content
```

---

## Filtros e Consultas RESTful

### 📊 **Query Parameters para Filtros**

Nossa API implementa filtros através de query parameters:

```http
# Filtro simples
GET /atividades?funcional=EMP001

# Múltiplos filtros
GET /atividades?funcional=EMP001&codigoAtividade=RUN

# Filtros de data
GET /atividades?dataInicio=2025-09-01&dataFim=2025-09-30

# Busca textual
GET /atividades?descricaoAtividade=corrida
```

### 🎯 **Convenções de Nomenclatura**

| Tipo de Filtro | Exemplo                                    | Descrição        |
| -------------- | ------------------------------------------ | ---------------- |
| **Equality**   | `funcional=EMP001`                         | Busca exata      |
| **Range**      | `dataInicio=2025-09-01&dataFim=2025-09-30` | Faixa de valores |
| **Substring**  | `descricaoAtividade=corrida`               | Busca textual    |
| **Sorting**    | `sort=dataHora,desc`                       | Ordenação        |
| **Pagination** | `page=1&size=20`                           | Paginação        |

### 🤔 **Por que Query Parameters e não Path Parameters para Filtros?**

#### 🔍 **Decisão Arquitetural RESTful**

**❌ Padrão INCORRETO (Path Parameter):**

```http
GET /atividades/funcional/EMP001     # Viola princípios REST
GET /atividades/tipo/RUN             # Hierarquia confusa
GET /atividades/funcionario/EMP001   # Não é um sub-recurso
```

> **O que é um sub-recurso?**
>
> **Sub-recurso** é um recurso que **pertence** a outro recurso e não faz sentido existir sozinho. É como uma "parte" de algo maior.
>
> **Exemplos CORRETOS de sub-recursos:**
>
> - `GET /posts/123/comentarios` ← Comentários **do post** 123
> - `GET /pedidos/456/itens` ← Itens **do pedido** 456
>
> **Por que `funcional` NÃO é sub-recurso?**
>
> - `funcional` é apenas um **filtro** para buscar atividades
> - Não é algo que "pertence" a uma atividade
> - É uma **propriedade** da atividade, não um recurso independente

**✅ Padrão CORRETO (Query Parameter):**

```http
GET /atividades?funcional=EMP001     # Filtra a coleção
GET /atividades?codigoAtividade=RUN  # Múltiplos filtros possíveis
GET /atividades?funcional=EMP001&codigoAtividade=RUN  # Combinações
```

#### 📋 **Justificativa Técnica Segundo Padrões REST**

| Aspecto            | Path Parameter                     | Query Parameter                               | Escolha REST                |
| ------------------ | ---------------------------------- | --------------------------------------------- | --------------------------- |
| **Propósito**      | Identificar recursos específicos   | Filtrar/modificar coleções                    | ✅ Query para filtros       |
| **Hierarquia**     | `/atividades/{id}` = recurso único | `/atividades?filter=value` = coleção filtrada | ✅ Mantém hierarquia clara  |
| **Combinação**     | Difícil combinar múltiplos filtros | `?a=1&b=2&c=3` naturalmente combinável        | ✅ Query permite combinação |
| **Semântica**      | `funcional` não é um sub-recurso   | `funcional` é um critério de busca            | ✅ Query expressa intenção  |
| **Cacheabilidade** | URLs longas e específicas          | URLs base com parâmetros variáveis            | ✅ Query otimiza cache      |

#### 🏗️ **Implementação Seguindo REST**

```java
@GetMapping("/atividades")  // ✅ Endpoint único para coleção
public ResponseEntity<List<AtividadeOutput>> listar(
    @RequestParam(required = false) String funcional,     // ✅ Filtro opcional
    @RequestParam(required = false) String codigoAtividade, // ✅ Outro filtro
    @RequestParam(required = false) String descricaoAtividade) {

    // Lógica que combina múltiplos filtros naturalmente
    return ResponseEntity.ok(service.listarComFiltros(funcional, codigoAtividade, descricaoAtividade));
}
```

#### 🎯 **Vantagens da Abordagem Query Parameter**

1. **✅ Flexibilidade**: Qualquer combinação de filtros
2. **✅ Escalabilidade**: Fácil adicionar novos filtros
3. **✅ Clareza Semântica**: `/atividades` sempre retorna atividades
4. **✅ Padrão REST**: Seguimos RFC 3986 para URIs
5. **✅ Cacheabilidade**: URLs consistentes para cache
6. **✅ Opcional**: Filtros podem ser omitidos

#### 📖 **Referência aos Padrões REST**

> **RFC 3986 (URI Specification)**: Query parameters são apropriados para dados **"não-hierárquicos"** que modificam ou filtram o recurso principal.
>
> **O que são dados "não-hierárquicos"?**
> São informações que **não representam uma hierarquia** (como pai → filho), mas sim **critérios de busca** ou **configurações**.
>
> **Exemplos de dados hierárquicos vs não-hierárquicos:**
>
> **✅ HIERÁRQUICOS (usam Path Parameters):**
>
> - `/posts/456/comentarios/789` ← Comentário **pertence** ao post
> - `/pedidos/111/itens/222` ← Item **pertence** ao pedido
>
> **✅ NÃO-HIERÁRQUICOS (usam Query Parameters):**
>
> - `/atividades?funcional=EMP001` ← **Filtro** por funcional
> - `/produtos?categoria=eletrônicos` ← **Filtro** por categoria
> - `/vendas?dataInicio=2025-01-01` ← **Filtro** por período
> - `/usuarios?ativo=true&ordenar=nome` ← **Critérios** de busca

> **Richardson Maturity Model**: Level 2 REST APIs usam query parameters para filtros em coleções, reservando path parameters apenas para identificação hierárquica de recursos.

**Conclusão**: Nossa implementação com `GET /atividades?funcional=EMP001` está **100% alinhada com os padrões REST**, proporcionando flexibilidade, escalabilidade e clareza semântica.

---

## Headers HTTP Importantes

### **Request Headers**

```http
Content-Type: application/json          # Tipo do conteúdo
Accept: application/json                # Tipo aceito na resposta
Authorization: Bearer <token>           # Autenticação
If-Match: "etag-value"                 # Controle de concorrência
If-None-Match: "etag-value"            # Cache condicional
User-Agent: MobileApp/1.0              # Identificação do cliente
```

### **Response Headers**

```http
Content-Type: application/json         # Tipo do conteúdo retornado
Location: /atividades/1               # URL do recurso criado (POST)
ETag: "unique-identifier"             # Identificador de versão
Last-Modified: Wed, 28 Sep 2025       # Data da última modificação
Cache-Control: public, max-age=300    # Controle de cache
Access-Control-Allow-Origin: *        # CORS
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
