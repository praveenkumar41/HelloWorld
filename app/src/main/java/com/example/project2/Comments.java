package com.example.project2;

public class Comments
{
    public String comment;
    public String publisher;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


    public Comments(String comment,String publisher)
    {
        this.comment=comment;
        this.publisher=publisher;
    }
    public Comments()
    {

    }

}
