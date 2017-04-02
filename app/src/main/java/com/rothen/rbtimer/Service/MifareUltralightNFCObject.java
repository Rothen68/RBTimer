package com.rothen.rbtimer.service;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;

import java.io.IOException;

/**
 * Created by apest on 24/03/2017.
 */

public class MifareUltralightNFCObject extends NfcObject {

    private MifareUltralight nfcObject;

    public MifareUltralightNFCObject(Tag tag)
    {
        nfcObject = MifareUltralight.get(tag);
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
