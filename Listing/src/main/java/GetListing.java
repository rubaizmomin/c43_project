import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GetListing extends Endpoint {
    /**
     * GET /listing/getlisting/:l_id
     * @param l_id
     * @return 200, 400, 404, 500
     * GEt all listings owned by uid
     */
    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        Integer l_id = null;
        try {
            l_id = Integer.parseInt(splitUrl[3]);
        } catch (Exception e){
            System.out.println("Cannot parse the uid in getlisting/lid");
            this.sendStatus(r, 500);
            return;
        }
        try {
            ResultSet rs = this.dao.getListingfromId(l_id);
            JSONObject listing = new JSONObject();
            if (rs.next()) {
                listing.put("home_address", rs.getString("home_address"));
                listing.put("listing_type", rs.getString("listing_type"));
                listing.put("postal_code", rs.getString("postal_code"));
                listing.put("city", rs.getString("city"));
                listing.put("country", rs.getString("country"));
            } else {
                System.out.println("The listing doesn't exist");
                this.sendStatus(r, 404);
                return;
            }
            JSONObject data = new JSONObject();
            data.put("data", listing);
            this.sendResponse(r, data, 200);
            return;
        }catch (Exception e) {
            this.sendStatus(r, 500);
            System.out.println("Error in getListingfromId and parsing");
            return;
        }
    }
}
