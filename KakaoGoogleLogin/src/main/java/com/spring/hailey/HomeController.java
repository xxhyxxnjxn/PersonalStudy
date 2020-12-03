package com.spring.hailey;

import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.coinone.service.AshleyService;
import com.spring.script.service.OrderService;
import com.spring.script.service.ScriptService;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.ScriptVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;

/**
 * Handles requests for the application home page.
 */
@EnableAsync
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Autowired
	ScriptService scriptService;

	@Autowired
	OrderService orderService;
	
	@Autowired
	private AshleyService ashleyService;
	
	ModelAndView mv = new ModelAndView();
	
	private String mypageView = "redirect:/botCreate";

	List<String> idList = new ArrayList<>();
	List<ApiVo> apiList = new ArrayList<>();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

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
	public void test(HttpServletResponse response, @RequestBody String str) throws Exception {

		HashMap<String, String> hash = new HashMap<String, String>();
		HashMap<String, String> hash2 = new HashMap<String, String>();

		URI uri = new URI("?" + str);

		List<NameValuePair> paramList = URLEncodedUtils.parse(uri, "utf-8");
		for (NameValuePair param : paramList) {
			hash.put(param.getName(), param.getValue());
			hash2.put(param.getName(), param.getValue());
			// System.out.println(param.getName() + " = " + param.getValue());
		}

		System.out.println("파이썬에서 가져온 신호 : " + hash);

		String signal_side = hash.get("side");
		String signal_sideNum = hash.get("side_num");

		List<ScriptVo> scriptList = scriptService.getScript(hash); // 스크립트 넘버가 같은 것을 찾는다.

		if (scriptList.isEmpty()) { // 신호 스크립트 넘버가 같은 데이터가 없을 때
			System.out.println("데이터 없음");

		} else { // 스크립트 넘버가 존재할 때
			String exist_side = scriptList.get(0).getSide();
			String exist_sideNum = scriptList.get(0).getSide_num();

			System.out.println(exist_side + " " + exist_sideNum);
			if (signal_side.equals("Buy")) { // 시그널이 buy일때
				if (signal_side.equals(exist_side)) { // 스크립트 테이블도 buy이고 시그널도 buy일 때는 pass
				} else if (exist_side.equals("Exit")) { // 같지 않을 때는 업데이트
					scriptService.updateScript_buy(hash); // 스크립트 테이블 업데이트
					// scriptService.updateSysBotCk_buy(hash); //봇체크 테이블 업데이트
					System.out.println(hash);
					ashleyService.coinoneOrder(hash);
					
					hash.put("position", "Exit");
					hash2.put("position", "Exit");
					System.out.println(hash);

					List<SysBotCkVo> sysBotList_bithumb = scriptService.getSysBotCk_bithumb(hash);
					List<SysBotCkVo2> sysBotList_upbit = scriptService.getSysBotCk_upbit(hash2);
					orderService.bithumbOrder(sysBotList_bithumb, signal_side,hash);
					orderService.upbitOrder(sysBotList_upbit, signal_side,hash2);

				}

			} else if (signal_side.equals("Exit")) {
				if (signal_side.equals(exist_side)) { // 스크립트 테이블도 Exit이고 시그널도 Exit일 때는 pass
					System.out.println("pass");
				} else if (!signal_side.equals(exist_side) && !signal_sideNum.equals(exist_sideNum)) {
					System.out.println("pass");
				} else if (exist_side.equals("Buy")) { // 같지 않을 때는 업데이트
					scriptService.updateScript_sell(hash); // 스크립트 테이블 업데이트
					//scriptService.updateSysBotCk_sell(hash); //봇체크 테이블 업데이트
					ashleyService.coinoneOrderExit(hash);
					hash.put("position", "Buy");
					hash2.put("position", "Buy");

					List<SysBotCkVo> sysBotList_bithumb = scriptService.getSysBotCk_bithumb(hash);
					List<SysBotCkVo2> sysBotList_upbit = scriptService.getSysBotCk_upbit(hash2);

					orderService.bithumbOrder(sysBotList_bithumb, signal_side,hash);
					orderService.upbitOrder(sysBotList_upbit, signal_side,hash2);

				}
			}

		}
		

		
	}
	

	@RequestMapping("/script")
	public ModelAndView script(HttpServletRequest request) {

	mv.setViewName("/admin/script");
	return mv;
	}
	
	@RequestMapping("/scriptlist")
	public void scriptlist(HttpServletRequest request, HttpServletResponse response) throws Exception{
	JSONArray jsonArray = scriptService.getScriptList();	
	response.getWriter().print(jsonArray);
	
	}
	
	
	@RequestMapping("/script_add")
	public ModelAndView script_add(HttpServletRequest request) {
		
	mv.setViewName("/admin/script_add");
	return mv;
	}
	

	@RequestMapping("/script_insert")
	public ModelAndView script_insert(@ModelAttribute ScriptVo scriptVo,HttpServletRequest request) {

	scriptService.script_insert(scriptVo);
		
	mv.setViewName("redirect:/script");
	return mv;
	}
	
	
	@RequestMapping("/script_change")
	public ModelAndView script_change(@RequestParam HashMap hash, HttpServletRequest request) {
	ScriptVo scriptVo= (ScriptVo)scriptService.getScriptOne(hash);
	System.out.println(scriptVo);
	mv.addObject("script", scriptVo);
	mv.setViewName("/admin/script_change");
	return mv;
	}
	
	@RequestMapping("/script_update")
	public ModelAndView script_update(@RequestParam HashMap hash,HttpServletRequest request) {

		scriptService.script_update(hash);
		
	mv.setViewName("redirect:/script");
	return mv;
	}
	
	@RequestMapping("/script_delete")
	public void script_delete(@RequestParam String scriptNo, HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONArray jsonArray =scriptService.script_delete(scriptNo);
	response.getWriter().print(jsonArray);
	
	}
	

}
