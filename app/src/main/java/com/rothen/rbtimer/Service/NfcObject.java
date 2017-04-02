package com.rothen.rbtimer.service;

import android.nfc.Tag;

import com.rothen.rbtimer.utils.Utils;

/**
 * Created by apest on 24/03/2017.
 */

public abstract class  NfcObject implements INfcObject {
    public static final String MIFAREULTRALIGHT = "android.nfc.tech.MifareUltralight";
    public static final String MIFARECLASSIC = "android.nfc.tech.MifareClassic";

    public static final INfcObject createNFCObjectFromTag(Tag tag)
    {
        String [] list = tag.getTechList();
        if(Utils.stringArrayContainsString(list,MIFARECLASSIC))
        {
            return new MifareClassicNFCObject(tag);
        }
        else if(Utils.stringArrayContainsString(list,MIFAREULTRALIGHT))
        {
            return new MifareUltralightNFCObject(tag);
        }
        else
        {
            return null;
        }
    }
}
