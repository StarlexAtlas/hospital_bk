package online.starlex.hospital.entity;

import javax.persistence.*;

@Table
@Entity(name = "drug_info")
public class DrugInfo {
    @Id
    @Column(name = "drug_code", columnDefinition = "bigint")
    private long drugCode;
    @Column(name = "drug_name", columnDefinition = "varchar(255)")
    private String drugName;
    @Column(name = "specification", columnDefinition = "varchar(255)")
    private String specification;
    @Column(name = "company", columnDefinition = "varchar(255)")
    private String company;
    @Column(name = "form", columnDefinition = "varchar(255)")
    private String form;

    public long getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(long drugCode) {
        this.drugCode = drugCode;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
