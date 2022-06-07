package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.CustomSearchBinding;
import com.mrq.paljobs.models.Proposal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ProposalViewHolder> implements Filterable {

    Context mContext;
    ArrayList<Proposal> list;
    List<Proposal> filterList;

    public SearchAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Proposal> getList() {
        return list;
    }

    public void setList(ArrayList<Proposal> list) {
        this.list = list;
        filterList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void addItem(Proposal proposal) {
        list.add(proposal);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search, parent, false);
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

        CustomSearchBinding binding;

        private ProposalViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomSearchBinding.bind(itemView);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Proposal> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(filterList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Proposal item : filterList) {
                        if (item.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

}
