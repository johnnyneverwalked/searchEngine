/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;


/**
 *
 * @author John Stamtsidis
 */
public class Searcher {
    
    private IndexSearcher isearcher;
    private MultiFieldQueryParser qparser;
    
    private Query query;
    
    public Searcher(Analyzer analyzer, String indexDirectoryPath, Boolean booleanSearcher) throws IOException {
        
        Path path = Paths.get(indexDirectoryPath);
        Directory directory = FSDirectory.open(path);   
        
        DirectoryReader ireader = DirectoryReader.open(directory);        
        isearcher = new IndexSearcher(ireader);
        
        if(booleanSearcher){
            isearcher.setSimilarity(new BooleanSimilarity());
        }else{
            isearcher.setSimilarity(new ClassicSimilarity());
        }
        
        
        
        qparser = new MultiFieldQueryParser(new  String[]{LuceneConstants.NAME,LuceneConstants.CONTENTS}, analyzer);
    }
    
    public TopDocs search(String searchQuery, int maxSearch) throws IOException, ParseException {
             
        query = qparser.parse(escapeQuery(searchQuery));
        //System.out.println(isearcher.explain(query, 0));           
        return isearcher.search(query,maxSearch);       
    }
    
    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
        return isearcher.doc(scoreDoc.doc);
    }
    
    private String escapeQuery(String query){
        query=query.replaceAll("\\+", "\\\\+");
        query=query.replaceAll("-", "\\\\-");
        query=query.replaceAll("\\?", "\\\\?");
        query=query.replaceAll(":", "\\\\:");
        query=query.replaceAll("!", "\\\\!");
        query=query.replaceAll("1thisisaphrasequery1", "\"");
           //System.out.println("ESCAPED QUERY : " + query);
        return query;
    }
        
    
}

