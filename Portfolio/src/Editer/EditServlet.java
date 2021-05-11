package Editer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import LogIn.GetConnection;

@WebServlet("/EditServlet")
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public EditServlet() {
        super();
    }
	protected void doPUT(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PreparedStatement statement;
		JSONObject valueObj=new JSONObject();
		String output = "application/json";
		if(request.getSession().getAttribute("userid")==null){
			response.sendRedirect("/Portfolio/SignIn.html");
		}
		else {
			try {
			statement = GetConnection.getInstance().prepareStatement("INSERT into login (username, email, password) values (?,?,?)");
			statement.setString(1, request.getParameter("username"));
	        statement.setString(2, request.getParameter("email"));
	        statement.setString(3, request.getParameter("password"));
	        statement.setInt(4,(Integer) request.getSession().getAttribute("userid"));
	        statement.executeUpdate ();
	        response.setContentType(output);
	         String SignUpIsSuccessful = "Your Sign Up is Successful";
	         valueObj.put("STATUS",SignUpIsSuccessful); 
	         response.getWriter().print(valueObj);
		}catch (ClassNotFoundException | SQLException | IOException | JSONException e) {
			e.printStackTrace();
		}
			}
	}
}
