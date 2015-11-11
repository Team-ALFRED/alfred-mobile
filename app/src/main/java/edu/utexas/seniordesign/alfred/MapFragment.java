package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";

    private OnFragmentInteractionListener mListener;
    private PhotoViewAttacher mAttacher;
    private Activity mActivity;

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

    public interface OnFragmentInteractionListener {
        public void onMapFragmentInteraction(Float[] id);
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {

        private ImageView map;

        public DownloadImageTask(ImageView map) {
            this.map = map;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap result = null;
            try {
                final String host = SettingsFragment.getPref(mActivity,
                        SettingsFragment.PREF_KEY_IP_ADDRESS);
                final String url = "http://" + host + "/map";
                Log.d(TAG, "URL: " + url);
                InputStream in = new java.net.URL(url).openStream();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                // options.inSampleSize = 2;
                options.inScaled = false;
                options.inDither = false;

                Bitmap decoded = BitmapFactory.decodeStream(in, null, options);
                result = Bitmap.createScaledBitmap(decoded, decoded.getWidth() * 4, decoded.getHeight() * 4, false);
                decoded.recycle();

                in.close();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                map.setImageBitmap(result);

                if (mAttacher != null) {
                    mAttacher.cleanup();
                }

                mAttacher = new PhotoViewAttacher(map);

                mAttacher.setMaximumScale(20.0f);
                // mAttacher.setScale(3.0f, 425, 775, true);

                mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View v, float x, float y) {
                        Float[] msg = new Float[2];
                        msg[0] = x;
                        msg[1] = y;
                        mListener.onMapFragmentInteraction(msg);
                    }
                });
            } else {
                Toast.makeText(mActivity, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
