package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Set;

/**
 * Created by shane.bruggeman on 7/26/15.
 */
public class WalkbyBluetoothManager {
    private BluetoothAdapter mBluetoothAdapter;
    private String BLUETOOTH_DEBUG = "BLUETOOTH_DEBUG";
    private int REQUEST_ENABLE_BT = 1;
    private ArrayAdapter mArrayAdapter;

    public WalkbyBluetoothManager(ArrayAdapter adapter) {
        mArrayAdapter = adapter;
    }

    public boolean connect(Activity activity) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d(BLUETOOTH_DEBUG, "This device does not support bluetooth");
            return false;
        }

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //check to see if user's mac address is already registered for them
        String myMacAddress = mBluetoothAdapter.getAddress();

        findDiscoverables();
        return true;
    }

    public void refresh() {
        findDiscoverables();
    }

    public void enableDiscovery(Activity activity) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
    }

    public void findDiscoverables() {
        Set<BluetoothDevice> visibleDevices = mBluetoothAdapter.getBondedDevices();
        Log.d("log1", "My mac address is: " + mBluetoothAdapter.getAddress());

        for(BluetoothDevice device : visibleDevices) {
            String mac_address = device.getAddress();
            String device_name = device.getName();
            Log.d("log1", device_name + ": " + mac_address);
        }

        Log.d("log1","" + visibleDevices.size());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("log1","requestCode: " + requestCode + "\nresultCode: " + resultCode);
    }
}
