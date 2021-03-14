package com.example.ndoyon_c196.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ndoyon_c196.Entity.course;
import com.example.ndoyon_c196.R;
import com.example.ndoyon_c196.Utility.Converters;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    private List<course> courses = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.standard_row, parent, false);
        return new CourseAdapter.CourseHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {
        course currentCourse = courses.get(position);
        holder.textViewTitle.setText(currentCourse.getCourse_name());
        holder.textViewStatus.setText(currentCourse.getCourse_status());
        String startdate = null;
        String enddate = null;
        try {
            startdate = Converters.DateToString(currentCourse.getCourse_start());
            enddate = Converters.DateToString(currentCourse.getCourse_end());
        } catch (ParseException e) { e.printStackTrace(); }
        holder.textViewDateInfo.setText(startdate + " - " + enddate);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    public course getCourseAt(int position) {
        return courses.get(position);
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewStatus;
        private TextView textViewDateInfo;

        public CourseHolder(@NonNull View itemView)  {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.viewTitle);
            textViewStatus = itemView.findViewById(R.id.Status);
            textViewDateInfo = itemView.findViewById(R.id.dateInfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(courses.get(position));
                    }
                }
            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(course course);
    }

    public void setOnItemClickListener(CourseAdapter.OnItemClickListener listener) {
        this.listener = listener;

    }

}
