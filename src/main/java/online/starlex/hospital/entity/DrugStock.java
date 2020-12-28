package online.starlex.hospital.entity;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.util.Date;

@Table
@Entity(name = "drug_stock")
public class DrugStock {
    @Id
    @Column(name = "drug_id", columnDefinition = "bigint")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("drug_id")
    private long drugId;
    @Column(name = "drug_name", columnDefinition = "varchar(255)")
    @SerializedName("drug_name")
    private String drugName;
    @Column(name = "drug_code", columnDefinition = "bigint")
    @SerializedName("drug_code")
    private long drugCode;
    @Column(name = "expiration_date", columnDefinition = "date")
    @SerializedName("expiration_date")
    private Date expirationDate;
    @Column(name = "value", columnDefinition = "int")
    @SerializedName("value")
    private int value;
    @Column(name = "stock", columnDefinition = "bigint")
    @SerializedName("stock")
    private long stock;
    @Column(name = "frozen_stock" ,columnDefinition = "bigint")
    @SerializedName("frozen_stock")
    private long frozenStock;

    public long getDrugId() {
        return drugId;
    }

    public void setDrugId(long drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public long getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(long drugCode) {
        this.drugCode = drugCode;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getFrozenStock() {
        return frozenStock;
    }

    public void setFrozenStock(long frozenStock) {
        this.frozenStock = frozenStock;
    }
}
