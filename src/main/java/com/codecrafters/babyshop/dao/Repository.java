package com.codecrafters.babyshop.dao;
import java.sql.SQLException; import java.util.List;
/** Generic interface contract shared by DAO classes. */
public interface Repository<T>{ List<T> findAll() throws SQLException; }
