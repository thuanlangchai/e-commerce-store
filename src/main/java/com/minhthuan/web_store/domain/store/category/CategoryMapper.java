package com.minhthuan.web_store.domain.store.category;

import com.minhthuan.web_store.domain.store.category.dto.CategoryRequest;
import com.minhthuan.web_store.domain.store.category.dto.CategoryResponse;
import com.minhthuan.web_store.domain.store.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
    CategoryResponse toCategoryResponse(Category category);
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
    List<CategoryResponse> toCategoryResponseList(List<Category> categoryList);
}
