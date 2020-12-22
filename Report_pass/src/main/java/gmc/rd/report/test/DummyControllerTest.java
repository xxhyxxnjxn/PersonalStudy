package gmc.rd.report.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import gmc.rd.report.entity.RoleType;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.UserRepository;

@RestController
public class DummyControllerTest {

	@Autowired // 의존성주입 (DI)
	private UserRepository userRepository;
	
	//delete 
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			return "삭제가 실패하였습니다. 해당 id는 데이터가 없습니다.";
		}

		return "삭제되었습니다"+id;	//없는 id 가 있으면 위험하다.
	}
	
	
	
	//email, password
	//Update
	//save 함수는 id를 전달하지않으면 insert를 해주고
	//id를 전달하면 해당id에 대한 데이터가 없으면 insert를해요
	//id를 전달하면 해당id에 대한 데이터가 없으면 update를해요
	@Transactional //save를 호출하지않앗는데 변경됨 : dirty checking 함수종료시 자동 commit된다.
	@PutMapping("/dummy/user/{id}")	
	public User udateUser(@PathVariable int id,@RequestBody User requestUser) {	// json 데이터 요청 -> Java Object (Message Converter)
		System.out.println("id:" + id);
		System.out.println("password:" + requestUser.getPassword());
		System.out.println("email:" + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하엿습니다.");
			//user에는 null이없으므로 제대로 수행됨.ㅋ
		});
		 user.setPassword(requestUser.getPassword());
		 user.setEmail(requestUser.getEmail());
		//requestUser.setId(id);
		//requestUser.setUsername("ssar");
		//userRepository.save(requestUser)		;//원래 insert에 넣어주는 것 하지만 다른 값들이 null로 변경되는 문제가 생김
		//userRepository.save(user);
		//더티체킹 : save를 하지않아도 update가 된다.
		return user;
	}
	
	//select
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id",direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = (Page<User>) userRepository.findAll();
		
		if(pagingUser.isFirst()) {
			
		}
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	//{id}주소로 파라미터를 전달받을수있음
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user/4 를 찾으면 내가 데이터베이스에서 못찾아오게되면 user가 null이 될것아냐?
		//그럼 retuyn null 이 되잖아 Optional 로 너의 user객체를 감싸서 가져올테니 null 인지 아닌지 판단해서 return해
	/*	User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
			//인터페이스가 들고있는 함수를 들고있는데 추상매서드이기 때문에 익명클래스를 만들어야하므로 overriding해줘야함.
			@Override
			public User get() {
				return new User();
			}// 빈객체를 넣어줌 : null은 아니게됨.ㅋ 그런데 사람들이 더원하는게 밑에 있음.
		});*/
		User user1 = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {

			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : "+id);
			}
			
		});
		//람다식
		/*
		User user2 = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("해당사용자는 없습니다.");
		});	*/
		
		return user1;
	}

	//http 의 body의 username, password, email을 가지고 데이터 요청
	@PostMapping("dummy/join")
	public String join (User user) {
		user.setRole(RoleType.USER);
		userRepository.save(user);
	return "회원가입이 완료되었습니다";
	}
	
}
