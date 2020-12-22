package gmc.rd.report.dto;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import gmc.rd.report.entity.User;

@Mapper
public interface UserDtoMapper {
	UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

	UserDto toDto(User user);
	List<UserDto> toDtoList(List<User> vm);
}
