package ru.practicum.services.admin_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface AdminUsersService {
    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    UserDto create(NewUserRequest newUserRequest);

    void delete(Long userId);
}