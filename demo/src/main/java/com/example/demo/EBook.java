package com.example.demo;
import jakarta.persistence.*;


@Entity 
@Table (name="e_books")
@PrimaryKeyJoinColumn(name = "book_id")// lein ket khpa chinh voi cha 


public class EBook extends Book {
    private String fileFormart;// EPUB,PDF
    private double fileSize;
    private String downloadLink;
    private int deviceLimit;// goi han so thiet bi dang nhap

    // getter
    public String getFileFormart(){
        return fileFormart;
    }
    public double getFileSize(){
        return fileSize;
    }
    public String getDownloadLink(){
        return downloadLink;
    }
    public int getDeviceLimit(){
        return deviceLimit;
    }

    // stter
    public void setFileFormat(String fileFromat){
        this.fileFormart=fileFromat;
    }
    public void setFileSize(double fileSize){
        this.fileSize=fileSize;
    }
    public void setDownloadLink(String downloadLink){
        this.downloadLink=downloadLink;
    }
    public void setDeviceLimit(int deviceLimit){
        this.deviceLimit=deviceLimit;
    }

}
