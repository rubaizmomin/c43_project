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

    public void addUser(String name, String email, String password, String address, String occupation, Integer sin, String dob) throws SQLException {
        String query = "INSERT INTO users (name, email, password, address, occupation, sin, dob) VALUES ('%s', '%s','%s', '%s', '%s', %d, '%s')";
        query = String.format(query, name, email, password, address, occupation, sin, dob);
        this.st.execute(query);
    }
}