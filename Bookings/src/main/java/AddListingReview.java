import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddListingReview extends Endpoint{


    //bookings/addreview/:l_id?u_id=u_id
    //POST REQUEST.
    //Body:{
    //          renter_comment: String
    //          render_rating: Integer
    //          rent_date : String
    //}
    @Override
    public void handlePost(HttpExchange r) throws JSONException, IOException, SQLException {
        String uri = r.getRequestURI().toString();
        String[] splitUrl = uri.split("/");
        String[] uriPartsP2 = splitUrl[3].split("\\?");
        Integer l_id = Integer.parseInt(uriPartsP2[0]);
        String[] uriPartsP3queryStr = uriPartsP2[1].split("=");
        Integer u_id = Integer.parseInt(uriPartsP3queryStr[1]);
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String renter_comment = null;

        Integer renter_rating = null;

        String rent_date = null;
        if(!deserialized.has("rent_date") || !deserialized.has("renter_comment") || !deserialized.has("renter_rating")){
            System.out.println("HERE");
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("rent_date")) {
            if (deserialized.get("rent_date").getClass() != String.class) {
                System.out.println("Not the correct type");
                this.sendStatus(r, 400);
                return;
            }
            //"year-month-date"
            rent_date = deserialized.getString("rent_date");

        }
        if (deserialized.has("renter_comment")) {
            if (deserialized.get("renter_comment").getClass() != String.class) {
                System.out.println("Not the correct type");
                this.sendStatus(r, 400);
                return;
            }
            //"year-month-date"
            renter_comment = deserialized.getString("renter_comment");

        }
        if (deserialized.has("renter_rating")) {
            if (deserialized.get("renter_rating").getClass() != Integer.class) {
                System.out.println("Not the correct type");
                this.sendStatus(r, 400);
                return;
            }
            //"year-month-date"
            renter_rating = deserialized.getInt("renter_rating");
        }
        ResultSet rs = this.dao.getEmailfromUid(u_id);
        if(!rs.next()){
            this.sendStatus(r, 400);
            System.out.println("Cannot get user emailfrom uid in addlistingreview");
            return;
        }
        String email = rs.getString("email");
        try{
            System.out.println(renter_comment + renter_rating);
            this.dao.UpdateRenterReview(email, l_id, renter_comment, renter_rating,rent_date);
            this.sendStatus(r, 200);
            return;
        }catch (Exception e){
            this.sendStatus(r, 500);
            System.out.println("Issue with updating review in bookings");
            return;
        }
    }

}
