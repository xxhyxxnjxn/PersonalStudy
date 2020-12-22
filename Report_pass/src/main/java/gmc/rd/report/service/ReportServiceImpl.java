package gmc.rd.report.service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.coinone.vo.CoinoneTransactionVo;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.api.upbit.vo.Trades;
import gmc.rd.report.api.upbit.vo.UpbitTransactionVo;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.dto.ReportDtoMapper;
import gmc.rd.report.dto.ReportDtoMapper2;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.UserRepository;

/**
 * 공통으로 계산 해야 하는 것 1. 평균 매도 매수 단가 = setData 2. 매도시 수익률 & 매도 누적 코인이 0 일 때 수익률 =
 * calData 각자 계산 1. 빗썸 1) 사이트 2) orderId 3) type 4) accUnits (남는 코인이 있으면 소수점
 * 자르기)
 * 
 * 2.업비트 1) 사이트 2) type 3) 누적 코인수
 * 
 * 
 * @author user
 *
 */
@Service
public class ReportServiceImpl implements ReportService {
   // 빗썸
   @Autowired
   private ReportRepository reportRepository;
   // 업비트
   @Autowired
   private ReportRepository2 reportRepository2;
   // 코인원
   @Autowired
   private ReportRepository3 reportRepository3;

   @Autowired
   private BithumbApiService bithumbApiService;

   @Autowired
   private CoinoneApiService coinoneApiService;

   @Autowired
   private UpbitApiService upbitApiService;

   BigDecimal bid_price = new BigDecimal("0.0");
   BigDecimal bid_units = new BigDecimal("0.0");
   BigDecimal ask_price = new BigDecimal("0.0");
   BigDecimal ask_units = new BigDecimal("0.0");

   @Autowired
   private ApiRepository apiRepository;
   @Autowired
   private UserRepository userRepository;
   @Transactional
   @Override
   public List<Report> selectReport(String sitename, List<VmDto> vmDto) throws Exception {

      Gson gson = new Gson();

      List<BithumbTransactionVo> bithumbTransaction = null;
      List<UpbitTransactionVo> upbitTransaction = null;
      List<UpbitTransactionVo> upbitTransaction2 = null;
      List<CoinoneTransactionVo> coinoneTransaction = null;

      List<ReportDto> reportDto = null;
      List<ReportDto2> reportDto2 = null;
      List<ReportDto3> reportDto3 = null;

      List<Report> report = null;
      List<Report2> report2 = null;
      List<Report3> report3 = null;

      String jsonStr = null;

      List<User> userList = userRepository.findAll();

      for (User user : userList) {
         System.out.println(userList);

         if (sitename.equals("bithumb")) {
            Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "bithumb");
            ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

            for (VmDto result : vmDto) {

               System.out.println("coin : " + result);
               bithumbTransaction = bithumbApiService.getUserTransaction(api.getApiKey(), api.getSecretKey(),
                     result.getVmId());

               if (bithumbTransaction != null) {

                  jsonStr = new ObjectMapper().writeValueAsString(bithumbTransaction);

                  Type listType = new TypeToken<List<ReportDto>>() {
                  }.getType();
                  reportDto = gson.fromJson(jsonStr, listType);

                  report = ReportDtoMapper.INSTANCE.toEntity(reportDto);
                  setAvgPrice_bithumb(sitename, report, user);

               }
            }

         } else if (sitename.equals("upbit")) {
            Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");
            System.out.println("아이디는" + user.getMemId());
            ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

            HashMap<String, String> hash = new HashMap<String, String>();
            hash.put("apiKey", api.getApiKey());
            hash.put("secretKey", api.getSecretKey());
            hash.put("state", "done");
            upbitTransaction = upbitApiService.getOrderList(hash);
            
            System.out.println("확인@");
            for (int i = 0; i < upbitTransaction.size(); i++) {
               System.out.println("UUID : " + upbitTransaction.get(i).getUuid());

               if (upbitTransaction.get(i).getPrice() == null) {
                  System.out.println("UUID : " + upbitTransaction.get(i).getUuid());

                  try {
                     hash.put("uuid", upbitTransaction.get(i).getUuid());
                     List<Trades> trades = upbitApiService.getOrderDetail2(hash);
                     System.out.println("trades에서 가져온 price : " + trades.get(0).getPrice());

                     upbitTransaction.get(i).setPrice(trades.get(0).getPrice());

                  } catch (Exception e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();

                  }
               }
            }

            hash.put("state", "cancel");
            upbitTransaction2 = upbitApiService.getOrderList(hash);
            for (int i = 0; i < upbitTransaction2.size(); i++) {
//               System.out.println("UUID : " + upbitTransaction2.get(i).getUuid());

               if (upbitTransaction2.get(i).getOrd_type().equals("limit")) {
                  //System.out.println("삭제할 UUID : " + upbitTransaction2.get(i).getUuid());
                  upbitTransaction2.remove(i);
                  //System.out.println("다음 UUID : " + upbitTransaction2.get(i).getUuid());
                  i--;
               }

            }
            
            System.out.println(upbitTransaction2.size());
            
            for (int i = 0; i < upbitTransaction2.size(); i++) {
               try {
                  hash.put("uuid", upbitTransaction2.get(i).getUuid());
                  System.out.println("개별 조회 UUID" + upbitTransaction2.get(i).getUuid());
                  List<Trades> trades = upbitApiService.getOrderDetail2(hash);
                  System.out.println("trades에서 가져온 price2 : " + trades.get(0).getPrice());

                  upbitTransaction2.get(i).setPrice(trades.get(0).getPrice());

               } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();

               }

            }
            upbitTransaction.addAll(upbitTransaction2);

            jsonStr = new ObjectMapper().writeValueAsString(upbitTransaction);
            Type listType = new TypeToken<List<ReportDto2>>() {
            }.getType();
            reportDto2 = gson.fromJson(jsonStr, listType);

            report2 = ReportDtoMapper2.INSTANCE.toEntity(reportDto2);

            setAvgPrice_upbit(sitename, report2, user);

            ////////////////////////////////////////////////

            /*
             * jsonStr = new ObjectMapper().writeValueAsString(upbitTransaction); listType =
             * new TypeToken<List<ReportDto2>>() {}.getType(); reportDto2 =
             * gson.fromJson(jsonStr, listType);
             * 
             * 
             * report2 = ReportDtoMapper2.INSTANCE.toEntity(reportDto2);
             * setAvgPrice_upbit(sitename, report2,user);
             */

         } else if (sitename.equals("coinone")) {
            Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
            ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);

            // for (VmDto result : vmDto) {

            // System.out.println("coin : " + result);
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("apiKey", api.getApiKey());
            hashMap.put("secretKey", api.getSecretKey());
            hashMap.put("currency", "EOS");

            coinoneTransaction = coinoneApiService.MyCompletedOrders2(hashMap);

            if (coinoneTransaction != null) {
               jsonStr = new ObjectMapper().writeValueAsString(coinoneTransaction);
               Type listType = new TypeToken<List<ReportDto3>>() {
               }.getType();
               reportDto3 = gson.fromJson(jsonStr, listType);

               report3 = ReportDtoMapper3.INSTANCE.toEntity(reportDto3);

               setAvgPrice_coinone(sitename, report3, "EOS", user);
            }

         }

      }
      return null;
   }

   
   @Override
   @Transactional
   public void setAvgPrice_bithumb(String sitename, List<Report> report, User user) throws ParseException { // 평균 단가
                                                                                    // 구하기

      /**
       * 매수 : price * units = amount 매도 : price * units = amount
       * 
       * 매수 평균 단가 : amount + ... / 매수 총 수량 매도 평균 단가 : amount + ... / 매도 총 수량
       * 
       * 
       * /** 계산법 가격 * 수량 = 단가 (totalPrice) 단가를 다 더한다 = totalPrice_cal 수량을 다 더한다 =
       * units_cal
       * 
       * totalPrice_cal/units_cal = 평균 매도 매수가 (avgPrice) 매도 매수 avgPrice * 누적 수량 = 매도가
       * 0일 때 수익률 계산하는 과정
       */

      BigDecimal bid_totalPrice_cal = new BigDecimal("0.0"); // 누적 totalPrice -> 평균 단가 구하기 위한 변수 (매도 할때마다 수익률 구하기위한)
      BigDecimal ask_totalPrice_cal = new BigDecimal("0.0");

      BigDecimal bid_units_cal = new BigDecimal("0.0"); // 누적 units -> 평균 단가 구하기 위한 변수 (매도 할 때마다 수익률 구하기 위한)
      BigDecimal ask_units_cal = new BigDecimal("0.0");

      BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
      BigDecimal ask_totalPrice = new BigDecimal("0.0");

      BigDecimal bid_avgPrice = new BigDecimal("0.0"); // 수익률 구하기 위한 평균 매수 매도가 ->
      BigDecimal ask_avgPrice = new BigDecimal("0.0");

      Report report_entity= null;
      for (int i = report.size() - 1; i >= 0; i--) {
//         BigDecimal bid_price = new BigDecimal("0.0") ;
//         BigDecimal bid_units = new BigDecimal("0.0") ;
//         BigDecimal ask_price = new BigDecimal("0.0") ;
//         BigDecimal ask_units = new BigDecimal("0.0") ;
         report.get(i).setUser(user);

         List<Report> report2 =  setBithumbData(report);

         if (report.get(i).getType().equals("매수")) {
            bid_price = new BigDecimal(report.get(i).getPrice());
            bid_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("매수 부분 price : " + bid_price); // 매수 시작 부분
            System.out.println("매수 부분 units : " + bid_units); // 매수 시작 부분

            bid_totalPrice = bid_price.multiply(bid_units);
            report.get(i).setTotalPrice(bid_totalPrice.toString()); // totalPrice 저장
            // System.out.println(bid_amount);

            bid_totalPrice_cal = bid_totalPrice_cal.add(bid_totalPrice);
            bid_units_cal = bid_units_cal.add(bid_units);

            bid_avgPrice = bid_totalPrice_cal.divide(bid_units_cal,4, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(bid_avgPrice.toString()); // 평균 단가 저장

         } else if (report.get(i).getType().equals("매도") && !report.get(i).getAccUnits().equals("0.0000")) {

            if (report.get(i).getPrice() == null) {
               report.get(i).setPrice("0");
            }

            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("매도 부분 price : " + ask_price); // 매도 부분
            System.out.println("매도 부분 units : " + ask_units); // 매도 부분

            ask_totalPrice = ask_price.multiply(ask_units);
            report.get(i).setTotalPrice(ask_totalPrice.toString()); // totalPrice 저장
            // System.out.println(ask_amount);

            /////////////////////////////////////////////////////////////////////
            // 매도 할 때마다 수익률을 구하기 위한 메서드
            // 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
            if (bid_totalPrice_cal.toString().equals("0.0")) {
               System.out.println("매수 기록이 없습니다.");
            } else {
               BigDecimal revenue_cal = setRevenue(sitename, bid_avgPrice, ask_price, ask_units);
               report.get(i).setRevenue(revenue_cal.toString());
               System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
            }
            //////////////////////////////////////////////////////////////////////

            ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
            ask_units_cal = ask_units_cal.add(ask_units);

            ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal,4, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(ask_avgPrice.toString()); // 평균 단가 저장

         }

         if (report.get(i).getType().equals("매도") && report.get(i).getAccUnits().equals("0.0000")) {
            if (report.get(i).getPrice() == null) {
               report.get(i).setPrice("0");

            }

            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  price : " + ask_price); // 마지막 매도 부분
            System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  units : " + ask_units); // 마지막 매도 부분

            ask_totalPrice = ask_price.multiply(ask_units);
            report.get(i).setTotalPrice(ask_totalPrice.toString()); // totalPrice 저장

/////////////////////////////////////////////////////////////////////
            // 매도 할 때마다 수익률을 구하기 위한 메서드
            // 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
            if (bid_totalPrice_cal.toString().equals("0.0")) {
               System.out.println("매수 기록이 없습니다.");
            } else {
               BigDecimal revenue_cal = setRevenue(sitename, bid_avgPrice, ask_price, ask_units);
               report.get(i).setRevenue(revenue_cal.toString());
               System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
            }
//////////////////////////////////////////////////////////////////////

            // System.out.println(ask_amount);
            ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
            ask_units_cal = ask_units_cal.add(ask_units);

            ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal,4, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(ask_avgPrice.toString()); // 평균 단가 저장

//////////////////////////////////////////////매도 누적 코인 수량이 0 일 때 수익률 구하기
            if (bid_avgPrice.toString().equals("0.0")
                  && !bid_units_cal.toString().equals(ask_units_cal.toString()) && !bid_units_cal.toString().equals(ask_units_cal.toString())) {
               System.out.println("매수 기록이 없습니다.");

            } else {

               BigDecimal clearRevenue = setClearRevenue(sitename, bid_avgPrice, ask_avgPrice, bid_units_cal,
                     ask_units_cal);
               report.get(i).setClearRevenue(clearRevenue.toString());
            }

            bid_totalPrice_cal = new BigDecimal("0.0");
               ask_totalPrice_cal = new BigDecimal("0.0");
               
               bid_units_cal = new BigDecimal("0.0");
               ask_units_cal = new BigDecimal("0.0");
               
               bid_totalPrice = new BigDecimal("0.0");
               ask_totalPrice = new BigDecimal("0.0");
               
               bid_avgPrice = new BigDecimal("0.0");
               ask_avgPrice = new BigDecimal("0.0");

         }

         bid_price = new BigDecimal("0.0");
         bid_units = new BigDecimal("0.0");
         ask_price = new BigDecimal("0.0");
         ask_units = new BigDecimal("0.0");

         
 	     	//Report report_entity  = new Report();
 	     	
          	String tempId = String.valueOf(report2.get(i).getOrderId());
          	report_entity = reportRepository.findByOrderId(tempId);

          	if(report_entity!=null){
          		
          		report_entity.setOrderId(report2.get(i).getOrderId());
    			report_entity.setAccUnits(report2.get(i).getAccUnits());
    			report_entity.setAvgPrice(report2.get(i).getAvgPrice());
    			report_entity.setClearRevenue(report2.get(i).getClearRevenue());
    			report_entity.setCurrency(report2.get(i).getCurrency());
    			report_entity.setFee(report2.get(i).getFee());
    			report_entity.setIdx(0);
    			report_entity.setPrice(report2.get(i).getPrice());
    			report_entity.setRevenue(report2.get(i).getRevenue());
    			report_entity.setSite(report2.get(i).getSite());
    			report_entity.setTotalPrice(report2.get(i).getTotalPrice());
    			report_entity.setTransactionDate(report2.get(i).getTransactionDate());
    			report_entity.setType(report2.get(i).getType());
    			report_entity.setUnits(report2.get(i).getUnits());
    			report_entity.setUser(user);
    			
          	}
 	     


      }
      System.out.println(report);

      if(report_entity==null) {
    		reportRepository.saveAll(report);
    	}


   }
   @Transactional
   @Override
   public void setAvgPrice_upbit(String sitename, List<Report2> report2, User user) throws ParseException {
      /**
       * 매수 : price * units = amount 매도 : price * units = amount
       * 
       * 매수 평균 단가 : amount + ... / 매수 총 수량 매도 평균 단가 : amount + ... / 매도 총 수량
       * 
       * 
       * /** 계산법 가격 * 수량 = 단가 (totalPrice) 단가를 다 더한다 = totalPrice_cal 수량을 다 더한다 =
       * units_cal
       * 
       * totalPrice_cal/units_cal = 평균 매도 매수가 (avgPrice) 매도 매수 avgPrice * 누적 수량 = 매도가
       * 0일 때 수익률 계산하는 과정
       */
      System.out.println("시작한다 계산");

      BigDecimal bid_totalPrice_cal = new BigDecimal("0.0"); // 누적 totalPrice -> 평균 단가 구하기 위한 변수 (매도 할때마다 수익률 구하기위한)
      BigDecimal ask_totalPrice_cal = new BigDecimal("0.0");

      BigDecimal bid_units_cal = new BigDecimal("0.0"); // 누적 units -> 평균 단가 구하기 위한 변수 (매도 할 때마다 수익률 구하기 위한)
      BigDecimal ask_units_cal = new BigDecimal("0.0");

      BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
      BigDecimal ask_totalPrice = new BigDecimal("0.0");

      BigDecimal bid_avgPrice = new BigDecimal("0.0"); // 수익률 구하기 위한 평균 매수 매도가 ->
      BigDecimal ask_avgPrice = new BigDecimal("0.0");

      List<Report2> report = null;
      Report2 report_entity = null;

//         BigDecimal bid_price = new BigDecimal("0.0") ;
//         BigDecimal bid_units = new BigDecimal("0.0") ;
//         BigDecimal ask_price = new BigDecimal("0.0") ;
//         BigDecimal ask_units = new BigDecimal("0.0") ;
      // report.get(i).setUser(user);

      report = setUpbitData(report2, user);

      for (int i = 0; i < report.size() - 1; i++) {
         /////////////////////// 수정중
         if (report.get(i).getType().equals("매수")) {
            bid_price = new BigDecimal(report.get(i).getPrice());
            bid_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("매수 부분 price : " + bid_price); // 매수 시작 부분
            System.out.println("매수 부분 units : " + bid_units); // 매수 시작 부분

            bid_totalPrice = bid_price.multiply(bid_units);
            report.get(i).setTotalPrice(bid_totalPrice.toString()); // totalPrice 저장
            // System.out.println(bid_amount);

            bid_totalPrice_cal = bid_totalPrice_cal.add(bid_totalPrice);
            bid_units_cal = bid_units_cal.add(bid_units);

            bid_avgPrice = bid_totalPrice_cal.divide(bid_units_cal,8, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(bid_avgPrice.toString()); // 평균 단가 저장

         } else if (report.get(i).getType().equals("매도") && !report.get(i).getAccUnits().equals("0.0000")) {

            if (report.get(i).getPrice() == null) {
               report.get(i).setPrice("0");
            }

            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("매도 부분 price : " + ask_price); // 매도 부분
            System.out.println("매도 부분 units : " + ask_units); // 매도 부분

            ask_totalPrice = ask_price.multiply(ask_units);
            report.get(i).setTotalPrice(ask_totalPrice.toString()); // totalPrice 저장
            // System.out.println(ask_amount);

            /////////////////////////////////////////////////////////////////////
            // 매도 할 때마다 수익률을 구하기 위한 메서드
            // 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
            if (bid_totalPrice_cal.toString().equals("0.0")) {
               System.out.println("매수 기록이 없습니다.");
            } else {
               BigDecimal revenue_cal = setRevenue(sitename, bid_avgPrice, ask_price, ask_units);
               report.get(i).setRevenue(revenue_cal.toString());
               System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
            }
            //////////////////////////////////////////////////////////////////////

            ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
            ask_units_cal = ask_units_cal.add(ask_units);

            ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal,8, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(ask_avgPrice.toString()); // 평균 단가 저장

         }

         if (report.get(i).getType().equals("매도") && report.get(i).getAccUnits().equals("0.0000")) {
            if (report.get(i).getPrice() == null) {
               report.get(i).setPrice("0");

            }

            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  price : " + ask_price); // 마지막 매도 부분
            System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  units : " + ask_units); // 마지막 매도 부분

            ask_totalPrice = ask_price.multiply(ask_units);
            report.get(i).setTotalPrice(ask_totalPrice.toString()); // totalPrice 저장

/////////////////////////////////////////////////////////////////////
            // 매도 할 때마다 수익률을 구하기 위한 메서드
            // 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
            if (bid_totalPrice_cal.toString().equals("0.0")) {
               System.out.println("매수 기록이 없습니다.");
            } else {
               BigDecimal revenue_cal = setRevenue(sitename, bid_avgPrice, ask_price, ask_units);
               report.get(i).setRevenue(revenue_cal.toString());
               System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
            }
//////////////////////////////////////////////////////////////////////


            	// System.out.println(ask_amount);
                ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
                ask_units_cal = ask_units_cal.add(ask_units);

                ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal,8, BigDecimal.ROUND_DOWN);

                report.get(i).setAvgPrice(ask_avgPrice.toString()); // 평균 단가 저장

            

//////////////////////////////////////////////매도 누적 코인 수량이 0 일 때 수익률 구하기
            if (bid_avgPrice.toString().equals("0.0") && !bid_units_cal.toString().equals(ask_units_cal.toString())) {
               System.out.println("매수 기록이 없습니다.");

            } else {

               BigDecimal clearRevenue = setClearRevenue(sitename, bid_avgPrice, ask_avgPrice, bid_units_cal,
                     ask_units_cal);
               report.get(i).setClearRevenue(clearRevenue.toString());
            }

            bid_totalPrice_cal = new BigDecimal("0.0");
               ask_totalPrice_cal = new BigDecimal("0.0");
               
               bid_units_cal = new BigDecimal("0.0");
               ask_units_cal = new BigDecimal("0.0");
               
               bid_totalPrice = new BigDecimal("0.0");
               ask_totalPrice = new BigDecimal("0.0");
               
               bid_avgPrice = new BigDecimal("0.0");
               ask_avgPrice = new BigDecimal("0.0");

         }

         bid_price = new BigDecimal("0.0");
         bid_units = new BigDecimal("0.0");
         ask_price = new BigDecimal("0.0");
         ask_units = new BigDecimal("0.0");
         
       //Report report_entity  = new Report();
	     	
       	String tempId = String.valueOf(report.get(i).getOrderId());
       	report_entity = reportRepository2.findByOrderId(tempId);

       	if(report_entity!=null){
       		
       		report_entity.setOrderId(report.get(i).getOrderId());
 			report_entity.setAccUnits(report.get(i).getAccUnits());
 			report_entity.setAvgPrice(report.get(i).getAvgPrice());
 			report_entity.setClearRevenue(report.get(i).getClearRevenue());
 			report_entity.setCurrency(report.get(i).getCurrency());
 			report_entity.setFee(report.get(i).getFee());
 			report_entity.setIdx(0);
 			report_entity.setPrice(report.get(i).getPrice());
 			report_entity.setRevenue(report.get(i).getRevenue());
 			report_entity.setSite(report.get(i).getSite());
 			report_entity.setTotalPrice(report.get(i).getTotalPrice());
 			report_entity.setTransactionDate(report.get(i).getTransactionDate());
 			report_entity.setType(report.get(i).getType());
 			report_entity.setUnits(report.get(i).getUnits());
 			report_entity.setUser(user);
 			
       	}
       	

      }
      System.out.println(report);

      
      System.out.println(report);

      if(report_entity==null) {
    	  reportRepository2.saveAll(report);
    	}
      

   }
   @Transactional
   @Override
   public void setAvgPrice_coinone(String sitename, List<Report3> report, String currency, User user)
         throws ParseException {
      /**
       * 매수 : price * units = amount 매도 : price * units = amount
       * 
       * 매수 평균 단가 : amount + ... / 매수 총 수량 매도 평균 단가 : amount + ... / 매도 총 수량
       * 
       * 
       * /** 계산법 가격 * 수량 = 단가 (totalPrice) 단가를 다 더한다 = totalPrice_cal 수량을 다 더한다 =
       * units_cal
       * 
       * totalPrice_cal/units_cal = 평균 매도 매수가 (avgPrice) 매도 매수 avgPrice * 누적 수량 = 매도가
       * 0일 때 수익률 계산하는 과정
       */

      BigDecimal bid_totalPrice_cal = new BigDecimal("0.0"); // 누적 totalPrice -> 평균 단가 구하기 위한 변수 (매도 할때마다 수익률 구하기위한)
      BigDecimal ask_totalPrice_cal = new BigDecimal("0.0");

      BigDecimal bid_units_cal = new BigDecimal("0.0"); // 누적 units -> 평균 단가 구하기 위한 변수 (매도 할 때마다 수익률 구하기 위한)
      BigDecimal ask_units_cal = new BigDecimal("0.0");

      BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
      BigDecimal ask_totalPrice = new BigDecimal("0.0");

      BigDecimal bid_avgPrice = new BigDecimal("0.0"); // 수익률 구하기 위한 평균 매수 매도가 ->
      BigDecimal ask_avgPrice = new BigDecimal("0.0");
      
      Report3 report_entity=null;
      

      for (int i = report.size() - 1; i >= 0; i--) {
//         BigDecimal bid_price = new BigDecimal("0.0") ;
//         BigDecimal bid_units = new BigDecimal("0.0") ;
//         BigDecimal ask_price = new BigDecimal("0.0") ;
//         BigDecimal ask_units = new BigDecimal("0.0") ;
         report.get(i).setUser(user);

        List<Report3> report2 =  setCoinoneData(report, currency);

         if (report.get(i).getType().equals("매수")) {
            bid_price = new BigDecimal(report.get(i).getPrice());
            bid_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("매수 부분 price : " + bid_price); // 매수 시작 부분
            System.out.println("매수 부분 units : " + bid_units); // 매수 시작 부분

            bid_totalPrice = bid_price.multiply(bid_units);
            report.get(i).setTotalPrice(bid_totalPrice.toString()); // totalPrice 저장
            // System.out.println(bid_amount);

            bid_totalPrice_cal = bid_totalPrice_cal.add(bid_totalPrice);
            bid_units_cal = bid_units_cal.add(bid_units);

            bid_avgPrice = bid_totalPrice_cal.divide(bid_units_cal,4, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(bid_avgPrice.toString()); // 평균 단가 저장

         } else if (report.get(i).getType().equals("매도") && !report.get(i).getAccUnits().equals("0.0000")) {

            if (report.get(i).getPrice() == null) {
               report.get(i).setPrice("0");
            }

            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("매도 부분 price : " + ask_price); // 매도 부분
            System.out.println("매도 부분 units : " + ask_units); // 매도 부분

            ask_totalPrice = ask_price.multiply(ask_units);
            report.get(i).setTotalPrice(ask_totalPrice.toString()); // totalPrice 저장
            // System.out.println(ask_amount);

            /////////////////////////////////////////////////////////////////////
            // 매도 할 때마다 수익률을 구하기 위한 메서드
            // 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
            if (bid_totalPrice_cal.toString().equals("0.0")) {
               System.out.println("매수 기록이 없습니다.");
            } else {
               BigDecimal revenue_cal = setRevenue(sitename, bid_avgPrice, ask_price, ask_units);
               report.get(i).setRevenue(revenue_cal.toString());
               System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
            }
            //////////////////////////////////////////////////////////////////////

            ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
            ask_units_cal = ask_units_cal.add(ask_units);

            ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal,4, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(ask_avgPrice.toString()); // 평균 단가 저장

         }

         if (report.get(i).getType().equals("매도") && report.get(i).getAccUnits().equals("0.0000")) {
            if (report.get(i).getPrice() == null) {
               report.get(i).setPrice("0");

            }

            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

            System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  price : " + ask_price); // 마지막 매도 부분
            System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  units : " + ask_units); // 마지막 매도 부분

            ask_totalPrice = ask_price.multiply(ask_units);
            report.get(i).setTotalPrice(ask_totalPrice.toString()); // totalPrice 저장

/////////////////////////////////////////////////////////////////////
            // 매도 할 때마다 수익률을 구하기 위한 메서드
            // 필요한 부분 : 매수 평균단가, 매도 단가, 매도 수량
            if (bid_totalPrice_cal.toString().equals("0.0")) {
               System.out.println("매수 기록이 없습니다.");
            } else {
               BigDecimal revenue_cal = setRevenue(sitename, bid_avgPrice, ask_price, ask_units);
               report.get(i).setRevenue(revenue_cal.toString());
               System.out.println("매도 할 떄마다 나오는 수익률 : " + report.get(i).getRevenue());
            }
//////////////////////////////////////////////////////////////////////

            // System.out.println(ask_amount);
            ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
            ask_units_cal = ask_units_cal.add(ask_units);

            ask_avgPrice = ask_totalPrice_cal.divide(ask_units_cal,4, BigDecimal.ROUND_DOWN);

            report.get(i).setAvgPrice(ask_avgPrice.toString()); // 평균 단가 저장

//////////////////////////////////////////////매도 누적 코인 수량이 0 일 때 수익률 구하기
            if (bid_avgPrice.toString().equals("0.0") && !bid_units_cal.toString().equals(ask_units_cal.toString())) {
               System.out.println("매수 기록이 없습니다.");

            } else {

               BigDecimal clearRevenue = setClearRevenue(sitename, bid_avgPrice, ask_avgPrice, bid_units_cal,
                     ask_units_cal);
               report.get(i).setClearRevenue(clearRevenue.toString());
            }

            bid_totalPrice_cal = new BigDecimal("0.0");
               ask_totalPrice_cal = new BigDecimal("0.0");
               
               bid_units_cal = new BigDecimal("0.0");
               ask_units_cal = new BigDecimal("0.0");
               
               bid_totalPrice = new BigDecimal("0.0");
               ask_totalPrice = new BigDecimal("0.0");
               
               bid_avgPrice = new BigDecimal("0.0");
               ask_avgPrice = new BigDecimal("0.0");

         }

         bid_price = new BigDecimal("0.0");
         bid_units = new BigDecimal("0.0");
         ask_price = new BigDecimal("0.0");
         ask_units = new BigDecimal("0.0");
         
	     	//Report report_entity  = new Report();
	     	
       	String tempId = String.valueOf(report2.get(i).getOrderId());
       	report_entity = reportRepository3.findByOrderId(tempId);

       	if(report_entity!=null){
       		
       		report_entity.setOrderId(report2.get(i).getOrderId());
 			report_entity.setAccUnits(report2.get(i).getAccUnits());
 			report_entity.setAvgPrice(report2.get(i).getAvgPrice());
 			report_entity.setClearRevenue(report2.get(i).getClearRevenue());
 			report_entity.setCurrency(report2.get(i).getCurrency());
 			report_entity.setFee(report2.get(i).getFee());
 			report_entity.setIdx(0);
 			report_entity.setPrice(report2.get(i).getPrice());
 			report_entity.setRevenue(report2.get(i).getRevenue());
 			report_entity.setSite(report2.get(i).getSite());
 			report_entity.setTotalPrice(report2.get(i).getTotalPrice());
 			report_entity.setTransactionDate(report2.get(i).getTransactionDate());
 			report_entity.setType(report2.get(i).getType());
 			report_entity.setUnits(report2.get(i).getUnits());
 			report_entity.setUser(user);
 			
       	}


      }
      System.out.println(report);

      if(report_entity==null) {
    	  reportRepository3.saveAll(report);
    	}
     
   }
   @Transactional
   @Override
   public List<Report> setBithumbData(List<Report> report) {

      for (int i = report.size() - 1; i >= 0; i--) {

         // OrderId설정 = timestamp+currency+units+type -> 나중에 거래 내역 다시 불러와서 저장할 때 중복저장 안되게
         // 함
         String oderId = report.get(i).getTransactionDate() + report.get(i).getCurrency() + report.get(i).getType();
         report.get(i).setOrderId(oderId);

         report.get(i).setSite("bithumb"); // 사이트 설정

         if (report.get(i).getType().equals("1")) { // type 설정
            report.get(i).setType("매수");
            // calUnits = calUnits.add(transUnits);

         } else if (report.get(i).getType().equals("2")) {
            report.get(i).setType("매도");
            // calUnits = calUnits.subtract(transUnits);

         } else if (report.get(i).getType().equals("3")) {
            report.get(i).setType("출금 중");

         } else if (report.get(i).getType().equals("4")) {
            report.get(i).setType("입금");

            if (report.get(i).getUnits().contains("+")) {
               String str = report.get(i).getUnits();
               str = str.replace("+ ", "");
               report.get(i).setUnits(str);
            }

            bid_price = new BigDecimal(report.get(i).getPrice());
            bid_units = new BigDecimal(report.get(i).getUnits());

         } else if (report.get(i).getType().equals("5")) {
            report.get(i).setType("출금");
            ask_price = new BigDecimal(report.get(i).getPrice());
            ask_units = new BigDecimal(report.get(i).getUnits());

         } else if (report.get(i).getType().equals("9")) {
            report.get(i).setType("KRW 입금 중");

         }

         // 누적 코인 수량 소수점 자르기
         BigDecimal cal_accUnits = new BigDecimal(report.get(i).getAccUnits());
         cal_accUnits = cal_accUnits.setScale(4, BigDecimal.ROUND_DOWN);
         report.get(i).setAccUnits(cal_accUnits.toString());
      }
      return report;

   }
   @Transactional
   @Override
   public List<Report2> setUpbitData(List<Report2> report2, User user) throws ParseException {

      BigDecimal totalacc = new BigDecimal("0.0000");
      BigDecimal askacc = new BigDecimal("0.0000");
      BigDecimal bidacc = new BigDecimal("0.0000");

      String accUnits = null;

      Report2 report_entity = null;
      
      for (int i = report2.size() - 1; i >= 0; i--) {
         report2.get(i).setUser(user);
         report2.get(i).setSite("upbit"); // 사이트 설정

         String currency = report2.get(i).getCurrency();
         currency = currency.replace("KRW-", "");
         report2.get(i).setCurrency(currency); // 코인 수정 KRW-BTC -> BTC
         
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            
            Date date = format.parse(report2.get(i).getTransactionDate());
            long timestamp = date.getTime();
            report2.get(i).setTransactionDate(String.valueOf(timestamp)); // 업비트에서 제공해주는 시간을 unix시간으로 바꾸는 작업
            

         if (report2.get(i).getType().equals("ask")) {

            report2.get(i).setType("매도");

         } else if (report2.get(i).getType().equals("bid")) {

            report2.get(i).setType("매수");
         }
//         
//         if(report2.get(i).getPrice() == null) {
//            report2.get(i).getOrderId();
//            HashMap<String, String> hash = new HashMap<String, String>();
//            hash.put("uuid", report2.get(i).getOrderId());
//            hash.put("apiKey", "KVh4nHexBIaJqB3sTMrjbhGxOOVdU2TSGdqJcmYT");
//            hash.put("secretKey", "GbzCXDojxGqoi2k73KG37ieLqwThXKXFDQMndsY8");
//            
//            System.out.println("usergood : "+report2.get(i).getUser());
//            report2.get(i).getUser();
//            /////////////////////// 0이면 딜리트 
//            try {
//               
//               List<Trades> trades= upbitApiService.getOrderDetail2(hash);
//               System.out.println("trades에서 가져온 price : "+trades.get(0).getPrice());
//               System.out.println(trades.size());
//                  
//                  report2.get(i).setPrice(trades.get(0).getPrice());
//
//               
//            } catch (Exception e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//
//            }
//         }
         
         String tempId = String.valueOf(report2.get(i).getOrderId());
         report_entity = reportRepository2.findByOrderId(tempId);
         
         if(report_entity !=null) {
        	 report_entity.setUser(user);
        	 report_entity.setSite(report2.get(i).getSite());
        	 report_entity.setCurrency(report2.get(i).getCurrency());
        	 report_entity.setTransactionDate(report2.get(i).getTransactionDate());
        	 report_entity.setType(report2.get(i).getType());
         }

      }

      if(report_entity == null) {
    	  reportRepository2.saveAll(report2);
      }
      
      List<Report2> abc = reportRepository2.findByabc();

      // double totalacc = 0;
      for (int i = 0; i < abc.size() - 1; i++) {
         if (abc.get(i).getType().equals("매수")) {
            if (abc.get(i).getCurrency().equals(abc.get(i + 1).getCurrency())) {
               // String askacc = abc.get(i).getUnits();
               askacc = new BigDecimal(abc.get(i).getUnits());
               // double askaccD = Double.valueOf(askacc);
               totalacc = totalacc.add(askacc);

               abc.get(i).setAccUnits(totalacc.toString());
//               abc.get(i).setIdx(i);

            } else {

               totalacc = new BigDecimal("0.0000");
            }

            report2.get(i).setAccUnits(totalacc.toString());

         } else if (abc.get(i).getType().equals("매도")) {
            
            if (abc.get(i).getCurrency().equals(abc.get(i + 1).getCurrency())) {
               
            }
            
            // String bidacc = abc.get(i).getUnits();
            bidacc = new BigDecimal(abc.get(i).getUnits());
            // double bidaccD = Double.valueOf(bidacc);
            totalacc = totalacc.subtract(bidacc);

            if (Double.valueOf(totalacc.toString()) < 0.0001) {

               totalacc = new BigDecimal("0.0000");

               report2.get(i).setAccUnits(totalacc.toString());

               abc.get(i).setAccUnits(totalacc.toString());
//               abc.get(i).setIdx(i);
            }
            report2.get(i).setAccUnits(totalacc.toString());

            abc.get(i).setAccUnits(totalacc.toString());
//            abc.get(i).setIdx(i);
         }

//         String tempId = String.valueOf(report2.get(i).getOrderId());
//         Report2 report_entity = reportRepository2.findByOrderId(tempId);
         accUnits = report2.get(i).getAccUnits();

//         report_entity.setAccUnits(report2.get(i).getAccUnits());

//         reportRepository.saveAll(abc);
      }
      return abc;
   }
   @Transactional
   @Override
   public List<Report3> setCoinoneData(List<Report3> report3, String currency) {
      BigDecimal accUnits = new BigDecimal("0.0000");
      BigDecimal ask_units_cal = new BigDecimal("0.0000");
      BigDecimal bid_units_cal = new BigDecimal("0.0000");

      for (int i = report3.size() - 1; i >= 0; i--) { // 과거순부터 계산
         report3.get(i).setSite("coinone"); // 사이트 설정

         report3.get(i).setCurrency(currency); // 코인 설정

         if (report3.get(i).getType().equals("ask")) { // type 설정

            report3.get(i).setType("매도");

         } else if (report3.get(i).getType().equals("bid")) {

            report3.get(i).setType("매수");
         }

         // 누적코인 설정
         System.out.println("type : " + report3.get(i).getType());

         if (report3.get(i).getType().equals("매도")) {

            if (!bid_units_cal.toString().equals("0.0000")) {
               System.out.println("매도 units : " + report3.get(i).getUnits());
               ask_units_cal = new BigDecimal(report3.get(i).getUnits());
               accUnits = accUnits.subtract(ask_units_cal);
            } else {
               accUnits = new BigDecimal("0.0000");
            }

         } else if (report3.get(i).getType().equals("매수")) {
            System.out.println("매수 units : " + report3.get(i).getUnits());
            bid_units_cal = new BigDecimal(report3.get(i).getUnits());
            accUnits = accUnits.add(bid_units_cal);
         }

         System.out.println("accUnits : " + accUnits);
         report3.get(i).setAccUnits(accUnits.toString());

         System.out.println("fee : " + report3.get(i).getFee());

      }
      
      return report3;
   }
   @Transactional
   @Override
   public BigDecimal setRevenue(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_price, BigDecimal ask_units)
         throws ParseException {

      BigDecimal fee = new BigDecimal("0.0");
         BigDecimal cal = new BigDecimal("2");
         
         if(sitename.equals("bithumb")) {
            fee = new BigDecimal("0.0025");
            fee = fee.multiply(cal);
            
         }else if(sitename.equals("coinone")) {
            fee = new BigDecimal("0.002");
            fee = fee.multiply(cal);
            
         }else if(sitename.equals("upbit")) {
            fee = new BigDecimal("0.0005");
            fee = fee.multiply(cal);
         }
         
         BigDecimal revenue = ask_price.subtract(bid_avgPrice);
         revenue = revenue.subtract(cal);
         revenue = revenue.divide(bid_avgPrice,4, BigDecimal.ROUND_DOWN);
      
         return revenue;
         
//         BigDecimal bid_amount = bid_avgPrice.multiply(ask_units).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//         bid_amount = bid_amount.multiply(fee).setScale(2, BigDecimal.ROUND_DOWN);
//         BigDecimal bid = bid_amount.add(fee).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//         
//         
//         BigDecimal ask_amount = ask_price.multiply(ask_units).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//         ask_amount = ask_amount.multiply(fee).setScale(2, BigDecimal.ROUND_DOWN);
//         BigDecimal ask = ask_amount.subtract(fee).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//         
//         BigDecimal revenue = ask.subtract(bid);
//         revenue = revenue.divide(bid,4, BigDecimal.ROUND_DOWN);
   }
   @Transactional
   @Override
   public BigDecimal setClearRevenue(String sitename, BigDecimal bid_avgPrice, BigDecimal ask_avgPrice,
         BigDecimal bid_units_cal, BigDecimal ask_units_cal) throws ParseException { // 매도 누적수량이 0일 때 수익률 계산 저장

      BigDecimal fee = new BigDecimal("0.0");
         BigDecimal cal = new BigDecimal("2");
         
         if(sitename.equals("bithumb")) {
            fee = new BigDecimal("0.0025");
            fee = fee.multiply(cal);
            
         }else if(sitename.equals("coinone")) {
            fee = new BigDecimal("0.002");
            fee = fee.multiply(cal);
            
         }else if(sitename.equals("upbit")) {
            fee = new BigDecimal("0.0005");
            fee = fee.multiply(cal);
         }
         
         BigDecimal clearRevenue = ask_avgPrice.subtract(bid_avgPrice);
         clearRevenue = clearRevenue.subtract(cal);
         clearRevenue = clearRevenue.divide(bid_avgPrice,4, BigDecimal.ROUND_DOWN);
      
         return clearRevenue;
     
//     BigDecimal bid_amount = bid_avgPrice.multiply(bid_units_cal).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//     bid_amount = bid_amount.multiply(fee).setScale(2, BigDecimal.ROUND_DOWN);
//     BigDecimal bid = bid_amount.add(fee).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//     
//     
//     BigDecimal ask_amount = ask_avgPrice.multiply(ask_units_cal).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//     ask_amount = ask_amount.multiply(fee).setScale(2, BigDecimal.ROUND_DOWN);
//     BigDecimal ask = ask_amount.subtract(fee).setScale(0, BigDecimal.ROUND_HALF_DOWN);
//     
//     BigDecimal clearRevenue = ask.subtract(bid);
//     clearRevenue = clearRevenue.divide(bid,4, BigDecimal.ROUND_DOWN);

   }

//   @Override
//   public void getReport(List<Report> report) throws ParseException {
//      
//      
//      
//      List<Report> db_report = reportRepository.findByCurrency2(report.get(0).getCurrency());
//      List<ReportDto> reportDto = ReportDtoMapper.INSTANCE.toDto(db_report);
//         
//      setClear(reportDto);
////      List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
//      
//   }

//   @Override
//   public void setClear(List<ReportDto> report) throws ParseException{ // 매도 누적수량이 0일 때 수익률 계산 저장
//
//      BigDecimal bid_totalPrice_cal = new BigDecimal("0.0"); // 누적 totalPrice -> 평균 단가 저장하기 위한 변수
//      BigDecimal ask_totalPrice_cal = new BigDecimal("0.0");
//      
//      BigDecimal bid_units_cal = new BigDecimal("0.0"); // 누적 units -> 평균 단가 구하기 위한 변수
//      BigDecimal ask_units_cal = new BigDecimal("0.0");
//      
//      BigDecimal bid_totalPrice = new BigDecimal("0.0"); // price * units 구하는 식
//      BigDecimal ask_totalPrice = new BigDecimal("0.0");
//      
//      for(int i=report.size()-1; i>=0; i--) {
//
//          
//         BigDecimal bid_price = new BigDecimal("0.0");
//         BigDecimal bid_units = new BigDecimal("0.0");
//         
//         BigDecimal ask_price = new BigDecimal("0.0");
//         BigDecimal ask_units = new BigDecimal("0.0");
//         
//         if(report.get(i).getType().equals("매수")) {
//
//            bid_price = new BigDecimal(report.get(i).getPrice());
//            bid_units = new BigDecimal(report.get(i).getUnits());
//            
//            System.out.println("매수 부분 price : "+bid_price); // 매수 시작 부분
//            System.out.println("매수 부분 units : "+bid_units); // 매수 시작 부분
//            
//            bid_totalPrice = bid_price.multiply(bid_units).setScale(0, BigDecimal.ROUND_HALF_UP);
//
//            bid_totalPrice_cal = bid_totalPrice_cal.add(bid_totalPrice);
//            bid_units_cal = bid_units_cal.add(bid_units);
//
//
//            
//         }else if(report.get(i).getType().equals("매도")&&!report.get(i).getAccUnits().equals("0.0000")){
//            ask_price = new BigDecimal(report.get(i).getPrice());
//            ask_units = new BigDecimal(report.get(i).getUnits());
//            
//            System.out.println("매도 부분 price : "+ask_price); // 매도 부분
//            System.out.println("매도 부분 units : "+ask_units); // 매도 부분
//            
//            ask_totalPrice = ask_price.multiply(ask_units).setScale(0, BigDecimal.ROUND_HALF_UP);
//
//            ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
//            ask_units_cal = ask_units_cal.add(ask_units);
//            
//
//         }
//         
//         if(report.get(i).getType().equals("매도")&&report.get(i).getAccUnits().equals("0.0000")) {
//            
//               ask_price = new BigDecimal(report.get(i).getPrice());
//               ask_units = new BigDecimal(report.get(i).getUnits());
//
//               System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  price : " + ask_price); // 마지막 매도 부분
//               System.out.println("마지막 매도 부분 ( 수익률 구하는 부분 )  units : " + ask_units); // 마지막 매도 부분
//
//               ask_totalPrice = ask_price.multiply(ask_units).setScale(0, BigDecimal.ROUND_HALF_UP);
//
//               ask_totalPrice_cal = ask_totalPrice_cal.add(ask_totalPrice);
//               ask_units_cal = ask_units_cal.add(ask_units);
//
//
//               BigDecimal fee_str = new BigDecimal("0.0025");
//
//               BigDecimal bid_fee = bid_totalPrice_cal.multiply(fee_str);
//               BigDecimal ask_fee = ask_totalPrice_cal.multiply(fee_str);
//
//               BigDecimal bid_amount = bid_totalPrice_cal.add(bid_fee).setScale(0, BigDecimal.ROUND_HALF_UP);
//               BigDecimal ask_amount = ask_totalPrice_cal.subtract(ask_fee).setScale(0, BigDecimal.ROUND_HALF_UP);
//
//               BigDecimal dif = ask_amount.subtract(bid_amount);
//               
//               if (bid_amount.toString().equals("0")) {
//                  System.out.println("매수 기록이 없습니다.");
//               } else {
//                  BigDecimal revenue = dif.divide(bid_amount, 5, BigDecimal.ROUND_HALF_UP);
//
//                  // BigDecimal revenue = bid_amount.divide(,5,BigDecimal.ROUND_HALF_UP);
//                  System.out.println("bid_amount : " + bid_amount);
//                  System.out.println("ask_amount : " + ask_amount);
//                  System.out.println("수익률 : " + revenue);
//
//                  report.get(i).setClearRevenue(revenue.toString());
//
//                  System.out.println(report.get(i).getClearRevenue());
//                  
//                  bid_totalPrice_cal = new BigDecimal("0.0");
//                  ask_totalPrice_cal = new BigDecimal("0.0");
//
//                  bid_units_cal = new BigDecimal("0.0");
//                  ask_units_cal = new BigDecimal("0.0");
//
//                  bid_totalPrice = new BigDecimal("0.0");
//                  ask_totalPrice = new BigDecimal("0.0");
//               }
//               
//            
//         }
//
//         String tempId = String.valueOf(report.get(i).getOrderId());
//         Report report_entity = reportRepository.findByOrderId(tempId);
//         System.out.println(report_entity);
//
//         report_entity.setClearRevenue(report.get(i).getClearRevenue());
//      }
//      
//      
//
//   }

}