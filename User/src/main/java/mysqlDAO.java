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
    public ResultSet getUserfromUid(String u_id) throws SQLException {
        String query = "SELECT * FROM Users WHERE u_id = '%s'";
        query = String.format(query, u_id);
        return this.st.executeQuery(query);
    }

    public ResultSet getUserfromEmail(String email) throws SQLException {
        String query = "SELECT * FROM Users WHERE email = '%s'";
        query = String.format(query, email);
        return this.st.executeQuery(query);
    }
    public ResultSet checkEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = '%s'";
        query = String.format(query, email);
        return this.st.executeQuery(query);
    }
    public ResultSet checkLogin(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = '%s' AND password = '%s'";
        query = String.format(query, email, password);
        return this.st.executeQuery(query);
    }

    public ResultSet checkSin(String sin) throws SQLException {
        String query = "SELECT * FROM users WHERE sin = '%s'";
        query = String.format(query, sin);
        return this.st.executeQuery(query);
    }

    public void addUser(String name, String email, String password, String address, String occupation, String sin, String dob) throws SQLException {
        String query = "INSERT INTO users (name, email, password, address, occupation, sin, dob) VALUES ('%s', '%s','%s', '%s', '%s', %s, '%s')";
        query = String.format(query, name, email, password, address, occupation, sin, dob);
        this.st.execute(query);
    }

    public void updateUser(String u_id, String name, String email, String password, String address, String occupation, String sin, String dob) throws SQLException {
        String query = "UPDATE users SET name='%s', email='%s', password='%s', address='%s', occupation='%s', sin='%s', dob='%s' WHERE u_id = '%s'";
        query = String.format(query, name, email, password, address, occupation, sin, dob, u_id);
        this.st.execute(query);
    }

    public void deleteUser(String u_id) throws SQLException {
        String query = "DELETE FROM users WHERE u_id = '%s'";
        query = String.format(query, u_id);
        this.st.execute(query);
    }

    public ResultSet getHost(String email) throws SQLException {
        String query = "SELECT * FROM host WHERE email = '%s'";
        query = String.format(query, email);
        return this.st.executeQuery(query);
    }

    public ResultSet getRenter(String email) throws SQLException {
        String query = "SELECT * FROM renter WHERE email = '%s'";
        query = String.format(query, email);
        return this.st.executeQuery(query);
    }

    public void deleteHost(String email) throws SQLException {
        String query ="DELETE FROM host WHERE email = '%s'";
        query = String.format(query, email);
        this.st.execute(query);
    }

    public void deleteRenter(String email) throws SQLException {
        String query ="DELETE FROM renter WHERE email = '%s'";
        query = String.format(query, email);
        this.st.execute(query);
    }
}