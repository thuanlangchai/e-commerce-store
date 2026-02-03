package com.minhthuan.web_store.domain.store.address.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class AddressResponse {
    private Long id;
    private String streetName;
    private String phone;
    private String city;
    private String district;
    private String ward;
}
