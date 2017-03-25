package com.github.usmanovbf;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Crawler extends WebCrawler {


    private final Pattern PROHIBITED_FILTERS = Pattern.compile( ".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz)).*$" );
    private String KPFU_URL = "^http\\:\\/\\/.*?" + "kpfu.ru" + "/.*";
    private String PROHIBITED_KPFU_URL = "^http\\:\\/\\/.*?" + "ku.kpfu.ru" + "/.*";


    @Override
    public boolean shouldVisit( Page referringPage, WebURL url ) {
        String href = url.getURL().toLowerCase();
        return !PROHIBITED_FILTERS.matcher( href ).matches()
                && href.matches( KPFU_URL )
                && !href.matches( PROHIBITED_KPFU_URL );
    }

    @Override
    public void visit( Page page ) {
        String url = page.getWebURL().getURL();
        logger.log( uk.org.lidalia.slf4jext.Level.INFO, "Host visited times: " + AppConfig.incrementCountOfVisited() + ", on " + url );

        WebSite webSite = (WebSite) myController.getCustomData();
        // Получаем матрицу смежности веб-страниц

        HashMap<WebPage, HashMap<WebPage, Boolean>> adjacencyMatrix = webSite.getAdjacencyMatrix();
        WebPage sourceWebPage = new WebPage( url );

        // Если посетили новую страницу
        if (!adjacencyMatrix.containsKey( sourceWebPage ))
            // То помещаем ее в качестве нового источника, откуда будут доступны другие страницы
            adjacencyMatrix.put( sourceWebPage, new HashMap<WebPage, Boolean>() );
        // Сама страница доступна для себя
        adjacencyMatrix.get( sourceWebPage ).put( sourceWebPage, Boolean.TRUE );

        // Для каждой страницы, на которую можно перейти от текущей
        for (WebURL outgoingUrl : page.getParseData().getOutgoingUrls()) {
            // Если она попадает под условия
            if (outgoingUrl.getURL().matches( KPFU_URL ) && !outgoingUrl.getURL().matches( PROHIBITED_KPFU_URL ) && !PROHIBITED_FILTERS.matcher( outgoingUrl.getURL() ).matches()) {
                // Помещаем ее в список доступных от текущей страницы-источника
                adjacencyMatrix.get( sourceWebPage ).put( new WebPage( outgoingUrl.getURL() ), Boolean.TRUE );
                // И кладем в список новых страниц источников, если такой еще не было
                adjacencyMatrix.put( new WebPage( outgoingUrl.getURL() ), new HashMap<WebPage, Boolean>() );
            }
        }

    }
}
