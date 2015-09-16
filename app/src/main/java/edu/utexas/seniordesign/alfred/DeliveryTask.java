package edu.utexas.seniordesign.alfred;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.utexas.seniordesign.alfred.models.Delivery;
import edu.utexas.seniordesign.alfred.models.Item;

public class DeliveryTask extends AsyncTask<String, Void, Item> {

    private static final String TAG = "DeliveryTask";
    private DeliveryFragment fragment;
    private Delivery delivery;

    public DeliveryTask(DeliveryFragment fragment, Delivery d) {
        this.fragment = fragment;
        this.delivery = d;
    }

    /** progress bar to show user that the backup is processing. */
    /**
     * application context.
     */
    @Override
    protected void onPreExecute() {}

    @Override
    protected Item doInBackground(final String... args) {
        Item result = null;
        try {
            final String url = Constants.ALFRED_URL + "/deliver";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            result = restTemplate.postForObject(url, delivery, Item.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(final Item result) {
        DeliveryFragment.OnFragmentInteractionListener listener = fragment.getListener();
        if (listener != null) {
            listener.onDeliveryFragmentInteraction(result);
        }
    }

}