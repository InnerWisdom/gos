package com.example.gos;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gos.databinding.FragmentManageTaskBinding;

public class ManageTaskFragment extends Fragment {

    private FragmentManageTaskBinding binding;


    private final TaskDao taskDao;

    private int task_id;

    public ManageTaskFragment(Application application) {
        taskDao = AppDatabase.getInstance(application).taskDao();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        task_id = requireArguments().getInt(KEY_TASK_ID, DEFAULT_VALUE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManageTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize();
    }

    private void initialize() {

        binding.saveButton.setOnClickListener(v -> {
            if (task_id != DEFAULT_VALUE) {
                AppDatabase.databaseExecutor.execute(() -> {
                    taskDao.update(task_id, binding.editTextTask.getEditableText().toString());
                });
                closeFragment();
            } else {
                AppDatabase.databaseExecutor.execute(() -> {
                    taskDao.insert(new Task(DEFAULT_TASK_ID, binding.editTextTask.getEditableText().toString()));
                });
                closeFragment();
            }
        });
    }

    private void closeFragment() {
        getParentFragmentManager().popBackStack();
    }

    private final int DEFAULT_VALUE = 0;
    public static String KEY_TASK_ID = "KEY_TASK_ID";

    public static ManageTaskFragment newInstance(Integer taskId) {
        ManageTaskFragment fragment = new ManageTaskFragment(new Application());

        Bundle bundle = new Bundle();
        if (taskId != null) {
            bundle.putInt(KEY_TASK_ID, taskId);
        }

        fragment.setArguments(bundle);
        return fragment;
    }
    private final int DEFAULT_TASK_ID = 0;
}