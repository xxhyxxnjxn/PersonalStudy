package gmc.rd.report.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //User클래스가 MySQL에 테이블이 생성이 된다.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder//빌더 패턴!!
public class CoinoneState {

   @Id  //primaryKey
   @Column(nullable=false, length=50)
   private String site;
   @Column(nullable=false, length=50) 
   private boolean state;// 인증키가 끼어듦 = true 평상시 false
   
   
   
}