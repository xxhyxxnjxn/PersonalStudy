package com.spring.member.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.member.dao.MemberDao;
import com.spring.member.vo.ApiVo;
import com.spring.member.vo.MemberVo;


@Service("memberService")
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDao memberDao;

	@Override
	public MemberVo login(HashMap<String, Object> map) {
		MemberVo memberVo = memberDao.login(map);
		
		return memberVo;
	}

	@Override
	public void setJoin(HashMap<String, Object> map) {
		
			memberDao.setJoin(map);
		
	}
	
	///////////카카오
	@Override
	public void setJoin_kakao(HashMap<String, Object> map) {
		memberDao.setJoin_kakao(map);
	}
	@Override
	public MemberVo getMem_Kakao(HashMap<String, Object> map){
		MemberVo getMem_Kakao = memberDao.getMem_Kakao(map);
		return getMem_Kakao;
	}
	
	@Override
	public MemberVo getFindId(HashMap<String, Object> map) {
		MemberVo memberVo = memberDao.getFindId(map);
		
		return memberVo;
	}

	@Override
	public MemberVo getFindPw(HashMap<String, Object> map) {
		MemberVo memberVo = memberDao.getFindPw(map);
		
		return memberVo;
	}

	@Override
	public void setChangePw(HashMap<String, Object> map) {
		memberDao.setChangePw(map);
	}

	@Override
	public String checkId(HashMap<String, Object> map) {
		String result = memberDao.checkId(map);
		return result;
	}

	@Override
	public void setApi(HashMap<String, Object> map) {
		memberDao.setApi(map);
	}

	@Override
	public List<ApiVo> showApiKey(HashMap<String, Object> map) {
		List<ApiVo> getShowApi = memberDao.showApiKey(map);
		return getShowApi;
	}

	@Override
	public void updateApiKey(HashMap<String, Object> map) {
		memberDao.updateApi(map);
	}
	
	@Override
	public void deleteApiKey(HashMap<String, Object> map) {
		memberDao.deleteApi(map);
	}
	
	@Override
	public String checkConnect(HashMap<String, Object> map) {
		String result = memberDao.checkConnect(map);
		return result;
	}
	
	@Override
	public String checkSecret(HashMap<String, Object> map) {
		String result = memberDao.checkSecret(map);
		return result;
	}
	
	@Override
	public String checkSite(HashMap<String, Object> map) {
		String result = memberDao.checkSite(map);
		return result;
	}
	
}
