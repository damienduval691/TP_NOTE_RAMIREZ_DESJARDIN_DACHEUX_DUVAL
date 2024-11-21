package fr.itii25.taches;

import fr.itii25.message.Message;
import fr.itii25.message.MessageDeCommande;
import fr.itii25.message.MessageDeDonnees;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Emetteur implements Runnable {

    private LinkedBlockingQueue<Message> canalDeCommunication;
    private boolean exit;
    private String stringToSend;

    public Emetteur(LinkedBlockingQueue<Message> canalDeCommunication) {
        this(canalDeCommunication, "", false);
    }

    private Emetteur(LinkedBlockingQueue<Message> canalDeCommunication, String stringToSend, boolean exit) {
        if(canalDeCommunication != null){
            this.canalDeCommunication = canalDeCommunication;
            this.stringToSend = stringToSend;
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
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Entrez vos messages (tapez 'FIN' pour terminer) :");
            while (exit) {
                String input = scanner.nextLine();
                if ("FIN".equalsIgnoreCase(input)) {
                    MessageDeCommande messageToSend = new MessageDeCommande("FIN");
                    if(messageToSend != null)
                        canalDeCommunication.put(messageToSend); // Envoi d'un message de commande
                    break;
                } else {
                    MessageDeDonnees messageToSend = new MessageDeDonnees(input);
                    if(messageToSend != null)
                        canalDeCommunication.put(messageToSend); // Envoi d'un message de données
                }
            }
        } catch (InterruptedException E){
            Thread.currentThread().interrupt();
            System.out.println("Interruption du Thread en cours");
        }
    }
}
