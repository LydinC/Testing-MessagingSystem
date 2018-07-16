import net.bytebuddy.implementation.bind.annotation.Super;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SupervisorAgentTests {

    SupervisorAgent supervisorAgent;

    @Before
    public void setup() {
        supervisorAgent = new SupervisorAgent();
    }

    @After
    public void teardown() {
        supervisorAgent = null;
    }

    @Test
    public void testRequestLoginKey() {
        setup();
        Assert.assertEquals(10, supervisorAgent.requestLoginKey("a0").length());
        teardown();
    }
}