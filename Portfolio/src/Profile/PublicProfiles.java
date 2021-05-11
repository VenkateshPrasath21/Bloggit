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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import LogIn.GetConnection;

@WebServlet("/PublicProfile/*")
public class PublicProfiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public PublicProfiles() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getSession().getAttribute("userid")==null){
			response.sendRedirect("/signin.html");
		}
		else {
			String userNotFound="<!DOCTYPE html><html><head><title>User not found</title><style type='text/css'>html{height: 100%;width: 100%;}body{width: 100%;height: 100%;display: flex;justify-content: center;}</style></head><body><div style='font-family: sans-serif;margin-top: 280px;font-size: 30px;font-weight: 500;'>Sorry, User Not Found :(</div></body></html>";
			String output = "text/html";
			String userid = request.getRequestURI();
			System.out.println(userid);
			String StringOfuserid = userid.split("/")[userid.split("/").length-1];
			response.setContentType(output);
			
			if(StringOfuserid.matches("^[0-9]+$") && StringOfuserid.length() <= 12){
				JSONObject dataObj=new JSONObject();
		        JSONObject valueObj=new JSONObject();
		        JSONArray valueArray=new JSONArray();
				PreparedStatement statement;
				PreparedStatement statement2;
				PreparedStatement statement3;
				try {
					statement = GetConnection.getInstance().prepareStatement("SELECT userid,username,description,imageurl,email from profile WHERE userid=?");
					statement.setInt(1,Integer.parseInt(StringOfuserid));
					ResultSet rs = statement.executeQuery();
					boolean bool = false;
					bool = rs.next();
					if(bool){
						statement2 = GetConnection.getInstance().prepareStatement("SELECT COUNT(follower) from follow WHERE userid=?");
						statement2.setInt(1,Integer.parseInt(StringOfuserid));
						ResultSet rs2 = statement2.executeQuery();
						statement3 = GetConnection.getInstance().prepareStatement("SELECT COUNT(userid) from follow WHERE follower=?");
						statement3.setInt(1,Integer.parseInt(StringOfuserid));
						ResultSet rs3 = statement3.executeQuery();
						 rs2.next();
						 rs3.next();
				         valueObj.put("USER_ID",rs.getInt("userid"));
				         valueObj.put("DESCRIPTION",rs.getString("description"));
				         valueObj.put("IMAGE_URL",rs.getString("imageurl"));
				         valueObj.put("USER_NAME",rs.getString("username"));
				         valueObj.put("EMAIL",rs.getString("email"));
				         valueObj.put("FOLLOWERS",rs2.getInt("COUNT(follower)"));
				         valueObj.put("FOLLOWING", rs3.getInt("COUNT(userid)"));
				         valueArray.put(valueObj);
					}else {
						response.getWriter().print(userNotFound);
					}
					dataObj.put("DATA",valueArray);
					response.getWriter().print(dataObj);
					}
				
			catch (ClassNotFoundException | SQLException | IOException | JSONException e) {
			e.printStackTrace();
			}
		}
	}
		}
}
