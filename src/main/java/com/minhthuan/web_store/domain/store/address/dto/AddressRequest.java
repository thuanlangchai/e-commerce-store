package com.minhthuan.web_store.domain.store.address.dto;


import com.minhthuan.web_store.common.excpetion.ErrorCode;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class AddressRequest {
    private String streetName;
    @Size(min = 10, max = 10, message = "PHONE_INVALID")
    private String phone;
    private String city;
    private String district;
    private String ward;
}
