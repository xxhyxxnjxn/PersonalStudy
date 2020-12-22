package com.spring.member.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.spring.api.bithumb.service.BithumbApiService;
import com.spring.api.upbit.service.UpbitApiService;
import com.spring.coinoneh.api.service.CoinoneApiService;
import com.spring.login.service.KakaoLoginService;
import com.spring.login.vo.KakaoAccessTokenVo;
import com.spring.login.vo.KakaoUserInfoVo;
//import com.spring.bithumb.api.service.ApiService;
import com.spring.member.service.MemberService;
import com.spring.member.vo.ApiVo;
import com.spring.member.vo.MemberVo;
//import com.spring.mypage.service.MypageService;
//import com.spring.upbit.api.Api;

import okhttp3.OkHttpClient;
import okhttp3.Request;


@Controller
public class MemberController{
   

   
   @RequestMapping(value="/googleAuth", method=RequestMethod.GET)
   public String googleAuth(HttpSession session,Model model, @RequestParam(value = "code") String authCode) throws JsonProcessingException{
	   System.out.println(authCode); // 로그인 과정으로 얻은 code값
	 		
	   // JsonNode트리형태로 토큰받아온다
       JsonNode jsonToken = apiservice.getGoogleAccessToken(authCode);
       // 여러 json객체 중 access_token을 가져온다
       
       accessToken = jsonToken.get("access_token");

       System.out.println("access_token : " + accessToken);
       
       session.setAttribute("google_accessToken", accessToken.toString());
       
       //accessToken을 이용하여 사용자 정보를 얻어온다.
       JsonNode userInfo = apiservice.getGoogleUserInfo(accessToken.toString());

       //System.out.println(userInfo);
       
       String google_id = userInfo.path("id").asText();
       String google_name = userInfo.path("name").asText();
       String google_email = userInfo.path("email").asText();
       
       System.out.println("id : " + google_id);
       System.out.println("name : " + google_name);
       System.out.println("email : " + google_email);
       
   
       
   }
   
    
}