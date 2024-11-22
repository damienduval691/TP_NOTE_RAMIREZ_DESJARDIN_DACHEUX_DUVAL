package fr.itii25.option1.taches;

import fr.itii25.option1.message.Message;
import fr.itii25.option1.message.MessageDeCommande;
import fr.itii25.option1.message.MessageDeDonnees;

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

        exit = true;

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
