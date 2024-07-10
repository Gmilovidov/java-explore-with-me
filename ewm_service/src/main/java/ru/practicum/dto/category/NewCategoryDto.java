package ru.practicum.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @Size(min = 1)
    @Size(max = 50)
    @NotBlank(message = "Имя категории не может быть пустым")
    private String name;
}