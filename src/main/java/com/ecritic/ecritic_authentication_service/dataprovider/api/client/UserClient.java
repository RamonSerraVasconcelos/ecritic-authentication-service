package com.ecritic.ecritic_authentication_service.dataprovider.api.client;

import com.ecritic.ecritic_authentication_service.dataprovider.api.config.FeignDefaultConfig;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${clients.user.name}", url = "${clients.user.url}", configuration = FeignDefaultConfig.class)
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/info")
    UserEntityResponse getUserAuthorizationInfo(@RequestParam(value = "email", required = false) String email,
                                                @RequestParam(value = "userId", required = false) String userId);

    @RequestMapping(method = RequestMethod.PUT, value = "/auth/users/external")
    UserEntityResponse upsertUser(@RequestBody UserRequest userRequest);
}
