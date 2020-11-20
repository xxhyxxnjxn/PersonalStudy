package com.spring.script.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.script.dao.ScriptDao;
import com.spring.script.service.ScriptService;
import com.spring.script.vo.ScriptVo;

@Repository("ScriptService")
public class ScriptSerivceImpl implements ScriptService{

	@Autowired
	ScriptDao scriptDao;
	
	@Override
	public void setScript(HashMap<String,String> hash) {
		scriptDao.setScript(hash);
	}
	
	@Override
	public void updateScript(HashMap<String,String> hash) {
		scriptDao.updateScript(hash);
	}
	@Override
	public List<ScriptVo> getScript(HashMap<String,String> hash){
		List<ScriptVo> getScript = scriptDao.getScript(hash);
		return getScript;
	}
}
