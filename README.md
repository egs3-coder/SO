# API Hemocentro — Projeto Integrador

API REST que leva o sangue do doador ao paciente com segurança: triagem do doador, registro da doação com geração de bolsa, controle de estoque por validade e atendimento de transfusões com compatibilidade ABO/Rh.

Java 21 · Spring Boot 3.5 · PostgreSQL 17 · Docker Compose · JUnit 5 · Testcontainers · Cucumber · GitHub Actions · Render

## Integrantes

> _a preencher_

## Como rodar

Pré-requisito: Docker. Do zero, com um comando:

```bash
git clone <url-do-repositorio> && cd hemocentro
cp .env.example .env          # ajuste a senha
docker compose up -d --build
docker compose ps             # api e db devem aparecer como "healthy"
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
curl -X POST localhost:8080/doadores/1/doacoes  # H2: registra doação e gera bolsa
curl localhost:8080/estoque                     # bolsas utilizáveis por tipo
curl -X POST localhost:8080/transfusoes -H 'Content-Type: application/json' \
     -d '{"tipoReceptor": "A+", "quantidade": 1}'   # H3: entrega bolsa compatível
```

Para parar: `docker compose down` (os dados ficam no volume `pgdata`; `down -v` apaga).

## Testes

```bash
mvn test      # só unitários (rápido, sem Docker)
mvn verify    # unitários + integração + aceitação (precisa do Docker rodando)
```

| Nível | Onde | O que cobre |
|---|---|---|
| Unitário | `src/test/java/.../dominio/*Test.java` | Uma classe/nested por tarefa: compatibilidade ABO/Rh, validade, seleção de bolsas, idade, peso, intervalo, limite anual |
| Integração | `BolsaRepositoryIT` | Repositórios + migrations Flyway contra PostgreSQL real (Testcontainers) |
| Aceitação (BDD) | `src/test/resources/features/*.feature` | Um arquivo Gherkin por história (H1, H2, H3), via HTTP na aplicação inteira |

Story map e tarefas: [`docs/story-map.md`](docs/story-map.md). Histórico red → green: `git log --oneline` (cada `test:` vem antes do `feat:` correspondente).

## Pipeline e deploy

`.github/workflows/ci-cd.yml` — **build → testes → imagem → deploy**:

- **PR para main**: roda `testes` (`mvn verify`). Vermelho barra o merge.
- **merge na main**: `testes` → `imagem` (multi-stage, push no GHCR com tag = SHA) → `deploy` (Render puxa a imagem; o job só fica verde quando a versão nova responde na URL pública).
- **rollback**: Actions → CI/CD → *Run workflow* → informar o SHA de uma versão anterior.

### Configuração (uma vez)

1. Criar o repositório no GitHub e enviar o código (`git push -u origin main`).
2. Rodar a esteira uma vez (push na main) para publicar a imagem; em *Packages* no GitHub, tornar o pacote **público** (ou cadastrar credencial do GHCR no Render).
3. No Render: *New → Blueprint*, apontar para o repositório (usa o `render.yaml`; troque `OWNER/REPO` antes). Isso cria a API e o Postgres.
4. No Render, na API: *Settings → Deploy Hook* → copiar a URL.
5. No GitHub, *Settings → Secrets and variables → Actions*:
   - Secret `RENDER_DEPLOY_HOOK_URL` = URL do deploy hook
   - Variable `APP_URL` = URL pública da API (ex.: `https://hemocentro-api.onrender.com`)
6. Proteger a main: `./scripts/proteger-main.sh dono/repositorio` (ou em *Settings → Branches*: PR obrigatório + check `testes`).

URL no ar: _a preencher_

## Decisões

> _a preencher (tabela da seção 6 do enunciado)_

## Uso de IA

> _a preencher_
