package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;

public interface CoinoneRevenueService {

	public void setAvgPrice_coinone(String sitename,List<Report3> report3,User user) throws ParseException;
	public void setAvgPrice_coinone_latest(String sitename, List<Report3> coinoneReport_isNull, List<Report3> coinoneReport_latestRow, User user) throws ParseException;
	
	public List<ReportDto3> setCoinoneData(List<ReportDto3> report);
	public List<ReportDto3> setCoinoneData_latest(List<ReportDto3> report,List<ReportDto3> coinoneReport_latestRow);
	
	BigDecimal setRevenue3(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice) throws ParseException;
	
	BigDecimal convert_fee(String fee);
	
	public BigDecimal setAddTotalPriceCal(String sitename, BigDecimal ask_accUnits, BigDecimal bid_avgPrice)throws ParseException;
	
	public List<ReportDto3> replaceNull( List<ReportDto3> coinoneReport_latestRow);
}
