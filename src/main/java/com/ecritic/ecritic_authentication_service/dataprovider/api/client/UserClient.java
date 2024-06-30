package com.ecritic.ecritic_authentication_service.dataprovider.api.client;

import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${clients.user.name}", url = "${clients.user.url}")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/info")
    UserEntity getUserAuthorizationInfo(@RequestParam("email") String email);
}
