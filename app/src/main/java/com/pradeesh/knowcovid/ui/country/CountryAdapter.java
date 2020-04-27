package com.pradeesh.knowcovid.ui.country;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.pradeesh.knowcovid.model.Countries;
import com.pradeesh.knowcovid.R;


import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<Countries> countries;
    private Context context;
    private OnItemClickListener onItemClickListener;


    public CountryAdapter(Context context) {
        // this.countries = countries;
        this.context = context;
        this.countries = new ArrayList<>();
        this.countries.addAll(CountryFragment.countriesList);
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holders, int position) {
        final CountryViewHolder holder = holders;

        Countries country = countries.get(position);


        holder.textView_recovered.setText(String.format("%,d", country.getRecovered()));
        holder.textView_country.setText(country.getCountry());
        if (country.getTodayDeaths() != 0) {
            holder.textView_todayDeaths.setText("(+" + String.format("%,d", country.getTodayDeaths()) + ")");
        }

        holder.textView_numberCases.setText(String.format("%,d", country.getCases()));
        if (country.getTodayCases() != 0) {
            holder.textView_todayCases.setText("(+" + String.format("%,d", country.getTodayCases()) + ")");
        }

        holder.textView_deaths.setText(String.format("%,d", country.getDeaths()));
        Glide.with(context).load(country.getCountryInfo().getFlag()).into(holder.flag);


    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * When data changes, this method updates the list of clipEntries
     * and notifies the adapter to use the new values on it
     */
    public void setClips(List<Countries> mCountries) {
        countries = mCountries;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // Filter Class - utilised in v2
    public void filter(String charText) {
        charText = charText.toLowerCase();

        //countries.clear();
        //countries = DashboardFragment.countriesList;
        CountryFragment.countriesList.clear();
        if (charText.length() == 0) {
            CountryFragment.countriesList.addAll(countries);
            // countries.addAll(DashboardFragment.countriesList);
        } else {
            for (Countries wp : countries) {
                if (wp.getCountry().toLowerCase().contains(charText)) {
                    CountryFragment.countriesList.add(wp);
                    //countries.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView textView_country, textView_numberCases, textView_todayCases, textView_deaths, textView_todayDeaths, textView_recovered;
        ImageView flag;
        OnItemClickListener onItemClickListener;

        public CountryViewHolder(View itemView) {

            super(itemView);


            textView_country = itemView.findViewById(R.id.textView_country);
            textView_numberCases = itemView.findViewById(R.id.number_of_cases);
            textView_todayCases = itemView.findViewById(R.id.today_cases);
            textView_deaths = itemView.findViewById(R.id.number_of_deaths);
            textView_todayDeaths = itemView.findViewById(R.id.todays_deaths);
            textView_recovered = itemView.findViewById(R.id.number_of_recovered);
            flag = itemView.findViewById(R.id.flag);


            this.onItemClickListener = onItemClickListener;

        }


    }
}
