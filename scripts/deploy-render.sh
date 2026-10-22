#!/usr/bin/env bash
# Dispara o deploy de uma imagem no Render e espera a versão nova responder.
# Uso: deploy-render.sh <imagem:tag> <versao-esperada>
# Requer: RENDER_DEPLOY_HOOK_URL (secret) e APP_URL (variável do repositório).
set -euo pipefail

IMAGEM="${1:?informe a imagem com tag}"
VERSAO="${2:?informe a versão esperada}"
: "${RENDER_DEPLOY_HOOK_URL:?secret RENDER_DEPLOY_HOOK_URL não configurado}"
: "${APP_URL:?variável APP_URL não configurada}"

IMAGEM_CODIFICADA=$(python3 -c 'import sys, urllib.parse; print(urllib.parse.quote(sys.argv[1], safe=""))' "$IMAGEM")

echo "Disparando deploy de $IMAGEM"
curl -fsS -X POST "${RENDER_DEPLOY_HOOK_URL}&imgURL=${IMAGEM_CODIFICADA}" -o /dev/null

# Smoke test: a API devolve a versão (SHA) embutida na imagem.
# Versões novas respondem em /api/info; versões antigas (antes do painel web) respondiam em /.
# Conferimos os dois, para o rollback para uma versão antiga continuar funcionando.
# O plano gratuito "dorme" sem uso, então damos até 10 minutos.
for tentativa in $(seq 1 40); do
  if curl -fsS --max-time 20 "${APP_URL%/}/api/info" 2>/dev/null | grep -q "\"versao\":\"${VERSAO}\"" \
     || curl -fsS --max-time 20 "${APP_URL%/}/" 2>/dev/null | grep -q "\"versao\":\"${VERSAO}\""; then
    echo "Versão ${VERSAO} no ar em ${APP_URL}"
    curl -fsS "${APP_URL%/}/actuator/health"
    echo
    exit 0
  fi
  echo "Aguardando a versão ${VERSAO} (tentativa ${tentativa}/40)..."
  sleep 15
done

echo "ERRO: a versão ${VERSAO} não respondeu em ${APP_URL} dentro do tempo limite" >&2
exit 1
