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
 * Criteria class for the {@link et.com.hmmk.rmt.domain.FormProgresss} entity. This class is used
 * in {@link et.com.hmmk.rmt.web.rest.FormProgresssResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /form-progressses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FormProgresssCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter submited;

    private InstantFilter startedOn;

    private InstantFilter submitedOn;

    private InstantFilter sentedOn;

    private LongFilter answerId;

    private LongFilter userId;

    private LongFilter formId;

    private LongFilter projectId;

    private Boolean distinct;

    public FormProgresssCriteria() {}

    public FormProgresssCriteria(FormProgresssCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.submited = other.submited == null ? null : other.submited.copy();
        this.startedOn = other.startedOn == null ? null : other.startedOn.copy();
        this.submitedOn = other.submitedOn == null ? null : other.submitedOn.copy();
        this.sentedOn = other.sentedOn == null ? null : other.sentedOn.copy();
        this.answerId = other.answerId == null ? null : other.answerId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.formId = other.formId == null ? null : other.formId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FormProgresssCriteria copy() {
        return new FormProgresssCriteria(this);
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

    public BooleanFilter getSubmited() {
        return submited;
    }

    public BooleanFilter submited() {
        if (submited == null) {
            submited = new BooleanFilter();
        }
        return submited;
    }

    public void setSubmited(BooleanFilter submited) {
        this.submited = submited;
    }

    public InstantFilter getStartedOn() {
        return startedOn;
    }

    public InstantFilter startedOn() {
        if (startedOn == null) {
            startedOn = new InstantFilter();
        }
        return startedOn;
    }

    public void setStartedOn(InstantFilter startedOn) {
        this.startedOn = startedOn;
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

    public InstantFilter getSentedOn() {
        return sentedOn;
    }

    public InstantFilter sentedOn() {
        if (sentedOn == null) {
            sentedOn = new InstantFilter();
        }
        return sentedOn;
    }

    public void setSentedOn(InstantFilter sentedOn) {
        this.sentedOn = sentedOn;
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

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
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
        final FormProgresssCriteria that = (FormProgresssCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(submited, that.submited) &&
            Objects.equals(startedOn, that.startedOn) &&
            Objects.equals(submitedOn, that.submitedOn) &&
            Objects.equals(sentedOn, that.sentedOn) &&
            Objects.equals(answerId, that.answerId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(formId, that.formId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, submited, startedOn, submitedOn, sentedOn, answerId, userId, formId, projectId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormProgresssCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (submited != null ? "submited=" + submited + ", " : "") +
            (startedOn != null ? "startedOn=" + startedOn + ", " : "") +
            (submitedOn != null ? "submitedOn=" + submitedOn + ", " : "") +
            (sentedOn != null ? "sentedOn=" + sentedOn + ", " : "") +
            (answerId != null ? "answerId=" + answerId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (formId != null ? "formId=" + formId + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
