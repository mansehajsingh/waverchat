package com.waverchat.api.v1.util.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Visitor;
import org.springframework.lang.Nullable;

class InheritableBooleanBuilder {

    public BooleanBuilder builder;

    public InheritableBooleanBuilder() {
        this.builder = new BooleanBuilder();
    }

    public InheritableBooleanBuilder(Predicate initial) {
        this.builder = new BooleanBuilder(initial);
    }

    public BooleanBuilder getBuilder() {
        return builder;
    }

    public <R, C> R accept(Visitor<R, C> v, C context) {
        return this.builder.accept(v, context);
    }

    public InheritableBooleanBuilder and(@Nullable Predicate right) {
        this.builder = builder.and(right);
        return this;
    }

    public InheritableBooleanBuilder andAnyOf(Predicate... args) {
        this.builder = this.builder.andAnyOf(args);
        return this;
    }

    public InheritableBooleanBuilder andNot(Predicate right) {
        this.builder = this.builder.and(right.not());
        return this;
    }

    public InheritableBooleanBuilder clone() throws CloneNotSupportedException {
        return (InheritableBooleanBuilder)super.clone();
    }

    public boolean equals(Object o) {
        return this.builder.equals(o);
    }

    public @Nullable Predicate getValue() {
        return this.builder.getValue();
    }

    public int hashCode() {
        return this.builder.hashCode();
    }

    public boolean hasValue() {
        return this.builder.hasValue();
    }

    public InheritableBooleanBuilder not() {
        this.builder = this.builder.not();
        return this;
    }

    public InheritableBooleanBuilder or(@Nullable Predicate right) {
        this.builder = this.builder.or(right);
        return this;
    }

    public InheritableBooleanBuilder orAllOf(Predicate... args) {
        this.builder = this.builder.orAllOf(args);
        return this;
    }

    public InheritableBooleanBuilder orNot(Predicate right) {
        this.builder = this.builder.orNot(right);
        return this;
    }

    public Class<? extends Boolean> getType() {
        return this.builder.getType();
    }

    public String toString() {
        return this.builder.toString();
    }

}