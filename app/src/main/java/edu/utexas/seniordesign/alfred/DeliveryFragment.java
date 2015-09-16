package edu.utexas.seniordesign.alfred;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.utexas.seniordesign.alfred.models.Item;

public class DeliveryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DeliveryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delivery, container, false);
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

    public OnFragmentInteractionListener getListener() {
        return mListener;
    }

    public interface OnFragmentInteractionListener {
        public void onDeliveryFragmentInteraction(Item result);
    }

}
