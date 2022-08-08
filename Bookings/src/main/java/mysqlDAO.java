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

    public ResultSet getEmailfromUid(Integer u_id) throws SQLException {
        String query = "SELECT email FROM Users WHERE u_id = %d";
        query = String.format(query, u_id);
        return this.st.executeQuery(query);
    }

    public ResultSet getDataThroughLid(Integer l_id) throws SQLException {
        String query = "SELECT * FROM Listing WHERE l_id = %d";
        query = String.format(query, l_id);
        return this.st.executeQuery(query);
    }
    public void UpdateRenterReview(String email, Integer l_id, String renter_comment, Integer renter_rating, String rent_date) throws SQLException {
        String query = "UPDATE rents natural join listing SET comment = '%s', rating = %d WHERE rent_date = '%s' and email = '%s' and l_id = %d";
        query = String.format(query, renter_comment, renter_rating, rent_date, email,l_id);
        System.out.println(query);
        this.st.execute(query);
        return;
    }

    public ResultSet getDataThroughAddress(String home_address) throws SQLException {
        String query = "SELECT * FROM Listing WHERE home_address = '%s'";
        query = String.format(query, home_address);
        return this.st.executeQuery(query);
    }

    public ResultSet CheckBooking(Integer u_id, Integer l_id, String rent_date) throws SQLException {
        String query = "SELECT * from renter natural join rents natural join users natural join listing where u_id = %d and l_id = %d and rent_date = '%s'";
        query = String.format(query, u_id, l_id, rent_date);
        System.out.println(query);
        return this.st.executeQuery(query);
    }

    public void RemoveBooking(String date, String email, String home_address) throws SQLException {
        String query = "DELETE FROM rents  where rent_date = '%s' and email = '%s' and home_address = '%s'";
        query = String.format(query, date, email, home_address);
        System.out.println(query);
        this.st.execute(query);

        return;
    }

    public void AddCancellationRecord(String date, String email, String home_address) throws SQLException {
        String query = "INSERT INTO Cancellation(cancel_date, email, home_address) VALUES ( '%s' , '%s', '%s')";
        query = String.format(query, date, email, home_address);
        this.st.execute(query);
        return;
    }
}

