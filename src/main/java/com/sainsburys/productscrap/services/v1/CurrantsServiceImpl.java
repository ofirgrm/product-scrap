package com.sainsburys.productscrap.services.v1;

import com.sainsburys.productscrap.api.v1.mapper.CurrantsListMapper;
import com.sainsburys.productscrap.api.v1.model.ProductResultDTO;
import com.sainsburys.productscrap.common.HtmlDocument;
import com.sainsburys.productscrap.domain.Currants;
import com.sainsburys.productscrap.domain.Product;
import com.sainsburys.productscrap.domain.ProductResult;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class CurrantsServiceImpl
        extends ProductServiceImpl
        implements CurrantsService {

    @Value("${com.sainsbury.products.currants}")
    private String CURRANTS_URL;

    public CurrantsServiceImpl(HtmlDocument htmlDocument) {
        super(htmlDocument);
    }

    @Override
    public ProductResultDTO getProductList() {
        ProductResult productList = new ProductResult();
        scrapProducts(productList);

        return CurrantsListMapper.INSTANCE.productResultToProductResultDTO(productList);
    }

    @Override
    protected Element scrapProductLink(Element productLink, Product product) {
        Element sectionElement = super.scrapProductLink(productLink, product);

        scrapProductCalories(sectionElement, product);

        return sectionElement;
    }

    protected void scrapProductCalories(Element sectionElement, Product product) {
        Integer caloriesInt = null;

        if (sectionElement == null || sectionElement.getElementsByClass("nutritionTable").first() == null) {
            ((Currants) product).setCalPer100gr(null);
            return;
        }

        String calories = prepareCaloriesStream(sectionElement, "Energy")
                .map(element -> {
                    Element calorieElement = element.nextElementSibling().getElementsByTag("td").first();
                    return calorieElement.text();
                }).findFirst()
                .orElse(null);

        // second try
        if (calories == null) {
            calories = prepareCaloriesStream(sectionElement, "Energy kcal")
                    .map(element -> {
                        Element calorieElement = element.getElementsByTag("td").first();
                        return calorieElement.text();
                    }).findFirst()
                    .orElse(null);
            if (calories != null) {
                caloriesInt = Integer.valueOf(calories);
            }
        } else {
            caloriesInt = Integer.valueOf(calories.substring(0, calories.indexOf("kcal")));
        }

//        if (calories == null) {
//            return;
//        }

        ((Currants) product).setCalPer100gr(caloriesInt);
    }

    private Stream<Element> prepareCaloriesStream(Element sectionElement, String text) {
        return sectionElement
                .getElementsByClass("nutritionTable").first()
                .getElementsByTag("tbody").first()
                .getElementsByTag("tr")
                .stream()
                .filter(element -> {
                    Element th = element.getElementsByTag("th").first();
                    return th.text().equals(text);
                });
    }


    @Override
    protected Product createProduct() {
        return new Currants();
    }

    @Override
    protected String getProductUrl() {
        return CURRANTS_URL;
    }

    public void setProductUrl(String OVERRIDE_CURRANTS_URL) {
        this.CURRANTS_URL = OVERRIDE_CURRANTS_URL;
    }

}
