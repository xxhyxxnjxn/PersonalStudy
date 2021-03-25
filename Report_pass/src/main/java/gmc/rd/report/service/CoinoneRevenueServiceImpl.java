package gmc.rd.report.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.dto.ReportDtoMapper2;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ReportRepository3;

@Service
public class CoinoneRevenueServiceImpl implements CoinoneRevenueService{

	// 코인원
	@Autowired
	private ReportRepository3 reportRepository3;

	BigDecimal bid_price = new BigDecimal("0.0");
	BigDecimal bid_units = new BigDecimal("0.0");
	BigDecimal ask_price = new BigDecimal("0.0");
	BigDecimal ask_units = new BigDecimal("0.0");
	
	@Transactional
	@Override
	public void setAvgPrice_coinone(String sitename, List<Report3> coinoneReport, User user)
			throws ParseException { // 엑셀 계산시


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

		List<ReportDto3> reportDto = ReportDtoMapper3.INSTANCE.toDto(coinoneReport);
		
		List<ReportDto3> report = setCoinoneData(reportDto);
		
		for (int i = report.size() - 1; i >= 0; i--) {

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

			// Report report_entity = new Report();
			int x =  reportRepository3.updateReport(report.get(i).getOrderId(), report.get(i).getSite(), report.get(i).getTransactionDate(), report.get(i).getCurrency(), report.get(i).getType(), report.get(i).getPrice(), report.get(i).getUnits(), report.get(i).getBidUnitsCal(), 
					report.get(i).getAskUnitsCal(), report.get(i).getBidAvgPrice(), report.get(i).getAskAvgPrice(), report.get(i).getFee(), report.get(i).getTotalPrice(), report.get(i).getBidTotalPriceCal(), report.get(i).getAskTotalPriceCal(), report.get(i).getRevenue(), report.get(i).getAccUnits(), report.get(i).getCal(),
					report.get(i).getTotalRate(), report.get(i).getTotalRateCal(), report.get(i).getExpectIncome(), report.get(i).getIncomeCal(), report.get(i).getIncome(), user.getMemId(), report.get(i).getAskAccUnits(), report.get(i).getBidAccUnits());
			
//			String tempId = String.valueOf(report.get(i).getOrderId());
//			report_entity = reportRepository3.findByOrderId(tempId);
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
	public List<ReportDto3> setCoinoneData(List<ReportDto3> report3) {
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

	@Override
	@Transactional
	public void setAvgPrice_coinone_latest(String sitename, List<Report3> coinoneReport_isNullEntity, List<Report3> coinoneReport_latestRowEntity, User user) throws ParseException { 

		List<ReportDto3> coinoneReport_latestRow = ReportDtoMapper3.INSTANCE.toDto(coinoneReport_latestRowEntity);
		List<ReportDto3> coinoneReport_isNull = ReportDtoMapper3.INSTANCE.toDto(coinoneReport_isNullEntity);
		
		coinoneReport_latestRow = replaceNull(coinoneReport_latestRow);
		
		BigDecimal bid_totalPrice_cal= new BigDecimal(coinoneReport_latestRow.get(0).getBidTotalPriceCal()); 
		BigDecimal ask_totalPrice_cal = new BigDecimal(coinoneReport_latestRow.get(0).getAskTotalPriceCal());
		
		BigDecimal bid_units_cal= new BigDecimal(coinoneReport_latestRow.get(0).getBidUnitsCal()); 
		BigDecimal ask_units_cal = new BigDecimal(coinoneReport_latestRow.get(0).getAskUnitsCal()); 

		BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
		BigDecimal ask_totalPrice = new BigDecimal("0.0");

		BigDecimal bid_avgPrice = new BigDecimal(coinoneReport_latestRow.get(0).getBidAvgPrice()); 
		BigDecimal ask_avgPrice = new BigDecimal(coinoneReport_latestRow.get(0).getAskAvgPrice());

		BigDecimal bid_fee = new BigDecimal("0.0");
		BigDecimal ask_fee = new BigDecimal("0.0");
		
		BigDecimal bid_accUnits = new BigDecimal(coinoneReport_latestRow.get(0).getBidAccUnits());
		
		BigDecimal total_rate_cal= new BigDecimal(coinoneReport_latestRow.get(0).getTotalRateCal());
		BigDecimal cal = new BigDecimal(coinoneReport_latestRow.get(0).getCal()); 
		
		BigDecimal ask_accUnits = new BigDecimal(coinoneReport_latestRow.get(0).getAskAccUnits());
		BigDecimal askAccUnits= new BigDecimal("0.0");
		
		BigDecimal totalPriceCal= new BigDecimal("0.0");
		
		BigDecimal bidTotalPriceCal= new BigDecimal(coinoneReport_latestRow.get(0).getBidTotalPriceCal()); 
		BigDecimal bidUnitsCal= new BigDecimal("0.0");
		BigDecimal askTotalPriceCal= new BigDecimal("0.0");
		BigDecimal askUnitsCal= new BigDecimal("0.0");
		
		Report3 report_entity = null;
		List<ReportDto3> report = setCoinoneData_latest(coinoneReport_isNull, coinoneReport_latestRow);
		
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

				if(i==0 && coinoneReport_latestRow.get(0).getType().equals("매도")) {
					bid_totalPrice_cal= new BigDecimal("0.0");
					bid_units_cal= new BigDecimal("0.0"); 
				}else if(i==0 && coinoneReport_latestRow.get(0).getType().equals("매수")) {
					ask_accUnits = new BigDecimal("0.0"); // 더해주자마자 바로 초기화
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
					//bid_totalPrice = bid_totalPrice.add(bid_fee);
					
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
					
					if(i==0 && coinoneReport_latestRow.get(0).getType().equals("매수")) {
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

			int x =  reportRepository3.updateReport(report.get(i).getOrderId(), report.get(i).getSite(), report.get(i).getTransactionDate(), report.get(i).getCurrency(), report.get(i).getType(), report.get(i).getPrice(), report.get(i).getUnits(), report.get(i).getBidUnitsCal(), 
					report.get(i).getAskUnitsCal(), report.get(i).getBidAvgPrice(), report.get(i).getAskAvgPrice(), report.get(i).getFee(), report.get(i).getTotalPrice(), report.get(i).getBidTotalPriceCal(), report.get(i).getAskTotalPriceCal(), report.get(i).getRevenue(), report.get(i).getAccUnits(), report.get(i).getCal(),
					report.get(i).getTotalRate(), report.get(i).getTotalRateCal(), report.get(i).getExpectIncome(), report.get(i).getIncomeCal(), report.get(i).getIncome(), user.getMemId(), report.get(i).getAskAccUnits(), report.get(i).getBidAccUnits());
//			String tempId = String.valueOf(report.get(i).getOrderId());
//			report_entity = reportRepository3.findByOrderId(tempId);
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
	public List<ReportDto3> setCoinoneData_latest(List<ReportDto3> report,List<ReportDto3> coinoneReport_latestRow) {

		BigDecimal accUnits = new BigDecimal("0.0000");
		BigDecimal ask_units_cal = new BigDecimal("0.0000");
		BigDecimal bid_units_cal = new BigDecimal("0.0000");

		for (int i = 0; i <report.size(); i++) { // 과거순부터 계산

			if(i == 0 && coinoneReport_latestRow.get(0).getAccUnits()!="0.0000") {
				accUnits = new BigDecimal(coinoneReport_latestRow.get(0).getAccUnits());
				System.out.println("DD"+accUnits);
			}

			if (report.get(i).getType().equals("매도")) {
				//report.get(i).setType("매도");
				if (!bid_units_cal.toString().equals("0.0000")) {
					
					ask_units_cal = new BigDecimal(report.get(i).getUnits());
					accUnits = accUnits.subtract(ask_units_cal);
					
				} else {
					accUnits = new BigDecimal("0.0000");
				}
				
				if(Double.valueOf(accUnits.toString()) < 0.0000){
					accUnits = new BigDecimal("0.0000");
					
				}

			} else if (report.get(i).getType().equals("매수")) {
				//report.get(i).setType("매수");
			
				bid_units_cal = new BigDecimal(report.get(i).getUnits());
				accUnits = accUnits.add(bid_units_cal);
				
			} else if (report.get(i).getType().equals("입금")) {
				//report.get(i).setType("입금");
			
				bid_units_cal = new BigDecimal(report.get(i).getUnits());
				accUnits = accUnits.add(bid_units_cal);
				
				BigDecimal price = new BigDecimal(report.get(i).getPrice());
				String fee_str = report.get(i).getFee();
				
				if(Pattern.matches(".*[a-zA-Z]+.*", fee_str)) {
					BigDecimal fee = new BigDecimal(fee_str.replaceAll("[A-Z]", ""));
					
					fee = fee.multiply(price);
					report.get(i).setFee(fee.toPlainString());
				}
				
			} else if (report.get(i).getType().equals("출금")) {
				//report.get(i).setType("출금");
				
				if (!bid_units_cal.toString().equals("0.0000")) {
					
					ask_units_cal = new BigDecimal(report.get(i).getUnits());
					accUnits = accUnits.subtract(ask_units_cal);
					
				}else {
					accUnits = new BigDecimal("0.0000");
				}
				
				if(Double.valueOf(accUnits.toString()) < 0.0000){
					accUnits = new BigDecimal("0.0000");
				}
				
				BigDecimal price = new BigDecimal(report.get(i).getPrice());
				String fee_str = report.get(i).getFee();
				
				if(Pattern.matches(".*[a-zA-Z]+.*", fee_str)) {
					BigDecimal fee = new BigDecimal(fee_str.replaceAll("[A-Z]", ""));
					
					fee = fee.multiply(price);
					report.get(i).setFee(fee.toPlainString());
				}

			}

			report.get(i).setAccUnits(accUnits.toString());

		}

		return report;

	}

		@Transactional
	   @Override
	   public BigDecimal setRevenue3(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice)
	         throws ParseException { // 매도 한번 했을 때 수익률


	      //매도 평균 단가 - 매수 평균 단가 = A
	      //A/매수 평균단가
	      //여기서는 시세차이만 본다는 생각으로...
			BigDecimal zero = BigDecimal.ZERO;
			BigDecimal revenue = new BigDecimal("0.0");
			
			//if(!"0.00000000".equals(bid_avgPrice.toPlainString())||!bid_avgPrice.toPlainString().equals("0.0000")) {
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


			BigDecimal fee = new BigDecimal("0.002");
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
	public List<ReportDto3> replaceNull( List<ReportDto3> coinoneReport_latestRow){
		if(coinoneReport_latestRow.get(0).getBidTotalPriceCal()==null) {
			coinoneReport_latestRow.get(0).setBidTotalPriceCal("0.0");
		}
		if(coinoneReport_latestRow.get(0).getAskTotalPriceCal()==null) {
			coinoneReport_latestRow.get(0).setAskTotalPriceCal("0.0");
		}
		
		if(coinoneReport_latestRow.get(0).getBidUnitsCal()==null) {
			coinoneReport_latestRow.get(0).setBidUnitsCal("0.0"); 
		}
		if(coinoneReport_latestRow.get(0).getAskUnitsCal()==null) {
			coinoneReport_latestRow.get(0).setAskUnitsCal("0.0"); 
		}
		
		if(coinoneReport_latestRow.get(0).getBidAvgPrice()==null) {
			coinoneReport_latestRow.get(0).setBidAvgPrice("0.0"); 
		}
		if(coinoneReport_latestRow.get(0).getAskAvgPrice()==null) {
			coinoneReport_latestRow.get(0).setAskAvgPrice("0.0");
		}
		
		if(coinoneReport_latestRow.get(0).getBidAccUnits()==null) {
			coinoneReport_latestRow.get(0).setBidAccUnits("0.0");
		}

		if(coinoneReport_latestRow.get(0).getTotalRateCal()==null) {
			coinoneReport_latestRow.get(0).setTotalRateCal("0.0");
		}
		if(coinoneReport_latestRow.get(0).getTotalRateCal()==null) {
			coinoneReport_latestRow.get(0).setTotalRateCal("0.0");
		}
		if(coinoneReport_latestRow.get(0).getCal()==null) {
			coinoneReport_latestRow.get(0).setCal("0.0"); 
		}
		if(coinoneReport_latestRow.get(0).getAskAccUnits()==null) {
			coinoneReport_latestRow.get(0).setAskAccUnits("0.0");
		}

		return coinoneReport_latestRow;
	}
}
