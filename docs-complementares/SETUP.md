# 🔧 Setup e Instalação

## 📋 Pré-requisitos

**Requisitos obrigatórios:**
- **Java 17+** - Runtime da aplicação
- **Docker & Docker Compose** - Containers e orquestração
- **Git** - Clone do repositório

**Verificar se já estão instalados:**
```bash
java -version      # Deve mostrar Java 17+
docker --version   # Docker Engine
docker-compose --version  # Docker Compose
git --version      # Git
```

### **Instalação Rápida**

#### **Java 17 (se não tiver):**
- **Windows/macOS:** https://adoptium.net/ → OpenJDK 17 LTS
- **Ubuntu:** `sudo apt install openjdk-17-jdk`
- **macOS:** `brew install openjdk@17`

#### **Docker (se não tiver):**
- **Windows/macOS:** https://www.docker.com/products/docker-desktop/
- **Ubuntu:** 
```bash
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
```

---

## ⚡ Como Rodar a Aplicação

### **Opção 1: Docker Compose (Recomendado)**

**🎯 Ideal para:** Primeira execução, testes, demonstrações

**Passos:**
```bash
# 1. Clone o repositório
git clone https://github.com/jheniferlorrane/atividade-fisica-case-jhenifer.git
cd atividade-fisica-case-jhenifer

# 2. Execute com Docker Compose
docker-compose up -d

# 3. Aguarde inicialização (30-60 segundos)
docker-compose logs -f app

# 4. Teste se está funcionando
curl http://localhost:8080/atividades
```

**O que acontece:**
- ✅ MySQL 8.0 na porta 3307
- ✅ Spring Boot App na porta 8080
- ✅ Volume para persistência de dados
- ✅ Banco criado automaticamente

**Comandos úteis:**
```bash
# Ver status dos containers
docker-compose ps

# Ver logs da aplicação
docker-compose logs -f app

# Ver logs do MySQL
docker-compose logs -f mysql

# Parar tudo
docker-compose down

# Parar e limpar dados
docker-compose down -v
```

### **Opção 2: MySQL no Docker + IDE para desenvolvimento**

**🎯 Ideal para:** Desenvolvimento ativo, debugging, modificações no código

**Passos:**
```bash
# 1. Subir apenas o MySQL
docker-compose up mysql -d

# 2. Verificar se MySQL está rodando
docker-compose logs mysql

# 3. Abrir projeto na IDE
# - IntelliJ IDEA: File → Open → pasta 'back-end'
# - VS Code: Abrir pasta 'back-end'

# 4. Executar AtividadeApplication.java na IDE
# Ou via linha de comando:
cd back-end
./mvnw spring-boot:run    # Linux/macOS
mvnw.cmd spring-boot:run  # Windows
```

**Vantagens desta opção:**
- ✅ Hot reload ao modificar código
- ✅ Debug direto na IDE
- ✅ Logs formatados na IDE
- ✅ Rebuild mais rápido

### **Opção 3: Tudo local (sem Docker)**

**🎯 Ideal para:** Ambientes corporativos sem Docker

**Pré-requisitos adicionais:**
- MySQL 8.0 instalado localmente
- Maven 3.6+ (opcional, projeto tem wrapper)

**Configuração MySQL:**
```sql
# Conectar como root
mysql -u root -p

# Criar banco e usuário
CREATE DATABASE atividade_db;
CREATE USER 'atividade_user'@'localhost' IDENTIFIED BY 'senha123';
GRANT ALL PRIVILEGES ON atividade_db.* TO 'atividade_user'@'localhost';
FLUSH PRIVILEGES;
```

**Configurar aplicação:**
```bash
# Editar back-end/src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/atividade_db
spring.datasource.username=atividade_user
spring.datasource.password=senha123
```

**Executar:**
```bash
cd back-end
./mvnw spring-boot:run
```

---

## 🗄️ Configuração do Banco de Dados

### **Configuração Automática (Docker)**

O projeto está configurado para funcionar "out of the box":

```yaml
# docker-compose.yml
mysql:
  image: mysql:8.0
  environment:
    MYSQL_ROOT_PASSWORD: rootpassword
    MYSQL_DATABASE: atividade_db
    MYSQL_USER: atividade_user
    MYSQL_PASSWORD: userpassword
  ports:
    - "3307:3306"  # Porta externa 3307 para não conflitar
```

### **Estrutura da Tabela**

A aplicação cria automaticamente:

```sql
CREATE TABLE atividade (
    id_atividade BIGINT AUTO_INCREMENT PRIMARY KEY,
    funcional VARCHAR(50) NOT NULL,
    data_hora DATETIME NOT NULL,
    codigo_atividade VARCHAR(20) NOT NULL,
    descricao_atividade VARCHAR(255) NOT NULL,
    
    -- Índices para performance
    INDEX idx_funcional (funcional),
    INDEX idx_codigo_atividade (codigo_atividade)
);
```

### **Dados de Exemplo (Opcional)**

```bash
# Executar script SQL (se desejar)
mysql -u root -p < bd/script.sql

# Ou conectar e inserir manualmente
docker-compose exec mysql mysql -u root -p atividade_db
```

---

## 🔍 Verificação da Instalação

### **Checklist Básico**

#### **1. Verificar containers:**
```bash
docker-compose ps

# Esperado:
# mysql-atividade    Up    0.0.0.0:3307->3306/tcp
# app-atividade      Up    0.0.0.0:8080->8080/tcp
```

#### **2. Testar API:**
```bash
# Health check básico
curl http://localhost:8080/atividades

# Deve retornar: [] (array vazio) ou dados se houver
```

#### **3. Testar CRUD completo:**
```bash
# Criar atividade
curl -X POST http://localhost:8080/atividades \
  -H "Content-Type: application/json" \
  -d '{
    "funcional": "TEST001",
    "dataHora": "2025-09-28T15:00:00",
    "codigoAtividade": "TesteSetup",
    "descricaoAtividade": "Teste de setup"
  }'

# Listar para ver se foi criada
curl http://localhost:8080/atividades

# Testar filtro
curl "http://localhost:8080/atividades?funcional=TEST001"
```

#### **4. Verificar logs:**
```bash
# Logs devem mostrar aplicação iniciada
docker-compose logs app | grep "Started AtividadeApplication"

# Não deve ter erros críticos
docker-compose logs app | grep ERROR
```

### **Métricas de Performance**

```bash
# Tempo de resposta
curl -w "Tempo: %{time_total}s\n" -o /dev/null -s http://localhost:8080/atividades

# Uso de recursos
docker stats --no-stream
```

---

## ⚡ Troubleshooting

### **Problemas Comuns**

#### **"Port 8080 already in use"**
```bash
# Identificar processo
lsof -i :8080        # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Matar processo ou alterar porta no docker-compose.yml
ports:
  - "8081:8080"  # Usar porta 8081
```

#### **"Connection refused to MySQL"**
```bash
# Verificar se MySQL está rodando
docker-compose logs mysql

# Aguardar inicialização completa (pode demorar 30-60s na primeira vez)
# Reiniciar se necessário
docker-compose restart mysql
```

#### **"Application failed to start"**
```bash
# Ver logs detalhados
docker-compose logs app

# Problemas comuns:
# - Aguardar MySQL inicializar
# - Verificar memória disponível
# - Verificar se Java 17+ está sendo usado
```

#### **API não responde**
```bash
# Verificar se aplicação está rodando
docker-compose ps

# Testar conectividade básica
curl -i http://localhost:8080/actuator/health

# Se não responder, verificar logs
docker-compose logs -f app
```

### **Comandos de Debug**

```bash
# Entrar no container da aplicação
docker-compose exec app bash

# Entrar no MySQL
docker-compose exec mysql mysql -u root -p

# Verificar network entre containers
docker-compose exec app ping mysql

# Reiniciar stack completa
docker-compose down && docker-compose up -d
```

### **Variáveis de Ambiente**

Para configurações customizadas, crie arquivo `.env`:

```bash
# .env
MYSQL_ROOT_PASSWORD=suaSenhaSegura
MYSQL_PASSWORD=outraSenhaSegura
DB_HOST=mysql
DB_PORT=3306
APP_PORT=8080
```

---

## 🚀 Próximos Passos

### **Após instalação bem-sucedida:**

1. **Explorar API:** Acesse http://localhost:8080/swagger-ui.html
2. **Testar endpoints:** Consulte **TESTES.md**
3. **Configurar rede:** Veja **REDE.md** para acesso remoto
4. **Monitorar logs:** Leia **LOGGING.md**

### **Para desenvolvimento:**

1. **Configure IDE** com plugins Spring Boot
2. **Import projeto** Maven (pasta back-end)
3. **Configure profile** de desenvolvimento
4. **Use MySQL no Docker** + aplicação na IDE

### **Para produção:**

1. **Configure SSL/TLS**
2. **Use profiles específicos**
3. **Configure monitoramento**
4. **Implemente backup automatizado**

---

**🎯 Setup concluído! Sua API está pronta para uso!**

**Links úteis:**
- **API:** http://localhost:8080/atividades
- **Swagger:** http://localhost:8080/swagger-ui.html


## 👩‍💻 Desenvolvido por

**Jhenifer Lorrane Anacleto Rodrigues**  
_Case Técnico - API REST - Atividades Físicas_