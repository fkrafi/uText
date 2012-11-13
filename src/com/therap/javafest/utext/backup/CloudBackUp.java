package com.therap.javafest.utext.backup;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class CloudBackUp {

	private HttpClient client;
	private HttpGet request;
	private HttpResponse response;
	private StatusLine statusLine;
	private HttpEntity entity;

	public CloudBackUp() {
		client = new DefaultHttpClient();
		request = new HttpGet();
	}

	public int login(String email, String password) {
		String url = "http://adhyayanbd.com/utext_api/index.php?method=login&email="
				+ email + "&password=" + password;
		try {
			URI uri = new URI(url);
			request.setURI(uri);
			response = client.execute(request);
			statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status == 200) {
				entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				JSONObject object = new JSONObject(data);
				int success = object.getInt("success");
				if (success == 1) {
					return 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int register(String fullname, String email, String password) {
		String url = "http://adhyayanbd.com/utext_api/index.php?method=register&fullname=fullname&email="
				+ email + "&password=" + password;
		try {
			URI uri = new URI(url);
			request.setURI(uri);
			response = client.execute(request);
			statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status == 200) {
				entity = response.getEntity();
				String data = EntityUtils.toString(entity);
				JSONObject object = new JSONObject(data);
				int success = object.getInt("success");
				if (success == 1) {
					return 1;
				}
				int error = object.getInt("error");
				if (error != 0) {
					return (error + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

}
