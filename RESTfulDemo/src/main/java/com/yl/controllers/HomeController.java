package com.yl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 *
 *
 * </pre>
 * <p/>
 * Created by luxiaohu at 16/9/23 15:46
 */
@Controller
public class HomeController {


    @RequestMapping(value = "/")
    @ResponseBody
    public String home(){

        String p = "GET: /pies //Returns all Pies in the database\n </br>" +
                  "GET: /pies/{id} //Returns a specific Pie by its ID\n</br>" +
                 "GET: /pies?name={name} //Returns a list of pies that matches the name\n</br>" +
                 "POST: /pies //Create a new Pie and add it to the database\n</br>";

        return p;

    }



}
