package com.spring.script.service;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;

import com.spring.script.vo.ApiVo;
import com.spring.script.vo.ScriptVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;

public interface ScriptService {
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
	JSONArray getScriptList();
	void script_insert(ScriptVo scriptVo);
	JSONArray script_delete(String scriptNo);
	void script_update(HashMap<String, String> hash);
	ScriptVo getScriptOne(HashMap hash);

}
