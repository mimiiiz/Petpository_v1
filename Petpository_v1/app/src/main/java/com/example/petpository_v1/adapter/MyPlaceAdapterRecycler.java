package com.example.petpository_v1.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.petpository_v1.Model.Place;
import com.example.petpository_v1.R;
import com.example.petpository_v1.Sitter.PendingActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 5/12/2559.
 */

public class MyPlaceAdapterRecycler extends RecyclerView.Adapter<MyPlaceAdapterRecycler.ViewHolder> {

    Context mContext;
    ArrayList<Place> places;

    public MyPlaceAdapterRecycler(Context context, ArrayList<Place> places) {
        this.mContext = context;
        this.places = places;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private ImageView placeImageViewer;
        private TextView placeNameLabel;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.placeCardView);
            placeImageViewer = (ImageView) view.findViewById(R.id.placeImageViewer);
            placeNameLabel = (TextView) view.findViewById(R.id.placeNameLabel);
        }
    }

    @Override
    public MyPlaceAdapterRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sister_place_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyPlaceAdapterRecycler.ViewHolder holder, final int position) {
        holder.placeNameLabel.setText(places.get(position).getPlaceName());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference urlReference = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        StorageReference storageReference = urlReference.child(places.get(position).getPlaceId() + "/0");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).fitCenter().centerCrop().into(holder.placeImageViewer);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PendingActivity.class);
                intent.putExtra("placeId", places.get(position).getPlaceId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Toast.makeText(mContext, places.get(position).getPlaceName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
