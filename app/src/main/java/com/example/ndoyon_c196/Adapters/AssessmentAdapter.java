package com.example.ndoyon_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ndoyon_c196.Entity.assessment;
import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Converters;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder> {
    private List<assessment> assessments = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standard_row, parent, false);
        return new AssessmentAdapter.AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentHolder holder, int position) {
        assessment currentAssessment = assessments.get(position);
        holder.textViewTitle.setText(currentAssessment.getAssessment_name());
        holder.textViewStatus.setText(currentAssessment.getAssessment_type());
        String dueDate = null;
        ;
        try {
            dueDate = Converters.DateToString(currentAssessment.getAssessment_due_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewDateInfo.setText("Due Date: " + dueDate);
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public void setAssessments(List<assessment> assessments) {
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    public assessment getAssessmentAt(int position) {
        return assessments.get(position);
    }

    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewStatus;
        private TextView textViewDateInfo;

        public AssessmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.viewTitle);
            textViewStatus = itemView.findViewById(R.id.Status);
            textViewDateInfo = itemView.findViewById(R.id.dateInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(assessment assessment);
    }

    public void setOnItemClickListener(AssessmentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}