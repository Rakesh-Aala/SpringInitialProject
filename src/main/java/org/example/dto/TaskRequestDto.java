package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.data.Entity.Status;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {
    @NotBlank(message = "title is required")
    @Size(max = 100, message = "title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "description must be less than 500 characters")
    private String description;

    private Status status;
}
