package et.com.hmmk.rmt.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link et.com.hmmk.rmt.domain.Company} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter strategicObjective;

    private StringFilter futureFocusArea;

    private StringFilter currentFundingCycle;

    private LongFilter userId;

    private LongFilter typeOfOrganationId;

    private Boolean distinct;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.strategicObjective = other.strategicObjective == null ? null : other.strategicObjective.copy();
        this.futureFocusArea = other.futureFocusArea == null ? null : other.futureFocusArea.copy();
        this.currentFundingCycle = other.currentFundingCycle == null ? null : other.currentFundingCycle.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.typeOfOrganationId = other.typeOfOrganationId == null ? null : other.typeOfOrganationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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

    public StringFilter getStrategicObjective() {
        return strategicObjective;
    }

    public StringFilter strategicObjective() {
        if (strategicObjective == null) {
            strategicObjective = new StringFilter();
        }
        return strategicObjective;
    }

    public void setStrategicObjective(StringFilter strategicObjective) {
        this.strategicObjective = strategicObjective;
    }

    public StringFilter getFutureFocusArea() {
        return futureFocusArea;
    }

    public StringFilter futureFocusArea() {
        if (futureFocusArea == null) {
            futureFocusArea = new StringFilter();
        }
        return futureFocusArea;
    }

    public void setFutureFocusArea(StringFilter futureFocusArea) {
        this.futureFocusArea = futureFocusArea;
    }

    public StringFilter getCurrentFundingCycle() {
        return currentFundingCycle;
    }

    public StringFilter currentFundingCycle() {
        if (currentFundingCycle == null) {
            currentFundingCycle = new StringFilter();
        }
        return currentFundingCycle;
    }

    public void setCurrentFundingCycle(StringFilter currentFundingCycle) {
        this.currentFundingCycle = currentFundingCycle;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTypeOfOrganationId() {
        return typeOfOrganationId;
    }

    public LongFilter typeOfOrganationId() {
        if (typeOfOrganationId == null) {
            typeOfOrganationId = new LongFilter();
        }
        return typeOfOrganationId;
    }

    public void setTypeOfOrganationId(LongFilter typeOfOrganationId) {
        this.typeOfOrganationId = typeOfOrganationId;
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
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(strategicObjective, that.strategicObjective) &&
            Objects.equals(futureFocusArea, that.futureFocusArea) &&
            Objects.equals(currentFundingCycle, that.currentFundingCycle) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(typeOfOrganationId, that.typeOfOrganationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, strategicObjective, futureFocusArea, currentFundingCycle, userId, typeOfOrganationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (strategicObjective != null ? "strategicObjective=" + strategicObjective + ", " : "") +
            (futureFocusArea != null ? "futureFocusArea=" + futureFocusArea + ", " : "") +
            (currentFundingCycle != null ? "currentFundingCycle=" + currentFundingCycle + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (typeOfOrganationId != null ? "typeOfOrganationId=" + typeOfOrganationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
