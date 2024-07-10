package ru.practicum.services.public_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.services.public_api.PublicCategoriesService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoriesServiceImpl implements PublicCategoriesService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);

        if (categories.getContent().isEmpty()) {
            return new ArrayList<>();
        }

        return categories.getContent().stream()
                .map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        return CategoryMapper.mapToCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Категория с id=" + catId + " не найдена.")));
    }
}