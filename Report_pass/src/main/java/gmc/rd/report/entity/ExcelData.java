package gmc.rd.report.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelData {

	  private String site;
	  private String transactionDate;
	  private String orderId;
	  private String type;
	  private String currency;
	  private String units;
	  private String price;
	  private String totalPrice;
	  private String fee;
	  
}