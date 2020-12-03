package com.spring.bot.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

import com.spring.bot.vo.BotSiteVo;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysOrderlistVo;

@Repository("BotCheckDao")
public class BotCheckDaoImpl implements BotCheckDao{

	@Autowired
	private SqlSession sqlSession;

	
	@Override
	public List<SysBotCkVo> getBotName(HashMap<String, Object> map){
		List<SysBotCkVo> getBotName = sqlSession.selectList("bot.sys_bot_ck_tbl_select_botname",map);
		return getBotName;
	}
	@Override
	public List<SysBotCkVo> getBot(String id){
		List<SysBotCkVo> getBot = sqlSession.selectList("bot.sys_bot_ck_tbl_select",id);
		return getBot;
	}
	
	@Override
	public List<ApiVo> getApi(HashMap<String, String> map){
		List<ApiVo> getApi = sqlSession.selectList("bot.api_tbl_select",map);
		return getApi;
	}
	@Override
	public List<SysOrderlistVo> getBotOrderlist_units(HashMap<String, Object> map){
		List<SysOrderlistVo> getBotOrderlist_units = sqlSession.selectList("bot.sys_bot_ck_tbl_select_units", map);
		return getBotOrderlist_units;
	}
	@Override
	public void updateBotState(HashMap<String, Object> map) {
		sqlSession.update("bot.sys_bot_ck_tbl_state_update", map);
	}
	@Override
	public List<BotSiteVo> getBotSite(){
		List<BotSiteVo> getBotSite = sqlSession.selectList("bot.bot_site_select");
		return getBotSite;
	}
	
	@Override
	public void setApi(HashMap<String, Object> map){
		sqlSession.insert("bot.api_tbl_insert", map);
	}
	
	@Override
	public String getApiCheck(HashMap<String, Object> map) {
		String result = sqlSession.selectOne("bot.api_tbl_check", map);
		return result;
	}
	
	@Override
	public void setBotCheck(HashMap<String, Object> map) {
		sqlSession.insert("bot.sys_bot_ck_tbl_insert", map);
	}
	
	@Override
	public List<SysOrderlistVo> getBotOrderlist(HashMap<String, Object> map){
		List<SysOrderlistVo> getBotOrderlist = sqlSession.selectList("bot.sys_orderlist_tbl_select",map);
		return getBotOrderlist;
	}
	
	@Override
	public void updateApi(HashMap<String, Object> map) {
		sqlSession.update("bot.api_tbl_update", map);
	}
	
	@Override
	public void updateBotCheck(HashMap<String, Object> map) {
		sqlSession.update("bot.sys_bot_ck_tbl_update", map);
	}
	@Override
	public void deleteApi(HashMap<String, Object> map) {
		sqlSession.delete("bot.api_tbl_delete", map);
	}
	@Override
	public void deleteBotCheck(HashMap<String, Object> map) {
		sqlSession.delete("bot.sys_bot_ck_tbl_delete", map);
	}
	@Override
	public void deleteBotOrderlist(HashMap<String, Object> map) {
		sqlSession.delete("bot.sys_orderlist_tbl_delete", map);
	}
}
