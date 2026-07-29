package com.bookcorner.address.repository;

import com.bookcorner.address.entity.Address;
import com.bookcorner.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserOrderByCreatedAtDesc(User user);

    Optional<Address> findByUserAndDefaultAddressTrue(User user);

    Optional<Address> findByIdAndUser(Long id, User user);
}
