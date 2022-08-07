import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class UpdateUser extends Endpoint {
    /**
     * PATCH /user/updateUser/:uid
     * @body name, email, password, occupation, address, sin, dob
     * @return 200, 400, 404, 500
     * Update a user information using the given information.
     */
    @Override
    public void handlePatch(HttpExchange r) throws IOException, JSONException, SQLException {
        // Check params
        String[] params = r.getRequestURI().toString().split("/");
        if (params.length != 4 || params[3].isEmpty()) {
            this.sendStatus(r, 400);
            return;
        }
        String u_id = params[3];

        // Get user information from database
        String name = null;
        String email = null;
        String password = null;
        String address = null;
        String dob = null;
        String occupation = null;
        String sin = null;
        try {
            ResultSet rs = this.dao.getUserfromUid(u_id);
            // If user doesn't exist, 404
            if (rs.next()) {
                name = rs.getString("name");
                email = rs.getString("email");
                password = rs.getString("password");
                address = rs.getString("address");
                dob = rs.getString("dob");
                occupation = rs.getString("occupation");
                sin = rs.getString("sin");
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

        // Check request body
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        if (deserialized.has("name")) {
            if (deserialized.get("name").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            name = deserialized.getString("name");
        }
        if (deserialized.has("email")) {
            if (deserialized.get("email").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            String newEmail = deserialized.getString("email");
            if (!email.equals(newEmail)) {
                // Check if email is unique
                ResultSet rs = this.dao.checkEmail(newEmail);
                // If email is already in database, send 409
                if (rs.next()) {
                    this.sendStatus(r, 409);
                    return;
                }
                email = newEmail;
            }
        }
        if (deserialized.has("password")) {
            if (deserialized.get("password").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            password = deserialized.getString("password");
        }
        if (deserialized.has("address")) {
            if (deserialized.get("address").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            address = deserialized.getString("address");
        }
        if (deserialized.has("dob")) {
            if (deserialized.get("dob").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            dob = deserialized.getString("dob");
            //"day/month/year"
            String[] splitUrl = dob.split("/");
            LocalDate birthdate = null;
            LocalDate today = null;
            try {
                birthdate = LocalDate.of(Integer.parseInt(splitUrl[2]), Integer.parseInt(splitUrl[1]), Integer.parseInt(splitUrl[0]));
                today = LocalDate.now();
            } catch(Exception e)
            {
                e.printStackTrace();
                this.sendStatus(r, 400);
                return;
            }
            Period period = Period.between(birthdate, today);
            if (period.getYears() < 18) {
                this.sendStatus(r, 400);
                return;
            }
        }
        if (deserialized.has("occupation")) {
            if (deserialized.get("occupation").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            occupation = deserialized.getString("occupation");
        }
        if (deserialized.has("sin")) {
            if (deserialized.get("sin").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            String newSin = deserialized.getString("sin");
            if (!sin.equals(newSin)) {
                // Check if sin number is unique
                ResultSet rs = this.dao.checkSin(newSin);
                // If sin number is already in database, send 409
                if (rs.next()) {
                    this.sendStatus(r, 409);
                    return;
                }
                sin = newSin;
            }
        }

        // Update user information
        try {
            this.dao.updateUser(u_id, name, email, password, address, occupation, sin, dob);
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }

        // Send response
        JSONObject json = new JSONObject();
        try {
            ResultSet rs = this.dao.getUserfromUid(u_id);
            if (rs.next()) {
                json.put("u_id", rs.getInt("u_id"));
            }
        } catch (Exception e) {
            System.out.println("Can't get users from Uid");
            this.sendStatus(r, 500);
            return;
        }
        this.sendResponse(r, json,200);
    }
}
