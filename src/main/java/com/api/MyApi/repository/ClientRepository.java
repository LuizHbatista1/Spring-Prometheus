package com.api.MyApi.repository;


import com.api.MyApi.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client , Long> {




}
