# projeto-compiladores-ifsc

## 1. Pré-requisitos

Compilador Java: JDK 19 ou maior. \
LLVM: Clang 16.0.0.

## 2. Download
Baixe o programa já compilado e pronto para ser executado na ultima versão [aqui](https://github.com/erikborella/projeto-compiladores-ifsc/releases/download/0.1.0-alpha/projeto-compiladores-ifsc_0.1.0-alpha.zip). Veja nas [Releases](https://github.com/erikborella/projeto-compiladores-ifsc/releases) também outras versões disponiveis.

## 3. Utilização

O programa compilado tem como objetivo compilar o código suportado para a [linguagem intermediaria do LLVM](https://llvm.org/docs/LangRef.html) na forma textual, ou seja, ele irá fazer o papel de frontend do compilador gerando uma representação para o backend (LLVM) poder realizar os proximos passos de otimização, geração de código alvo e linkagem em um executavel.

Para rodar o executavel utilize o comando:
```sh
java -jar .\projeto-compiladores-ifsc-1.0-SNAPSHOT.jar
```

Execute com o argumento `-h` para obter a informação de comandos que o compilador suporta
```sh
java -jar .\projeto-compiladores-ifsc-1.0-SNAPSHOT.jar -h
```
![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/bf293a78-fc73-4f90-80f6-94915400ed2e)

Executando o programa sem nenhuma argumento será iniciado o modo interativo onde o código a ser compilado pode ser escrito sem necessitar estar em um arquvio salvo. Para compilar o código digitado é necessário digitar `--stop` e para encerrar o programa é necessário digitar `--exit`.
```sh
java -jar .\projeto-compiladores-ifsc-1.0-SNAPSHOT.jar
```
![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/b077d1c2-1a71-4102-80d1-6224c8af21e2)

Para compilar de um arquivo salve em disco, passe o caminho do arquivo como o primeiro argumento do programa. Na pasta `exemplos` existe um exemplo de jogo da velha.
```sh
java -jar .\projeto-compiladores-ifsc-1.0-SNAPSHOT.jar .\exemplos\jogoVelha.txt
```
![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/651166ac-d678-4504-9058-2235ec0224bf)

Por padrão o programa imprime o código LLVM IR compilado em tela, mas também é possivel especificar um arquivo para o código compilado ser salvo em disco com o argumento `-o`. Exemplo compilando o jogo da velha e salvando em arquivo na pasta `exemplos`.
```sh
java -jar .\projeto-compiladores-ifsc-1.0-SNAPSHOT.jar .\exemplos\jogoVelha.txt -o .\exemplos\jogoVelha.ll
```
> Recomendado sempre salvar os arquivos compilador com a extensão `.ll` para o compilador do LLVM indentificar corretamente o arquivo. \

![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/e8ff38ec-c224-4e55-ad82-cd4e40d8566a)

É possivel também habilitar a visualização da arvore de derivação usada pelo compilador na parte das analiases lexica e sintatica com o argumento `-t`:
```sh
java -jar .\projeto-compiladores-ifsc-1.0-SNAPSHOT.jar -t
```
![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/3a85eb1b-3411-47dd-a4f5-19ac171c6b11)

## 4. Gerando um executavel

Para criar um executavel é necessario ter o clang na versão 16.
É necessário apenas especificar o caminho do código LLVM IR compilado para o clang:
```sh
clang-16 .\exemplos\jogoVelha.ll
```
E então um executavel deverá ser criado e o programa poderá ser executado.

## 5. Compilando o projeto

Caso deseja compilar o programa, siga os seguintes passos:

1. Clone o projeto com o comando:
   ```sh
   git clone https://github.com/erikborella/projeto-compiladores-ifsc.git
   ```

2. Abra o projeto `projeto-compiladores-ifsc` localizado dentro da pasta do projeto clonado na sua IDE de preferencia. \
   ![Captura de tela 2024-03-13 081727](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/5bf5bc8a-3125-4f4f-8321-6047bb776989)

3. Compile o projeto usando o Maven executando o Goal `package`:
   1. Exemplo pela IDE NetBeans: \
      ![Captura de tela 2024-03-13 084506](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/ef122581-697c-4921-a11b-c38ab9c5291e)
      ![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/adbe513f-64b1-48dd-b0b7-456bf73bf59c)

   2. Exemplo usando pelo cmd: \
      Executar comando:
      ```sh
      mvn package
      ```
      ![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/f8bda813-3eab-495e-8240-bd22a23ab683)

4. Depois do processo de compilação for concluido na pasta `target` do projeto será criado um arquivo chamado `projeto-compiladores-ifsc-1.0-SNAPSHOT.jar`, esse é o executavel do compilador. \
   ![image](https://github.com/erikborella/projeto-compiladores-ifsc/assets/27148919/8e270162-fa49-4c0c-8647-3a1642c478f5)
