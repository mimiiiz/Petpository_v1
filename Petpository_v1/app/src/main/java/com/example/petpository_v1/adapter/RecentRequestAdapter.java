package com.example.petpository_v1.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Model.RequestPet;
import com.example.petpository_v1.Model.User;
import com.example.petpository_v1.R;
import com.example.petpository_v1.Sitter.RecentRequestActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Acer on 5/12/2559.
 */

public class RecentRequestAdapter extends RecyclerView.Adapter<RecentRequestAdapter.ViewHolder> {

    Context mContext;
    ArrayList<RequestPet> requests;
    ViewHolder.ClickListener listener;

    public RecentRequestAdapter(Context mContext, ArrayList<RequestPet> requests, ViewHolder.ClickListener listener) {
        this.mContext = mContext;
        this.requests = requests;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        ImageView sizeSymbol;
        ImageView dogImage;
        TextView dogNameLabel;
        TextView dogOwnerLabel;
        TextView dogBreedLabel;

        ClickListener listener;

        public interface ClickListener {
            void onClick(int position);
        }

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.requestCardView);
            sizeSymbol = (ImageView) itemView.findViewById(R.id.sizeSymbol);
            dogImage = (ImageView) itemView.findViewById(R.id.petImageView);
            dogNameLabel = (TextView) itemView.findViewById(R.id.dogNameLabel);
            dogOwnerLabel = (TextView) itemView.findViewById(R.id.ownerNameLabel);
            dogBreedLabel = (TextView) itemView.findViewById(R.id.dogTypeTextView);
            this.listener = listener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d("click", getAdapterPosition()+"");
            if (listener != null) {
                listener.onClick(getAdapterPosition());
            }
        }
    }

    @Override
    public RecentRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recent_request_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecentRequestAdapter.ViewHolder holder, int position) {
        holder.dogNameLabel.setText(requests.get(position).getPet().getPetName());
        findOwner(requests.get(position).getRequestUID_owner(), holder, position);
        holder.dogBreedLabel.setText(requests.get(position).getPet().getPetType());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        StorageReference dogImageStorage = storageRef.child("Owner/" + requests.get(position).getRequestUID_owner() + "/" + requests.get(position).getPet().getPetID() + "/0");

        dogImageStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).fitCenter().centerCrop().into(holder.dogImage);
            }
        });

        setSizeSymbol(requests.get(position).getPet().getPetSize(), holder, position);



    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public void findOwner(String uidOwner, final ViewHolder holder, final int position){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("Users").child(uidOwner).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ownerName = (String) dataSnapshot.getValue();
                holder.dogOwnerLabel.setText(ownerName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setSizeSymbol(String size, ViewHolder holder, int position){

        if (size.equals("Small")){
            holder.sizeSymbol.setImageResource(R.drawable.s);
        }else if(size.equals("Medium")){
            holder.sizeSymbol.setImageResource(R.drawable.m);
        }else if(size.equals("Large")){
            holder.sizeSymbol.setImageResource(R.drawable.l);
        }
    }
}
