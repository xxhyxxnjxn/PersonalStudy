package gmc.rd.report.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gmc.rd.report.dto.ResponseDto;
import gmc.rd.report.entity.RoleType;
import gmc.rd.report.entity.User;
import gmc.rd.report.service.UserService;

@RestController
public class UserApiController {
	

	@Autowired
	private UserService userService;
	
	@PostMapping("/auth/joinProc")
	 public ResponseDto<Integer> save(@RequestBody User user) {
		
	
		userService.회원가입(user);
		 return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
		}
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user) {
		userService.회원수정(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1 );
	}

	
	
	 
}
