package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

    final UserStorage userStorage;
    final FilmStorage filmStorage;
    final GenreDao genreDao;
    final FriendsDao friendsDao;
    final UserService userService;
    final FilmService filmService;
    final MpaDao mpaDao;


    User getUser() {
        return User.builder()
                .id(1L)
                .login("dolore")
                .name("id")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
    }

    // счетчик имеющихся пользователей в БД (для изолированности тестов друг от друга)
    long getCountUsers() {
        int countUser = userStorage.findAllUsers().size();
        return ++countUser;
    }

    long getCountFilms() {
        int countFilm = filmStorage.findAllFilms().size();
        return ++countFilm;
    }

    // тесты добавления сущностей User
    @Test
    @DisplayName("Добавление объекта User: Предоставлены корректные данные")
    void FindUserByIdTest() {
        long userId = getCountUsers();
        userStorage.createUser(getUser());
        Optional<User> userOptional = userStorage.findUserById(userId);
        Assertions.assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", userId)
                        .hasFieldOrPropertyWithValue("login", "dolore"));
        assertThat(userStorage.findAllUsers().size())
                .isNotZero()
                .isEqualTo(userId);
    }

    @Test
    @DisplayName("Добавление объекта User: Логин пустой")
    void createUserWithLoginEmptyTest() {
        User tempUser = getUser();
        tempUser.setId(2L);
        tempUser.setLogin("");
        assertThatThrownBy(() -> userStorage.createUser(tempUser))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Логин не должен быть пустым/содержать пробелы");
        assertThatExceptionOfType(ObjectNotFoundException.class).
                isThrownBy(() -> userStorage.findUserById(2L));
    }

    @Test
    @DisplayName("Добавление объекта User: Логин содержит пробелы")
    void createUserLoginContainsSpaceTest() {
        long userId = getCountUsers();
        User tempUser = getUser();
        tempUser.setId(userId);
        tempUser.setLogin("Login login");
        assertThatThrownBy(() -> userStorage.createUser(tempUser))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Логин не должен содержать пробелы");
        assertThatExceptionOfType(ObjectNotFoundException.class).
                isThrownBy(() -> userStorage.findUserById(userId));
    }

    @Test
    @DisplayName("Добавление объекта User: Дата рождения в будущем")
    void createUserBirthdateInFutureTest() {
        long userId = getCountUsers();
        User tempUser = getUser();
        tempUser.setId(userId);
        tempUser.setBirthday(LocalDate.of(2046, 8, 20));
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userStorage.createUser(tempUser));
        assertThatExceptionOfType(ObjectNotFoundException.class)
                .isThrownBy(() -> userStorage.findUserById(userId));
    }

    @Test
    @DisplayName("Добавление объекта User: Дата рождения имеет значение null")
    void createUserBirthdateIsNullTest() {
        long userId = getCountUsers();
        User tempUser = getUser();
        tempUser.setId(userId);
        tempUser.setBirthday(null);
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> userStorage.createUser(tempUser));
        assertThatExceptionOfType(ObjectNotFoundException.class)
                .isThrownBy(() -> userStorage.findUserById(userId));
    }

    @Test
    @DisplayName("Добавление объекта User: Дата рождения отсутствует")
    void createUserBirthdateIsEmptyTest() {
        long userId = getCountUsers();
        User tempUser = User.builder()
                .id(userId)
                .login("dolore")
                .name("id")
                .email("mail@mail.ru")
                .build();
        assertThatExceptionOfType(ValidationException.class).
                isThrownBy(() -> userStorage.createUser(tempUser));
        assertThatExceptionOfType(ObjectNotFoundException.class).
                isThrownBy(() -> userStorage.findUserById(userId));
    }

    @Test
    @DisplayName("Добавление объекта User: E-mail адрес неправильный")
    void createUserIncorrectEmailTest() {
        long userId = getCountUsers();
        User tempUser = getUser();
        tempUser.setId(userId);
        tempUser.setEmail("mail.ru");
        assertThatExceptionOfType(ValidationException.class).
                isThrownBy(() -> userStorage.createUser(tempUser));
        assertThatExceptionOfType(ObjectNotFoundException.class).
                isThrownBy(() -> userStorage.findUserById(userId));
    }

    @Test
    @DisplayName("Добавление объекта User: E-mail адрес не заполнен")
    void createUserEmailEmptyTest() {
        long userId = getCountUsers();
        User tempUser = User.builder()
                .id(userId)
                .login("dolore")
                .name("id")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        assertThatExceptionOfType(ValidationException.class).
                isThrownBy(() -> userStorage.createUser(tempUser));
        assertThatExceptionOfType(ObjectNotFoundException.class).
                isThrownBy(() -> userStorage.findUserById(userId));
    }

    @Test
    @DisplayName("Добавление объекта User: E-mail уже пристуствует в коллекции")
    void createUserEmailExistInCollectionTest() {
        User tempUser = getUser();
        tempUser.setEmail("example@email.ru");
        User tempUser2 = getUser();
        tempUser2.setLogin("LoginLogin");
        tempUser2.setEmail("example@email.ru");
        userStorage.createUser(tempUser2);
        assertThatThrownBy(() -> userStorage.createUser(tempUser))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Пользователь с указанной электронной почтой уже зарегистрирован");
    }

    @Test
    @DisplayName("Добавление объекта User: E-mail адрес имеет значение null")
    void createUserEmailNullTest() {
        User tempUser = User.builder()
                .id(2L)
                .login("dolore")
                .name("id")
                .email(null)
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        assertThatExceptionOfType(ValidationException.class).
                isThrownBy(() -> userStorage.createUser(tempUser));
        assertThatExceptionOfType(ObjectNotFoundException.class).
                isThrownBy(() -> userStorage.findUserById(2L));
    }

    @Test
    @DisplayName("Добавление объекта User: Поле Name не заполнено")
    void createUserWithoutNameTest() {
        long userId = getCountUsers();
        User withoutNameUser = getUser();
        withoutNameUser.setLogin("NewLogin");
        withoutNameUser.setName("");
        withoutNameUser.setEmail("without.name@mail.ru");
        userStorage.createUser(withoutNameUser);
        Optional<User> userOptional = userStorage.findUserById(userStorage.findAllUsers().size());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "NewLogin"));
        assertThat(userStorage.findAllUsers().size())
                .isNotZero()
                .isEqualTo(userId);
    }

    // тесты в части касающейся друзей
    @Test
    @DisplayName("Friendships test")
    void usersFriendsTest() {
        long userId = getCountUsers();
        User user = getUser();
        user.setId(userId);
        user.setLogin("userLogin");
        user.setEmail("user@mail.ru");
        userStorage.createUser(user);

        long userIdFriends = getCountUsers();
        User userFriends = getUser();
        userFriends.setId(userIdFriends);
        userFriends.setEmail("userFriends@mail.ru");
        userFriends.setLogin("userFriends");
        userStorage.createUser(userFriends);
        friendsDao.addFriend(userId, userIdFriends);

        Collection<User> users = friendsDao.getFriends(userId);
        assertThat(users)
                .isNotEmpty().extracting(User::getLogin)
                .containsOnly("userFriends");

        long userIdFriends2 = getCountUsers();
        User userFriends2 = getUser();
        userFriends2.setId(userIdFriends);
        userFriends2.setEmail("userFriends2@mail.ru");
        userFriends2.setLogin("userFriends2");
        userStorage.createUser(userFriends2);
        friendsDao.addFriend(userId, userIdFriends2);
        friendsDao.addFriend(userIdFriends, userIdFriends2);
        // подтверждаем дружбу между пользователями userIdFriends2 и userIdFriends
        friendsDao.addFriend(userIdFriends2, userIdFriends);
        // очищаем список друзей
        users.clear();
        users = friendsDao.getFriends(userId);
        Assertions.assertThat(users)
                .isNotEmpty().extracting(User::getLogin)
                .containsOnlyOnceElementsOf(Arrays.asList("userFriends", "userFriends2"));
        // очищаем список друзей
        users.clear();
        users = userService.getCommonFriends(userId, userIdFriends);
        assertThat(users)
                .isNotEmpty().extracting(User::getLogin)
                .containsOnly("userFriends2");
        friendsDao.deleteFriend(userIdFriends, userIdFriends2);
        // очищаем список друзей
        users.clear();
        users = friendsDao.getFriends(userIdFriends);
        assertThat(users).isEmpty();
    }

    // тесты по наличию жанров в БД
    @Test
    @DisplayName("Проверка наличия заданных жанров в БД")
    void getAllGenresTest() {
        Collection<Genre> genres = genreDao.findAllGenre();
        assertThat(genres)
                .isNotEmpty().extracting(Genre::getName)
                .containsOnlyOnceElementsOf(Arrays.asList("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный",
                        "Боевик"));
    }

    // тесты по наличию MPA в БД
    @Test
    @DisplayName("Проверка наличия корректности MPA")
    void findAllMpaTest() {
        Collection<Mpa> mpas = mpaDao.findAllMpa();
        assertThat(mpas)
                .isNotEmpty().extracting(Mpa::getName)
                .containsOnlyOnceElementsOf(Arrays.asList("G", "PG", "PG-13", "R", "NC-17"));
    }

    // тесты по наличию MPA в БД
    @Test
    @DisplayName("Проверка на существование в БД рейтинга с переданным id")
    void existMpaTest() {
        assertThat(mpaDao.existMpa(2))
                .isTrue();
        assertThatExceptionOfType(ObjectNotFoundException.class)
                .isThrownBy(() -> mpaDao.existMpa(6))
                .withMessage("Рейтинг с идентификатором = 6 не существует");
    }

    @Test
    @DisplayName("Получение рейтинга для фильма")
    void getMpaByIdTest() {
        assertThat(mpaDao.getMpaById(2))
                .isPresent()
                .isEqualTo(Optional.of(new Mpa(2, "PG")))
                .toString()
                .contains("Для фильма с id = 2, рейтинг = PG");
    }

    @Test
    @DisplayName("Добавление объекта Film: Название фильма пустое")
    void createFilmWithNameEmptyTest() {
        long filmId = getCountFilms();
        Film film = Film.builder()
                .id(filmId)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> filmStorage.createFilm(film))
                .withMessage("Название фильма не может быть пустым");
    }


    @Test
    @DisplayName("Добавление объекта Film: Описание фильма более 200 символов")
    void createFilmDescriptionIsLongTest() {
        long filmId = getCountFilms();
        Film film = Film.builder()
                .id(filmId)
                .name("Friday")
                .description("DescriptionDescri ptionDescriptionDescriptionDesc riptionDescriptionDescriptionDescrip" +
                        "onptionDescriptionDescriptionDesc ription DescriptionD escription Description ptionDescript" +
                        "riptionDesc ription DescriptionD escription Description escription Description ptionDescript")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(100)
                .build();
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> filmStorage.createFilm(film))
                .withMessage("Превышена максимальная длина описания — 200 символов");
    }

    @Test
    @DisplayName("Добавление объекта Film: Продолжительность фильма не положительное число")
    void createFilmLengthIsNegativeTest() {
        long filmId = getCountFilms();
        Film film = Film.builder()
                .id(filmId)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(0)
                .build();
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> filmStorage.createFilm(film))
                .withMessage("Продолжительность фильма должна быть положительной");
    }

    @Test
    @DisplayName("Добавление объекта Film: Дата релиза — ранее 28 декабря 1895 года")
    void createFilmReleaseDateInPastTest() {
        long filmId = getCountFilms();
        Film film = Film.builder()
                .id(filmId)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 1, 1))
                .duration(0)
                .build();
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> filmStorage.createFilm(film))
                .withMessage("Дата релиза — раньше 28 декабря 1895 года");
    }

    @Test
    @DisplayName("Добавление объекта Film: Данные корректны")
    void createFilmTest() {
        long filmId = getCountFilms();
        Film film = Film.builder()
                .id(filmId)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .mpa(Optional.empty())
                .genres(Optional.empty())
                .build();
        film.setMpa(Optional.of(new Mpa(1, "G")));
        filmService.createFilm(film);
    }
}