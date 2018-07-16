import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;

public class WelcomeServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {

        try {
            String agentID = (String) request.getSession().getAttribute("agentID");
            String supervisorID = (String) request.getSession().getAttribute("supervisorID");

            request.getSession().setAttribute("agentID", agentID);
            request.getSession().setAttribute("supervisorID", supervisorID);

            RequestDispatcher rd = request.getRequestDispatcher("requestloginkey.jsp");
            rd.forward(request, response);
        } catch(Throwable exception) {
            System.out.println(exception);
        }
    }
}