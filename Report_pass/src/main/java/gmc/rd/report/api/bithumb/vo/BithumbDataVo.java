package gmc.rd.report.api.bithumb.vo;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BithumbDataVo {
	private String status;
	private List<BithumbTransactionVo> data;

}
