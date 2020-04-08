package com.xinyu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xinyu.model.Part;

@Mapper
public interface PartDao {

	public List<Part> getPartByDevice(Integer deviceId);

	public void savePart(List<Part> partList);

	public void updatePart(List<Part> updateList);

	public void deletePart(List<Long> id);

}
