package com.gt.go4lunch.utils;

public class RatingCalculator {

    private double mRating;

    public RatingCalculator(double rating){
        mRating = rating;
    }

    public int getHowManyStars(){
        return (int) Math.round(3 * mRating / 5);
    }
}
