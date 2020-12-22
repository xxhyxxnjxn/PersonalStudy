package gmc.rd.report.service;

import java.util.List;

import gmc.rd.report.api.bithumb.vo.BithumbBalanceKrwVo;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceKrwVo;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.dto.ApiDto;

public interface ShowService {
	List<ApiDto> getApis(String memId);

	BithumbBalanceKrwVo getBithumbBalance(ApiDto apidto);
	
	List<UpbitAccountDataVo> getUpbitBalance(ApiDto apidto);

	CoinoneBalanceKrwVo getCoinoneBalance(ApiDto apidto);

}
