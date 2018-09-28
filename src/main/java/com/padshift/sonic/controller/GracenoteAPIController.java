package com.padshift.sonic.controller;

import com.padshift.sonic.entities.Genre;
import com.padshift.sonic.entities.Video;
import com.padshift.sonic.entities.VideoDetails;
import com.padshift.sonic.service.VideoService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import radams.gracenote.webapi.GracenoteException;
import radams.gracenote.webapi.GracenoteWebAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ruzieljonm on 24/07/2018.
 */
@SuppressWarnings("Duplicates")
@Controller
public class GracenoteAPIController {

    @Autowired
    VideoService videoService;


    @RequestMapping("/metadata")
    public String showmetadata() {





        String clientID = "2034677681"; // Put your clientID here.
        String clientTag = "75917E36EEDFB95B94EC9E68E804B835"; // Put your clientTag here.
        String tracktitle, artist, albumdate, genre, vcount, likes, dislikes;

        try {
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
//            api.searchTrack("Julian Jordan & Martin Garrix", "", "BFAM");
//                tracktitle = api.getTracktitle();
//                artist = api.getArtist();
//
//                albumdate = api.getAlbumDate();
//                genre = api.getGenre();
//                System.out.println("TITLE: " + tracktitle + " ARTIST: " + artist + " DATE: " + albumdate + " GENRE: " + genre);


            List<Video> videoList = videoService.findAll();
            ArrayList<VideoDetails> vids = videoService.findAllVideoDetails();

            ArrayList<String> vyou = new ArrayList<>();
            ArrayList<String> check = new ArrayList<>();


            for (int i=0; i<vids.size(); i++){
                check.add(vids.get(i).getVideoid());
            }

            for (int i=0; i<videoList.size(); i++){
                vyou.add(videoList.get(i).getVideoid());
            }




            for (int i = 0; i < videoList.size(); i++) {


                if (check.contains(videoList.get(i).getVideoid())==false) {

                    api.searchTrack(videoList.get(i).getMvtitle(), "", videoList.get(i).getMvtitle());
                    tracktitle = api.getTracktitle();
                    artist = api.getArtist();

                    albumdate = api.getAlbumDate();
                    genre = api.getGenre();


                    String[] statistics = getVideoStatistics(videoList.get(i).getVideoid());
                    System.out.println("AAAAAAA" + statistics[0]);

                    vcount = statistics[0];
                    likes = statistics[1];
                    dislikes = statistics[2];


                    System.out.println("TITLE: " + tracktitle + " ARTIST: " + artist + " DATE: " + albumdate + " GENRE: " + genre);


                    saveMVDetails(videoList.get(i).getVideoid(), videoList.get(i).getMvtitle(), artist, albumdate, genre, vcount, likes, dislikes);
                    System.out.println("SAVED");

                }
            }


        } catch (GracenoteException e) {
            e.printStackTrace();
        }

        return "testing";
    }

    @RequestMapping("/testCount")
    public String[] getVideoStatistics(String videoId){
        String urlvcount =  "https://www.googleapis.com/youtube/v3/videos?part=statistics&id="+ videoId +"&key=AIzaSyAxsoedlgT5NfsEI_inmsXKflR_DdYs5kU";

        String[] statistic = new String[3];
        URL obj = null;
        try {
            obj = new URL(urlvcount);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();

            System.out.println("\nSending'Get' request to URL : " + urlvcount);
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

            myresponse = new JSONObject(response.toString());
            System.out.println(myresponse);


            JSONArray videos = new JSONArray(myresponse.getJSONArray("items").toString());
            if(videos.length()>0) {

                JSONObject vid = videos.getJSONObject(0);

                JSONObject stats = vid.getJSONObject("statistics");
                if (stats.has("viewCount")) {
                    statistic[0] = stats.getString("viewCount");
                } else {
                    statistic[0] = "0";
                }

                if (stats.has("likeCount")) {
                    statistic[1] = stats.getString("likeCount");
                } else {
                    statistic[1] = "0";
                }
                if (stats.has("dislikeCount")) {
                    statistic[2] = stats.getString("dislikeCount");
                } else {
                    statistic[2] = "0";
                }
            }else {

                statistic[0] = "0";
                statistic[1] = "0";
                statistic[2] = "0";
            }

//            System.out.println(" views: " + stats.getString("viewCount") + " likes: " + stats.getString("likeCount") + " dislikes: " + stats.getString("dislikeCount") );


        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


        return statistic;

    }


    public void saveMVDetails(String vidId, String title, String artist, String date, String genre, String vcount, String likes, String dislikes) {

        VideoDetails newMVDetails = new VideoDetails();
        newMVDetails.setVideoid(vidId);
        newMVDetails.setTitle(title);
        newMVDetails.setArtist(artist);

        newMVDetails.setDate(date);
        newMVDetails.setGenre(genre);
        newMVDetails.setViewCount(vcount);
        newMVDetails.setLikes(likes);
        newMVDetails.setDislikes(dislikes);

        videoService.saveVideoDetails(newMVDetails);


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

            CharSequence rnb = "R&B";
            boolean boolrnb = genres.get(i).toString().contains(rnb);

            CharSequence country = "Country";
            boolean boolcountry = genres.get(i).toString().contains(country);

            CharSequence house = "House";
            boolean boolhouse = genres.get(i).toString().contains(house);

            CharSequence metal = "Metal";
            boolean boolmetal = genres.get(i).toString().contains(metal);

            CharSequence reggae = "Reggae";
            boolean boolreggae = genres.get(i).toString().contains(reggae);

            CharSequence relig = "Religious";
            boolean boolrelig= genres.get(i).toString().contains(relig);

            CharSequence hiphop = "Hip-Hop";
            boolean boolhiphop= genres.get(i).toString().contains(hiphop);


            if(boolpop==true){
                Genre genre = new Genre();
                genre.setGenreId(1);
                genre.setGenreName("Pop Music");
                genre.setGenrePhoto("/images/pop.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrock==true || boolmetal==true){
                Genre genre = new Genre();
                genre.setGenreId(2);
                genre.setGenreName("Rock Music");
                genre.setGenrePhoto("/images/rock.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolalt==true){
                Genre genre = new Genre();
                genre.setGenreId(3);
                genre.setGenreName("Alternative Music");
                genre.setGenrePhoto("/images/alternative.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrnb==true){
                Genre genre = new Genre();
                genre.setGenreId(4);
                genre.setGenreName("R&B/Soul Music");
                genre.setGenrePhoto("/images/rnb.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolcountry==true){
                Genre genre = new Genre();
                genre.setGenreId(5);
                genre.setGenreName("Country Music");
                genre.setGenrePhoto("/images/country.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolhouse==true){
                Genre genre = new Genre();
                genre.setGenreId(6);
                genre.setGenreName("House Music");
                genre.setGenrePhoto("/images/house.png");
                videoService.saveGenre(genre);
                genre=null;
            }



            if(boolreggae==true){
                Genre genre = new Genre();
                genre.setGenreId(7);
                genre.setGenreName("Reggae Music");
                genre.setGenrePhoto("/images/reggae.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolrelig==true){
                Genre genre = new Genre();
                genre.setGenreId(8);
                genre.setGenreName("Religious Music");
                genre.setGenrePhoto("/images/religious.png");
                videoService.saveGenre(genre);
                genre=null;
            }

            if(boolhiphop==true){
                Genre genre = new Genre();
                genre.setGenreId(9);
                genre.setGenreName("Hip-Hop/Rap Music");
                genre.setGenrePhoto("/images/hiprap.png");
                videoService.saveGenre(genre);
                genre=null;
            }

        }


        return "testing";
    }

    @RequestMapping("/changeGenre")
    public String refineGenre(){

        ArrayList<VideoDetails> videos = videoService.findAllVideoDetails();
        for(VideoDetails vid: videos){
            if(vid.getGenre().toString().toLowerCase().contains("pop")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Pop Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("rock") || vid.getGenre().toString().toLowerCase().contains("metal")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Rock Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("alternative")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Alternative Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("r&b") || vid.getGenre().toString().toLowerCase().contains("soul")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "R&B/Soul Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("country")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Country Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("house")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "House Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("reggae")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Reggae Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("religious")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Religious Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

            if(vid.getGenre().toString().toLowerCase().contains("hip-hop/rap")){
                VideoDetails videt = new VideoDetails(vid.getVideoid(), vid.getTitle(), vid.getArtist(), vid.getDate(), "Hip-Hop/Rap Music", vid.getViewCount(), vid.getLikes(), vid.getDislikes());
                videoService.saveVideoDetails(videt);
            }

        }
        return "testing";
    }


}