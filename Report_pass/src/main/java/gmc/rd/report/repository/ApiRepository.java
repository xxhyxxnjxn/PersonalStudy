package gmc.rd.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.Api;

public interface ApiRepository extends JpaRepository<Api, Integer> {

	List<Api> findBymemId(String memId);

	Api findByMemIdAndSite(String memId, String site);

	List<Api> findByMemId(String memId);

	

}
