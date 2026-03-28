package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @PostMapping("/1")
    public String test1(){
        return "Test api 1";
    }

    @GetMapping("/2")
    public String test2(){
        return "Test api 2";
    }
}
