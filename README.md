# Literalura - Sistema de Gerenciamento de Livros e Autores

O Challenge Literalura é um projeto elaborado pela Alura juntamente com a Oracle pelo ensino One. Sua missão é exercitar todo o material estudado até então na formação de Java. É pedido que o estudante faça uma aplicação capaz de se comunicar com a api Gutendex. Em caso de sucesso, sendo possível buscar e salvar dados de livros diretamente no banco de dados PostgreSQL, além de possibilitar o acesso aos dados salvos e se necessário novas adições a aplicação.

## Pré-requisitos
Antes de começar, certifique-se de ter instalado em sua máquina:

Java Development Kit (JDK) 17 ou superior
Apache Maven
Um ambiente de desenvolvimento integrado (IDE) de sua preferência, como IntelliJ IDEA ou Eclipse.

## Instalação e Configuração

Clone o repositório do Literalura para o seu ambiente local:
bash
Copiar código
```bash
git clone https://github.com/ewertoncaripuna/literalura.git
```
Importe o projeto para a sua IDE e aguarde a importação das dependências do Maven.

Verifique e, se necessário, atualize as configurações do banco de dados no arquivo **application.properties** para refletir o seu ambiente de desenvolvimento.

Utilização
Execute a aplicação Java.

Após a execução, o sistema exibirá um menu com várias opções. Escolha a opção desejada digitando o número correspondente e pressionando Enter.

Siga as instruções exibidas na tela para realizar as operações desejadas, como buscar livros por título, listar autores cadastrados, entre outras.

Para sair da aplicação digite "0" no menu.

# Funcionalidades
`*`**Buscar livro por título:** Permite buscar livros pelo título digitado pelo usuário.  
`*`**Listar livros cadastrados:** Lista todos os livros cadastrados no sistema.  
`*`**Listar autores cadastrados:** Lista todos os autores cadastrados no sistema.  
`*`**Listar autores vivos em um ano específico:** Lista os autores que estavam vivos em um ano específico informado pelo usuário.  
`*`**Listar livros por idioma:** Lista os livros cadastrados no sistema por idioma.  
`*`**Listar os 10 livros mais baixados:** Apresenta os 10 livros mais baixados cadastrados no sistema.  
`*`**Listar livros por Autor:** Permite buscar livros pelo nome do autor.  
