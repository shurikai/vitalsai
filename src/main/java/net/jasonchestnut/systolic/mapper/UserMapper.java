package net.jasonchestnut.systolic.mapper;

import net.jasonchestnut.systolic.dto.UserResponse;
import net.jasonchestnut.systolic.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    User toEntity(UserResponse userResponse);
}
