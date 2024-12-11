# RideWay App - Aplicativo de Taxi

## Descrição

O RideWay App é um aplicativo de transporte que permite aos usuários solicitar corridas com
diferentes opções de veículos e preços. O aplicativo foi desenvolvido como parte de um teste de
emprego para a vaga de Shopper.

## Funcionalidades

* **Solicitação de Corridas:** Permite aos usuários solicitar corridas informando o endereço de
  origem e destino.
* **Opções de Veículos:** Oferece diferentes opções de veículos, como carros, motos e bicicletas,
  cada um com seu respectivo preço.
* **Histórico de Corridas:** Exibe o histórico de corridas realizadas pelo usuário, com detalhes
  como data, hora, origem, destino e valor.
* **Integração com Google Maps:** Utiliza o Google Maps para exibir o mapa, calcular rotas e
  fornecer informações de localização.

## Tecnologias Utilizadas

* **Kotlin:** Linguagem de programação principal.
* **Android SDK:** Kit de desenvolvimento de software para Android.
* **Hilt:** Biblioteca para injeção de dependências.
* **Retrofit:** Biblioteca para realizar requisições HTTP.
* **Gson:** Biblioteca para serialização e desserialização de JSON.
* **Picasso:** Biblioteca para carregamento de imagens.
* **Google Maps SDK:** Kit de desenvolvimento de software para integração com o Google Maps.
* **Google Location Services:** Serviços de localização do Google.

## Arquitetura

O aplicativo segue a arquitetura MVVM (Model-View-ViewModel), que separa a lógica de negócio da
interface do usuário, tornando o código mais organizado, testável e manutenível.

## Instalação

1. Clone o
   repositório: `git clone https://github.com/seu-usuario/RideWay-App_Teste_Emprego_Shopper.git`
2. Abra o projeto no Android Studio.
3. Configure as credenciais da API do Google Maps no arquivo `local.properties` <br> `GOOGLE_MAPS_API_KEY={Minha APiKEy}`.
4. Execute o aplicativo em um emulador ou dispositivo físico.

## Uso

1. Abra o aplicativo.
2. Informe o endereço de origem e destino.
- **Exemplos Origem** ```Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200```
- **Exemplos Destino** ```Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031``` - ```Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000``` - ```Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001```
3. Selecione a opção de veículo desejada.
4. Confirme a solicitação de corrida.
5. Acompanhe o status da corrida no mapa.
6. Após a corrida, visualize os detalhes no histórico de corridas.

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

