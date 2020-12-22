package gmc.rd.report.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//스프링이 com.cos.blog패키지 이하를 스캔해서 메모리를 new 하는 것은 아니구요
					// 특정 어노테이션이 붙어있는 클래스 클래스파일을 new 해서 (loC)스프링 컨테이너에 관리해줍니다.
//http://localhost:8080/test/hello
@RestController
public class BlogControllerTest {
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello Spring boot</h1>";
	}
}
