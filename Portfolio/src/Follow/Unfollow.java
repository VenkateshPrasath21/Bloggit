package Follow;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import LogIn.GetConnection;

/**
 * Servlet implementation class Follow
 */
@WebServlet("/Unfollow")
public class Unfollow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Unfollow() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PreparedStatement statement;
		JSONObject dataObj=new JSONObject();
		String output = "application/json";
		try {
			statement = GetConnection.getInstance().prepareStatement("DELETE FROM follow WHERE userid=? AND follower=?");
			statement.setInt(1,Integer.parseInt(request.getParameter("userid")));
			statement.setInt(2,(Integer) request.getSession().getAttribute("userid"));
			statement.executeUpdate();
			dataObj.put("MESSAGE", "success" );
			response.getWriter().print(dataObj);
			}catch(ClassNotFoundException | SQLException | IOException | JSONException e){
				e.printStackTrace();
			}
	}

}
