package SignUp;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;
import LogIn.GetConnection;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public SignUpServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JSONObject valueObj=new JSONObject();
		String output = "application/json";
		PreparedStatement statement;
		if(request.getParameter("password").equals(request.getParameter("confirmpassword"))){
			try{
				statement = GetConnection.getInstance().prepareStatement("SELECT email,username FROM login WHERE username=? OR email=?");
				statement.setString(1, request.getParameter("username"));
		        statement.setString(2, request.getParameter("email"));
		         ResultSet rs = statement.executeQuery();
		         boolean bool = false;
		         bool=rs.next();
			if(bool){
				if(rs.getString("username").equals(request.getParameter("username")) && rs.getString("email").equals(request.getParameter("email"))) {
					response.setContentType(output);
					valueObj.put("STATUS", "Your Username and Email already exists");
					response.getWriter().print(valueObj);
			}
				else if(rs.getString("username").equals(request.getParameter("username"))) {
					response.setContentType(output);
					valueObj.put("STATUS", "Your Username already exists");
					response.getWriter().print(valueObj);
			}
				else if(rs.getString("email").equals(request.getParameter("email"))) {
					response.setContentType(output);
					valueObj.put("STATUS", "Your Email already exists");
					response.getWriter().print(valueObj);
			}
				}
			else 
			{
		 try{
			statement = GetConnection.getInstance().prepareStatement("INSERT into login (username, email, password) values (?,?,?)");
			statement.setString(1, request.getParameter("username"));
	        statement.setString(2, request.getParameter("email"));
	        statement.setString(3, request.getParameter("password"));
	        statement.executeUpdate ();
	        response.setContentType(output);
	         String SignUpIsSuccessful = "Your Sign Up is Successful";
	         valueObj.put("STATUS",SignUpIsSuccessful); 
	         response.getWriter().print(valueObj);
         }
		 catch (ClassNotFoundException | SQLException | IOException | JSONException e){
			e.printStackTrace();}
		}
			}
			catch (ClassNotFoundException | SQLException | IOException | JSONException e) 
			{
			e.printStackTrace();
			}
		}
		else{
			 try {
				response.setContentType(output);
				valueObj.put("STATUS", "Your new password is not equals to the old password");
				response.getWriter().print(valueObj);
				}
			 catch (JSONException e) {
				e.printStackTrace();
				}
			 }
		}
	}