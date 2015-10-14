package com.dunai.rule34.interfaces;

import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.models.Remote;

/**
 * Created by anderson on 04.06.15.
 */
public interface OnFetchReceiver {
    public abstract void onFetchRemote(Remote object, String tag);

    void onFetchRemoteError(RESTException e, String tag);
}
