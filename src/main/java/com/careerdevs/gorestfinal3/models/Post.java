package com.careerdevs.gorestfinal3.models;

import javax.persistence.*;

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

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id;

    private String title;

    @Column(length = 512) //update the default length
    private String body;


    public Long getId() {
        return id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    @Column(length = 512) //update the default length
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
