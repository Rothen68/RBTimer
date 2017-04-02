package com.rothen.rbtimer.service;

import java.io.IOException;

/**
 * Created by apest on 24/03/2017.
 */

public interface INfcObject {
    void connect() throws IOException;
    boolean isConnected();

}
