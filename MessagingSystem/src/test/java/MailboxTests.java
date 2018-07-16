import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class MailboxTests {

    Mailbox mailbox;
    @Before
    public void setup() {
        mailbox = new Mailbox();
    }

    @After
    public void teardown() {
        mailbox = null;
    }

    @Test
    public void testHasMessagesForEmptyMailbox() {
        setup();
        assertEquals(false, mailbox.hasMessages());
        teardown();
    }

    @Test
    public void testHasMessagesForNonEmptyMailbox() {
        setup();
        Message message = Mockito.mock(Message.class);
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(message);
        mailbox.messages = messages;
        assertEquals(true, mailbox.hasMessages());
        teardown();
    }

    @Test
    public void testEmptyMailboxForConsumeNextMessage() {
        setup();
        assertEquals(null, mailbox.consumeNextMessage());
        teardown();
    }

    @Test
    public void testNonEmptyMailboxForConsumeNextMessage() {
        setup();
        Message message = Mockito.mock(Message.class);
        when(message.getTimestamp()).thenReturn(System.currentTimeMillis());
        mailbox.messages.add(message);
        assertEquals(message, mailbox.consumeNextMessage());
        teardown();
    }

    @Test
    public void testExpiredMessageForConsumeNextMessage() {
        setup();
        Message message = Mockito.mock(Message.class);
        when(message.getTimestamp()).thenReturn(System.currentTimeMillis() - 31*60*1000);
        mailbox.messages.add(message);
        assertEquals(null, mailbox.consumeNextMessage());
        teardown();
    }

    @Test
    public void testGetMessages() {
        setup();
        assertEquals(0, mailbox.getMessages().size());
        teardown();
    }
    @Test
    public void testGetReadMessages() {
        setup();
        assertEquals(0, mailbox.getReadMessages().size());
        teardown();
    }
}
