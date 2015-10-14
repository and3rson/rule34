package com.dunai.rule34.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dunai.rule34.R;
import com.dunai.rule34.helpers.DownloadAndCacheImageTask;
import com.dunai.rule34.models.impl.Post;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by anderson on 13.10.15.
 */
public class ImageActivity extends ActionBarActivity {
    private static final String TAG = "rule34/ImageActivity";
    Post post;

    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.image);

        this.imageView = (ImageView) this.findViewById(R.id.imageView);

        new DownloadAndCacheImageTask(this, this.imageView, null).run(getIntent().getStringExtra("url"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu, menu);
        menu.findItem(R.id.action_download_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                File direct = new File(Environment.getExternalStorageDirectory() + "/rule34");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                DownloadManager mgr = (DownloadManager) ImageActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(getIntent().getStringExtra("url"));
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);

                String slug;

                MessageDigest md5 = null;
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    Log.e(TAG, "Failed to generate slug!", e);
                    return true;
                }
                md5.reset();
                md5.update(getIntent().getStringExtra("url").getBytes());
                byte[] digest = md5.digest();
                BigInteger bigInt = new BigInteger(1, digest);
                String hashtext = bigInt.toString(16);
                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hashtext.length() < 32 ) {
                    hashtext = "0" + hashtext;
                }
                slug = hashtext;

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(true).setTitle("Rule34");
                request.setDescription("Downloading image");
                request.setDestinationInExternalPublicDir("/rule34", slug + ".jpg");

                mgr.enqueue(request);
                return true;
            }
        });
        return true;
    }
}
