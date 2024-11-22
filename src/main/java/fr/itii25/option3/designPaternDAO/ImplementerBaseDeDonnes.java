package fr.itii25.option3.designPaternDAO;
import java.sql.*;

public abstract class ImplementerBaseDeDonnes implements Interface_BaseDeDonnees {
    /**
     * Informations de connexion.
     */
    private String urlJDBC; // URL de la base de données
    private String utilisateur; // Nom d'utilisateur
    private String password; // Mot de passe
    private Connection connection; // Objet Connection

    public ImplementerBaseDeDonnes(String urlJDBC, String utilisateur, String password) {
        this.urlJDBC = urlJDBC;
        this.utilisateur = utilisateur;
        this.password = password;
    }

    /**
     * Charge le driver JDBC.
     * @param nomDriver Nom du driver JDBC
     */
    public void chargerDriver(String nomDriver) {
        try {
            Class.forName(nomDriver);
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver JDBC");
            e.printStackTrace();
        }
    }

    /**
     * Établit une connexion à la base de données.
     * @return Connection Objet Connection connecté à la base de données
     */
    public Connection connexion() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(urlJDBC, utilisateur, password);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données.
     */
    public void deconectee() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion");
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour créer une table.
     * Création d'une table
     * Exemple d'application :
     * String structureTableau = "id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER";
     * dbConnection.createTable("Personnes", structureTableau);
     */
    public void creerTableau(String nomTableau, String structureTableau) {
        String query = "CREATE TABLE IF NOT EXISTS " + nomTableau + " (" + structureTableau + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la table : " + nomTableau);
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour vérifier l'état de la connexion.
     */

    public boolean estConnectee() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Consulter les donnees
     * @param requete Requete de consultation
     */
    public ResultSet consulterDonnees(String requete){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connexion().createStatement();
            rs = stmt.executeQuery(requete);
            return rs;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la consultation des données.");
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Creer une base de donnees
     * @param nomBaseDeDonnees nom de la base de donnees
     */
    public void creerBaseDeDonnees(String nomBaseDeDonnees) {

    }
}
