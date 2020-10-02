package io.mohkeita.orderservice.client;

import io.mohkeita.orderservice.model.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/account")
    Account add(@RequestBody Account account);
    @PutMapping("/account/withdraw/{accountId}/{amount}")
    Account withdraw(@PathVariable("accountId") String id, @PathVariable("amount") int amount);
}
