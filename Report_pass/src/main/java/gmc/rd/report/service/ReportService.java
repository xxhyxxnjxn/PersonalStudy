package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;



public interface ReportService {
	
	public List<Report> selectReport(String sitename,List<VmDto> vmDto) throws JsonProcessingException, ParseException, Exception;
	public void setAvgPrice_bithumb(String sitename,List<Report> report,User user) throws ParseException;
	public void setAvgPrice_upbit(String sitename,List<Report2> report2,User user) throws ParseException;
	public void setAvgPrice_coinone(String sitename,List<Report3> report3,String currency,User user) throws ParseException;
	public List<Report> setBithumbData(List<Report> report);
	//public void setUpbitData(List<Report2> report);
	public List<Report3> setCoinoneData(List<Report3> report,String currency);
	public BigDecimal setRevenue(String site, BigDecimal bid_avgPrice,BigDecimal ask_price,BigDecimal ask_units) throws ParseException;
	public BigDecimal setClearRevenue(String site,BigDecimal bid_avgPrice,BigDecimal ask_avgPrice, BigDecimal bid_units_cal, BigDecimal ask_units_cal)throws ParseException;
	//public void getReport(List<Report> report) throws ParseException;
	//public void setClear(List<ReportDto> report) throws ParseException;
	List<Report2> setUpbitData(List<Report2> report2, User user) throws ParseException;
}
