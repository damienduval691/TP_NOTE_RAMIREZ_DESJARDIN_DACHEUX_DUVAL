package fr.itii25.option3.taches;

import fr.itii25.option3.message.MessageDeCommande;
import fr.itii25.option3.message.MessageDeDonnees;
import fr.itii25.option3.designPaternDAO.ImplementerBaseDeDonnes;
import fr.itii25.option3.message.Message;

import java.sql.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Recepteur implements Runnable {

    private LinkedBlockingQueue<Message> canalDeCommunication;
    private boolean exit;

    public Recepteur(LinkedBlockingQueue<Message> canalDeCommunication) {
        this(canalDeCommunication,  true);
    }

    //Constructeur complet avec en passage le canal de communication
    // + vérification que le canal de communication existe bien en base
    private Recepteur(LinkedBlockingQueue<Message> canalDeCommunication, boolean exit) {
        if(canalDeCommunication != null){
            this.canalDeCommunication = canalDeCommunication;
            this.exit = exit;
        }
        else
            System.out.println("Le canal n'a pas été créé");
    }

    public void stop(){
        exit = false;
    }

    @Override
    public void run() {

        //Ajouts des drives des JDBC
        String nomDriverJDBCPostgre    = "org.postgresql.Driver";

        //Ajouts des liens vers les JDBC
        String urlJDBCPostgre = "jdbc:postgresql://localhost:5432/";

        ImplementerBaseDeDonnes dbPostgre = new ImplementerBaseDeDonnes(urlJDBCPostgre,"user", "admin") {};

        dbPostgre.chargerDriver(nomDriverJDBCPostgre);
        dbPostgre.connexion();

        dbPostgre.creerTableau("Actor","" +
                "actor_id INT, " +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "last_update TIMESTAMP," +
                "actor_datecre TIMESTAMP");

        String requeteSelect = "SELECT * FROM Actor";
        ResultSet rs = null;
        try {
            //On envoie tant que l'utilisateur n'a pas tapé les commandes indiquées
            while (exit) {
                Message message = canalDeCommunication.take(); // Récupération du message
                String requeteInsert = "";
                if (message instanceof MessageDeDonnees) {
                    ResultSet values = ((MessageDeDonnees<ResultSet>) message).getMessageDeDonnee();

                    System.out.println("Données reçues : " + values);
                    try {
                        int i = 0;
                        String parametres = "";

                        while (values.next()) {
                            i++;
                            int id = values.getInt(1);
                            String FName = values.getString(2);
                            String LName = values.getString(3);
                            Timestamp DateTime = values.getTimestamp(4);

                            parametres = "" + id + ",'" + FName + "','" + LName + "','" + DateTime + "',CURRENT_TIMESTAMP";
                            String structureTableau = "actor_id, first_name, last_name, last_update, actor_datecre";
                            dbPostgre.insererDeDonnees("ACTOR", structureTableau, parametres);


                        }
                    }catch (SQLException e){
                        System.out.println("Insert non réussi.");
                        e.printStackTrace();
                    }

                } else if (message instanceof MessageDeCommande) {
                    String command = ((MessageDeCommande) message).getMessageDeCommande();
                    System.out.println("Commande reçue : " + command);
                    if ("FIN".equals(command)) {
                        System.out.println("Tâche Réceptrice arrêtée.");
                        stop();
                    } else if ("2".equals(command)) {
                        int i = 0;

                        rs = null;
                        rs = dbPostgre.consulterDonnees(requeteSelect);
                        try{
                            System.out.println("Voici le contenue de la table ACTOR : ");
                            while(rs.next()){
                                String parametres = "";
                                i++;
                                int id              = rs.getInt(1);
                                String FName        = rs.getString(2);
                                String LName        = rs.getString(3);
                                Timestamp DateTime  = rs.getTimestamp(4);

                                System.out.println(id+" ,"+FName+" ,"+LName+" ,"+DateTime);
                            }
                            System.out.println("Fin du contenue de la table ACTOR.");
                        } catch (SQLException e){
                            System.out.println("Une erreur est survenue lors de l'affichage des données !");
                            e.printStackTrace();
                        }
                    } else if ("3".equals(command)) {
                        dbPostgre.effacerDonnees("ACTOR");
                        System.out.println("Le contenu de la table ACTOR à été effacer !");
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Tâche Réceptrice interrompue.");
        }

        dbPostgre.deconectee();

    }
}
