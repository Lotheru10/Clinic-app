package pl.edu.agh.to.clinicapp.opinion;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to.clinicapp.dto.opinion_dto.CreateOpinionDTO;

@RestController
@RequestMapping(path = "api/opinions")
@Tag(name ="Opinion", description = "API for managing opinions")
public class OpinionController {
    private final OpinionService opinionService;

    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addOpinion(CreateOpinionDTO createOpinionDTO){
        opinionService.addOpinion(createOpinionDTO);
    }


}
