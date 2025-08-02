package org.example.service;

import org.example.data.Entity.Status;
import org.example.data.Entity.Task;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @CacheEvict(value = {"tasks", "allTasks", "taskByStatus"}, allEntries = true)
    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    @Cacheable(value = "tasks", key = "#id")
    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    @Cacheable(value = "allTasks", key = "#pageable.pageNumber")
    public Page<Task> getAllTasks(Pageable pageable){
        return taskRepository.findAll(pageable);
    }

    @CachePut(value = "tasks", key = "#id")
    @CacheEvict(value = {"allTasks", "tasksByStatus"}, allEntries = true)
    public Task updateTask(Long id, Task updatedTask){
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setStatus(updatedTask.getStatus());
                    return taskRepository.save(task);
                }).orElseThrow(()-> new ResourceNotFoundException("Task with id "+id+" not found"));
    }

    @CacheEvict(value = {"tasks", "allTasks", "taskByStatus"}, allEntries = true)
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    @Cacheable(value = "taskByStatus", key = "#status.toString()+'_'+#pageable.pageNumber")
    public Page<Task> getTaskByStatus(Status status, Pageable pageable){
        return taskRepository.findByStatus(status, pageable);
    }
}
