package gmc.rd.report.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import gmc.rd.report.entity.User;
import lombok.Getter;

// 스프링 시큐리티가 로그인요청을 가로채서 로그인을 진행하고 완료가되면 
//userdetails 타입의 오프젝트를 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다,
@Getter
public class PrincipalDetail implements UserDetails {
	
	private User user; //객체를 품고있는거  : 콤포지션 

	//이걸 꼭 만들어줘야 우리의 정보를 세션에 담을 수 있다!!
	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getMemId();
	}

	// 계정이 만료되자않았는지 리턴한다. (true 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	// 계정이 잠겨있지않았는지 리턴한다. (true 잠기지않음)
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	
	// 비밀번호가 만료되어있지않았는지 리턴한다. (true 만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	// 계정이 활성화인지 리턴한다(true: 활성화)
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	//계정이 갖고있는 권한을 리턴함.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		/*//자바 원래 함수
		collectors.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return "ROLE_"+user.getRole();// ROLE_USER 로 리턴해줌
			}
		});*/
		//람다식
		collectors.add(()->{return "ROLE_"+user.getRole();});
		
		return collectors;
	}

	
}
