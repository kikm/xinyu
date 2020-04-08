package com.xinyu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xinyu.model.Unit;

@Mapper
public interface UnitDao {

	public List<Unit> getUnit();

}
