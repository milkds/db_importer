package importer.export;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DuplicateAttributeRemover {
    private List<ExportFitEntity> fitEntities;
    private String GEN_DIVIDER;

    DuplicateAttributeRemover(List<ExportFitEntity> entities, String GEN_DIVIDER) {
        this.fitEntities = entities;
        this.GEN_DIVIDER = GEN_DIVIDER;
    }

    void removeAttributeDuplicates() {
        removeCarAttDuplicates();
        removeFitAttDuplicates();


        Set<String> carAttributes = new HashSet<>();
        fitEntities.forEach(entity->{
            String allCurrentCarAtts = entity.getAllCarAttributes();
            String[] split = allCurrentCarAtts.split(GEN_DIVIDER);
            List<String> filteredCurrentCarAtts = new ArrayList<>();
            for (String s: split){
                if (carAttributes.contains(s)) {
                    continue;
                }
                carAttributes.add(s);
                filteredCurrentCarAtts.add(s);
            }
            String allFilteredAtts = buildCarAtts(filteredCurrentCarAtts);
            entity.setAllCarAttributes(allFilteredAtts);
        });
    }

    private void removeFitAttDuplicates() {

    }

    private void removeCarAttDuplicates() {

    }

    private String buildCarAtts(List<String> filteredCurrentCarAtts) {
        StringBuilder attBuilder = new StringBuilder();
        filteredCurrentCarAtts.forEach(att->{
            attBuilder.append(att).append(GEN_DIVIDER);
        });

        return attBuilder.toString();
    }
}
