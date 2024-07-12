package ru.practicum.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exceptions.DataNotFoundException;
import ru.practicum.exceptions.WrongConditionException;
import ru.practicum.helper.UtilsUpdateWithoutNull;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.models.Category;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.services.admin_api.AdminCategoriesService;

@Service
@RequiredArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(CategoryMapper.mapToCategory(newCategoryDto)));
    }

    @Override
    public void delete(Long catId) {
        findCategoryById(catId);

        if (!eventRepository.findAllByCategory_Id(catId).isEmpty()) {
            throw new WrongConditionException("Категория не пуста.");
        }

        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        Category category = findCategoryById(catId);
        UtilsUpdateWithoutNull.copyProperties(categoryDto, category);
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    private Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Категория с id=" + catId + " не найдена."));
    }
}
