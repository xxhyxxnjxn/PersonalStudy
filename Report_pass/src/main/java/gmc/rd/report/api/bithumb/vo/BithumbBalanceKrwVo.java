package gmc.rd.report.api.bithumb.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BithumbBalanceKrwVo {
	private String total_krw;
	private String in_use_krw;
	private String available_krw;
	private String total_btc;
	private String in_use_btc;
	private String available_btc;
	private String xcoin_last_btc;
}
