package com.myapps.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.myapps.backend.jokeApi.JokeApi;
import com.myapps.backend.jokeApi.model.JokeBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.myapps.jokedisplayer.JokeDisplayActivity;

import java.io.IOException;

class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static final String LOG_TAG = "EndpointsAsyncTask";
    private static JokeApi jokeApi = null;
    private Context context;
    private ProgressBar mProgressBar;

    public EndpointsAsyncTask(ProgressBar progressBar, Context context){
        this.mProgressBar = progressBar;
        this.context = context;
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(jokeApi == null) {  // Only do this once
            JokeApi.Builder builder = new JokeApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/");

            // end options for devappserver

            jokeApi = builder.build();
        }

        try {
            return jokeApi.putJoke(new JokeBean()).execute().getJoke();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressBar != null)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(final String result) {
        super.onPostExecute(result);

        if (mProgressBar != null) {
              mProgressBar.setVisibility(View.GONE);
        }

         startJokeActivity(result);

    }

    private void startJokeActivity(String mResult) {
        Intent intent = new Intent(context, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_INTENT, mResult);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
