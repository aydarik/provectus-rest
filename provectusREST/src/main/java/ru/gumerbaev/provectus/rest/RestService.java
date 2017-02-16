package ru.gumerbaev.provectus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.gumerbaev.provectus.rest.annotation.Rest;
import ru.gumerbaev.provectus.rest.annotation.Restable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Configurable
public class RestService extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(RestService.class.getName());

	@Autowired
	ApplicationContext context;

	public RestService() {
		LOGGER.info("RestService started.");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("doGet method called.");
		methodCall(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("doPost method called.");
		resp.setStatus(201);
		methodCall(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("doPut method called.");
		methodCall(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LOGGER.info("doDelete method called.");
		methodCall(req, resp);
	}

	private void methodCall(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter writer = resp.getWriter();

		// Spring application context
		ApplicationContext context = ContextProvider.getApplicationContext();

		// Messages to display
		List<Object> messages = new ArrayList<>();

		boolean noMethodFound = true;
		// Check for Restable-annotated beans
		for (Object bean : context.getBeansWithAnnotation(Restable.class).values()) {
			String uriPath = req.getPathInfo();
			String restablePath = bean.getClass().getAnnotation(Restable.class).path();
			if (uriPath.startsWith(restablePath)) {
				// Remove global path from URI
				uriPath = uriPath.replaceFirst(restablePath, "");
				// Check for Rest-annotated methods
				for (Method m : bean.getClass().getMethods()) {
					Rest rest = m.getAnnotation(Rest.class);
					if (rest != null && req.getMethod().equals(rest.type().toString())) {
						String[] splitPath = uriPath.split("/");
						String[] splitRest = rest.path().split("/");
						// Hack for default path if no path defined
						if (splitPath.length == 1 && splitPath[0].isEmpty()) {
							splitPath = new String[0];
						}

						// Check for matched methods
						boolean matches = false;
						List<String> params = new ArrayList<>();
						if (splitPath.length == splitRest.length) {
							matches = true;
							for (int i = 0; i < splitPath.length; i++) {
								// Getting params
								if (splitRest[i].matches("\\{.*\\}")) {
									params.add(splitPath[i]);
								} else {
									if (!splitPath[i].equalsIgnoreCase(splitRest[i])) {
										matches = false;
										break;
									}
								}
							}
						}

						if (matches) {
							try {
								noMethodFound = false;
								messages.add(m.invoke(bean, params.toArray()));
							} catch (Exception e) {
								resp.setStatus(500);
								writer.println("ERROR: " + e.getCause());
								writer.print(e.getLocalizedMessage());
								return;
							}
						}
					}
				}
			}
		}

		if (noMethodFound) {
			resp.setStatus(405);
			writer.print("ERROR: No " + req.getMethod() + " method found for URI: " + req.getPathInfo());
		} else {
			messages.forEach(writer::print);
		}
	}
}
