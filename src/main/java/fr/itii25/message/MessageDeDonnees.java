package fr.itii25.message;

public class MessageDeDonnees extends Message{

    private String messageDeDonnee;

    public MessageDeDonnees(String messageDeDonnee) {
        if (messageDeDonnee == null || messageDeDonnee.isBlank())
            throw new IllegalArgumentException("Ce message ne peut pas être vide !");
        else
            this.messageDeDonnee = messageDeDonnee;
    }

    public String getMessageDeDonnee(){
        return messageDeDonnee;
    }



}
