import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Endpoint{

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String email = null;
        String password = null;
        if (!deserialized.has("email") || !deserialized.has("password")) {
            this.sendStatus(r, 400);
            return;
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
        if (email == null && password == null) {
            this.sendStatus(r, 400);
            return;
        }
        ResultSet rs1;
        boolean resultHasNext;
        try {
            rs1 = this.dao.checkLogin(email, password);
            resultHasNext = rs1.next();
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
        // check if user with given name, email, password exists, return 400 if yes:
        if (!resultHasNext) {
            ResultSet rs2 = this.dao.checkEmail(email);
            resultHasNext = rs2.next();
            if (!resultHasNext) {
                this.sendStatus(r, 404);
                return;
            }
            //Password is incorrect if you reach here with valid email.
            this.sendStatus(r, 401);
        }
        // update db, return 500 if error:
        this.sendStatus(r, 200);
        return;
    }
}