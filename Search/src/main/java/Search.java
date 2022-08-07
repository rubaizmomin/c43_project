import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Search extends Endpoint{

    @Override
    public void handleGet(HttpExchange r) throws JSONException, IOException, SQLException {
        String [] queries = r.getRequestURI().toString().split("\\?");
        String query = "";
        Integer anum = 0;
        for(int i = 0; i < queries.length; i++){
            System.out.println(queries[i]);
        }
        for(int i = 0; i < queries.length; i++){
            if(queries[i].contains("home_address")) {
                String[] home_address_query = queries[i].split("=");
                if (home_address_query.length == 2) {
                    String encoded_address = home_address_query[1];
                    String decoded_address = null;
                    try {
                        decoded_address = URLDecoder.decode(encoded_address, "UTF-8");
                    } catch (Exception e) {
                        System.out.println("Cannot convert UTF of home_address");
                        this.sendStatus(r, 500);
                        return;
                    }
                    if (query.isEmpty()) {
                        query = query.concat("(home_address LIKE '%" + decoded_address + "%' OR " + "'" + decoded_address + "') ");
                    } else{
                        query = query.concat(" AND (home_address LIKE '%" + decoded_address + "%' OR " + "'" + decoded_address + "') ");
                    }
                }
            }
            if(queries[i].contains("amenities")){
                ArrayList<Integer> alist = new ArrayList<>();
                String [] amenities_query = queries[i].split("=");
                String amenities = amenities_query[1];
                String [] each_amenity = amenities.split("&");
                for(int j = 0; j < each_amenity.length; j++){
                    String encoded_amenity = each_amenity[j];
                    String decoded_amenity = null;
                    try {
                        decoded_amenity = URLDecoder.decode(encoded_amenity, "UTF-8");
                    } catch (Exception e) {
                        System.out.println("Cannot convert UTF of amenity");
                        this.sendStatus(r, 500);
                        return;
                    }
                    Integer a_id = null;
                    try {
                        System.out.println(decoded_amenity);
                        ResultSet rs2 = this.dao.getAmenityID(decoded_amenity);
                        if(rs2.next()) {
                            a_id = rs2.getInt("a_id");
                            alist.add(a_id);
                            anum++;
                        }
                        else{
                            this.sendStatus(r, 404);
                            System.out.println("the filtered amenity does not exists");
                        }
                    }catch (Exception e){
                        this.sendStatus(r, 500);
                        System.out.println("The filtered amenity does not exists");
                        return;
                    }
                }
                for(int k = 0; k < alist.size(); k++) {
                    if (query.isEmpty()) {
                        query = query.concat("a_id = " + alist.get(k).toString() + " ");
                    } else if(query.contains("a_id")) {
                        query = query.concat(" OR a_id = " + alist.get(k).toString() + " ");
                    } else {
                        query = query.concat(" AND a_id = " + alist.get(k).toString() + " ");
                    }
                }
            }
        }
        try{
            ResultSet rs1 = null;
            query = query.concat("GROUP BY home_address ");
            if(query.isEmpty()) {
                rs1 = this.dao.filterwithoutquery();
            }
            else if(!query.isEmpty()){
                if(query.contains("a_id")) {
                    query = query.concat(" HAVING COUNT(DISTINCT a_id) = " + anum);
                }

                rs1 = this.dao.filterwithquery(query);
            }
            ArrayList<JSONObject> listingarr = new ArrayList<>();
            while(rs1.next()) {
                JSONObject listing = new JSONObject();
                listing.put("home_address", rs1.getString("home_address"));
                listing.put("l_id", rs1.getInt("l_id"));
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
        } catch (SQLException e) {
            System.out.println("Error with filter");
        }
    }
}
