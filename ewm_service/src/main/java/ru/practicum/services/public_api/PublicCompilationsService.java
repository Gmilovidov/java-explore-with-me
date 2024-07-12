package ru.practicum.services.public_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Long compId);
}
