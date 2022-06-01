package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.CustomProposalCompanyBinding;
import com.mrq.paljobs.models.Proposal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    Context mContext;
    ArrayList<Proposal> list;

    public CompanyAdapter(Context mContext) {
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
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_proposal_company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Proposal model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {

        CustomProposalCompanyBinding binding;

        private CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomProposalCompanyBinding.bind(itemView);
        }

        private void bind(Proposal model) {
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
//            binding.recyclerview.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);
        }
    }

}
