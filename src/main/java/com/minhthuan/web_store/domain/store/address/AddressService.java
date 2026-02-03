package com.minhthuan.web_store.domain.store.address;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.common.utils.JwtUtil;
import com.minhthuan.web_store.domain.store.address.dto.AddressRequest;
import com.minhthuan.web_store.domain.store.address.dto.AddressResponse;
import com.minhthuan.web_store.domain.store.address.entity.Address;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final JwtUtil jwtUtil;

    public ApiResponse<AddressResponse> createAddress(AddressRequest addressRequest) {
        if (addressRequest == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        User user = jwtUtil.getCurrentUser();

        Address address = addressMapper.toAddress(addressRequest);
        address.setUser(user);
        addressRepository.save(address);
        AddressResponse addressResponse = addressMapper.toAddressResponse(address);
        return new ApiResponse<>(addressResponse, "Create Successfully");
    }

    public ApiResponse<AddressResponse> updateAddress(Long id, AddressRequest addressRequest) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        addressMapper.updateAddress(address, addressRequest);
        addressRepository.save(address);
        AddressResponse addressResponse = addressMapper.toAddressResponse(address);
        return new ApiResponse<AddressResponse>(addressResponse, "Update Successfully");
    }

    public ApiResponse<AddressResponse> deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        addressRepository.delete(address);
        AddressResponse addressResponse = addressMapper.toAddressResponse(address);
        return new ApiResponse<>(addressResponse, "Delete Successfully");
    }

    public ApiResponse<List<AddressResponse>> findAllAddress() {
        List<Address> addressList = addressRepository.findAll();
        List<AddressResponse> addressResponseList = addressMapper.toAddressResponseList(addressList);
        return new ApiResponse<>(addressResponseList, "Find All Successfully");
    }

    public ApiResponse<List<AddressResponse>> findAddressesByUserId(Long userId) {
        List<Address> addressList = addressRepository.findByUserId(userId);
        List<AddressResponse> addressResponseList = addressMapper.toAddressResponseList(addressList);
        return new ApiResponse<>(addressResponseList, "Find Addresses by User ID Successfully");
    }
}
