package importer.export;

import java.util.*;

class AttributeGrouper {
    private String carLine;
    private List<ExportFitEntity> fitEntities;
    private String GEN_DIVIDER;
    private String ATT_DIVIDER;

    AttributeGrouper(List<ExportFitEntity> entities, String GEN_DIVIDER, String ATT_DIVIDER) {
        this.carLine = carLine;
        this.fitEntities = entities;
        this.GEN_DIVIDER = GEN_DIVIDER;
        this.ATT_DIVIDER = ATT_DIVIDER;
    }

    //Method is really big, but it is so because of performance issue (we don't want to iterate same set 4 times
    ExportCarFitEntity getGroupedAttributes() {
        ExportCarFitEntity result = new ExportCarFitEntity();
        Map<String, Set<String>> carAttMap = new HashMap<>();
        Map<String, Set<String>> fitAttMap = new HashMap<>();
        Set<String> positions = new HashSet<>();
        Set<String> drives = new HashSet<>();
        fitEntities.forEach(fitEntity -> {
            String[] carSplit = fitEntity.getAllCarAttributes().split(GEN_DIVIDER);
            for (String s: carSplit){
                if (s.length()==0){
                    continue;
                }
                String[] carAttSplit = s.split(ATT_DIVIDER);
                Set<String> curSet = carAttMap.computeIfAbsent(carAttSplit[0], k -> new HashSet<>());
                curSet.add(carAttSplit[1]);
            }

            String[] fitSplit = fitEntity.getAllOtherFitAttributes().split(GEN_DIVIDER);
            for (String s: fitSplit){
                if (s.length()==0){
                    continue;
                }
                String[] fitAttSplit = s.split(ATT_DIVIDER);
                Set<String> curSet = fitAttMap.computeIfAbsent(fitAttSplit[0], k -> new HashSet<>());
                curSet.add(fitAttSplit[1]);
            }

            positions.add(fitEntity.getPosition());
            drives.add(fitEntity.getDrive());
        });
        ExportFitEntity entity = fitEntities.get(0);
        result.setCarAttMap(carAttMap);
        result.setFitAttMap(fitAttMap);
        result.setPositions(positions);
        result.setDrives(drives);
        result.setCarYearAttributes(entity.getCarYearAttributes());
        result.setCarLiftAttributes(entity.getCarLiftAttributes());
        result.setCarLine(getCarLine(entity));

        return result;
    }

    private String getCarLine(ExportFitEntity entity) {
        return entity.getCarMake() + " " + entity.getCarModel()+ " " +
                entity.getYearStart() + "-" + entity.getYearFinish();
    }

}
