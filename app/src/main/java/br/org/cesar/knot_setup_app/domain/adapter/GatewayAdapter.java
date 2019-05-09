package br.org.cesar.knot_setup_app.domain.adapter;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
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

public class GatewayAdapter extends ArrayAdapter<NsdServiceInfo> {

    private List<NsdServiceInfo> deviceList;

    public GatewayAdapter(@NonNull Context context, int resource, @NonNull List<NsdServiceInfo> objects) {
        super(context, resource, objects);
        deviceList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final GatewayAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gateway,
                    parent, false);
            holder.deviceName = convertView.findViewById(R.id.gateway_name);
            convertView.setTag(holder);
        } else {
            holder = (GatewayAdapter.ViewHolder) convertView.getTag();
        }

        NsdServiceInfo device = deviceList.get(position);
        holder.deviceName.setText(device.getServiceName());
        return convertView;
    }

    private class ViewHolder {

        private TextView deviceName;
        private ImageView deviceImage;

    }


}
