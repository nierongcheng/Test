package com.example.codi.testbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {

    private static final String NAME = "xiao";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mAdapter;
    private ToggleButton mToggleBtn;
    private ListView mDeviceLv;
    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
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

        mToggleBtn.setOnCheckedChangeListener(this);

        if(!mAdapter.isEnabled()) {
            // 请求打开蓝牙设备
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, Const.REQUEST_CODE_ENABLE_BLUETOOTH);

            mToggleBtn.setChecked(false);
        } else {
            mToggleBtn.setChecked(true);
            findDevices();
        }

        mDeviceAdapter = new DeviceAdapter(mDevices, this);
        mDeviceLv.setAdapter(mDeviceAdapter);
        mDeviceLv.setOnItemClickListener(this);
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

    /**
     * 打开设备发现，使其能被其它的设备扫描到
     */
    public void openDiscoverable() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // 设置被发现时间为300s
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
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
            } else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                if(state == BluetoothAdapter.STATE_ON) {
                    findDevices();
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice bluetoothDevice = mDevices.get(position);
        new ConnectThread(bluetoothDevice).start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice mDevice;
        private final BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice device) {
            this.mDevice = device;

            BluetoothSocket tmp = null;
            try {
                tmp = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mSocket = tmp;

        }

        @Override
        public void run() {
            // 在连接之前关闭查找
            mAdapter.cancelDiscovery();
            try {
                if(!this.mSocket.isConnected()) {
                    this.mSocket.connect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                cancel();
            }

            // TODO 操作socket
        }

        public void cancel() {
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mServerSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mServerSocket.accept();
                } catch (IOException e) {
                    break;
                }

                if (socket != null) {
                    // TODO 操作socket

                    cancel();
                    break;
                }
            }
        }

        public void cancel() {
            if (mServerSocket != null) {
                try {
                    mServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mInStream = tmpIn;
            mOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    Toast.makeText(MainActivity.this, new String(buffer, 0, bytes), Toast.LENGTH_SHORT).show();
//                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) { }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            mAdapter.enable();

//            mDeviceLv.getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    findDevices();
//                }
//            }, 3000);
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

    public void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
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
