package dmp.model.user;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dmp_user_log")
@Data
@NoArgsConstructor
public class UserLog implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(length = 1000)
    private String token;
    
    private String ipAddress;
    
    private String imeiNo;
    
    @NotNull
    private Timestamp loginTime;
    
    private Timestamp logoutTime;
    
    @Column(name = "is_logged_in", nullable = false, columnDefinition = "char DEFAULT 0", length = 1)
    private Boolean isLoggedIn;
}