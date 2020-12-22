package gmc.rd.report.api.bithumb.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BithumbBalanceVo {
	private String status;
	private String message;
	private BithumbBalanceKrwVo data;
}
