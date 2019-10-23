package com.gt.go4lunch.utiltests;

import android.content.Context;
import android.content.res.Resources;

import com.gt.go4lunch.data.Close;
import com.gt.go4lunch.data.Open;
import com.gt.go4lunch.data.Period;
import com.gt.go4lunch.utils.OpeningHoursCalculator;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.joda.time.DateTimeUtils.setCurrentMillisSystem;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OpeningHoursCalculatorTests {

    private List<Period> listPeriod = new ArrayList<>();

    @Before
    public void addPeriodsInList() {

        Period period1 = new Period(
                new Close(
                        5,
                        "1400"
                ),
                new Open(
                        5,
                        "1100"
                )
        );
        Period period2 = new Period(
                new Close(
                        5,
                        "2200"
                ),
                new Open(
                        5,
                        "1800"
                )
        );

        Period period3 = new Period(
                new Close(
                        0,
                        "1300"
                ),
                new Open(
                        0,
                        "1000"
                )
        );
        Period period4 = new Period(
                new Close(
                        0,
                        "2300"
                ),
                new Open(
                        0,
                        "1800"
                )
        );

        Period period5 = new Period(
                new Close(
                        2,
                        "1430"
                ),
                new Open(
                        2,
                        "1100"
                )
        );
        Period period6 = new Period(
                new Close(
                        2,
                        "2230"
                ),
                new Open(
                        2,
                        "1830"
                )
        );

        Period period7 = new Period(
                new Close(
                        6,
                        "2345"
                ),
                new Open(
                        6,
                        "1145"
                )
        );

        Period period9 = new Period(
                new Close(
                        1,
                        "1438"
                ),
                new Open(
                        1,
                        "1138"
                )
        );
        Period period10 = new Period(
                new Close(
                        1,
                        "2238"
                ),
                new Open(
                        1,
                        "1838"
                )
        );
        Period period11 = new Period(
                null,
                new Open(
                        3,
                        "0000"
                )
                );

        Period period12 = new Period(
                new Close(
                        4,
                        "0000"
                ),
                null
        );

        listPeriod.add(period1);
        listPeriod.add(period2);
        listPeriod.add(period3);
        listPeriod.add(period4);
        listPeriod.add(period5);
        listPeriod.add(period6);
        listPeriod.add(period7);
        listPeriod.add(period9);
        listPeriod.add(period10);
        listPeriod.add(period11);
        listPeriod.add(period12);

    }

    @Before
    public void prepareTimeBeforeTests() {
        Context context = mock(Context.class);
        Context appContext = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(resources.openRawResource(anyInt())).thenReturn(mock(InputStream.class));
        when(appContext.getResources()).thenReturn(resources);
        when(context.getApplicationContext()).thenReturn(appContext);
        JodaTimeAndroid.init(context);
    }

    @After
    public void resetTimeSystem(){
        setCurrentMillisSystem();
    }

    @Test
    public void shouldReturnStringOpenUntil14h00(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when friday 12h12
        DateTimeUtils.setCurrentMillisFixed(1570788720000L);

        //then
        Assert.assertEquals("Open until 14h00", calculator.getStringOpenUntilOrClose());

    }

    @Test
    public void shouldReturnStringOpenUntil23h00(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when sunday 20h12
        DateTimeUtils.setCurrentMillisFixed(1570990320000L);

        //then
        Assert.assertEquals("Open until 23h00", calculator.getStringOpenUntilOrClose());

    }

    @Test
    public void shouldReturnStringOpenUntil14h38(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when monday 12h12
        DateTimeUtils.setCurrentMillisFixed(1571047920000L);

        //then
        Assert.assertEquals("Open until 14h38", calculator.getStringOpenUntilOrClose());

    }

    @Test
    public void shouldReturnStringClosedWhenNoOpenPeriod(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when thursday 20h12
        DateTimeUtils.setCurrentMillisFixed(1571335920000L);

        //then
        Assert.assertEquals("Closed", calculator.getStringOpenUntilOrClose());

    }

    @Test
    public void shouldReturnStringClosedWhenBetweenOpenPeriod(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when tuesday 17h12
        DateTimeUtils.setCurrentMillisFixed(1571152320000L);

        //then
        Assert.assertEquals("Closed", calculator.getStringOpenUntilOrClose());

    }

    @Test
    public void shouldReturnStringOpen24(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when wednesday 20h12
        DateTimeUtils.setCurrentMillisFixed(1570644720000L);

        //then
        Assert.assertEquals("Open 24/24", calculator.getStringOpenUntilOrClose());
    }

    @Test
    public void shouldReturnOpenUntil23h45WhenOneOpeningPeriod(){

        //given
        OpeningHoursCalculator calculator = new OpeningHoursCalculator(listPeriod);

        //when saturday 20h12
        DateTimeUtils.setCurrentMillisFixed(1571508720000L);

        //then
        Assert.assertEquals("Open until 23h45", calculator.getStringOpenUntilOrClose());
    }

}
