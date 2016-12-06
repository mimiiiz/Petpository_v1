package com.example.petpository_v1.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Owner.PlaceDetailActivity;
import com.example.petpository_v1.R;
import com.example.petpository_v1.Model.Place;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Thara on 15/10/2559.
 */

public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder> {

    private List<Place> placeList ;
    private Context mContext;

    public PlaceRecyclerAdapter(Context context, List<Place> placeList){
        this.placeList = placeList;
        this.mContext = context;
        Log.d("adapter", "yes");
        Log.d("placeList", placeList.toString());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView placeImage;
        private TextView placeNameLabel;
        private TextView placeWorkDayLabel;
        private TextView placePriceLabel;
        private TextView distanceLabel;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view.findViewById(R.id.placeCardView);
            placeImage = (ImageView) view.findViewById(R.id.placeImageViewer);
            placeNameLabel = (TextView) view.findViewById(R.id.placeNameLabel);
            placeWorkDayLabel = (TextView) view.findViewById(R.id.placeDogNumberLabel);
            placePriceLabel = (TextView) view.findViewById(R.id.placePriceLabel);
            distanceLabel = (TextView) view.findViewById(R.id.distanceLabel);
        }
    }

    @Override
    public PlaceRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_place_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PlaceRecyclerAdapter.ViewHolder holder, final int position) {


        //Glide.with(mContext).load(placeList.get(position).getPlacePhoto().get(0)).fitCenter().centerCrop().into(holder.placeImage);
        holder.placeNameLabel.setText(placeList.get(position).getPlaceName());
        holder.placeWorkDayLabel.setText(placeList.get(position).getPlaceDogNum() + " ตัว");
        holder.placePriceLabel.setText(String.format("%.2f บาท", placeList.get(position).getPlacePrice()));
        if (placeList.get(position).getDistance() > 1000) {
            holder.distanceLabel.setText("999+ km");
        } else {
            holder.distanceLabel.setText(String.format("%.2f km", placeList.get(position).getDistance()));

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, placeList.get(position).getPlaceName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, PlaceDetailActivity.class);
                intent.putExtra("place", placeList.get(position));
                mContext.startActivity(intent);

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");

        Log.i("AAAAAAAA", placeList.get(position).getPlaceId());
        Log.i("mimikak", "mimikak");
        StorageReference aaa = storageRef.child("Place/" + placeList.get(position).getPlaceId() + "/0");
        Glide.with(mContext).using(new FirebaseImageLoader()).load(aaa).fitCenter().centerCrop().into(holder.placeImage);
//        aaa.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//                Log.d("D>>>>>>>>>>>>>>>>>>>>>>", uri.toString());
//                Glide.with(mContext).load(uri).fitCenter().centerCrop().into(holder.placeImage);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }


}
