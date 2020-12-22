package gmc.rd.report.api.upbit.vo;

import lombok.Data;

@Data
public class UpbitAccountDataVo {

	private String currency;
	private String balance;
	private String locked;
	private String avg_buy_price;
	private String avg_buy_price_modified;
	private String unit_currency;
	
}
