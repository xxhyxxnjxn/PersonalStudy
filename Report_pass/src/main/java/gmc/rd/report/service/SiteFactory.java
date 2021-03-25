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

public abstract class SiteFactory {
	
	public abstract RevenueService factoryReturn(String site);
	public abstract RevenueLastestService factoryReturnLastes(String site);
	
}
