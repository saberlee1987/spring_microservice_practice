package com.saber.productservice.services;

import com.saber.productservice.dto.ProductRequestDto;
import com.saber.productservice.model.Product;

import java.util.List;

public interface ProductService {

    Product save(ProductRequestDto productRequest);
    List<Product> getAllProducts();
}
