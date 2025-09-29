# Banco de Dados - MySQL

## Visão Geral

O projeto utiliza **MySQL 8.0.35** como sistema de gerenciamento de banco de dados relacional, escolhido por sua:

- **Robustez**: Suporte a transações ACID
- **Performance**: Otimizações para consultas relacionais
- **Compatibilidade**: Amplo suporte a ORMs (Hibernate/JPA)
- **Escalabilidade**: Capacidade para crescimento futuro
- **Comunidade**: Vasta documentação e suporte

---

## Arquitetura do Banco

### **Configuração MySQL**

```sql
-- Configurações principais
CREATE DATABASE IF NOT EXISTS atividade
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

-- Engine: InnoDB (padrão)
-- Transações: ACID compliance
-- Isolamento: READ COMMITTED
-- Auto-commit: Habilitado
```

### 🌐 **Conexão e Credenciais**

| Parâmetro    | Desenvolvimento                    | Produção            |
| ------------ | ---------------------------------- | ------------------- |
| **Host**     | `localhost` / `mysql`              | `<prod-host>`       |
| **Porta**    | `3307` (externa), `3306` (interna) | `3306`              |
| **Database** | `atividade`                        | `atividade`         |
| **Usuário**  | `user`                             | `app_user`          |
| **Senha**    | `pass`                             | `<secure-password>` |

**String de Conexão:**

```
jdbc:mysql://localhost:3307/atividade?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true
```

---

## Modelagem de Dados

### 🏃‍♂️ **Tabela: `atividade`**

```sql
CREATE TABLE IF NOT EXISTS atividade (
    id_atividade BIGINT AUTO_INCREMENT PRIMARY KEY
        COMMENT 'ID único da atividade',
    funcional VARCHAR(50) NOT NULL
        COMMENT 'Código funcional do funcionário',
    data_hora DATETIME NOT NULL
        COMMENT 'Data e hora da atividade',
    codigo_atividade VARCHAR(20) NOT NULL
        COMMENT 'Tipo da atividade física',
    descricao_atividade VARCHAR(255) NOT NULL
        COMMENT 'Descrição detalhada'
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;
```

### 📊 **Detalhamento dos Campos**

| Campo                   | Tipo           | Constraints                     | Descrição                        | Exemplo                    |
| ----------------------- | -------------- | ------------------------------- | -------------------------------- | -------------------------- |
| **id_atividade**        | `BIGINT`       | `PRIMARY KEY`, `AUTO_INCREMENT` | Identificador único da atividade | `1, 2, 3...`               |
| **funcional**           | `VARCHAR(50)`  | `NOT NULL`                      | Código do funcionário na empresa | `EMP001`, `FUNC123`        |
| **data_hora**           | `DATETIME`     | `NOT NULL`                      | Data e hora da realização        | `2025-09-28 07:30:00`      |
| **codigo_atividade**    | `VARCHAR(20)`  | `NOT NULL`                      | Código/tipo da atividade         | `RUN`, `WALK`, `GYM`       |
| **descricao_atividade** | `VARCHAR(255)` | `NOT NULL`                      | Descrição detalhada              | `Corrida de 5km no parque` |

### 🔍 **Índices Recomendados**

```sql
-- Índice principal (já existe via PRIMARY KEY)
-- INDEX pk_atividade (id_atividade)

-- Índices para otimização de consultas
CREATE INDEX idx_funcional ON atividade(funcional);
CREATE INDEX idx_data_hora ON atividade(data_hora);
CREATE INDEX idx_codigo_atividade ON atividade(codigo_atividade);

-- Índice composto para filtros múltiplos
CREATE INDEX idx_funcional_data ON atividade(funcional, data_hora);
```

---

## Mapeamento JPA/Hibernate

### 🏛️ **Entidade JPA**

```java
@Entity
@Table(name = "atividade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long idAtividade;

    @Column(name = "funcional", nullable = false, length = 50)
    private String funcional;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "codigo_atividade", nullable = false, length = 20)
    private String codigoAtividade;

    @Column(name = "descricao_atividade", nullable = false)
    private String descricaoAtividade;
}
```

### ⚙️ **Configuração do Hibernate**

```properties
# application.properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

# Configurações de performance
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

---

## Queries de Exemplo

### 🔍 **Consultas Básicas**

```sql
-- Listar todas as atividades
SELECT * FROM atividade ORDER BY data_hora DESC;

-- Buscar atividades de um funcionário
SELECT * FROM atividade
WHERE funcional = 'EMP001'
ORDER BY data_hora DESC;

-- Filtrar por tipo de atividade
SELECT * FROM atividade
WHERE codigo_atividade = 'RUN'
ORDER BY data_hora DESC;

-- Atividades em um período
SELECT * FROM atividade
WHERE data_hora BETWEEN '2025-09-01' AND '2025-09-30'
ORDER BY data_hora ASC;
```

### 📈 **Consultas Analíticas**

```sql
-- Contagem de atividades por funcionário
SELECT funcional, COUNT(*) as total_atividades
FROM atividade
GROUP BY funcional
ORDER BY total_atividades DESC;

-- Atividades mais populares
SELECT codigo_atividade, COUNT(*) as frequencia
FROM atividade
GROUP BY codigo_atividade
ORDER BY frequencia DESC;

-- Relatório mensal
SELECT
    DATE_FORMAT(data_hora, '%Y-%m') as mes,
    COUNT(*) as total_atividades,
    COUNT(DISTINCT funcional) as funcionarios_ativos
FROM atividade
WHERE data_hora >= '2025-01-01'
GROUP BY DATE_FORMAT(data_hora, '%Y-%m')
ORDER BY mes;

-- Horários de pico
SELECT
    HOUR(data_hora) as hora,
    COUNT(*) as atividades
FROM atividade
GROUP BY HOUR(data_hora)
ORDER BY hora;
```

---

## Configuração Docker

### 🐳 **Container MySQL**

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
  command: >
    --default-authentication-plugin=mysql_native_password
    --character-set-server=utf8mb4
    --collation-server=utf8mb4_unicode_ci
    --sql_mode=STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO
  ports:
    - "3307:3306"
  volumes:
    - mysql_data:/var/lib/mysql
    - ./bd/script.sql:/docker-entrypoint-initdb.d/init.sql
```

### 💾 **Persistência de Dados**

```bash
# Volume nomeado para persistir dados
docker volume create mysql_data

# Localização dos dados (Linux/Mac)
/var/lib/docker/volumes/mysql_data/_data

# Backup do volume
docker run --rm -v mysql_data:/source -v $(pwd):/backup alpine tar czf /backup/mysql_backup.tar.gz -C /source .

# Restaurar backup
docker run --rm -v mysql_data:/dest -v $(pwd):/backup alpine tar xzf /backup/mysql_backup.tar.gz -C /dest
```

---

## 🛠️ Ferramentas de Administração

### 💻 **Conexão via CLI**

```bash
# Conectar via Docker
docker-compose exec mysql mysql -u user -p atividade

# Conectar externamente
mysql -h localhost -P 3307 -u user -p atividade
```

### 🔍 **Comandos Úteis**

```sql
-- Verificar estrutura da tabela
DESCRIBE atividade;

-- Ver informações detalhadas
SHOW CREATE TABLE atividade;

-- Estatísticas da tabela
SELECT
    TABLE_NAME,
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    (DATA_LENGTH + INDEX_LENGTH) as TOTAL_SIZE
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'atividade';

-- Verificar índices
SHOW INDEX FROM atividade;

-- Analisar performance de query
EXPLAIN SELECT * FROM atividade WHERE funcional = 'EMP001';
```

### 🖥️ **Cliente Gráfico**

Ferramentas recomendadas:

- **MySQL Workbench** (oficial)
- **phpMyAdmin** (web-based)
- **DBeaver** (universal)
- **DataGrip** (JetBrains)

**Configuração de conexão:**

```
Host: localhost
Port: 3307
Username: user
Password: pass
Database: atividade
```

---

## Monitoramento e Performance

### 📈 **Métricas Importantes**

```sql
-- Status geral do MySQL
SHOW GLOBAL STATUS;

-- Variáveis de configuração
SHOW VARIABLES LIKE '%innodb%';

-- Processos em execução
SHOW PROCESSLIST;

-- Tamanho do banco
SELECT
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'atividade';
```

### ⚡ **Otimizações**

```sql
-- Otimizar tabelas
OPTIMIZE TABLE atividade;

-- Analisar tabelas
ANALYZE TABLE atividade;

-- Verificar integridade
CHECK TABLE atividade;

-- Reparar se necessário
REPAIR TABLE atividade;
```

---

## 🔐 Segurança e Backup

### 🛡️ **Configurações de Segurança**

```sql
-- Criar usuário específico para aplicação
CREATE USER 'app_user'@'%' IDENTIFIED BY 'secure_password_here';

-- Permissões mínimas necessárias
GRANT SELECT, INSERT, UPDATE, DELETE ON atividade.* TO 'app_user'@'%';

-- Não conceder privilégios administrativos
-- REVOKE ALL PRIVILEGES ON *.* FROM 'app_user'@'%';

FLUSH PRIVILEGES;
```

### 💾 **Estratégia de Backup**

```bash
# Backup completo
mysqldump -h localhost -P 3307 -u user -p atividade > backup_$(date +%Y%m%d).sql

# Backup apenas estrutura
mysqldump -h localhost -P 3307 -u user -p --no-data atividade > schema_$(date +%Y%m%d).sql

# Backup apenas dados
mysqldump -h localhost -P 3307 -u user -p --no-create-info atividade > data_$(date +%Y%m%d).sql

# Restaurar backup
mysql -h localhost -P 3307 -u user -p atividade < backup_20250928.sql
```

### 🔄 **Backup Automatizado via Docker**

```bash
# Script de backup automatizado
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
docker-compose exec mysql mysqldump -u user -ppass atividade > "./backups/backup_${DATE}.sql"

# Manter apenas últimos 7 backups
find ./backups -name "backup_*.sql" -mtime +7 -delete
```
