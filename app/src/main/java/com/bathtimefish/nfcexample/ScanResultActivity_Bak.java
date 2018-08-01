package com.bathtimefish.nfcexample;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class ScanResultActivity_Bak extends Activity {

    private Bundle mBundle;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanresult_bak);

        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        TextView textView6 = (TextView) findViewById(R.id.textView6);
        TextView textView7 = (TextView) findViewById(R.id.textView7);
        TextView textView8 = (TextView) findViewById(R.id.textView8);

        mBundle = getIntent().getExtras();

        if (mBundle != null) {
            byte[] nfcIDm = mBundle.getByteArray(NfcAdapter.EXTRA_ID);

            // Fix IDm as 8 digits (If an unknown card arrives return an error)
            if (nfcIDm.length == 8) {
// Add the least significant digit to the rest (give some) but give irreversibility.
// *** Algorithm is a monkey so IDm leaks danger Ali ***

                int[] param = new int[8];

                for (int i = 0; i < nfcIDm.length; i++) {
                    param[i] = nfcIDm[i] & 0xff; // byte 2 int conversion
                }

                mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                mEditor = mPreferences.edit();
                /* Adding one digit */
                for (int i = 0; i < param.length - 1; i++) {
                    param[i] += param[7];
                    mEditor.putInt("PARAM" + i, param[i]);
                }
                param[7] = 0;// I do not know if it is meaningful.
                mEditor.commit();

                textView1.setText("Data set complete!");
                textView2.append(String.valueOf(param[0]));
                textView3.append(String.valueOf(param[1]));
                textView4.append(String.valueOf(param[2]));
                textView5.append(String.valueOf(param[3]));
                textView6.append(String.valueOf(param[4]));
                textView7.append(String.valueOf(param[5]));
                textView8.append(String.valueOf(param[6]));

            } else {
                Toast.makeText(this, "Sorry Please scan another card", Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    // @Override
    // protected void onNewIntent(Intent intent) {
    // super.onNewIntent(intent);
    //
    // byte[] rawMsgs = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
    //
    // Log.v(TAG, "length:" + rawMsgs.length);
    //
    // for (int i = 0; i < rawMsgs.length; i++) {
    // Log.v(TAG, "rawMsgs[" + i + "]" + rawMsgs[i]);
    // }
    //
    // }
}
