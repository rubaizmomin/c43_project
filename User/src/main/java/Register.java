import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class Register extends Endpoint{
    /**
     * POST /user/register
     * @body name, email, password, occupation, address, sin, dob
     * @return 200, 400, 500
     * Register a user into the system using the given information.
     */
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        // uri checked completely at this point
        // check body:
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String name = null;
        String email = null;
        String password = null;
        String occupation = null;
        String address = null;
        Long sin = null;
        String dob = null;
        if (!deserialized.has("name") || !deserialized.has("email") || !deserialized.has("password") || !deserialized.has("occupation")|| !deserialized.has("address") || !deserialized.has("sin") || !deserialized.has("dob")) {
            this.sendStatus(r, 400);
            return;
        }
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
            email = deserialized.getString("email");
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
            if (deserialized.get("sin").getClass() != Long.class) {
                this.sendStatus(r, 400);
                return;
            }
            sin = deserialized.getLong("sin");
        }
        // if all the variables are still null then there's no variables in request so retrun 400:
        if (name == null && email == null && password == null && address == null && occupation == null && sin == null && dob == null) {
            this.sendStatus(r, 400);
            return;
        }
        //check if there is already an email in the database
        ResultSet rs1;
        boolean resultHasNext;
        try {
            rs1 = this.dao.checkEmail(email);
            resultHasNext = rs1.next();
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
        // checks if there is any user with that email. if yes, conflict error.
        if (resultHasNext) {
            this.sendStatus(r, 409);
            return;
        }
        try {
            this.dao.addUser(name, email, password, address, occupation, sin, dob);
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
        JSONObject json = new JSONObject();
        try{
            ResultSet rs3 = this.dao.getUserfromEmail(email);
            if(rs3.next()){
                json.put("u_id", rs3.getInt("u_id"));
            }
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Cant get users from Uid");
            return;
        }
        this.sendResponse(r, json,200);
        return;
    }
}