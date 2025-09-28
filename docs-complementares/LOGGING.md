# 📊 Sistema de Logging e Monitoramento

## 🔍 Visão Geral

A aplicação utiliza **SLF4J + Logback** para logging estruturado e profissional, facilitando debug e monitoramento.

### **Categorias de Log**

#### **[Controller]** - Requisições HTTP
Registra todas as requisições recebidas e suas respostas:
```log
2025-09-28 10:30:45 INFO --- [Controller] POST /atividades - Criando nova atividade para funcional: 123458
2025-09-28 10:30:46 INFO --- [Controller] GET /atividades - Listando todas as atividades
2025-09-28 10:30:47 INFO --- [Controller] GET /atividades/1 - Buscando atividade com ID: 1
```

#### **[ServiceImpl]** - Lógica de Negócio
Mostra processamento interno e validações:
```log
2025-09-28 10:30:45 INFO --- [ServiceImpl] Processando criação de atividade - Funcional: 123458
2025-09-28 10:30:45 INFO --- [ServiceImpl] Atividade criada com sucesso - ID: 1
2025-09-28 10:30:46 WARN --- [ServiceImpl] Tentativa de buscar atividade inexistente - ID: 999
```

#### **[DB]** - Operações de Banco
Registra todas as interações com o banco de dados:
```log
2025-09-28 10:30:45 INFO --- [DB] Salvando atividade no banco: Atividade{funcional='123458'}
2025-09-28 10:30:45 INFO --- [DB] Atividade salva com sucesso - ID gerado: 1
2025-09-28 10:30:46 INFO --- [DB] Executando busca por filtros - Funcional: 123458
```

---

## 📈 Como Visualizar os Logs

### **Via Docker Compose**

```bash
# Logs da aplicação em tempo real
docker-compose logs -f app

# Logs das últimas 100 linhas
docker-compose logs --tail=100 app

# Logs do MySQL
docker-compose logs -f mysql

# Todos os serviços
docker-compose logs -f

# Filtrar por nível
docker-compose logs app | grep ERROR
docker-compose logs app | grep WARN
```

### **Via IDE (IntelliJ/VS Code)**

Quando executar a aplicação na IDE, os logs aparecem no console com:
- 🟢 **INFO** - Operações normais
- 🟡 **WARN** - Situações de atenção  
- 🔴 **ERROR** - Problemas/exceções
- 🔵 **DEBUG** - Informações detalhadas (apenas em dev)

---

## 🎯 Exemplos de Logs por Cenário

### **Criação de Atividade (Sucesso)**
```log
2025-09-28 10:30:45 INFO --- [Controller] POST /atividades - Criando nova atividade para funcional: 123458
2025-09-28 10:30:45 INFO --- [ServiceImpl] Processando criação de atividade - Funcional: 123458, Atividade: Caminhada
2025-09-28 10:30:45 INFO --- [ServiceImpl] Validação realizada com sucesso
2025-09-28 10:30:45 INFO --- [DB] Salvando atividade no banco: Atividade{funcional='123458', codigo='Caminhada'}
2025-09-28 10:30:45 INFO --- [DB] Atividade salva com sucesso - ID gerado: 1
2025-09-28 10:30:45 INFO --- [Controller] Atividade criada com sucesso - ID: 1
```

### **Busca com Filtros**
```log
2025-09-28 10:35:20 INFO --- [Controller] GET /atividades?funcional=123458&codigoAtividade=Corrida
2025-09-28 10:35:20 INFO --- [ServiceImpl] Listando atividades com filtros - Funcional: 123458, CodigoAtividade: Corrida
2025-09-28 10:35:20 INFO --- [DB] Executando consulta com filtros customizados
2025-09-28 10:35:20 INFO --- [DB] Consulta com filtros executada - 2 registros encontrados
```

### **Erro de Validação**
```log
2025-09-28 10:40:30 INFO --- [Controller] POST /atividades - Criando nova atividade
2025-09-28 10:40:30 ERROR --- [ServiceImpl] Erro de validação: Funcional não pode estar vazio
2025-09-28 10:40:30 WARN --- [Controller] Dados de entrada inválidos - BadRequest
```

### **Atividade Não Encontrada**
```log
2025-09-28 10:45:15 INFO --- [Controller] GET /atividades/999 - Buscando atividade com ID: 999
2025-09-28 10:45:15 INFO --- [DB] Executando busca por ID: 999
2025-09-28 10:45:15 WARN --- [ServiceImpl] Atividade não encontrada para ID: 999
2025-09-28 10:45:15 WARN --- [Controller] Atividade não encontrada - ID: 999
```

---

## 🔧 Configuração dos Logs

### **Níveis de Log por Ambiente**

#### **Desenvolvimento (padrão):**
```properties
# application.properties
logging.level.br.com.atividade=INFO
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

#### **Produção:**
```properties
# application-prod.properties
logging.level.br.com.atividade=WARN
logging.level.org.springframework.web=WARN
logging.level.org.hibernate.SQL=WARN
```

### **Formato dos Logs**

```properties
# Console (desenvolvimento)
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} %5p --- [%t] %c{1} : %m%n

# Arquivo (produção)
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} %5p --- [%t] %c{1} : %m%n
logging.file.name=logs/atividade-api.log
logging.file.max-size=10MB
logging.file.max-history=30
```

---

## 📊 Monitoramento com Spring Actuator

### **Endpoints Disponíveis**

```bash
# Health check da aplicação
curl http://localhost:8080/actuator/health

# Informações da aplicação
curl http://localhost:8080/actuator/info

# Métricas gerais
curl http://localhost:8080/actuator/metrics

# Métricas específicas
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/http.server.requests
```

### **Configuração do Actuator**

```properties
# application.properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
management.info.git.mode=full
```

### **Exemplo de Health Check**

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 91943530496
      }
    }
  }
}
```

---

## 🎯 Análise de Performance

### **Métricas Importantes**

```bash
# Tempo de resposta das requisições
curl http://localhost:8080/actuator/metrics/http.server.requests

# Uso de memória JVM
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Conexões do pool de banco
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
```

### **Logs de Performance**

A aplicação registra automaticamente:
```log
2025-09-28 10:30:45 INFO --- [Performance] Tempo de resposta GET /atividades: 45ms
2025-09-28 10:30:45 INFO --- [Performance] Query executada em: 12ms
2025-09-28 10:30:45 INFO --- [Performance] Mapeamento DTO realizado em: 3ms
```

### **Identificação de Problemas**

**🐌 Queries Lentas:**
```log
2025-09-28 10:30:45 WARN --- [DB] Query demorou 250ms: SELECT * FROM atividade WHERE funcional = ?
```

**🔥 Alto Volume de Requisições:**
```log
2025-09-28 10:30:45 WARN --- [Controller] Mais de 100 requisições por minuto detectadas
```

**💾 Problemas de Memória:**
```log
2025-09-28 10:30:45 ERROR --- [JVM] OutOfMemoryError: Java heap space
```

---

## 🔍 Troubleshooting com Logs

### **Cenários Comuns**

#### **API não responde**
```bash
# Verificar se aplicação iniciou
docker-compose logs app | grep "Started AtividadeApplication"

# Verificar porta
docker-compose logs app | grep "Tomcat started on port"
```

#### **Erro de conexão com banco**
```bash
# Verificar conexão MySQL
docker-compose logs app | grep -i "connection"
docker-compose logs mysql | grep "ready for connections"
```

#### **Erro 500 inesperado**
```bash
# Buscar stack traces
docker-compose logs app | grep -A 10 ERROR

# Verificar validações
docker-compose logs app | grep "validation"
```

### **Comandos Úteis para Debug**

```bash
# Logs com timestamp
docker-compose logs -t app

# Seguir logs específicos
docker-compose logs -f app | grep -i error

# Últimos erros
docker-compose logs app | grep ERROR | tail -5

# Estatísticas rápidas
docker-compose logs app | grep INFO | wc -l    # Total de logs INFO
docker-compose logs app | grep ERROR | wc -l   # Total de erros
```

---

## 🚀 Configurações Avançadas

### **Log Rotation**

```properties
# application.properties
logging.logback.rollingpolicy.file-name-pattern=logs/atividade-api.%d{yyyy-MM-dd}.%i.gz
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=30
logging.logback.rollingpolicy.total-size-cap=1GB
```

### **Structured Logging (JSON)**

Para ambientes de produção com sistemas de agregação de logs:

```xml
<!-- logback-spring.xml -->
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
        <providers>
            <timestamp/>
            <logLevel/>
            <loggerName/>
            <message/>
            <mdc/>
        </providers>
    </encoder>
</appender>
```

### **Alertas Automáticos**

```properties
# application.properties (produção)
logging.level.br.com.atividade.alert=ERROR

# Script de monitoramento
#!/bin/bash
ERROR_COUNT=$(docker-compose logs app | grep ERROR | wc -l)
if [ $ERROR_COUNT -gt 10 ]; then
    echo "ALERTA: Mais de 10 erros detectados!" | mail -s "API Alert" admin@empresa.com
fi
```

---

**🎯 Com este sistema de logging você tem visibilidade completa do comportamento da aplicação!**

**Links úteis:**
- **Health Check:** http://localhost:8080/actuator/health
- **Métricas:** http://localhost:8080/actuator/metrics
- **Logs em tempo real:** `docker-compose logs -f app`