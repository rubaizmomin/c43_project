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
        System.out.println(query);
        return this.st.executeQuery(query);
    }

    public void DeleteAvailability(String date, String home_address) throws SQLException {
        String query = "DELETE available FROM available WHERE available_date = '%s' and home_address = '%s'";
        query = String.format(query, date, home_address);
        this.st.execute(query);
        return;
    }

    public ResultSet getEmailfromUid(Integer u_id) throws SQLException {
        String query = "SELECT email FROM Users WHERE u_id = %d";
        query = String.format(query, u_id);
        System.out.println(query);
        return this.st.executeQuery(query);
    }

    public ResultSet CheckRenter(String email) throws SQLException {
        String query = "SELECT * FROM Renter WHERE email = '%s'";
        query = String.format(query, email);
        return this.st.executeQuery(query);
    }

    public void UpdatePaymentInfo(String email, String payment_info) throws SQLException {
        String query = "UPDATE Renter SET payment_info = '%s' WHERE email = '%s'";
        query = String.format(query, email, payment_info);
        this.st.execute(query);
        return;
    }
    public void AddBooking(String date, String email, String home_address) throws SQLException {
        String query = "INSERT INTO rents(rent_date, email, home_address) VALUES ('%s', '%s', '%s')";
        query = String.format(query, date, email, home_address);
        System.out.println(query);
        this.st.execute(query);
        return;
    }

    public void AddRenter(String email, String payment_info) throws SQLException {
        String query = "INSERT INTO Renter (email, payment_info) VALUES ('%s', '%s')";
        query = String.format(query, email, payment_info);
        this.st.execute(query);
        return;
    }
    public ResultSet getAvailableDataFromLid(Integer l_id) throws SQLException {
        String query = "select * from available natural join listing where l_id = %d";
        query = String.format(query, l_id);
        return this.st.executeQuery(query);
    }
}