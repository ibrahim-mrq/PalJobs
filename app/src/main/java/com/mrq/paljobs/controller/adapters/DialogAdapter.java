package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.interfaceis.DialogInterface;
import com.mrq.paljobs.databinding.CustomDialogTextBinding;

import java.util.ArrayList;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.CategorySubViewHolder> {

    Context mContext;
    ArrayList<String> list;
    DialogInterface anInterface;

    public DialogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setAnInterface(DialogInterface anInterface) {
        this.anInterface = anInterface;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void remove(String category) {
        list.remove(category);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategorySubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dialog_text, parent, false);
        return new CategorySubViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategorySubViewHolder holder, int position) {
        String model = list.get(position);
        holder.bind(model);

        holder.itemView.setOnClickListener(view -> {
            anInterface.onclick(model);
        });

    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public static class CategorySubViewHolder extends RecyclerView.ViewHolder {

        CustomDialogTextBinding binding;

        private CategorySubViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomDialogTextBinding.bind(itemView);
        }

        private void bind(String model) {
            binding.title.setText(model);
        }
    }
}