package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.interfaceis.RemoveInterface;
import com.mrq.paljobs.databinding.CustomProposalBinding;
import com.mrq.paljobs.models.Favorite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context mContext;
    ArrayList<Favorite> list;
    RemoveInterface anInterface;

    public FavoriteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Favorite> getList() {
        return list;
    }

    public void setList(ArrayList<Favorite> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setRemoveListener(RemoveInterface anInterface) {
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_proposal, parent, false);
        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite model = list.get(position);
        holder.bind(model);

        holder.binding.save.setOnClickListener(view -> {
            anInterface.remove(model.getId());
        });

    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        CustomProposalBinding binding;

        private FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomProposalBinding.bind(itemView);
        }

        private void bind(Favorite model) {
            if (!model.getCompanyImage().isEmpty()) {
                Picasso.get().load(model.getCompanyImage())
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(binding.image);
            }

            binding.title.setText(model.getTitle());
            binding.name.setText(model.getCompanyName());
            binding.time.setText(model.getTime());
            binding.content.setText(model.getContent());
            SkillsAdapter adapter = new SkillsAdapter(mContext);
            adapter.setList(model.getSkills());
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);
            binding.save.setImageResource(R.drawable.ic_save);
        }
    }
}
