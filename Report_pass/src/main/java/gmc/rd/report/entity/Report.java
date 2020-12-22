package gmc.rd.report.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reportbi_tbl")
public class Report {
	@Column(nullable = false, length = 100)
	private int idx;

	@Column(nullable = true, length = 100)
	private String site;
	@Id // primary key
	@Column(nullable = false, length = 100)
	private String orderId;
	@Column(nullable = true)
	private String transactionDate;
	@Column(nullable = true, length = 100)
	private String currency;
	@Column(nullable = true, length = 100)
	private String type;
	@Column(nullable = true, length = 100)
	private String price;
	@Column(nullable = true, length = 100)
	private String units;
	@Column(nullable = true, length = 100)
	private String avgPrice;
	@Column(nullable = true, length = 100)
	private String fee;
	@Column(nullable = true, length = 100)
	private String totalPrice;
	@Column(nullable = true, length = 100)
	private String revenue;
	@Column(nullable = true, length = 100)
	private String accUnits;
	@Column(nullable = true, length = 100)
	private String clearRevenue;
	@Column(nullable = true, length = 1000)
	private String log;
	@Column(nullable = true, length = 1000)
	private String file;

	@ManyToOne(fetch=FetchType.EAGER)//연관관계맺음 Many = Many, User =One
	@JoinColumn(name = "memId")
	private User user; //DB는 오프젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할수있다.

}
