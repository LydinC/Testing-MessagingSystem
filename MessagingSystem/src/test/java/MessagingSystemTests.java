import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class MessagingSystemTests {

    MessagingSystem messagingSystem;
    @Before
    public void setup() {
        messagingSystem = new MessagingSystem();
    }

    @After
    public void teardown() {
        messagingSystem = null;
    }

    @Test
    public void testSetBlockedWords() {
        setup();
        messagingSystem.setBlockedWords();
        assertEquals(3, messagingSystem.blockedWords.size());
        teardown();
    }

    @Test
    public void testValidGetIndexWithAgentID() {
        setup();
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        messagingSystem.agents.add(agent);
        assertEquals(0,messagingSystem.getIndexWithAgentId("a0"));
        teardown();
    }

    @Test
    public void testInvalidGetIndexWithAgentID() {
        setup();
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        messagingSystem.agents.add(agent);
        assertEquals(-1,messagingSystem.getIndexWithAgentId("a1"));
        teardown();
    }

    @Test
    public void testFiftyCharacterSessionKey() {
        setup();
        assertEquals(50,messagingSystem.getSessionKey().length());
        teardown();
    }

    @Test
    public void testRegistrationWithInvalidLengthLoginKey() {
        setup();
        assertEquals(false,messagingSystem.registerLoginKey("abcd","a0"));
        teardown();
    }

    @Test
    public void testRegistrationWithNonUniqueLoginKey() {
        setup();
        //add an agent with the same login key to be added
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        when(agent.getLoginKey()).thenReturn("1234567890");
        messagingSystem.agents.add(agent);

        assertEquals(false,messagingSystem.registerLoginKey("1234567890","a1"));
        teardown();
    }

    @Test
    public void testValidRegistration() {
        setup();
        //add an agent with the different login key to be added
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        when(agent.getLoginKey()).thenReturn("1234567890");
        messagingSystem.agents.add(agent);

        assertEquals(true,messagingSystem.registerLoginKey("0123456789","a1"));
        teardown();
    }

    @Test
    public void testLoginWithExpiredKey() {
        setup();
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        when(agent.getLoginKeyAcquiredTime()).thenReturn(System.currentTimeMillis() - 61*1000);
        messagingSystem.agents.add(agent);
        assertEquals(null, messagingSystem.login("a0","0123456789"));
    }

    @Test
    public void testLoginWithNonMatchingKey() {
        setup();
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        when(agent.getLoginKeyAcquiredTime()).thenReturn(System.currentTimeMillis());
        messagingSystem.agents.add(agent);
        messagingSystem.agentLoginKeys.put("a0","");
        assertEquals(null, messagingSystem.login("a0","0123456789"));
    }

    @Test
    public void testValidLogin() {
        setup();
        Agent agent = Mockito.mock(Agent.class);
        when(agent.getAgentID()).thenReturn("a0");
        when(agent.getLoginKeyAcquiredTime()).thenReturn(System.currentTimeMillis());
        messagingSystem.agents.add(agent);
        messagingSystem.agentLoginKeys.put("a0","0123456789");
        assertEquals(50, messagingSystem.login("a0","0123456789").length());
    }

    @Test
    public void testSendMessageWithInvalidSessionKey() {
        setup();
        messagingSystem.agentSessionKeys.put("a0","0123456789");
        assertEquals("Source agent session key mismatch. Source agent could be logged out. Message not sent.",
                messagingSystem.sendMessage("","a0","a1","hello"));
        teardown();
    }

    @Test
    public void testSendMessageWithInvalidLength() {
        setup();
        messagingSystem.agentSessionKeys.put("a0","0123456789");
        String message = "";
        for(int i=0; i<142; i++) {
            message = message + "a";
        }
        assertEquals("Message exceeds maximum 140 characters. Message not sent.",
                messagingSystem.sendMessage("0123456789","a0","a1",message));
        teardown();
    }

    @Test
    public void testSendMessageWithBlockedWord() {
        setup();
        messagingSystem.blockedWords.add("recipe");
        messagingSystem.blockedWords.add("ginger");
        messagingSystem.blockedWords.add("nuclear");
        messagingSystem.agentSessionKeys.put("a0","0123456789");
        Agent target = Mockito.mock(Agent.class);
        Mailbox mailbox = Mockito.mock(Mailbox.class);
        when(target.getMailbox()).thenReturn(mailbox);
        when(target.getAgentID()).thenReturn("a1");
        ArrayList<Message> messages = new ArrayList<Message>();
        messagingSystem.agents.add(target);
        when(mailbox.getMessages()).thenReturn(messages);
        messagingSystem.agentSessionKeys.put("a0","0123456789");
        String message = "The key ingredient is GINGER!!!";
        assertEquals("OK",
                messagingSystem.sendMessage("0123456789","a0","a1",message));
        teardown();
    }

    @Test
    public void testSendMessageToFullMailbox() {
        setup();
        messagingSystem.agentSessionKeys.put("a0","0123456789");
        Agent target = Mockito.mock(Agent.class);
        Mailbox mailbox = Mockito.mock(Mailbox.class);
        when(target.getMailbox()).thenReturn(mailbox);
        when(target.getAgentID()).thenReturn("a1");
        ArrayList<Message> messages = new ArrayList<Message>();
        messagingSystem.agents.add(target);
        for(int i=0; i<25; i++) {
            Message message = Mockito.mock(Message.class);
            messages.add(message);
        }
        when(mailbox.getMessages()).thenReturn(messages);
        assertEquals("Target agent has reached mailbox capacity. Message not sent.",
                messagingSystem.sendMessage("0123456789","a0","a1",""));
    }

    @Test
    public void testValidSendMessage() {
        setup();
        messagingSystem.agentSessionKeys.put("a0","0123456789");
        Agent target = Mockito.mock(Agent.class);
        Mailbox mailbox = Mockito.mock(Mailbox.class);
        when(target.getMailbox()).thenReturn(mailbox);
        when(target.getAgentID()).thenReturn("a1");
        ArrayList<Message> messages = new ArrayList<Message>();
        messagingSystem.agents.add(target);
        when(mailbox.getMessages()).thenReturn(messages);
        assertEquals("OK",
                messagingSystem.sendMessage("0123456789","a0","a1","HELLO"));
    }

    @Test
    public void testGetAgents(){
        setup();
        assertEquals(0, messagingSystem.getAgents().size());
        teardown();
    }

    @Test
    public void testGetBlockedWords(){
        setup();
        messagingSystem.blockedWords.add("recipe");
        messagingSystem.blockedWords.add("ginger");
        messagingSystem.blockedWords.add("nuclear");
        assertEquals(3, messagingSystem.getBlockedWords().size());
        assertEquals("recipe",messagingSystem.getBlockedWords().get(0));
        assertEquals("ginger",messagingSystem.getBlockedWords().get(1));
        assertEquals("nuclear",messagingSystem.getBlockedWords().get(2));
        teardown();
    }

    @Test
    public void testGetAgentLoginKeys(){
        setup();
        assertEquals(0, messagingSystem.getAgentSessionKeys().size());
        teardown();
    }

    @Test
    public void testGetAgentSessionKeys(){
        setup();
        assertEquals(0, messagingSystem.getAgentLoginKeys().size());
        teardown();
    }
}
