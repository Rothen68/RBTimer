package com.rothen.rbtimer.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.CountDownTimer;
import android.util.Log;

import com.rothen.rbtimer.utils.Utils;

import java.io.IOException;

/**
 * Manage NFC
 * Created by apest on 17/03/2017.
 */

public class NFCService implements NfcAdapter.ReaderCallback {

    public interface NFCAdapterListener
    {
        void onTagConnectionLost();
    }

    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    private NFCAdapterListener listener;

    private NfcAdapter nfcAdapter;

    private CountDownTimer tagPresenceCheckTimer;
    private INfcObject nfcObject;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
//                    NfcA.class.getName(),
//                    NfcB.class.getName(),
//                    NfcF.class.getName(),
//                    NfcV.class.getName(),
//                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
//                    Ndef.class.getName()
            }
    };



    public NFCService(Context context, NFCAdapterListener listener)
    {
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.listener = listener;
    }

    public boolean isNFCAvailable()
    {
        return nfcAdapter!=null;
    }

    public boolean isNFCEnabled()
    {
        if(isNFCAvailable())
        {
            return nfcAdapter.isEnabled();
        }
        else
        {
            return false;
        }
    }

    public void registerIntentFilter(Class<?> c, Activity activity)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, c).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    public void unRegisterIntentFilter(Activity activity)
    {
        nfcAdapter.disableForegroundDispatch(activity);
        if(tagPresenceCheckTimer!=null)
        {
            tagPresenceCheckTimer.cancel();
        }
    }

    public String lookupTag(Intent i)
    {
        if (i.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Tag tag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            onTagDiscovered(tag);
            byte[] id = i.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            return Utils.ByteArrayToHexString(id);
        }
        return "";
    }


    @Override
    public void onTagDiscovered(Tag tag) {

        nfcObject = NfcObject.createNFCObjectFromTag(tag);
        try {
            nfcObject.connect();
            startLoopTagPresenceTimer();

        } catch (IOException e) {
            Log.e("NFC", e.getMessage());
        }
    }


    private void startLoopTagPresenceTimer()
    {
        tagPresenceCheckTimer = new CountDownTimer(60000,500) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!nfcObject.isConnected())
                {
                    listener.onTagConnectionLost();
                    tagPresenceCheckTimer.cancel();
                }
            }

            @Override
            public void onFinish() {
                startLoopTagPresenceTimer();
            }
        }.start();
    }
}
