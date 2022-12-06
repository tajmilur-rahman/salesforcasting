package com.sales.forecasting.data.service;

import com.sales.forecasting.data.entity.UsersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UsersDetails, UUID> {

    @Query("select u from UsersDetails u where u.userId=:userId")
    public UsersDetails findByUserId(@Param("userId") String userId);


}