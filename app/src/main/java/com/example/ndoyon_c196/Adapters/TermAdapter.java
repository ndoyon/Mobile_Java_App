package com.example.ndoyon_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndoyon_c196.Entity.term;
import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Converters;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {
    private List<term> terms = new ArrayList<>();
    private OnItemClickListener  listener;

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standard_row, parent, false);
        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        term currentTerm = terms.get(position);
        holder.textViewTitle.setText(currentTerm.getTerm_name());
        holder.textViewStatus.setText(currentTerm.getTerm_status());
        String startdate = null;
        String enddate = null;
        try {
            startdate = Converters.DateToString(currentTerm.getTerm_start());
            enddate = Converters.DateToString(currentTerm.getTerm_end());
        } catch (ParseException e) { e.printStackTrace(); }
        holder.textViewDateInfo.setText(startdate + " - " + enddate);

    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void setTerms(List<term> terms) {
        this.terms = terms;
        notifyDataSetChanged();
    }

    public term getTermAt(int position) {
        return terms.get(position);
    }

    public void restoreItem(term term, int position) {
        terms.add(position, term);
        notifyItemInserted(position);
    }

    class TermHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewStatus;
        private TextView textViewDateInfo;

        public TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.viewTitle);
            textViewStatus = itemView.findViewById(R.id.Status);
            textViewDateInfo = itemView.findViewById(R.id.dateInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(terms.get(position));
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(term term);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
