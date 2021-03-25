package gmc.rd.report.api.bithumb.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class BithumbTransactionVo {

	@JsonProperty(value="type")
	private String search;
	
	@JsonProperty(value="transactionDate")
	private String transfer_date;
	
	@JsonProperty(value="currency")
	private String order_currency;
	
	private String payment_currency;
	
	@JsonProperty(value="units")
	private String units;
	
	@JsonProperty(value="price")
	private String price;
	
	private String amount;
	
	private String fee_currency;
	
	@JsonProperty(value="fee")
	private String fee;
	
	private String order_balance;
	
	private String payment_balance;

	@JsonGetter("type")
	public String getSearch() {
		return search;
	}
	@JsonSetter("type")
	public void setSearch(String search) {
		this.search = search;
	}

	public String getTransfer_date() {
		return transfer_date;
	}

	public void setTransfer_date(String transfer_date) {
		this.transfer_date = transfer_date;
	}

	public String getOrder_currency() {
		return order_currency;
	}

	public void setOrder_currency(String order_currency) {
		this.order_currency = order_currency;
	}

	public String getPayment_currency() {
		return payment_currency;
	}

	public void setPayment_currency(String payment_currency) {
		this.payment_currency = payment_currency;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getFee_currency() {
		return fee_currency;
	}

	public void setFee_currency(String fee_currency) {
		this.fee_currency = fee_currency;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOrder_balance() {
		return order_balance;
	}

	public void setOrder_balance(String order_balance) {
		this.order_balance = order_balance;
	}

	public String getPayment_balance() {
		return payment_balance;
	}

	public void setPayment_balance(String payment_balance) {
		this.payment_balance = payment_balance;
	}

}
