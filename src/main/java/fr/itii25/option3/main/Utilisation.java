package fr.itii25.option3.main;

import fr.itii25.option3.message.Message;
import fr.itii25.option3.taches.Emetteur;
import fr.itii25.option3.taches.Recepteur;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Utilisation {
    public static void main(String[] args) {

        //Déclaration du canal de communication
        LinkedBlockingQueue<Message> canalCommunication = new LinkedBlockingQueue<Message>();

        Lock locker = new ReentrantLock();

        //Déclaration des Runnable Emtteur & Recepteur, avec en paramètre, le canal de communication
        Emetteur tacheEmettrice     = new Emetteur(canalCommunication, locker);
        Recepteur tacheReceptrice   = new Recepteur(canalCommunication, locker);

        //Décalaration des Thread avec en paramètre les tâches emetteur & recepteur
        Thread emetteurThread = new Thread(tacheEmettrice);
        Thread recepteurThread = new Thread(tacheReceptrice);

        //Démarrage des deux Threads
        emetteurThread.start();
        recepteurThread.start();
    }
}
