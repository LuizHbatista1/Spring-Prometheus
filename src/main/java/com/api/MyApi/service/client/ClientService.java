package com.api.MyApi.service.client;

import com.api.MyApi.DTOS.ClientDTO;
import com.api.MyApi.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientFactory clientFactory;

    @Autowired
    public ClientService(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }


    public Client createClient(ClientDTO clientDTO){

        return clientFactory.createAndSaveClient(clientDTO);

    }


}
