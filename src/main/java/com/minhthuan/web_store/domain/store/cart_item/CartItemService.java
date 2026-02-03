package com.minhthuan.web_store.domain.store.cart_item;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.cart.CartRepository;
import com.minhthuan.web_store.domain.store.cart.entity.Cart;
import com.minhthuan.web_store.domain.store.cart_item.CartItemMapper;
import com.minhthuan.web_store.domain.store.cart_item.CartItemRepository;
import com.minhthuan.web_store.domain.store.cart_item.dto.CartItemRequest;
import com.minhthuan.web_store.domain.store.cart_item.dto.CartItemResponse;
import com.minhthuan.web_store.domain.store.cart_item.entity.CartItem;
import com.minhthuan.web_store.domain.store.product.ProductRepository;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.user.UserRepository;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public ApiResponse<CartItemResponse> createCartItem(CartItemRequest cartItemRequest) {
        if (cartItemRequest == null)
            throw new CustomException(ErrorCode.NOT_NULL);
        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        User user = jwtUtil.getCurrentUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        CartItem cartItem = cartItemMapper.toCartItem(cartItemRequest);
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        return new ApiResponse<>(cartItemResponse, "Create cart item successfully");
    }

    public ApiResponse<CartItemResponse> updateCartItem(Long id, CartItemRequest cartItemRequest) {
        if (cartItemRequest == null)
            throw new CustomException(ErrorCode.NOT_NULL);
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        cartItemMapper.updateCartItem(cartItem, cartItemRequest);
        cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        return new ApiResponse<>(cartItemResponse, "Update cart item successfully");
    }

    public  ApiResponse<CartItemResponse> deleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        cartItemRepository.delete(cartItem);
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        return new ApiResponse<>(cartItemResponse, "Delete cart item successfully");
    }

    public ApiResponse<List<CartItemResponse>> findAllCartItems() {
        User user = jwtUtil.getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        List<CartItemResponse> cartItemResponses = cartItemMapper.toCartItemResponseList(cartItems);
        return new ApiResponse<>(cartItemResponses, "Get cart items successfully");
    }
}
