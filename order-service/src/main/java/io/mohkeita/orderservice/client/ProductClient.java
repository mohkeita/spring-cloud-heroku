package io.mohkeita.orderservice.client;

import io.mohkeita.orderservice.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductClient {

    @PostMapping("/product/ids")
    List<Product> findByIds(List<String> ids);
}
