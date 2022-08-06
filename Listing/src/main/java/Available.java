import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static com.mysql.cj.MysqlType.JSON;


public class Available extends Endpoint{

    /**
     * POST /listing/addavailable/:uid?l_id=:listingID
     * @param u_id, l_id
     * @return 200, 400, 404, 500
     * Add availability to a listing
     */

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        String[] uriPartsP2 = splitUrl[3].split("\\?");
        Integer u_id = Integer.parseInt(uriPartsP2[0]);
        String[] uriPartsP3queryStr = uriPartsP2[1].split("=");
        Integer l_id = Integer.parseInt(uriPartsP3queryStr[1]);
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        JSONArray jarr = null;
        ArrayList<String> datearray = new ArrayList<>();
        String price = null;
        String home_address = null;
        LocalDate availdate = null;
        Double rental_price = null;
        if(!deserialized.has("available_date") && !deserialized.has("rental_price")){
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("available_date")) {
            System.out.println(deserialized.get("available_date").getClass());
            if (deserialized.get("available_date").getClass() != JSONArray.class) {
                System.out.println("Not the correct type");
                this.sendStatus(r, 400);
                return;
            }
            jarr = deserialized.getJSONArray("available_date");
            for(int i = 0; i<jarr.length(); i++){
                datearray.add(jarr.get(i).toString());
            }
            //"day/month/year"
            for(int i = 0; i < datearray.size(); i++) {
                String[] splitdate = datearray.get(i).split("/");
                LocalDate today = null;
                try {
                    availdate = LocalDate.of(Integer.parseInt(splitdate[0]), Integer.parseInt(splitdate[1]), Integer.parseInt(splitdate[2]));
                    today = LocalDate.now();
                    if (availdate.isBefore(today)) {
                        System.out.println("Available date is before today");
                        this.sendStatus(r, 400);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.sendStatus(r, 400);
                    return;
                }
            }
        }
        if (deserialized.has("rental_price")) {
            if (deserialized.get("rental_price").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            price = deserialized.getString("rental_price");
            try{
                rental_price = Double.parseDouble(price);
            }catch (Exception e){
                System.out.println("Cannot parse double");
                this.sendStatus(r, 500);
                return;
            }
        }
        String email = null;
        try{
            ResultSet rs1 = this.dao.getEmailfromUid(u_id);
            if(!rs1.next()){
                this.sendStatus(r, 404);
                System.out.println("Cannot find associated email with l_id");
                return;
            }
            email = rs1.getString("email");
        }catch (Exception e) {
            System.out.println("Some error in getEmailfromUid");
            this.sendStatus(r, 500);
            return;
        }
        try{
            ResultSet rs = this.dao.checkListingAuthorization(email, l_id);
            if(!rs.next()){
                this.sendStatus(r, 404);
                System.out.println("The Host does not own the Listing");
                return;
            }
        } catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Issue with checkListingAuthorizxation");
            return;
        }
        try{
            ResultSet rs1 = this.dao.getDataThroughLid(l_id);
            if(!rs1.next()){
                this.sendStatus(r, 404);
                System.out.println("Cannot find Listing using the L_id");
                return;
            }
            home_address = rs1.getString("home_address");
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Issue with getDataThroughLid");
            return;
        }
        for(int i = 0; i < datearray.size(); i++) {
            System.out.println(datearray.get(i));
            try {
                ResultSet rs3 = this.dao.CheckDate(datearray.get(i));
                if (!rs3.next()) {
                    System.out.println("I am here so if you get issue with date, it is with addDatetoCalendar");
                    this.dao.AddDatetoCalendar(datearray.get(i));
                }
            } catch (Exception e) {
                System.out.println("Issue with date");
                this.sendStatus(r, 500);
                return;
            }
            try {
                ResultSet rs5 = this.dao.CheckAvailability(home_address, datearray.get(i));
                if (rs5.next()) {
                    System.out.println("Listing already has the available date. pls patch if you want to change the price");
                    System.out.println("This date will not be added");
                    continue;
                }
            } catch (Exception e) {
                System.out.println("Error with checking available date");
                this.sendStatus(r, 500);
                return;
            }
            try{
                this.dao.AddAvailability(home_address, datearray.get(i), rental_price);
            }catch (Exception e){
                this.sendStatus(r, 500);
                System.out.println("Something wrong with adding availability.[BOTTOM OF AVAILABLE] HERE");
                return;
            }
        }
        this.sendStatus(r, 200);
        return;
    }

    /**
     * GET /listing/addavailable/:u_id
     * @param u_id
     * @return 200, 400, 404, 500
     * GEt all listings owned by uid
     */
    @Override
    public void handleGet(HttpExchange r) throws IOException, JSONException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        Integer u_id = null;
        try{
            u_id = Integer.parseInt(splitUrl[3]);
        } catch (Exception e){
            System.out.println("Cannot parse the uid in addavailable/uid");
            this.sendStatus(r, 500);
            return;
        }
        String email = null;
        try{
            ResultSet rs = this.dao.getUserfromUid(u_id);
            if(!rs.next()){
                this.sendStatus(r, 400);
                System.out.println("Cannot get User from the mentioned Uid");
                return;
            }
            email = rs.getString("email");
        } catch (SQLException e) {
            this.sendStatus(r, 500);
            System.out.println("Issue with GetUserfromUid in GET addavailable/:uid");
        }
        try{
            ResultSet rs1 = this.dao.getListingfromEmail(email);
            //{data : [{home_address: String, l_id: Integer, listing_type: String, postal_code: String, city: String, country: String}, ...]}
            ArrayList<JSONObject> listingarr = new ArrayList<>();
            while(rs1.next()){
                JSONObject listing = new JSONObject();
                listing.put("home_address", rs1.getString("home_address"));
                listing.put("l_id", rs1.getString("l_id"));
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
            System.out.println("Error in getListingfromEmail and parsing");
            return;
        }
    }
}
