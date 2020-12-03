package com.spring.member.dao;

import java.util.HashMap;
import java.util.List;

import com.spring.member.vo.ApiVo;
import com.spring.member.vo.MemberVo;

public interface MemberDao {

   MemberVo login(HashMap<String, Object> map);

   void setJoin(HashMap<String, Object> map);
   
   void setJoin_kakao(HashMap<String, Object> map);
   MemberVo getMem_Kakao(HashMap<String, Object> map);
   
   MemberVo getFindId(HashMap<String, Object> map);

   MemberVo getFindPw(HashMap<String, Object> map);

   void setChangePw(HashMap<String, Object> map);

   void setApi(HashMap<String, Object> map);

   List<ApiVo> showApiKey(HashMap<String, Object> map);

   

   

   String checkId(HashMap<String, Object> map);
   
   void updateApi(HashMap<String, Object> map);
   
   void deleteApi(HashMap<String, Object> map);
   
   String checkConnect(HashMap<String, Object> map);
   
   String checkSecret(HashMap<String, Object> map);
   
   String checkSite(HashMap<String, Object> map);
   

}