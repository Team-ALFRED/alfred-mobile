package edu.utexas.seniordesign.alfred;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import edu.utexas.seniordesign.alfred.models.Delivery;
import edu.utexas.seniordesign.alfred.models.Item;

public class MainActivity extends FragmentActivity implements
        ItemFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener,
        DeliveryFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private String item = null;
    private String loc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Main activity created!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ItemFragment firstFragment = new ItemFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    private void replaceFragment(Fragment fragment) {
        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        getSupportFragmentManager().popBackStack();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onItemFragmentInteraction(String id) {
        // TODO: handle item selection
        Log.i(TAG, "Got item fragment interaction with id: " + id);
        item = id;

        replaceFragment(new MapFragment());
    }

    @Override
    public void onMapFragmentInteraction(String id) {
        // TODO: handle location selection
        Log.i(TAG, "Got location fragment interaction with id: " + id);
        loc = id;

        DeliveryFragment fragment = new DeliveryFragment();
        Delivery delivery = new Delivery(item, loc);
        new DeliveryTask(fragment, delivery).execute(item, loc);
        replaceFragment(fragment);
    }

    @Override
    public void onDeliveryFragmentInteraction(Item result) {
        // TODO: handle delivery selection
        if (result.getError() != null) {
            Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "Got delivery fragment interaction with result: " + result);

        replaceFragment(new ItemFragment());
        clearBackStack();
    }
}
