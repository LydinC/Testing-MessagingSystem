import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.util.Random;

public class Agent {

    String agentID = "";
    String loginKey = "";
    String sessionKey = "";
    String name = "";
    MessagingSystem ms;
    Mailbox mailbox = new Mailbox();
    int messageCount = 0;
    String supervisorID; // the supervisor of the agent
    long loginKeyAcquiredTime;
    long loginTime;

    public boolean retrieveLoginKeyFromSupervisor() {
        if(getSupervisor()!= null) {
            loginKey = getSupervisor().requestLoginKey(getAgentID());
            //set login time to current time
            loginKeyAcquiredTime = System.currentTimeMillis();
            return ms.registerLoginKey(getLoginKey(), getAgentID());
        } else{
            System.out.println("No supervisor available");
            return false;
        }
    }
    public boolean login(){
        if(getSupervisor()!= null && getMs().getAgentLoginKeys().get(getAgentID()) != null) {

            //check key uniqueness and length
            if(getMs().getAgentLoginKeys().get(getAgentID()).equals(getLoginKey())) {
                sessionKey = getMs().login(getAgentID(), getLoginKey());

                //took longer than 1 minute to login
                if(sessionKey == null) {
                    System.out.println("Login key expired.");
                    loginKey = "";
                    sessionKey = "";
                    return false;
                } else {
                    loginTime = System.currentTimeMillis();
                }
            } else {
                System.out.println("Could not register login key, please try again.");
                return false;
            }

            return true;
        }
        else{
            System.out.println("No supervisor available or login key incorrect");
            return false;
        }
    }

    public boolean sendMessage(String receiverID, String messageContent) {
        //check if the agent should be logged out
        if(logoutRequired()) {
            //agent logged out
            System.out.println("Cannot send message.");
            return false;
        }
        //check that agent has not sent more than 25 messages
        if(messageCount < 25) {
            String deliveryReport = ms.sendMessage(getSessionKey(), getAgentID(), receiverID, messageContent);
            if (deliveryReport.equals("OK")) {
                messageCount++;
                return true;
            } else {
                System.out.println(deliveryReport);
            }
        } else { //cant send message and force log out
            System.out.println("Cannot send more that 25 messages. Logging Out.");
            loginKey = "";
            sessionKey = "";
            //mailbox.messages.clear();
            mailbox.readMessages.clear();
            messageCount = 0;
        }
        return false;
    }

    public Supervisor getSupervisor(){
        for(Agent a : ms.getAgents()) {
            if (a.getAgentID().equals(getSupervisorID()) && (a instanceof SupervisorAgent)) {
                return (Supervisor) a;
            }
        }
        System.out.println("No supervisor found with id : " + supervisorID);
        return null;
    }

    public String readNextMessage() {
        if(logoutRequired()) {
            return "Cannot read next message.";
        }
        Message nextMessage = mailbox.consumeNextMessage();
        if(nextMessage == null) {
            return "No messages available.";
        }
        return nextMessage.getSourceAgentID() + ": " + nextMessage.getMessageContent();
    }

    public boolean logoutRequired() {
        //check if the agent should be logged out
        if((loginTime + 10*60*1000) < System.currentTimeMillis()) {
            //exceeded 10 minutes in the system, thus force log out
            System.out.println("10 minute session time exceeded. Logging Out.");
            loginKey = "";
            sessionKey = "";
            //mailbox.messages.clear();
            mailbox.readMessages.clear();
            messageCount = 0;

            return true;
        }
        return false;
    }

    public void logout() {
        System.out.println("Logging Out.");
        loginKey = "";
        sessionKey = "";
        //mailbox.messages.clear();
        mailbox.readMessages.clear();
        messageCount = 0;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public long getLoginKeyAcquiredTime() {
        return loginKeyAcquiredTime;
    }

    public Mailbox getMailbox() {
        return mailbox;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public MessagingSystem getMs() {
        return ms;
    }

    public String getAgentID() {
        return agentID;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public String getSupervisorID() {
        return supervisorID;
    }

}

