package io.github.fukkitmc.fukkit.nms;

import java.util.Objects;

class Member {
    public final String owner, name, descriptor;

    Member(String owner, String name, String descriptor) {
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(owner, member.owner) &&
                Objects.equals(name, member.name) &&
                Objects.equals(descriptor, member.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, name, descriptor);
    }

    @Override
    public String toString() {
        return "Member{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}
