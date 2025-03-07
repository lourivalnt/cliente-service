Aqui está o **README atualizado** para refletir as mudanças no projeto, incluindo a integração do Redis, uso de DTOs e mapeamento com Lombok/MapStruct:

---

# Cliente Service

API RESTful para gerenciar clientes e endereços, seguindo uma arquitetura hexagonal (Ports and Adapters) com **cache via Redis**.

[![Java Version](https://img.shields.io/badge/Java-17-blue)](https://openjdk.org/)
[![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.4.2-green)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-Cache-red)](https://redis.io/)

---

## **Visão Geral**

- **Arquitetura Hexagonal**: Separação clara entre lógica de negócio (`core`) e infraestrutura (`application`).
- **Cache com Redis**: Reduz o número de consultas ao banco de dados PostgreSQL.
- **DTOs e Mappers**: Garante conversão segura entre entidades do core e dados da infraestrutura.
- **Documentação**: Swagger/OpenAPI para endpoints (disponível em `/swagger-ui.html`).

---

## **Módulos do Projeto**

### **1. Core**
- **Modelos**: `Cliente`, `Endereco` (entidades de domínio).
- **Ports**: Interfaces como `ClienteRepositoryPort` e `ClienteCachePort`.
- **Serviços**: Lógica de negócio (`ClienteService`).
- **Exceções**: Tratamento de erros específicos (ex: `ClienteNaoEncontradoException`).

### **2. Application**
- **Controladores**: Exposição de endpoints REST (`ClienteController`).
- **Adaptadores**:
    - `ClienteRepositoryAdapter`: Implementação JDBC para PostgreSQL.
    - `ClienteRedisAdapter`: Implementação de cache com Redis.
- **Mappers**:
    - `ClienteMapper`: Converte entre `Cliente` (core) e `ClienteDTO` (application).
    - `ClienteRowMapper`: Mapeia resultados do banco de dados para `ClienteDTO`.
- **Configurações**: Redis, Swagger e beans do Spring.

---

## **Pré-requisitos**

- **Java 17**
- **PostgreSQL** (configuração em `application.properties`)
- **Redis** (configuração em `application.properties`)
- **Maven 3.8+**

---

## **Configuração**

### **1. Banco de Dados (PostgreSQL)**
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/cliente_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

### **2. Redis**
```properties
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=5000ms
```

### **3. Swagger**
```properties
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## **Execução**

1. **Compilar o projeto**:
   ```bash
   mvn clean install
   ```

2. **Iniciar a aplicação**:
   ```bash
   mvn spring-boot:run
   ```

3. **Acessar a API**:
    - Documentação Swagger: `http://localhost:8080/swagger-ui.html`
    - Endpoints: `http://localhost:8080/clientes`

---

## **Endpoints da API**

| Método | Endpoint            | Descrição                          | DTO de Entrada/Saída       |
|--------|---------------------|------------------------------------|----------------------------|
| GET    | `/clientes`         | Lista todos os clientes            | `List<ClienteDTO>`         |
| GET    | `/clientes/{id}`    | Busca cliente por ID               | `ClienteDTO`               |
| POST   | `/clientes`         | Cadastra novo cliente               | `ClienteDTO` (entrada)     |
| DELETE | `/clientes/{id}`    | Exclui cliente por ID              | N/A                        |

---

## **Cache com Redis**

- **TTL (Time to Live)**: Dados expiram após **10 minutos**.
- **Operações Cacheadas**:
    - `GET /clientes/{id}`: Primeiro busca no Redis, depois no PostgreSQL.
    - `POST /clientes`: Salva no banco e atualiza o cache.
    - `DELETE /clientes/{id}`: Remove do banco e invalida o cache.

---

## **Mapeamento de Dados**

### **1. ClienteMapper (MapStruct)**
Converte entre `Cliente` (core) e `ClienteDTO` (application):
```java
@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteDTO toDTO(Cliente cliente);
    Cliente toEntity(ClienteDTO dto);
}
```

### **2. ClienteRowMapper (Spring JDBC)**
Mapeia resultados do PostgreSQL para `ClienteDTO`:
```java
@Component
public class ClienteRowMapper implements RowMapper<ClienteDTO> {
    @Override
    public ClienteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ClienteDTO.builder()
                .id(rs.getLong("cliente_id"))
                .nome(rs.getString("cliente_nome"))
                .idade(rs.getInt("cliente_idade"))
                .cpf(rs.getString("cliente_cpf"))
                .profissao(rs.getString("cliente_profissao"))
                .endereco(EnderecoDTO.builder()
                        .id(rs.getLong("endereco_id"))
                        .cep(rs.getString("endereco_cep"))
                        .rua(rs.getString("endereco_rua"))
                        .numero(rs.getString("endereco_numero"))
                        .bairro(rs.getString("endereco_bairro"))
                        .complemento(rs.getString("endereco_complemento"))
                        .cidade(rs.getString("endereco_cidade"))
                        .uf(rs.getString("endereco_uf"))
                        .build())
                .build();
    }
}
```

---

## **Testes**

### **1. Testes Unitários**
```bash
mvn test
```

### **2. Testes de Integração**
- Para testar o cache Redis, execute:
  ```bash
  redis-server # Inicia o Redis localmente
  mvn test
  ```

---

## **Estrutura do Projeto**

```
cliente-service/
├── core/
│   ├── src/main/java/com/clienteservice/core/
│   │   ├── exception/            # Exceções de negócio
│   │   ├── model/                # Entidades (Cliente, Endereco)
│   │   ├── ports/                # Interfaces (ClienteRepositoryPort, ClienteCachePort)
│   │   └── service/              # Lógica de negócio (ClienteService)
├── application/
│   ├── src/main/java/com/clienteservice/application/
│   │   ├── config/               # Configurações (Redis, Swagger)
│   │   ├── controller/           # Controladores REST
│   │   ├── dto/                  # DTOs (ClienteDTO, EnderecoDTO)
│   │   ├── mapper/               # Mapeamento entre DTOs e entidades
│   │   ├── repository/           # Adaptadores (Redis, JDBC)
│   │   └── ClienteApplication.java # Classe principal
└── pom.xml                       # Configuração Maven pai
```

---

## **Contribuição**

- **Branch**: Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`).
- **Commits**: Use commits descritivos (`git commit -m "Adiciona cache Redis"`).
- **Pull Request**: Descreva claramente as mudanças.

---

## **Licença**

Este projeto está licenciado sob a **MIT License**. Consulte [LICENSE](LICENSE) para mais detalhes.

---

### **Contato**

- Email: seu-email@example.com
- GitHub: [https://github.com/seu-usuario](https://github.com/seu-usuario)

---

**Principais Atualizações**:
- Integração com Redis para cache.
- Uso de DTOs e mapeamento com MapStruct/Lombok.
- Separação clara entre core (lógica) e application (infraestrutura).

Se precisar de mais detalhes, consulte os pacotes `core` e `application`! 😊