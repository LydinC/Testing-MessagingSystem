import com.sun.org.apache.regexp.internal.RE;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

public class MainPageServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("messagingSystem") == null) {
            MessagingSystem messagingSystem = new MessagingSystem();
            messagingSystem.setBlockedWords();
            session.setAttribute("messagingSystem", messagingSystem);
            Agent supervisor = new SupervisorAgent();
            messagingSystem.agents.add(supervisor);
            supervisor.ms = messagingSystem;
            supervisor.agentID = "s0";
            supervisor.login();
            session.setAttribute("supervisor", supervisor);
        }
        try {
            String agentID = request.getParameter("agentID");
            if (agentID.startsWith("spy-")) {
                response.sendRedirect("mainpage.jsp");
            }
            MessagingSystem messagingSystem = (MessagingSystem) session.getAttribute("messagingSystem");
            String supervisorID = request.getParameter("supervisorID");
            if(messagingSystem.getIndexWithAgentId(agentID) == -1){ //new Agent
                if(messagingSystem.getIndexWithAgentId(supervisorID) != -1) { //supervisor exists
                    //creation of agent in system
                    Agent agent = new Agent();
                    agent.agentID = agentID;
                    agent.supervisorID = supervisorID;
                    agent.ms = messagingSystem;
                    messagingSystem.agents.add(agent);

                    request.getSession().setAttribute("agentID", agentID);
                    request.getSession().setAttribute("supervisorID", supervisorID);

                    RequestDispatcher rd = request.getRequestDispatcher("welcome.jsp");
                    rd.forward(request, response);
                    //response.sendRedirect("welcome.jsp");
                } else { //no supervisor exists with given ID -- error message
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    String docType =
                            "<!doctype html public \"-//w3c//dtd html 4.0 " +
                                    "transitional//en\">\n";
                    //print error message and offer button redirection to mainpage
                    out.println(docType +
                            "<html>\n" +
                            "<head><title>" + "</title></head>\n" +
                            "<body>\n" +
                            "<h1> Invalid Supervisor ID</h1>\n" +
                            "<input type=button onClick=\"location.href='mainpage.jsp'\" value = 'back to mainpage' name=\"main\">" +
                            "</body> </html>");
                }
            } else { //agent already in system
                if(messagingSystem.getAgents().get(messagingSystem.getIndexWithAgentId(agentID)).supervisorID.equals(supervisorID)) {
                    //matching supervisor ID, redirect to welcome page
                    request.getSession().setAttribute("agentID", agentID);
                    request.getSession().setAttribute("supervisorID", supervisorID);
                    //if(messagingSystem.getAgents().get(messagingSystem.getIndexWithAgentId(agentID)).loginKey.equals("")) {
                        RequestDispatcher rd = request.getRequestDispatcher("welcome.jsp");
                        rd.forward(request, response);
                    //}
                } else { //supervisor inputted is different that previous supervisor ID
                    response.setContentType("text/html");
                    PrintWriter out = response.getWriter();
                    String docType =
                            "<!doctype html public \"-//w3c//dtd html 4.0 " +
                                    "transitional//en\">\n";

                    out.println(docType +
                            "<html>\n" +
                            "<head><title>" + "</title></head>\n" +
                            "<body>\n" +
                            "<h1> This is not your previous supervisor</h1>\n" +
                            "<input type=button onClick=\"location.href='mainpage.jsp'\" value = 'back to mainpage' name=\"main\">" +
                            "</body> </html>");
                }
            }
        } catch(Throwable exception) {
            System.out.println(exception);
        }
    }
}