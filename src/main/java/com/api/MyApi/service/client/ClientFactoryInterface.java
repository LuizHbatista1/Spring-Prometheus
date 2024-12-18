package com.api.MyApi.service.client;

import com.api.MyApi.DTOS.ClientDTO;
import com.api.MyApi.domain.Client;

public interface ClientFactoryInterface {

    Client createAndSaveClient(ClientDTO clientDTO);


}
