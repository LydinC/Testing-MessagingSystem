import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MessagingSystem {

    ArrayList<Agent> agents = new ArrayList<Agent>(); //agents in the system
    ArrayList<String> blockedWords = new ArrayList<String>(); //list of blocked words
    HashMap<String, String> agentLoginKeys = new HashMap<String, String>();
    HashMap<String, String> agentSessionKeys = new HashMap<String, String>();

    public String login(String agentID, String loginKey){
        //check key is less than a minute old
        if((getAgents().get(getIndexWithAgentId(agentID)).getLoginKeyAcquiredTime() + 60*1000) > System.currentTimeMillis()) {
            //check that key and agent ID match
            if(getAgentLoginKeys().get(agentID).equals(loginKey)) {
                String sessionKey = getSessionKey();
                getAgentSessionKeys().put(agentID, sessionKey);
                return sessionKey;
            }
        }
        return null;
    }

    public boolean registerLoginKey(String loginKey, String agentID){
        //check that login key is 10 characters long
        if(loginKey.length() != 10) {
            System.out.println("The login key is not 10 characters long");
            return false;
        }

        //check that login key is unique
        boolean isKeyUnique = true;
        for(Agent a : agents) {
            if(!a.getAgentID().equals(agentID) && a.getLoginKey().equals(loginKey)){
                isKeyUnique = false;
            }
        }

        if(!isKeyUnique){
            System.out.println("Login key is not unique.");
            return false;
        }

        getAgentLoginKeys().put(agentID, loginKey);
        return true;
    }

    public String sendMessage(String sessionKey, String sourceAgentID, String targetAgentID, String message){
        //checks session key, which confirms that the agent is logged in
        if(getAgentSessionKeys().get(sourceAgentID).equals(sessionKey)) {
            //check message length
            if (message.length() <= 140) {
                //check message for blocked words
                for (String blockedWord : getBlockedWords()) {
                    if (message.toLowerCase().contains(blockedWord)) {
                        message = message.toLowerCase().replaceAll(blockedWord,"");
                        //return ("Blocked word is in message : " + blockedWord + ". Message not sent.");
                    }
                }
                //store message object in mailbox of target agent
                Message newMessage = new Message(sourceAgentID, targetAgentID, System.currentTimeMillis(), message);
                if (getAgents().get(getIndexWithAgentId(targetAgentID)).getMailbox().getMessages().size() < 25) {
                    getAgents().get(getIndexWithAgentId(targetAgentID)).getMailbox().getMessages().add(newMessage);
                } else return "Target agent has reached mailbox capacity. Message not sent.";
            } else {
                return "Message exceeds maximum 140 characters. Message not sent.";
            }

        } else {
            return "Source agent session key mismatch. Source agent could be logged out. Message not sent.";
        }
        //need to check : login key not expired (10mins) and content <140 chars and no blocked words)
        return "OK";
    }

    public int getIndexWithAgentId(String agentID) {
        for(int i=0; i<getAgents().size(); i++) {
            if(getAgents().get(i).getAgentID().equals(agentID)) {
                return i;
            }
        }
        return -1; //out of bounds exception if no agent is found
    }

    public void setBlockedWords(){
        blockedWords.add("recipe");
        blockedWords.add("ginger");
        blockedWords.add("nuclear");
    }

    public String getSessionKey() {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!%^&*()$£";
        int max = alphabet.length();
        Random r = new Random();
        String keyGenerator  = "";
        for (int i = 0; i < 50; i++) {
            keyGenerator = keyGenerator + alphabet.charAt(r.nextInt(max));
        }
        return keyGenerator;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<String> getBlockedWords() {
        return blockedWords;
    }

    public HashMap<String, String> getAgentLoginKeys() {
        return agentLoginKeys;
    }

    public HashMap<String, String> getAgentSessionKeys() {
        return agentSessionKeys;
    }
}
