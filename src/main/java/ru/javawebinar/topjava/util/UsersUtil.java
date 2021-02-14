package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> users = Arrays.asList(
            new User("Иван", "ivan@yandex.ru", "123", Role.USER),
            new User("Саша", "sasha@yandex.ru", "321", Role.USER),
            new User("Вова", "vova@yandex.ru", "132", Role.ADMIN)
    );
}
