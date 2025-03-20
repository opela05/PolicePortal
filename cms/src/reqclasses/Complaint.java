package reqclasses;

public class Complaint {
    private long complaintId;
    private int precinctId;
    private String boroughName;
    private String dateOfCrime;  
    private String timeOfCrime;  
    private int offenseCode;
    private String locationDesc;
    private String specificLoc;
    private String offenseType;
    private String premisesDesc;
    private String reportDate;    
    private String suspAgeGroup;
    private String suspRace;
    private String suspSex;
    private String vicAgeGroup;
    private String vicRace;
    private String vicSex;
    private double latitude;
    private double longitude;

    public Complaint(long complaintId, int precinctId, String boroughName, String dateOfCrime,
                     String timeOfCrime, int offenseCode, String locationDesc, String specificLoc,
                     String offenseType, String premisesDesc, String reportDate, String suspAgeGroup,
                     String suspRace, String suspSex, String vicAgeGroup, String vicRace,
                     String vicSex, double latitude, double longitude) {
        this.complaintId = complaintId;
        this.precinctId = precinctId;
        this.boroughName = boroughName;
        this.dateOfCrime = dateOfCrime;
        this.timeOfCrime = timeOfCrime;
        this.offenseCode = offenseCode;
        this.locationDesc = locationDesc;
        this.specificLoc = specificLoc;
        this.offenseType = offenseType;
        this.premisesDesc = premisesDesc;
        this.reportDate = reportDate;
        this.suspAgeGroup = suspAgeGroup;
        this.suspRace = suspRace;
        this.suspSex = suspSex;
        this.vicAgeGroup = vicAgeGroup;
        this.vicRace = vicRace;
        this.vicSex = vicSex;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public long getComplaintId() {
        return complaintId;
    }
    public void setComplaintId(long complaintId) {
        this.complaintId = complaintId;
    }
    public int getPrecinctId() {
        return precinctId;
    }
    public void setPrecinctId(int precinctId) {
        this.precinctId = precinctId;
    }
    public String getBoroughName() {
        return boroughName;
    }
    public void setBoroughName(String boroughName) {
        this.boroughName = boroughName;
    }
    public String getDateOfCrime() {
        return dateOfCrime;
    }
    public void setDateOfCrime(String dateOfCrime) {
        this.dateOfCrime = dateOfCrime;
    }
    public String getTimeOfCrime() {
        return timeOfCrime;
    }
    public void setTimeOfCrime(String timeOfCrime) {
        this.timeOfCrime = timeOfCrime;
    }
    public int getOffenseCode() {
        return offenseCode;
    }
    public void setOffenseCode(int offenseCode) {
        this.offenseCode = offenseCode;
    }
    public String getLocationDesc() {
        return locationDesc;
    }
    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }
    public String getSpecificLoc() {
        return specificLoc;
    }
    public void setSpecificLoc(String specificLoc) {
        this.specificLoc = specificLoc;
    }
    public String getOffenseType() {
        return offenseType;
    }
    public void setOffenseType(String offenseType) {
        this.offenseType = offenseType;
    }
    public String getPremisesDesc() {
        return premisesDesc;
    }
    public void setPremisesDesc(String premisesDesc) {
        this.premisesDesc = premisesDesc;
    }
    public String getReportDate() {
        return reportDate;
    }
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
    public String getSuspAgeGroup() {
        return suspAgeGroup;
    }
    public void setSuspAgeGroup(String suspAgeGroup) {
        this.suspAgeGroup = suspAgeGroup;
    }
    public String getSuspRace() {
        return suspRace;
    }
    public void setSuspRace(String suspRace) {
        this.suspRace = suspRace;
    }
    public String getSuspSex() {
        return suspSex;
    }
    public void setSuspSex(String suspSex) {
        this.suspSex = suspSex;
    }
    public String getVicAgeGroup() {
        return vicAgeGroup;
    }
    public void setVicAgeGroup(String vicAgeGroup) {
        this.vicAgeGroup = vicAgeGroup;
    }
    public String getVicRace() {
        return vicRace;
    }
    public void setVicRace(String vicRace) {
        this.vicRace = vicRace;
    }
    public String getVicSex() {
        return vicSex;
    }
    public void setVicSex(String vicSex) {
        this.vicSex = vicSex;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "Complaint:\n" +
                "\ncomplaintId=" + complaintId +
                "\nprecinctId=" + precinctId +
                "\nboroughName='" + boroughName + '\'' +
                "\ndateOfCrime='" + dateOfCrime + '\'' +
                "\ntimeOfCrime='" + timeOfCrime + '\'' +
                "\noffenseCode=" + offenseCode +
                "\nlocationDesc='" + locationDesc + '\'' +
                "\nspecificLoc='" + specificLoc + '\'' +
                "\noffenseType='" + offenseType + '\'' +
                "\npremisesDesc='" + premisesDesc + '\'' +
                "\nreportDate='" + reportDate + '\'' +
                "\nsuspAgeGroup='" + suspAgeGroup + '\'' +
                "\nsuspRace='" + suspRace + '\'' +
                "\nsuspSex='" + suspSex + '\'' +
                "\nvicAgeGroup='" + vicAgeGroup + '\'' +
                "\nvicRace='" + vicRace + '\'' +
                "\nvicSex='" + vicSex + '\'' +
                "\nlatitude=" + latitude +
                "\nlongitude=" + longitude +
                '}';
    }
}