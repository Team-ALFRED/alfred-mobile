package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.utexas.seniordesign.alfred.models.Item;
import edu.utexas.seniordesign.alfred.models.Sync;

public class ItemFragment extends ListFragment {
    private static final String TAG = "ItemFragment";

    private OnFragmentInteractionListener mListener;
    private Activity mActivity;

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HttpRequestTask(this).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item, null);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

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

        private final ListFragment mFragment;

        public HttpRequestTask(ListFragment fragment) {
            mFragment = fragment;
        }

        @Override
        protected Sync doInBackground(Void... params) {
            try {
                final String host = SettingsFragment.getPref(mActivity,
                        SettingsFragment.PREF_KEY_IP_ADDRESS);
                final String url = "http://" + host + "/sync";
                Log.d(TAG, "URL: " + url);
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
        protected void onPostExecute(final Sync sync) {
            if (sync != null) {
                ArrayAdapter<Item> adapter = new ArrayAdapter<>(mActivity,
                        android.R.layout.simple_list_item_1, android.R.id.text1, sync.getInv());
                adapter.notifyDataSetChanged();
                setListAdapter(adapter);
            } else {
                Toast.makeText(mActivity, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
