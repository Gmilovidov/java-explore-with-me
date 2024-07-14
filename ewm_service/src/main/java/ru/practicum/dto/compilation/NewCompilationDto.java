package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events = new ArrayList<>();
    private Boolean pinned = false;
    @Size(min = 1, max = 50)
    @NotBlank(message = "заголовок не может быть пустым")
    private String title;
}
