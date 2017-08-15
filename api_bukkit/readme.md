# Base da API MineMarket

Este projeto consiste na conex�o com os servidores minemarket via HTTP para carregar os comandos pendentes que devem ser rodados para a ativa��o ou remo��o de produtos comprados.

## Funcionamento da API

Existe uma aplica��o web sendo executada nos nossos servidores, que disponibiliza informa��es referentes aos comandos pendentes.

Ela � acessada via POST, e todas as chamadas da aplica��o cont�m um token, chamada de api_key.

A aplica��o web ser� atualizada constantemente e, por isso, n�o recomendamos implementa��es que a utilizem de forma direta.

Por este motivo, disponibilizamos este projeto com a base de conex�o com os servidores MineMarket, que pode ser implementada em aplica��es Java.

