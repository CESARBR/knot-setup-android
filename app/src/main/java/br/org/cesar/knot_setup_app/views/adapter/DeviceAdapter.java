package br.org.cesar.knot_setup_app.views.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.BluetoothDevice;


public class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    private List<BluetoothDevice> deviceList;

    public DeviceAdapter(@NonNull Context context, int resource, @NonNull List<BluetoothDevice>
            objects) {
        super(context, resource, objects);

        deviceList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_device,
                    parent, false);
            holder.deviceName = convertView.findViewById(R.id.device_name);
            holder.deviceMAC = convertView.findViewById(R.id.device_mac);
            holder.deviceDistance = convertView.findViewById(R.id.device_distance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        android.bluetooth.BluetoothDevice device = deviceList.get(position).getDevice();

        holder.deviceName.setText(device.getName() == null ? "" : device.getName());
        holder.deviceMAC.setText(device.getAddress());
        holder.deviceDistance.setText(Integer.toString(deviceList.get(position).getRssi()));

        return convertView;
    }

    private class ViewHolder {

        private TextView deviceName;
        private TextView deviceMAC;
        private TextView deviceDistance;

    }
}
