import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;

public class ReadMessageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
        HttpSession session = request.getSession();
        try {
            MessagingSystem messagingSystem = (MessagingSystem) session.getAttribute("messagingSystem");
            String agentID = (String) request.getSession().getAttribute("agentID");
            Agent agent = messagingSystem.getAgents().get(messagingSystem.getIndexWithAgentId(agentID));
            String message = agent.readNextMessage();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String docType =
                    "<!doctype html public \"-//w3c//dtd html 4.0 " +
                            "transitional//en\">\n";

            out.println(docType +
                    "<html>\n" +
                    "<head><title>" + "</title></head>\n" +

                    "<body>\n" +
                    "<p id=\"message\">" + message + "</p>\n");
                    if(agent.getLoginKey().equals("")) {
                        out.println("<input type=button onClick=\"location.href='mainpage.jsp'\" value = 'back to mainpage' name = \"back\">");
                    } else {
                        out.println("<input type=button onClick=\"location.href='loggedinmenu.jsp'\" value = 'back to menu page' name = \"back\">");
                    }
                    out.println("</body> </html>");

        } catch(Throwable exception) {
            System.out.println(exception);
        }
    }
}
