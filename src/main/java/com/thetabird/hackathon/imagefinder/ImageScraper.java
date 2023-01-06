package com.thetabird.hackathon.imagefinder;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImageScraper implements Runnable{
    private String url;
	private ArrayList<String> outputImages;

	public ImageScraper(String url, ArrayList<String> outputImages){
        this.url = url;
		this.outputImages = outputImages;
	}

	/**
	 * Print thread ID to console, and initialize image scraping from webpage.
	 * 
	 */
	public void run(){
		long id = Thread.currentThread().getId();
		System.out.println("Thread " + id + " is running: "  + url);
		try{
			scrapeWebpageForImages(url);
		}catch(IOException e){
			System.out.println("Exception on Thread " + id);
			System.out.println(e);
		}
		//System.out.println("Thread " + id + " finished.");
	}

	
	/** 
	 * Scrape webpage for all image tags, filter invalid file extensions, and append valid tags to an arrayList.
	 * 
	 * @param url URL of webpage to pull images from.
	 * @throws IOException
	 */
	protected void scrapeWebpageForImages(String url) throws IOException{
		//Connect to URL and get HTML data
		Connection conn = Jsoup.connect(url).timeout(1000);
		Document doc = conn.get();
        
        // Get all img tags
        Elements img = doc.getElementsByTag("img");
        
        // Loop through img tags
        for (Element el : img) {
			String tag = el.attr("abs:src");
            
			//Do not include img sources that don't have a valid image file extension
            if(!tag.contains(".png") && !tag.contains(".apng") && 
            !tag.contains(".jpeg") && !tag.contains(".jpg") &&
            !tag.contains(".jfif") && !tag.contains(".pjpeg") &&
            !tag.contains(".pjp") && !tag.contains(".tiff") &&
            !tag.contains(".tif") && !tag.contains(".cur") &&
            !tag.contains(".svg") && !tag.contains(".webp") &&
            !tag.contains(".bmp") && !tag.contains(".ico")){
                continue;
            }
            
			//Do not include image sources that are already in the list
			if(outputImages.contains(tag)){continue;}
            
			//Append image source to arraylist of valid sources.
			outputImages.add(tag);
            
        }
	}

}