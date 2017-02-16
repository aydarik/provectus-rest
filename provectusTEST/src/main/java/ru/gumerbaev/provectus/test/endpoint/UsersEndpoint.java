package ru.gumerbaev.provectus.test.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gumerbaev.provectus.rest.annotation.Param;
import ru.gumerbaev.provectus.rest.annotation.Rest;
import ru.gumerbaev.provectus.rest.annotation.Restable;
import ru.gumerbaev.provectus.test.dao.UserDAO;

import javax.validation.constraints.NotNull;

@Component
@Restable(path = "/user")
public class UsersEndpoint {

	@Autowired
	UserDAO userDAO;

	@Rest(type = Rest.RestType.GET)
	public String list() {
		return userDAO.list();
	}

	@Rest(type = Rest.RestType.GET, path = "/{id}")
	public String get(@Param("id") @NotNull String id) {
		return userDAO.getUser(Long.parseLong(id));
	}

	@Rest(type = Rest.RestType.POST, path = "/{name}")
	public Long add(@Param("name") @NotNull String name) {
		return userDAO.addUser(name);
	}

	@Rest(type = Rest.RestType.DELETE, path = "/{id}")
	public String del(@Param("id") @NotNull String id) {
		return userDAO.delUser(Long.parseLong(id));
	}
}
