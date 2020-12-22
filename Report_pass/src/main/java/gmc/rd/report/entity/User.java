package gmc.rd.report.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//orm
@Entity //User클래스가 MySQL에 테이블이 생성이 된다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//빌더 패턴!!
public class User {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)//프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int idx;   //시퀀스, auto_increment
	@Id  //primaryKey
	@Column(nullable=false, length=30,unique=true) // 아이디
	private String memId;
	@Column(nullable=false, length=100)  // 비밀번호를 해쉬로 변경해서 암호화해야하므로 넉넉
	private String password;
	@Column(nullable=false, length=50) 
	private String email;
	@Column(nullable=false, length=50) 
	private String name;
	@Column(nullable=false, length=100) 
	private String phone;
	
	//db는 roletype이 없으므로 
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum을 쓰는게 좋다 : ADMIN, USER
	// admin,user,manager 권한을 주면 됨. 도메인이 정해짐 : 머가 정해졌다는 의미 .
	
	@CreationTimestamp // 시간이 자동입력
	private Timestamp createDate;
	
}
