package com.example.hitcapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private int resource;
    private List<Notification> objects;

    public NotificationAdapter(@NonNull Context context, int resource, @NonNull List<Notification> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Notification notification = objects.get(position);

        ImageView imgIcon = convertView.findViewById(R.id.imgNotificationIcon);
        TextView txtTitle = convertView.findViewById(R.id.txtNotificationTitle);
        TextView txtContent = convertView.findViewById(R.id.txtNotificationContent);
        TextView txtDate = convertView.findViewById(R.id.txtNotificationDate);
        Button btnDetail = convertView.findViewById(R.id.btnNotificationDetail);

        imgIcon.setImageResource(notification.getIcon());
        txtTitle.setText(notification.getTitle());
        txtContent.setText(notification.getContent());
        txtDate.setText(notification.getDate());

        btnDetail.setOnClickListener(v -> {
            Toast.makeText(context, "Chi tiết: " + notification.getTitle(), Toast.LENGTH_SHORT).show();
            // Ở đây bạn có thể mở một Activity mới để xem chi tiết thông báo
        });

        return convertView;
    }
}
