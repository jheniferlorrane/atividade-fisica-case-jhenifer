# Setup com Docker Compose

## O que é Docker?

Docker é uma plataforma de containerização que permite empacotar aplicações e suas dependências em containers isolados. Cada container é como uma "caixinha" que já vem com tudo pronto para rodar uma aplicação, incluindo:

- Sistema operacional base
- Runtime da linguagem (Java, Node.js)
- Dependências e bibliotecas
- Configurações específicas

### **Vantagens do Docker**

- **Consistência**: Mesmo ambiente em desenvolvimento, teste e produção
- **Isolamento**: Aplicações não interferem umas nas outras
- **Portabilidade**: Roda em qualquer máquina com Docker
- **Escalabilidade**: Fácil replicação de serviços

---

## O que é Docker Compose?

Docker Compose é uma ferramenta para definir e executar aplicações multi-container. Com um único arquivo YAML (`docker-compose.yml`), você pode:

- Definir todos os serviços da aplicação
- Configurar redes entre containers
- Gerenciar volumes de dados
- Orquestrar a inicialização dos serviços

---

## 🏗️ Arquitetura dos Containers

### 📊 Visão Geral

```
┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐
│   Frontend          │  │   Backend           │  │   Database          │
│   (atividade_       │  │   (atividade_app)   │  │   (mysql_atividade) │
│    frontend)        │  │                     │  │                     │
│                     │  │   Maven + Java 17   │  │   MySQL 8.0.35      │
│   Node.js 20        │  │   Spring Boot       │  │                     │
│   React + Vite      │  │   Port: 8080        │  │   Port: 3306        │
│   Port: 5173        │  │                     │  │   (mapped to 3307)  │
└─────────────────────┘  └─────────────────────┘  └─────────────────────┘
         │                         │                         │
         └─────────────────────────┼─────────────────────────┘
                                   │
                       ┌─────────────────────┐
                       │   Docker Network    │
                       │   (bridge)          │
                       └─────────────────────┘
```

> **Nota para Iniciantes**: Este diagrama mostra como os 3 "containers" (caixinhas isoladas) se comunicam entre si. Pense em cada container como um computador virtual independente:
>
> - **Frontend**: a tela que você vê no navegador
> - **Backend**: o "cérebro" que processa os dados
> - **Database**: onde os dados ficam guardados
> - **Docker Network**: a "ponte" que permite eles conversarem entre si

---

## Detalhamento dos Serviços

### 🗄️ **MySQL Container** (`mysql_atividade`)

```yaml
mysql:
  image: mysql:8.0.35
  container_name: mysql_atividade
  restart: always
  environment:
    MYSQL_ROOT_PASSWORD: rootpass
    MYSQL_DATABASE: atividade
    MYSQL_USER: user
    MYSQL_PASSWORD: pass
  command: --default-authentication-plugin=mysql_native_password
  ports:
    - "0.0.0.0:3307:3306"
  volumes:
    - mysql_data:/var/lib/mysql
```

**Características:**

- **Imagem**: MySQL oficial versão 8.0.35
- **Porta**: 3307 (externa) → 3306 (interna)
- **Volume**: Dados persistentes em `mysql_data`
- **Autenticação**: Plugin nativo para compatibilidade
- **Rede**: Acesso de qualquer IP (0.0.0.0)

### ⚙️ **Backend Container** (`atividade_app`)

```yaml
app:
  image: maven:3.9-eclipse-temurin-17
  container_name: atividade_app
  restart: always
  working_dir: /app
  volumes:
    - ./back-end:/app
    - ~/.m2:/root/.m2
  command: bash -c "mvn clean package -DskipTests && java -jar target/atividade-0.0.1-SNAPSHOT.jar"
  ports:
    - "0.0.0.0:8080:8080"
  depends_on:
    - mysql
  environment:
    - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/atividade
    - SPRING_DATASOURCE_USERNAME=user
    - SPRING_DATASOURCE_PASSWORD=pass
    - SERVER_ADDRESS=0.0.0.0
```

**Características:**

- **Imagem**: Maven com OpenJDK 17 (Eclipse Temurin)
- **Build**: Compilação automática com Maven
- **Volume**: Código-fonte montado + cache Maven (.m2)
- **Dependências**: Aguarda MySQL estar pronto
- **Variáveis**: Configuração dinâmica via environment

### 🎨 **Frontend Container** (`atividade_frontend`)

```yaml
frontend:
  image: node:20-alpine
  container_name: atividade_frontend
  restart: always
  working_dir: /app
  volumes:
    - ./front-end:/app
    - /app/node_modules
  command: sh -c "npm install && npm run dev -- --host 0.0.0.0"
  ports:
    - "0.0.0.0:5173:5173"
  depends_on:
    - app
  environment:
    - VITE_API_URL=/api
```

**Características:**

- **Imagem**: Node.js 20 Alpine (leve)
- **Dev Server**: Vite com hot-reload
- **Volume**: Código + node_modules isolado
- **Porta**: 5173 acessível externamente
- **Dependências**: Aguarda API estar disponível

---

## Como Executar o Projeto

### ⚡ **Execução Rápida**

```bash
# Subir todos os serviços
docker-compose up -d

# Ver logs em tempo real
docker-compose logs -f

# Parar todos os serviços
docker-compose down
```

### 🔧 **Comandos Úteis**

```bash
# Status dos containers
docker-compose ps

# Rebuild completo
docker-compose up --build

# Subir serviço específico
docker-compose up mysql -d

# Logs de serviço específico
docker-compose logs app
docker-compose logs frontend
docker-compose logs mysql

# Executar comando dentro do container
docker-compose exec app bash
docker-compose exec mysql mysql -u user -p atividade

# Parar e remover volumes (CUIDADO: apaga dados do MySQL)
docker-compose down -v
```

---

## Fluxo de Inicialização

### 📈 **Ordem de Startup**

1. **MySQL** inicia primeiro

   - Criação do banco `atividade`
   - Configuração do usuário `user`
   - Aguarda estar "healthy"

2. **Backend** aguarda MySQL

   - Maven baixa dependências
   - Compila o projeto (`mvn clean package`)
   - Executa o JAR gerado
   - Spring Boot conecta com MySQL
   - API fica disponível em :8080

3. **Frontend** aguarda Backend
   - `npm install` das dependências
   - Inicia Vite dev server
   - Interface fica disponível em :5173
   - Conecta com API via axios

### ⏱️ **Tempos Estimados**

| Serviço   | Tempo de Inicialização |
| --------- | ---------------------- |
| MySQL     | ~10-15 segundos        |
| Backend   | ~30-45 segundos        |
| Frontend  | ~15-20 segundos        |
| **Total** | **~60-80 segundos**    |

---

## Configuração de Rede

### 🔗 **Comunicação Inter-Containers**

Os containers se comunicam através de uma rede Docker interna:

```bash
# Frontend → Backend
http://atividade_app:8080/atividades

# Backend → MySQL
jdbc:mysql://mysql:3306/atividade
```

### 🌍 **Acesso Externo**

| Serviço  | Container Port | Host Port | URL Externa           |
| -------- | -------------- | --------- | --------------------- |
| Frontend | 5173           | 5173      | http://localhost:5173 |
| Backend  | 8080           | 8080      | http://localhost:8080 |
| MySQL    | 3306           | 3307      | localhost:3307        |

### 📡 **Configuração para Rede Local**

Para acessar de outras máquinas na rede local:

1. **Descobrir IP da máquina host**:

   ```bash
   # Windows
   ipconfig | findstr IPv4

   # Linux/Mac
   ip addr show | grep inet
   ```

2. **Acessar de outras máquinas**:
   ```bash
   # Substituir <IP-DA-MAQUINA> pelo IP descoberto
   http://<IP-DA-MAQUINA>:5173  # Frontend
   http://<IP-DA-MAQUINA>:8080  # Backend API
   ```

---

## Gerenciamento de Volumes

### 📁 **Volume do MySQL**

```yaml
volumes:
  mysql_data:
```

**Características:**

- **Tipo**: Named volume (gerenciado pelo Docker)
- **Persistência**: Dados sobrevivem a restarts
- **Localização**: `/var/lib/docker/volumes/mysql_data`

> **Para Iniciantes**: Um "named volume" é como um HD externo virtual que o Docker cria e gerencia automaticamente. Mesmo se você parar e reiniciar os containers, todos os dados do banco de dados (suas atividades cadastradas) continuam seguros neste "HD virtual".

### 🔄 **Volumes de Bind Mount**

```yaml
volumes:
  - ./back-end:/app # Código backend
  - ./front-end:/app # Código frontend
  - ~/.m2:/root/.m2 # Cache Maven
  - /app/node_modules # Node modules isolado
```

**Vantagens:**

- **Desenvolvimento**: Mudanças refletem imediatamente
- **Performance**: Cache de dependências
- **Isolamento**: node_modules não interfere com host

> **Para Iniciantes**: "Bind Mount" é como criar uma ponte entre uma pasta do seu computador e uma pasta dentro do container. Quando você edita um arquivo no seu computador, a mudança aparece instantaneamente dentro do container - é assim que você pode desenvolver sem precisar recriar tudo a cada mudança!

---

## Troubleshooting Avançado

### 🚨 **Problemas Comuns e Soluções**

#### ❌ **Erro: "Port already in use"**

```bash
# Verificar processos usando as portas
netstat -tulpn | grep :5173
netstat -tulpn | grep :8080
netstat -tulpn | grep :3307

# Matar processo específico
kill -9 <PID>

# Ou mudar portas no docker-compose.yml
ports:
  - "5174:5173"  # Usar porta diferente
```

#### ❌ **MySQL não conecta**

```bash
# Verificar logs do MySQL
docker-compose logs mysql

# Testar conexão direta
docker-compose exec mysql mysql -u user -p atividade

# Reset completo do volume
docker-compose down -v
docker-compose up mysql -d
```

#### ❌ **Backend não compila**

```bash
# Limpar cache Maven
docker-compose exec app mvn clean

# Rebuild forçado
docker-compose up --build app

# Verificar logs detalhados
docker-compose logs app | head -100
```

#### ❌ **Frontend não carrega**

```bash
# Verificar node_modules
docker-compose exec frontend ls -la node_modules

# Reinstalar dependências
docker-compose exec frontend rm -rf node_modules
docker-compose restart frontend

# Verificar variáveis de ambiente
docker-compose exec frontend env | grep VITE
```

### 🔍 **Debug de Performance**

```bash
# Monitorar recursos dos containers
docker stats

# Verificar logs de sistema
dmesg | tail

# Espaço em disco
df -h
docker system df
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
