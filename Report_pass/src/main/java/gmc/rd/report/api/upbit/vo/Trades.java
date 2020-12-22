package gmc.rd.report.api.upbit.vo;

import lombok.Data;

@Data
public class Trades {
	private String market;
	private String uuid;
	private String price;
	private String volume;
	private String funds;
	private String created_at;
	private String side;

}
