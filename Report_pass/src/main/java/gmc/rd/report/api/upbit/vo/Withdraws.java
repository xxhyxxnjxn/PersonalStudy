package gmc.rd.report.api.upbit.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Withdraws {
	
	private UpbitErrorVo error;
	
	private String type;		//입출금 종류	String
	
	@JsonProperty(value="orderId")
	private String uuid;		//출금의 고유 아이디	String
	private String currency;	//화폐를 의미하는 영문 대문자 코드	String
	private String txid;		//출금의 트랜잭션 아이디	String
	private String state;		//출금 상태	String
	@JsonProperty(value="transactionDate")
	private String created_at;	//출금 생성 시간	DateString
	//@JsonProperty(value="transactionDate")
	private String done_at;		//출금 완료 시간	DateString
	@JsonProperty(value="units")
	private String amount;		//출금 금액/수량	NumberString
	private String fee;			//출금 수수료	NumberString
	private String transaction_type; //	출금 유형
	
	
}
