package com.example.gos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("SELECT * FROM task")
    LiveData<List<Task>> loadTaskList();

    @Query("SELECT * FROM task WHERE id = :id")
    Task loadTask(int id);

    @Query("UPDATE task SET value = :value WHERE id = :id")
    void update(int id, String value);

    @Query("SELECT * FROM task WHERE LENGTH(value) > 10 ORDER BY LENGTH(value)")
    List<Task> filterByTextLength();
}