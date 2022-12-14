/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 
 */
public class ClassRoom implements Comparable<ClassRoom>{
    // Can have maximum of 3 Teachers
    // Can have maximum of 30 students

    private String name;
    private int capacity;
    private Map<Teacher, List<Student>> teacherStudentGroup;
    private Map<String, List<Student>> studentTeacherGroup;
    private String ageGroup;

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAgeGroup(String ageGroup){
    this.ageGroup = ageGroup;
    }
    public String getAgeGroup(){
        return this.ageGroup;
    }
    
     public void setTeacherStudentGroup(Map<Teacher, List<Student>> teacherStudentGroup){
        this.teacherStudentGroup = teacherStudentGroup;
    }
    public Map<Teacher, List<Student>> getTeacherStudentGroup(){
        return this.teacherStudentGroup;
    }
    
    public void setStudentTeacherGroup(Map<String, List<Student>> studentTeacherGroup){
        this.studentTeacherGroup = studentTeacherGroup;
    }
    public Map<String, List<Student>> getStudentTeacherGroup(){
        return this.studentTeacherGroup;
    }


    public ClassRoom(){

    }

    public ClassRoom(String name){
        this.name = name;
        teacherStudentGroup = new HashMap<>();
       
    }
    @Override
    public int compareTo(ClassRoom o) {
        return Integer.compare(this.getCapacity(), o.getCapacity());
    }
}
