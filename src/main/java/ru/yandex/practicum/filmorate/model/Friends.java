package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Friends {
    private Integer userId;
    private Integer friendUserId;
    private Integer friendshipsStatus;
    private LocalDate localDateEvent;
}
