/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchengine;

/**
 *
 * @author John Stamtsidis
 */
public class UrlNode {
    
    private String url;
    private String body;
    private String name;
    
    public UrlNode(String url, String name, String body){
        this.url = url;
        this.body = body;
        this.name = name;
    }
    
    public String get(String field){
        if(field.equals(LuceneConstants.CONTENTS))
            return body;
        else if(field.equals(LuceneConstants.URL))
            return url;
        else if (field.equals(LuceneConstants.NAME))
            return name;
        else
            return null;
    }
    
}
