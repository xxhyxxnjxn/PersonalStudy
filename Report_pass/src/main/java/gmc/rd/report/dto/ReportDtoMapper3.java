package gmc.rd.report.dto;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import gmc.rd.report.entity.Report3;


@Mapper
public interface ReportDtoMapper3 {
	ReportDtoMapper3 INSTANCE = Mappers.getMapper(ReportDtoMapper3.class);

	List<ReportDto3> toDto(List<Report3> report);

	
	List<Report3> toEntity(List<ReportDto3> reportDto);
	
}
