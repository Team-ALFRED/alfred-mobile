package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";

    private OnFragmentInteractionListener mListener;
    private PhotoViewAttacher mAttacher;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public interface OnFragmentInteractionListener {
        public void onMapFragmentInteraction(String id);
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView map;

        public DownloadImageTask(ImageView map) {
            this.map = map;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                InputStream in = new java.net.URL(Constants.ALFRED_URL + "/map").openStream();
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
                mAttacher = new PhotoViewAttacher(map);
                mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View v, float x, float y) {
                        String msg = String.format("%g:%g", x * v.getWidth(), y * v.getHeight());
                        mListener.onMapFragmentInteraction(msg);
                    }
                });
            }
        }
    }
}
