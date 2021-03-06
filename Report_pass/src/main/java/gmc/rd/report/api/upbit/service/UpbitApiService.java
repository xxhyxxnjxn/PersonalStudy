package gmc.rd.report.api.upbit.service;

import java.util.HashMap;
import java.util.List;

import gmc.rd.report.api.upbit.vo.Deposit;
import gmc.rd.report.api.upbit.vo.MarketAll;
import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitAccountVo;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;
import gmc.rd.report.api.upbit.vo.Withdraws;

public interface UpbitApiService {
	UpbitAccountVo getAccounts(HashMap<String, String> hashMap);

	String getOrderBook(HashMap<String, String> hashMap);

	String getTicker(HashMap<String, String> hashMap);

	String marketSellOrder(HashMap<String, String> hashMap) throws Exception;

	String marketBuyOrder(HashMap<String, String> hashMap) throws Exception;

	String limiteSellOrder(HashMap<String, String> hashMap) throws Exception;

	String limiteBuyOrder(HashMap<String, String> hashMap) throws Exception;

	String cancelOrder(HashMap<String, String> hashMap) throws Exception;

	List<UpbitTransactionVo> getOrderList(HashMap<String, String> hashMap) throws Exception;
	
	public String getOrderList2(HashMap<String, String> hashMap) throws Exception;

	String getOrderDetail(HashMap<String, String> hashMap) throws Exception;

	String getBalance(HashMap<String, String> hashMap) throws Exception;
	
	List<Trades> getOrderDetail2(HashMap<String, String> hashMap) throws Exception;
	
	String getWithdraws(HashMap<String, String> hashMap) throws Exception;
	
	String getDeposits(HashMap<String, String> hashMap) throws Exception;
	
	List<MarketAll> getMarketAll() throws Exception;

	Withdraws getWithdrawsError(HashMap<String, String> hashMap) throws Exception;

	Deposit getDepositsError(HashMap<String, String> hashMap) throws Exception;

	String getCandleStick(HashMap<String, String> hashMap);
}
