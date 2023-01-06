## ImageFinder Goal
The goal of this task is to create a full stack application that performs a web crawl on a URL string provided by the user.
**Technologies**: Java Servlets, JSoup, React.js, Maven

### Functionality
- Finds all images on the target web page(s).
- Crawls sub-pages to find more images.
- Implements multi-threading so that the crawl can be performed on multiple pages at a time.
- Keeps the crawl within the same domain as the input URL.
- Avoids re-crawling any pages that have already been visited.
 😊

## Structure
The ImageFinder servlet is found in `src/main/java/com/thetabird/hackathon/imagefinder/ImageFinder.java`.

The main landing page for this project can be found in `src/main/webapp/index.html`. This page serves as the starting page for the web application. 

Finally, in the root directory of this project, you will find the `pom.xml`. This contains the project configuration details used by maven to build the project.

### Requirements
- Maven 3.5+
- Java 8

### Setup
To start, open a terminal window and navigate to wherever you unzipped to the root directory `imagefinder`. To build, test, and run the project, run the command:


>`mvn clean test package jetty:run`

You should see a line at the bottom that says "Started Jetty Server". Now, if you enter `localhost:8080` into your browser, you should see the image scraper page.

## Notes

As much as I'd love to make the best image scraper in the world, certain circumstances in my life currently do not enable the time and resources to do so. 
  
The purpose of this note is to highlight some of the things I would have implemented given a longer timeframe.
  
  - In a production environment, I would probably opt to have responses  from the servlet sent in real-time through a websocket. This would shorten the response time by a lot - and since I'm assuming the main application for this project would be for people to see all  the webpage's images (as opposed to automated systems pulling  images for data processing purposes), a better user experience  would be provided if batches of images were sent ASAP so that the user could lovingly stare at the first batch while more results  are processed and sent. 
  
  - Currently, the servlet does not provide a response at all if it encounters an unhandled exception, and the frontend just displays a generic message. For production, I would definitely invest in  better error handling, i.e. actually noting what causes an issue and sending the relevant data back to the user for further action.
  
  - With regards to potentially detecting people in images, I would probably opt for Amazon Rekognition or Google's Cloud Vision API. This would also slightly complicate the response structure and  increase response times. But it's definitely handleable.
  
  - With regards to potentially detecting logos in images, I thought about labling .svg's as logos, but noticed that a lot of logos end in .jpg or .png and a lot of non-logos end in .svg. Such overlap defeats the accuracy of this method, and I would assume that image recognition would also be needed for this — but I'd love to hear what you think about methods of implementation.