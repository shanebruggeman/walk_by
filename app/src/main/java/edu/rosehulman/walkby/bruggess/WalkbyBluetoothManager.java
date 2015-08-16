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
    private int REQUEST_ENABLE_BT = 1;
    private ArrayAdapter mArrayAdapter;

    public WalkbyBluetoothManager(ArrayAdapter adapter) {
        mArrayAdapter = adapter;
    }

    public boolean connect(Activity activity) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Log.d(LoginActivity.DEBUG_KEY, "This device does not support bluetooth");
            return false;
        }

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        //check to see if user's mac address is already registered for them
        String myMacAddress = mBluetoothAdapter.getAddress();
        return true;
    }

    public boolean refresh() {
        return findDiscoverables();
    }

    private boolean findDiscoverables() {
        Set<BluetoothDevice> visibleDevices = mBluetoothAdapter.getBondedDevices();
        Log.d(LoginActivity.DEBUG_KEY, "My mac address is: " + mBluetoothAdapter.getAddress());

        for(BluetoothDevice device : visibleDevices) {
            String mac_address = device.getAddress();
            String device_name = device.getName();
            Log.d(LoginActivity.DEBUG_KEY, device_name + ": " + mac_address);
        }

        Log.d(LoginActivity.DEBUG_KEY,"Around me there are " + visibleDevices.size() + " visible devices");

        return (visibleDevices.size() > 0);
    }

    public void enableDiscovery(Activity activity) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LoginActivity.DEBUG_KEY,"requestCode: " + requestCode + "\nresultCode: " + resultCode);
    }
}
