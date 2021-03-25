package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.RevenueStateRepository;

@Service
public class FactoryReturn extends SiteFactory{

	
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private RevenueStateRepository revenueStateRepository;
	@Autowired
	private ReportRepository2 reportRepository2;
	@Autowired
	private ReportRepository3 reportRepository3;
	
	
	@Override
	public RevenueService factoryReturn(String site) {
		switch(site) {
		case "bithumb" : return new BithumbRevenue(reportRepository,revenueStateRepository);
		case "upbit" : return new UpbitRevenue(reportRepository2,revenueStateRepository);
		case "coinone" : return new CoinoneRevenue(reportRepository3,revenueStateRepository);
		}
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RevenueLastestService factoryReturnLastes(String site) {
		switch(site) {
		case "bithumb" : return new BithumbRevenueLastest(reportRepository,revenueStateRepository);
		case "upbit" : return new UpbitRevenueLastest(reportRepository2,revenueStateRepository);
		case "coinone" : return new CoinoneRevenueLastest(reportRepository3,revenueStateRepository);
		}
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
