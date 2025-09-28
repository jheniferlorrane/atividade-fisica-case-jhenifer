# 🐳 Arquivos Docker

Os arquivos Docker foram movidos para a raiz do projeto para melhor organização:

## Arquivos:

- `docker-compose.yml` - Orquestração dos containers (MySQL + App Spring Boot com volume mounting)

## Como usar:

```bash
# Subir todos os serviços
docker-compose up -d

# Apenas MySQL (para desenvolvimento na IDE)
docker-compose up mysql -d

# Parar serviços
docker-compose down
```

## Estrutura:

```
📁 atividade-fisica-case-jhenifer/
├── 🐳 docker-compose.yml    # ← Orquestração
├── 📂 back-end/            # ← Código fonte
├── 📂 bd/                  # ← Scripts SQL
├── 📂 doc-complementares/  # ← Documentações detalhadas
└── collection          # ← Coleções Insomnia
```

## 👩‍💻 Desenvolvido por

**Jhenifer Lorrane Anacleto Rodrigues**

_Case Técnico - API REST - Atividades Físicas_
