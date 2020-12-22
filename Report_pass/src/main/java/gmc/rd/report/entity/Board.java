package gmc.rd.report.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)//auto_increment
	private int id;
	
	@Column(nullable = false, length=100)
	private String title;
	
	@Id
	@Column(nullable = false, length=100)
	private String orderId;
	
	@Lob //대용량 데이터
	private String content ; // 썸머노트 라이브러리 <html>태그가 섞여서 디자인이 됨.

	@ManyToOne(fetch=FetchType.EAGER)//연관관계맺음 Many = Many, User =One
	@JoinColumn(name = "memId")
	private User user; //DB는 오프젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할수있다.
	
	@OneToMany(mappedBy="board",fetch=FetchType.LAZY)	//select 하기 위해서 잇는 것
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;

	
}
