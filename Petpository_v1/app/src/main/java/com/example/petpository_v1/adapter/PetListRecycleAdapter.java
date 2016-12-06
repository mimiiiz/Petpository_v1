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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.Owner.ChoosePetActivity;
import com.example.petpository_v1.Owner.SentRequestActivity;
import com.example.petpository_v1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mark on 12/6/2016.
 */
public class PetListRecycleAdapter extends RecyclerView.Adapter<PetListRecycleAdapter.ViewHolder> {
    private ArrayList<Pet> myPetList;
    public Context context;

    public PetListRecycleAdapter(ArrayList<Pet> myPetList, Context context) {
        this.myPetList = myPetList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView petCardView;
        public ImageButton petImageButton;
        public TextView petName;
        public TextView petSize;
        public TextView petType;
        public ViewHolder(View itemView) {
            super(itemView);
            petCardView = (CardView) itemView.findViewById(R.id.pet_list_card_view);
            petName = (TextView) itemView.findViewById(R.id.my_pet_name);
            petImageButton = (ImageButton) itemView.findViewById(R.id.my_pet_image);
            petSize = (TextView) itemView.findViewById(R.id.my_pet_size);
            petType = (TextView) itemView.findViewById(R.id.my_pet_type);
        }
    }

    @Override
    public PetListRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_pet_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.petName.setText(myPetList.get(position).getPetName());
        holder.petSize.setText(myPetList.get(position).getPetSize().substring(0,1));
        holder.petType.setText(myPetList.get(position).getPetType());

        View.OnClickListener select = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SentRequestActivity.class);
                intent.putExtra("pet_data",myPetList.get(position));
                context.startActivity(intent);
            }
        };
        holder.petImageButton.setOnClickListener(select);
        holder.petCardView.setOnClickListener(select);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://petpository-d8def.appspot.com");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String petId = myPetList.get(position).getPetID() + "/0";
        StorageReference imageRef = storageRef.child("Owner").child(userID).child(petId);
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(holder.petImageButton);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.petImageButton.setImageResource(R.drawable.refresh);
            }
        });
    }



    @Override
    public int getItemCount() {
        return myPetList.size();
    }
}
