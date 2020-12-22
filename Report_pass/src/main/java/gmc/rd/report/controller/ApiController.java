package gmc.rd.report.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.service.ApiService;

@Controller
public class ApiController {
	
	@Autowired
	ApiService apiService;
	ModelAndView mv = new ModelAndView();
	//apikey 생성 페이지
	@GetMapping("/apikey")
	public ModelAndView ApiKey(@AuthenticationPrincipal PrincipalDetail principal) {
		
		mv.addObject("memId",principal.getUsername());
		mv.setViewName("mem/apikey");
		return mv;
	}
	//apikey 생성
	@PostMapping("/apikey/insert")
	public void insertApiKey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,ApiDto apiDto) {
		apiDto.setMemId(principal.getUsername());
		apiService.insertApiKey(apiDto);
		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//apikey 업데이트 페이지	
	@PostMapping("/apikey")
	public ModelAndView updateApiKey(ApiDto apiDto) {

		apiDto = apiService.selectApiKey(apiDto);

		mv.addObject("apiDto",apiDto);
		mv.setViewName("mem/apikeyupdate");
		return mv;
	}
	//apikey 생성
	@PostMapping("/apikey/update")
	public void updateApiKey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response,ApiDto apiDto) {
		apiDto.setMemId(principal.getUsername());
		apiService.updateApiKey(apiDto);
		
		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@PostMapping("/apikey/site")
	public void selectApiKey(@AuthenticationPrincipal PrincipalDetail principal,HttpServletResponse response, ApiDto apiDto) {
		apiDto.setMemId(principal.getUsername());
		apiDto = apiService.selectApiKey(apiDto);
		try {
			response.getWriter().print(apiDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/apikey/auth")
	public void authApikey(@AuthenticationPrincipal PrincipalDetail principal, HttpServletResponse response, ApiDto apiDto) {
		apiDto.setMemId(principal.getUsername());
      	String result = apiService.authApikey(apiDto);

		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
//	@RequestMapping("//mem/apikey/")
//	public void scriptlist(HttpServletRequest request, HttpServletResponse response) throws Exception{
//	JSONArray jsonArray = scriptService.getScriptList();	
//	response.getWriter().print(jsonArray);
//	}

	
}