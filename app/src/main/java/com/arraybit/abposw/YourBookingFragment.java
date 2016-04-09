package com.arraybit.abposw;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arraybit.adapter.BookingAdapter;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BookingMaster;
import com.arraybit.parser.BookingJSONParser;

import java.util.ArrayList;

public class YourBookingFragment extends Fragment implements View.OnClickListener, BookingJSONParser.BookingRequestListener, BookingAdapter.BookingOnClickListener {

    RecyclerView rvBooking;
    FloatingActionButton fabBooking;
    LinearLayout headerLayout;
    LinearLayoutManager linearLayoutManager;
    BookingAdapter adapter;
    View view;
    int currentPage = 1, position;
    SharePreferenceManage objSharePreferenceManage = new SharePreferenceManage();
    com.arraybit.abposw.ProgressDialog progressDialog = new ProgressDialog();

    public YourBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_booking, container, false);
        Toolbar app_bar = (Toolbar) view.findViewById(R.id.app_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);

        if (app_bar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(app_bar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                app_bar.setElevation(getActivity().getResources().getDimension(R.dimen.app_bar_elevation));
            }
        }
        app_bar.setTitle(getResources().getString(R.string.title_fragment_your_booking));
        setHasOptionsMenu(true);

        rvBooking = (RecyclerView) view.findViewById(R.id.rvBooking);
        fabBooking = (FloatingActionButton) view.findViewById(R.id.fabBooking);
        headerLayout = (LinearLayout) view.findViewById(R.id.headerLayout);
        headerLayout.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        if (Service.CheckNet(getActivity())) {
            RequestBookingMaster();
        } else {
            Globals.ShowSnackBar(container, getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
        }

        fabBooking.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                .equals(getActivity().getResources().getString(R.string.title_fragment_your_booking))) {
            if (v.getId() == R.id.fabBooking) {
//            AddBookingFragment fragment2 = new AddBookingFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.linearlayoutAddBooking, fragment2);
//            fragmentTransaction.commit();
                Globals.ReplaceFragment(new AddBookingFragment(getActivity()), getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_add_booking_fragment), R.id.yourBookingFragment);
            }
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

    @Override
    public void AddBookingResponse(String errorCode) {
    }

    @Override
    public void UpdateBookingStatusResponse(String errorCode) {
        progressDialog.dismiss();
        SetError(errorCode);
    }

    @Override
    public void SelectBookingResponse(ArrayList<BookingMaster> alBookingMaster) {
        progressDialog.dismiss();
        SetRecyclerView(alBookingMaster);
    }

    @Override
    public void CancelClickListener(BookingMaster objBookingMaster, int position) {
        this.position = position;
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.UpdateBookingMaster(objBookingMaster, getActivity(), this);
    }

    //region Private Method
    private void RequestBookingMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectAllBookingMaster(getActivity(), this, String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId), objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
    }

    private void SetRecyclerView(ArrayList<BookingMaster> alBookingMaster) {
        if (alBookingMaster != null && alBookingMaster.size() > 0) {
            rvBooking.setVisibility(View.VISIBLE);

            adapter = new BookingAdapter(getActivity(), alBookingMaster, this);
            rvBooking.setAdapter(adapter);
            rvBooking.setLayoutManager(linearLayoutManager);
        }
    }

    private void SetError(String errorCode) {
        switch (errorCode) {
            case "-1":
                Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.MsgServerNotResponding), getActivity(), 1000);
                break;
            default:
                //Globals.ShowSnackBar(view, getActivity().getResources().getString(R.string.ybAddBookingSuccessMsg), getActivity(), 1000);
                adapter.UpdateBookingStatus(position);
                //(getActivity()).setResult(Activity.RESULT_OK);
                //(getActivity()).finish();
                break;
        }

    }
    //endregion
}
