package com.padshift.sonic.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ruzieljonm on 24/07/2018.
 */
@Controller
public class YoutubeAPIController {

    @Autowired
    UserController userController;

    @RequestMapping("/youtubeapi")
    public String runYoutubeAPI(){

        String nextpagetoken = null;
        String urlwithpagetoken = "https://www.googleapis.com/youtube/v3/search?pageToken="+nextpagetoken+"&part=snippet&maxResults=50&order=viewCount&q=music+video&type=video&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&order=viewCount&q=music+video&type=video&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";

        int cnt =0;
        sendRequest(url,nextpagetoken,cnt);



        return "FetchYoutubeAPI";
    }


    public void sendRequest(String requesturl, String nextpagetoken, int cnt){


        cnt++;
        if(cnt<25) {
            try {

                URL obj = new URL(requesturl);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                int responseCode = con.getResponseCode();

                System.out.println("\nSending'Get' request to URL : " + requesturl);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream())
                );


                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                System.out.println(response.toString());

                JSONObject myresponse = null;
                try {
                    myresponse = new JSONObject(response.toString());
                    System.out.println(myresponse);

                    if(myresponse.has("nextPageToken")){

                        nextpagetoken = myresponse.getString("nextPageToken");

                        JSONArray videos = new JSONArray(myresponse.getJSONArray("items").toString());
                        for (int i = 0; i < videos.length(); i++) {
                            JSONObject vid = videos.getJSONObject(i);

                            JSONObject vidId = vid.getJSONObject("id");
                            JSONObject vidTitle = vid.getJSONObject("snippet");
                            JSONObject thumbnail = (vidTitle.getJSONObject("thumbnails")).getJSONObject("medium");

                            System.out.println(vidId.getString("videoId") + " -  " + vidTitle.getString("title") + "  " + thumbnail.getString("url"));
                            userController.saveMV(vidId.getString("videoId"), vidTitle.getString("title"), thumbnail.getString("url"));

                        }

                        videos = null;
                    }else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("NEXT PAGE TOKEN:" + nextpagetoken );
            String urlwithpagetoken = "https://www.googleapis.com/youtube/v3/search?pageToken="+nextpagetoken+"&part=snippet&maxResults=50&order=viewCount&q=official+music+video&type=video&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";


            sendRequest(urlwithpagetoken,nextpagetoken,cnt);
        }









    }

}
