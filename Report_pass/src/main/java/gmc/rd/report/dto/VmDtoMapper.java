package gmc.rd.report.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import gmc.rd.report.entity.Vm;

@Mapper
public interface VmDtoMapper {
	VmDtoMapper INSTANCE = Mappers.getMapper(VmDtoMapper.class);

	List<VmDto> ToDto(List<Vm> vm);
	
	VmDto ToDto2(Vm vm);
}
