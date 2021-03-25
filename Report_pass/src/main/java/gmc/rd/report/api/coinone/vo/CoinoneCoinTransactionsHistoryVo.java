package gmc.rd.report.api.coinone.vo;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CoinoneCoinTransactionsHistoryVo {
	String result;
	String errorCode;
	List<CoinoneCoinTransactionsDataVo> transactions;
}
