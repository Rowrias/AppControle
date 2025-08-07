# 📦 Controle de Entrada e Saída de Produtos

Sistema web desenvolvido para controle de entrada e saída de produtos em uma microempresa. O projeto foi realizado como parte da disciplina **Atividade Extensionista III – UNINTER**.

---

## 🚀 Funcionalidades principais

- ✅ Login com controle de acesso (administrador e usuário comum)
- ✅ Cadastro de funcionários, clientes e produtos (CRUD completo)
- ✅ Registro de entradas com preenchimento inteligente (auto-completar)
- ✅ Atualização automática de estoque
- ✅ Histórico de saídas com limpeza automática após 6 meses
- ✅ Interface responsiva (notebook, tablet, celular)

---

## 🏭 Contexto

A aplicação foi desenvolvida para atender uma empresa, que anteriormente fazia o controle de estoque de forma manual, utilizando papel e planilhas.  
Esse processo resultava em erros de contagem, perda de informações e falhas de rastreabilidade.  
O sistema proposto resolve essas questões com uma solução acessível, segura e adaptada às necessidades da empresa.

---

## 🛠️ Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Thymeleaf
- HTML5 + CSS3
- MySQL (modo de desenvolvimento e produção)
- Maven

---

## 🧑‍💻 Como executar o projeto localmente

### ⚙️ Pré-requisitos

- Java JDK 17+
- Maven 3.1+
- MySQL
- IDE (IntelliJ, Eclipse ou VSCode)

### 🔧 Configure o banco de dados (MYSQL) no arquivo

src/main/resources/application.properties;
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
spring.datasource.username=root
spring.datasource.password=root
```

### 🚀 Rode a aplicação com:
- Eclipse/IntelliJ: execute o arquivo AppControleApplication.java como uma aplicação Spring Boot.
- VSCode ou terminal: execute com o comando:
```

./mvnw spring-boot:run
```

### 🔐 Acesso ao sistema
```

http://localhost:8080
```
- Usuário: backup
- Senha: #backup

Esses dados podem ser alterados no arquivo Inicializacao.java.

---

## 👤 Autor
- Desenvolvido por Rodrigo Najdek Vieira Rodrigues
- 🔗 https://github.com/Rowrias
