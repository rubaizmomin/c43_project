import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CancelBooking extends Endpoint{
    //bookings/cancelbooking/:l_id?u_id=:u_id
    //POST REQUEST.
    //Body:{
    //          cancel_date : ["2022-10-15", "2022-10-16"]
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
        JSONArray jarr = null;
        ArrayList<String> datearray = new ArrayList<>();
        String email = null;
        String home_address = null;
        if (!deserialized.has("cancel_date")){
            this.sendStatus(r, 500);
            return;
        }
            if (deserialized.has("cancel_date")) {
                if (deserialized.get("cancel_date").getClass() != JSONArray.class) {
                    System.out.println("Not the correct type");
                    this.sendStatus(r, 400);
                    return;
                }
                jarr = deserialized.getJSONArray("cancel_date");
                for (int i = 0; i < jarr.length(); i++) {
                    datearray.add(jarr.get(i).toString());
                }
            }
        ResultSet rs10 = this.dao.getEmailfromUid(u_id);
        if (!rs10.next()) {
            this.sendStatus(r, 400);
            System.out.println("The email is not there");
            return;
        }
        email = rs10.getString("email");

        ResultSet rs11 = this.dao.getDataThroughLid(l_id);
        if (!rs11.next()) {
            this.sendStatus(r, 400);
            System.out.println("The lid is not there");
            return;
        }
        home_address = rs11.getString("home_address");
        for (int i = 0; i < datearray.size(); i++) {
            ResultSet rs = this.dao.CheckBooking(u_id, l_id, datearray.get(i));
            if (!rs.next()) {
                this.sendStatus(r, 400);
                System.out.println("The date is not booked on your u_id");
                return;
            }
            try {
                this.dao.RemoveBooking(datearray.get(i), email, home_address);
                this.dao.AddCancellationRecord(datearray.get(i), email, home_address);
            } catch (Exception e) {
                this.sendStatus(r, 500);
                System.out.println("Error caught at removebooking");
                return;
            }
        }
        this.sendStatus(r, 200);
        return;
    }
}
