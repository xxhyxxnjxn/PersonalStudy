package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.User;

public interface BithumbRevenueService {
	public void setAvgPrice_bithumb(String sitename,List<Report> report,User user) throws ParseException;
	public void setAvgPrice_bithumb_latest(String sitename, List<Report> bithumbReport_isNull, List<Report> bithumbReport_latestRow, User user) throws ParseException;
	public List<ReportDto> setBithumbData(List<ReportDto> report);
	public List<ReportDto> setBithumbData_latest(List<ReportDto> report,List<ReportDto> bithumbReport_latestRow);
	BigDecimal setRevenue3(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice) throws ParseException;
	BigDecimal convert_fee(String fee);
	public BigDecimal setAddTotalPriceCal(String sitename, BigDecimal ask_accUnits, BigDecimal bid_avgPrice)throws ParseException;
	
	public List<ReportDto> replaceNull( List<ReportDto> bithumbReport_latestRow);
}
