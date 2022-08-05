import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddAmenity extends Endpoint {

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        // uri checked completely at this point
        // check body:
        System.out.println("4");
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String amenity_type = null;

        if (!deserialized.has("amenity_type")) {
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("amenity_type")) {
            if (deserialized.get("amenity_type").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            amenity_type = deserialized.getString("amenity_type");
        }
        // if all the variables are still null then there's no variables in request so retrun 400:
        if (amenity_type == null) {
            this.sendStatus(r, 400);
            return;
        }
        //check if there is already an email in the database
        ResultSet rs1;
        boolean resultHasNext;
        try {
            rs1 = this.dao.checkAmenityType(amenity_type);
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
            this.dao.addAmenity(amenity_type);
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
        this.sendStatus(r, 200);
        return;
    }
}