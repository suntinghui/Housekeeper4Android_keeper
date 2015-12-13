package com.housekeeper.model;

import android.os.Parcel;

import com.ares.house.dto.app.RentContainAppDto;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

/**
 * Created by sth on 12/7/15.
 */
public class RentContainAppDtoEx extends RentContainAppDto implements AsymmetricItem {

    @Override
    public int getColumnSpan() {
        return 1;
    }

    @Override
    public int getRowSpan() {
        return 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
