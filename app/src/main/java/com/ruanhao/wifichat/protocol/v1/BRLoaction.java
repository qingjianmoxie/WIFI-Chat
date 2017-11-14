package com.ruanhao.wifichat.protocol.v1;

import com.google.gson.Gson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiang.shen on 2017年5月5日.
 *
 */
public class BRLoaction implements Parcelable {
	private double lon;
	private double lat;

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

}
