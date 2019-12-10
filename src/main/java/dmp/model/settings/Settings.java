package dmp.model.settings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dmp_settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String keyName;
    private String keyValue;
    
    public long getId() {
        return id;
    }
    
    public String getKeyName() {
        return keyName;
    }
    
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    
    public String getKeyValue() {
        return keyValue;
    }
    
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
