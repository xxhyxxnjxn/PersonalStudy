package gmc.rd.report.api.upbit.vo;

import lombok.Data;

@Data
public class MarketAll {
	private String market; 			//	업비트에서 제공중인 시장 정보	String
	private String korean_name;		//거래 대상 암호화폐 한글명	String
	private String english_name	;	//거래 대상 암호화폐 영문명	String
}
