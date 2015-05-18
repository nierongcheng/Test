package com.example.codi.testbluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Codi on 2015/5/18.
 */
public class DeviceAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> mDevices;
    private LayoutInflater mInflater;

    public DeviceAdapter(ArrayList<BluetoothDevice> devices, Context context) {
        this.mDevices = devices;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setDevices(ArrayList<BluetoothDevice> devices) {
        this.mDevices = devices;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
