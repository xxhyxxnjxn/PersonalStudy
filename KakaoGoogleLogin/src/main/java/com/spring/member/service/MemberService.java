package com.spring.member.service;

import java.util.HashMap;
import java.util.List;

import com.spring.member.vo.ApiVo;
import com.spring.member.vo.MemberVo;

public interface MemberService {

	public MemberVo login(HashMap<String, Object> map);

	public void setJoin(HashMap<String, Object> map);
	
	void setJoin_kakao(HashMap<String, Object> map);
	MemberVo getMem_Kakao(HashMap<String, Object> map);

	public MemberVo getFindId(HashMap<String, Object> map);

	public MemberVo getFindPw(HashMap<String, Object> map);

	public void setChangePw(HashMap<String, Object> map);

	public void setApi(HashMap<String, Object> map);

	public List<ApiVo> showApiKey(HashMap<String, Object> map);

	public String checkId(HashMap<String, Object> map);

	public void updateApiKey(HashMap<String, Object> map);
	
	public void deleteApiKey(HashMap<String, Object> map);
	
	public String checkConnect(HashMap<String, Object> map);
	
	public String checkSecret(HashMap<String, Object> map);
	
	public String checkSite(HashMap<String, Object> map);

}
