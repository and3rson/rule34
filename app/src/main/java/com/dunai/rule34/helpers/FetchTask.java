package com.dunai.rule34.helpers;

import com.dunai.rule34.exceptions.RESTException;
import com.dunai.rule34.helpers.RemoteTask;
import com.dunai.rule34.interfaces.OnFetchReceiver;
import com.dunai.rule34.models.Query;
import com.dunai.rule34.models.Remote;

/**
 * Created by anderson on 04.06.15.
 */
public class FetchTask extends RemoteTask {
    private OnFetchReceiver receiver;
    private RESTException exc = null;

    public FetchTask(OnFetchReceiver receiver, String tag) {
        super(tag);
        this.receiver = receiver;
    }

    @Override
    protected Remote doInBackground(Remote... params) {
        try {
            params[0].load(this.getQuery());
        } catch(RESTException e) {
            this.exc = e;
        }
        return params[0];
    }

    protected void onPostExecute(Remote object) {
        super.onPostExecute(object);

        if (this.exc == null) {
            this.receiver.onFetchRemote(object, this.getTag());
        } else {
            this.receiver.onFetchRemoteError(this.exc, this.getTag());
        }
    }
}
