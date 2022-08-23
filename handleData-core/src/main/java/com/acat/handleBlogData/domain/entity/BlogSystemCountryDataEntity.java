package com.acat.handleBlogData.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "blog_system_country_data")
public class BlogSystemCountryDataEntity implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country")
    private String country;
}
