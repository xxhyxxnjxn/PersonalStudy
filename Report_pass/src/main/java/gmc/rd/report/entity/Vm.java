package gmc.rd.report.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="vm_tbl")
public class Vm {

	@Id // primary key
	@GeneratedValue (strategy = GenerationType.IDENTITY)// 프로젝트에서 연결된 DB 의 넘버링 전략을 따라간다.
	private int idx;
	@Column(nullable = false, length = 50)
	private String vmId;
	@Column(nullable = false, length = 50)
	private String vmName;
	@Column(nullable = false, length = 50)
	private String siteName;
}
