package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Friends {
    private Integer userId;
    private Integer friendUserId;
    private Integer friendshipsStatus;
    private LocalDate localDateEvent;
}
