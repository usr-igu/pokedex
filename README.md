# Pokedex App

Este é um aplicativo Pokedex (Android) desenvolvido em Jetpack Compose como parte de um aprendizado prático, seguindo o tutorial de Philipp Lackner ([vídeo da playlist](https://www.youtube.com/watch?v=v0of23TxIKc&list=PLQkwcJG4YTCTimTCpEL5FZgaWdIZQuB7m)).

## Funcionalidades

* Visualização de uma lista de Pokemons.
* Detalhes de cada Pokemon.
* Pesquisa de Pokemons.

## Tecnologias e Diferenças do Tutorial Original

Este projeto foi desenvolvido utilizando uma versão mais recente do Android Studio (Android Studio 2025.1.2) e incorpora as seguintes tecnologias e abordagens que diferem do tutorial original:

*   **Room Persistence:** Utilizado para armazenar dados dos Pokemons localmente no dispositivo, permitindo uma funcionalidade de pesquisa mais robusta e acesso offline aos dados já carregados.
*   **Paging3:** Implementado para carregar e exibir listas paginadas de Pokemons de forma eficiente.
*   A navegação foi realizada usando o navigation3.

