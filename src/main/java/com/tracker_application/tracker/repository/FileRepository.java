package com.tracker_application.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tracker_application.tracker.model.File;

public interface FileRepository extends JpaRepository<File, Long> {

}
