package com.bbangle.bbangle.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSignatureAgreement is a Querydsl query type for SignatureAgreement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSignatureAgreement extends EntityPathBase<SignatureAgreement> {

    private static final long serialVersionUID = -2015049429L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSignatureAgreement signatureAgreement = new QSignatureAgreement("signatureAgreement");

    public final BooleanPath agreementStatus = createBoolean("agreementStatus");

    public final DateTimePath<java.time.LocalDateTime> dateOfSignature = createDateTime("dateOfSignature", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final EnumPath<Agreement> name = createEnum("name", Agreement.class);

    public QSignatureAgreement(String variable) {
        this(SignatureAgreement.class, forVariable(variable), INITS);
    }

    public QSignatureAgreement(Path<? extends SignatureAgreement> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSignatureAgreement(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSignatureAgreement(PathMetadata metadata, PathInits inits) {
        this(SignatureAgreement.class, metadata, inits);
    }

    public QSignatureAgreement(Class<? extends SignatureAgreement> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

