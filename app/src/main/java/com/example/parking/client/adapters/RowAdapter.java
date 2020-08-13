package com.example.parking.client.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.parking.client.R;
import com.example.parking.client.dto.RowDTO;
import com.example.parking.client.sql.FavoriteDbHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class RowAdapter extends BaseAdapter {
  private List<RowDTO> listData;
  private LayoutInflater layoutInflater;

  public RowAdapter(Context aContext, List<RowDTO> listData) {
    this.listData = listData;
    layoutInflater = LayoutInflater.from(aContext);
  }

  @Override
  public int getCount() {
    return listData.size();
  }

  @Override
  public Object getItem(int position) {
    return listData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View v, ViewGroup vg) {
    ViewHolder holder;
    if (v == null) {
      v = layoutInflater.inflate(R.layout.row, null);
      holder = new ViewHolder();
      holder.uName = v.findViewById(R.id.name);
      holder.uDistance = v.findViewById(R.id.distance);
      holder.uFreeSpaces = v.findViewById(R.id.freeSpace);
      holder.uPrice = v.findViewById(R.id.price);
      holder.uFavorite = v.findViewById(R.id.favorite);
      v.setTag(holder);
    } else {
      holder = (ViewHolder) v.getTag();
    }
    holder.uName.setText(listData.get(position).getName());
    holder.uDistance.setText(StringUtils.replace(listData.get(position).getDistance(), "km", "км"));
    holder.uFreeSpaces.setText(
        String.format("%s слободних места", listData.get(position).getFreeSpace()));
    if (listData.get(position).getPrice() != null) {
      holder.uPrice.setText(String.format("%sдин/сат", listData.get(position).getPrice()));
    } else {
      holder.uPrice.setText("");
    }
    if (listData.get(position).isCriticalCapacity()) {
      holder.uName.setTextColor(Color.parseColor("#bb0000"));
    } else {
      holder.uName.setTextColor(Color.parseColor("#107f3e"));
    }
    holder.uFavorite.setChecked(listData.get(position).isFavorite());
    holder.uFavorite.setOnCheckedChangeListener(
            (buttonView, isChecked) -> {
              FavoriteDbHelper dbHandler = new FavoriteDbHelper(vg.getContext());
              if (isChecked) {
                dbHandler.insert(holder.uName.getText().toString());
              } else {
                dbHandler.delete(holder.uName.getText().toString());
              }
            });
    return v;
  }

  static class ViewHolder {
    TextView uName;
    TextView uDistance;
    TextView uFreeSpaces;
    TextView uPrice;
    CheckBox uFavorite;
  }
}
