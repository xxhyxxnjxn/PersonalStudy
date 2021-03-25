package gmc.rd.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gmc.rd.report.entity.CoinoneState;

public interface CoinoneStateRepository extends JpaRepository<CoinoneState, Integer>{

   @Query(value = "select * from coinoneState where site = 'coinone'", nativeQuery = true)
   CoinoneState findOne();


   
   
   
}