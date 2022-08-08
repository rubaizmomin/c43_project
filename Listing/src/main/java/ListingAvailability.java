import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListingAvailability extends Endpoint{
    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        Integer l_id = null;
        String home_address = null;
        try{
            l_id = Integer.parseInt(splitUrl[3]);
        } catch (Exception e){
            System.out.println("Cannot parse the uid in addavailable/uid");
            this.sendStatus(r, 500);
            return;
        }

        try{
            ResultSet rs = this.dao.getDataThroughLid(l_id);
            if(!rs.next()){
                this.sendStatus(r, 400);
                System.out.println("Cannot get address from the mentioned l_id");
                return;
            }
            home_address = rs.getString("home_address");
        } catch (SQLException e) {
            this.sendStatus(r, 500);
            System.out.println("Issue with getdatathroughLid in GET listingavailability/:lid");
        }
        try{
            ResultSet rs1 = this.dao.getAvailableDatesFromAddress(home_address);
            //{available_date_data: [{
            //                          available_date: String,
            //                          rental_price: Double
            //                        },
            //                        {
            //                           available_date: String,
            //                            rental_price: Double
            //                         }, ...]
            //}

            ArrayList<JSONObject> listingarr = new ArrayList<>();
            while(rs1.next()){
                JSONObject listing = new JSONObject();
                listing.put("available_date", rs1.getString("available_date"));
                listing.put("rental_price", rs1.getInt("rental_price"));
                listingarr.add(listing);
            }
            JSONObject data = new JSONObject();
            data.put("available_date_data", listingarr);
            this.sendResponse(r, data, 200);
            return;
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Error in getting Data from Availability using home address and parsing");
            return;
        }
    }
}
