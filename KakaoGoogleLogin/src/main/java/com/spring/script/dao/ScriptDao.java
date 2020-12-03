package com.spring.script.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.script.vo.ApiVo;
import com.spring.script.vo.ScriptVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;
import com.spring.script.vo.SysOrderlistVo;


public interface ScriptDao {
	void setScript(HashMap<String,String> hash);
	void updateScript_buy(HashMap<String,String> hash);
	void updateSysBotCk_buy(HashMap<String,String> hash);
	void updateScript_sell(HashMap<String,String> hash);
	void updateSysBotCk_sell(HashMap<String,String> hash);
	List<ScriptVo> getScript(HashMap<String,String> hash);
	List<SysBotCkVo> getSysBotCk_bithumb(HashMap<String,String> hash);
	List<SysBotCkVo2> getSysBotCk_upbit(HashMap<String,String> hash);
	List<SysBotCkVo> getSysBotCk_coinone(HashMap<String,String> hash);
	List<ApiVo> getApi(HashMap<String,String> hash);

	void setOrderListTbl(HashMap<String, String> deHash);

	void bithumb_orderlist_insert(HashMap<String, String> hash);
	SysOrderlistVo bithumb_orderlist_select(HashMap<String, String> hash);
	SysOrderlistVo getOrderListTbl(HashMap<String, String> getHash);
	SysOrderlistVo upbit_orderlist_select(HashMap<String, String> hash);
	List<ScriptVo> getScriptList();
	void script_insert(ScriptVo scriptVo);
	void script_delete(String scriptNo);
	void script_update(HashMap<String, String> hash);
	ScriptVo getScriptOne(HashMap hash);
	
}
