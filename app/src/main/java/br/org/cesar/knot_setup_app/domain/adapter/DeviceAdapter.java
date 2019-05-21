package br.org.cesar.knot_setup_app.domain.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.Gateway;

public class DeviceAdapter extends ArrayAdapter<Gateway> {
    private List<Gateway> deviceList;


    public DeviceAdapter(@NonNull Context context, int resource, @NonNull List<Gateway> objects) {
        super(context, resource, objects);
        deviceList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gateway,
                    parent, false);
            holder.deviceName = convertView.findViewById(R.id.device_name);
            holder.deviceInfo = convertView.findViewById(R.id.device_info);
            holder.deviceInfo2 = convertView.findViewById(R.id.device_info2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gateway device = deviceList.get(position);
        holder.deviceName.setText(device.getName());
        holder.deviceInfo.setText(device.getId());
        holder.deviceInfo2.setText("");

        return convertView;
    }

    private class ViewHolder {

        private TextView deviceName;
        private TextView deviceInfo;
        private TextView deviceInfo2;
        private ImageView deviceImage;

    }
}
