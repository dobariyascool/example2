package com.arraybit.abposw;

import android.content.Context;
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
import com.arraybit.global.EndlessRecyclerOnScrollListener;
import com.arraybit.global.Globals;
import com.arraybit.global.Service;
import com.arraybit.global.SharePreferenceManage;
import com.arraybit.modal.BookingMaster;
import com.arraybit.parser.BookingJSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class YourBookingFragment extends Fragment implements View.OnClickListener, BookingJSONParser.BookingRequestListener, BookingAdapter.BookingOnClickListener, AddBookingFragment.AddNewBookingListener {

    RecyclerView rvBooking;
    FloatingActionButton fabBooking;
    LinearLayout headerLayout, errorLayout;
    LinearLayoutManager linearLayoutManager;
    BookingAdapter adapter;
    ArrayList<BookingMaster> alBookingMaster = new ArrayList<>();
    View view;
    Context context;
    int currentPage = 1, position, cnt;
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
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
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
                AddBookingFragment addBookingFragment = new AddBookingFragment(getActivity());
                addBookingFragment.setTargetFragment(this, 0);
                Globals.ReplaceFragment(addBookingFragment, getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.title_add_booking_fragment), R.id.yourBookingFragment);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getTargetFragment() != null) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName() != null
                    && getActivity().getSupportFragmentManager().getBackStackEntryAt(getActivity().getSupportFragmentManager().getBackStackEntryCount() - 1).getName()
                    .equals(getActivity().getResources().getString(R.string.title_fragment_your_booking))) {
                Globals.HideKeyBoard(getActivity(), getView());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        rvBooking.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!adapter.isItemAnimate) {
                    adapter.isItemAnimate = true;
                }
            }
        });

        rvBooking.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (!adapter.isItemAnimate) {
                    adapter.isItemAnimate = true;
                }
                if (current_page > currentPage) {
                    currentPage = current_page;
                    if (Service.CheckNet(getActivity())) {
                        cnt = 0;
                        RequestBookingMaster();
                    } else {
                        Globals.ShowSnackBar(rvBooking, getActivity().getResources().getString(R.string.MsgCheckConnection), getActivity(), 1000);
                    }
                }
            }
        });
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
        this.alBookingMaster = alBookingMaster;
        SetRecyclerView();
    }

    @Override
    public void CancelClickListener(BookingMaster objBookingMaster, int position) {
        this.position = position;
        progressDialog.show(getActivity().getSupportFragmentManager(), "");
        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.UpdateBookingMaster(objBookingMaster, getActivity(), this);
    }

    @Override
    public void AddNewBooking(BookingMaster objBookingMaster) {
        Date dt;
        SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        SimpleDateFormat DisplayTimeFormat = new SimpleDateFormat(Globals.DisplayTimeFormat,Locale.US);
        try {
            dt = sdfTimeFormat.parse(objBookingMaster.getFromTime());
            objBookingMaster.setFromTime(DisplayTimeFormat.format(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            dt = sdfTimeFormat.parse(objBookingMaster.getToTime());
            objBookingMaster.setToTime(DisplayTimeFormat.format(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(objBookingMaster!=null){
            currentPage = 1;
            if(alBookingMaster==null){
                alBookingMaster = new ArrayList<>();
                alBookingMaster.add(0,objBookingMaster);
                SetRecyclerView();
            }else {
                alBookingMaster.add(0, objBookingMaster);
                SetRecyclerView();
            }
        }
    }

    //region Private Method
    private void RequestBookingMaster() {
        progressDialog.show(getActivity().getSupportFragmentManager(), "");

        BookingJSONParser objBookingJSONParser = new BookingJSONParser();
        objBookingJSONParser.SelectAllBookingMaster(getActivity(), this, String.valueOf(currentPage), String.valueOf(Globals.linktoBusinessMasterId), objSharePreferenceManage.GetPreference("LoginPreference", "CustomerMasterId", getActivity()));
    }

    private void SetRecyclerView() {
        rvBooking.setVisibility(View.VISIBLE);

        if (alBookingMaster == null) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, context.getResources().getString(R.string.MsgSelectFail), rvBooking);
            }
        } else if (alBookingMaster.size() == 0) {
            if (currentPage == 1) {
                Globals.SetErrorLayout(errorLayout, true, context.getResources().getString(R.string.MsgNoRecord), rvBooking);
            }
        } else {
            Globals.SetErrorLayout(errorLayout, false, null, rvBooking);
            if (currentPage > 1) {
                adapter.BookingDataChanged(alBookingMaster);
                return;
            } else if (alBookingMaster.size() < 10) {
                currentPage += 1;
            }
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
