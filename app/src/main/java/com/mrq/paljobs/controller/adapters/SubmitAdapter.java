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
import com.mrq.paljobs.databinding.CustomSubmitBinding;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Submit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubmitAdapter extends RecyclerView.Adapter<SubmitAdapter.SubmitViewHolder> {

    Context mContext;
    ArrayList<Submit> list;

    public SubmitAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Submit> getList() {
        return list;
    }

    public void setList(ArrayList<Submit> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubmitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_submit, parent, false);
        return new SubmitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitViewHolder holder, int position) {
        Submit model = list.get(position);
        holder.bind(model);

        holder.itemView.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProposalDetailsActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_SUBMIT)
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

    class SubmitViewHolder extends RecyclerView.ViewHolder {

        CustomSubmitBinding binding;

        private SubmitViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomSubmitBinding.bind(itemView);
        }

        private void bind(Submit model) {
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
        }
    }

}
