import com.mysql.cj.protocol.Resultset;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetAmenity extends Endpoint {
    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        try {
            ResultSet rs = this.dao.getAllAmenity();
            System.out.println("HERE");
            ArrayList<JSONObject> amenityArr = new ArrayList<>();
            while (rs.next()) {
                JSONObject listing = new JSONObject();
                listing.put("a_id", rs.getInt("a_id"));
                listing.put("amenity_type", rs.getString("amenity_type"));
                amenityArr.add(listing);
            }
            JSONObject data = new JSONObject();
            data.put("data", amenityArr);
            this.sendResponse(r, data, 200);
            return;
        } catch (Exception e) {
            System.out.println("Can't show all amenities by server error");
            this.sendStatus(r, 500);
            return;
        }
    }
}
