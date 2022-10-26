# Bill API - Desafio Técnico
O [desafio técnico](https://platformbuilders.notion.site/Desafio-T-cnico-483464fe010e4122b88499f4b3d625d9) é uma API RESTful que consiste em recebe um código de boleto vencido e retornar os valores de multas e juros calculados. 

## Banco de dados
O banco de dados usado na aplicação é o [MariaDB](https://mariadb.org/). <br>
Recomendo manter o MariaDB, porque trocar o banco pode quebrar a aplicação por problemas no hibernate. <br>

### Configurando o MariaDB
Passo 1 - [Baixar e instalar o MariaDB](https://mariadb.com/kb/en/getting-installing-and-upgrading-mariadb/) <br>
Passo 2 - Criar o banco de dados sql: **create database `bill-payment`;** <br>
Passo 3 - [Criar o usuário](https://mariadb.com/kb/en/create-user/) <br>

## Como executar a aplicação
Para executar a aplicação será necessário o Java 17, [maven](https://maven.apache.org/) e o [MariaDB](https://mariadb.org/) previamente configurado. <br>
O banco de dados tem que estar devidamente configurado para a aplicação funcionar, com usuário configurado também.

Passo 1 - Clonar o repositório. <br>
Passo 2 - Configurar o *application.properties* na pasta /src/main/resources <br>
Passo 3 - Acessar o diretório base do projeto pelo terminal. <br>
Passo 4 - Rodar o comando: **mvn clean package** <br>
Passo 5 - Pegar o arquivo .jar na pasta /target e executar: **java -jar <nome_do_arquivo>.jar**
 
## Como executar os testes
O desafio possui testes de unidade e de integração, para executá-los você vai precisar do [maven](https://maven.apache.org/). <br>
<br>
Passo 1 - Clonar o repositório. <br>
Passo 2 - Acessar o diretório base do projeto pelo terminal. <br>
Passo 3 - Executar o comando: **mvn test** <br>

## Routes
| Method		| URL 							| Returns 				| Body 				| 
| --- 			| --- 							| --- 						| --- 				| 
| **POST**	| **/bill?token=** 	| Calculated Bill	| BillDTO			| 

Para ser autorizado precisa passar o token como parâmetro. <br>
Para conseguir o token consumir a [API](https://vagas.builders/api/builders/auth/tokens) de Auth da própria builder ou basta importar o arquivo *postman_collection.json* na raíz do projeto para facilitar os testes no [postman](https://www.postman.com/). <br>

## Body JSON

### BillDTO JSON:
```yaml
{
  "bar_code": "34191790010104351004791020150008191070069000",
  "payment_date": "2022-12-18"
}
```
O boleto deve ser válido, estar vencido e ser do tipo NPC. <br>
A data de pagamento deve estar no presente ou futuro. <br>

## Arquitetura Hexagonal
O desafio foi feito usando a [arquitetura hexagonal](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)), mas não gosto do nome arquitetura hexagonal por ser confuso, prefiro chamar de arquitetura de inversão de dependências, pois o princípio da arquitetura é exatamente este: ao invés da regra de negócios depender da dependência, a dependência que vai depender da regra de negócio, e essa dependência é realizada através de interfaces (abstrações), essas interfaces são portas que serão implementadas por adaptadores (framework, database, etc).

## Author
| [<img src="https://github.com/cruzapi.png?size=115" width=115><br><sub>@CruzAPI</sub>](https://github.com/cruzapi) |
| :---: |

## Technology

- [Spring](https://spring.io/)
- [Maven](https://maven.apache.org/)
