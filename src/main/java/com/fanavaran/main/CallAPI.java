package com.fanavaran.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CallAPI {

    public static String GetUserRequest(int userId) throws IOException {
        URL urlForGetRequest = new URL("https://jsonplaceholder.typicode.com/users/" + String.valueOf(userId));
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();
    
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            JSONObject obj = new JSONObject(response.toString());
            String lat = obj.getJSONObject("address").getJSONObject("geo").get("lat").toString();
            return lat;
        } else {
            return "failed";
        }
    }
}