package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import com.epam.project.domain.Product;
import com.epam.project.domain.User;
import com.epam.project.exceptions.ProductServiceException;
import com.epam.project.exceptions.UnknownUserException;
import com.epam.project.service.IProductServ;
import com.epam.project.service.IUserServ;
import com.epam.project.service.ServiceFactory;

import java.util.List;

public class CommandMissing implements ICommand {

    @Override
    public ExecutionResult execute(SessionRequestContent content) {
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.FORWARD);
        try {
            IUserServ userServ = ServiceFactory.getUserService();
            User guest = userServ.findUser("Guest", "guest");
            IProductServ productServ = ServiceFactory.getProductService();
            List<Product> products = productServ.findAllProducts();
            result.addSessionAttribute("user", guest);
            result.addRequestAttribute("products", products);
            result.setPage(Configuration.getInstance().getProperty("main"));
        } catch (ProductServiceException | UnknownUserException pse) {
            result.setPage(Configuration.getInstance().getProperty("error"));
        }
        return result;
    }
}
