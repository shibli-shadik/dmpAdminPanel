package dmp.controller.admin.setup;

import dmp.model.user.Images;
import dmp.model.user.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping(value = "/images")
public class ImageController {
    
    @Autowired
    private ImagesRepository imagesRepository;
    
    @RequestMapping(value = "/getImage/{imgId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long imgId)
            throws SQLException
    {
        Images img = imagesRepository.findById(imgId).get();
        
        return ResponseEntity
                .ok()
                .header("Content-Disposition",
                        "inline;filename=\"" + img.getFileName() + "\"")
                .contentLength(img.getFileContent().length())
                .body(new InputStreamResource(img.getFileContent()
                        .getBinaryStream()));
    }
}
