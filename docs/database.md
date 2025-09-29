# Banco de Dados - MySQL

## Visão Geral

O projeto utiliza **MySQL 8.0.35** como sistema de gerenciamento de banco de dados relacional, escolhido por sua:

- **Robustez**: Suporte a transações **ACID** (Atomicidade, Consistência, Isolamento, Durabilidade - garantindo que operações no banco sejam confiáveis)
- **Performance**: Otimizações para consultas relacionais (busca dados rapidamente)
- **Compatibilidade**: Funciona bem com **JPA** (Java Persistence API - ferramenta para trabalhar com banco em Java)
- **Escalabilidade**: Capacidade para crescimento futuro (aguentar mais usuários)
- **Comunidade**: Vasta documentação e suporte (muita gente usa, então há ajuda disponível)

> **O que é ACID?** É um conjunto de propriedades que garantem que operações no banco sejam seguras:
>
> - **A**tomicidade: Ou tudo funciona, ou nada funciona (não fica pela metade)
> - **C**onsistência: Dados sempre ficam em estado válido
> - **I**solamento: Operações simultâneas não interferem uma na outra
> - **D**urabilidade: Dados salvos não se perdem

---

## Estrutura do Banco

### **Script de Criação (Automático)**

O Docker executa automaticamente este script quando sobe o banco pela primeira vez:

```sql
CREATE DATABASE IF NOT EXISTS atividade;
USE atividade;

CREATE TABLE IF NOT EXISTS atividade (
    id_atividade BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único da atividade',
    funcional VARCHAR(50) NOT NULL COMMENT 'Código funcional do funcionário',
    data_hora DATETIME NOT NULL COMMENT 'Data e hora da atividade',
    codigo_atividade VARCHAR(20) NOT NULL COMMENT 'Tipo da atividade física',
    descricao_atividade VARCHAR(255) NOT NULL COMMENT 'Descrição detalhada'
);
```

> **O que nosso script especifica:**
>
> - **AUTO_INCREMENT**: Banco gera IDs automaticamente (1, 2, 3...)
> - **PRIMARY KEY**: Chave principal que identifica unicamente cada registro
> - **NOT NULL**: Campos obrigatórios, não podem ficar vazios
> - **COMMENT**: Comentários explicativos para cada campo

### 🌐 **Como Conectar**

| Parâmetro    | Valor no Docker          | Explicação                            |
| ------------ | ------------------------ | ------------------------------------- |
| **Host**     | `localhost` (ou `mysql`) | Endereço do servidor                  |
| **Porta**    | `3307` (externa)         | Porta para conectar de fora do Docker |
| **Database** | `atividade`              | Nome do banco de dados                |
| **Usuário**  | `user`                   | Nome de usuário para conectar         |
| **Senha**    | `pass`                   | Senha do usuário                      |

> **Por que porta 3307?** O Docker mapeia a porta interna 3306 (padrão do MySQL) para 3307 externa para evitar conflitos se você já tiver MySQL instalado na máquina.

## � Estrutura da Tabela

### 🏃‍♂️ **Tabela: `atividade`**

Baseada no script que é executado automaticamente:

```sql
CREATE TABLE IF NOT EXISTS atividade (
    id_atividade BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único da atividade',
    funcional VARCHAR(50) NOT NULL COMMENT 'Código funcional do funcionário',
    data_hora DATETIME NOT NULL COMMENT 'Data e hora da atividade',
    codigo_atividade VARCHAR(20) NOT NULL COMMENT 'Tipo da atividade física',
    descricao_atividade VARCHAR(255) NOT NULL COMMENT 'Descrição detalhada'
);
```

### 📊 **Explicação dos Campos**

| Campo                   | Tipo           | Obrigatório | O que armazena                   | Exemplos                   |
| ----------------------- | -------------- | ----------- | -------------------------------- | -------------------------- |
| **id_atividade**        | `BIGINT`       | Automático  | Número único da atividade        | `1, 2, 3...`               |
| **funcional**           | `VARCHAR(50)`  | Sim         | Código do funcionário na empresa | `EMP001`, `FUNC123`        |
| **data_hora**           | `DATETIME`     | Sim         | Quando a atividade foi feita     | `2025-09-28 07:30:00`      |
| **codigo_atividade**    | `VARCHAR(20)`  | Sim         | Tipo de exercício                | `RUN`, `WALK`, `GYM`       |
| **descricao_atividade** | `VARCHAR(255)` | Sim         | Descrição do que foi feito       | `Corrida de 5km no parque` |

> **Tipos de dados usados no nosso script:**
>
> - **BIGINT AUTO_INCREMENT PRIMARY KEY**: ID único que cresce automaticamente
> - **VARCHAR(50) NOT NULL**: Texto obrigatório de até 50 caracteres
> - **VARCHAR(20) NOT NULL**: Texto obrigatório de até 20 caracteres
> - **VARCHAR(255) NOT NULL**: Texto obrigatório de até 255 caracteres
> - **DATETIME NOT NULL**: Data e hora obrigatórias (formato: AAAA-MM-DD HH:MM:SS)
> - **COMMENT**: Comentários que explicam cada campo

## Como Java Acessa o Banco (JPA/Hibernate)

### 🏛️ **Entidade JPA (Representação Java da Tabela)**

```java
@Data
@Entity
@Table(name = "atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Long idAtividade;

    @Column(name = "funcional")
    private String funcional;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "codigo_atividade")
    private String codigoAtividade;

    @Column(name = "descricao_atividade")
    private String descricaoAtividade;
}
```

> **Explicação das anotações:**
>
> - **@Entity**: Marca que esta classe representa uma tabela do banco
> - **@Table(name = "atividade")**: Especifica o nome da tabela
> - **@Id**: Marca o campo como chave primária
> - **@GeneratedValue**: Valor gerado automaticamente pelo banco
> - **@Column(name = "...")**: Liga o campo Java ao campo da tabela
> - **@Data**: Lombok gera getters, setters, toString automaticamente

### ⚙️ **Configurações Automáticas do Spring**

O Spring Boot configura tudo automaticamente com base no `application.properties`:

```properties
# Estas configurações são aplicadas automaticamente:
spring.jpa.hibernate.ddl-auto=update  # Atualiza tabelas se necessário
spring.jpa.show-sql=false             # Não mostra SQL no console
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.open-in-view=false         # Otimização de performance
```

> **O que significa cada configuração:**
>
> - **ddl-auto=update**: Se você mudar a entidade, Spring atualiza a tabela automaticamente
> - **show-sql=false**: SQL não aparece no console (muda para true se quiser ver)
> - **MySQL8Dialect**: Fala para o Hibernate usar recursos específicos do MySQL 8
> - **open-in-view=false**: Otimização que melhora performance da aplicação

## Exemplos de Consultas SQL

### 🔍 **Consultas Básicas (que a aplicação faz automaticamente)**

```sql
-- Listar todas as atividades (GET /atividades)
SELECT * FROM atividade ORDER BY data_hora DESC;

-- Buscar atividades de um funcionário (GET /atividades?funcional=EMP001)
SELECT * FROM atividade
WHERE funcional = 'EMP001'
ORDER BY data_hora DESC;

-- Filtrar por tipo de atividade (GET /atividades?codigoAtividade=RUN)
SELECT * FROM atividade
WHERE codigo_atividade = 'RUN'
ORDER BY data_hora DESC;

-- Buscar por ID (GET /atividades/1)
SELECT * FROM atividade WHERE id_atividade = 1;

-- Criar nova atividade (POST /atividades)
INSERT INTO atividade (funcional, data_hora, codigo_atividade, descricao_atividade)
VALUES ('EMP001', '2025-09-28 07:30:00', 'RUN', 'Corrida matinal');

-- Atualizar atividade (PUT /atividades/1)
UPDATE atividade
SET descricao_atividade = 'Corrida de 10km'
WHERE id_atividade = 1;

-- Deletar atividade (DELETE /atividades/1)
DELETE FROM atividade WHERE id_atividade = 1;
```

### 📈 **Consultas para Relatórios**

```sql
-- Quantas atividades cada funcionário fez
SELECT funcional, COUNT(*) as total_atividades
FROM atividade
GROUP BY funcional
ORDER BY total_atividades DESC;

-- Quais exercícios são mais populares
SELECT codigo_atividade, COUNT(*) as vezes_feito
FROM atividade
GROUP BY codigo_atividade
ORDER BY vezes_feito DESC;

-- Atividades do mês atual
SELECT DATE_FORMAT(data_hora, '%Y-%m') as mes, COUNT(*) as total
FROM atividade
WHERE data_hora >= '2025-09-01'
GROUP BY DATE_FORMAT(data_hora, '%Y-%m');

-- Em que horários as pessoas mais se exercitam
SELECT HOUR(data_hora) as hora, COUNT(*) as atividades
FROM atividade
GROUP BY HOUR(data_hora)
ORDER BY hora;
```

> **Explicação das funções SQL:**
>
> - **COUNT(\*)**: Conta quantos registros
> - **GROUP BY**: Agrupa resultados (tipo "por funcionário", "por tipo")
> - **ORDER BY**: Ordena resultados (ASC = crescente, DESC = decrescente)
> - **DATE_FORMAT()**: Formata datas (ex: '2025-09' para mostrar só ano-mês)
> - **HOUR()**: Extrai só a hora de um datetime

## Configuração Automática via Docker

### 🐳 **Como o Docker Configura Tudo**

O arquivo `docker-compose.yml` configura automaticamente:

```yaml
mysql:
  image: mysql:8.0.35 # Versão específica do MySQL
  container_name: mysql_atividade # Nome do container
  restart: always # Reinicia se parar
  environment:
    MYSQL_ROOT_PASSWORD: rootpass # Senha do administrador
    MYSQL_DATABASE: atividade # Cria o banco automaticamente
    MYSQL_USER: user # Usuário da aplicação
    MYSQL_PASSWORD: pass # Senha do usuário
  ports:
    - "3307:3306" # Porta externa:interna
  volumes:
    - ./bd/script.sql:/docker-entrypoint-initdb.d/init.sql # Executa script automaticamente
```

> **O que acontece automaticamente quando você roda `docker-compose up`:**
>
> 1. Docker baixa a imagem MySQL 8.0.35
> 2. Cria o banco de dados `atividade`
> 3. Cria o usuário `user` com senha `pass`
> 4. Executa o script `bd/script.sql` (cria a tabela)
> 5. Banco fica pronto para usar na porta 3307

### 💾 **Onde os Dados Ficam Salvos**

```bash
# Docker cria um volume automático para persistir dados
# Mesmo se você parar/reiniciar o container, dados não se perdem

# Para ver onde estão os dados:
docker volume ls

# Para fazer backup manual:
docker-compose exec mysql mysqldump -u user -ppass atividade > backup.sql

# Para restaurar backup:
docker-compose exec mysql mysql -u user -ppass atividade < backup.sql
```

> **Persistência de dados:** O Docker automaticamente salva todos os dados em um volume. Isso significa que mesmo se você parar o container e subir novamente, todos os registros de atividades continuam lá.

## 🛠️ Como Acessar o Banco de Dados

### 💻 **Conectar via Terminal**

```bash
# Conectar através do Docker (mais fácil)
docker-compose exec mysql mysql -u user -p atividade
# Vai pedir a senha: pass

# Ou conectar de fora do Docker
mysql -h localhost -P 3307 -u user -p atividade
```

### 🔍 **Comandos Básicos no MySQL**

```sql
-- Ver estrutura da tabela
DESCRIBE atividade;

-- Ver como a tabela foi criada
SHOW CREATE TABLE atividade;
```

### 🖥️ **Ferramentas com Interface Gráfica**

Se você preferir uma interface visual ao invés do terminal:

**Ferramentas gratuitas:**

- **MySQL Workbench** (oficial da Oracle)
- **phpMyAdmin** (interface web)
- **DBeaver** (universal, funciona com vários bancos)

**Configuração para conectar:**

```
Host: localhost
Porta: 3307
Usuário: user
Senha: pass
Banco: atividade
```

> **Dica:** MySQL Workbench é a ferramenta oficial e mais completa. DBeaver é mais simples e leve para uso básico.

## 🧪 **Comandos Úteis para Testes**

```sql
-- Limpar tabela para testes (cuidado!)
TRUNCATE TABLE atividade;

-- Inserir dados de teste
INSERT INTO atividade (funcional, data_hora, codigo_atividade, descricao_atividade) VALUES
('EMP001', '2025-09-28 07:30:00', 'RUN', 'Corrida matinal'),
('EMP002', '2025-09-28 18:45:00', 'GYM', 'Treino de força'),
('EMP001', '2025-09-29 08:00:00', 'WALK', 'Caminhada no parque');

-- Ver últimos registros inseridos
SELECT * FROM atividade ORDER BY id_atividade DESC LIMIT 5;

-- Contar quantos registros tem
SELECT COUNT(*) FROM atividade;

-- Ver estrutura da tabela
DESCRIBE atividade;
```

> **Dica para testes:** Use TRUNCATE para limpar a tabela rapidamente, mas cuidado - ele remove TODOS os dados!

````

### 🧪 **Comandos Úteis para Testes**

```sql
-- Limpar tabela para testes (cuidado!)
TRUNCATE TABLE atividade;

-- Inserir dados de teste
INSERT INTO atividade (funcional, data_hora, codigo_atividade, descricao_atividade) VALUES
('EMP001', '2025-09-28 07:30:00', 'RUN', 'Corrida matinal'),
('EMP002', '2025-09-28 18:45:00', 'GYM', 'Treino de força'),
('EMP001', '2025-09-29 08:00:00', 'WALK', 'Caminhada no parque');

-- Ver últimos registros inseridos
SELECT * FROM atividade ORDER BY id_atividade DESC LIMIT 5;
````

> **Dica para testes:** Use TRUNCATE para limpar a tabela rapidamente, mas cuidado - ele remove TODOS os dados!

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
