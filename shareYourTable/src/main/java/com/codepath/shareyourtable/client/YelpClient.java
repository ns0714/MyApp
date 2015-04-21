package com.codepath.shareyourtable.client;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.model.Token;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class YelpClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = YelpApi.class; 
	public static final String REST_URL = "http://api.yelp.com/v2"; 
	public static final String REST_CONSUMER_KEY = "sdQrYA7nlafwC8QsoI0pZw"; 
	public static final String REST_CONSUMER_SECRET = "i72nHZtXilxKuDQcNHjuxY1G3gM";
	public static final String TOKEN = "NqDKIHhujv7nlrIVtI8-nwn1qgRCbyOH"; 
    public static final String TOKEN_SECRET = "2NoGAg0aDu7oRuV1Qrc8kZCK9CA";
	public static final String REST_CALLBACK_URL = "oauth://yelp"; 
	
	Token accessToken;

	public YelpClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
		client.setAccessToken(new Token(TOKEN, TOKEN_SECRET));
	}

	public void getSearchResults(AsyncHttpResponseHandler handler,
			String term, String location, int limit, int offset) {
		String apiUrl = getApiUrl("search");
		RequestParams params = new RequestParams();
		params.put("term", term);
		params.put("limit", String.valueOf(limit));
		params.put("location", location);
		params.put("sort", "0");
        params.put("offset", String.valueOf(offset));
		client.get(apiUrl, params, handler);
	}

}