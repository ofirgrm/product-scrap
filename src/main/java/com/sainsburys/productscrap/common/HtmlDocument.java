package com.sainsburys.productscrap.common;

import com.sainsburys.productscrap.exceptions.ProductPageException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HtmlDocument {

    public Document readUrl(String url) {
        Document doc;

        try {
            doc = Jsoup.connect(url).get();
            // JSoup bug. Sometimes the HTML is returned empty
            // and the issue resolves after calling it again.
            if (doc.body().text().isEmpty()){
                doc = Jsoup.connect(url).get();
            }
        } catch (IOException ex) {
            throw new ProductPageException(ex);
        }

        return doc;
    }

}
