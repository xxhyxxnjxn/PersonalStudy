package gmc.rd.report.service;

import java.util.List;

import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Report;

public interface VmService {
	public List<VmDto> getVm();
	public List<Report> getVmOne(String siteName)throws Exception;
	public VmDto getVmOne2(String siteName,String vmId);
}
