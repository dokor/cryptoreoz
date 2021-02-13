package crypto.db.generated;

import javax.annotation.Generated;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.sql.Column;

/**
 * UserPlatform is a Querydsl bean type
 */
@Generated("com.coreoz.plume.db.querydsl.generation.IdBeanSerializer")
public class UserPlatform {

    @Column("api_key")
    private String apiKey;

    @Column("id_platform")
    @JsonSerialize(using=com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private Long idPlatform;

    @Column("id_user")
    @JsonSerialize(using=com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    private Long idUser;

    @Column("secret_key")
    private String secretKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Long getIdPlatform() {
        return idPlatform;
    }

    public void setIdPlatform(Long idPlatform) {
        this.idPlatform = idPlatform;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public boolean equals(Object o) {
        if (idPlatform == null || idUser == null) {
            return super.equals(o);
        }
        if (!(o instanceof UserPlatform)) {
            return false;
        }
        UserPlatform obj = (UserPlatform) o;
        return idPlatform.equals(obj.idPlatform) && idUser.equals(obj.idUser);
    }

    @Override
    public int hashCode() {
        if (idPlatform == null || idUser == null) {
            return super.hashCode();
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + idPlatform.hashCode();
        result = prime * result + idUser.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserPlatform#" + idPlatform+ ";" + idUser;
    }

}

