package com.padshift.sonic.controller;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.padshift.sonic.entities.*;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import org.hibernate.annotations.SourceType;
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
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @RequestMapping("/signinpage")
    public String showLoginPage() {
        return "signinsignup";
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String generalSigninPost(HttpServletRequest request, ModelMap map) {
        String userName = request.getParameter("inputUserName");
        String userPass = request.getParameter("inputPassword");

        User checkUser = userService.findUserByUsernameAndPassword(userName, userPass);

        if (checkUser != null) {
            return "Homepage";
        } else {
            return "signinsignup";
        }
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String generalSignup(HttpServletRequest request, ModelMap map, HttpSession session) {
        User newUser = new User();
        newUser.setUserName(request.getParameter("inputUserName"));
        newUser.setUserPass(request.getParameter("inputPassword"));
        newUser.setUserEmail(request.getParameter("inputEmail"));
        userService.saveUser(newUser);
        session.setAttribute("username", request.getParameter("inputUserName"));
        //map.addAttribute("username", session.getAttribute("username"));
        return "RegistrationGenre";
    }

    @RequestMapping(value = "/submitpref", method = RequestMethod.POST)
    public String submitPreference(HttpServletRequest request, HttpSession session, Model model) {
        String[] pref = request.getParameterValues("preference");
        float percentage = (100 / pref.length - 1) + (100 % pref.length);

        String username = (String) session.getAttribute("username");

        User user = userService.findByUsername(username);
        UserPreference userpref = new UserPreference();
        userpref.setUserId(user.getUserId());
        if (Arrays.asList(pref).contains("pop")) {
            userpref.setPop(percentage);
        }

        if (Arrays.asList(pref).contains("classical")) {
            userpref.setClassical(percentage);
        }

        if (Arrays.asList(pref).contains("country")) {
            userpref.setCountry(percentage);
        }

        if (Arrays.asList(pref).contains("rnb")) {
            userpref.setRnb(percentage);
        }

        if (Arrays.asList(pref).contains("electronic")) {
            userpref.setElectronic(percentage);
        }

        if (Arrays.asList(pref).contains("rock")) {
            userpref.setRock(percentage);
        }
        userService.saveUserPreference(userpref);
        return showHomepage(model, session);

    }

    @RequestMapping("/homepagev2")
    public String showHomepage(Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");
        User usertemp= userService.findByUsername(username);
        System.out.println("kapoy:" + username);




        System.out.println("TANG INA:" + usertemp.getUserId());
        String userid = Integer.toString(usertemp.getUserId());
        session.setAttribute("userid",userid);
        System.out.println("THE USER ID:" + userid);


        User user = userService.findByUsername(username);
        UserPreference up = userService.findUserPreferenceByUserId(user.getUserId());
        ArrayList<VideoDetails> videoList = new ArrayList<VideoDetails>();


        if (up.getPop() > 10) {
            ArrayList<VideoDetails> asianPop = (ArrayList<VideoDetails>) videoService.findAllByGenre("Asian Pop");
            ArrayList<VideoDetails> westernPop = (ArrayList<VideoDetails>) videoService.findAllByGenre("Western Pop");
            for (int i = 0; i < asianPop.size(); i++) {
                videoList.add(asianPop.get(i));
            }
            for (int i = 0; i < westernPop.size(); i++) {
                videoList.add(westernPop.get(i));
            }
        }

        if (up.getRnb() > 10) {
            ArrayList<VideoDetails> conrnb = (ArrayList<VideoDetails>) videoService.findAllByGenre("Contemporary R&B/Soul");
            for (int i = 0; i < conrnb.size(); i++) {
                videoList.add(conrnb.get(i));
            }
        }

        if (up.getElectronic() > 10) {
            ArrayList<VideoDetails> electhiphop = (ArrayList<VideoDetails>) videoService.findAllByGenre("Western Hip-Hop/Rap");
            for (int i = 0; i < electhiphop.size(); i++) {
                videoList.add(electhiphop.get(i));
            }
        }


        for (int i = 0; i < videoList.size(); i++) {
            //videoList.get(i).getTitle();
            System.out.println(videoList.get(i).getTitle() + " - " + videoList.get(i).getGenre());
        }


        ArrayList<VVD> vr1 = new ArrayList<VVD>();
        ArrayList<VVD> vr2 = new ArrayList<VVD>();
        ArrayList<VVD> vr3 = new ArrayList<VVD>();
        ArrayList<VVD> vr4 = new ArrayList<VVD>();

       //VVD vid = new VVD();

        for (int i = 0; i < videoList.size(); i++) {
            if (i <= 4) {
                VVD vid = new VVD(videoList.get(i).getVideoid(), videoList.get(i).getTitle(), videoList.get(i).getArtist(), videoList.get(i).getGenre(), videoList.get(i).getDate(),"https://i.ytimg.com/vi/" + videoList.get(i).getVideoid() + "/mqdefault.jpg");
                vr1.add(vid);
                vid = null;


            }
            if (i >= 5 && i <= 9) {
                VVD vid = new VVD(videoList.get(i).getVideoid(), videoList.get(i).getTitle(), videoList.get(i).getArtist(), videoList.get(i).getGenre(), videoList.get(i).getDate(),"https://i.ytimg.com/vi/" + videoList.get(i).getVideoid() + "/mqdefault.jpg");
                vr2.add(vid);
                vid = null;
            }
            if (i >= 10 && i <= 14) {
                VVD vid = new VVD(videoList.get(i).getVideoid(), videoList.get(i).getTitle(), videoList.get(i).getArtist(), videoList.get(i).getGenre(), videoList.get(i).getDate(),"https://i.ytimg.com/vi/" + videoList.get(i).getVideoid() + "/mqdefault.jpg");
                vr3.add(vid);
                vid = null;
            }
            if (i >= 15 && i < 20) {
                VVD vid = new VVD(videoList.get(i).getVideoid(), videoList.get(i).getTitle(), videoList.get(i).getArtist(), videoList.get(i).getGenre(), videoList.get(i).getDate(),"https://i.ytimg.com/vi/" + videoList.get(i).getVideoid() + "/mqdefault.jpg");
                vr4.add(vid);
                vid = null;
            }
        }


        model.addAttribute("r1", vr1);
        model.addAttribute("r2", vr2);
        model.addAttribute("r3", vr3);
        model.addAttribute("r4", vr4);

        return "Homepage";
    }


    @RequestMapping("/gotoPlayer")
    public String gotoPlayer(HttpServletRequest request, Model model, HttpSession session){
        String vididtoplay = request.getParameter("clicked");

        System.out.println(vididtoplay);
        System.out.println("aaaaaaaaaaa" + session.getAttribute("userid"));

        UserHistory userhist = new UserHistory();
        userhist.setUserId(Integer.parseInt((String) session.getAttribute("userid")));
        userhist.setVideoid(vididtoplay);

//        VideoDetails video = videoService.findByVideoid(vididtoplay);
//        String genre = "%"+video.getGenre()+"%";

       // System.out.println(userhist.getUserId() + userhist.getVideoid());

        userService.saveUserHistory(userhist);

        VideoDetails playvid = videoService.findByVideoid(vididtoplay);
        ArrayList<VideoDetails> upnext = (ArrayList<VideoDetails>) videoService.findAllByGenre("Western Pop");
        Collections.shuffle(upnext);

        System.out.println(playvid.getTitle() + " " + playvid.getArtist());

        String url = "https://www.youtube.com/embed/" + playvid.getVideoid();

        String thumbnail1 = "https://i.ytimg.com/vi/" + upnext.get(1).getVideoid() +"/mqdefault.jpg";
        String thumbnail2 = "https://i.ytimg.com/vi/" + upnext.get(2).getVideoid() +"/mqdefault.jpg";
        String thumbnail3 = "https://i.ytimg.com/vi/" + upnext.get(3).getVideoid() +"/mqdefault.jpg";


        model.addAttribute("emblink", url);
        model.addAttribute("upnext1", upnext.get(1));
        model.addAttribute("upnext2", upnext.get(2));
        model.addAttribute("upnext3", upnext.get(3));

        model.addAttribute("tn1", thumbnail1);
        model.addAttribute("tn2", thumbnail2);
        model.addAttribute("tn3", thumbnail3);
        return "VideoPlayerV2";

    }


    @RequestMapping("/vplayer")
    public String showVideoPlayer() {
        return "VideoPlayerV2";
    }


    @RequestMapping("/profile")
    public String showUserProfile(HttpServletRequest request, Model model) {
        List<Video> videoList = videoService.findAll();
        for (int i = 0; i < videoList.size(); i++) {
            System.out.println(videoList.get(i).getVideoid());
        }
        model.addAttribute("vids", videoList);
        return "UserProfile";
    }



    public void saveMV(String vidId, String title, String url) {
        Video newVideo = new Video();
        newVideo.setVideoid(vidId);
        newVideo.setMvtitle(title);
        newVideo.setThumbnail(url);
        videoService.saveVideo(newVideo);
    }


    @RequestMapping("/genreselection")
    public String showGenreSelection(){



        return "GenreSelection";
    }


    @RequestMapping("/savegenretodb")
    public String saveGenretoDB(){
        ArrayList<String> genres = videoService.findDistinctGenre();
        for (int i=0; i<genres.size(); i++){

            CharSequence pop = "Pop";
            boolean boolpop = genres.get(i).toString().contains(pop);

            CharSequence rock = "Rock";
            boolean boolrock= genres.get(i).toString().contains(rock);

            CharSequence alt = "Alternative";
            boolean boolalt = genres.get(i).toString().contains(alt);



            if(boolpop==true){
                System.out.println(genres.get(i).toString());
                Genre genre = new Genre();
                genre.setGenreId(1);
                genre.setGenreName("Pop Music");
                videoService.saveGenre(genre);
            }

        }


        return "testing";
    }






    }






