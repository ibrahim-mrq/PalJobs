package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.interfaceis.RemoveSkillsInterface;
import com.mrq.paljobs.databinding.CustomSkillsBinding;

import java.util.ArrayList;

public class SkillsSelectedAdapter extends RecyclerView.Adapter<SkillsSelectedAdapter.SkillsViewHolder> {

    Context mContext;
    ArrayList<String> list;
    RemoveSkillsInterface anInterface;

    public SkillsSelectedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void removeSkills(RemoveSkillsInterface anInterface) {
        this.anInterface = anInterface;
    }

    @NonNull
    @Override
    public SkillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_skills, parent, false);
        return new SkillsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillsViewHolder holder, int position) {
        String model = list.get(position);
        holder.bind(model);
        holder.itemView.setOnClickListener(view -> anInterface.remove(model, position));
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    static class SkillsViewHolder extends RecyclerView.ViewHolder {

        CustomSkillsBinding binding;

        private SkillsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomSkillsBinding.bind(itemView);
        }

        private void bind(String model) {
            binding.name.setText(model);
        }
    }

}
