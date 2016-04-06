package com.arraybit.abposw;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arraybit.global.Globals;

public class YourBookingFragment extends Fragment implements View.OnClickListener {

    RecyclerView rvBooking;
    FloatingActionButton fabBooking;
    View view;

    public YourBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_booking, container, false);

        rvBooking = (RecyclerView) view.findViewById(R.id.rvBooking);
        fabBooking = (FloatingActionButton) view.findViewById(R.id.fabBooking);

        fabBooking.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()!=null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount()-1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_your_booking))) {
              if (v.getId() == R.id.fabBooking) {
//            AddBookingFragment fragment2 = new AddBookingFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.linearlayoutAddBooking, fragment2);
//            fragmentTransaction.commit();
                
            }

            Globals.ReplaceFragment(new AddBookingFragment(), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_add_booking_fragment), R.id.yourBookingFragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Globals.HideKeyBoard(getActivity(), getView());
            getActivity().getSupportFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

}
