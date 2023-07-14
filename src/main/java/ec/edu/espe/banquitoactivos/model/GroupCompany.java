package ec.edu.espe.banquitoactivos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

import java.util.Date;

@Entity
@Table(name = "GROUP_COMPANY")
public class GroupCompany {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "GROUP_COMPANY_ID", nullable = false)
    private Integer id;

    @Column(name = "BRANCH_ID", nullable = false)
    private Integer branchId;

    @Column(name = "LOCATION_ID", nullable = false)
    private Integer locationId;

    @Column(name = "UNIQUE_KEY", length = 36)
    private String uniqueKey;

    @Column(name = "GROUP_NAME", nullable = false, length = 200)
    private String groupName;

    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 100)
    private String emailAddress;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "LINE1", nullable = false, length = 100)
    private String line1;

    @Column(name = "LINE2", nullable = false, length = 100)
    private String line2;

    @Column(name = "LATITUDE", nullable = false, precision = 0)
    private Float latitude;

    @Column(name = "LONGITUDE", nullable = false, precision = 0)
    private Float longitude;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTIVATION_DATE")
    private Date activationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

    @Column(name = "STATE", nullable = false, length = 3)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLOSED_DATE")
    private Date closedDate;

    @Column(name = "COMMENTS", nullable = false, length = 500)
    private String comments;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "BRANCH_ID", referencedColumnName = "BRANCH_ID", nullable = false, insertable = false, updatable = false)
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "LOCATION_ID", nullable = false, insertable = false, updatable = false)
    private GeoLocation geoLocation;

    public GroupCompany() {
    }

    public GroupCompany(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "GroupCompany{" +
                "id=" + id +
                ", branchId=" + branchId +
                ", locationId=" + locationId +
                ", uniqueKey='" + uniqueKey + '\'' +
                ", groupName='" + groupName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", creationDate=" + creationDate +
                ", activationDate=" + activationDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", state='" + state + '\'' +
                ", closedDate=" + closedDate +
                ", comments='" + comments + '\'' +
                ", branch=" + branch +
                ", geoLocation=" + geoLocation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GroupCompany that = (GroupCompany) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
