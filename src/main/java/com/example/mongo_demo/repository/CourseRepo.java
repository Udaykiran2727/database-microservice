package com.example.mongo_demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.mongo_demo.model.Course;

public interface CourseRepo extends MongoRepository<Course, String> {

}
