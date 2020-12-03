package com.spring.coinone.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.script.vo.ApiVo;
import com.spring.script.vo.BotSignVO;
import com.spring.script.vo.SysOrderlistVo;


public interface AshleyDao {

	List<BotSignVO> showBotSign(HashMap<String, String> hash);

	List<ApiVo> getMemKey(HashMap<String, Object> getApiMap);

	void insertOrderlist(HashMap orderMap);

	void UpdateBotSign(HashMap<String, Object> hashUpdate0);

	List<SysOrderlistVo> getAssetCoin(HashMap<String, Object> getHash);

	void UpdateBotSignExit(HashMap<String, Object> hashUpdate0);

	List<BotSignVO> showBotSignExit(HashMap<String, String> hash);

	

}
