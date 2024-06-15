package com.saber.productservice.mapper;

import com.saber.productservice.dto.ProductRequestDto;
import com.saber.productservice.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    Product requestToModel(ProductRequestDto productRequest);
}
