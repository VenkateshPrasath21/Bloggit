package Blogger;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import LogIn.GetConnection;
import java.io.File;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@WebServlet("/bloggit")
@MultipartConfig
public class BlogAddingServlet extends HttpServlet {
    public BlogAddingServlet() {
        super();
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("userid")==null){
			response.sendRedirect("/signin.html");
		}else {
		PreparedStatement statement;
		Part file=request.getPart("file");
		String filename=Paths.get(file.getSubmittedFileName()).toString();
		InputStream ios = file.getInputStream();
		BufferedImage bi=ImageIO.read(ios);
		String metadata="bloggit-"+System.currentTimeMillis();
		File outputfile = new File("\\Users\\venkat-zuch755\\eclipse-workspace\\Portfolio\\WebContent\\images\\"+metadata+".png");
		ImageIO.write(bi, "png", outputfile);
		
		try {
		statement = GetConnection.getInstance().prepareStatement("INSERT into Blogs (title, body, imageurl, userid, description) values (?,?,?,?,?)");
		statement.setString(1, request.getParameter("title"));
        statement.setString(2, request.getParameter("myDoc"));
        statement.setString(3, "/Portfolio/images/"+metadata+".png");
        statement.setInt(4,(Integer) request.getSession().getAttribute("userid"));
        statement.setString(5, request.getParameter("description"));
        statement.executeUpdate ();
        PreparedStatement statement2=GetConnection.getInstance().prepareStatement("select blogid from Blogs ORDER BY blogid DESC LIMIT 1");
		ResultSet rs2 = statement2.executeQuery();
		rs2.next();
		response.sendRedirect("http://localhost:9093/blogs/"+rs2.getInt("blogid"));
	}catch (ClassNotFoundException | SQLException | IOException e) {
		e.printStackTrace();
		}
		 }
}
	}