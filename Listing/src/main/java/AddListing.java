import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddListing extends Endpoint{
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String listing_type = null;
        String postal_code = null;
        String home_address = null;
        String city = null;
        String country = null;
        String lat = null;
        String lon = null;
        Integer u_id = null;
        String email = null;
        if (!deserialized.has("u_id")|| !deserialized.has("listing_type") || !deserialized.has("postal_code")|| !deserialized.has("home_address") || !deserialized.has("city")|| !deserialized.has("country")|| !deserialized.has("latitude") || !deserialized.has("longitude")) {
            System.out.println("Not all params");
            this.sendStatus(r, 400);
            return;

        }
        if (deserialized.has("u_id")) {
            if (deserialized.get("u_id").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            try{
                u_id = Integer.parseInt(deserialized.getString("u_id"));
            }catch (Exception e){
                System.out.println("Cannot parse u_id. not a string");
                this.sendStatus(r, 400);
                return;
            }

        }
        if (deserialized.has("listing_type")) {
            if (deserialized.get("listing_type").getClass() != String.class) {
                System.out.println("listing_type");
                this.sendStatus(r, 400);
                return;
            }
            listing_type = deserialized.getString("listing_type");
            if(!listing_type.equals("House") && !listing_type.equals("Apartment") && !listing_type.equals("Guesthouse") && !listing_type.equals("Hotel"))
            {
                this.sendStatus(r, 400);
                System.out.println("Wrong listing type");
                return;
            }
        }
        if (deserialized.has("postal_code")) {
            if (deserialized.get("postal_code").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            postal_code = deserialized.getString("postal_code");
            if(postal_code.length() != 6)
            {
                this.sendStatus(r, 400);
                System.out.println("Wrong postal format");
                return;
            }
        }
        if (deserialized.has("home_address")) {
            if (deserialized.get("home_address").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            home_address = deserialized.getString("home_address");
        }
        if (deserialized.has("city")) {
            if (deserialized.get("city").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            city = deserialized.getString("city");
        }
        if (deserialized.has("country")) {
            if (deserialized.get("country").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            country = deserialized.getString("country");
        }
        if (deserialized.has("latitude")) {
            if (deserialized.get("latitude").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            lat = deserialized.getString("latitude");
        }
        if (deserialized.has("longitude")) {
            if (deserialized.get("longitude").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            lon = deserialized.getString("longitude");
        }
        Float lati, longi;
        try {
            lati = Float.parseFloat(lat);
            longi = Float.parseFloat(lon);
        } catch(Exception e) {
            System.out.println("Wrong latitude and Longitude format");
            this.sendStatus(r, 500);
            return;
        }
        Boolean resulthasnext = false;
        try{
            ResultSet rs6 = this.dao.getUserfromUid(u_id);
            if(!rs6.next()){
                this.sendStatus(r, 400);
                System.out.println("The user is not in the database");
                return;
            }
        }catch (Exception e){
            System.out.println("Cant check uid");
            this.sendStatus(r, 500);
            return;
        }
        try{
            ResultSet rs1 = this.dao.getDataThroughAddress(home_address);
            resulthasnext = rs1.next();
        }catch (Exception e)
        {
            this.sendStatus(r, 500);
            System.out.println("error in checkAddress");
            return;
        }
        if(resulthasnext){
            this.sendStatus(r, 409);
            System.out.println("Address exists");
            return;
        }
        System.out.println("I am here too in addListing");
        try{

            this.dao.addlisting(listing_type, postal_code, home_address, city, country, lati, longi);
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("error in addListing");
            return;
        }

        try{
            String uri = "/host/register";
            String url = "http://localhost:8003";
            JSONObject hostjson = new JSONObject();
            ResultSet emaildata = this.dao.getEmailfromUid(u_id);
            if(emaildata.next()) {
                email = emaildata.getString("email");
                hostjson.put("email", email);
            }
            HttpResponse<String> s2 = this.sendRequest("POST", url, uri, hostjson.toString());
            if(s2.statusCode() != 200)
            {
                this.sendStatus(r, s2.statusCode());
                return;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try{
            this.dao.addlistingtoOwns(email, home_address);
        } catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("error in addlistingtoOwns");
            return;
        }
        this.sendStatus(r, 200);
    }
}
