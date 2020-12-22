package gmc.rd.report.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import gmc.rd.report.entity.User;
import gmc.rd.report.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService{
	@Autowired
	private UserRepository userRepository;	
	
	// 스프링이 로그인요청을 가로챌때 , username, password라는 변수 2개를 가로채는데
	// password 부분 처리는 알아서함.
	// username 이 db에 있는지만 확인해주면됨.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByMemId(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : "+username);
				});
		return new PrincipalDetail(principal);
		//시큐리티의 세션에 유저정보가 저장이 됨.
	}
	
}
