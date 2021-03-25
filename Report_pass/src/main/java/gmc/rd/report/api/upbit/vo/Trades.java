package gmc.rd.report.api.upbit.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Trades {
	private String market;

	@JsonProperty(value="orderId")
	private String uuid;
	private String price;

	@JsonProperty(value="units")
	private String volume;
	private String funds;
	@JsonProperty(value="transactionDate")
	private String created_at;
	@JsonProperty(value="type")
	private String side;
	private String fee;
	private String currency;

}
