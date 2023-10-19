package com.tracker_application.tracker.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracker_application.tracker.model.TaskTracker;

@Repository
public interface TaskTrackerRepository extends JpaRepository<TaskTracker, Long> {

   // @Query(value = "SELECT A.EMPLOYEENAME, B.TASK_DETAILS FROM EMPLOYEE A INNER
   // JOIN TASKTRACKER B ON A.ID = B.EMPLOYEEID WHERE A.EMP_VERTICLE_HEAD_ID=
   // :verticleHeadId", nativeQuery = true)
   // public List<TaskTracker> findAllByVerticleHeadId(long verticleHeadId);

   // @Query(value = "select e.employeename, t.task_details from employee e,
   // tasktracker t where e.id = t.employeeid and t.date > :date", nativeQuery =
   // true)
   // public List<TaskTracker> findByDate(@Param(value = "date") Date date);

   // @Query(value = "select e.emp_name from emp_master e, emp_master_tracker t
   // where e.emp_id = t.emp_id and t.login_time = :date", nativeQuery = true)
   // public Boolean existsByDate(@Param(value = "date") Date date);
   @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM emp_master_tracker WHERE TO_CHAR(login_time, 'dd-Mon-yy') = TO_CHAR(sysdate, 'dd-Mon-yy') AND emp_id = :emp_id", nativeQuery = true)
   public int findByEmployeeId(@Param("emp_id") String id);

   @Query("Select tt from TaskTracker tt where to_char(tt.date, 'MM-DD-YY') = to_char(sysdate, 'MM-DD-YY')")
   public List<TaskTracker> findByCurrentDate();

}
