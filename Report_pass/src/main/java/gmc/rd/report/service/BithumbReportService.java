package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import gmc.rd.report.api.upbit.vo.Deposit;
import gmc.rd.report.api.upbit.vo.MarketAll;
import gmc.rd.report.api.upbit.vo.Withdraws;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;



public interface BithumbReportService {
	public void selectReport(String userId, String currency ,User user) throws JsonProcessingException, ParseException, Exception;
	public void selectCandleStick(User user) throws JsonProcessingException, ParseException, Exception;
	
}
