package gmc.rd.report.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.bithumb.vo.BithumbBalanceVo;
import gmc.rd.report.api.bithumb.vo.BithumbBalanceKrwVo;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceKrwVo;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceVo;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.api.upbit.vo.UpbitAccountVo;
import gmc.rd.report.api.upbit.vo.UpbitBalanceVo;
import gmc.rd.report.api.upbit.vo.UpbitErrorVo;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;

@Service
public class ApiServiceImpl implements ApiService {

	@Autowired
	ApiRepository apiRepository;

	@Autowired
	BithumbApiService bithumbApiService;

	@Autowired
	UpbitApiService upbitApiService;

	@Autowired
	CoinoneApiService coinoneApiService;

	@Override
	public void insertApiKey(ApiDto apiDto) {
		Api api = new Api();
		api.setApiKey(apiDto.getApiKey());
		api.setMemId(apiDto.getMemId());
		api.setSecretKey(apiDto.getSecretKey());
		api.setSite(apiDto.getSite());
		apiRepository.save(api);
	}

	@Override
	public ApiDto selectApiKey(ApiDto apiDto) {
		Api api = apiRepository.findByMemIdAndSite(apiDto.getMemId(), apiDto.getSite());
		apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

		System.out.println(api);
		return apiDto;
	}

	@Override
	public String authApikey(ApiDto apiDto) {
		String site = apiDto.getSite();
		HashMap hashMap = new HashMap();
		String result = "인증에 실패했습니다.";
		hashMap.put("apiKey", apiDto.getApiKey());
		hashMap.put("secretKey", apiDto.getSecretKey());
		if (site.equals("upbit")) {
			UpbitAccountVo upbitAccountVo = upbitApiService.getAccounts(hashMap);
			if (upbitAccountVo.getError() == null) {
				result = "인증에 성공했습니다.";
			} else {
				result = upbitAccountVo.getError().getMessage();
			}
		} else if (site.equals("bithumb")) {
			BithumbBalanceVo bithumbBalanceVo =bithumbApiService.getBalanceVo(hashMap);
			if(bithumbBalanceVo.getStatus().equals("0000")) {
				result = "인증에 성공했습니다.";	
			}else if(bithumbBalanceVo.getMessage().equals("Invalid Apikey")){
				result = "잘못된 엑세스 키입니다.";
			}else if(bithumbBalanceVo.getMessage().equals("Method Not Allowed.(Access IP)")){
				result = "접속 허용 IP가 아닙니다.";
			}
			System.out.println(bithumbBalanceVo);
		} else if (site.equals("coinone")) {
			CoinoneBalanceVo coinoneBalanceVo = coinoneApiService.getBalanceVo(hashMap);
			if(coinoneBalanceVo.getResult().equals("success")) {
				result = "인증에 성공했습니다.";
			}
		}
		return result;

	}

	@Transactional
	@Override
	public void updateApiKey(ApiDto apiDto) {
		System.out.println(apiDto);
		Api api = apiRepository.findByMemIdAndSite(apiDto.getMemId(), apiDto.getSite());
		System.out.println(api);
		api.setApiKey(apiDto.getApiKey());
		api.setMemId(apiDto.getMemId());
		api.setSecretKey(apiDto.getSecretKey());
		api.setSite(apiDto.getSite());
	}

	@Override
	public List<Api> selectSite(User user) {
		String memId = user.getMemId();
		return apiRepository.findByMemId(memId);
	}
}
