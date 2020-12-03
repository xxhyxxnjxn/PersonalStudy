package com.spring.script.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.script.dao.ScriptDao;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.ScriptVo;
import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;
import com.spring.script.vo.SysOrderlistVo;

@Repository("ScriptDao")
public class ScriptDaoImpl implements ScriptDao{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void setScript(HashMap<String,String> hash){
		sqlSession.insert("script.ScriptInsert",hash);
	}
	@Override
	public void updateScript_buy(HashMap<String,String> hash) {
		sqlSession.update("script.Script_Update_buy",hash);
	}
	@Override
	public void updateSysBotCk_buy(HashMap<String,String> hash) {
		sqlSession.update("script.SysBotCk_Update_buy",hash);
	}
	@Override
	public void updateScript_sell(HashMap<String,String> hash) {
		sqlSession.update("script.Script_Update_sell",hash);
	}
	@Override
	public void updateSysBotCk_sell(HashMap<String,String> hash) {
		sqlSession.update("script.SysBotCk_Update_sell",hash);
	}
	
	@Override
	public List<ScriptVo> getScript(HashMap<String,String> hash){
		List<ScriptVo> getScript = sqlSession.selectList("script.ScriptSelect",hash);
		return getScript;
	}
	@Override
	public List<SysBotCkVo> getSysBotCk_bithumb(HashMap<String,String> hash){
		List<SysBotCkVo> getSysBotCk = sqlSession.selectList("script.SysBotCk_Bithumb_Select",hash);
		return getSysBotCk;
	}
	@Override
	public List<SysBotCkVo2> getSysBotCk_upbit(HashMap<String,String> hash){
		List<SysBotCkVo2> getSysBotCk = sqlSession.selectList("script.SysBotCk_Upbit_Select",hash);
		return getSysBotCk;
	}
	@Override
	public List<SysBotCkVo> getSysBotCk_coinone(HashMap<String,String> hash){
		List<SysBotCkVo> getSysBotCk = sqlSession.selectList("script.SysBotCk_Coinone_Select",hash);
		return getSysBotCk;
	}
	@Override
	public List<ApiVo> getApi(HashMap<String,String> hash){
		List<ApiVo> getApi = sqlSession.selectList("script.Api_Select",hash);
		return getApi;
	}
	@Override

	public void setOrderListTbl(HashMap<String, String> deHash) {
		sqlSession.insert("script.OrderListInsert",deHash);
		
	}

	public void bithumb_orderlist_insert(HashMap<String, String> hash) {
		sqlSession.insert("script.Bithumb_orderlist_insert",hash);
		
	}
	@Override
	public SysOrderlistVo bithumb_orderlist_select(HashMap<String, String> hash) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("script.Bithumb_orderlist_select",hash);
	}
	
	@Override
	public SysOrderlistVo getOrderListTbl(HashMap<String, String> getHash) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("script.Upbit_orderlist_select",getHash);
	}
	@Override
	public SysOrderlistVo upbit_orderlist_select(HashMap<String, String> hash) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("script.Upbit_orderlist_select",hash);
	}
	@Override
	public List<ScriptVo> getScriptList() {
		// TODO Auto-generated method stub
		return  sqlSession.selectList("script.getScriptList");
	}
	@Override
	public void script_insert(ScriptVo scriptVo) {
		sqlSession.selectList("script.ScriptInsert",scriptVo);
		
	}
	@Override
	public void script_delete(String scriptNo) {
		sqlSession.delete("script.ScriptDelete",scriptNo);
		
	}
	@Override
	public void script_update(HashMap<String, String> hash) {
		sqlSession.update("script.ScriptUpdate", hash);
		
	}
	@Override
	public ScriptVo getScriptOne(HashMap hash) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("script.ScriptSelectOne", hash);

	}

}
