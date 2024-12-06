package fr.itii25.option3.designPaternDAO;
import java.sql.*;

public interface Interface_BaseDeDonnees {

    /**
     * Charge le driver JDBC.
     * @param nomDriver Nom du driver JDBC
     */
    public void chargerDriver(String nomDriver) throws ClassNotFoundException;

    /**
     * Établit une connexion à la base de données.
     * @return Connection Objet Connection connecté à la base de données
     */
    public Connection connexion() throws SQLException;

    /**
     * Méthode pour vérifier l'état de la connexion.
     */
    public boolean isConnected() throws SQLException;


    /**
     * Création d'une table
     * @param nomTableau Nom de la table à créer
     * @param structureTableau Structure de la table à créer
     * Exemple d'application :
     * dbConnection.createTable(maTable,structureTable);
     * dbConnection.createTable("Personnes","id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER");
     * Rêquete type :
     *                       CREATE TABLE IF NOT EXISTS maTable (structureTable)
     *                       CREATE TABLE IF NOT EXISTS Personnes (id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER)
     */
    public void creerTableau(String nomTableau, String structureTableau) throws SQLException;

    /**
     * Consulte les donnees d'une table
     * @param parametres paramètres que l'on veut
     * @param nameTable nom de la table
     *
     * Exemple d'application :
     * bConnection.createTable(parametres,String nameTable);
     * bConnection.createTable("*","actor");
     * Rêquete type :
     *                  SELECT parametres FROM nameTable
     *                  SELECT * FROM actor ;
     */
    public ResultSet consulterDonnees(String parametres, String nameTable) throws SQLException;
    /**
     * Inserer de donnees dans un tableau
     * @param maTable nom du Tableau
     * @param structureTableau structure du Tableau
     * @param donneesTableau donnees du Tableau
     * Exemple d'application :
     *                       dbConnection.insererDeDonnees(maTable,structureTable,donneesTableay);
     *                       dbConnection.insererDeDonnees("category","12, type1");
     * Rêquete type :
     *                       INSERT INTO maTable (structureTable) VALUES (donneesTableau)
     *                       INSERT INTO categoru (category_id, name) VALUES (12, type1)
     */
    public void insererDeDonnees(String maTable, String structureTableau, String donneesTableau) throws SQLException;
    /**
     * Effacer un tableau
     * @param maTable nom du Tableau
     * Exemple d'application :
     *                       dbConnection.effacerDonnees(maTable);
     *                       dbConnection.insererDeDonnees("category");
     * Rêquete type :
     *                       DELETE FROM maTable
     *                       DELETE FROM categoru
     */
    public void effacerDonnees(String maTable) throws SQLException;
    /**
     * Ferme la connexion à la base de données.
     */
    public void disconnect() throws SQLException;





}
