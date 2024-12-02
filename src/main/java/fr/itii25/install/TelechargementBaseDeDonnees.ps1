# update-databases.ps1

Write-Host "Mise à jour des images Docker pour PostgreSQL et SakilaDB/MySQL..."

# Commande pour récupérer l'image Docker de PostgreSQL
Write-Host "Téléchargement de l'image PostgreSQL..." -ForegroundColor Green
docker pull postgres

# Commande pour récupérer l'image Docker de SakilaDB/MySQL
Write-Host "Téléchargement de l'image SakilaDB/MySQL..." -ForegroundColor Green
docker pull sakiladb/mysql

Write-Host "Mise à jour terminée." -ForegroundColor Cyan
