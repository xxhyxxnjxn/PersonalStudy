package gmc.rd.report.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.User;
//findall -모든 것을 리턴함, findbyId
//DAO
//자동으로 bean등록 가능 : spring io를 할수잇음
//@Repository 생략가능
public interface UserRepository extends JpaRepository<User, Integer> {
//기본적인 crud함수를 다들고있음
	//SELECT * FROM user WHERE username=1?;
	Optional<User> findByMemId(String memId);

}
//JPA Naming 전략
//select * from user where username =?첫번쨰 and password =?두번째;

//@Query(value="SELECT * FROM user WHERE username=? AND password =?",nativeQuery = true)
//User login(String username, String password);
