package gmc.rd.report.thread;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
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
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.coinone.vo.CoinoneCoinTransactionsDataVo;
import gmc.rd.report.api.coinone.vo.CoinoneTransactionVo;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.dto.ReportDtoMapper3;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.dto.VmDtoMapper;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.CoinoneState;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.entity.Vm;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.CoinoneStateRepository;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.UserRepository;
import gmc.rd.report.repository.VmRepository;
import gmc.rd.report.service.UpbitReportService;
import gmc.rd.report.service.VmService;

@WebListener
@Controller
public class CoinoneThread /*implements ServletContextListener, Runnable */{
// 하루에 한번씩 조건 공통코인 넣는 작업

	/** 작업을 수행할 thread */
	private Thread thread;
	private boolean isShutdown = false;
	/** context */
	private ServletContext sc;

	/** 작업을 수행한다 */
	public void startDaemon() {
		if (thread == null) {
		//thread = new Thread(this, "Daemon thread for background task");
//            thread.setDaemon(true);
		}
		if (!thread.isAlive()) {
			thread.start();
		}
	}

	/** 스레드가 실제로 작업하는 부분 */
	@Autowired
	private VmService vmService;

	
	
	public void run()  {

//		Thread currentThread = Thread.currentThread();
//		while (currentThread == thread && !this.isShutdown) {
//			try {
//				long start = System.currentTimeMillis();
//				System.out.println("== DaemonListener is running. ==");
//
////이란에 실행할 함수 입력
//				List<Vm> vm = vmRepository.findBySiteName("coinone");
//				List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
//				
//				Gson gson = new Gson();
//
//				List<CoinoneTransactionVo> coinoneTransaction = null;
//				List<CoinoneCoinTransactionsDataVo> coinoneCoinTransactionsDataVo = null;
//
//				List<ReportDto3> reportDto3 = null;
//
//				List<Report3> report3 = null;
//
//				String jsonStr = null;
//				
//				 List<User> userList = userRepository.findAll(); 
//				 for (User user : userList) {
//					 
//				 String userId = user.getMemId(); 
//				 user = userRepository.findByMemId1(user.getMemId()); 
//
//
//						Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
//						ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);
//
//						if (apiDto == null) {
//							System.out.println("코인원 api 키 없음");
//						} else {
//							int cnt = 0;
//							for (VmDto result : vmDto) {
//								  //if db table이 true이면 인증키가 끼어듦 = true 평상시 false
//				                  CoinoneState cs =  coinoneStateRepository.findOne();
//
//				                  
//				                  if(cs.isState()) {
//				                     //sleep, 그다음
//				                     Thread.sleep(2000);   //2초  delay
//				                     System.out.println("api key 인증요청 deley----------------------------------------------------------------------------------");
//				                     //db update 
//				                     cs.setState(false);
//
//				                     System.out.println("확인 3");
//				                     System.out.println(cs.isState());
//				                     
//				                  }
//								
//								if ((cnt % 5 == 0) && (cnt != 0)) {
//									System.out.println("delay되라 " + cnt);
//
//									Thread.sleep(2000);
//
//								}
//
//								System.out.println("coin : " + result.getVmId());
//								Date timestamp = new Date();
//								System.out.println(timestamp);
//								System.out.println(cnt);
//
//								HashMap<String, String> hashMap = new HashMap<String, String>();
//								hashMap.put("apiKey", api.getApiKey());
//								hashMap.put("secretKey", api.getSecretKey());
//								cnt++;
//
//								hashMap.put("currency",result.getVmId() );
//								
//								coinoneTransaction = coinoneApiService.MyCompletedOrders2(hashMap); // 거래 내역
//								coinoneCoinTransactionsDataVo = coinoneApiService.coinTransacionHistory(hashMap); // 입출금 내역
//								Thread.sleep(2000);
//								
//								if (coinoneTransaction != null) {
//								
//									for(int i = 0 ; i<coinoneTransaction.size();i++) {
//				                        
//				                        coinoneTransaction.get(i).setOrderId(coinoneTransaction.get(i).getOrderId()+coinoneTransaction.get(i).getType()+coinoneTransaction.get(i).getTimestamp());
//				                        System.out.println(   coinoneTransaction.get(i).getOrderId());                     
//				                     
//				                     }
//									
//									
//									jsonStr = new ObjectMapper().writeValueAsString(coinoneTransaction);
//									Type listType = new TypeToken<List<ReportDto3>>() {
//									}.getType();
//									reportDto3 = gson.fromJson(jsonStr, listType);
//
//									report3 = ReportDtoMapper3.INSTANCE.toEntity(reportDto3);
//									
//									for(int i=0; i<report3.size();i++) {
//										report3.get(i).setUser(user);
//										report3.get(i).setSite("coinone");
//										report3.get(i).setCurrency(result.getVmId());
//										if (report3.get(i).getType().equals("ask")) {
//											report3.get(i).setType("매도");
//
//										} else if (report3.get(i).getType().equals("bid")) {
//											report3.get(i).setType("매수");
//
//										}
//									}
//									reportRepository3.saveAll(report3);
//								}
//								
//								if(coinoneCoinTransactionsDataVo!=null) {
//									for(int i = 0 ; i<coinoneCoinTransactionsDataVo.size();i++) {
//				                        
//										coinoneCoinTransactionsDataVo.get(i).setTxid(coinoneCoinTransactionsDataVo.get(i).getTxid()+coinoneCoinTransactionsDataVo.get(i).getType()+coinoneCoinTransactionsDataVo.get(i).getTimestamp());
//				                        System.out.println(  coinoneCoinTransactionsDataVo.get(i).getTxid());                     
//				                     
//				                     }
//									
//									
//									jsonStr = new ObjectMapper().writeValueAsString(coinoneCoinTransactionsDataVo);
//									Type listType = new TypeToken<List<ReportDto3>>() {
//									}.getType();
//									reportDto3 = gson.fromJson(jsonStr, listType);
//									
//									report3 = ReportDtoMapper3.INSTANCE.toEntity(reportDto3);
//									
//									for(int i=0; i<report3.size();i++) {
//										report3.get(i).setUser(user);
//										report3.get(i).setSite("coinone");
//										report3.get(i).setCurrency(result.getVmId());
//										report3.get(i).setFee("0.0");
//										report3.get(i).setPrice("0.0");
//										if (report3.get(i).getType().equals("receive")) {
//											report3.get(i).setType("입금");
//
//										} else if (report3.get(i).getType().equals("send")) {
//											report3.get(i).setType("출금");
//
//										}
//									}
//									
//									reportRepository3.saveAll(report3);
//									
//								}
//								
//
//							}
//							}
//						}
////이안에 실행할 함수 입력
//
//				Thread.sleep(1000*60);
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