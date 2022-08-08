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

    public ResultSet GetBookingsFromUid(Integer u_id) throws SQLException {
        String query = "SELECT * from renter natural join rents natural join users where u_id = %d";
        query = String.format(query, u_id);
        System.out.println(query);
        return this.st.executeQuery(query);
    }
}

