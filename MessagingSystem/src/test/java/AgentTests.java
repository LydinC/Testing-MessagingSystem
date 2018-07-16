import net.bytebuddy.implementation.bind.annotation.Super;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AgentTests {

    Agent agent;
    @Before
    public void setup() {
        agent = new Agent();
        agent.agentID = "a0";
        agent.loginKey = "";
        agent.sessionKey = "";
    }

    @After
    public void teardown() {
        agent = null;
    }


    //Retrieve Login Key Method testing//
    //===============================================================================================//
    @Test
    public void testRetrieveLoginKeyWithNoSupervisor() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        when(messagingSystem.getAgents()).thenReturn(agents);
        assertEquals(false, agent.retrieveLoginKeyFromSupervisor());
        teardown();
    }

    @Test
    public void testValidRetrieveLoginKey() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        when(messagingSystem.registerLoginKey("0123456789","a0")).thenReturn(true);
        agent.ms = messagingSystem;
        agent.supervisorID = "s0";
        SupervisorAgent supervisor = Mockito.mock(SupervisorAgent.class);
        when(supervisor.getAgentID()).thenReturn("s0");
        when(supervisor.requestLoginKey("a0")).thenReturn("0123456789");

        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        agents.add(supervisor);
        when(messagingSystem.getAgents()).thenReturn(agents);
        assertEquals(true, agent.retrieveLoginKeyFromSupervisor());
        teardown();
    }
    //Login Method testing//
    //===============================================================================================//
    @Test
    public void testLoginWithNoSupervisor() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        when(messagingSystem.getAgents()).thenReturn(agents);
        assertEquals(false, agent.login());
        teardown();
    }

    @Test
    public void testLoginWithSupervisor() {
        setup();
        agent.loginKey = "0123456789";
        agent.supervisorID = "s0";
        agent.loginKeyAcquiredTime = System.currentTimeMillis();
        Agent supervisor = Mockito.mock(SupervisorAgent.class);
        when(supervisor.getAgentID()).thenReturn("s0");
        //when(supervisor.requestLoginKey("a0")).thenReturn("0123456789");
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        HashMap<String, String> loginKeys = new HashMap<String, String>();
        loginKeys.put("a0","0123456789");
        when(messagingSystem.getAgentLoginKeys()).thenReturn(loginKeys);
        String sessionKey = "qazwsxedcrfvtgbyhnujmik,,kiujmnheytgbrfvcdewsxzaqw";
        when(messagingSystem.login("a0","0123456789")).thenReturn(sessionKey);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        agents.add(supervisor);
        when(messagingSystem.getAgents()).thenReturn(agents);
        assertEquals(true, agent.login());
        teardown();
    }

    @Test
    public void testLoginWithUnregisteredLoginKey() {
        setup();
        SupervisorAgent supervisor = Mockito.mock(SupervisorAgent.class);
        when(supervisor.getAgentID()).thenReturn("s0");
        when(supervisor.requestLoginKey("a0")).thenReturn("0123456789");
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        agents.add(supervisor);
        when(messagingSystem.getAgents()).thenReturn(agents);
        when(messagingSystem.registerLoginKey("0123456789","a0")).thenReturn(false);

        agent.supervisorID = "s0";

        assertEquals(false, agent.login());
        teardown();
    }

    @Test
    public void testLoginWithExpiredLoginKey() {
        setup();
        SupervisorAgent supervisor = Mockito.mock(SupervisorAgent.class);
        when(supervisor.getAgentID()).thenReturn("s0");
        when(supervisor.requestLoginKey("a0")).thenReturn("0123456789");
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        agents.add(supervisor);
        when(messagingSystem.getAgents()).thenReturn(agents);
        when(messagingSystem.registerLoginKey("0123456789","a0")).thenReturn(true);
        when(messagingSystem.login("a0","0123456789")).thenReturn(null);


        agent.supervisorID = "s0";

        assertEquals(false, agent.login());
        teardown();
    }

    //Sending Message//
    //===============================================================================================//
    @Test
    public void testExceededMessageCountWhenSendingMessage() {
        setup();
        //Bypass the first if statement
        agent.loginTime = System.currentTimeMillis();
        //set message count over the limit
        agent.messageCount = 25;
        assertEquals(false, agent.sendMessage("a0","msg"));
        teardown();
    }

    @Test
    public void testValidSendingMessage() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        when(messagingSystem.sendMessage("","a0","s0","hello")).thenReturn("OK");
        //set agent login time to current time so as to not use the login method in this test
        agent.loginTime = System.currentTimeMillis();
        assertEquals(true, agent.sendMessage("s0","hello"));
        teardown();
    }

    @Test
    public void testInvalidSendingMessage() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        when(messagingSystem.sendMessage("","a0","s0","")).thenReturn("Message exceeds maximum 140 characters. Message not sent.");
        //set agent login time to current time so as to not use the login method in this test
        agent.loginTime = System.currentTimeMillis();
        assertEquals(false, agent.sendMessage("s0",""));
        teardown();
    }

    @Test
    public void testForceLogoutWhileSendingMessage() {
        setup();
        //set agent login time to current time - 11 minutes
        agent.loginTime = System.currentTimeMillis() - 11*60*1000;
        assertEquals(false, agent.sendMessage("",""));
        teardown();
    }

    //Supervisor//
    //===============================================================================================//

    @Test
    public void testNoSupervisorAvailable() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        when(messagingSystem.getAgents()).thenReturn(agents);
        assertEquals(null, agent.getSupervisor());
        teardown();
    }

    @Test
    public void testSupervisorAvailable() {
        setup();
        //set up a supervisor so that a supervisor will be available
        Agent supervisor = Mockito.mock(SupervisorAgent.class);
        when(supervisor.getAgentID()).thenReturn("s0");
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        ArrayList<Agent> agents = new ArrayList<Agent>();
        agents.add(agent);
        agents.add(supervisor);
        when(messagingSystem.getAgents()).thenReturn(agents);
        agent.supervisorID = "s0";
        assertEquals(supervisor, agent.getSupervisor());
        teardown();
    }

    //ReadNextMessage//
    //===============================================================================================//

    @Test
    public void testForceLogoutWhileReadingMessage() {
        setup();
        //set agent login time to current time - 11 minutes
        agent.loginTime = System.currentTimeMillis() - 11*60*1000;
        assertEquals("Cannot read next message.", agent.readNextMessage());
        teardown();
    }

    @Test
    public void testNoMessageToReadFromMailbox(){
        setup();
        agent.loginTime = System.currentTimeMillis();

        Mailbox mailbox = Mockito.mock(Mailbox.class);
        agent.mailbox = mailbox;

        when(mailbox.consumeNextMessage()).thenReturn(null);
        assertEquals("No messages available.", agent.readNextMessage());
        teardown();
    }

    @Test
    public void testValidReadNextMessage(){
        setup();
        agent.loginTime = System.currentTimeMillis();

        Mailbox mailbox = Mockito.mock(Mailbox.class);
        agent.mailbox = mailbox;

        Message message1 = Mockito.mock(Message.class);

        when(message1.getMessageContent()).thenReturn("hello123");
        when(message1.getSourceAgentID()).thenReturn("a0");
        when(message1.getTargetAgentID()).thenReturn("a1");
        when(message1.getTimestamp()).thenReturn(0L);

        when(mailbox.consumeNextMessage()).thenReturn(message1);
        assertEquals("a0: hello123", agent.readNextMessage());

        teardown();
    }

    //Logout Required//
    //===================================================================================//

    @Test
    public void testHaveToLogout(){
        setup();
        agent.loginTime = System.currentTimeMillis() - 11*60*1000;
        assertEquals(true, agent.logoutRequired());
        teardown();
    }

    @Test
    public void testDoNotHaveToLogout(){
        setup();
        agent.loginTime =System.currentTimeMillis();
        assertEquals(false, agent.logoutRequired());
        teardown();
    }

    //Test Logout
    //===================================================================================//
    @Test
    public void testLogout(){
        setup();
        agent.loginKey = "0123456789";
        agent.logout();
        assertEquals("", agent.loginKey);
        assertEquals("", agent.sessionKey);
        assertEquals(0, agent.messageCount);
        teardown();
    }

    //Test Getters
    //===================================================================================//


    @Test
    public void testGetMessageCount() {
        setup();
        assertEquals(0, agent.messageCount);
        teardown();
    }

    @Test
    public void testGetLoginKeyAcquiredTime() {
        setup();
        assertEquals(0, agent.getLoginKeyAcquiredTime());
        teardown();
    }

    @Test
    public void testGetMailbox() {
        setup();
        Mailbox  newMailbox = Mockito.mock(Mailbox.class);
        agent.mailbox = newMailbox;
        assertEquals(newMailbox, agent.getMailbox());
        teardown();
    }

    @Test
    public void testGetLoginTime() {
        setup();
        assertEquals(0, agent.getLoginTime());
        teardown();
    }

    @Test
    public void testGetMs() {
        setup();
        MessagingSystem messagingSystem = Mockito.mock(MessagingSystem.class);
        agent.ms = messagingSystem;
        assertEquals(messagingSystem, agent.getMs());
        teardown();
    }

    @Test
    public void testGetAgentID() {
        setup();
        assertEquals("a0", agent.getAgentID());
        teardown();
    }

    @Test
    public void testGetLoginKey() {
        setup();
        agent.loginKey = "0123456789";
        assertEquals("0123456789", agent.getLoginKey());
        teardown();
    }
    @Test
    public void testGetSessionKey() {
        setup();
        assertEquals("", agent.getSessionKey());
        teardown();
    }

    @Test
    public void testGetSupervisorID() {
        setup();
        agent.supervisorID = "s0";
        assertEquals("s0", agent.getSupervisorID());
        teardown();
    }

}