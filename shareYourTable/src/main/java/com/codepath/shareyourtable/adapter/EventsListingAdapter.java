
package com.codepath.shareyourtable.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.helpers.DateHelper;
import com.codepath.shareyourtable.model.Event;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class EventsListingAdapter extends ArrayAdapter<Event> {

    private Context context;
    private ParseFile imgFile;
    private ViewHolder viewHolder;
    private String fullName;
    private int lastPosition = -1;

    public EventsListingAdapter(Context context, int resource, List<Event> events) {
        super(context, R.layout.eventsfeed_item, events);
        this.context = context;
    }

    private static class ViewHolder {

        private ImageView ivUserProfilePic;
        private TextView tvUsername;
        private TextView tvLocation;
        private TextView tvTableFor;
        private TextView tvDate;
        private TextView tvTime;
        private ImageButton ibLookingFor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Event event = (Event) getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.eventsfeed_item, parent, false);
            viewHolder.ivUserProfilePic = (ImageView) convertView
                    .findViewById(R.id.ivUserProfilePic);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvTableFor = (TextView) convertView.findViewById(R.id.tvTableFor);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);

            viewHolder.ibLookingFor = (ImageButton) convertView.findViewById(R.id.ibLookingFor);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivUserProfilePic.setImageResource(android.R.color.transparent);
        viewHolder.tvLocation.setText(getContext().getResources().getString(
                R.string.going_to_event) + " " + event.getLocation());
        viewHolder.tvTableFor.setText(getContext().getResources().getString(
                R.string.table_for) + " " + event.getTableFor());
        viewHolder.tvUsername.setText(event.getFullName());
        viewHolder.tvDate.setText(DateHelper.getDayOfWeek(event.getEventDate()) + " " + DateHelper.getFormattedDate(event.getEventDate()));
        viewHolder.tvTime.setText(event.getTime());

        byte[] bitmapdata;
        imgFile = event.getProfileImg();
        if (imgFile != null) {
            try {
                bitmapdata = imgFile.getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0,
                        bitmapdata.length);
                viewHolder.ivUserProfilePic.setImageBitmap(bitmap);
                Palette palette = Palette.generate(bitmap, 16);

                Palette.Swatch vibrant = palette.getLightVibrantSwatch();
                if (vibrant != null) {
                    // Set the background color of a layout based on the vibrant color
                    viewHolder.tvDate.setBackgroundColor(vibrant.getRgb());
                    // Update the title TextView with the proper text color
                    viewHolder.tvDate.setTextColor(vibrant.getTitleTextColor());
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        if (event.getLookingFor().equals(getContext().getResources().getString(R.string.dating))) {
            viewHolder.ibLookingFor.setImageResource(R.drawable.heart);
        } else {
            viewHolder.ibLookingFor.setImageResource(R.drawable.people);
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(),
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }
}
