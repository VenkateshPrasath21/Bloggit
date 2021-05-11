package Home;

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
import org.json.JSONArray;

import LogIn.GetConnection;

@WebServlet("/index")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray valueArray=new JSONArray();
		JSONObject dataObj=new JSONObject();
		String output = "application/json";
		if(request.getSession().getAttribute("userid")==null){
			response.sendRedirect("/signin.html");
		}
		else {
			System.out.println(request.getSession().getAttribute("userid"));
			PreparedStatement statement;
			try {
				statement = GetConnection.getInstance().prepareStatement("select blogid,profile.imageurl,blogs.imageurl,profile.userid,dateadded,title,username,views,blogs.topics,body from blogs INNER JOIN profile ON blogs.userid = profile.userid ORDER BY views DESC");
		
		         ResultSet rs = statement.executeQuery();
		         response.setContentType(output);
		         while(true){
		        	if(rs.next()) {
		        		JSONObject titleObj = new JSONObject();
		        		String body = String.join("", rs.getString("body").split("(<([^>]+)>)"));
		        		String FirstLine = body.split("\\.")[0];
		        		titleObj.put("BLOG_IMAGE_URL", rs.getString("blogs.imageurl"));
		        		titleObj.put("TITLE", rs.getString("title"));
		        		titleObj.put("CREATED",rs.getString("dateadded"));
		        		titleObj.put("AUTHOR_NAME", rs.getString("username"));
		        		titleObj.put("link","/blogs/"+ rs.getString("blogid"));
		        		titleObj.put("AUTHOR_NAME", rs.getString("username"));
		        		titleObj.put("AUTHOR_IMAGE_URL", rs.getString("profile.imageurl"));
		        		titleObj.put("FIRST_LINE", FirstLine);
		        		titleObj.put("READ_TIME", 5);
		        		valueArray.put(titleObj);
		         }
		        	else {
		        	 break;
		        	 }
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