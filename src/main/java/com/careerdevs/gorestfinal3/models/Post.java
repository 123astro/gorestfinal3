package com.careerdevs.gorestfinal3.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Post {

    //https://gorest.co.in/public/v2/posts


//    {
//        id: 1283,
//         user_id: 2562,
//         title: "Cilicium supra ut terebro deorsum dolorem tam demitto tergeo artificiose et.",
//         body: "tendus acies."
//    },

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int user_id;
    private String title;
    private String body;


    public Long getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
