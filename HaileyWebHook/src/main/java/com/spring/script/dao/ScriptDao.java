package com.spring.script.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.script.vo.ScriptVo;


public interface ScriptDao {
	void setScript(HashMap<String,String> hash);
	void updateScript(HashMap<String,String> hash);
	List<ScriptVo> getScript(HashMap<String,String> hash);
}
