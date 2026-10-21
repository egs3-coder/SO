# Evidências

## E1 · Container automatizado

| Evidência | Arquivo |
|---|---|
| `docker compose ps` com `api` e `db` **healthy** e a API respondendo (`curl localhost:8080/estoque`) | [e1-compose-healthy.png](e1-compose-healthy.png) |
| Dados persistem no volume após `docker compose down` + `up` (estoque continua com `"O-":1`) | [e1-persistencia-volume.png](e1-persistencia-volume.png) |

## E2 · TDD

| Evidência | Arquivo |
|---|---|
| Histórico red → green: cada `test:` antes do `feat:` correspondente | [e2-historico-red-green.txt](e2-historico-red-green.txt) |
| Teste vermelho na esteira barrando o merge (validade alterada de 35 para 30 dias) | [e3-pr-vermelho-bloqueado.png](e3-pr-vermelho-bloqueado.png) |

## E3 · Deploy automatizado

| Evidência | Arquivo |
|---|---|
| Execução **vermelha** que barrou o merge e o deploy (PR #1: `testes` ❌, `imagem` e `deploy` pulados) | [e3-pr-vermelho-bloqueado.png](e3-pr-vermelho-bloqueado.png) |
| Execução **verde** que publicou (`testes` → `imagem` → `deploy`) | [e3-deploy-verde.png](e3-deploy-verde.png) |
| Histórico das execuções (verde, vermelha, PR verde, merge) | [e3-historico-execucoes.png](e3-historico-execucoes.png) |
| URL pública no ar com a versão do merge (`4566e43`) | [e3-url-no-ar.png](e3-url-no-ar.png) |
| URL com a versão anterior (`5e879ec`), primeiro deploy | [e3-url-versao-anterior.png](e3-url-versao-anterior.png) |
| **Rollback** para `5e879ec` (workflow manual) | [e3-rollback.png](e3-rollback.png) |
| Volta para a versão atual `4566e43` | [e3-rollback-retorno.png](e3-rollback-retorno.png) |
| `main` protegida | [e3-main-protegida.png](e3-main-protegida.png) |
| Ambiente criado no Render pelo Blueprint (API + PostgreSQL) | [e3-render-blueprint.png](e3-render-blueprint.png) |
| Ambiente `producao` registrado em Deployments do GitHub | [e3-deployments-producao.png](e3-deployments-producao.png) |
