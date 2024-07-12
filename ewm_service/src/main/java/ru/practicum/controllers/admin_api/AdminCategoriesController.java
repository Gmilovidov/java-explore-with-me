package ru.practicum.controllers.admin_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.services.admin_api.AdminCategoriesService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(adminCategoriesService.create(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        adminCategoriesService.delete(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{catId}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long catId,
                              @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok().body(adminCategoriesService.update(catId, categoryDto));
    }
}
