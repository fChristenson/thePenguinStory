package se.fidde.thepenguinstory.domain.penguin;

import java.io.Serializable;

public class PenguinLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    private double longitude;
    private double latitude;

    public PenguinLocation(double longitude, double latitude) {
        setLongitude(longitude);
        setLatitude(latitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        double epsilon = 0.0001;
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PenguinLocation other = (PenguinLocation) obj;
        if (Math.abs(longitude - other.getLongitude()) > epsilon)
            return false;
        if (Math.abs(latitude - other.getLatitude()) > epsilon)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PenguinLocation [longitude=" + longitude + ", latitude="
                + latitude + "]";
    }

}
