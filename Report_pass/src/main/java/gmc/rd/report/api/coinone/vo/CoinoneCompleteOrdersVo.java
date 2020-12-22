package gmc.rd.report.api.coinone.vo;

import java.util.List;

public class CoinoneCompleteOrdersVo {
	private String result;
	private String errorCode;
	private String errorMsg;
	
	private List<CoinoneTransactionVo> completeOrders;

	public List<CoinoneTransactionVo> getCompleteOrders() {
		return completeOrders;
	}

	public void setCompleteOrders(List<CoinoneTransactionVo> completeOrders) {
		this.completeOrders = completeOrders;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


}
