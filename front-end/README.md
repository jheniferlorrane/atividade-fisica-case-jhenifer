# 🌐 Front-end - Atividades Físicas# React + Vite

Interface web moderna e intuitiva para gerenciar atividades físicas de funcionários.This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

## 🚀 TecnologiasCurrently, two official plugins are available:

- **React 18** - Biblioteca para interfaces- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) for Fast Refresh

- **Vite** - Build tool moderna e rápida- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

- **Axios** - Cliente HTTP para API

- **React Hook Form** - Gerenciamento de formulários## React Compiler

- **Date-fns** - Manipulação de datas

The React Compiler is not enabled on this template. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## ⚡ Como Executar

## Expanding the ESLint configuration

### **Pré-requisitos**

- Node.js 18+ If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.

- API backend rodando em `http://localhost:8080`

### **Instalação e Execução**

```bash
# Entrar no diretório
cd front-end

# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm run dev

# Acessar no navegador
http://localhost:5173
```

## 🎯 Funcionalidades

### ✅ **CRUD Completo**

- ➕ **Criar** nova atividade
- 👁️ **Visualizar** lista de atividades
- ✏️ **Editar** atividade existente
- 🗑️ **Deletar** atividade

### 🔍 **Filtros Avançados**

- **Funcional** - Busca exata
- **Código da Atividade** - Busca exata
- **Descrição** - Busca parcial (LIKE)
- **Combinação** - Todos os filtros juntos

### 📱 **Interface Responsiva**

- Design adaptável para mobile e desktop
- Cards organizados em grid responsivo
- Formulários modais elegantes
- Feedback visual para todas as ações

## 🏗️ Estrutura do Código

```
src/
├── components/           # Componentes reutilizáveis
│   ├── FormularioAtividade.jsx   # Modal de criar/editar
│   ├── FiltrosAtividade.jsx      # Barra de filtros
│   └── ListaAtividades.jsx       # Grid de atividades
├── services/
│   └── api.js           # Serviços da API REST
├── App.jsx              # Componente principal
└── main.jsx             # Ponto de entrada
```

## 📝 Campos da Atividade

| Campo         | Tipo     | Validação                  | Exemplo                  |
| ------------- | -------- | -------------------------- | ------------------------ |
| **Funcional** | String   | Obrigatório, máx 50 chars  | "12345"                  |
| **Código**    | String   | Obrigatório, máx 20 chars  | "ACT001", "Yoga"         |
| **Descrição** | String   | Obrigatório, máx 255 chars | "Yoga matinal no parque" |
| **Data/Hora** | DateTime | Obrigatório, formato ISO   | "2024-01-15T08:00:00"    |

## 🔗 Integração com API

### **Base URL**: `http://localhost:8080`

| Método | Endpoint           | Uso no Front-end    |
| ------ | ------------------ | ------------------- |
| GET    | `/atividades`      | Listar + Filtros    |
| GET    | `/atividades/{id}` | Buscar específica   |
| POST   | `/atividades`      | Criar nova          |
| PUT    | `/atividades/{id}` | Atualizar existente |
| DELETE | `/atividades/{id}` | Deletar             |

## 🎨 Design System

### **Cores**

- **Primária**: Gradiente azul/roxo (`#667eea` → `#764ba2`)
- **Sucesso**: Verde (`#4CAF50`)
- **Perigo**: Vermelho suave
- **Fundo**: Cinza claro (`#f5f7fa`)

### **Componentes**

- **Cards** - Interface principal para atividades
- **Modal** - Formulários de criar/editar
- **Grid responsivo** - Layout adaptável
- **Botões** - Estados hover e loading

## 🚀 Build para Produção

```bash
# Gerar build otimizado
npm run build

# Preview do build
npm run preview
```

---

**✨ Interface simples, código limpo e 100% funcional!**
