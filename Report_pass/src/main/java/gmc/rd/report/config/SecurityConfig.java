package gmc.rd.report.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.config.auth.PrincipalDetailService;

//빈등록 : 스프링 컨테이너에서 객체를 관리할 수 있게하는 것
@Configuration	// 빈등록 (loC 관리)
@EnableWebSecurity // Security라는 필터를 추가 = 스프링 시큐리티가 활성화가 되어있는데  어떤 설정을 해당 파일에서 하겠다. = 시큐리티 필터가 등록
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private PrincipalDetailService principalDetailService;
	
	// 비밀번호 암호화하기
	@Bean// loC가 되요
	public BCryptPasswordEncoder encodePWD() {
		String encPassword = new BCryptPasswordEncoder().encode("1234");
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 db에 있는 해쉬랑 비교할 수 있음.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.csrf().disable() //csrf 토큰 비활성화 (테스트시 걸어두는게 제일좋음)
			.authorizeRequests()
				.antMatchers("/auth/**","/js/**","/css/**","/image/**","/","/vm")	//해당주소는 인증이 안되도 들어갈수잇고 이 경로에 포함되지않는 모든 페이지는 인증이 되고 나서야 들어갈수있다.
				.permitAll()
				.anyRequest()
				.authenticated()
			.and()
		 		.formLogin()
		 		.loginPage("/auth/loginForm")
		 		.loginProcessingUrl("/auth/loginProc")//스프링 시큐리티가 해당주소로 요청오는 로그인을 가로채서 대신 로그인해준다.
				.defaultSuccessUrl("/getList")//로그인끝나면 어디로갈까? 인증되지않은 것 정상적으로 되면 온다.
				//실패하게 되면 failurl
				;
	}
}
