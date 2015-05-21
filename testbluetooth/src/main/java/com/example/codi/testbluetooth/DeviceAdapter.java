package com.example.codi.testbluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        Holder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_device_item, parent, false);
            holder = new Holder();
            holder.mName = (TextView) convertView.findViewById(R.id.device_name_item);
            holder.mAddress = (TextView) convertView.findViewById(R.id.device_address_item);
            holder.mStatus = (TextView) convertView.findViewById(R.id.device_status_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        BluetoothDevice device = mDevices.get(position);
        holder.mName.setText(device.getName());
        holder.mAddress.setText(device.getAddress());
        holder.mStatus.setText(String.valueOf(device.getBondState()));

        return convertView;
    }

    static class Holder {
        TextView mName;
        TextView mAddress;
        TextView mStatus;
    }

}
