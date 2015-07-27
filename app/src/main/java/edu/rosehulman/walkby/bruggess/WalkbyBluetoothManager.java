package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

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

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableIntent);
        establishConnection(activity);

        return true;
    }

    public void establishConnection(Activity activity) {
        //find and pair with bluetooth devices
    }
}
