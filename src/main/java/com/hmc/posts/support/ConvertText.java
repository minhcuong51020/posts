package com.hmc.posts.support;

import io.github.furstenheim.CopyDown;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

@Component
public class ConvertText {

    private final CopyDown converter = new CopyDown();

    public String htmlToMarkDown(String html) {
        return converter.convert(html);
    }

    public String convertHtmlToText(String html) {
        return Jsoup.parse(html).wholeText();
    }

}
