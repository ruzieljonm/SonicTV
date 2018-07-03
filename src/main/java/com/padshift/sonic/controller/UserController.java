package com.padshift.sonic.controller;

import com.padshift.sonic.entities.User;
import com.padshift.sonic.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ruzieljonm on 26/06/2018.
 */

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;

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

    @RequestMapping("/profile")
    public String showUserProfile(){
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

                    System.out.println(vidId.getString("videoId") + " -  " + vidTitle.getString("title"));


                }

//                String vidUrl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=2Vv-BfVoq4g&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";
//                URL vidobj = new URL(url);
//                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //System.out.println("TEST: " + videos.getJSONObject());


            } catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        return "FetchYoutubeAPI";
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




}
