#!/usr/bin/env bash
# Protege a branch main (GitHub Flow): só entra por pull request aprovado e com o check "testes" verde.
# Uso: ./scripts/proteger-main.sh dono/repositorio      (requer GitHub CLI autenticado: gh auth login)
# Obs.: em repositório privado, proteção de branch exige plano pago; em repositório público é gratuito.
set -euo pipefail

REPO="${1:?informe dono/repositorio}"

gh api --method PUT "repos/${REPO}/branches/main/protection" --input - <<'JSON'
{
  "required_status_checks": { "strict": true, "contexts": ["testes"] },
  "enforce_admins": true,
  "required_pull_request_reviews": { "required_approving_review_count": 1 },
  "restrictions": null,
  "allow_force_pushes": false,
  "allow_deletions": false
}
JSON

echo "main protegida em ${REPO}: PR obrigatório, 1 aprovação, check 'testes' obrigatório, sem force push."
