package gmc.rd.report.service;

import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import gmc.rd.report.api.upbit.vo.Deposit;
import gmc.rd.report.api.upbit.vo.MarketAll;
import gmc.rd.report.api.upbit.vo.Withdraws;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;

public interface UpbitReportService {
	public List<Report2> selectReport(String sitename, int page_cnt, User user) throws JsonProcessingException, ParseException, Exception;
	public List<Report2> selectReport2(String sitename, int page_cnt, User user) throws JsonProcessingException, ParseException, Exception;
	public List<MarketAll> getCoinList() throws Exception;
	public List<Withdraws> getWithdraw(String userId, String currencyselect,User user,Api api) throws Exception;
	public List<Deposit> getDeposit(String userId, String currencyselect,User user,Api api) throws Exception;
	
	public void selectCandleStick(User user)throws JsonMappingException, JsonProcessingException;
}
