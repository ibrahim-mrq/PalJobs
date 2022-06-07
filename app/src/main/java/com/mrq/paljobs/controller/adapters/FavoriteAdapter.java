package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.ProfileActivity;
import com.mrq.paljobs.controller.activities.ProposalDetailsActivity;
import com.mrq.paljobs.controller.activities.SubmitActivity;
import com.mrq.paljobs.controller.interfaceis.RemoveInterface;
import com.mrq.paljobs.databinding.CustomFavoriteBinding;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Submit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context mContext;
    ArrayList<Favorite> list;
    ArrayList<Submit> submit;
    RemoveInterface anInterface;

    public FavoriteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Favorite> getList() {
        return list;
    }

    public void setList(ArrayList<Favorite> list, ArrayList<Submit> submit) {
        this.list = list;
        this.submit = submit;
        notifyDataSetChanged();
    }

    public void setRemoveListener(RemoveInterface anInterface) {
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_favorite, parent, false);
        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite model = list.get(position);
        holder.bind(model);

        model.setSubmit(Constants.ifItemIsSubmit(mContext, model, submit, holder.binding.btnSubmit));

        holder.binding.save.setOnClickListener(view -> {
            anInterface.remove(model.getId());
        });

        holder.itemView.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProposalDetailsActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_FAVORITE)
                    .putExtra(Constants.TYPE_MODEL, model));
        });

        holder.binding.btnSubmit.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, SubmitActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_FAVORITE)
                    .putExtra(Constants.TYPE_MODEL, model));
        });

        holder.binding.image.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProfileActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_EDIT)
                    .putExtra(Constants.TYPE_ID, model.getCompanyId())
            );
        });

        holder.binding.name.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProfileActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_EDIT)
                    .putExtra(Constants.TYPE_ID, model.getCompanyId())
            );
        });
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        CustomFavoriteBinding binding;

        private FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomFavoriteBinding.bind(itemView);
        }

        private void bind(Favorite model) {
            if (!model.getCompanyImage().isEmpty()) {
                Picasso.get().load(model.getCompanyImage())
                        .placeholder(R.drawable.ic_company_logo)
                        .into(binding.image);
            } else {
                binding.image.setImageResource(R.drawable.ic_company_logo);
            }

            binding.title.setText(model.getTitle());
            binding.name.setText(model.getCompanyName());
            binding.time.setText(model.getTime());
            binding.content.setText(model.getContent());
            SkillsAdapter adapter = new SkillsAdapter(mContext);
            adapter.setList(model.getSkills());
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);
            binding.save.setImageResource(R.drawable.ic_save);
        }
    }
}
