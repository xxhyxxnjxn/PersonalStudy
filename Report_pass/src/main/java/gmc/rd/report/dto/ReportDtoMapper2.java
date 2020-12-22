package gmc.rd.report.dto;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import gmc.rd.report.entity.Report2;


@Mapper
public interface ReportDtoMapper2 {
	ReportDtoMapper2 INSTANCE = Mappers.getMapper(ReportDtoMapper2.class);

	List<ReportDto2> toDto(List<Report2> report);

	
	List<Report2> toEntity(List<ReportDto2> reportDto);
	
}
