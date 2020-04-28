package com.pradeesh.knowcovid.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pradeesh.knowcovid.model.TotalCases;
import com.pradeesh.knowcovid.R;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.pradeesh.knowcovid.utils.Constant.ERROR;
import static com.pradeesh.knowcovid.utils.Constant.FAILURE;
import static com.pradeesh.knowcovid.utils.Constant.SUCCESS;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private HomeViewModel homeViewModel;
    private TextView textView_numberOfDeaths, textView_numberOfCases, textView_numberOfRecovered;
    private TextView textView_deaths, textView_cases, textView_recovered;
    private ImageView imageView_recovered, imageView_cases, imageView_deaths;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    private Toolbar toolbar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initHomeFragmentUIComponents(root);

        homeViewModel.getLocal().observe(getViewLifecycleOwner(), new Observer<TotalCases>() {
            @Override
            public void onChanged(TotalCases totalCases) {
                setUiBasedOnStatus(HomeViewModel.status);
                if (totalCases != null) {
                    textView_numberOfCases.setText("" + String.format("%,d", totalCases.getCases()));
                    textView_numberOfDeaths.setText("" + String.format("%,d", totalCases.getDeaths()));
                    textView_numberOfRecovered.setText("" + String.format("%,d", totalCases.getRecovered()));
                }
            }
        });

        return root;
    }

    private void initHomeFragmentUIComponents(View root) {

        textView_numberOfCases = root.findViewById(R.id.number_of_cases);
        textView_numberOfDeaths = root.findViewById(R.id.number_of_deaths);
        textView_numberOfRecovered = root.findViewById(R.id.number_of_recovered);

        textView_deaths = root.findViewById(R.id.textView_deaths);
        textView_cases = root.findViewById(R.id.textView_cases);
        textView_recovered = root.findViewById(R.id.textView_recovered);

        imageView_cases = root.findViewById(R.id.imageView);
        imageView_deaths = root.findViewById(R.id.imageDeath);
        imageView_recovered = root.findViewById(R.id.imageRecovered);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        errorLayout = root.findViewById(R.id.errorLayout);
        errorImage = root.findViewById(R.id.errorImage);
        errorMessage = root.findViewById(R.id.errorMessage);
        btnRetry = root.findViewById(R.id.btnRetry);

        toolbar = root.findViewById(R.id.toolbar);
    }

    private void setUiBasedOnStatus(String value) {
        switch (value) {
            case SUCCESS:
                Log.d(TAG, "error is : " + HomeViewModel.errorCode);
                Log.d(TAG, "status is: " + HomeViewModel.status);
                swipeRefreshLayout.setRefreshing(false);
                HomeViewModel.status = "";
                break;
            case FAILURE:
                Log.d(TAG, "error is : " + HomeViewModel.errorCode);
                Log.d(TAG, "status is: " + HomeViewModel.status);
                swipeRefreshLayout.setRefreshing(false);
                HomeViewModel.status = "";
                showErrorMessage(
                        R.drawable.oops,
                        "Oops..",
                        getString(R.string.error_network) +
                                HomeViewModel.errorCode);
                break;
            case ERROR:
                Log.d(TAG, "error is : " + HomeViewModel.errorCode);
                Log.d(TAG, "status is: " + HomeViewModel.status);

                HomeViewModel.status = "";
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.no_result,
                        getString(R.string.no_result),
                        getString(R.string.retry) +
                                HomeViewModel.errorCode);
                break;
            default:
                HomeViewModel.status = "";
                break;

        }
    }

    private void hideMainLayout() {
        imageView_recovered.setVisibility(View.GONE);
        imageView_deaths.setVisibility(View.GONE);
        imageView_cases.setVisibility(View.GONE);
        textView_recovered.setVisibility(View.GONE);
        textView_cases.setVisibility(View.GONE);
        textView_deaths.setVisibility(View.GONE);
        textView_numberOfRecovered.setVisibility(View.GONE);
        textView_numberOfDeaths.setVisibility(View.GONE);
        textView_numberOfCases.setVisibility(View.GONE);
    }

    private void revealMainLayout() {
        imageView_recovered.setVisibility(View.VISIBLE);
        imageView_deaths.setVisibility(View.VISIBLE);
        imageView_cases.setVisibility(View.VISIBLE);
        textView_deaths.setVisibility(View.VISIBLE);
        textView_cases.setVisibility(View.VISIBLE);
        textView_recovered.setVisibility(View.VISIBLE);
        textView_numberOfCases.setVisibility(View.VISIBLE);
        textView_numberOfDeaths.setVisibility(View.VISIBLE);
        textView_numberOfRecovered.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(int imageView, String title, String message) {
        Log.d("TAG", "showError called");
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            hideMainLayout();
        }

        errorImage.setImageResource(imageView);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartApp();
                errorLayout.setVisibility(View.GONE);
                revealMainLayout();

            }
        });

    }

    @Override
    public void onRefresh() {
        restartApp();
    }

    private void restartApp() {
        getActivity().recreate();
    }
}
