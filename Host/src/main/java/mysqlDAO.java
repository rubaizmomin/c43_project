import java.sql.*;

public class mysqlDAO {

    public Connection conn;
    public Statement st;
    public mysqlDAO() {
        String url = "jdbc:mysql://localhost:3306/c43_project";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
            this.conn = DriverManager.getConnection(url, "root", "");
            this.st = this.conn.createStatement();
            System.out.println("Database connected!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    public void addHost(String email) throws SQLException {
        String query = "INSERT INTO Host (email) VALUES ('%s')";
        query = String.format(query, email);
        this.st.execute(query);
        return;
    }
}

