package com.conferencebookingsystem.API;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AddFoodbeverage {

    AsyncTask<String, Void, String> asyncAddFoodbeverage;
    String token;
    int conferenceRoomAvailabilityNumber, foodBeverageNumber;
    String timeToServe;
    ArrayList<Integer> selectedFoodBeverageIDs;

    public AddFoodbeverage(String token, int conferenceRoomAvailabilityNumber, ArrayList<Integer> selectedFoodBeverageIDs, String timeToServe) {
        this.token = token;
        this.conferenceRoomAvailabilityNumber = conferenceRoomAvailabilityNumber;
        this.timeToServe = timeToServe;
        this.selectedFoodBeverageIDs = selectedFoodBeverageIDs;
//        System.out.println("AddFoodBeverage class: selected foodbeverage size - " + this.selectedFoodBeverageIDs.size());
//        System.out.println("AddFoodBeverage class: selected foodbeverage list - " + this.selectedFoodBeverageIDs);
    }

    public void startAddFoodbeverage(){
        asyncAddFoodbeverage = new RestConnectionAddFoodbeverage();
        asyncAddFoodbeverage.execute("https://dev-be.timetomeet.se/service/rest/bookingfoodbeverage/add/", jsonParam());
    }

    private String jsonParam(){
        String jsonParam = "{" +
                "    \"conferenceRoomAvailability\": " + conferenceRoomAvailabilityNumber + "," +
                "    \"foodBeverage\":" + selectedFoodBeverageIDs.get(0) + "," +
                "    \"amount\":" + 1 + "," +
                "    \"comment\":\"test test test\","  +
                "    \"timeToServe\": \"" + timeToServe + "\""+
                "}";
        System.out.println("The jsonparam: " + jsonParam);
        return jsonParam;
    }

    class RestConnectionAddFoodbeverage extends AsyncTask<String, Void, String> {
        private String responseContent;
        private String Error = null;
        private URL url;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... requestData) {
            BufferedReader reader=null;

            try {
                url = new URL(requestData[0]);
                System.out.println("The URL: " + url);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept","application/json");
                connection.setRequestProperty("Authorization","Token " + token);
                connection.setDoOutput(true); // True för POST, PUT. False för GET
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(requestData[1]); // { "username": ....
                wr.flush();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    sb.append(line + "  \n");
                }
                responseContent = sb.toString();
            }
            catch(Exception ex) {
                Error = ex.getMessage();
                System.out.println("The error message is: " + Error);
            }
            finally {
                try {
                    reader.close();
                }
                catch(Exception ex) {

                }
            }

            return responseContent;
        }

        protected void onPostExecute(final String result) {
            System.out.println("AddFoodBeverage class - response content received from API " + responseContent);

        }
    }

}
