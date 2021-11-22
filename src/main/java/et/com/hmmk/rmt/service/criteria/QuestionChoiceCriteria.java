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
 * Criteria class for the {@link et.com.hmmk.rmt.domain.QuestionChoice} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.QuestionChoiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /question-choices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionChoiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter option;

    private LongFilter questionId;

    private Boolean distinct;

    public QuestionChoiceCriteria() {}

    public QuestionChoiceCriteria(QuestionChoiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.option = other.option == null ? null : other.option.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionChoiceCriteria copy() {
        return new QuestionChoiceCriteria(this);
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

    public StringFilter getOption() {
        return option;
    }

    public StringFilter option() {
        if (option == null) {
            option = new StringFilter();
        }
        return option;
    }

    public void setOption(StringFilter option) {
        this.option = option;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public LongFilter questionId() {
        if (questionId == null) {
            questionId = new LongFilter();
        }
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
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
        final QuestionChoiceCriteria that = (QuestionChoiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(option, that.option) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, option, questionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionChoiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (option != null ? "option=" + option + ", " : "") +
            (questionId != null ? "questionId=" + questionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
