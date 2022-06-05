package com.mrq.paljobs.controller.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.JobDetailsActivity;
import com.mrq.paljobs.controller.interfaceis.DeleteInterface;
import com.mrq.paljobs.databinding.CustomProposalCompanyBinding;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;

import java.util.ArrayList;

@SuppressLint("NonConstantResourceId")
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    Context mContext;
    ArrayList<Proposal> list;
    DeleteInterface anInterface;

    public CompanyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<Proposal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void deleteInterface(DeleteInterface anInterface) {
        this.anInterface = anInterface;
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
        holder.itemView.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, JobDetailsActivity.class)
                    .putExtra(Constants.TYPE_MODEL, model)
            );
        });

        holder.binding.more.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, holder.binding.more, Gravity.BOTTOM);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.popup_edit:
                        break;
                    case R.id.popup_delete:
                        anInterface.delete(model);
                        popupMenu.dismiss();
                        break;
                }
                return true;
            });
            popupMenu.show();
        });
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
            binding.title.setText(model.getTitle());
            binding.time.setText(model.getTime());
            binding.content.setText(model.getContent());
            SkillsAdapter adapter = new SkillsAdapter(mContext);
            adapter.setList(model.getSkills());
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);
        }
    }

}
