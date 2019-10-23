package com.gt.go4lunch.utils;

import android.annotation.SuppressLint;

import com.gt.go4lunch.MainApplication;
import com.gt.go4lunch.R;
import com.gt.go4lunch.data.Period;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class OpeningHoursCalculator {

    private List<Period> mListPeriod;
    private List<Integer> mListOpenForToday = new ArrayList<>();
    private List<Integer> mListCloseForToday = new ArrayList<>();

    public OpeningHoursCalculator(List<Period> periods) {

        mListPeriod = periods;
    }

    @SuppressWarnings("ConstantConditions")
    public String getStringOpenUntilOrClose() {

        String mStringToReturn;
        if (mListPeriod != null) {

            int todayDay = getTodayNumber();

            try {

                for (Period period : mListPeriod) {


                    if (period.getOpen() != null && period.getOpen().getDay() == todayDay) {
                        mListOpenForToday.add(Integer.parseInt(period.getOpen().getTime()));
                    }

                    if (period.getClose() != null && period.getClose().getDay() == todayDay) {
                        mListCloseForToday.add(Integer.parseInt(period.getClose().getTime()));
                    }
                }

                if (mListOpenForToday.isEmpty()){
                    mStringToReturn = "Closed";
                    return mStringToReturn;
                }

                if (!mListOpenForToday.isEmpty() && mListCloseForToday.isEmpty()){
                    mStringToReturn = "Open 24/24";
                    return mStringToReturn;
                }

                int actualTime = getTimeNow();

                boolean uniqueOpeningInterval = true;

                int beginInterval = 0;
                int endInterval = 0;
                int intervalForBegin = 1;
                int intervalForEnd = 1;

                // if 2 intervals for opening
                //intervalForBegin & intervalForEnd must be different to indicate same true interval : for loop for listOpen is the reverse of for loop for listClose.
                //else must be the same

                for (int i = mListOpenForToday.size() - 1; i >= 0; i--) {
                    if (actualTime > mListOpenForToday.get(i)) {
                        beginInterval = mListOpenForToday.get(i);
                        break;
                    }
                    uniqueOpeningInterval = false;
                    intervalForBegin++;
                }

                for (int closeTime : mListCloseForToday) {
                    if (actualTime < closeTime) {
                        endInterval = closeTime;
                        break;
                    }
                    uniqueOpeningInterval = false;
                    intervalForEnd++;
                }

                if (actualTime > beginInterval && actualTime < endInterval && (intervalForBegin != intervalForEnd || uniqueOpeningInterval)) {

                    @SuppressLint("DefaultLocale") String endTime4Digits = String.format("%04d", endInterval);

                    mStringToReturn = "Open until " + endTime4Digits.substring(0,2) + "h" + endTime4Digits.substring(2,4);
                } else {
                    mStringToReturn = "Closed";
                }

            } catch (NullPointerException e) {
                mStringToReturn = MainApplication.instanceApp.getString(R.string.no_opening_hours);
            }
        } else {
            mStringToReturn = MainApplication.instanceApp.getString(R.string.no_opening_hours);
        }

        return mStringToReturn;
    }

    private int getTodayNumber() {

        return new DateTime().getDayOfWeek() % 7;
    }

    private int getTimeNow() {

        int actualHour = new DateTime().getHourOfDay();
        int actualMinute = new DateTime().getMinuteOfHour();

        String actualTime =  "" + actualHour + actualMinute;

        return Integer.parseInt(actualTime);
    }
}
