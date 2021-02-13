package crypto.db.generated;

import javax.annotation.Generated;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.sql.Column;

/**
 * Currency is a Querydsl bean type
 */
@Generated("com.coreoz.plume.db.querydsl.generation.IdBeanSerializer")
public class Currency extends com.coreoz.plume.db.querydsl.crud.CrudEntityQuerydsl {

    @Column("code")
    private String code;

    @Column("id")
    @JsonSerialize(using=com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private Long id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (id == null) {
            return super.equals(o);
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        Currency obj = (Currency) o;
        return id.equals(obj.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Currency#" + id;
    }

}

