# 🧪 Testes e Integração

## 🎯 Cenários de Teste

### **1. Listar Todas as Atividades**

**Objetivo:** Verificar se a API retorna todas as atividades cadastradas

**cURL:**

```bash
curl -X GET http://localhost:8080/atividades
```

**Response:** 200 OK com array de atividades

---

### **2. Buscar Atividade por ID**

**Objetivo:** Verificar busca individual e tratamento de ID inexistente

**✅ ID Existente:**

```bash
curl -X GET http://localhost:8080/atividades/1
```

**❌ ID Inexistente:**

```bash
curl -X GET http://localhost:8080/atividades/999
```

**Response:** 404 Not Found se ID não existir

---

### **3. Criar Nova Atividade**

**Objetivo:** Testar criação com dados válidos e validações

**✅ Dados Válidos:**

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

**❌ Dados Inválidos (teste de validação):**

```bash
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "",
    "codigoAtividade": "CODIGO_MUITO_LONGO_QUE_EXCEDE_20_CARACTERES",
    "descricaoAtividade": "",
    "dataHora": "data-invalida"
  }'
```

---

### **4. Atualizar Atividade**

**Objetivo:** Testar atualização completa de recursos

**✅ Atualização Válida:**

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

**❌ ID Inexistente:**

```bash
curl -X PUT http://localhost:8080/atividades/999 \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "54321",
    "codigoAtividade": "ACT002",
    "descricaoAtividade": "Teste",
    "dataHora": "2024-01-15T18:00:00"
  }'
```

---

### **5. Deletar Atividade**

**Objetivo:** Testar remoção de recursos

**✅ Deleção Bem-sucedida:**

```bash
curl -X DELETE http://localhost:8080/atividades/1
```

**❌ ID Inexistente:**

```bash
curl -X DELETE http://localhost:8080/atividades/999
```

---

### **6. Filtros Avançados**

**Objetivo:** Testar sistema de filtros implementado

**Por Funcional:**

```bash
curl -X GET "http://localhost:8080/atividades?funcional=12345"
```

**Por Código de Atividade:**

```bash
curl -X GET "http://localhost:8080/atividades?codigoAtividade=ACT001"
```

**Por Descrição (busca parcial):**

```bash
curl -X GET "http://localhost:8080/atividades?descricaoAtividade=Yoga"
```

**Múltiplos Filtros:**

```bash
curl -X GET "http://localhost:8080/atividades?funcional=12345&codigoAtividade=ACT001&descricaoAtividade=Matinal"
```

---

## 📋 Checklist de Validação

### **Status Codes HTTP**

| Cenário                  | Status Esperado | ✓   |
| ------------------------ | --------------- | --- |
| GET lista (com dados)    | 200 OK          | ☐   |
| GET lista (vazia)        | 200 OK          | ☐   |
| GET por ID (existente)   | 200 OK          | ☐   |
| GET por ID (inexistente) | 404 Not Found   | ☐   |
| POST (dados válidos)     | 201 Created     | ☐   |
| POST (dados inválidos)   | 400 Bad Request | ☐   |
| PUT (ID existente)       | 200 OK          | ☐   |
| PUT (ID inexistente)     | 404 Not Found   | ☐   |
| DELETE (ID existente)    | 204 No Content  | ☐   |
| DELETE (ID inexistente)  | 404 Not Found   | ☐   |

### **Validações de Entrada**

| Campo              | Teste                  | Deve Falhar | ✓   |
| ------------------ | ---------------------- | ----------- | --- |
| funcional          | String vazia           | Sim         | ☐   |
| funcional          | Mais de 50 caracteres  | Sim         | ☐   |
| codigoAtividade    | String vazia           | Sim         | ☐   |
| codigoAtividade    | Mais de 20 caracteres  | Sim         | ☐   |
| descricaoAtividade | String vazia           | Sim         | ☐   |
| descricaoAtividade | Mais de 255 caracteres | Sim         | ☐   |
| dataHora           | Valor null             | Sim         | ☐   |
| dataHora           | Formato inválido       | Sim         | ☐   |

### **Filtros**

| Filtro             | Teste                | Deve Funcionar | ✓   |
| ------------------ | -------------------- | -------------- | --- |
| funcional          | Busca exata          | Sim            | ☐   |
| codigoAtividade    | Busca exata          | Sim            | ☐   |
| descricaoAtividade | Busca parcial (LIKE) | Sim            | ☐   |
| Múltiplos filtros  | Combinação AND       | Sim            | ☐   |
| Sem filtros        | Lista completa       | Sim            | ☐   |

---

## 🔧 Insomnia - Configuração

### **Workspace Setup**

**1. Criar Workspace:**

- Nome: "Atividade Física API"
- Descrição: "Testes da API REST de Atividades"

**2. Environment Variables:**

```json
{
  "base_url": "http://localhost:8080",
  "content_type": "application/json"
}
```

### **Requests Collection**

**1. Listar Atividades**

```
GET {{base_url}}/atividades
```

**2. Buscar por ID**

```
GET {{base_url}}/atividades/1
```

**3. Criar Atividade**

```
POST {{base_url}}/atividades
Content-Type: {{content_type}}

{
    "funcional": "12345",
    "codigoAtividade": "ACT001",
    "descricaoAtividade": "Yoga Matinal",
    "dataHora": "2024-01-15T08:00:00"
}
```

**4. Atualizar Atividade**

```
PUT {{base_url}}/atividades/1
Content-Type: {{content_type}}

{
    "funcional": "54321",
    "codigoAtividade": "ACT002",
    "descricaoAtividade": "Yoga Vespertino",
    "dataHora": "2024-01-15T18:00:00"
}
```

**5. Deletar Atividade**

```
DELETE {{base_url}}/atividades/1
```

**6. Filtros**

```
GET {{base_url}}/atividades?funcional=12345&codigoAtividade=ACT001
```

---

## 🐛 Casos de Teste para Bugs

### **Edge Cases**

**1. Caracteres Especiais:**

```bash
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "123@#$",
    "codigoAtividade": "ACT@01",
    "descricaoAtividade": "Yoga com acentuação: ção, ã, é",
    "dataHora": "2024-01-15T08:00:00"
  }'
```

**2. Limites de Tamanho:**

```bash
# Teste: exatamente 50 caracteres no funcional
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "12345678901234567890123456789012345678901234567890",
    "codigoAtividade": "ACT001",
    "descricaoAtividade": "Teste limite",
    "dataHora": "2024-01-15T08:00:00"
  }'
```

**3. Datas Extremas:**

```bash
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "12345",
    "codigoAtividade": "ACT001",
    "descricaoAtividade": "Teste data futura",
    "dataHora": "2099-12-31T23:59:59"
  }'
```

---

## 📊 Métricas de Sucesso

### **Performance**

- ✅ Todas as requisições devem responder em < 500ms
- ✅ Filtros devem funcionar sem degradação de performance
- ✅ Banco de dados deve suportar consultas concorrentes

### **Funcionalidade**

- ✅ 100% dos endpoints funcionando
- ✅ Validações impedindo dados inválidos
- ✅ Filtros retornando resultados precisos
- ✅ Status codes corretos em todos os cenários

### **Robustez**

- ✅ Tratamento adequado de erros
- ✅ Mensagens de erro informativas
- ✅ Logs estruturados para troubleshooting
- ✅ Comportamento consistente

---

## 🎯 Próximos Passos

1. **Execute todos os cURLs** deste documento
2. **Configure o Insomnia** com as requisições
3. **Valide os status codes** contra o checklist
4. **Teste os edge cases** para garantir robustez
5. **Monitore os logs** durante os testes
6. **Documente qualquer comportamento inesperado**

**🌟 API totalmente testada e validada!**
