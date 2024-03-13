package com.bbangle.bbangle.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -982771437L;

    public static final QMember member = new QMember("member1");

    public final com.bbangle.bbangle.model.QBaseEntity _super = new com.bbangle.bbangle.model.QBaseEntity(this);

    public final StringPath birth = createString("birth");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath phone = createString("phone");

    public final StringPath profile = createString("profile");

    public final ListPath<com.bbangle.bbangle.model.WishlistFolder, com.bbangle.bbangle.model.QWishlistFolder> wishlistFolders = this.<com.bbangle.bbangle.model.WishlistFolder, com.bbangle.bbangle.model.QWishlistFolder>createList("wishlistFolders", com.bbangle.bbangle.model.WishlistFolder.class, com.bbangle.bbangle.model.QWishlistFolder.class, PathInits.DIRECT2);

    public final ListPath<com.bbangle.bbangle.model.WishlistStore, com.bbangle.bbangle.model.QWishlistStore> wishlistStores = this.<com.bbangle.bbangle.model.WishlistStore, com.bbangle.bbangle.model.QWishlistStore>createList("wishlistStores", com.bbangle.bbangle.model.WishlistStore.class, com.bbangle.bbangle.model.QWishlistStore.class, PathInits.DIRECT2);

    public final ListPath<Withdrawal, QWithdrawal> withdrawals = this.<Withdrawal, QWithdrawal>createList("withdrawals", Withdrawal.class, QWithdrawal.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

