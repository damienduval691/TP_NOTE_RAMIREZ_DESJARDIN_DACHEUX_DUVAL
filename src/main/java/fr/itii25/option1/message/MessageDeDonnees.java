package fr.itii25.option1.message;

public class MessageDeDonnees extends Message{

    private String messageDeDonnee;

    //Constructeur en paramètre le messageDeDonnee
    public MessageDeDonnees(String messageDeDonnee) {
        //On vérifie si le paramètre passé existe bien en mémoire, sinon
        if (messageDeDonnee == null)
            throw new IllegalArgumentException("messageDeDonnee is null");
        else if(messageDeDonnee.isBlank())
            System.out.println("Le message est vide !");
        else this.messageDeDonnee = messageDeDonnee;

    }

    public String getMessageDeDonnee(){
        return messageDeDonnee;
    }



}
