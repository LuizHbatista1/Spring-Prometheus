package com.api.MyApi.service;

import com.api.MyApi.DTOS.ClientDTO;
import com.api.MyApi.domain.Client;
import com.api.MyApi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientFactory implements ClientFactoryInterface {

    private ClientRepository clientRepository;

    @Autowired
    public ClientFactory(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createAndSaveClient(ClientDTO clientDTO) {
        Client newClient = new Client();
        newClient.setName(clientDTO.name());
        newClient.setAge(clientDTO.age());
        newClient.setGender(clientDTO.gender());
        return this.clientRepository.save(newClient);


    }
}
