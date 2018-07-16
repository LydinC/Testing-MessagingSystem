import javax.servlet.*;
import javax.servlet.http.*;
import java.io.PrintWriter;

public class LoggedInMenuServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
        try {
            String choice = request.getParameter("choice");
            String agentID = (String) request.getSession().getAttribute("agentID");
            String supervisorID = (String) request.getSession().getAttribute("supervisorID");

            request.getSession().setAttribute("agentID", agentID);
            request.getSession().setAttribute("supervisorID", supervisorID);
            if(choice.equals("1")) {
                RequestDispatcher rd = request.getRequestDispatcher("sendmessage.jsp");
                rd.forward(request, response);
            }
            else if(choice.equals("2")) {
                RequestDispatcher rd = request.getRequestDispatcher("readmessage.jsp");
                rd.forward(request, response);
            }
            else if(choice.equals("3")) {
                RequestDispatcher rd = request.getRequestDispatcher("logout.jsp");
                rd.forward(request, response);
            }
            else {
                RequestDispatcher rd = request.getRequestDispatcher("loggedinmenu.jsp");
                rd.forward(request, response);
            }
        } catch(Throwable exception) {
            System.out.println(exception);
        }
    }
}