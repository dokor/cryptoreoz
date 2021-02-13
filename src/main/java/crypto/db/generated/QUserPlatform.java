package crypto.db.generated;

import com.coreoz.plume.admin.db.generated.AdminUser;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QUserPlatform is a Querydsl query type for UserPlatform
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QUserPlatform extends com.querydsl.sql.RelationalPathBase<UserPlatform> {

    private static final long serialVersionUID = 101037056;

    public static final QUserPlatform userPlatform = new QUserPlatform("cry_user_platform");

    public final StringPath apiKey = createString("apiKey");

    public final NumberPath<Long> idPlatform = createNumber("idPlatform", Long.class);

    public final NumberPath<Long> idUser = createNumber("idUser", Long.class);

    public final StringPath secretKey = createString("secretKey");

    public final com.querydsl.sql.PrimaryKey<UserPlatform> primary = createPrimaryKey(idPlatform, idUser);

    public final com.querydsl.sql.ForeignKey<AdminUser> cryUserPlatformPLMUSERIdFk = createForeignKey(idUser, "id");

    public final com.querydsl.sql.ForeignKey<Platform> cryUserPlatformCryPlatformIdFk = createForeignKey(idPlatform, "id");

    public QUserPlatform(String variable) {
        super(UserPlatform.class, forVariable(variable), "null", "cry_user_platform");
        addMetadata();
    }

    public QUserPlatform(String variable, String schema, String table) {
        super(UserPlatform.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QUserPlatform(String variable, String schema) {
        super(UserPlatform.class, forVariable(variable), schema, "cry_user_platform");
        addMetadata();
    }

    public QUserPlatform(Path<? extends UserPlatform> path) {
        super(path.getType(), path.getMetadata(), "null", "cry_user_platform");
        addMetadata();
    }

    public QUserPlatform(PathMetadata metadata) {
        super(UserPlatform.class, metadata, "null", "cry_user_platform");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(apiKey, ColumnMetadata.named("api_key").withIndex(3).ofType(Types.VARCHAR).withSize(255));
        addMetadata(idPlatform, ColumnMetadata.named("id_platform").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(idUser, ColumnMetadata.named("id_user").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(secretKey, ColumnMetadata.named("secret_key").withIndex(4).ofType(Types.VARCHAR).withSize(255));
    }

}

