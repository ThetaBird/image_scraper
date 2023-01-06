package com.thetabird.hackathon.imagefinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class RequestProcessor{

	private HttpServletRequest req; 
	private HttpServletResponse resp;
	private Gson GSON;

	public RequestProcessor(HttpServletRequest req, HttpServletResponse resp, Gson GSON){
		this.req = req;
		this.resp = resp;
		this.GSON = GSON;
	}

	/**
	 * Called in ImageFinder right after RequestProcessor instance creation.
	 * Get path, URL parameter from request, pull links and images if valid url, and write a JSON string to response if exists.
	 * 
	 */
	public void run(){
		String path = req.getServletPath();
		String url = req.getParameter("url");
		System.out.println("Got request of:" + path + " with query param:" + url);

		//Prepare array of image src's to parse to JSON
		String[] images;

		if(url == null){//No URL provided, do not provide response
			System.out.println("Null url");
			return;
		}else if(url.equals("test")){//Test url provided, return predetermined values without making any requests
			images = ImageFinder.testImages;
		}
		else{
			//Attempt to fix incomplete urls
			if(!url.startsWith("https")){url = "https://" + url;}
		
			//Get array of scraped images
			images = createRunnable(url);
			System.out.println(images.length);
		}

        //Convert array to JSON String
		String json = GSON.toJson(images);
		
        //Respond to POST request sent to servlet
		try{
			PrintWriter out = resp.getWriter();
			resp.setContentType("text/json");
			resp.setCharacterEncoding("UTF-8");
			
			out.print(json);
			out.flush(); //commit response by flushing the stream
		}catch(IOException e){System.out.println("Exception");}
	}
	
	/** 
	 * Create threads for URL and Image scrapers.
	 * 
	 * @param url URL to scrape links from.
	 * @return String[] Array containing image src's pulled from webpage & from linked webpages in the same domain
	 */
	protected String[] createRunnable(String url){	
        // Scrape URLs from the webpage
		URLScraper urlScraper = new URLScraper(url);
		Thread t = new Thread((Runnable)urlScraper);
		t.start();
		try{
            //Wait for thread to finish
			t.join();
            
            //Get list of urls for all webpages to scrape images from
			ArrayList<String> allURLs = urlScraper.getURLs();

            //Prepare arraylists
			ArrayList<String> outputImages = new ArrayList<String>(); //empty ArrayList for images (pass by reference to the image scraping threads)
			ArrayList<Thread> threads = new ArrayList<Thread>(); //empty ArrayList for threads (to wait for them to finish)

            //Loop through scraped URLs and scrape them for images
			for(String link : allURLs){
                System.out.println(link);
				ImageScraper imageScraper = new ImageScraper(link, outputImages);
				Thread imageScraperThread = new Thread((Runnable)imageScraper);
				threads.add(imageScraperThread);
				
			}

			int count = 0;
			//Start threads, add delay to prevent firewall blocks
			for(Thread thread : threads){
				Thread.sleep(100);
				thread.start();
				count++;
			}
			System.out.println(count + " started");
			count = 0;
            //Wait for image scraping threads to finish
			for(Thread thread : threads){
				//System.out.println("Waiting for finish");
				thread.join();
				count++;
				System.out.println(count + " finished");
			}
			System.out.println("all finished");
            //Prepared arraylist should now contain scraped images from the latest threads. Convert to array and return for JSON conversion
			return outputImages.toArray(new String[outputImages.size()]);
		}catch(InterruptedException e){System.out.println("Interrupted");}

        //Return null if exception occurs
		return null;
	}

    
}