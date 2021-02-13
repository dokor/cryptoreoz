package crypto.db.generated;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QCurrency is a Querydsl query type for Currency
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QCurrency extends com.querydsl.sql.RelationalPathBase<Currency> {

    private static final long serialVersionUID = 546473907;

    public static final QCurrency currency = new QCurrency("cry_currency");

    public final StringPath code = createString("code");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<Currency> primary = createPrimaryKey(id);

    public QCurrency(String variable) {
        super(Currency.class, forVariable(variable), "null", "cry_currency");
        addMetadata();
    }

    public QCurrency(String variable, String schema, String table) {
        super(Currency.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QCurrency(String variable, String schema) {
        super(Currency.class, forVariable(variable), schema, "cry_currency");
        addMetadata();
    }

    public QCurrency(Path<? extends Currency> path) {
        super(path.getType(), path.getMetadata(), "null", "cry_currency");
        addMetadata();
    }

    public QCurrency(PathMetadata metadata) {
        super(Currency.class, metadata, "null", "cry_currency");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(code, ColumnMetadata.named("code").withIndex(2).ofType(Types.VARCHAR).withSize(255).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

