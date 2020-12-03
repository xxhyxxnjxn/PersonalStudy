package com.spring.bot.service;

import java.util.HashMap;
import java.util.List;

import com.spring.bot.vo.BotSiteVo;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysOrderlistVo;

public interface BotCheckService {
	List<SysBotCkVo> getBotName(HashMap<String, Object> map);
	List<SysBotCkVo> getBot(String id);
	List<ApiVo> getApi(HashMap<String, String> map);
	List<SysOrderlistVo> getBotOrderlist_units(HashMap<String, Object> map);
	void updateBotState(HashMap<String, Object> map);
	List<BotSiteVo> getBotSite();
	void setApi(HashMap<String, Object> map);
	String getApiCheck(HashMap<String, Object> map);
	void setBotCheck(HashMap<String, Object> map);
	List<SysOrderlistVo> getBotOrderlist(HashMap<String, Object> map);
	void updateApi(HashMap<String, Object> map);
	void updateBotCheck(HashMap<String, Object> map);
	void deleteApi(HashMap<String, Object> map);
	void deleteBotCheck(HashMap<String, Object> map);
	void deleteBotOrderlist(HashMap<String, Object> map);
}
