package LogIn;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogInServlet")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LogInServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.print("JSP\n");
		 PreparedStatement statement;
		try {
			statement = GetConnection.getInstance().prepareStatement("SELECT userid,username FROM login WHERE username=? AND password=?");
			statement.setString(1, request.getParameter("username"));
	        statement.setString(2, request.getParameter("password"));
	         ResultSet rs = statement.executeQuery();
	         boolean bool = false;
	         bool=rs.next();
	         
         if(bool){
        	 System.out.print(rs.getInt("userid"));
        	 HttpSession session=request.getSession(false);
				
				if (session!=null && !session.isNew()) {
				    session.invalidate();
				    System.out.print(session.getId());
				}
				session=request.getSession(true);
				request.getSession().setMaxInactiveInterval(60*60*24);
     		 session.setAttribute("userid", rs.getInt("userid"));
     		 response.addHeader("Set-Cookie", " identification="+rs.getString("username")+";");
     		 response.sendRedirect("/homepage.html");
         }
         else {
        	 response.getWriter().print("Wait a minute! WHO ARE YOU!?");
         }
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
