package com.qgexam.marking.dao;

import java.util.List;

public interface MarkingDao {

    List<Integer> getExamIdList(Integer teacherId);
}
