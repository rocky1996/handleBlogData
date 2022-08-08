package com.acat.handleBlogData.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "blog_system_index_target_data")
public class BlogSystemIndexTargetDataEntity implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "index")
    private String index;

    @Column(name = "index_chinese_name")
    private String indexChineseName;

    @Column(name = "before_treatment")
    private String beforeTreatment;

    @Column(name = "after_treatment")
    private String afterTreatment;

    @Column(name = "governance_failure")
    private String governanceFailure;

    @Column(name = "warehousing_succeeded")
    private String warehousingSucceeded;

    @Column(name = "warehousing_failed")
    private String warehousingFailed;

    @Column(name = "removal_quantity")
    private String removalQuantity;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
