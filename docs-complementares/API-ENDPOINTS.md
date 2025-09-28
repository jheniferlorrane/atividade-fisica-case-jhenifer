# 🌐 Endpoints da API REST

## 📋 Resumo dos Endpoints

| Método | Endpoint              | Função                  | Status Esperado |
| ------ | --------------------- | ----------------------- | --------------- |
| GET    | `/atividades`         | Listar todas atividades | 200 OK          |
| GET    | `/atividades?filtros` | Listar com filtros      | 200 OK          |
| GET    | `/atividades/{id}`    | Buscar por ID           | 200 OK / 404    |
| POST   | `/atividades`         | Criar nova atividade    | 201 Created     |
| PUT    | `/atividades/{id}`    | Atualizar atividade     | 200 OK / 404    |
| DELETE | `/atividades/{id}`    | Deletar atividade       | 204 No Content  |

---

## 🔍 GET /atividades - Listar Atividades

### **Endpoint Básico**

```http
GET http://localhost:8080/atividades
```

### **Com Filtros Avançados**

```http
GET http://localhost:8080/atividades?funcional=12345&codigoAtividade=ACT001&descricaoAtividade=Yoga
```

### **Parâmetros de Filtro**

| Parâmetro            | Tipo   | Obrigatório | Descrição                   | Exemplo  |
| -------------------- | ------ | ----------- | --------------------------- | -------- |
| `funcional`          | String | Não         | Filtro por funcional        | `12345`  |
| `codigoAtividade`    | String | Não         | Filtro por código           | `ACT001` |
| `descricaoAtividade` | String | Não         | Filtro por descrição (LIKE) | `Yoga`   |

### **Exemplos de Response**

**✅ Sucesso (200 OK)**

```json
[
  {
    "id": 1,
    "funcional": "12345",
    "codigoAtividade": "ACT001",
    "descricaoAtividade": "Yoga Matinal",
    "dataHora": "2024-01-15T08:00:00"
  },
  {
    "id": 2,
    "funcional": "67890",
    "codigoAtividade": "ACT002",
    "descricaoAtividade": "Corrida no Parque",
    "dataHora": "2024-01-15T18:30:00"
  }
]
```

**📋 Lista Vazia (200 OK)**

```json
[]
```

### **cURL**

```bash
curl -X GET http://localhost:8080/atividades
curl -X GET "http://localhost:8080/atividades?funcional=12345"
curl -X GET "http://localhost:8080/atividades?funcional=12345&codigoAtividade=ACT001&descricaoAtividade=Yoga"
```

---

## 👁️ GET /atividades/{id} - Buscar por ID

### **Endpoint**

```http
GET http://localhost:8080/atividades/{id}
```

### **Parâmetros**

| Parâmetro | Tipo | Localização | Obrigatório | Descrição       |
| --------- | ---- | ----------- | ----------- | --------------- |
| `id`      | Long | Path        | Sim         | ID da atividade |

### **Exemplos de Response**

**✅ Encontrado (200 OK)**

```json
{
  "id": 1,
  "funcional": "12345",
  "codigoAtividade": "ACT001",
  "descricaoAtividade": "Yoga Matinal",
  "dataHora": "2024-01-15T08:00:00"
}
```

**❌ Não Encontrado (404 Not Found)**

```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Atividade não encontrada com ID: 999",
  "path": "/atividades/999"
}
```

### **cURL**

```bash
curl -X GET http://localhost:8080/atividades/1
```

---

## ➕ POST /atividades - Criar Atividade

### **Endpoint**

```http
POST http://localhost:8080/atividades
Content-Type: application/json
```

### **Request Body**

```json
{
  "funcional": "12345",
  "codigoAtividade": "ACT001",
  "descricaoAtividade": "Yoga Matinal",
  "dataHora": "2024-01-15T08:00:00"
}
```

### **Validações**

Todos os campos são obrigatórios. Detalhes completos em [DADOS-VALIDACOES.md](DADOS-VALIDACOES.md)

### **Exemplos de Response**

**✅ Criado com Sucesso (201 Created)**

```json
{
  "id": 3,
  "funcional": "12345",
  "codigoAtividade": "ACT001",
  "descricaoAtividade": "Yoga Matinal",
  "dataHora": "2024-01-15T08:00:00"
}
```

**❌ Erro de Validação (400 Bad Request)**

```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "funcional",
      "message": "Funcional não pode estar vazio"
    },
    {
      "field": "descricaoAtividade",
      "message": "Descrição não pode ter mais de 255 caracteres"
    }
  ]
}
```

### **cURL**

```bash
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "12345",
    "codigoAtividade": "ACT001",
    "descricaoAtividade": "Yoga Matinal",
    "dataHora": "2024-01-15T08:00:00"
  }'
```

---

## ✏️ PUT /atividades/{id} - Atualizar Atividade

### **Endpoint**

```http
PUT http://localhost:8080/atividades/{id}
Content-Type: application/json
```

### **Parâmetros**

| Parâmetro | Tipo | Localização | Obrigatório | Descrição       |
| --------- | ---- | ----------- | ----------- | --------------- |
| `id`      | Long | Path        | Sim         | ID da atividade |

### **Request Body**

```json
{
  "funcional": "54321",
  "codigoAtividade": "ACT002",
  "descricaoAtividade": "Yoga Vespertino - Atualizado",
  "dataHora": "2024-01-15T18:00:00"
}
```

### **Exemplos de Response**

**✅ Atualizado com Sucesso (200 OK)**

```json
{
  "id": 1,
  "funcional": "54321",
  "codigoAtividade": "ACT002",
  "descricaoAtividade": "Yoga Vespertino - Atualizado",
  "dataHora": "2024-01-15T18:00:00"
}
```

**❌ Atividade Não Encontrada (404 Not Found)**

```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Atividade não encontrada com ID: 999",
  "path": "/atividades/999"
}
```

### **cURL**

```bash
curl -X PUT http://localhost:8080/atividades/1 \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "54321",
    "codigoAtividade": "ACT002",
    "descricaoAtividade": "Yoga Vespertino - Atualizado",
    "dataHora": "2024-01-15T18:00:00"
  }'
```

---

## 🗑️ DELETE /atividades/{id} - Deletar Atividade

### **Endpoint**

```http
DELETE http://localhost:8080/atividades/{id}
```

### **Parâmetros**

| Parâmetro | Tipo | Localização | Obrigatório | Descrição       |
| --------- | ---- | ----------- | ----------- | --------------- |
| `id`      | Long | Path        | Sim         | ID da atividade |

### **Exemplos de Response**

**✅ Deletado com Sucesso (204 No Content)**

```
(Sem body - apenas status 204)
```

**❌ Atividade Não Encontrada (404 Not Found)**

```json
{
  "timestamp": "2024-01-15T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Atividade não encontrada com ID: 999",
  "path": "/atividades/999"
}
```

### **cURL**

```bash
curl -X DELETE http://localhost:8080/atividades/1
```

---

**Para testes detalhados consulte:** [TESTES-INTEGRACAO.md](TESTES-INTEGRACAO.md)
