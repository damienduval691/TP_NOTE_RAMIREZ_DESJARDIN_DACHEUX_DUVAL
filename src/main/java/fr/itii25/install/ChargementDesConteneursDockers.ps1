# LancerDockerBases.ps1
Write-Host "Lancement des conteneurs Docker PostgreSQL et SakilaDB/MySQL..."

# Lancer le conteneur PostgreSQL
Write-Host "Demarrage de PostgreSQL..."
docker run --rm -p 5432:5432 -e POSTGRES_USER="user" -e POSTGRES_PASSWORD="admin" postgres:latest

Write-Host "PostgreSQL arrete ou termine."

# Lancer le conteneur SakilaDB/MySQL
Write-Host "Demarrage de SakilaDB/MySQL..."
docker run --rm -p 3306:3306 -d sakiladb/mysql:latest

Write-Host "SakilaDB/MySQL est lance en mode detache."

Write-Host "Tous les conteneurs Docker sont demarres ou arretes selon leur mode d'execution." -ForegroundColor Cyan
