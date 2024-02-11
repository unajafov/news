package az.najafov.deforestationnews.mapper;

import az.najafov.deforestationnews.dto.UserResponseDto;
import az.najafov.deforestationnews.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RegionMapper regionMapper;

    public UserResponseDto toResponseDto(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setUserName(user.getUsername());
        if (Objects.nonNull(user.getRegion())) {
            responseDto.setRegion(regionMapper.toResponseDto(user.getRegion()));
        }
        return responseDto;
    }

}
