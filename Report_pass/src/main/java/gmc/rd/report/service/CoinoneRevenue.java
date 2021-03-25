package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.dto.ReportDtoMapper2;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.RevenueStateRepository;

@Service
public class CoinoneRevenue extends RevenueService{
	private final ReportRepository3 reportRepository3;
	
	private RevenueStateRepository revenueStateRepository;
	
	BigDecimal bid_price = new BigDecimal("0.0");
	BigDecimal bid_units = new BigDecimal("0.0");
	BigDecimal ask_price = new BigDecimal("0.0");
	BigDecimal ask_units = new BigDecimal("0.0");
	
	String sitename = "coinone";
	
	public CoinoneRevenue(ReportRepository3 reportRepository3, RevenueStateRepository revenueStateRepository) {
		this.reportRepository3 = reportRepository3;
		this.revenueStateRepository = revenueStateRepository;
	}
	
	@Override
	public List<ReportDto> groupCurrency(User user){
		List<Report3> report = reportRepository3.groupByCurrency(user.getMemId());
		List<ReportDto> reportDto = ReportDtoMapper3.INSTANCE.toDto2(report);
		return reportDto;
	}
	
	@Override
	public List<ReportDto> orderByData(User user, String currency){
		List<Report3> orderByData = reportRepository3.orderByTransactionDate(user.getMemId(),currency);
		List<ReportDto> reportDto = ReportDtoMapper3.INSTANCE.toDto2(orderByData);
		
		return reportDto;
	}
	
	@Override
	public void setAvgPrice(List<ReportDto> reportDto,User user) throws ParseException{
		BigDecimal bid_totalPrice_cal = new BigDecimal("0.0"); // 누적 totalPrice -> 평균 단가 구하기 위한 변수 (매도 할때마다 수익률 구하기위한)
		BigDecimal ask_totalPrice_cal = new BigDecimal("0.0");

		BigDecimal bid_units_cal = new BigDecimal("0.0"); // 누적 units -> 평균 단가 구하기 위한 변수 (매도 할 때마다 수익률 구하기 위한)
		BigDecimal ask_units_cal = new BigDecimal("0.0");

		BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
		BigDecimal ask_totalPrice = new BigDecimal("0.0");

		BigDecimal bid_avgPrice = new BigDecimal("0.0"); // 수익률 구하기 위한 평균 매수 매도가 
		BigDecimal ask_avgPrice = new BigDecimal("0.0");

		BigDecimal bid_fee = new BigDecimal("0.0");
		BigDecimal ask_fee = new BigDecimal("0.0");
		
		BigDecimal bid_accUnits = new BigDecimal("0.0");
		
		BigDecimal total_rate_cal = new BigDecimal("0.0"); // 누적 정산 비율
		BigDecimal cal = new BigDecimal("0.0"); // 과거 누적 수익 금액 저장 공간
		
		BigDecimal ask_accUnits= new BigDecimal("0.0");
		BigDecimal askAccUnits= new BigDecimal("0.0");
		
		BigDecimal totalPriceCal= new BigDecimal("0.0");
		
		BigDecimal bidTotalPriceCal= new BigDecimal("0.0");
		BigDecimal bidUnitsCal= new BigDecimal("0.0");
		BigDecimal askTotalPriceCal= new BigDecimal("0.0");
		BigDecimal askUnitsCal= new BigDecimal("0.0");

		Report3 report_entity = null;

		//List<ReportDto3> reportDto = ReportDtoMapper3.INSTANCE.toDto(coinoneReport);
		
		List<ReportDto> report = setCoinoneData(reportDto);
		
		revenueStateRepository.updateAsTotalNum(String.valueOf(report.size()), user.getMemId(), sitename);
		for (int i = report.size() - 1; i >= 0; i--) {
			revenueStateRepository.updateAsNum(String.valueOf(report.size()-i), user.getMemId(), sitename);
			//report.get(i).setUser(user);

			if (report.get(i).getType().equals("매수")||report.get(i).getType().equals("입금")) {
				
					if (report.get(i).getPrice() == null) {
						report.get(i).setPrice("0.0");
					}
				
					if (!ask_totalPrice_cal.toString().equals("0.0") && !ask_units_cal.toString().equals("0.0")&& !ask_accUnits.toString().equals("0.0")) {
						askTotalPriceCal = ask_totalPrice_cal;
						askUnitsCal = ask_units_cal;
						askAccUnits = ask_accUnits;
					}

					report.get(i).setAskAvgPrice(ask_avgPrice.toPlainString());
					report.get(i).setAskTotalPriceCal(askTotalPriceCal.toPlainString());
					report.get(i).setAskUnitsCal(askUnitsCal.toPlainString());
					report.get(i).setAskAccUnits(askAccUnits.toPlainString());
				
					ask_totalPrice_cal = new BigDecimal("0.0");
					ask_units_cal = new BigDecimal("0.0");

					total_rate_cal = new BigDecimal("0.0"); // 누적 정산 비율 초기화
					cal = new BigDecimal("0.0"); // 과거 누적 수익 금액 저장 공간
					
					
					bid_price = new BigDecimal(report.get(i).getPrice());
					bid_units = new BigDecimal(report.get(i).getUnits());
					
					//System.out.println("매수 부분 price : " + bid_price); // 매수 시작 부분
					//System.out.println("매수 부분 units : " + bid_units); // 매수 시작 부분

					bid_fee = convert_fee(report.get(i).getFee()); // 수익률 계산하기 해서 수수료 저장
					//bid_fee = bid_price.multiply(bid_fee);
					//report.get(i).setFee(bid_fee.toPlainString());
					
					bid_totalPrice = bid_price.multiply(bid_units);
					//bid_totalPrice = bid_totalPrice.subtract(bid_fee);
					
					report.get(i).setTotalPrice(bid_totalPrice.toPlainString()); // totalPrice 저장

					
					bid_units_cal = bid_units_cal.add(bid_units); // 매수 누적 수량 + 남은 코인 수량이 더해져야 하는 거지
					
//					if(!ask_accUnits.toPlainString().equals("0.0")&&!ask_accUnits.toPlainString().equals("0.0000")) {
//						totalPriceCal = setAddTotalPriceCal(sitename,ask_accUnits,bid_avgPrice);
//						bid_units_cal = bid_units_cal.add(ask_accUnits);
//						
//					}
					report.get(i).setBidUnitsCal(bid_units_cal.toPlainString());
					
					bid_totalPrice_cal = bid_totalPrice_cal.add(bid_totalPrice); 
					bid_totalPrice_cal = bid_totalPrice_cal.add(totalPriceCal); // 매도 한 번 했을 때 남은 수량에 대한 그 다음 매수 누적 정산 금액에 더해준다
					
					//bid_units_cal = bid_units_cal.add(bid_units); // 매수 누적 수량 + 남은 코인 수량이 더해져야 하는 거지

					bid_accUnits = new BigDecimal(report.get(i).getAccUnits());
					report.get(i).setBidAccUnits(bid_accUnits.toPlainString());

					bid_avgPrice = bid_totalPrice_cal.divide(bid_units_cal, 8, BigDecimal.ROUND_DOWN); // 정산 금액 -> 수수료 포함

					report.get(i).setBidAvgPrice(bid_avgPrice.toPlainString()); // 평균 단가 저장

					report.get(i).setBidTotalPriceCal(bid_totalPrice_cal.toPlainString()); // 매수 누적 정산 금액
					
					totalPriceCal = new BigDecimal("0.0"); // 더해주자마자 바로 초기화
					ask_accUnits = new BigDecimal("0.0"); // 더해주자마자 바로 초기화
					
					bidTotalPriceCal = bid_totalPrice_cal;
				

			} else if (report.get(i).getType().equals("매도")||report.get(i).getType().equals("출금")) {

					if (report.get(i).getPrice() == null) {
						report.get(i).setPrice("0.0");
					}
					if(!bid_units_cal.toString().equals("0.0")) {
						bidUnitsCal = bid_units_cal;
					}
					report.get(i).setBidAvgPrice(bid_avgPrice.toPlainString());
					report.get(i).setBidUnitsCal(bidUnitsCal.toPlainString());
					report.get(i).setBidAccUnits(bid_accUnits.toPlainString());
					
					bid_totalPrice_cal = new BigDecimal("0.0");
					bid_units_cal = new BigDecimal("0.0");

					ask_price = new BigDecimal(report.get(i).getPrice());
					ask_units = new BigDecimal(report.get(i).getUnits());

					//System.out.println("매도 부분 price : " + ask_price); // 매도 부분
					//System.out.println("매도 부분 units : " + ask_units); // 매도 부분

					ask_fee = convert_fee(report.get(i).getFee()); // 수익률 계산하기 해서 수수료 저장

					ask_totalPrice = ask_price.multiply(ask_units);
					ask_totalPrice = ask_totalPrice.subtract(ask_fee);
					
					report.get(i).setTotalPrice(ask_totalPrice.toPlainString()); // totalPrice 저장

					ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
					report.get(i).setAskTotalPriceCal(ask_totalPrice_cal.toPlainString());
					
					ask_units_cal = ask_units_cal.add(ask_units);
					report.get(i).setAskUnitsCal(ask_units_cal.toPlainString());

					ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal, 8, BigDecimal.ROUND_DOWN);

					report.get(i).setAskAvgPrice(ask_avgPrice.toPlainString()); // 평균 단가 저장

					ask_accUnits = new BigDecimal(report.get(i).getAccUnits()); // 정산이 안됐을 때 계산해줘야할 수량
					report.get(i).setAskAccUnits(ask_accUnits.toPlainString());
					
					/////////////////////////////////////////////////////////////////////
					// 매도 할 때마다 수익률을 구하기 위한 메서드
					// 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
					if (bidTotalPriceCal.toString().equals("0.0")) {
						System.out.println("매수 기록이 없습니다.");
					} else {
						BigDecimal revenue_cal = setRevenue3_second(sitename, bid_avgPrice, ask_avgPrice,ask_units_cal,ask_accUnits);
						report.get(i).setRevenue(revenue_cal.toPlainString());
						
						//System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
						
						// 추가 할 것 : 수익금액, 누적
						BigDecimal total_rate = ask_units.divide(bid_accUnits, 8, BigDecimal.ROUND_DOWN); // 정산 비율
						total_rate_cal = total_rate_cal.add(total_rate); // 누적 정산 비율
						
						BigDecimal expect_income = bidTotalPriceCal.multiply(revenue_cal); // 예상 수익금
						BigDecimal income_cal = expect_income.multiply(total_rate_cal); // 누적 수익 금액
						
						BigDecimal income = income_cal.subtract(cal); // 현재 누적 수익 금액 - 과거 누적 수익 금액 = 개별 수익 금액
						cal = income_cal;
						
						report.get(i).setBidTotalPriceCal(bidTotalPriceCal.toPlainString()); // 매수 누적 정산 금액
						report.get(i).setTotalRate(total_rate.toPlainString()); // 정산비율
						report.get(i).setTotalRateCal(total_rate_cal.toPlainString()); // 누적 정산비율
						report.get(i).setExpectIncome(expect_income.toPlainString()); // 예상 수익금
						report.get(i).setIncomeCal(income_cal.toPlainString()); // 누적 수익 금액
						report.get(i).setIncome(income.toPlainString()); // 개별 수익 금액
						report.get(i).setCal(cal.toPlainString()); // 과거 누적 수익 금액
					}
					//////////////////////////////////////////////////////////////////////
				

			}

			bid_price = new BigDecimal("0.0");
			bid_units = new BigDecimal("0.0");
			ask_price = new BigDecimal("0.0");
			ask_units = new BigDecimal("0.0");

			// Report report_entity = new Report();
//			int x = reportRepository2.updateReport(report.get(i).getOrderId(), report.get(i).getSite(), report.get(i).getTransactionDate(), report.get(i).getCurrency(), report.get(i).getType(), report.get(i).getPrice(), report.get(i).getUnits(), report.get(i).getBidUnitsCal(), 
//			report.get(i).getAskUnitsCal(), report.get(i).getBidAvgPrice(), report.get(i).getAskAvgPrice(), report.get(i).getFee(), report.get(i).getTotalPrice(), report.get(i).getBidTotalPriceCal(), report.get(i).getAskTotalPriceCal(), report.get(i).getRevenue(), report.get(i).getAccUnits(), report.get(i).getCal(),
//			report.get(i).getTotalRate(), report.get(i).getTotalRateCal(), report.get(i).getExpectIncome(), report.get(i).getIncomeCal(), report.get(i).getIncome(), user.getMemId(), report.get(i).getAskAccUnits(), report.get(i).getBidAccUnits());
//수익률 업데이트 쿼리문		
		}

		String type = "test";
		String before_type = "test";

		revenueStateRepository.updateAsTotalNum(String.valueOf(report.size()), user.getMemId(), sitename);
		for (int i = 0; i <report.size(); i++) {
			revenueStateRepository.updateAsNum(String.valueOf(i), user.getMemId(), sitename);
			type = report.get(i).getType();

			if (type.equals(before_type) && type.equals("매수")) {
				reportRepository3.deleteByList(report.get(i).getOrderId());

			} else if (type.equals(before_type) && type.equals("매도")) {
				reportRepository3.deleteByList(report.get(i).getOrderId());
			}

			before_type = report.get(i).getType();

			int x = reportRepository3.updateReport(report.get(i).getOrderId(), report.get(i).getSite(),
					report.get(i).getTransactionDate(), report.get(i).getCurrency(), report.get(i).getType(),
					report.get(i).getPrice(), report.get(i).getUnits(), report.get(i).getBidUnitsCal(),
					report.get(i).getAskUnitsCal(), report.get(i).getBidAvgPrice(), report.get(i).getAskAvgPrice(),
					report.get(i).getFee(), report.get(i).getTotalPrice(), report.get(i).getBidTotalPriceCal(),
					report.get(i).getAskTotalPriceCal(), report.get(i).getRevenue(), report.get(i).getAccUnits(),
					report.get(i).getCal(), report.get(i).getTotalRate(), report.get(i).getTotalRateCal(),
					report.get(i).getExpectIncome(), report.get(i).getIncomeCal(), report.get(i).getIncome(),
					user.getMemId(), report.get(i).getAskAccUnits(), report.get(i).getBidAccUnits());
		}
	}

	public List<ReportDto> setCoinoneData(List<ReportDto> report3) {
		BigDecimal accUnits = new BigDecimal("0.0000");
		BigDecimal ask_units_cal = new BigDecimal("0.0000");
		BigDecimal bid_units_cal = new BigDecimal("0.0000");

		for (int i = report3.size() - 1; i >= 0; i--) { // 과거순부터 계산


			if (report3.get(i).getType().equals("매도")) {
				//report3.get(i).setType("매도");
				if (!bid_units_cal.toString().equals("0.0000")) {
					
					ask_units_cal = new BigDecimal(report3.get(i).getUnits());
					accUnits = accUnits.subtract(ask_units_cal);
					
				} else {
					accUnits = new BigDecimal("0.0000");
				}
				
				if(Double.valueOf(accUnits.toString()) < 0.0000){
					accUnits = new BigDecimal("0.0000");
					
				}

			} else if (report3.get(i).getType().equals("매수")) {
				//report3.get(i).setType("매수");
			
				bid_units_cal = new BigDecimal(report3.get(i).getUnits());
				accUnits = accUnits.add(bid_units_cal);
				
				
				
				
			} else if (report3.get(i).getType().equals("입금")) {
				//report3.get(i).setType("입금");
			
				bid_units_cal = new BigDecimal(report3.get(i).getUnits());
				accUnits = accUnits.add(bid_units_cal);
				
				BigDecimal price = new BigDecimal(report3.get(i).getPrice());
				String fee_str = report3.get(i).getFee();
				
				if(Pattern.matches(".*[a-zA-Z]+.*", fee_str)) {
					BigDecimal fee = new BigDecimal(fee_str.replaceAll("[A-Z]", ""));
					
					fee = fee.multiply(price);
					report3.get(i).setFee(fee.toPlainString());
				}
				
				
			} else if (report3.get(i).getType().equals("출금")) {
				//report3.get(i).setType("출금");
				
				if (!bid_units_cal.toString().equals("0.0000")) {
					
					ask_units_cal = new BigDecimal(report3.get(i).getUnits());
					accUnits = accUnits.subtract(ask_units_cal);
					
				}else {
					accUnits = new BigDecimal("0.0000");
				}
				
				if(Double.valueOf(accUnits.toString()) < 0.0000){
					accUnits = new BigDecimal("0.0000");
				}
				
				BigDecimal price = new BigDecimal(report3.get(i).getPrice());
				String fee_str = report3.get(i).getFee();
				
				if(Pattern.matches(".*[a-zA-Z]+.*", fee_str)) {
					BigDecimal fee = new BigDecimal(fee_str.replaceAll("[A-Z]", ""));
					
					fee = fee.multiply(price);
					report3.get(i).setFee(fee.toPlainString());
				}
			}

			report3.get(i).setAccUnits(accUnits.toString());

		}

		return report3;
	}

	public BigDecimal setRevenue3(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice)
			throws ParseException { // 매도 한번 했을 때 수익률

		BigDecimal zero = BigDecimal.ZERO;
		BigDecimal revenue = new BigDecimal("0.0");

		if (bid_avgPrice.compareTo(zero) > 0) {
			revenue = ask_avgPrice.subtract(bid_avgPrice);
			revenue = revenue.divide(bid_avgPrice, 8, BigDecimal.ROUND_DOWN);
		}
		return revenue;

	}

	   public BigDecimal setRevenue3_second(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice,BigDecimal ask_units,BigDecimal ask_accUnits)
				throws ParseException { // 매도 한번 했을 때 수익률

			// 매도 평균 단가 - 매수 평균 단가 = A
			// A/매수 평균단가
			// 여기서는 시세차이만 본다는 생각으로...
			BigDecimal zero = BigDecimal.ZERO;
			BigDecimal revenue = new BigDecimal("0.0");
			
			//if(!bid_avgPrice.toPlainString().equals("0.0000")) {
			//if(!"0.00000000".equals(bid_avgPrice.toPlainString())||!bid_avgPrice.toPlainString().equals("0.0000")) {
			if(bid_avgPrice.compareTo(zero)>0) {
				bid_avgPrice = bid_avgPrice.multiply(ask_units);
				ask_avgPrice = ask_avgPrice.multiply(ask_units);
				
				revenue = ask_avgPrice.subtract(bid_avgPrice);
				revenue = revenue.divide(bid_avgPrice, 8, BigDecimal.ROUND_DOWN);
			}
	      return revenue;

		}
	   
	public BigDecimal convert_fee(String fee) {

		String convert = fee.replace(",", "");
		BigDecimal convert_fee = new BigDecimal(convert);

		return convert_fee;
	}

	public BigDecimal setAddTotalPriceCal(String sitename, BigDecimal ask_accUnits, BigDecimal bid_avgPrice)
			throws ParseException { // 매도 한번 했을 때 수익률

		BigDecimal fee = new BigDecimal("0.002");
		BigDecimal totalPriceCal = new BigDecimal("0.0");



		BigDecimal totalPrice = bid_avgPrice.multiply(ask_accUnits); // 금액
		BigDecimal fee_cal = totalPrice.multiply(fee); // 수수료
		totalPriceCal = totalPrice.add(fee_cal);

		return totalPriceCal;

	}
}
