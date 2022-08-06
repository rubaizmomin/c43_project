import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RentAvailable extends Endpoint {
    /**
     * GET /rentavailable/:l_id
     * @body empty
     * @return 200, 400, 500
     * Get all available details about l_id
     * This is called when the renter clicks a listing on /rent and the l_id of that listing is passed in the url(rentavailable/:l_id)
     */
    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        Integer l_id = null;
        try{
            l_id = Integer.parseInt(splitUrl[2]);
        } catch (Exception e){
            System.out.println("Cannot parse the uid in rentavailable/:lid");
            this.sendStatus(r, 500);
            return;
        }
        try{
            ResultSet rs1 = this.dao.getAvailableDataFromLid(l_id);
            if(!rs1.next()){
                System.out.println("Cannot find any data about the listing");
                this.sendStatus(r, 404);
                return;
            }
            JSONObject data = new JSONObject();
            ArrayList<JSONObject> avail_data = new ArrayList<>();
            //{     home_address: String,
            //      l_id: Integer,
            //      listing_type: String,
            //      postal_code: String,
            //      city: String,
            //      country: String,
            //      avail_data : [{
            //                      available_date : String,
            //                      rental_price: Double
            //                    },
            //                    ...]
            //}
            //Note: So you have to display the home_address, l_id, listing type, postal, city, country just once on the screen as a text
            //      Then below, there will be a table with available_date and rental_price for that listing through which renters can choose
            try {
                if (rs1.next()) {
                    data.put("home_address", rs1.getString("home_address"));
                    data.put("l_id", rs1.getInt("l_id"));
                    data.put("listing_type", rs1.getString("listing_type"));
                    data.put("postal_code", rs1.getString("postal_code"));
                    data.put("city", rs1.getString("city"));
                    data.put("country", rs1.getString("country"));
                    JSONObject avail = new JSONObject();
                    avail.put("available_date", rs1.getString("available_date"));
                    avail.put("rental_price", Double.parseDouble(rs1.getString("rental_price")));
                    avail_data.add(avail);
                }
                while (rs1.next()) {
                    JSONObject avail = new JSONObject();
                    avail.put("available_date", rs1.getString("available_date"));
                    avail.put("rental_price", Double.parseDouble(rs1.getString("rental_price")));
                    avail_data.add(avail);
                }
                data.put("data", avail_data);
                this.sendResponse(r, data, 200);
                return;
            }catch(Exception e){
                System.out.println("Issue with parsing data in rentavailable");
                this.sendStatus(r, 500);
                return;
            }
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Issue with getting available data from l_d to get rentavailable dates");
        }
    }
}
