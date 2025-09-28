# 🏃‍♂️ API REST - Atividade Física
API REST para gestão de atividades físicas de funcionários, desenvolvida em Java com Spring Boot. Inclui endpoints para criação, listagem e filtragem de atividades, validação de dados, testes unitários e documentação via Swagger.

| Tecnologia         | Versão  | Propósito                     |
| ------------------ | ------- | ----------------------------- |
| **Java**           | 17      | Linguagem principal           |
| **Spring Boot**    | 3.5.6   | Framework REST                |
| **MySQL**          | 8.0     | Banco de dados relacional     |
| **Docker Compose** | Latest  | Orquestração de containers    |
| **Maven**          | Wrapper | Gerenciamento de dependências |

### 🎯 Escolha seu caminho:

**🚀 Começar agora:**  
[SETUP.md](doc-complementares/SETUP.md) → [API-ENDPOINTS.md](doc-complementares/API-ENDPOINTS.md)

**🏗️ Entender arquitetura:**  
[ARQUITETURA.md](doc-complementares/ARQUITETURA.md) → [DADOS-VALIDACOES.md](doc-complementares/DADOS-VALIDACOES.md)

**🧪 Testar completamente:**  
[TESTES-INTEGRACAO.md](doc-complementares/TESTES-INTEGRACAO.md) → [LOGGING.md](doc-complementares/LOGGING.md)

**🌐 Acesso externo:**  
[REDE.md](doc-complementares/REDE.md)

---

## ⚡ Quick Start

### **1️⃣ Clone e Entre no Diretório**

```bash
git clone https://github.com/jheniferlorrane/atividade-fisica-case-jhenifer.git
cd atividade-fisica-case-jhenifer
```

### **2️⃣ Suba o Ambiente Docker**

```bash
cd back-end
docker compose up -d
```

### **3️⃣ Acesse a API**

- **Base URL:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### **4️⃣ Teste Rapidamente**

```bash
# Listar atividades
curl -X GET http://localhost:8080/atividades

# Criar nova atividade
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

## 📚 Documentação

**� Acesso direto:** [Api-rest-doc.md](doc-complementares/Api-rest-doc.md) - Navegação completa

**⚡ Links rápidos:**

- [� SETUP](doc-complementares/SETUP.md) - Como rodar
- [🌐 ENDPOINTS](doc-complementares/API-ENDPOINTS.md) - Como usar
- [🧪 TESTES](doc-complementares/TESTES-INTEGRACAO.md) - Como testar

---

## 🎯 API REST

**CRUD completo** com filtros avançados: `funcional`, `codigoAtividade`, `descricaoAtividade`

**Exemplo:** `GET /atividades?funcional=12345&descricaoAtividade=Yoga`

---

## 🔗 Acesso Rápido

- **🌐 API Base:** http://localhost:8080
- **📖 Swagger UI:** http://localhost:8080/swagger-ui.html
- **🗄️ Database:** localhost:3307 (user: `user` / pass: `pass`)
- **📊 Logs:** Estruturados no console com categorias [Controller], [ServiceImpl], [DB]
- **🧪 Collection:** `collection`

---

## 📁 Estrutura do Projeto

```
📁 atividade-fisica-case-jhenifer/
├── 🐳 docker-compose.yml         # Orquestração de containers
├── 📂 back-end/                 # Código Spring Boot + Dockerfile
├── 📂 doc-complementares/       # 📚 Documentação técnica especializada
├── 📂 collection/               # 🧪 Collection do Insomnia
└── 📂 bd/                      # 🗄️ Scripts SQL
```

---

**✨ Desenvolvido por:** [Jhenifer Lorrane Anacleto Rodrigues](https://github.com/jheniferlorrane) | **🎯 Status:** 100% Funcional
