package edu.sjsu.snappychat.fragment.chats;


/**
 * Created by Akshatha Anantharmu on 12/6/16.
 */

public class ChatModel {
    private String message;
    private String imagemessage;
    private String Sender;
    private String receiver;

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImagemessage() {
        return imagemessage;
    }

    public void setImagemessage(String imagemessage) {
        this.imagemessage = imagemessage;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public ChatModel(){

    }
    public ChatModel(String s, String m,String rcv,String img){
        this.Sender =s ;
        this.message =m;
        this.imagemessage = img;
        this.receiver = rcv;
    }

}
