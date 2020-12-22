package gmc.rd.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "api_tbl")
public class Api {
	@Id // primary key
	@GeneratedValue (strategy = GenerationType.IDENTITY)// 프로젝트에서 연결된 DB 의 넘버링 전략을 따라간다.
	private int idx;
	@Column(nullable = false, length = 100)
	private String memId;
	@Column(nullable = false, length = 100)
	private String site;
	@Column(nullable = false, length = 1000)
	private String apiKey;
	@Column(nullable = false, length = 1000)
	private String secretKey;
	

}
