package gmc.rd.report.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller//file자체를 리턴  데이터이동은rest는 문자자체를 리턴
public class TempControllerTest {
	
	//http://localhost:8080/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome(){
		System.out.println("tempHome()");
				return "/home.html";//정적파일 동적파일(jsp)는 못찾음
	}
	@GetMapping("/temp/jsp")
	public String tempJsp(){
		System.out.println("tempjsp()");
		//prefix 
		
		return "test";//정적파일 동적파일(jsp)는 못찾음
	}
	
}
