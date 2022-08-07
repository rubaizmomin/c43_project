import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class DeleteUser extends Endpoint {
    /**
     * DELETE /user/deleteUser/:uid
     * @return 200, 400, 404, 500
     * Delete a user.
     */
    @Override
    public void handleDelete(HttpExchange r) throws IOException, JSONException {
        // Check params
        String[] params = r.getRequestURI().toString().split("/");
        if (params.length != 4 || params[3].isEmpty()) {
            this.sendStatus(r, 400);
            return;
        }

        String u_id = params[3];
        String email = null;

        // Check if user is in the database
        try {
            ResultSet rs = this.dao.getUserfromUid(u_id);
            // If user doesn't exist, 404
            if (rs.next()) {
                email = rs.getString("email");
            } else {
                System.out.println("User is not in db");
                this.sendStatus(r, 404);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

        // Check if the user is the host
        try {
            ResultSet rs = this.dao.getHost(email);
            // If user is the host, delete host
            if (rs.next()) {
                this.dao.deleteHost(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

        // Check if the user is the renter
        try {
            ResultSet rs = this.dao.getRenter(email);
            // If user is the renter, delete renter
            if (rs.next()) {
                this.dao.deleteRenter(email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

        // Delete user
        try {
            this.dao.deleteUser(u_id);
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

        // Send response
        JSONObject json = new JSONObject();
        // Check if user is deleted from the database
        try {
            ResultSet rs = this.dao.getUserfromUid(u_id);
            if (rs.next()) {
                System.out.println("Failed to remove user");
                this.sendStatus(r, 400);
                return;
            }
        } catch (Exception e) {
            System.out.println("Can't find users from Uid");
            this.sendStatus(r, 500);
            return;
        }
        this.sendResponse(r, json,200);
    }
}
