package gmc.rd.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.RoleType;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 bean에 등록을 해줌 loC를 해준다.
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public void  회원가입(User user) {
		String rawPassword = user.getPassword();// 1234dnjsans
		String encPassword = encoder.encode(rawPassword);//해쉬
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	@Transactional
	public void 회원수정(User user) {
		//수정시에는 영속성 컨텍스트 user오브젝트를 영속화 시키고, 영속화된 user오브젝트를 수정
		//select해서 User오브젝트를 db로 가져오는 이유는 영속화하기 위해서
		//영속화된 오브젝트를 변경하면 자동으로 db에서 update문을 날려줌
		User persistance = userRepository.findByMemId(user.getMemId()).orElseThrow(()->{
			return new IllegalArgumentException("회원찾기 실패");
		});
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		persistance.setPassword(encPassword);
		persistance.setEmail(user.getEmail());
		persistance.setName(user.getName());
		persistance.setPhone(user.getPhone());
		//회원수정 함수 종료시 = 서비스 종료 = 트렌젝션 종료 = commit이 자동으로 됩니다
		//영속화된 persistance객체의 변화가 감지되면 더티체킹이 되어 update문을 날려줌
	}


}
