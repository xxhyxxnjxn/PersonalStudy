package gmc.rd.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.TradeRepository;
import gmc.rd.report.repository.TradeRepository2;
import gmc.rd.report.repository.TradeRepository3;
@Service
public class TradeService {

	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private TradeRepository2 tradeRepository2;
	@Autowired
	private TradeRepository3 tradeRepository3;
	
	@Transactional
	public void 트레이딩일지작성(Report report, User user) {
		report.setUser(user);
		tradeRepository.save(report);
	
		
		
	}
	public List<Report> 트레이딩목록(User user,String orderby) {
		
		String memId = user.getMemId();
		System.out.println("--------------------------------------------"+orderby);

			if(orderby.equals("DESC")) {
				
				
				return tradeRepository.findByMemIdDesc(memId);	
						
			}else {
			return tradeRepository.findByMemIdAsc(memId);	
			}	

		
	
	}
	public List<Report2> 트레이딩목록2(User user,String orderby) {
		
		String memId = user.getMemId();		
		if(orderby.equals("DESC")) {
			return tradeRepository2.findByMemIdDesc(memId);	
					
		}else {
		return tradeRepository2.findByMemIdAsc(memId);	
		}
	
	}
	public List<Report3> 트레이딩목록3(User user,String orderby) {
		
		String memId = user.getMemId();		
		if(orderby.equals("DESC")) {
			return tradeRepository3.findByMemIdDesc(memId);	
					
		}else {
		return tradeRepository3.findByMemIdAsc(memId);	
		}
	
	}
	public List<Report> selectCoin(User user) {
		String memId = user.getMemId();
		return tradeRepository.findGroupbyCoin(memId);
	}
	public List<Report2> selectCoin2(User user) {
		String memId = user.getMemId();
		return tradeRepository2.findGroupbyCoin(memId);
	}
	public List<Report3> selectCoin3(User user) {
		String memId = user.getMemId();
		return tradeRepository3.findGroupbyCoin(memId);
	}
	public List<Report> 트레이딩사이트별목록(String site) {
		
		return tradeRepository.findBySite(site);
	}
}
