package com.example.petpository_v1.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.Owner.SentRequestActivity;
import com.example.petpository_v1.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mark on 12/7/2016.
 */
public class HistoryRecycleAdapter extends RecyclerView.Adapter<HistoryRecycleAdapter.ViewHolder> {

    private ArrayList<RequestPet> historyList;
    Context context;
    String uid;
    public HistoryRecycleAdapter(ArrayList<RequestPet> historyList, Context context, String uid) {
        this.historyList = historyList;
        this.context = context;
        this.uid = uid;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView dateRange;
        public TextView status;
        public TextView petName;
        public TextView sitterName;
        public ImageView petImage;
        public ImageView placeImage;

        public ViewHolder(View v) {
            super(v);
            petName = (TextView) v.findViewById(R.id.pet_name);
            sitterName = (TextView) v.findViewById(R.id.place_name);
            dateRange = (TextView) v.findViewById(R.id.date_range);
            status = (TextView) v.findViewById(R.id.status);
            petImage = (ImageView) v.findViewById(R.id.my_pet_image);
            placeImage = (ImageView) v.findViewById(R.id.sitter_place_image);
        }
    }
    @Override
    public HistoryRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pet pet = historyList.get(position).getPet();
        holder.petName.setText(pet.getPetName());
        String placeName = historyList.get(position).getRequestPlaceName();
        if (placeName!=null){
            holder.sitterName.setText(historyList.get(position).getRequestPlaceName());
        }



        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");

        Date dateStart = new Date();
        Date dateEnd = new Date();

        try {
            dateStart  = format.parse(historyList.get(position).getRequestStartDate());
            dateEnd = format.parse(historyList.get(position).getRequestEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = dateEnd.getTime() - dateStart.getTime();
        long diffDay = (diff / (60 * 60 * 1000 * 24 ))+1;

        holder.dateRange.setText(diffDay + " days");
        String status = historyList.get(position).getRequestStatus();
        if (status.equals("pending")){
            holder.status.setBackgroundResource(R.color.colorAccent);
        }else if (status.equals("accept")){
            holder.status.setBackgroundResource(R.color.colorSecondaryText);
        }
        holder.status.setText(status);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        String petId = pet.getPetID() + "/0";

        StorageReference petImageRef = storageRef.child("Owner").child(uid).child(petId);
        petImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(holder.petImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.petImage.setImageResource(R.drawable.refresh);
            }
        });

        StorageReference placeImageRef = storageRef.child("Place").child(historyList.get(position).getRequestPlaceID()+"/0");
        Glide.with(context).using(new FirebaseImageLoader())
                .load(placeImageRef)
                .fitCenter()
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.placeImage);
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
