# 🧱 Projeto Base - Clean Architecture com Spring Boot

Este docuemento serve como base para projetos Java que seguem os princípios da **Clean Architecture** e **SOLID**, utilizando **Spring Boot** e **Gradle** como ferramentas principais.

---

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot** (API REST, Injeção de Dependência, Configuração)
- **Gradle** (Gerenciador de dependências e build system)
- **JUnit / Mockito** (Testes automatizados)
- **Lombok** (Redução de boilerplate)

---

## 🧩 Arquitetura

A aplicação segue a **Clean Architecture**, que visa separar regras de negócio de detalhes de implementação, tornando o código mais flexível, testável e independente de frameworks.

### Camadas Principais

1. **domain** – Contém as entidades e regras de negócio (modelo de domínio puro, sem dependência de frameworks).
2. **application** – Contém os casos de uso (serviços de aplicação) e as interfaces que o domínio expõe ou consome (ports).
3. **infrastructure** – Implementa os adaptadores externos, como persistência, mensageria, integrações com outras APIs, etc.
4. **presentation** – Contém os controladores REST, DTOs, mapeadores e tudo que lida com a entrada/saída (HTTP, UI, etc.).

Essa separação garante que mudanças no framework ou na persistência **não afetem** as regras de negócio.

---

## 🧠 Princípios SOLID

Este projeto foi idealizado com base nos princípios **SOLID**:

- **S**ingle Responsibility: cada classe tem apenas uma responsabilidade.
- **O**pen/Closed: o código é aberto à extensão, mas fechado à modificação.
- **L**iskov Substitution: subclasses podem substituir suas superclasses sem alterar o comportamento.
- **I**nterface Segregation: interfaces específicas, evitando métodos desnecessários.
- **D**ependency Inversion: módulos de alto nível não dependem de baixo nível, mas de abstrações.

---

## 🗂️ Estrutura de Pastas (Exemplo)
```
src/
└── main/
    ├── java/
    │   └── com/exemplo/projeto/
    │       ├── domain/
    │       │   ├── entity/          # Entidades, value objects, agregados
    │       │   └── repository/      # Interfaces de repositório (ports)
    │       │
    │       ├── application/
    │       │   ├── usecase/         # Casos de uso / services de aplicação
    │       │   ├── dto/             # DTOs internos da aplicação
    │       │   └── mapper/          # Mapeadores entre camadas
    │       │
    │       ├── infrastructure/
    │       │   ├── persistence/     # Implementações de repositórios (JPA, etc.)
    │       │   ├── client/          # Clients HTTP, mensageria, etc.
    │       │   └── config/          # Configurações de infraestrutura
    │       │
    │       └── presentation/
    │           ├── controller/     # REST controllers
    │           ├── request/        # DTOs de entrada (requests)
    │           └── response/       # DTOs de saída (responses)
    │
    └── resources/
        ├── application.yml
        └── ...

test/
└── java/
    └── com/exemplo/projeto/
        └── ...                     # Testes unitários e de integração
```

---
## Checklist para a IA ao iniciar um novo projeto

1. **Criar projeto base**
   - Criar projeto Spring Boot com Gradle + Java 21 (pode usar Spring Initializr ou simular o boilerplate).

2. **Definir estrutura base de pacotes**
   - Usar a estrutura:
     - `com.exemplo.projeto.domain`
     - `com.exemplo.projeto.application`
     - `com.exemplo.projeto.infrastructure`
     - `com.exemplo.projeto.presentation`

3. **Criar exemplos mínimos**
   - Uma entidade simples em `domain/entity/` (ex.: `User`).
   - Uma interface `UserRepository` em `domain/repository/`.
   - Um `UserUseCase` em `application/usecase/`.
   - Uma implementação JPA em `infrastructure/persistence/`.
   - Um `UserController` em `presentation/controller/` com `UserRequest` e `UserResponse`.

4. **Configurar testes**
   - Criar teste unitário simples para o caso de uso.
   - Criar teste de integração para o controller (se aplicável).

5. **Configurar dependências básicas no `build.gradle`**
   - Adicionar dependências de:
     - Spring Web  
     - Spring Data JPA  
     - Spring Boot Test  
     - Lombok  
     - MapStruct (se usado)

6. **Garantir que integrações mínimas funcionem**
   - O projeto deve compilar e rodar com:
     ```bash
     ./gradlew bootRun
     ```
   - O endpoint base `/` ou `/health` deve estar acessível.
   - Os testes mínimos devem passar com:
     ```bash
     ./gradlew test
     ```
