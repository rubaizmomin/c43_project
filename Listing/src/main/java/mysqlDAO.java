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
    public void addlisting(String listing_type, String postal_code, String home_address, String city, String country, Float lat, Float lon) throws SQLException {
        String query ="INSERT INTO Listing (listing_type, postal_code, home_address, city, country, latitude, longitude) VALUES ('%s', '%s', '%s', '%s', '%s', %f, %f)";
        query =String.format(query, listing_type, postal_code, home_address, city, country, lat, lon);
        this.st.execute(query);
        return;
    }


    public void addlistingtoOwns(String email, String home_address) throws SQLException {
        String query ="INSERT INTO owns (email, home_address) VALUES ('%s', '%s')";
        query =String.format(query, email, home_address);
        this.st.execute(query);
        return;
    }

    public ResultSet getDataThroughAddress(String home_address) throws SQLException {
        String query = "SELECT * FROM Listing WHERE home_address = '%s'";
        query = String.format(query, home_address);
        return this.st.executeQuery(query);
    }
}

