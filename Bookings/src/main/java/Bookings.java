import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Bookings extends Endpoint{

    /**
     * GET /bookings/u_id
     * @param u_id
     * @return 200, 400, 404, 500
     * Get booking data for the u_id
     */

    // the respons will be:
    //  bookings : [{
//                        rent_date: String,
//                        home_address: String,
    //                    l_id : Integer
//                   },
//                   {
//                        rent_date: String,
//                        home_address: String,
    //                    l_id : Integer
//                   }..]
    //}
    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        Integer u_id = null;
        try {
            u_id = Integer.parseInt(splitUrl[2]);
        } catch (Exception e){
            System.out.println("Cannot parse the uid in getlisting/lid");
            this.sendStatus(r, 500);
            return;
        }
        try{
            ResultSet rs1 = this.dao.GetBookingsFromUid(u_id);
            System.out.println("HERE");
            ArrayList<JSONObject> bookingarr = new ArrayList<>();
            while(rs1.next()){
                //  bookings : [{
//                        rent_date: String,
//                        home_address: String
//                   },
//                   {
//                        rent_date: String,
//                        home_address: String
//                   }..]
                //}
                JSONObject booking = new JSONObject();
                System.out.println(rs1.getString("rent_date") + rs1.getString("home_address"));
                booking.put("rent_date", rs1.getString("rent_date"));
                String home_address = rs1.getString("home_address");
                booking.put("home_address", home_address);
                ResultSet rs20 = this.dao.getDataThroughAddress(home_address);
                if(!rs20.next()){
                    this.sendStatus(r, 404);
                    System.out.println("Cannot get data through address");
                    return;
                }
                Integer l_id = rs20.getInt("l_id");
                booking.put("l_id", l_id);
                bookingarr.add(booking);
            }
            JSONObject data = new JSONObject();
            data.put("bookings", bookingarr);
            this.sendResponse(r, data, 200);
            return;
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Error in getListingfromEmail and parsing");
            return;
        }

    }
}
