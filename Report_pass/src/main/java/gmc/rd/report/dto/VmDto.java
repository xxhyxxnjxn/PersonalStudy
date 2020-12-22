package gmc.rd.report.dto;


import lombok.Data;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
public class VmDto {
	private int idx;
	private String vmId;
	private String vmName;
	private String siteName;

}
