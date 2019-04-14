package de.budget.project.controller;

import de.budget.project.exception.exceptions.CategoryTypeNotFoundException;
import de.budget.project.model.dao.CategoryDAO;
import de.budget.project.model.entites.Category;
import de.budget.project.model.types.CategoryType;
import de.budget.project.model.web.CategoryWebDto;
import de.budget.project.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/categories")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryWebDto createCategory(@RequestBody CategoryWebDto categoryWebDto) {
        return convertToDto(categoryService.createCategory(convertToEntity(categoryWebDto)));
    }

    @GetMapping("/categories/{id}")
    @ResponseBody
    public CategoryWebDto getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null){
            throw new CategoryTypeNotFoundException("Category by id: " + id + " is not found");
        } else
        return convertToDto(category);
    }

    @GetMapping("/categories/type/{type}")
    @ResponseBody
    public List<CategoryWebDto> getAllByCategoryType(@PathVariable CategoryType type) {
        List<Category> categories = categoryService.getAllByCategoryType(type);
        if(categories.isEmpty()){
            throw new CategoryTypeNotFoundException("There are no category items by categoryType " + type);
        }
        return categories
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryWebDto convertToDto(Category category) {
        return new CategoryWebDto(category.getName(), category.getCategoryType().getId());
    }

    private Category convertToEntity(CategoryWebDto categoryWebDto) {
        Category category = new Category();
        category.setName(categoryWebDto.getName());
        category.setCategoryType(CategoryType.findCategoryTypeId(categoryWebDto.getCategoryTypeId()));
        return category;
    }

    @GetMapping("/categories")
    public List<CategoryDAO> getAll(){
        return categoryService.getAllCategories();
    }
}