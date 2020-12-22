package gmc.rd.report.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.rd.report.api.bithumb.service.BithumbApiService;
import gmc.rd.report.api.coinone.service.CoinoneApiService;
import gmc.rd.report.api.upbit.service.UpbitApiService;
import gmc.rd.report.dto.VmDto;
import gmc.rd.report.dto.VmDtoMapper;
import gmc.rd.report.entity.Report;
import gmc.rd.report.entity.Vm;
import gmc.rd.report.repository.ReportRepository;
import gmc.rd.report.repository.VmRepository;



@Service
public class VmServiceImpl implements VmService{

	@Autowired
	private VmRepository vmRepository;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private BithumbApiService bithumbApiService;
	
	@Autowired
	private CoinoneApiService coinoneApiService;
	
	@Autowired
	private UpbitApiService upbitApiService;
	
	@Autowired
	private ReportService reportService;
	
	public VmServiceImpl(VmRepository vmRepository) {
		this.vmRepository = vmRepository;
	}


	@Override
	public List<VmDto> getVm() {
		//System.out.println(vmRepository.findAll());
		List<Vm> vm = vmRepository.findAll();
		List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
		
		return vmDto;
	}
	
	@Override
	public List<Report> getVmOne(String siteName) throws Exception{
		// System.out.println(vmRepository.findAll());
		List<Vm> vm = vmRepository.findBySiteName(siteName);
		List<VmDto> vmDto = VmDtoMapper.INSTANCE.ToDto(vm);
		
		return reportService.selectReport(siteName, vmDto);
	}

	@Override
	public VmDto getVmOne2(String siteName,String vmId){
		// System.out.println(vmRepository.findAll());
		Vm vm = vmRepository.findBySiteNameAndVmId(siteName,vmId);
		VmDto vmDto = VmDtoMapper.INSTANCE.ToDto2(vm);

		return vmDto;
	}
}
