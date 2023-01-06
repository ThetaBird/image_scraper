package com.thetabird.hackathon.imagefinder;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class URLScraper implements Runnable{
    private String url;
	private ArrayList<String> allURLs = new ArrayList<String>();

	public URLScraper(String url){
		this.url = url;
	}

	/**
	 * Print thread ID to console, and initialize URL scraping from webpage.
	 * 
	 */
	public void run(){
		try{
			System.out.println("URL Scraper Thread " + Thread.currentThread().getId()+ " is running");
			scrapeWebpageForURLs(url);
		}catch(IOException e){
			System.out.println("Exception");
		}
		
	}

	
	/** 
	 * Scrape webpage for links to other pages. 
	 * Filter invalid links (pointing to pages outside of original domain) and append valid links to an ArrayList.
	 * 
	 * @param url URL of webpage to scrape links from.
	 * @throws IOException
	 */
	protected void scrapeWebpageForURLs(String url) throws IOException{
		//Connect to url and get HTML data
		Document doc = Jsoup.connect(url).get();

		//Get parsed domain from initially provided url
        String domain = getRootURL(url);
		
        //Add absolute url of original url to arraylist
		Element firstlink = doc.select("a").first();
        String absHref = firstlink.attr("abs:href");
        allURLs.add(absHref);

        // Get all url tags
        Elements urls = doc.select("a[href]");

        // Loop through img tags
        for (Element el : urls) {
			//Get absolute path of url
			String link = el.attr("abs:href");

			//Remove reference to section in page (to prevent multiple calls to the same page)
			link = link.split("#")[0];
			
			//Do not add link if not in domain
			if(!link.startsWith(url) && !domain.equals(getRootURL(link))){continue;}

			//Remove slash at end of URL (to prevent multiple calls to the same page)
			if(link.endsWith("/")){link = link.substring(0, link.length()-1);}
			
			//Do not add link if already in list. Otherwise, add link to list.
			if(allURLs.contains(link)){continue;}
			allURLs.add(link);
        }
	}

    
	/**
	 * Parse a URL string and return its domain. 
	 * 
	 * @param url URL string to pull domain from.
	 * @return String parsed domain string
	 */
	public String getRootURL(String url){
       
        String[] splitRootURL = url.split("//"); //example: "https://coronavirus.lehigh.edu/home" => ["https:","coronavirus.lehigh.edu/home"]
        System.out.println(splitRootURL[1]);
        
        String domain = splitRootURL[1].split("/")[0]; //example: "coronavirus.lehigh.edu/home" => ["coronavirus.lehigh.edu","home"] => "coronavirus.lehigh.edu"
        //System.out.println(domain);
        String[] rootDomainArr = domain.split("\\."); //example: "coronavirus.lehigh.edu" => ["coronavirus","lehigh","edu"]
        //System.out.println(rootDomainArr[0]);
        String rootURL = splitRootURL[0] + "//" + rootDomainArr[rootDomainArr.length-2] + "." + rootDomainArr[rootDomainArr.length-1] + "/"; //example: ["https:","coronavirus.lehigh.edu/home"] & ["coronavirus","lehigh","edu"] => "https://lehigh.edu/"
		System.out.println(rootURL);
        return rootURL;
    }

	
	/** 
	 * Access private allURLs variable and return its value.
	 * 
	 * @return ArrayList<String> list of all valid URLs pulled from the webpage
	 */
	public ArrayList<String> getURLs(){
		return allURLs;
	}
}
