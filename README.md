# API Hemocentro — Projeto Integrador

API REST que leva o sangue do doador ao paciente com segurança: triagem do doador, registro da doação com geração de bolsa, controle de estoque por validade e atendimento de transfusões com compatibilidade ABO/Rh.

**URL no ar:** https://hemocentro-api.onrender.com

Java 21 · Spring Boot 3.5 · PostgreSQL 17 · Docker Compose · JUnit 5 · Testcontainers · Cucumber · GitHub Actions · Render

## Integrantes

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

### Tarefa de cada integrante (Release 2)

| Integrante | Tarefa | PR |
|---|---|---|
| Ewerton Guilherme da Silva | T4.1 · Alerta de estoque crítico (`GET /estoque/criticos`) | [#3](https://github.com/egs3-coder/SO/pull/3) ✅ no ar |
| Pablo Arthur Eustáquio de Lima | T1.6 · Bloqueio por tatuagem recente | _a preencher_ |
| João Ricardo Alves de Brito | T1.7 · Hemoglobina mínima | _a preencher_ |
| Lucas Aprigio dos Santos | T4.2 · Descartar bolsas vencidas | _a preencher_ |
| Saulo Eduardo Almeida dos Santos | T2.5 · Dígitos verificadores do CPF | _a preencher_ |
| Thiago Cardozo da Conceição | T1.8 · Frequência cardíaca | _a preencher_ |
| Eloi de Lima Sousa | T1.9 · Pressão arterial | _a preencher_ |

Cada integrante entrega a sua tarefa por pull request, com a própria conta do GitHub, seguindo o ciclo: **1º commit só com o teste (vermelho na esteira) → 2º commit com o código (verde) → aprovação de um colega → merge → deploy automático.** O detalhe de cada tarefa está em [`docs/story-map.md`](docs/story-map.md#release-2--uma-tarefa-por-integrante).

## Como rodar

Pré-requisito: Docker. Do zero, com poucos comandos:

```bash
git clone https://github.com/egs3-coder/SO.git && cd SO
cp .env.example .env          # ajuste a senha
docker compose up -d --build
docker compose ps             # api e db devem aparecer como "healthy" (a API leva ~40 s)
curl http://localhost:8080/actuator/health
```

Exemplo de uso:

```bash
# cadastrar doador
curl -X POST localhost:8080/doadores -H 'Content-Type: application/json' -d '{
  "nome": "Maria Silva", "cpf": "12345678901", "dataNascimento": "1995-03-10",
  "sexo": "FEMININO", "pesoKg": 62.5, "tipoSanguineo": "O-"
}'

curl localhost:8080/doadores/1/triagem          # H1: apto? por quê?
curl -X POST localhost:8080/doadores/1/doacoes  # H2: registra doação e gera bolsa (35 dias)
curl localhost:8080/estoque                     # bolsas utilizáveis por tipo
curl localhost:8080/estoque/criticos            # T4.1: tipos abaixo do estoque mínimo
curl -X POST localhost:8080/transfusoes -H 'Content-Type: application/json' \
     -d '{"tipoReceptor": "A+", "quantidade": 1}'   # H3: entrega bolsa compatível
```

Para parar: `docker compose down` (os dados ficam no volume `pgdata`; `down -v` apaga).

> **GitHub Codespaces:** no Docker do Codespace a rede entre containers pode não funcionar (a API dá *Connect timed out* ao falar com o banco). Nesse caso, crie localmente um `compose.override.yaml` que coloca a API na rede do container do banco (`network_mode: "service:db"`, `DB_HOST: localhost`, porta 8080 publicada no `db`). O `compose.yaml` do repositório não muda e funciona normalmente no Docker Desktop, no CI e no Render.

## Testes

```bash
mvn test      # só unitários (rápido, sem Docker)
mvn verify    # unitários + integração + aceitação (precisa do Docker rodando)
```

| Nível | Onde | O que cobre |
|---|---|---|
| Unitário | `src/test/java/.../dominio/*Test.java` | Uma classe ou `@Nested` por tarefa: compatibilidade ABO/Rh, validade, seleção de bolsas, idade, peso, intervalo, limite anual |
| Integração | `BolsaRepositoryIT` | Repositórios + migrations Flyway contra PostgreSQL real (Testcontainers) |
| Aceitação (BDD) | `src/test/resources/features/*.feature` | Um arquivo Gherkin por história (H1, H2, H3), via HTTP na aplicação inteira |

- **Story map e tarefas:** [`docs/story-map.md`](docs/story-map.md). Backbone com 5 atividades, Release 1 com 3 histórias quebradas em 14 tarefas.
- **Histórico red → green (Release 1):** [`docs/evidencias/e2-historico-red-green.txt`](docs/evidencias/e2-historico-red-green.txt). Cada commit `test: ... - red` vem antes do `feat: ... - green` da mesma tarefa. Esses commits foram feitos antes de o pipeline existir, então o vermelho deles não aparece na esteira (ver [Processo e histórico](#processo-e-histórico)).
- **Histórico red → green (Release 2):** cada tarefa nova entra por PR com o teste primeiro. O vermelho e o verde ficam registrados nos checks do próprio PR.
- **Testes na esteira:** o job `testes` roda `mvn verify` em todo push e pull request. É check obrigatório da `main`: teste vermelho barra o merge.

### Quais regras ganharam teste primeiro e por quê (risco)

| Ordem | Regra | Por quê |
|---|---|---|
| 1 | Compatibilidade ABO/Rh (T3.1) | Um erro aqui causa reação hemolítica e pode matar o paciente. É o maior risco do sistema. |
| 2 | Validade da bolsa (T2.1) e bolsa vencida nunca entregue (T3.2) | Transfundir sangue vencido é risco direto ao paciente. |
| 3 | Seleção por isogrupo + FEFO e recusa por estoque insuficiente (T3.3, T3.4) | Evita gastar O- (doador universal) à toa e esconder falta de sangue com entrega parcial. |
| 4 | Triagem: idade, peso, intervalo e limite anual (T1.1–T1.4) | Protege a saúde do doador e segue a norma (Portaria de Consolidação MS nº 5/2017, simplificada). |

### O que ficou sem teste

- Entrevista clínica, exames sorológicos, bloqueios temporários (tatuagem, viagem) e fracionamento em hemocomponentes: fora da Release 1 (linhas 2 e 3 do story map).
- Validação de CPF (só o formato de 11 dígitos é verificado) e concorrência real entre transfusões simultâneas (há trava pessimista no banco, mas sem teste de carga).
- Controllers e DTOs não têm teste unitário próprio: são cobertos pelos cenários de aceitação via HTTP.

## Pipeline e deploy

`.github/workflows/ci-cd.yml`: **build → testes → imagem → deploy**.

- **PR para main:** roda `testes` (`mvn verify`). Vermelho barra o merge.
- **Merge na main:** `testes` → `imagem` (Dockerfile multi-stage, push no GHCR com tag = SHA do commit) → `deploy` (o Render puxa a imagem pelo deploy hook; o job só fica verde quando a versão nova responde na URL pública).
- **Rollback:** Actions → CI/CD → *Run workflow* → informar o SHA de uma versão anterior. A imagem daquela versão já está no GHCR e volta ao ar em cerca de 3 minutos. Testado: `4566e43` → `5e879ec` → `4566e43` (ver evidências).

### Configuração (uma vez)

1. Rodar a esteira uma vez (push na main) para publicar a imagem; em *Packages* no GitHub, tornar o pacote **público**.
2. No Render: *New → Blueprint*, apontar para o repositório (usa o `render.yaml`). Isso cria a API e o Postgres.
3. No Render, na API: *Settings → Deploy Hook* → copiar a URL.
4. No GitHub, *Settings → Secrets and variables → Actions*:
   - Secret `RENDER_DEPLOY_HOOK_URL` = URL do deploy hook
   - Variable `APP_URL` = `https://hemocentro-api.onrender.com`
5. Proteger a main em *Settings → Branches*: PR obrigatório com **1 aprovação**, check `testes` obrigatório e **"Do not allow bypassing the above settings"** ligado (sem bypass de admin).

## Processo e histórico

Registramos aqui, com transparência, como o trabalho aconteceu.

**Release 1 (base do projeto).** A estrutura inicial (regras das histórias H1, H2 e H3, testes, Dockerfile, compose e pipeline) foi gerada em uma única sessão com IA, pela conta do Product Owner (`egs3-coder`), e enviada ao repositório de uma vez: são 26 commits feitos em cerca de 9 minutos, **antes de o pipeline existir**. A ordem `test:` → `feat:` dos commits está correta, mas o vermelho de cada teste **nunca rodou na esteira**, e os outros seis integrantes não aparecem nesse histórico. Depois disso, o grupo validou tudo rodando de verdade: esteira, PR barrado de propósito (#1), deploy, rollback e compose local.

**Release 2 (processo de cada integrante).** Para mostrar o TDD acontecendo na esteira e a participação de cada um, cada integrante entrega uma tarefa nova por PR, com a própria conta:

1. cria uma branch `feat/<tarefa>-<nome>`;
2. sobe **só o teste** → o check `testes` do PR fica **vermelho**;
3. sobe o código → o check fica **verde**;
4. um colega revisa e **aprova** (a `main` exige 1 aprovação e não tem bypass de admin);
5. merge → `imagem` → `deploy` automático.

**Primeira entrega da Release 2:** [PR #3](https://github.com/egs3-coder/SO/pull/3) (Ewerton, T4.1). O commit `test: ... - red` ficou vermelho na esteira (76 testes, 3 falhas) e o commit `feat: ... - green` ficou verde (76 testes, 0 falhas). Depois da aprovação de um colega, o merge publicou a versão `0bf4766` com o endpoint `/estoque/criticos`.

**Proteção da `main`:** PR obrigatório, 1 aprovação, check `testes` obrigatório e atualizado, e "Do not allow bypassing the above settings" ligado. Nem o administrador faz merge sem aprovação.

## Decisões

| Decisão | Opções comuns | O que escolhemos e por quê |
|---|---|---|
| Linguagem e framework | Java/Spring, Node/Nest, Python/FastAPI | **Java 21 + Spring Boot 3.5**: é a stack trabalhada em aula (ola-java), com ótimo suporte a testes (JUnit, Testcontainers, Cucumber). |
| Imagem base | `eclipse-temurin:21-jre`, `-jre-alpine`, `openjdk` | **`eclipse-temurin:21-jre-alpine`** no runtime: só o JRE, sem compilador nem Maven, imagem menor e com menos pacotes vulneráveis. A compilação fica no 1º estágio (`maven:3.9-eclipse-temurin-21-alpine`). Roda como usuário não-root. |
| Por que multi-stage | Imagem única, build fora do Docker, multi-stage | **Multi-stage**: o 1º estágio compila e roda os testes unitários (teste vermelho = sem imagem); o 2º leva só o `.jar`. O que vai para produção é exatamente o que foi testado, sem ferramentas de build. |
| Banco de dados | PostgreSQL, MySQL, MongoDB | **PostgreSQL 17**: relacional (doador → doação → bolsa), transações e `SELECT ... FOR UPDATE` para duas transfusões não levarem a mesma bolsa. O Render oferece Postgres gerenciado. |
| Onde ficam os dados | Volume nomeado, bind mount | **Volume nomeado `pgdata`**: os dados sobrevivem ao `docker compose down` (testado) e não dependem da estrutura de pastas da máquina. |
| Schema do banco | `ddl-auto: update`, Flyway, Liquibase | **Flyway** (`V1__schema_inicial.sql`) + Hibernate só valida: o schema é versionado no Git e igual em todo ambiente. |
| Rede do Compose | Rede padrão, rede customizada, portas no host | **Rede padrão do Compose**: a API acha o banco pelo nome do serviço (`DB_HOST=db`). O banco não publica porta no host, só a API (8080). |
| Ordem de subida | `depends_on` simples, script de espera, healthcheck | **`depends_on: condition: service_healthy`** + healthcheck (`pg_isready` no banco, `/actuator/health` na API): a API só sobe com o banco pronto. |
| Configuração | Valores no código, `.env`, variáveis de ambiente | **Variáveis de ambiente** (`DB_HOST`, `DB_PASSWORD`, `PORT`…) com `.env.example` versionado e `.env` no `.gitignore`. |
| Qual regra testar primeiro | — | **Compatibilidade ABO/Rh primeiro**: um erro ali põe um paciente em risco. Depois validade, seleção de bolsas e triagem (tabela de risco acima). |
| Tipos de teste | Só unitário, H2 em memória, banco real | **Unitário** no domínio puro (rápido, sem Spring), **integração com Postgres real** via Testcontainers (mesmo banco da produção, não um H2 "parecido") e **aceitação em Gherkin** com Cucumber. |
| Fluxo de branch | Git Flow, GitHub Flow, GitLab Flow | **GitHub Flow**: grupo pequeno, uma versão no ar e deploy a cada merge. Git Flow (develop, release, hotfix) seria burocracia sem ganho para uma única versão em produção. |
| Ferramenta de CI/CD | GitHub Actions, GitLab CI, Jenkins | **GitHub Actions**: o código já está no GitHub, sem servidor para manter, e o GHCR (registro de imagens) vem junto. |
| Provedor de deploy | Render, Railway, Fly.io | **Render**: plano gratuito com deploy por imagem Docker, Postgres gerenciado e Blueprint (`render.yaml`) que cria tudo a partir do repositório. |
| Como o deploy é disparado | Auto-deploy do provedor, deploy hook pela esteira | **Deploy hook chamado pela esteira** (`autoDeploy: false` no Render): só publica se testes e imagem passaram. O job confere a versão no ar antes de ficar verde. |
| Segredos | `.env` local, secrets da ferramenta | **URL do deploy hook no GitHub Secrets**; senha do banco gerada pelo Render e injetada como variável de ambiente; `.env` no `.gitignore`. Nenhum segredo no repositório. |
| Rollback | Rollback no painel do Render, reverter commit, republicar imagem antiga | **Republicar a imagem antiga pelo SHA** (workflow manual): cada versão fica no GHCR com a tag do commit, então voltar não exige rebuild. O painel do Render também permite rollback como plano B. |

## Uso de IA

Usamos o **Claude (Anthropic)** como assistente em todo o desafio. A IA rascunhou; o grupo validou, executou e assina.

**O que a IA fez:**
- Propôs o story map e as regras de negócio do hemocentro (com base na norma do Ministério da Saúde, simplificada).
- Escreveu os testes e o código no ciclo red → green, commit a commit.
- Criou o Dockerfile, o `compose.yaml`, o workflow do GitHub Actions, o `render.yaml` e os scripts.
- Guiou a configuração do GitHub, do Render e do Codespace passo a passo, e diagnosticou os problemas que apareceram (deploy sem secrets, SHA errado no rollback, rede entre containers no Codespace).

**O que a IA não fez:** as tarefas da Release 2 são escritas, explicadas e enviadas por cada integrante, que pode usar a IA como apoio (declarado no próprio PR), mas precisa saber explicar o teste e o código na apresentação.

**Como revisamos:**
- Rodamos tudo de verdade: a esteira executou os testes (unitários, integração e aceitação), o deploy foi feito no Render e a API foi testada no ar e localmente.
- Provocamos uma falha de propósito (validade de 35 → 30 dias) para confirmar que o teste barra o merge.
- Conferimos as regras de negócio (idade, peso, intervalos, compatibilidade ABO/Rh) contra a norma.
- Testamos o rollback nos dois sentidos e a persistência do volume.

**Prompts principais:**
1. *"Faça esse desafio (PDF do enunciado) atendendo todos os critérios; o foco primeiro é o desafio e seus componentes principais para teste."* Stack escolhida: Java + Spring Boot, projeto do zero, GitHub Actions + Render.
2. *"O tema seria em relação a hemocentro."*
3. *"Me diga o passo a passo para eu rodar ele na versão online pelo GitHub."*
4. Envio de prints de cada etapa (Actions, Render, PR, Codespace) pedindo o próximo passo e a correção de erros.
5. *"Baseado nos prints que eu mandei, faça os arquivos de evidência."*
6. *"O professor pediu: restaurar o README, mover os prints para docs/evidencias, exigir 1 aprovação na main sem bypass e cada integrante entregar 1 tarefa via PR com o teste vermelho primeiro. Me ajude com a minha parte."*

## Evidências

Índice completo em [`docs/evidencias/`](docs/evidencias/README.md).

| Item | Evidência |
|---|---|
| E1: compose sobe do zero, api e db healthy | [e1-compose-healthy.png](docs/evidencias/e1-compose-healthy.png) |
| E1: dados persistem no volume | [e1-persistencia-volume.png](docs/evidencias/e1-persistencia-volume.png) |
| E2: histórico red → green | [e2-historico-red-green.txt](docs/evidencias/e2-historico-red-green.txt) |
| E3: execução vermelha que barrou o merge | [e3-pr-vermelho-bloqueado.png](docs/evidencias/e3-pr-vermelho-bloqueado.png) |
| E3: execução verde que publicou | [e3-deploy-verde.png](docs/evidencias/e3-deploy-verde.png) |
| E3: URL no ar | [e3-url-no-ar.png](docs/evidencias/e3-url-no-ar.png) |
| E3: rollback e retorno | [e3-rollback.png](docs/evidencias/e3-rollback.png), [e3-rollback-retorno.png](docs/evidencias/e3-rollback-retorno.png) |
| E3: main protegida | [e3-main-protegida.png](docs/evidencias/e3-main-protegida.png) |
| Release 2: PRs de cada integrante (vermelho → verde → aprovação → deploy) | links na tabela de [Integrantes](#integrantes) |
