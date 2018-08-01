package com.bathtimefish.nfcexample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
// import android.nfc.tech.NfcF;
import android.nfc.tech.NfcA;
import android.os.Bundle;

public class ScanActivity extends Activity {

    private NfcAdapter mAdapter; //Provide terminal NFC adapter
    private Intent mIntent;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        mIntent = new Intent(this, ScanResultActivity.class);

        mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        // Which Broadcast will be received (Detailed on above)
        mFilters = new IntentFilter[] {
                ndef,
        };

        // Which type of card to read
        mTechLists = new String[][] {
                new String[] {
                        // I might be misreading it here.
                        NfcA.class.getName(), // MIFARE
                        // NfcB.class.getName(), // B
                        // NfcF.class.getName(), // FeliCa
                        // FeliCa seems to be able to read Pitapa
                }
        };

    }

    @Override
    protected void onResume() {
        // Turn on things like NFC read switch
        super.onResume();
        // Log.v(TAG, "onResume!");

        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    @Override
    protected void onPause() {
        //Turn OFF the NFC read switch
        super.onPause();
        // Log.v(TAG, "onPause!");

        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }
}
