# Base da API MineMarket

Este projeto consiste na conexão com os servidores minemarket via HTTP para carregar os comandos pendentes que devem ser rodados para a ativação ou remoção de produtos comprados.

## Funcionamento da API

Existe uma aplicação web sendo executada nos nossos servidores, que disponibiliza informações referentes aos comandos pendentes.

Ela é acessada via POST, e todas as chamadas da aplicação contém um token, chamada de api_key.

A aplicação web será atualizada constantemente e, por isso, não recomendamos implementações que a utilizem de forma direta.

Por este motivo, disponibilizamos este projeto com a base de conexão com os servidores MineMarket, que pode ser implementada em aplicações Java.

