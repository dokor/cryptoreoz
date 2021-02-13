package crypto.db.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QPlatform is a Querydsl query type for Platform
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QPlatform extends com.querydsl.sql.RelationalPathBase<Platform> {

    private static final long serialVersionUID = 1845755925;

    public static final QPlatform platform = new QPlatform("cry_platform");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<Platform> primary = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<UserPlatform> _cryUserPlatformCryPlatformIdFk = createInvForeignKey(id, "id_platform");

    public QPlatform(String variable) {
        super(Platform.class, forVariable(variable), "null", "cry_platform");
        addMetadata();
    }

    public QPlatform(String variable, String schema, String table) {
        super(Platform.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPlatform(String variable, String schema) {
        super(Platform.class, forVariable(variable), schema, "cry_platform");
        addMetadata();
    }

    public QPlatform(Path<? extends Platform> path) {
        super(path.getType(), path.getMetadata(), "null", "cry_platform");
        addMetadata();
    }

    public QPlatform(PathMetadata metadata) {
        super(Platform.class, metadata, "null", "cry_platform");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(255));
    }

}

