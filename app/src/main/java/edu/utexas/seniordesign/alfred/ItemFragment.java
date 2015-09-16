package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import edu.utexas.seniordesign.alfred.dummy.DummyContent;
import edu.utexas.seniordesign.alfred.models.Item;
import edu.utexas.seniordesign.alfred.models.Sync;

public class ItemFragment extends ListFragment {
    private static final String TAG = "ItemFragment";

    private OnFragmentInteractionListener mListener;

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HttpRequestTask().execute();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            Item item = (Item) getListAdapter().getItem(position);
            mListener.onItemFragmentInteraction(item.getName());
        }
    }

    public interface OnFragmentInteractionListener {
        public void onItemFragmentInteraction(String id);
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Sync> {
        @Override
        protected Sync doInBackground(Void... params) {
            try {
                final String url = "http://alfred.lf.lc/sync";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Sync sync = restTemplate.getForObject(url, Sync.class);
                return sync;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Sync sync) {
            if (getActivity() != null) {
                ArrayAdapter<Item> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, sync.getInv());
                adapter.notifyDataSetChanged();
                setListAdapter(adapter);
            }
        }
    }

}
