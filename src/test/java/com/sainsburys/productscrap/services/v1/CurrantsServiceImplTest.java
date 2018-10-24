package com.sainsburys.productscrap.services.v1;

import com.sainsburys.productscrap.api.v1.model.ProductResultDTO;
import com.sainsburys.productscrap.common.HtmlDocument;
import com.sainsburys.productscrap.domain.Currants;
import com.sainsburys.productscrap.domain.ProductResult;
import com.sainsburys.productscrap.exceptions.ProductPageException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CurrantsServiceImplTest {

    private static final String WEBPAGE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
    private static final String RELATIVE_URL = "../../../../../../shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
    private static final String FULL_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";

    CurrantsServiceImpl currantsService;

    @Mock
    HtmlDocument htmlDocument;
    @Mock
    Document doc;
    @Mock
    Element element1;
    @Mock
    Element element2;
    @Mock
    Element element3;
    @Mock
    Element element4;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        currantsService = new CurrantsServiceImpl(htmlDocument);
        currantsService.setProductUrl(WEBPAGE_URL);
    }

    @Test
    public void scrapProducts() {
        ProductResult productResult = new ProductResult();
        Elements productElements = new Elements();
        productElements.addAll(Arrays.asList(
                element1, element2, element3
        ));
        given(htmlDocument.readUrl(anyString())).willReturn(doc);
        given(doc.getElementsByClass(anyString())).willReturn(productElements);
        given(element1.getElementsByClass(anyString())).willReturn(new Elements());
        given(element2.getElementsByClass(anyString())).willReturn(new Elements());
        given(element3.getElementsByClass(anyString())).willReturn(new Elements());

        currantsService.scrapProducts(productResult);

        assertThat(productResult.getProducts().size(), is(3));
        verify(htmlDocument, times(1)).readUrl(anyString());
    }

    @Test
    public void scrapTotals() {
        ProductResult productResult = new ProductResult();
        productResult.setProducts(Arrays.asList(
                new Currants("title1", "description1", BigDecimal.valueOf(1.5), 100),
                new Currants("title2", "description2", BigDecimal.valueOf(5.5), 200),
                new Currants("title3", "description3", BigDecimal.valueOf(3), 300)
        ));

        currantsService.scrapTotals(productResult);

        assertThat(productResult.getTotals().getGross(), is(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP)));
        assertThat(productResult.getTotals().getVat(), is(BigDecimal.valueOf(1.67)));
    }

    @Test
    public void scrapProduct() {
        given(element1.getElementsByClass(anyString())).willReturn(new Elements());

        currantsService.scrapProduct(element1, new Currants());

        verify(element1, times(2)).getElementsByClass(anyString());
    }

    @Test
    public void scrapProductLink() {
        Elements productElements = new Elements();

        given(element1.attr("href")).willReturn(RELATIVE_URL);
        given(htmlDocument.readUrl(anyString())).willReturn(doc);
        given(doc.getElementsByClass("section")).willReturn(productElements);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        currantsService.scrapProductLink(element1, new Currants());

        verify(htmlDocument, times(1)).readUrl(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(),
                is(FULL_URL));
    }

    @Test(expected = ProductPageException.class)
    public void convertRelativeUrlToAbsoluteUrl() {
        currantsService.setProductUrl("dummyUrl");
        currantsService.convertRelativeUrlToAbsoluteUrl("dummyRelativeUrl");
    }

    @Test
    public void scrapProductDescription() {
        String description = "This is a test description";
        Elements productElements1 = new Elements();
        productElements1.addAll(Arrays.asList(element2));
        Elements productElements2 = new Elements();
        productElements2.addAll(Arrays.asList(element4));
        Currants product = new Currants();

        given(element1.getElementsByTag("h3")).willReturn(productElements1);
        given(element2.text()).willReturn("Description");
        given(element2.nextElementSibling()).willReturn(element3);
        given(element3.getElementsByTag("p")).willReturn(productElements2);
        given(element4.text()).willReturn(description);

        currantsService.scrapProductDescription(element1, product);

        assertThat(product.getDescription(), is(description));
    }

    @Test(expected = ProductPageException.class)
    public void scrapProductDescriptionNotFound() {
        Elements productElements1 = new Elements();
        productElements1.addAll(Arrays.asList(element2));

        given(element1.getElementsByTag("h3")).willReturn(productElements1);
        given(element2.text()).willReturn("");

        Currants product = new Currants();

        currantsService.scrapProductDescription(element1, product);
    }

    @Test
    public void scrapProductTitle() {
        String title = "This is a test title";
        Elements productElements = new Elements();
        productElements.addAll(Arrays.asList(element1));
        Currants product = new Currants();

        given(element1.getElementsByClass("productInfo")).willReturn(productElements);
        given(element1.getElementsByTag(anyString())).willReturn(productElements);
        given(element1.text()).willReturn(title);

        currantsService.scrapProductTitle(element1, product);

        assertThat(product.getTitle(), is(title));
    }

    @Test
    public void scrapProductPrice() {
        BigDecimal price = BigDecimal.valueOf(12.3);
        Elements productElements = new Elements();
        productElements.addAll(Arrays.asList(element1));
        Currants product = new Currants();

        given(element1.getElementsByClass("pricePerUnit")).willReturn(productElements);
        given(element1.text()).willReturn("£12.3/unit");

        currantsService.scrapProductPrice(element1, product);

        assertThat(product.getUnitPrice(), is(price));
    }

    @Test
    public void scrapProductCaloriesMethod1() {
        int calories = 123;
        Elements productElements1 = new Elements();
        productElements1.addAll(Arrays.asList(element1));
        Elements productElements2 = new Elements();
        productElements2.addAll(Arrays.asList(element2));
        Currants product = new Currants();

        given(element1.getElementsByClass("nutritionTable")).willReturn(productElements1);
        given(element1.getElementsByTag(anyString())).willReturn(productElements1);
        given(element1.text()).willReturn("Energy");
        given(element1.nextElementSibling()).willReturn(element2);
        given(element2.getElementsByTag(anyString())).willReturn(productElements2);
        given(element2.text()).willReturn("123kcal");

        currantsService.scrapProductCalories(element1, product);

        assertThat(product.getCalPer100gr(), is(calories));
    }

    @Test
    public void scrapProductCaloriesMethod2() {
        int calories = 123;
        Elements productElements1 = new Elements();
        productElements1.addAll(Arrays.asList(element1));
        Elements productElements2 = new Elements();
        productElements2.addAll(Arrays.asList(element2));
        Currants product = new Currants();

        given(element1.getElementsByClass("nutritionTable")).willReturn(productElements1);
        given(element1.getElementsByTag(not(eq("td")))).willReturn(productElements1);
        given(element1.getElementsByTag("td")).willReturn(productElements2);
        given(element1.text()).willReturn("Energy kcal");
        given(element1.nextElementSibling()).willReturn(element2);
        given(element2.text()).willReturn("123");

        currantsService.scrapProductCalories(element1, product);

        assertThat(product.getCalPer100gr(), is(calories));
    }

    @Test
    public void scrapProductCaloriesMethodNotFound1() {
        Currants product = new Currants();

        currantsService.scrapProductCalories(null, product);

        assertNull(product.getCalPer100gr());
    }

    @Test
    public void scrapProductCaloriesMethodNotFound2() {
        Currants product = new Currants();

        given(element1.getElementsByClass("nutritionTable")).willReturn(new Elements());

        currantsService.scrapProductCalories(element1, product);

        assertNull(product.getCalPer100gr());
    }

    @Test
    public void getProductList() {
        given(htmlDocument.readUrl(anyString())).willReturn(doc);
        given(doc.getElementsByClass(anyString())).willReturn(new Elements());

        ProductResultDTO productResultDTO = currantsService.getProductList();

        verify(htmlDocument, times(1)).readUrl(anyString());
        assertThat(productResultDTO.getResults().size(), is(0));
    }

}