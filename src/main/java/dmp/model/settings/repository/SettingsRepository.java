package dmp.model.settings.repository;

import dmp.model.settings.Settings;
import org.springframework.data.repository.CrudRepository;

public interface SettingsRepository extends CrudRepository<Settings, Long> {
    Settings findByKeyName(String keyName);
}
