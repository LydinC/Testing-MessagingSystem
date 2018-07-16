import java.util.ArrayList;

public class Mailbox {

    ArrayList<Message> messages = new ArrayList<Message>();
    ArrayList<Message> readMessages = new ArrayList<Message>();

    public Message consumeNextMessage(){
        //check that messages are available for consumption
        if(hasMessages()){
            //check timestamp
            if((getMessages().get(0).getTimestamp() + 30*60*1000) > System.currentTimeMillis()){
                Message message = getMessages().get(0);
                //send message to the read messages so it is not lost upon removal
                readMessages.add(message);
                //remove the message from inbox since it has been read
                messages.remove(0);
                return message;
            } else {
                messages.remove(0);
                //read next non expired message
                return consumeNextMessage();
            }
        }
        return null;
    }

    public boolean hasMessages(){
        return !getMessages().isEmpty();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<Message> getReadMessages() {
        return readMessages;
    }
}
