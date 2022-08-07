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
            if(queries[i].contains("amenities")) {
                ArrayList<Integer> alist = new ArrayList<>();
                String[] amenities_query = queries[i].split("=");
                String amenities = amenities_query[1];
                String[] each_amenity = amenities.split("&");

                for (int j = 0; j < each_amenity.length; j++) {
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
                        ResultSet rs2 = this.dao.getAmenityID(decoded_amenity);
                        if (rs2.next()) {
                            a_id = rs2.getInt("a_id");
                            alist.add(a_id);
                            anum++;
                        } else {
                            this.sendStatus(r, 404);
                            System.out.println("the filtered amenity does not exists");
                        }
                    } catch (Exception e) {
                        this.sendStatus(r, 500);
                        System.out.println("The filtered amenity does not exists");
                        return;
                    }
                }
//                for(int k = 0; k < alist.size(); k++) {
//                    if (query.isEmpty()) {
//                        query = query.concat("a_id = " + alist.get(k).toString() + " ");
//                    } else if(query.contains("a_id")) {
//                        query = query.concat(" OR a_id = " + alist.get(k).toString() + " ");
//                    } else {
//                        query = query.concat(" AND a_id = " + alist.get(k).toString() + " ");
//                    }
//                }
                if (query.isEmpty()) {
                    query = query.concat(" a_id IN (");
                } else {
                    query = query.concat(" AND a_id IN (");
                }
                for (int k = 0; k < alist.size() - 1; k++) {
                    query = query.concat(alist.get(k).toString() + ",");
                }
                query = query.concat(alist.get(alist.size() - 1).toString() + ") ");

            }
                //Amenity: and a_id IN (7, 10) group by home_address HAVING COUNT(distinct a_id) = 2
            if(queries[i].contains("price")){
                String[] price_query = queries[i].split("\\$");
                String min_max_maybe = price_query[1];
                String [] min_max_check = min_max_maybe.split("&");
                if(min_max_check.length == 2){
                    String min_price_string = min_max_check[0];
                    String max_price_string = min_max_check[1];
                    Float min_price = Float.parseFloat(min_price_string.split("=")[1]);
                    Float max_price = Float.parseFloat(max_price_string.split("=")[1]);
                    if(query.isEmpty()){
                        query = query.concat(" rental_price >= " + min_price + " and rental_price <= " + max_price + " ");
                    }
                    else {
                        query = query.concat(" AND rental_price >= " + min_price + " and rental_price <= " + max_price + " ");
                    }
                }
                if(min_max_check.length == 1){
                    if(min_max_check[0].contains("min_price")){
                        String min_price_string = min_max_check[0];
                        Float min_price = Float.parseFloat(min_price_string.split("=")[1]);
                        if(query.isEmpty()){
                            query = query.concat(" rental_price >= " + min_price + " ");
                        }
                        else {
                            query = query.concat(" AND rental_price >= " + min_price + " ");
                        }
                    }
                    else if(min_max_check[0].contains("max_price")){
                        String max_price_string = min_max_check[0];
                        Float max_price = Float.parseFloat(max_price_string.split("=")[1]);
                        if(query.isEmpty()){
                            query = query.concat(" rental_price >= " + max_price + " ");
                        }
                        else {
                            query = query.concat(" AND rental_price <= " + max_price + " ");
                        }
                    }
                }
            }
            if(queries[i].contains("coordinates")){
                String[] coordinates_query = queries[i].split("\\$");
                String coordinates_string = coordinates_query[1];
                String [] lat_long_string = coordinates_string.split("&");
                Double lat = Double.parseDouble(lat_long_string[0].split("=")[1]);
                Double lon = Double.parseDouble(lat_long_string[1].split("=")[1]);
                Double distance = Double.parseDouble(lat_long_string[2].split("=")[1]);
                if(query.isEmpty()){
                    query = query.concat(" (POWER(latitude - " + lat + ", 2) + POWER(longitude - " + lon + ", 2)) < " + distance + " ");
                }
                else{
                    query = query.concat(" AND (POWER(latitude - " + lat + ", 2) + POWER(longitude - " + lon + ", 2)) < " + distance + " ");
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
