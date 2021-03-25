package gmc.rd.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //User클래스가 MySQL에 테이블이 생성이 된다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//빌더 패턴!!
public class RevenueState {
	@Id // primary key
	@GeneratedValue (strategy = GenerationType.IDENTITY)// 프로젝트에서 연결된 DB 의 넘버링 전략을 따라간다.
	private int idx;
	@Column(nullable = false, length = 100)
	private String memId;
	@Column(nullable = false, length = 100)
	private String site;
	@Column(nullable = false, length = 100)
	private boolean state;
	@Column(nullable = true, length = 100)
	private String num;
	@Column(nullable = true, length = 100)
	private String totalNum;

}
