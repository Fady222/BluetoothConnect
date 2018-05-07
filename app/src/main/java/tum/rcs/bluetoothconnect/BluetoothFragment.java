package tum.rcs.bluetoothconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BluetoothFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BluetoothFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    final ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();

    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_SET_DISCOVERABLE = 2;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 3;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 4;

    //Tags for logs
    private static final String discoveryTag =  "Discovery";

    // handles discovery of Bluetooth devices
    BroadcastReceiver discoveryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // If a device is discovered, add to list and update ListView in fragment
            if(intent.getAction() == BluetoothDevice.ACTION_FOUND){
                //Log.d(discoveryTag, "Entered discovery");
                BluetoothDevice newDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                discoveredDevices.add(newDevice);


                DiscoveryListAdapter discoveryListAdapter = new DiscoveryListAdapter(getActivity(),
                        R.layout.discovery_list_item, R.id.name_textView, discoveredDevices);


                ListView discovery_listView = (ListView)getActivity().findViewById(R.id.discovery_list_view);
                discovery_listView.setAdapter(discoveryListAdapter);
            }
        }
    };


    public BluetoothFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get default Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check phone supports Bluetooth, otherwise exit
        if (mBluetoothAdapter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title)
                    .setPositiveButton(R.string.dialog_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });
            AlertDialog dialog = builder.create();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // register DiscoveryBroadcastReceiver
        getActivity().registerReceiver(discoveryBroadcastReceiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        // If Bluetooth is not enabled, enable it and set the device discoverable
        if (!mBluetoothAdapter.isEnabled()) {
            if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivityForResult(discoverableIntent, REQUEST_SET_DISCOVERABLE);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        }

        //start Discovery of Bluetooth devices
        mBluetoothAdapter.startDiscovery();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(discoveryBroadcastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(getActivity(), BluetoothConnectService.class);
                    getActivity().startService(intent);
                } else if (resultCode == Activity.RESULT_CANCELED) {

                }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
