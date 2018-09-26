package com.padshift.sonic.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ruzieljonm on 24/07/2018.
 */
@SuppressWarnings("Duplicates")
@Controller
public class YoutubeAPIController {

    @Autowired
    UserController userController;

    @RequestMapping(value="/fetchMusicVideos", method = RequestMethod.POST)
    public String fetchMusicVideos(HttpServletRequest request){
        String queryGenre = request.getParameter("genre");
        queryGenre = queryGenre.replaceAll("\\s+","");
        System.out.println("Query this Genre : " + queryGenre);
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&order=date&q="+queryGenre+"+music+video&type=video&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();

            System.out.println("\nSending'Get' request to URL : " + url);
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



                    JSONArray videos = new JSONArray(myresponse.getJSONArray("items").toString());
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject vid = videos.getJSONObject(i);

                        JSONObject vidId = vid.getJSONObject("id");
                        JSONObject vidTitle = vid.getJSONObject("snippet");
                        JSONObject thumbnail = (vidTitle.getJSONObject("thumbnails")).getJSONObject("medium");

                        System.out.println(vidId.getString("videoId") + " -  " + vidTitle.getString("title") + "  " + thumbnail.getString("url"));
//                        userController.saveMV(vidId.getString("videoId"), vidTitle.getString("title"), thumbnail.getString("url"));

                    }

                    videos = null;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "testing";
    }

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


    @RequestMapping("/playlistAPI")
    public String sendPlaylistRequest(){

        String playlistId = "PLI_7Mg2Z_-4JiTe-Mcr9XmxXJLQ53Pppq";
        String nextpagetoken = null;
        String[] playlistid = {"PLpYIFjf1aEg8jn4YDmhbc5WfwX9iUSQuX","PLczNuvx1IpyGsj8VujNvtluuChj7-wORv","PL7KJ8NgcCDuUftHEusHd3VWkl1BKC__Rs", "PLuUrokoVSxleFKSVLCOKmpFa3QR9biaFx","PLRUpgoNFP-iH9Ze6RSom4O4JhrHYGc6ow", "PL9tY0BWXOZFutmwU0n5ObcTt7-k8-LGMr", "PLWlTX25IDqIy_HeO8BsOyt0wAhE9No3Nt","PL9tY0BWXOZFtfP5S8PX-PGHR6yaAmfbjV", "PLvFYFNbi-IBFeP5ALr50hoOmKiYRMvzUq", "PL4E8252BF0EE57BBB", "PLJ8hEo0VUT07KZKgfen0Lk5fAhhyDQxvE", "PL7AEF8A70B6EEC5F0","PLBeHYvz79ZP1Goal254irJse8yxbaDWsQ", "PLjp0AEEJ0-fGKG_3skl0e1FQlJfnx-TJz","PLz68cPKdtIOxcO2jNfWrKN-4qY6QyDvw_", "PL1343579D67ED4740", "PLMmqTuUsDkRLPIsLKF0xIFX4JY457GRx8", "PL4o29bINVT4ECr8O6FU3PzXh2NX8_JE2-", "PLsvoYlzBrLFAJd4hNQSHw1lYjDKeQB_iU","PLJGccvC-yhA269oq16Dh3q0uAUYgdJug8", "PL6Go6XFhidED5RmiuRdks87fyOvlXqn14","https://youtu.be/8WYHDfJDPDc","PL330A461779A9106A","PLxvodScTx2RsV2VIDIBksBmuLFngu3mgU", "PLKUA473MWUv3VnK3E7ElYEqVN7eb7h92_","PLI_7Mg2Z_-4JiTe-Mcr9XmxXJLQ53Pppq", "PLkoOCo9K_o4Ut5v-8eGFp1ciy5ylZ90WG","PLn4zT-06U9FUrZmdiXHm9AyJSdcM6cdxp","PLuUrokoVSxlfUJuJB_D8j_wsFR4exaEmy","PL4QNnZJr8sRNKjKzArmzTBAlNYBDN2h-J","PL55713C70BA91BD6E", "PLjzeyhEA84sS6ogo2mXWdcTrL2HRfJUv8","PLRZlMhcYkA2EQRcAq4nf7pFP3LcD5uX7h","PL8A83124F1D79BD4F", "PLFgquLnL59ak1QNHmrUSjNM6WTegpgX__"};

//        String urlwithpagetoken = "https://www.googleapis.com/youtube/v3/search?pageToken="+nextpagetoken+"&part=snippet&maxResults=50&order=viewCount&q=music+video&type=video&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";
        int cnt=0;
        for (int i=0; i<5; i++){
            playlistAPI(playlistid[i]);
        }


        return "testing";
    }


    public void playlistAPI(String playlistid){
        String url1 = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+playlistid+"&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";

        String nextpagetoken=null;
        try {

                URL obj = new URL(url1);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                int responseCode = con.getResponseCode();

                System.out.println("\nSending'Get' request to URL : " + url1);
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

                    if (myresponse.has("nextPageToken")) {

                            nextpagetoken = myresponse.getString("nextPageToken");


                        String thumbnail = null;
                        JSONArray videos = new JSONArray(myresponse.getJSONArray("items").toString());
                        for (int i = 0; i < videos.length(); i++) {
                            JSONObject vid = videos.getJSONObject(i);

                            JSONObject vidsnip = vid.getJSONObject("snippet");
                            String vidTitle = vidsnip.getString("title");
                            if((vidsnip.getJSONObject("thumbnails"))!=null) {
                                thumbnail = (vidsnip.getJSONObject("thumbnails")).getJSONObject("medium").getString("url");
                            }
                            String videoid = vidsnip.getJSONObject("resourceId").getString("videoId");

                            System.out.println(videoid + " -  " + vidTitle + "  " + thumbnail);
                            userController.saveMV(videoid, vidTitle, thumbnail);

                        }

                        videos = null;
                    } else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        nextPageRequest(nextpagetoken, playlistid);






    }


    public String nextPageRequest(String nextpagetoken, String playlistid){
        String url2 = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId="+playlistid+"&pageToken="+nextpagetoken+"&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";

        try {

            URL obj = new URL(url2);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();

            System.out.println("\nSending'Get' request to URL : " + url2);
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



                String thumbnail = null;
                    JSONArray videos = new JSONArray(myresponse.getJSONArray("items").toString());
                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject vid = videos.getJSONObject(i);

                        JSONObject vidsnip = vid.getJSONObject("snippet");
                        String vidTitle = vidsnip.getString("title");
                        if(!vidsnip.isNull("thumbnails")) {
                            thumbnail = (vidsnip.getJSONObject("thumbnails")).getJSONObject("medium").getString("url");
                        }
                        String videoid = vidsnip.getJSONObject("resourceId").getString("videoId");

                        System.out.println(videoid + " -  " + vidTitle + "  " + thumbnail);
                        userController.saveMV(videoid, vidTitle, thumbnail);

                    }

                    videos = null;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        nextpagetoken=null;


        return "testing";
    }




}
