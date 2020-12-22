package gmc.rd.report.api.coinone.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CoinoneBalanceVo {
	private String result;
	private String errorCode;
		
	private CoinoneBalanceKrwVo krw;

}
