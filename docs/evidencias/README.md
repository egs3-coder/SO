# Evidências

Salvar aqui os prints/links pedidos no enunciado:

- [ ] `docker compose ps` com `api` e `db` em **healthy**
- [ ] API respondendo (`curl localhost:8080/actuator/health` e `curl localhost:8080/estoque`)
- [ ] `git log --oneline` mostrando os pares `test:` → `feat:`
- [ ] Execução **vermelha** que barrou o merge/deploy (dica: abrir um PR que quebra uma regra, ex.: trocar 35 por 30 dias em `ValidadeBolsa`)
- [ ] Execução **verde** que publicou (jobs `testes` → `imagem` → `deploy`)
- [ ] URL pública respondendo (raiz `/` mostra a versão = SHA do commit)
- [ ] Rollback: execução manual do workflow com um SHA anterior e a raiz mostrando a versão antiga
- [ ] Tela da proteção da main (PR obrigatório + check `testes`)
