import java.sql.*;

public class mysqlDAO {

    public Connection conn;
    public Statement st;
    public mysqlDAO(){
        String url = "jdbc:mysql://localhost:3306/c43_project";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");
            this.conn = DriverManager.getConnection(url, "root", "");
            this.st =this.conn.createStatement();
            System.out.println("Database connected!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
    public ResultSet bydatebycity(String low, String up, String city) throws SQLException {
        String query = "select * from listing natural join rents where rent_date between '%s' and '%s' and city = '%s'";
        query = String.format(query, low, up, city);
        return this.st.executeQuery(query);
    }
}