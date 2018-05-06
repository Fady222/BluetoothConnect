package tum.rcs.bluetoothconnect;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DiscoveryListAdapter extends ArrayAdapter<BluetoothDevice> {

    private final FragmentActivity fragmentActivity;
    private final ArrayList<BluetoothDevice> data;
    private final int layoutResourceId;
    private final int textViewResourceId;

    public DiscoveryListAdapter(@NonNull Context context, int resource, int textViewResourceId,
                                @NonNull ArrayList<BluetoothDevice> objects) {
        super(context, resource, textViewResourceId, objects);
        this.fragmentActivity = (FragmentActivity)context;
        data = objects;
        this.layoutResourceId = resource;
        this.textViewResourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = super.getView(position, convertView, parent);


        TextView titel_textView = fragmentActivity.findViewById(R.id.titel_textView);
        TextView name_textView = row.findViewById(R.id.name_textView);
        TextView address_textView = row.findViewById(R.id.address_textView);

        titel_textView.setText("Discovered Devices");
        name_textView.setText(data.get(position).getName());
        address_textView.setText(data.get(position).getAddress());

        return row;
    }
}
