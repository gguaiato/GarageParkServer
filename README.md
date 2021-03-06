Garage Park Server
==================

Requisitos para compilação
-------------------------------------------
 * [Maven](http://maven.apache.org/download.cgi)
 * [Java EE SDK 1.7 ou superior](http://www.oracle.com/technetwork/java/javaee/downloads/java-ee-sdk-7-downloads-1956236.html)

Instruções de compilação
------------------------------------

Após a devida instalação dos requisitos para compilação, navegar até a pasta onde está localizado o projeto e proceder com o seguinte comando:

* mvn clean package

Logo após, será criado o arquivo garage-park-server.war no diretório de nome target da pasta do projeto. Este deve ser usado para deploy no servidor Tomcat.

Requisitos para execução
-------------------------------------
* [Apache Tomcat 7.0](http://tomcat.apache.org/download-70.cgi)
* [Banco de dados MySQL](http://dev.mysql.com/downloads/mysql/5.6.html)

Instruções para execução
-------------------------------------
Por padrão, a aplicação procurará por uma instancia do MySQL Server localmente através da porta 3306 com um banco de dados vazio de nome garagepark_db existente. Tais configurações podem ser alteradas após um deploy inicial com a modificação do arquivo META-INF/persintence.xml e reinicialização do servidor Tomcat.

Após configuração do MySQL Server para uso da aplicação, o arquivo garage-park-server.war deve ser copiado para pasta webapps dentro da pasta de instalação do Apache Tomcat 7.0. Em seguida deve-se inicializar o servidor.

Informações sobre o projeto
====================

Utilização dos serviços
--------------------------------
A utilização dos serviços de GET, POST e DELETE deve ser feita com base no endereço do servidor mais o nome garage-park-server (proveniente do garage-park-server.war) com adição do recurso desejado.

#### PersonService

Caminho base: /person

##### Cadastro de pessoa 
* curl --data "nusp=8599999&name=Gabriel&gender=masculino" http://localhost:8080/garage-park-server/v1/person

##### Atualização de validade de uma pessoa 
* curl -X PUT http://localhost:8080/garage-park-server/v1/person -d "nusp=8599999" -d "valid=false"

##### Recuperação de dados de uma pessoa
* curl http://localhost:8080/garage-park-server/v1/person/8599999


#### ParkingSpotService

Caminho base: /parking

##### Cadastro de vaga 
* curl --data "name=professor_1" http://localhost:8080/garage-park-server/v1/parking

##### Visualização de dados de vaga
* curl http://localhost:8080/garage-park-server/v1/parking/professor_1

##### Listagem de todas as vagas
* curl http://localhost:8080/garage-park-server/v1/parking/?limit=2

##### Cadastro de reserva de uma vaga
* curl --data "nusp=8599999" http://localhost:8080/garage-park-server/v1/parking/professor_1/reservation

##### Liberação da reserva de uma vaga
* curl -X PUT http://localhost:8080/garage-park-server/v1/parking/professor_1/clear
