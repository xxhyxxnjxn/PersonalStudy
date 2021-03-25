package gmc.rd.report.thread;

import java.text.ParseException;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.User;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.UserRepository;
import gmc.rd.report.service.UpbitReportService;
import gmc.rd.report.service.VmService;

@WebListener
@Controller
public class UpbitThread /*implements ServletContextListener, Runnable*/ {
// 하루에 한번씩 조건 공통코인 넣는 작업

	/** 작업을 수행할 thread */
	private Thread thread;
	private boolean isShutdown = false;
	/** context */
	private ServletContext sc;

	/** 작업을 수행한다 */
	public void startDaemon() {
		if (thread == null) {
	//	thread = new Thread(this, "Daemon thread for background task");
//            thread.setDaemon(true);
		}
		if (!thread.isAlive()) {
			thread.start();
		}
	}

	/** 스레드가 실제로 작업하는 부분 */
	@Autowired
	private VmService vmService;


	
	int page_cnt = 1;
	List<Report2> report2;
	public void run()  {
//
//		Thread currentThread = Thread.currentThread();
//		while (currentThread == thread && !this.isShutdown) {
//			try {
//				long start = System.currentTimeMillis();
//				System.out.println("== DaemonListener is running. ==");
//
////이란에 실행할 함수 입력
//				
//				List<User> userList = userRepository.findAll();
//				for (User user : userList) {
//					
//					user.getMemId();
//					//user = userRepository.findByMemId1(user.getMemId());
//					Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "upbit");
//					if(api == null) {
//						System.out.println(user.getMemId() + " : apikey 없음" );
//					}else {
//						
//						while (true) {
//							report2 = upbitReportService.selectReport("upbit", page_cnt, user);
//
//							if (report2 == null) {
//								page_cnt = 1;
//								break;
//							} else {
//								if(page_cnt == 4) {
//									
//									break;
//									
//								}else {
//									
//									page_cnt++;
//								}
//							}
//						}
//					}
//					
//					System.out.println("업비트 끝");
//				}
//
//
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