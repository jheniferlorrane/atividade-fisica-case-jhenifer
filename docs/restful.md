# RESTful API - Princípios e Boas Práticas

## O que é REST?

**REST** (Representational State Transfer) é um estilo arquitetural para sistemas distribuídos que define um conjunto de constraints e princípios para criar APIs escaláveis e bem organizadas.

### **Princípios Fundamentais**

1. **Client-Server**: Separação clara entre cliente e servidor
2. **Stateless**: Cada requisição contém todas as informações necessárias
3. **Cacheable**: Respostas podem ser cacheadas para melhor performance
4. **Uniform Interface**: Interface consistente entre componentes
5. **Layered System**: Arquitetura em camadas
6. **Code on Demand** (opcional): Código executável pode ser transferido

---

## Como Nossa API Implementa REST

### **Uniform Interface**

Nossa API segue rigorosamente a interface uniforme através de:

#### **1. Identificação de Recursos**

Cada recurso é identificado por URLs bem definidas:

```
/atividades          # Coleção de atividades
/atividades/{id}     # Atividade específica
```

#### 📊 **2. Representação de Recursos**

Recursos são representados em JSON com estrutura consistente:

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal de 5km"
}
```

#### 🔄 **3. Manipulação via Representações**

Clientes podem manipular recursos através das representações recebidas:

```javascript
// Cliente recebe representação
const atividade = await api.get("/atividades/1");

// Modifica localmente
atividade.descricaoAtividade = "Corrida de 7km";

// Envia de volta para atualizar
await api.put("/atividades/1", atividade);
```

#### 🔗 **4. HATEOAS** (Hypermedia as Engine of Application State)

Embora não implementado na versão atual, futuramente incluiremos links relacionados:

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal",
  "_links": {
    "self": "/atividades/1",
    "funcionario": "/funcionarios/EMP001",
    "edit": "/atividades/1",
    "delete": "/atividades/1"
  }
}
```

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

## Códigos de Status HTTP Implementados

### ✅ **Família 2xx - Sucesso**

| Código  | Nome       | Quando Usar            | Implementado |
| ------- | ---------- | ---------------------- | ------------ |
| **200** | OK         | GET, PUT bem-sucedidos | ✅           |
| **201** | Created    | POST criou recurso     | ✅           |
| **204** | No Content | DELETE bem-sucedido    | ✅           |

### ❌ **Família 4xx - Erro do Cliente**

| Código  | Nome                 | Quando Usar          | Implementado |
| ------- | -------------------- | -------------------- | ------------ |
| **400** | Bad Request          | Dados inválidos      | ✅           |
| **404** | Not Found            | Recurso não existe   | ✅           |
| **405** | Method Not Allowed   | Verbo não suportado  | ✅           |
| **422** | Unprocessable Entity | Validação de negócio | Futuro       |

### 🔥 **Família 5xx - Erro do Servidor**

| Código  | Nome                  | Quando Usar           | Implementado |
| ------- | --------------------- | --------------------- | ------------ |
| **500** | Internal Server Error | Erro interno          | ✅           |
| **503** | Service Unavailable   | Servidor indisponível | Futuro       |

---

## Padrões de Requisição/Resposta

### 📝 **POST - Criação de Recurso**

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

### 📋 **GET - Leitura de Coleção**

**Padrão REST:**

```http
GET /atividades?funcional=EMP001&page=1&size=10 HTTP/1.1
Accept: application/json
```

**Resposta (200 OK):**

```http
HTTP/1.1 200 OK
Content-Type: application/json
Cache-Control: public, max-age=300

{
  "content": [
    {
      "idAtividade": 1,
      "funcional": "EMP001",
      "dataHora": "2025-09-28T08:00:00",
      "codigoAtividade": "RUN",
      "descricaoAtividade": "Corrida matinal"
    }
  ],
  "pageable": {
    "page": 1,
    "size": 10,
    "totalElements": 25,
    "totalPages": 3
  }
}
```

### 🎯 **GET - Leitura de Recurso Específico**

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

### ✏️ **PUT - Atualização Completa**

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

### 🗑️ **DELETE - Remoção de Recurso**

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

# Paginação (futuro)
GET /atividades?page=1&size=20&sort=dataHora,desc
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

> **RFC 3986 (URI Specification)**: Query parameters são apropriados para "não-hierárquicos" dados que modificam ou filtram o recurso principal.

> **Richardson Maturity Model**: Level 2 REST APIs usam query parameters para filtros em coleções, reservando path parameters apenas para identificação hierárquica de recursos.

**Conclusão**: Nossa implementação com `GET /atividades?funcional=EMP001` está **100% alinhada com os padrões REST**, proporcionando flexibilidade, escalabilidade e clareza semântica.

---

## Headers HTTP Importantes

### 📥 **Request Headers**

```http
Content-Type: application/json          # Tipo do conteúdo
Accept: application/json                # Tipo aceito na resposta
Authorization: Bearer <token>           # Autenticação (futuro)
If-Match: "etag-value"                 # Controle de concorrência
If-None-Match: "etag-value"            # Cache condicional
User-Agent: MobileApp/1.0              # Identificação do cliente
```

### 📤 **Response Headers**

```http
Content-Type: application/json         # Tipo do conteúdo retornado
Location: /atividades/1               # URL do recurso criado (POST)
ETag: "unique-identifier"             # Identificador de versão
Last-Modified: Wed, 28 Sep 2025       # Data da última modificação
Cache-Control: public, max-age=300    # Controle de cache
Access-Control-Allow-Origin: *        # CORS
```

---

## Boas Práticas Implementadas

### 🎯 **1. Naming Conventions**

```http
# ✅ Bom - Substantivos no plural
GET /atividades
POST /atividades
GET /atividades/1

# ❌ Ruim - Verbos nas URLs
GET /getAtividades
POST /createAtividade
DELETE /deleteAtividade/1
```

### 🔄 **2. Consistent Error Responses**

```json
{
  "timestamp": "2025-09-28T15:30:00.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/atividades",
  "errors": [
    {
      "field": "funcional",
      "rejectedValue": "",
      "message": "Funcional é obrigatório"
    }
  ]
}
```

### 📊 **3. Stateless Design**

```java
// ✅ Stateless - toda informação na requisição
@GetMapping("/atividades")
public ResponseEntity<List<AtividadeOutput>> listar(
    @RequestParam(required = false) String funcional,
    @RequestParam(required = false) String codigoAtividade) {
    // Implementação
}

// ❌ Stateful - dependente de estado do servidor
@GetMapping("/atividades")
public ResponseEntity<List<AtividadeOutput>> listar() {
    // Usa estado da sessão - não REST
    String funcional = userSession.getCurrentFuncional();
}
```

### 🔒 **4. Idempotency**

```java
// ✅ PUT é idempotente
@PutMapping("/atividades/{id}")
public ResponseEntity<AtividadeOutput> atualizar(
    @PathVariable Long id,
    @RequestBody AtividadeInput input) {
    // Sempre produz o mesmo resultado
    return ResponseEntity.ok(service.atualizar(id, input));
}

// ✅ DELETE é idempotente
@DeleteMapping("/atividades/{id}")
public ResponseEntity<Void> excluir(@PathVariable Long id) {
    service.excluir(id); // Safe para múltiplas chamadas
    return ResponseEntity.noContent().build();
}
```

---

## Funcionalidades RESTful Futuras

### 📈 **1. HATEOAS (Level 3 REST)**

```json
{
  "idAtividade": 1,
  "funcional": "EMP001",
  "dataHora": "2025-09-28T08:00:00",
  "codigoAtividade": "RUN",
  "descricaoAtividade": "Corrida matinal",
  "_links": {
    "self": { "href": "/atividades/1" },
    "edit": { "href": "/atividades/1", "method": "PUT" },
    "delete": { "href": "/atividades/1", "method": "DELETE" },
    "funcionario": { "href": "/funcionarios/EMP001" }
  },
  "_actions": {
    "duplicate": {
      "href": "/atividades",
      "method": "POST",
      "type": "application/json"
    }
  }
}
```

### 🔄 **2. ETags e Conditional Requests**

```java
@GetMapping("/atividades/{id}")
public ResponseEntity<AtividadeOutput> buscarPorId(
    @PathVariable Long id,
    @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

    AtividadeOutput atividade = service.buscarPorId(id);
    String etag = generateETag(atividade);

    if (etag.equals(ifNoneMatch)) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    return ResponseEntity.ok()
        .eTag(etag)
        .body(atividade);
}
```

### 📊 **3. Paginação Padronizada**

```java
@GetMapping("/atividades")
public ResponseEntity<Page<AtividadeOutput>> listar(
    @RequestParam(required = false) String funcional,
    @PageableDefault(size = 20, sort = "dataHora", direction = Sort.Direction.DESC)
    Pageable pageable) {

    Page<AtividadeOutput> page = service.listar(funcional, pageable);
    return ResponseEntity.ok(page);
}
```

### 🔍 **4. Content Negotiation**

```java
@GetMapping(value = "/atividades/{id}",
           produces = {MediaType.APPLICATION_JSON_VALUE,
                      MediaType.APPLICATION_XML_VALUE})
public ResponseEntity<AtividadeOutput> buscarPorId(
    @PathVariable Long id,
    @RequestHeader(value = "Accept", required = false) String accept) {

    AtividadeOutput atividade = service.buscarPorId(id);

    if (MediaType.APPLICATION_XML_VALUE.equals(accept)) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(atividade);
    }

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(atividade);
}
```
