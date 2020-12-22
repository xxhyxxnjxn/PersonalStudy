package gmc.rd.report.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	private int id;
	private String password;
	private String email;
	private String username;
	
	
}
