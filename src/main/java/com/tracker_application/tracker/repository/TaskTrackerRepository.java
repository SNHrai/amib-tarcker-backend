package com.tracker_application.tracker.repository;

import java.util.Date;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracker_application.tracker.model.TaskTracker;






@Repository
public interface TaskTrackerRepository extends JpaRepository<TaskTracker,Long>{
   
//     @Query(value = "SELECT A.EMPLOYEENAME, B.TASK_DETAILS FROM EMPLOYEE A INNER JOIN TASKTRACKER B ON A.ID = B.EMPLOYEEID WHERE A.EMP_VERTICLE_HEAD_ID=  :verticleHeadId", nativeQuery = true)
//    public List<TaskTracker> findAllByVerticleHeadId(long verticleHeadId);
 
  
   @Query(value = "select e.employeename, t.task_details from employee e, tasktracker t where e.id = t.employeeid and t.date > :date", nativeQuery = true)
   public List<TaskTracker> findByDate(@Param(value = "date") Date date);

}
