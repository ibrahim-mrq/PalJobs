package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.ProfileActivity;
import com.mrq.paljobs.controller.activities.ProposalDetailsActivity;
import com.mrq.paljobs.controller.activities.SubmitActivity;
import com.mrq.paljobs.controller.interfaceis.SaveInterface;
import com.mrq.paljobs.databinding.CustomProposalBinding;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Submit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {

    Context mContext;
    ArrayList<Proposal> list;
    ArrayList<Favorite> favorites;
    ArrayList<Submit> submit;
    SaveInterface anInterface;

    public ProposalAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Proposal> getList() {
        return list;
    }

    public void setList(ArrayList<Proposal> list, ArrayList<Favorite> favorites, ArrayList<Submit> submit) {
        this.list = list;
        this.favorites = favorites;
        this.submit = submit;
        notifyDataSetChanged();
    }

    public void addItem(Proposal proposal, ArrayList<Favorite> favorites, ArrayList<Submit> submit) {
        list.add(proposal);
        this.favorites = favorites;
        this.submit = submit;
        notifyDataSetChanged();
    }

    public void setSaveInterface(SaveInterface anInterface) {
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_proposal, parent, false);
        return new ProposalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProposalViewHolder holder, int position) {
        Proposal model = list.get(position);
        holder.bind(model);

        model.setSaved(Constants.ifItemInFavorite(model, favorites, holder.binding.save));
        model.setSubmit(Constants.ifItemIsSubmit(mContext, model, submit, holder.binding.btnSubmit));

        holder.binding.save.setOnClickListener(view -> {
            anInterface.onclick(model, holder.binding.save);
        });

        holder.itemView.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProposalDetailsActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_PROPOSAL)
                    .putExtra(Constants.TYPE_MODEL, model));
        });

        holder.binding.btnSubmit.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, SubmitActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_PROPOSAL)
                    .putExtra(Constants.TYPE_MODEL, model));
        });

        holder.binding.image.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProfileActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_EDIT)
            );
        });

        holder.binding.name.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ProfileActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_EDIT)
            );
        });

    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    class ProposalViewHolder extends RecyclerView.ViewHolder {

        CustomProposalBinding binding;

        private ProposalViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomProposalBinding.bind(itemView);
        }

        private void bind(Proposal model) {
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
