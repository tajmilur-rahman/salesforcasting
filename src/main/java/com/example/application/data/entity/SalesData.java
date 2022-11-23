package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class SalesData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long Id;

    Date recordDate;

    String productCategory;

    double productSale;

}
