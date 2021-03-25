package gmc.rd.report.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.eval.StringValueEval;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.dto.ReportDtoMapper2;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.dto.VmDtoMapper;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.BankStateMentStateRepository;
import gmc.rd.report.repository.CandleStickStateRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.service.BithumbReportService;
import gmc.rd.report.service.UserService;

@Controller
public class ExcelController {
	
	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private ReportRepository2 reportRepository2;
	@Autowired
	private ReportRepository3 reportRepository3;
	
	@Qualifier("bithumbReportServiceInsert")
	@Autowired
	private BithumbReportService bithumbReportServiceInsert;
	@Qualifier("bithumbReportServiceUpdate")
	@Autowired
	private BithumbReportService bithumbReportServiceUpdate;
	
	@Autowired
	private BankStateMentStateRepository bankStateMentStateRepository;

	@Autowired
	private CandleStickStateRepository candleStickStateRepository;
	
	@Autowired
	private UserService userService;
	
	 @ResponseBody
	 @Transactional
	  @PostMapping("/excel/read")
	  public void readExcel(@RequestParam("file") MultipartFile file,@AuthenticationPrincipal PrincipalDetail principal)  throws ParseException, Exception { // 2
		 /*
		  * 		boardService.updateLog(orderId,log);
		try {
			response.getWriter().print("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		  * 
		  * */
		bankStateMentStateRepository.updateAsStart(principal.getUsername(),"bithumb");
		candleStickStateRepository.updateAsStart(principal.getUsername(),"bithumb");
		
		User this_user = principal.getUser();
	    List<Report> dataList = new ArrayList<>();

	    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3
	 

	    if (!extension.equals("xlsx") && !extension.equals("xls")) {
	      throw new IOException("엑셀파일만 업로드 해주세요.");
	    }

	    Workbook workbook = null;

	    if (extension.equals("xlsx")) {
	      workbook = new XSSFWorkbook(file.getInputStream());
	    } else if (extension.equals("xls")) {
	      workbook = new HSSFWorkbook(file.getInputStream());
	    }

	    Sheet worksheet = workbook.getSheetAt(0);
	    
 
	    
	    HashMap<String, String> hashMap = new HashMap<>();
	   
	    for (int i = 3; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4
	    	
		      Row row = worksheet.getRow(i);

		      Report data = new Report();
	      // 날짜 타임스탬프로 변환시키기
	  	
	      // 자산 영문으로 바꾸기
	      String temCurrency = row.getCell(3).getStringCellValue().toString();
	      String[] Unitcurrency = temCurrency.split(" ");
	      //원화 거래는 제외
	      if(!Unitcurrency[1].equals("KRW")) {
	    	  String temDate = row.getCell(0).getStringCellValue().toString();
	    	  	try {
	    	  		
	    	  		
					SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					fm.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
					Date to = fm.parse(temDate);
					long timestamp=to.getTime()*1000;
					
					
					
					data.setTransactionDate(String.valueOf(timestamp));
				    
				      
				      
				      data.setCurrency(Unitcurrency[1]);
				      
				      // 구분 입금출금
				      String temType = row.getCell(2).getStringCellValue();
				      String temTypeNum = "";
				      String realUnit = "";
				      if(temType.equals("매수")) {
				    	  temTypeNum ="1";
				    	  data.setType(temType);	    	 	
					      String temTotPrice = row.getCell(7).getStringCellValue().toString();
					      String[] UnitTotPrice = temTotPrice.split(" ");
					      double totPrice = Double.valueOf(UnitTotPrice[0].replace(",", ""));
					 //     data.setTotalPrice(String.valueOf(totPrice).replace("-", ""));
					      // 주문 수량
					      String temUnits = row.getCell(3).getStringCellValue().toString();
					      String[] Unit= temUnits.split(" ");
					      realUnit =Unit[0].replace(",", "").replace("+", "").replace(" ","");
					      data.setUnits(realUnit);
					      
				      }else if(temType.equals("매도")) {
				    	  temTypeNum ="2";
				    	  data.setType(temType);	    	 	    	  	  
					      String temTotPrice = row.getCell(7).getStringCellValue().toString();
					      String[] UnitTotPrice = temTotPrice.split(" ");
					      double totPrice = Double.valueOf(UnitTotPrice[0].replace(",", ""));
					  //    data.setTotalPrice(String.valueOf(totPrice));
					      // 주문 수량
					      String temUnits = row.getCell(3).getStringCellValue().toString();
					      String[] Unit= temUnits.split(" ");
					      realUnit =Unit[0].replace(",", "").replace("+", "").replace(" ","");
					      data.setUnits(realUnit);
				      }else if(temType.equals("에어드랍")||temType.equals("COIN 이벤트 입금")||temType.equals("외부입금")) {
				    	  temTypeNum ="4";
				    	  data.setType("입금");	    	 	    	  
					    //  data.setTotalPrice("");
				    	  // 주문 수량
					      String temUnits = row.getCell(3).getStringCellValue().toString();
					      String[] Unit= temUnits.split(" ");
					      realUnit =Unit[0].replace(",", "").replace("+", "").replace(" ","");
					      data.setUnits(realUnit);
				      }else if(temType.equals("외부출금")) {
				    	  temTypeNum ="5";
				    	  data.setType("출금");
				    	  String temTotPrice = row.getCell(7).getStringCellValue().toString();
						  String[] UnitTotPrice = temTotPrice.split(" ");
						  String totPrice = UnitTotPrice[0].replace(",", "");
						  //   data.setTotalPrice(String.valueOf(totPrice));
				    	  realUnit = String.valueOf(totPrice).replace("-", "");
				    	//  data.setTotalPrice("");
				    	  // 주문 수량 = 정산금액

					      data.setUnits(realUnit);
				      }
				      //  주문아이디 조합하기
				     String orderIdTime =  String.valueOf(timestamp/1000000);
				     String temOrderId = "";
				     String num = "0";
				     for (int k = i+1; k < worksheet.getPhysicalNumberOfRows(); k++) {
				    		
							Row rowFirst = worksheet.getRow(i);
				    		Row rowSecond = worksheet.getRow(k);
				    		String rowFirstRow= rowFirst.getCell(0).getStringCellValue().toString()+rowFirst.getCell(3).getStringCellValue().toString()+ rowFirst.getCell(2).getStringCellValue().toString();
				    		String rowSecondRow= rowSecond.getCell(0).getStringCellValue().toString()+rowSecond.getCell(3).getStringCellValue().toString()+ rowSecond.getCell(2).getStringCellValue().toString();
				    		System.err.println(rowFirstRow);
				    		System.err.println(rowSecondRow);
				    		if(rowFirstRow.equals(rowSecondRow)) {
				    			orderIdTime +=  1 ;
				    			temOrderId = orderIdTime+Unitcurrency[1]+temTypeNum+realUnit.replace(".", "")+this_user.getMemId()+num;
				    			data.setOrderId(temOrderId);
				    		}else {
				    			temOrderId = orderIdTime+Unitcurrency[1]+temTypeNum+realUnit.replace(".", "")+this_user.getMemId()+num;
				    			data.setOrderId(temOrderId);
				    		}
						}
				     
				     
						} catch (ParseException e) {
							e.printStackTrace();
						}
				      // 거래금액 krw 랑 부호 때기
				      String temPrice = row.getCell(4).getStringCellValue().toString();
				      String[] UnitPrice = temPrice.split(" ");
				      //엑셀 값이 -인 경우 0으로 대체
				      if(UnitPrice[0].replace(",", "").equals("-")) {
				    	  data.setPrice("0.0");
				      }else {
				    	  data.setPrice(UnitPrice[0].replace(",", ""));
				      }
				      
				      String temFee = row.getCell(6).getStringCellValue().toString();
				      String[] UnitFee = temFee.split(" ");
				      // 엑셀 값이 - 인경우 0으로 대체
				      if(UnitFee[0].replace(",", "").equals("-")) {
				    	  data.setFee("0.0");
				      }else {
				    	  data.setFee(UnitFee[0].replace(",", ""));
				      }
				      data.setSite("bithumb");
				      data.setUser(this_user);
				      dataList.add(data);
				      reportRepository.saveAll(dataList);
				    }
		      }
	   
	    
	    String memId = principal.getUsername();
        User user = userService.selectMemId(principal.getUser());
        
        bankStateMentStateRepository.updateAs(user.getMemId(),"bithumb");
        
		bithumbReportServiceUpdate.selectCandleStick(user); // 입출금 가격 candlestick 조회
		candleStickStateRepository.updateAs(user.getMemId(),"bithumb");
	 

	  }
//	 // 코인원
//	 @ResponseBody
//	 @Transactional
//	 @PostMapping("/excel/readCoinone")
//	 public void readcoinoneExcel(@RequestParam("file") MultipartFile file,@AuthenticationPrincipal PrincipalDetail principal)  throws Exception { // 2
//		 /*
//		  * 		boardService.updateLog(orderId,log);
//		try {
//			response.getWriter().print("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		  * 
//		  * */
//		 
////		 	String fileName= file.getOriginalFilename();
////	        File file2= new File(fileName);
//
//	        // this gives you a 2-dimensional array of strings
//		 
//		 	String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3
//		 
//
//		    if (!extension.equals("csv")) {
//		      throw new IOException("엑셀파일만 업로드 해주세요.");
//		    }
//		    
//	        List<List<String>> lines = new ArrayList<>();
//	        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
//	        List<ReportDto3> dataList = new ArrayList<>();
//	        
//	        ReportDto3 data = new ReportDto3();
//	        Report3 report3 = null;
//	        User this_user = principal.getUser();
//
//	        try{
//	           
//	        	String line;
//	            while((line=br.readLine()) != null){
//	                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
//	                // this adds the currently parsed line to the 2-dimensional string array
//	                lines.add(Arrays.asList(values));
//	            }
//
//	            br.close();
//	        }catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        }
//	        
//	        // the following code lets you iterate through the 2-dimensional array
//	        int lineNo = 1;
//	       
//	        BigDecimal price = new BigDecimal("0.0");
//	       // DecimalFormat df=new DecimalFormat("#.##");
//	        
//	        String timestamp_str=null;
//	        String currency=null;
//	        String units=null;
//	        String type=null;
//	        
//	        String before_orderId = "beforeOrderId";
//	        String orderId = null;
//	        
//	        int num = 0;
//	        
//	        for(List<String> line: lines) {
//	            int columnNo = 1;
//
//	            if(lineNo>1) {
//	       
//	            	data.setUser(this_user);
//	            	data.setSite("coinone");
//	            	
//	            	for (String value: line) {
//	            
//	                
//	                	if(columnNo == 1) { //시간
//		                	SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							//fm.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
//		                	System.out.println(value);
//							Date to = fm.parse(value.replace("\"", ""));
//							long timestamp=to.getTime();
//							
//							//System.out.println(timestamp);
//							
//		                	//reportDto.get(lineNo-2).setTransactionDate(String.valueOf(timestamp));
//							timestamp_str = String.valueOf(timestamp);
//							data.setTransactionDate(String.valueOf(timestamp));
//		                	
//		                }else if(columnNo == 3) { // 코인
//		       
//		                	value = value.replace("\"", "").replace(" ", "");
//		                	
//		                	currency = value;
//		                	//System.out.println(value);
//		                	data.setCurrency(value);
//		                }else if(columnNo == 5) { //타입
//		                	value = value.replace("\"", "").replace(" ", "");
//		                	if(value.equals("지정가매수")) {
//		                		
//		                		type="bid";
//		                		data.setType("매수");
//		                		//reportDto.get(lineNo).setType("bid");
//		                	}else if(value.equals("지정가매도")) {
//		                		type="ask";
//		                		data.setType("매도");
//		                		//reportDto.get(lineNo).setType("ask");
//		                	}
//		                	
//		                }else if(columnNo == 6) { // 체결량
//		                	
//		                	value = value.replaceAll("[A-Z]", "").replace("\"", "").replace(",", "");
//		                	//System.out.println(value);
//		                	//units = String.valueOf( df.format(Math.round(Double.valueOf(value)*100)/100.0) );
//		                	units = value;
//		                	data.setUnits(value);
//		                }else if(columnNo == 7) { // 체결가
//		                	value = value.replace(",", "").replaceAll("[A-Z]", "").replace("\"", "");
//		                	
//		                	price = new BigDecimal(value);
//		                	//System.out.println(value);
//		                	
//		                	data.setPrice(value);
//		                }else if(columnNo == 9) { // 수수료
//		                	value = value.replaceAll("[A-Z]", "").replace("\"", "").replace(",", "");
//		                	//System.out.println(type);
//		                	if(type.equals("bid")) {
//		                		BigDecimal fee = new BigDecimal(value);
//			                	fee = fee.multiply(price);
//			                	//System.out.println(fee.toPlainString());
//			                	
//			                	data.setFee(fee.toPlainString());
//			                	
//		                	}else if(type.equals("ask")) {
//		                		//System.out.println(value);
//		                		
//		                		data.setFee(value);
//		                	}
//		                	
//		                	
//		                	
//		                	//reportDto.get(lineNo).setPrice(fee.toPlainString());
//		                }
//	               
//	                
//	                //System.out.println("Line " + lineNo + " Column " + columnNo + ": " + value);
//	                columnNo++;
//	                }
//	            	
//	            	orderId = timestamp_str+currency+type+units.replace(".", "");
//	            	
//	            	if(before_orderId.equals(orderId)) {
//	            		num++;
//	            	}else {
//	            		num=0;
//	            	}
//	            	data.setOrderId(orderId+num);
//	            	
//	            	before_orderId = orderId;
//	            	
//	            	System.out.println(data);
//	            	report3 = ReportDtoMapper3.INSTANCE.toEntityExcel(data);
//	            	reportRepository3.save(report3);
//	            	
//	            }
//
//	            lineNo++;
//	        }
//	       
//	 }
//	 
//	 // 코인원
//	 @ResponseBody
//	 @Transactional
//	 @PostMapping("/excel/readCoinone/bankstatement")
//	 public void readcoinoneExcelBank(@RequestParam("file") MultipartFile file,@AuthenticationPrincipal PrincipalDetail principal)  throws IOException, ParseException { // 2
//		 /*
//		  * 		boardService.updateLog(orderId,log);
//		try {
//			response.getWriter().print("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		  * 
//		  * */
//		 
////		 	String fileName= file.getOriginalFilename();
////	        File file2= new File(fileName);
//
//	        // this gives you a 2-dimensional array of strings
//		 
//		 	String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3
//		 
//
//		    if (!extension.equals("csv")) {
//		      throw new IOException("엑셀파일만 업로드 해주세요.");
//		    }
//		    
//	        List<List<String>> lines = new ArrayList<>();
//	        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
//	        List<ReportDto3> dataList = new ArrayList<>();
//	        
//	        ReportDto3 data = new ReportDto3();
//	        Report3 report3 = null;
//	        User this_user = principal.getUser();
//
//	        try{
//	           
//	        	String line;
//	            while((line=br.readLine()) != null){
//	                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
//	                // this adds the currently parsed line to the 2-dimensional string array
//	                lines.add(Arrays.asList(values));
//	            }
//
//	            br.close();
//	        }catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        }
//	        
//	        // the following code lets you iterate through the 2-dimensional array
//	        int lineNo = 1;
//	       
//	        BigDecimal price = new BigDecimal("0.0");
//	       // DecimalFormat df=new DecimalFormat("#.##");
//	        
//	        String timestamp_str=null;
//	        String currency=null;
//	        String units=null;
//	        String type=null;
//	        
//	        String before_orderId = "beforeOrderId";
//	        String orderId = null;
//	        
//	        int num = 0;
//	        
//	        for(List<String> line: lines) {
//	            int columnNo = 1;
//
//	            if(lineNo>1) {
//	       
//	            	data.setUser(this_user);
//	            	data.setSite("coinone");
//	            	data.setPrice("0.0");
//	            	
//	            	for (String value: line) {
//	            
//	                
//	                	if(columnNo == 1) { //시간
//		                	SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							//fm.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
//							Date to = fm.parse(value.replace("\"", ""));
//							long timestamp=to.getTime();
//							
//							//System.out.println(timestamp);
//							
//		                	//reportDto.get(lineNo-2).setTransactionDate(String.valueOf(timestamp));
//							timestamp_str = String.valueOf(timestamp);
//							data.setTransactionDate(String.valueOf(timestamp));
//		                	
//		                }else if(columnNo == 2) { // 코인
//		       
//		                	value = value.replace("\"", "").replace(" ", "");
//		                	if(value!="KRW") {
//		                		currency = value;
//			                	//System.out.println(value);
//			                	data.setCurrency(value);
//		                	}
//		                	
//		                }else if(columnNo == 3) { //타입
//		                	value = value.replace("\"", "").replace(" ", "");
//		                	
//		                	
//		                		if(value.equals("출금")) {
//			                		
//			                		type="send";
//			                		data.setType(value);
//			                		//reportDto.get(lineNo).setType("bid");
//			                	}else if(value.equals("입금")) {
//			                		type="receive";
//			                		data.setType(value);
//			                		//reportDto.get(lineNo).setType("ask");
//			                	}
//
//		                		
//		                	
//		                	
//		                	
//		                }else if(columnNo == 4) { // 체결량
//		                	
//		                	value = value.replaceAll("[A-Z]", "").replace("\"", "").replace(",", "");
//		                	//System.out.println(value);
//		                	units = value;
//		                	//units = String.valueOf(  df.format(Math.round(Double.valueOf(value)*100)/100.0) );
//		                	data.setUnits(value);
//		                }else if(columnNo == 6) { // 수수료
//		                	//value = value.replaceAll("[A-Z]", "").replace("\"", "").replace(",", "");
//		                	value = value.replace("\"", "").replace(",", "");
//		                	
//		                	data.setFee(value);
//
//		                	//reportDto.get(lineNo).setPrice(fee.toPlainString());
//		                }
//	               
//	                
//	                //System.out.println("Line " + lineNo + " Column " + columnNo + ": " + value);
//	                columnNo++;
//	                }
//	            	
//	            	//data.setOrderId(timestamp_str+currency+type+units.replace(".", ""));
//	            	
//	            	orderId = timestamp_str+currency+type+units.replace(".", "");
//	            	
//	            	if(before_orderId.equals(orderId)) {
//	            		num++;
//	            	}else {
//	            		num=0;
//	            	}
//	            	data.setOrderId(orderId+num);
//	            	
//	            	before_orderId = orderId;
//	            	
//	            	
//	            	System.out.println(data);
//	            	report3 = ReportDtoMapper3.INSTANCE.toEntityExcel(data);
//	            	reportRepository3.save(report3);
//	            	
//	            }
//
//	            lineNo++;
//	        }
//		 
//	 }
//	 
//	 
//	 @ResponseBody
//	 @Transactional
//	  @PostMapping("/excel/readUpbit/bankstatement")
//	  public void readUpbitExcelBank(@RequestParam("file") MultipartFile file,@AuthenticationPrincipal PrincipalDetail principal)  throws IOException, ParseException { // 2
//
//		User this_user = principal.getUser();
//	    List<Report2> db_dataList = new ArrayList<>();
//	    List<ReportDto2> db_dataListDto = new ArrayList<>();
//
//	    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3
//	 
//
//	    if (!extension.equals("xlsx") && !extension.equals("xls")) {
//	      throw new IOException("엑셀파일만 업로드 해주세요.");
//	    }
//
//	    Workbook workbook = null;
//
//	    if (extension.equals("xlsx")) {
//	      workbook = new XSSFWorkbook(file.getInputStream());
//	    } else if (extension.equals("xls")) {
//	      workbook = new HSSFWorkbook(file.getInputStream());
//	    }
//
//	    Sheet worksheet = workbook.getSheetAt(0);
//	    
// 
//	    
//	    HashMap<String, String> hashMap = new HashMap<>();
//	   
//	    for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4
//	    	
//		      	Row row = worksheet.getRow(i);
//		      	
//		      	String currency = row.getCell(1).getStringCellValue().toString(); // 코인 
//		      	
//		      	if(!currency.equals("KRW")) {
//		      		
//		      		
//		      		System.out.println("원래 시간 : "+row.getCell(0)); // 시간
//			      	String temDate = row.getCell(0).getStringCellValue().toString();
//			      
//			      	SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd HH:mm");
//			      	fm.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
//			      	
//			      	SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
//			      	df.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9")); 
//					
//					Date to = fm.parse(temDate);
//					long timestamp=to.getTime();
//					System.out.println(timestamp);
//					
//					//String date = df.format(timestamp);
//					Date cutDate = df.parse(temDate);
//					System.out.println("시간 자르기 : "+cutDate);
//					System.out.println(cutDate.getTime());
//					
//					Calendar cal = Calendar.getInstance();
//					//Date dt = df.parse(temDate);
//					cal.setTime(cutDate);
//					cal.add(Calendar.DATE, 1);
//					
//					System.out.println("하루 더한 시간"+df.format(cal.getTime()));
//					System.out.println(cal.getTimeInMillis());
//					
//					System.out.println("currency"+currency);
//					
//					String type = row.getCell(3).getStringCellValue().toString(); // 종류
//					System.out.println("type"+type);
//					String units = null;
//					DecimalFormat unitdf=new DecimalFormat("0.0#######");
//					if(type.equals("출금")) {
//						units = row.getCell(4).getStringCellValue().toString(); // 거래 수량
//						units = units.replaceAll("[A-Z]", "").replace(",", "");
//						units = unitdf.format(Double.valueOf(units));
//						
//						BigDecimal bd_untis = new BigDecimal(units);
//						
//						String fee = row.getCell(7).getStringCellValue().toString();
//						fee = fee.replaceAll("[A-Z]", "").replace(",", "");
//						fee = unitdf.format(Double.valueOf(fee));
//						
//						BigDecimal bd_fee = new BigDecimal(fee);
//						
//						bd_untis = bd_untis.add(bd_fee);
//						units = bd_untis.toPlainString();
//						
//						System.out.println("units : "+units);
//					}else if(type.equals("입금")) {
//						units = row.getCell(4).getStringCellValue().toString(); // 거래 수량
//						units = units.replaceAll("[A-Z]", "").replace(",", "");
//						units = unitdf.format(Double.valueOf(units));
//						System.out.println("units : "+units);
//					}
//					
//					
//					String price = row.getCell(5).getStringCellValue().toString(); // 거래 금액
//					price = price.replaceAll("[A-Z]", "").replace(",", "");
//					
//					System.out.println("price"+price);
//					
//					db_dataList = reportRepository2.findByOrderIdExcel(this_user.getMemId(), units, currency, type, String.valueOf(cutDate.getTime()), String.valueOf(cal.getTimeInMillis()) );
//					db_dataListDto = ReportDtoMapper2.INSTANCE.toDtoExcel(db_dataList);
//					
//					for(int j=0;j<db_dataList.size();j++) {
//						String orderId = db_dataListDto.get(j).getOrderId();
//						
//						int x = reportRepository2.updateByPrice(orderId, price, units, currency, type, String.valueOf(cutDate.getTime()), String.valueOf(cal.getTimeInMillis()));
//						
//					}
//					
//					//System.out.println(db_dataList);
//		      	}
//
//		      }
//
//	  }
	 
	 
	 
	 
	 
	 
}
