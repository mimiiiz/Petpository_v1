package com.example.petpository_v1.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petpository_v1.Model.Pet;
import com.example.petpository_v1.R;

import java.util.ArrayList;

/**
 * Created by Mark on 12/6/2016.
 */
public class PetListRecycleAdapter extends RecyclerView.Adapter<PetListRecycleAdapter.ViewHolder> {
    private ArrayList<Pet> myPetList;

    public PetListRecycleAdapter(ArrayList<Pet> myPetList) {
        this.myPetList = myPetList;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.petName.setText(myPetList.get(position).getPetName());
        holder.petSize.setText(myPetList.get(position).getPetSize().substring(0,1));
        holder.petType.setText(myPetList.get(position).getPetType());
        holder.petCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(null, myPetList.get(position).getPetName() + "ชั้นเลือกนาย", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return myPetList.size();
    }
}
