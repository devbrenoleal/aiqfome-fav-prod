# aiqfome-fav-prod

Desafio técnico de uma API para gerenciar os produtos favoritos dos usuários da plataforma

Para organizar a execução das tarefas, utilizei as Issues do Github e separei em Milestones as etapas que eu iria executar
Cada Milestone possui Issues dentro, e na descrição de cada Issue, existem passos mais detalhados sobre a execução.

Pontos sobre o projeto:
- Em ProdutoFavoritoController, não há um endpoint base, como /api/produtos-favoritos que liste todos os produtos
  favoritos do banco. Esta decisão foi proposital, pois, não vi razão para listar produtos favoritos que não pertençam a um cliente.
- Testes unitários não foram implementados devido ao tempo e escopo do projeto, mas seria um bom follow-up

A stack escolhida foi devido a comodidade, Java e Spring são ferramentas que já trabalho há muito tempo e tenho
muita familiaridade com elas.

O projeto possui uma divisão simples de arquivos, nenhuma arquitetura específica foi adotada, porém, as boas práticas
de SOLID foram exercitadas na medida do possível.

- `config` possui algumas configurações essenciais para a segurança da API funcionar corretamente
- `controller` guarda as classes com os endpoints de acesso ao sistema
- `domain` estão as classes de reflexo do banco de dados. Optei por utilizar uma abordagem ORM
- `dto` possuem classes de auxílio para recebimento e retorno de requests
- `exception` exceções específicas do sistema
- `persistence` camada de repositórios do banco de dados
- `security` classes utilitárias para autenticação, autorização e geração de token
- `service` serviços do sistema

Para fácil execução do projeto, criei um arquivo `docker-compose.yml` já com os containers do Spring e do PostgreSQL

Se possuir o Docker instalado, execute docker-compose up --build para inicializar o projeto, isso setará tudo que é necessário
para a execução, como banco, propriedades e configurações.

Caso contrário:

1. Baixe e instale a versão 17 do java: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. Baixe e instale o PostgreSQL 15 https://www.postgresql.org/download/
3. Clone o repositório e abra no editor de sua escolha (utilizei o Intellij IDEA Community, disponível em: https://www.jetbrains.com/idea/download/?section=windows)
4. Ao abrir o projeto, selecione o JDK que você instalou (depende do editor, no intellij, siga: File -> Project Structure -> SDK)
5. Baixe as dependências pelo maven executando o comando `mvn dependency:go-offline -B` ou, se estiver no intellij: abra o pom.xml -> botão direito -> maven -> reload project
6. Certifique-se de que o processamento de anotações esteja ligado, caso o contrário o Lombok não será reconhecido e o projeto acusará erro de compilação.
7. Rode o projeto a partir da classe DemoApplication.
8. Para ler a documentação, acesse http://localhost:8080/swagger-ui/index.html#/

Para testes, utilzei o Postman (https://www.postman.com/)

Stack:
1. Java (openjdk 17)
2. Spring Framework (3.4.9) [Boot, Data, Security]
3. PostgreSQL 15 
4. Docker 
5. Maven 
6. OpenAPI/Swagger