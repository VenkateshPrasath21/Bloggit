package Blog;

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
@WebServlet("/Blogs/*")
public class Blog extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Blog() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String blogNotFound="<!DOCTYPE html><html><head><title>Blog doesn't exist</title><style type='text/css'>html{height: 100%;width: 100%;}body{width: 100%;height: 100%;display: flex;justify-content: center;}</style></head><body><div style='font-family: sans-serif;margin-top: 280px;font-size: 30px;font-weight: 500;'>Blog doesn't exist :(</div></body></html>";
			JSONArray valueArray=new JSONArray();
			JSONObject dataObj=new JSONObject();
			JSONObject blogObj=new JSONObject();
			String output = "application/json";
			String userid = request.getRequestURI();
			System.out.println(userid);
			String StringOfblogid = userid.split("/")[userid.split("/").length-1];
			response.setContentType(output);	
			if(StringOfblogid.matches("^[0-9]+$") && StringOfblogid.length() <= 12){
				PreparedStatement statement;
				try {
					statement = GetConnection.getInstance().prepareStatement("SELECT * FROM Blogs INNER JOIN Profile ON Blogs.userid = Profile.userid WHERE blogid=?");
					statement.setInt(1,Integer.parseInt(StringOfblogid));
					ResultSet rs = statement.executeQuery();
					boolean bool = false;
					bool = rs.next();
					if(bool){	
		        		blogObj.put("DATE_ADDED",rs.getString("dateadded"));
		        		blogObj.put("IMAGE_URL",rs.getString("Blogs.imageurl"));
		        		blogObj.put("USER_NAME",rs.getString("username"));
		        		blogObj.put("BODY",rs.getString("body"));
		        		blogObj.put("TITLE",rs.getString("title"));
		        		blogObj.put("DESCRIPTION",rs.getString("description"));
		        		blogObj.put("AUTHOR_IMAGE_URL",rs.getString("profile.imageurl"));
		        		valueArray.put(blogObj);
		        		}else
		        		{
		        			response.getWriter().print(blogNotFound);
					}
					dataObj.put("DATA",valueArray);
					response.getWriter().print(dataObj);
				}
				catch (ClassNotFoundException | SQLException | IOException | JSONException e) {
				e.printStackTrace();
				}
			}
			else {
				response.getWriter().print(blogNotFound);
			}
		}	
}
