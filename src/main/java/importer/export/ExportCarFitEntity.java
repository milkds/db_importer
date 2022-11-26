package importer.export;

import java.util.Map;
import java.util.Set;

class ExportCarFitEntity {
    private Map<String, Set<String>> carAttMap;
    private Map<String, Set<String>> fitAttMap;
    private Set<String> positions;
    private Set<String> drives;
    private String carYearAttributes;
    private String carLiftAttributes;
    private String carLine;

    void setCarAttMap(Map<String, Set<String>> carAttMap) {
        this.carAttMap = carAttMap;
    }

    void setFitAttMap(Map<String, Set<String>> fitAttMap) {
        this.fitAttMap = fitAttMap;
    }

    void setPositions(Set<String> positions) {
        this.positions = positions;
    }

    public Set<String> getPositions() {
        return positions;
    }

    void setDrives(Set<String> drives) {
        this.drives = drives;
    }

    public Set<String> getDrives() {
        return drives;
    }

    public Map<String, Set<String>> getCarAttMap() {
        return carAttMap;
    }

    public Map<String, Set<String>> getFitAttMap() {
        return fitAttMap;
    }

    void setCarYearAttributes(String carYearAttributes) {
        this.carYearAttributes = carYearAttributes;
    }

    public String getCarYearAttributes() {
        return carYearAttributes;
    }

    void setCarLiftAttributes(String carLiftAttributes) {
        this.carLiftAttributes = carLiftAttributes;
    }

    public String getCarLiftAttributes() {
        return carLiftAttributes;
    }

    public String getCarLine() {
        return carLine;
    }

    public void setCarLine(String carLine) {
        this.carLine = carLine;
    }
}
