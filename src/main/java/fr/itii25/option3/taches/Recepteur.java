package fr.itii25.option3.taches;

import fr.itii25.option3.designPaternDAO.ImplementerBaseDeDonnes;
import fr.itii25.option3.message.Message;
import fr.itii25.option3.message.MessageDeCommande;
import fr.itii25.option3.message.MessageDeDonnees;

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

        ImplementerBaseDeDonnes dbMySQL = new ImplementerBaseDeDonnes(urlJDBCPostgre,"user", "admin") {};

        dbMySQL.creerTableau("Actor","" +
                "actor_id INT, " +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "last_update DateTime");

        dbMySQL.chargerDriver("com.mysql.cj.jdbc.Driver");
        dbMySQL.connexion();
        ResultSet rs = dbMySQL.consulterDonnees("SELECT * FROM actor");

        try {
            //On envoie tant que l'utilisateur n'a pas tapé FIN
            while (exit) {
                Message message = canalDeCommunication.take(); // Récupération du message
                if (message instanceof MessageDeDonnees) {
                    System.out.println("Données reçues : " + ((MessageDeDonnees) message).getMessageDeDonnee());
                } else if (message instanceof MessageDeCommande) {
                    String command = ((MessageDeCommande) message).getMessageDeCommande();
                    System.out.println("Commande reçue : " + command);
                    if ("FIN".equals(command)) {
                        System.out.println("Tâche Réceptrice arrêtée.");
                        int i = 0;
                        try{
                            while(rs.next()){
                                i++;
                                int id = rs.getInt(0);

                                System.out.println(id);
                            }
                            System.out.println("Voici la table actor !");
                        } catch (SQLException e){

                        }
                        stop();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Tâche Réceptrice interrompue.");
        }

    }
}
