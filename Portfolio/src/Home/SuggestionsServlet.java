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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import LogIn.GetConnection;
import java.util.Arrays;

@WebServlet("/SuggestionsServlet")
public class SuggestionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public SuggestionsServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray valueArray=new JSONArray();
		JSONObject dataObj=new JSONObject();
		String output = "application/json";
		PreparedStatement statement;
		try {
			statement = GetConnection.getInstance().prepareStatement("SELECT Topics FROM profile where userid =?");
			statement.setInt( 1, (Integer) request.getSession().getAttribute("userid") );
	         ResultSet rs = statement.executeQuery();
	         rs.next();
	         String topics = rs.getString("Topics");
	         String[] array = topics.split(",");
	         System.out.print(array);
	         response.setContentType(output);
	         {
	        		PreparedStatement statement2=GetConnection.getInstance().prepareStatement("select blogid,profile.imageurl,blogs.imageurl,profile.userid,dateadded,title,username,views,blogs.topics,body from blogs INNER JOIN profile ON blogs.userid = profile.userid where blogs.topics = ? OR blogs.topics = ? OR blogs.topics = ? ORDER BY views DESC LIMIT 0,5");
	        		statement2.setString(1, array[0]);
	        		statement2.setString(2, array[1]);
	        		statement2.setString(3, array[2]);
	        		ResultSet rs2 = statement2.executeQuery();
	        		while(true) {if(rs2.next())
	        		{
	        		JSONObject titleObj = new JSONObject();
	        		String body = String.join("", rs2.getString("body").split("(<([^>]+)>)"));
	        		String FirstLine = body.split("\\.")[0];
	        		titleObj.put("BLOG_IMAGE_URL", rs2.getString("blogs.imageurl"));
	        		titleObj.put("CREATED",rs2.getString("dateadded"));
	        		titleObj.put("TITLE",rs2.getString("title"));
	        		titleObj.put("AUTHOR_NAME", rs2.getString("username"));
	        		titleObj.put("AUTHOR_IMAGE_URL", rs2.getString("profile.imageurl"));
	        		titleObj.put("FIRST_LINE", FirstLine);
	        		titleObj.put("link","/blogs/"+ rs2.getInt("blogid"));
	        		titleObj.put("READ_TIME", 5);
	        		valueArray.put(titleObj);
	        		}
	        		else{
	        			break;
	        		}
	        			}
	        		dataObj.put("DATA",valueArray);
	        		dataObj.put("TOPICS",rs.getString("Topics"));
	         response.getWriter().print(dataObj);
	         }
	         }
	         catch(ClassNotFoundException | SQLException | IOException | JSONException e){
	e.printStackTrace();
	}
	}
}