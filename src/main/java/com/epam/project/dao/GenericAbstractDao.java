package com.epam.project.dao;

import com.epam.project.exceptions.DataBaseConnectionException;
import com.epam.project.exceptions.DataNotFoundException;
import com.epam.project.exceptions.IncorrectPropertyException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

// ToDo: add optional connection open/close inside methods implementations
public abstract class GenericAbstractDao<T> {

    protected Mapper<T, PreparedStatement> mapperToDB;
    protected Mapper<ResultSet, T> mapperFromDB;

    public GenericAbstractDao() {
    }

    public void setMapperToDB(Mapper<T, PreparedStatement> mapperToDB) {
        this.mapperToDB = mapperToDB;
    }

    public void setMapperFromDB(Mapper<ResultSet, T> mapperFromDB) {
        this.mapperFromDB = mapperFromDB;
    }

    List<T> findAll(Class t, String SQL_getAll) throws IncorrectPropertyException, DataBaseConnectionException, DataNotFoundException {
        List<T> items = new LinkedList<>();
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_getAll);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                try {
                    T item = (T) t.newInstance();
                    mapperFromDB.map(resultSet, item);
                    items.add(item);
                } catch (InstantiationException ie) {
                } catch (IllegalAccessException iae) {
                }
            }
        } catch (SQLException sqle) {
            throw new DataNotFoundException();
        } finally {
            MySQLDaoFactory.closeConnection(connection);
        }
        return items;
    }

    public T findBy(Class t, String SQL_selectByParameter, Integer parameter) throws IncorrectPropertyException, DataBaseConnectionException, DataNotFoundException {
        T item = null;
        try {
            item = (T) t.newInstance();
        } catch (InstantiationException ie) {
            throw new DataNotFoundException();
        } catch (IllegalAccessException iae) {
            throw new DataNotFoundException();
        }
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_selectByParameter);
            preparedStatement.setInt(1, parameter);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                mapperFromDB.map(resultSet, item);
            else
                throw new DataNotFoundException();
        } catch (SQLException sqle) {
            throw new DataNotFoundException();
        } finally {
            MySQLDaoFactory.closeConnection(connection);
        }
        return item;
    }

    public T findBy(Class t, String SQL_selectByParameter, String parameter) throws IncorrectPropertyException, DataBaseConnectionException, DataNotFoundException {
        T item = null;
        try {
            item = (T) t.newInstance();
        } catch (InstantiationException ie) {
            throw new DataNotFoundException();
        } catch (IllegalAccessException iae) {
            throw new DataNotFoundException();
        }
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_selectByParameter);
            preparedStatement.setString(1, parameter);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                mapperFromDB.map(resultSet, item);
            else
                throw new DataNotFoundException();
        } catch (SQLException sqle) {
            throw new DataNotFoundException();
        } finally {
            MySQLDaoFactory.closeConnection(connection);
        }
        return item;
    }

    public boolean addToDB(T item, String SQL_addNew) throws IncorrectPropertyException, DataBaseConnectionException {
        boolean result = false;
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_addNew);
            mapperToDB.map(item, preparedStatement);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException sqle) {
            result = false;
        } finally {
            MySQLDaoFactory.closeConnection(connection);
            return result;
        }
    }

    public boolean updateInDB(T item, String SQL_update, Integer paramNum, Integer param) throws IncorrectPropertyException, DataBaseConnectionException {
        boolean result = false;
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_update);
            mapperToDB.map(item, preparedStatement);
            preparedStatement.setInt(paramNum, param);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException sqle) {
            result = false;
        } finally {
            MySQLDaoFactory.closeConnection(connection);
            return result;
        }
    }

    public boolean updateInDB(T item, String SQL_update, Integer paramNum, String param) throws IncorrectPropertyException, DataBaseConnectionException {
        boolean result = false;
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_update);
            mapperToDB.map(item, preparedStatement);
            preparedStatement.setString(paramNum, param);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException sqle) {
            result = false;
        } finally {
            MySQLDaoFactory.closeConnection(connection);
            return result;
        }
    }

    public boolean deleteFromDB(String SQL_delete, Integer id) throws IncorrectPropertyException, DataBaseConnectionException {
        boolean result = false;
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_delete);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException sqle) {
            result = false;
        } finally {
            MySQLDaoFactory.closeConnection(connection);
            return result;
        }
    }

    public boolean deleteFromDB(String SQL_delete, String param) throws IncorrectPropertyException, DataBaseConnectionException {
        boolean result = false;
        Connection connection = MySQLDaoFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_delete);
            preparedStatement.setString(1, param);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException sqle) {
            result = false;
        } finally {
            MySQLDaoFactory.closeConnection(connection);
            return result;
        }
    }
}