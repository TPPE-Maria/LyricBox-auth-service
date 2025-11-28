# LyricBox

## Auth Service - Microserviço de Autenticação

Este é um microserviço de autenticação desenvolvido com **Spring Boot** que fornece funcionalidades completas de gerenciamento de usuários e autenticação baseada em JWT.

## 🏃‍♂️ Como executar

```bash
docker-compose up -d
```

## 📚 Documentação da API

- **Swagger UI**: http://localhost:8080/api/auth/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/auth/api-docs
- **Health Check**:
```
GET /api/auth/actuator/health
```

### Lista de Endpoints

- `POST /api/auth/register`   - Registrar usuário (Público)
- `POST /api/auth/login`      - Fazer login (Público)

## 🛠 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **H2 Database** (desenvolvimento)
- **Maven** (gerenciamento de dependências)
- **Docker** (containerização)
- **OpenAPI/Swagger** (documentação)
- **JUnit 5** (testes)

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+ ou Docker
- Git

## 🔐 Segurança

- **Senhas**: Criptografadas com BCrypt
- **JWT**: Tokens assinados com HMAC-SHA256
- **CORS**: Configurado para desenvolvimento
- **Rate Limiting**: Implementado via Spring Security
- **Validação**: Bean Validation (JSR-303)

## 🏗 Arquitetura

```
src/
├── main/java/com/microservices/authservice/
│   ├── config/          # Configurações (Security, OpenAPI, etc)
│   ├── controller/      # Controllers REST
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # Entidades JPA
│   ├── exception/      # Tratamento de exceções
│   ├── repository/     # Repositórios JPA
│   ├── security/       # Filtros e configurações de segurança
│   ├── service/        # Lógica de negócio
│   └── util/           # Utilitários (JWT, etc)
└── test/               # Testes unitários e de integração
```