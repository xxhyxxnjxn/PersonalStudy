package gmc.rd.report.api.upbit.service;

import java.util.HashMap;
import java.util.List;

import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitAccountVo;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;

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

	String getOrderDetail(HashMap<String, String> hashMap) throws Exception;

	String getBalance(HashMap<String, String> hashMap) throws Exception;
	
	List<Trades> getOrderDetail2(HashMap<String, String> hashMap) throws Exception;
	
}
