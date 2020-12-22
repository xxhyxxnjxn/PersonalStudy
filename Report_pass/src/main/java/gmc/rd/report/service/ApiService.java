package gmc.rd.report.service;

import java.util.List;

import gmc.rd.report.api.bithumb.vo.BithumbBalanceKrwVo;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceKrwVo;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.api.upbit.vo.UpbitBalanceVo;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.User;

public interface ApiService {

	void insertApiKey(ApiDto apiDto);

	ApiDto selectApiKey(ApiDto apiDto);

	String authApikey(ApiDto apiDto);

	void updateApiKey(ApiDto apiDto);

	List<Api> selectSite(User user);


}
