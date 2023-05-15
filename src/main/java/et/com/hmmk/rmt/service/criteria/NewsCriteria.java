package et.com.hmmk.rmt.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link et.com.hmmk.rmt.domain.News} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.NewsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /news?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NewsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter createdBy;

    private InstantFilter registeredTime;

    private InstantFilter updateTime;

    private Boolean distinct;

    public NewsCriteria() {}

    public NewsCriteria(NewsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.registeredTime = other.registeredTime == null ? null : other.registeredTime.copy();
        this.updateTime = other.updateTime == null ? null : other.updateTime.copy();
        this.distinct = other.distinct;
    }

    @Override
    public NewsCriteria copy() {
        return new NewsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getRegisteredTime() {
        return registeredTime;
    }

    public InstantFilter registeredTime() {
        if (registeredTime == null) {
            registeredTime = new InstantFilter();
        }
        return registeredTime;
    }

    public void setRegisteredTime(InstantFilter registeredTime) {
        this.registeredTime = registeredTime;
    }

    public InstantFilter getUpdateTime() {
        return updateTime;
    }

    public InstantFilter updateTime() {
        if (updateTime == null) {
            updateTime = new InstantFilter();
        }
        return updateTime;
    }

    public void setUpdateTime(InstantFilter updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NewsCriteria that = (NewsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(registeredTime, that.registeredTime) &&
            Objects.equals(updateTime, that.updateTime) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdBy, registeredTime, updateTime, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NewsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (registeredTime != null ? "registeredTime=" + registeredTime + ", " : "") +
            (updateTime != null ? "updateTime=" + updateTime + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
