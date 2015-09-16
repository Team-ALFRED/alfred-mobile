package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;

import edu.utexas.seniordesign.alfred.dummy.DummyContent;
import edu.utexas.seniordesign.alfred.models.Item;
import edu.utexas.seniordesign.alfred.models.Sync;

public class MapFragment extends ListFragment {
    private static final String TAG = "MapFragment";

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new HttpRequestTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        ImageView mapView = (ImageView) v.findViewById(R.id.imageViewMap);
        new DownloadImageTask(mapView).execute();
        return v;
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
            mListener.onMapFragmentInteraction(getListAdapter().getItem(position).toString());
        }
    }

    public interface OnFragmentInteractionListener {
        public void onMapFragmentInteraction(String id);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, sync.getMap());
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);
        }
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView map;

        public DownloadImageTask(ImageView map) {
            this.map = map;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                InputStream in = new java.net.URL("http://alfred.lf.lc/map").openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                map.setImageBitmap(result);
            }
        }
    }
}
