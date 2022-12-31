package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    @Min(0)
    private Long id;
    @Email(message = "Адрес электронной почты не соответствует стандартному формату")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы!")
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;
}
