# Story map — Hemocentro

## Backbone (jornada do sangue)

| 1. Captar doador | 2. Triar doador | 3. Coletar | 4. Armazenar | 5. Distribuir |
|---|---|---|---|---|
| Cadastrar doador | Verificar aptidão (idade, peso, intervalo) | Registrar doação e gerar bolsa | Controlar validade e estoque por tipo | Atender solicitação de transfusão |
| Agendar doação | Registrar entrevista clínica | Registrar exames sorológicos | Alertar estoque crítico | Registrar reação transfusional |
| Campanhas / lembretes | Bloqueio temporário (viagem, tatuagem…) | Fracionar em hemocomponentes | Descartar bolsa vencida | Relatórios de consumo |

**Release 1** = primeira linha (o mínimo para o sangue ir do doador ao paciente com segurança).
Linhas 2 e 3 ficam para releases futuras.

## Histórias da Release 1 e tarefas

### H1 — Triagem do doador
> Como **atendente do hemocentro**, quero **saber se um doador está apto** para **não coletar sangue de quem a norma proíbe**.

Regras baseadas na Portaria de Consolidação MS nº 5/2017 (Anexo IV), simplificadas para o PI.

| Tarefa | Regra | Teste |
|---|---|---|
| T1.1 | Idade entre 16 e 69 anos; 16–17 só com consentimento do responsável; primeira doação só até 60 anos | `TriagemDoadorTest.Idade` |
| T1.2 | Peso mínimo de 50 kg | `TriagemDoadorTest.Peso` |
| T1.3 | Intervalo mínimo desde a última doação: 60 dias (homens) / 90 dias (mulheres) | `TriagemDoadorTest.Intervalo` |
| T1.4 | Máximo de doações em 12 meses: 4 (homens) / 3 (mulheres) | `TriagemDoadorTest.LimiteAnual` |
| T1.5 | Endpoint `GET /doadores/{id}/triagem` | `triagem.feature` |

### H2 — Registrar doação
> Como **técnico de coleta**, quero **registrar a doação e gerar a bolsa** para **que ela entre no estoque com validade correta**.

| Tarefa | Regra | Teste |
|---|---|---|
| T2.1 | Bolsa de concentrado de hemácias vale 35 dias após a coleta (CPDA-1) | `ValidadeBolsaTest` |
| T2.2 | Doador inapto não pode doar (reaproveita H1) | `doacao.feature` |
| T2.3 | Persistir doação + bolsa no banco | `BolsaRepositoryIT` |
| T2.4 | Endpoint `POST /doadores/{id}/doacoes` | `doacao.feature` |

### H3 — Atender solicitação de transfusão
> Como **médico**, quero **receber bolsas compatíveis com o meu paciente** para **transfundir sem risco de reação hemolítica**.

| Tarefa | Regra | Teste |
|---|---|---|
| T3.1 | Compatibilidade ABO/Rh de hemácias (doador ⊆ antígenos do receptor) | `TipoSanguineoTest` |
| T3.2 | Bolsa vencida nunca é entregue | `SelecaoBolsasTest` |
| T3.3 | Prioriza o mesmo tipo (isogrupo) e depois a bolsa que vence primeiro (FEFO) | `SelecaoBolsasTest` |
| T3.4 | Estoque insuficiente → recusa (não entrega parcial) | `SelecaoBolsasTest` |
| T3.5 | Endpoint `POST /transfusoes` marca as bolsas como utilizadas | `transfusao.feature`, `BolsaRepositoryIT` |
