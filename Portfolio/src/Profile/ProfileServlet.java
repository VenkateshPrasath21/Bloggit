package Profile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import LogIn.GetConnection;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet  {
	private static final long serialVersionUID = 1L;
    public ProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if(request.getSession().getAttribute("userid")==null){
			response.sendRedirect("/signin.html");
		}
		else {
			System.out.println(request.getSession().getAttribute("userid"));
			PreparedStatement statement;
			try {
				statement = GetConnection.getInstance().prepareStatement("SELECT userid,username,email FROM profile WHERE userid=?");
				statement.setInt(1,(Integer) request.getSession().getAttribute("userid"));
		         ResultSet rs = statement.executeQuery();
		         rs.next();
		         System.out.println(rs.getInt("userid"));
		         System.out.println(rs.getString("username"));
		         System.out.println(rs.getString("email"));
		         String output = "application/json";
		         response.setContentType(output);
		         JSONObject dataObj=new JSONObject();
		         JSONObject valueObj=new JSONObject();
		         valueObj.put("userid",rs.getInt("userid"));
		         valueObj.put("username",rs.getString("username"));
		         valueObj.put("email",rs.getString("email"));
		 		dataObj.put("DATA",valueObj);
		 		response.getWriter().print(dataObj);		 		    
		}catch (ClassNotFoundException | SQLException | IOException | JSONException e) {
			e.printStackTrace();
		}
	}
	}
	}
