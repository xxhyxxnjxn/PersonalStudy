package com.spring.script.service;

import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.spring.script.vo.SysBotCkVo;
import com.spring.script.vo.SysBotCkVo2;

public interface OrderService {
	void bithumbOrder(List<SysBotCkVo> sysBotList_bithumb,String exit_side, HashMap<String, String> hashMap) throws ParseException;
	void upbitOrder(List<SysBotCkVo2> sysBotList_upbit,String exit_side, HashMap<String, String> hash) throws ParseException;

}
