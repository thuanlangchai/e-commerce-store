package com.minhthuan.web_store.domain.store.address;

import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.address.dto.AddressRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final JwtUtil jwtUtil;

    @GetMapping("/user/address")
    public ResponseEntity<ApiResponse<?>> getAddress() {
        return ResponseEntity.ok(addressService.findAllAddress());
    }

    @GetMapping("/user/address/{id}")
    public ResponseEntity<ApiResponse<?>> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findAddressesByUserId(id));
    }

    @PostMapping("/user/create-address")
    public ResponseEntity<ApiResponse<?>> createAddress(@RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.ok(addressService.createAddress(addressRequest));
    }

    @PutMapping("/user/update-address/{id}")
    public ResponseEntity<ApiResponse<?>> updateAddress(@PathVariable Long id, @RequestBody @Valid AddressRequest addressRequest) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressRequest));
    }

    @DeleteMapping("/admin/delete-address/{id}")
    public  ResponseEntity<ApiResponse<?>> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }
}
