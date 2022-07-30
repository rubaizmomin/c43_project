import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class mysqlDAO {

    public Connection conn;
    public Statement st;
    String url = "jdbc:mysql://localhost:3306/c43_project";
    String username = "root";
    String password = "";

    public mysqlDAO(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
            this.conn = DriverManager.getConnection(url, username, password);
            this.st =this.conn.createStatement();
            System.out.println("Database connected!");
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
