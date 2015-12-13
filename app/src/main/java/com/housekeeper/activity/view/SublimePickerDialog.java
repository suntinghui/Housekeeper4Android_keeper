package com.housekeeper.activity.view;

import android.app.Dialog;
import android.content.Context;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.wufriends.housekeeper.keeper.R;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by sth on 9/30/15.
 */
public class SublimePickerDialog extends Dialog {

    // Date & Time formatter used for formatting
    // text on the switcher button
    DateFormat mDateFormatter, mTimeFormatter;

    // Picker
    SublimePicker mSublimePicker;

    // Callback to activity
    Callback mCallback;

    SublimeOptions options;

    public SublimePickerDialog(Context context, SublimeOptions options) {
        this(context, R.style.ProgressHUD, options);
    }

    public SublimePickerDialog(Context context, int theme, SublimeOptions options) {
        super(context, theme);

        this.initView(context, options);
    }

    private void initView(Context context, SublimeOptions options) {
        this.setContentView(R.layout.dialog_sublime_picker);

        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        mTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));

        mSublimePicker = (SublimePicker) this.findViewById(R.id.sublime_picker);
        mSublimePicker.initializePicker(options, mListener);
    }

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onCancelled() {
            if (mCallback!= null) {
                mCallback.onCancelled();
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }

        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimePicker,
                                            int year, int monthOfYear, int dayOfMonth,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            if (mCallback != null) {
                mCallback.onDateTimeRecurrenceSet(year, monthOfYear, dayOfMonth,
                        hourOfDay, minute, recurrenceOption, recurrenceRule);
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }

        // You can also override 'formatDate(Date)' & 'formatTime(Date)'
        // to supply custom formatters.
    };

    // Set activity callback
    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    // For communicating with the activity
    public interface Callback {
        void onCancelled();

        void onDateTimeRecurrenceSet(int year, int monthOfYear, int dayOfMonth,
                                     int hourOfDay, int minute,
                                     SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                     String recurrenceRule);
    }


}
