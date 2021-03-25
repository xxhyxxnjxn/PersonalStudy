package gmc.rd.report.service;

import java.text.ParseException;
import java.util.List;

import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.entity.User;

public abstract class RevenueLastestService {
	public abstract List<ReportDto> groupCurrency(User user); 
	
	public abstract List<ReportDto> findByNull(User user, String currency); 
	public abstract List<ReportDto> findByRow(User user, String currency); 

	public abstract void setAvgPrice(List<ReportDto> findByNullDto,List<ReportDto> findByRowDto,User user) throws ParseException;
	
	public String site = null;
	
	public List<ReportDto> groupByDto = null;
	public List<ReportDto> findByNullDto = null;
	public List<ReportDto> findByRowDto = null;
	
	public void findByRowDto(User user,String site) throws ParseException {
		groupByDto = groupCurrency(user);

		for(int i=0;i<groupByDto.size();i++) {
			
			findByNullDto = findByNull(user,groupByDto.get(i).getCurrency());
			findByRowDto = findByRow(user,groupByDto.get(i).getCurrency());
			//revenue(user);
			
		}
	}
	
	public void revenue(User user) throws ParseException {
		setAvgPrice(findByNullDto,findByRowDto, user);
	}
	
}
