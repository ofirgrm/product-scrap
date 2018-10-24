package com.sainsburys.productscrap.controllers.v1;

import com.sainsburys.productscrap.api.v1.model.ProductResultDTO;
import com.sainsburys.productscrap.services.v1.CurrantsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currants")
public class CurrantsController {

    private CurrantsService currantsService;

    public CurrantsController(CurrantsService currantsService) {
        this.currantsService = currantsService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Scrap Sainsbury's Berries, Cherries, Currants & Currants web-page")
    @ApiResponses({@ApiResponse(code = 200, message = "OK", response = ProductResultDTO.class)})
    public ProductResultDTO getProductList() {
        return currantsService.getProductList();
    }

}
