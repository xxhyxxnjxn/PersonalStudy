package gmc.rd.report.api.upbit.vo;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetail {
	private String uuid	;
	private String side	;
	private String ord_type;
	private String price;
	private String state;
	private String market;
	private String created_at;
	private String volume;
	private String remaining_volume;
	private String reserved_fee;
	private String remaining_fee;
	private String paid_fee;
	private String locked;
	private String executed_volume;
	private String trades_count;
	
	private List<Trades> trades;
}
