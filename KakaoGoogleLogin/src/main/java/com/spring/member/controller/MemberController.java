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
import com.spring.api.bithumb.service.ApiService;
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
   
	
   @Autowired
   MemberService memberService;
   
   @Autowired
   ApiService apiservice;
   
   //@Autowired
   //ApiService apiService;
   
   //@Autowired
   //private MypageService mypageService;
   final static String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
	final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
	final static String GOOGLE_REVOKE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/revoke";
   
   private JsonNode accessToken;
   
   @RequestMapping("/")
   public ModelAndView home(HttpServletRequest request) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      HttpSession session = request.getSession();
      System.out.println("세션" + request.getSession());
      MemberVo mem = (MemberVo) session.getAttribute("login");
      mem.getM_id();
      System.out.println("야야잉아이디다"+mem.getM_id());
      ModelAndView mv = new ModelAndView();
      mv.setViewName("home");
      return mv;
   }
   
   @RequestMapping(value="/login", method=RequestMethod.GET)
   public String loginform(HttpSession session) {
	  session.invalidate();
      return "login/loginForm";
   }
   
   @RequestMapping("/kakaologinForm")
   public String kakaologinForm(HttpServletRequest request) {
	   HttpSession session = request.getSession();
	   session.invalidate();
		
      return "kakao/kakaologinForm";

   }
   
   @RequestMapping(value="/kakaologin", method=RequestMethod.GET)
   public String kakaologin(HttpSession session,@RequestParam(value = "code", required = false) String code) {
	   //JsonNode accessToken;
	   
       // JsonNode트리형태로 토큰받아온다
       JsonNode jsonToken = apiservice.getKakaoAccessToken(code);
       // 여러 json객체 중 access_token을 가져온다
       accessToken = jsonToken.get("access_token");

       System.out.println("access_token : " + accessToken);
       
       //HttpSession session = request.getSession();
       session.setAttribute("accessToken", accessToken.toString());
       
    // access_token을 통해 사용자 정보 요청
       JsonNode userInfo = apiservice.getKakaoUserInfo(accessToken.toString());

       System.out.println(userInfo);

    // Get id
       String id = userInfo.path("id").asText();
       String name = null;
       String email = null;

       // 유저정보 카카오에서 가져오기 Get properties
       JsonNode properties = userInfo.path("properties");
       JsonNode kakao_account = userInfo.path("kakao_account");

       name = properties.path("nickname").asText();
       email = kakao_account.path("email").asText();

       System.out.println("id : " + id);
       System.out.println("name : " + name);
       System.out.println("email : " + email);
       
       HashMap<String,Object> kakao_hash = new HashMap<String,Object>();
       
       kakao_hash.put("kakao_id", id);
       MemberVo getMem_Kakao=memberService.getMem_Kakao(kakao_hash);
       if(getMem_Kakao == null) {
    	   session.setAttribute("kakao_id", id);
    	   session.setAttribute("kakao_name", name);
    	   session.setAttribute("kakao_email", email);
    	   return "join/kakao_joinForm";
       }else {
    	   session.setAttribute("login", getMem_Kakao);
           session.setAttribute("id", getMem_Kakao.getM_id());
           System.out.println("ddsfeffwsfewsf"+getMem_Kakao);
           return "home";
       }
       
   }
   
   @RequestMapping(value="/googleAuth", method=RequestMethod.GET)
   public String googleAuth(HttpSession session,Model model, @RequestParam(value = "code") String authCode) throws JsonProcessingException{
	   System.out.println(authCode);
	 		
	   // JsonNode트리형태로 토큰받아온다
       JsonNode jsonToken = apiservice.getGoogleAccessToken(authCode);
       // 여러 json객체 중 access_token을 가져온다
       //System.out.println(jsonToken);
       accessToken = jsonToken.get("access_token");

       System.out.println("access_token : " + accessToken);
       
       session.setAttribute("google_accessToken", accessToken.toString());
       
       JsonNode userInfo = apiservice.getGoogleUserInfo(accessToken.toString());

       //System.out.println(userInfo);
       
       String google_id = userInfo.path("id").asText();
       String google_name = userInfo.path("name").asText();
       String google_email = userInfo.path("email").asText();
       
       System.out.println("id : " + google_id);
       System.out.println("name : " + google_name);
       System.out.println("email : " + google_email);
       
       HashMap<String,Object> google_hash = new HashMap<String,Object>();
       
       google_hash.put("google_id", google_id);
       MemberVo getMem_Kakao=memberService.getMem_Kakao(google_hash);
       if(getMem_Kakao == null) {
    	   session.setAttribute("google_id", google_id);
    	   session.setAttribute("google_name", google_name);
    	   session.setAttribute("google_email", google_email);
    	   return "join/kakao_joinForm";
       }else {
    	   session.setAttribute("login", getMem_Kakao);
           session.setAttribute("id", getMem_Kakao.getM_id());
           System.out.println("ddsfeffwsfewsf"+getMem_Kakao);
           return "home";
       }
       
   }
   
   //로그인
   @RequestMapping(value="/loginProcess", method=RequestMethod.POST)
   public String loginProcess(HttpSession session, Model model,@RequestParam HashMap<String, Object>map) {
            
      String   returnURL ="";
      if(session.getAttribute("login")!= null) {
         session.removeAttribute("login");
      }
      MemberVo vo = memberService.login(map);
      if(vo !=null) {
         session.setAttribute("login", vo);
         session.setAttribute("id", vo.getM_id());
         System.out.println("ddsfeffwsfewsf"+vo);
         returnURL ="redirect:/";
         
      }else {
    	 model.addAttribute("message", "아이디나 비밀번호가 틀렸습니다.");
         returnURL = "login/loginForm";
      }
      return returnURL;
      	
   }
   //로그아웃
   @RequestMapping("/logout")
   public String logout(HttpSession session) {
	  //그냥 아이디로 로그인 할 시에 주석 해제
	   
      session.removeAttribute("login");
      //session.invalidate();
      
	  if(session.getAttribute("accessToken")!=null) {      
      JsonNode userInfo = apiservice.kakaoLogout(session.getAttribute("accessToken").toString());

      System.out.println(userInfo);
      
      session.removeAttribute("accessToken");
      session.removeAttribute("kakao_id");
      
	  }
	  
	  session.invalidate();
      return "redirect:/";
   }
   
   //회원가입 구역
   @RequestMapping("/JoinForm") 
   public String joinForm() { 
      return "join/joinForm"; 
   }
   
   @RequestMapping("/Join/Join") // 값 넣기
   public ModelAndView join(@RequestParam HashMap<String, Object> map) {
      System.out.println("join모델엔뷰"+map);
      memberService.setJoin(map);
  
      ModelAndView mv = new ModelAndView();
      
      mv.setViewName("login/loginForm");
      return mv;
   }
   
   @RequestMapping("/Join/kakao_Join") // 값 넣기
   public String kakao_Join(@RequestParam HashMap<String, Object> map,HttpServletRequest request) {
	  map.put("kakao_id", (String) request.getSession().getAttribute("kakao_id"));
      System.out.println("join모델엔뷰"+map);
      memberService.setJoin_kakao(map);
  
      return "login/loginForm";
   }
	
	
	  @RequestMapping("/Join/CheckId") // 아이디 체크
	  @ResponseBody
	  public String checkId(@RequestParam HashMap<String, Object> map) {
		  System.out.println("checked id : "+map);
		  String vo = memberService.checkId(map); 
		  System.out.println("final :" + vo); 
		  
		  return vo; 
		  
	  }
	 
	 
   //회원가입 구역 끝
   
	 //아이디 비밀번호 찾는 구역
	 @RequestMapping("/FindIDForm")
   public ModelAndView findIdForm() {
      System.out.println("findform모델엔뷰");
      ModelAndView mv = new ModelAndView();
      mv.setViewName("join/findIdForm");
      return mv;   
   }
   @RequestMapping("/FindID")
   public ModelAndView findId(@RequestParam HashMap<String, Object> map) {
      System.out.println("find모델엔뷰" + map);
      ModelAndView mv = new ModelAndView();
      
      MemberVo vo = memberService.getFindId(map);
      System.out.println("findid모델뷰에 " + vo);
      
      if(vo ==null) {
         mv.setViewName("join/findIdForm");
      }
      else {
         mv.addObject("find",vo);
         mv.setViewName("join/findId");
      }
      //memberService.setFindId(map);
      return mv;
   }
   
   @RequestMapping("/FindPWFrom")
   public ModelAndView findPwFrom() {
      System.out.println("findpwform모델엔뷰");
      ModelAndView mv = new ModelAndView();
      mv.setViewName("join/findPWForm");
      return mv;
   }
   @RequestMapping("/FindPW")
   public ModelAndView findPw(@RequestParam HashMap<String, Object> map) {
      System.out.println("findPw모델엔뷰" + map);
      ModelAndView mv = new ModelAndView();
         
      MemberVo vo = memberService.getFindPw(map);
      System.out.println("findPw모델뷰에 " + vo);
      if(vo ==null) {
         mv.setViewName("join/findPWForm");
         System.out.println();
      }
      else {
         mv.addObject("findpw",vo);
         mv.setViewName("join/findPw");
         
      }
      //memberService.setFindId(map);
      return mv;
   }
   
   @RequestMapping("/ChangePw")
   public ModelAndView changePw(@RequestParam HashMap<String, Object> map) {
      ModelAndView mv = new ModelAndView();
      System.out.println("ChangePw 모델엔뷰 :"+map);
      memberService.setChangePw(map);
      mv.setViewName("login/loginForm");
      return mv;
   }
   
   //api 키 
   @RequestMapping("/ApiUpdate")
   public ModelAndView ApiUpdate(@RequestParam HashMap<String, Object> map) {
	   List<ApiVo> infoApiList = memberService.showApiKey(map);
	   ModelAndView mv = new ModelAndView();
	   System.out.println(infoApiList);
	   mv.addObject("infoApiList", infoApiList); // 데이터를 저장
	   mv.setViewName("Info/updateInfo");
	   return mv;
   }
   
   
   @RequestMapping("/Inputapi")
   public ModelAndView Inputapi(@RequestParam HashMap<String, Object> map, HttpServletRequest request) {
	   HttpSession session = request.getSession();
	   MemberVo mem = (MemberVo) session.getAttribute("login");
	   mem.getM_id();
	   ModelAndView mv = new ModelAndView();
	   memberService.setApi(map);
	   mv.setViewName("redirect:/ApiUpdate?m_id="+mem.getM_id());
	   return mv;
   }
   
   ////////////추가 api 수정 삭제 part
   @RequestMapping("/UpdateapiForm") 
   public String UpdateapiForm() { 
      return "Info/updateAPI"; 
   }
   
   @RequestMapping("/Updateapi")
   public String Updateapi(@RequestParam HashMap<String, Object> map, HttpServletRequest request) {
	   
	   String m_id = (String) request.getSession().getAttribute("id");
	   map.put("m_id", m_id); 
	   
	   System.out.println("api test : "+map);
	   memberService.updateApiKey(map);
	   
	  return "Info/updateAPI"; 
   }
   
   
   @RequestMapping("/Deleteapi")
   public String Deleteapi(@RequestParam HashMap<String, Object> map, HttpServletRequest request) {
	   //System.out.println("delete test : "+map);
	   memberService.deleteApiKey(map);

	   return "redirect:/";
   }
   
   @RequestMapping("/Info/CheckConnect") // apiKey 체크
	  @ResponseBody
	  public String CheckConnect(@RequestParam HashMap<String, Object> map) {
		  System.out.println("checked connect : "+map);
		  String vo = memberService.checkConnect(map); 
		  System.out.println("final :" + vo); 
		  
		  return vo; 
		  
	  }
   
   @RequestMapping("/Info/CheckSecret") // secretKey 체크
	  @ResponseBody
	  public String CheckSecret(@RequestParam HashMap<String, Object> map) {
		  System.out.println("checked secret : "+map);
		  String vo = memberService.checkSecret(map); 
		  System.out.println("final :" + vo); 
		  
		  return vo; 
		  
	  }
   
   @RequestMapping("/Info/CheckSite") // sitenameKey 체크
	  @ResponseBody
	  public String CheckSite(@RequestParam HashMap<String, Object> map, HttpServletRequest request) {
	   
	   	  String m_id = (String) request.getSession().getAttribute("id");
	   	  map.put("m_id", m_id); 
	   
		  System.out.println("checked sitename : "+map);
		  String vo = memberService.checkSite(map); 
		  System.out.println("final :" + vo); 
		  
		  return vo; 
		  
	  }
   

	/*
	 * @RequestMapping("/APICheck_Bithumb")
	 * 
	 * @ResponseBody public void APICheck_Bithumb(String coin,String connect,String
	 * secret, HttpServletResponse response) { HashMap<String, String> upbit = new
	 * HashMap<String, String>(); upbit.put("market", coin); upbit.put("accessKey",
	 * connect); upbit.put("secretKey", secret);
	 * 
	 * 
	 * try { String json = apiService.showBalance(coin,connect,secret);
	 * 
	 * response.getWriter().print(json); } catch (IOException e) {
	 * e.printStackTrace(); } }
	 * 
	 * @RequestMapping("/APICheck_Upbit")
	 * 
	 * @ResponseBody public void APICheck_Upbit(String coin,String connect,String
	 * secret, HttpServletResponse response) { HashMap<String, String> upbit = new
	 * HashMap<String, String>(); upbit.put("market", coin); upbit.put("accessKey",
	 * connect); upbit.put("secretKey", secret);
	 * 
	 * 
	 * try { String json=""; try { json = Api.getOrdersChance2(upbit); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * response.getWriter().print(json); }catch (IOException e) {
	 * e.printStackTrace(); } }
	 */
   
    
}