package com.spring.bot.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.bot.dao.BotCheckDao;
import com.spring.bot.vo.BotSiteVo;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysOrderlistVo;


@Service("BotCheckService")
public class BotCheckServiceImpl implements BotCheckService{
	
	@Autowired
	BotCheckDao botCheckDao;
	
	@Override
	public List<SysBotCkVo> getBotName(HashMap<String, Object> map){
		List<SysBotCkVo> getBotName = botCheckDao.getBotName(map);
		return getBotName;
	}
	
	@Override
	public List<SysBotCkVo> getBot(String id){
		 List<SysBotCkVo> getBot = botCheckDao.getBot(id);
		 return getBot;
	}
	
	@Override
	public List<ApiVo> getApi(HashMap<String, String> map){
		List<ApiVo> getApi = botCheckDao.getApi(map);
		 return getApi;
	}
	@Override
	public List<SysOrderlistVo> getBotOrderlist_units(HashMap<String, Object> map){
		List<SysOrderlistVo> getBotOrderlist_units = botCheckDao.getBotOrderlist_units(map);
		 return getBotOrderlist_units;
	}
	@Override
	public void updateBotState(HashMap<String, Object> map) {
		botCheckDao.updateBotState(map);
	}
	@Override
	public List<BotSiteVo> getBotSite(){
		List<BotSiteVo> getBotSite = botCheckDao.getBotSite();
		return getBotSite;
	}
	
	@Override
	public void setApi(HashMap<String, Object> map){
		botCheckDao.setApi(map);
	}
	
	@Override
	public String getApiCheck(HashMap<String, Object> map) {
		String result = botCheckDao.getApiCheck(map);
		return result;
	}
	
	@Override
	public void setBotCheck(HashMap<String, Object> map) {
		botCheckDao.setBotCheck(map);
	}
	
	@Override
	public List<SysOrderlistVo> getBotOrderlist(HashMap<String, Object> map){
		List<SysOrderlistVo> getBotOrderlist = botCheckDao.getBotOrderlist(map);
		return getBotOrderlist;
	}
	
	@Override
	public void updateApi(HashMap<String, Object> map) {
		botCheckDao.updateApi(map);
	}
	@Override
	public void updateBotCheck(HashMap<String, Object> map) {
		botCheckDao.updateBotCheck(map);
	}
	@Override
	public void deleteApi(HashMap<String, Object> map) {
		botCheckDao.deleteApi(map);
	}
	@Override
	public void deleteBotCheck(HashMap<String, Object> map) {
		botCheckDao.deleteBotCheck(map);
	}
	@Override
	public void deleteBotOrderlist(HashMap<String, Object> map) {
		botCheckDao.deleteBotOrderlist(map);
	}
}
