package dmp.model.user.repository;

import dmp.model.user.Images;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends CrudRepository<Images, Long> {	
	
}
