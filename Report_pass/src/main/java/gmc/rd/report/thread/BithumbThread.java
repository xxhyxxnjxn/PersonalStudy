package gmc.rd.report.thread;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ReportDtoMapper;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.dto.VmDtoMapper;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.User;
import gmc.rd.report.entity.Vm;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.UserRepository;
import gmc.rd.report.repository.VmRepository;
import gmc.rd.report.service.BithumbReportService;
import gmc.rd.report.service.UpbitReportService;
import gmc.rd.report.service.VmService;

@WebListener
@Controller
public class BithumbThread/* implements ServletContextListener, Runnable*/  {
// 하루에 한번씩 조건 공통코인 넣는 작업

	/** 작업을 수행할 thread */
	private Thread thread;
	private boolean isShutdown = false;
	/** context */
	private ServletContext sc;

	/** 작업을 수행한다 */
	public void startDaemon() {
		if (thread == null) {
		//	 thread = new Thread(this, "Daemon thread for background task");
//            thread.setDaemon(true);
		}
		if (!thread.isAlive()) {
			thread.start();
		}
	}
	/** 스레드가 실제로 작업하는 부분 */
	@Autowired
	private VmService vmService;



	public void run() {

//		Thread currentThread = Thread.currentThread();
//		while (currentThread == thread && !this.isShutdown) {
//			try {
//				long start = System.currentTimeMillis();
//				System.out.println("== DaemonListener is running. ==");
//
////이란에 실행할 함수 입력
//				
//				// thread에서는 user로 for문 돌리면 됨
//				// 여기서는 로그인한 유저정보를 받아와야한다.
//				List<Vm> vm = vmRepository.findBySiteName("bithumb");
//				
//				List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
//				
//				Gson gson = new Gson();
//
//				List<BithumbTransactionVo> bithumbTransaction = null;
//
//				List<ReportDto> reportDto = null;
//
//				List<Report> report = null;
//				List<Report> bithumbReport = null;
//
//				String jsonStr = null;
//				
//				 List<User> userList = userRepository.findAll(); 
//				 for (User user : userList) {
//					 
//				 String userId = user.getMemId(); 
//				 user = userRepository.findByMemId1(user.getMemId()); 
//				Api api = apiRepository.findByMemIdAndSite(userId, "bithumb"); // 로그인한 유저 아이디 들고와야함
//				ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);
//				
//				if (apiDto == null) {
//					System.out.println("빗썸 api 키 없음");
//					
//				} else {
//
//					for (VmDto result : vmDto) {
//
//						System.out.println("coin : " + result);
//							String offSet = "0";
//							bithumbTransaction = bithumbApiService.getUserTransaction(api.getApiKey(), api.getSecretKey(),result.getVmId(),offSet);
//							
//							if(bithumbTransaction==null) {
//								break;
//							}else {
//								jsonStr = new ObjectMapper().writeValueAsString(bithumbTransaction);
//
//								Type listType = new TypeToken<List<ReportDto>>() {
//								}.getType();
//								reportDto = gson.fromJson(jsonStr, listType);
//
//								report = ReportDtoMapper.INSTANCE.toEntity(reportDto);
//								
//								for (int i = 0; i < report.size(); i++) {
//									// OrderId설정 = timestamp+currency+units+type -> 나중에 거래 내역 다시 불러와서 저장할 때 중복저장 안되게
//									// 함
//									String oderId = report.get(i).getTransactionDate() + report.get(i).getCurrency() + report.get(i).getType();
//									report.get(i).setOrderId(oderId);
//									String tempType = report.get(i).getType();
//									if(tempType.equals("1")) {
//										report.get(i).setType("매수");
//									}else if(tempType.equals("2")) {
//										report.get(i).setType("매도");								
//									}else if(tempType.equals("4")) {
//										report.get(i).setType("입금");			
//										report.get(i).setPrice("0.0");
//									}else if(tempType.equals("5")) {
//										report.get(i).setType("출금");								
//										report.get(i).setPrice("0.0");
//									}		
//									report.get(i).setSite("bithumb"); // 사이트 설정
//									report.get(i).setUser(user); // 사이트 설정
//									
//								}
//								System.err.println(report.size());
//								//bithumbReport = setBithumbData(report,user);
//								 reportRepository.saveAll(report);
//							} // transaction
//						
//					}//vm for문 돌리기
//				}// 빗썸 api key 돌리기
//			}//user list 돌리기
//				 
////이안에 실행할 함수 입력
//
//				Thread.sleep(1000 * 60);
//
//				long end = System.currentTimeMillis();
//
//				System.out.println("실행 시간 1 : " + (end - start) / 1000.0);
//				System.out.println("실행 시간 1 start: " + start);
//				System.out.println("실행 시간 1 end: " + end);
//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println("== DaemonListener end. ==");

	}

	/** 컨텍스트 초기화 시 데몬 스레드를 작동한다 */
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("== DaemonListener.contextInitialized has been called. ==");
		sc = event.getServletContext();
		startDaemon();
	}

	/** 컨텍스트 종료 시 thread를 종료시킨다 */
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("== DaemonListener.contextDestroyed has been called. ==");
		this.isShutdown = true;
		try {
			thread.join();
			thread = null;
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}