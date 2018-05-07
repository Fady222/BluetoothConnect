package tum.rcs.bluetoothconnect;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_BlUETOOTH_FRAGMENT = "BluetoothFragment";
    BluetoothFragment mBluetoothFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        FragmentManager fragmentManager = getFragmentManager();
        BluetoothFragment fragment =(BluetoothFragment) fragmentManager.findFragmentByTag(TAG_BlUETOOTH_FRAGMENT);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



        if (fragment == null) {
            fragmentTransaction.replace(R.id.fragment,new BluetoothFragment(),TAG_BlUETOOTH_FRAGMENT);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.fragment,fragment,TAG_BlUETOOTH_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}
