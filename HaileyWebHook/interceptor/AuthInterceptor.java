package com.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter{

	//prehandle : 컨트롤러보다 먼저 수행되는 메소드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		System.out.println("prehandle : " + request.getRequestURI());
		
		//session 객체를 가지고 온다
		HttpSession session = request.getSession();
		//로그인처리를 담당하는 사용자 정보를 담고 있는 객체를 가져옴
		Object object = session.getAttribute("login");
		
		switch (request.getRequestURI()) { //현재 파일정보
		case "/login":
		case "/loginProcess":
		case "/JoinForm":
		case "/Join/Join":
		case "/FindIDForm":
		case "/FindID":	
		case "/FindPWFrom":
		case "/FindPW":
		case "/ChangePw":
		case "/Join/CheckId":
			return true; //제외
		}
		if(object ==null) {
			response.sendRedirect("/login");
			return false; // 더이상 컨트롤러 요청으로 가지 않도록 false 리턴
		}
		
		//prehandle 의 return 은 컨프롤러 요청 uri 로 가도 되는지 허가하는 의미
		return true;
	}
	//posthandle : 컨트롤러가 수행되고 화면이 보여지기 직전에 수행되는 메소드
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}
}
