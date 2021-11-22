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
 * Criteria class for the {@link et.com.hmmk.rmt.domain.MultipleChoiceAnsewer} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.MultipleChoiceAnsewerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /multiple-choice-ansewers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MultipleChoiceAnsewerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter choice;

    private LongFilter answerId;

    private Boolean distinct;

    public MultipleChoiceAnsewerCriteria() {}

    public MultipleChoiceAnsewerCriteria(MultipleChoiceAnsewerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.choice = other.choice == null ? null : other.choice.copy();
        this.answerId = other.answerId == null ? null : other.answerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MultipleChoiceAnsewerCriteria copy() {
        return new MultipleChoiceAnsewerCriteria(this);
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

    public StringFilter getChoice() {
        return choice;
    }

    public StringFilter choice() {
        if (choice == null) {
            choice = new StringFilter();
        }
        return choice;
    }

    public void setChoice(StringFilter choice) {
        this.choice = choice;
    }

    public LongFilter getAnswerId() {
        return answerId;
    }

    public LongFilter answerId() {
        if (answerId == null) {
            answerId = new LongFilter();
        }
        return answerId;
    }

    public void setAnswerId(LongFilter answerId) {
        this.answerId = answerId;
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
        final MultipleChoiceAnsewerCriteria that = (MultipleChoiceAnsewerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(choice, that.choice) &&
            Objects.equals(answerId, that.answerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, choice, answerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MultipleChoiceAnsewerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (choice != null ? "choice=" + choice + ", " : "") +
            (answerId != null ? "answerId=" + answerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
