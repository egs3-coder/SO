# language: pt
Funcionalidade: H2 - Registrar doação
  Como técnico de coleta
  Quero registrar a doação e gerar a bolsa
  Para que ela entre no estoque com validade correta

  Cenário: Doador apto doa e a bolsa entra no estoque com 35 dias de validade
    Dado um doador "Carlos" do sexo "MASCULINO" com 40 anos, 80 kg e tipo "B-"
    Quando "Carlos" doa sangue hoje
    Então a doação é registrada com bolsa "B-" válida por 35 dias
    E o estoque tem 1 bolsa "B-" disponível

  Cenário: Doadora que doou há 30 dias é recusada pelo intervalo mínimo
    Dado um doador "Ana" do sexo "FEMININO" com 28 anos, 58 kg e tipo "A-"
    E "Ana" doou sangue há 30 dias
    Quando "Ana" doa sangue hoje
    Então a doação é recusada com o motivo "Intervalo mínimo entre doações é de 90 dias"
    E o estoque tem 1 bolsa "A-" disponível
