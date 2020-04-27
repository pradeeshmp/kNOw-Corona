package com.pradeesh.knowcovid.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.pradeesh.knowcovid.model.Countries;

import java.util.List;

public class CountriesDiffCallback extends DiffUtil.Callback {
    private final List<Countries> mOldCountriesList;
    private final List<Countries> mNewCountriesList;

    public CountriesDiffCallback(List<Countries> mOldCountriesList, List<Countries> mNewCountriesList) {
        this.mOldCountriesList = mOldCountriesList;
        this.mNewCountriesList = mNewCountriesList;
    }

    @Override
    public int getOldListSize() {
        return mOldCountriesList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewCountriesList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCountriesList.get(oldItemPosition).getCountryId() == mNewCountriesList
                .get(newItemPosition).getCountryId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Countries oldCountries = mOldCountriesList.get(oldItemPosition);
        final Countries newCountries = mNewCountriesList.get(newItemPosition);

        return oldCountries.getCases()==(newCountries.getCases());

    }
}
