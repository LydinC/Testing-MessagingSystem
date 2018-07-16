import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;

public class RequestLoginKeyServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
        HttpSession session = request.getSession();
        try {
            MessagingSystem messagingSystem = (MessagingSystem) session.getAttribute("messagingSystem");
            String c_agentID =  request.getParameter("c_agentID");
            String c_supervisorID = request.getParameter("c_supervisorID");

            String agentID = (String) request.getSession().getAttribute("agentID");
            String supervisorID = (String) request.getSession().getAttribute("supervisorID");

            Agent agent = messagingSystem.getAgents().get(messagingSystem.getIndexWithAgentId(c_agentID));

            if(c_agentID.equals(agentID) && c_supervisorID.equals(supervisorID)) {
                agent.retrieveLoginKeyFromSupervisor();
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                String docType =
                        "<!doctype html public \"-//w3c//dtd html 4.0 " +
                                "transitional//en\">\n";

                out.println(docType +
                        "<html>\n" +
                        "<head><title>" + "</title></head>\n" +

                        "<body>\n" +
                        "<h1>"+agentID+ ", Your Login Key Is: </h1>" +
                        "<p id=\"key\">" + agent.getLoginKey() + "</p>\n" +
                        "<input type=button onClick=\"location.href='login.jsp'\" value = 'login' name=\"l\">" +
                        "</body> </html>");

            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                String docType =
                        "<!doctype html public \"-//w3c//dtd html 4.0 " +
                                "transitional//en\">\n";

                out.println(docType +
                        "<html>\n" +
                        "<head><title>" + "</title></head>\n" +

                        "<body>\n" +
                        "<h1> Confirmation and Initial IDs Do Not Match</h1>\n" +
                        "<input type=button onClick=\"location.href='welcome.jsp'\" value = 'back to welcome page' name = \"back\">" +
                        "</body> </html>");
            }
        } catch(Throwable exception) {
            System.out.println(exception);
        }
    }
}