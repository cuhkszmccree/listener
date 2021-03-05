package com.example.listener.Service;

import com.example.listener.Config.Constant;
import org.springframework.stereotype.Component;

@Component
public class Handler {
    public void handle_Q1(String message){
        System.out.println(Constant.Queue1 + " has used " + message);
    }

    public void handle_Q2(String message){
        System.out.println(Constant.Queue2 + " has used " + message);
    }

    public void handle_Q3(String message){
        System.out.println(Constant.Queue3 + " has used " + message);
    }

    public void handle_Q4(String message){
        System.out.println(Constant.Queue4 + " has used " + message);
    }
}
