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
            System.out.println("I am here and I am getting this" + amenity_type);
            if(!amenity_type.equals("Wifi") && !amenity_type.equals("Kitchen") && !amenity_type.equals("Washer") && !amenity_type.equals("Dryer") && !amenity_type.equals("Air conditioning"))
            {
                this.sendStatus(r, 400);
                System.out.println("Wrong Amenity");
                return;
            }
        }
        //check if there is already an email in the database
        ResultSet rs1;
        boolean resultHasNext;
        try {
            System.out.println("Checking amenity type");
            rs1 = this.dao.checkAmenityType(amenity_type);
            resultHasNext = rs1.next();
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
        // checks if there is any user with that email. if yes, conflict error.
        if (resultHasNext) {
            this.sendStatus(r, 200);
            System.out.println("Amenity Already Added");
            return;
        }
        try {
            System.out.println("New Amenity added" + amenity_type);
            this.dao.addAmenity(amenity_type);
            this.sendStatus(r, 200);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            this.sendStatus(r, 500);
            return;
        }
    }
}