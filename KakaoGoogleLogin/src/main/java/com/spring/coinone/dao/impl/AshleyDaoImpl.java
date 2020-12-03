package com.spring.coinone.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.coinone.dao.AshleyDao;
import com.spring.script.vo.ApiVo;
import com.spring.script.vo.BotSignVO;
import com.spring.script.vo.SysOrderlistVo;
@Repository("ashleyDao")
public class AshleyDaoImpl implements AshleyDao {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<BotSignVO> showBotSign(HashMap<String, String> hash) {
		List<BotSignVO> showBotSign  = sqlSession.selectList("system.showBotSign",hash);
		//= (List<BotSignVO>) hashMap.get("result");
		return 	showBotSign;
	}

	@Override
	public List<ApiVo> getMemKey(HashMap<String, Object> getApiMap) {
		List<ApiVo> getMemKey  = sqlSession.selectList("system.getMemKey",getApiMap);
		//= (List<BotSignVO>) hashMap.get("result");
		return 	getMemKey;
	}

	@Override
	public void insertOrderlist(HashMap orderMap) {
		sqlSession.insert("system.insertOrderlist",orderMap);
		
	}

	@Override
	public void UpdateBotSign(HashMap<String, Object> hashUpdate0) {
		sqlSession.update("system.updateBotSign",hashUpdate0);
		
	}

	@Override
	public List<SysOrderlistVo> getAssetCoin(HashMap<String, Object> getHash) {
		List<SysOrderlistVo> getAssetCoin = sqlSession.selectList("system.showBalanceCoin",getHash);
		return 	getAssetCoin;
	}

	@Override
	public void UpdateBotSignExit(HashMap<String, Object> hashUpdate0) {
		sqlSession.update("system.UpdateBotSignExit",hashUpdate0);
	}

	@Override
	public List<BotSignVO> showBotSignExit(HashMap<String, String> hash) {
		List<BotSignVO> showBotSignExit  = sqlSession.selectList("system.showBotSignExit",hash);
		System.out.println("2------------------------------------------------------------------------------------------------------------------------"+showBotSignExit);
		//= (List<BotSignVO>) hashMap.get("result");
		return 	showBotSignExit;
	}
	
	

	
}
