package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class Genre {
    @Min(0)
    private Integer id;
    private String name;
}