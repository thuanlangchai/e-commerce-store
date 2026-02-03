package com.minhthuan.web_store.domain.store.order.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequest {
    private Long addressId;
    private List<Long> ids;
}
