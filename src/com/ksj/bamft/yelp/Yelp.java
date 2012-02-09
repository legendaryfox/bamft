package com.ksj.bamft.yelp;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.ksj.bamft.constants.Constants;

import android.util.Log;

/**
 * Example for accessing the Yelp API.
 */
public class Yelp {

	OAuthService service;
	Token accessToken;

	/**
	 * Setup the Yelp API OAuth credentials.
	 *
	 * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
	 *
	 * @param consumerKey Consumer key
	 * @param consumerSecret Consumer secret
	 * @param token Token
	 * @param tokenSecret Token secret
	 */
	public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}
	
	public Yelp() {
		this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(Constants.YELP_CONSUMER_KEY).apiSecret(Constants.YELP_CONSUMER_SECRET).build();
		this.accessToken = new Token(Constants.YELP_TOKEN, Constants.YELP_TOKEN_SECRET);
	}

	/**
	 * Search with term and location.
	 *
	 * @param term Search term
	 * @param latitude Latitude
	 * @param longitude Longitude
	 * @return JSON string response
	 */
	public String search(String term, double latitude, double longitude) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	public String businessInfo(String businessName) {
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/business/" + businessName);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		//Log.d("YELP", "Yelp Request was " + request.getSanitizedUrl());
		return response.getBody();

	}

	// CLI
	/*
  public static void main(String[] args) {
    // Update tokens here from Yelp developers site, Manage API access.
    String consumerKey = "-5C4AuSdFEk_CG5eWkkspQ";
    String consumerSecret = "tdqFAH50qL3aIYzmjXluFkWutug";
    String token = "mNCtTzIZhsSEKYILLYMq6HZtYXKWtkYB";
    String tokenSecret = "gCID4b-mQqNf7MxdYgcPMGywWDU";

    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    //String response = yelp.search("burritos", 30.361471, -87.164326);
	String response = yelp.businessInfo("bbqsmith-boston");

    //System.out.println(response);
	Log.d("YELP", response);
  }
	 */

	public String getBusinessInfo(String businessName) {

		/*
		Yelp yelp = new Yelp(Constants.YELP_CONSUMER_KEY, Constants.YELP_CONSUMER_SECRET, Constants.YELP_TOKEN, Constants.YELP_TOKEN_SECRET);
		*/
		//Log.d("YELP", "getbusinessinfo start");
		String response = this.businessInfo(businessName);

		//System.out.println(response);
		//Log.d("YELP", response);
		//Log.d("YELP", "getbuinssesinfo end");
		return response;
	}
}
