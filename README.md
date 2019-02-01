# soa_osb_test

## Estrutura do projeto

O projeto foi estruturado como módulos Maven sendo eles:

* **reservation** - Projeto que calcula o valor de reservas.
* **HotelDetails** - Projeto que detalha os dados de um hotel.

Os passos para obter o projeto em execução são os seguintes:

```
$ git clone https://github.com/I-am-Miguel/soa_osb_test.git soa_osb_test_miguel
$ cd soa_osb_test_miguel
$ ls
HotelDetails  LICENSE  pom.xml  README.md  reservation
$ mvn clean package
```
Após executar _mvn clean package_ todos os artefatos serão compilados, testados e empacotados para distribuição.


## Desafio reservation

Após construir o projeto, a execução do primeiro desafio, reservation, pode ser visualizada pela execução da seguinte uri:

```
$ curl -X GET "http://localhost:8080/rest/reservation/1032/2019-01-26/2019-01-27/2/3/" -H "accept: application/json" 
=========================
Output: 
[{
  "id":1,
  "cityName":"Porto Seguro",
  "rooms":[{
      "roomID":0,
      "categoryName":"Standard",
      "totalPrice":15116.89,
      "priceDetail":{
          "pricePerDayChild":848.61,
          "pricePerDayAdult":1372.54
          }
       }]
   }
...]

```
Neste exemplo, temos os seguintes parâmetros informados:
```
cityCode: Id da cidade, dentre as pré-definidas:[1032(Porto Seguro),7110(Rio de Janeiro),9626(São Paulo)]
checkin: Data de entrada da reserva, utilizando formatação YYYY-mm-dd
checkout: Data de saída da reserva, utilizando formatação YYYY-mm-dd
adult_qtd: Quantidade de adultos a serem incluídas na reserva
child_qtd: Quantidade de crianças a serem incluídas na reserva
```
O resultado apresentado é o proposto pelo próprio exemplo. Outras saídas são apresentadas de acordo com as datas e quantitativos utilizados na consulta.

## Desafio HotelDetails

Após construir o projeto, a execução do segundo desafio, HotelDetails, pode ser visualizada pela execução da seguinte uri:


```
$ curl -X GET "http://localhost:8080/rest/details/1/2019-01-26/2019-01-26/1/0/" -H "accept: application/json"
=========================
Output: 
{
  "id":1,
  "cityName":"Porto Seguro",
  "rooms":[{
      "roomID":0,
      "categoryName":"Standard",
      "totalPrice":15116.89,
      "priceDetail":{
          "pricePerDayChild":848.61,
          "pricePerDayAdult":1372.54
          }
       }]
}
```
Neste exemplo, temos os seguintes parâmetros informados:
```
hotelId: Id do hotel que se deseja efetuar o detalhamento
checkin: Data de entrada da reserva, utilizando formatação YYYY-mm-dd
checkout: Data de saída da reserva, utilizando formatação YYYY-mm-dd
adult_qtd: Quantidade de adultos a serem incluídas na reserva
child_qtd: Quantidade de crianças a serem incluídas na reserva
```
## Resumão da Solução Implementada


### Reservation

Inicialmente utilizei a RouterFunctions do WebFlux, onde fui feliz até dar inicío a documentação com o swagger, como optei por utilizar as anotations do mesmo para gerar o UI tive complicações já que não haviam controladores rest especificos para cada endpoints, e sim handles:

Anteriormente:
```
RouterFunction<ServerResponse> route =
     RouterFunctions.route(RequestPredicates.GET("/rest/reservation/{cityCode}/{checkin}/{checkout}/{adult_qtd}/{child_qtd}/"), reservationHandler::checkReservation);
```
Atualmente:
```
@RequestMapping(method = RequestMethod.GET, value = "/reservation/{cityCode}/{checkin}/{checkout}/{adult_qtd}/{child_qtd}/")
	public Flux<HotelResponse> checkReservation(@PathVariable("cityCode") Integer cityCode,
			@PathVariable("checkin") @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin,
			@PathVariable("checkout") @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout,
			@PathVariable("adult_qtd") Integer adult, @PathVariable("child_qtd") Integer child) { ...}
```

A funcionalidade permaneceu a mesma, no entanto um pouco mais verborrágica.


Basicamente, a ideia é realizar a maior quantidade de requisições com um menor tempo de responsa possível, o que foi um desafio pessoal, já que o Spring é Sincrono, o que causaria um pool elevado de requisições nos endpoints disponíveis, desta forma optei pelo paradigma reativo incorporado no Spring5 para sanar tal necessidade, [para mais informações](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) .

Os módulos foram gerados utilizando as seguintes tecnologias:
* **reservation**
  - Spring Reactive/ WebFlux
  - mongodb
  - Lombok
 
* **HotelDetails** 
  - Spring mvc
  - Lombok
  
Documentação de endpoints gerada utilizando [Swagger](https://swagger.io)


Conectei o mongo em um serviço free do [MLAB](https://mlab.com/), talvez isso possa causar uma certa lentidão de acordo com o uso.


Para maiores detalhes, por favor, observar estruturação de todas as classes e código nos projetos em questão. :smile:

#### OBS 1

Durante o trabalho tive algumas dúvidas relacionadas a descrição do projeto, como por exemplo, se deveria ou não, aplicar a comissão nas diárias do adulto e crianças, assim fiz um [pull request](https://github.com/rodrigocvc/soa_osb_test/pull/2) com algumas modificações na descrição

#### OBS 2

Imagino que poderia haver algumas otimizações no processo de comunicação com o broker disponibilizado, o handshake para estabelecer a sessão é bem lento. Optei por "bufferizar" as informações disponibilizadas pelo broker, um job [Scheduled](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/Scheduled.html) que roda a cada 3 minutos capturando os dados e persistindo no banco do MLAB
