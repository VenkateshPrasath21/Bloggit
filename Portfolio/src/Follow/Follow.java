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
@WebServlet("/Follow")
public class Follow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Follow() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PreparedStatement statement2;
		JSONObject dataObj=new JSONObject();
		String output = "application/json";
		try {
		PreparedStatement statement;
		statement = GetConnection.getInstance().prepareStatement("Select * from follow where userid = ? AND follower = ?");
		statement.setInt(1,Integer.parseInt(request.getParameter("userid")));
		statement.setInt(2,(Integer) request.getSession().getAttribute("userid"));
		ResultSet rs = statement.executeQuery();
		if(rs.next()) {
			dataObj.put("MESSAGE", "already following" );
			response.getWriter().print(dataObj);
		}
		else {
		
			statement2 = GetConnection.getInstance().prepareStatement("INSERT INTO follow (userid,follower) VALUES (?,?)");
			statement2.setInt(1,Integer.parseInt(request.getParameter("userid")));
			statement2.setInt(2,(Integer) request.getSession().getAttribute("userid"));
			statement2.executeUpdate();
			dataObj.put("MESSAGE", "success"  );
			response.getWriter().print(dataObj);
			}
	}catch(ClassNotFoundException | SQLException | IOException | JSONException e){
		e.printStackTrace();
	}
}
}