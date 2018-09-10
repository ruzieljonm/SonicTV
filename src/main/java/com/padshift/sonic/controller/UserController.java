package com.padshift.sonic.controller;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.padshift.sonic.entities.*;
import com.padshift.sonic.service.GenreService;
import com.padshift.sonic.service.UserService;
import com.padshift.sonic.service.VideoService;
import org.hibernate.annotations.SourceType;
import org.hibernate.mapping.Array;
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
import java.util.*;

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

    @Autowired
    GenreService genreService;

    @RequestMapping("/")
    public String showLoginPage(HttpSession session, Model model) {
        if(session.isNew()) {
            return "signinsignup";
        }else{
            return showHomepage(model,session);
        }
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String generalSigninPost(HttpServletRequest request, Model model, HttpSession session) {
        String userName = request.getParameter("inputUserName");
        String userPass = request.getParameter("inputPassword");

        User checkUser = userService.findByUsernameAndPassword(userName, userPass);

        if (checkUser != null) {
            session.setAttribute("userid",checkUser.getUserId());
            session.setAttribute("username",checkUser.getUserName());
            System.out.println(checkUser.getUserId() + " "+ checkUser.getUserName());

            return showHomepage(model,session);
        } else {
            return "signinsignup";
        }
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String generalSignup(HttpServletRequest request, Model model, HttpSession session) {
        User newUser = new User();
        newUser.setUserName(request.getParameter("inputUserName"));
        newUser.setUserPass(request.getParameter("inputPassword"));
        newUser.setUserEmail(request.getParameter("inputEmail"));
        userService.saveUser(newUser);
        User checkUser = userService.findByUsername(request.getParameter("inputUserName"));
        session.setAttribute("userid", checkUser.getUserId());
        session.setAttribute("username", request.getParameter("inputUserName"));

        return showGenreSelection(model,session);
    }


    @RequestMapping("/genreselection")
    public String showGenreSelection(Model model, HttpSession session){

        ArrayList<Genre> genres = genreService.findAll();

        for (int i=0; i<genres.size(); i++){
            System.out.println(genres.get(i).getGenreName());
        }
        model.addAttribute("genres", genres);
        model.addAttribute("starname", "pota");

        return "GenreSelection";
    }

    @RequestMapping(value = "/submitpref", method = RequestMethod.POST)
    public String submitPreference(HttpServletRequest request, HttpSession session, Model model) {

        System.out.println(session.getAttribute("userid") + "usah id");

        ArrayList<Genre> genres = genreService.findAll();
        UserPreference[] genrePreference = new UserPreference[genres.size()];

        for (int i=0; i<genrePreference.length; i++){
            genrePreference[i] = new UserPreference();
        }
        int userid = Integer.parseInt(session.getAttribute("userid").toString());

        for (int i=0; i<genrePreference.length; i++){
            genrePreference[i].setUserId(userid);
            float temp;
            if(request.getParameter(genres.get(i).getGenreName().toString())==null){
                 temp=0;
            }else {
                 temp = Float.parseFloat(request.getParameter(genres.get(i).getGenreName().toString()));
            }
            genrePreference[i].setPrefWeight(temp);
            genrePreference[i].setGenreId(genres.get(i).getGenreId());
            System.out.println(genrePreference[i].getUserId() + "-" + genrePreference[i].getGenreId() + "-" + genrePreference[i].getPrefWeight());

            userService.saveUserPreference(genrePreference[i]);


        }

        return showHomepage(model,session);

    }

    String topgenre=null;

    @RequestMapping("/homepagev2")
    public String showHomepage(Model model, HttpSession session) {




        String userid = session.getAttribute("userid").toString();
        System.out.println("tang ina" + userid);
        User user = userService.findByUserId(Integer.parseInt(userid));

        System.out.println(user.getUserId() + " "+ user.getUserName());



        ArrayList<UserPreference> userPref = userService.findAllByUserId(user.getUserId());

        for(UserPreference usp : userPref){
            System.out.println(usp.getGenreId() + " : " + usp.getPrefWeight());
        }

        Collections.sort(userPref, UserPreference.PrefWeightComparator);
        System.out.println("sorted");
        for(UserPreference usp : userPref){
            System.out.println(usp.getGenreId() + " : " + usp.getPrefWeight());
        }


        ArrayList<Genre> genres = videoService.findAllGenre();

        for(Genre gen : genres){
            System.out.println(gen.getGenreName());
        }


        ArrayList<VVD> vr1 = new ArrayList<VVD>();
        ArrayList<VVD> vr2 = new ArrayList<VVD>();
        ArrayList<VVD> vr3 = new ArrayList<VVD>();
        ArrayList<VVD> vr4 = new ArrayList<VVD>();


        String[] topfour = new String[4];

        for(int i=0; i<topfour.length; i++){
            topfour[i] = (videoService.findGenreByGenreId(userPref.get(i).getGenreId())).getGenreName();
        }

        topgenre = topfour[0];



        ArrayList<VideoDetails> vidListGen1 = videoService.findAllByGenre(topfour[0]);
        ArrayList<VideoDetails> vidListGen2 = videoService.findAllByGenre(topfour[1]);
        ArrayList<VideoDetails> vidListGen3 = videoService.findAllByGenre(topfour[2]);
        ArrayList<VideoDetails> vidListGen4 = videoService.findAllByGenre(topfour[3]);

        for(int i=0; i<5; i++){
            System.out.println(vidListGen1.get(i).getTitle() +" ------------- " + vidListGen1.get(i).getViewCount());
        }

        System.out.println("----------IMO MAMA GA SORTING------------");

        Collections.sort(vidListGen1);
        Collections.sort(vidListGen2);
        Collections.sort(vidListGen3);
        Collections.sort(vidListGen4);

        for(int i=0; i<5; i++){
            System.out.println(vidListGen1.get(i).getTitle() +" ------------- " + vidListGen1.get(i).getViewCount());
        }


        for(int i=0; i<6; i++){
            VVD vid1 = new VVD(vidListGen1.get(i).getVideoid(), vidListGen1.get(i).getTitle(), vidListGen1.get(i).getArtist(), vidListGen1.get(i).getGenre(), vidListGen1.get(i).getDate(), "https://i.ytimg.com/vi/" + vidListGen1.get(i).getVideoid() + "/mqdefault.jpg");
            vr1.add(vid1);
            vid1 = null;

            VVD vid2 = new VVD(vidListGen2.get(i).getVideoid(), vidListGen2.get(i).getTitle(), vidListGen2.get(i).getArtist(), vidListGen2.get(i).getGenre(), vidListGen2.get(i).getDate(), "https://i.ytimg.com/vi/" + vidListGen2.get(i).getVideoid() + "/mqdefault.jpg");
            vr2.add(vid2);
            vid2 = null;

            VVD vid3 = new VVD(vidListGen3.get(i).getVideoid(), vidListGen3.get(i).getTitle(), vidListGen3.get(i).getArtist(), vidListGen3.get(i).getGenre(), vidListGen3.get(i).getDate(), "https://i.ytimg.com/vi/" + vidListGen3.get(i).getVideoid() + "/mqdefault.jpg");
            vr3.add(vid3);
            vid3 = null;

            VVD vid4 = new VVD(vidListGen4.get(i).getVideoid(), vidListGen4.get(i).getTitle(), vidListGen4.get(i).getArtist(), vidListGen4.get(i).getGenre(), vidListGen4.get(i).getDate(), "https://i.ytimg.com/vi/" + vidListGen4.get(i).getVideoid() + "/mqdefault.jpg");
            vr4.add(vid4);
            vid4 = null;


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

        System.out.println("video id : " + vididtoplay);
//        System.out.println("aaaaaaaaaaa" + session.getAttribute("userid"));

        UserHistory userhist = new UserHistory();
        userhist.setUserId(Integer.parseInt(session.getAttribute("userid").toString()));
        userhist.setVideoid(vididtoplay);

//        VideoDetails video = videoService.findByVideoid(vididtoplay);
//        String genre = "%"+video.getGenre()+"%";

       // System.out.println(userhist.getUserId() + userhist.getVideoid());

        userService.saveUserHistory(userhist);
        VideoDetails playvid = videoService.findByVideoid(vididtoplay);
        ArrayList<VideoDetails> upnext = (ArrayList<VideoDetails>) videoService.findAllByGenre(topgenre);
        Collections.sort(upnext);

        System.out.println(playvid.getTitle() + " " + playvid.getArtist());

        String url = "https://www.youtube.com/embed/" + playvid.getVideoid();

        String thumbnail1 = "https://i.ytimg.com/vi/" + upnext.get(1).getVideoid() +"/mqdefault.jpg";
        String thumbnail2 = "https://i.ytimg.com/vi/" + upnext.get(2).getVideoid() +"/mqdefault.jpg";
        String thumbnail3 = "https://i.ytimg.com/vi/" + upnext.get(3).getVideoid() +"/mqdefault.jpg";

        ArrayList<VideoDetails> videoList = new ArrayList<VideoDetails>();

        videoList = videoService.findAllVideoDetails();

        ArrayList<VVD> vr1 = new ArrayList<VVD>();

        for (int i = 0; i < 6; i++) {
                VVD vid = new VVD(videoList.get(i).getVideoid(), videoList.get(i).getTitle(), videoList.get(i).getArtist(), videoList.get(i).getGenre(), videoList.get(i).getDate(),"https://i.ytimg.com/vi/" + videoList.get(i).getVideoid() + "/mqdefault.jpg");
                vr1.add(vid);
                vid = null;
        }

        model.addAttribute("r1", vr1);

        model.addAttribute("emblink", url);
        model.addAttribute("vidtitle", playvid.getTitle());
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


    @RequestMapping("/homefeed")
    public String showHomeFeed(){
        return "HomeFeed";
    }

    @RequestMapping("/explore")
    public String showExplore(){
        return "Explore";
    }












    }






