package com.example.codeblocks.views.blocks;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class ViewPoint extends Point {

    public ViewPoint() {
        super();
    }

    public ViewPoint(Point point) {
        super(point);
    }

    public ViewPoint(int x, int y) {
        super(x, y);
    }

    public void setFrom(ViewPoint other) {
        x = other.x;
        y = other.y;
    }

    public static final Parcelable.Creator<ViewPoint> CREATOR
            = new Parcelable.Creator<ViewPoint>() {
        public ViewPoint createFromParcel(Parcel in) {
            ViewPoint r = new ViewPoint();
            r.readFromParcel(in);
            return r;
        }

        public ViewPoint[] newArray(int size) {
            return new ViewPoint[size];
        }
    };
}
