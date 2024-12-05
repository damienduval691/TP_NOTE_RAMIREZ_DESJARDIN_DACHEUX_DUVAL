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
        if((nomDriver != null) && (!nomDriver.isEmpty()) && (!nomDriver.equals("") ) ) {
            try {
                Class.forName(nomDriver);
            } catch (ClassNotFoundException e) {
                System.err.println("Erreur de chargement du driver JDBC");
                e.printStackTrace();
            }
        } else
            System.out.println("Attention, vous avez passé un nom de driver null");
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
     * Méthode pour vérifier l'état de la connexion.
     */

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ferme la connexion à la base de données.
     */
    public void disconnect() {
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
    public void creerTableau(String maTable, String structureTable) {
        if((maTable != null && !maTable.equals(""))&&(structureTable != null && !structureTable.equals(""))){
            String query = "CREATE TABLE IF NOT EXISTS " + maTable + " (" + structureTable + ")";
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la création de la table : " + maTable);
                e.printStackTrace();
            }
        } else
            System.out.println("Attention, l'un des paramètres est vide.");
    }

    /**
     * Consulter les donnees
     * @param parametres paramètres que l'on veut
     * @param nameTable nom de la table
     */
    public ResultSet consulterDonnees(String parametres, String nameTable){

        if((parametres != null && !parametres.equals(""))&&(nameTable != null && !nameTable.equals(""))){
            String requete = "SELECT "+parametres+ " FROM " + nameTable +";";
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
        } else {
            System.out.println("La requête passée est vide / null");
            return null;
        }
    }


    /**
     * Inserer de donnees dans un tableau
     * @param maTable nom du Tableau
     * @param structureTableau structure du Tableau
     * @param donneesTableau donnees du Tableau
     */
    public void insererDeDonnees(String maTable, String structureTableau, String donneesTableau) {
        if((maTable != null && !maTable.equals(""))
                && (structureTableau != null && !structureTableau.equals(""))
                && (donneesTableau != null && !donneesTableau.equals(""))) {
            String query = "INSERT INTO " + maTable + " (" + structureTableau + ") VALUES ("+ donneesTableau+ ")";
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la création de la table : " + maTable);
                e.printStackTrace();
            }
        } else
            System.out.println("Attention, l'un des paramètres rentré est null / vide");
    }

    public void effacerDonnees(String maTable){
        if(maTable != null && !maTable.equals("")){
            String query = "DELETE FROM " + maTable;
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la création de la table : " + maTable);
                e.printStackTrace();
            }
        } else
            System.out.println("Attention, la table en paramètre est vide.");

    }
}
