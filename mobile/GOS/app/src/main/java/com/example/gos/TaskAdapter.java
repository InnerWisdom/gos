package com.example.gos;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gos.databinding.TaskLayoutBinding;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private static TaskViewHolder.TaskLayoutListener listener;

    public TaskAdapter(TaskViewHolder.TaskLayoutListener listener) {
        super(TASK_DIFF_CALLBACK);

        TaskAdapter.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskLayoutBinding binding = TaskLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private final TaskLayoutBinding binding;

        public TaskViewHolder(TaskLayoutBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(Task task) {
            binding.textViewValue.setText(task.getValue());
            binding.buttonEdit.setOnClickListener(v -> {
                listener.onEditTaskClick(task);
            });
        }

        public interface TaskLayoutListener {
            void onEditTaskClick(Task task);
        }
    }

    private static final DiffUtil.ItemCallback<Task> TASK_DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem == newItem;
        }
    };
}
