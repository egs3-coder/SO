# Hemocentro — Projeto Integrador

> **Desafio do PI: Container, TDD e Deploy automatizados** · Infraestrutura de Software 2026.2 · CESAR School

Sistema que leva o sangue **do doador ao paciente com segurança**. Ele cuida de cinco coisas: triagem do doador, registro da doação com geração da bolsa, controle do estoque por validade, alerta de estoque crítico e atendimento de transfusões só com sangue compatível (ABO/Rh).

| | |
|---|---|
| 🌐 **Site no ar** | https://hemocentro-api.onrender.com |
| 🔖 **Versão publicada (JSON)** | https://hemocentro-api.onrender.com/api/info |
| 📦 **Repositório** | https://github.com/egs3-coder/SO |
| ⚙️ **Esteira (Actions)** | https://github.com/egs3-coder/SO/actions |
| 🖼️ **Evidências (prints)** | [`docs/evidencias/`](docs/evidencias/) · índice em [`docs/evidencias/README.md`](docs/evidencias/README.md) |

Java 21 · Spring Boot 3.5 · PostgreSQL 17 · Flyway · Docker multi-stage · Docker Compose · JUnit 5 · Testcontainers · Cucumber · GitHub Actions · GHCR · Render

---

## Sumário

1. [Integrantes e tarefas](#1-integrantes-e-tarefas)
2. [O sistema](#2-o-sistema)
3. [E1 · Container automatizado: como rodar](#3-e1--container-automatizado-como-rodar)
4. [E2 · TDD: story map e testes](#4-e2--tdd-story-map-e-testes)
5. [Release 2: a entrega de cada integrante, caso a caso](#5-release-2-a-entrega-de-cada-integrante-caso-a-caso)
6. [E3 · Pipeline, deploy e rollback](#6-e3--pipeline-deploy-e-rollback)
7. [Evidências](#7-evidências)
8. [Processo e histórico (com transparência)](#8-processo-e-histórico-com-transparência)
9. [Decisões](#9-decisões)
10. [Uso de IA](#10-uso-de-ia)
11. [Checklist dos critérios do enunciado](#11-checklist-dos-critérios-do-enunciado)

---

## 1. Integrantes e tarefas

<table>
  <tr>
    <td align="center" width="25%"><img src="docs/equipe/ewerton.jpg" width="110" alt="Ewerton Guilherme da Silva"><br><b>Ewerton Guilherme da Silva</b><br><sub>Product Owner</sub></td>
    <td align="center" width="25%"><img src="docs/equipe/pablo.jpg" width="110" alt="Pablo Arthur Eustáquio de Lima"><br><b>Pablo Arthur Eustáquio de Lima</b><br><sub>Backend Developer</sub></td>
    <td align="center" width="25%"><img src="docs/equipe/joao.jpg" width="110" alt="João Ricardo Alves de Brito"><br><b>João Ricardo Alves de Brito</b><br><sub>Backend Developer</sub></td>
    <td align="center" width="25%"><img src="docs/equipe/lucas.jpg" width="110" alt="Lucas Aprigio dos Santos"><br><b>Lucas Aprigio dos Santos</b><br><sub>Backend Developer</sub></td>
  </tr>
  <tr>
    <td align="center" width="25%"><img src="docs/equipe/saulo.jpg" width="110" alt="Saulo Eduardo Almeida dos Santos"><br><b>Saulo Eduardo Almeida dos Santos</b><br><sub>Backend Developer</sub></td>
    <td align="center" width="25%"><img src="docs/equipe/thiago.jpg" width="110" alt="Thiago Cardozo da Conceição"><br><b>Thiago Cardozo da Conceição</b><br><sub>Frontend / UI Designer</sub></td>
    <td align="center" width="25%"><img src="docs/equipe/eloi.jpg" width="110" alt="Eloi de Lima Sousa"><br><b>Eloi de Lima Sousa</b><br><sub>Frontend / UI Designer</sub></td>
    <td></td>
  </tr>
</table>

Cada integrante entregou **uma regra nova (Release 2)** por pull request, **com a própria conta do GitHub**, seguindo o mesmo ritual: o teste vem primeiro e fica vermelho, o código vem depois e fica verde, um colega aprova e o merge publica sozinho.

| Integrante | Conta | Tarefa | PR | Execução do deploy (merge) |
|---|---|---|---|---|
| Ewerton Guilherme da Silva | `ewertongui` | T4.1 · Alerta de estoque crítico | [#3](https://github.com/egs3-coder/SO/pull/3) | [#12 · `0bf4766`](https://github.com/egs3-coder/SO/actions/runs/39611729274) ✅ |
| Lucas Aprigio dos Santos | `lucasAprigi0` | T4.2 · Descarte de bolsas vencidas | [#12](https://github.com/egs3-coder/SO/pull/12) | [#28 · `852605a`](https://github.com/egs3-coder/SO/actions/runs/39697281558) ✅ |
| João Ricardo Alves de Brito | `joaobrito-dev` | T1.7 · Hemoglobina mínima | [#10](https://github.com/egs3-coder/SO/pull/10) | [#30 · `26a3f11`](https://github.com/egs3-coder/SO/actions/runs/39698041141) ✅ |
| Saulo Eduardo Almeida dos Santos | `Saulo1Almeida` | T2.5 · CPF válido (dígitos verificadores) | [#11](https://github.com/egs3-coder/SO/pull/11) | [#32 · `a925937`](https://github.com/egs3-coder/SO/actions/runs/39698399748) ✅ |
| Eloi de Lima Sousa | `eloiliima` | T1.9 · Pressão arterial | [#14](https://github.com/egs3-coder/SO/pull/14) | [#38 · `e0b0b35`](https://github.com/egs3-coder/SO/actions/runs/39703058746) ✅ |
| Pablo Arthur Eustáquio de Lima | `pael-pixel` | T1.6 · Tatuagem recente | [#16](https://github.com/egs3-coder/SO/pull/16) | [#43 · `5240cc3`](https://github.com/egs3-coder/SO/actions/runs/39782395429) ✅ |
| Thiago Cardozo da Conceição | `thcardozo` | T1.8 · Frequência cardíaca | [#17](https://github.com/egs3-coder/SO/pull/17) | [#48 · `f241ea0`](https://github.com/egs3-coder/SO/actions/runs/39786359771) ✅ |

O detalhe de cada uma está na [seção 5](#5-release-2-a-entrega-de-cada-integrante-caso-a-caso).

---

## 2. O sistema

### O problema

Num hemocentro, um erro de software não é só um bug: transfundir sangue incompatível pode causar uma **reação hemolítica fatal**, e uma bolsa vencida também põe o paciente em risco. Coletar de quem não poderia doar põe em risco o **doador**. Por isso cada regra crítica tem um teste automatizado, e nenhuma mudança chega à produção sem passar por eles.

### A jornada do sangue (backbone do story map)

```
 1. Captar      →   2. Triar        →   3. Coletar        →   4. Armazenar         →   5. Distribuir
 cadastrar          verificar se        registrar doação      controlar validade       atender transfusão
 o doador           pode doar (H1)      e gerar bolsa (H2)    e estoque (T4.1/T4.2)    compatível (H3)
```

### Endpoints

| Rota | História | O que faz |
|---|---|---|
| `GET /` | — | **Site do hemocentro** (página web estática servida pela própria API): a jornada do sangue operável ao vivo |
| `POST /doadores` | apoio | Cadastra o doador. `201`; CPF repetido → `409` |
| `GET /doadores/{id}` | apoio | Consulta o doador |
| `GET /doadores/{id}/triagem` | H1 | `{"apto": true/false, "motivos": [...]}`, com **todos** os motivos de uma vez |
| `POST /doadores/{id}/doacoes` | H2 | Se apto, registra a doação e gera a bolsa (35 dias). Se inapto, `422` com os motivos |
| `GET /estoque` | apoio | Bolsas utilizáveis (disponíveis e não vencidas) por tipo |
| `GET /estoque/criticos` | T4.1 | Tipos abaixo do estoque mínimo |
| `POST /transfusoes` | H3 | Entrega bolsas compatíveis (isogrupo + FEFO). Sem estoque suficiente, `409` e não entrega nada |
| `GET /api/info` | infra | Serviço, **versão no ar (SHA do commit)** e lista de rotas. A esteira usa isso para conferir deploy e rollback |
| `GET /api/resumo`, `GET /api/doadores-recentes` | infra | Dados de leitura usados pelo site |
| `GET /actuator/health` | infra | Saúde da aplicação e do banco (healthcheck do Compose e do Render) |

### Arquitetura

```
src/main/java/br/cesar/hemocentro/
├── dominio/     regras puras, sem Spring nem banco (TipoSanguineo, ValidadeBolsa, SelecaoBolsas,
│                TriagemDoador, AlertaEstoque, DescarteBolsas, Cpf, RegraTatuagem, RegraHemoglobina,
│                RegraFrequenciaCardiaca, RegraPressaoArterial)
├── aplicacao/   casos de uso com transação (DoadorService, DoacaoService, EstoqueService)
├── modelo/      entidades JPA e repositórios (Doador, Doacao, Bolsa)
└── api/         controllers REST, DTOs e tratamento de erros (Problem Details, RFC 9457)
src/main/resources/
├── db/migration/V1__schema_inicial.sql   schema versionado (Flyway)
└── static/index.html                      site (HTML + CSS + JS, sem build)
```

As regras ficam no pacote `dominio`, em Java puro, e por isso são testadas em milissegundos, sem subir Spring nem banco. A data de "hoje" é injetada (`Clock`), então nenhum teste depende do dia em que roda.

---

## 3. E1 · Container automatizado: como rodar

Pré-requisito: **Docker**. Do zero, em poucos comandos:

```bash
git clone https://github.com/egs3-coder/SO.git && cd SO
cp .env.example .env            # ajuste a senha
docker compose up -d --build
docker compose ps               # api e db devem aparecer como "healthy" (a API leva ~40 s)
curl http://localhost:8080/actuator/health
```

Depois disso, abra **http://localhost:8080** no navegador para usar o site. Também dá para testar pela linha de comando:

```bash
curl -X POST localhost:8080/doadores -H 'Content-Type: application/json' -d '{
  "nome": "Maria Silva", "cpf": "52998224725", "dataNascimento": "1995-03-10",
  "sexo": "FEMININO", "pesoKg": 62.5, "tipoSanguineo": "O-"
}'
curl localhost:8080/doadores/1/triagem          # H1: apto? por quê?
curl -X POST localhost:8080/doadores/1/doacoes  # H2: registra doação e gera bolsa (35 dias)
curl localhost:8080/estoque                     # bolsas utilizáveis por tipo
curl localhost:8080/estoque/criticos            # T4.1: tipos abaixo do mínimo
curl -X POST localhost:8080/transfusoes -H 'Content-Type: application/json' \
     -d '{"tipoReceptor": "A+", "quantidade": 1}'   # H3: entrega bolsa compatível
```

Para parar: `docker compose down`. Os dados ficam no volume `pgdata`; só `docker compose down -v` apaga.

| Peça | Como está feita | Por quê |
|---|---|---|
| `Dockerfile` | **Multi-stage**: o 1º estágio (`maven:3.9-eclipse-temurin-21-alpine`) compila e roda os testes unitários; o 2º (`eclipse-temurin:21-jre-alpine`) leva só o `.jar` e roda como **usuário não-root** | Teste vermelho = sem imagem. Em produção não entram Maven nem JDK |
| `compose.yaml` | `db` (Postgres 17) + `api`, **volume nomeado `pgdata`**, **healthcheck** nos dois, `depends_on: condition: service_healthy` | A API só sobe com o banco pronto; os dados sobrevivem ao `down` |
| Configuração | Variáveis de ambiente (`DB_HOST`, `DB_PASSWORD`, `PORT`…); `.env.example` versionado e `.env` no `.gitignore` | Nenhum segredo no repositório; a mesma imagem roda local e no Render |

> **GitHub Codespaces:** no Docker do Codespace, a rede entre containers pode falhar (a API dá *Connect timed out* ao falar com o banco). O contorno é um `compose.override.yaml` local, fora do Git, que põe a API na rede do container do banco (`network_mode: "service:db"`, `DB_HOST: localhost`). O `compose.yaml` oficial não muda e funciona no Docker Desktop, no CI e no Render.

Evidências: [`e1-compose-healthy.png`](docs/evidencias/e1-compose-healthy.png) · [`e1-persistencia-volume.png`](docs/evidencias/e1-persistencia-volume.png)

---

## 4. E2 · TDD: story map e testes

### Story map

Story map completo em [`docs/story-map.md`](docs/story-map.md). Ele tem o **backbone** de 5 atividades (a jornada acima). A **Release 1** traz 3 histórias quebradas em 14 tarefas, e a **Release 2** traz 7 tarefas, uma por integrante.

| História (Release 1) | Como… quero… para… | Tarefas |
|---|---|---|
| **H1 · Triagem** | Como atendente, quero saber se o doador está apto para não coletar de quem a norma proíbe | T1.1 idade · T1.2 peso · T1.3 intervalo · T1.4 limite anual · T1.5 endpoint |
| **H2 · Doação** | Como técnico de coleta, quero registrar a doação e gerar a bolsa para que ela entre no estoque com a validade certa | T2.1 validade 35 dias · T2.2 inapto não doa · T2.3 persistência · T2.4 endpoint |
| **H3 · Transfusão** | Como médico, quero receber bolsas compatíveis para transfundir sem risco de reação hemolítica | T3.1 ABO/Rh · T3.2 sem vencidas · T3.3 isogrupo + FEFO · T3.4 recusa sem estoque · T3.5 endpoint |

### Testes por nível

```bash
mvn test      # só unitários (rápido, sem Docker)
mvn verify    # unitários + integração + aceitação (precisa do Docker)
```

| Nível | Onde | O que cobre |
|---|---|---|
| **Unitário** | `src/test/java/.../dominio/*Test.java` | Pelo menos um por tarefa de regra: ABO/Rh, validade, seleção de bolsas, triagem e as 7 regras da Release 2 |
| **Integração** | `BolsaRepositoryIT` (+ `PainelIT`) | Repositórios e migrations Flyway contra **PostgreSQL real** (Testcontainers), com a mesma versão de produção |
| **Aceitação (BDD)** | `src/test/resources/features/*.feature` | Um arquivo Gherkin **em português** por história (H1, H2, H3), via HTTP na aplicação inteira |

### Quais regras ganharam teste primeiro e por quê (risco)

| Ordem | Regra | Por quê |
|---|---|---|
| 1 | Compatibilidade ABO/Rh (T3.1) | Um erro causa reação hemolítica e pode matar o paciente. É o maior risco do sistema |
| 2 | Validade da bolsa (T2.1) e bolsa vencida nunca entregue (T3.2) | Sangue vencido é risco direto ao paciente |
| 3 | Isogrupo + FEFO e recusa por estoque insuficiente (T3.3, T3.4) | Evita gastar O- (doador universal) à toa e não esconde falta de sangue com entrega parcial |
| 4 | Triagem: idade, peso, intervalo, limite anual (T1.1–T1.4) | Protege o doador (Portaria de Consolidação MS nº 5/2017, simplificada) |
| 5 | Release 2 (T1.6–T1.9, T2.5, T4.1, T4.2) | Ampliam a segurança: mais critérios de triagem, CPF válido, alerta de falta e descarte de vencidas |

### Testes na esteira

O job `testes` roda `mvn verify` em todo push e pull request. Ele é **check obrigatório** da `main`: com o teste vermelho, o merge fica bloqueado e nada é publicado.

### O que ficou sem teste (e por quê)

- **Integração das regras de triagem da Release 2 com o cadastro.** Tatuagem, hemoglobina, frequência cardíaca e pressão estão prontas e testadas como regras de domínio. Ainda não entram no `GET /triagem` porque o cadastro não coleta esses dados (fica para a próxima release). O mesmo vale para a validação de CPF (T2.5): o cadastro hoje só confere os 11 dígitos.
- Entrevista clínica, exames sorológicos e fracionamento em hemocomponentes: linhas 2 e 3 do story map, fora do escopo.
- Concorrência real entre transfusões simultâneas: há trava pessimista (`SELECT … FOR UPDATE`), mas não há teste de carga.
- Controllers e DTOs não têm teste unitário próprio; são cobertos pelos cenários de aceitação via HTTP.

---

## 5. Release 2: a entrega de cada integrante, caso a caso

### O ritual (igual para todos)

```
1. branch feat/<tarefa>-<nome>  →  2. commit SÓ com o teste (+ esqueleto vazio da classe)
→  3. check "testes" do PR fica VERMELHO  →  4. commit com o código da regra
→  5. check fica VERDE  →  6. um colega aprova (main exige 1 aprovação, sem bypass)
→  7. merge  →  8. esteira: testes → imagem → deploy automático no Render
```

**Por que o 1º commit leva um "esqueleto"?** É uma classe com o método que só devolve "apto". O código precisa **compilar** para o teste rodar e **falhar pela regra**, não por erro de compilação. Esse é o vermelho legítimo do TDD: o teste descreve o comportamento que ainda não existe.

Prints de cada caso estão em [`docs/evidencias/`](docs/evidencias/), com índice por integrante em [`docs/evidencias/README.md`](docs/evidencias/README.md#release-2--prints-de-cada-integrante).

---

### 5.1 Ewerton · T4.1 · Alerta de estoque crítico · [PR #3](https://github.com/egs3-coder/SO/pull/3)

**Regra.** Um tipo sanguíneo fica **crítico** quando tem menos bolsas utilizáveis que o mínimo: **5 para O- e O+**, **3 para os demais**. Novo endpoint: `GET /estoque/criticos`.

**Por quê.** O- é o doador universal, usado em emergência antes de saber o tipo do paciente, e O+ é o tipo mais comum. Os dois acabam primeiro, por isso o alerta deles dispara mais cedo.

**Testes (`AlertaEstoqueTest`, 4 casos):** estoque cheio não gera alerta · tipo O com 4 é crítico e com 5 não é · demais tipos com 2 é crítico e com 3 não é · tipo ausente conta como zero.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `a2e4b19` *test: alerta de estoque crítico (T4.1) - red* | ❌ vermelho na esteira (3 falhas) |
| Código | `f499b84` *feat: alerta de estoque crítico e endpoint GET /estoque/criticos (T4.1) - green* | ✅ verde |
| Revisão e merge | aprovado por `eloiliima`; merge por `ewertongui` | — |
| Deploy | [execução #12](https://github.com/egs3-coder/SO/actions/runs/39611729274) · `0bf4766` · 4 min 37 s (testes 38 s → imagem 1 min 8 s → deploy 2 min 44 s) | ✅ no ar |

---

### 5.2 Lucas · T4.2 · Descarte de bolsas vencidas · [PR #12](https://github.com/egs3-coder/SO/pull/12)

**Regra.** `DescarteBolsas.paraDescartar(estoque, hoje)` devolve os ids das bolsas **vencidas**, na ordem do estoque. Ela reaproveita `ValidadeBolsa.vencida` (T2.1): a bolsa vale até o último dia, inclusive.

**Por quê.** Uma bolsa vencida não pode ficar parada no estoque, onde poderia ser contada ou entregue por engano.

**Testes (`DescarteBolsasTest`, 4 casos):** venceu ontem → descarta · vence hoje → ainda pode usar · estoque misturado → só as vencidas, na ordem · estoque vazio → nada.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `4363638` (teste + esqueleto) · 21/10 20:41 | ❌ vermelho |
| Código | `34837fc` *feat: descarte de bolsas vencidas (T4.2) - green* · 20:48 | ✅ verde |
| Revisão e merge | aprovado por um colega; merge por `lucasAprigi0` | — |
| Deploy | [execução #28](https://github.com/egs3-coder/SO/actions/runs/39697281558) · `852605a` · 4 min 22 s (35 s → 57 s → 2 min 42 s) | ✅ no ar |

---

### 5.3 João · T1.7 · Hemoglobina mínima · [PR #10](https://github.com/egs3-coder/SO/pull/10)

**Regra.** Para doar, a hemoglobina precisa ser **≥ 13,0 g/dL (homem)** ou **≥ 12,5 g/dL (mulher)**.

**Por quê.** Abaixo do mínimo, o doador pode ficar anêmico depois da doação.

**Testes (`RegraHemoglobinaTest`, 4 casos nos limites):** homem 13,0 apto · homem 12,9 inapto · mulher 12,5 apta · mulher 12,4 inapta.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `73c6d44` *test: hemoglobina mínima por sexo (T1.7) - red* · 20:37 | ❌ vermelho ("Failing after 19s", merge bloqueado) |
| Código | `d4efa48` *feat: regra de hemoglobina mínima na triagem (T1.7) - green* · 20:48 | ✅ verde |
| Revisão e merge | aprovado por um colega; merge por `Saulo1Almeida` | — |
| Deploy | [execução #30](https://github.com/egs3-coder/SO/actions/runs/39698041141) · `26a3f11` · 4 min 14 s (37 s → 50 s → 2 min 39 s) | ✅ no ar |

---

### 5.4 Saulo · T2.5 · CPF válido · [PR #11](https://github.com/egs3-coder/SO/pull/11)

**Regra.** `Cpf.valido(cpf)` exige 11 dígitos e confere os **2 dígitos verificadores** (soma ponderada, resto da divisão por 11). CPFs com todos os dígitos iguais (111.111.111-11…) são inválidos: passam na conta, mas não existem.

**Por quê.** Um CPF digitado errado cria um doador "fantasma" e permite cadastrar a mesma pessoa duas vezes, o que burlaria o limite de doações.

**Testes (`CpfTest`, 5 casos):** CPF correto válido · último dígito errado · todos iguais · menos de 11 dígitos · nulo.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `c0fdd27` *test: CPF com dígitos verificadores (T2.5) - red* · 20:39 | ❌ vermelho |
| Código | `9baf7e1` *feat: validação dos dígitos verificadores do CPF (T2.5) - green* · 20:46 | ✅ verde |
| Revisão e merge | aprovado por um colega; merge por `lucasAprigi0` | — |
| Deploy | [execução #32](https://github.com/egs3-coder/SO/actions/runs/39698399748) · `a925937` · 5 min 22 s (38 s → 52 s → 2 min 54 s) | ✅ no ar |

---

### 5.5 Eloi · T1.9 · Pressão arterial · [PR #14](https://github.com/egs3-coder/SO/pull/14)

**Regra.** Pressão **sistólica ≤ 180 mmHg** e **diastólica ≤ 100 mmHg**. Se as duas estiverem altas, aparecem **os dois motivos**, igual ao `TriagemDoador`.

**Por quê.** Com a pressão muito alta, o doador corre mais risco de passar mal durante a doação.

**Testes (`RegraPressaoArterialTest`, 4 casos):** 180×100 apto · 181×80 inapto pela sistólica · 120×101 inapto pela diastólica · 190×110 os dois motivos.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `01de691` (teste + esqueleto) · 21:43 | ❌ vermelho |
| Código | `106c78e` (implementação da regra) · 21:49 | ✅ verde |
| Revisão e merge | aprovado por `joaobrito-dev`; merge por `eloiliima` | — |
| Deploy | [execução #38](https://github.com/egs3-coder/SO/actions/runs/39703058746) · `e0b0b35` · 4 min 35 s (44 s → 1 min 15 s → 2 min 29 s) | ✅ no ar |

---

### 5.6 Pablo · T1.6 · Tatuagem recente · [PR #16](https://github.com/egs3-coder/SO/pull/16)

**Regra.** Quem fez **tatuagem ou maquiagem definitiva há menos de 12 meses** está inapto. Quem nunca fez está apto.

**Por quê.** Infecções como a hepatite podem não aparecer nos exames logo depois do contágio. A espera de 12 meses cobre essa "janela".

**Testes (`RegraTatuagemTest`, 4 casos):** nunca fez → apto · há 11 meses → inapto · ontem → inapto · há exatamente 12 meses → apto.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `47d70e8` (teste + esqueleto) · 22/10 09:47 | ❌ vermelho ("Failing after 22s") |
| Código | `ef5fae5` (implementação da regra) · 09:52 | ✅ verde |
| Revisão e merge | aprovado por um colega; merge por `ewertongui` | — |
| Deploy | [execução #43](https://github.com/egs3-coder/SO/actions/runs/39782395429) · `5240cc3` · 5 min 26 s (49 s → 1 min 3 s → 3 min 26 s) | ✅ no ar |

> **O que deu errado antes (e como corrigimos):** na primeira tentativa ([PR #13](https://github.com/egs3-coder/SO/pull/13)), os arquivos foram enviados para dentro de uma pasta `1-RED/`, fora de `src/`. O Maven não os enxergou, então nenhum teste novo rodou: a esteira ficou verde sem testar nada, e o PR foi mergeado. Percebemos o erro, removemos a pasta no [PR #15](https://github.com/egs3-coder/SO/pull/15) e refizemos a tarefa corretamente no PR #16, desta vez com o vermelho de verdade.

---

### 5.7 Thiago · T1.8 · Frequência cardíaca · [PR #17](https://github.com/egs3-coder/SO/pull/17)

**Regra.** O pulso precisa estar **entre 50 e 100 batimentos por minuto**, com os limites incluídos.

**Por quê.** Fora dessa faixa, o pulso pode indicar problema cardíaco, febre ou nervosismo; nesses casos o doador não deve doar naquele dia.

**Testes (`RegraFrequenciaCardiacaTest`, 4 casos nos limites):** 49 inapto · 50 apto · 100 apto · 101 inapto.

| Etapa | Commit | Resultado |
|---|---|---|
| Teste primeiro | `e3be19a` (teste + esqueleto, conta `thcardozo`) · 22/10 09:55 | ❌ vermelho |
| Código | `24d7d2c` (`thcardozo`, 10:13) + `fee0bf5` (correção do caminho, `pael-pixel`, 10:21) | ✅ verde |
| Revisão e merge | aprovado depois do verde; merge por `pael-pixel` | — |
| Deploy | [execução #48](https://github.com/egs3-coder/SO/actions/runs/39786359771) · `f241ea0` · 4 min 38 s (36 s → 1 min 12 s → 2 min 40 s) | ✅ no ar |

> **O que deu errado (e como corrigimos):** o upload do código foi feito arrastando a pasta de fora, e o arquivo foi parar em `src/src/main/...`. O Maven não compila esse caminho, então o check continuou vermelho, e foi o próprio gate que mostrou o problema. Pablo reenviou o arquivo no caminho certo (`fee0bf5`), o check ficou verde e só então o PR foi aprovado. Uma aprovação dada enquanto o check ainda estava vermelho foi descartada automaticamente pelo GitHub com o commit novo ("dismiss stale approvals").

---

## 6. E3 · Pipeline, deploy e rollback

Arquivo: [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml).

```
pull request ──► testes (mvn verify) ──► ✅ libera o merge / ❌ bloqueia
merge na main ──► testes ──► imagem (Docker → GHCR, tag = SHA) ──► deploy (Render) ──► confere /api/info
manual ──► rollback (informa o SHA de uma versão anterior)
```

| Job | Quando roda | O que faz |
|---|---|---|
| `testes` | todo push e PR | `mvn verify`: unitário + integração (Postgres real) + aceitação. **Check obrigatório** |
| `imagem` | push na `main` com testes verdes | Build multi-stage e push para `ghcr.io/egs3-coder/so:<SHA>` e `:latest` |
| `deploy` | depois da imagem | Chama o deploy hook do Render e **espera a URL responder com o SHA novo** em `/api/info`. Só então fica verde |
| `rollback` | manual (*Run workflow*) | Republica no Render a imagem de um SHA anterior, que já está no GHCR (sem rebuild) |

### Fluxo de branch: GitHub Flow, aplicado de verdade

Toda mudança entra por branch curta e pull request. Proteção da `main` (*Settings → Branches*):

- ✅ Pull request obrigatório, com **1 aprovação**;
- ✅ **Aprovações antigas descartadas** quando entra commit novo;
- ✅ Check **`testes`** obrigatório, com a branch atualizada com a `main`;
- ✅ **"Do not allow bypassing the above settings"**: nem o administrador faz merge sem aprovação;
- ❌ Force push e exclusão da `main` proibidos.

### Deploy no Render e segredos

- Ambiente criado pelo **Blueprint** ([`render.yaml`](render.yaml)): Web Service por imagem Docker + PostgreSQL gerenciado, com `autoDeploy: false` para que só a esteira publique.
- **Segredos:** a URL do deploy hook fica no GitHub Secrets (`RENDER_DEPLOY_HOOK_URL`); `APP_URL` é uma variável do Actions; a senha do banco de produção é gerada e guardada pelo Render. Nenhum segredo no repositório.

### Como voltar à versão anterior (rollback)

1. *Actions → CI/CD → Run workflow*.
2. Colar o **SHA completo (40 caracteres)** de uma versão anterior, que fica no histórico de commits ou nas execuções.
3. O job republica a imagem daquele SHA e só fica verde quando `/api/info` responde com ela (~3 min).

Testado nos dois sentidos: `4566e43` → `5e879ec` → `4566e43` ([`e3-rollback.png`](docs/evidencias/e3-rollback.png), [`e3-rollback-retorno.png`](docs/evidencias/e3-rollback-retorno.png)). Plano B: *Rollback* no painel do Render.

---

## 7. Evidências

Todas as imagens estão em **[`docs/evidencias/`](docs/evidencias/)**, com o índice completo em [`docs/evidencias/README.md`](docs/evidencias/README.md).

| Critério | Evidência |
|---|---|
| E1 · compose sobe do zero, `api` e `db` healthy | [e1-compose-healthy.png](docs/evidencias/e1-compose-healthy.png) |
| E1 · dados persistem no volume | [e1-persistencia-volume.png](docs/evidencias/e1-persistencia-volume.png) |
| E2 · histórico red → green da Release 1 | [e2-historico-red-green.txt](docs/evidencias/e2-historico-red-green.txt) |
| E2 · red → green da Release 2 (um PR por integrante) | [seção 5](#5-release-2-a-entrega-de-cada-integrante-caso-a-caso) + [prints por integrante](docs/evidencias/README.md#release-2--prints-de-cada-integrante) |
| E3 · execução **vermelha** que barrou o merge | [e3-pr-vermelho-bloqueado.png](docs/evidencias/e3-pr-vermelho-bloqueado.png) + PRs vermelhos da Release 2 |
| E3 · execução **verde** que publicou | [e3-deploy-verde.png](docs/evidencias/e3-deploy-verde.png) + as 7 execuções da tabela da [seção 1](#1-integrantes-e-tarefas) |
| E3 · URL no ar | https://hemocentro-api.onrender.com · [e3-url-no-ar.png](docs/evidencias/e3-url-no-ar.png) |
| E3 · rollback e retorno | [e3-rollback.png](docs/evidencias/e3-rollback.png) · [e3-rollback-retorno.png](docs/evidencias/e3-rollback-retorno.png) |
| E3 · `main` protegida (1 aprovação, sem bypass) | [e3-main-protegida.png](docs/evidencias/e3-main-protegida.png) · [regra completa](docs/evidencias/1792601920050_image.png) · [continuação](docs/evidencias/1792601927865_image.png) |
| E3 · ambiente criado pelo Blueprint e deployments | [e3-render-blueprint.png](docs/evidencias/e3-render-blueprint.png) · [e3-deployments-producao.png](docs/evidencias/e3-deployments-producao.png) |

---

## 8. Processo e histórico (com transparência)

**Release 1 (base do projeto).** A estrutura inicial foi gerada com IA numa única sessão, pela conta do Product Owner (`egs3-coder`), e enviada de uma vez: as regras H1–H3, os testes, o Dockerfile, o compose e a esteira. São 26 commits em cerca de 9 minutos, **antes de a esteira existir**. A ordem `test:` → `feat:` está correta, mas o vermelho desses testes nunca rodou no CI, e os outros integrantes não aparecem nesse histórico. Depois, o grupo validou tudo rodando de verdade: esteira, PR barrado de propósito (#1, validade 35 → 30 dias), deploy, rollback e compose local.

**Ajustes pedidos pelo professor (21/10).** Atendidos:

1. o README completo foi restaurado (PR #4);
2. os prints foram para `docs/evidencias/` (PRs #5, #8 e #18);
3. a `main` passou a exigir **1 aprovação, sem bypass de admin**;
4. **cada integrante entregou 1 tarefa via PR com a própria conta e o teste vermelho primeiro** (seção 5).

**O que aprendemos com os erros da Release 2:**
- **Upload na pasta errada.** O GitHub mostra o caminho completo do arquivo antes do commit. Se não começa com `src/`, o Maven não enxerga. Isso aconteceu no PR #13 (Pablo) e no #17 (Thiago).
- **Aprovar só depois do verde.** A regra "dismiss stale approvals" protege: uma aprovação dada com o check vermelho é descartada quando entra o commit novo.
- **Esteira verde nem sempre quer dizer testado.** No PR #13 a esteira ficou verde porque os arquivos estavam fora de `src/` e nenhum teste novo rodou. Por isso, conferimos no log quantos testes rodaram.

**Problemas de infraestrutura resolvidos:** o primeiro deploy falhou porque os secrets ainda não existiam (foram criados e a execução foi re-rodada); um rollback com SHA incompleto falhou de forma segura (exit 22, a produção não foi afetada); a rede entre containers do Codespace foi contornada com o `compose.override.yaml` local.

---

## 9. Decisões

| Decisão | Opções comuns | O que escolhemos e por quê |
|---|---|---|
| Linguagem e framework | Java/Spring, Node/Nest, Python/FastAPI | **Java 21 + Spring Boot 3.5**: stack trabalhada em aula, com o ecossistema de testes mais maduro (JUnit, Testcontainers, Cucumber). |
| Imagem base | `eclipse-temurin:21-jre`, `-jre-alpine`, `openjdk` | **`eclipse-temurin:21-jre-alpine`** no runtime: só o JRE, imagem menor e com menos pacotes vulneráveis; `openjdk` foi descontinuada. Usuário não-root. |
| Multi-stage | Imagem única, build fora do Docker, multi-stage | **Multi-stage**: o 1º estágio compila e testa (teste vermelho = sem imagem); o 2º leva só o `.jar`. Vai para produção exatamente o que foi testado. |
| Banco | PostgreSQL, MySQL, MongoDB | **PostgreSQL 17**: dados relacionais (doador → doação → bolsa), transações e `SELECT … FOR UPDATE`; Postgres gerenciado no Render. |
| Onde ficam os dados | Volume nomeado, bind mount | **Volume nomeado `pgdata`**: sobrevive ao `down` (testado) e não depende das pastas da máquina. |
| Schema | `ddl-auto: update`, Flyway, Liquibase | **Flyway** (`V1__schema_inicial.sql`) + Hibernate só valida: schema versionado e idêntico em todo ambiente. |
| Rede do Compose | Rede padrão, customizada, portas no host | **Rede padrão**: a API acha o banco por `db`; o banco não publica porta no host. |
| Ordem de subida | `depends_on` simples, script de espera, healthcheck | **`depends_on: service_healthy`** + healthchecks (`pg_isready`, `/actuator/health`). |
| Configuração | No código, `.env`, variáveis de ambiente | **Variáveis de ambiente**, `.env.example` versionado, `.env` no `.gitignore` (12-factor). |
| Ordem dos testes | — | **ABO/Rh primeiro** (risco de morte), depois validade, seleção, triagem e Release 2. |
| Níveis de teste | Só unitário, H2, banco real | **Unitário** no domínio puro, **integração com Postgres real** (Testcontainers, não H2) e **aceitação em Gherkin** em português. |
| Fluxo de branch | Git Flow, GitHub Flow, GitLab Flow | **GitHub Flow**: uma versão no ar e deploy a cada merge; Git Flow seria burocracia sem ganho. |
| CI/CD | GitHub Actions, GitLab CI, Jenkins | **GitHub Actions**: o código já está no GitHub, sem servidor para manter, e o GHCR vem integrado. |
| Provedor | Render, Railway, Fly.io | **Render**: gratuito, deploy por imagem, Postgres gerenciado e Blueprint (`render.yaml`). |
| Disparo do deploy | Auto-deploy do provedor, deploy hook | **Deploy hook pela esteira** (`autoDeploy: false`): só publica o que passou nos testes, e o job confere a versão no ar. |
| Segredos | `.env`, secrets da ferramenta | **GitHub Secrets** para o hook; senha do banco gerada pelo Render; nada no repositório. |
| Rollback | Painel do Render, reverter commit, republicar imagem | **Republicar a imagem antiga pelo SHA** (workflow manual, ~3 min, sem rebuild). Painel do Render como plano B. |
| Interface web | SPA com framework, página estática | **Página estática servida pelo próprio Spring** (`static/index.html`): sem build extra nem segundo serviço, publicada pela mesma esteira e com o mesmo gate de testes. |
| Entrega da Release 2 | Um integrante faz tudo, cada um sua tarefa | **Uma tarefa por integrante, com a própria conta, red → green no PR**: mostra o TDD na esteira e a participação de todos. |

---

## 10. Uso de IA

Usamos o **Claude (Anthropic)** como assistente durante o desafio. **A IA rascunhou; o grupo validou, executou e assina.**

**O que a IA fez:**
- propôs o story map e as regras (com base na norma do Ministério da Saúde, simplificada);
- escreveu os testes e o código da Release 1 no ciclo red → green;
- criou o Dockerfile, o compose, o workflow, o `render.yaml`, os scripts e o site;
- montou para cada integrante um guia da sua tarefa da Release 2;
- guiou a configuração do GitHub e do Render a partir de prints e diagnosticou os erros;
- redigiu o README e o relatório.

**O que o grupo fez:**
- definiu tema, stack e provedor;
- criou e configurou o repositório, os secrets, o Blueprint e a proteção da `main`;
- cada integrante subiu a sua tarefa pela própria conta, conferiu o vermelho e o verde na esteira e revisou o PR de um colega;
- executou e testou tudo de verdade (esteira, deploy, rollback, compose local).

Cada PR da Release 2 traz no comentário a declaração *"Uso de IA: o Claude ajudou a escrever o teste e o código; revisei e sei explicar cada caso."*

**Como revisamos:**
- rodando, não só lendo: os três níveis de teste na esteira, a API no ar e no compose local;
- provocando uma falha de propósito (35 → 30 dias) para ver o merge barrado;
- conferindo as regras contra a norma;
- testando o rollback nos dois sentidos e a persistência do volume.

**Prompts principais:**
1. *"Faça esse desafio (PDF do enunciado) atendendo todos os critérios; o foco primeiro é o desafio e seus componentes principais para teste."* Stack: Java + Spring Boot, do zero, GitHub Actions + Render.
2. *"O tema seria em relação a hemocentro."*
3. *"Me diga o passo a passo para eu rodar ele na versão online pelo GitHub."*
4. Envio de prints de cada etapa (Actions, Render, PRs, Codespace) com *"o que eu faço agora?"*.
5. *"O professor pediu: restaurar o README, mover os prints para docs/evidencias, exigir 1 aprovação sem bypass e cada integrante entregar 1 tarefa via PR com o teste vermelho primeiro."*
6. *"Me diga etapa por etapa o que cada integrante tem que fazer."*
7. *"Crie o site com a estrutura da gente e essa paleta"* e *"monte o PDF e o README detalhados, caso por caso."*

---

## 11. Checklist dos critérios do enunciado

| Critério | Onde está |
|---|---|
| **E1** Dockerfile multi-stage | [`Dockerfile`](Dockerfile) · [seção 3](#3-e1--container-automatizado-como-rodar) |
| **E1** compose com API + banco, volume, healthcheck, depends_on | [`compose.yaml`](compose.yaml) · [e1-compose-healthy.png](docs/evidencias/e1-compose-healthy.png) |
| **E1** configuração fora do código, sem segredos | `.env.example`, `.gitignore`, `application.yml` |
| **E1** sobe do zero em poucos comandos | [seção 3](#3-e1--container-automatizado-como-rodar) |
| **E2** story map com backbone e Release 1; 3 histórias em tarefas | [`docs/story-map.md`](docs/story-map.md) · [seção 4](#4-e2--tdd-story-map-e-testes) |
| **E2** unitário por tarefa, integração com banco, aceitação por história | `src/test/java`, `BolsaRepositoryIT`, `src/test/resources/features` |
| **E2** histórico red → green | [e2-historico-red-green.txt](docs/evidencias/e2-historico-red-green.txt) + 7 PRs da [seção 5](#5-release-2-a-entrega-de-cada-integrante-caso-a-caso) |
| **E2** testes na esteira; vermelho barra o merge | job `testes` obrigatório · [e3-pr-vermelho-bloqueado.png](docs/evidencias/e3-pr-vermelho-bloqueado.png) |
| **E3** pipeline build → testes → imagem → deploy | [`.github/workflows/ci-cd.yml`](.github/workflows/ci-cd.yml) · [seção 6](#6-e3--pipeline-deploy-e-rollback) |
| **E3** fluxo de branch aplicado e `main` protegida | PRs #1–#18 · [e3-main-protegida.png](docs/evidencias/e3-main-protegida.png) |
| **E3** deploy automático, segredos no cofre, URL no ar | [`render.yaml`](render.yaml) · GitHub Secrets · https://hemocentro-api.onrender.com |
| **E3** execução vermelha e verde; rollback | [seção 7](#7-evidências) |
| Justificativas (tabela de decisões) | [seção 9](#9-decisões) |
| Uso de IA declarado, com prompts | [seção 10](#10-uso-de-ia) |
| Relatório em PDF | [`docs/Relatorio-Desafio-PI-Hemocentro.pdf`](docs/Relatorio-Desafio-PI-Hemocentro.pdf) |
