/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Johnny
 */
public class Engine {
    
    private final String indexDir = "index";
    private final String indexBDir = "indexB";

    private Indexer indexer;
    private Searcher searcher; //VSM searcher
    private Searcher searcherB; //BM searcher    
    private Analyzer analyzer;
    
    public Engine() throws IOException{
        
        analyzer = new StandardAnalyzer();
        indexer = new Indexer(analyzer,indexDir,indexBDir);
        indexer.commit();
        searcher = new Searcher(analyzer,indexDir,false);
        searcherB = new Searcher(analyzer,indexBDir,true);
    }
    
    public void index(String dataDir) throws IOException{
        int numIndexed;
        long startTime = System.currentTimeMillis();	
        numIndexed = indexer.index(dataDir);
        long endTime = System.currentTimeMillis();
        indexer.commit();
        System.out.println(numIndexed+" Files indexed, time taken: "+(endTime-startTime)+" ms");
    }
    
    public void crawl(String url, int maxSites) throws IOException{
        int numIndexed;
        long startTime = System.currentTimeMillis();	
        numIndexed = indexer.crawl(url,maxSites);
        long endTime = System.currentTimeMillis();
        indexer.commit();
        System.out.println(numIndexed+" Files indexed, time taken: "+(endTime-startTime)+" ms");
    }
    
    public void close()throws IOException{
        indexer.close();
    }
    
    public void search(String searchQuery, boolean isBooleanSearch, int maxSearch) throws IOException, ParseException{
        
        TopDocs hits;
        if(!isBooleanSearch){
            hits = searcher.search(searchQuery,maxSearch);
            
        }else{
            
            hits = searcherB.search(searchQuery,maxSearch);
            
        }
         
        if(hits != null){
            
            for(ScoreDoc scoreDoc : hits.scoreDocs){
                Document doc = searcher.getDocument(scoreDoc);
                System.out.println("Result: "+ doc.get(LuceneConstants.NAME));
                if(!doc.get(LuceneConstants.PATH).equals("-"))
                    System.out.println(doc.get(LuceneConstants.PATH));
                else
                    System.out.println(doc.get(LuceneConstants.URL));             
            }
            System.out.println(hits.totalHits + " documents found.");
        }
    }
    
    
}
