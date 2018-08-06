package com.curtisgetz.baking.ui.recipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtisgetz.baking.R;
import com.curtisgetz.baking.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter {

    private List<Step> mSteps;
    private StepClickListener mClickListener;

    public interface StepClickListener{
        void onStepClickListener(int clickedPos);
    }


    public StepsAdapter(StepClickListener listener) {
        this.mClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_item, parent, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((StepViewHolder) holder).stepDescTV.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(mSteps == null) return 0;
        return mSteps.size();
    }


    public void setSteps(List<Step> steps){
        this.mSteps = new ArrayList<>();
        this.mSteps.addAll(steps);
        notifyDataSetChanged();
    }



    class StepViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

        @BindView(R.id.step_short_desc) TextView stepDescTV;

        public StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickListener.onStepClickListener(getAdapterPosition());
        }
    }


}
