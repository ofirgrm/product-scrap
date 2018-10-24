package com.sainsburys.productscrap.api.v1.mapper;

import com.sainsburys.productscrap.api.v1.model.CurrantsDTO;
import com.sainsburys.productscrap.api.v1.model.ProductResultDTO;
import com.sainsburys.productscrap.domain.Currants;
import com.sainsburys.productscrap.domain.Product;
import com.sainsburys.productscrap.domain.ProductResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrantsListMapper {

    CurrantsListMapper INSTANCE = Mappers.getMapper(CurrantsListMapper.class);

    @Mapping(source = "products", target = "results")
    ProductResultDTO productResultToProductResultDTO(ProductResult productResult);

    default CurrantsDTO map(Product product) {
        Currants currants = (Currants) product;
        return CurrantsMapper.INSTANCE.currantsToCurrantsDTO(currants);
    }

}
