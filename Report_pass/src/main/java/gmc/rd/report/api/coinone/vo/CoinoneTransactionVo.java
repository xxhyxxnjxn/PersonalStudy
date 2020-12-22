package gmc.rd.report.api.coinone.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;
import lombok.Builder;

@Builder
public class CoinoneTransactionVo {
	
	@JsonProperty(value="transactionDate")
	private String timestamp;
	
	@JsonProperty(value="price")
	private String price;
	
	@JsonProperty(value="type")
	private String type;
	
	@JsonProperty(value="units")
	private String qty;
	
	private String feeRate;
	
	@JsonProperty(value="fee")
	private String fee;
	
	@JsonProperty(value="orderId")
	private String orderId;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	
}
