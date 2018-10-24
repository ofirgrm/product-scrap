package com.sainsburys.productscrap.services.v1;

import com.sainsburys.productscrap.common.HtmlDocument;
import com.sainsburys.productscrap.domain.Product;
import com.sainsburys.productscrap.domain.ProductResult;
import com.sainsburys.productscrap.domain.ProductTotals;
import com.sainsburys.productscrap.exceptions.ProductPageException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class ProductServiceImpl implements ProductService {

    protected HtmlDocument htmlDocument;

    public ProductServiceImpl(HtmlDocument htmlDocument) {
        this.htmlDocument = htmlDocument;
    }

    protected void scrapProducts(ProductResult productResult) {
        Document doc = this.htmlDocument.readUrl(getProductUrl());
        List<Product> productList = new ArrayList<>();
        productResult.setProducts(productList);

        Elements productElements = doc.getElementsByClass("product");
        for (Element productElement : productElements) {
            Product product = createProduct();
            scrapProduct(productElement, product);
            productList.add(product);
        }

        scrapTotals(productResult);
    }

    protected void scrapTotals(ProductResult productResult) {
        if (!productResult.getProducts().isEmpty()) {
            ProductTotals productTotals = new ProductTotals();
            BigDecimal gross = productResult.getProducts().stream()
                    .map(product -> product.getUnitPrice())
                    .reduce(BigDecimal::add).get().setScale(2, RoundingMode.HALF_UP);
            BigDecimal vat = gross.divide(BigDecimal.valueOf(6), 2, RoundingMode.HALF_UP);

            productTotals.setGross(gross);
            productTotals.setVat(vat);

            productResult.setTotals(productTotals);
        }
    }

    protected void scrapProduct(Element productElement, Product product) {
        Element productLink = scrapProductTitle(productElement, product);
        scrapProductLink(productLink, product);
        scrapProductPrice(productElement, product);
    }

    protected Element scrapProductLink(Element productLink, Product product) {
        if (productLink != null) {
            String url = productLink.attr("href");
            url = convertRelativeUrlToAbsoluteUrl(url);
            Document doc = this.htmlDocument.readUrl(url);

            Element sectionElement = doc
                    .getElementsByClass("section").first();

            scrapProductDescription(sectionElement, product);

            return sectionElement;
        }

        return null;
    }

    protected String convertRelativeUrlToAbsoluteUrl(String relativeUrl) {
        try {
            URL url = new URL(new URL(getProductUrl()), relativeUrl);
            return url.toString();

        } catch (MalformedURLException ex) {
            throw new ProductPageException("Invalid url: " + relativeUrl, ex);
        }
    }

    protected void scrapProductDescription(Element sectionElement, Product product) {
        if (sectionElement != null) {
            String description = sectionElement.getElementsByTag("h3")
                    .stream()
                    .filter(element -> element.text().equals("Description"))
                    .map(element -> element.nextElementSibling()
                            .getElementsByTag("p")
                            .stream()
                            .filter(pElement -> !pElement.text().isEmpty())
                            .findFirst().orElse(null)
                            .text())
                    .findFirst()
                    .orElseThrow(() -> new ProductPageException("Description not found"));

            product.setDescription(description);
        }
    }

    protected Element scrapProductTitle(Element productElement, Product product) {
        Element productInfoElement = productElement.getElementsByClass("productInfo").first();
        if (productInfoElement != null) {
            Element nameLinkElement = productInfoElement
                    .getElementsByTag("h3").first()
                    .getElementsByTag("a").first();
            product.setTitle(nameLinkElement.text());
            return nameLinkElement;
        }

        return null;
    }

    protected Element scrapProductPrice(Element productElement, Product product) {
        Element pricePerUnitElement = productElement.getElementsByClass("pricePerUnit").first();
        if (pricePerUnitElement != null) {
            String price = pricePerUnitElement.text();
            String priceStr = price.substring(1, price.indexOf("/"));
            product.setUnitPrice(new BigDecimal(priceStr));

            return pricePerUnitElement;
        }
        return null;
    }

    protected abstract Product createProduct();

    protected abstract String getProductUrl();

}
