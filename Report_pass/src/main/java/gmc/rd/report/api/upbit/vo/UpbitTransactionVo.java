package gmc.rd.report.api.upbit.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpbitTransactionVo {
	
	@JsonProperty(value="orderId")
	private String uuid;
	
	@JsonProperty(value="type")
	private String side;
	
	private String ord_type;
	
	@JsonProperty(value="price")
	private String price;
	
	private String state;
	
	@JsonProperty(value="currency")
	private String market;
	
	@JsonProperty(value="transactionDate")
	private String created_at;
	
	private String volume;
	
	private String remaining_volume;
	
	private String reserved_fee;
	
	@JsonProperty(value="fee")
	private String paid_fee;
	
	private String locked;
	
	@JsonProperty(value="units")
	private String executed_volume;
	
	private String trades_count;
	
}
