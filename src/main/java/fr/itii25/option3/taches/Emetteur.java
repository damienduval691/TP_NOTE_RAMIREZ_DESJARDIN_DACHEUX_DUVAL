package fr.itii25.option3.taches;

import fr.itii25.option3.message.MessageDeCommande;
import fr.itii25.option3.message.MessageDeDonnees;
import fr.itii25.option3.designPaternDAO.ImplementerBaseDeDonnees;
import fr.itii25.option3.message.Message;


import java.sql.ResultSet;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;

public class Emetteur implements Runnable {

    private LinkedBlockingQueue<Message> canalDonnees;
    private Lock verrou;
    private boolean exit;

    public Emetteur(LinkedBlockingQueue<Message> canalDeCommunication, Lock verrou) {
        this(canalDeCommunication, true, verrou );
    }

    //Constructeur complet avec en passage le canal de communication
    // + vérification que le canal de communication existe bien en base
    private Emetteur(LinkedBlockingQueue<Message> canalDeCommunication, boolean exit, Lock verrou) {
        if((canalDeCommunication != null) && (verrou != null)) {
            this.canalDonnees = canalDeCommunication;
            this.exit = exit;
            this.verrou = verrou;
        }
        else
            System.out.println("Le canal ou le verrou n'a pas été créé");
    }

    private void sendMsg(Message message) {
        try {
            canalDonnees.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop(){
        exit = false;
    }

    @Override
    public void run() {

        //Ajouts des drives des JDBC
        String nomDriverJDBCMYSQL      = "com.mysql.cj.jdbc.Driver";
        //Ajouts des liens vers les JDBC
        String urlJDBCMYSQL = "jdbc:mysql://localhost:3306/sakila";
        // Connexion à la base de donnees
        ImplementerBaseDeDonnees dbMySQL = new ImplementerBaseDeDonnees(urlJDBCMYSQL,"sakila","p_ssW0rd") {};

        dbMySQL.chargerDriver(nomDriverJDBCMYSQL);
        dbMySQL.connexion();

        ResultSet rs = null;

        //Récupération des interactions utilisateurs
        try (Scanner scanner = new Scanner(System.in)) {
            //On donne des options de commande à l'utilisateur :
            while (exit) {
                //On vérifie que le verrou est vérouillable, si oui il se lock
                if(verrou.tryLock()) {
                    System.out.println("");
                    System.out.println("#######################################################");
                    System.out.println("1 - Lancez l'acquisition des données");
                    System.out.println("2 - Afficher les données de la table de réception");
                    System.out.println("3 - Supprimez les données de la table de réception");
                    System.out.println("FIN - Terminer le programme");
                    System.out.print("Choisissez une option : ");


                    String input = scanner.nextLine();
                    //On supprime tous les espaces de la chaine pour éviter les erreurs
                    input=input.replaceAll("\\s+", "");
                    verrou.lock();
                    if ("FIN".equalsIgnoreCase(input)) {
                        MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                        canalDonnees.put(messageToSend); // Envoi d'un message de commande
                        stop();
                    } else if ("1".equalsIgnoreCase(input)) {
                        //Récupération des données de la tables actor et envoie dans le canal de communication
                        System.out.println("");
                        System.out.println("Acquisition des données : ");
                        System.out.println("Sélectionnez la table voulue : ");
                        System.out.println("1 = actor, 2 = category, 3 = city ");
                        System.out.print("Votre choix : ");
                        String choix = scanner.nextLine();
                        //Selon le choix, on change la table de référence (actor, category, city)
                        switch (choix) {
                            case "1":
                                sendMsg(new MessageDeDonnees<ResultSet>(dbMySQL.consulterDonnees("*", "actor")));
                                break;

                            case "2":
                                sendMsg(new MessageDeDonnees<ResultSet>(dbMySQL.consulterDonnees("*", "category")));
                                break;

                            case "3":
                                sendMsg(new MessageDeDonnees<ResultSet>(dbMySQL.consulterDonnees("*", "city")));
                                break;
                            default:
                                System.out.println("Vous avez saisi un mauvais identifiant. Veuillez recommencer.");
                                break;
                        }
                        System.out.println("Acquisition des données effectuée");
                    } else if ("2".equalsIgnoreCase(input)) { //On vérifie si c'est 2 (visualiser les données)
                        System.out.println("");
                        System.out.println("Visualisation des données : ");
                        System.out.println("Sélectionnez la table voulue : ");
                        System.out.println("1 = actor, 2 = category, 3 = city ");
                        System.out.print("Votre choix : ");

                        String choix = scanner.nextLine();
                        String choixTable = "";
                        //Selon le choix, on passe comme commande 2.actor, 2.city... le 2. permet, au récepteur, de savoir quel commande éxecuter (ici visualiser)
                        switch (choix) {
                            case "1":
                                sendMsg(new MessageDeCommande("2.actor"));
                                break;

                            case "2":
                                sendMsg(new MessageDeCommande("2.category"));
                                break;

                            case "3":
                                sendMsg(new MessageDeCommande("2.city"));
                                break;
                            default:
                                System.out.println("Vous avez saisi un mauvais identifiant. Veuillez recommencer.");
                                break;
                        }
                        verrou.unlock();
                    } else if ("3".equalsIgnoreCase(input)) { //On vérifie si c'est 3 (supprimer les données)
                        System.out.println("");
                        System.out.println("Suppression des données : ");
                        System.out.println("Sélectionnez la table voulue : ");
                        System.out.println("1 = actor, 2 = category, 3 = city ");
                        System.out.print("Votre choix : ");

                        String choix = scanner.nextLine();
                        //Selon le choix, on passe comme commande 2.actor, 2.city... le 2. permet, au récepteur, de savoir quel commande éxecuter (ici visualiser)
                        switch (choix) {
                            case "1":
                                sendMsg(new MessageDeCommande("3.actor"));
                                break;

                            case "2":
                                sendMsg(new MessageDeCommande("3.category"));
                                break;

                            case "3":
                                sendMsg(new MessageDeCommande("3.city"));
                                break;
                            default:
                                System.out.println("Vous avez saisi un mauvais identifiant. Veuillez recommencer.");
                                break;
                        }
                    } else
                        System.out.println("Identifiant inconnu.");
                    verrou.unlock();
                    Thread.sleep(200);
                }

            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }

        dbMySQL.disconnect();

    }
}





