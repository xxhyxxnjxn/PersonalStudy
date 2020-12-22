package gmc.rd.report.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 사용자가 요청 -> 응답 (Html 파일)
// @Controller

// 사용자가 요청 -> 응답 (Data)
@RestController
	// 인터넷브라우저 요청은 get요청만 가능하다. 그래서 사용하는것이 postman
public class HttpControllerTest {
	
	private static final String TAG ="HttpControllerTest : ";	
	
	public String lombokTest() {
		Member m = new Member(1,"ssar","1234","email");
		System.out.println(TAG+"getter : "+m.getId());
		m.setId (500);
		System.out.println(TAG+"setter : "+m.getId());
		return "lombok test 완료";
	}
	
	
	@GetMapping("/http/get")//select
		public String getTest(Member m ) {

			return "get요청: "+m.getId()+" "+m.getUsername()+" "+m.getEmail()+" "+m.getPassword();
		}
	@PostMapping("/http/post")//insert // data을 application/json
		public String postTest(@RequestBody Member m) { //application/json 을 자동으로
														//MessageConvertor( 스프링부트)가 일을 대신해주는 역할을 함.
			return "post요청"+m.getId()+" "+m.getUsername()+" "+m.getEmail()+" "+m.getPassword();
		}
	@PutMapping("/http/put")//update
		public String putTest(@RequestBody Member m) {
			return "put요청"+m.getId()+" "+m.getUsername()+" "+m.getEmail()+" "+m.getPassword();
		}
	@DeleteMapping("/http/delete")//delete
		public String deleteTest() {
			return "delete요청";
		}
		
		
	
}
