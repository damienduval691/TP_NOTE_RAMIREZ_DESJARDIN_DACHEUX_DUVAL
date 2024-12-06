package fr.itii25.option3.designPaternDAO;
import java.sql.*;


public abstract class ImplementerBaseDeDonnees implements Interface_BaseDeDonnees {
    /**
     * Informations pour la connexion à la base de donnees
     */
    private String urlJDBC;
    private String utilisateur;
    private String password;
    private Connection connection;

    /**
     * Constructeur d'ImplementerBaseDeDonnees
     * @param urlJDBC Nom du driver JDBC
     * @param utilisateur Nom de l'utilisateur de la base de donnees
     * @param password Mot de passe de la base de donnees
     *
     * Exemple d'utilisation ("jdbc:mysql://localhost:3306/user","user","password")
    */

    public ImplementerBaseDeDonnees(String urlJDBC, String utilisateur, String password) {
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
            // On vérifie si on est déjà connecté
            if (!isConnected()) {
                connection = DriverManager.getConnection(urlJDBC, utilisateur, password);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Vérifie si la connexion à la base de données est fait
     * @return true si connecté, false sinon.
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
     * Création d'une table
     * @param maTable Nom de la table à créer
     * @param structureTable Structure de la table à créer
     * Exemple d'application :
     * dbConnection.createTable(maTable,structureTable);
     * dbConnection.createTable("Personnes","id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER");
     * Rêquete type :
     *                       CREATE TABLE IF NOT EXISTS maTable (structureTable)
     *                       CREATE TABLE IF NOT EXISTS Personnes (id SERIAL PRIMARY KEY, nom VARCHAR(50), age INTEGER)
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
            System.out.println("L'un des paramètres est vide / null");
            return null;
        }
    }


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
