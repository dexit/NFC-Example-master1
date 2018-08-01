package com.bathtimefish.nfcexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.widget.Toast;
import android.media.MediaPlayer;
// import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ScanResultActivity extends Activity {

    private Bundle mBundle;
    private MediaPlayer mMediaPlayer;
	private WebView webView;
	private String mNFCID;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanresult);

        mBundle = getIntent().getExtras();
        
        // Play audio
        mMediaPlayer = MediaPlayer.create(this, R.raw.success_scan);
        mMediaPlayer.start();

        if (mBundle != null) {
            byte[] nfcIDBytes = mBundle.getByteArray(NfcAdapter.EXTRA_ID);
            String nfcID = "undefined";

            // Fix IDm as 8 digits (If an unknown card arrives return an error)
            //if (nfcIDBytes.length == 8) {
            	//byte [] -> HexString
            	// Generate a string buffer twice as long as the byte array.
            	StringBuffer strbuf = new StringBuffer(nfcIDBytes.length * 2);
            	// Repeat the process for the number of elements of the byte array.
            	for (int index = 0; index < nfcIDBytes.length; index++) {
            		// Convert byte value to natural number.
            		int bt = nfcIDBytes[index] & 0xff;
            		// It is judged whether the byte value is 0x10 or less.
            		if (bt < 0x10) {
            			// When 0x10 or less, 0 is added to the character string buffer.
            			strbuf.append("0");
            		}
            		// Convert byte value to hexadecimal character string and add it to the string buffer.
            		strbuf.append(Integer.toHexString(bt));
            	}
            	nfcID = strbuf.toString();
            	mNFCID = nfcID;

            //} else {
            //    Toast.makeText(this, "Scan another card", Toast.LENGTH_LONG).show();
            //    finish();
            //}

        }
        
        /* Display of WebView */
        setContentView(R.layout.webview);
        
        // Obtain webView
        webView = (WebView)findViewById(R.id.webView1);
        
        // Set the default directory of web resources
        webView.loadDataWithBaseURL("file:///android_asset/www/", null, null, "utf-8", null);
        //webView.loadDataWithBaseURL("file:///android_asset/www/js", null, "application/javascript", "utf-8", null);
        //webView.loadDataWithBaseURL("file:///android_asset/www/css", null, "text/css", "utf-8", null);
        //webView.loadDataWithBaseURL("file:///android_asset/www/image", null, "image/png", null, null);
        // Erase the margin on the right 10 px
        webView.setVerticalScrollbarOverlay(true);
        // Prevent start of standard browser by link click
        webView.setWebViewClient(new WebViewClient());
        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        // Enable Flash
        webView.getSettings().setPluginsEnabled( true );
        
        // Added JavaScript interface.
        // It will be able to handle the object called android from JavaScript
        //webView.addJavascriptInterface(new WebViewInterface(this), "Android");
        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        
        // Display ResultPage
        webView.loadUrl("file:///android_asset/www/scanResult.html");
        //webView.loadUrl("http://html5test.com");
        
    	try {
    		webView.requestFocus();
    	} catch (Exception e) {
    	}
    }
    
    public class JavaScriptInterface {
    	Context mContext;
    	
        public JavaScriptInterface (Context c) {  
        	mContext = c;
        } 
        
    	/** Show a toast from the web page */
    	public void showToast(String toast) {
    		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    	}
    	
    	public String getNfcId() {
    		return mNFCID;
    	}
    	
    	// Call page index Activity
    	public void goIndex() {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
    		// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Tokyo"));
            startActivity(intent);
    	}
    }
    
    // Return to the previous page when you press the back button on the device
    /*
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( event.getAction() == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_BACK
                && webView.canGoBack() == true ) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }
    */
}
