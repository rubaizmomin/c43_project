import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
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
        ResultSet rs1;
        Boolean hasresultset;
        try{
            rs1 = this.dao.checkEmail(email);
            hasresultset = rs1.next();
        } catch(Exception e){
            this.sendStatus(r, 500);
            System.out.println("Error with addHost");
            return;
        }
        if(hasresultset)
        {
            this.sendStatus(r, 200);
            System.out.println("Host already Exists. No need to add again");
            return;
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
