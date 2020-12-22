package gmc.rd.report.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.bithumb.vo.BithumbBalanceKrwVo;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceKrwVo;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.service.ApiService;
import gmc.rd.report.service.ShowService;

@Controller
public class ShowController {

	@Autowired
	ShowService showService;
	@Autowired
	ApiService apiService;
	@Autowired
	ApiRepository apiRepository;

	@Autowired
	BithumbApiService bithumbApiService;

	@Autowired
	UpbitApiService upbitApiService;

	@Autowired
	CoinoneApiService coinoneApiService;

//	@RequestMapping("/list")
//	public ModelAndView list() {
//		String memId = "123";
//		List<Api> list = apiRepository.findBymemId(memId);
//		list.get(0).getApiKey();
//		System.out.println(list.get(0).getApiKey());
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("list",list);
//		mv.setViewName("list");
//		
//		return mv;
//	}

	@RequestMapping("/getList")
	public ModelAndView getList(HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal)
			throws IOException {
		ModelAndView mv = new ModelAndView();
		List<ApiDto> list = showService.getApis(principal.getUsername());
		DecimalFormat formatter = new DecimalFormat("###,###");
		if (list.size() == 0) {
			mv.setViewName("intro");
			return mv;
		} else {

			System.out.println("사이트 불러오기 : " + list.get(0).getSite());

			BithumbBalanceKrwVo vo = null;
			List<UpbitAccountDataVo> vo2 = null;
			CoinoneBalanceKrwVo vo3 = null;
			String str1 = null;
			String str2 = null;
			String str3 = null;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getSite().equals("bithumb")) {
					vo = showService.getBithumbBalance(list.get(i));
					double number =  Math.round(Double.valueOf(vo.getAvailable_krw()));
					int intnum = (int)(number);
					formatter.format(intnum);
					str1 = formatter.format(intnum);
				} else if (list.get(i).getSite().equals("upbit")) {
					vo2 = showService.getUpbitBalance(list.get(i));
					str2 = null;
					for (int j = 0; j < vo2.size(); j++) {
						if (vo2.get(j).getCurrency().equals("KRW")) {
							double number =  Math.round(Double.valueOf(vo2.get(j).getBalance()));
							int intnum = (int)(number);
							str2 = formatter.format(intnum);
							
							
						}
					}
				} else if (list.get(i).getSite().equals("coinone")) {
					vo3 = showService.getCoinoneBalance(list.get(i));
					double number =  Math.round(Double.valueOf(vo3.getBalance()));
					int intnum = (int)(number);
					str3 = formatter.format(intnum);
					
					
				}
			}

			mv.addObject("bithumbbal", str1);
			mv.addObject("upbitbal", str2);
			mv.addObject("coinonebal", str3);
			mv.addObject("list", list);
			mv.setViewName("intro");
			return mv;

		}
	}

	@RequestMapping("/gobalance")
	public String balance(ApiDto apiDto) {
		System.out.println("들고온거" + apiDto);
		ApiDto apidto = apiService.selectApiKey(apiDto);

//		apidto.getIdx();
//		apidto.getMemId();
//		apidto.getSite();
//		apidto.getApiKey();
//		apidto.getSecretKey();
//		System.out.println("===============================");
		System.out.println(apidto);

		if (apiDto.getSite().equals("bithumb")) {

			BithumbBalanceKrwVo vo = showService.getBithumbBalance(apidto);

		} else if (apiDto.getSite().equals("upbit")) {

			List<UpbitAccountDataVo> vo = showService.getUpbitBalance(apidto);

		} else if (apiDto.getSite().equals("coinone")) {

			CoinoneBalanceKrwVo vo = showService.getCoinoneBalance(apidto);

			System.out.println("코인원 자산 : " + vo.getAvail());
		}

		return "gobalance";
	}

	@RequestMapping("/deleteapi")
	public void delete(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto) {
		System.out.println("delete 다녀감");
		System.out.println(apiDto);
		apiRepository.deleteById(Integer.valueOf(apiDto.getIdx()));
		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/updateapi")
	public void update(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto) {
		System.out.println("update 다녀감");
		System.out.println(apiDto);

		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/detailapi")
	public void detailapi(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto) {
		System.out.println("update 다녀감");
		System.out.println(apiDto);
		System.out.println();
		System.out.println();
		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/showBalance")
	public void showBalance(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto)
			throws IOException {

		ApiDto apidto = apiService.selectApiKey(apiDto);

		if (apiDto.getSite().equals("bithumb")) {

			BithumbBalanceKrwVo vo = showService.getBithumbBalance(apidto);
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("avail", vo.getAvailable_krw());

			response.getWriter().print(jSONObject);

		} else if (apiDto.getSite().equals("upbit")) {

			List<UpbitAccountDataVo> vo = showService.getUpbitBalance(apidto);
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("avail", vo.get(0).getBalance());
			response.getWriter().print(jSONObject);

		} else if (apiDto.getSite().equals("coinone")) {

			CoinoneBalanceKrwVo vo = showService.getCoinoneBalance(apidto);
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("avail", vo.getAvail());
			response.getWriter().print(jSONObject);

		}

	}

}
