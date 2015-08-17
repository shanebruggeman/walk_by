package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Set;

import cloud_controller.user.crud_operations.UserAddMacAddressAsyncTask;
import cloud_controller.user.crud_operations.UserAddOwnMacAddressAsyncTask;
import cloud_controller.user.crud_operations.UserMacAddressCallback;

/**
 * Created by shane.bruggeman on 7/26/15.
 */
public class WalkbyBluetoothManager implements UserMacAddressCallback {
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private ArrayAdapter mArrayAdapter;

    private boolean selfAdded;
    private String username;
    private ArrayList<String> recentlyEncounteredMacs;

    public WalkbyBluetoothManager(ArrayAdapter adapter, String username) {
        mArrayAdapter = adapter;
        selfAdded = false;
        this.username = username;
        this.recentlyEncounteredMacs = new ArrayList<String>();
    }

    public void finishMacAddress(String macAddress) {
        selfAdded = true;
    }

    //begins interaction with bluetooth
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

    //hard refresh option
    public void refresh() {
        findDiscoverables();
    }

    //handles the finding and discovery of other walkby users
    private boolean findDiscoverables() {
        Set<BluetoothDevice> visibleDevices = mBluetoothAdapter.getBondedDevices();
        //Log.d(LoginActivity.DEBUG_KEY, "My mac address is: " + mBluetoothAdapter.getAddress());

        //make sure our own mac address is in the system
        if(!selfAdded) {
            Log.d(LoginActivity.DEBUG_KEY, "Making call to update my own mac address");
            (new UserAddOwnMacAddressAsyncTask(this)).execute(username, mBluetoothAdapter.getAddress());
        }

        //find and record finding all devices that are walkby users
        for(BluetoothDevice device : visibleDevices) {
            String macAddress = device.getAddress();
            String device_name = device.getName();
            Log.d(LoginActivity.DEBUG_KEY, device_name + ": " + macAddress);

            //should reduce the number of async calls to add encounters
            if(!recentlyEncounteredMacs.contains(macAddress)) {
                //Log.d(LoginActivity.DEBUG_KEY, "Registering encounter with " + macAddress);
                recentlyEncounteredMacs.add(macAddress);
                (new UserAddMacAddressAsyncTask()).execute(username, macAddress);
            }
        }

        //Log.d(LoginActivity.DEBUG_KEY,"Around me there are " + visibleDevices.size() + " visible devices");

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
