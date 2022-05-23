package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.CustomProposalBinding;
import com.mrq.paljobs.models.Proposal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {

    Context mContext;
    ArrayList<Proposal> list;

    public ProposalAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Proposal> getList() {
        return list;
    }

    public void setList(ArrayList<Proposal> list) {
        this.list = list;
        notifyDataSetChanged();
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
            Picasso.get().load(model.getCompanyImage())
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(binding.image);
            binding.title.setText(model.getTitle());
            binding.name.setText(model.getCompanyName());
            binding.time.setText(model.getTime());
            binding.content.setText(model.getContent());
            SkillsAdapter adapter = new SkillsAdapter(mContext);
            adapter.setList(model.getSkills());
            binding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);
        }
    }

}
