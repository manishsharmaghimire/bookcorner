package com.bookcorner.address.service;

import com.bookcorner.address.dto.AddressRequest;
import com.bookcorner.address.dto.AddressResponse;
import com.bookcorner.address.entity.Address;
import com.bookcorner.address.mapper.AddressMapper;
import com.bookcorner.address.repository.AddressRepository;
import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AuthenticationService authenticationService;

    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        var authenticatedUser = authenticationService.getAuthenticatedUser();
        Address address = this.addressMapper.toAddress(request);
        address.setUser(authenticatedUser);

        // If setting as default, unset existing default address
        if (Boolean.TRUE.equals(request.getDefaultAddress())) {
            addressRepository.findByUserAndDefaultAddressTrue(authenticatedUser)
                    .ifPresent(existingDefault -> {
                        existingDefault.setDefaultAddress(false);
                        addressRepository.save(existingDefault);
                    });
        }

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toAddressResponse(savedAddress);
    }

    public List<AddressResponse> getMyAddresses() {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        List<Address> addresses = addressRepository.findByUserOrderByCreatedAtDesc(authenticatedUser);
        return addresses.stream()
                .map(addressMapper::toAddressResponse)
                .toList();
    }

    public AddressResponse getAddressById(Long id) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        return addressMapper.toAddressResponse(address);
    }

    @Transactional
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        // If setting as default, unset existing default address
        if (Boolean.TRUE.equals(request.getDefaultAddress())) {
            addressRepository.findByUserAndDefaultAddressTrue(authenticatedUser)
                    .ifPresent(existingDefault -> {
                        if (!existingDefault.getId().equals(id)) {
                            existingDefault.setDefaultAddress(false);
                            addressRepository.save(existingDefault);
                        }
                    });
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setDefaultAddress(request.getDefaultAddress() != null ? request.getDefaultAddress() : false);

        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toAddressResponse(updatedAddress);
    }

    @Transactional
    public void deleteAddress(Long id) {
        User authenticatedUser = authenticationService.getAuthenticatedUser();
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        addressRepository.delete(address);
    }



    @Transactional
    public AddressResponse setDefaultAddress(Long id) {


        var user = authenticationService.getAuthenticatedUser();
        // 2. Find selected address
        Address address = addressRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Address not found.")
                );


        addressRepository.findByUserAndDefaultAddressTrue(user).ifPresent(existingDefault -> {

            existingDefault.setDefaultAddress(false);
            addressRepository.save(existingDefault);

        });
        address.setDefaultAddress(true);

        // 5. Save
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toAddressResponse(savedAddress);

    }
}
