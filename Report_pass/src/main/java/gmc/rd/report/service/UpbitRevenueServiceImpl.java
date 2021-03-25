package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.dto.ReportDtoMapper2;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ReportRepository2;

@Service
public class UpbitRevenueServiceImpl implements UpbitRevenueService{

	// 업비트
	@Autowired
	private ReportRepository2 reportRepository2;
	
	BigDecimal bid_price = new BigDecimal("0.0");
	BigDecimal bid_units = new BigDecimal("0.0");
	BigDecimal ask_price = new BigDecimal("0.0");
	BigDecimal ask_units = new BigDecimal("0.0");
	
	@Transactional
	@Override
	public void setAvgPrice_upbit(String sitename, List<Report2> upbitReport, User user) throws ParseException {

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
		
		List<Report2> reportEntity = null;
		List<ReportDto2> reportDto = ReportDtoMapper2.INSTANCE.toDto(upbitReport);
		
		Report2 report_entity = null;
		
		

		List<ReportDto2> report = setUpbitData(reportDto);

		for (int i = 0; i < report.size(); i++) {
			
			if (report.get(i).getType().equals("매수")||report.get(i).getType().equals("입금")) {
				
				if (report.get(i).getPrice() == null) {
					report.get(i).setPrice("0.0");
				}
				if(!ask_totalPrice_cal.toString().equals("0.0") && !ask_units_cal.toString().equals("0.0")&& !ask_accUnits.toString().equals("0.0")) {
					askTotalPriceCal=ask_totalPrice_cal;
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

				bid_totalPrice = bid_price.multiply(bid_units);
				bid_totalPrice = bid_totalPrice.add(bid_fee);
				
				report.get(i).setTotalPrice(bid_totalPrice.toPlainString()); // totalPrice 저장

				
				bid_units_cal = bid_units_cal.add(bid_units); // 매수 누적 수량 + 남은 코인 수량이 더해져야 하는 거지
				
				if(!ask_accUnits.toPlainString().equals("0.0")&&!ask_accUnits.toPlainString().equals("0.0000")) {
					totalPriceCal = setAddTotalPriceCal(sitename,ask_accUnits,bid_avgPrice);
					bid_units_cal = bid_units_cal.add(ask_accUnits);
					
				}
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
					BigDecimal revenue_cal = setRevenue3(sitename, bid_avgPrice, ask_avgPrice);
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

			int x = reportRepository2.updateReport(report.get(i).getOrderId(), report.get(i).getSite(), report.get(i).getTransactionDate(), report.get(i).getCurrency(), report.get(i).getType(), report.get(i).getPrice(), report.get(i).getUnits(), report.get(i).getBidUnitsCal(), 
					report.get(i).getAskUnitsCal(), report.get(i).getBidAvgPrice(), report.get(i).getAskAvgPrice(), report.get(i).getFee(), report.get(i).getTotalPrice(), report.get(i).getBidTotalPriceCal(), report.get(i).getAskTotalPriceCal(), report.get(i).getRevenue(), report.get(i).getAccUnits(), report.get(i).getCal(),
					report.get(i).getTotalRate(), report.get(i).getTotalRateCal(), report.get(i).getExpectIncome(), report.get(i).getIncomeCal(), report.get(i).getIncome(), user.getMemId(), report.get(i).getAskAccUnits(), report.get(i).getBidAccUnits());
//			String tempId = String.valueOf(report.get(i).getOrderId());
//			report_entity = reportRepository2.findByOrderId(tempId);
//
//			if (report_entity != null) {
//
//				report_entity.setIdx(0);
//				
//				report_entity.setSite(report.get(i).getSite());
//				report_entity.setOrderId(report.get(i).getOrderId());
//				report_entity.setTransactionDate(report.get(i).getTransactionDate());
//				report_entity.setCurrency(report.get(i).getCurrency());
//				report_entity.setType(report.get(i).getType());
//				report_entity.setPrice(report.get(i).getPrice());
//				report_entity.setUnits(report.get(i).getUnits());
//				
//				report_entity.setBidUnitsCal(report.get(i).getBidUnitsCal());
//				report_entity.setAskUnitsCal(report.get(i).getAskUnitsCal());
//				
//				report_entity.setBidAvgPrice(report.get(i).getBidAvgPrice());
//				report_entity.setAskAvgPrice(report.get(i).getAskAvgPrice());
//				
//				report_entity.setFee(report.get(i).getFee());
//				report_entity.setTotalPrice(report.get(i).getTotalPrice());
//				
//				report_entity.setBidTotalPriceCal(report.get(i).getBidTotalPriceCal());
//				report_entity.setAskTotalPriceCal(report.get(i).getAskTotalPriceCal());
//				
//				report_entity.setRevenue(report.get(i).getRevenue());
//				report_entity.setAccUnits(report.get(i).getAccUnits());
//				
//				report_entity.setCal(report.get(i).getCal());
//				
//				report_entity.setTotalRate(report.get(i).getTotalRate());
//				report_entity.setTotalRateCal(report.get(i).getTotalRateCal());
//				report_entity.setExpectIncome(report.get(i).getExpectIncome());
//				report_entity.setIncomeCal(report.get(i).getIncomeCal());
//				report_entity.setIncome(report.get(i).getIncome());
//				
//				report_entity.setUser(user);
//
//			}

		}

	}

	@Transactional
	@Override
	public List<ReportDto2> setUpbitData(List<ReportDto2> abc) throws ParseException {

		BigDecimal totalacc = new BigDecimal("0.0000");
		BigDecimal askacc = new BigDecimal("0.0000");
		BigDecimal bidacc = new BigDecimal("0.0000");

		//String equals_currency = abc.get(0).getCurrency();

		for (int i = 0; i < abc.size(); i++) {

			if (abc.get(i).getType().equals("매수")) {
				//abc.get(i).setType("매수");
				
				bidacc = new BigDecimal(abc.get(i).getUnits());
				totalacc = totalacc.add(bidacc);

				abc.get(i).setAccUnits(totalacc.toPlainString());

			} else if (abc.get(i).getType().equals("매도")) {
				//abc.get(i).setType("매도");
				
				if (!bidacc.toString().equals("0.0000")) {
					askacc = new BigDecimal(abc.get(i).getUnits());
					totalacc = totalacc.subtract(askacc);

				} else {
					totalacc = new BigDecimal("0.0000");
				}
				
				if (Double.valueOf(totalacc.toString()) <= 0.00000000) {
					totalacc = new BigDecimal("0.0000");
				} 

				abc.get(i).setAccUnits(totalacc.toPlainString());

			}else if(abc.get(i).getType().equals("출금")) { // 출금 - 매도
				//abc.get(i).setType("출금");
				
				if (!bidacc.toString().equals("0.0000")) {
					askacc = new BigDecimal(abc.get(i).getUnits());
					totalacc = totalacc.subtract(askacc);

				} else {
					totalacc = new BigDecimal("0.0000");
				}
				
				if (Double.valueOf(totalacc.toString()) <= 0.00000000) {
					totalacc = new BigDecimal("0.0000");
				} 

				abc.get(i).setAccUnits(totalacc.toPlainString());
				
			} else if(abc.get(i).getType().equals("입금")) { // 입금 - 매수
				//abc.get(i).setType("입금");
				
				bidacc = new BigDecimal(abc.get(i).getUnits());
				totalacc = totalacc.add(bidacc);

				abc.get(i).setAccUnits(totalacc.toPlainString());
			}

		}
		return abc;
	}
	
	@Override
	@Transactional
	public void setAvgPrice_upbit_latest(String sitename, List<Report2> upbitReport_isNullEntity, List<Report2> upbitReport_latestRowEntity, User user) throws ParseException { 

		List<ReportDto2> upbitReport_latestRow = ReportDtoMapper2.INSTANCE.toDto(upbitReport_latestRowEntity);
		List<ReportDto2> upbitReport_isNull = ReportDtoMapper2.INSTANCE.toDto(upbitReport_isNullEntity);
		
		upbitReport_latestRow = replaceNull(upbitReport_latestRow);
		
		BigDecimal bid_totalPrice_cal= new BigDecimal(upbitReport_latestRow.get(0).getBidTotalPriceCal()); 
		BigDecimal ask_totalPrice_cal = new BigDecimal(upbitReport_latestRow.get(0).getAskTotalPriceCal());
		
		BigDecimal bid_units_cal= new BigDecimal(upbitReport_latestRow.get(0).getBidUnitsCal()); 
		BigDecimal ask_units_cal = new BigDecimal(upbitReport_latestRow.get(0).getAskUnitsCal()); 

		BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
		BigDecimal ask_totalPrice = new BigDecimal("0.0");

		BigDecimal bid_avgPrice = new BigDecimal(upbitReport_latestRow.get(0).getBidAvgPrice()); 
		BigDecimal ask_avgPrice = new BigDecimal(upbitReport_latestRow.get(0).getAskAvgPrice());

		BigDecimal bid_fee = new BigDecimal("0.0");
		BigDecimal ask_fee = new BigDecimal("0.0");
		
		BigDecimal bid_accUnits = new BigDecimal(upbitReport_latestRow.get(0).getBidAccUnits());
		
		BigDecimal total_rate_cal= new BigDecimal(upbitReport_latestRow.get(0).getTotalRateCal());
		BigDecimal cal = new BigDecimal(upbitReport_latestRow.get(0).getCal()); 
		
		BigDecimal ask_accUnits = new BigDecimal(upbitReport_latestRow.get(0).getAskAccUnits());
		BigDecimal askAccUnits= new BigDecimal("0.0");
		
		BigDecimal totalPriceCal= new BigDecimal("0.0");
		
		BigDecimal bidTotalPriceCal= new BigDecimal(upbitReport_latestRow.get(0).getBidTotalPriceCal()); 
		BigDecimal bidUnitsCal= new BigDecimal("0.0");
		BigDecimal askTotalPriceCal= new BigDecimal("0.0");
		BigDecimal askUnitsCal= new BigDecimal("0.0");
		
		Report2 report_entity = null;

		List<ReportDto2> report = setUpbitData_latest(upbitReport_isNull, upbitReport_latestRow);
		
		for (int i = 0; i <report.size(); i++) {
			
			if (report.get(i).getType().equals("매수")||report.get(i).getType().equals("입금")) {
				
				if (report.get(i).getPrice() == null) {
					report.get(i).setPrice("0.0");
				}
			
				if (!ask_totalPrice_cal.toString().equals("0.0") && !ask_units_cal.toString().equals("0.0")&& !ask_accUnits.toString().equals("0.0")) {
					askTotalPriceCal = ask_totalPrice_cal;
					askUnitsCal = ask_units_cal;
					askAccUnits = ask_accUnits;
				}

				if(i==0 && upbitReport_latestRow.get(0).getType().equals("매도")) {
					bid_totalPrice_cal= new BigDecimal("0.0");
					bid_units_cal= new BigDecimal("0.0"); 
				}else if(i==0 && upbitReport_latestRow.get(0).getType().equals("매수")) {
					ask_accUnits = new BigDecimal("0.0");
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
					
					bid_fee = convert_fee(report.get(i).getFee()); // 수익률 계산하기 해서 수수료 저장

					bid_totalPrice = bid_price.multiply(bid_units);
					bid_totalPrice = bid_totalPrice.add(bid_fee);
					
					report.get(i).setTotalPrice(bid_totalPrice.toPlainString()); // totalPrice 저장

					
					bid_units_cal = bid_units_cal.add(bid_units); // 매수 누적 수량 + 남은 코인 수량이 더해져야 하는 거지
					
					if(!ask_accUnits.toPlainString().equals("0.0")&&!ask_accUnits.toPlainString().equals("0.0000")) {
						totalPriceCal = setAddTotalPriceCal(sitename,ask_accUnits,bid_avgPrice);
						bid_units_cal = bid_units_cal.add(ask_accUnits);
						
						
					}
					report.get(i).setBidUnitsCal(bid_units_cal.toPlainString());
					
					bid_totalPrice_cal = bid_totalPrice_cal.add(bid_totalPrice); 
					bid_totalPrice_cal = bid_totalPrice_cal.add(totalPriceCal); // 매도 한 번 했을 때 남은 수량에 대한 그 다음 매수 누적 정산 금액에 더해준다
					
					System.out.println(bid_accUnits);
					System.out.println(report.get(i).getAccUnits());
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
					
					if(i==0 && upbitReport_latestRow.get(0).getType().equals("매수")) {
						ask_totalPrice_cal= new BigDecimal("0.0");
						ask_units_cal= new BigDecimal("0.0"); 
					}
					
					report.get(i).setBidAvgPrice(bid_avgPrice.toPlainString());
					report.get(i).setBidUnitsCal(bidUnitsCal.toPlainString());
					report.get(i).setBidAccUnits(bid_accUnits.toPlainString());
					
					bid_totalPrice_cal = new BigDecimal("0.0");
					bid_units_cal = new BigDecimal("0.0");

					ask_price = new BigDecimal(report.get(i).getPrice());
					ask_units = new BigDecimal(report.get(i).getUnits());


					ask_fee = convert_fee(report.get(i).getFee()); // 수익률 계산하기 해서 수수료 저장

					ask_totalPrice = ask_price.multiply(ask_units);
					ask_totalPrice = ask_totalPrice.subtract(ask_fee);
					
					report.get(i).setTotalPrice(ask_totalPrice.toPlainString()); // totalPrice 저장

					ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
					report.get(i).setAskTotalPriceCal(ask_totalPrice_cal.toPlainString());
					
					ask_units_cal = ask_units_cal.add(ask_units);
					report.get(i).setAskUnitsCal(ask_units_cal.toString());

					ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal, 8, BigDecimal.ROUND_DOWN);

					report.get(i).setAskAvgPrice(ask_avgPrice.toPlainString()); // 평균 단가 저장

					System.out.println(report.get(i).getAccUnits());
					ask_accUnits = new BigDecimal(report.get(i).getAccUnits()); // 정산이 안됐을 때 계산해줘야할 수량
					report.get(i).setAskAccUnits(ask_accUnits.toPlainString());
					
					// 매도 할 때마다 수익률을 구하기 위한 메서드
					// 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
					if (bidTotalPriceCal.toString().equals("0.0")) {
						System.out.println("매수 기록이 없습니다.");
					} else {
						BigDecimal revenue_cal = setRevenue3(sitename, bid_avgPrice, ask_avgPrice);
						report.get(i).setRevenue(revenue_cal.toPlainString());
						
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

			}

			bid_price = new BigDecimal("0.0");
			bid_units = new BigDecimal("0.0");
			ask_price = new BigDecimal("0.0");
			ask_units = new BigDecimal("0.0");

			int x = reportRepository2.updateReport(report.get(i).getOrderId(), report.get(i).getSite(), report.get(i).getTransactionDate(), report.get(i).getCurrency(), report.get(i).getType(), report.get(i).getPrice(), report.get(i).getUnits(), report.get(i).getBidUnitsCal(), 
					report.get(i).getAskUnitsCal(), report.get(i).getBidAvgPrice(), report.get(i).getAskAvgPrice(), report.get(i).getFee(), report.get(i).getTotalPrice(), report.get(i).getBidTotalPriceCal(), report.get(i).getAskTotalPriceCal(), report.get(i).getRevenue(), report.get(i).getAccUnits(), report.get(i).getCal(),
					report.get(i).getTotalRate(), report.get(i).getTotalRateCal(), report.get(i).getExpectIncome(), report.get(i).getIncomeCal(), report.get(i).getIncome(), user.getMemId(), report.get(i).getAskAccUnits(), report.get(i).getBidAccUnits());
			
//			String tempId = String.valueOf(report.get(i).getOrderId());
//			report_entity = reportRepository2.findByOrderId(tempId);
//
//			if (report_entity != null) {
//
//				report_entity.setIdx(0);
//				
//				report_entity.setSite(report.get(i).getSite());
//				report_entity.setOrderId(report.get(i).getOrderId());
//				report_entity.setTransactionDate(report.get(i).getTransactionDate());
//				report_entity.setCurrency(report.get(i).getCurrency());
//				report_entity.setType(report.get(i).getType());
//				report_entity.setPrice(report.get(i).getPrice());
//				report_entity.setUnits(report.get(i).getUnits());
//				
//				report_entity.setBidUnitsCal(report.get(i).getBidUnitsCal());
//				report_entity.setAskUnitsCal(report.get(i).getAskUnitsCal());
//				
//				report_entity.setBidAvgPrice(report.get(i).getBidAvgPrice());
//				report_entity.setAskAvgPrice(report.get(i).getAskAvgPrice());
//				
//				report_entity.setFee(report.get(i).getFee());
//				report_entity.setTotalPrice(report.get(i).getTotalPrice());
//				
//				report_entity.setBidTotalPriceCal(report.get(i).getBidTotalPriceCal());
//				report_entity.setAskTotalPriceCal(report.get(i).getAskTotalPriceCal());
//				
//				report_entity.setRevenue(report.get(i).getRevenue());
//				report_entity.setAccUnits(report.get(i).getAccUnits());
//				
//				report_entity.setCal(report.get(i).getCal());
//				
//				report_entity.setTotalRate(report.get(i).getTotalRate());
//				report_entity.setTotalRateCal(report.get(i).getTotalRateCal());
//				report_entity.setExpectIncome(report.get(i).getExpectIncome());
//				report_entity.setIncomeCal(report.get(i).getIncomeCal());
//				report_entity.setIncome(report.get(i).getIncome());
//				
//				report_entity.setUser(user);
//
//			}

		}

	}
	
	@Transactional
	@Override
	public List<ReportDto2> setUpbitData_latest(List<ReportDto2> abc,List<ReportDto2> upbitReport_latestRow) {

		BigDecimal totalacc = new BigDecimal("0.0000");
		BigDecimal askacc = new BigDecimal("0.0000");
		BigDecimal bidacc = new BigDecimal("0.0000");
		
		
		for (int i = 0; i <abc.size(); i++) {
			
			if(i == 0 && upbitReport_latestRow.get(0).getAccUnits()!="0.0000") {
				totalacc = new BigDecimal(upbitReport_latestRow.get(0).getAccUnits());
				System.out.println("DD"+totalacc);
			}

			if (abc.get(i).getType().equals("매수")) {
				//abc.get(i).setType("매수");
				
				bidacc = new BigDecimal(abc.get(i).getUnits());
				totalacc = totalacc.add(bidacc);

				abc.get(i).setAccUnits(totalacc.toPlainString());

			} else if (abc.get(i).getType().equals("매도")) {
				//abc.get(i).setType("매도");
				
				askacc = new BigDecimal(abc.get(i).getUnits());
				totalacc = totalacc.subtract(askacc);
				
				if (Double.valueOf(totalacc.toString()) <= 0.00000000) {
					totalacc = new BigDecimal("0.0000");
				} 

				abc.get(i).setAccUnits(totalacc.toPlainString());

			}else if(abc.get(i).getType().equals("출금")) { // 출금 - 매도
				//abc.get(i).setType("출금");
				
				askacc = new BigDecimal(abc.get(i).getUnits());
				totalacc = totalacc.subtract(askacc);

				
				if (Double.valueOf(totalacc.toString()) <= 0.00000000) {
					totalacc = new BigDecimal("0.0000");
				} 

				abc.get(i).setAccUnits(totalacc.toPlainString());
				
			} else if(abc.get(i).getType().equals("입금")) { // 입금 - 매수
				//abc.get(i).setType("입금");
				
				bidacc = new BigDecimal(abc.get(i).getUnits());
				totalacc = totalacc.add(bidacc);

				abc.get(i).setAccUnits(totalacc.toPlainString());
			}

			
		}
		return abc;

	}
	
	@Transactional
	   @Override
	   public BigDecimal setRevenue3(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice)
	         throws ParseException { // 매도 한번 했을 때 수익률


	      //매도 평균 단가 - 매수 평균 단가 = A
	      //A/매수 평균단가
			BigDecimal zero = BigDecimal.ZERO;
			BigDecimal revenue = new BigDecimal("0.0");
			
			//if(!"0.00000000".equals(bid_avgPrice.toPlainString()) || !bid_avgPrice.toPlainString().equals("0.0000")) {
			if(bid_avgPrice.compareTo(zero)>0) {
				revenue = ask_avgPrice.subtract(bid_avgPrice);
				revenue = revenue.divide(bid_avgPrice, 8, BigDecimal.ROUND_DOWN);
			}
	      return revenue;

	   }
	  
	@Override
	public BigDecimal convert_fee(String fee) {
		
		String convert = fee.replace(",", "");
		BigDecimal convert_fee = new BigDecimal(convert);
		
		
		return convert_fee;
	}
	
	
	
	@Override
	 public BigDecimal setAddTotalPriceCal(String sitename, BigDecimal ask_accUnits, BigDecimal bid_avgPrice)throws ParseException { // 매도 한번 했을 때 수익률


			BigDecimal fee = new BigDecimal("0.0005");
			BigDecimal totalPriceCal = new BigDecimal("0.0");
			
//			if (sitename.equals("bithumb")) {
//				fee = new BigDecimal("0.0025");
//	
//			} else if (sitename.equals("coinone")) {
//				fee = new BigDecimal("0.002");
//	
//			} else if (sitename.equals("upbit")) {
//				fee = new BigDecimal("0.0005");
//
//			}

			BigDecimal totalPrice = bid_avgPrice.multiply(ask_accUnits); // 금액
			BigDecimal fee_cal = totalPrice.multiply(fee); // 수수료
			totalPriceCal = totalPrice.add(fee_cal);
			
	      return totalPriceCal;

	   }
	
	@Override
	public List<ReportDto2> replaceNull( List<ReportDto2> upbitReport_latestRow){
		if(upbitReport_latestRow.get(0).getBidTotalPriceCal()==null) {
			upbitReport_latestRow.get(0).setBidTotalPriceCal("0.0");
		}
		if(upbitReport_latestRow.get(0).getAskTotalPriceCal()==null) {
			upbitReport_latestRow.get(0).setAskTotalPriceCal("0.0");
		}
		
		if(upbitReport_latestRow.get(0).getBidUnitsCal()==null) {
			upbitReport_latestRow.get(0).setBidUnitsCal("0.0"); 
		}
		if(upbitReport_latestRow.get(0).getAskUnitsCal()==null) {
			upbitReport_latestRow.get(0).setAskUnitsCal("0.0"); 
		}
		
		if(upbitReport_latestRow.get(0).getBidAvgPrice()==null) {
			upbitReport_latestRow.get(0).setBidAvgPrice("0.0"); 
		}
		if(upbitReport_latestRow.get(0).getAskAvgPrice()==null) {
			upbitReport_latestRow.get(0).setAskAvgPrice("0.0");
		}
		
		if(upbitReport_latestRow.get(0).getBidAccUnits()==null) {
			upbitReport_latestRow.get(0).setBidAccUnits("0.0");
		}

		if(upbitReport_latestRow.get(0).getTotalRateCal()==null) {
			upbitReport_latestRow.get(0).setTotalRateCal("0.0");
		}
		if(upbitReport_latestRow.get(0).getTotalRateCal()==null) {
			upbitReport_latestRow.get(0).setTotalRateCal("0.0");
		}
		if(upbitReport_latestRow.get(0).getCal()==null) {
			upbitReport_latestRow.get(0).setCal("0.0"); 
		}
		if(upbitReport_latestRow.get(0).getAskAccUnits()==null) {
			upbitReport_latestRow.get(0).setAskAccUnits("0.0");
		}

		return upbitReport_latestRow;
	}
}
