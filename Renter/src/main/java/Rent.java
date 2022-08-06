import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Rent extends Endpoint{

    /**
     * GET /rent
     * @body empty
     * @return 200, 400, 500
     * Get all available listings
     */
    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException {
        try{
            ResultSet rs1 = this.dao.GetAvailableListings();
            ArrayList<JSONObject> listingarr = new ArrayList<>();
            //{data : [{
            //          home_address: String,
            //          l_id: Integer,
            //          listing_type: String,
            //          postal_code: String,
            //          city: String,
            //          country: String
            //          },
            //          ...]}
            while(rs1.next()){
                JSONObject listing = new JSONObject();
                listing.put("home_address", rs1.getString("home_address"));
                listing.put("l_id", rs1.getInt("l_id"));
                listing.put("listing_type", rs1.getString("listing_type"));
                listing.put("postal_code", rs1.getString("postal_code"));
                listing.put("city", rs1.getString("city"));
                listing.put("country", rs1.getString("country"));
                listingarr.add(listing);
            }
            JSONObject data = new JSONObject();
            data.put("data", listingarr);
            this.sendResponse(r, data, 200);
            return;
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Cannot show all listings error in rent");
            return;
        }

    }
}
