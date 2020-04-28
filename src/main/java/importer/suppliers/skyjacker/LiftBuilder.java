package importer.suppliers.skyjacker;

import importer.entities.FitmentAttribute;

import java.util.HashSet;
import java.util.Set;

public class LiftBuilder {
    private int rear_lift_start;
    private int rear_lift_finish;
    private int front_lift_start;
    private int front_lift_finish;

    private boolean isFront;
    private boolean isRear;

    public LiftBuilder(Set<FitmentAttribute> attributes) {
        if (attributes.size()==0){
            return;
        }
        Set<FitmentAttribute> newAttributes = new HashSet<>();
        attributes.forEach(att->{
            FitmentAttribute newAtt = processAttribute(att);
            if (newAtt!=null){
                newAttributes.add(newAtt);
            }
        });
        if (newAttributes.size()>0){
            setPositions(newAttributes);
            attributes.addAll(newAttributes);
        }
    }

    private void setPositions(Set<FitmentAttribute> newAttributes) {
    }

    private FitmentAttribute processAttribute(FitmentAttribute att) {
        boolean curPositionRear;
        boolean curPositionFront;
        String value = att.getFitmentAttValue();
        if (value.contains("Rear")){
            curPositionRear = true;
        }
        else if (value.contains("Front")){
            curPositionFront = true;
        }
        else {
            return null;
        }
        int curLift = getLift(value);

        return null;
    }

    private int getLift(String value) {



        return 0;
    }
}
