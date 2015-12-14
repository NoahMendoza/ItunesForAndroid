package com.example.noah.itunesforandroid.movies;

/**
 * Created by Noah on 12/13/2015.
 */
public class Movie {

    String movieName;
    String rating;
    String director;
    String explicit;
    String genre;
    String shortDescription;
    String longDescription;
    String releaseDate;
    double hdPrice;
    double regularPrice;
    double rentalPrice;
    double hdRentalPrice;
    String runTime;
    String imageUrl;


    public Movie( String movieName, String rating, String director,String explicit, String genre,
                  String shortDescription, String longDescription,String releaseDate, double hdPrice,
                  double regularPrice, double rentalPrice, double hdRentalPrice, String runTime, String imageUrl)
    {

        this.runTime = runTime;
        this.movieName = movieName;
        this.rating = rating;
        this.director = director;
        this.explicit = explicit;
        this.genre = genre;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.releaseDate = releaseDate;
        this.hdPrice = hdPrice;
        this.regularPrice = regularPrice;
        this.rentalPrice = rentalPrice;
        this.hdRentalPrice = hdRentalPrice;
        this.imageUrl = imageUrl;
    }

    public String getRating() {
        return rating;
    }

    public String getImageUrl() { return imageUrl; }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getExplicit() {
        return explicit;
    }

    public void setExplicit(String explicit) {
        this.explicit = explicit;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getHdPrice() {
        return hdPrice;
    }

    public void setHdPrice(double hdPrice) {
        this.hdPrice = hdPrice;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public double getHdRentalPrice() {
        return hdRentalPrice;
    }

    public void setHdRentalPrice(double hdRentalPrice) {
        this.hdRentalPrice = hdRentalPrice;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
}

