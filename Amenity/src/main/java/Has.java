import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Has extends Endpoint{

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException, InterruptedException {
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        ArrayList<String> amenities = new ArrayList<>();
        String home_address = null;
        JSONArray jarr = new JSONArray();
        if (!deserialized.has("home_address") && !deserialized.has("amenity")) {
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("home_address")) {
            if (deserialized.get("home_address").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            home_address = deserialized.getString("home_address");
            System.out.println("I am printing the home" + home_address);
        }
        if (deserialized.has("amenity")) {
            if (deserialized.get("amenity").getClass() != JSONArray.class) {
                System.out.println("Not the correct type of amenity");
                this.sendStatus(r, 400);
                return;
            }
            jarr = deserialized.getJSONArray("amenity");
            for(int i = 0; i<jarr.length(); i++){
                amenities.add(jarr.get(i).toString());
                System.out.println("I am printing the home" + amenities.get(i));
            }
        }
        for(int i =0; i < amenities.size(); i++){
            System.out.println("I am now sending request to amenity/add");

            try {
                ResultSet rs1;
                boolean resultHasNext;
                try {
                    System.out.println("Checking amenity type");
                    rs1 = this.dao.checkAmenityType(amenities.get(i));
                    resultHasNext = rs1.next();
                } catch (SQLException e) {
                    e.printStackTrace();
                    this.sendStatus(r, 500);
                    return;
                }
                // checks if there is any user with that email. if yes, conflict error.
                if (resultHasNext) {
                    System.out.println("Amenity Already Added");
                }
                try {
                    System.out.println("New Amenity added" + amenities.get(i));
                    this.dao.addAmenity(amenities.get(i));
                } catch (SQLException e) {
                    e.printStackTrace();
                    this.sendStatus(r, 500);
                    return;
                }
            }catch (Exception e){
                this.sendStatus(r, 500);
                System.out.println("Cannot send amenity to check if amenity exists");
                return;
            }
            try {
                ResultSet rs1 = this.dao.getAmenityID(amenities.get(i));
                System.out.println("1");
                Integer a_id;
                if(rs1.next()) {
                    a_id = rs1.getInt("a_id");
                } else{
                    this.sendStatus(r, 500);
                    System.out.println("No more rows");
                    return;
                }
                System.out.println("2");
                this.dao.addAmenitytoListing(home_address, a_id);
                System.out.println("3");
            }catch (Exception e){
                this.sendStatus(r, 500);
                System.out.println("Cannot add amenity to listing");
                return;
            }
        }
        this.sendStatus(r, 200);
        return;
    }
}
