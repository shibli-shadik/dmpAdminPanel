package dmp.model.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "dmp_user")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String firstName;
    
    private String middleName;
    
    private String lastName;
    
    @Transient
    private String displayName;
    
    @NotNull
    @Column(length = 500)
    private String password;
    
    @Column(length = 500)
    private String salt;
    
    @Email
    @Column(unique = true)
    @NotNull
    private String email;
    
    @Column(unique = true)
    private String mobile;
    
    private String imeiNo;
    
    private int successiveFailedLogins;
    
    private Timestamp disabledDateTime;
    
    @Column(name = "is_deleted", nullable = false, columnDefinition = "char DEFAULT 0", length = 1)
    private Boolean isDeleted;
    
    @Column(name = "is_enabled", nullable = false, columnDefinition = "char DEFAULT 0", length = 1)
    private Boolean isEnabled;
    
    @Column(name = "has_sequrity_group", nullable = false, columnDefinition = "char DEFAULT 0", length = 1)
    private Boolean hasSequrityGroup;
    
    @Column(name = "is_password_reset", nullable = false, columnDefinition = "char DEFAULT 0", length = 1)
    private Boolean isPasswordReset;
    
    @Column(name = "is_password_expired", nullable = false, columnDefinition = "char DEFAULT 0", length = 1)
    private Boolean isPasswordExpired;
    
    private Timestamp passwordUpdatedOn;
    
    private String dateOfBirth;
    
    private String nationalIdCardNo;
    
    @Column(length = 1000)
    private String address;
    
    @OneToOne(fetch = FetchType.LAZY)
    private Role role;
    
    @ManyToOne
    @JoinColumn(name = "picture_id")
    private Images image;
    
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private final Date createdAt = new Date();
    
    @NotNull
    private Timestamp updatedAt;
    
    public Long getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getDisplayName() {
        
        String fullName = "";
        
        if(this.middleName == null)
        {
            fullName = this.firstName + ' ' + this.lastName;
        }
        else
        {
            fullName = this.firstName + ' ' + this.middleName + ' ' + this.lastName;
        }
        
        return fullName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSalt() {
        return salt;
    }
    
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getImeiNo() {
        return imeiNo;
    }
    
    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }
    
    public Boolean getHasSequrityGroup() {
        return hasSequrityGroup;
    }
    
    public void setHasSequrityGroup(Boolean hasSequrityGroup) {
        this.hasSequrityGroup = hasSequrityGroup;
    }
    
    public Boolean getIsPasswordReset() {
        return isPasswordReset;
    }
    
    public void setIsPasswordReset(Boolean isPasswordReset) {
        this.isPasswordReset = isPasswordReset;
    }
    
    public Boolean getIsPasswordExpired() {
        return isPasswordExpired;
    }
    
    public void setIsPasswordExpired(Boolean isPasswordExpired) {
        this.isPasswordExpired = isPasswordExpired;
    }
    
    public Timestamp getPasswordUpdatedOn() {
        return passwordUpdatedOn;
    }
    
    public void setPasswordUpdatedOn(Timestamp passwordUpdatedOn) {
        this.passwordUpdatedOn = passwordUpdatedOn;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getNationalIdCardNo() {
        return nationalIdCardNo;
    }
    
    public void setNationalIdCardNo(String nationalIdCardNo) {
        this.nationalIdCardNo = nationalIdCardNo;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public boolean isDeleted() {
        return isDeleted;
    }
    
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public boolean isEnabled() {
        return isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public int getSuccessiveFailedLogins() {
        return successiveFailedLogins;
    }
    
    public void setSuccessiveFailedLogins(int successiveFailedLogins) {
        this.successiveFailedLogins = successiveFailedLogins;
    }
    
    public Timestamp getDisabledDateTime() {
        return disabledDateTime;
    }
    
    public void setDisabledDateTime(Timestamp disabledDateTime) {
        this.disabledDateTime = disabledDateTime;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Images getImage() {
        return image;
    }
    
    public void setImage(Images image) {
        this.image = image;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
