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
@Table(name = "reportco_tbl")
public class Report3 {
	@Column(nullable = false, length = 100)
	private int idx;

	@Column(nullable = false, length = 100)
	private String site;
	@Id // primary key
	@Column(nullable = false, length = 100)
	private String orderId;
	@Column(nullable = false)
	private String transactionDate;
	@Column(nullable = false, length = 100)
	private String currency;
	@Column(nullable = false, length = 100)
	private String type;
	@Column(nullable = true, length = 100)
	private String price;
	@Column(nullable = false, length = 100)
	private String units;
	
	@Column(nullable = true, length = 100)
	private String bidUnitsCal;
	@Column(nullable = true, length = 100)
	private String askUnitsCal;
	
	@Column(nullable = true, length = 100)
	private String bidAvgPrice;
	@Column(nullable = true, length = 100)
	private String askAvgPrice;
	
	@Column(nullable = false, length = 100)
	private String fee;
	
	@Column(nullable = true, length = 100)
	private String totalPrice;
	
	@Column(nullable = true, length = 100)
	private String bidTotalPriceCal;
	@Column(nullable = true, length = 100)
	private String askTotalPriceCal;
	
	@Column(nullable = true, length = 100)
	private String revenue;
	
	@Column(nullable = true, length = 100)
	private String accUnits;
	@Column(nullable = true, length = 100)
	private String bidAccUnits;
	@Column(nullable = true, length = 100)
	private String askAccUnits;
	
	@Column(nullable = true, length = 1000)
	private String log;
	@Column(nullable = true, length = 1000)
	private String file;
	@Column(nullable = true, length = 1000)
	private String cal;
	
	//
//	@Column(nullable = true, length = 1000)
//	private String bidTotal;
	@Column(nullable = true, length = 1000)
	private String totalRate;
	@Column(nullable = true, length = 1000)
	private String totalRateCal;
	@Column(nullable = true, length = 1000)
	private String expectIncome;
	@Column(nullable = true, length = 1000)
	private String incomeCal;
	@Column(nullable = true, length = 1000)
	private String income;
	
	@ManyToOne(fetch=FetchType.EAGER)//연관관계맺음 Many = Many, User =One
	@JoinColumn(name = "memId")
	private User user; //DB는 오프젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할수있다.

}
