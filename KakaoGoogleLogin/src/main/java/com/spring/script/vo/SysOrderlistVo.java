package com.spring.script.vo;

public class SysOrderlistVo {
	private String m_id;
	private String order_date;
	private String site;
	private String currency;
	private String price;
	private String units;
	private String order_id;
	private String side;
	private String fee;
	private String revenue;
	private String bot_name;
	private String tot_price;
	
	
	public String getTot_price() {
		return tot_price;
	}
	public void setTot_price(String tot_price) {
		this.tot_price = tot_price;
	}
	public String getBot_name() {
		return bot_name;
	}
	public void setBot_name(String bot_name) {
		this.bot_name = bot_name;
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getRevenue() {
		return revenue;
	}
	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}
	
}
