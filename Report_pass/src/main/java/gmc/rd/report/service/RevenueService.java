package gmc.rd.report.service;

import java.text.ParseException;
import java.util.List;

import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.entity.User;

public abstract class RevenueService {
	
	public abstract List<ReportDto> groupCurrency(User user); 
	public abstract List<ReportDto> orderByData(User user, String currency);
	public abstract void setAvgPrice(List<ReportDto> reportDto,User user) throws ParseException;
	
	List<ReportDto> groupByDto = null;
	List<ReportDto> orderByDto = null;
	
	public void revenue(User user) throws ParseException {
		groupByDto = groupCurrency(user);
		orderByDto = null;
		for(int i=0;i<groupByDto.size();i++) {
			orderByDto = orderByData(user,groupByDto.get(i).getCurrency());
			
			setAvgPrice(orderByDto, user);
		}
	}
}
