package gmc.rd.report.api.upbit.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpbitAccountVo {
	private UpbitErrorVo error;
	private List<UpbitAccountDataVo> data;

 
 
}
