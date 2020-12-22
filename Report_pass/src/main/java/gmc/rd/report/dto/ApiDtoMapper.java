package gmc.rd.report.dto;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import gmc.rd.report.entity.Api;

@Mapper
public interface ApiDtoMapper {
	ApiDtoMapper INSTANCE = Mappers.getMapper(ApiDtoMapper.class);

	ApiDto ToDto(Api api);

	List<ApiDto> ToDtoList(List<Api> api);
}
