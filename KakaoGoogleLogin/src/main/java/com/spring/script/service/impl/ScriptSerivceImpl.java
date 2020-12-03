package com.spring.script.service.impl;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.script.dao.ScriptDao;
import com.spring.script.service.ScriptService;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.ScriptVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;

@Repository("ScriptService")
public class ScriptSerivceImpl implements ScriptService{

	@Autowired
	ScriptDao scriptDao;
	
	@Override
	public void setScript(HashMap<String,String> hash) {
		scriptDao.setScript(hash);
	}
	
	@Override
	public void updateScript_buy(HashMap<String,String> hash) {
		scriptDao.updateScript_buy(hash);
	}
	
	@Override
	public void updateSysBotCk_buy(HashMap<String,String> hash) {
		scriptDao.updateSysBotCk_buy(hash);
	}
	@Override
	public void updateScript_sell(HashMap<String,String> hash) {
		scriptDao.updateScript_sell(hash);
	}
	@Override
	public void updateSysBotCk_sell(HashMap<String,String> hash) {
		scriptDao.updateSysBotCk_sell(hash);
	}
	
	@Override
	public List<ScriptVo> getScript(HashMap<String,String> hash){
		List<ScriptVo> getScript = scriptDao.getScript(hash);
		return getScript;
	}
	@Override
	public List<SysBotCkVo> getSysBotCk_bithumb(HashMap<String,String> hash){
		List<SysBotCkVo> getSysBotCk = scriptDao.getSysBotCk_bithumb(hash);
		return getSysBotCk;
	}
	@Override
	public List<SysBotCkVo2> getSysBotCk_upbit(HashMap<String,String> hash){
		List<SysBotCkVo2> getSysBotCk = scriptDao.getSysBotCk_upbit(hash);
		return getSysBotCk;
	}
	@Override
	public List<SysBotCkVo> getSysBotCk_coinone(HashMap<String,String> hash){
		List<SysBotCkVo> getSysBotCk = scriptDao.getSysBotCk_coinone(hash);
		return getSysBotCk;
	}
	@Override
	public List<ApiVo> getApi(HashMap<String,String> hash){
		List<ApiVo> getApi = scriptDao.getApi(hash);
		return getApi;
	}

	@Override
	public JSONArray getScriptList() {
		List<ScriptVo> getScriptList = scriptDao.getScriptList();
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i<getScriptList.size();i++) {
			ScriptVo scriptVo = (ScriptVo) getScriptList.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("scriptNo", scriptVo.getScriptNo());
			jsonObject.put("side", scriptVo.getSide());
			jsonObject.put("side_num", scriptVo.getSide_num());
			jsonObject.put("log", scriptVo.getLog());
			jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}

	@Override
	public void script_insert(ScriptVo scriptVo) {
		
		scriptDao.script_insert(scriptVo);
		
	}

	@Override
	public JSONArray script_delete(String scriptNo) {

		scriptDao.script_delete(scriptNo);
		List<ScriptVo> getScriptList = scriptDao.getScriptList();
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i<getScriptList.size();i++) {
			ScriptVo scriptVo = (ScriptVo) getScriptList.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("scriptNo", scriptVo.getScriptNo());
			jsonObject.put("side", scriptVo.getSide());
			jsonObject.put("side_num", scriptVo.getSide_num());
			jsonObject.put("log", scriptVo.getLog());
			jsonArray.add(jsonObject);
		}
		
		return jsonArray;
		
	}

	@Override
	public void script_update(HashMap<String, String> hash) {
		scriptDao.script_update(hash);
		
	}

	@Override
	public ScriptVo getScriptOne(HashMap hash) {
		// TODO Auto-generated method stub
		return scriptDao.getScriptOne(hash);
		
	}


}
