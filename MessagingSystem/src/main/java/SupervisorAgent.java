import java.util.Random;

public class SupervisorAgent extends Agent implements Supervisor {
    public String requestLoginKey(String AgentID) {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!%^&*()$£";
        int max = alphabet.length();
        Random r = new Random();
        String keyGenerator  = "";
        for (int i = 0; i < 10; i++) {
            keyGenerator = keyGenerator + alphabet.charAt(r.nextInt(max));
        }
        return keyGenerator;
    }
}
