package gmc.rd.report.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import gmc.rd.report.entity.User;
import gmc.rd.report.service.UserService;

@Controller
public class UserController {
   @Autowired
   private UserService userService;
   //인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
   // 그냥 주소가 /이면 index.jsp 허용
   // static이하에 있는 /js/**, /css/**, /image/**
   
   @GetMapping("/auth/joinForm")
   public String joinForm() {
         
      return "user/joinForm";
   }
   @GetMapping("/auth/loginForm")
   public String loginForm() {
      
      return "user/loginForm";
   }
   @GetMapping("/user/updateForm")
   public String updateForm() {
      return "user/updateForm";
   }

   @GetMapping("/auth/userId")
   public void selectMemId(HttpServletResponse response, User user) {
      user = userService.selectMemId(user);
      
   }
   
   
}