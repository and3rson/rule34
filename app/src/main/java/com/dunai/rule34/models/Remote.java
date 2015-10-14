package com.dunai.rule34.models;

import android.util.Log;
import com.dunai.rule34.exceptions.RESTException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by anderson on 12.10.15.
 */
public abstract class Remote {
    private static final String TAG = "rule34/Remote";
    private static final String API_ROOT = "http://rule34.xxx/index.php?";

    private HashMap<String, String> args = new HashMap<>();
    private TYPE type = TYPE.XML;

    enum TYPE {
        XML,
        HTML
    }

    public Remote(TYPE type) {
        this.type = type;
    }

    public Object _get(Query query) throws RESTException {
//        String query = "";
//        for(HashMap.Entry<String, String> entry : this.args.entrySet()) {
//            query += entry.getKey() + "=" + entry.getValue() + "&";
//        }

        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
        HttpConnectionParams.setSoTimeout(httpParams, 15000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
//        String query;
//        try {
//            query = URLEncoder.encode(forceURL != null ? forceURL : this.url, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            throw new RESTException(e.getMessage());
//        }
        String url = API_ROOT;
        if (query != null) {
            url += query.toString();
        }
        HttpRequestBase request = new HttpGet(url);

        String result;

        Log.i(TAG, "Requesting " + request.getURI().toString());

        try {
            HttpResponse response = httpClient.execute(request);
            result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
        } catch (Exception e) {
            throw new RESTException(e.getMessage());
        }

        switch (type) {
            case XML:
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                Document doc;

                try {
                    DocumentBuilder db = dbFactory.newDocumentBuilder();
                    InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
                    doc = db.parse(is);
                } catch (Exception e) {
                    throw new RESTException(e.getMessage());
                }

                return doc.getDocumentElement();
            case HTML:
                return Jsoup.parse(result);
        }

        return null;
    }

    public abstract void load(Query query) throws RESTException;
}
