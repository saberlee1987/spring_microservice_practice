package com.saber.productservice.services.impl;

import com.saber.productservice.dto.ProductRequestDto;
import com.saber.productservice.mapper.ProductMapper;
import com.saber.productservice.model.Product;
import com.saber.productservice.repositories.ProductRepository;
import com.saber.productservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Override
    @Transactional
    public Product save(ProductRequestDto productRequest) {
        Product product = productMapper.requestToModel(productRequest);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
