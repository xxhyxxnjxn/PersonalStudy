package gmc.rd.report.service;

import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;

public interface CoinoneReportService {
	public void selectReport(User user, ApiDto apiDto, String currency) throws JsonProcessingException, ParseException, Exception;
	

	public void selectReportUpdate(User user, ApiDto apiDto, String currency) throws Exception;
	
	public void selectCandleStcik(User user) throws Exception;
	
	public void selectCandleStcikUpdate(User user) throws Exception;

	public void selectBankStateMent(User user, ApiDto apiDto, String currency) throws Exception;


	void selectBankStateMentUpdate(User user, ApiDto apiDto, String currency) throws Exception;
}
