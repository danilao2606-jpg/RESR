package ru.kata.spring.boot_security.demo.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toResponseDTO(User user); //Для отправки клиенту

    User toEntity(UserRequestDTO userRequestDTO);  //Для сохранения в БД

    void updateUserController(UserRequestDTO dto, @MappingTarget User user); //Обновление User в БД

}
