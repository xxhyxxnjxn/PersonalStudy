package gmc.rd.report.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReportDto {
	private int idx;	
	private String memId;
	private String site;
	private String orderId;
	private String transactionDate;
	private String currency;
	private String type;
	private String units;
	private String fee;
	private String avgPrice;
	private String price;
	private String totalPrice;
	private String revenue;
	private String clearRevenue;
	private String log;
	private String file;
	private String accUnits;
	
	private String bidUnitsCal;
	private String askUnitsCal;
	
	private String bidAvgPrice;
	private String askAvgPrice;
	
	private String bidTotalPriceCal;
	private String askTotalPriceCal;
	
	private String bidAccUnits;
	private String askAccUnits;
	
	private String totalRate;
	private String totalRateCal;
	private String expectIncome;
	private String incomeCal;
	private String income;
	private String cal;
	
}
