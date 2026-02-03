package com.minhthuan.web_store.domain.store.cart;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.cart.CartMapper;
import com.minhthuan.web_store.domain.store.cart.CartRepository;
import com.minhthuan.web_store.domain.store.cart.dto.CartRequest;
import com.minhthuan.web_store.domain.store.cart.dto.CartResponse;
import com.minhthuan.web_store.domain.store.cart.entity.Cart;
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
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final JwtUtil jwtUtil;

    public ApiResponse<CartResponse> findCart() {
        User user = jwtUtil.getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        CartResponse cartResponse = cartMapper.toCartResponse(cart);
        return new ApiResponse<>(cartResponse, "Find cart successfully");
    }
}
