---

# Cliente Service

O **Cliente Service** é uma API RESTful desenvolvida em Java com Spring Boot. O objetivo deste projeto é gerenciar clientes e seus endereços associados, seguindo uma arquitetura hexagonal (Ports and Adapters) para garantir modularidade, escalabilidade e testabilidade.

---

## **Índice**

1. [Visão Geral](#visão-geral)
2. [Funcionalidades](#funcionalidades)
3. [Arquitetura](#arquitetura)
4. [Pré-requisitos](#pré-requisitos)
5. [Configuração](#configuração)
6. [Execução](#execução)
7. [Endpoints da API](#endpoints-da-api)
8. [Testes](#testes)
9. [Contribuição](#contribuição)
10. [Licença](#licença)

---

## **Visão Geral**

O **Cliente Service** permite criar, listar, buscar e excluir clientes. Cada cliente pode ter um endereço associado, representado por um ID de referência. A API foi projetada para ser simples, robusta e fácil de integrar com outros sistemas.

---

## **Funcionalidades**

- **Listar todos os clientes**: Retorna uma lista de todos os clientes cadastrados.
- **Buscar cliente por ID**: Retorna os detalhes de um cliente específico com base no ID.
- **Cadastrar novo cliente**: Permite cadastrar um novo cliente com informações como nome, idade, CPF, profissão e endereço associado.
- **Excluir cliente**: Remove um cliente do sistema com base no ID.
- **Documentação da API**: A API é documentada usando Swagger/OpenAPI para facilitar o consumo.

---

## **Arquitetura**

O projeto segue uma **arquitetura hexagonal (Ports and Adapters)**, dividida em dois módulos principais:

### **Módulo `core`**
- Contém a lógica de negócio e interfaces que definem contratos para interações externas.
- Pacotes:
    - `exception`: Classes de exceção relacionadas ao domínio.
    - `model`: Entidades do domínio (`Cliente`, `Endereco`).
    - `ports`: Interfaces para operações externas (como repositórios).
    - `service`: Implementação da lógica de negócio.

### **Módulo `application`**
- Contém implementações técnicas, como controladores REST, adaptadores de repositório e configurações.
- Pacotes:
    - `adapter`: Implementações dos adaptadores (ex.: repositório com JDBC).
    - `controller`: Controladores REST que expõem endpoints.
    - `dto`: Objetos de Transferência de Dados (DTOs) para comunicação entre camadas.
    - `mapper`: Mapeamento entre entidades e DTOs.
    - `exception`: Tratamento global de exceções e respostas padronizadas.
    - `config`: Configurações globais (ex.: Swagger).

---

## **Pré-requisitos**

Para executar este projeto, você precisará dos seguintes itens instalados:

- **Java 17 ou superior**: [Download do JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- **Maven 3.8 ou superior**: [Download do Maven](https://maven.apache.org/download.cgi)
- **Banco de Dados MySQL**: Configure um banco de dados MySQL ou use outro banco suportado pelo Spring Data JPA.
- **Postman ou Insomnia** (opcional): Para testar os endpoints da API.

---

## **Configuração**

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seu-usuario/cliente-service.git
   cd cliente-service
   ```

2. **Configure o banco de dados**:
    - Edite o arquivo `application.properties` no diretório `src/main/resources` para configurar as credenciais do banco de dados:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/cliente_db
      spring.datasource.username=seu-usuario
      spring.datasource.password=sua-senha
      spring.jpa.hibernate.ddl-auto=update
      ```

3. **Execute os scripts SQL iniciais**:
    - Os arquivos `schema.sql` e `data.sql` no diretório `resources` criam as tabelas e inserem dados iniciais no banco de dados.

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
    - A API estará disponível em: `http://localhost:8080/clientes`
    - Documentação Swagger: `http://localhost:8080/swagger-ui.html`

---

## **Endpoints da API**

| Método HTTP | Endpoint               | Descrição                                   |
|-------------|------------------------|---------------------------------------------|
| GET         | `/clientes`            | Lista todos os clientes                     |
| GET         | `/clientes/{id}`       | Busca um cliente por ID                     |
| POST        | `/clientes`            | Cadastra um novo cliente                    |
| DELETE      | `/clientes/{id}`       | Exclui um cliente por ID                    |

#### Exemplo de Requisição POST (`/clientes`):
```json
{
  "nome": "João Silva",
  "idade": 30,
  "cpf": "123.456.789-00",
  "profissao": "Engenheiro",
  "enderecoId": 1
}
```

---

## **Testes**

Os testes unitários e de integração estão localizados no pacote `src/test/java`. Para executá-los:

```bash
mvn test
```

---

## **Contribuição**

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature: `git checkout -b feature/nova-funcionalidade`.
3. Commit suas alterações: `git commit -m "Adiciona nova funcionalidade"`.
4. Envie para o repositório remoto: `git push origin feature/nova-funcionalidade`.
5. Abra um Pull Request explicando suas alterações.

---

## **Licença**

Este projeto está licenciado sob a **MIT License**. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

---

### **Contato**

Se tiver dúvidas ou sugestões, entre em contato:

---

