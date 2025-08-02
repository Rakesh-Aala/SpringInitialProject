package org.example.mapper;

import org.example.data.Entity.Task;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ModelMapper modelMapper;

    public TaskMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    public Task toEntity(TaskRequestDto requestDto){
        return modelMapper.map(requestDto, Task.class);
    }

    public TaskResponseDto toDTO(Task task){
        return modelMapper.map(task, TaskResponseDto.class);
    }

}
