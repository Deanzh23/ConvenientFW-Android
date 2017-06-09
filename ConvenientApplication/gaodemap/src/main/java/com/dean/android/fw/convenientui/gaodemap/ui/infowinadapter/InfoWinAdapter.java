package com.dean.android.fw.convenientui.gaodemap.ui.infowinadapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.Marker;
import com.dean.android.fw.convenientui.gaodemap.R;
import com.dean.android.fw.convenientui.gaodemap.listener.OnInfoWindowClickListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dean on 2017/5/24.
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter {

    private Context context;

    private View rootView;
    private String id;

    private OnInfoWindowClickListener onInfoWindowClickListener;

    public InfoWinAdapter(Context context, OnInfoWindowClickListener onInfoWindowClickListener) {
        this.context = context;
        this.onInfoWindowClickListener = onInfoWindowClickListener;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_map_info_window, null);
        setUI(marker);

        return rootView;
    }

    private void setUI(Marker marker) {
        TextView titleView = (TextView) rootView.findViewById(R.id.titleView);
        titleView.setText(TextUtils.isEmpty(marker.getTitle()) ? "" : marker.getTitle());
//        id = marker.getTitle();
//
//        TextView currentDataView = (TextView) rootView.findViewById(R.id.currentDataView);
//        currentDataView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clicked(0, id);
//            }
//        });
//
//        TextView historyDataView = (TextView) rootView.findViewById(R.id.historyDataView);
//        historyDataView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clicked(1, id);
//            }
//        });
//
//        TextView currentWarnView = (TextView) rootView.findViewById(R.id.currentWarnView);
//        currentWarnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clicked(2, id);
//            }
//        });
//
//        TextView historyWarnView = (TextView) rootView.findViewById(R.id.historyWarnView);
//        historyWarnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clicked(3, id);
//            }
//        });
    }

    public void clicked(int type, String id) {
        if (onInfoWindowClickListener != null) {
            String idParam = id.split("\n")[0].split(":")[1];
            String nameParam = id.split("\n")[1].split(":")[1];

            List<String> params = new LinkedList<>();
            params.add(String.valueOf(type));
            params.add(idParam);
            params.add(nameParam);

            onInfoWindowClickListener.onInfoWindowClicked(params);
            Log.d(InfoWinAdapter.class.getSimpleName(), "[onClick] --> id==" + id);
        }
    }

}
