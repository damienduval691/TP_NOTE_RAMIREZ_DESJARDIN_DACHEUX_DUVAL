Write-Host "Lancement des conteneurs Docker PostgreSQL et SakilaDB/MySQL..."

# Lancer le conteneur PostgreSQL en mode détaché
Write-Host "Demarrage de PostgreSQL..."
docker run --rm -d -p 5432:5432 -e POSTGRES_USER="user" -e POSTGRES_PASSWORD="admin" postgres:latest

Write-Host "PostgreSQL est demarre en mode detache."

# Lancer le conteneur SakilaDB/MySQL
Write-Host "Demarrage de SakilaDB/MySQL..."
docker run --rm -d -p 3306:3306 sakiladb/mysql:latest

Write-Host "SakilaDB/MySQL est lance en mode detache."

Write-Host "Tous les conteneurs Docker sont demarres en mode detache."

