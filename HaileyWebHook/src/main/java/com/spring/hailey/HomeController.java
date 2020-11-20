package com.spring.hailey;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.script.service.ScriptService;
import com.spring.script.vo.ScriptVo;
import com.bitforex.api.service.BitforexApiService;
import com.spring.bithumb.api.service.*;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Autowired
	ScriptService scriptService;
	
	@Autowired
	ApiService bithumbapiService;
	@Autowired
	BitforexApiService bitforexApi;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		bitforexApi.tradeOrder("coin-usdt-eos");
		
		return "home";
	}
	
//	@RequestMapping(value = "/test", method = RequestMethod.POST)
//	@ResponseBody
//	public void test(HttpServletResponse response, @RequestBody String str) throws Exception{
//		HashMap <String,String> hash = new HashMap<String,String>();
//		
//		JSONParser parser = new JSONParser();
//		Object obj;
//		JSONObject jsonObject;
//
//		obj = parser.parse(str);
//		jsonObject = (JSONObject) obj;
//		
//		String signal_scriptNo = jsonObject.get("scriptNo").toString();
//		String signal_side = jsonObject.get("side").toString();
//		String signal_sideNum = jsonObject.get("side_num").toString();
//		String signal_log = jsonObject.get("log").toString();
//		
//		hash.put("scriptNo", signal_scriptNo);
//		hash.put("side",signal_side);
//		hash.put("side_num",signal_sideNum);
//		hash.put("log",signal_log);
//
//		System.out.println("파이썬에서 가져온 신호 : "+hash);
//
//		List<ScriptVo> list = scriptService.getScript(signal_scriptNo); //스크립트 넘버가 같은 것을 찾는다.
//
//		String exist_side = list.get(0).getSide();
//
//		if(signal_side.equals(exist_side)) { //스크립트 사이드 비교 들어온 신호랑 이미 가지고 있는 신호인 것을 비교
//			System.out.println("pass");
//		}else if(!signal_side.equals(exist_side)) {
//			System.out.println("change");
//			scriptService.updateScript(hash);
//		}
//		
//		//scriptService.setScript(hash); //database insert
//		//scriptService.updateScript(hash); //datebase update
//
//	}
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	public void test(HttpServletResponse response,@RequestBody String str) throws Exception{

		HashMap <String,String> hash = new HashMap<String,String>();
		
		URI uri = new URI("?"+str);
		
		List<NameValuePair> paramList = URLEncodedUtils.parse(uri, "utf-8");
		for(NameValuePair param : paramList){ 
			hash.put(param.getName(), param.getValue());
			//System.out.println(param.getName() + " = " + param.getValue()); 
		}

		System.out.println("파이썬에서 가져온 신호 : "+hash);

		String signal_side = hash.get("side");

		List<ScriptVo> list = scriptService.getScript(hash); //스크립트 넘버가 같은 것을 찾는다.
		
		if(list.isEmpty()) {
			System.out.println("데이터 없음");
			
		}else {
			String exist_side = list.get(0).getSide();

			if (signal_side.equals(exist_side)) { // 스크립트 사이드 비교 들어온 신호랑 이미 가지고 있는 신호인 것을 비교
				System.out.println("pass");
			} else if (!signal_side.equals(exist_side)) {

				System.out.println("change");
				scriptService.updateScript(hash);

//			if(signal_side.equals("Buy")) {
//				bithumbapiService.marketbuy("BTC", units, "802f4dce5c52e55ab1ece1c772717188", "7f74c2b5a04107c722b1663f79538ffb");
//			}

			}
		}
	}
	
	
}
