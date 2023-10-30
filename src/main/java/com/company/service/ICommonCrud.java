package com.company.service;

import com.company.exceptions.BadRequestException;
import com.company.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ICommonCrud<T> {
    List<T> findAll();
    T save(T t);

    default T findById(Long id) throws ResourceNotFoundException {
        throw new UnsupportedOperationException("Find by Long ID not supported");
    }

    default T findById(String id) throws ResourceNotFoundException {
        throw new UnsupportedOperationException("Find by String ID not supported");
    }
    default T update(Long id, T t) throws ResourceNotFoundException, BadRequestException {
        throw new UnsupportedOperationException("Update by Long ID not supported");
    }

    default T update(String id, T t) throws ResourceNotFoundException, BadRequestException {
        throw new UnsupportedOperationException("Update by String ID not supported");
    }

    default void deleteById(Long id) throws ResourceNotFoundException {
        throw new UnsupportedOperationException("Delete by Long ID not supported");
    }

    default void deleteById(String id) throws ResourceNotFoundException, BadRequestException {
        throw new UnsupportedOperationException("Delete by String ID not supported");
    }
}
