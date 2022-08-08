import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class bydatebypostalcode extends Endpoint{

    //    {"low": String,
//      "up" : String,
//      "postalcode": String
//    }
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String low = null;
        String up = null;
        String pc = null;
        if (!deserialized.has("low") || !deserialized.has("up") || !deserialized.has("postalcode")) {
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("low")) {
            if (deserialized.get("low").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            low = deserialized.getString("low");
        }
        if (deserialized.has("up")) {
            if (deserialized.get("up").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            up = deserialized.getString("up");
        }
        if (deserialized.has("postalcode")) {
            if (deserialized.get("postalcode").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            pc = deserialized.getString("postalcode");
        }
        ResultSet rs = this.dao.bydatebypc(low, up, pc);
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
