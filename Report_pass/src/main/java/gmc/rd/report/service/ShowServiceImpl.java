package gmc.rd.report.service;

import java.util.HashMap;
import java.util.List;

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
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.ApiRoadingState;
import gmc.rd.report.entity.BankStateMentState;
import gmc.rd.report.entity.CandleStickState;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.BankStateMentStateRepository;
import gmc.rd.report.repository.CandleStickStateRepository;
@Service
public class ShowServiceImpl implements ShowService{
	
	
	@Autowired
	ApiRepository apiRepository;

	@Autowired
	BithumbApiService bithumbApiService;

	@Autowired
	UpbitApiService upbitApiService;

	@Autowired
	CoinoneApiService coinoneApiService;

	@Autowired
	private ApiRoadingStateRepository apiRoadingRepository;
	@Autowired
	private BankStateMentStateRepository bankStateMentStateRepository;
	@Autowired
	private CandleStickStateRepository candleStickStateRepository;
	
	@Override
	public List<ApiDto> getApis(String memId) {
		List<Api> api = apiRepository.findBymemId(memId);
		//System.out.println("이건 api"+api);
		List<ApiDto> apiDto = ApiDtoMapper.INSTANCE.ToDtoList(api);
		//System.out.println("이건 Dto"+apiDto);
		return apiDto;
	}

	@Override
	public BithumbBalanceKrwVo getBithumbBalance(ApiDto apiDto) {

		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("apiKey", apiDto.getApiKey());
		hash.put("secretKey", apiDto.getSecretKey());
		hash.put("currency", "BTC");
		bithumbApiService.getBalance(hash);
		//System.out.println("빗썸 밸런스 가져오기" + bithumbApiService.getBalance(hash));

		BithumbBalanceKrwVo bithumbBalanceKrwVo = null;
		Gson gson = new Gson();
		BithumbBalanceVo bithumbBalancVo = gson.fromJson(bithumbApiService.getBalance(hash),
				BithumbBalanceVo.class);
		bithumbBalanceKrwVo = bithumbBalancVo.getData();

		bithumbBalanceKrwVo.getAvailable_krw();
		System.out.println("빗썸 자산" + bithumbBalanceKrwVo.getAvailable_krw());
		return bithumbBalanceKrwVo;

	}

	@Override
	public List<UpbitAccountDataVo> getUpbitBalance(ApiDto apidto) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		List<UpbitAccountDataVo> upbitAccountDataVoList = null;

		try {
			hashMap.put("apiKey", apidto.getApiKey());
			hashMap.put("secretKey", apidto.getSecretKey());
			UpbitAccountVo upbitAccountVo = upbitApiService.getAccounts(hashMap);
			upbitAccountDataVoList = upbitAccountVo.getData();
			upbitAccountVo.getError();
			System.out.println("업비트 자산 : "+upbitAccountDataVoList.get(0).getBalance());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return upbitAccountDataVoList;
	}

	@Override
	public CoinoneBalanceKrwVo getCoinoneBalance(ApiDto apidto) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			hashMap.put("apiKey", apidto.getApiKey());
			hashMap.put("secretKey", apidto.getSecretKey());
			//System.out.println(coinoneApiService.getBalance(hashMap));

			CoinoneBalanceKrwVo coinconBalanceKrwVo = null;
			Gson gson = new Gson();
			
			ApiRoadingState apiRoadingState = apiRoadingRepository.findByMemIdAndSite(apidto.getMemId(), apidto.getSite());
			BankStateMentState bankStateMentState = bankStateMentStateRepository.findByMemIdAndSite(apidto.getMemId(), apidto.getSite());
			CandleStickState candleStickState = candleStickStateRepository.findByMemIdAndSite(apidto.getMemId(), apidto.getSite());

			if(apiRoadingState.getState().equals("1") && bankStateMentState.getState().equals("1")&&candleStickState.getState().equals("1")) {
				coinoneApiService.getBalance(hashMap);
				CoinoneBalanceVo coinoneBalanceVo = gson.fromJson(coinoneApiService.getBalance(hashMap),
						CoinoneBalanceVo.class);
				coinconBalanceKrwVo = coinoneBalanceVo.getKrw();
				return coinconBalanceKrwVo;
			}else {
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
