package vascopanigi.MoneyZen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vascopanigi.MoneyZen.entities.Label;
import vascopanigi.MoneyZen.exceptions.BadRequestException;
import vascopanigi.MoneyZen.payloads.label.NewLabelDTO;
import vascopanigi.MoneyZen.payloads.label.NewLabelResponseDTO;
import vascopanigi.MoneyZen.repositories.LabelRepository;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    private NewLabelResponseDTO save(NewLabelDTO body){
        this.labelRepository.findByName(body.name()).ifPresent(label -> {
            throw new BadRequestException("Label: " + body.name() + ", already exist.");
        });

        Label newLabel = new Label(body.name());
        return new NewLabelResponseDTO(newLabel.getName());
    }

}
