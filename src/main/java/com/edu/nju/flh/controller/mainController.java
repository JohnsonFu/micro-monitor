package com.edu.nju.flh.controller;

/**
 * Created by fulinhua on 2018/3/24.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class mainController {

        @RequestMapping("/")
        String home() {
            return "chart";
        }


    }
