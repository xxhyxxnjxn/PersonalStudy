package gmc.rd.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import gmc.rd.report.dto.VmDto;
import gmc.rd.report.entity.Vm;
import gmc.rd.report.repository.VmRepository;
import gmc.rd.report.service.VmService;


@EnableScheduling
@Controller
public class TransactionController {

	@Autowired
	VmRepository vmRepository;
	
	@Autowired
	VmService vmService;
	
	// http://localhost:8080/vm
//	@GetMapping("/vm")
//	public List<VmDto> getAllVm(){
//		List<VmDto> vmDto = vmService.getVm();
//		return vmDto;
//	}
		
	@Scheduled(fixedDelay = 20000)
	//@GetMapping("/vm")
	public void getAllVm() throws Exception{

		String[] site_name = { /* "bithumb","coinone", */"upbit"};
		//String site_name = "upbit";
		for(int i = 0 ; i<site_name.length;i++) {
		vmService.getVmOne(site_name[i]);
		}
		System.out.println("test");
		
	}
	
	
	@Scheduled(fixedDelay = 20000)
	//@GetMapping("/vm")
	public void getAllVm2() throws Exception{

		String[] site_name = {  "bithumb"/*,"coinone", "upbit"*/};
		//String site_name = "upbit";
		for(int i = 0 ; i<site_name.length;i++) {
		vmService.getVmOne(site_name[i]);
		}
		System.out.println("test");
		
	}
	
	@Scheduled(fixedDelay = 20000)
//	@GetMapping("/vm")
	public void getAllVm3() throws Exception{

		String[] site_name = { /* "bithumb", */"coinone"/* , "upbit" */};
		//String site_name = "upbit";
		for(int i = 0 ; i<site_name.length;i++) {
		vmService.getVmOne(site_name[i]);
		}
		System.out.println("test");
		
	}
	
	
	
	// http://localhost:8080/vm
//	@GetMapping("/vm/{site_name}")
//	public List<Vm> getVm(@PathVariable String site_name) {
//		vmRepository.findAllByVm(site_name);
//		
//		return vmRepository.findAllByVm(site_name);
//	}
}
