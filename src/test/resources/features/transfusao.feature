# language: pt
Funcionalidade: H3 - Atender solicitação de transfusão
  Como médico
  Quero receber bolsas compatíveis com o meu paciente
  Para transfundir sem risco de reação hemolítica

  Contexto:
    Dado o estoque possui as bolsas:
      | tipo | vence_em_dias |
      | O-   | 10            |
      | A+   | 5             |
      | A+   | 20            |
      | B+   | -1            |

  Cenário: Receptor A+ recebe a bolsa A+ que vence primeiro
    Quando o médico solicita 1 bolsa para um receptor "A+"
    Então a transfusão é atendida com 1 bolsa do tipo "A+"
    E a bolsa entregue vence em 5 dias
    E restam 2 bolsas utilizáveis no estoque

  Cenário: Receptor O- só pode receber O-
    Quando o médico solicita 1 bolsa para um receptor "O-"
    Então a transfusão é atendida com 1 bolsa do tipo "O-"

  Cenário: Bolsa vencida não conta e a solicitação é recusada sem consumir estoque
    Quando o médico solicita 2 bolsas para um receptor "B+"
    Então a solicitação é recusada por estoque insuficiente
    E restam 3 bolsas utilizáveis no estoque
