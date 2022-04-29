package com.careerdevs.gorestfinal3.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ToDo {

    //https://gorest.co.in/public/v2/todos
//        id: 1431,
//        user_id: 2835,
//        title: "Agnosco doloribus deludo aufero quod sed cresco accusamus aut acidus torrens.",
//        due_on: "2022-05-15T00:00:00.000+05:30",
//         status: "pending"
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long user_id;
    private String title;
    private Date due_on;
    private String status;

    public long getId() {
        return id;
    }
    public long getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDue_on() {
        return due_on;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "user_id=" + user_id +
                ", title='" + title + '\'' +
                ", due_on=" + due_on +
                ", status='" + status + '\'' +
                '}';
    }

}
