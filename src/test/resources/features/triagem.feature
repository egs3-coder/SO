# language: pt
Funcionalidade: H1 - Triagem do doador
  Como atendente do hemocentro
  Quero saber se um doador está apto
  Para não coletar sangue de quem a norma proíbe

  Cenário: Doador adulto saudável está apto
    Dado um doador "Maria" do sexo "FEMININO" com 30 anos, 60 kg e tipo "O+"
    Quando consulto a triagem de "Maria"
    Então o doador está apto

  Cenário: Doador abaixo do peso está inapto
    Dado um doador "João" do sexo "MASCULINO" com 25 anos, 48 kg e tipo "A+"
    Quando consulto a triagem de "João"
    Então o doador está inapto com o motivo "Peso mínimo para doação é 50 kg"

  Cenário: Primeira doação acima de 60 anos não é permitida
    Dado um doador "Seu Antônio" do sexo "MASCULINO" com 65 anos, 75 kg e tipo "B+"
    Quando consulto a triagem de "Seu Antônio"
    Então o doador está inapto com o motivo "Primeira doação só é permitida até 60 anos"
