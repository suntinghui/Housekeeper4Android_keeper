package com.housekeeper.model;

import android.os.Parcel;

import com.ares.house.dto.app.EquipmentAppDto;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

/**
 * Created by sth on 9/25/15.
 */
public class EquipmentAppDtoEx extends EquipmentAppDto implements AsymmetricItem {

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
