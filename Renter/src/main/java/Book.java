import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Book extends Endpoint{
//    {
    //    u_id: String,
//        payment_info : 19284984938493
//        booking_date: [
//                          date: String,
//                          date: String...
//                        ]
//    }rent/book/l_id
    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException {
        String[] splitUrl = r.getRequestURI().getPath().split("/");
        Integer l_id = null;
        String home_address = null;
        try{
            l_id = Integer.parseInt(splitUrl[3]);
            System.out.println(l_id);
        } catch (Exception e){
            System.out.println("Cannot parse the uid in rentavailable/:lid");
            this.sendStatus(r, 500);
            return;
        }
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String payment_info = null;
        ArrayList<String> booking_dates = new ArrayList<>();
        JSONArray jarr = new JSONArray();
        Integer u_id = null;
        String email = null;
        if (!deserialized.has("u_id") || !deserialized.has("payment_info") || !deserialized.has("booking_date")) {
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("u_id")) {
            if (deserialized.get("u_id").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            u_id = Integer.parseInt(deserialized.getString("u_id"));
        }
        if (deserialized.has("payment_info")) {
            if (deserialized.get("payment_info").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            payment_info = deserialized.getString("payment_info");
        }
        if (deserialized.has("booking_date")) {
            if (deserialized.get("booking_date").getClass() != JSONArray.class) {
                this.sendStatus(r, 400);
                return;
            }
            jarr = deserialized.getJSONArray("booking_date");
            for(int i = 0; i<jarr.length(); i++){
                System.out.println(jarr.get(i).toString());
                booking_dates.add(jarr.get(i).toString());
            }
        }
        System.out.println(u_id + payment_info);
        try {
            ResultSet rs = this.dao.getDataThroughLid(l_id);
            if (!rs.next()) {
                this.sendStatus(r, 404);
                System.out.println("Cannot get home_address of the potential booking");
                return;
            }
            home_address = rs.getString("home_address");
            ResultSet rs5 = this.dao.getEmailfromUid(u_id);
            if (!rs5.next()) {
                this.sendStatus(r, 404);
                System.out.println("Cannot get email of the potential booking");
                return;
            }
            System.out.println("HEREEE");
            email = rs5.getString("email");
            System.out.println("HEREEE");

        }catch(Exception e){
            this.sendStatus(r, 500);
            System.out.println("Some error in getting homeaddress from Lid in book");
            return;
        }
        for(int i = 0; i < booking_dates.size(); i++){
            System.out.println(booking_dates.get(i) + 1);
            try{
                this.dao.DeleteAvailability(booking_dates.get(i), home_address);
            }catch (Exception e){
                System.out.println("Cannot delete the date froma available because of error");
                this.sendStatus(r, 500);
                return;
            }
            System.out.println(booking_dates.get(i) + 2);
            try{
                ResultSet rs3 = this.dao.CheckRenter(email);
                System.out.println(booking_dates.get(i) + 4);
                if(rs3.next()){
                    System.out.println(booking_dates.get(i) + 5);
                    System.out.println("Renter already added. Checking payment_info");
                    String payment_info_db = rs3.getString("payment_info");
                    if(!payment_info_db.equals(payment_info)){
                        System.out.println("The payment info is not update so it will update now");
                        this.dao.UpdatePaymentInfo(email, payment_info_db);
                    }
                }
                else {
                    try {
                        this.dao.AddRenter(email, payment_info);
                    } catch (Exception e) {
                        this.sendStatus(r, 500);
                        System.out.println("Error in adding renter");
                        return;
                    }
                }
                System.out.println(booking_dates.get(i) + 3);
            }catch (Exception e){
                System.out.println("Error in updating payment_info");
                this.sendStatus(r, 500);
                return;
            }
            try{
                this.dao.AddBooking(booking_dates.get(i), email, home_address);
            }catch (Exception e){
                this.sendStatus(r, 500);
                System.out.println("Error in add booking");
                return;
            }
        }

        this.sendStatus(r, 200);
        return;

    }
}

