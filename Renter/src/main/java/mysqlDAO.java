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

    public ResultSet GetAvailableListings() throws SQLException {
        String query = " select distinct home_address, l_id, listing_type, postal_code, city, country from available natural join listing";
        return this.st.executeQuery(query);
    }

    public ResultSet getDataThroughLid(Integer l_id) throws SQLException {
        String query = "SELECT * FROM Listing WHERE l_id = %d";
        query = String.format(query, l_id);
        return this.st.executeQuery(query);
    }

    public ResultSet getAvailableDataFromLid(Integer l_id) throws SQLException {
        String query = "select * from available natural join listing where l_id = %d";
        query = String.format(query, l_id);
        return this.st.executeQuery(query);
    }
}