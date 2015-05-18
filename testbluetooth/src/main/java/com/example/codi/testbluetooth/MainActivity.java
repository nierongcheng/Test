package com.example.codi.testbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private BluetoothAdapter mAdapter;
    private ToggleButton mToggleBtn;
    private ListView mDeviceLv;
    private ArrayList<BluetoothDevice> mDevices;
    private DeviceAdapter mDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);
        mDeviceLv = (ListView) findViewById(R.id.deviceLv);

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mAdapter == null) {
            Toast.makeText(this, "your device not support bluetooth!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(!mAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Const.REQUEST_CODE_ENABLE_BLUETOOTH);

            mToggleBtn.setChecked(false);
        } else {
            mToggleBtn.setChecked(true);
            findDevices();
        }

        mToggleBtn.setOnCheckedChangeListener(this);
        mDevices = new ArrayList<>();
        mDeviceAdapter = new DeviceAdapter(mDevices, this);
        mDeviceLv.setAdapter(mDeviceAdapter);
    }

    /**
     * 查询已绑定的蓝牙设备
     * @return
     */
    private Set<BluetoothDevice> queryBondedDevices() {
        Set<BluetoothDevice> bondedDevices = mAdapter.getBondedDevices();
        if(bondedDevices == null || bondedDevices.size() == 0) {
            return null;
        }
        return bondedDevices;
    }

    public void findDevices() {
        mDevices.clear();
        Set<BluetoothDevice> bondedDevices = queryBondedDevices();
        if (bondedDevices != null) {
            mDevices.addAll(bondedDevices);
            mDeviceAdapter.notifyDataSetChanged();
        }
        // 开始搜索
        mAdapter.startDiscovery();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!mDevices.contains(device)) {
                    mDevices.add(device);
                    mDeviceAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, intentFilter);
    }

    public void unregisterBroadcast() {
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == Const.REQUEST_CODE_ENABLE_BLUETOOTH) {

            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            mAdapter.enable();
            findDevices();
        } else {
            // 停止搜索
            if(mAdapter.isDiscovering()) {
                mAdapter.cancelDiscovery();
            }
            // 关掉蓝牙
            mAdapter.disable();

            mDevices.clear();
            mDeviceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBroadcast();
    }
}
