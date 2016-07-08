package com.sap.ui5.selenium.utilities;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;




public class HttpRequest {

	String CONTENT_TYPE = "application/json;charset=utf-8";
	String user = System.getProperty("hanaUser");
	String pass = System.getProperty("hanaPassword");
	String hanaHost = System.getProperty("hanaHost");
	String hanaHTTPPort = System.getProperty("hanaHTTPPort");
	
	String authStr = (user==null?"SYSTEM":user) + ":" + (pass==null?"manager":pass);
    String authEncoded = Base64.encodeBase64String(authStr.getBytes());
    Boolean isCloud = new Boolean(System.getProperty("isCloud")).booleanValue();
    Boolean isSAML = new Boolean(System.getProperty("isSAML")).booleanValue();
    String baseUrl ="";
    
    public HttpRequest(){
		baseUrl = getBaseURL();
	}
    
    public String getBaseURL(){
    	String port = "";
		if(isCloud){
			port="";
		}/*else if(isSAML){
			port = hanaHTTPPort.replaceFirst("80", ":43");
		}*/else{
			port = ":"+hanaHTTPPort;
		}
		return ((isCloud/*||isSAML*/)?"https://":"http://")+hanaHost+port;
    }
	
	
	public JSONObject sendPost(String url,String postData,String username, String password) throws Exception {
		String authStr = (username==null?user:username) + ":" + (password==null?pass:password);
	    String authEncoded = Base64.encodeBase64String(authStr.getBytes());
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		String body="";
		try {

            HttpGet httpget = new HttpGet(baseUrl + "/sap/fpa/services/csrf.xsjs");

            httpget.addHeader("X-CSRF-Token", "Fetch");
            httpget.addHeader("Authorization", "Basic "+authEncoded);
            HttpResponse response = httpClient.execute(httpget);
            String token = response.getHeaders("X-CSRF-Token")[0].getValue();
            
            HttpPost httpPost =  new HttpPost(baseUrl + url);
            httpPost.addHeader("X-CSRF-Token", token);
            httpPost.addHeader("Authorization", "Basic "+authEncoded);
            
            HttpEntity myEntity = new StringEntity(postData);
            
            httpPost.setEntity(myEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
            	body = body+inputLine;
            in.close();
            EntityUtils.consume(entity);
            return new JSONObject(body);
        }catch(Exception e){
        	return null;
		} finally {
            httpClient.close();

		}
		
	}
	
	public JSONObject sendGet(String url, String username, String password) throws Exception {
		String authStr = (username==null?user:username) + ":" + (password==null?pass:password);
		String authEncoded = Base64.encodeBase64String(authStr.getBytes());
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		String body="";
		try {

            HttpGet httpget = new HttpGet(baseUrl + "/sap/fpa/services/csrf.xsjs");
            httpget.addHeader("X-CSRF-Token", "Fetch");
            httpget.addHeader("Authorization", "Basic "+authEncoded);
            HttpResponse response = httpClient.execute(httpget);
            String token = response.getHeaders("X-CSRF-Token")[0].getValue();
            
            httpget =  new HttpGet(baseUrl + url);
            httpget.addHeader("X-CSRF-Token", token);
            httpget.addHeader("Authorization", "Basic "+authEncoded);
            response = httpClient.execute(httpget);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            while ((inputLine = in.readLine()) != null){
            	//System.out.println(inputLine);
            	body = body+inputLine;
            }

            in.close();
            return new JSONObject(body);
        }catch(Exception e){
			System.out.println(body);
		} finally {
            httpClient.close();

		}
		return null;
	}
}
