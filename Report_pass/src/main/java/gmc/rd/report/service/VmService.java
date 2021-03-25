package gmc.rd.report.service;

import java.util.List;

import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;

public interface VmService {

	public void getVmCoinone(User user) throws Exception;
	
	public void getVmBithumb(String userId, User user)throws Exception;
	public void getVmBithumbUpdate(String userId, User user)throws Exception;

	void getVmCoinoneUpdate(User user) throws Exception;

}
