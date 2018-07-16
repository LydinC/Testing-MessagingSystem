import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageTests {

    Message message;
    @Before
    public void setup() {
        message = new Message("src","trg",0,"msg");
    }

    @After
    public void teardown() {
        message = null;
    }

    @Test
    public void testGetSourceAgentID(){
        setup();
        assertEquals("src", message.getSourceAgentID());
        teardown();
    }

    @Test
    public void testGetTargetAgentID(){
        setup();
        assertEquals("trg", message.getTargetAgentID());
        teardown();
    }

    @Test
    public void testGetTimestamp(){
        setup();
        assertEquals(0, message.getTimestamp());
        teardown();
    }

    @Test
    public void testGetMessageContent(){
        setup();
        assertEquals("msg", message.getMessageContent());
        teardown();
    }
}
