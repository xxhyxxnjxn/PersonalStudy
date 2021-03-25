package gmc.rd.report.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
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
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private ReportRepository2 reportRepository2;
	@Autowired
	private ReportRepository3 reportRepository3;

	@Transactional
	public void 트레이딩일지작성(Report report, User user) {
		report.setUser(user);
		tradeRepository.save(report);
	}

	public Page<Report> 트레이딩목록1(User user, Pageable page, String currency, String start, String end, String type)
			throws Exception {
		Page<Report> list = null;
		if (start == "" || end == "") {
			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					list = tradeRepository.findByUser(user, page);
				} else {
					list = tradeRepository.findByUserAndType(user, type, page);
				}

			} else {
				if (type.equals("ALL")) {
					list = tradeRepository.findByUserAndCurrency(user, currency, page);
				} else {
					list = tradeRepository.findByUserAndCurrencyAndType(user, currency, type, page);
				}

			}

		} else {

			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = fm.parse(start);
			long startlong = startDate.getTime();
			String start2 = String.valueOf(startlong);

			Date endDate = fm.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
			long endlong = endDate.getTime();
			String end2 = String.valueOf(endlong);

			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					list = tradeRepository.findByUserAndTransactionDateBetween(user, start2, end2, page);
				} else {
					list = tradeRepository.findByUserAndTypeAndTransactionDateBetween(user, type, start2, end2, page);
				}

			} else {
				if (type.equals("ALL")) {
					list = tradeRepository.findByUserAndCurrencyAndTransactionDateBetween(user, currency, start2, end2,
							page);
				} else {
					list = tradeRepository.findByUserAndCurrencyAndTypeAndTransactionDateBetween(user, currency, type,
							start2, end2, page);
				}

			}

		}

		return list;
	}

	public Page<Report2> 트레이딩목록2(User user, Pageable page, String currency, String start, String end, String type)
			throws Exception {
		Page<Report2> list = null;

		if (start == "" || end == "") {
			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					list = tradeRepository2.findByUser(user, page);
				} else {
					list = tradeRepository2.findByUserAndType(user, type, page);
				}

			} else {
				if (type.equals("ALL")) {
					list = tradeRepository2.findByUserAndCurrency(user, currency, page);
				} else {
					list = tradeRepository2.findByUserAndCurrencyAndType(user, currency, type, page);
				}

			}

		} else {

			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = fm.parse(start);
			long startlong = startDate.getTime();
			String start2 = String.valueOf(startlong);

			Date endDate = fm.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();

			long endlong = endDate.getTime();
			String end2 = String.valueOf(endlong);

			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					list = tradeRepository2.findByUserAndTransactionDateBetween(user, start2, end2, page);
				} else {
					list = tradeRepository2.findByUserAndTypeAndTransactionDateBetween(user, type, start2, end2, page);
				}

			} else {
				if (type.equals("ALL")) {
					list = tradeRepository2.findByUserAndCurrencyAndTransactionDateBetween(user, currency, start2, end2,
							page);
				} else {
					list = tradeRepository2.findByUserAndCurrencyAndTypeAndTransactionDateBetween(user, currency, type,
							start2, end2, page);
				}

			}

		}

		return list;
	}

	public Page<Report3> 트레이딩목록3(User user, Pageable page, String currency, String start, String end, String type)
			throws Exception {
		Page<Report3> list = null;
		if (start == "" || end == "") {
			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					list = tradeRepository3.findByUser(user, page);
				} else {
					list = tradeRepository3.findByUserAndType(user, type, page);
				}

			} else {
				if (type.equals("ALL")) {
					list = tradeRepository3.findByUserAndCurrency(user, currency, page);
				} else {
					list = tradeRepository3.findByUserAndCurrencyAndType(user, currency, type, page);
				}

			}

		} else {

			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = fm.parse(start);
			long startlong = startDate.getTime();
			String start2 = String.valueOf(startlong);

			Date endDate = fm.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
			long endlong = endDate.getTime();
			String end2 = String.valueOf(endlong);

			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					list = tradeRepository3.findByUserAndTransactionDateBetween(user, start2, end2, page);
				} else {
					list = tradeRepository3.findByUserAndTypeAndTransactionDateBetween(user, type, start2, end2, page);
				}

			} else {
				if (type.equals("ALL")) {
					list = tradeRepository3.findByUserAndCurrencyAndTransactionDateBetween(user, currency, start2, end2,
							page);
				} else {
					list = tradeRepository3.findByUserAndCurrencyAndTypeAndTransactionDateBetween(user, currency, type,
							start2, end2, page);
				}

			}

		}

		return list;
	}

	public List<Report> bankstatement(User user) {

		String memId = user.getMemId();
		return tradeRepository.findByBankStatement(memId);
	}

	public List<Report2> bankstatement2(User user,String orderby,String type,String priceSelect) {

		String memId = user.getMemId();
		
		//List<Report2> bankstatement2_data = tradeRepository2.findByBankStatement(memId);
		
		List<Report2> bankstatement2_data = null ;
		
		if(priceSelect.equals("ALL")) {
			if(orderby.equals("DESC")) {
				if(type.equals("ALL")) {
					bankstatement2_data = tradeRepository2.findByBankStatement(memId);
				}else {
					bankstatement2_data = tradeRepository2.findByBankStatementandType(memId,type);
				}
				
			}else {
				if(type.equals("ALL")) {
					bankstatement2_data = tradeRepository2.findByBankStatement2(memId);
				}else {
					bankstatement2_data = tradeRepository2.findByBankStatementandType2(memId,type);
				}
			}
			
		}else {
			if(orderby.equals("DESC")) {
				if(type.equals("ALL")) {
					bankstatement2_data = tradeRepository2.findByBankStatement(memId,"0.0");
				}else {
					bankstatement2_data = tradeRepository2.findByBankStatementandType(memId,type,"0.0");
				}
				
			}else {
				if(type.equals("ALL")) {
					bankstatement2_data = tradeRepository2.findByBankStatement2(memId,"0.0");
				}else {
					bankstatement2_data = tradeRepository2.findByBankStatementandType2(memId,type,"0.0");
				}
			}
			
		}
		
		
		return bankstatement2_data;
	}

	public List<Report3> bankstatement3(User user) {

		String memId = user.getMemId();
		return tradeRepository3.findByBankStatement(memId);
	}
	
	public List<Report> bankstatement0(User user) {

		String memId = user.getMemId();
		return tradeRepository.findByBankStatement0(memId);
	}

	public List<Report2> bankstatement02(User user) {

		String memId = user.getMemId();
		return tradeRepository2.findByBankStatement0(memId);
	}

	public List<Report3> bankstatement03(User user) {

		String memId = user.getMemId();
		return tradeRepository3.findByBankStatement0(memId);
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


	@Transactional
	public void bankstatementup(String orderId, String price) {
		Report2 report = tradeRepository2.findByOrderId(orderId); // 영속화완료
		System.err.println(report);
		report.setPrice(price);
	}

	@Transactional
	public void bankstatementbi(String orderId, String price) {
		Report report = tradeRepository.findByOrderId(orderId); // 영속화완료
		System.err.println(report);
		report.setPrice(price);
	}

	@Transactional
	public void bankstatementco(String orderId, String price) {
		Report3 report = tradeRepository3.findByOrderId(orderId); // 영속화완료
		System.err.println(report);
		report.setPrice(price);
	}
	
	@Transactional
	public long findCount(User user, String currency,String start,String end,String type) throws ParseException {
		long count;
		
		//list = reportRepository.findByCount(memId,currency, start, end, type);
		
		if (start == "" || end == "") {
			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					count = tradeRepository.countByUser(user);
				} else {
					count = tradeRepository.countByUserAndType(user, type);
				}

			} else {
				if (type.equals("ALL")) {
					count = tradeRepository.countByUserAndCurrency(user, currency);
				} else {
					count = tradeRepository.countByUserAndCurrencyAndType(user, currency, type);
				}

			}

		} else {

			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = fm.parse(start);
			long startlong = startDate.getTime();
			String start2 = String.valueOf(startlong);

			Date endDate = fm.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
			long endlong = endDate.getTime();
			String end2 = String.valueOf(endlong);

			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					count = tradeRepository.countByUserAndTransactionDateBetween(user, start2, end2);
				} else {
					count = tradeRepository.countByUserAndTypeAndTransactionDateBetween(user, type, start2, end2);
				}

			} else {
				if (type.equals("ALL")) {
					count = tradeRepository.countByUserAndCurrencyAndTransactionDateBetween(user, currency, start2, end2);
				} else {
					count = tradeRepository.countByUserAndCurrencyAndTypeAndTransactionDateBetween(user, currency, type,
							start2, end2);
				}

			}

		}
		
		
		//System.out.println("DDDDDDDDDDDDDD"+list);
		return count;
	}
	
	@Transactional
	public long findCount2(User user, String currency,String start,String end,String type) throws ParseException {
		long count;
		
		//list = reportRepository.findByCount(memId,currency, start, end, type);
		
		if (start == "" || end == "") {
			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					count = tradeRepository2.countByUser(user);
				} else {
					count = tradeRepository2.countByUserAndType(user, type);
				}

			} else {
				if (type.equals("ALL")) {
					count = tradeRepository2.countByUserAndCurrency(user, currency);
				} else {
					count = tradeRepository2.countByUserAndCurrencyAndType(user, currency, type);
				}

			}

		} else {

			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = fm.parse(start);
			long startlong = startDate.getTime();
			String start2 = String.valueOf(startlong);

			Date endDate = fm.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
			long endlong = endDate.getTime();
			String end2 = String.valueOf(endlong);

			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					count = tradeRepository2.countByUserAndTransactionDateBetween(user, start2, end2);
				} else {
					count = tradeRepository2.countByUserAndTypeAndTransactionDateBetween(user, type, start2, end2);
				}

			} else {
				if (type.equals("ALL")) {
					count = tradeRepository2.countByUserAndCurrencyAndTransactionDateBetween(user, currency, start2, end2);
				} else {
					count = tradeRepository2.countByUserAndCurrencyAndTypeAndTransactionDateBetween(user, currency, type,
							start2, end2);
				}

			}

		}
		
		
		//System.out.println("DDDDDDDDDDDDDD"+list);
		return count;
	}
	
	@Transactional
	public long findCount3(User user, String currency,String start,String end,String type) throws ParseException {
		long count;
		
		//list = reportRepository.findByCount(memId,currency, start, end, type);
		
		if (start == "" || end == "") {
			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					count = tradeRepository3.countByUser(user);
				} else {
					count = tradeRepository3.countByUserAndType(user, type);
				}

			} else {
				if (type.equals("ALL")) {
					count = tradeRepository3.countByUserAndCurrency(user, currency);
				} else {
					count = tradeRepository3.countByUserAndCurrencyAndType(user, currency, type);
				}

			}

		} else {

			SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = fm.parse(start);
			long startlong = startDate.getTime();
			String start2 = String.valueOf(startlong);

			Date endDate = fm.parse(end);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			endDate = cal.getTime();
			long endlong = endDate.getTime();
			String end2 = String.valueOf(endlong);

			if (currency.equals("ALL")) {
				if (type.equals("ALL")) {
					count = tradeRepository3.countByUserAndTransactionDateBetween(user, start2, end2);
				} else {
					count = tradeRepository3.countByUserAndTypeAndTransactionDateBetween(user, type, start2, end2);
				}

			} else {
				if (type.equals("ALL")) {
					count = tradeRepository3.countByUserAndCurrencyAndTransactionDateBetween(user, currency, start2, end2);
				} else {
					count = tradeRepository3.countByUserAndCurrencyAndTypeAndTransactionDateBetween(user, currency, type,
							start2, end2);
				}

			}

		}
		
		
		//System.out.println("DDDDDDDDDDDDDD"+list);
		return count;
	}
}
