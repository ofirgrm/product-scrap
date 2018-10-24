package com.sainsburys.productscrap.api.v1.mapper;

import com.sainsburys.productscrap.api.v1.model.CurrantsDTO;
import com.sainsburys.productscrap.domain.Currants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrantsMapper {

    CurrantsMapper INSTANCE = Mappers.getMapper(CurrantsMapper.class);

    @Mapping(source = "calPer100gr", target = "kcal_per_100g")
    @Mapping(source = "unitPrice", target = "unit_price")
    CurrantsDTO currantsToCurrantsDTO(Currants currants);

}
