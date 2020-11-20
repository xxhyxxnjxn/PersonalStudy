package com.spring.script.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.script.dao.ScriptDao;
import com.spring.script.vo.ScriptVo;

@Repository("ScriptDao")
public class ScriptDaoImpl implements ScriptDao{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void setScript(HashMap<String,String> hash){
		sqlSession.insert("script.ScriptInsert",hash);
	}
	@Override
	public void updateScript(HashMap<String,String> hash) {
		sqlSession.update("script.ScriptUpdate",hash);
	}
	@Override
	public List<ScriptVo> getScript(HashMap<String,String> hash){
		List<ScriptVo> getScript = sqlSession.selectList("script.ScriptSelect",hash);
		return getScript;
	}
}
