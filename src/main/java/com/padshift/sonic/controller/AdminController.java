package com.padshift.sonic.controller;

import com.padshift.sonic.entities.Genre;
import com.padshift.sonic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by ruzieljonm on 26/09/2018.
 */
@Controller
public class AdminController {

    @Autowired
    VideoService videoService;

    @Autowired
    GracenoteAPIController gracenoteAPIController;

    @RequestMapping("/adminHomePage")
    public String showAdminHomePage(){
        return "HomePageAdmin";
    }


    @RequestMapping(value = "/querypage", method = RequestMethod.GET)
    public String showQueryPage(Model model){
        ArrayList<Genre> genres =  videoService.findAllGenre();
        model.addAttribute("genre", genres);
        return "QueryAdmin";
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public String config(HttpServletRequest request, Model model){
        String configChoice = request.getParameter("config");
        System.out.println(configChoice);
        if(configChoice.equals("1")){
             return showQueryPage(model);
        }else if(configChoice.equals("2")){
            System.out.println("GOOD MORNING");
             return gracenoteAPIController.showmetadata();
        }else{
            return "testing";
        }
    }

    @RequestMapping(value = "/byartist", method = RequestMethod.POST)
    public String byArtist(){
        return "ByArtist";
    }


    @RequestMapping(value = "/bysinglevideo", method = RequestMethod.POST)
    public String bySingleVideo(){
        return "BySingleVideo";
    }







}
