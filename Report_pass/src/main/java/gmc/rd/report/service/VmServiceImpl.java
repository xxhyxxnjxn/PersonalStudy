package gmc.rd.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.dto.ApiDto;
import gmc.rd.report.dto.ApiDtoMapper;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.dto.VmDtoMapper;
import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Report2;
import gmc.rd.report.entity.Report3;
import gmc.rd.report.entity.User;
import gmc.rd.report.entity.Vm;
import gmc.rd.report.repository.ApiRepository;
import gmc.rd.report.repository.ApiRoadingStateRepository;
import gmc.rd.report.repository.BankStateMentStateRepository;
import gmc.rd.report.repository.CandleStickStateRepository;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.VmRepository;



@Service
public class VmServiceImpl implements VmService{

	@Autowired
	private VmRepository vmRepository;
	@Autowired
	private ApiRepository apiRepository;
	
//	@Autowired
//	private BithumbReportService bithumbReportService;
	
	@Autowired
	private ApiRoadingStateRepository apiRoadingRepository;
	@Autowired
	private BankStateMentStateRepository bankStateMentStateRepository;
	@Autowired
	private CandleStickStateRepository candleStickStateRepository;
	
	@Qualifier("bithumbReportServiceInsert")
	@Autowired
	private BithumbReportService bithumbReportServiceInsert;
	
	@Qualifier("bithumbReportServiceUpdate")
	@Autowired
	private BithumbReportService bithumbReportServiceUpdate;
	
	@Autowired
	private CoinoneReportService coinoneReportService;
	
	public VmServiceImpl(VmRepository vmRepository) {
		this.vmRepository = vmRepository;
	}
	@Override
	public void getVmBithumb(String userId,User user) throws Exception{
		// System.out.println(vmRepository.findAll());
		List<Vm> vm = vmRepository.findBySiteName("bithumb");
		List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
		
		//bithumbReportService.selectReport(userId, vmDto,user);
		apiRoadingRepository.updateAsTotalNum(String.valueOf(vmDto.size()), user.getMemId(), "bithumb");
		for (int i = 0; i < vmDto.size(); i++) {
			apiRoadingRepository.updateAsNum(String.valueOf(i), user.getMemId(), "bithumb");
			bithumbReportServiceInsert.selectReport(userId, vmDto.get(i).getVmId(), user); // 이거 써야함
		}
		apiRoadingRepository.updateAs(user.getMemId(),"bithumb");
	}
	@Override
	public void getVmBithumbUpdate(String userId,User user) throws Exception{
		// System.out.println(vmRepository.findAll());
		List<Vm> vm = vmRepository.findBySiteName("bithumb");
		List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
		
		// bithumbReportService.selectReportUpdate(userId, vmDto,user);
		apiRoadingRepository.updateAsTotalNum(String.valueOf(vmDto.size()), user.getMemId(), "bithumb");
		for (int i = 0; i < vmDto.size(); i++) {
			apiRoadingRepository.updateAsNum(String.valueOf(i), user.getMemId(), "bithumb");
			bithumbReportServiceUpdate.selectReport(userId, vmDto.get(i).getVmId(),user);
		}
		
		apiRoadingRepository.updateAs(user.getMemId(),"bithumb");
		
	}

	@Override
	public void getVmCoinone(User user) throws InterruptedException{
		// System.out.println(vmRepository.findAll());
		List<Vm> vm = vmRepository.findBySiteName("coinone");
		List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);

		
		Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
		ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);
		
		apiRoadingRepository.updateAsTotalNum(String.valueOf(vmDto.size()),user.getMemId(),"coinone");
		for (int i = 0; i < vmDto.size(); i++) {
			apiRoadingRepository.updateAsNum(String.valueOf(i), user.getMemId(), "coinone");

			if ((i % 4 == 0) && (i != 0)) {
				System.out.println("delay되라 " + i);
				Thread.sleep(2500);

			}
			try {
				coinoneReportService.selectReport(user, apiDto, vmDto.get(i).getVmId());
			}catch(Exception e) {
				Thread.sleep(15000);
				i--;
				e.printStackTrace();
			}
			
		}
		int updateAsAmount =  apiRoadingRepository.updateAs(user.getMemId(),"coinone");
		
		
		bankStateMentStateRepository.updateAsTotalNum(String.valueOf(vmDto.size()),user.getMemId(),"coinone");
		for (int i = 0; i < vmDto.size(); i++) {
			bankStateMentStateRepository.updateAsNum(String.valueOf(i), user.getMemId(), "coinone");
			if ((i % 4 == 0) && (i != 0)) {
				System.out.println("delay되라 " + i);
				Thread.sleep(2500);

			}
			try {
				coinoneReportService.selectBankStateMent(user, apiDto, vmDto.get(i).getVmId());
			}catch(Exception e) {
				Thread.sleep(15000);
				i--;
				e.printStackTrace();
			}
			
		}
		int updateAsAmount_bank =  bankStateMentStateRepository.updateAs(user.getMemId(),"coinone");
		
		//return coinoneReportService.selectReport(user, vmDto);
	}
	
	@Override
	public void getVmCoinoneUpdate(User user) throws InterruptedException{
		// System.out.println(vmRepository.findAll());
		List<Vm> vm = vmRepository.findBySiteName("coinone");
		List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);

		
		Api api = apiRepository.findByMemIdAndSite(user.getMemId(), "coinone");
		ApiDto apiDto = ApiDtoMapper.INSTANCE.ToDto(api);
		
		apiRoadingRepository.updateAsTotalNum(String.valueOf(vmDto.size()),user.getMemId(),"coinone");
		for (int i = 0; i < vmDto.size(); i++) {
			apiRoadingRepository.updateAsNum(String.valueOf(i), user.getMemId(), "coinone");

			if ((i % 4 == 0) && (i != 0)) {
				System.out.println("delay되라 " + i);
				Thread.sleep(2500);

			}
			try {
				coinoneReportService.selectReportUpdate(user, apiDto, vmDto.get(i).getVmId());
			}catch(Exception e) {
				Thread.sleep(15000);
				i--;
				e.printStackTrace();
			}
			
		}
		int updateAsAmount =  apiRoadingRepository.updateAs(user.getMemId(),"coinone");
		
		
		bankStateMentStateRepository.updateAsTotalNum(String.valueOf(vmDto.size()),user.getMemId(),"coinone");
		for (int i = 0; i < vmDto.size(); i++) {
			bankStateMentStateRepository.updateAsNum(String.valueOf(i), user.getMemId(), "coinone");
			if ((i % 4 == 0) && (i != 0)) {
				System.out.println("delay되라 " + i);
				Thread.sleep(2500);

			}
			try {
				coinoneReportService.selectBankStateMentUpdate(user, apiDto, vmDto.get(i).getVmId());
			}catch(Exception e) {
				Thread.sleep(15000);
				i--;
				e.printStackTrace();
			}
			
		}
		int updateAsAmount_bank =  bankStateMentStateRepository.updateAs(user.getMemId(),"coinone");
		
		//return coinoneReportService.selectReport(user, vmDto);
	}
	


}
