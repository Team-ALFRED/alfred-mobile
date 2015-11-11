package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import edu.utexas.seniordesign.alfred.models.Delivery;
import edu.utexas.seniordesign.alfred.models.Item;

public class DeliveryTask extends AsyncTask<String, Void, Item> {

    private static final String TAG = "DeliveryTask";
    private DeliveryFragment fragment;
    private Delivery delivery;
    private final String url;
    private final String secret_token;

    public DeliveryTask(Activity activity, DeliveryFragment fragment, Delivery d) {
        this.fragment = fragment;
        this.delivery = d;

        final String host = SettingsFragment.getPref(activity,
                SettingsFragment.PREF_KEY_IP_ADDRESS);
        url = "http://" + host + "/deliver";
        secret_token = SettingsFragment.getPref(activity,
                SettingsFragment.PREF_KEY_SECRET_TOKEN);
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
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", secret_token);
            HttpEntity<Delivery> request = new HttpEntity<>(delivery, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Item> response = restTemplate.exchange(url, HttpMethod.POST, request, Item.class);
            result = response.getBody();
        } catch (HttpClientErrorException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(final Item result) {
        DeliveryFragment.OnFragmentInteractionListener listener = fragment.getListener();
        if (listener != null && !isCancelled()) {
            listener.onDeliveryFragmentInteraction(result);
        }
    }

}