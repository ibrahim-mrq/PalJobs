package com.mrq.paljobs.controller.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.interfaceis.SaveInterface;
import com.mrq.paljobs.databinding.CustomProposalBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {

    Context mContext;
    ArrayList<Proposal> list;
    SaveInterface anInterface;

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

        loadSavedProposals(holder.binding.save);

        holder.binding.save.setOnClickListener(view -> {
            anInterface.onclick(model, holder.binding.save);
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

            if (model.getSaved()) {
                binding.save.setImageResource(R.drawable.ic_save);
            } else {
                binding.save.setImageResource(R.drawable.ic_unsave);
            }
        }
    }

    private void loadSavedProposals(ImageView imageView) {
        new ApiRequest<Favorite>().getData(
                mContext,
                "FavoriteProposal",
                "customerId",
                Hawk.get(Constants.USER_TOKEN),
                Favorite.class,
                new Results<ArrayList<Favorite>>() {
                    @Override
                    public void onSuccess(ArrayList<Favorite> favorites) {
                        ArrayList<String> id = new ArrayList<>();
//
                        for (int i = 0; i < favorites.size(); i++) {
                            id.add(favorites.get(i).getProposalId());
                        }
                        Log.e("response", "id = " + id);
                        for (String s : id) {
                            if (id.contains(s)) {
                                imageView.setImageResource(R.drawable.ic_save);
                            } else {
                                imageView.setImageResource(R.drawable.ic_unsave);
                            }
                        }

                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int k = i + 1; k < favorites.size(); k++) {
                                if (list.get(i).getId().equals(favorites.get(k).getProposalId())) {
                                    Log.e("response", i + " and " + k + " are pairs");
                                    imageView.setImageResource(R.drawable.ic_save);
                                } else {
                                    imageView.setImageResource(R.drawable.ic_unsave);
                                    Log.e("response", i + " and " + k + " are no pairs");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {

                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onException(@NotNull String exception) {

                    }

                    @Override
                    public void onLoading(boolean loading) {

                    }
                }
        );
    }
}
