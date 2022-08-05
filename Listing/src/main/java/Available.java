import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
        String date = null;
        String price = null;
        String home_address = null;
        LocalDate availdate = null;
        Double rental_price = null;
        if(!deserialized.has("date") && !deserialized.has("rental_price")){
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("available_date")) {
            if (deserialized.get("available_date").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            date = deserialized.getString("available_date");
            //"day/month/year"
            String[] splitdate = date.split("/");
            LocalDate today = null;
            try {
                availdate = LocalDate.of(Integer.parseInt(splitdate[2]), Integer.parseInt(splitdate[1]), Integer.parseInt(splitdate[0]));
                System.out.println(availdate);
                today = LocalDate.now();
                if(availdate.isBefore(today)){
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
        try{
            System.out.println(date);
            ResultSet rs3 = this.dao.CheckDate(date);
            if(!rs3.next()) {
                this.dao.AddDatetoCalendar(date);
            }
        } catch(Exception e){
            System.out.println("Issue with date");
            this.sendStatus(r, 500);
            return;
        }
        try{
            ResultSet rs5 = this.dao.CheckAvailability(home_address, date);
            if(rs5.next()){
                System.out.println("Listing already has the available date. pls patch if you want to change the price");
                this.sendStatus(r, 409);
                return;
            }
        } catch(Exception e){
            System.out.println("Error with checking available date");
            this.sendStatus(r, 500);
            return;
        }
        try{
            this.dao.AddAvailability(home_address, date, rental_price);
            this.sendStatus(r, 200);
            return;
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Something wrong with adding availability.[BOTTOM OF AVAILABLE] HERE");
            return;
        }
    }
}
