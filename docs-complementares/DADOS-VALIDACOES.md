# 📊 Dados e Validações

## 🏗️ Estrutura de Dados

### **Entidade Principal: Atividade**

```java
@Entity
@Table(name = "atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funcional", nullable = false, length = 50)
    private String funcional;

    @Column(name = "codigo_atividade", nullable = false, length = 20)
    private String codigoAtividade;

    @Column(name = "descricao_atividade", nullable = false)
    private String descricaoAtividade;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}
```

### **DTO de Entrada (AtividadeInput)**

```json
{
  "funcional": "string",
  "codigoAtividade": "string",
  "descricaoAtividade": "string",
  "dataHora": "2024-01-15T08:00:00"
}
```

### **DTO de Saída (AtividadeOutput)**

```json
{
  "id": 1,
  "funcional": "string",
  "codigoAtividade": "string",
  "descricaoAtividade": "string",
  "dataHora": "2024-01-15T08:00:00"
}
```

---

## ✅ Validações Implementadas

### **AtividadeInput - Bean Validation**

| Campo                | Validações                                                  | Mensagem de Erro                             |
| -------------------- | ----------------------------------------------------------- | -------------------------------------------- |
| `funcional`          | `@NotBlank` `@Size(max = 50)`                               | "Funcional não pode estar vazio/muito longo" |
| `codigoAtividade`    | `@NotBlank` `@Size(max = 20)`                               | "Código não pode estar vazio/muito longo"    |
| `descricaoAtividade` | `@NotBlank` `@Size(max = 255)`                              | "Descrição não pode estar vazia/muito longa" |
| `dataHora`           | `@NotNull` `@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")` | "Data/hora obrigatória e formato inválido"   |

### **Exemplo de Validação no Código**

```java
public class AtividadeInput {
    @NotBlank(message = "Funcional não pode estar vazio")
    @Size(max = 50, message = "Funcional não pode ter mais de 50 caracteres")
    private String funcional;

    @NotBlank(message = "Código da atividade não pode estar vazio")
    @Size(max = 20, message = "Código da atividade não pode ter mais de 20 caracteres")
    private String codigoAtividade;

    @NotBlank(message = "Descrição da atividade não pode estar vazia")
    @Size(max = 255, message = "Descrição da atividade não pode ter mais de 255 caracteres")
    private String descricaoAtividade;

    @NotNull(message = "Data e hora são obrigatórias")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHora;
}
```

---

## 🎯 Filtros Avançados

### **Parâmetros de Filtro**

| Filtro               | Tipo   | Operação  | Exemplo                    | SQL Gerado                                 |
| -------------------- | ------ | --------- | -------------------------- | ------------------------------------------ |
| `funcional`          | String | Igual (=) | `?funcional=12345`         | `WHERE a.funcional = '12345'`              |
| `codigoAtividade`    | String | Igual (=) | `?codigoAtividade=ACT001`  | `WHERE a.codigoAtividade = 'ACT001'`       |
| `descricaoAtividade` | String | Like (%)  | `?descricaoAtividade=Yoga` | `WHERE a.descricaoAtividade LIKE '%Yoga%'` |

### **Query JPQL Personalizada**

```java
@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    @Query("SELECT a FROM Atividade a WHERE " +
           "(:funcional IS NULL OR a.funcional = :funcional) AND " +
           "(:codigoAtividade IS NULL OR a.codigoAtividade = :codigoAtividade) AND " +
           "(:descricaoAtividade IS NULL OR LOWER(a.descricaoAtividade) LIKE LOWER(CONCAT('%', :descricaoAtividade, '%')))")
    List<Atividade> findWithFilters(
        @Param("funcional") String funcional,
        @Param("codigoAtividade") String codigoAtividade,
        @Param("descricaoAtividade") String descricaoAtividade
    );
}
```

### **Exemplos de Uso dos Filtros**

**Filtro por funcional:**

```http
GET /atividades?funcional=12345
```

**Filtro por código:**

```http
GET /atividades?codigoAtividade=ACT001
```

**Filtro por descrição (busca parcial):**

```http
GET /atividades?descricaoAtividade=Yoga
```

**Múltiplos filtros combinados:**

```http
GET /atividades?funcional=12345&codigoAtividade=ACT001&descricaoAtividade=Matinal
```

---

## 🔄 Conversão de Dados (MapStruct)

### **AtividadeMapper Interface**

```java
@Mapper(componentModel = "spring")
public interface AtividadeMapper {

    AtividadeOutput toOutput(Atividade atividade);

    Atividade toEntity(AtividadeInput input);

    List<AtividadeOutput> toOutputList(List<Atividade> atividades);
}
```

### **Conversão Automática**

| Direção                    | Método           | Quando Usar         |
| -------------------------- | ---------------- | ------------------- |
| `Entity → DTO`             | `toOutput()`     | GET responses       |
| `DTO → Entity`             | `toEntity()`     | POST/PUT requests   |
| `List<Entity> → List<DTO>` | `toOutputList()` | GET lista responses |

### **Benefícios do MapStruct**

- ✅ **Performance**: Conversão em tempo de compilação
- ✅ **Segurança**: Detecção de erros em build time
- ✅ **Manutenibilidade**: Código gerado automaticamente
- ✅ **Type-Safe**: Conversões tipadas e seguras

---

## 📋 Estrutura do Banco de Dados

### **Tabela: atividade**

```sql
CREATE TABLE atividade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    funcional VARCHAR(50) NOT NULL,
    codigo_atividade VARCHAR(20) NOT NULL,
    descricao_atividade VARCHAR(255) NOT NULL,
    data_hora DATETIME NOT NULL,
    INDEX idx_funcional (funcional),
    INDEX idx_codigo_atividade (codigo_atividade),
    INDEX idx_data_hora (data_hora)
);
```

### **Índices para Performance**

| Índice                 | Campo(s)           | Propósito                     |
| ---------------------- | ------------------ | ----------------------------- |
| `PRIMARY`              | `id`               | Chave primária                |
| `idx_funcional`        | `funcional`        | Filtros por funcional         |
| `idx_codigo_atividade` | `codigo_atividade` | Filtros por código            |
| `idx_data_hora`        | `data_hora`        | Ordenação e filtros temporais |

---

## 🧪 Exemplos de Dados

### **Dados de Teste Válidos**

```json
[
  {
    "funcional": "12345",
    "codigoAtividade": "ACT001",
    "descricaoAtividade": "Yoga Matinal",
    "dataHora": "2024-01-15T08:00:00"
  },
  {
    "funcional": "67890",
    "codigoAtividade": "ACT002",
    "descricaoAtividade": "Corrida no Parque",
    "dataHora": "2024-01-15T18:30:00"
  },
  {
    "funcional": "11111",
    "codigoAtividade": "ACT003",
    "descricaoAtividade": "Natação Iniciante",
    "dataHora": "2024-01-16T07:00:00"
  }
]
```

### **Casos de Teste para Validação**

**❌ Dados Inválidos:**

```json
{
  "funcional": "", // Vazio - deve falhar
  "codigoAtividade": "CODIGO_MUITO_LONGO_PARA_O_CAMPO", // Muito longo - deve falhar
  "descricaoAtividade": "", // Vazio - deve falhar
  "dataHora": "data-invalida" // Formato inválido - deve falhar
}
```

**✅ Response de Erro Esperada:**

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
      "field": "codigoAtividade",
      "message": "Código da atividade não pode ter mais de 20 caracteres"
    },
    {
      "field": "descricaoAtividade",
      "message": "Descrição da atividade não pode estar vazia"
    },
    {
      "field": "dataHora",
      "message": "Data e hora são obrigatórias"
    }
  ]
}
```

---

## 🎯 Regras de Negócio

### **Validações de Entrada**

1. **Funcional**: Máximo 50 caracteres, não pode ser vazio
2. **Código Atividade**: Máximo 20 caracteres, não pode ser vazio
3. **Descrição**: Máximo 255 caracteres, não pode ser vazia
4. **Data/Hora**: Formato ISO 8601, não pode ser nula

### **Filtros**

1. **Funcional**: Busca exata (=)
2. **Código**: Busca exata (=)
3. **Descrição**: Busca parcial (LIKE %)
4. **Combinação**: Todos os filtros podem ser usados juntos

### **Resposta**

1. **Criação**: Retorna objeto criado com ID gerado
2. **Atualização**: Retorna objeto atualizado completo
3. **Listagem**: Retorna array (pode ser vazio [])
4. **Busca por ID**: Retorna objeto ou 404 se não existir
5. **Deleção**: Retorna 204 (sem conteúdo) ou 404 se não existir

**🎯 Estrutura de dados robusta e validada para garantir qualidade dos dados!**
