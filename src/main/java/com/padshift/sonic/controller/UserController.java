package com.padshift.sonic.controller;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.Video;
import com.padshift.sonic.entities.VideoDetails;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import radams.gracenote.webapi.GracenoteException;
import radams.gracenote.webapi.GracenoteMetadata;
import radams.gracenote.webapi.GracenoteWebAPI;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by ruzieljonm on 26/06/2018.
 */

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    VideoService videoService;

    @RequestMapping("/homepage")
    public String showLoginPage(){
        return "loginPage";
    }

    @RequestMapping("/signup")
    public String showSignUpPage(){
        return "signupPage";
    }

    @RequestMapping("/vplayer")
    public String showVideoPlayer(){

        return "VideoPlayer";
    }

//    @RequestMapping("/profile")
//    public String showUserProfile(){
//        return "UserProfile";
//    }

    @RequestMapping("/profile")
    public String showUserProfile(HttpServletRequest request, Model model) {
        List<Video> videoList = videoService.findAll();
        for (int i =0; i<videoList.size(); i++){
            System.out.println(videoList.get(i).getVideoid());

        }



        //String link = "https://www.youtube.com/embed/";
        model.addAttribute("vids", videoList);

        
        return "UserProfile";
    }



    @RequestMapping("/request")
    public String showFYAPI(){
        try {
            String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=50&q=music+video&type=video&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";
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
            while((inputLine = in.readLine())!=null){
                response.append(inputLine);
            }

            in.close();


            System.out.println(response.toString());

            JSONObject myresponse = null;

            try {
                myresponse = new JSONObject(response.toString());
                System.out.println(myresponse);

                JSONArray videos = new JSONArray(myresponse.getJSONArray("items").toString());
                for (int i = 0; i < videos.length(); i++){
                    JSONObject vid = videos.getJSONObject(i);

                    JSONObject vidId = vid.getJSONObject("id");
                    JSONObject vidTitle = vid.getJSONObject("snippet");
                    JSONObject thumbnail = (vidTitle.getJSONObject("thumbnails")).getJSONObject("medium");

                    System.out.println(vidId.getString("videoId") + " -  " + vidTitle.getString("title") + "  " + thumbnail.getString("url") );
                    this.saveMV(vidId.getString("videoId"),vidTitle.getString("title"),thumbnail.getString("url"));

                }


                //System.out.println("TEST: " + videos.getJSONObject());


            } catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        return "FetchYoutubeAPI";
    }
//    @RequestMapping("/nlu")
//    public void NLU(){
//
//
//
//
//        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
//                "2018-03-16",
//                "{ruzieljonmantalaba@gmail.com}",
//                "{Software.engineer123}"
//        );
//
//        service.setEndPoint("https://gateway-fra.watsonplatform.net/natural-language-understanding/api");
//
//        String text = "Moira Dela Torre";
//
//        CategoriesOptions categories = new CategoriesOptions();
//
//        Features features = new Features.Builder()
//                .categories(categories)
//                .build();
//
//        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
//                .text(text)
//                .features(features)
//                .build();
//
//        AnalysisResults response = service
//                .analyze(parameters)
//                .execute();
//        System.out.println(response);
//    }


    public void saveMV(String vidId, String title, String url){
        Video newVideo = new Video();

        newVideo.setVideoid(vidId);
        newVideo.setMvtitle(title);
        newVideo.setThumbnail(url);
        videoService.saveVideo(newVideo);

    }

    @RequestMapping("/request2")
    public String showTesting() throws IOException {




        return "testing";
    }




    @RequestMapping(value="/signup", method=RequestMethod.POST)
    public String generalSignup(HttpServletRequest request, ModelMap map) {

        User newUser = new User();

        newUser.setUserName(request.getParameter("inputUserName"));
        newUser.setUserPass(request.getParameter("inputPassword"));
        newUser.setUserEmail(request.getParameter("inputEmail"));
        userService.saveUser(newUser);

        return "loginPage";
    }


    @RequestMapping(value="/signin", method= RequestMethod.POST)
    public String generalSigninPost(HttpServletRequest request, ModelMap map) {
        String userName = request.getParameter("inputUserName");
        String userPass = request.getParameter("inputPassword");

        User checkUser = userService.findUserByUsernameAndPassword(userName,userPass);

        if(checkUser!=null){
            return "VideoPlayer";
        }else {
            return "loginPage";
        }

    }




    @RequestMapping("/metadata")
    public String showmetadata(){
        String clientID  = "2034677681"; // Put your clientID here.
        String clientTag = "75917E36EEDFB95B94EC9E68E804B835"; // Put your clientTag here.
        String tracktitle, artist, albumdate, genre, album;

        try
        {
            /* You first need to register your client information in order to get a userID.
            Best practice is for an application to call this only once, and then cache the userID in
            persistent storage, then only use the userID for subsequent API calls. The class will cache
            it for just this session on your behalf, but you should store it yourself. */
            GracenoteWebAPI api = new GracenoteWebAPI(clientID, clientTag); // If you have a userID, you can specify it as the third parameter to constructor.
            String userID = api.register();
            System.out.println("UserID = " + userID);



//            GracenoteWebAPI._execute();
            // Once you have the userID, you can search for tracks, artists or albums easily.
            System.out.println("Search Track:");

            List<Video> videoList = videoService.findAll();
            for(int i=0;i<videoList.size();i++){
                api.searchTrack(videoList.get(i).getMvtitle(),"",videoList.get(i).getMvtitle());
                tracktitle = api.getTracktitle();
                artist = api.getArtist();

                albumdate = api.getAlbumDate();
                genre = api.getGenre();
                System.out.println("TITLE: "+tracktitle+" ARTIST: "+artist+" DATE: "+albumdate+" GENRE: "+genre);
                saveMVDetails(videoList.get(i).getVideoid(),tracktitle,artist,albumdate,genre);
            }


        }
        catch (GracenoteException e)
        {
            e.printStackTrace();
        }

        return "metadata";
    }


    public void saveMVDetails(String vidId, String title, String artist, String date, String genre){

        VideoDetails newMVDetails = new VideoDetails();
        newMVDetails.setVideoid(vidId);
        newMVDetails.setTitle(title);
        newMVDetails.setArtist(artist);

        newMVDetails.setDate(date);
        newMVDetails.setGenre(genre);

        videoService.saveVideoDetails(newMVDetails);
//        Video newVideo = new Video();
//
//        newVideo.setVideoid(vidId);
//        newVideo.setMvtitle(title);
//        newVideo.setThumbnail(url);
//        videoService.saveVideo(newVideo);
    }


}

