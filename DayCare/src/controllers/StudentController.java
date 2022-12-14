package controllers;

import models.*;
import utility.FileUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentController {

    private static StudentController instance;

    private StudentFactory studentFactory;

    private StudentController(){
        instance = null;
        studentFactory = StudentFactory.getInstance();
    }

    public static StudentController getInstance(){
        if (instance == null){
            instance = new StudentController();
        }
        return instance;
    }

    public List<Person> addObject(String csvFile) {
        FileUtil fu = new FileUtil();
        System.out.println("Inside addObject method");
        List<String> fileOutput = fu.filereader(csvFile);
        Person tmpStudent = null;
        List<Person> tmplist = new ArrayList<>();
        for (String inputLine : fileOutput) {
            try{
                String[] values = inputLine.split(",");
                int id;
                try{
                    id =Integer.parseInt(values[0]);
                }catch(java.lang.NumberFormatException e){
                    id =Integer.parseInt(values[0].substring(1));
                }
                String dateOfBirthStr = values[1];
                Date dob = null;
                try{
                    dob = new SimpleDateFormat("MM-dd-yyyy").parse(dateOfBirthStr);
                }catch(Exception e){
                }
                int age = Integer.parseInt(values[2]);
                String name = values[3];
                double gpa = Double.parseDouble(values[4]);
                String contactName = values[5];
                String contactPhone = values[6];

                Date mmrVacc1 = null;
                try{
                    String mmrVaccine1 = values[7];
                    mmrVacc1 = new SimpleDateFormat("MM-dd-yyyy").parse(mmrVaccine1);
                }catch(Exception e){
                }
                Date mmrVacc2 = null;
                try{
                    String mmrVaccine2 = values[8];
                    mmrVacc2 = new SimpleDateFormat("MM-dd-yyyy").parse(mmrVaccine2);
                }catch(Exception e){
                }
                Date varicellaVacc1 = null;
                try{
                    String varicella1 = values[9];
                    varicellaVacc1 = new SimpleDateFormat("MM-dd-yyyy").parse(varicella1);
                }catch(Exception e){
                }
                Date varicellaVacc2 = null;
                try{
                    String varicella2 = values[10];
                    varicellaVacc2 = new SimpleDateFormat("MM-dd-yyyy").parse(varicella2);
                }catch(Exception e){
                }
                String teacherAssigned =null;
                try{
                     teacherAssigned = values[11];
                }catch(Exception e){
//                    System.err.println("Exception ocurred : " + e);
                }
      
                tmpStudent = getObject(id, dob, age, name, gpa,
                      contactName, contactPhone, mmrVacc1, mmrVacc2, varicellaVacc1,
                      varicellaVacc2,teacherAssigned);
//                System.out.println(tmpStudent.toString());
                tmplist.add(tmpStudent);
            }catch(Exception e){
                System.err.println("Exception ocurred : " + e.getStackTrace()[0].getLineNumber());
            }

        }
        return tmplist;
    }

    public Student getObject(int id, Date dob, int age, String name,
                                    double gpa, String contactName, String emergencyPhone,
                                    Date mmrVacc1, Date mmrVacc2,
                                    Date varicella1, Date varicella2, String teacher ) {

        Student tmpstudent = new Student(id, dob, age, name, gpa,
                contactName, emergencyPhone, mmrVacc1, mmrVacc2,
                varicella1,  varicella2,teacher);
        School.addStudent(tmpstudent);
        addClassRoom(tmpstudent);
        return tmpstudent;
    }
    public void addClassRoom(Student student){
    Optional<Teacher> t = School.teachers.stream().filter(e->e.getName().equalsIgnoreCase(student.getTeacherAssigned())).findFirst();
      Teacher teacher = t.get();
      teacher.setStudentCount(teacher.getStudentCount()+1);

      Optional<ClassRoom> cl = School.classrooms.stream().filter(e->e.getAgeGroup()!=null &&  e.getAgeGroup().equalsIgnoreCase(Student.getAgeGroupMapping(student.getAge()))).findAny();
      ClassRoom classRoom = null;
         if(cl.isPresent()){
         classRoom = cl.get();
         if(classRoom.getStudentTeacherGroup().containsKey(teacher.getName())){
                List<Student> std = classRoom.getStudentTeacherGroup().get(teacher.getName());
                std.add(student);
                classRoom.getStudentTeacherGroup().put(teacher.getName(), std);
            }
         else{
             //Map<String, List<Student>> tt = new HashMap<>();
            List<Student> stList = new ArrayList<>();
            stList.add(student);
           classRoom.getStudentTeacherGroup().put(teacher.getName(), stList);

//            tt.put(teacher.getName(), stList);
         //   classRoom.setStudentTeacherGroup(tt);

         }
        
         int index = School.classrooms.indexOf(classRoom);
         School.classrooms.set(index, classRoom);
         
      }
         else
         {
             classRoom = new ClassRoom();
                 classRoom.setCapacity(0);
            Map<String, List<Student>> tt = new HashMap<>();
            List<Student> stList = new ArrayList<>();
            stList.add(student);
            tt.put(teacher.getName(), stList);
            classRoom.setStudentTeacherGroup(tt);
            classRoom.setAgeGroup(Student.getAgeGroupMapping(student.getAge()));
             School.classrooms.add(classRoom);
             
         }
         
       //  classRoom.setCapacity(classRoom.getCapacity()+1);

    //     System.out.println(classRoom.teacherStudentGroup);


        //     System.out.println(classRoom.teacherStudentGroup);

    }

    public Student getObj(int id, Date dob, int age, String name,
                                 double gpa, String contactName,String emergencyPhone,
                                 Date mmrVacc1, Date mmrVacc2,
                                 Date varicella1, Date varicella2,int index,String teacher ) {

        Student tmpstudent = studentFactory.getObject(id, dob, age, name, gpa,
                contactName, emergencyPhone, mmrVacc1, mmrVacc2,
                varicella1,  varicella2,teacher);
        School.students.set(index, tmpstudent);
//        School.addStudent(tmpstudent);
        return tmpstudent;
    }

    public List<String> teachList(int age){
        List<String> teacherList = School.teachers.stream()
                .filter(e-> (e.getAgeGroupAssigned().equalsIgnoreCase(Student.getAgeGroupMapping(age)))&&Demo.studentRatioRules.get(Student.getAgeGroupMapping(age))>e.getStudentCount())
                .map(e->{return e.getName();}).collect(Collectors.toList());
        System.out.println(teacherList);
        return teacherList;
    }

}
