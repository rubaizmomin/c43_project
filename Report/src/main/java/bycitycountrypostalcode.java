import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class bycitycountrypostalcode extends Endpoint{
//    {
//        city:String,
//        country:String,
//        postalcode: String
//    }
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String city = null;
        String country = null;
        String pc = null;
        if (!deserialized.has("city") || !deserialized.has("country") || !deserialized.has("postalcode")) {
            this.sendStatus(r, 400);
            return;
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
        if (deserialized.has("postalcode")) {
            if (deserialized.get("postalcode").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            pc = deserialized.getString("postalcode");
        }
        ResultSet rs = this.dao.bycitycountrypostalcode(city, country, pc);
        ArrayList<JSONObject> listingarr = new ArrayList<>();
        while(rs.next()){
            JSONObject listing = new JSONObject();
            listing.put("home_address", rs.getString("home_address"));
            listing.put("l_id", rs.getInt("l_id"));
            listing.put("listing_type", rs.getString("listing_type"));
            listing.put("postal_code", rs.getString("postal_code"));
            listing.put("city", rs.getString("city"));
            listing.put("country", rs.getString("country"));
            listingarr.add(listing);
        }
        JSONObject data = new JSONObject();
        data.put("data", listingarr);
        this.sendResponse(r, data, 200);
        return;
    }
}
