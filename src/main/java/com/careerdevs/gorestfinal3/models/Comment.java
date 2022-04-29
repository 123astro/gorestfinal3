package com.careerdevs.gorestfinal3.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Comment {

    //https://gorest.co.in/public/v2/comments
//    {
//        id: 1398,
//                post_id: 1383,
//            name: "Brajesh Dubashi",
//            email: "brajesh_dubashi@halvorson.net",
//            body: "Nisi ut aspernatur. Non voluptatibus ipsam. Quae soluta voluptas. Minus doloribus est."
//    },

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private long post_id;
    private String name;
    private String email;
    private String body;

    public long getId() {
        return id;
    }

    public long getPost_id() {
        return post_id;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

}
