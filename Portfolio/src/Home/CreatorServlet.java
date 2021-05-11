package Home;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import LogIn.GetConnection;

@WebServlet("/CreatorServlet")
public class CreatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public CreatorServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray valueArray=new JSONArray();
		JSONObject dataObj=new JSONObject();
		String output = "application/json";
		System.out.print("\n"+request.getSession().getAttribute("userid")+"\n");
		try {
		PreparedStatement statement = GetConnection.getInstance().prepareStatement("SELECT Topics FROM profile where userid = ?");
		statement.setInt(1, (Integer)request.getSession().getAttribute("userid"));
        ResultSet rs = statement.executeQuery();
        rs.next();
        String topics = rs.getString("Topics");
        String[] array = topics.split(",");
        System.out.print(array);
        response.setContentType(output);
        PreparedStatement statement2=GetConnection.getInstance().prepareStatement("select username,profile.description,profile.imageurl,profile.userid from blogs INNER JOIN profile ON blogs.userid = profile.userid where blogs.topics = ? OR blogs.topics = ? OR blogs.topics = ? ORDER BY views ASC");
		statement2.setString(1, array[0]);
		statement2.setString(2, array[1]);
		statement2.setString(3, array[2]);
        ResultSet rs2 = statement2.executeQuery();
        JSONObject titleObj = new JSONObject();
        while(true) {
			if(rs2.next()){
				if(rs2.getString("profile.userid").equals("1")){
	        			System.out.println("Venkatesh"+rs2.getString("profile.userid"));
	        			System.out.println("Jaswanth"+request.getSession().getAttribute("userid"));
	        		}else {
	        			titleObj.put(rs2.getString("username"), rs2.getString("description")+"SJ24021955"+rs2.getString("imageurl")+"SJ24021955"+rs2.getString("profile.userid"));
	        		}
					}else{
	        			break;
	        		}
	        			}
      
        JSONArray keys = titleObj.names ();

        for (int i = 0; i < keys.length (); i++) {
        	JSONObject valueObj = new JSONObject();
           String key = keys.getString (i); // Here's your key
           String value = titleObj.getString (key); // Here's your value
           valueObj.put("USERNAME", key);
           valueObj.put("DESCRIPTION", value.split("SJ24021955")[0]);
           valueObj.put("IMAGE_URL", value.split("SJ24021955")[1]);
           valueObj.put("LINK", "/profile/"+value.split("SJ24021955")[2]);
   			valueArray.put(valueObj);
        }
        
        dataObj.put("DATA",valueArray);
        response.getWriter().print(dataObj);
	}
		catch(ClassNotFoundException | SQLException | IOException | JSONException e){
		e.printStackTrace();
		}
}
}