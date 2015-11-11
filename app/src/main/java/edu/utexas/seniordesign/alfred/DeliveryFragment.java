package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;

import edu.utexas.seniordesign.alfred.models.Delivery;
import edu.utexas.seniordesign.alfred.models.Item;

public class DeliveryFragment extends Fragment {

    private static final String TAG = "DeliveryFragment";
    private final Delivery mDelivery;

    private OnFragmentInteractionListener mListener;
    private Activity mActivity;
    private AsyncTask<String, Void, Item> mTask;

    public DeliveryFragment(Delivery delivery) {
        mDelivery = delivery;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delivery, container, false);

        Button btn = (Button) v.findViewById(R.id.buttonHardStop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHardStop();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

        mTask = new DeliveryTask(mActivity, this, mDelivery).execute();
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public OnFragmentInteractionListener getListener() {
        return mListener;
    }

    public interface OnFragmentInteractionListener {
        public void onDeliveryFragmentInteraction(Item result);
    }

    public void onHardStop() {
        RequestQueue queue = VolleySingleton.getInstance(
                mActivity.getApplicationContext()).getRequestQueue();

        final String host = SettingsFragment.getPref(mActivity,
                SettingsFragment.PREF_KEY_IP_ADDRESS);
        final String url = "http://" + host + "/stop";

        StringRequest stopRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTask.cancel(true);

                        Item stopItem = new Item();
                        stopItem.setError("Hard stop requested");
                        mListener.onDeliveryFragmentInteraction(stopItem);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mActivity, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stopRequest);
    }

}
