package com.spring.bot.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.api.bithumb.service.ApiService;
import com.spring.api.upbit.ApiUpbit;
import com.spring.bot.service.BotCheckService;

import com.spring.bot.vo.BotSiteVo;
import com.spring.coinoneh.api.service.CoinoneApiService;
import com.spring.member.vo.MemberVo;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysOrderlistVo;

@Controller
public class BotController {

	@Autowired
	BotCheckService botCheckService;
	
	@Autowired
	ApiService bithumbApiService;
	
	@Autowired
	ApiUpbit apiUpbit;
	
	@Autowired
	CoinoneApiService coinoneApiService;
	
	
	String bithumbBalanceResult="";
	String upbitBalanceResult="";
	String coinoneBalanceResult="";
	
	@RequestMapping("/botCreate")
	public ModelAndView mypage(HttpServletRequest request) {
	ModelAndView mv = new ModelAndView();
	
	mv.setViewName("/mypage/mypage");
	return mv;
	
	}
	
	@RequestMapping("/show_bot")
	public void show_bot(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String id = (String) request.getSession().getAttribute("id");
		
		List<SysBotCkVo> getBot = botCheckService.getBot(id);
		JSONArray jSONArray = new JSONArray();
		
		HashMap<String, Object> map = new HashMap<String,Object>();
		
		for(int i=0; i<getBot.size(); i++) {
			
			JSONObject jSONObject = new JSONObject();	
			jSONObject.put("site", getBot.get(i).getSite() );
			jSONObject.put("currency", getBot.get(i).getCurrency() );
			jSONObject.put("bot_ck", getBot.get(i).getBot_ck() );
			jSONObject.put("set_per", getBot.get(i).getSet_per());
			jSONObject.put("bot_name", getBot.get(i).getBot_name());
			jSONObject.put("position", getBot.get(i).getPosition());
			jSONArray.add(jSONObject);
		}	
		
		
		response.getWriter().print(jSONArray);
	}

	@RequestMapping("/bot_units")
	@ResponseBody
	public void bot_units(HttpServletRequest request, HttpServletResponse response, @RequestParam HashMap<String, Object> map) throws Exception{
		String id = (String) request.getSession().getAttribute("id");
		map.put("m_id",id);
		
		List<SysOrderlistVo> getOrderlistUnits = botCheckService.getBotOrderlist_units(map);

		BigDecimal cal_units = new BigDecimal("0");
		JSONObject jSONObject = new JSONObject();
		for (int i = 0; i < getOrderlistUnits.size(); i++) {

			BigDecimal units = new BigDecimal(getOrderlistUnits.get(i).getUnits());
			cal_units = cal_units.add(units);
			
		}
		jSONObject.put("units", cal_units.toString());

		response.getWriter().print(jSONObject);
	}
	
	@RequestMapping("/site_balance")
	@ResponseBody
	public String site_balance(HttpServletRequest request, @RequestParam HashMap<String, String> map) throws Exception{
		String id = (String) request.getSession().getAttribute("id");
		map.put("m_id", id);

		String result = "";
		List<ApiVo> getApi = botCheckService.getApi(map);

		map.put("apiKey", getApi.get(0).getApiKey());
		System.out.println(map);
		map.put("secretKey", getApi.get(0).getSecretKey());
		if(map.get("site").equals("bithumb")) {
			result = bithumbApiService.getBalance(map);
			//System.out.println(result);
			
		}else if(map.get("site").equals("upbit")) {
			result = apiUpbit.getBalance(map);
			//System.out.println(result);
			
		}else if(map.get("site").equals("coinone")) {
			result = coinoneApiService.getBalance(map);
			//System.out.println(result);
		}

		//System.out.println(result);
		
		return result;
	}
	
	@RequestMapping("/update_bot_state")
	public void update_bot_state(HttpServletRequest request, @RequestParam HashMap<String, Object> map) throws Exception{
		String id = (String) request.getSession().getAttribute("id");

		map.put("m_id", id);
		
		//System.out.println(map.get("bot_ck").getClass().getName());
		
		if(map.get("bot_ck").equals("false")) {
			map.put("bot_ck","0");
		}
		else if(map.get("bot_ck").equals("true")){
			map.put("bot_ck","1");
		}
		System.out.println(map);
		botCheckService.updateBotState(map);
		 //mv.setViewName("redirect:/ApiUpdate?m_id="+mem.getM_id());
	}
	
	@RequestMapping("/symbol_add")
	public ModelAndView symbol_add(HttpServletRequest request) {
	ModelAndView mv = new ModelAndView();
	
	mv.setViewName("/mypage/symbol_add");
	return mv;
	}
	
	@RequestMapping("/site_tbl")
	public void site_tbl(HttpServletRequest request, HttpServletResponse response) throws Exception{
	String id = (String) request.getSession().getAttribute("id");
		
	List getBotSite = botCheckService.getBotSite();

	JSONArray jSONArray = new JSONArray();
	
	for(int i=0; i<getBotSite.size(); i++) {
		BotSiteVo botSiteVo = (BotSiteVo)getBotSite.get(i);
		JSONObject jSONObject = new JSONObject();	
		jSONObject.put("site", botSiteVo.getSite() );
		jSONArray.add(jSONObject);
	}	
	
	
	response.getWriter().print(jSONArray);
	}
	
	@RequestMapping("/insert_api_tbl")
	public void insert_api_tbl(HttpServletRequest request, @RequestParam HashMap<String, Object> map) throws Exception{
		String id = (String) request.getSession().getAttribute("id");
		
		String apiKey = (String) map.get("apiKey");
		String substr_apiKey = apiKey.substring(apiKey.length()-2, apiKey.length());
		String secretKey = (String) map.get("secretKey");
		String substr_secretKey = secretKey.substring(secretKey.length()-2, secretKey.length());
		
		String bot_name = id+substr_apiKey+substr_secretKey;

		map.put("m_id", id);
		map.put("bot_name",bot_name);
		
		botCheckService.setApi(map);
		botCheckService.setBotCheck(map);
		 //mv.setViewName("redirect:/ApiUpdate?m_id="+mem.getM_id());
	}
	
	@RequestMapping("/check_api")
	@ResponseBody
	public String check_api(HttpServletRequest request, @RequestParam HashMap<String, String> map) throws Exception{
		
		String result = "";
		if(map.get("site").equals("bithumb")) {
			result = bithumbApiService.getBalance(map);
			//System.out.println(result);
			
		}else if(map.get("site").equals("upbit")) {
			result = apiUpbit.getBalance(map);
			//System.out.println(result);
			
		}else if(map.get("site").equals("coinone")) {
			result = coinoneApiService.getBalance(map);
			//System.out.println(result);
		}
		
		return result;
	}
	
	@RequestMapping("/check_api_tbl")
	@ResponseBody
	public String check_api_tbl(HttpServletRequest request, @RequestParam HashMap<String, Object> map) throws Exception{
		String result = botCheckService.getApiCheck(map);
		
		return result;
	}
	
	@RequestMapping("/symbol_transaction")
	public ModelAndView symbol_transaction(@RequestParam String bot_site, @RequestParam String currency,HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();
		session.setAttribute("bot_site", bot_site);
		session.setAttribute("currency", currency);

		ModelAndView mv = new ModelAndView();

		HashMap<String,Object> map = new HashMap<String,Object>();
		String id = (String) request.getSession().getAttribute("id");
		map.put("m_id", id);
		map.put("site", bot_site);
		map.put("currency", currency);
		
		List<SysBotCkVo> getBotName = botCheckService.getBotName(map);
		if(!getBotName.isEmpty()) {
			session.setAttribute("bot_name", getBotName.get(0).getBot_name());
		}

		mv.setViewName("/mypage/symbol_transaction");
		return mv;
	}
	
	@RequestMapping("/show_botOrderlist")
	public void show_botOrderlist(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		
		String id = (String) request.getSession().getAttribute("id");
		String bot_site = (String) request.getSession().getAttribute("bot_site");
		String currency = (String) request.getSession().getAttribute("currency");
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("m_id", id);
		map.put("site", bot_site);
		map.put("currency", currency);
		
		
		List<SysOrderlistVo> getBotOrderlist = botCheckService.getBotOrderlist(map);
		
		JSONArray jSONArray = new JSONArray();

		for (int i = 0; i < getBotOrderlist.size(); i++) {

			JSONObject jSONObject = new JSONObject();
			jSONObject.put("date", getBotOrderlist.get(i).getOrder_date());
			jSONObject.put("price", getBotOrderlist.get(i).getPrice());
			jSONObject.put("units", getBotOrderlist.get(i).getUnits());
			jSONObject.put("side", getBotOrderlist.get(i).getSide());
			jSONObject.put("tot_price", getBotOrderlist.get(i).getTot_price());
			jSONObject.put("fee", getBotOrderlist.get(i).getFee());
			jSONObject.put("revenue", getBotOrderlist.get(i).getRevenue());
			jSONArray.add(jSONObject);
		}

		if(!getBotOrderlist.isEmpty()) {
			session.setAttribute("bot_name",  getBotOrderlist.get(0).getBot_name());
		}
		
		response.getWriter().print(jSONArray);

	}
	
	@RequestMapping("/show_apiKey")
	@ResponseBody
	public void show_apiKey(HttpServletRequest request, HttpServletResponse response,@RequestParam HashMap<String, String> map) throws Exception{
		
		String id = (String) request.getSession().getAttribute("id");
		map.put("m_id", id);

		List<ApiVo> getApi = botCheckService.getApi(map);

		JSONObject jSONObject = new JSONObject();
		jSONObject.put("apiKey", getApi.get(0).getApiKey());
		jSONObject.put("secretKey", getApi.get(0).getSecretKey());

		response.getWriter().print(jSONObject);
		
	}
	
	@RequestMapping("/symbol_update")
	public ModelAndView symbol_update(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		
		String id = (String) request.getSession().getAttribute("id");
		String bot_site = (String) request.getSession().getAttribute("bot_site");
		String currency = (String) request.getSession().getAttribute("currency");
		String bot_name = (String) request.getSession().getAttribute("bot_name");
		
//		HashMap<String,Object> map = new HashMap<String,Object>();
//		map.put("m_id", id);
//		map.put("site", bot_site);
//		map.put("bot_name", bot_name);
//		
//		List<ApiVo> getApi = botCheckService.getApi(map);

		mv.setViewName("/mypage/symbol_update");
		return mv;
	}
	
	@RequestMapping("/update_bot")
	public void update_bot(HttpServletRequest request, @RequestParam HashMap<String, Object> map) throws Exception{
		String id = (String) request.getSession().getAttribute("id");
		String bot_site = (String) request.getSession().getAttribute("bot_site");
		
		String apiKey = (String) map.get("apiKey");
		String substr_apiKey = apiKey.substring(apiKey.length()-2, apiKey.length());

		String bot_name = substr_apiKey+id;

		map.put("m_id", id);
		map.put("site", bot_site);
		map.put("bot_name",bot_name);
		
		botCheckService.updateApi(map);
		botCheckService.updateBotCheck(map);
		 //mv.setViewName("redirect:/ApiUpdate?m_id="+mem.getM_id());
	}
	
	@RequestMapping("/delete_bot")
	public void delete_bot(HttpServletRequest request, @RequestParam HashMap<String, Object> map) throws Exception{
		String id = (String) request.getSession().getAttribute("id");
		String bot_site = (String) request.getSession().getAttribute("bot_site");
		String currency = (String) request.getSession().getAttribute("currency");
		String bot_name = (String) request.getSession().getAttribute("bot_name");
		
		map.put("m_id", id);
		map.put("site", bot_site);
		map.put("currency", currency);
		map.put("bot_name", bot_name);

		System.out.println(map);
		botCheckService.deleteApi(map);
		botCheckService.deleteBotCheck(map);
		botCheckService.deleteBotOrderlist(map);
		 //mv.setViewName("redirect:/ApiUpdate?m_id="+mem.getM_id());
	}
	
}
