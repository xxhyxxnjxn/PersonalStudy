package gmc.rd.report.dto;


import java.util.List;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import gmc.rd.report.api.bithumb.vo.BithumbTransactionVo;
import gmc.rd.report.entity.Report;


@Mapper
public interface ReportDtoMapper {
	ReportDtoMapper INSTANCE = Mappers.getMapper(ReportDtoMapper.class);

	List<ReportDto> toDto(List<Report> report);

	
	List<Report> toEntity(List<ReportDto> reportDto);
	
}
