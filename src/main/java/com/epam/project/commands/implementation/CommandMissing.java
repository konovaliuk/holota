package com.epam.project.commands.implementation;

import com.epam.project.commands.ICommand;
import com.epam.project.config.Configuration;
import com.epam.project.controller.Direction;
import com.epam.project.controller.ExecutionResult;
import com.epam.project.controller.SessionRequestContent;
import com.epam.project.domain.User;
import com.epam.project.domain.UserCart;
import com.epam.project.exceptions.UnknownUserException;
import com.epam.project.service.IUserServ;
import com.epam.project.service.ServiceFactory;

public class CommandMissing implements ICommand {

    @Override
    public ExecutionResult execute(SessionRequestContent content) {
        ExecutionResult result = new ExecutionResult();
        result.setDirection(Direction.FORWARD);
        try {
            IUserServ userServ = ServiceFactory.getUserService();
            User guest = userServ.findUser("Guest", "guest");
            UserCart cart = new UserCart(guest.getName());
            result.addSessionAttribute("user", guest);
            result.addSessionAttribute("cart", cart);
            result.addRequestAttribute("command", "main");
            result.setPage("/project?command=main");
        } catch (UnknownUserException pse) {
            result.setPage(Configuration.getInstance().getProperty("error"));
        }
        return result;
    }
}