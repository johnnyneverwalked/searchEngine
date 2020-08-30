/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author John Stamtsidis
 */
public class Crawler {
    
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; 008/0.83; http://www.80legs.com/webcrawler.html) Gecko/2008032620";
    private static final int MAX_PAGES = 10;
    private Set<String> pagesVisited;
    private List<String> pagesToVisit;
    
    public Crawler(){
        pagesVisited = new HashSet<>();
        pagesToVisit = new LinkedList<>();
    }
    
    private String nextUrl(){
                
        String nUrl;
        do{
            nUrl = pagesToVisit.remove(0);
            if(pagesToVisit.isEmpty())
                break;
        }while(pagesVisited.contains(nUrl));
        pagesVisited.add(nUrl);
        return nUrl;
    }
    
    public List<UrlNode> start(String url, int maxPages){
        
        List<UrlNode> crawledPages = new LinkedList<>();
        
        String currentUrl;
        int flag = 0;
        
        if(maxPages<1)
            maxPages=MAX_PAGES;
        
        while(pagesVisited.size()<maxPages){
            if(pagesToVisit.isEmpty()){
                currentUrl = url;
                pagesVisited.add(url);
                flag++;
            }else{
                currentUrl = nextUrl();
            }
            //System.out.println("Trying to access " + currentUrl);
            UrlNode values = crawl(currentUrl);
            if(values!=null)
                crawledPages.add(values);
            
            //check if the crawler is stuck
            if(flag>1000){
                System.out.println("The crawler got stuck for more than 1000 cycles. Stopping...");
                break;}
        }
        
        System.out.println("\n**Done** Visited " + pagesVisited.size() + " web page(s)");
        return crawledPages;
    }
    
    private UrlNode crawl(String url){
        
        
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).timeout(5000);
            Document htmlDocument = connection.get();
            
            if(connection.response().statusCode() == 200)
                System.out.println("Received web page at " + url);
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return null;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            //System.out.println("Found " + linksOnPage.size() + " links");
            for(Element link : linksOnPage)
            {
                //ignore javascript links
                link.select("script").remove();
                
                pagesToVisit.add(link.absUrl("href"));
            }
            
            UrlNode node = new UrlNode(url, htmlDocument.title(), htmlDocument.body().text());
            
            return node;
        }
        catch(IOException e)
        {
            System.out.println("Error in out HTTP request " + e);
            return null;
        }     
        
    }
    
}
