package com.minhthuan.web_store.domain.store.address;



import com.minhthuan.web_store.domain.store.address.dto.AddressRequest;
import com.minhthuan.web_store.domain.store.address.dto.AddressResponse;
import com.minhthuan.web_store.domain.store.address.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest addressRequest);
    AddressResponse toAddressResponse(Address address);
    List<AddressResponse> toAddressResponseList(List<Address> addressList);
    void updateAddress(@MappingTarget Address address, AddressRequest addressRequest);
}
