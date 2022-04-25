package com.careerdevs.gorestfinal3.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Comment {
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

    private int port_id;
    private String name;
    private String email;
    private String body;

    public long getId() {
        return id;
    }

    public int getPort_id() {
        return port_id;
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
                ", port_id=" + port_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
