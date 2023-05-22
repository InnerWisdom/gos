package com.example.gos;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gos.databinding.FragmentTaskListBinding;

import java.util.List;

public class TaskListFragment extends Fragment {

    private FragmentTaskListBinding binding;

    public final LiveData<List<Task>> taskList;
    private final TaskDao taskDao;
    private final MutableLiveData<List<Task>> _filteredTaskList = new MutableLiveData<>();
    public LiveData<List<Task>> filteredTaskList = _filteredTaskList;

    public TaskListFragment( Application application) {
        taskDao = AppDatabase.getInstance(application).taskDao();
        taskList = taskDao.loadTaskList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
    }

    private void initialize() {

        TaskAdapter taskAdapter = new TaskAdapter(listener);

        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.taskRecyclerView.setAdapter(taskAdapter);

        AppDatabase.databaseExecutor.execute(() -> {
            _filteredTaskList.postValue(taskDao.filterByTextLength());
        });

        taskList.observe(getViewLifecycleOwner(), taskAdapter::submitList);

        binding.addTaskButton.setOnClickListener(v -> {
            openManageTaskFragment(null);
        });

        binding.reportButton.setOnClickListener(v -> {

            AppDatabase.databaseExecutor.execute(() -> {
                _filteredTaskList.postValue(taskDao.filterByTextLength());
            });
            filteredTaskList.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                @Override
                public void onChanged(List<Task> tasks) {
                    Intent intent = new Intent(requireActivity().getApplicationContext(), ReportActivity.class);
                    intent.putExtra(ReportActivity.KEY, getString(R.string.quantity_report_template, tasks.size()));

                    filteredTaskList.removeObserver(this);
                    requireActivity().startActivity(intent);
                }
            });
        });
    }

    public TaskAdapter.TaskViewHolder.TaskLayoutListener listener = task -> {
        openManageTaskFragment(task.getId());
    };

    private void openManageTaskFragment(Integer taskId) {
        getParentFragmentManager().beginTransaction()
                .add(R.id.fragment_container, ManageTaskFragment.newInstance(taskId))
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
    }

    public static TaskListFragment newInstance(Application a) {
        return new TaskListFragment(a);
    }
}
