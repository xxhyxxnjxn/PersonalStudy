package gmc.rd.report.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.bithumb.vo.BithumbBalanceKrwVo;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.coinone.vo.CoinoneBalanceKrwVo;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.api.upbit.vo.UpbitAccountDataVo;
import gmc.rd.report.config.auth.PrincipalDetail;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ReportDto;
import gmc.rd.report.dto.ReportDto2;
import gmc.rd.report.dto.ReportDto3;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.CoinoneState;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.BankStateMentStateRepository;
import gmc.rd.report.repository.CandleStickStateRepository;
import gmc.rd.report.repository.CoinoneStateRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.ReportRepository2;
import gmc.rd.report.repository.ReportRepository3;
import gmc.rd.report.repository.RevenueStateRepository;
import gmc.rd.report.service.ApiService;
import gmc.rd.report.service.ShowService;

@Controller
public class ShowController {

   @Autowired
   private ShowService showService;
   @Autowired
   private  ApiService apiService;
   @Autowired
   private  ApiRepository apiRepository;
   @Autowired
	private ApiRoadingStateRepository apiRoadingRepository;
   @Autowired
	private BankStateMentStateRepository bankStateMentStateRepository;
	@Autowired
	private CandleStickStateRepository candleStickStateRepository;
   @Autowired
   private RevenueStateRepository revenueStateRepository;
   @Autowired
   private CoinoneStateRepository coinoneStateRepository;
   @Autowired
   private ReportRepository reportRepository;
   @Autowired
   private ReportRepository2 reportRepository2;
   @Autowired
   private ReportRepository3 reportRepository3;


   @RequestMapping("/getList")
   public ModelAndView getList(HttpServletResponse response, @AuthenticationPrincipal PrincipalDetail principal)
         throws IOException {
      ModelAndView mv = new ModelAndView();
      List<ApiDto> list = showService.getApis(principal.getUsername());
      DecimalFormat formatter = new DecimalFormat("###,###");
      if (list.size() == 0) {
         mv.setViewName("intro");
         return mv;
      } else {

        // System.out.println("사이트 불러오기 : " + list.get(0).getSite());

         BithumbBalanceKrwVo vo = null;
         List<UpbitAccountDataVo> vo2 = null;
         CoinoneBalanceKrwVo vo3 = null;
         String str1 = null;
         String str2 = null;
         String str3 = null;
         for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSite().equals("bithumb")) {
               
               vo = showService.getBithumbBalance(list.get(i));
               double number =  Math.round(Double.valueOf(vo.getAvailable_krw()));
               int intnum = (int)(number);
               formatter.format(intnum);
               str1 = formatter.format(intnum);
            } else if (list.get(i).getSite().equals("upbit")) {
               
               vo2 = showService.getUpbitBalance(list.get(i));
               str2 = null;
               for (int j = 0; j < vo2.size(); j++) {
                  if (vo2.get(j).getCurrency().equals("KRW")) {
                     double number =  Math.round(Double.valueOf(vo2.get(j).getBalance()));
                     int intnum = (int)(number);
                     str2 = formatter.format(intnum);
                     
                     
                  }
               }
            } else if (list.get(i).getSite().equals("coinone")) {
               // db업데이트 하는거 false - > true로 coinoneState update하기
               CoinoneState cs = coinoneStateRepository.findOne();
               //System.out.println(cs);
               
               if(cs.isState()==false) {
                  cs.setState(true);
                  
                  //System.out.println(cs);
               }else {
                  try {
                     Thread.sleep(1000);
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }
               }
               vo3 = showService.getCoinoneBalance(list.get(i));
               if(vo3!=null) {
            	   double number =  Math.round(Double.valueOf(vo3.getBalance()));
                   int intnum = (int)(number);
                   str3 = formatter.format(intnum);
               }else {
            	   str3 = null;
               }
               
            }
         }
         mv.addObject("bithumbbal", str1);
         mv.addObject("upbitbal", str2);
         mv.addObject("coinonebal", str3);
         mv.addObject("list", list);
         mv.setViewName("intro");
         return mv;

      }
   }

   @RequestMapping("/gobalance")
   public String balance(ApiDto apiDto) {
     // System.out.println("들고온거" + apiDto);
      ApiDto apidto = apiService.selectApiKey(apiDto);

//      apidto.getIdx();
//      apidto.getMemId();
//      apidto.getSite();
//      apidto.getApiKey();
//      apidto.getSecretKey();
//      System.out.println("===============================");
      System.out.println(apidto);

      if (apiDto.getSite().equals("bithumb")) {

         BithumbBalanceKrwVo vo = showService.getBithumbBalance(apidto);

      } else if (apiDto.getSite().equals("upbit")) {

         List<UpbitAccountDataVo> vo = showService.getUpbitBalance(apidto);

      } else if (apiDto.getSite().equals("coinone")) {

         CoinoneBalanceKrwVo vo = showService.getCoinoneBalance(apidto);

        // System.out.println("코인원 자산 : " + vo.getAvail());
      }

      return "gobalance";
   }
   @Transactional
   @RequestMapping("/deleteapi")
   public void delete(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto) {
      System.out.println("delete 다녀감");
      System.out.println(apiDto);
      Optional<Api> apidto =  apiRepository.findById(Integer.valueOf(apiDto.getIdx()));
      String temSite = apidto.get().getSite();
      String memId = apidto.get().getMemId();
      int apiStateAmount = apiRoadingRepository.deleteByMemId(temSite,memId);
      bankStateMentStateRepository.deleteByMemId(temSite, memId);
	  candleStickStateRepository.deleteByMemId(temSite, memId);
	  int revenueStateAmount = revenueStateRepository.deleteByMemId(temSite,memId);
	  apiRepository.deleteById(Integer.valueOf(apiDto.getIdx()));
      try {
         response.getWriter().print("");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   @Transactional
   @RequestMapping("/deleteList")
   public void deleteList(HttpServletResponse response, HttpServletRequest request,  ApiDto apiDto) {
	   System.out.println("delete 다녀감");
	
	   Optional<Api> apidto =  apiRepository.findById(Integer.valueOf(apiDto.getIdx()));
	    apiRepository.deleteById(Integer.valueOf(apiDto.getIdx()));
	    
	    String memId = apidto.get().getMemId();
	    String temSite = apidto.get().getSite();
	    int val = 0;
	   if(temSite.equals("bithumb")) {
		  val = reportRepository.deleteByMemId(memId);
		   
	   }
	   else if(temSite.equals("coinone")) {
		   val =  reportRepository3.deleteByMemId(memId);
		   
	   }
	   else if(temSite.equals("upbit")) {
		 val =  reportRepository2.deleteByMemId(memId);
		 
		   
	   }
	   int apiStateAmount = apiRoadingRepository.deleteByMemId(temSite,memId);
	   bankStateMentStateRepository.deleteByMemId(temSite, memId);
	   candleStickStateRepository.deleteByMemId(temSite, memId);
	   int revenueStateAmount = revenueStateRepository.deleteByMemId(temSite,memId);
	   if(val==0) {
		   System.out.println("삭제된 내역없음");
	   }else {		   
		   System.out.println("1개이상 삭제 ");
	   }
	   
	   
	   try {
		   response.getWriter().print("");
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
   }

   @RequestMapping("/updateapi")
   public void update(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto) {
      //System.out.println("update 다녀감");
      //System.out.println(apiDto);

      try {
         response.getWriter().print("");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @RequestMapping("/detailapi")
   public void detailapi(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto) {
//      System.out.println("update 다녀감");
//      System.out.println(apiDto);
//      System.out.println();
//      System.out.println();
      try {
         response.getWriter().print("");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @RequestMapping("/showBalance")
   public void showBalance(HttpServletResponse response, HttpServletRequest request, ApiDto apiDto)
         throws IOException {

      ApiDto apidto = apiService.selectApiKey(apiDto);

      if (apiDto.getSite().equals("bithumb")) {

         BithumbBalanceKrwVo vo = showService.getBithumbBalance(apidto);
         JSONObject jSONObject = new JSONObject();
         jSONObject.put("avail", vo.getAvailable_krw());

         response.getWriter().print(jSONObject);

      } else if (apiDto.getSite().equals("upbit")) {

         List<UpbitAccountDataVo> vo = showService.getUpbitBalance(apidto);
         JSONObject jSONObject = new JSONObject();
         jSONObject.put("avail", vo.get(0).getBalance());
         response.getWriter().print(jSONObject);

      } else if (apiDto.getSite().equals("coinone")) {

         CoinoneBalanceKrwVo vo = showService.getCoinoneBalance(apidto);
         JSONObject jSONObject = new JSONObject();
         jSONObject.put("avail", vo.getAvail());
         response.getWriter().print(jSONObject);

      }

   }
   
   @RequestMapping("/roadingstate")
   public void roadingstate(@AuthenticationPrincipal PrincipalDetail principal,HttpServletResponse response, HttpServletRequest request,ApiDto apiDto) throws Exception{
	   apiDto.setMemId(principal.getUsername());
	   
	   apiDto = apiService.selectApiKey(apiDto);
	   
	   System.out.println("roadingstate : " + apiDto.getSite());
	   System.out.println("roadingstate : " + apiDto.getMemId());
	   System.out.println("roadingstate : " + apiDto.getApiKey());
	   System.out.println("roadingstate : " + apiDto.getSecretKey());
	  
   }
   
   @RequestMapping("/roading")
   public String roading(HttpServletResponse response, HttpServletRequest request ,@RequestParam String site,@RequestParam String apiKey, @RequestParam String secretKey) throws Exception{
	  
	  System.out.println(site);
	  System.out.println(apiKey);
	  System.out.println(secretKey);
	  return "mem/roading";
   }

}