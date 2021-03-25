package gmc.rd.report.api.coinone.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CoinoneCoinTransactionsDataVo {
	@JsonProperty(value="orderId")
	String txid;
	String type;
	String from;
	String to;
	String confirmations;
	@JsonProperty(value="units")
	String quantity;
	@JsonProperty(value="transactionDate")
	String timestamp;
}
