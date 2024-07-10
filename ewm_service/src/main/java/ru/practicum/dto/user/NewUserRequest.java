package ru.practicum.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Size(min = 6, max = 254)
    @Email(message = "некорректный формат email.")
    @NotBlank(message = "email не может быть пустым.")
    private String email;
    @Size(min = 2, max = 250)
    @NotBlank(message = "имя не может быть пустым.")
    private String name;
}