package et.com.hmmk.rmt.service.criteria;

import et.com.hmmk.rmt.domain.enumeration.DataType;
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
 * Criteria class for the {@link et.com.hmmk.rmt.domain.Question} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DataType
     */
    public static class DataTypeFilter extends Filter<DataType> {

        public DataTypeFilter() {}

        public DataTypeFilter(DataTypeFilter filter) {
            super(filter);
        }

        @Override
        public DataTypeFilter copy() {
            return new DataTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter mandatory;

    private DataTypeFilter dataType;

    private LongFilter questionChoiceId;

    private LongFilter answerId;

    private LongFilter formId;

    private Boolean distinct;

    public QuestionCriteria() {}

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.mandatory = other.mandatory == null ? null : other.mandatory.copy();
        this.dataType = other.dataType == null ? null : other.dataType.copy();
        this.questionChoiceId = other.questionChoiceId == null ? null : other.questionChoiceId.copy();
        this.answerId = other.answerId == null ? null : other.answerId.copy();
        this.formId = other.formId == null ? null : other.formId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
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

    public BooleanFilter getMandatory() {
        return mandatory;
    }

    public BooleanFilter mandatory() {
        if (mandatory == null) {
            mandatory = new BooleanFilter();
        }
        return mandatory;
    }

    public void setMandatory(BooleanFilter mandatory) {
        this.mandatory = mandatory;
    }

    public DataTypeFilter getDataType() {
        return dataType;
    }

    public DataTypeFilter dataType() {
        if (dataType == null) {
            dataType = new DataTypeFilter();
        }
        return dataType;
    }

    public void setDataType(DataTypeFilter dataType) {
        this.dataType = dataType;
    }

    public LongFilter getQuestionChoiceId() {
        return questionChoiceId;
    }

    public LongFilter questionChoiceId() {
        if (questionChoiceId == null) {
            questionChoiceId = new LongFilter();
        }
        return questionChoiceId;
    }

    public void setQuestionChoiceId(LongFilter questionChoiceId) {
        this.questionChoiceId = questionChoiceId;
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

    public LongFilter getFormId() {
        return formId;
    }

    public LongFilter formId() {
        if (formId == null) {
            formId = new LongFilter();
        }
        return formId;
    }

    public void setFormId(LongFilter formId) {
        this.formId = formId;
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
        final QuestionCriteria that = (QuestionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mandatory, that.mandatory) &&
            Objects.equals(dataType, that.dataType) &&
            Objects.equals(questionChoiceId, that.questionChoiceId) &&
            Objects.equals(answerId, that.answerId) &&
            Objects.equals(formId, that.formId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mandatory, dataType, questionChoiceId, answerId, formId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (mandatory != null ? "mandatory=" + mandatory + ", " : "") +
            (dataType != null ? "dataType=" + dataType + ", " : "") +
            (questionChoiceId != null ? "questionChoiceId=" + questionChoiceId + ", " : "") +
            (answerId != null ? "answerId=" + answerId + ", " : "") +
            (formId != null ? "formId=" + formId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
