package com.minhthuan.web_store.domain.store.category;


import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.category.CategoryService;
import com.minhthuan.web_store.domain.store.category.dto.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/user/categories")
    public ResponseEntity<ApiResponse<?>> getCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @PostMapping("/seller/create-category")
    public  ResponseEntity<ApiResponse<?>> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/seller/update-category/{id}")
    public  ResponseEntity<ApiResponse<?>> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest));
    }

    @DeleteMapping("/seller/delete-category/{id}")
    public   ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable Long id) {
        return  ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
