package com.spring.member.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.member.vo.ApiVo;
import com.spring.member.vo.MemberVo;

@Repository("memberDao")
public class MemberDaoImpl implements MemberDao {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public MemberVo login(HashMap<String, Object> map) {
		
		MemberVo memberVo= sqlSession.selectOne("Mem.Login",map);
		
		return memberVo;
	}

	@Override
	public void setJoin(HashMap<String, Object> map) {
		sqlSession.insert("Mem.MemInsert", map);
	}
	
	@Override
	public void setJoin_kakao(HashMap<String, Object> map) {
		sqlSession.insert("Mem.MemInsert_Kakao", map);
	}

	@Override
	public MemberVo getMem_Kakao(HashMap<String, Object> map){
		MemberVo getMem_Kakao = sqlSession.selectOne("Mem.MemSelect_Kakao",map);
		
		return getMem_Kakao;
	}
	
	@Override
	public MemberVo getFindId(HashMap<String, Object> map) {
		MemberVo memberVo= sqlSession.selectOne("Mem.FindId",map);
		System.out.println(memberVo);
		return memberVo;
	}

	@Override
	public MemberVo getFindPw(HashMap<String, Object> map) {
		MemberVo memberVo= sqlSession.selectOne("Mem.FindPw",map);
		System.out.println("Memberdaoimpl : "+memberVo);
		return memberVo;
	}

	@Override
	public void setChangePw(HashMap<String, Object> map) {
		sqlSession.update("Mem.ChangePw", map);
		System.out.println("changepwdaoimpl : "+sqlSession.update("Mem.ChangePw", map));
	}

	@Override
	public void setApi(HashMap<String, Object> map) {
		sqlSession.insert("Mem.ApiInsert", map);
	}

	@Override
	public List<ApiVo> showApiKey(HashMap<String, Object> map) {
		List<ApiVo> getShowApi = sqlSession.selectList("Mem.ApiShow",map);
		System.out.println("ddddddd"+getShowApi);
		System.out.println(sqlSession.selectList("Mem.ApiShow",map));
		return getShowApi;
	}

	@Override
	public String checkId(HashMap<String, Object> map) {
		String result = sqlSession.selectOne("Mem.CheckId", map);
		//System.out.println("dao map : "+map);
		/*
		 * List<MemberVo> vo2 = (List<MemberVo>) map.get("result");
		 * System.out.println("dao"+vo2); String vo = vo2.get(0).getM_id();
		 * System.out.println("dao"+vo);
		 */
		return result;
	}
	
	@Override
	public void updateApi(HashMap<String, Object> map) {
		sqlSession.update("Mem.updateAPI", map);
	}
	
	@Override
	public void deleteApi(HashMap<String, Object> map) {
		sqlSession.delete("Mem.deleteAPI", map);
	}

	@Override
	public String checkConnect(HashMap<String, Object> map) {
		String result = sqlSession.selectOne("Mem.CheckConnect", map);
		
		return result;
	}
	
	@Override
	public String checkSecret(HashMap<String, Object> map) {
		String result = sqlSession.selectOne("Mem.CheckSecret", map);
		
		return result;
	}
	
	@Override
	public String checkSite(HashMap<String, Object> map) {
		String result = sqlSession.selectOne("Mem.CheckSite", map);
		
		return result;
	}

}
