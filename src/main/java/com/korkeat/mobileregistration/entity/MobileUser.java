package com.korkeat.mobileregistration.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@Entity
@Table(name="mobile_users",
       indexes = {@Index(name="ref_code_index", columnList = "ref_code")}
      )
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MobileUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name="phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name="salary")
    private long salary;

    @Column(name="ref_code")
    @JsonProperty("ref_code")
    private String refCode;

    @Column(name="member_type")
    @JsonProperty("member_type")
    private MobileUserType mobileUserType;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @ConstructorProperties({"phoneNumber", "salary"})
    public MobileUser(String phoneNumber, long salary) throws Exception {
        this.phoneNumber = normalizePhoneNumber(phoneNumber);
        this.salary = salary;
        this.refCode = createRefCode(phoneNumber);
        this.mobileUserType = getMemberTypeBySalary(salary);
    }

    private MobileUserType getMemberTypeBySalary(long salary) throws Exception {
        if(isOverOrEqual(salary, 50000)) {
            return MobileUserType.PLATINUM;
        } else if (isOverOrEqual(salary, 30000)) {
            return MobileUserType.GOLD;
        } else if(isOverOrEqual(salary, 15000)) {
            return MobileUserType.SILVER;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Salary under minimum requirement");
    }

    private boolean isOverOrEqual(long num, long min) {
        return num >= min;
    }

    private String createRefCode(String phoneNumber) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyyMMdd")
                .withZone(ZoneOffset.UTC);
        String normalizedPhoneNumber = normalizePhoneNumber(phoneNumber);
        return formatter.format(Instant.now()) + StringUtils.right(normalizedPhoneNumber, 4);
    }

    private String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber
                .replace("-", "")
                .replace(" ", "");
    }

    @PrePersist
    private void createTimestamp() {
        this.createdAt = this.updatedAt = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = new Date(System.currentTimeMillis());
    }
}
