package importer.service;

import importer.dao.FitmentDAO;
import importer.entities.FitmentAttribute;
import importer.entities.ProductionFitment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FitmentService {
    private static final Logger logger = LogManager.getLogger(FitmentService.class.getName());

    public void saveFitment(ProductionFitment fitment, Session session) {
        new CarService().saveCar(fitment, session);
        prepareFitmentAttributes(fitment, session);
        FitmentDAO.saveFitment(fitment, session);
        logger.debug("Fitment saved: " + fitment);
    }

    private void prepareFitmentAttributes(ProductionFitment fitment, Session session) {
        Set<FitmentAttribute> attributes = fitment.getFitmentAttributes();
        Set<FitmentAttribute> checkedAttributes = new HashSet<>();
        attributes.forEach(attribute->{
            FitmentAttribute checkedAtt = FitmentDAO.checkFitAttribute(attribute, session);
            checkedAttributes.add(Objects.requireNonNullElse(checkedAtt, attribute));
        });
        fitment.setFitmentAttributes(checkedAttributes);
    }
}
