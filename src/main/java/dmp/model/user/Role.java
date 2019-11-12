package dmp.model.user;

import dmp.model.enums.RoleType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dmp_roles")
@Data
@NoArgsConstructor
public class Role implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private RoleType roleType;
    
    private String roleTypeDisplayName;
}

