package com.rothen.rbtimer.service;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

import java.io.IOException;

/**
 * Created by apest on 24/03/2017.
 */

public class MifareClassicNFCObject extends NfcObject {

    private MifareClassic nfcObject;

    public MifareClassicNFCObject(Tag tag)
    {
        nfcObject = MifareClassic.get(tag);
    }

    @Override
    public void connect() throws IOException {
        nfcObject.connect();
    }

    @Override
    public boolean isConnected() {
        return nfcObject.isConnected();
    }
}
