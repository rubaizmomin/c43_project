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
    public ResultSet filterwithquery(String condition) throws SQLException {
        String query = "select home_address, l_id, listing_type, postal_code, city, country from listing natural join has natural join available WHERE %s";
        query = String.format(query, condition);
        System.out.println(query);
        return this.st.executeQuery(query);
    }

    public ResultSet filterwithoutquery(String condition) throws SQLException {
        String query = "select home_address, l_id, listing_type, postal_code, city, country from listing natural join has natural join available %s";
        query = String.format(query, condition);
        System.out.println(query);
        return this.st.executeQuery(query);
    }

    public ResultSet getAmenityID(String amenity_type) throws SQLException {
        String query = "SELECT * FROM amenity WHERE amenity_type = '%s'";
        query = String.format(query, amenity_type);
        return this.st.executeQuery(query);
    }
}