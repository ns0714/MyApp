package com.codepath.shareyourtable.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shareyourtable.R;
import com.codepath.shareyourtable.helpers.BitMapHelper;
import com.codepath.shareyourtable.model.Message;
import com.codepath.shareyourtable.model.Profile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatListAdapter extends ArrayAdapter<Message> {
    private Bitmap bitmap;
    private String mUserId;
    private String username;
    private String receiverUsername;
    private String profileUrl;
    private ImageView profileView;
    boolean isMe;

    public ChatListAdapter(Context context, String userId, List<Message> messages) {
            super(context, 0, messages);
            this.mUserId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                inflate(R.layout.chat_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView)convertView.findViewById(R.id.ivProfileLeft);
            holder.imageRight = (ImageView)convertView.findViewById(R.id.ivProfileRight);
            holder.body = (TextView)convertView.findViewById(R.id.tvBody);
            convertView.setTag(holder);
        }
        final Message message = (Message)getItem(position);
        username = message.getUsername();
         final ViewHolder holder = (ViewHolder)convertView.getTag();


        System.out.print("current user object id " + mUserId );
        System.out.print("message user id " + message.getUserId() );

         isMe = message.getUserId().equals(mUserId);
        // Show-hide image based on the logged-in user. 
                // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        if(isMe){
            //android.graphics.Bitmap@427cba48
            getUserProfilePic(holder,username, true);
           // holder.imageRight.setImageBitmap(bitmap);
        }else{
            getUserProfilePic(holder, username, false);
           // holder.imageLeft.setImageBitmap(bitmap);
        }

        // = isMe ? holder.imageRight : holder.imageLeft;
        //getUserProfilePic(message.getUsername());
        //Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
       // holder.imageLeft.setImageBitmap(bitmap);
        holder.body.setText(message.getBody());
        return convertView;
    }

    public void getUserProfilePic(final ViewHolder holder, String username, final boolean isMe){
        ParseQuery<Profile> imgQuery = ParseQuery.getQuery(Profile.class);
        imgQuery.whereEqualTo("username", username);
        System.out.print("passed user" + username);
        imgQuery.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> profiles, ParseException e) {
                if(e == null){
                    Profile profile = profiles.get(0);
                    System.out.print("messages" + profiles.get(0).getUsername());
                    ParseFile profileImg = profile.getProfilePicture();
                    System.out.print("profile" + profiles.get(0).getProfilePicture());
                    bitmap = BitMapHelper.getBitMapFromFile(profileImg);
                    Uri u = getImageUri(getContext(), bitmap);
                    System.out.print("BITTTTTTT" + bitmap);
                    System.out.print("URIIIIII" + u);
                    if(isMe) {
                        System.out.print("It came here right");
                        Picasso.with(getContext()).load(u).into(holder.imageRight);
                        //holder.imageRight.setImageResource(0);
                        //holder.imageRight.setImageBitmap(bitmap);
                    }else{
                        System.out.print("It came here left");
                        Picasso.with(getContext()).load(u).into(holder.imageLeft);
                        //holder.imageLeft.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }
    
    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
    
    final class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}