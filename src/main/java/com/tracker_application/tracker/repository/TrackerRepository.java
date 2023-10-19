package com.tracker_application.tracker.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tracker_application.tracker.model.TaskTracker;

public interface TrackerRepository extends CrudRepository<TaskTracker, Long> {

    @Modifying
    @Query(value = "Update TaskTracker t set t.date = :date, t.empLeaves = :empLeaves where t.user = :userid", nativeQuery = true)
    public void updateLoginTime(@Param(value = "date") java.util.Date date, @Param(value = "empLeaves") String leave,
            @Param(value = "userid") String userid);

}
