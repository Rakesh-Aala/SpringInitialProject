package org.example.controller;

import org.example.data.Entity.Status;
import org.example.data.Entity.Task;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.mapper.TaskMapper;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/tasks")
@Validated
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @PostMapping
    public TaskResponseDto createTask(@RequestBody TaskRequestDto requestDto){
        Task task = taskMapper.toEntity(requestDto);
        Task saved = taskService.createTask(task);
        return taskMapper.toDTO(saved);
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable(required = true) Long id){
        return taskMapper.toDTO(taskService.getTaskById(id).orElseThrow(()->new RuntimeException("no task found")));
    }

    @GetMapping
    public Page<TaskResponseDto> getAllTasks(@RequestParam(required = false)Status status,
                                             @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        Page<Task> tasks = (status!=null) ? taskService.getTaskByStatus(status, pageable) : taskService.getAllTasks(pageable);
        return tasks.map(taskMapper::toDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTaskById(@PathVariable Long id){
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    public TaskResponseDto updateTaskById(@PathVariable Long id, @RequestBody Task task){
        return taskMapper.toDTO(taskService.updateTask(id, task));
    }

}
