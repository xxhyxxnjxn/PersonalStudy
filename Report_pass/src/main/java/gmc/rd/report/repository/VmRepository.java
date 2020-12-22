package gmc.rd.report.repository;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gmc.rd.report.entity.Api;
import gmc.rd.report.entity.Vm;

public interface VmRepository extends JpaRepository<Vm, Integer>{

	@Query(value = "select * from vm_tbl where siteName=:siteName", nativeQuery = true)
	public List<Vm> findAllByVm(String siteName);
	
	@Query(value = "select * from vm_tbl where siteName=:siteName and vmId =:vmId", nativeQuery = true)
	public Vm findByVm(String siteName,String vmId);
	
	List<Vm> findBySiteName(String siteName);
	
	Vm findBySiteNameAndVmId(String siteName,String vmId);
	
}
