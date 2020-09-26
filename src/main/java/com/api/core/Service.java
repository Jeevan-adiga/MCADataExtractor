package com.api.core;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.api.response.ListMovies;
import com.api.response.MoviesDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Constants;

import auto.framework.ReportLog;

public class Service {

	
	public static ListMovies provideMovieList() throws IOException {
		
		String url = Constants.WEBSERVICE_ENDPOINT + "list_movies.json";
		ReportLog.addInfo("Provide MovieList Url:"+url);
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		get.setHeader(HttpHeaders.ACCEPT,"application/json; charset=utf-8");
		
		ReportLog.addInfo("Using Headers:");
		for(Header header : get.getAllHeaders()) {
			ReportLog.addInfo(header.getName()+"-"+header.getValue());
		}

		HttpResponse response = client.execute(get);
		
		HttpEntity entity = response.getEntity();
		
		ReportLog.assertTrue(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, 
				"Provide MovieList ResponseCode:"+response.getStatusLine().getStatusCode());
		
		String responseString = EntityUtils.toString(entity);
		ReportLog.addInfo("Provide MovieList Response:"+responseString);
		
		ObjectMapper mapper = new ObjectMapper();
		ListMovies listMovies = mapper.readValue(responseString, ListMovies.class);  

		return listMovies;
	}
	
	public static MoviesDetails provideMovieDetailsWithoutImageWithCast(String movieId) throws IOException {
		
		String url = Constants.WEBSERVICE_ENDPOINT + "movie_details.json?movie_id="+movieId+"&with_images=false&with_cast=true";
		ReportLog.addInfo("MovieDetails Url:"+url);
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		get.setHeader(HttpHeaders.ACCEPT,"application/json; charset=utf-8");
		
		ReportLog.addInfo("Using Headers:");
		for(Header header : get.getAllHeaders()) {
			ReportLog.addInfo(header.getName()+"-"+header.getValue());
		}

		HttpResponse response = client.execute(get);
		
		HttpEntity entity = response.getEntity();
		
		ReportLog.assertTrue(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, 
				"MovieDetails ResponseCode:"+response.getStatusLine().getStatusCode());
		
		String responseString = EntityUtils.toString(entity);
		ReportLog.addInfo("MovieDetails Response:"+responseString);
		
		ObjectMapper mapper = new ObjectMapper();
		MoviesDetails moviesDetails = mapper.readValue(responseString, MoviesDetails.class);  

		return moviesDetails;
	}
}
