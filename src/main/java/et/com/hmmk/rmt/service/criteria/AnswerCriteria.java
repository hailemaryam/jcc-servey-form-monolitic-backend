package et.com.hmmk.rmt.service.criteria;

import et.com.hmmk.rmt.domain.enumeration.DataType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.DurationFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link et.com.hmmk.rmt.domain.Answer} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.AnswerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /answers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnswerCriteria implements Serializable, Criteria {

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

    private DoubleFilter number;

    private BooleanFilter booleanAnswer;

    private StringFilter shortAnswer;

    private StringFilter multipleChoice;

    private StringFilter dropDown;

    private StringFilter fileName;

    private InstantFilter date;

    private DurationFilter time;

    private InstantFilter submitedOn;

    private DataTypeFilter dataType;

    private LongFilter multipleChoiceAnsewerId;

    private LongFilter userId;

    private LongFilter questionId;

    private LongFilter formProgresssId;

    private Boolean distinct;

    public AnswerCriteria() {}

    public AnswerCriteria(AnswerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.booleanAnswer = other.booleanAnswer == null ? null : other.booleanAnswer.copy();
        this.shortAnswer = other.shortAnswer == null ? null : other.shortAnswer.copy();
        this.multipleChoice = other.multipleChoice == null ? null : other.multipleChoice.copy();
        this.dropDown = other.dropDown == null ? null : other.dropDown.copy();
        this.fileName = other.fileName == null ? null : other.fileName.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.submitedOn = other.submitedOn == null ? null : other.submitedOn.copy();
        this.dataType = other.dataType == null ? null : other.dataType.copy();
        this.multipleChoiceAnsewerId = other.multipleChoiceAnsewerId == null ? null : other.multipleChoiceAnsewerId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.formProgresssId = other.formProgresssId == null ? null : other.formProgresssId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AnswerCriteria copy() {
        return new AnswerCriteria(this);
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

    public DoubleFilter getNumber() {
        return number;
    }

    public DoubleFilter number() {
        if (number == null) {
            number = new DoubleFilter();
        }
        return number;
    }

    public void setNumber(DoubleFilter number) {
        this.number = number;
    }

    public BooleanFilter getBooleanAnswer() {
        return booleanAnswer;
    }

    public BooleanFilter booleanAnswer() {
        if (booleanAnswer == null) {
            booleanAnswer = new BooleanFilter();
        }
        return booleanAnswer;
    }

    public void setBooleanAnswer(BooleanFilter booleanAnswer) {
        this.booleanAnswer = booleanAnswer;
    }

    public StringFilter getShortAnswer() {
        return shortAnswer;
    }

    public StringFilter shortAnswer() {
        if (shortAnswer == null) {
            shortAnswer = new StringFilter();
        }
        return shortAnswer;
    }

    public void setShortAnswer(StringFilter shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public StringFilter getMultipleChoice() {
        return multipleChoice;
    }

    public StringFilter multipleChoice() {
        if (multipleChoice == null) {
            multipleChoice = new StringFilter();
        }
        return multipleChoice;
    }

    public void setMultipleChoice(StringFilter multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public StringFilter getDropDown() {
        return dropDown;
    }

    public StringFilter dropDown() {
        if (dropDown == null) {
            dropDown = new StringFilter();
        }
        return dropDown;
    }

    public void setDropDown(StringFilter dropDown) {
        this.dropDown = dropDown;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public StringFilter fileName() {
        if (fileName == null) {
            fileName = new StringFilter();
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public DurationFilter getTime() {
        return time;
    }

    public DurationFilter time() {
        if (time == null) {
            time = new DurationFilter();
        }
        return time;
    }

    public void setTime(DurationFilter time) {
        this.time = time;
    }

    public InstantFilter getSubmitedOn() {
        return submitedOn;
    }

    public InstantFilter submitedOn() {
        if (submitedOn == null) {
            submitedOn = new InstantFilter();
        }
        return submitedOn;
    }

    public void setSubmitedOn(InstantFilter submitedOn) {
        this.submitedOn = submitedOn;
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

    public LongFilter getMultipleChoiceAnsewerId() {
        return multipleChoiceAnsewerId;
    }

    public LongFilter multipleChoiceAnsewerId() {
        if (multipleChoiceAnsewerId == null) {
            multipleChoiceAnsewerId = new LongFilter();
        }
        return multipleChoiceAnsewerId;
    }

    public void setMultipleChoiceAnsewerId(LongFilter multipleChoiceAnsewerId) {
        this.multipleChoiceAnsewerId = multipleChoiceAnsewerId;
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

    public LongFilter getFormProgresssId() {
        return formProgresssId;
    }

    public LongFilter formProgresssId() {
        if (formProgresssId == null) {
            formProgresssId = new LongFilter();
        }
        return formProgresssId;
    }

    public void setFormProgresssId(LongFilter formProgresssId) {
        this.formProgresssId = formProgresssId;
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
        final AnswerCriteria that = (AnswerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(number, that.number) &&
            Objects.equals(booleanAnswer, that.booleanAnswer) &&
            Objects.equals(shortAnswer, that.shortAnswer) &&
            Objects.equals(multipleChoice, that.multipleChoice) &&
            Objects.equals(dropDown, that.dropDown) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(date, that.date) &&
            Objects.equals(time, that.time) &&
            Objects.equals(submitedOn, that.submitedOn) &&
            Objects.equals(dataType, that.dataType) &&
            Objects.equals(multipleChoiceAnsewerId, that.multipleChoiceAnsewerId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(formProgresssId, that.formProgresssId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            number,
            booleanAnswer,
            shortAnswer,
            multipleChoice,
            dropDown,
            fileName,
            date,
            time,
            submitedOn,
            dataType,
            multipleChoiceAnsewerId,
            userId,
            questionId,
            formProgresssId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnswerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (booleanAnswer != null ? "booleanAnswer=" + booleanAnswer + ", " : "") +
            (shortAnswer != null ? "shortAnswer=" + shortAnswer + ", " : "") +
            (multipleChoice != null ? "multipleChoice=" + multipleChoice + ", " : "") +
            (dropDown != null ? "dropDown=" + dropDown + ", " : "") +
            (fileName != null ? "fileName=" + fileName + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (time != null ? "time=" + time + ", " : "") +
            (submitedOn != null ? "submitedOn=" + submitedOn + ", " : "") +
            (dataType != null ? "dataType=" + dataType + ", " : "") +
            (multipleChoiceAnsewerId != null ? "multipleChoiceAnsewerId=" + multipleChoiceAnsewerId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (questionId != null ? "questionId=" + questionId + ", " : "") +
            (formProgresssId != null ? "formProgresssId=" + formProgresssId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
