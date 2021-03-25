package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;

public interface UpbitRevenueService {
	public void setAvgPrice_upbit(String sitename,List<Report2> report2,User user) throws ParseException;
	public void setAvgPrice_upbit_latest(String sitename, List<Report2> upbitReport_isNull, List<Report2> upbitReport_latestRow, User user) throws ParseException;
	
	public List<ReportDto2> setUpbitData(List<ReportDto2> report2) throws ParseException;
	public List<ReportDto2> setUpbitData_latest(List<ReportDto2> report,List<ReportDto2> upbitReport_latestRow);
	
	BigDecimal setRevenue3(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice) throws ParseException;
	
	BigDecimal convert_fee(String fee);
	
	public BigDecimal setAddTotalPriceCal(String sitename, BigDecimal ask_accUnits, BigDecimal bid_avgPrice)throws ParseException;
	
	public List<ReportDto2> replaceNull( List<ReportDto2> upbitReport_latestRow);
}
