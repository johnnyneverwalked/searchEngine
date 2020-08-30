/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;



/**
 *
 * @author John Stamtsidis
 */
public class SearchEngine {

    
    public static void main(String[] args) {
        
    
        try{
            Engine engine = new Engine();
            
            if("search".equals(args[0]))
                engine.search(args[1],Boolean.getBoolean(args[2]),Integer.parseInt(args[3]));
            else if("index".equals(args[0]))
                engine.index(args[1]);
            else if("crawl".equals(args[0]))                           
                engine.crawl(args[1],Integer.parseInt(args[2]));
            engine.close();
        }catch(IOException e){e.printStackTrace();}
         catch(ParseException e){e.printStackTrace();}

    }
    
}
