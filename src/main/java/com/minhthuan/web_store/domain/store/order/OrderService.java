package com.minhthuan.web_store.domain.store.order;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.cart_item.CartItemRepository;
import com.minhthuan.web_store.domain.store.cart_item.entity.CartItem;
import com.minhthuan.web_store.domain.store.order.OrderMapper;
import com.minhthuan.web_store.domain.store.order.OrderRepository;
import com.minhthuan.web_store.domain.store.order.dto.OrderRequest;
import com.minhthuan.web_store.domain.store.order.dto.OrderResponse;
import com.minhthuan.web_store.domain.store.order.entity.Order;
import com.minhthuan.web_store.domain.store.order.entity.OrderStatus;
import com.minhthuan.web_store.domain.store.order.entity.PaymentStatus;
import com.minhthuan.web_store.domain.store.order_item.entity.OrderItem;
import com.minhthuan.web_store.domain.store.product.ProductRepository;
import com.minhthuan.web_store.domain.store.product.entity.Product;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final JwtUtil jwtUtil;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public ApiResponse<OrderResponse> createOrder(OrderRequest orderRequest) {
        if (orderRequest == null)
            throw new CustomException(ErrorCode.NOT_NULL);
        User user = jwtUtil.getCurrentUser();

        Order order = orderMapper.toOrder(orderRequest);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setCreateAt(LocalDate.now());

        List<CartItem> cartItems = cartItemRepository.findByIdIn(orderRequest.getIds());
        Map<Long, Integer> mapIds = new HashMap<>();
        List<OrderItem> orderItems = new ArrayList<>();

        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            totalPrice += orderItem.getPrice();
            orderItems.add(orderItem);
            mapIds.merge(cartItem.getProduct().getId(), cartItem.getQuantity(), Integer::sum);
        }
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        mapIds.forEach((id, quantity) -> {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
            int soldCount = product.getSoldCount() + quantity;
            product.setSoldCount(soldCount);
            productRepository.save(product);
        });

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        return new ApiResponse<>(orderResponse, "Create order successfully");
    }

    public ApiResponse<List<OrderResponse>> getMyOrders() {
        User user = jwtUtil.getCurrentUser();
        List<Order> orders = orderRepository.findAllByUserIdOrderByCreateAtDesc(user.getId());

        // Eagerly init items & product images
        orders.forEach(order -> {
            if (order.getOrderItems() != null) {
                order.getOrderItems().forEach(item -> {
                    if (item.getProduct() != null && item.getProduct().getImages() != null) {
                        item.getProduct().getImages().size();
                    }
                });
            }
        });

        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
        return new ApiResponse<>(responses, "Get orders successfully");
    }

}
