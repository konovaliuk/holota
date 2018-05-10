package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import com.epam.project.domain.User;
import com.epam.project.exceptions.UnknownUserException;
import com.epam.project.service.ServiceFactory;

public class CommandLogin implements ICommand {

    @Override
    public ExecutionResult execute(SessionRequestContent content) {
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.RETURN);
        String login = content.getRequestParameter("name")[0];
        String password = content.getRequestParameter("password")[0];
        try {
            User user = ServiceFactory.getUserService().findUser(login, password);
            result.addSessionAttribute("user", user);
            //IProductServ productServ = ServiceFactory.getProductService();
            //List<Product> products = productServ.findAllProducts();
            //result.addRequestAttribute("products", products);
            result.setPage(Configuration.getInstance().getProperty("main"));
        }
        catch (UnknownUserException /*| ProductServiceException*/ uue) {
            result.setPage(Configuration.getInstance().getProperty("error"));
        }
        return result;
    }
}
