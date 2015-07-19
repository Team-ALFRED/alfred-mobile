package edu.utexas.seniordesign.alfred;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class DeliveryTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "DeliveryTask";
    private DeliveryFragment fragment;

    public DeliveryTask(DeliveryFragment fragment) {
        this.fragment = fragment;
    }

    /** progress bar to show user that the backup is processing. */
    /**
     * application context.
     */
    @Override
    protected void onPreExecute() {}

    @Override
    protected Boolean doInBackground(final String... args) {
        try {
            Thread.sleep(1000);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "error", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        DeliveryFragment.OnFragmentInteractionListener listener = fragment.getListener();
        if (listener != null) {
            listener.onDeliveryFragmentInteraction(success);
        }
    }

}