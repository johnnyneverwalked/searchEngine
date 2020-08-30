/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;

/**
 *
 * @author John Stamtsidis
 */
public class Indexer {
    
    private IndexWriter iwriter; //VSM index
    private IndexWriter iwriterB; //BM index
    private TextFileFilter filter;
       
    public Indexer(Analyzer analyzer,String indexDirectoryPath , String indexBDirectoryPath) throws IOException{
        
        filter = new TextFileFilter();
        
        Path path = Paths.get(indexDirectoryPath);
        Directory directory = FSDirectory.open(path);
        
        Path pathB = Paths.get(indexBDirectoryPath);
        Directory directoryB = FSDirectory.open(pathB);

      //create the indexer
         
        
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode( IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setSimilarity(new ClassicSimilarity());
        iwriter = new IndexWriter(directory, config);
        
        IndexWriterConfig configB = new IndexWriterConfig(analyzer);
        configB.setOpenMode( IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        configB.setSimilarity(new BooleanSimilarity());
        iwriterB = new IndexWriter(directoryB, configB);
    }

    
    public void close() throws CorruptIndexException, IOException{
        iwriter.close();
        iwriterB.close();
    }
    
    public void commit() throws CorruptIndexException, IOException{
        iwriter.commit();
        iwriterB.commit();
    }
    
    private Document getFileDocument(File file) throws IOException{
        Document document = new Document();
        String reader = new Scanner(file).useDelimiter("\\A").next();
        
      //index file contents
        Field contentField = new Field(LuceneConstants.CONTENTS, 
         reader, 
         TextField.TYPE_STORED);
      
      //index file name
        Field fileNameField = new Field(LuceneConstants.NAME,
         file.getName(),
         TextField.TYPE_STORED);
      
      //store file path unindexed
        Field filePathField = new Field(LuceneConstants.PATH,
         "uploads/".concat(file.getName()),
         StringField.TYPE_STORED);
        
      //empty url field for consistency
        Field fileUrlField = new Field(LuceneConstants.URL,
         "-",
         StringField.TYPE_STORED);
        
        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);
        document.add(fileUrlField);
        
        return document;
    }
    
    private Document getUrlDocument(UrlNode node){
        Document document = new Document();
        
      //index file contents
        Field contentField = new Field(LuceneConstants.CONTENTS, 
         node.get(LuceneConstants.CONTENTS), 
         TextField.TYPE_STORED);        
      
      //index file name
        Field fileNameField = new Field(LuceneConstants.NAME,
         node.get(LuceneConstants.NAME),
         TextField.TYPE_STORED);
      
      //empty path field for consistency
        Field filePathField = new Field(LuceneConstants.PATH,
         "-",
         StringField.TYPE_STORED);
        
      //store url unindexed
        Field fileUrlField = new Field(LuceneConstants.URL,
         node.get(LuceneConstants.URL),
         StringField.TYPE_STORED);
        
        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);
        document.add(fileUrlField);
        
        return document;
    }
    
    private void indexFile(File file) throws IOException{
        if(filter.accept(file)){
            System.out.println("Indexing "+file.getCanonicalPath());
            Document doc = getFileDocument(file);
            iwriter.updateDocument(new Term(LuceneConstants.PATH,doc.get(LuceneConstants.PATH)), doc);
            iwriterB.updateDocument(new Term(LuceneConstants.PATH,doc.get(LuceneConstants.PATH)), doc);
        }
    }
    
    public void indexUrl(UrlNode node) throws IOException{
        
        System.out.println("Indexing "+node.get(LuceneConstants.URL));
        Document doc = getUrlDocument(node);
        iwriter.updateDocument(new Term(LuceneConstants.URL,doc.get(LuceneConstants.URL)), doc);
        iwriterB.updateDocument(new Term(LuceneConstants.URL,doc.get(LuceneConstants.URL)), doc);
    }
    
    public int crawl(String url, int maxPages) throws IOException {
        Crawler crawler = new Crawler();
        List<UrlNode> sites = crawler.start(url, maxPages);
            for(UrlNode site : sites){
                indexUrl(site);       
            }
        
        return iwriter.numDocs();
    }
    
    public int index(String dataDirPath) throws IOException{
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();
               
        for(File file : files){
            if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead())
                indexFile(file);
            
        }
        
        return iwriter.numDocs();
    }
}
