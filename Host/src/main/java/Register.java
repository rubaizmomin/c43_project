import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public class Register extends Endpoint{

    @Override
    public void handlePost(HttpExchange r) throws IOException, JSONException, SQLException {
        String body = Utils.convert(r.getRequestBody());
        JSONObject deserialized = new JSONObject(body);
        String email = null;
        if(!deserialized.has("email")){
            this.sendStatus(r, 400);
            return;
        }
        if (deserialized.has("email")) {
            if (deserialized.get("email").getClass() != String.class) {
                this.sendStatus(r, 400);
                return;
            }
            email = deserialized.getString("email");
        }
        try{
            this.dao.addHost(email);
        } catch(Exception e){
            this.sendStatus(r, 500);
            System.out.println("Error with addHost");
            return;
        }
        this.sendStatus(r, 200);
        return;
    }
}
