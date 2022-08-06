import io.github.cdimascio.dotenv.Dotenv;

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
//            int result = st.executeUpdate("GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;");
//            result = st.executeUpdate("FLUSH PRIVILEGES;");
//            result = st.executeUpdate("USE DATABASE c43_project");
//            st.executeQuery("CREATE DATABASE [IF NOT EXISTS] c43_project;");
            System.out.println("Database connected!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public ResultSet checkAmenityType(String amenity_type) throws SQLException {
        String query = "SELECT * FROM amenity WHERE amenity_type = '%s'";
        query = String.format(query, amenity_type);
        return this.st.executeQuery(query);
    }

    public void addAmenity(String amenity_type) throws SQLException {
        String query = "INSERT INTO amenity (amenity_type) VALUES ('%s')";
        query = String.format(query, amenity_type);
        this.st.execute(query);
    }
    public ResultSet getAmenityID(String amenity_type) throws SQLException {
        String query = "SELECT * FROM amenity WHERE amenity_type = '%s'";
        query = String.format(query, amenity_type);
        return this.st.executeQuery(query);
    }
    public void addAmenitytoListing(String home_address, Integer a_id) throws SQLException {
        String query = "INSERT INTO has (home_address, a_id) VALUES ('%s', %d)";
        query = String.format(query, home_address, a_id);
        this.st.execute(query);
        return;
    }
}