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
@WebServlet("/ShowFollowing")
public class ShowFollowing extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public ShowFollowing() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PreparedStatement statement;
		JSONArray valueArray=new JSONArray();
		JSONObject dataObj=new JSONObject();
		String output = "application/json";
		try {
			statement = GetConnection.getInstance().prepareStatement("Select username, description,profile.userid,imageurl from follow INNER JOIN profile ON profile.userid = follow.userid where follow.follower = ?");
			statement.setInt(1,Integer.parseInt( request.getParameter("userid")));
			ResultSet rs = statement.executeQuery();
			while(true) {
				if(rs.next()) {
					JSONObject titleObj = new JSONObject();
					titleObj.put("DESCRIPTION",rs.getString("description"));
			        titleObj.put("IMAGE_URL",rs.getString("imageurl"));
			        titleObj.put("USER_NAME",rs.getString("username"));
					titleObj.put("FOLLOWING_ID",rs.getInt("profile.userid"));
					valueArray.put(titleObj);
				}else {
					break;
				}
			}	
			dataObj.put("DATA",valueArray);
			response.getWriter().print(dataObj);
	}catch(ClassNotFoundException | SQLException | IOException | JSONException e){
		e.printStackTrace();
		}
}
	}
