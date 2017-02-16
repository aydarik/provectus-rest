package ru.gumerbaev.provectus.test.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope("singleton")
public class UserDAO {

    private Map<Long, UserEntity> users = new HashMap<>();
    private AtomicLong counter = new AtomicLong();

    public String getUser(long id) {
        try {
            return users.get(id).getName();
        } catch (NullPointerException npe) {
            return "<no user>";
        }
    }

    public Long addUser(String name) {
        long id = counter.getAndIncrement();
        users.put(id, new UserEntity(name));
        return id;
    }

    public String delUser(long id) {
        if (users.containsKey(id)) {
            users.remove(id);
            return "<success>";
        } else {
            return "<no user>";
        }
    }

    public String list() {
        if (!users.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Long, UserEntity> entry : users.entrySet()) {
                builder.append(entry.getKey()).append(": ").append(entry.getValue().getName()).append("\r\n");
            }
            return builder.toString();
        } else {
            return "<no users>";
        }
    }
}
