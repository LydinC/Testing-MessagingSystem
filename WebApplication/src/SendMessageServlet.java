import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;

public class SendMessageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
        HttpSession session = request.getSession();
        try {
            MessagingSystem messagingSystem = (MessagingSystem) session.getAttribute("messagingSystem");
            String agentID = (String) request.getSession().getAttribute("agentID");
            Agent agent = messagingSystem.getAgents().get(messagingSystem.getIndexWithAgentId(agentID));

            if(agent.sendMessage(request.getParameter("receiverID") , request.getParameter("messageContent"))) {
                RequestDispatcher rd = request.getRequestDispatcher("loggedinmenu.jsp");
                rd.forward(request, response);
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
                        "<h1>"+agentID+ "</h1>" +
                        "<p id=\"error\">Cannot Send Message</p>\n" +
                        "<input type=button onClick=\"location.href='mainpage.jsp'\" value = 'back to mainpage' name=\"main\">" +
                        "</body> </html>");
            }

        } catch(Throwable exception) {
            System.out.println(exception);
        }
    }
}
