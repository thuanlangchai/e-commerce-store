package com.minhthuan.web_store.domain.store.category;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.category.CategoryMapper;
import com.minhthuan.web_store.domain.store.category.CategoryRepository;
import com.minhthuan.web_store.domain.store.category.dto.CategoryRequest;
import com.minhthuan.web_store.domain.store.category.dto.CategoryResponse;
import com.minhthuan.web_store.domain.store.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public ApiResponse<CategoryResponse> createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new CustomException(ErrorCode.EXISTED);
        }
        Category category = categoryMapper.toCategory(categoryRequest);
        categoryRepository.save(category);
        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
        return new ApiResponse<>(categoryResponse, "Create Successfully");
    }

    public  ApiResponse<CategoryResponse> updateCategory(Long id, CategoryRequest categoryRequest) {
        if (id == null || !categoryRepository.existsById(id) || categoryRequest == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        categoryMapper.updateCategory(category, categoryRequest);
        categoryRepository.save(category);
        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
        return new ApiResponse<>(categoryResponse, "Update Successfully");
    }

    public  ApiResponse<CategoryResponse> deleteCategory(Long id) {
        if (id == null || !categoryRepository.existsById(id)) {
            throw  new CustomException(ErrorCode.NOT_FOUND);
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        categoryRepository.delete(category);
        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(category);
        return new ApiResponse<>(categoryResponse, "Delete Successfully");
    }

    public ApiResponse<List<CategoryResponse>> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponseList = categoryMapper.toCategoryResponseList(categories);
        return new ApiResponse<>(categoryResponseList, "Find All Successfully");
    }
}
